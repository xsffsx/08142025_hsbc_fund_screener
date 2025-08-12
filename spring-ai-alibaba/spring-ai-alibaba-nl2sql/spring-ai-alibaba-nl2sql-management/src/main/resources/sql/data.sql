--
-- PostgreSQL database dump
--

-- Dumped from database version 13.21 (Debian 13.21-1.pgdg120+1)
-- Dumped by pg_dump version 13.21 (Debian 13.21-1.pgdg120+1)


--
-- Data for Name: agent; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO agent VALUES (1, 'NL2SQL智能体', '默认NL2SQL智能体', NULL, 'published', NULL, NULL, NULL, NULL, '2025-08-10 00:40:41.312187', '2025-08-10 00:40:41.312187');
INSERT INTO agent VALUES (2, '测试智能体2', '测试描述2', NULL, 'draft', NULL, NULL, NULL, NULL, '2025-08-10 20:18:09.737091', '2025-08-10 20:18:09.737091');


--
-- Data for Name: agent_datasource; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO agent_datasource VALUES (2001, 2, 201, 1, '2025-08-11 10:46:32.309281', '2025-08-11 10:46:32.309281');
INSERT INTO agent_datasource VALUES (2000, 1, 202, 1, '2025-08-11 10:46:32.309281', '2025-08-11 10:46:32.309281');
INSERT INTO agent_datasource VALUES (2002, 2, 202, 1, '2025-08-11 10:46:32.309281', '2025-08-11 10:46:32.309281');


--
-- Data for Name: agent_knowledge; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO agent_knowledge VALUES (3001, 2, '基金产品字段字典 (B_UT_PROD)', '核心字段:\n- PROD_ID: 产品ID (主键)\n- PROD_NAME: 产品名称\n- PROD_TYPE_CDE: 产品类型代码 (如 EQTY/DEBT/MONEY/MIX/INDEX)\n- PROD_SUBTP_CDE: 产品子类型代码\n- RISK_LVL_CDE: 风险等级 (L/M/H)\n- CCY_PROD_TRADE_CDE: 交易币种 (CHAR(3))\n- CCY_PROD_CDE: 产品币种 (CHAR(3))\n- FUND_RTAIN_MIN_AMT: 最低保留金额\n- FUND_SW_IN_MIN_AMT: 最小转换入金额\n- ALLOW_BUY_PROD_IND / ALLOW_SELL_PROD_IND / ALLOW_SW_IN_PROD_IND / ALLOW_SW_OUT_PROD_IND: 交易可用性指示位', 'document', '数据字典', '基金,字段,UT,产品类型,风险等级,币种', 'active', NULL, NULL, NULL, 'text', 'completed', 2100246635, '2025-08-11 10:46:32.310033', '2025-08-11 10:46:32.310033');
INSERT INTO agent_knowledge VALUES (3002, 2, '渠道与交易可用性 (B_UT_PROD_OFER_CHANL / B_UT_PROD_CHANL_ATTR)', '渠道字段:\n- B_UT_PROD_OFER_CHANL.CHANL_COMN_CDE: 通用渠道码 (如 网银/柜台/手机 等统一编码)\n- B_UT_PROD_CHANL_ATTR.CHANL_CDE: 渠道具体编码\n交易指示位:\n- ALLOW_BUY_PROD_IND / ALLOW_SELL_PROD_IND / ALLOW_SW_IN_PROD_IND / ALLOW_SW_OUT_PROD_IND', 'document', '渠道', '渠道,交易,指示位,销售渠道', 'active', NULL, NULL, NULL, 'text', 'completed', 2100246635, '2025-08-11 10:46:32.310033', '2025-08-11 10:46:32.310033');
INSERT INTO agent_knowledge VALUES (3003, 2, '客户与批处理作业 (CUSTOMER / ACTV_DATA)', 'CUSTOMER: 客户基础信息 (CUSTOMER_ID, COUNTRY_CODE, GROUP_MEMBER, ACTIVE)\nACTV_DATA: 批处理作业批次与类型 (JOB_BATCH_EXEC_ID, JOB_BATCH_TYPE_CDE), 更新时间 REC_UPDT_DT_TM; 用于ETL/装载流程追踪。', 'document', '数据流程', '客户,批处理,作业,ETL,批次', 'active', NULL, NULL, NULL, 'text', 'completed', 2100246635, '2025-08-11 10:46:32.310033', '2025-08-11 10:46:32.310033');
INSERT INTO agent_knowledge VALUES (3004, 2, '金融术语中英对照与同义词', '术语归一:\n- 申购=Buy, 赎回=Sell, 转换=Switch\n- 风险等级=Risk Level (RL)\n- 币种=Currency (CCY)\n- 渠道=Channel\n- 最低保留金额=Retain Min, 最小转换入=Switch-in Min', 'document', '术语', '术语对照,同义词,Glossary', 'active', NULL, NULL, NULL, 'text', 'completed', 2100246635, '2025-08-11 10:46:32.310033', '2025-08-11 10:46:32.310033');


