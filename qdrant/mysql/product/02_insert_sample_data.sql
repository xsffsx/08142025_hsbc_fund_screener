-- =====================================================
-- Cognos Sample Data for MySQL
-- 示例数据插入脚本
-- 创建时间: 2025-01-04 14:30:35
-- 数据库: nl2sql
-- =====================================================

-- 设置字符集
SET NAMES utf8mb4;

-- =====================================================
-- 1. 插入代码描述值数据
-- =====================================================
INSERT INTO `CDE_DESC_VALUE` (
    `CTRY_REC_CDE`, `GRP_MEMBR_REC_CDE`, `CDV_TYPE_CDE`, `CDV_CDE`, 
    `CDV_DESC`, `CDV_PLL_DESC`, `CDV_SLL_DESC`
) VALUES 
-- 产品类型代码
('HK', 'HSBC', 'PROD_TYPE', 'MF', '互惠基金', 'Mutual Fund', 'Mutual Fund'),
('HK', 'HSBC', 'PROD_TYPE', 'BD', '债券', 'Bond', 'Bond'),
('HK', 'HSBC', 'PROD_TYPE', 'ELI', '股票挂钩投资', 'Equity Linked Investment', 'Equity Linked Investment'),
('HK', 'HSBC', 'PROD_TYPE', 'ST', '股票', 'Stock', 'Stock'),
('HK', 'HSBC', 'PROD_TYPE', 'FX', '外汇', 'Foreign Exchange', 'Foreign Exchange'),

-- 产品状态代码
('HK', 'HSBC', 'PROD_STAT', 'A', '活跃', 'Active', 'Active'),
('HK', 'HSBC', 'PROD_STAT', 'S', '暂停', 'Suspended', 'Suspended'),
('HK', 'HSBC', 'PROD_STAT', 'M', '到期', 'Matured', 'Matured'),
('HK', 'HSBC', 'PROD_STAT', 'T', '终止', 'Terminated', 'Terminated'),
('HK', 'HSBC', 'PROD_STAT', 'P', '待发布', 'Pending', 'Pending'),

-- 风险等级代码
('HK', 'HSBC', 'RISK_LEVEL', '1', '保守型', 'Conservative', 'Conservative'),
('HK', 'HSBC', 'RISK_LEVEL', '2', '稳健型', 'Moderate', 'Moderate'),
('HK', 'HSBC', 'RISK_LEVEL', '3', '平衡型', 'Balanced', 'Balanced'),
('HK', 'HSBC', 'RISK_LEVEL', '4', '积极型', 'Aggressive', 'Aggressive'),
('HK', 'HSBC', 'RISK_LEVEL', '5', '激进型', 'Speculative', 'Speculative'),

-- 资产类别代码
('HK', 'HSBC', 'ASSET_CLASS', 'EQUITY', '股票', 'Equity', 'Equity'),
('HK', 'HSBC', 'ASSET_CLASS', 'BOND', '债券', 'Bond', 'Bond'),
('HK', 'HSBC', 'ASSET_CLASS', 'MIXED', '混合', 'Mixed', 'Mixed'),
('HK', 'HSBC', 'ASSET_CLASS', 'MONEY', '货币市场', 'Money Market', 'Money Market'),
('HK', 'HSBC', 'ASSET_CLASS', 'COMMODITY', '商品', 'Commodity', 'Commodity');

-- =====================================================
-- 2. 插入产品主表数据
-- =====================================================
INSERT INTO `TB_PROD` (
    `CTRY_REC_CDE`, `GRP_MEMBR_REC_CDE`, `PROD_ID`, `PROD_TYPE`, `PROD_TYPE_CDE`,
    `PROD_SUBTP_CDE`, `PROD_CDE`, `PROD_NAME`, `PROD_PLL_NAME`, `PROD_SLL_NAME`,
    `PROD_SHRT_NAME`, `PROD_DESC`, `ASET_CLASS_CDE`, `PROD_STAT_CDE`,
    `CCY_PROD_CDE`, `RISK_LVL_CDE`, `PROD_LNCH_DT`, `PROD_MTUR_DT`,
    `ALLOW_BUY_PROD_IND`, `ALLOW_SELL_PROD_IND`, `PROD_BID_PRC_AMT`,
    `PROD_OFFR_PRC_AMT`, `PROD_MKT_PRC_AMT`, `PROD_NAV_PRC_AMT`
) VALUES
-- 基金产品
('HK', 'HSBC', 1001, 'MF', 'MF', 'EQUITY_GROWTH', 'HKFUND001',
 '汇丰成长基金', 'HSBC Growth Fund', 'HSBC Growth Fund', '成长基金',
 '专注于成长型股票投资的基金', 'EQUITY', 'A', 'HKD', '4',
 '2024-01-01', '2029-12-31', 'Y', 'Y', 10.50, 10.55, 10.52, 10.52),

