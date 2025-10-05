#!/usr/bin/env python3
"""
数据概览脚本：显示 hsbc_fund_unified 表的统计信息
"""

import psycopg2
from psycopg2.extras import RealDictCursor

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
    
    try:
        with conn.cursor(cursor_factory=RealDictCursor) as cur:
            cur.execute(query)
            results = cur.fetchall()
            
            if not results:
                print("  (无结果)")
                return
            
            # 打印表头
            if results:
                headers = list(results[0].keys())
                header_line = " | ".join(f"{h:>15}" for h in headers)
                print(f"  {header_line}")
                print(f"  {'-' * len(header_line)}")
                
                # 打印数据行
                for row in results:
                    values = [str(row[h]) if row[h] is not None else 'NULL' for h in headers]
                    data_line = " | ".join(f"{v:>15}" for v in values)
                    print(f"  {data_line}")
                
    except Exception as e:
        print(f"  ✗ 查询错误: {e}")

def main():
    """主函数"""
    print("=== HSBC Fund 数据概览 ===")
    
    try:
        with psycopg2.connect(**DB_CONFIG) as conn:
            
            # 1. 基础统计
            execute_query(conn, """
                SELECT 
                    COUNT(*) as total_products,
                    COUNT(DISTINCT family_name) as unique_families,
                    COUNT(DISTINCT currency) as currencies,
                    COUNT(DISTINCT hsbc_category_name) as hsbc_categories,
                    AVG(risk_level) as avg_risk_level,
                    MIN(nav) as min_nav,
                    MAX(nav) as max_nav,
                    AVG(nav) as avg_nav
                FROM hsbc_fund_unified
            """, "基础统计信息")
            
            # 2. 风险等级分布
            execute_query(conn, """
                SELECT 
                    risk_level,
                    COUNT(*) as fund_count,
                    ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER(), 2) as percentage
                FROM hsbc_fund_unified 
                WHERE risk_level IS NOT NULL
                GROUP BY risk_level 
                ORDER BY risk_level
            """, "风险等级分布")
            
            # 3. 币种分布（前10）
            execute_query(conn, """
                SELECT 
                    currency,
                    COUNT(*) as fund_count,
                    ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER(), 2) as percentage
                FROM hsbc_fund_unified 
                WHERE currency IS NOT NULL
                GROUP BY currency 
                ORDER BY fund_count DESC 
                LIMIT 10
            """, "主要币种分布")
            
            # 4. 基金公司排名（前10）
            execute_query(conn, """
                SELECT 
                    family_name,
                    COUNT(*) as fund_count,
                    ROUND(AVG(risk_level), 2) as avg_risk,
                    ROUND(AVG(nav), 2) as avg_nav
                FROM hsbc_fund_unified 
                WHERE family_name IS NOT NULL
                GROUP BY family_name 
                ORDER BY fund_count DESC 
                LIMIT 10
            """, "基金公司排名")
            
            # 5. HSBC 分类分布
            execute_query(conn, """
                SELECT 
                    hsbc_category_name,
                    COUNT(*) as fund_count,
                    ROUND(AVG(risk_level), 2) as avg_risk
                FROM hsbc_fund_unified 
                WHERE hsbc_category_name IS NOT NULL
                GROUP BY hsbc_category_name 
                ORDER BY fund_count DESC 
                LIMIT 10
            """, "HSBC 分类分布")
            
            # 6. 交易许可统计
            execute_query(conn, """
                SELECT 
                    'allow_buy' as permission_type,
                    SUM(CASE WHEN allow_buy = true THEN 1 ELSE 0 END) as allowed,
                    SUM(CASE WHEN allow_buy = false THEN 1 ELSE 0 END) as not_allowed,
                    COUNT(*) as total
                FROM hsbc_fund_unified
                UNION ALL
                SELECT 
                    'allow_sell' as permission_type,
                    SUM(CASE WHEN allow_sell = true THEN 1 ELSE 0 END) as allowed,
                    SUM(CASE WHEN allow_sell = false THEN 1 ELSE 0 END) as not_allowed,
                    COUNT(*) as total
                FROM hsbc_fund_unified
                UNION ALL
                SELECT 
                    'allow_sw_in' as permission_type,
                    SUM(CASE WHEN allow_sw_in = true THEN 1 ELSE 0 END) as allowed,
                    SUM(CASE WHEN allow_sw_in = false THEN 1 ELSE 0 END) as not_allowed,
                    COUNT(*) as total
                FROM hsbc_fund_unified
            """, "交易许可统计")
            
            # 7. NAV 价格区间分布
            execute_query(conn, """
                SELECT
                    nav_range,
                    COUNT(*) as fund_count
                FROM (
                    SELECT
                        CASE
                            WHEN nav < 10 THEN '< 10'
                            WHEN nav < 50 THEN '10-50'
                            WHEN nav < 100 THEN '50-100'
                            WHEN nav < 500 THEN '100-500'
                            ELSE '≥ 500'
                        END as nav_range
                    FROM hsbc_fund_unified
                    WHERE nav IS NOT NULL
                ) t
                GROUP BY nav_range
                ORDER BY
                    CASE nav_range
                        WHEN '< 10' THEN 1
                        WHEN '10-50' THEN 2
                        WHEN '50-100' THEN 3
                        WHEN '100-500' THEN 4
                        ELSE 5
                    END
            """, "NAV 价格区间分布")
            
            # 8. 数据完整性检查
            execute_query(conn, """
                SELECT 
                    'product_code' as field_name,
                    COUNT(*) as total_records,
                    COUNT(product_code) as non_null_count,
                    ROUND(COUNT(product_code) * 100.0 / COUNT(*), 2) as completeness_pct
                FROM hsbc_fund_unified
                UNION ALL
                SELECT 
                    'name' as field_name,
                    COUNT(*) as total_records,
                    COUNT(name) as non_null_count,
                    ROUND(COUNT(name) * 100.0 / COUNT(*), 2) as completeness_pct
                FROM hsbc_fund_unified
                UNION ALL
                SELECT 
                    'risk_level' as field_name,
                    COUNT(*) as total_records,
                    COUNT(risk_level) as non_null_count,
                    ROUND(COUNT(risk_level) * 100.0 / COUNT(*), 2) as completeness_pct
                FROM hsbc_fund_unified
                UNION ALL
                SELECT 
                    'nav' as field_name,
                    COUNT(*) as total_records,
                    COUNT(nav) as non_null_count,
                    ROUND(COUNT(nav) * 100.0 / COUNT(*), 2) as completeness_pct
                FROM hsbc_fund_unified
                UNION ALL
                SELECT 
                    'currency' as field_name,
                    COUNT(*) as total_records,
                    COUNT(currency) as non_null_count,
                    ROUND(COUNT(currency) * 100.0 / COUNT(*), 2) as completeness_pct
                FROM hsbc_fund_unified
            """, "关键字段数据完整性")
            
    except psycopg2.Error as e:
        print(f"✗ 数据库连接错误: {e}")
        return
    
    print("\n=== 数据概览完成 ===")

if __name__ == '__main__':
    main()
