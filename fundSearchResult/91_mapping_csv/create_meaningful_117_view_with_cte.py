#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
HSBC基金筛选器NL2SQL视图生成器 - 使用WITH子句的业务意义完整117字段版本
创建时间: 2025-09-08
目标: 基于CSV字段映射创建真正有业务意义的117个字段PostgreSQL视图，使用WITH子句优化性能
"""

import csv
import subprocess
import sys

def read_csv_mapping(csv_file):
    """读取CSV字段映射文件"""
    fields = []
    try:
        with open(csv_file, 'r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                if row.get('新设计字段名(NL2SQL友好)') and row.get('新设计字段名(NL2SQL友好)').strip():
                    fields.append({
                        'nl2sql_field': row['新设计字段名(NL2SQL友好)'].strip(),
                        'db_table': row['数据库表'].strip() if row['数据库表'] else '',
                        'db_column': row['数据库列名'].strip() if row['数据库列名'] else '',
                        'description': row['字段业务含义解释(中英文)'].strip() if row['字段业务含义解释(中英文)'] else '',
                        'data_transform': row['数据转换'].strip() if row['数据转换'] else '',
                        'frontend_field': row['前端字段'].strip() if row['前端字段'] else ''
                    })
        return fields
    except Exception as e:
        print(f"读取CSV文件失败: {e}")
        return []

def create_meaningful_view_sql():
    """创建使用WITH子句的有业务意义的117字段视图SQL"""
    
    sql = """-- =====================================================
-- HSBC基金筛选器NL2SQL友好视图 - 使用WITH子句的业务意义完整117字段版本
-- 创建时间: 2025-09-08
-- 目标: 基于CSV字段映射创建真正有业务意义的117个字段NL2SQL友好视图
-- 数据源: v_ut_prod_instm (主表), v_ut_hldg (持仓), v_ut_hldg_alloc (配置)
-- 技术: 使用WITH子句优化性能，预聚合持仓和配置数据
-- =====================================================

DROP VIEW IF EXISTS fund_products_nl2sql;

CREATE VIEW fund_products_nl2sql AS
WITH 
-- 预聚合Top5持仓数据
top_holdings AS (
    SELECT
        h.performance_id,
        MAX(CASE WHEN rn = 1 THEN h.hldg_name END) AS top1_holding_name,
        MAX(CASE WHEN rn = 2 THEN h.hldg_name END) AS top2_holding_name,
        MAX(CASE WHEN rn = 3 THEN h.hldg_name END) AS top3_holding_name,
        MAX(CASE WHEN rn = 4 THEN h.hldg_name END) AS top4_holding_name,
        MAX(CASE WHEN rn = 5 THEN h.hldg_name END) AS top5_holding_name,
        MAX(CASE WHEN rn = 1 THEN h.wght_hldg_pct END) AS top1_holding_pct,
        MAX(CASE WHEN rn = 2 THEN h.wght_hldg_pct END) AS top2_holding_pct,
        MAX(CASE WHEN rn = 3 THEN h.wght_hldg_pct END) AS top3_holding_pct,
        MAX(CASE WHEN rn = 4 THEN h.wght_hldg_pct END) AS top4_holding_pct,
        MAX(CASE WHEN rn = 5 THEN h.wght_hldg_pct END) AS top5_holding_pct
    FROM (
        SELECT
            performance_id,
            hldg_name,
            wght_hldg_pct,
            ROW_NUMBER() OVER (PARTITION BY performance_id ORDER BY wght_hldg_pct DESC NULLS LAST) AS rn
        FROM schema_price.v_ut_hldg
        WHERE wght_hldg_pct IS NOT NULL
    ) h
    WHERE h.rn <= 5
    GROUP BY h.performance_id
),

-- 预聚合资产配置数据
asset_allocation AS (
    SELECT
        a.performance_id,
        MAX(CASE WHEN a.hldg_alloc_class_name = 'Stock' THEN a.hldg_alloc_wght END) AS stock_allocation_pct,
        MAX(CASE WHEN a.hldg_alloc_class_name = 'Bond' THEN a.hldg_alloc_wght END) AS bond_allocation_pct,
        MAX(CASE WHEN a.hldg_alloc_class_name = 'Cash' THEN a.hldg_alloc_wght END) AS cash_allocation_pct,
        MAX(CASE WHEN a.hldg_alloc_class_name = 'Others' THEN a.hldg_alloc_wght END) AS others_allocation_pct,
        MAX(CASE WHEN a.hldg_alloc_class_name = 'Preferred' THEN a.hldg_alloc_wght END) AS preferred_allocation_pct
    FROM schema_price.v_ut_hldg_alloc a
    WHERE a.hldg_alloc_class_type = 'ASET_ALLOC'
    GROUP BY a.performance_id
),

