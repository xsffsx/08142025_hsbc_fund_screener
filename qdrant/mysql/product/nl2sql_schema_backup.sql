-- MySQL dump 10.13  Distrib 8.0.43, for Linux (aarch64)
--
-- Host: localhost    Database: nl2sql
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `CDE_DESC_VALUE`
--

DROP TABLE IF EXISTS `CDE_DESC_VALUE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CDE_DESC_VALUE` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CTRY_REC_CDE` char(2) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国家记录代码',
  `GRP_MEMBR_REC_CDE` char(4) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集团成员记录代码',
  `CDV_TYPE_CDE` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '代码值类型代码',
  `CDV_CDE` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '代码值代码',
  `CDV_DESC` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代码值描述',
  `CDV_PLL_DESC` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代码值并行语言描述',
  `CDV_SLL_DESC` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代码值第二语言描述',
  `CDV_DISP_SEQ_NUM` decimal(38,0) DEFAULT NULL COMMENT '代码值显示序号',
  `CDV_PARNT_TYPE_CDE` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代码值父类型代码',
  `CDV_PARNT_CDE` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代码值父代码',
  `REC_CMNT_TEXT` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '记录注释文本',
  `REC_CREAT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间',
  `REC_UPDT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '记录更新时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CDE_DESC_VALUE_UK1` (`CTRY_REC_CDE`,`GRP_MEMBR_REC_CDE`,`CDV_TYPE_CDE`,`CDV_CDE`)
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代码描述值表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LOG_EQTY_LINK_INVST`
--

DROP TABLE IF EXISTS `LOG_EQTY_LINK_INVST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LOG_EQTY_LINK_INVST` (
  `ROWID` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行ID',
  `LOG_ID` decimal(38,0) NOT NULL COMMENT '日志ID',
  `PROC_NAME` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '过程名称',
  `LOG_LEVEL` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '日志级别',
  `LOG_MSG` varchar(4000) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '日志消息',
  `SQL_ERRM` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'SQL错误消息',
  `ERRM_STAT` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误消息状态',
  `CTRY_CDE` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家代码',
  `ORGN_CDE` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组织代码',
  `PROD_ID` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品ID',
  `LOG_DATE` timestamp(6) NULL DEFAULT NULL COMMENT '日志日期',
  PRIMARY KEY (`ROWID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='股票挂钩投资日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROD_FORM_REQMT`
--

