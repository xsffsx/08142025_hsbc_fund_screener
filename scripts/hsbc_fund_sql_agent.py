#!/usr/bin/env python3
"""
HSBC Fund NL2SQL MVP - 单 Agent（连接 LM Studio 本地 LLM）
- 数据库: postgresql://hsbc_user:hsbc_pass@localhost:5433/hsbc_fund
- LLM: LM Studio (OpenAI 兼容 API)，模型 qwen/qwen3-30b-a3b-2507
"""

import os
import logging
import time
import re
from typing import Dict, Any, Optional, List
from logging.handlers import RotatingFileHandler

from langchain_community.agent_toolkits.sql.toolkit import SQLDatabaseToolkit
from langchain_community.utilities.sql_database import SQLDatabase
from langchain_community.agent_toolkits.sql.base import create_sql_agent
from langchain_openai import ChatOpenAI

# 将 SQL Agent 内部日志输出到 logs 目录
LOG_DIR = os.path.join(os.path.dirname(__file__), "logs")
os.makedirs(LOG_DIR, exist_ok=True)
LOG_FILE = os.path.join(LOG_DIR, "hsbc_fund_nl2sql.log")
_formatter = logging.Formatter("%(asctime)s | %(levelname)s | %(name)s | %(message)s")
_core_logger = logging.getLogger("nl2sql.core")
_core_logger.setLevel(logging.DEBUG)
if not _core_logger.handlers:
    _fh = RotatingFileHandler(LOG_FILE, maxBytes=5*1024*1024, backupCount=3, encoding="utf-8")
    _fh.setFormatter(_formatter)
    _core_logger.addHandler(_fh)
_core_logger.propagate = False

DB_URI = os.getenv("HSBC_DB_URI", "postgresql+psycopg://hsbc_user:hsbc_pass@localhost:5433/hsbc_fund")
# LM Studio 默认 OpenAI 兼容服务: http://localhost:1234/v1
LMSTUDIO_BASE_URL = os.getenv("LMSTUDIO_BASE_URL", "http://localhost:1234/v1")
LMSTUDIO_MODEL = os.getenv("LMSTUDIO_MODEL", "qwen/qwen3-30b-a3b-2507")
LMSTUDIO_API_KEY = os.getenv("OPENAI_API_KEY", "lm-studio")  # LM Studio 可用任意占位 key


def compress_schema_text(text: str) -> str:
    """
    压缩数据库 Schema 文本，移除不必要的空格和换行符
    """
    if not text:
        return text

    # 移除多余的空白字符
    text = re.sub(r'\s+', ' ', text)

    # 移除 CREATE TABLE 语句中的多余空格
    text = re.sub(r'\s*\(\s*', '(', text)
    text = re.sub(r'\s*\)\s*', ')', text)
    text = re.sub(r'\s*,\s*', ',', text)

    # 压缩示例数据部分
    # 将长的 JSON 字符串截断
    text = re.sub(r'\{[^}]{200,}\}', '{...}', text)
    text = re.sub(r'\[[^\]]{200,}\]', '[...]', text)

    # 移除过长的字符串值
    text = re.sub(r"'[^']{100,}'", "'...'", text)
    text = re.sub(r'"[^"]{100,}"', '"..."', text)


# --- Utilities: Sanitize SQL input from LLM (strip markdown fences, labels) ---
_SQL_FENCE_BLOCK = re.compile(r"```(?:sql)?\s*([\s\S]*?)\s*```", re.IGNORECASE)
_SQL_FENCE_START = re.compile(r"^```(?:sql)?\s*", re.IGNORECASE)
_SQL_FENCE_END = re.compile(r"```\s*$", re.IGNORECASE)


def sanitize_sql_input(s: str) -> str:
    """Remove markdown code fences/backticks and common prefixes from SQL text.
    Examples handled:
    - ```sql\nSELECT ...\n```
    - ```\nSELECT ...\n```
    - leading/trailing backticks, labels like 'SQL:'
    """
    if s is None:
        return s
    s = str(s).strip()
    m = _SQL_FENCE_BLOCK.search(s)
    if m:
        s = m.group(1).strip()
    else:
        s = _SQL_FENCE_START.sub("", s)
        s = _SQL_FENCE_END.sub("", s)
        s = s.replace("```", " ")
    # Remove common textual prefixes
    s = re.sub(r"^sql\s*[:：]", "", s, flags=re.IGNORECASE).strip()
    # Collapse extra whitespace
    s = re.sub(r"\s+", " ", s).strip()
    return s