('HK', 'HSBC', 1002, 'MF', 'MF', 'BOND_INCOME', 'HKFUND002',
 '汇丰收益债券基金', 'HSBC Income Bond Fund', 'HSBC Income Bond Fund', '收益债券基金',
 '专注于高收益债券投资的基金', 'BOND', 'A', 'HKD', '2',
 '2024-01-01', '2029-12-31', 'Y', 'Y', 8.20, 8.25, 8.22, 8.22),

-- 债券产品
('HK', 'HSBC', 1003, 'BD', 'BD', 'CORP_BOND', 'HKBOND001',
 '汇丰企业债券', 'HSBC Corporate Bond', 'HSBC Corporate Bond', '企业债券',
 '高评级企业债券', 'BOND', 'A', 'HKD', '3',
 '2024-01-01', '2027-01-01', 'Y', 'Y', 98.50, 99.00, 98.75, 98.75),

-- 股票挂钩投资产品
('HK', 'HSBC', 1004, 'ELI', 'ELI', 'STRUCTURED', 'HKELI001',
 '汇丰结构性产品', 'HSBC Structured Product', 'HSBC Structured Product', '结构性产品',
 '与恒生指数挂钩的结构性产品', 'MIXED', 'A', 'HKD', '5',
 '2024-01-01', '2025-01-01', 'Y', 'Y', 95.00, 95.50, 95.25, 95.25),

-- 股票产品
('HK', 'HSBC', 1005, 'ST', 'ST', 'BLUE_CHIP', 'HK0005',
 '汇丰控股', 'HSBC Holdings', 'HSBC Holdings', '汇丰控股',
 '汇丰控股有限公司股票', 'EQUITY', 'A', 'HKD', '4',
 '1991-01-01', NULL, 'Y', 'Y', 62.50, 63.00, 62.75, 62.75);

-- =====================================================
-- 3. 插入产品扩展字段数据
-- =====================================================
INSERT INTO `TB_PROD_USER_DEFIN_EXT_FIELD` (
    `ROWID`, `PROD_ID`, `FIELD_TYPE_CDE`, `FIELD_CDE`, `FIELD_SEQ_NUM`,
    `CTRY_REC_CDE`, `GRP_MEMBR_REC_CDE`, `FIELD_DATA_TYPE_TEXT`,
    `FIELD_STRNG_VALUE_TEXT`
) VALUES 
('ROW001', 1001, 'A', 'FUND_MANAGER', 1, 'HK', 'HSBC', 'String', '汇丰投资管理'),
('ROW002', 1001, 'A', 'BENCHMARK', 1, 'HK', 'HSBC', 'String', '恒生指数'),
('ROW003', 1002, 'A', 'FUND_MANAGER', 1, 'HK', 'HSBC', 'String', '汇丰固定收益'),
('ROW004', 1002, 'A', 'BENCHMARK', 1, 'HK', 'HSBC', 'String', '彭博债券指数'),
('ROW005', 1004, 'A', 'UNDERLYING_ASSET', 1, 'HK', 'HSBC', 'String', '恒生指数'),
('ROW006', 1004, 'A', 'PROTECTION_LEVEL', 1, 'HK', 'HSBC', 'String', '85%');

-- =====================================================
-- 4. 插入债券工具数据
-- =====================================================
INSERT INTO `TB_DEBT_INSTM` (
    `PROD_ID_DEBT_INSTM`, `ISR_BOND_NAME`, `ISSUE_NUM`, `PROD_ISS_DT`,
    `COUPN_ANNL_RATE`, `YIELD_TO_MTUR_BID_PCT`, `BOND_STAT_CDE`
) VALUES 
(1003, '汇丰企业债券2024', 'HSBC2024001', '2024-01-01', 4.50, 4.25, 'A');

-- =====================================================
-- 5. 插入股票挂钩投资数据
-- =====================================================
INSERT INTO `TB_EQTY_LINK_INVST` (
    `PROD_ID_EQTY_LINK_INVST`, `EQTY_LINK_INVST_TYPE_CDE`, `TRD_DT`,
    `YIELD_TO_MTUR_PCT`, `DEN_AMT`, `TRD_MIN_AMT`, `VALN_DT`, `SETL_DT`
) VALUES 
(1004, 'AUTO_CALLABLE', '2024-01-01', 8.50, 100000.00, 10000.00, '2024-12-31', '2025-01-03');