--
-- Data for Name: agent_preset_question; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO agent_preset_question VALUES (1, 1, '查询基金产品的风险等级分布', 1, 1, '2025-08-10 02:17:37.557165', '2025-08-10 02:17:37.557165');
INSERT INTO agent_preset_question VALUES (2, 1, '显示最近一个月的基金申购金额统计', 2, 1, '2025-08-10 02:17:37.557165', '2025-08-10 02:17:37.557165');
INSERT INTO agent_preset_question VALUES (3, 1, '分析不同币种的基金产品数量', 3, 1, '2025-08-10 02:17:37.557165', '2025-08-10 02:17:37.557165');
INSERT INTO agent_preset_question VALUES (4, 1, '查询基金产品的风险等级分布', 1, 1, '2025-08-10 02:18:11.179866', '2025-08-10 02:18:11.179866');


--
-- Data for Name: business_knowledge; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO business_knowledge VALUES (1001, '基金产品类型', '基金产品类型与子类型用于标识产品品类（如股票型、债券型、货币型、混合型、指数型等），对应 B_UT_PROD.PROD_TYPE_CDE 与 PROD_SUBTP_CDE。', '产品类型,基金类型,产品子类型,Fund Type,Product Type,Subtype,Unit Trust,UT', 1, 'finance_fund', '2', '2025-08-11 10:46:32.306385', '2025-08-11 10:46:32.306385');
INSERT INTO business_knowledge VALUES (1002, '风险等级', '风险等级用于衡量基金产品的风险水平（如低/中/高），对应 B_UT_PROD.RISK_LVL_CDE。', '风险级别,风险水平,Risk Level,Risk Category,RL', 1, 'finance_fund', '2', '2025-08-11 10:46:32.306385', '2025-08-11 10:46:32.306385');
INSERT INTO business_knowledge VALUES (1003, '投资金额与阈值', '投资相关最小金额与增量阈值（如最小申购、最小转换入、最低保留与加码增量等），对应 B_UT_PROD.FUND_RTAIN_MIN_AMT, FUND_SW_IN_MIN_AMT, INVST_INCRM_MIN_AMT, INVST_MIP_INCRM_MIN_AMT。', '最小申购金额,最小转换入,最低保留金额,加码增量,Investment Amount,Minimum Subscription,Switch In Min,Retain Min,Increment', 1, 'finance_fund', '2', '2025-08-11 10:46:32.306385', '2025-08-11 10:46:32.306385');
INSERT INTO business_knowledge VALUES (1004, '产品交易与渠道可用性', '购买/赎回/转换可用性由指示字段决定（如 ALLOW_BUY_PROD_IND/ALLOW_SELL_PROD_IND/ALLOW_SW_IN_PROD_IND/ALLOW_SW_OUT_PROD_IND）。销售/办理渠道信息在 B_UT_PROD_OFER_CHANL.CHANL_COMN_CDE 与 B_UT_PROD_CHANL_ATTR.CHANL_CDE。', '可购买,可赎回,可转换入,可转换出,交易可用性,渠道,销售渠道,办理渠道,Channel', 1, 'finance_fund', '2', '2025-08-11 10:46:32.306385', '2025-08-11 10:46:32.306385');
INSERT INTO business_knowledge VALUES (1005, '交易与产品币种', '基金交易与产品币种，常见如 USD/HKD/CNY 等。对应 B_UT_PROD.CCY_PROD_TRADE_CDE 与 CCY_PROD_CDE。', '币种,货币,交易币种,产品币种,Currency,CCY', 1, 'finance_fund', '2', '2025-08-11 10:46:32.306385', '2025-08-11 10:46:32.306385');
INSERT INTO business_knowledge VALUES (1006, '客户基本信息', '客户标识、国家、集团成员与是否激活等字段，来自 CUSTOMER 表（如 CUSTOMER_ID, COUNTRY_CODE, GROUP_MEMBER, ACTIVE）。', '客户,客户ID,客户分组,国家代码,是否激活,Customer', 1, 'finance_fund', '2', '2025-08-11 10:46:32.306385', '2025-08-11 10:46:32.306385');
INSERT INTO business_knowledge VALUES (1007, '批处理作业与批次', '批处理作业标识与类型（JOB_BATCH_EXEC_ID, JOB_BATCH_TYPE_CDE）及更新时间 REC_UPDT_DT_TM 存于 ACTV_DATA，用于数据装载/清洗/同步等流程追踪。', '批处理,批次,作业,Job,Batch,更新时间,ETL,装载', 1, 'finance_fund', '2', '2025-08-11 10:46:32.306385', '2025-08-11 10:46:32.306385');
INSERT INTO business_knowledge VALUES (1008, '金融术语中英文对照', '常见基金/金融术语中英文对照及同义词归一（如 申购/Buy、赎回/Sell、转换/Switch、风险等级/Risk Level）。', '术语对照,中英对照,同义词,Term Mapping,Glossary', 1, 'finance_fund', '2', '2025-08-11 10:46:32.306385', '2025-08-11 10:46:32.306385');


