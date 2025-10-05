#!/usr/bin/env python3
"""
智能查询路由器测试脚本
对比基于规则的路由器 vs 基于 LLM 的路由器
"""

import os
import time
import re
from typing import Dict, List
from enum import Enum

# 模拟基于规则的路由器
class RuleBasedRouter:
    """传统的基于规则的查询路由器"""
    
    SIMPLE_PATTERNS = [
        r"风险等级.*[1-5]",
        r"币种.*[A-Z]{3}",
        r"基金公司",
        r"NAV.*[0-9]+",
        r"名称.*包含"
    ]
    
    COMPLEX_PATTERNS = [
        r"收益率.*[0-9]+%",
        r"持仓.*[国家|地区|行业|公司]",
        r"风险指标.*[beta|alpha|夏普]",
        r"历史.*[价格|走势]",
        r"配置.*比例"
    ]
    
    AGGREGATION_PATTERNS = [
        r"统计.*数量",
        r"平均.*[风险|收益]",
        r"按.*分组",
        r"总计.*[基金|资产]",
        r"分布.*情况"
    ]
    
    def route_query(self, query: str) -> Dict[str, any]:
        """基于规则路由查询"""
        start_time = time.time()
        
        query_lower = query.lower()
        
        # 检查聚合模式
        if any(re.search(pattern, query) for pattern in self.AGGREGATION_PATTERNS):
            query_type = "aggregation"
            confidence = 0.8
        # 检查复杂模式
        elif any(re.search(pattern, query) for pattern in self.COMPLEX_PATTERNS):
            query_type = "complex"
            confidence = 0.7
        # 检查简单模式
        elif any(re.search(pattern, query) for pattern in self.SIMPLE_PATTERNS):
            query_type = "simple"
            confidence = 0.6
        else:
            query_type = "hybrid"
            confidence = 0.5
        
        analysis_time = time.time() - start_time
        
        return {
            "query_type": query_type,
            "confidence": confidence,
            "reasoning": f"基于正则表达式匹配: {query_type}",
            "analysis_time": analysis_time,
            "method": "rule_based"
        }

