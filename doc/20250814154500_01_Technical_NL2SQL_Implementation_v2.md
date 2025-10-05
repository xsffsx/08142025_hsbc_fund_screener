# HSBC 基金筛选器 NL2SQL MVP 实现方案（单 Agent、无路由）

- 文档创建时间: 2025-08-14 19:10:00
- 版本: v2.0 (MVP 简化版)
- 参考: ./20250814154500_01_Technical_NL2SQL_Implementation.md

## 目录
- 1. 背景与目标
- 2. 单 Agent vs 多 Agent/路由 对比
- 3. 架构设计（Mermaid，统一样式）
- 4. MVP 实现（核心代码与提示词）
- 5. JSONB 查询优化与提示词设计
- 6. 示例查询（标量/JSONB/聚合）
- 7. 数据准备：从 full_backup.sql 恢复
- 8. 部署与运行步骤
- 9. 约束与风险与扩展

## 1. 背景与目标
- 数据库：单表 hsbc_fund_unified（标量列 + JSONB 列，已建 B-Tree/GIN 索引）
- 数据来源：docker/postgres/full_backup.sql（DDL+DML）
- 目标：最小代码复杂度、最大功能覆盖（标量筛选、JSONB深度查询、聚合分析）
- 结论：MVP 不需要复杂查询路由；采用“单一 LangChain SQL Agent + 强提示词”即可覆盖主要查询类型

## 2. 单 Agent vs 多 Agent/路由 对比
- 单 Agent（MVP 推荐）
  - 优点：实现简单、维护低、交付快；依赖 LLM 语义泛化可覆盖标量/JSONB/聚合
  - 风险：极少数复杂意图拆解不完美；可用提示词和示例模板缓解
- 多 Agent/路由（成熟期可选）
  - 优点：细分专长与策略
  - 代价：引入路由判断与状态管理，复杂度显著上升，MVP 阶段性价比低

## 3. 架构设计（Mermaid，统一样式）
（样式参考 .augment/20250802140100_01_Level1_Architecture_Diagram.md）
```mermaid
flowchart TD
    User(("👤<br/>用户"))

    subgraph AppLayer ["💻 应用层"]
      NL2SQLAgent["🤖<br/>NL2SQL Agent<br/>单一 LangChain SQL Agent"]
    end

    subgraph AILayer ["🤖 AI处理层"]
      LLMCore["🧠<br/>LLM (GPT-4/3.5)"]
      SQLToolkit["🛠️<br/>SQL Database Toolkit"]
    end

    subgraph DataLayer ["📊 数据层"]
      DB[("🗄️<br/>PostgreSQL 16")]:::db
      Table["📈 hsbc_fund_unified<br/>标量列 + JSONB 列"]:::db
    end

    User -->|#1 提问| NL2SQLAgent
    NL2SQLAgent -->|#2 生成/校验SQL| SQLToolkit
    NL2SQLAgent -->|#3 语义理解| LLMCore
    SQLToolkit -->|#4 执行SQL| DB
    DB -->|#5 读取| Table
    SQLToolkit -->|#6 结果| NL2SQLAgent
    NL2SQLAgent -->|#7 答案| User

    classDef ui fill:transparent,stroke:#af52de,stroke-width:3px,color:#ffffff
    classDef ai fill:transparent,stroke:#eab308,stroke-width:3px,color:#ffffff
    classDef logic fill:transparent,stroke:#10b981,stroke-width:3px,color:#ffffff
    classDef db fill:transparent,stroke:#06b6d4,stroke-width:3px,color:#ffffff

    class User ui
    class NL2SQLAgent logic
    class LLMCore ai
    class SQLToolkit logic
```

