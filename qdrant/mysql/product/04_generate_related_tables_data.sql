-- =====================================================
-- Cognos关联表大量测试数据生成脚本 (第二部分)
-- 生成债券、股票挂钩投资、产品替代ID等关联表数据
-- 创建时间: 2025-01-04 14:40:00
-- 数据库: nl2sql
-- =====================================================

-- 设置字符集和优化参数
SET NAMES utf8mb4;
SET autocommit = 0;
SET unique_checks = 0;
SET foreign_key_checks = 0;

-- =====================================================
-- 4. 生成债券工具表数据 (约800条记录)
-- =====================================================

-- 清理现有债券数据
DELETE FROM TB_DEBT_INSTM WHERE PROD_ID_DEBT_INSTM >= 1000;

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateBondData$$

CREATE PROCEDURE GenerateBondData()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE prod_id_val DECIMAL(38,0);
    DECLARE bond_counter INT DEFAULT 1;
    
    -- 游标遍历所有债券产品
    DECLARE bond_cursor CURSOR FOR 
        SELECT PROD_ID FROM TB_PROD WHERE PROD_ID >= 1000 AND PROD_TYPE_CDE = 'BD';
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN bond_cursor;
    
    read_loop: LOOP
        FETCH bond_cursor INTO prod_id_val;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        INSERT INTO TB_DEBT_INSTM (
            PROD_ID_DEBT_INSTM, ISR_BOND_NAME, ISSUE_NUM, PROD_ISS_DT,
            COUPN_ANNL_RATE, YIELD_TO_MTUR_BID_PCT, BOND_STAT_CDE,
            CTRY_BOND_ISSUE_CDE, GRNTR_NAME, CPTL_TIER_TEXT, COUPN_TYPE,
            INDEX_FLT_RATE_NAME, BOND_FLT_SPRD_RATE, INT_BASIS_CALC_TEXT,
            INVST_SOLD_LEST_AMT, INVST_INCRM_SOLD_AMT, YIELD_BID_PCT, YIELD_OFFER_PCT
        ) VALUES (
            prod_id_val,
            CONCAT(
                ELT(1 + FLOOR(RAND() * 8), '香港政府', '中国政府', '汇丰银行', '中银香港', '恒生银行', '腾讯控股', '阿里巴巴', '中国移动'),
                '债券', YEAR(CURDATE()), '-', LPAD(bond_counter, 3, '0')
            ),
            CONCAT('BOND', YEAR(CURDATE()), LPAD(bond_counter, 6, '0')),
            DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 1095) DAY), -- 过去3年内发行
            1.0 + RAND() * 6.0, -- 年化利率 1.0% - 7.0%
            0.8 + RAND() * 6.2, -- 到期收益率 0.8% - 7.0%
            'A', -- 活跃状态
            ELT(1 + FLOOR(RAND() * 5), 'HK', 'CN', 'US', 'SG', 'UK'),
            ELT(1 + FLOOR(RAND() * 6), '香港政府', '中国政府', '汇丰银行', '中银香港', '恒生银行', '无担保'),
            ELT(1 + FLOOR(RAND() * 4), 'Tier 1', 'Tier 2', 'Senior', 'Subordinated'),
            ELT(1 + FLOOR(RAND() * 3), 'Fixed', 'Floating', 'Zero Coupon'),
            IF(RAND() < 0.3, ELT(1 + FLOOR(RAND() * 4), 'HIBOR', 'LIBOR', 'SOFR', 'SHIBOR'), NULL),
            IF(RAND() < 0.3, 0.5 + RAND() * 2.0, NULL), -- 浮动利差 0.5% - 2.5%
            ELT(1 + FLOOR(RAND() * 3), '30/360', 'Actual/365', 'Actual/360'),
            10000 + FLOOR(RAND() * 90000), -- 最小卖出金额 10K - 100K
            1000 + FLOOR(RAND() * 9000), -- 增量卖出金额 1K - 10K
            0.8 + RAND() * 6.2, -- 买入收益率
            1.0 + RAND() * 6.5  -- 卖出收益率
        );
        
        SET bond_counter = bond_counter + 1;
        
        -- 每100条记录提交一次
        IF bond_counter % 100 = 0 THEN
            COMMIT;
        END IF;
        
    END LOOP;
    
    CLOSE bond_cursor;
    COMMIT;