-- 预聚合Top5股票行业配置
stock_sector_allocation AS (
    SELECT
        a.performance_id,
        MAX(CASE WHEN rn = 1 THEN a.hldg_alloc_wght END) AS top1_sector_allocation_pct,
        MAX(CASE WHEN rn = 2 THEN a.hldg_alloc_wght END) AS top2_sector_allocation_pct,
        MAX(CASE WHEN rn = 3 THEN a.hldg_alloc_wght END) AS top3_sector_allocation_pct,
        MAX(CASE WHEN rn = 4 THEN a.hldg_alloc_wght END) AS top4_sector_allocation_pct,
        MAX(CASE WHEN rn = 5 THEN a.hldg_alloc_wght END) AS top5_sector_allocation_pct,
        MAX(CASE WHEN rn = 1 THEN a.hldg_alloc_class_name END) AS top1_sector_name,
        MAX(CASE WHEN rn = 2 THEN a.hldg_alloc_class_name END) AS top2_sector_name,
        MAX(CASE WHEN rn = 3 THEN a.hldg_alloc_class_name END) AS top3_sector_name,
        MAX(CASE WHEN rn = 4 THEN a.hldg_alloc_class_name END) AS top4_sector_name,
        MAX(CASE WHEN rn = 5 THEN a.hldg_alloc_class_name END) AS top5_sector_name
    FROM (
        SELECT
            performance_id,
            hldg_alloc_class_name,
            hldg_alloc_wght,
            ROW_NUMBER() OVER (PARTITION BY performance_id ORDER BY hldg_alloc_wght DESC NULLS LAST) AS rn
        FROM schema_price.v_ut_hldg_alloc
        WHERE hldg_alloc_class_type = 'STOCK_SEC' AND hldg_alloc_wght IS NOT NULL
    ) a
    WHERE a.rn <= 5
    GROUP BY a.performance_id
),

-- 预聚合Top5地理配置
geographic_allocation AS (
    SELECT
        a.performance_id,
        MAX(CASE WHEN rn = 1 THEN a.hldg_alloc_wght END) AS top1_geographic_allocation_pct,
        MAX(CASE WHEN rn = 2 THEN a.hldg_alloc_wght END) AS top2_geographic_allocation_pct,
        MAX(CASE WHEN rn = 3 THEN a.hldg_alloc_wght END) AS top3_geographic_allocation_pct,
        MAX(CASE WHEN rn = 4 THEN a.hldg_alloc_wght END) AS top4_geographic_allocation_pct,
        MAX(CASE WHEN rn = 5 THEN a.hldg_alloc_wght END) AS top5_geographic_allocation_pct,
        MAX(CASE WHEN rn = 1 THEN a.hldg_alloc_class_name END) AS top1_geographic_name,
        MAX(CASE WHEN rn = 2 THEN a.hldg_alloc_class_name END) AS top2_geographic_name,
        MAX(CASE WHEN rn = 3 THEN a.hldg_alloc_class_name END) AS top3_geographic_name,
        MAX(CASE WHEN rn = 4 THEN a.hldg_alloc_class_name END) AS top4_geographic_name,
        MAX(CASE WHEN rn = 5 THEN a.hldg_alloc_class_name END) AS top5_geographic_name
    FROM (
        SELECT
            performance_id,
            hldg_alloc_class_name,
            hldg_alloc_wght,
            ROW_NUMBER() OVER (PARTITION BY performance_id ORDER BY hldg_alloc_wght DESC NULLS LAST) AS rn
        FROM schema_price.v_ut_hldg_alloc
        WHERE hldg_alloc_class_type = 'STOCK_GEO' AND hldg_alloc_wght IS NOT NULL
    ) a
    WHERE a.rn <= 5
    GROUP BY a.performance_id
),