## 4. MVP 实现（核心代码与提示词）
安装依赖
```bash
pip install -U langchain langchain-community langchain-openai psycopg2-binary
```
核心代码（单 Agent + 强提示词）
```python
from langchain_community.agent_toolkits.sql.toolkit import SQLDatabaseToolkit
from langchain_community.utilities.sql_database import SQLDatabase
from langchain_community.agent_toolkits.sql.base import create_sql_agent
from langchain_openai import ChatOpenAI
import os

DB_URI = "postgresql://hsbc_user:hsbc_pass@localhost:5433/hsbc_fund"
PROMPT = """你是 HSBC 基金 SQL 助手，用 hsbc_fund_unified 单表回答问题。
【标量列】risk_level, currency, allow_buy, nav, family_name, hsbc_category_name
【JSONB列】risk_json, top10_holdings, holding_allocation, summary_cumulative, chart_timeseries
【JSONB范式】
- 风险：FROM hsbc_fund_unified f, LATERAL jsonb_array_elements(f.risk_json) r
    取：(r->'yearRisk'->>'totalReturn')::NUMERIC, (r->'yearRisk'->>'year')::INTEGER
- 持仓：LATERAL jsonb_array_elements(f.top10_holdings->'items') item（market/securityName/weighting）
- 配置：LATERAL jsonb_array_elements(f.holding_allocation) a, jsonb_array_elements(a->'breakdowns') b
- 收益：LATERAL jsonb_array_elements(f.summary_cumulative->'items') it（period/totalReturn）
【优化】先标量过滤再展开 JSONB；数值使用 ::NUMERIC；默认最多 {top_k} 行。"""

def build_agent():
    os.environ.setdefault("OPENAI_API_KEY", "<set_your_key>")
    db = SQLDatabase.from_uri(DB_URI)
    llm = ChatOpenAI(model="gpt-3.5-turbo", temperature=0)
    toolkit = SQLDatabaseToolkit(db=db, llm=llm)
    return create_sql_agent(llm=llm, toolkit=toolkit, agent_type="tool-calling",
        verbose=True, max_iterations=15, top_k=10, prefix=PROMPT,
        agent_executor_kwargs={"return_intermediate_steps": True})
```
最小使用
```python
agent = build_agent()
resp = agent.invoke({"input": "找出风险等级5且1年收益率>40%的基金"})
print(resp["output"])  # 答案
```

## 5. JSONB 查询优化与提示词设计
- 统一路径：
  - 风险：risk_json → yearRisk.(year,totalReturn,sharpeRatio)
  - 持仓：top10_holdings.items → securityName/market/weighting
  - 配置：holding_allocation → methods/breakdowns(name,weighting)
  - 收益：summary_cumulative.items → period/totalReturn
- 性能：先标量过滤（risk_level/currency/family_name 等），再 LATERAL 展开；使用 ::NUMERIC/::INTEGER；用 @> 与 ? 做包含/键存在检查

## 6. 示例查询（标量/JSONB/聚合）
- 标量
```sql
SELECT product_code, name, risk_level, nav, currency
FROM hsbc_fund_unified
WHERE risk_level = 5 AND currency = 'USD'
ORDER BY nav DESC
LIMIT 10;
```
- JSONB：1Y 收益率 > 40%
```sql
SELECT f.product_code, f.name,
       (r->'yearRisk'->>'totalReturn')::NUMERIC AS return_1y
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.risk_json) r
WHERE (r->'yearRisk'->>'year')::INTEGER = 1
  AND (r->'yearRisk'->>'totalReturn')::NUMERIC > 40
ORDER BY return_1y DESC
LIMIT 10;
```
- JSONB：含加拿大持仓
```sql
SELECT DISTINCT f.product_code, f.name
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.top10_holdings->'items') item
WHERE item->>'market' = 'CA'
LIMIT 20;
```
- 混合：BlackRock 且持有美国股票
```sql
SELECT DISTINCT f.product_code, f.name
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.top10_holdings->'items') item
WHERE f.family_name = 'BlackRock' AND item->>'market' = 'US'
LIMIT 20;
```
- 聚合：按风险等级统计
```sql
SELECT risk_level, COUNT(*) AS fund_count, ROUND(AVG(nav), 2) AS avg_nav
FROM hsbc_fund_unified
WHERE risk_level IS NOT NULL
GROUP BY risk_level
ORDER BY risk_level;
```

## 7. 数据准备：从 full_backup.sql 恢复
```bash
cd 08132025_hsbc_fund_screener/docker/postgres
# 启动容器（端口 5433）
docker-compose up -d
# 恢复（会覆盖数据库）
docker exec -i postgresql-hsbc-fund-screener psql -U hsbc_user -d hsbc_fund < full_backup.sql
# 验证
docker exec -i postgresql-hsbc-fund-screener psql -U hsbc_user -d hsbc_fund -c "SELECT COUNT(*) FROM hsbc_fund_unified;"
```

## 8. 部署与运行步骤
```bash
pip install -U langchain langchain-community langchain-openai psycopg2-binary
export OPENAI_API_KEY=your_openai_api_key
# 运行演示（将 build_agent 粘贴到脚本）
python - << 'PY'
from your_module import build_agent
agent = build_agent()
print(agent.invoke({"input":"BlackRock有多少只基金？"})["output"])
PY
```

## 9. 约束与风险与扩展
- 约束：LLM 偶发无效 SQL；通过“查询范式 + 类型转换 + 先标量后 JSONB”降低风险
- 风险：JSONB 结构个别字段缺失，查询需做键存在判断
- 扩展：引入轻量路由（仅切换提示词）、缓存（Redis）与慢 SQL 采样（EXPLAIN/ANALYZE）、为高频 JSONB 路径加物化视图/生成列、FastAPI 服务化

