-- =====================================================
-- Cognos大量测试数据生成脚本
-- 基于现有表结构和业务逻辑生成大量测试数据
-- 创建时间: 2025-01-04 14:35:00
-- 数据库: nl2sql
-- 目标: 为每个核心表生成2000-5000条记录
-- =====================================================

-- 设置字符集和优化参数
SET NAMES utf8mb4;
SET autocommit = 0;
SET unique_checks = 0;
SET foreign_key_checks = 0;

-- =====================================================
-- 1. 扩展代码描述值数据 (约500条记录)
-- =====================================================

-- 清理现有数据
DELETE FROM CDE_DESC_VALUE WHERE CTRY_REC_CDE IN ('HK', 'SG', 'CN', 'US', 'UK');

-- 插入扩展的代码描述数据
INSERT INTO `CDE_DESC_VALUE` (
    `CTRY_REC_CDE`, `GRP_MEMBR_REC_CDE`, `CDV_TYPE_CDE`, `CDV_CDE`, 
    `CDV_DESC`, `CDV_PLL_DESC`, `CDV_SLL_DESC`, `CDV_DISP_SEQ_NUM`
) VALUES 

-- 产品类型代码 (多个国家和机构)
('HK', 'HSBC', 'PROD_TYPE', 'MF', '互惠基金', 'Mutual Fund', 'Mutual Fund', 1),
('HK', 'HSBC', 'PROD_TYPE', 'BD', '债券', 'Bond', 'Bond', 2),
('HK', 'HSBC', 'PROD_TYPE', 'ELI', '股票挂钩投资', 'Equity Linked Investment', 'Equity Linked Investment', 3),
('HK', 'HSBC', 'PROD_TYPE', 'ST', '股票', 'Stock', 'Stock', 4),
('HK', 'HSBC', 'PROD_TYPE', 'FX', '外汇', 'Foreign Exchange', 'Foreign Exchange', 5),
('HK', 'HSBC', 'PROD_TYPE', 'ETF', '交易所交易基金', 'Exchange Traded Fund', 'Exchange Traded Fund', 6),
('HK', 'HSBC', 'PROD_TYPE', 'REIT', '房地产投资信托', 'Real Estate Investment Trust', 'Real Estate Investment Trust', 7),
('HK', 'HSBC', 'PROD_TYPE', 'WARRANT', '认股权证', 'Warrant', 'Warrant', 8),
('HK', 'HSBC', 'PROD_TYPE', 'CBBC', '牛熊证', 'Callable Bull/Bear Contract', 'Callable Bull/Bear Contract', 9),
('HK', 'HSBC', 'PROD_TYPE', 'OPTION', '期权', 'Option', 'Option', 10),

-- 新加坡市场
('SG', 'DBS', 'PROD_TYPE', 'MF', '互惠基金', 'Mutual Fund', 'Mutual Fund', 1),
('SG', 'DBS', 'PROD_TYPE', 'BD', '债券', 'Bond', 'Bond', 2),
('SG', 'DBS', 'PROD_TYPE', 'ST', '股票', 'Stock', 'Stock', 3),
('SG', 'DBS', 'PROD_TYPE', 'ETF', '交易所交易基金', 'Exchange Traded Fund', 'Exchange Traded Fund', 4),
('SG', 'DBS', 'PROD_TYPE', 'REIT', '房地产投资信托', 'Real Estate Investment Trust', 'Real Estate Investment Trust', 5),

-- 中国市场
('CN', 'ICBC', 'PROD_TYPE', 'MF', '公募基金', 'Public Mutual Fund', 'Public Mutual Fund', 1),
('CN', 'ICBC', 'PROD_TYPE', 'BD', '债券', 'Bond', 'Bond', 2),
('CN', 'ICBC', 'PROD_TYPE', 'ST', 'A股', 'A Share', 'A Share', 3),
('CN', 'ICBC', 'PROD_TYPE', 'WMP', '理财产品', 'Wealth Management Product', 'Wealth Management Product', 4),

-- 美国市场
('US', 'JPM', 'PROD_TYPE', 'MF', '共同基金', 'Mutual Fund', 'Mutual Fund', 1),
('US', 'JPM', 'PROD_TYPE', 'ETF', '交易所交易基金', 'Exchange Traded Fund', 'Exchange Traded Fund', 2),
('US', 'JPM', 'PROD_TYPE', 'ST', '股票', 'Stock', 'Stock', 3),
('US', 'JPM', 'PROD_TYPE', 'BD', '债券', 'Bond', 'Bond', 4),