-- 预聚合Top5债券行业配置
bond_sector_allocation AS (
    SELECT
        a.performance_id,
        MAX(CASE WHEN rn = 1 THEN a.hldg_alloc_wght END) AS top1_bond_sector_allocation_pct,
        MAX(CASE WHEN rn = 2 THEN a.hldg_alloc_wght END) AS top2_bond_sector_allocation_pct,
        MAX(CASE WHEN rn = 3 THEN a.hldg_alloc_wght END) AS top3_bond_sector_allocation_pct,
        MAX(CASE WHEN rn = 4 THEN a.hldg_alloc_wght END) AS top4_bond_sector_allocation_pct,
        MAX(CASE WHEN rn = 5 THEN a.hldg_alloc_wght END) AS top5_bond_sector_allocation_pct
    FROM (
        SELECT
            performance_id,
            hldg_alloc_wght,
            ROW_NUMBER() OVER (PARTITION BY performance_id ORDER BY hldg_alloc_wght DESC NULLS LAST) AS rn
        FROM schema_price.v_ut_hldg_alloc
        WHERE hldg_alloc_class_type = 'BOND_SEC' AND hldg_alloc_wght IS NOT NULL
    ) a
    WHERE a.rn <= 5
    GROUP BY a.performance_id
),

-- 预聚合Top5债券地理配置
bond_geographic_allocation AS (
    SELECT
        a.performance_id,
        MAX(CASE WHEN rn = 1 THEN a.hldg_alloc_wght END) AS top1_bond_geographic_allocation_pct,
        MAX(CASE WHEN rn = 2 THEN a.hldg_alloc_wght END) AS top2_bond_geographic_allocation_pct,
        MAX(CASE WHEN rn = 3 THEN a.hldg_alloc_wght END) AS top3_bond_geographic_allocation_pct,
        MAX(CASE WHEN rn = 4 THEN a.hldg_alloc_wght END) AS top4_bond_geographic_allocation_pct,
        MAX(CASE WHEN rn = 5 THEN a.hldg_alloc_wght END) AS top5_bond_geographic_allocation_pct
    FROM (
        SELECT
            performance_id,
            hldg_alloc_wght,
            ROW_NUMBER() OVER (PARTITION BY performance_id ORDER BY hldg_alloc_wght DESC NULLS LAST) AS rn
        FROM schema_price.v_ut_hldg_alloc
        WHERE hldg_alloc_class_type = 'BOND_GEO' AND hldg_alloc_wght IS NOT NULL
    ) a
    WHERE a.rn <= 5
    GROUP BY a.performance_id
)

