#!/usr/bin/env python3
"""
HSBC Fund NL2SQL 演示脚本
基于 LangChain SQL Agent 实现自然语言到 SQL 的转换
"""

import os
import sys
import time
import json
from typing import Dict, Any, Optional
import re

# 检查依赖
try:
    from langchain_community.agent_toolkits.sql.toolkit import SQLDatabaseToolkit
    from langchain_community.utilities.sql_database import SQLDatabase
    from langchain_community.agent_toolkits.sql.base import create_sql_agent
    from langchain_openai import ChatOpenAI
    from langchain_core.prompts import PromptTemplate
except ImportError as e:
    print(f"❌ 缺少依赖: {e}")
    print("请安装: pip install langchain langchain-community langchain-openai")
    sys.exit(1)

# 数据库配置
DB_CONFIG = {
    'host': 'localhost',
    'port': 5433,
    'database': 'hsbc_fund',
    'user': 'hsbc_user',
    'password': 'hsbc_pass'
}

# HSBC 基金专用提示词
HSBC_FUND_PREFIX = """你是一个专门处理 HSBC 基金数据的 SQL 查询助手。
数据库包含 1,407 个基金产品，存储在 hsbc_fund_unified 表中。

表结构特点：
1. 标量列：用于高频简单查询（风险等级、币种、NAV等）
   - product_code (主键), name, family_name, currency
   - risk_level (1-5), nav, allow_buy, allow_sell
   - hsbc_category_name, assets_under_mgmt

2. JSONB 列：用于复杂结构查询（风险指标、持仓分配、历史数据等）
   - risk_json: 1/3/5/10年风险数据数组，使用 jsonb_array_elements() 展开
   - top10_holdings: 前十大持仓，通过 ->'items' 访问持仓数组
   - holding_allocation: 资产配置，按 methods 字段筛选分配类型
   - chart_timeseries: 历史价格数组，包含 date 和 navPrice
   - summary_cumulative: 累计收益率数据

JSONB 查询示例：
- 查询1年收益率: (risk_item->'yearRisk'->>'totalReturn')::NUMERIC WHERE (risk_item->'yearRisk'->>'year')::INTEGER = 1
- 查询持仓地区: item->>'market' FROM jsonb_array_elements(top10_holdings->'items') AS item
- 查询资产配置: breakdown->>'name', (breakdown->>'weighting')::NUMERIC FROM jsonb_array_elements(holding_allocation) AS allocation, jsonb_array_elements(allocation->'breakdowns') AS breakdown WHERE allocation->>'methods' = 'assetAllocations'

查询限制：
- 默认返回最多 {top_k} 条结果
- 仅执行 SELECT 查询，禁止 DML 操作
- 优先使用标量列进行筛选，再使用 JSONB 进行复杂查询
- 对于数值比较，记得使用 ::NUMERIC 进行类型转换
"""

class HSBCFundNL2SQL:
    """HSBC 基金 NL2SQL 服务"""
    
    def __init__(self, db_uri: str, openai_api_key: str, model: str = "gpt-3.5-turbo"):
        """初始化 NL2SQL 服务"""
        self.db_uri = db_uri
        self.model = model
        
        # 设置 OpenAI API Key
        os.environ["OPENAI_API_KEY"] = openai_api_key
        
        try:
            # 初始化数据库连接
            self.db = SQLDatabase.from_uri(db_uri)
            print(f"✅ 数据库连接成功")
            
            # 初始化 LLM
            self.llm = ChatOpenAI(
                model=model,
                temperature=0,
                max_tokens=2000
            )
            print(f"✅ LLM 初始化成功: {model}")
            
            # 创建工具包
            self.toolkit = SQLDatabaseToolkit(db=self.db, llm=self.llm)
            
            # 创建 SQL Agent
            self.agent = create_sql_agent(
                llm=self.llm,
                toolkit=self.toolkit,
                agent_type="tool-calling",
                prefix=HSBC_FUND_PREFIX,
                verbose=True,
                max_iterations=15,
                top_k=10
            )
            print(f"✅ SQL Agent 创建成功")
            
        except Exception as e:
            print(f"❌ 初始化失败: {e}")
            raise
    
    def query(self, natural_language_query: str) -> Dict[str, Any]:
        """执行自然语言查询"""
        print(f"\n🔍 执行查询: {natural_language_query}")
        start_time = time.time()
        
        try:
            # 执行查询
            result = self.agent.invoke({"input": natural_language_query})
            execution_time = time.time() - start_time
            
            # 提取 SQL 语句（从中间步骤中）
            sql_query = self._extract_sql_from_steps(result.get("intermediate_steps", []))
            
            response = {
                "success": True,
                "query": natural_language_query,
                "sql": sql_query,
                "result": result["output"],
                "execution_time": round(execution_time, 2),
                "intermediate_steps": len(result.get("intermediate_steps", []))
            }
            
            print(f"✅ 查询成功 (耗时: {execution_time:.2f}s)")
            return response
            
        except Exception as e:
            execution_time = time.time() - start_time
            print(f"❌ 查询失败: {e}")
            return {
                "success": False,
                "error": str(e),
                "query": natural_language_query,
                "execution_time": round(execution_time, 2)
            }
    
    def _extract_sql_from_steps(self, steps) -> Optional[str]:
        """从中间步骤中提取 SQL 语句"""
        for step in steps:
            if len(step) >= 2:
                action, observation = step[0], step[1]
                if hasattr(action, 'tool') and action.tool == 'sql_db_query':
                    return action.tool_input.get('query', '')
        return None
    
    def get_table_info(self) -> str:
        """获取表结构信息"""
        return self.db.get_table_info(['hsbc_fund_unified'])