-- 产品状态代码
('HK', 'HSBC', 'PROD_STAT', 'A', '活跃', 'Active', 'Active', 1),
('HK', 'HSBC', 'PROD_STAT', 'S', '暂停', 'Suspended', 'Suspended', 2),
('HK', 'HSBC', 'PROD_STAT', 'M', '到期', 'Matured', 'Matured', 3),
('HK', 'HSBC', 'PROD_STAT', 'T', '终止', 'Terminated', 'Terminated', 4),
('HK', 'HSBC', 'PROD_STAT', 'P', '待发布', 'Pending', 'Pending', 5),
('HK', 'HSBC', 'PROD_STAT', 'C', '已取消', 'Cancelled', 'Cancelled', 6),
('HK', 'HSBC', 'PROD_STAT', 'D', '已退市', 'Delisted', 'Delisted', 7),

-- 风险等级代码
('HK', 'HSBC', 'RISK_LEVEL', '1', '保守型', 'Conservative', 'Conservative', 1),
('HK', 'HSBC', 'RISK_LEVEL', '2', '稳健型', 'Moderate', 'Moderate', 2),
('HK', 'HSBC', 'RISK_LEVEL', '3', '平衡型', 'Balanced', 'Balanced', 3),
('HK', 'HSBC', 'RISK_LEVEL', '4', '积极型', 'Aggressive', 'Aggressive', 4),
('HK', 'HSBC', 'RISK_LEVEL', '5', '激进型', 'Speculative', 'Speculative', 5),

-- 资产类别代码
('HK', 'HSBC', 'ASSET_CLASS', 'EQUITY', '股票', 'Equity', 'Equity', 1),
('HK', 'HSBC', 'ASSET_CLASS', 'BOND', '债券', 'Bond', 'Bond', 2),
('HK', 'HSBC', 'ASSET_CLASS', 'MIXED', '混合', 'Mixed', 'Mixed', 3),
('HK', 'HSBC', 'ASSET_CLASS', 'MONEY', '货币市场', 'Money Market', 'Money Market', 4),
('HK', 'HSBC', 'ASSET_CLASS', 'COMMODITY', '商品', 'Commodity', 'Commodity', 5),
('HK', 'HSBC', 'ASSET_CLASS', 'REAL_ESTATE', '房地产', 'Real Estate', 'Real Estate', 6),
('HK', 'HSBC', 'ASSET_CLASS', 'ALTERNATIVE', '另类投资', 'Alternative Investment', 'Alternative Investment', 7),

-- 货币代码
('HK', 'HSBC', 'CURRENCY', 'HKD', '港币', 'Hong Kong Dollar', 'Hong Kong Dollar', 1),
('HK', 'HSBC', 'CURRENCY', 'USD', '美元', 'US Dollar', 'US Dollar', 2),
('HK', 'HSBC', 'CURRENCY', 'EUR', '欧元', 'Euro', 'Euro', 3),
('HK', 'HSBC', 'CURRENCY', 'GBP', '英镑', 'British Pound', 'British Pound', 4),
('HK', 'HSBC', 'CURRENCY', 'JPY', '日元', 'Japanese Yen', 'Japanese Yen', 5),
('HK', 'HSBC', 'CURRENCY', 'CNY', '人民币', 'Chinese Yuan', 'Chinese Yuan', 6),
('HK', 'HSBC', 'CURRENCY', 'SGD', '新加坡元', 'Singapore Dollar', 'Singapore Dollar', 7),
('HK', 'HSBC', 'CURRENCY', 'AUD', '澳元', 'Australian Dollar', 'Australian Dollar', 8),
('HK', 'HSBC', 'CURRENCY', 'CAD', '加元', 'Canadian Dollar', 'Canadian Dollar', 9),
('HK', 'HSBC', 'CURRENCY', 'CHF', '瑞士法郎', 'Swiss Franc', 'Swiss Franc', 10),

-- 市场投资代码
('HK', 'HSBC', 'MARKET', 'HK', '香港', 'Hong Kong', 'Hong Kong', 1),
('HK', 'HSBC', 'MARKET', 'US', '美国', 'United States', 'United States', 2),
('HK', 'HSBC', 'MARKET', 'EU', '欧洲', 'Europe', 'Europe', 3),
('HK', 'HSBC', 'MARKET', 'JP', '日本', 'Japan', 'Japan', 4),
('HK', 'HSBC', 'MARKET', 'CN', '中国', 'China', 'China', 5),
('HK', 'HSBC', 'MARKET', 'SG', '新加坡', 'Singapore', 'Singapore', 6),
('HK', 'HSBC', 'MARKET', 'AU', '澳洲', 'Australia', 'Australia', 7),
('HK', 'HSBC', 'MARKET', 'EMRG', '新兴市场', 'Emerging Markets', 'Emerging Markets', 8),
('HK', 'HSBC', 'MARKET', 'GLOB', '全球', 'Global', 'Global', 9),
('HK', 'HSBC', 'MARKET', 'ASIA', '亚洲', 'Asia', 'Asia', 10),

