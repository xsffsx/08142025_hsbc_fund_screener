-- =====================================================
-- Cognos辅助表大量测试数据生成脚本 (第三部分)
-- 生成产品替代ID、表单要求、限制等辅助表数据
-- 创建时间: 2025-01-04 14:45:00
-- 数据库: nl2sql
-- =====================================================

-- 设置字符集和优化参数
SET NAMES utf8mb4;
SET autocommit = 0;
SET unique_checks = 0;
SET foreign_key_checks = 0;

-- =====================================================
-- 7. 生成产品替代ID表数据 (约12000条记录)
-- =====================================================

-- 清理现有产品替代ID数据
DELETE FROM TB_PROD_ALT_ID WHERE PROD_ID >= 1000;

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateAltIdData$$

CREATE PROCEDURE GenerateAltIdData()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE prod_id_val DECIMAL(38,0);
    DECLARE prod_type_val VARCHAR(15);
    DECLARE prod_code_val VARCHAR(30);
    DECLARE alt_counter INT DEFAULT 1;
    
    -- 游标遍历所有产品
    DECLARE prod_cursor CURSOR FOR 
        SELECT PROD_ID, PROD_TYPE_CDE, PROD_CDE FROM TB_PROD WHERE PROD_ID >= 1000;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN prod_cursor;
    
    read_loop: LOOP
        FETCH prod_cursor INTO prod_id_val, prod_type_val, prod_code_val;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 为每个产品生成3-5个替代ID
        -- ISIN代码
        INSERT INTO TB_PROD_ALT_ID (
            ROWID, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_ID,
            PROD_CDE_ALT_CLASS_CDE, PROD_TYPE_CDE, PROD_ALT_NUM
        ) VALUES (
            CONCAT('ALT', LPAD(alt_counter, 8, '0')),
            'HK', 'HSBC', prod_id_val, 'I', prod_type_val,
            CONCAT('HK', LPAD(FLOOR(RAND() * 1000000000), 9, '0'), LPAD(alt_counter % 100, 2, '0'))
        );
        SET alt_counter = alt_counter + 1;
        
        -- Bloomberg代码
        INSERT INTO TB_PROD_ALT_ID (
            ROWID, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_ID,
            PROD_CDE_ALT_CLASS_CDE, PROD_TYPE_CDE, PROD_ALT_NUM
        ) VALUES (
            CONCAT('ALT', LPAD(alt_counter, 8, '0')),
            'HK', 'HSBC', prod_id_val, 'B', prod_type_val,
            CONCAT(prod_code_val, ' HK')
        );
        SET alt_counter = alt_counter + 1;
        
        -- Reuters代码
        INSERT INTO TB_PROD_ALT_ID (
            ROWID, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_ID,
            PROD_CDE_ALT_CLASS_CDE, PROD_TYPE_CDE, PROD_ALT_NUM
        ) VALUES (
            CONCAT('ALT', LPAD(alt_counter, 8, '0')),
            'HK', 'HSBC', prod_id_val, 'R', prod_type_val,
            CONCAT(prod_code_val, '.HK')
        );
        SET alt_counter = alt_counter + 1;
        
        -- 如果是股票，添加交易所代码
        IF prod_type_val = 'ST' THEN
            INSERT INTO TB_PROD_ALT_ID (
                ROWID, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_ID,
                PROD_CDE_ALT_CLASS_CDE, PROD_TYPE_CDE, PROD_ALT_NUM
            ) VALUES (
                CONCAT('ALT', LPAD(alt_counter, 8, '0')),
                'HK', 'HSBC', prod_id_val, 'E', prod_type_val,
                CONCAT(LPAD(prod_id_val % 10000, 4, '0'), '.HK')
            );
            SET alt_counter = alt_counter + 1;
        END IF;
        
        -- 如果是基金，添加基金代码
        IF prod_type_val = 'MF' THEN
            INSERT INTO TB_PROD_ALT_ID (
                ROWID, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_ID,
                PROD_CDE_ALT_CLASS_CDE, PROD_TYPE_CDE, PROD_ALT_NUM
            ) VALUES (
                CONCAT('ALT', LPAD(alt_counter, 8, '0')),
                'HK', 'HSBC', prod_id_val, 'F', prod_type_val,
                CONCAT('F', LPAD(prod_id_val, 6, '0'))
            );
            SET alt_counter = alt_counter + 1;
        END IF;
        
        -- 每1000条记录提交一次
        IF alt_counter % 1000 = 0 THEN
            COMMIT;
        END IF;
        
    END LOOP;
    
    CLOSE prod_cursor;
    COMMIT;
