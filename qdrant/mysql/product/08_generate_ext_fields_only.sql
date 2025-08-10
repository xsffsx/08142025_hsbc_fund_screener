-- =====================================================
-- 仅生成产品扩展字段数据脚本
-- 为ID 4001-7000的产品生成扩展字段
-- 创建时间: 2025-01-04 17:10:00
-- 数据库: nl2sql
-- =====================================================

-- 设置字符集和优化参数
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;
SET autocommit = 0;
SET unique_checks = 0;
SET foreign_key_checks = 0;

-- =====================================================
-- 生成产品扩展字段数据 (每个产品3-5个扩展字段)
-- =====================================================

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateExtFieldsOnly$$

CREATE PROCEDURE GenerateExtFieldsOnly()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE prod_id_val DECIMAL(38,0);
    DECLARE prod_type_val VARCHAR(15);
    DECLARE field_counter INT DEFAULT 1;
    
    -- 游标遍历新增的产品
    DECLARE prod_cursor CURSOR FOR 
        SELECT PROD_ID, PROD_TYPE_CDE FROM TB_PROD WHERE PROD_ID BETWEEN 4001 AND 7000;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN prod_cursor;
    
    read_loop: LOOP
        FETCH prod_cursor INTO prod_id_val, prod_type_val;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 根据产品类型生成相应的扩展字段
        CASE prod_type_val
            WHEN 'MF' THEN
                -- 基金产品扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT) VALUES 
                (CONCAT('EXT', LPAD(field_counter, 8, '0')), prod_id_val, 'U', 'FUND_MANAGER', 1, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 8), '张伟明', '李小华', '王建国', '陈美玲', '刘志强', '赵敏', '孙大伟', '周静')),
                (CONCAT('EXT', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'U', 'BENCHMARK', 2, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 6), '恒生指数', 'MSCI亚洲指数', '沪深300指数', '标普500指数', '富时100指数', '日经225指数')),
                (CONCAT('EXT', LPAD(field_counter + 2, 8, '0')), prod_id_val, 'U', 'MANAGEMENT_FEE', 3, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(0.5 + RAND() * 2.0, 2), '%')),
                (CONCAT('EXT', LPAD(field_counter + 3, 8, '0')), prod_id_val, 'U', 'PERFORMANCE_FEE', 4, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(RAND() * 20.0, 2), '%'));
                SET field_counter = field_counter + 4;
                
            WHEN 'BD' THEN
                -- 债券产品扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT) VALUES 
                (CONCAT('EXT', LPAD(field_counter, 8, '0')), prod_id_val, 'U', 'ISSUER', 1, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 6), '香港政府', '中国政府', '汇丰银行', '中银香港', '恒生银行', '腾讯控股')),
                (CONCAT('EXT', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'U', 'CREDIT_RATING', 2, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 7), 'AAA', 'AA+', 'AA', 'AA-', 'A+', 'A', 'A-')),
                (CONCAT('EXT', LPAD(field_counter + 2, 8, '0')), prod_id_val, 'U', 'COUPON_RATE', 3, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(1.0 + RAND() * 6.0, 2), '%')),
                (CONCAT('EXT', LPAD(field_counter + 3, 8, '0')), prod_id_val, 'U', 'YIELD_TO_MATURITY', 4, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(0.8 + RAND() * 6.2, 2), '%'));
                SET field_counter = field_counter + 4;
                
            WHEN 'ST' THEN
                -- 股票产品扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT) VALUES 
                (CONCAT('EXT', LPAD(field_counter, 8, '0')), prod_id_val, 'U', 'COMPANY_NAME', 1, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 10), '腾讯控股有限公司', '阿里巴巴集团', '美团', '小米集团', '比亚迪股份', '中国平安', '招商银行', '工商银行', '中国移动', '中石油')),
                (CONCAT('EXT', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'U', 'MARKET_CAP', 2, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 4), '大型股', '中型股', '小型股', '微型股')),
                (CONCAT('EXT', LPAD(field_counter + 2, 8, '0')), prod_id_val, 'U', 'SECTOR', 3, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 8), '科技', '金融', '消费', '医疗', '工业', '能源', '房地产', '公用事业')),
                (CONCAT('EXT', LPAD(field_counter + 3, 8, '0')), prod_id_val, 'U', 'PE_RATIO', 4, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(5.0 + RAND() * 50.0, 2), 'x'));
                SET field_counter = field_counter + 4;
                
            WHEN 'ETF' THEN
                -- ETF产品扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT) VALUES 
                (CONCAT('EXT', LPAD(field_counter, 8, '0')), prod_id_val, 'U', 'TRACKING_INDEX', 1, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 6), '恒生指数', '恒生科技指数', '沪深300指数', '标普500指数', '纳斯达克100指数', '富时中国A50指数')),
                (CONCAT('EXT', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'U', 'EXPENSE_RATIO', 2, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(0.1 + RAND() * 1.0, 2), '%')),
                (CONCAT('EXT', LPAD(field_counter + 2, 8, '0')), prod_id_val, 'U', 'TRACKING_ERROR', 3, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(RAND() * 2.0, 2), '%')),
                (CONCAT('EXT', LPAD(field_counter + 3, 8, '0')), prod_id_val, 'U', 'DIVIDEND_YIELD', 4, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(RAND() * 5.0, 2), '%'));
                SET field_counter = field_counter + 4;
                
            WHEN 'ELI' THEN
                -- 股票挂钩投资扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT) VALUES 
                (CONCAT('EXT', LPAD(field_counter, 8, '0')), prod_id_val, 'U', 'UNDERLYING_ASSET', 1, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 5), '恒生指数', '腾讯控股', '阿里巴巴', '美团', '小米集团')),
                (CONCAT('EXT', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'U', 'PROTECTION_LEVEL', 2, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(80.0 + RAND() * 15.0, 1), '%')),
                (CONCAT('EXT', LPAD(field_counter + 2, 8, '0')), prod_id_val, 'U', 'BARRIER_LEVEL', 3, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(60.0 + RAND() * 20.0, 1), '%')),
                (CONCAT('EXT', LPAD(field_counter + 3, 8, '0')), prod_id_val, 'U', 'MAX_RETURN', 4, 'HK', 'HSBC', 'STRING', CONCAT(FORMAT(5.0 + RAND() * 20.0, 1), '%'));
                SET field_counter = field_counter + 4;
                
            ELSE
                -- 其他产品类型的通用扩展字段
                INSERT INTO TB_PROD_USER_DEFIN_EXT_FIELD (ROWID, PROD_ID, FIELD_TYPE_CDE, FIELD_CDE, FIELD_SEQ_NUM, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, FIELD_DATA_TYPE_TEXT, FIELD_STRNG_VALUE_TEXT) VALUES 
                (CONCAT('EXT', LPAD(field_counter, 8, '0')), prod_id_val, 'U', 'PRODUCT_CATEGORY', 1, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 4), '专业投资者产品', '零售投资者产品', '机构投资者产品', '高净值客户产品')),
                (CONCAT('EXT', LPAD(field_counter + 1, 8, '0')), prod_id_val, 'U', 'LIQUIDITY', 2, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 3), '高流动性', '中等流动性', '低流动性')),
                (CONCAT('EXT', LPAD(field_counter + 2, 8, '0')), prod_id_val, 'U', 'SETTLEMENT_PERIOD', 3, 'HK', 'HSBC', 'STRING', ELT(1 + FLOOR(RAND() * 4), 'T+0', 'T+1', 'T+2', 'T+3'));
                SET field_counter = field_counter + 3;
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

-- 执行扩展字段数据生成
CALL GenerateExtFieldsOnly();

-- 删除存储过程
DROP PROCEDURE GenerateExtFieldsOnly;

-- 恢复设置
SET foreign_key_checks = 1;
SET unique_checks = 1;
SET autocommit = 1;

-- 最终提交
COMMIT;

-- 显示生成的扩展字段统计
SELECT 'Extension Fields Generation Completed!' AS STATUS;

SELECT 
    'TB_PROD_USER_DEFIN_EXT_FIELD (新增)' AS table_name, 
    COUNT(*) AS new_records 
FROM TB_PROD_USER_DEFIN_EXT_FIELD WHERE PROD_ID BETWEEN 4001 AND 7000;