-- 行业投资代码
('HK', 'HSBC', 'SECTOR', 'TECH', '科技', 'Technology', 'Technology', 1),
('HK', 'HSBC', 'SECTOR', 'FINL', '金融', 'Financial', 'Financial', 2),
('HK', 'HSBC', 'SECTOR', 'HLTH', '医疗保健', 'Healthcare', 'Healthcare', 3),
('HK', 'HSBC', 'SECTOR', 'CONS', '消费', 'Consumer', 'Consumer', 4),
('HK', 'HSBC', 'SECTOR', 'INDU', '工业', 'Industrial', 'Industrial', 5),
('HK', 'HSBC', 'SECTOR', 'ENRG', '能源', 'Energy', 'Energy', 6),
('HK', 'HSBC', 'SECTOR', 'UTIL', '公用事业', 'Utilities', 'Utilities', 7),
('HK', 'HSBC', 'SECTOR', 'MATL', '材料', 'Materials', 'Materials', 8),
('HK', 'HSBC', 'SECTOR', 'REAL', '房地产', 'Real Estate', 'Real Estate', 9),
('HK', 'HSBC', 'SECTOR', 'TELE', '电信', 'Telecommunications', 'Telecommunications', 10),

-- 产品子类型代码
('HK', 'HSBC', 'PROD_SUBTYPE', 'EQUITY_GROWTH', '股票成长型', 'Equity Growth', 'Equity Growth', 1),
('HK', 'HSBC', 'PROD_SUBTYPE', 'EQUITY_VALUE', '股票价值型', 'Equity Value', 'Equity Value', 2),
('HK', 'HSBC', 'PROD_SUBTYPE', 'EQUITY_BLEND', '股票混合型', 'Equity Blend', 'Equity Blend', 3),
('HK', 'HSBC', 'PROD_SUBTYPE', 'BOND_GOVT', '政府债券', 'Government Bond', 'Government Bond', 4),
('HK', 'HSBC', 'PROD_SUBTYPE', 'BOND_CORP', '企业债券', 'Corporate Bond', 'Corporate Bond', 5),
('HK', 'HSBC', 'PROD_SUBTYPE', 'BOND_HIGH_YIELD', '高收益债券', 'High Yield Bond', 'High Yield Bond', 6),
('HK', 'HSBC', 'PROD_SUBTYPE', 'MIXED_CONSERVATIVE', '保守混合型', 'Conservative Mixed', 'Conservative Mixed', 7),
('HK', 'HSBC', 'PROD_SUBTYPE', 'MIXED_BALANCED', '平衡混合型', 'Balanced Mixed', 'Balanced Mixed', 8),
('HK', 'HSBC', 'PROD_SUBTYPE', 'MIXED_AGGRESSIVE', '积极混合型', 'Aggressive Mixed', 'Aggressive Mixed', 9),
('HK', 'HSBC', 'PROD_SUBTYPE', 'STRUCTURED', '结构性产品', 'Structured Product', 'Structured Product', 10),

-- 基准指数代码
('HK', 'HSBC', 'BENCHMARK', 'HSI', '恒生指数', 'Hang Seng Index', 'Hang Seng Index', 1),
('HK', 'HSBC', 'BENCHMARK', 'HSCEI', '恒生中国企业指数', 'Hang Seng China Enterprises Index', 'Hang Seng China Enterprises Index', 2),
('HK', 'HSBC', 'BENCHMARK', 'SPX', '标普500指数', 'S&P 500 Index', 'S&P 500 Index', 3),
('HK', 'HSBC', 'BENCHMARK', 'NASDAQ', '纳斯达克指数', 'NASDAQ Index', 'NASDAQ Index', 4),
('HK', 'HSBC', 'BENCHMARK', 'MSCI_WORLD', 'MSCI世界指数', 'MSCI World Index', 'MSCI World Index', 5),
('HK', 'HSBC', 'BENCHMARK', 'MSCI_EM', 'MSCI新兴市场指数', 'MSCI Emerging Markets Index', 'MSCI Emerging Markets Index', 6),
('HK', 'HSBC', 'BENCHMARK', 'BLOOMBERG_AGG', '彭博综合债券指数', 'Bloomberg Aggregate Bond Index', 'Bloomberg Aggregate Bond Index', 7),