END$$

DELIMITER ;

-- 执行存储过程生成债券数据
CALL GenerateBondData();

-- 删除存储过程
DROP PROCEDURE GenerateBondData;

-- =====================================================
-- 5. 生成股票挂钩投资表数据 (约200条记录)
-- =====================================================

-- 清理现有股票挂钩投资数据
DELETE FROM TB_EQTY_LINK_INVST WHERE PROD_ID_EQTY_LINK_INVST >= 1000;

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateELIData$$

CREATE PROCEDURE GenerateELIData()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE prod_id_val DECIMAL(38,0);
    DECLARE eli_counter INT DEFAULT 1;
    
    -- 游标遍历所有股票挂钩投资产品
    DECLARE eli_cursor CURSOR FOR 
        SELECT PROD_ID FROM TB_PROD WHERE PROD_ID >= 1000 AND PROD_TYPE_CDE = 'ELI';
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN eli_cursor;
    
    read_loop: LOOP
        FETCH eli_cursor INTO prod_id_val;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        INSERT INTO TB_EQTY_LINK_INVST (
            PROD_ID_EQTY_LINK_INVST, PROD_EXTNL_CDE, PROD_EXTNL_TYPE_CDE,
            EQTY_LINK_INVST_TYPE_CDE, TRD_DT, DSCNT_BUY_PCT, DSCNT_SELL_PCT,
            YIELD_TO_MTUR_PCT, DEN_AMT, TRD_MIN_AMT, SUPT_AON_IND,
            PYMT_DT, VALN_DT, OFFER_TYPE_CDE, SETL_DT, LNCH_PROD_IND,
            PDCY_CALL_CDE, PDCY_KNOCK_IN_CDE
        ) VALUES (
            prod_id_val,
            CONCAT('EXT', LPAD(prod_id_val, 8, '0')),
            'STRUCTURED',
            ELT(1 + FLOOR(RAND() * 4), 'AUTO_CALLABLE', 'BARRIER_REVERSE_CONVERTIBLE', 'DIGITAL_COUPON', 'PHOENIX'),
            DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 365) DAY), -- 过去1年内交易
            RAND() * 5.0, -- 买入折扣 0% - 5%
            RAND() * 5.0, -- 卖出折扣 0% - 5%
            5.0 + RAND() * 15.0, -- 到期收益率 5% - 20%
            100000.00, -- 面额
            10000 + FLOOR(RAND() * 90000), -- 最小交易金额 10K - 100K
            ELT(1 + FLOOR(RAND() * 2), 'Y', 'N'),
            DATE_ADD(CURDATE(), INTERVAL FLOOR(RAND() * 90) DAY), -- 未来3个月内支付
            DATE_ADD(CURDATE(), INTERVAL FLOOR(RAND() * 30) DAY), -- 未来1个月内估值
            ELT(1 + FLOOR(RAND() * 3), 'FIXED', 'FLOATING', 'CALLABLE'),
            DATE_ADD(CURDATE(), INTERVAL (3 + FLOOR(RAND() * 7)) DAY), -- 3-10天后结算
            'Y',
            ELT(1 + FLOOR(RAND() * 3), 'M', 'Q', 'N'), -- 月度/季度/无
            ELT(1 + FLOOR(RAND() * 3), 'D', 'C', 'N')  -- 日度/连续/无
        );
        
        SET eli_counter = eli_counter + 1;
        
        -- 每50条记录提交一次
        IF eli_counter % 50 = 0 THEN
            COMMIT;
        END IF;
        
    END LOOP;
    
    CLOSE eli_cursor;
    COMMIT;
END$$

DELIMITER ;

-- 执行存储过程生成股票挂钩投资数据
CALL GenerateELIData();

-- 删除存储过程
DROP PROCEDURE GenerateELIData;

-- =====================================================
-- 6. 生成股票挂钩投资标的股票表数据 (约400条记录)
-- =====================================================

-- 清理现有标的股票数据
DELETE FROM TB_EQTY_LINK_INVST_UNDL_STOCK WHERE PROD_ID_EQTY_LINK_INVST >= 1000;