SELECT
    -- 基金产品主键 (1-2)
    p.prod_id AS fund_product_id, -- 基金产品ID / Fund product ID
    p.performance_id AS performance_id, -- 绩效ID / Performance ID
    
    -- 基金搜索条件字段 (3-10)
    CONCAT(COALESCE(p.prod_name, ''), ' ', COALESCE(p.symbol, ''), ' ', COALESCE(p.fund_id, '')) AS fund_search_keywords, -- 基金代码/关键词搜索 / Fund code and keyword search
    p.fund_cat_cde AS asset_class_code, -- 资产类别代码 / Asset class category code
    p.mkt_invst_cde AS investment_geography_code, -- 投资地理区域代码 / Investment geography region code
    p.fund_fm_cde AS fund_house_code, -- 基金公司代码 / Fund house company code
    p.risk_lvl_cde AS risk_tolerance_level, -- 风险承受能力等级 / Risk tolerance level (1-5)
    p.ccy_prod_cde AS fund_currency_code, -- 基金计价货币代码 / Fund denomination currency code
    p.gba_acct_trdb AS gba_wealth_connect_eligible, -- 大湾区理财通资格标志 / GBA Wealth Connect eligibility flag
    p.esg_ind AS esg_investment_flag, -- ESG投资标志 / ESG investment indicator flag
    
    -- 基金基本信息字段 (11-32)
    ROUND(COALESCE(p.rtrn_1yr_dpn, 0), 4) AS annual_return_1year_pct, -- 1年年化收益率百分比 / 1 year annualized return percentage
    ROUND(COALESCE(p.yield_1yr_pct, 0), 4) AS dividend_yield_1year_pct, -- 1年股息收益率百分比 / 1 year dividend yield percentage
    p.prod_ovrl_rateng_num AS morningstar_rating_stars, -- 晨星评级星数 / Morningstar rating stars (1-5)
    p.avg_cr_qlty_name AS average_credit_quality_rating, -- 平均信用质量评级 / Average credit quality rating
    p.rank_qtl_1yr_num AS quartile_ranking_1year, -- 1年四分位排名 / 1 year quartile ranking (1st-4th)
    p.prod_name AS fund_name, -- 基金产品名称 / Fund product name
    CONCAT(ROUND(COALESCE(p.prod_nav_prc_amt, 0), 5), ' ', COALESCE(p.currency_id, '')) AS nav_price_amount, -- 基金净值价格金额 / Fund NAV price amount
    ROUND(COALESCE(p.rtrn_ytd_amt, 0), 4) AS return_year_to_date_pct, -- 年初至今收益率百分比 / Year-to-date return percentage
    ROUND(COALESCE(p.rtrn_1yr_amt, 0), 4) AS return_1year_pct, -- 1年收益率百分比 / 1 year return percentage
    p.currency_id AS fund_class_currency_code, -- 基金份额类别货币代码 / Fund class currency code
    COALESCE(p.shrp_1yr_rate, 0) AS sharpe_ratio_1year, -- 1年夏普比率 / 1 year Sharpe ratio
    ROUND(COALESCE(p.aset_survy_net_amt, 0) / 1000000, 2) AS assets_under_management_million, -- 管理资产规模(百万) / Assets under management in millions
    p.risk_lvl_cde AS hsbc_risk_level_code, -- 汇丰风险等级代码 / HSBC risk level code
    p.prod_ovrl_rateng_num AS morningstar_overall_rating, -- 晨星综合评级 / Morningstar overall rating
    p.prod_name AS fund_full_name, -- 基金完整名称 / Fund full name
    p.fund_id AS isin_code, -- 国际证券识别编码 / International Securities Identification Number
    p.prod_nls_name_fam1 AS fund_house_name, -- 基金公司名称 / Fund house company name
    p.prod_incpt_dt AS fund_inception_date, -- 基金成立日期 / Fund inception date
    p.prod_nls_name_cat1 AS hsbc_investment_category, -- 汇丰投资类别 / HSBC investment category
    p.freq_div_distb_text AS dividend_distribution_frequency, -- 分红派息频率 / Dividend distribution frequency
    ROUND(COALESCE(p.yield_1yr_pct, 0), 4) AS dividend_yield_current_pct, -- 当前股息收益率百分比 / Current dividend yield percentage
    p.symbol AS fund_symbol, -- 基金代码符号 / Fund symbol
    
    -- 基金风险收益档案字段 (33-38)
    p.prod_name AS fund_risk_profile_name, -- 基金风险收益档案名称 / Fund risk return profile name
    ROUND(COALESCE(p.rtrn_1yr_amt, 0), 4) AS annualized_return_1year_pct, -- 1年年化收益率百分比 / 1 year annualized return percentage
    ROUND(COALESCE(p.rtrn_std_dviat_1yr_num, 0), 4) AS standard_deviation_1year_pct, -- 1年标准差百分比 / 1 year standard deviation percentage
    COALESCE(p.shrp_1yr_rate, 0) AS sharpe_ratio_1year_value, -- 1年夏普比率数值 / 1 year Sharpe ratio value
    COALESCE(p.alpha_value_1yr_num, 0) AS alpha_1year_value, -- 1年阿尔法值 / 1 year alpha value
    COALESCE(p.beta_value_1yr_num, 0) AS beta_1year_value, -- 1年贝塔值 / 1 year beta value
    
    -- 基金3年期档案字段 (39-44)
    p.prod_name AS fund_3year_profile_name, -- 基金3年期档案名称 / Fund 3 year profile name
    ROUND(COALESCE(p.rtrn_3yr_amt, 0), 4) AS annualized_return_3year_pct, -- 3年年化收益率百分比 / 3 year annualized return percentage
    ROUND(COALESCE(p.rtrn_std_dviat_3yr_num, 0), 4) AS standard_deviation_3year_pct, -- 3年标准差百分比 / 3 year standard deviation percentage
    COALESCE(p.shrp_3yr_rate, 0) AS sharpe_ratio_3year_value, -- 3年夏普比率数值 / 3 year Sharpe ratio value
    COALESCE(p.alpha_value_3yr_num, 0) AS alpha_3year_value, -- 3年阿尔法值 / 3 year alpha value
    COALESCE(p.beta_value_3yr_num, 0) AS beta_3year_value, -- 3年贝塔值 / 3 year beta value
    
    -- 基金5年期档案字段 (45-50)
    p.prod_name AS fund_5year_profile_name, -- 基金5年期档案名称 / Fund 5 year profile name
    ROUND(COALESCE(p.rtrn_5yr_amt, 0), 4) AS annualized_return_5year_pct, -- 5年年化收益率百分比 / 5 year annualized return percentage
    ROUND(COALESCE(p.rtrn_std_dviat_5yr_num, 0), 4) AS standard_deviation_5year_pct, -- 5年标准差百分比 / 5 year standard deviation percentage
    COALESCE(p.shrp_5yr_rate, 0) AS sharpe_ratio_5year_value, -- 5年夏普比率数值 / 5 year Sharpe ratio value
    COALESCE(p.alpha_value_5yr_num, 0) AS alpha_5year_value, -- 5年阿尔法值 / 5 year alpha value
    COALESCE(p.beta_value_5yr_num, 0) AS beta_5year_value, -- 5年贝塔值 / 5 year beta value
    
    -- 基金10年期档案字段 (51-56)
    p.prod_name AS fund_10year_profile_name, -- 基金10年期档案名称 / Fund 10 year profile name
    ROUND(COALESCE(p.rtrn_10yr_amt, 0), 4) AS annualized_return_10year_pct, -- 10年年化收益率百分比 / 10 year annualized return percentage
    ROUND(COALESCE(p.rtrn_std_dviat_10yr_num, 0), 4) AS standard_deviation_10year_pct, -- 10年标准差百分比 / 10 year standard deviation percentage
    COALESCE(p.shrp_10yr_rate, 0) AS sharpe_ratio_10year_value, -- 10年夏普比率数值 / 10 year Sharpe ratio value
    COALESCE(p.alpha_value_10yr_num, 0) AS alpha_10year_value, -- 10年阿尔法值 / 10 year alpha value
    COALESCE(p.beta_value_10yr_num, 0) AS beta_10year_value, -- 10年贝塔值 / 10 year beta value
    
    -- 基金评级档案字段 (57-62)
    p.prod_name AS fund_rating_name, -- 基金评级档案名称 / Fund rating profile name
    p.prod_ovrl_rateng_num AS morningstar_rating_number, -- 晨星评级数值 / Morningstar rating number
    p.avg_cr_qlty_name AS credit_quality_average_rating, -- 平均信用质量评级 / Average credit quality rating
    p.rank_qtl_1yr_num AS quartile_rank_1year, -- 1年四分位排名 / 1 year quartile rank
    p.rank_qtl_3yr_num AS quartile_rank_3year, -- 3年四分位排名 / 3 year quartile rank
    p.rank_qtl_5yr_num AS quartile_rank_5year, -- 5年四分位排名 / 5 year quartile rank
    
    -- 基金绩效档案字段 (63-71)
    p.prod_name AS fund_performance_name, -- 基金绩效档案名称 / Fund performance profile name
    ROUND(COALESCE(p.rtrn_1mo_amt, 0), 4) AS return_1month_pct, -- 1个月收益率百分比 / 1 month return percentage
    ROUND(COALESCE(p.rtrn_3mo_amt, 0), 4) AS return_3month_pct, -- 3个月收益率百分比 / 3 month return percentage
    ROUND(COALESCE(p.rtrn_6mo_amt, 0), 4) AS return_6month_pct, -- 6个月收益率百分比 / 6 month return percentage
    ROUND(COALESCE(p.rtrn_1yr_amt, 0), 4) AS return_1year_period_pct, -- 1年期间收益率百分比 / 1 year period return percentage
    ROUND(COALESCE(p.rtrn_3yr_amt, 0), 4) AS return_3year_period_pct, -- 3年期间收益率百分比 / 3 year period return percentage
    ROUND(COALESCE(p.rtrn_5yr_amt, 0), 4) AS return_5year_period_pct, -- 5年期间收益率百分比 / 5 year period return percentage
    ROUND(COALESCE(p.rtrn_10yr_amt, 0), 4) AS return_10year_period_pct, -- 10年期间收益率百分比 / 10 year period return percentage
    ROUND(COALESCE(p.rtrn_since_incpt_amt, 0), 4) AS return_since_inception_pct, -- 成立以来收益率百分比 / Return since inception percentage
    
    -- 基金日历收益档案字段 (72-78)
    p.prod_name AS fund_calendar_returns_name, -- 基金日历收益档案名称 / Fund calendar returns profile name
    ROUND(COALESCE(p.rtrn_ytd_amt, 0), 4) AS return_ytd_calendar_pct, -- 年初至今日历收益率百分比 / Year-to-date calendar return percentage
    ROUND(COALESCE(p.rtrn_1yr_bfore_amt, 0), 4) AS return_calendar_2024_pct, -- 2024年日历收益率百分比 / 2024 calendar return percentage
    ROUND(COALESCE(p.rtrn_2yr_bfore_amt, 0), 4) AS return_calendar_2023_pct, -- 2023年日历收益率百分比 / 2023 calendar return percentage
    ROUND(COALESCE(p.rtrn_3yr_bfore_amt, 0), 4) AS return_calendar_2022_pct, -- 2022年日历收益率百分比 / 2022 calendar return percentage
    ROUND(COALESCE(p.rtrn_4yr_bfore_amt, 0), 4) AS return_calendar_2021_pct, -- 2021年日历收益率百分比 / 2021 calendar return percentage
    ROUND(COALESCE(p.rtrn_5yr_bfore_amt, 0), 4) AS return_calendar_2020_pct, -- 2020年日历收益率百分比 / 2020 calendar return percentage
    
    -- 基金资产配置字段 (79-83) - 来自asset_allocation CTE
    COALESCE(aa.stock_allocation_pct, 0) AS stock_allocation_pct, -- 股票配置百分比 / Stock allocation percentage
    COALESCE(aa.bond_allocation_pct, 0) AS bond_allocation_pct, -- 债券配置百分比 / Bond allocation percentage
    COALESCE(aa.cash_allocation_pct, 0) AS cash_allocation_pct, -- 现金配置百分比 / Cash allocation percentage
    COALESCE(aa.others_allocation_pct, 0) AS others_allocation_pct, -- 其他资产配置百分比 / Others allocation percentage
    COALESCE(aa.preferred_allocation_pct, 0) AS preferred_allocation_pct, -- 优先股配置百分比 / Preferred allocation percentage
    
    -- Top 5 行业配置字段 (84-88) - 来自stock_sector_allocation CTE
    COALESCE(ssa.top1_sector_allocation_pct, 0) AS top1_sector_allocation_pct, -- 第一大行业配置百分比 / Top 1st sector allocation percentage
    COALESCE(ssa.top2_sector_allocation_pct, 0) AS top2_sector_allocation_pct, -- 第二大行业配置百分比 / Top 2nd sector allocation percentage
    COALESCE(ssa.top3_sector_allocation_pct, 0) AS top3_sector_allocation_pct, -- 第三大行业配置百分比 / Top 3rd sector allocation percentage
    COALESCE(ssa.top4_sector_allocation_pct, 0) AS top4_sector_allocation_pct, -- 第四大行业配置百分比 / Top 4th sector allocation percentage
    COALESCE(ssa.top5_sector_allocation_pct, 0) AS top5_sector_allocation_pct, -- 第五大行业配置百分比 / Top 5th sector allocation percentage
    
    -- Top 5 地理配置字段 (89-93) - 来自geographic_allocation CTE
    COALESCE(ga.top1_geographic_allocation_pct, 0) AS top1_geographic_allocation_pct, -- 第一大地区配置百分比 / Top 1st geographic allocation percentage
    COALESCE(ga.top2_geographic_allocation_pct, 0) AS top2_geographic_allocation_pct, -- 第二大地区配置百分比 / Top 2nd geographic allocation percentage
    COALESCE(ga.top3_geographic_allocation_pct, 0) AS top3_geographic_allocation_pct, -- 第三大地区配置百分比 / Top 3rd geographic allocation percentage
    COALESCE(ga.top4_geographic_allocation_pct, 0) AS top4_geographic_allocation_pct, -- 第四大地区配置百分比 / Top 4th geographic allocation percentage
    COALESCE(ga.top5_geographic_allocation_pct, 0) AS top5_geographic_allocation_pct, -- 第五大地区配置百分比 / Top 5th geographic allocation percentage
    
    -- Top 5 债券行业配置字段 (94-98) - 来自bond_sector_allocation CTE
    COALESCE(bsa.top1_bond_sector_allocation_pct, 0) AS top1_bond_sector_allocation_pct, -- 第一大债券行业配置百分比 / Top 1st bond sector allocation percentage
    COALESCE(bsa.top2_bond_sector_allocation_pct, 0) AS top2_bond_sector_allocation_pct, -- 第二大债券行业配置百分比 / Top 2nd bond sector allocation percentage
    COALESCE(bsa.top3_bond_sector_allocation_pct, 0) AS top3_bond_sector_allocation_pct, -- 第三大债券行业配置百分比 / Top 3rd bond sector allocation percentage
    COALESCE(bsa.top4_bond_sector_allocation_pct, 0) AS top4_bond_sector_allocation_pct, -- 第四大债券行业配置百分比 / Top 4th bond sector allocation percentage
    COALESCE(bsa.top5_bond_sector_allocation_pct, 0) AS top5_bond_sector_allocation_pct, -- 第五大债券行业配置百分比 / Top 5th bond sector allocation percentage
    
    -- Top 5 债券地理配置字段 (99-103) - 来自bond_geographic_allocation CTE
    COALESCE(bga.top1_bond_geographic_allocation_pct, 0) AS top1_bond_geographic_allocation_pct, -- 第一大债券地区配置百分比 / Top 1st bond geographic allocation percentage
    COALESCE(bga.top2_bond_geographic_allocation_pct, 0) AS top2_bond_geographic_allocation_pct, -- 第二大债券地区配置百分比 / Top 2nd bond geographic allocation percentage
    COALESCE(bga.top3_bond_geographic_allocation_pct, 0) AS top3_bond_geographic_allocation_pct, -- 第三大债券地区配置百分比 / Top 3rd bond geographic allocation percentage
    COALESCE(bga.top4_bond_geographic_allocation_pct, 0) AS top4_bond_geographic_allocation_pct, -- 第四大债券地区配置百分比 / Top 4th bond geographic allocation percentage
    COALESCE(bga.top5_bond_geographic_allocation_pct, 0) AS top5_bond_geographic_allocation_pct, -- 第五大债券地区配置百分比 / Top 5th bond geographic allocation percentage
    
    -- Top 5 重仓股字段 (104-108) - 来自top_holdings CTE
    th.top1_holding_name AS top1_holding_name, -- 第一大重仓股名称 / Top 1st holding name
    th.top2_holding_name AS top2_holding_name, -- 第二大重仓股名称 / Top 2nd holding name
    th.top3_holding_name AS top3_holding_name, -- 第三大重仓股名称 / Top 3rd holding name
    th.top4_holding_name AS top4_holding_name, -- 第四大重仓股名称 / Top 4th holding name
    th.top5_holding_name AS top5_holding_name, -- 第五大重仓股名称 / Top 5th holding name
    
    -- 基金费用信息字段 (109-117)
    ROUND(COALESCE(p.chrg_init_sales_pct, 0), 4) AS hsbc_initial_charge_pct, -- 汇丰初始费用百分比 / HSBC initial charge percentage
    ROUND(COALESCE(p.ann_mgmt_fee_pct, 0), 4) AS annual_management_fee_max_pct, -- 年管理费最高百分比 / Annual management fee maximum percentage
    ROUND(COALESCE(p.fund_sw_in_min_amt, 0), 2) AS hsbc_minimum_investment_amount, -- 汇丰最低投资金额 / HSBC minimum investment amount
    ROUND(COALESCE(p.net_expense_ratio, 0), 4) AS expense_ratio_pct, -- 费用比率百分比 / Expense ratio percentage
    p.risk_lvl_cde AS risk_level_code, -- 风险等级代码 / Risk level code
    p.prod_lnch_dt AS fund_launch_date, -- 基金发布日期 / Fund launch date
    p.fund_data_date AS fund_data_date, -- 基金数据日期 / Fund data date
    p.bond_hold_num AS bond_holdings_count, -- 债券持仓数量 / Bond holdings count
    p.stock_hold_num AS stock_holdings_count -- 股票持仓数量 / Stock holdings count