-- 基金公司代码
('HK', 'HSBC', 'FUND_COMPANY', 'HSBC_AM', '汇丰投资管理', 'HSBC Asset Management', 'HSBC Asset Management', 1),
('HK', 'HSBC', 'FUND_COMPANY', 'BLACKROCK', '贝莱德', 'BlackRock', 'BlackRock', 2),
('HK', 'HSBC', 'FUND_COMPANY', 'VANGUARD', '先锋', 'Vanguard', 'Vanguard', 3),
('HK', 'HSBC', 'FUND_COMPANY', 'FIDELITY', '富达', 'Fidelity', 'Fidelity', 4),
('HK', 'HSBC', 'FUND_COMPANY', 'JPMORGAN', '摩根大通', 'JPMorgan', 'JPMorgan', 5),
('HK', 'HSBC', 'FUND_COMPANY', 'GOLDMAN', '高盛', 'Goldman Sachs', 'Goldman Sachs', 6),
('HK', 'HSBC', 'FUND_COMPANY', 'UBS', '瑞银', 'UBS', 'UBS', 7),
('HK', 'HSBC', 'FUND_COMPANY', 'DEUTSCHE', '德意志银行', 'Deutsche Bank', 'Deutsche Bank', 8),
('HK', 'HSBC', 'FUND_COMPANY', 'CREDIT_SUISSE', '瑞信', 'Credit Suisse', 'Credit Suisse', 9),
('HK', 'HSBC', 'FUND_COMPANY', 'BNP_PARIBAS', '法国巴黎银行', 'BNP Paribas', 'BNP Paribas', 10);

COMMIT;

-- =====================================================
-- 2. 生成大量产品主表数据 (3000条记录)
-- =====================================================

-- 清理现有产品数据
DELETE FROM TB_PROD WHERE PROD_ID >= 1000;

-- 使用存储过程生成大量产品数据
DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateProducts$$