END$$

DELIMITER ;

-- 执行存储过程生成替代ID数据
CALL GenerateAltIdData();

-- 删除存储过程
DROP PROCEDURE GenerateAltIdData;

-- =====================================================
-- 8. 生成产品表单要求表数据 (约6000条记录)
-- =====================================================

-- 清理现有表单要求数据
DELETE FROM PROD_FORM_REQMT WHERE PROD_ID >= 1000;

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateFormReqmtData$$

CREATE PROCEDURE GenerateFormReqmtData()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE prod_id_val DECIMAL(38,0);
    DECLARE prod_type_val VARCHAR(15);
    DECLARE risk_level_val CHAR(1);
    DECLARE form_counter INT DEFAULT 1;
    
    -- 游标遍历所有产品
    DECLARE prod_cursor CURSOR FOR 
        SELECT PROD_ID, PROD_TYPE_CDE, RISK_LVL_CDE FROM TB_PROD WHERE PROD_ID >= 1000;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN prod_cursor;
    
    read_loop: LOOP
        FETCH prod_cursor INTO prod_id_val, prod_type_val, risk_level_val;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 基础风险披露 (所有产品)
        INSERT INTO PROD_FORM_REQMT (
            ROWID, PROD_ID, FORM_REQ_CDE
        ) VALUES (
            CONCAT('FORM', LPAD(form_counter, 8, '0')),
            prod_id_val, 'RISK_DISCLOSURE'
        );
        SET form_counter = form_counter + 1;
        
        -- 投资目标确认 (所有产品)
        INSERT INTO PROD_FORM_REQMT (
            ROWID, PROD_ID, FORM_REQ_CDE
        ) VALUES (
            CONCAT('FORM', LPAD(form_counter, 8, '0')),
            prod_id_val, 'INVESTMENT_OBJECTIVE'
        );
        SET form_counter = form_counter + 1;
        
        -- 根据产品类型添加特定表单要求
        CASE prod_type_val
            WHEN 'MF' THEN
                INSERT INTO PROD_FORM_REQMT (ROWID, PROD_ID, FORM_REQ_CDE) VALUES 
                (CONCAT('FORM', LPAD(form_counter, 8, '0')), prod_id_val, 'FUND_PROSPECTUS'),
                (CONCAT('FORM', LPAD(form_counter + 1, 8, '0')), prod_id_val, 'FUND_FACT_SHEET');
                SET form_counter = form_counter + 2;
                
            WHEN 'BD' THEN
                INSERT INTO PROD_FORM_REQMT (ROWID, PROD_ID, FORM_REQ_CDE) VALUES 
                (CONCAT('FORM', LPAD(form_counter, 8, '0')), prod_id_val, 'BOND_TERMS'),
                (CONCAT('FORM', LPAD(form_counter + 1, 8, '0')), prod_id_val, 'CREDIT_RISK_DISCLOSURE');
                SET form_counter = form_counter + 2;
                
            WHEN 'ELI' THEN
                INSERT INTO PROD_FORM_REQMT (ROWID, PROD_ID, FORM_REQ_CDE) VALUES 
                (CONCAT('FORM', LPAD(form_counter, 8, '0')), prod_id_val, 'STRUCTURED_PRODUCT_RISK'),
                (CONCAT('FORM', LPAD(form_counter + 1, 8, '0')), prod_id_val, 'DERIVATIVE_RISK'),
                (CONCAT('FORM', LPAD(form_counter + 2, 8, '0')), prod_id_val, 'BARRIER_MECHANISM');
                SET form_counter = form_counter + 3;
                
            WHEN 'ST' THEN
                INSERT INTO PROD_FORM_REQMT (ROWID, PROD_ID, FORM_REQ_CDE) VALUES 
                (CONCAT('FORM', LPAD(form_counter, 8, '0')), prod_id_val, 'EQUITY_RISK'),
                (CONCAT('FORM', LPAD(form_counter + 1, 8, '0')), prod_id_val, 'MARKET_VOLATILITY');
                SET form_counter = form_counter + 2;
                
            WHEN 'ETF' THEN
                INSERT INTO PROD_FORM_REQMT (ROWID, PROD_ID, FORM_REQ_CDE) VALUES 
                (CONCAT('FORM', LPAD(form_counter, 8, '0')), prod_id_val, 'ETF_TRACKING_ERROR'),
                (CONCAT('FORM', LPAD(form_counter + 1, 8, '0')), prod_id_val, 'INDEX_METHODOLOGY');
                SET form_counter = form_counter + 2;
                
            ELSE
                INSERT INTO PROD_FORM_REQMT (ROWID, PROD_ID, FORM_REQ_CDE) VALUES 
                (CONCAT('FORM', LPAD(form_counter, 8, '0')), prod_id_val, 'GENERAL_TERMS');
                SET form_counter = form_counter + 1;
        END CASE;
        
        -- 高风险产品额外要求
        IF risk_level_val >= '4' THEN
            INSERT INTO PROD_FORM_REQMT (ROWID, PROD_ID, FORM_REQ_CDE) VALUES 
            (CONCAT('FORM', LPAD(form_counter, 8, '0')), prod_id_val, 'HIGH_RISK_ACKNOWLEDGMENT'),
            (CONCAT('FORM', LPAD(form_counter + 1, 8, '0')), prod_id_val, 'SUITABILITY_ASSESSMENT');
            SET form_counter = form_counter + 2;
        END IF;
        
        -- 每500条记录提交一次
        IF form_counter % 500 = 0 THEN
            COMMIT;
        END IF;
        
    END LOOP;
    
    CLOSE prod_cursor;
    COMMIT;