DELIMITER $$

DROP PROCEDURE IF EXISTS GenerateUnderlyingStockData$$

CREATE PROCEDURE GenerateUnderlyingStockData()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE prod_id_val DECIMAL(38,0);
    DECLARE stock_counter INT DEFAULT 1;
    DECLARE underlying_count INT;
    DECLARE i INT;
    
    -- 游标遍历所有股票挂钩投资产品
    DECLARE eli_cursor CURSOR FOR 
        SELECT PROD_ID_EQTY_LINK_INVST FROM TB_EQTY_LINK_INVST WHERE PROD_ID_EQTY_LINK_INVST >= 1000;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN eli_cursor;
    
    read_loop: LOOP
        FETCH eli_cursor INTO prod_id_val;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 每个ELI产品随机生成1-4个标的资产
        SET underlying_count = 1 + FLOOR(RAND() * 4);
        SET i = 1;
        
        WHILE i <= underlying_count DO
            INSERT INTO TB_EQTY_LINK_INVST_UNDL_STOCK (
                ROWID, PROD_ID_EQTY_LINK_INVST, INSTM_UNDL_CDE, INSTM_UNDL_TEXT,
                PROD_ID_UNDL_INSTM, PROD_STRK_PRC_AMT, PROD_KNOCK_IN_PRICE_AMT,
                PROD_INIT_SPOT_PRICE_AMT, PROD_BREAK_EVEN_PRC_AMT,
                PROD_CLOSE_PRC_AMT, PROD_EXER_PRC_AMT, SPRD_CNT, INSTM_ENTL_CNT,
                PROD_STRK_PRICE_INIT_PCT, PROD_KNOCK_IN_PRICE_INIT_PCT,
                PROD_FLR_PRC_AMT, PROD_BAR_PRC_AMT, PROD_KNOCK_IN_TRIG_PRC_AMT,
                PROD_EARL_CALL_TRIG_PRC_AMT, PROD_DOWN_OUT_BAR_PRC_AMT
            ) VALUES (
                CONCAT('STOCK', LPAD(stock_counter, 8, '0')),
                prod_id_val,
                ELT(i, 'HSI', '0700.HK', '9988.HK', '3690.HK', '1810.HK'), -- 恒指、腾讯、阿里、美团、小米
                ELT(i, '恒生指数', '腾讯控股', '阿里巴巴', '美团', '小米集团'),
                prod_id_val + i, -- 假设的标的产品ID
                20000 + RAND() * 30000, -- 行权价 20K - 50K
                (20000 + RAND() * 30000) * (0.7 + RAND() * 0.2), -- 敲入价格 (70%-90%行权价)
                25000 + RAND() * 25000, -- 初始现货价格 25K - 50K
                (20000 + RAND() * 30000) * (0.9 + RAND() * 0.1), -- 盈亏平衡价格
                20000 + RAND() * 30000, -- 收盘价格
                20000 + RAND() * 30000, -- 行使价格
                1000 + FLOOR(RAND() * 9000), -- 价差数量
                0.1 + RAND() * 0.9, -- 工具权利数量
                85.0 + RAND() * 15.0, -- 行权价格初始百分比 85% - 100%
                65.0 + RAND() * 20.0, -- 敲入价格初始百分比 65% - 85%
                (20000 + RAND() * 30000) * 0.8, -- 底价
                (20000 + RAND() * 30000) * 0.75, -- 障碍价格
                (20000 + RAND() * 30000) * 0.7, -- 敲入触发价格
                (20000 + RAND() * 30000) * 1.1, -- 提前赎回触发价格
                (20000 + RAND() * 30000) * 0.6  -- 向下敲出障碍价格
            );
            
            SET i = i + 1;
            SET stock_counter = stock_counter + 1;
        END WHILE;
        
        -- 每100条记录提交一次
        IF stock_counter % 100 = 0 THEN
            COMMIT;
        END IF;
        
    END LOOP;
    
    CLOSE eli_cursor;
    COMMIT;
END$$

DELIMITER ;

-- 执行存储过程生成标的股票数据
CALL GenerateUnderlyingStockData();

-- 删除存储过程
DROP PROCEDURE GenerateUnderlyingStockData;