CREATE PROCEDURE GenerateProducts()
BEGIN
    DECLARE i INT DEFAULT 1000;
    DECLARE max_products INT DEFAULT 4000;
    DECLARE prod_type VARCHAR(15);
    DECLARE prod_subtype VARCHAR(30);
    DECLARE asset_class VARCHAR(15);
    DECLARE currency VARCHAR(3);
    DECLARE risk_level CHAR(1);
    DECLARE market VARCHAR(5);
    DECLARE sector VARCHAR(10);
    DECLARE country VARCHAR(2);
    DECLARE institution VARCHAR(4);
    DECLARE prod_status CHAR(1);
    DECLARE launch_date DATE;
    DECLARE maturity_date DATE;
    DECLARE bid_price DECIMAL(21,6);
    DECLARE offer_price DECIMAL(21,6);
    DECLARE market_price DECIMAL(21,6);
    DECLARE nav_price DECIMAL(21,6);

    -- 产品类型数组
    DECLARE prod_types_cursor CURSOR FOR
        SELECT 'MF', 'EQUITY_GROWTH', 'EQUITY', 'HKD', '4', 'HK', 'TECH', 'HK', 'HSBC', 'A'
        UNION ALL SELECT 'MF', 'EQUITY_VALUE', 'EQUITY', 'USD', '3', 'US', 'FINL', 'HK', 'HSBC', 'A'
        UNION ALL SELECT 'MF', 'BOND_GOVT', 'BOND', 'HKD', '2', 'HK', 'FINL', 'HK', 'HSBC', 'A'
        UNION ALL SELECT 'BD', 'BOND_CORP', 'BOND', 'USD', '3', 'US', 'FINL', 'HK', 'HSBC', 'A'
        UNION ALL SELECT 'ETF', 'EQUITY_BLEND', 'EQUITY', 'HKD', '3', 'HK', 'TECH', 'HK', 'HSBC', 'A'
        UNION ALL SELECT 'ST', 'BLUE_CHIP', 'EQUITY', 'HKD', '4', 'HK', 'FINL', 'HK', 'HSBC', 'A'
        UNION ALL SELECT 'ELI', 'STRUCTURED', 'MIXED', 'HKD', '5', 'HK', 'FINL', 'HK', 'HSBC', 'A'
        UNION ALL SELECT 'REIT', 'REAL_ESTATE', 'REAL_ESTATE', 'HKD', '3', 'HK', 'REAL', 'HK', 'HSBC', 'A'
        UNION ALL SELECT 'WARRANT', 'STRUCTURED', 'EQUITY', 'HKD', '5', 'HK', 'TECH', 'HK', 'HSBC', 'A'
        UNION ALL SELECT 'CBBC', 'STRUCTURED', 'EQUITY', 'HKD', '5', 'HK', 'FINL', 'HK', 'HSBC', 'A';

    WHILE i <= max_products DO
        -- 随机选择产品属性
        SET prod_type = ELT(1 + FLOOR(RAND() * 10), 'MF', 'BD', 'ETF', 'ST', 'ELI', 'REIT', 'WARRANT', 'CBBC', 'OPTION', 'FX');

        -- 根据产品类型设置相应属性
        CASE prod_type
            WHEN 'MF' THEN
                SET prod_subtype = ELT(1 + FLOOR(RAND() * 3), 'EQUITY_GROWTH', 'EQUITY_VALUE', 'BOND_GOVT');
                SET asset_class = IF(prod_subtype LIKE 'EQUITY%', 'EQUITY', 'BOND');
                SET risk_level = ELT(1 + FLOOR(RAND() * 4), '2', '3', '4', '5');
            WHEN 'BD' THEN
                SET prod_subtype = ELT(1 + FLOOR(RAND() * 3), 'BOND_GOVT', 'BOND_CORP', 'BOND_HIGH_YIELD');
                SET asset_class = 'BOND';
                SET risk_level = ELT(1 + FLOOR(RAND() * 3), '1', '2', '3');
            WHEN 'ETF' THEN
                SET prod_subtype = ELT(1 + FLOOR(RAND() * 3), 'EQUITY_GROWTH', 'EQUITY_VALUE', 'EQUITY_BLEND');
                SET asset_class = 'EQUITY';
                SET risk_level = ELT(1 + FLOOR(RAND() * 4), '2', '3', '4', '5');
            WHEN 'ST' THEN
                SET prod_subtype = 'BLUE_CHIP';
                SET asset_class = 'EQUITY';
                SET risk_level = ELT(1 + FLOOR(RAND() * 5), '1', '2', '3', '4', '5');
            WHEN 'ELI' THEN
                SET prod_subtype = 'STRUCTURED';
                SET asset_class = 'MIXED';
                SET risk_level = '5';
            WHEN 'REIT' THEN
                SET prod_subtype = 'REAL_ESTATE';
                SET asset_class = 'REAL_ESTATE';
                SET risk_level = ELT(1 + FLOOR(RAND() * 3), '2', '3', '4');
            ELSE
                SET prod_subtype = 'STRUCTURED';
                SET asset_class = 'EQUITY';
                SET risk_level = '5';
        END CASE;

        -- 随机选择其他属性
        SET currency = ELT(1 + FLOOR(RAND() * 5), 'HKD', 'USD', 'EUR', 'GBP', 'JPY');
        SET market = ELT(1 + FLOOR(RAND() * 5), 'HK', 'US', 'EU', 'JP', 'CN');
        SET sector = ELT(1 + FLOOR(RAND() * 10), 'TECH', 'FINL', 'HLTH', 'CONS', 'INDU', 'ENRG', 'UTIL', 'MATL', 'REAL', 'TELE');
        SET country = ELT(1 + FLOOR(RAND() * 4), 'HK', 'SG', 'CN', 'US');
        SET institution = ELT(1 + FLOOR(RAND() * 4), 'HSBC', 'DBS', 'ICBC', 'JPM');
        SET prod_status = ELT(1 + FLOOR(RAND() * 100), 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'S'); -- 90% Active, 10% Suspended

        -- 生成日期
        SET launch_date = DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 3650) DAY); -- 过去10年内
        SET maturity_date = CASE
            WHEN prod_type IN ('BD', 'ELI') THEN DATE_ADD(launch_date, INTERVAL (1 + FLOOR(RAND() * 10)) YEAR)
            WHEN prod_type = 'MF' THEN DATE_ADD(launch_date, INTERVAL (5 + FLOOR(RAND() * 15)) YEAR)
            ELSE NULL
        END;

        -- 生成价格 (根据产品类型)
        CASE prod_type
            WHEN 'MF' THEN
                SET nav_price = 5 + RAND() * 50; -- 5-55
                SET bid_price = nav_price * (0.995 + RAND() * 0.01);
                SET offer_price = nav_price * (1.005 + RAND() * 0.01);
                SET market_price = nav_price;
            WHEN 'BD' THEN
                SET market_price = 95 + RAND() * 10; -- 95-105
                SET bid_price = market_price * (0.998 + RAND() * 0.002);
                SET offer_price = market_price * (1.002 + RAND() * 0.002);
                SET nav_price = market_price;
            WHEN 'ST' THEN
                SET market_price = 1 + RAND() * 500; -- 1-501
                SET bid_price = market_price * (0.999 + RAND() * 0.001);
                SET offer_price = market_price * (1.001 + RAND() * 0.001);
                SET nav_price = market_price;
            ELSE
                SET market_price = 10 + RAND() * 90; -- 10-100
                SET bid_price = market_price * (0.995 + RAND() * 0.005);
                SET offer_price = market_price * (1.005 + RAND() * 0.005);
                SET nav_price = market_price;
        END CASE;

        -- 插入产品记录
        INSERT INTO TB_PROD (
            CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_ID, PROD_TYPE, PROD_TYPE_CDE,
            PROD_SUBTP_CDE, PROD_CDE, PROD_NAME, PROD_PLL_NAME, PROD_SLL_NAME,
            PROD_SHRT_NAME, PROD_DESC, ASET_CLASS_CDE, PROD_STAT_CDE,
            CCY_PROD_CDE, RISK_LVL_CDE, PROD_LNCH_DT, PROD_MTUR_DT,
            MKT_INVST_CDE, SECT_INVST_CDE,
            ALLOW_BUY_PROD_IND, ALLOW_SELL_PROD_IND, ALLOW_BUY_UT_PROD_IND, ALLOW_BUY_AMT_PROD_IND,
            ALLOW_SELL_UT_PROD_IND, ALLOW_SELL_AMT_PROD_IND, ALLOW_SW_IN_PROD_IND, ALLOW_SW_OUT_PROD_IND,
            PROD_BID_PRC_AMT, PROD_OFFR_PRC_AMT, PROD_MKT_PRC_AMT, PROD_NAV_PRC_AMT,
            INCM_CHAR_PROD_IND, CPTL_GURNT_PROD_IND, YIELD_ENHN_PROD_IND, GRWTH_CHAR_PROD_IND,
            TOP_SELL_PROD_IND, TOP_PERFM_PROD_IND, INVST_INIT_MIN_AMT, INVST_INIT_MAX_AMT
        ) VALUES (
            country, institution, i, prod_type, prod_type,
            prod_subtype, CONCAT(country, prod_type, LPAD(i, 6, '0')),
            CONCAT(institution, ' ',
                CASE prod_type
                    WHEN 'MF' THEN '基金'
                    WHEN 'BD' THEN '债券'
                    WHEN 'ETF' THEN 'ETF'
                    WHEN 'ST' THEN '股票'
                    WHEN 'ELI' THEN '结构性产品'
                    WHEN 'REIT' THEN 'REIT'
                    ELSE '金融产品'
                END, ' ', LPAD(i, 4, '0')),
            CONCAT(institution, ' ',
                CASE prod_type
                    WHEN 'MF' THEN 'Fund'
                    WHEN 'BD' THEN 'Bond'
                    WHEN 'ETF' THEN 'ETF'
                    WHEN 'ST' THEN 'Stock'
                    WHEN 'ELI' THEN 'Structured Product'
                    WHEN 'REIT' THEN 'REIT'
                    ELSE 'Financial Product'
                END, ' ', LPAD(i, 4, '0')),
            CONCAT(institution, ' ',
                CASE prod_type
                    WHEN 'MF' THEN 'Fund'
                    WHEN 'BD' THEN 'Bond'
                    WHEN 'ETF' THEN 'ETF'
                    WHEN 'ST' THEN 'Stock'
                    WHEN 'ELI' THEN 'Structured Product'
                    WHEN 'REIT' THEN 'REIT'
                    ELSE 'Financial Product'
                END, ' ', LPAD(i, 4, '0')),
            CONCAT(prod_type, LPAD(i, 4, '0')),
            CONCAT('专业的',
                CASE prod_type
                    WHEN 'MF' THEN '基金投资产品'
                    WHEN 'BD' THEN '债券投资产品'
                    WHEN 'ETF' THEN '交易所交易基金'
                    WHEN 'ST' THEN '股票投资'
                    WHEN 'ELI' THEN '结构性投资产品'
                    WHEN 'REIT' THEN '房地产投资信托'
                    ELSE '金融投资产品'
                END),
            asset_class, prod_status, currency, risk_level, launch_date, maturity_date,
            market, sector,
            'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y',
            bid_price, offer_price, market_price, nav_price,
            IF(prod_type IN ('BD', 'REIT'), 'Y', 'N'),
            IF(prod_type IN ('BD', 'ELI') AND risk_level <= '3', 'Y', 'N'),
            IF(prod_type IN ('ELI', 'WARRANT'), 'Y', 'N'),
            IF(prod_type IN ('MF', 'ETF') AND asset_class = 'EQUITY', 'Y', 'N'),
            IF(RAND() < 0.1, 'Y', 'N'), -- 10% 热销产品
            IF(RAND() < 0.05, 'Y', 'N'), -- 5% 顶级表现产品
            CASE prod_type
                WHEN 'MF' THEN 1000 + FLOOR(RAND() * 9000) -- 1K-10K
                WHEN 'BD' THEN 10000 + FLOOR(RAND() * 90000) -- 10K-100K
                WHEN 'ST' THEN 100 + FLOOR(RAND() * 900) -- 100-1000
                ELSE 5000 + FLOOR(RAND() * 45000) -- 5K-50K
            END,
            CASE prod_type
                WHEN 'MF' THEN 1000000 + FLOOR(RAND() * 9000000) -- 1M-10M
                WHEN 'BD' THEN 5000000 + FLOOR(RAND() * 45000000) -- 5M-50M
                WHEN 'ST' THEN 10000000 -- 10M
                ELSE 2000000 + FLOOR(RAND() * 18000000) -- 2M-20M
            END
        );

        SET i = i + 1;

        -- 每1000条记录提交一次
        IF i % 1000 = 0 THEN
            COMMIT;
        END IF;

    END WHILE;

    COMMIT;
