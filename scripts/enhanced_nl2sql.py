#!/usr/bin/env python3
"""
增强版 HSBC Fund NL2SQL 系统
集成基于 LLM 的智能查询路由器，提供更精准的查询策略
"""

import os
import sys
import time
import json
from typing import Dict, Any, Optional

try:
    from langchain_community.agent_toolkits.sql.toolkit import SQLDatabaseToolkit
    from langchain_community.utilities.sql_database import SQLDatabase
    from langchain_community.agent_toolkits.sql.base import create_sql_agent
    from langchain_openai import ChatOpenAI
    from llm_query_router import LLMQueryRouter, QueryOptimizer, QueryType, QueryComplexity
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

class EnhancedHSBCFundNL2SQL:
    """增强版 HSBC 基金 NL2SQL 服务"""
    
    def __init__(self, db_uri: str, openai_api_key: str, model: str = "gpt-3.5-turbo"):
        """初始化增强版 NL2SQL 服务"""
        self.db_uri = db_uri
        self.model = model
        
        # 设置 OpenAI API Key
        os.environ["OPENAI_API_KEY"] = openai_api_key
        
        try:
            # 初始化数据库连接
            self.db = SQLDatabase.from_uri(db_uri)
            print(f"✅ 数据库连接成功")
            
            # 初始化 LLM
            self.llm = ChatOpenAI(model=model, temperature=0, max_tokens=2000)
            print(f"✅ LLM 初始化成功: {model}")
            
            # 初始化查询路由器和优化器
            self.router = LLMQueryRouter(model=model)
            self.optimizer = QueryOptimizer()
            print(f"✅ 智能路由器初始化成功")
            
            # 创建不同类型的 SQL Agent
            self.agents = self._create_specialized_agents()
            print(f"✅ 专用 SQL Agent 创建成功")
            
        except Exception as e:
            print(f"❌ 初始化失败: {e}")
            raise
    
    def _create_specialized_agents(self) -> Dict[str, Any]:
        """创建针对不同查询类型的专用 Agent"""
        
        # 基础工具包
        toolkit = SQLDatabaseToolkit(db=self.db, llm=self.llm)
        
        agents = {}
        
        # 简单查询 Agent - 优化标量列查询
        simple_prefix = """你是一个专门处理简单基金查询的SQL助手。
专注于使用标量列进行高效查询，避免复杂的JSONB操作。

优化策略：
- 优先使用有索引的字段：risk_level, currency, allow_buy, nav_date
- 使用简单的WHERE条件和ORDER BY
- 避免使用JSONB字段，除非绝对必要
- 限制结果数量，默认最多 {top_k} 条

表结构重点：
- product_code (主键), name, family_name, currency
- risk_level (1-5), nav, allow_buy, allow_sell
- hsbc_category_name, assets_under_mgmt, inception_date"""

        agents[QueryType.SIMPLE] = create_sql_agent(
            llm=self.llm,
            toolkit=toolkit,
            agent_type="tool-calling",
            prefix=simple_prefix,
            verbose=False,
            max_iterations=10,
            top_k=10
        )
        
        # 复杂查询 Agent - 专门处理JSONB查询
        complex_prefix = """你是一个专门处理复杂基金数据查询的SQL专家。
擅长JSONB字段的深度查询和数据展开。

JSONB查询技巧：
- 使用 jsonb_array_elements() 展开数组
- 使用 LATERAL JOIN 进行侧向连接
- 使用 ->> 操作符提取文本值，::NUMERIC 转换数值
- 利用GIN索引优化查询性能

关键JSONB字段：
- risk_json: 风险指标 (risk_item->'yearRisk'->>'totalReturn')::NUMERIC
- top10_holdings: 持仓 (item->>'securityName', item->>'market')
- holding_allocation: 配置 (breakdown->>'name', breakdown->>'weighting')
- summary_cumulative: 收益 (item->>'period', item->>'totalReturn')

查询模式：
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.jsonb_field) AS element
WHERE conditions"""

        agents[QueryType.COMPLEX] = create_sql_agent(
            llm=self.llm,
            toolkit=toolkit,
            agent_type="tool-calling",
            prefix=complex_prefix,
            verbose=False,
            max_iterations=15,
            top_k=10
        )
        
        # 混合查询 Agent - 标量+JSONB组合查询
        hybrid_prefix = """你是一个处理混合查询的SQL专家。
擅长结合标量列筛选和JSONB字段展示的复合查询。

优化策略：
1. 先用标量列进行筛选（利用B-Tree索引）
2. 再用JSONB字段进行数据展开和展示
3. 避免在JSONB字段上进行大范围筛选

查询模板：
SELECT f.product_code, f.name, f.risk_level,
       (jsonb_element->path)::TYPE as extracted_value
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.jsonb_field) AS jsonb_element
WHERE f.scalar_field = value  -- 先筛选标量列
  AND (jsonb_element->path)::TYPE condition  -- 再筛选JSONB
ORDER BY extracted_value DESC
LIMIT {top_k};"""

        agents[QueryType.HYBRID] = create_sql_agent(
            llm=self.llm,
            toolkit=toolkit,
            agent_type="tool-calling",
            prefix=hybrid_prefix,
            verbose=False,
            max_iterations=15,
            top_k=10
        )
        
        # 聚合查询 Agent - 统计分析查询
        aggregation_prefix = """你是一个专门处理聚合统计查询的SQL专家。
擅长COUNT、SUM、AVG、GROUP BY等聚合操作。

聚合查询优化：
- 在GROUP BY前先筛选数据
- 使用HAVING进行聚合后筛选
- 合理使用索引字段进行分组
- 避免在JSONB字段上直接聚合

常用聚合模式：
- 按基金公司统计：GROUP BY family_name
- 按风险等级分布：GROUP BY risk_level
- 按币种统计：GROUP BY currency
- 按分类统计：GROUP BY hsbc_category_name"""

        agents[QueryType.AGGREGATION] = create_sql_agent(
            llm=self.llm,
            toolkit=toolkit,
            agent_type="tool-calling",
            prefix=aggregation_prefix,
            verbose=False,
            max_iterations=12,
            top_k=20
        )
        
        return agents
    
    def query(self, natural_language_query: str) -> Dict[str, Any]:
        """执行智能路由的自然语言查询"""
        print(f"\n🔍 执行查询: {natural_language_query}")
        start_time = time.time()
        
        try:
            # 第一步：智能路由分析
            print("🎯 分析查询类型...")
            analysis = self.router.analyze_query(natural_language_query)
            
            # 第二步：查询优化
            print(f"📊 查询类型: {analysis.query_type.value} (复杂度: {analysis.complexity.value})")
            optimization = self.optimizer.optimize_query_strategy(analysis)
            
            # 第三步：选择合适的Agent
            agent = self._select_agent(analysis.query_type)
            
            # 第四步：执行查询
            print(f"🚀 使用 {analysis.query_type.value} Agent 执行查询...")
            result = agent.invoke({"input": natural_language_query})
            
            execution_time = time.time() - start_time
            
            # 提取SQL语句
            sql_query = self._extract_sql_from_steps(result.get("intermediate_steps", []))
            
            response = {
                "success": True,
                "query": natural_language_query,
                "analysis": {
                    "query_type": analysis.query_type.value,
                    "complexity": analysis.complexity.value,
                    "confidence": analysis.confidence,
                    "reasoning": analysis.reasoning,
                    "required_fields": analysis.required_fields,
                    "jsonb_fields": analysis.jsonb_fields
                },
                "optimization": optimization,
                "sql": sql_query,
                "result": result["output"],
                "execution_time": round(execution_time, 2),
                "intermediate_steps": len(result.get("intermediate_steps", []))
            }
            
            print(f"✅ 查询成功 (耗时: {execution_time:.2f}s, 置信度: {analysis.confidence:.2f})")
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
    
    def _select_agent(self, query_type: QueryType):
        """根据查询类型选择合适的Agent"""
        agent = self.agents.get(query_type)
        if agent is None:
            # 降级到混合查询Agent
            print(f"⚠️  未找到 {query_type.value} 专用Agent，使用混合查询Agent")
            agent = self.agents.get(QueryType.HYBRID)
        return agent
    
    def _extract_sql_from_steps(self, steps) -> Optional[str]:
        """从中间步骤中提取SQL语句"""
        for step in steps:
            if len(step) >= 2:
                action, observation = step[0], step[1]
                if hasattr(action, 'tool') and action.tool == 'sql_db_query':
                    return action.tool_input.get('query', '')
        return None
    
    def get_routing_stats(self) -> Dict[str, Any]:
        """获取路由统计信息"""
        return {
            "available_agents": list(self.agents.keys()),
            "router_model": self.router.llm.model_name,
            "database_tables": self.db.get_usable_table_names(),
            "optimization_strategies": list(self.optimizer.optimization_strategies.keys())
        }