def compress_table_info(db: SQLDatabase, table_names: List[str], logger=None) -> str:
    """
    获取压缩后的表信息
    """
    try:
        # 获取原始表信息
        table_info = db.get_table_info(table_names)

        # 压缩文本
        compressed_info = compress_schema_text(table_info)

        if logger:
            logger.debug(f"原始 Schema 长度: {len(table_info)} 字符")
            logger.debug(f"压缩后 Schema 长度: {len(compressed_info)} 字符")
            logger.debug(f"压缩率: {(1 - len(compressed_info)/len(table_info))*100:.1f}%")

        return compressed_info
    except Exception as e:
        if logger:
            logger.error(f"压缩表信息失败: {e}")
        return db.get_table_info(table_names)


PROMPT = """你是 HSBC 基金 SQL 助手，使用单表 hsbc_fund_unified 回答问题。

【标量列-高效筛选】risk_level, currency, allow_buy, nav, family_name, hsbc_category_name
【JSONB列-深度查询】risk_json, top10_holdings, holding_allocation, summary_cumulative, chart_timeseries

【JSONB查询范式】
- 风险：FROM hsbc_fund_unified f, LATERAL jsonb_array_elements(f.risk_json) r
  取：(r->'yearRisk'->>'totalReturn')::NUMERIC, (r->'yearRisk'->>'year')::INTEGER
- 持仓：LATERAL jsonb_array_elements(f.top10_holdings->'items') item（market/securityName/weighting）
- 配置：LATERAL jsonb_array_elements(f.holding_allocation) a, jsonb_array_elements(a->'breakdowns') b
- 收益：LATERAL jsonb_array_elements(f.summary_cumulative->'items') it（period/totalReturn）

【优化原则】先用标量列 WHERE 缩小结果，再展开 JSONB；数值比较使用 ::NUMERIC；默认最多 {top_k} 行。
只执行 SELECT 查询，禁止 DML。
"""

_agent = None  # 全局缓存，避免重复初始化
_db: Optional[SQLDatabase] = None