END$$

DELIMITER ;

-- 执行存储过程生成产品数据
CALL GenerateProducts();

-- 删除存储过程
DROP PROCEDURE GenerateProducts;

-- =====================================================
-- 3. 生成产品扩展字段数据 (约8000条记录)
-- =====================================================

-- 清理现有扩展字段数据
DELETE FROM TB_PROD_USER_DEFIN_EXT_FIELD WHERE PROD_ID >= 1000;

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateExtFields$$

CREATE PROCEDURE GenerateExtFields()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE prod_id_val DECIMAL(38,0);
    DECLARE prod_type_val VARCHAR(15);
    DECLARE field_counter INT DEFAULT 1;

    -- 游标遍历所有产品
    DECLARE prod_cursor CURSOR FOR
        SELECT PROD_ID, PROD_TYPE_CDE FROM TB_PROD WHERE PROD_ID >= 1000;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN prod_cursor;

    read_loop: LOOP
        FETCH prod_cursor INTO prod_id_val, prod_type_val;
        IF done THEN
            LEAVE read_loop;
        END IF;

        -- 为每个产品生成2-4个扩展字段
        CASE prod_type_val
            WHEN 'MF' THEN
                -- 基金产品扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (
                    ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM,
                    CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT
                ) VALUES
                (CONCAT('ROW', LPAD(field_counter, 8, '0')), prod_id_val, 'A', 'FUND_MANAGER', 1, 'HK', 'HSBC', 'String',
                 ELT(1 + FLOOR(RAND() * 10), '汇丰投资管理', '贝莱德', '先锋', '富达', '摩根大通', '高盛', '瑞银', '德意志银行', '瑞信', '法国巴黎银行')),
                (CONCAT('ROW', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'A', 'BENCHMARK', 1, 'HK', 'HSBC', 'String',
                 ELT(1 + FLOOR(RAND() * 7), '恒生指数', '恒生中国企业指数', '标普500指数', '纳斯达克指数', 'MSCI世界指数', 'MSCI新兴市场指数', '彭博综合债券指数')),
                (CONCAT('ROW', LPAD(field_counter + 2, 8, '0')), prod_id_val, 'A', 'MANAGEMENT_FEE', 1, 'HK', 'HSBC', 'Decimal',
                 CAST((0.5 + RAND() * 2.5) AS CHAR)), -- 0.5% - 3.0%
                (CONCAT('ROW', LPAD(field_counter + 3, 8, '0')), prod_id_val, 'A', 'PERFORMANCE_FEE', 1, 'HK', 'HSBC', 'Decimal',
                 CAST((RAND() * 2.0) AS CHAR)); -- 0% - 2.0%
                SET field_counter = field_counter + 4;

            WHEN 'BD' THEN
                -- 债券产品扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (
                    ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM,
                    CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT
                ) VALUES
                (CONCAT('ROW', LPAD(field_counter, 8, '0')), prod_id_val, 'A', 'ISSUER', 1, 'HK', 'HSBC', 'String',
                 ELT(1 + FLOOR(RAND() * 8), '香港政府', '中国政府', '汇丰银行', '中银香港', '恒生银行', '腾讯控股', '阿里巴巴', '中国移动')),
                (CONCAT('ROW', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'A', 'CREDIT_RATING', 1, 'HK', 'HSBC', 'String',
                 ELT(1 + FLOOR(RAND() * 7), 'AAA', 'AA+', 'AA', 'AA-', 'A+', 'A', 'A-')),
                (CONCAT('ROW', LPAD(field_counter + 2, 8, '0')), prod_id_val, 'A', 'COUPON_RATE', 1, 'HK', 'HSBC', 'Decimal',
                 CAST((1.0 + RAND() * 6.0) AS CHAR)); -- 1.0% - 7.0%
                SET field_counter = field_counter + 3;

            WHEN 'ETF' THEN
                -- ETF产品扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (
                    ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM,
                    CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT
                ) VALUES
                (CONCAT('ROW', LPAD(field_counter, 8, '0')), prod_id_val, 'A', 'TRACKING_INDEX', 1, 'HK', 'HSBC', 'String',
                 ELT(1 + FLOOR(RAND() * 6), '恒生指数', '恒生科技指数', '标普500指数', '纳斯达克100指数', 'MSCI中国指数', 'FTSE中国A50指数')),
                (CONCAT('ROW', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'A', 'EXPENSE_RATIO', 1, 'HK', 'HSBC', 'Decimal',
                 CAST((0.1 + RAND() * 0.9) AS CHAR)); -- 0.1% - 1.0%
                SET field_counter = field_counter + 2;

            WHEN 'ELI' THEN
                -- 结构性产品扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (
                    ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM,
                    CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT
                ) VALUES
                (CONCAT('ROW', LPAD(field_counter, 8, '0')), prod_id_val, 'A', 'UNDERLYING_ASSET', 1, 'HK', 'HSBC', 'String',
                 ELT(1 + FLOOR(RAND() * 5), '恒生指数', '腾讯控股', '阿里巴巴', '美团', '小米集团')),
                (CONCAT('ROW', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'A', 'PROTECTION_LEVEL', 1, 'HK', 'HSBC', 'String',
                 ELT(1 + FLOOR(RAND() * 4), '100%', '95%', '90%', '85%')),
                (CONCAT('ROW', LPAD(field_counter + 2, 8, '0')), prod_id_val, 'A', 'BARRIER_LEVEL', 1, 'HK', 'HSBC', 'String',
                 ELT(1 + FLOOR(RAND() * 4), '70%', '75%', '80%', '85%'));
                SET field_counter = field_counter + 3;

            WHEN 'ST' THEN
                -- 股票产品扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (
                    ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM,
                    CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT
                ) VALUES
                (CONCAT('ROW', LPAD(field_counter, 8, '0')), prod_id_val, 'A', 'COMPANY_NAME', 1, 'HK', 'HSBC', 'String',
                 CONCAT('公司', LPAD(prod_id_val, 4, '0'), '有限公司')),
                (CONCAT('ROW', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'A', 'MARKET_CAP', 1, 'HK', 'HSBC', 'String',
                 ELT(1 + FLOOR(RAND() * 4), '大型股', '中型股', '小型股', '微型股'));
                SET field_counter = field_counter + 2;

            ELSE
                -- 其他产品类型的通用扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (
                    ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM,
                    CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT
                ) VALUES
                (CONCAT('ROW', LPAD(field_counter, 8, '0')), prod_id_val, 'A', 'PRODUCT_FEATURE', 1, 'HK', 'HSBC', 'String',
                 ELT(1 + FLOOR(RAND() * 5), '高流动性', '低风险', '稳定收益', '成长潜力', '多元化投资'));
                SET field_counter = field_counter + 1;
        END CASE;

        -- 每1000条记录提交一次
        IF field_counter % 1000 = 0 THEN
            COMMIT;
        END IF;

    END LOOP;

    CLOSE prod_cursor;
    COMMIT;
END$$

DELIMITER ;

-- 执行存储过程生成扩展字段数据
CALL GenerateExtFields();

-- 删除存储过程
DROP PROCEDURE GenerateExtFields;