def main():
    """主函数 - 演示增强版 NL2SQL 功能"""
    print("🚀 增强版 HSBC Fund NL2SQL 演示")
    print("=" * 60)
    
    # 检查环境变量
    openai_api_key = os.getenv("OPENAI_API_KEY")
    if not openai_api_key:
        print("❌ 请设置 OPENAI_API_KEY 环境变量")
        return
    
    # 构建数据库连接字符串
    db_uri = f"postgresql://{DB_CONFIG['user']}:{DB_CONFIG['password']}@{DB_CONFIG['host']}:{DB_CONFIG['port']}/{DB_CONFIG['database']}"
    
    try:
        # 初始化增强版 NL2SQL 服务
        nl2sql = EnhancedHSBCFundNL2SQL(
            db_uri=db_uri,
            openai_api_key=openai_api_key,
            model="gpt-3.5-turbo"
        )
        
        # 显示系统信息
        stats = nl2sql.get_routing_stats()
        print(f"\n📊 系统信息:")
        print(f"   可用Agent: {', '.join(stats['available_agents'])}")
        print(f"   路由模型: {stats['router_model']}")
        
        # 演示查询列表（覆盖不同类型）
        demo_queries = [
            # 简单查询
            "找出风险等级为5的前5个基金",
            
            # 复杂查询  
            "哪些基金的1年收益率超过30%？显示收益率和夏普比率",
            
            # 混合查询
            "找出BlackRock管理的、持有美国股票的基金",
            
            # 聚合查询
            "按基金公司统计基金数量和平均风险等级",
            
            # 分析查询
            "分析不同风险等级基金的平均收益率分布"
        ]
        
        print(f"\n📋 准备执行 {len(demo_queries)} 个演示查询...")
        
        # 执行演示查询
        for i, query in enumerate(demo_queries, 1):
            print(f"\n{'='*60}")
            print(f"演示查询 {i}/{len(demo_queries)}")
            print(f"{'='*60}")
            
            result = nl2sql.query(query)
            
            if result["success"]:
                analysis = result["analysis"]
                print(f"🎯 路由分析:")
                print(f"   类型: {analysis['query_type']} (复杂度: {analysis['complexity']})")
                print(f"   置信度: {analysis['confidence']:.2f}")
                print(f"   涉及字段: {', '.join(analysis['required_fields'])}")
                if analysis['jsonb_fields']:
                    print(f"   JSONB字段: {', '.join(analysis['jsonb_fields'])}")
                print(f"   策略: {result['optimization']['strategy']}")
                
                if result.get('sql'):
                    print(f"\n🔤 生成SQL:\n{result['sql']}")
                
                print(f"\n📊 查询结果:\n{result['result']}")
                print(f"⏱️  总耗时: {result['execution_time']}秒")
            else:
                print(f"❌ 查询失败: {result['error']}")
            
            # 添加延迟
            if i < len(demo_queries):
                print("⏳ 等待3秒...")
                time.sleep(3)
        
        print(f"\n{'='*60}")
        print("🎉 演示完成！")
        
    except Exception as e:
        print(f"❌ 程序执行失败: {e}")
        return 1
    
    return 0

if __name__ == "__main__":
    sys.exit(main())
