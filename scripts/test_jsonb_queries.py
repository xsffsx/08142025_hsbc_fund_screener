#!/usr/bin/env python3
"""
JSONB 查询示例测试脚本
验证 hsbc_fund_unified 表中 JSONB 列的查询功能
"""

import psycopg2
from psycopg2.extras import RealDictCursor
import json

# 数据库连接配置
DB_CONFIG = {
    'host': 'localhost',
    'port': 5433,
    'database': 'hsbc_fund',
    'user': 'hsbc_user',
    'password': 'hsbc_pass'
}

def execute_query(conn, query, description):
    """执行查询并打印结果"""
    print(f"\n=== {description} ===")
    print(f"SQL: {query}")
    print("结果:")
    
    try:
        with conn.cursor(cursor_factory=RealDictCursor) as cur:
            cur.execute(query)
            results = cur.fetchall()
            
            if not results:
                print("  (无结果)")
                return
            
            for i, row in enumerate(results, 1):
                print(f"  [{i}] {dict(row)}")
                
    except Exception as e:
        print(f"  ✗ 查询错误: {e}")

def main():
    """主函数"""
    print("=== HSBC Fund JSONB 查询测试 ===")
    
    try:
        with psycopg2.connect(**DB_CONFIG) as conn:
            
            # 1. 基础验证：检查数据是否存在
            execute_query(conn, 
                "SELECT product_code, name, risk_level, nav FROM hsbc_fund_unified WHERE product_code = 'U43051'",
                "基础数据验证")
            
            # 2. 1Y 累计收益查询
            execute_query(conn, """
                SELECT 
                    product_code,
                    name,
                    (item->>'totalReturn')::NUMERIC AS return_1y
                FROM hsbc_fund_unified f,
                     LATERAL jsonb_array_elements(f.summary_cumulative->'items') AS item
                WHERE f.product_code = 'U43051' 
                  AND item->>'period' = '1Y'
            """, "1Y 累计收益查询")
            
            # 3. YTD 累计收益查询
            execute_query(conn, """
                SELECT 
                    product_code,
                    name,
                    (item->>'totalReturn')::NUMERIC AS return_ytd
                FROM hsbc_fund_unified f,
                     LATERAL jsonb_array_elements(f.summary_cumulative->'items') AS item
                WHERE f.product_code = 'U43051' 
                  AND item->>'period' = 'YTD'
            """, "YTD 累计收益查询")
            
            # 4. 前十大持仓中的加拿大公司
            execute_query(conn, """
                SELECT 
                    product_code,
                    item->>'securityName' AS security_name,
                    item->>'market' AS market,
                    (item->>'weighting')::NUMERIC AS weighting_pct
                FROM hsbc_fund_unified f,
                     LATERAL jsonb_array_elements(f.top10_holdings->'items') AS item
                WHERE f.product_code = 'U43051' 
                  AND item->>'market' = 'CA'
                ORDER BY (item->>'weighting')::NUMERIC DESC
            """, "前十大持仓中的加拿大公司")
            
            # 5. 地区暴露 - 加拿大权重
            execute_query(conn, """
                SELECT 
                    product_code,
                    allocation->>'methods' AS allocation_method,
                    breakdown->>'name' AS region_name,
                    (breakdown->>'weighting')::NUMERIC AS weighting_pct
                FROM hsbc_fund_unified f,
                     LATERAL jsonb_array_elements(f.holding_allocation) AS allocation,
                     LATERAL jsonb_array_elements(allocation->'breakdowns') AS breakdown
                WHERE f.product_code = 'U43051' 
                  AND allocation->>'methods' = 'regionalExposures'
                  AND breakdown->>'name' = 'CA'
            """, "地区暴露 - 加拿大权重")
            
            # 6. 资产配置分布
            execute_query(conn, """
                SELECT 
                    product_code,
                    breakdown->>'name' AS asset_type,
                    (breakdown->>'weighting')::NUMERIC AS weighting_pct
                FROM hsbc_fund_unified f,
                     LATERAL jsonb_array_elements(f.holding_allocation) AS allocation,
                     LATERAL jsonb_array_elements(allocation->'breakdowns') AS breakdown
                WHERE f.product_code = 'U43051' 
                  AND allocation->>'methods' = 'assetAllocations'
                ORDER BY (breakdown->>'weighting')::NUMERIC DESC
            """, "资产配置分布")
            
            # 7. 1年期风险指标
            execute_query(conn, """
                SELECT 
                    product_code,
                    (risk_item->'yearRisk'->>'year')::INTEGER AS year_period,
                    (risk_item->'yearRisk'->>'beta')::NUMERIC AS beta,
                    (risk_item->'yearRisk'->>'stdDev')::NUMERIC AS std_dev,
                    (risk_item->'yearRisk'->>'alpha')::NUMERIC AS alpha,
                    (risk_item->'yearRisk'->>'sharpeRatio')::NUMERIC AS sharpe_ratio
                FROM hsbc_fund_unified f,
                     LATERAL jsonb_array_elements(f.risk_json) AS risk_item
                WHERE f.product_code = 'U43051' 
                  AND (risk_item->'yearRisk'->>'year')::INTEGER = 1
            """, "1年期风险指标")
            
            # 8. 最新 NAV 价格变化
            execute_query(conn, """
                SELECT 
                    product_code,
                    (price_quote->>'priceQuote')::NUMERIC AS current_nav,
                    price_quote->>'currency' AS currency,
                    (price_quote->>'changeAmount')::NUMERIC AS change_amount,
                    (price_quote->>'changePercent')::NUMERIC AS change_percent
                FROM hsbc_fund_unified f
                WHERE f.product_code = 'U43051'
            """, "最新 NAV 价格变化")
            
            # 9. 历史价格数据点数量
            execute_query(conn, """
                SELECT 
                    product_code,
                    jsonb_array_length(chart_timeseries) AS price_data_points,
                    (chart_timeseries->0->>'date') AS earliest_date,
                    (chart_timeseries->-1->>'date') AS latest_date
                FROM hsbc_fund_unified f
                WHERE f.product_code = 'U43051'
            """, "历史价格数据统计")
            
            # 10. 基金经理信息
            execute_query(conn, """
                SELECT 
                    product_code,
                    mgmt_contact->>'companyName' AS management_company,
                    manager->>'managerName' AS manager_name,
                    manager->>'startDate' AS start_date
                FROM hsbc_fund_unified f,
                     LATERAL jsonb_array_elements(f.mgmt_contact->'mgmtInfos') AS manager
                WHERE f.product_code = 'U43051'
                ORDER BY manager->>'startDate'
            """, "基金经理信息")
            
            # 11. 搜索条件范围验证
            execute_query(conn, """
                SELECT 
                    product_code,
                    criteria->>'criteriaKey' AS criteria_key,
                    (criteria->>'minimum')::NUMERIC AS min_value,
                    (criteria->>'maximum')::NUMERIC AS max_value
                FROM hsbc_fund_unified f,
                     LATERAL jsonb_array_elements(f.search_criteria->'minMaxCriterias') AS criteria
                WHERE f.product_code = 'U43051' 
                  AND criteria->>'criteriaKey' IN ('Y1RTRN', 'BETA', 'EXPR', 'AUM')
                ORDER BY criteria->>'criteriaKey'
            """, "关键搜索条件范围")
            
            # 12. JSONB 键存在性检查
            execute_query(conn, """
                SELECT 
                    product_code,
                    summary_cumulative ? 'items' AS has_cumulative_items,
                    top10_holdings ? 'items' AS has_top10_items,
                    holding_allocation IS NOT NULL AS has_allocation_data,
                    other_fund_classes ? 'assetClasses' AS has_other_classes,
                    jsonb_typeof(chart_timeseries) AS chart_data_type
                FROM hsbc_fund_unified f
                WHERE f.product_code = 'U43051'
            """, "JSONB 数据完整性检查")
            
    except psycopg2.Error as e:
        print(f"✗ 数据库连接错误: {e}")
        return
    
    print("\n=== 测试完成 ===")

if __name__ == '__main__':
    main()
