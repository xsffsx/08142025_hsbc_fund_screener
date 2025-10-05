#!/usr/bin/env python3
"""
基于 LLM 的智能查询路由器
通过 LLM 分析自然语言查询，智能路由到最优的查询策略
"""

import json
import time
from typing import Dict, Any, List, Optional
from enum import Enum
from dataclasses import dataclass

try:
    from langchain_openai import ChatOpenAI
    from langchain_core.prompts import ChatPromptTemplate
    from langchain_core.output_parsers import JsonOutputParser
    from pydantic import BaseModel, Field
except ImportError as e:
    print(f"❌ 缺少依赖: {e}")
    print("请安装: pip install langchain langchain-openai")
    exit(1)

class QueryType(str, Enum):
    """查询类型枚举"""
    SIMPLE = "simple"           # 简单标量查询
    COMPLEX = "complex"         # 复杂JSONB查询  
    HYBRID = "hybrid"           # 混合查询
    AGGREGATION = "aggregation" # 聚合统计查询
    ANALYTICAL = "analytical"   # 分析型查询

class QueryComplexity(str, Enum):
    """查询复杂度"""
    LOW = "low"       # 低复杂度：单表简单筛选
    MEDIUM = "medium" # 中复杂度：JSONB路径查询
    HIGH = "high"     # 高复杂度：多层JSONB展开+聚合

@dataclass
class QueryAnalysis(BaseModel):
    """查询分析结果"""
    query_type: QueryType = Field(description="查询类型")
    complexity: QueryComplexity = Field(description="查询复杂度")
    required_fields: List[str] = Field(description="需要的字段列表")
    jsonb_fields: List[str] = Field(description="涉及的JSONB字段")
    filters: List[str] = Field(description="筛选条件")
    aggregations: List[str] = Field(description="聚合操作")
    confidence: float = Field(description="分析置信度 0-1")
    reasoning: str = Field(description="路由决策理由")
    suggested_strategy: str = Field(description="建议的查询策略")

class LLMQueryRouter:
    """基于 LLM 的智能查询路由器"""
    
    def __init__(self, model: str = "gpt-3.5-turbo", temperature: float = 0.1):
        self.llm = ChatOpenAI(model=model, temperature=temperature)
        self.parser = JsonOutputParser(pydantic_object=QueryAnalysis)
        self.prompt = self._create_routing_prompt()
        self.chain = self.prompt | self.llm | self.parser
        
    def _create_routing_prompt(self) -> ChatPromptTemplate:
        """创建查询路由提示词"""
        
        system_prompt = """你是一个专业的数据库查询分析师，专门分析 HSBC 基金数据查询需求。

数据库结构：
hsbc_fund_unified 表包含 1,407 个基金产品，采用混合存储架构：

【标量列】- 用于高频简单查询，有 B-Tree 索引：
- product_code (主键), name, family_name, family_code
- risk_level (1-5), currency, hsbc_category_name  
- nav, nav_date, allow_buy, allow_sell, allow_sw_in
- assets_under_mgmt, expense_ratio, inception_date

【JSONB列】- 用于复杂结构查询，有 GIN 索引：
- risk_json: 1/3/5/10年风险指标数组
  结构: [{"yearRisk": {"year": 1, "beta": 1.02, "alpha": 3.2, "stdDev": 24.91, "sharpeRatio": 1.40, "totalReturn": 43.56}}]
  
- top10_holdings: 前十大持仓明细
  结构: {"items": [{"securityName": "公司名", "market": "US", "weighting": 8.45}], "asOfDate": "2025-07-31"}
  
- holding_allocation: 资产配置和地区分布
  结构: [{"methods": "assetAllocations", "breakdowns": [{"name": "Equity", "weighting": 95.19}]}]
  
- summary_cumulative: 累计收益率
  结构: {"items": [{"period": "1Y", "totalReturn": 43.56}]}
  
- chart_timeseries: 历史价格时间序列
  结构: [{"date": "2022-08-16", "navPrice": 45.23}]

查询类型定义：
1. SIMPLE: 仅使用标量列的简单筛选和排序
2. COMPLEX: 主要使用JSONB字段的复杂查询
3. HYBRID: 标量列筛选 + JSONB字段展示
4. AGGREGATION: 包含COUNT、SUM、AVG等聚合操作
5. ANALYTICAL: 复杂分析，如趋势分析、相关性分析

复杂度评估：
- LOW: 单表标量列查询，简单WHERE条件
- MEDIUM: 涉及1-2个JSONB字段的路径查询
- HIGH: 多个JSONB字段展开、复杂聚合、多层嵌套

请分析用户查询并返回JSON格式的路由决策。"""

        human_prompt = """用户查询: {query}

请分析这个查询并返回详细的路由决策，包括：
1. 查询类型和复杂度
2. 需要的字段和JSONB路径
3. 筛选条件和聚合操作
4. 置信度和决策理由
5. 建议的查询策略

{format_instructions}"""

        return ChatPromptTemplate.from_messages([
            ("system", system_prompt),
            ("human", human_prompt)
        ])
    
    def analyze_query(self, query: str) -> QueryAnalysis:
        """分析查询并返回路由决策"""
        try:
            result = self.chain.invoke({
                "query": query,
                "format_instructions": self.parser.get_format_instructions()
            })
            return QueryAnalysis(**result)
        except Exception as e:
            # 降级到简单分析
            return self._fallback_analysis(query, str(e))
    
    def _fallback_analysis(self, query: str, error: str) -> QueryAnalysis:
        """LLM失败时的降级分析"""
        query_lower = query.lower()
        
        # 简单关键词匹配
        jsonb_keywords = ["收益率", "持仓", "风险指标", "历史", "配置", "夏普", "beta", "alpha"]
        simple_keywords = ["风险等级", "币种", "基金公司", "nav", "名称"]
        
        has_jsonb = any(keyword in query_lower for keyword in jsonb_keywords)
        has_simple = any(keyword in query_lower for keyword in simple_keywords)
        
        if has_jsonb and has_simple:
            query_type = QueryType.HYBRID
            complexity = QueryComplexity.MEDIUM
        elif has_jsonb:
            query_type = QueryType.COMPLEX
            complexity = QueryComplexity.HIGH
        else:
            query_type = QueryType.SIMPLE
            complexity = QueryComplexity.LOW
        
        return QueryAnalysis(
            query_type=query_type,
            complexity=complexity,
            required_fields=["product_code", "name"],
            jsonb_fields=["risk_json"] if has_jsonb else [],
            filters=["基于关键词推断"],
            aggregations=[],
            confidence=0.6,
            reasoning=f"LLM分析失败，使用关键词匹配降级分析。错误: {error}",
            suggested_strategy="使用标准查询模板"
        )