--
-- Data for Name: datasource; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO datasource VALUES (201, 'NL2SQL-PostgreSQL(System)', 'postgresql', 'localhost', 5432, 'nl2sql', 'nl2sql_user', 'nl2sql_pass', 'jdbc:postgresql://localhost:5432/nl2sql', 'active', 'success', 'PostgreSQL for NL2SQL System Tables (Finance Product DDL)', 2100246635, '2025-08-11 10:46:32.308852', '2025-08-11 10:46:32.308852');
INSERT INTO datasource VALUES (202, 'NL2SQL-Oracle(Business)', 'oracle', 'localhost', 1521, 'xepdb1', 'nl2sql_user', 'nl2sql_pass', 'jdbc:oracle:thin:@localhost:1521/xepdb1', 'active', 'success', 'Oracle for NL2SQL Business Tables (Finance Product Data)', 2100246635, '2025-08-11 10:46:32.308852', '2025-08-11 10:46:32.308852');


--
--
--



--
-- Data for Name: semantic_model; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO semantic_model VALUES (2001, '2', 'prodId', '产品ID,基金ID,Product ID,Fund ID', 'B_UT_PROD.PROD_ID', '基金产品唯一标识', 'B_UT_PROD 表的主键之一', 'integer', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2002, '2', 'prodName', '产品名称,基金名称,Product Name,Fund Name', 'B_UT_PROD.PROD_NAME', '基金产品中文名称', 'B_UT_PROD.PROD_NAME', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2003, '2', 'productTypeCode', '产品类型,基金类型,Type,Fund Type,UT', 'B_UT_PROD.PROD_TYPE_CDE', 'PROD_TYPE_CDE=UT(FUND)', 'B_UT_PROD.PROD_TYPE_CDE', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2004, '2', 'productSubTypeCode', '产品子类型,Subtype,Sub Category', 'B_UT_PROD.PROD_SUBTP_CDE', '基金产品子类型代码', 'B_UT_PROD.PROD_SUBTP_CDE', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2005, '2', 'tradeCurrencyCode', '交易币种,交易货币,Trade CCY', 'B_UT_PROD.CCY_PROD_TRADE_CDE', '交易相关币种代码（CHAR(3)）', 'B_UT_PROD.CCY_PROD_TRADE_CDE', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2006, '2', 'productCurrencyCode', '产品币种,货币,CCY', 'B_UT_PROD.CCY_PROD_CDE', '产品币种（CHAR(3)）', 'B_UT_PROD.CCY_PROD_CDE', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2007, '2', 'riskLevelCode', '风险等级,风险级别,Risk Level,RL', 'B_UT_PROD.RISK_LVL_CDE', '基金产品风险等级代码（常见: L/M/H）', 'B_UT_PROD.RISK_LVL_CDE', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2008, '2', 'fundRetainMinAmount', '最低保留金额,最小持有,Retain Min', 'B_UT_PROD.FUND_RTAIN_MIN_AMT', '账户最低保留金额（离账后需保留的最小金额）', 'B_UT_PROD.FUND_RTAIN_MIN_AMT', 'decimal', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2009, '2', 'fundSwitchInMinAmount', '最小转换入金额,Switch-in Min', 'B_UT_PROD.FUND_SW_IN_MIN_AMT', '最小转换入金额', 'B_UT_PROD.FUND_SW_IN_MIN_AMT', 'decimal', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2010, '2', 'allowBuyIndicator', '可购买,允许购买,申购可用,Buyable', 'B_UT_PROD.ALLOW_BUY_PROD_IND', '是否允许申购/买入（指示位）', 'B_UT_PROD.ALLOW_BUY_PROD_IND', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2011, '2', 'allowSellIndicator', '可赎回,允许卖出,Sellable', 'B_UT_PROD.ALLOW_SELL_PROD_IND', '是否允许赎回/卖出（指示位）', 'B_UT_PROD.ALLOW_SELL_PROD_IND', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2012, '2', 'allowSwitchInIndicator', '可转换入,允许转换入,Switch In', 'B_UT_PROD.ALLOW_SW_IN_PROD_IND', '是否允许转换入（指示位）', 'B_UT_PROD.ALLOW_SW_IN_PROD_IND', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2013, '2', 'allowSwitchOutIndicator', '可转换出,允许转换出,Switch Out', 'B_UT_PROD.ALLOW_SW_OUT_PROD_IND', '是否允许转换出（指示位）', 'B_UT_PROD.ALLOW_SW_OUT_PROD_IND', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2014, '2', 'investIncrementMinAmount', '最小加码增量,加码增量,Increment Min', 'B_UT_PROD.INVST_INCRM_MIN_AMT', '追加投资的最小增量金额', 'B_UT_PROD.INVST_INCRM_MIN_AMT', 'decimal', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2015, '2', 'investMipIncrementMinAmount', '最小加码增量(MIP),MIP增量', 'B_UT_PROD.INVST_MIP_INCRM_MIN_AMT', 'MIP（月供/定投）追加投资最小增量金额', 'B_UT_PROD.INVST_MIP_INCRM_MIN_AMT', 'decimal', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2016, '2', 'channelCommonCode', '渠道通用代码,公共渠道码,Common Channel', 'B_UT_PROD_OFER_CHANL.CHANL_COMN_CDE', '产品可用的通用渠道代码（如网银/柜台/手机等统一编码）', 'B_UT_PROD_OFER_CHANL.CHANL_COMN_CDE', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2017, '2', 'channelCode', '渠道代码,销售渠道,办理渠道,Channel Code', 'B_UT_PROD_CHANL_ATTR.CHANL_CDE', '产品在某渠道的具体编码', 'B_UT_PROD_CHANL_ATTR.CHANL_CDE', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2018, '2', 'jobBatchExecId', '批次ID,作业批次,Job Batch ID', 'ACTV_DATA.JOB_BATCH_EXEC_ID', '批处理作业执行批次ID', 'ACTV_DATA.JOB_BATCH_EXEC_ID', 'integer', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2019, '2', 'jobBatchTypeCode', '批次类型,作业类型,Batch Type', 'ACTV_DATA.JOB_BATCH_TYPE_CDE', '批处理作业类型代码（如 B_UT_WPC 等）', 'ACTV_DATA.JOB_BATCH_TYPE_CDE', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2020, '2', 'recordUpdateTime', '记录更新时间,更新时间,Record Update Time', 'ACTV_DATA.REC_UPDT_DT_TM', '记录的最后更新时间', 'ACTV_DATA.REC_UPDT_DT_TM', 'datetime', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2021, '2', 'customerId', '客户ID,客户编号,Customer ID', 'CUSTOMER.CUSTOMER_ID', '客户标识（可能为外部或内部统一ID）', 'CUSTOMER.CUSTOMER_ID', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2022, '2', 'groupMemberCode', '集团成员,集团代码,Group Member', 'CUSTOMER.GROUP_MEMBER', '客户所属集团成员标识', 'CUSTOMER.GROUP_MEMBER', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2023, '2', 'countryCode', '国家代码,地区代码,Country Code', 'CUSTOMER.COUNTRY_CODE', '客户所在国家/地区代码', 'CUSTOMER.COUNTRY_CODE', 'string', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);
INSERT INTO semantic_model VALUES (2024, '2', 'customerActive', '是否激活,有效标识,Active', 'CUSTOMER.ACTIVE', '客户激活状态（0/1 或 Y/N）', 'CUSTOMER.ACTIVE', 'integer', '2025-08-11 10:46:32.307704', '2025-08-11 10:46:32.307704', 1, 1);


--
-- Name: agent_datasource_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('agent_datasource_id_seq', 37, true);


--
-- Name: agent_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('agent_id_seq', 2, true);


--
-- Name: agent_knowledge_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('agent_knowledge_id_seq', 1, false);


--
-- Name: agent_preset_question_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('agent_preset_question_id_seq', 4, true);


--
-- Name: business_knowledge_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('business_knowledge_id_seq', 1, false);


--
-- Name: datasource_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('datasource_id_seq', 1, false);


--
-- Name: semantic_model_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('semantic_model_id_seq', 9, true);


--
-- PostgreSQL database dump complete
--