def main():
    """主函数 - 演示 NL2SQL 功能"""
    print("=== HSBC Fund NL2SQL 演示 ===")
    
    # 检查环境变量
    openai_api_key = os.getenv("OPENAI_API_KEY")
    if not openai_api_key:
        print("❌ 请设置 OPENAI_API_KEY 环境变量")
        print("export OPENAI_API_KEY='your-api-key-here'")
        return
    
    # 构建数据库连接字符串
    db_uri = f"postgresql://{DB_CONFIG['user']}:{DB_CONFIG['password']}@{DB_CONFIG['host']}:{DB_CONFIG['port']}/{DB_CONFIG['database']}"
    
    try:
        # 初始化 NL2SQL 服务
        nl2sql = HSBCFundNL2SQL(
            db_uri=db_uri,
            openai_api_key=openai_api_key,
            model="gpt-3.5-turbo"  # 使用更经济的模型进行演示
        )
        
        # 演示查询列表
        demo_queries = [
            "数据库中总共有多少个基金产品？",
            "找出风险等级为5的前5个基金，按NAV排序",
            "BlackRock 有多少只基金？平均风险等级是多少？",
            "找出1年收益率超过30%的基金，显示基金名称和收益率",
            "哪些基金在前十大持仓中包含美国公司？",
        ]
        
        print(f"\n📋 准备执行 {len(demo_queries)} 个演示查询...")
        
        # 执行演示查询
        for i, query in enumerate(demo_queries, 1):
            print(f"\n{'='*60}")
            print(f"演示查询 {i}/{len(demo_queries)}")
            print(f"{'='*60}")
            
            result = nl2sql.query(query)
            
            if result["success"]:
                print(f"🔤 生成SQL: {result.get('sql', 'N/A')}")
                print(f"📊 查询结果:\n{result['result']}")
                print(f"⏱️  执行时间: {result['execution_time']}秒")
            else:
                print(f"❌ 查询失败: {result['error']}")
            
            # 添加延迟避免API限制
            if i < len(demo_queries):
                print("⏳ 等待3秒...")
                time.sleep(3)
        
        print(f"\n{'='*60}")
        print("🎉 演示完成！")
        print(f"{'='*60}")
        
        # 交互式查询模式
        print("\n💬 进入交互式查询模式（输入 'quit' 退出）:")
        while True:
            try:
                user_query = input("\n🔍 请输入您的查询: ").strip()
                if user_query.lower() in ['quit', 'exit', '退出']:
                    break
                
                if not user_query:
                    continue
                
                result = nl2sql.query(user_query)
                
                if result["success"]:
                    print(f"\n📊 查询结果:\n{result['result']}")
                    if result.get('sql'):
                        print(f"\n🔤 生成的SQL:\n{result['sql']}")
                else:
                    print(f"\n❌ 查询失败: {result['error']}")
                    
            except KeyboardInterrupt:
                break
            except Exception as e:
                print(f"❌ 发生错误: {e}")
        
        print("\n👋 再见！")
        
    except Exception as e:
        print(f"❌ 程序执行失败: {e}")
        return 1
    
    return 0

if __name__ == "__main__":
    sys.exit(main())