class QueryOptimizer:
    """基于路由结果的查询优化器"""
    
    def __init__(self):
        self.optimization_strategies = {
            QueryType.SIMPLE: self._optimize_simple_query,
            QueryType.COMPLEX: self._optimize_complex_query,
            QueryType.HYBRID: self._optimize_hybrid_query,
            QueryType.AGGREGATION: self._optimize_aggregation_query,
            QueryType.ANALYTICAL: self._optimize_analytical_query
        }
    
    def optimize_query_strategy(self, analysis: QueryAnalysis) -> Dict[str, Any]:
        """根据分析结果优化查询策略"""
        optimizer = self.optimization_strategies.get(analysis.query_type)
        if optimizer:
            return optimizer(analysis)
        else:
            return self._default_optimization(analysis)
    
    def _optimize_simple_query(self, analysis: QueryAnalysis) -> Dict[str, Any]:
        """优化简单查询"""
        return {
            "strategy": "direct_scalar",
            "use_indexes": ["risk_level", "currency", "allow_buy"],
            "limit_early": True,
            "avoid_jsonb": True,
            "estimated_performance": "fast"
        }
    
    def _optimize_complex_query(self, analysis: QueryAnalysis) -> Dict[str, Any]:
        """优化复杂JSONB查询"""
        return {
            "strategy": "jsonb_focused",
            "use_gin_indexes": analysis.jsonb_fields,
            "lateral_joins": True,
            "filter_before_expand": True,
            "estimated_performance": "medium"
        }
    
    def _optimize_hybrid_query(self, analysis: QueryAnalysis) -> Dict[str, Any]:
        """优化混合查询"""
        return {
            "strategy": "scalar_first_jsonb_second",
            "filter_order": ["scalar_filters", "jsonb_expansion"],
            "use_both_indexes": True,
            "estimated_performance": "medium"
        }
    
    def _optimize_aggregation_query(self, analysis: QueryAnalysis) -> Dict[str, Any]:
        """优化聚合查询"""
        return {
            "strategy": "aggregation_optimized",
            "group_early": True,
            "use_partial_indexes": True,
            "estimated_performance": "slow"
        }
    
    def _optimize_analytical_query(self, analysis: QueryAnalysis) -> Dict[str, Any]:
        """优化分析查询"""
        return {
            "strategy": "analytical_complex",
            "consider_materialized_views": True,
            "parallel_processing": True,
            "estimated_performance": "very_slow"
        }
    
    def _default_optimization(self, analysis: QueryAnalysis) -> Dict[str, Any]:
        """默认优化策略"""
        return {
            "strategy": "default",
            "estimated_performance": "unknown"
        }

def demo_query_router():
    """演示查询路由器功能"""
    print("🎯 LLM 查询路由器演示")
    print("=" * 50)
    
    # 初始化路由器
    router = LLMQueryRouter()
    optimizer = QueryOptimizer()
    
    # 测试查询列表
    test_queries = [
        "找出风险等级为5的美元基金",
        "哪些基金的1年收益率超过40%？",
        "BlackRock有多少只基金？平均风险等级是多少？",
        "找出持有苹果公司股票的基金",
        "显示所有基金的资产配置分布情况",
        "分析过去一年表现最好的基金的共同特征",
        "找出风险等级3-4且持有中国股票的HSBC基金的1年收益率"
    ]
    
    for i, query in enumerate(test_queries, 1):
        print(f"\n📝 测试查询 {i}: {query}")
        print("-" * 50)
        
        start_time = time.time()
        
        # 分析查询
        analysis = router.analyze_query(query)
        
        # 优化策略
        optimization = optimizer.optimize_query_strategy(analysis)
        
        analysis_time = time.time() - start_time
        
        # 显示结果
        print(f"🎯 查询类型: {analysis.query_type.value}")
        print(f"📊 复杂度: {analysis.complexity.value}")
        print(f"🔍 涉及字段: {', '.join(analysis.required_fields)}")
        if analysis.jsonb_fields:
            print(f"📋 JSONB字段: {', '.join(analysis.jsonb_fields)}")
        print(f"🎲 置信度: {analysis.confidence:.2f}")
        print(f"💡 策略: {optimization['strategy']}")
        print(f"⚡ 预期性能: {optimization['estimated_performance']}")
        print(f"⏱️  分析耗时: {analysis_time:.3f}s")
        print(f"📝 理由: {analysis.reasoning}")

if __name__ == "__main__":
    import os
    
    # 检查API Key
    if not os.getenv("OPENAI_API_KEY"):
        print("❌ 请设置 OPENAI_API_KEY 环境变量")
        exit(1)
    
    demo_query_router()
