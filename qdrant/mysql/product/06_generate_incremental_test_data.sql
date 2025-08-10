-- =====================================================
-- Cognos财富管理数据库增量测试数据生成脚本
-- 在现有数据基础上增加3000-5000条新记录
-- 创建时间: 2025-01-04 16:45:00
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
-- 1. 生成增量产品数据 (3000条新产品)
-- =====================================================

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateIncrementalProducts$$

CREATE PROCEDURE GenerateIncrementalProducts()
BEGIN
    DECLARE i INT DEFAULT 4001; -- 从4001开始，避免与现有数据冲突
    DECLARE max_products INT DEFAULT 7000; -- 生成到7000，共3000条新记录
    DECLARE prod_type VARCHAR(15);
    DECLARE prod_name VARCHAR(255);
    DECLARE prod_desc TEXT;
    DECLARE currency_code VARCHAR(3);
    DECLARE risk_level CHAR(1);
    DECLARE status_code CHAR(1);
    DECLARE market_price DECIMAL(15,4);
    DECLARE bid_price DECIMAL(15,4);
    DECLARE offer_price DECIMAL(15,4);
    DECLARE launch_date DATE;
    DECLARE maturity_date DATE;
    DECLARE min_investment DECIMAL(15,2);
    DECLARE increment_amount DECIMAL(15,2);
    
    WHILE i <= max_products DO
        -- 随机选择产品类型
        SET prod_type = ELT(1 + FLOOR(RAND() * 12), 'MF', 'BD', 'ST', 'ETF', 'ELI', 'WARRANT', 'CBBC', 'OPTION', 'REIT', 'FX', 'COMMODITY', 'CRYPTO');
        
        -- 随机选择货币
        SET currency_code = ELT(1 + FLOOR(RAND() * 8), 'HKD', 'USD', 'EUR', 'GBP', 'JPY', 'CNY', 'SGD', 'AUD');
        
        -- 随机选择风险等级
        SET risk_level = ELT(1 + FLOOR(RAND() * 5), '1', '2', '3', '4', '5');
        
        -- 随机选择状态
        SET status_code = ELT(1 + FLOOR(RAND() * 4), 'A', 'S', 'M', 'T');
        
        -- 根据产品类型生成产品名称和描述
        CASE prod_type
            WHEN 'MF' THEN
                SET prod_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 10), '汇丰', '中银', '恒生', '东亚', '渣打', '花旗', '摩根', '富达', '贝莱德', '施罗德'),
                    ELT(1 + FLOOR(RAND() * 8), '亚洲', '环球', '中国', '新兴市场', '欧洲', '美国', '日本', '科技'),
                    ELT(1 + FLOOR(RAND() * 6), '股票', '债券', '平衡', '增长', '收益', '价值'),
                    '基金', LPAD(i, 4, '0')
                );
                SET prod_desc = CONCAT('专业管理的', prod_name, '，适合', 
                    CASE risk_level 
                        WHEN '1' THEN '保守型' 
                        WHEN '2' THEN '稳健型' 
                        WHEN '3' THEN '平衡型' 
                        WHEN '4' THEN '积极型' 
                        ELSE '激进型' 
                    END, '投资者');
                SET market_price = 5.0 + RAND() * 50.0;
                SET min_investment = 1000 + FLOOR(RAND() * 9000);
                
            WHEN 'BD' THEN
                SET prod_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 8), '香港政府', '中国政府', '汇丰银行', '中银香港', '恒生银行', '腾讯控股', '阿里巴巴', '中国移动'),
                    YEAR(CURDATE()) + FLOOR(RAND() * 5), '年期债券', LPAD(i, 4, '0')
                );
                SET prod_desc = CONCAT(prod_name, '，固定收益投资产品，适合稳健型投资者');
                SET market_price = 95.0 + RAND() * 10.0;
                SET min_investment = 10000 + FLOOR(RAND() * 90000);
                
            WHEN 'ST' THEN
                SET prod_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 15), '腾讯控股', '阿里巴巴', '美团', '小米集团', '比亚迪', '中国平安', '招商银行', '工商银行', '中国移动', '中石油', '中石化', '建设银行', '农业银行', '中国银行', '中国人寿'),
                    ' (', LPAD(i % 10000, 4, '0'), '.HK)'
                );
                SET prod_desc = CONCAT(prod_name, '普通股，在香港交易所上市交易');
                SET market_price = 1.0 + RAND() * 500.0;
                SET min_investment = 100 + FLOOR(RAND() * 900);
                
            WHEN 'ETF' THEN
                SET prod_name = CONCAT(
                    ELT(1 + FLOOR(RAND() * 6), '盈富基金', '恒生科技ETF', '南方A50ETF', 'SPDR金ETF', '安硕核心ETF', '易方达ETF'),
                    LPAD(i, 4, '0')
                );
                SET prod_desc = CONCAT(prod_name, '，追踪指数表现的交易所交易基金');
                SET market_price = 10.0 + RAND() * 90.0;
                SET min_investment = 500 + FLOOR(RAND() * 4500);
                
            WHEN 'ELI' THEN
                SET prod_name = CONCAT(
                    '股票挂钩投资', YEAR(CURDATE()), '-', LPAD(i, 4, '0')
                );
                SET prod_desc = CONCAT(prod_name, '，与指定股票表现挂钩的结构性投资产品');
                SET market_price = 10.0 + RAND() * 90.0;
                SET min_investment = 10000 + FLOOR(RAND() * 90000);
                
            ELSE
                SET prod_name = CONCAT(prod_type, '产品', LPAD(i, 4, '0'));
                SET prod_desc = CONCAT(prod_name, '，专业投资产品');
                SET market_price = 1.0 + RAND() * 100.0;
                SET min_investment = 1000 + FLOOR(RAND() * 9000);
        END CASE;
        
        -- 计算买卖价格
        SET bid_price = market_price * (0.995 + RAND() * 0.01);
        SET offer_price = market_price * (1.0 + RAND() * 0.01);
        
        -- 设置日期
        SET launch_date = DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 1095) DAY);
        SET maturity_date = CASE 
            WHEN prod_type IN ('BD', 'ELI') THEN DATE_ADD(launch_date, INTERVAL (1 + FLOOR(RAND() * 10)) YEAR)
            ELSE NULL 
        END;
        
        SET increment_amount = CASE 
            WHEN min_investment >= 10000 THEN 1000 + FLOOR(RAND() * 9000)
            ELSE 100 + FLOOR(RAND() * 900)
        END;
        
        -- 插入产品记录
        INSERT INTO TB_PROD (
            CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_ID, PROD_CDE, PROD_TYPE_CDE, PROD_STAT_CDE,
            PROD_NAME, PROD_DESC, CCY_PROD_CDE, RISK_LVL_CDE, PROD_MKT_PRC_AMT,
            PROD_BID_PRC_AMT, PROD_OFFR_PRC_AMT, PROD_LNCH_DT, PROD_MTUR_DT
        ) VALUES (
            'HK',
            'HSBC',
            i,
            CONCAT(prod_type, LPAD(i, 6, '0')),
            prod_type,
            status_code,
            prod_name,
            prod_desc,
            currency_code,
            risk_level,
            market_price,
            bid_price,
            offer_price,
            launch_date,
            maturity_date
        );
        
        SET i = i + 1;
        
        -- 每500条记录提交一次
        IF i % 500 = 0 THEN
            COMMIT;
        END IF;
        
    END WHILE;
    
    COMMIT;
END$$

DELIMITER ;

-- 执行产品数据生成
CALL GenerateIncrementalProducts();

-- 删除存储过程
DROP PROCEDURE GenerateIncrementalProducts;

-- =====================================================
-- 2. 生成产品扩展字段数据 (每个产品3-5个扩展字段)
-- =====================================================

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateIncrementalExtFields$$

CREATE PROCEDURE GenerateIncrementalExtFields()
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
CALL GenerateIncrementalExtFields();

-- 删除存储过程
DROP PROCEDURE GenerateIncrementalExtFields;