END$$

DELIMITER ;

-- 执行存储过程生成表单要求数据
CALL GenerateFormReqmtData();

-- 删除存储过程
DROP PROCEDURE GenerateFormReqmtData;

-- =====================================================
-- 9. 生成产品限制客户国家表数据 (约1500条记录)
-- =====================================================

-- 清理现有限制数据
DELETE FROM PROD_RESTR_CUST_CTRY WHERE PROD_ID >= 1000;

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateRestrictionData$$

CREATE PROCEDURE GenerateRestrictionData()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE prod_id_val DECIMAL(38,0);
    DECLARE prod_type_val VARCHAR(15);
    DECLARE restr_counter INT DEFAULT 1;
    
    -- 游标遍历所有产品 (只对部分产品添加限制)
    DECLARE prod_cursor CURSOR FOR 
        SELECT PROD_ID, PROD_TYPE_CDE FROM TB_PROD 
        WHERE PROD_ID >= 1000 AND RAND() < 0.3; -- 30%的产品有地域限制
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN prod_cursor;
    
    read_loop: LOOP
        FETCH prod_cursor INTO prod_id_val, prod_type_val;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 美国居民限制 (FATCA合规)
        IF RAND() < 0.8 THEN -- 80%概率限制美国居民
            INSERT INTO PROD_RESTR_CUST_CTRY (
                ROWID, PROD_ID, CTRY_ISO_CDE, RESTR_CTRY_TYPE_CDE, RESTR_CDE
            ) VALUES (
                CONCAT('RESTR', LPAD(restr_counter, 8, '0')),
                prod_id_val, 'US', 'R', 'Y'
            );
            SET restr_counter = restr_counter + 1;
        END IF;
        
        -- 欧盟居民限制 (MiFID II合规)
        IF prod_type_val IN ('ELI', 'WARRANT', 'CBBC') AND RAND() < 0.6 THEN
            INSERT INTO PROD_RESTR_CUST_CTRY (
                ROWID, PROD_ID, CTRY_ISO_CDE, RESTR_CTRY_TYPE_CDE, RESTR_CDE
            ) VALUES (
                CONCAT('RESTR', LPAD(restr_counter, 8, '0')),
                prod_id_val, 'DE', 'R', 'Y'
            );
            SET restr_counter = restr_counter + 1;
            
            INSERT INTO PROD_RESTR_CUST_CTRY (
                ROWID, PROD_ID, CTRY_ISO_CDE, RESTR_CTRY_TYPE_CDE, RESTR_CDE
            ) VALUES (
                CONCAT('RESTR', LPAD(restr_counter, 8, '0')),
                prod_id_val, 'FR', 'R', 'Y'
            );
            SET restr_counter = restr_counter + 1;
        END IF;
        
        -- 中国大陆居民限制
        IF prod_type_val IN ('ELI', 'WARRANT') AND RAND() < 0.4 THEN
            INSERT INTO PROD_RESTR_CUST_CTRY (
                ROWID, PROD_ID, CTRY_ISO_CDE, RESTR_CTRY_TYPE_CDE, RESTR_CDE
            ) VALUES (
                CONCAT('RESTR', LPAD(restr_counter, 8, '0')),
                prod_id_val, 'CN', 'R', 'Y'
            );
            SET restr_counter = restr_counter + 1;
        END IF;
        
        -- 专业投资者限制
        IF prod_type_val IN ('ELI', 'OPTION') AND RAND() < 0.5 THEN
            INSERT INTO PROD_RESTR_CUST_CTRY (
                ROWID, PROD_ID, CTRY_ISO_CDE, RESTR_CTRY_TYPE_CDE, RESTR_CDE
            ) VALUES (
                CONCAT('RESTR', LPAD(restr_counter, 8, '0')),
                prod_id_val, 'HK', 'P', 'Y'
            );
            SET restr_counter = restr_counter + 1;
        END IF;
        
        -- 每200条记录提交一次
        IF restr_counter % 200 = 0 THEN
            COMMIT;
        END IF;
        
    END LOOP;
    
    CLOSE prod_cursor;
    COMMIT;