def compare_routers():
    """对比两种路由器的效果"""
    
    # 检查环境变量
    if not os.getenv("OPENAI_API_KEY"):
        print("⚠️  OPENAI_API_KEY 未设置，仅测试规则路由器")
        test_rule_router_only()
        return
    
    try:
        from llm_query_router import LLMQueryRouter
        llm_router = LLMQueryRouter(model="gpt-3.5-turbo")
        print("✅ LLM 路由器初始化成功")
    except Exception as e:
        print(f"❌ LLM 路由器初始化失败: {e}")
        test_rule_router_only()
        return
    
    rule_router = RuleBasedRouter()
    
    # 测试查询集合
    test_queries = [
        # 简单查询
        "找出风险等级为5的美元基金",
        "显示BlackRock的所有基金名称",
        "NAV大于100的基金有哪些？",
        
        # 复杂查询
        "哪些基金的1年收益率超过40%？",
        "找出夏普比率大于1.5的基金",
        "显示持有苹果公司股票的基金",
        "分析基金的地区配置比例",
        
        # 混合查询
        "BlackRock管理的、持有美国股票的基金",
        "风险等级3-4且1年收益率>20%的基金",
        "HSBC分类为股票型且持有中国公司的基金",
        
        # 聚合查询
        "按基金公司统计基金数量",
        "计算不同风险等级的平均收益率",
        "统计各币种基金的分布情况",
        
        # 分析查询
        "分析高风险基金的收益率分布特征",
        "比较不同地区配置基金的风险收益关系",
        "研究基金规模与收益率的相关性"
    ]
    
    print(f"\n🔍 对比测试 {len(test_queries)} 个查询")
    print("=" * 80)
    
    results = []
    
    for i, query in enumerate(test_queries, 1):
        print(f"\n📝 测试 {i}: {query}")
        print("-" * 60)
        
        # 规则路由器
        rule_result = rule_router.route_query(query)
        
        # LLM路由器
        try:
            llm_analysis = llm_router.analyze_query(query)
            llm_result = {
                "query_type": llm_analysis.query_type.value,
                "confidence": llm_analysis.confidence,
                "reasoning": llm_analysis.reasoning[:100] + "..." if len(llm_analysis.reasoning) > 100 else llm_analysis.reasoning,
                "analysis_time": 0.5,  # 估算时间
                "method": "llm_based"
            }
        except Exception as e:
            llm_result = {
                "query_type": "error",
                "confidence": 0.0,
                "reasoning": f"LLM分析失败: {str(e)[:50]}...",
                "analysis_time": 0.0,
                "method": "llm_based"
            }
        
        # 显示对比结果
        print(f"🔧 规则路由器:")
        print(f"   类型: {rule_result['query_type']}")
        print(f"   置信度: {rule_result['confidence']:.2f}")
        print(f"   耗时: {rule_result['analysis_time']:.3f}s")
        
        print(f"🤖 LLM路由器:")
        print(f"   类型: {llm_result['query_type']}")
        print(f"   置信度: {llm_result['confidence']:.2f}")
        print(f"   耗时: {llm_result['analysis_time']:.3f}s")
        
        # 判断一致性
        consistency = "✅ 一致" if rule_result['query_type'] == llm_result['query_type'] else "❌ 不一致"
        print(f"🎯 结果: {consistency}")
        
        results.append({
            "query": query,
            "rule_type": rule_result['query_type'],
            "llm_type": llm_result['query_type'],
            "rule_confidence": rule_result['confidence'],
            "llm_confidence": llm_result['confidence'],
            "consistent": rule_result['query_type'] == llm_result['query_type']
        })
        
        # 添加延迟避免API限制
        if i < len(test_queries):
            time.sleep(1)
    
    # 统计分析
    print(f"\n📊 对比统计")
    print("=" * 80)
    
    total_queries = len(results)
    consistent_count = sum(1 for r in results if r['consistent'])
    consistency_rate = consistent_count / total_queries * 100
    
    print(f"总查询数: {total_queries}")
    print(f"一致结果: {consistent_count}")
    print(f"一致率: {consistency_rate:.1f}%")
    
    # 按查询类型统计
    rule_types = {}
    llm_types = {}
    
    for result in results:
        rule_type = result['rule_type']
        llm_type = result['llm_type']
        
        rule_types[rule_type] = rule_types.get(rule_type, 0) + 1
        llm_types[llm_type] = llm_types.get(llm_type, 0) + 1
    
    print(f"\n🔧 规则路由器分类统计:")
    for query_type, count in sorted(rule_types.items()):
        print(f"   {query_type}: {count} 个")
    
    print(f"\n🤖 LLM路由器分类统计:")
    for query_type, count in sorted(llm_types.items()):
        print(f"   {query_type}: {count} 个")
    
    # 置信度分析
    avg_rule_confidence = sum(r['rule_confidence'] for r in results) / total_queries
    avg_llm_confidence = sum(r['llm_confidence'] for r in results if r['llm_confidence'] > 0) / total_queries
    
    print(f"\n📈 置信度分析:")
    print(f"   规则路由器平均置信度: {avg_rule_confidence:.2f}")
    print(f"   LLM路由器平均置信度: {avg_llm_confidence:.2f}")
    
    # 不一致案例分析
    inconsistent_cases = [r for r in results if not r['consistent']]
    if inconsistent_cases:
        print(f"\n❌ 不一致案例分析 ({len(inconsistent_cases)} 个):")
        for case in inconsistent_cases:
            print(f"   查询: {case['query'][:50]}...")
            print(f"   规则: {case['rule_type']} vs LLM: {case['llm_type']}")

def test_rule_router_only():
    """仅测试规则路由器"""
    print("🔧 仅测试规则路由器")
    print("=" * 50)
    
    router = RuleBasedRouter()
    
    test_queries = [
        "找出风险等级为5的美元基金",
        "哪些基金的1年收益率超过40%？",
        "按基金公司统计基金数量",
        "分析高风险基金的收益率分布"
    ]
    
    for i, query in enumerate(test_queries, 1):
        print(f"\n📝 测试 {i}: {query}")
        result = router.route_query(query)
        print(f"   类型: {result['query_type']}")
        print(f"   置信度: {result['confidence']:.2f}")
        print(f"   理由: {result['reasoning']}")

if __name__ == "__main__":
    print("🎯 智能查询路由器对比测试")
    print("=" * 50)
    compare_routers()
