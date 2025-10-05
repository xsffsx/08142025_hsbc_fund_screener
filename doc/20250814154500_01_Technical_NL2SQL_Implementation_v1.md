# HSBC 基金筛选器 NL2SQL 实现技术方案

**文档创建时间**: 2025-08-14 15:45:00  
**版本**: v1.0  
**作者**: Augment Agent  

## 目录

- [1. 概述](#1-概述)
- [2. 架构设计](#2-架构设计)
- [3. 数据库结构分析](#3-数据库结构分析)
- [4. 智能查询路由器](#4-智能查询路由器)
- [5. LangChain 集成方案](#5-langchain-集成方案)
- [6. JSONB 查询优化策略](#6-jsonb-查询优化策略)
- [7. 实现步骤](#7-实现步骤)
- [8. 示例查询](#8-示例查询)
- [9. 性能优化](#9-性能优化)
- [10. 部署配置](#10-部署配置)

## 1. 概述

本文档详细描述了基于现有 HSBC 基金数据库架构和 LangChain SQL 工具包的自然语言到 SQL（NL2SQL）实现方案。系统将支持用户使用自然语言查询 1,407 个基金产品的复杂信息，包括标量字段和 JSONB 结构化数据。

### 1.1 核心特性

- **混合数据结构支持**: 标量列（高频查询优化）+ JSONB 列（复杂结构保留）
- **智能查询路由**: 根据查询复杂度选择最优执行路径
- **JSONB 深度查询**: 支持风险指标、持仓分配、历史收益等复杂查询
- **性能优化**: 利用 GIN 索引和查询计划优化
- **多语言支持**: 中英文自然语言查询

### 1.2 技术栈

- **数据库**: PostgreSQL 16 + pgvector
- **NL2SQL 引擎**: LangChain SQL Agent + OpenAI/本地 LLM
- **查询优化**: GIN 索引 + 查询计划缓存
- **连接池**: psycopg2 ThreadedConnectionPool

## 2. 架构设计

### 2.1 系统架构图

```mermaid
flowchart TD
    %% 用户交互层
    User[👤 用户自然语言查询] --> NLProcessor[🧠 自然语言处理器]

    %% 智能路由层
    NLProcessor --> LLMRouter[🎯 LLM 智能路由器]
    LLMRouter --> QueryAnalyzer[📊 查询分析器]
    QueryAnalyzer --> QueryOptimizer[⚡ 查询优化器]

    %% 专用 Agent 层
    QueryOptimizer -->|简单查询| SimpleAgent[📊 简单查询 Agent]
    QueryOptimizer -->|复杂查询| ComplexAgent[🔍 复杂查询 Agent]
    QueryOptimizer -->|混合查询| HybridAgent[⚡ 混合查询 Agent]
    QueryOptimizer -->|聚合查询| AggregationAgent[📈 聚合查询 Agent]
    QueryOptimizer -->|分析查询| AnalyticalAgent[🔬 分析查询 Agent]
    
    %% 工具层
    SimpleAgent --> SQLToolkit1[🛠️ 标量优化工具包]
    ComplexAgent --> SQLToolkit2[🛠️ JSONB 专用工具包]
    HybridAgent --> SQLToolkit3[🛠️ 混合查询工具包]
    AggregationAgent --> SQLToolkit4[🛠️ 聚合分析工具包]
    AnalyticalAgent --> SQLToolkit5[🛠️ 高级分析工具包]

    SQLToolkit1 --> SchemaInfo[📋 Schema Info Tool]
    SQLToolkit2 --> QueryExec[⚙️ Query Execution Tool]
    SQLToolkit3 --> QueryCheck[✅ Query Checker Tool]
    SQLToolkit4 --> QueryExec
    SQLToolkit5 --> QueryExec
    
    %% 数据库层
    QueryExec --> PostgreSQL[(🗄️ PostgreSQL 16)]
    PostgreSQL --> HSBCTable[📈 hsbc_fund_unified]
    
    %% 数据结构
    HSBCTable --> ScalarCols[📊 标量列<br/>product_code, name<br/>risk_level, nav, currency]
    HSBCTable --> JSONBCols[🔗 JSONB 列<br/>risk_json, top10_holdings<br/>holding_allocation, chart_timeseries]
    
    %% 索引优化
    ScalarCols --> BTreeIdx[🌳 B-Tree 索引]
    JSONBCols --> GINIdx[🔍 GIN 索引]
    
    %% 结果处理
    QueryExec --> ResultProcessor[📤 结果处理器]
    ResultProcessor --> FormattedResult[📋 格式化结果]
    FormattedResult --> User
    
    %% 样式定义
    classDef userLayer fill:#e1f5fe,stroke:#01579b,stroke-width:2px,color:#000000
    classDef nlLayer fill:#f3e5f5,stroke:#4a148c,stroke-width:2px,color:#000000
    classDef agentLayer fill:#e8f5e8,stroke:#1b5e20,stroke-width:2px,color:#000000
    classDef toolLayer fill:#fff3e0,stroke:#e65100,stroke-width:2px,color:#000000
    classDef dbLayer fill:#fce4ec,stroke:#880e4f,stroke-width:2px,color:#000000
    classDef indexLayer fill:#f1f8e9,stroke:#33691e,stroke-width:2px,color:#000000
    
    class User,FormattedResult userLayer
    class NLProcessor,LLMRouter,QueryAnalyzer,QueryOptimizer nlLayer
    class SimpleAgent,ComplexAgent,HybridAgent,AggregationAgent,AnalyticalAgent agentLayer
    class SQLToolkit1,SQLToolkit2,SQLToolkit3,SQLToolkit4,SQLToolkit5,SchemaInfo,QueryExec,QueryCheck,ResultProcessor toolLayer
    class PostgreSQL,HSBCTable,ScalarCols,JSONBCols dbLayer
    class BTreeIdx,GINIdx indexLayer
```

### 2.2 查询处理流程

```mermaid
sequenceDiagram
    participant U as 用户
    participant NLP as NL处理器
    participant QR as 查询路由
    participant LC as LangChain Agent
    participant DB as PostgreSQL
    participant RP as 结果处理器
    
    U->>NLP: "找出风险等级5且1年收益率>40%的基金"
    NLP->>QR: 解析查询意图
    QR->>QR: 识别查询类型：混合查询
    QR->>LC: 路由到LangChain Agent
    
    LC->>DB: 获取表结构信息
    DB-->>LC: hsbc_fund_unified schema
    
    LC->>LC: 生成SQL查询
    Note over LC: SELECT product_code, name, risk_level,<br/>(risk_item->'yearRisk'->>'totalReturn')::NUMERIC<br/>FROM hsbc_fund_unified f,<br/>LATERAL jsonb_array_elements(f.risk_json) AS risk_item<br/>WHERE risk_level = 5<br/>AND (risk_item->'yearRisk'->>'year')::INTEGER = 1<br/>AND (risk_item->'yearRisk'->>'totalReturn')::NUMERIC > 40
    
    LC->>DB: 执行SQL查询
    DB-->>LC: 查询结果
    
    LC->>RP: 传递结果
    RP->>RP: 格式化和后处理
    RP-->>U: 返回自然语言答案
```

## 3. 数据库结构分析

### 3.1 表结构概览

`hsbc_fund_unified` 表采用混合存储策略：

#### 标量列（高频查询优化）
- **标识字段**: `product_code` (主键), `isin_code`, `name`
- **分类字段**: `family_name`, `hsbc_category_name`, `currency`
- **风险收益**: `risk_level`, `nav`, `nav_date`
- **交易许可**: `allow_buy`, `allow_sell`, `allow_sw_in`
- **费用规模**: `expense_ratio`, `assets_under_mgmt`

#### JSONB 列（复杂结构保留）
- **`risk_json`**: 1/3/5/10年风险指标数组
- **`summary_cumulative`**: 累计收益率数据
- **`top10_holdings`**: 前十大持仓明细
- **`holding_allocation`**: 资产配置和地区分布
- **`chart_timeseries`**: 历史价格时间序列
- **`price_quote`**: 实时报价和变动信息

### 3.2 JSONB 结构示例

#### risk_json 结构
```json
[
  {
    "yearRisk": {
      "year": 1,
      "beta": 1.02,
      "alpha": 3.2,
      "stdDev": 24.912,
      "sharpeRatio": 1.401,
      "totalReturn": 43.55753,
      "endDate": "2025-07-31"
    }
  }
]
```

#### top10_holdings 结构
```json
{
  "items": [
    {
      "securityName": "Newmont Corp",
      "market": "US",
      "weighting": 8.45
    }
  ],
  "asOfDate": "2025-07-31"
}
```

## 4. 智能查询路由器

### 4.1 基于 LLM 的查询分析

传统的基于规则的查询路由器存在以下局限性：
- **规则固化**: 难以处理自然语言的多样性和歧义性
- **维护困难**: 新的查询模式需要手动添加规则
- **准确性有限**: 复杂查询的分类准确率不高

我们采用基于 LLM 的智能查询路由器，具有以下优势：

#### 4.1.1 智能查询分析
```python
class QueryAnalysis(BaseModel):
    """LLM 分析结果结构"""
    query_type: QueryType          # 查询类型：simple/complex/hybrid/aggregation/analytical
    complexity: QueryComplexity    # 复杂度：low/medium/high
    required_fields: List[str]     # 需要的字段列表
    jsonb_fields: List[str]        # 涉及的JSONB字段
    filters: List[str]             # 筛选条件
    aggregations: List[str]        # 聚合操作
    confidence: float              # 分析置信度 0-1
    reasoning: str                 # 路由决策理由
    suggested_strategy: str        # 建议的查询策略
```

#### 4.1.2 查询类型定义

**SIMPLE (简单查询)**:
- 仅使用标量列的基础筛选和排序
- 示例: "找出风险等级为5的美元基金"
- 特点: 高性能，利用B-Tree索引

**COMPLEX (复杂查询)**:
- 主要使用JSONB字段的深度查询
- 示例: "哪些基金的1年收益率超过40%且夏普比率>1.2"
- 特点: 需要JSONB展开和复杂条件

**HYBRID (混合查询)**:
- 标量列筛选 + JSONB字段展示
- 示例: "BlackRock管理的持有美国股票的基金"
- 特点: 先标量筛选，再JSONB展开

**AGGREGATION (聚合查询)**:
- 包含COUNT、SUM、AVG等聚合操作
- 示例: "按基金公司统计基金数量和平均风险等级"
- 特点: 需要GROUP BY和聚合函数

**ANALYTICAL (分析查询)**:
- 复杂分析，如趋势分析、相关性分析
- 示例: "分析不同风险等级基金的收益率分布"
- 特点: 多维度分析，可能需要窗口函数

### 4.2 专用 Agent 架构

基于查询类型，系统创建5个专用的SQL Agent，每个Agent都有针对性的优化：

#### 4.2.1 简单查询 Agent
```python
simple_prefix = """你是一个专门处理简单基金查询的SQL助手。
专注于使用标量列进行高效查询，避免复杂的JSONB操作。

优化策略：
- 优先使用有索引的字段：risk_level, currency, allow_buy, nav_date
- 使用简单的WHERE条件和ORDER BY
- 避免使用JSONB字段，除非绝对必要
- 限制结果数量，默认最多 {top_k} 条"""
```

#### 4.2.2 复杂查询 Agent
```python
complex_prefix = """你是一个专门处理复杂基金数据查询的SQL专家。
擅长JSONB字段的深度查询和数据展开。

JSONB查询技巧：
- 使用 jsonb_array_elements() 展开数组
- 使用 LATERAL JOIN 进行侧向连接
- 使用 ->> 操作符提取文本值，::NUMERIC 转换数值
- 利用GIN索引优化查询性能"""
```

### 4.3 查询优化策略

每种查询类型都有对应的优化策略：

```python
optimization_strategies = {
    QueryType.SIMPLE: {
        "strategy": "direct_scalar",
        "use_indexes": ["risk_level", "currency", "allow_buy"],
        "limit_early": True,
        "estimated_performance": "fast"
    },
    QueryType.COMPLEX: {
        "strategy": "jsonb_focused",
        "use_gin_indexes": ["risk_json", "top10_holdings"],
        "lateral_joins": True,
        "estimated_performance": "medium"
    },
    QueryType.HYBRID: {
        "strategy": "scalar_first_jsonb_second",
        "filter_order": ["scalar_filters", "jsonb_expansion"],
        "estimated_performance": "medium"
    }
}
```

### 4.4 智能路由流程

```mermaid
sequenceDiagram
    participant U as 用户
    participant LR as LLM路由器
    participant QA as 查询分析器
    participant QO as 查询优化器
    participant SA as 专用Agent
    participant DB as 数据库

    U->>LR: "找出风险等级5且1年收益率>40%的基金"
    LR->>QA: 分析查询意图和复杂度
    QA->>QA: LLM分析查询结构
    QA-->>LR: 返回分析结果 (HYBRID, MEDIUM, 0.95)

    LR->>QO: 基于分析结果优化策略
    QO-->>LR: 返回优化建议

    LR->>SA: 选择混合查询Agent
    SA->>DB: 执行优化后的SQL查询
    DB-->>SA: 返回查询结果
    SA-->>U: 格式化结果返回
```

## 5. LangChain 集成方案

### 5.1 核心组件配置

#### SQLDatabaseToolkit 初始化
```python
from langchain_community.agent_toolkits.sql.toolkit import SQLDatabaseToolkit
from langchain_community.utilities.sql_database import SQLDatabase
from langchain_openai import ChatOpenAI

# 数据库连接
db = SQLDatabase.from_uri(
    "postgresql://hsbc_user:hsbc_pass@localhost:5433/hsbc_fund"
)

# LLM 配置
llm = ChatOpenAI(
    model="gpt-4",
    temperature=0,
    max_tokens=2000
)

# 工具包初始化
toolkit = SQLDatabaseToolkit(db=db, llm=llm)
```

#### SQL Agent 创建与 AgentExecutor 配置
```python
from langchain_community.agent_toolkits.sql.base import create_sql_agent
from langchain.agents import AgentExecutor

# 方法1: 使用 create_sql_agent (推荐)
agent_executor = create_sql_agent(
    llm=llm,
    toolkit=toolkit,
    agent_type="tool-calling",
    verbose=True,
    max_iterations=15,
    max_execution_time=30.0,
    early_stopping_method="force",
    top_k=10,
    agent_executor_kwargs={
        "return_intermediate_steps": True,
        "handle_parsing_errors": True
    }
)

# 方法2: 手动创建 AgentExecutor (高级配置)
from langchain.agents import create_tool_calling_agent
from langchain_core.prompts import ChatPromptTemplate

# 创建自定义提示词
prompt = ChatPromptTemplate.from_messages([
    ("system", "你是一个专业的SQL查询助手..."),
    ("human", "{input}"),
    ("placeholder", "{agent_scratchpad}")
])

# 创建Agent
agent = create_tool_calling_agent(llm, toolkit.get_tools(), prompt)

# 创建AgentExecutor
agent_executor = AgentExecutor(
    agent=agent,
    tools=toolkit.get_tools(),
    verbose=True,
    max_iterations=15,
    max_execution_time=30.0,
    early_stopping_method="force",
    return_intermediate_steps=True,
    handle_parsing_errors=True
)
```

### 5.2 AgentExecutor 详细配置

#### 5.2.1 核心参数说明

```python
agent_executor = create_sql_agent(
    llm=llm,                          # 语言模型
    toolkit=toolkit,                  # SQL工具包
    agent_type="tool-calling",        # Agent类型

    # 执行控制参数
    max_iterations=15,                # 最大迭代次数
    max_execution_time=30.0,          # 最大执行时间(秒)
    early_stopping_method="force",    # 早停策略: "force" 或 "generate"

    # 查询参数
    top_k=10,                        # 默认返回行数
    verbose=True,                    # 是否显示详细日志

    # 高级配置
    agent_executor_kwargs={
        "return_intermediate_steps": True,    # 返回中间步骤
        "handle_parsing_errors": True,       # 处理解析错误
        "trim_intermediate_steps": 10,       # 保留的中间步骤数
        "callbacks": [custom_callback]       # 自定义回调
    }
)
```

#### 5.2.2 Agent类型选择

```python
# 1. tool-calling (推荐) - 支持函数调用的现代模型
agent_executor = create_sql_agent(
    llm=llm,
    toolkit=toolkit,
    agent_type="tool-calling",
    verbose=True
)

# 2. openai-tools (兼容性) - OpenAI特定的工具调用
agent_executor = create_sql_agent(
    llm=llm,
    toolkit=toolkit,
    agent_type="openai-tools",
    verbose=True
)

# 3. zero-shot-react-description (传统) - 基于ReAct模式
agent_executor = create_sql_agent(
    llm=llm,
    toolkit=toolkit,
    agent_type="zero-shot-react-description",
    verbose=True
)
```

#### 5.2.3 错误处理与重试机制

```python
from langchain.callbacks.base import BaseCallbackHandler
from typing import Any, Dict, List

class SQLErrorHandler(BaseCallbackHandler):
    """SQL查询错误处理回调"""

    def on_tool_error(self, error: Exception, **kwargs: Any) -> Any:
        """处理工具执行错误"""
        if "syntax error" in str(error).lower():
            print(f"🔧 SQL语法错误，尝试修复: {error}")
        elif "timeout" in str(error).lower():
            print(f"⏰ 查询超时，建议优化查询: {error}")
        else:
            print(f"❌ 未知错误: {error}")

# 使用错误处理器
agent_executor = create_sql_agent(
    llm=llm,
    toolkit=toolkit,
    agent_type="tool-calling",
    agent_executor_kwargs={
        "callbacks": [SQLErrorHandler()],
        "handle_parsing_errors": True
    }
)
```

### 5.2 自定义提示词优化

#### HSBC 基金专用提示词
```python
HSBC_FUND_PREFIX = """你是一个专门处理 HSBC 基金数据的 SQL 查询助手。
数据库包含 1,407 个基金产品，存储在 hsbc_fund_unified 表中。

表结构特点：
1. 标量列：用于高频简单查询（风险等级、币种、NAV等）
2. JSONB 列：用于复杂结构查询（风险指标、持仓分配、历史数据等）

JSONB 查询要点：
- risk_json: 使用 jsonb_array_elements() 展开年度风险数据
- top10_holdings: 通过 ->'items' 访问持仓数组
- holding_allocation: 按 methods 字段筛选分配类型
- chart_timeseries: 历史价格数组，包含 date 和 navPrice

查询限制：
- 默认返回最多 {top_k} 条结果
- 仅执行 SELECT 查询，禁止 DML 操作
- 优先使用标量列进行筛选，再使用 JSONB 进行复杂查询
"""
```

## 6. JSONB 查询优化策略

### 5.1 查询模式分类

#### 模式1：标量列筛选 + JSONB 展示
```sql
-- 查询风险等级5的基金的1年收益率
SELECT 
    product_code, 
    name,
    (risk_item->'yearRisk'->>'totalReturn')::NUMERIC as return_1y
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.risk_json) AS risk_item
WHERE risk_level = 5 
  AND (risk_item->'yearRisk'->>'year')::INTEGER = 1
ORDER BY return_1y DESC
LIMIT 10;
```

#### 模式2：JSONB 条件筛选
```sql
-- 查询持有加拿大股票的基金
SELECT 
    product_code,
    name,
    item->>'securityName' as security_name,
    (item->>'weighting')::NUMERIC as weighting
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.top10_holdings->'items') AS item
WHERE item->>'market' = 'CA'
ORDER BY weighting DESC;
```

#### 模式3：复合 JSONB 查询
```sql
-- 查询地区配置中亚洲权重>50%的基金
SELECT 
    f.product_code,
    f.name,
    SUM((breakdown->>'weighting')::NUMERIC) as asia_weight
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.holding_allocation) AS allocation,
     LATERAL jsonb_array_elements(allocation->'breakdowns') AS breakdown
WHERE allocation->>'methods' = 'regionalExposures'
  AND breakdown->>'name' IN ('JP', 'CN', 'HK', 'SG', 'KR')
GROUP BY f.product_code, f.name
HAVING SUM((breakdown->>'weighting')::NUMERIC) > 50
ORDER BY asia_weight DESC;
```

### 5.2 GIN 索引利用

现有 GIN 索引优化查询：
- `idx_hfu_summary_cum_gin`: 累计收益查询
- `idx_hfu_top10_gin`: 持仓查询
- `idx_hfu_hold_alloc_gin`: 资产配置查询
- `idx_hfu_risk_json_gin`: 风险指标查询
- `idx_hfu_chart_ts_gin`: 历史价格查询

### 5.3 查询性能优化技巧

1. **先筛选后展开**: 优先使用标量列筛选，减少 JSONB 处理量
2. **索引友好路径**: 使用 `@>` 操作符进行包含查询
3. **类型转换缓存**: 避免重复的 `::NUMERIC` 转换
4. **分页查询**: 大结果集使用 LIMIT + OFFSET

## 6. 实现步骤

### 6.1 环境准备

#### 依赖安装
```bash
pip install langchain langchain-community langchain-openai psycopg2-binary
```

#### 数据库连接测试
```python
import psycopg2
from langchain_community.utilities.sql_database import SQLDatabase

# 测试连接
try:
    db = SQLDatabase.from_uri(
        "postgresql://hsbc_user:hsbc_pass@localhost:5433/hsbc_fund"
    )
    print("✓ 数据库连接成功")
    print(f"✓ 表数量: {len(db.get_usable_table_names())}")
except Exception as e:
    print(f"✗ 连接失败: {e}")
```

### 6.2 核心实现

#### NL2SQL 服务类
```python
class HSBCFundNL2SQL:
    def __init__(self, db_uri: str, llm_model: str = "gpt-4"):
        self.db = SQLDatabase.from_uri(db_uri)
        self.llm = ChatOpenAI(model=llm_model, temperature=0)
        self.toolkit = SQLDatabaseToolkit(db=self.db, llm=self.llm)
        self.agent = self._create_agent()
    
    def _create_agent(self):
        return create_sql_agent(
            llm=self.llm,
            toolkit=self.toolkit,
            agent_type="tool-calling",
            prefix=HSBC_FUND_PREFIX,
            verbose=True,
            max_iterations=15,
            top_k=10
        )
    
    def query(self, natural_language_query: str) -> dict:
        """执行自然语言查询"""
        try:
            result = self.agent.invoke({"input": natural_language_query})
            return {
                "success": True,
                "query": natural_language_query,
                "sql": self._extract_sql(result),
                "result": result["output"],
                "intermediate_steps": result.get("intermediate_steps", [])
            }
        except Exception as e:
            return {
                "success": False,
                "error": str(e),
                "query": natural_language_query
            }
```

### 6.3 查询路由器实现

```python
class QueryRouter:
    """智能查询路由器"""
    
    SIMPLE_PATTERNS = [
        r"风险等级.*[1-5]",
        r"币种.*[A-Z]{3}",
        r"基金公司",
        r"NAV.*[0-9]+"
    ]
    
    COMPLEX_PATTERNS = [
        r"收益率.*[0-9]+%",
        r"持仓.*[国家|地区|行业]",
        r"风险指标.*[beta|alpha|夏普]",
        r"历史.*[价格|走势]"
    ]
    
    def route_query(self, query: str) -> str:
        """路由查询到合适的处理器"""
        if any(re.search(pattern, query) for pattern in self.COMPLEX_PATTERNS):
            return "complex"
        elif any(re.search(pattern, query) for pattern in self.SIMPLE_PATTERNS):
            return "simple"
        else:
            return "hybrid"

## 7. 示例查询

### 7.1 简单标量查询

#### 查询1：基础筛选
**自然语言**: "找出风险等级为5的美元基金，按NAV排序"

**生成SQL**:
```sql
SELECT product_code, name, risk_level, nav, currency
FROM hsbc_fund_unified
WHERE risk_level = 5 AND currency = 'USD'
ORDER BY nav DESC
LIMIT 10;
```

**预期结果**: 返回10个高风险美元基金，按净值降序排列

#### 查询2：基金公司统计
**自然语言**: "BlackRock有多少只基金？平均风险等级是多少？"

**生成SQL**:
```sql
SELECT
    family_name,
    COUNT(*) as fund_count,
    ROUND(AVG(risk_level), 2) as avg_risk_level
FROM hsbc_fund_unified
WHERE family_name = 'BlackRock'
GROUP BY family_name;
```

### 7.2 复杂JSONB查询

#### 查询3：风险收益分析
**自然语言**: "找出1年收益率超过40%且夏普比率大于1.2的基金"

**生成SQL**:
```sql
SELECT
    f.product_code,
    f.name,
    f.risk_level,
    (risk_item->'yearRisk'->>'totalReturn')::NUMERIC as return_1y,
    (risk_item->'yearRisk'->>'sharpeRatio')::NUMERIC as sharpe_ratio
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.risk_json) AS risk_item
WHERE (risk_item->'yearRisk'->>'year')::INTEGER = 1
  AND (risk_item->'yearRisk'->>'totalReturn')::NUMERIC > 40
  AND (risk_item->'yearRisk'->>'sharpeRatio')::NUMERIC > 1.2
ORDER BY return_1y DESC
LIMIT 10;
```

#### 查询4：持仓地区分析
**自然语言**: "哪些基金在前十大持仓中包含加拿大公司？"

**生成SQL**:
```sql
SELECT DISTINCT
    f.product_code,
    f.name,
    COUNT(item) as canadian_holdings,
    SUM((item->>'weighting')::NUMERIC) as total_ca_weight
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.top10_holdings->'items') AS item
WHERE item->>'market' = 'CA'
GROUP BY f.product_code, f.name
ORDER BY total_ca_weight DESC
LIMIT 10;
```

#### 查询5：资产配置查询
**自然语言**: "找出股票配置比例超过90%的基金"

**生成SQL**:
```sql
SELECT
    f.product_code,
    f.name,
    f.hsbc_category_name,
    (breakdown->>'weighting')::NUMERIC as equity_weight
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.holding_allocation) AS allocation,
     LATERAL jsonb_array_elements(allocation->'breakdowns') AS breakdown
WHERE allocation->>'methods' = 'assetAllocations'
  AND breakdown->>'name' ILIKE '%equity%'
  AND (breakdown->>'weighting')::NUMERIC > 90
ORDER BY equity_weight DESC
LIMIT 10;
```

### 7.3 混合查询示例

#### 查询6：综合筛选
**自然语言**: "找出HSBC管理的、风险等级3-4的、持有中国股票的基金，显示其1年收益率"

**生成SQL**:
```sql
SELECT DISTINCT
    f.product_code,
    f.name,
    f.risk_level,
    f.family_name,
    (risk_item->'yearRisk'->>'totalReturn')::NUMERIC as return_1y,
    COUNT(holding_item) as china_holdings
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.risk_json) AS risk_item,
     LATERAL jsonb_array_elements(f.top10_holdings->'items') AS holding_item
WHERE f.family_name ILIKE '%HSBC%'
  AND f.risk_level BETWEEN 3 AND 4
  AND (risk_item->'yearRisk'->>'year')::INTEGER = 1
  AND holding_item->>'market' IN ('CN', 'HK')
GROUP BY f.product_code, f.name, f.risk_level, f.family_name, return_1y
ORDER BY return_1y DESC
LIMIT 10;
```

## 8. 性能优化

### 8.1 查询优化策略

#### 8.1.1 索引利用最佳实践

**B-Tree索引优化**:
```sql
-- 利用复合索引
SELECT * FROM hsbc_fund_unified
WHERE risk_level = 5 AND currency = 'USD' AND allow_buy = true;

-- 避免函数包装索引列
-- ❌ 错误：WHERE UPPER(currency) = 'USD'
-- ✅ 正确：WHERE currency = 'USD'
```

**GIN索引优化**:
```sql
-- 使用包含操作符 @>
SELECT * FROM hsbc_fund_unified
WHERE risk_json @> '[{"yearRisk": {"year": 1}}]';

-- 使用路径存在操作符 ?
SELECT * FROM hsbc_fund_unified
WHERE top10_holdings ? 'items';

-- 使用路径查询 #>
SELECT * FROM hsbc_fund_unified
WHERE risk_json #> '{0,yearRisk,totalReturn}' IS NOT NULL;
```

#### 8.1.2 查询计划优化

**EXPLAIN ANALYZE 使用**:
```sql
EXPLAIN (ANALYZE, BUFFERS, FORMAT JSON)
SELECT f.product_code, f.name,
       (risk_item->'yearRisk'->>'totalReturn')::NUMERIC as return_1y
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.risk_json) AS risk_item
WHERE f.risk_level = 5
  AND (risk_item->'yearRisk'->>'year')::INTEGER = 1
ORDER BY return_1y DESC
LIMIT 10;
```

**查询重写优化**:
```sql
-- 原始查询（慢）
SELECT * FROM hsbc_fund_unified f
WHERE EXISTS (
    SELECT 1 FROM jsonb_array_elements(f.risk_json) AS r
    WHERE (r->'yearRisk'->>'totalReturn')::NUMERIC > 40
);

-- 优化后查询（快）
SELECT f.* FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.risk_json) AS risk_item
WHERE (risk_item->'yearRisk'->>'totalReturn')::NUMERIC > 40;
```

### 8.2 连接池配置

```python
from psycopg2.pool import ThreadedConnectionPool

class OptimizedDBConnection:
    def __init__(self):
        self.pool = ThreadedConnectionPool(
            minconn=2,
            maxconn=20,
            host='localhost',
            port=5433,
            database='hsbc_fund',
            user='hsbc_user',
            password='hsbc_pass',
            # 性能优化参数
            options='-c default_transaction_isolation=read_committed '
                   '-c statement_timeout=30000 '
                   '-c lock_timeout=10000'
        )

    def get_connection(self):
        return self.pool.getconn()

    def return_connection(self, conn):
        self.pool.putconn(conn)
```

### 8.3 缓存策略

#### 8.3.1 查询结果缓存
```python
import redis
import json
import hashlib

class QueryCache:
    def __init__(self, redis_host='localhost', redis_port=6379):
        self.redis_client = redis.Redis(host=redis_host, port=redis_port)
        self.cache_ttl = 3600  # 1小时

    def get_cache_key(self, query: str) -> str:
        return f"nl2sql:{hashlib.md5(query.encode()).hexdigest()}"

    def get_cached_result(self, query: str):
        key = self.get_cache_key(query)
        cached = self.redis_client.get(key)
        return json.loads(cached) if cached else None

    def cache_result(self, query: str, result: dict):
        key = self.get_cache_key(query)
        self.redis_client.setex(
            key,
            self.cache_ttl,
            json.dumps(result, ensure_ascii=False)
        )
```

#### 8.3.2 Schema缓存
```python
class SchemaCache:
    def __init__(self):
        self._schema_cache = {}
        self._cache_timestamp = None

    def get_table_schema(self, db: SQLDatabase, table_name: str):
        cache_key = f"{table_name}_schema"

        if cache_key not in self._schema_cache:
            schema_info = db.get_table_info([table_name])
            self._schema_cache[cache_key] = schema_info

        return self._schema_cache[cache_key]
```

## 9. 部署配置

### 9.1 Docker Compose 集成

```yaml
version: '3.8'

services:
  # 现有PostgreSQL服务
  postgresql:
    image: pgvector/pgvector:pg16
    container_name: postgresql-hsbc-fund-screener
    ports:
      - "5433:5432"
    environment:
      - POSTGRES_DB=hsbc_fund
      - POSTGRES_USER=hsbc_user
      - POSTGRES_PASSWORD=hsbc_pass
    volumes:
      - postgresql_data:/var/lib/postgresql/data
      - ./init:/docker-entrypoint-initdb.d
    networks:
      - hsbc-fund-network

  # NL2SQL 服务
  nl2sql-service:
    build:
      context: .
      dockerfile: Dockerfile.nl2sql
    container_name: hsbc-nl2sql-service
    ports:
      - "8080:8080"
    environment:
      - DATABASE_URL=postgresql://hsbc_user:hsbc_pass@postgresql:5432/hsbc_fund
      - OPENAI_API_KEY=${OPENAI_API_KEY}
      - REDIS_URL=redis://redis:6379
    depends_on:
      - postgresql
      - redis
    networks:
      - hsbc-fund-network

  # Redis 缓存
  redis:
    image: redis:7-alpine
    container_name: hsbc-redis-cache
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - hsbc-fund-network

volumes:
  postgresql_data:
  redis_data:

networks:
  hsbc-fund-network:
    driver: bridge
```

### 9.2 FastAPI 服务实现

```python
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import uvicorn

app = FastAPI(title="HSBC Fund NL2SQL API", version="1.0.0")

class QueryRequest(BaseModel):
    query: str
    language: str = "zh"  # zh/en

class QueryResponse(BaseModel):
    success: bool
    query: str
    sql: str = None
    result: str = None
    execution_time: float = None
    error: str = None

@app.post("/query", response_model=QueryResponse)
async def execute_nl2sql_query(request: QueryRequest):
    """执行自然语言查询"""
    try:
        start_time = time.time()

        # 初始化NL2SQL服务
        nl2sql_service = HSBCFundNL2SQL(
            db_uri=DATABASE_URL,
            llm_model="gpt-4"
        )

        # 执行查询
        result = nl2sql_service.query(request.query)
        execution_time = time.time() - start_time

        return QueryResponse(
            success=result["success"],
            query=request.query,
            sql=result.get("sql"),
            result=result.get("result"),
            execution_time=execution_time,
            error=result.get("error")
        )

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "HSBC Fund NL2SQL"}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8080)
```

### 9.3 环境变量配置

```bash
# .env 文件
DATABASE_URL=postgresql://hsbc_user:hsbc_pass@localhost:5433/hsbc_fund
OPENAI_API_KEY=your_openai_api_key_here
REDIS_URL=redis://localhost:6379

# LLM 配置
LLM_MODEL=gpt-4
LLM_TEMPERATURE=0
LLM_MAX_TOKENS=2000

# 查询配置
MAX_QUERY_TIME=30
DEFAULT_LIMIT=10
CACHE_TTL=3600

# 日志配置
LOG_LEVEL=INFO
LOG_FORMAT=json
```

### 9.4 监控和日志

```python
import logging
import time
from functools import wraps

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

def log_query_performance(func):
    """查询性能监控装饰器"""
    @wraps(func)
    def wrapper(*args, **kwargs):
        start_time = time.time()
        try:
            result = func(*args, **kwargs)
            execution_time = time.time() - start_time

            logger.info(f"Query executed successfully", extra={
                "execution_time": execution_time,
                "query": kwargs.get("natural_language_query", ""),
                "success": True
            })
            return result

        except Exception as e:
            execution_time = time.time() - start_time
            logger.error(f"Query failed", extra={
                "execution_time": execution_time,
                "query": kwargs.get("natural_language_query", ""),
                "error": str(e),
                "success": False
            })
            raise
    return wrapper
```

## 10. 总结与后续优化

### 10.1 实现总结

本技术方案提供了完整的 HSBC 基金 NL2SQL 实现框架：

1. **架构完整**: 从用户查询到结果返回的端到端流程
2. **性能优化**: 利用混合存储和索引优化查询性能
3. **扩展性强**: 支持复杂 JSONB 查询和多种查询模式
4. **生产就绪**: 包含缓存、监控、部署等生产环境考虑

### 10.2 后续优化方向

1. **查询意图识别**: 使用机器学习模型提升查询路由准确性
2. **SQL优化器**: 基于历史查询数据自动优化SQL生成
3. **多模态支持**: 支持图表生成和可视化查询结果
4. **实时数据**: 集成实时数据流处理能力
5. **多语言支持**: 扩展支持更多自然语言

### 10.3 部署建议

1. **分阶段部署**: 先部署简单查询，逐步扩展复杂功能
2. **A/B测试**: 对比不同LLM模型和提示词的效果
3. **用户反馈**: 建立查询质量反馈机制
4. **性能监控**: 持续监控查询性能和准确性

---

**文档版本**: v1.0
**最后更新**: 2025-08-14 15:45:00
**维护者**: Augment Agent
```