END$$

DELIMITER ;

-- 执行存储过程生成限制数据
CALL GenerateRestrictionData();

-- 删除存储过程
DROP PROCEDURE GenerateRestrictionData;

-- =====================================================
-- 10. 生成产品覆盖字段表数据 (约1000条记录)
-- =====================================================

-- 清理现有覆盖字段数据
DELETE FROM PROD_OVRID_FIELD WHERE PROD_ID >= 1000;

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateOverrideData$$

CREATE PROCEDURE GenerateOverrideData()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE prod_id_val DECIMAL(38,0);
    DECLARE override_counter INT DEFAULT 1;
    
    -- 游标遍历有父产品的产品 (假设部分产品有父子关系)
    DECLARE prod_cursor CURSOR FOR 
        SELECT PROD_ID FROM TB_PROD 
        WHERE PROD_ID >= 1000 AND RAND() < 0.2; -- 20%的产品有字段覆盖
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN prod_cursor;
    
    read_loop: LOOP
        FETCH prod_cursor INTO prod_id_val;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 随机选择1-3个字段进行覆盖
        IF RAND() < 0.8 THEN
            INSERT INTO PROD_OVRID_FIELD (
                ROWID, PROD_ID, FIELD_CDE
            ) VALUES (
                CONCAT('OVRD', LPAD(override_counter, 8, '0')),
                prod_id_val, 'PROD_NAME'
            );
            SET override_counter = override_counter + 1;
        END IF;
        
        IF RAND() < 0.6 THEN
            INSERT INTO PROD_OVRID_FIELD (
                ROWID, PROD_ID, FIELD_CDE
            ) VALUES (
                CONCAT('OVRD', LPAD(override_counter, 8, '0')),
                prod_id_val, 'PROD_DESC'
            );
            SET override_counter = override_counter + 1;
        END IF;
        
        IF RAND() < 0.4 THEN
            INSERT INTO PROD_OVRID_FIELD (
                ROWID, PROD_ID, FIELD_CDE
            ) VALUES (
                CONCAT('OVRD', LPAD(override_counter, 8, '0')),
                prod_id_val, 'RISK_LVL_CDE'
            );
            SET override_counter = override_counter + 1;
        END IF;
        
        IF RAND() < 0.3 THEN
            INSERT INTO PROD_OVRID_FIELD (
                ROWID, PROD_ID, FIELD_CDE
            ) VALUES (
                CONCAT('OVRD', LPAD(override_counter, 8, '0')),
                prod_id_val, 'PROD_STAT_CDE'
            );
            SET override_counter = override_counter + 1;
        END IF;
        
        -- 每100条记录提交一次
        IF override_counter % 100 = 0 THEN
            COMMIT;
        END IF;
        
    END LOOP;
    
    CLOSE prod_cursor;
    COMMIT;