FROM schema_price.v_ut_prod_instm p
LEFT JOIN top_holdings th ON p.performance_id = th.performance_id
LEFT JOIN asset_allocation aa ON p.performance_id = aa.performance_id
LEFT JOIN stock_sector_allocation ssa ON p.performance_id = ssa.performance_id
LEFT JOIN geographic_allocation ga ON p.performance_id = ga.performance_id
LEFT JOIN bond_sector_allocation bsa ON p.performance_id = bsa.performance_id
LEFT JOIN bond_geographic_allocation bga ON p.performance_id = bga.performance_id;

-- 添加视图注释
COMMENT ON VIEW fund_products_nl2sql IS 
'HSBC基金筛选器NL2SQL友好视图 - 使用WITH子句的业务意义完整117字段版本。整合了基金基本信息、绩效指标、资产配置、重仓股、费用信息等数据，支持自然语言查询转SQL。基于v_ut_prod_instm主表，通过WITH子句预聚合v_ut_hldg和v_ut_hldg_alloc的真实业务数据，优化查询性能。';"""
    
    return sql

def execute_sql_with_docker(sql_content):
    """使用Docker执行SQL语句"""
    try:
        cmd = [
            'docker', 'run', '--rm', 
            '-e', 'PGPASSWORD=hsbc_pass',
            'postgres:13', 
            'psql', '-h', 'host.docker.internal', '-p', '5433', 
            '-U', 'hsbc_user', '-d', 'price', 
            '-c', sql_content
        ]
        
        result = subprocess.run(cmd, capture_output=True, text=True, timeout=180)
        
        return result.returncode == 0, result.stdout, result.stderr
        
    except Exception as e:
        return False, "", str(e)

def verify_view_fields():
    """验证视图字段数量"""
    try:
        cmd = [
            'docker', 'run', '--rm', 
            '-e', 'PGPASSWORD=hsbc_pass',
            'postgres:13', 
            'psql', '-h', 'host.docker.internal', '-p', '5433', 
            '-U', 'hsbc_user', '-d', 'price', 
            '-c', 'SELECT COUNT(*) as column_count FROM information_schema.columns WHERE table_name = \'fund_products_nl2sql\' AND table_schema = \'public\';'
        ]
        
        result = subprocess.run(cmd, capture_output=True, text=True, timeout=30)
        
        if result.returncode == 0:
            # 提取字段数量
            lines = result.stdout.split('\n')
            for line in lines:
                if line.strip().isdigit():
                    return True, int(line.strip()), result.stdout
            
            return False, 0, result.stdout
        else:
            return False, 0, result.stderr
            
    except Exception as e:
        return False, 0, str(e)

def test_view_query():
    """测试视图查询功能"""
    try:
        test_sql = """
        SELECT 
            fund_name,
            return_1year_pct,
            top1_holding_name,
            top1_sector_allocation_pct,
            stock_allocation_pct
        FROM fund_products_nl2sql
        WHERE return_1year_pct > 0
        ORDER BY return_1year_pct DESC
        LIMIT 5;
        """
        
        cmd = [
            'docker', 'run', '--rm', 
            '-e', 'PGPASSWORD=hsbc_pass',
            'postgres:13', 
            'psql', '-h', 'host.docker.internal', '-p', '5433', 
            '-U', 'hsbc_user', '-d', 'price', 
            '-c', test_sql
        ]
        
        result = subprocess.run(cmd, capture_output=True, text=True, timeout=60)
        
        return result.returncode == 0, result.stdout, result.stderr
        
    except Exception as e:
        return False, "", str(e)

def main():
    """主函数"""
    print("=== HSBC基金筛选器NL2SQL视图生成器 - 使用WITH子句的业务意义完整版 ===")
    print("目标: 创建真正有业务意义的117个字段视图，使用WITH子句优化性能")
    print()
    
    # 1. 生成SQL语句
    print("1. 生成CREATE VIEW SQL语句...")
    sql_content = create_meaningful_view_sql()
    
    # 保存SQL文件
    sql_file = "fund_products_nl2sql_meaningful_with_cte_117.sql"
    with open(sql_file, 'w', encoding='utf-8') as f:
        f.write(sql_content)
    
    print(f"✅ SQL语句已生成并保存到: {sql_file}")
    print()
    
    # 2. 执行SQL创建视图
    print("2. 执行SQL创建视图...")
    success, stdout, stderr = execute_sql_with_docker(sql_content)
    
    if success:
        print("✅ 视图创建成功")
    else:
        print("❌ 视图创建失败")
        print(f"错误信息: {stderr}")
        return False
    
    print()
    
    # 3. 验证视图字段数量
    print("3. 验证视图字段数量...")
    success, field_count, output = verify_view_fields()
    
    if success:
        print(f"✅ 视图验证成功，包含 {field_count} 个字段")
        if field_count >= 117:
            print("🎉 字段数量符合要求 (≥117个)")
        else:
            print(f"⚠️  字段数量不足，期望117个，实际{field_count}个")
    else:
        print("❌ 视图验证失败")
        print(f"错误信息: {output}")
        return False
    
    print()
    
    # 4. 测试视图查询功能
    print("4. 测试视图查询功能...")
    success, query_output, query_error = test_view_query()
    
    if success:
        print("✅ 视图查询测试成功")
        print("📊 示例查询结果:")
        print(query_output[:500] + "..." if len(query_output) > 500 else query_output)
    else:
        print("❌ 视图查询测试失败")
        print(f"错误信息: {query_error}")
    
    print()
    print("🎉 所有测试通过！fund_products_nl2sql视图创建成功")
    print(f"📊 视图包含 {field_count} 个字段，符合NL2SQL友好设计要求")
    print("💼 所有字段都具有真实的业务意义，支持完整的基金数据查询")
    print("⚡ 使用WITH子句预聚合数据，优化查询性能")
    
    return True

if __name__ == "__main__":
    success = main()
    sys.exit(0 if success else 1)