-- =====================================================
-- 6. 插入股票挂钩投资标的股票数据
-- =====================================================
INSERT INTO `TB_EQTY_LINK_INVST_UNDL_STOCK` (
    `ROWID`, `PROD_ID_EQTY_LINK_INVST`, `INSTM_UNDL_CDE`, `INSTM_UNDL_TEXT`,
    `PROD_ID_UNDL_INSTM`, `PROD_STRK_PRC_AMT`, `PROD_KNOCK_IN_PRICE_AMT`,
    `PROD_INIT_SPOT_PRICE_AMT`, `PROD_BREAK_EVEN_PRC_AMT`
) VALUES 
('STOCK001', 1004, 'HSI', '恒生指数', 1005, 25000.00, 21250.00, 25000.00, 23750.00),
('STOCK002', 1004, '0005.HK', '汇丰控股', 1005, 65.00, 55.25, 65.00, 61.75);

-- =====================================================
-- 7. 插入产品替代ID数据
-- =====================================================
INSERT INTO `TB_PROD_ALT_ID` (
    `ROWID`, `CTRY_REC_CDE`, `GRP_MEMBR_REC_CDE`, `PROD_ID`,
    `PROD_CDE_ALT_CLASS_CDE`, `PROD_TYPE_CDE`, `PROD_ALT_NUM`
) VALUES 
('ALT001', 'HK', 'HSBC', 1001, 'I', 'MF', 'ISIN001'),
('ALT002', 'HK', 'HSBC', 1001, 'B', 'MF', 'BLOOMBERG001'),
('ALT003', 'HK', 'HSBC', 1002, 'I', 'MF', 'ISIN002'),
('ALT004', 'HK', 'HSBC', 1003, 'I', 'BD', 'ISIN003'),
('ALT005', 'HK', 'HSBC', 1005, 'R', 'ST', '0005.HK');

-- =====================================================
-- 8. 插入产品表单要求数据
-- =====================================================
INSERT INTO `PROD_FORM_REQMT` (
    `ROWID`, `PROD_ID`, `FORM_REQ_CDE`
) VALUES 
('FORM001', 1001, 'RISK_DISCLOSURE'),
('FORM002', 1001, 'INVESTMENT_OBJECTIVE'),
('FORM003', 1004, 'STRUCTURED_PRODUCT_RISK'),
('FORM004', 1004, 'DERIVATIVE_RISK'),
('FORM005', 1005, 'EQUITY_RISK');

-- =====================================================
-- 9. 插入产品限制客户国家数据
-- =====================================================
INSERT INTO `PROD_RESTR_CUST_CTRY` (
    `ROWID`, `PROD_ID`, `CTRY_ISO_CDE`, `RESTR_CTRY_TYPE_CDE`, `RESTR_CDE`
) VALUES 
('RESTR001', 1001, 'US', 'R', 'Y'),  -- 美国居民限制
('RESTR002', 1004, 'US', 'R', 'Y'),  -- 美国居民限制
('RESTR003', 1004, 'CN', 'R', 'Y');  -- 中国大陆居民限制

-- =====================================================
-- 10. 插入产品覆盖字段数据
-- =====================================================
INSERT INTO `PROD_OVRID_FIELD` (
    `ROWID`, `PROD_ID`, `FIELD_CDE`
) VALUES 
('OVRD001', 1001, 'PROD_NAME'),
('OVRD002', 1001, 'PROD_DESC'),
('OVRD003', 1004, 'RISK_LVL_CDE');

-- =====================================================
-- 数据插入完成提示
-- =====================================================
SELECT 'Sample Data Inserted Successfully!' AS STATUS;

-- 验证数据插入
SELECT 
    'TB_PROD' AS TABLE_NAME, 
    COUNT(*) AS RECORD_COUNT 
FROM TB_PROD
UNION ALL
SELECT 
    'CDE_DESC_VALUE' AS TABLE_NAME, 
    COUNT(*) AS RECORD_COUNT 
FROM CDE_DESC_VALUE
UNION ALL
SELECT 
    'TB_PROD_USER_DEFIN_EXT_FIELD' AS TABLE_NAME, 
    COUNT(*) AS RECORD_COUNT 
FROM TB_PROD_USER_DEFIN_EXT_FIELD
UNION ALL
SELECT 
    'TB_DEBT_INSTM' AS TABLE_NAME, 
    COUNT(*) AS RECORD_COUNT 
FROM TB_DEBT_INSTM
UNION ALL
SELECT 
    'TB_EQTY_LINK_INVST' AS TABLE_NAME, 
    COUNT(*) AS RECORD_COUNT 
FROM TB_EQTY_LINK_INVST;