END$$

DELIMITER ;

-- 执行存储过程生成覆盖字段数据
CALL GenerateOverrideData();

-- 删除存储过程
DROP PROCEDURE GenerateOverrideData;

-- =====================================================
-- 最终提交和清理
-- =====================================================

-- 恢复设置
SET foreign_key_checks = 1;
SET unique_checks = 1;
SET autocommit = 1;

-- 最终提交
COMMIT;

-- 显示生成的数据统计
SELECT 'Data Generation Completed!' AS STATUS;

SELECT 
    'TB_PROD' AS table_name, 
    COUNT(*) AS record_count 
FROM TB_PROD WHERE PROD_ID >= 1000
UNION ALL
SELECT 
    'CDE_DESC_VALUE', 
    COUNT(*) 
FROM CDE_DESC_VALUE
UNION ALL
SELECT 
    'TB_PROD_USER_DEFIN_EXT_FIELD', 
    COUNT(*) 
FROM TB_PROD_USER_DEFIN_EXT_FIELD WHERE PROD_ID >= 1000
UNION ALL
SELECT 
    'TB_DEBT_INSTM', 
    COUNT(*) 
FROM TB_DEBT_INSTM WHERE PROD_ID_DEBT_INSTM >= 1000
UNION ALL
SELECT 
    'TB_EQTY_LINK_INVST', 
    COUNT(*) 
FROM TB_EQTY_LINK_INVST WHERE PROD_ID_EQTY_LINK_INVST >= 1000
UNION ALL
SELECT 
    'TB_EQTY_LINK_INVST_UNDL_STOCK', 
    COUNT(*) 
FROM TB_EQTY_LINK_INVST_UNDL_STOCK WHERE PROD_ID_EQTY_LINK_INVST >= 1000
UNION ALL
SELECT 
    'TB_PROD_ALT_ID', 
    COUNT(*) 
FROM TB_PROD_ALT_ID WHERE PROD_ID >= 1000
UNION ALL
SELECT 
    'PROD_FORM_REQMT', 
    COUNT(*) 
FROM PROD_FORM_REQMT WHERE PROD_ID >= 1000
UNION ALL
SELECT 
    'PROD_RESTR_CUST_CTRY', 
    COUNT(*) 
FROM PROD_RESTR_CUST_CTRY WHERE PROD_ID >= 1000
UNION ALL
SELECT 
    'PROD_OVRID_FIELD', 
    COUNT(*) 
FROM PROD_OVRID_FIELD WHERE PROD_ID >= 1000;