DROP TABLE IF EXISTS `PROD_FORM_REQMT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROD_FORM_REQMT` (
  `ROWID` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行ID',
  `PROD_ID` decimal(38,0) NOT NULL COMMENT '产品ID',
  `FORM_REQ_CDE` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '表单要求代码',
  `REC_CREAT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间',
  PRIMARY KEY (`ROWID`),
  UNIQUE KEY `PROD_FORM_REQMT_UK1` (`PROD_ID`,`FORM_REQ_CDE`),
  KEY `PROD_FORM_REQMT_IDX1` (`PROD_ID`),
  CONSTRAINT `FK_PROD_FORM_REQMT_PROD` FOREIGN KEY (`PROD_ID`) REFERENCES `TB_PROD` (`PROD_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表单要求表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROD_OVRID_FIELD`
--

DROP TABLE IF EXISTS `PROD_OVRID_FIELD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROD_OVRID_FIELD` (
  `ROWID` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行ID',
  `PROD_ID` decimal(38,0) NOT NULL COMMENT '产品ID',
  `FIELD_CDE` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段代码',
  `REC_CREAT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间',
  PRIMARY KEY (`ROWID`),
  UNIQUE KEY `PROD_OVRID_FIELD_UK1` (`PROD_ID`,`FIELD_CDE`),
  KEY `PROD_OVRID_FIELD_IDX1` (`PROD_ID`),
  CONSTRAINT `FK_PROD_OVRID_FIELD_PROD` FOREIGN KEY (`PROD_ID`) REFERENCES `TB_PROD` (`PROD_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品覆盖字段表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROD_RESTR_CUST_CTRY`
--

DROP TABLE IF EXISTS `PROD_RESTR_CUST_CTRY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROD_RESTR_CUST_CTRY` (
  `ROWID` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行ID',
  `PROD_ID` decimal(38,0) NOT NULL COMMENT '产品ID',
  `CTRY_ISO_CDE` char(2) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国家ISO代码',
  `RESTR_CTRY_TYPE_CDE` char(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '限制国家类型代码',
  `RESTR_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '限制代码',
  `REC_CREAT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间',
  `REC_UPDT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '记录更新时间',
  PRIMARY KEY (`ROWID`),
  UNIQUE KEY `PROD_RESTR_CUST_CTRY_UK1` (`PROD_ID`,`CTRY_ISO_CDE`,`RESTR_CTRY_TYPE_CDE`),
  KEY `PROD_RESTR_CUST_CTRY_IDX1` (`PROD_ID`),
  KEY `PROD_RESTR_CUST_CTRY_IDX2` (`CTRY_ISO_CDE`,`RESTR_CTRY_TYPE_CDE`,`RESTR_CDE`,`PROD_ID`),
  CONSTRAINT `FK_PROD_RESTR_CUST_CTRY_PROD` FOREIGN KEY (`PROD_ID`) REFERENCES `TB_PROD` (`PROD_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品限制客户国家表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TB_DEBT_INSTM`
--

DROP TABLE IF EXISTS `TB_DEBT_INSTM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TB_DEBT_INSTM` (
  `PROD_ID_DEBT_INSTM` decimal(38,0) NOT NULL COMMENT '债券工具产品ID',
  `ISR_BOND_NAME` varchar(70) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发行人债券名称',
  `ISSUE_NUM` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发行编号',
  `PROD_ISS_DT` date DEFAULT NULL COMMENT '产品发行日期',
  `PDCY_COUPN_PYMT_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '周期性票息支付代码',
  `COUPN_ANNL_RATE` decimal(20,6) DEFAULT NULL COMMENT '票息年利率',
  `COUPN_EXT_INSTM_RATE` decimal(20,6) DEFAULT NULL COMMENT '票息外部工具利率',
  `PYMT_COUPN_NEXT_DT` date DEFAULT NULL COMMENT '下次票息支付日期',
  `FLEX_MAT_OPT_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '灵活到期选择指示',
  `INT_IND_ACCR_AMT` decimal(25,10) DEFAULT NULL COMMENT '利息指示应计金额',
  `INVST_INCRM_MIN_AMT` decimal(21,6) DEFAULT NULL COMMENT '投资增量最小金额',
  `PROD_BOD_LOT_QTY_CNT` decimal(20,6) DEFAULT NULL COMMENT '产品董事会批量数量',
  `MTUR_EXT_DT` date DEFAULT NULL COMMENT '到期延期日期',
  `SUB_DEBT_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '次级债务指示',
  `BOND_STAT_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '债券状态代码',
  `CTRY_BOND_ISSUE_CDE` char(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '债券发行国家代码',
  `GRNTR_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '担保人名称',
  `CPTL_TIER_TEXT` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资本层级文本',
  `COUPN_TYPE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '票息类型',
  `COUPN_PREV_RATE` decimal(20,6) DEFAULT NULL COMMENT '票息前一利率',
  `INDEX_FLT_RATE_NAME` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '指数浮动利率名称',
  `BOND_FLT_SPRD_RATE` decimal(20,4) DEFAULT NULL COMMENT '债券浮动利差率',
  `COUPN_CURR_START_DT` date DEFAULT NULL COMMENT '票息当前开始日期',
  `COUPN_PREV_START_DT` date DEFAULT NULL COMMENT '票息前一开始日期',
  `BOND_CALL_NEXT_DT` date DEFAULT NULL COMMENT '债券下次赎回日期',
  `INT_BASIS_CALC_TEXT` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '利息基础计算文本',
  `INT_ACCR_DAY_CNT` decimal(20,6) DEFAULT NULL COMMENT '利息应计天数',
  `INVST_SOLD_LEST_AMT` decimal(21,6) DEFAULT NULL COMMENT '投资卖出最少金额',
  `INVST_INCRM_SOLD_AMT` decimal(21,6) DEFAULT NULL COMMENT '投资增量卖出金额',
  `SHR_BID_CNT` decimal(20,6) DEFAULT NULL COMMENT '股份买入数量',
  `SHR_OFFR_CNT` decimal(20,6) DEFAULT NULL COMMENT '股份卖出数量',
  `PROD_CLOSE_BID_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品收盘买入价格金额',
  `PROD_CLOSE_OFFR_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品收盘卖出价格金额',
  `BOND_CLOSE_DT` date DEFAULT NULL COMMENT '债券收盘日期',
  `BOND_SETL_DT` date DEFAULT NULL COMMENT '债券结算日期',
  `DSCNT_MRGN_BSEL_PCT` decimal(20,4) DEFAULT NULL COMMENT '折扣保证金基础卖出百分比',
  `DSCNT_MRGN_BBUY_PCT` decimal(20,4) DEFAULT NULL COMMENT '折扣保证金基础买入百分比',
  `PRC_BOND_RECV_DT_TM` timestamp(6) NULL DEFAULT NULL COMMENT '债券价格接收时间',
  `YIELD_OFFER_TEXT` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收益率报价文本',
  `COUPN_ANNL_TEXT` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '票息年度文本',
  `YIELD_EFF_DT` date DEFAULT NULL COMMENT '收益率生效日期',
  `YIELD_BID_PCT` decimal(20,6) DEFAULT NULL COMMENT '收益率买入百分比',
  `YIELD_TO_CALL_BID_PCT` decimal(20,6) DEFAULT NULL COMMENT '到期赎回收益率买入百分比',
  `YIELD_TO_MTUR_BID_PCT` decimal(20,6) DEFAULT NULL COMMENT '到期收益率买入百分比',
  `YIELD_BID_CLOSE_PCT` decimal(20,6) DEFAULT NULL COMMENT '收益率买入收盘百分比',
  `YIELD_OFFER_PCT` decimal(20,6) DEFAULT NULL COMMENT '收益率卖出百分比',
  `YIELD_TO_CALL_OFFER_PCT` decimal(20,6) DEFAULT NULL COMMENT '到期赎回收益率卖出百分比',
  `YIELD_TO_MTUR_OFFER_TEXT` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '到期收益率卖出文本',
  `YIELD_OFFER_CLOSE_PCT` decimal(20,6) DEFAULT NULL COMMENT '收益率卖出收盘百分比',
  `YIELD_DT` date DEFAULT NULL COMMENT '收益率日期',
  `ISR_DESC` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发行人描述',
  `SR_TYPE_CDE` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '高级类型代码',
  `PROD_GBM_BID_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品GBM买入价格金额',
  `PROD_GBM_OFFR_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品GBM卖出价格金额',
  PRIMARY KEY (`PROD_ID_DEBT_INSTM`),
  CONSTRAINT `FK_DEBT_INSTM_PROD` FOREIGN KEY (`PROD_ID_DEBT_INSTM`) REFERENCES `TB_PROD` (`PROD_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='债券工具表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TB_EQTY_LINK_INVST`
--

DROP TABLE IF EXISTS `TB_EQTY_LINK_INVST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TB_EQTY_LINK_INVST` (
  `PROD_ID_EQTY_LINK_INVST` decimal(38,0) NOT NULL COMMENT '股票挂钩投资产品ID',
  `PROD_EXTNL_CDE` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品外部代码',
  `PROD_EXTNL_TYPE_CDE` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品外部类型代码',
  `EQTY_LINK_INVST_TYPE_CDE` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '股票挂钩投资类型代码',
  `TRD_DT` date DEFAULT NULL COMMENT '交易日期',
  `DSCNT_BUY_PCT` decimal(20,4) DEFAULT NULL COMMENT '折扣买入百分比',
  `DSCNT_SELL_PCT` decimal(20,4) DEFAULT NULL COMMENT '折扣卖出百分比',
  `YIELD_TO_MTUR_PCT` decimal(20,6) DEFAULT NULL COMMENT '到期收益率百分比',
  `DEN_AMT` decimal(18,3) DEFAULT NULL COMMENT '面额金额',
  `TRD_MIN_AMT` decimal(18,3) DEFAULT NULL COMMENT '交易最小金额',
  `SUPT_AON_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支持全部或无指示',
  `PYMT_DT` date DEFAULT NULL COMMENT '支付日期',
  `VALN_DT` date DEFAULT NULL COMMENT '估值日期',
  `OFFER_TYPE_CDE` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '报价类型代码',
  `CUST_SELL_QTA_CNT` decimal(38,0) DEFAULT NULL COMMENT '客户卖出配额数量',
  `RULE_QTA_ALTMT_CDE` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规则配额分配代码',
  `SETL_DT` date DEFAULT NULL COMMENT '结算日期',
  `LNCH_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发布产品指示',
  `RTRV_PROD_EXTNL_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '检索产品外部指示',
  `PROD_EXTNL_CAT_CDE` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品外部类别代码',
  `PDCY_CALL_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '周期性赎回代码',
  `PDCY_KNOCK_IN_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '周期性敲入代码',
  PRIMARY KEY (`PROD_ID_EQTY_LINK_INVST`),
  KEY `EQTY_LINK_INVST_IDX1` (`YIELD_TO_MTUR_PCT`,`PROD_ID_EQTY_LINK_INVST`),
  CONSTRAINT `FK_EQTY_LINK_INVST_PROD` FOREIGN KEY (`PROD_ID_EQTY_LINK_INVST`) REFERENCES `TB_PROD` (`PROD_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='股票挂钩投资表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TB_EQTY_LINK_INVST_UNDL_STOCK`
--

DROP TABLE IF EXISTS `TB_EQTY_LINK_INVST_UNDL_STOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TB_EQTY_LINK_INVST_UNDL_STOCK` (
  `ROWID` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行ID',
  `PROD_ID_EQTY_LINK_INVST` decimal(38,0) NOT NULL COMMENT '股票挂钩投资产品ID',
  `INSTM_UNDL_CDE` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工具标的代码',
  `INSTM_UNDL_TEXT` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工具标的文本',
  `CRNCY_INSTM_UNDL_PRICE_CDE` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工具标的价格货币代码',
  `PROD_STRK_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品行权价格金额',
  `PROD_STRK_CALL_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品行权看涨价格金额',
  `PROD_STRK_PUT_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品行权看跌价格金额',
  `PROD_CLOSE_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品收盘价格金额',
  `PROD_CLOSE_LOW_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品收盘最低价格金额',
  `PROD_CLOSE_UPPR_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品收盘最高价格金额',
  `PROD_CLOSE_PUT_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品收盘看跌价格金额',
  `PROD_CLOSE_CALL_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品收盘看涨价格金额',
  `PROD_EXER_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品行使价格金额',
  `PROD_BREAK_EVEN_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品盈亏平衡价格金额',
  `PROD_BREAK_EVEN_LOW_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品盈亏平衡最低价格金额',
  `PROD_BREAK_EVEN_UPPR_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品盈亏平衡最高价格金额',
  `PROD_BREAK_EVEN_PUT_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品盈亏平衡看跌价格金额',
  `PROD_BREAK_EVEN_CALL_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品盈亏平衡看涨价格金额',
  `SPRD_CNT` decimal(38,0) DEFAULT NULL COMMENT '价差数量',
  `INSTM_ENTL_CNT` decimal(16,4) DEFAULT NULL COMMENT '工具权利数量',
  `PROD_KNOCK_IN_PRICE_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品敲入价格金额',
  `PROD_STRK_PRICE_INIT_PCT` decimal(13,5) DEFAULT NULL COMMENT '产品行权价格初始百分比',
  `PROD_STRK_CALL_PRICE_INIT_PCT` decimal(13,5) DEFAULT NULL COMMENT '产品行权看涨价格初始百分比',
  `PROD_KNOCK_IN_PRICE_INIT_PCT` decimal(13,5) DEFAULT NULL COMMENT '产品敲入价格初始百分比',
  `REC_CREAT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间',
  `REC_UPDT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '记录更新时间',
  `PROD_ID_UNDL_INSTM` decimal(38,0) NOT NULL COMMENT '标的工具产品ID',
  `PROD_INIT_SPOT_PRICE_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品初始现货价格金额',
  `PROD_FLR_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品底价金额',
  `PROD_BAR_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品障碍价格金额',
  `PROD_KNOCK_IN_TRIG_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品敲入触发价格金额',
  `PROD_EARL_CALL_TRIG_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品提前赎回触发价格金额',
  `PROD_EXPI_CLOSE_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品到期收盘价格金额',
  `PROD_DOWN_OUT_BAR_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品向下敲出障碍价格金额',
  PRIMARY KEY (`ROWID`),
  UNIQUE KEY `EQTY_LINK_INVST_UNDL_STOCK_UK1` (`PROD_ID_EQTY_LINK_INVST`,`INSTM_UNDL_CDE`),
  KEY `EQTY_LINK_INVST_UNDL_STOCK_IDX1` (`PROD_ID_EQTY_LINK_INVST`),
  KEY `EQTY_LINK_INVST_UNDL_STOCK_IDX2` (`PROD_ID_UNDL_INSTM`,`PROD_ID_EQTY_LINK_INVST`),
  CONSTRAINT `FK_EQTY_LINK_INVST_UNDL_STOCK_EQTY` FOREIGN KEY (`PROD_ID_EQTY_LINK_INVST`) REFERENCES `TB_EQTY_LINK_INVST` (`PROD_ID_EQTY_LINK_INVST`) ON DELETE CASCADE,
  CONSTRAINT `FK_EQTY_LINK_INVST_UNDL_STOCK_UNDL` FOREIGN KEY (`PROD_ID_UNDL_INSTM`) REFERENCES `TB_PROD` (`PROD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='股票挂钩投资标的股票表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TB_PROD`
--

DROP TABLE IF EXISTS `TB_PROD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TB_PROD` (
  `CTRY_REC_CDE` char(2) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国家记录代码',
  `GRP_MEMBR_REC_CDE` char(4) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集团成员记录代码',
  `PROD_ID` decimal(38,0) NOT NULL COMMENT '产品ID',
  `PROD_TYPE` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品类型',
  `PROD_TYPE_CDE` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品类型代码',
  `PROD_SUBTP_CDE` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品子类型代码',
  `PROD_CDE` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品代码',
  `PROD_ALT_PRIM_NUM` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品替代主编号',
  `PROD_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品名称',
  `PROD_PLL_NAME` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品并行语言名称',
  `PROD_SLL_NAME` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品第二语言名称',
  `PROD_SHRT_NAME` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品简称',
  `PROD_SHRT_PLL_NAME` varchar(90) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品简称并行语言',
  `PROD_SHRT_SLL_NAME` varchar(90) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品简称第二语言',
  `PROD_DESC` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品描述',
  `PROD_PLL_DESC` varchar(900) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品描述并行语言',
  `PROD_SLL_DESC` varchar(900) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品描述第二语言',
  `ASET_CLASS_CDE` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资产类别代码',
  `PROD_STAT_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品状态代码',
  `CCY_PROD_CDE` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品货币代码',
  `RISK_LVL_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '风险等级代码',
  `PRD_PROD_CDE` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '周期产品代码',
  `PRD_PROD_NUM` decimal(38,0) DEFAULT NULL COMMENT '周期产品编号',
  `TERM_REMAIN_DAY_CNT` decimal(38,0) DEFAULT NULL COMMENT '剩余期限天数',
  `PROD_LNCH_DT` date DEFAULT NULL COMMENT '产品发布日期',
  `PROD_MTUR_DT` date DEFAULT NULL COMMENT '产品到期日期',
  `MKT_INVST_CDE` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '市场投资代码',
  `SECT_INVST_CDE` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '行业投资代码',
  `ALLOW_BUY_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许买入产品指示',
  `ALLOW_SELL_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许卖出产品指示',
  `ALLOW_BUY_UT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许按单位买入产品指示',
  `ALLOW_BUY_AMT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许按金额买入产品指示',
  `ALLOW_SELL_UT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许按单位卖出产品指示',
  `ALLOW_SELL_AMT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许按金额卖出产品指示',
  `ALLOW_SELL_MIP_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许卖出MIP产品指示',
  `ALLOW_SELL_MIP_UT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许按单位卖出MIP产品指示',
  `ALLOW_SELL_MIP_AMT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许按金额卖出MIP产品指示',
  `ALLOW_SW_IN_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许转入产品指示',
  `ALLOW_SW_IN_UT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许按单位转入产品指示',
  `ALLOW_SW_IN_AMT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许按金额转入产品指示',
  `ALLOW_SW_OUT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许转出产品指示',
  `ALLOW_SW_OUT_UT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许按单位转出产品指示',
  `ALLOW_SW_OUT_AMT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '允许按金额转出产品指示',
  `INCM_CHAR_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收入特征产品指示',
  `CPTL_GURNT_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资本保证产品指示',
  `YIELD_ENHN_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收益增强产品指示',
  `GRWTH_CHAR_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '成长特征产品指示',
  `PRTY_PROD_SRCH_RSULT_NUM` decimal(38,0) DEFAULT NULL COMMENT '优先产品搜索结果编号',
  `AVAIL_MKT_INFO_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '可用市场信息指示',
  `PRD_RTRN_AVG_NUM` decimal(13,5) DEFAULT NULL COMMENT '周期回报平均数',
  `RTRN_VOLTL_AVG_PCT` decimal(13,5) DEFAULT NULL COMMENT '回报波动平均百分比',
  `DMY_PROD_SUBTP_REC_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '虚拟产品子类型记录指示',
  `DISP_COM_PROD_SRCH_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '显示通用产品搜索指示',
  `MRK_TO_MKT_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '按市价计价指示',
  `PROD_NARR_TEXT` varchar(1200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品叙述文本',
  `CTRY_PROD_TRADE_1_CDE` char(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家产品交易1代码',
  `CTRY_PROD_TRADE_2_CDE` char(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家产品交易2代码',
  `CTRY_PROD_TRADE_3_CDE` char(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家产品交易3代码',
  `CTRY_PROD_TRADE_4_CDE` char(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家产品交易4代码',
  `CTRY_PROD_TRADE_5_CDE` char(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家产品交易5代码',
  `BUS_START_TM` timestamp(6) NULL DEFAULT NULL COMMENT '业务开始时间',
  `BUS_END_TM` timestamp(6) NULL DEFAULT NULL COMMENT '业务结束时间',
  `INTRO_PROD_CURR_PRD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '介绍产品当前周期指示',
  `PROD_TOP_SELL_RANK_NUM` decimal(38,0) DEFAULT NULL COMMENT '产品热销排名编号',
  `PROD_TOP_PERFM_RANK_NUM` decimal(38,0) DEFAULT NULL COMMENT '产品顶级表现排名编号',
  `PROD_TOP_YIELD_RANK_NUM` decimal(38,0) DEFAULT NULL COMMENT '产品顶级收益排名编号',
  `PROD_ISSUE_CROS_REFER_DT` date DEFAULT NULL COMMENT '产品发行交叉参考日期',
  `PROD_CMNT_TEXT` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品注释文本',
  `CCY_INVST_CDE` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '投资货币代码',
  `BCHMK_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '基准名称',
  `BCHMK_PLL_NAME` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '基准并行语言名称',
  `BCHMK_SLL_NAME` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '基准第二语言名称',
  `TRD_FIRST_DT` date DEFAULT NULL COMMENT '首次交易日期',
  `LOAN_PROD_OD_MRGN_PCT` decimal(20,6) DEFAULT NULL COMMENT '贷款产品透支保证金百分比',
  `QTY_UNIT_PROD_CDE` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数量单位产品代码',
  `PROD_LOC_CDE` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品位置代码',
  `PROD_TAX_FREE_WRAP_ACT_STA_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品免税包装账户状态代码',
  `PRC_EFF_DT` date DEFAULT NULL COMMENT '价格生效日期',
  `CCY_PROD_MKT_PRC_CDE` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品市场价格货币代码',
  `PROD_BID_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品买入价格金额',
  `PROD_OFFR_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品卖出价格金额',
  `PROD_MKT_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品市场价格金额',
  `PROD_NAV_PRC_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品净值价格金额',
  `PROD_PRC_UPDT_DT_TM` timestamp(6) NULL DEFAULT NULL COMMENT '产品价格更新时间',
  `DCML_PLACE_TRADE_UNIT_NUM` decimal(38,0) DEFAULT NULL COMMENT '小数位交易单位数',
  `PROD_OFFR_CHANL_UPDT_DT_TM` timestamp(6) NULL DEFAULT NULL COMMENT '产品提供渠道更新时间',
  `PLDG_LIMIT_ASSOC_ACCT_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '质押限制关联账户指示',
  `PROD_OWN_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品拥有代码',
  `FORGN_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '外国产品指示',
  `PROD_STAT_UPDT_DT_TM` timestamp(6) NULL DEFAULT NULL COMMENT '产品状态更新时间',
  `ASET_CAT_EXTNL_CDE` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资产类别外部代码',
  `PROD_MKT_PRICE_PREV_AMT` decimal(21,6) DEFAULT NULL COMMENT '产品市场价格前一金额',
  `REC_CREAT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间',
  `REC_UPDT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '记录更新时间',
  `REC_ONLN_UPDT_DT_TM` timestamp(6) NULL DEFAULT NULL COMMENT '记录在线更新时间',
  `PRC_INP_DT` date DEFAULT NULL COMMENT '价格输入日期',
  `CHRG_CAT_CDE` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收费类别代码',
  `SUPT_RCBL_CASH_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支持应收现金产品指示',
  `SUPT_RCBL_SCRIP_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支持应收股票产品指示',
  `ASET_UNDER_MGMT_CHRG_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '管理资产收费产品指示',
  `INVST_IMIG_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '投资移民产品指示',
  `RESTR_INVSTR_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '限制投资者产品指示',
  `PROD_INVST_OBJ_TEXT` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品投资目标文本',
  `INVST_INIT_MIN_AMT` decimal(21,6) DEFAULT NULL COMMENT '投资初始最小金额',
  `NO_SCRIB_FEE_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '无认购费产品指示',
  `TOP_SELL_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '热销产品指示',
  `TOP_PERFM_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '顶级表现产品指示',
  `INVST_INIT_MAX_AMT` decimal(21,6) DEFAULT NULL COMMENT '投资初始最大金额',
  `RCMND_PROD_DECSN_CDE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '推荐产品决策代码',
  `ASET_TEXT` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资产文本',
  `PROD_DERVT_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品衍生品代码',
  `PROD_DERVT_RVS_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品衍生品逆向代码',
  `PRD_DERV_RVS_EFF_DT` date DEFAULT NULL COMMENT '周期衍生品逆向生效日期',
  `EQTY_UNDL_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '股票标的指示',
  `WLTH_ACCUM_GOAL_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '财富积累目标指示',
  `PRTY_WLTH_ACCUM_GOAL_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优先财富积累目标代码',
  `PLAN_FOR_RTIRE_GOAL_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '退休规划目标指示',
  `PRTY_PLN_FOR_RTIRE_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优先退休规划代码',
  `EDUC_GOAL_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '教育目标指示',
  `PRTY_EDUC_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优先教育代码',
  `LIVE_IN_RTIRE_GOAL_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '退休生活目标指示',
  `PRTY_LIVE_IN_RTIRE_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优先退休生活代码',
  `PROTC_GOAL_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '保护目标指示',
  `PRTY_PROTC_CDE` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优先保护代码',
  `MNG_SOLN_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '管理解决方案指示',
  `PRD_INVST_TNOR_NUM` decimal(38,0) DEFAULT NULL COMMENT '周期投资期限数',
  `GEO_RISK_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地理风险指示',
  `PROD_LQDY_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品流动性指示',
  `RVRSE_ENQ_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '逆向查询产品指示',
  `PROD_INVST_TYPE_CDE` char(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品投资类型代码',
  `DIVR_NUM` decimal(21,6) DEFAULT NULL COMMENT '分散数',
  `PRICE_HIST_SRCE_RDY_STAT_CDE` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '价格历史来源准备状态代码',
  `PRICE_HIST_DELVR_DT_TM` timestamp(6) NULL DEFAULT NULL COMMENT '价格历史交付时间',
  `ISLM_PROD_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '伊斯兰产品指示',
  `CNTL_ADVC_IND` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '控制建议指示',
  `ASET_VOLTL_CLASS_MAJR_PRNT_CDE` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资产波动类别主要父代码',
  `REC_STGC_FIN_PLN_UPDT_DT_TM` timestamp(6) NULL DEFAULT NULL COMMENT '记录战略财务计划更新时间',
  `PROD_PARNT_ID` decimal(38,0) DEFAULT NULL COMMENT '产品父ID',
  PRIMARY KEY (`PROD_ID`),
  UNIQUE KEY `PROD_UK1` (`PROD_CDE`),
  KEY `PROD_IDX1` (`GRP_MEMBR_REC_CDE`,`CTRY_REC_CDE`,`PROD_ID`),
  KEY `PROD_IDX2` (`ASET_CLASS_CDE`,`PROD_ID`),
  KEY `PROD_IDX3` (`MKT_INVST_CDE`,`PROD_ID`),
  KEY `PROD_IDX4` (`TERM_REMAIN_DAY_CNT`,`PROD_ID`),
  KEY `PROD_IDX5` (`PROD_ALT_PRIM_NUM`,`GRP_MEMBR_REC_CDE`,`CTRY_REC_CDE`,`PROD_ID`),
  KEY `PROD_IDX6` (`PROD_PARNT_ID`),
  KEY `PROD_IDX7` (`PROD_STAT_CDE`,`PROD_TYPE_CDE`),
  KEY `PROD_IDX8` (`PROD_PRC_UPDT_DT_TM`),
  CONSTRAINT `FK_PROD_PARENT` FOREIGN KEY (`PROD_PARNT_ID`) REFERENCES `TB_PROD` (`PROD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TB_PROD_ALT_ID`
--

DROP TABLE IF EXISTS `TB_PROD_ALT_ID`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TB_PROD_ALT_ID` (
  `ROWID` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行ID',
  `CTRY_REC_CDE` char(2) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国家记录代码',
  `GRP_MEMBR_REC_CDE` char(4) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集团成员记录代码',
  `PROD_ID` decimal(38,0) NOT NULL COMMENT '产品ID',
  `PROD_CDE_ALT_CLASS_CDE` char(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品代码替代类别代码',
  `PROD_TYPE_CDE` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品类型代码',
  `PROD_ALT_NUM` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品替代编号',
  `REC_CREAT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间',
  `REC_UPDT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '记录更新时间',
  PRIMARY KEY (`ROWID`),
  UNIQUE KEY `PROD_ALT_ID_UK1` (`PROD_ID`,`PROD_CDE_ALT_CLASS_CDE`),
  KEY `PROD_ALT_ID_IDX1` (`PROD_ID`),
  KEY `PROD_ALT_ID_IDX2` (`PROD_ALT_NUM`,`PROD_CDE_ALT_CLASS_CDE`,`GRP_MEMBR_REC_CDE`,`CTRY_REC_CDE`,`PROD_ID`),
  CONSTRAINT `FK_PROD_ALT_ID_PROD` FOREIGN KEY (`PROD_ID`) REFERENCES `TB_PROD` (`PROD_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品替代ID表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TB_PROD_USER_DEFIN_EXT_FIELD`
--

DROP TABLE IF EXISTS `TB_PROD_USER_DEFIN_EXT_FIELD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TB_PROD_USER_DEFIN_EXT_FIELD` (
  `ROWID` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行ID',
  `PROD_ID` decimal(38,0) NOT NULL COMMENT '产品ID',
  `FIELD_TYPE_CDE` char(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段类型代码',
  `FIELD_CDE` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段代码',
  `FIELD_SEQ_NUM` decimal(38,0) NOT NULL COMMENT '字段序号',
  `CTRY_REC_CDE` char(2) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国家记录代码',
  `GRP_MEMBR_REC_CDE` char(4) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '集团成员记录代码',
  `FIELD_DATA_TYPE_TEXT` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字段数据类型文本',
  `FIELD_CHAR_VALUE_TEXT` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字段字符值文本',
  `FIELD_STRNG_VALUE_TEXT` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字段字符串值文本',
  `FIELD_INTG_VALUE_NUM` decimal(38,0) DEFAULT NULL COMMENT '字段整数值',
  `FIELD_DCML_VALUE_NUM` decimal(21,6) DEFAULT NULL COMMENT '字段小数值',
  `FIELD_DT_VALUE_DT` date DEFAULT NULL COMMENT '字段日期值',
  `FIELD_TS_VALUE_DT_TM` timestamp(6) NULL DEFAULT NULL COMMENT '字段时间戳值',
  `REC_CREAT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间',
  `REC_UPDT_DT_TM` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '记录更新时间',
  PRIMARY KEY (`ROWID`),
  UNIQUE KEY `PROD_USER_DEFIN_EXT_FIELD_UK1` (`PROD_ID`,`FIELD_TYPE_CDE`,`FIELD_CDE`,`FIELD_SEQ_NUM`),
  KEY `PROD_USER_DEFIN_EXT_FIELD_IDX1` (`PROD_ID`),
  KEY `PROD_USER_DEFIN_EXT_FIELD_IDX2` (`FIELD_TYPE_CDE`,`FIELD_CDE`),
  CONSTRAINT `FK_PROD_USER_DEFIN_EXT_FIELD_PROD` FOREIGN KEY (`PROD_ID`) REFERENCES `TB_PROD` (`PROD_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品用户自定义扩展字段表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agent` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'æ™ºèƒ½ä½“åç§°',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT 'æ™ºèƒ½ä½“æè¿°',
  `avatar` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'å¤´åƒURL',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'draft' COMMENT 'çŠ¶æ€ï¼šdraft-å¾…å‘å¸ƒï¼Œpublished-å·²å‘å¸ƒï¼Œoffline-å·²ä¸‹çº¿',
  `prompt` text COLLATE utf8mb4_unicode_ci COMMENT 'è‡ªå®šä¹‰Prompté…ç½®',
  `category` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'åˆ†ç±»',
  `admin_id` bigint DEFAULT NULL COMMENT 'ç®¡ç†å‘˜ID',
  `tags` text COLLATE utf8mb4_unicode_ci COMMENT 'æ ‡ç­¾ï¼Œé€—å·åˆ†éš”',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  PRIMARY KEY (`id`),
  KEY `idx_agent_name` (`name`),
  KEY `idx_agent_status` (`status`),
  KEY `idx_agent_category` (`category`),
  KEY `idx_agent_admin_id` (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='æ™ºèƒ½ä½“è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `agent_datasource`
--

DROP TABLE IF EXISTS `agent_datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agent_datasource` (
  `id` int NOT NULL AUTO_INCREMENT,
  `agent_id` int NOT NULL COMMENT 'æ™ºèƒ½ä½“ID',
  `datasource_id` int NOT NULL COMMENT 'æ•°æ®æºID',
  `is_active` tinyint DEFAULT '1' COMMENT 'æ˜¯å¦å¯ç”¨ï¼š0-ç¦ç”¨ï¼Œ1-å¯ç”¨',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_agent_datasource` (`agent_id`,`datasource_id`),
  KEY `idx_ads_agent_id` (`agent_id`),
  KEY `idx_ads_datasource_id` (`datasource_id`),
  KEY `idx_ads_is_active` (`is_active`),
  CONSTRAINT `agent_datasource_ibfk_1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE CASCADE,
  CONSTRAINT `agent_datasource_ibfk_2` FOREIGN KEY (`datasource_id`) REFERENCES `datasource` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='æ™ºèƒ½ä½“æ•°æ®æºå…³è”è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `agent_knowledge`
--

DROP TABLE IF EXISTS `agent_knowledge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agent_knowledge` (
  `id` int NOT NULL AUTO_INCREMENT,
  `agent_id` int NOT NULL COMMENT 'æ™ºèƒ½ä½“ID',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'çŸ¥è¯†æ ‡é¢˜',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT 'çŸ¥è¯†å†…å®¹',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'document' COMMENT 'çŸ¥è¯†ç±»åž‹ï¼šdocument-æ–‡æ¡£ï¼Œqa-é—®ç­”ï¼Œfaq-å¸¸è§é—®é¢˜',
  `category` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'çŸ¥è¯†åˆ†ç±»',
  `tags` text COLLATE utf8mb4_unicode_ci COMMENT 'æ ‡ç­¾ï¼Œé€—å·åˆ†éš”',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'active' COMMENT 'çŠ¶æ€ï¼šactive-å¯ç”¨ï¼Œinactive-ç¦ç”¨',
  `source_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'æ¥æºURL',
  `file_path` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'æ–‡ä»¶è·¯å¾„',
  `file_size` bigint DEFAULT NULL COMMENT 'æ–‡ä»¶å¤§å°ï¼ˆå­—èŠ‚ï¼‰',
  `file_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'æ–‡ä»¶ç±»åž‹',
  `embedding_status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'pending' COMMENT 'å‘é‡åŒ–çŠ¶æ€ï¼špending-å¾…å¤„ç†ï¼Œprocessing-å¤„ç†ä¸­ï¼Œcompleted-å·²å®Œæˆï¼Œfailed-å¤±è´¥',
  `creator_id` bigint DEFAULT NULL COMMENT 'åˆ›å»ºè€…ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  PRIMARY KEY (`id`),
  KEY `idx_ak_agent_id` (`agent_id`),
  KEY `idx_ak_title` (`title`),
  KEY `idx_ak_type` (`type`),
  KEY `idx_ak_status` (`status`),
  KEY `idx_ak_category` (`category`),
  KEY `idx_ak_embedding_status` (`embedding_status`),
  CONSTRAINT `agent_knowledge_ibfk_1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='æ™ºèƒ½ä½“çŸ¥è¯†è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `agent_preset_question`
--

DROP TABLE IF EXISTS `agent_preset_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agent_preset_question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `agent_id` int NOT NULL COMMENT 'æ™ºèƒ½ä½“ID',
  `question` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'é¢„è®¾é—®é¢˜å†…å®¹',
  `sort_order` int DEFAULT '0' COMMENT 'æŽ’åºé¡ºåº',
  `is_active` tinyint DEFAULT '1' COMMENT 'æ˜¯å¦å¯ç”¨ï¼š0-ç¦ç”¨ï¼Œ1-å¯ç”¨',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  PRIMARY KEY (`id`),
  KEY `idx_apq_agent_id` (`agent_id`),
  KEY `idx_apq_sort_order` (`sort_order`),
  KEY `idx_apq_is_active` (`is_active`),
  CONSTRAINT `agent_preset_question_ibfk_1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='æ™ºèƒ½ä½“é¢„è®¾é—®é¢˜è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `business_knowledge`
--

DROP TABLE IF EXISTS `business_knowledge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `business_knowledge` (
  `id` int NOT NULL AUTO_INCREMENT,
  `business_term` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ä¸šåŠ¡åè¯',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT 'æè¿°',
  `synonyms` text COLLATE utf8mb4_unicode_ci COMMENT 'åŒä¹‰è¯',
  `is_recall` int DEFAULT '1' COMMENT 'æ˜¯å¦å¬å›ž',
  `data_set_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'æ•°æ®é›†id',
  `agent_id` int DEFAULT NULL COMMENT 'å…³è”çš„æ™ºèƒ½ä½“ID',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  PRIMARY KEY (`id`),
  KEY `idx_bk_business_term` (`business_term`),
  KEY `idx_bk_data_set_id` (`data_set_id`),
  KEY `idx_bk_agent_id` (`agent_id`),
  KEY `idx_bk_is_recall` (`is_recall`),
  CONSTRAINT `business_knowledge_ibfk_1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ä¸šåŠ¡çŸ¥è¯†è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `datasource`
--

DROP TABLE IF EXISTS `datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datasource` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'æ•°æ®æºåç§°',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'æ•°æ®æºç±»åž‹ï¼šmysql, postgresql',
  `host` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ä¸»æœºåœ°å€',
  `port` int NOT NULL COMMENT 'ç«¯å£å·',
  `database_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'æ•°æ®åº“åç§°',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ç”¨æˆ·å',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'å¯†ç ï¼ˆåŠ å¯†å­˜å‚¨ï¼‰',
  `connection_url` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'å®Œæ•´è¿žæŽ¥URL',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'active' COMMENT 'çŠ¶æ€ï¼šactive-å¯ç”¨ï¼Œinactive-ç¦ç”¨',
  `test_status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'unknown' COMMENT 'è¿žæŽ¥æµ‹è¯•çŠ¶æ€ï¼šsuccess-æˆåŠŸï¼Œfailed-å¤±è´¥ï¼Œunknown-æœªçŸ¥',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT 'æè¿°',
  `creator_id` bigint DEFAULT NULL COMMENT 'åˆ›å»ºè€…ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  PRIMARY KEY (`id`),
  KEY `idx_ds_name` (`name`),
  KEY `idx_ds_type` (`type`),
  KEY `idx_ds_status` (`status`),
  KEY `idx_ds_creator_id` (`creator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='æ•°æ®æºè¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `semantic_model`
--

DROP TABLE IF EXISTS `semantic_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `semantic_model` (
  `id` int NOT NULL AUTO_INCREMENT,
  `agent_id` int DEFAULT NULL COMMENT 'å…³è”çš„æ™ºèƒ½ä½“ID',
  `field_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'æ™ºèƒ½ä½“å­—æ®µåç§°',
  `synonyms` text COLLATE utf8mb4_unicode_ci COMMENT 'å­—æ®µåç§°åŒä¹‰è¯',
  `origin_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT 'åŽŸå§‹å­—æ®µå',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT 'å­—æ®µæè¿°',
  `origin_description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'åŽŸå§‹å­—æ®µæè¿°',
  `type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT 'å­—æ®µç±»åž‹ (integer, varchar....)',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  `is_recall` tinyint DEFAULT '1' COMMENT '0 åœç”¨ 1 å¯ç”¨',
  `status` tinyint DEFAULT '1' COMMENT '0 åœç”¨ 1 å¯ç”¨',
  PRIMARY KEY (`id`),
  KEY `idx_sm_agent_id` (`agent_id`),
  KEY `idx_sm_field_name` (`field_name`),
  KEY `idx_sm_status` (`status`),
  KEY `idx_sm_is_recall` (`is_recall`),
  CONSTRAINT `semantic_model_ibfk_1` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='è¯­ä¹‰æ¨¡åž‹è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'nl2sql'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-04 14:05:59