def build_agent():
    global _agent, _db
    if _agent is not None:
        _core_logger.debug("返回已缓存的 Agent")
        return _agent

    _core_logger.info("开始构建 Agent...")

    # 数据库
    _core_logger.info(f"连接数据库: {DB_URI}")
    try:
        _db = SQLDatabase.from_uri(DB_URI)
        _core_logger.info("数据库连接成功")

        # 测试数据库连接
        table_names = _db.get_usable_table_names()
        _core_logger.info(f"可用表: {table_names}")
    except Exception as e:
        _core_logger.error(f"数据库连接失败: {e}")
        raise

    # 连接 LM Studio (OpenAI 兼容)
    _core_logger.info(f"初始化 LLM: {LMSTUDIO_MODEL} @ {LMSTUDIO_BASE_URL}")
    try:
        llm = ChatOpenAI(
            base_url=LMSTUDIO_BASE_URL,  # e.g., http://localhost:1234/v1
            api_key=LMSTUDIO_API_KEY,
            model=LMSTUDIO_MODEL,
            temperature=0,
            max_tokens=256,  # 减少生成长度，提升速度
        )
        _core_logger.info("LLM 初始化成功")
    except Exception as e:
        _core_logger.error(f"LLM 初始化失败: {e}")
        raise

    try:
        # 创建自定义的 SQLDatabaseToolkit，压缩 schema 信息
        toolkit = SQLDatabaseToolkit(
            db=_db,
            llm=llm,
        )

        # 手动覆盖 get_table_info 方法来压缩 schema
        original_get_table_info = _db.get_table_info
        # 保存原始方法的引用，用于调试
        _db._original_get_table_info = original_get_table_info
        def compressed_get_table_info(table_names=None):
            if table_names is None:
                table_names = _db.get_usable_table_names()
            # 使用原始方法获取表信息，然后压缩
            original_info = original_get_table_info(table_names)
            compressed_info = compress_schema_text(original_info)

            # 详细的压缩对比日志
            if original_info and compressed_info:
                original_chars = len(original_info)
                compressed_chars = len(compressed_info)
                # 简单的 token 估算：大约 4 字符 = 1 token
                original_tokens = original_chars // 4
                compressed_tokens = compressed_chars // 4
                compression_ratio = (1 - compressed_chars/original_chars) * 100

                _core_logger.debug("=" * 60)
                _core_logger.debug("SCHEMA 压缩对比:")
                _core_logger.debug(f"原始 Schema 字符数: {original_chars}")
                _core_logger.debug(f"原始 Schema 估算 tokens: {original_tokens}")
                _core_logger.debug(f"压缩后 Schema 字符数: {compressed_chars}")
                _core_logger.debug(f"压缩后 Schema 估算 tokens: {compressed_tokens}")
                _core_logger.debug(f"压缩率: {compression_ratio:.1f}%")
                _core_logger.debug(f"节省 tokens: {original_tokens - compressed_tokens}")
                _core_logger.debug("=" * 60)

                # 输出原始 Schema 的前500字符
                _core_logger.debug("原始 Schema 内容（前500字符）:")
                _core_logger.debug(original_info[:500] + "..." if len(original_info) > 500 else original_info)
                _core_logger.debug("-" * 40)

                # 输出压缩后的完整 Schema
                _core_logger.debug("压缩后 Schema 完整内容:")
                _core_logger.debug(compressed_info)
                _core_logger.debug("=" * 60)

            return compressed_info

        # 临时替换方法
        _db.get_table_info = compressed_get_table_info

        _core_logger.info("SQL Toolkit 创建成功（已启用 Schema 压缩）")
    except Exception as e:
        _core_logger.error(f"SQL Toolkit 创建失败: {e}")
        raise

    try:
        _core_logger.info("创建 SQL Agent，参数:")
        _core_logger.info(f"  agent_type: zero-shot-react-description")
        _core_logger.info(f"  max_iterations: 16")
        _core_logger.info(f"  top_k: 5")
        _core_logger.info(f"  prompt 长度: {len(PROMPT)} 字符")

        # 输出数据库 schema 信息
        _core_logger.debug("=" * 80)
        _core_logger.debug("数据库 Schema 信息:")
        try:
            # 获取表列表
            tables = _db.get_usable_table_names()
            _core_logger.debug(f"可用表: {tables}")

            # 获取每个表的 schema（压缩后）
            for table in tables:
                # 使用原始方法获取 schema，然后压缩
                original_schema = original_get_table_info([table])
                compressed_schema = compress_schema_text(original_schema)

                _core_logger.debug(f"表 {table} 的原始 Schema 长度: {len(original_schema)} 字符")
                _core_logger.debug(f"表 {table} 的压缩 Schema 长度: {len(compressed_schema)} 字符")
                _core_logger.debug(f"压缩率: {(1 - len(compressed_schema)/len(original_schema))*100:.1f}%")
                _core_logger.debug(f"表 {table} 的压缩 Schema:")
                _core_logger.debug(compressed_schema)
                _core_logger.debug("-" * 40)
        except Exception as e:
            _core_logger.error(f"获取数据库 schema 失败: {e}")
        _core_logger.debug("=" * 80)

        _agent = create_sql_agent(
            llm=llm,
            toolkit=toolkit,
            agent_type="zero-shot-react-description",  # 使用兼容的 agent 类型
            verbose=True,
            max_iterations=16,  # 提高迭代上限，增强复杂查询稳定性
            top_k=5,  # 减少返回行数
            prefix=PROMPT,
            agent_executor_kwargs={
                "return_intermediate_steps": True,
                "handle_parsing_errors": True,
            },
        )
        _core_logger.info("SQL Agent 创建成功")

        # 输出 Agent 的工具信息
        tools = toolkit.get_tools()
        _core_logger.info(f"可用工具数量: {len(tools)}")
        for i, tool in enumerate(tools):
            _core_logger.info(f"  工具 {i+1}: {tool.name} - {tool.description[:100]}...")

        return _agent
    except Exception as e:
        _core_logger.error(f"SQL Agent 创建失败: {e}")
        raise


def extract_sql_from_steps(steps: List) -> List[str]:
    _core_logger.debug(f"extract_sql_from_steps 输入: {len(steps)} 个步骤")
    sqls: List[str] = []
    for i, step in enumerate(steps or []):
        _core_logger.debug(f"处理步骤 {i+1}: {type(step)}")
        if len(step) >= 1:
            action = step[0]
            _core_logger.debug(f"  动作: {action}")
            _core_logger.debug(f"  动作类型: {type(action)}")
            try:
                tool_name = getattr(action, "tool", None)
                _core_logger.debug(f"  工具名称: {tool_name}")
                if tool_name == "sql_db_query":
                    tool_input = getattr(action, "tool_input", None)
                    _core_logger.debug(f"  tool_input: {tool_input}")
                    _core_logger.debug(f"  tool_input 类型: {type(tool_input)}")

                    # 处理不同类型的 tool_input
                    sql_query = None
                    if isinstance(tool_input, str):
                        # 直接是 SQL 字符串
                        sql_query = tool_input
                        _core_logger.debug(f"  直接 SQL 字符串: {sql_query}")
                    elif tool_input and hasattr(tool_input, 'get'):
                        # 是字典，尝试获取 query 字段
                        sql_query = tool_input.get("query")
                        _core_logger.debug(f"  从字典获取查询内容: {sql_query}")
                    else:
                        _core_logger.debug(f"  tool_input 类型不支持: {type(tool_input)}")

                    if sql_query:
                        sanitized_sql = sanitize_sql_input(str(sql_query))
                        _core_logger.debug(f"  清理后的 SQL: {sanitized_sql}")
                        sqls.append(sanitized_sql)
                    else:
                        _core_logger.debug(f"  未找到有效的 SQL 查询")
                else:
                    _core_logger.debug(f"  非 SQL 查询工具: {tool_name}")
            except Exception as e:
                _core_logger.debug(f"  处理步骤异常: {e}")
                continue
    _core_logger.debug(f"extract_sql_from_steps 返回: {len(sqls)} 条 SQL")
    return sqls


def nl2sql_query(natural_language_query: str) -> Dict[str, Any]:
    _core_logger.info(f"执行 NL2SQL 查询: {natural_language_query}")
    t0 = time.perf_counter()

    try:
        _core_logger.debug("获取 Agent...")
        agent = build_agent()

        # 输出完整的 prompt 到日志
        _core_logger.info("=" * 80)
        _core_logger.info("PROMPT 内容:")
        _core_logger.info(PROMPT)
        _core_logger.info("=" * 80)

        _core_logger.info("开始调用 Agent.invoke...")
        _core_logger.info(f"输入查询: {natural_language_query}")

        # 尝试预估 prompt 的总长度
        try:
            # 尝试获取 Agent 的 prompt 模板信息
            _core_logger.debug("=" * 80)
            _core_logger.debug("Agent 结构信息:")
            _core_logger.debug(f"Agent 类型: {type(agent)}")
            _core_logger.debug(f"Agent 属性: {dir(agent)}")

            # 尝试获取工具描述信息
            if hasattr(agent, 'tools'):
                tools_info = []
                for tool in agent.tools:
                    tools_info.append(f"{tool.name}: {tool.description[:100]}...")
                _core_logger.debug(f"工具描述总长度: {sum(len(desc) for desc in tools_info)} 字符")

            _core_logger.debug("=" * 80)
        except Exception as e:
            _core_logger.warning(f"无法预览 Agent 信息: {e}")

        # 详细的 Prompt 分析
        _core_logger.debug("=" * 80)
        _core_logger.debug("PROMPT 完整分析:")

        # 1. 基础 Prompt
        base_prompt_chars = len(PROMPT)
        base_prompt_tokens = base_prompt_chars // 4
        _core_logger.debug(f"1. 基础 Prompt 字符数: {base_prompt_chars}")
        _core_logger.debug(f"1. 基础 Prompt 估算 tokens: {base_prompt_tokens}")

        # 2. 工具描述
        tools_desc_chars = 0
        if hasattr(agent, 'tools'):
            tools_desc = []
            for tool in agent.tools:
                tools_desc.append(f"- {tool.name}: {tool.description}")
            tools_text = "\n".join(tools_desc)
            tools_desc_chars = len(tools_text)
        tools_desc_tokens = tools_desc_chars // 4
        _core_logger.debug(f"2. 工具描述字符数: {tools_desc_chars}")
        _core_logger.debug(f"2. 工具描述估算 tokens: {tools_desc_tokens}")

        # 3. 数据库 Schema
        schema_chars = 0
        schema_tokens = 0
        if _db:
            try:
                current_schema = _db.get_table_info()
                if current_schema:
                    schema_chars = len(current_schema)
                    schema_tokens = schema_chars // 4
            except:
                pass
        _core_logger.debug(f"3. 数据库 Schema 字符数: {schema_chars}")
        _core_logger.debug(f"3. 数据库 Schema 估算 tokens: {schema_tokens}")

        # 4. 用户查询
        query_chars = len(natural_language_query)
        query_tokens = query_chars // 4
        _core_logger.debug(f"4. 用户查询字符数: {query_chars}")
        _core_logger.debug(f"4. 用户查询估算 tokens: {query_tokens}")

        # 总计
        total_chars = base_prompt_chars + tools_desc_chars + schema_chars + query_chars
        total_tokens = total_chars // 4
        _core_logger.debug(f"总计字符数: {total_chars}")
        _core_logger.debug(f"总计估算 tokens: {total_tokens}")
        _core_logger.debug("=" * 80)

        t1 = time.perf_counter()
        _core_logger.debug(f"开始执行 agent.invoke，输入: {natural_language_query}")
        result = agent.invoke({"input": natural_language_query})
        invoke_time = (time.perf_counter() - t1) * 1000

        _core_logger.info(f"Agent.invoke 完成，耗时: {invoke_time:.1f}ms")
        _core_logger.debug(f"Agent.invoke 返回结果类型: {type(result)}")
        _core_logger.debug(f"Agent.invoke 返回结果键: {list(result.keys()) if isinstance(result, dict) else 'Not a dict'}")
        if isinstance(result, dict):
            _core_logger.debug(f"result['output'] 类型: {type(result.get('output'))}")
            _core_logger.debug(f"result['output'] 内容: {result.get('output')}")
            _core_logger.debug(f"result['intermediate_steps'] 长度: {len(result.get('intermediate_steps', []))}")

        # 输出中间步骤详情
        intermediate_steps = result.get("intermediate_steps", [])
        _core_logger.info(f"中间步骤数量: {len(intermediate_steps)}")

        for i, step in enumerate(intermediate_steps):
            _core_logger.info(f"步骤 {i+1}:")
            _core_logger.debug(f"  步骤类型: {type(step)}, 步骤长度: {len(step) if hasattr(step, '__len__') else 'N/A'}")
            if len(step) >= 2:
                action, observation = step[0], step[1]
                _core_logger.info(f"  动作: {action}")
                _core_logger.info(f"  观察: {str(observation)[:500]}...")  # 限制长度
                _core_logger.debug(f"  动作类型: {type(action)}")
                _core_logger.debug(f"  观察类型: {type(observation)}")
                if hasattr(action, 'tool'):
                    _core_logger.debug(f"  使用工具: {action.tool}")
                if hasattr(action, 'tool_input'):
                    _core_logger.debug(f"  工具输入: {action.tool_input}")
            else:
                _core_logger.warning(f"  步骤格式异常: {step}")

        _core_logger.debug("提取 SQL 语句...")
        sqls = extract_sql_from_steps(intermediate_steps)
        _core_logger.debug(f"extract_sql_from_steps 返回: {sqls}")

        for i, sql in enumerate(sqls):
            _core_logger.info(f"生成的 SQL {i+1}: {sql}")

        total_time = (time.perf_counter() - t0) * 1000
        _core_logger.info(f"查询完成，生成 {len(sqls)} 条 SQL，总耗时: {total_time:.1f}ms")

        # 输出最终结果
        output = result.get("output")
        _core_logger.info(f"最终输出: {str(output)[:500]}...")
        _core_logger.debug(f"最终输出完整内容: {output}")

        # 检查输出是否为空或无效
        if not output or output.strip() == "":
            _core_logger.warning("最终输出为空！")
        if len(sqls) == 0:
            _core_logger.warning("没有提取到任何 SQL 语句！")

        return {
            "success": True,
            "query": natural_language_query,
            "output": output,
            "sql": sqls,
        }
    except Exception as e:
        total_time = (time.perf_counter() - t0) * 1000
        _core_logger.exception(f"NL2SQL 查询失败，耗时: {total_time:.1f}ms | err={e}")
        return {
            "success": False,
            "query": natural_language_query,
            "error": str(e),
            "sql": [],
        }


if __name__ == "__main__":
    print("=== NL2SQL MVP (LM Studio) ===")
    print(f"DB: {DB_URI}")
    print(f"LLM: {LMSTUDIO_MODEL} @ {LMSTUDIO_BASE_URL}")
    agent = build_agent()
    demo = "找出风险等级为5的美元基金，按NAV排序，返回前5条"
    print("Q:", demo)
    resp = agent.invoke({"input": demo})
    print("Answer:", resp.get("output"))
    print("SQL:", extract_sql_from_steps(resp.get("intermediate_steps", [])))

