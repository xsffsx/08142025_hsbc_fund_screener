-- 初始化数据文件 - Finance Product / Fund / UT 领域 v1.0
-- 完整的基金业务知识与语义模型初始化脚本

USE nl2sql;

-- 可选：避免外键约束阻塞（如有外键可临时关闭）
SET FOREIGN_KEY_CHECKS = 0;

-- 1) 清空旧数据（移除默认销售/客服等示例）
DELETE FROM agent_datasource;
DELETE FROM agent_knowledge;
DELETE FROM datasource;
DELETE FROM semantic_model;
DELETE FROM business_knowledge;

-- 智能体数据 (先插入，因为其他表有外键引用)
INSERT IGNORE INTO `agent` (`id`, `name`, `description`, `avatar`, `status`, `prompt`, `category`, `admin_id`, `tags`, `create_time`, `update_time`) VALUES
(1, '中国人口GDP数据智能体', '专门处理中国人口和GDP相关数据查询分析的智能体', '/avatars/china-gdp-agent.png', 'published', '你是一个专业的数据分析助手，专门处理中国人口和GDP相关的数据查询。请根据用户的问题，生成准确的SQL查询语句。', '数据分析', 2100246635, '人口数据,GDP分析,经济统计', NOW(), NOW()),
(2, '金融产品分析智能体', '专注于金融产品数据分析和基金业务指标计算的智能体', '/avatars/finance-agent.png', 'published', '你是一个金融产品分析专家，能够帮助用户分析基金产品、投资组合和金融业务指标。你可以查询产品信息、风险等级、投资金额、货币类型等相关数据。', '金融分析', 2100246635, '金融产品,基金分析,投资组合,风险管理', NOW(), NOW()),
(3, '财务报表智能体', '专门处理财务数据和报表分析的智能体', '/avatars/finance-agent.png', 'draft', '你是一个财务分析专家，专门处理财务数据查询和报表生成。', '财务分析', 2100246635, '财务数据,报表分析,会计', NOW(), NOW()),
(4, '库存管理智能体', '专注于库存数据管理和供应链分析的智能体', '/avatars/inventory-agent.png', 'published', '你是一个库存管理专家，能够帮助用户查询库存状态、分析供应链数据。', '供应链', 2100246635, '库存管理,供应链,物流', NOW(), NOW());

-- 2) 新增业务知识（基金/UT 领域）
-- 表结构: business_knowledge(id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time)
-- 约定: agent_id=2 为"金融产品分析智能体"
INSERT INTO business_knowledge
(id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time)
VALUES
-- 基金产品类型（B_UT_PROD.PROD_TYPE_CDE / PROD_SUBTP_CDE）
(1001, '基金产品类型',
 '基金产品类型与子类型用于标识产品品类（如股票型、债券型、货币型、混合型、指数型等），对应 B_UT_PROD.PROD_TYPE_CDE 与 PROD_SUBTP_CDE。',
 '产品类型,基金类型,产品子类型,Fund Type,Product Type,Subtype,Unit Trust,UT',
 1, 'finance_fund', 2, NOW(), NOW()),

-- 风险等级（B_UT_PROD.RISK_LVL_CDE）
(1002, '风险等级',
 '风险等级用于衡量基金产品的风险水平（如低/中/高），对应 B_UT_PROD.RISK_LVL_CDE。',
 '风险级别,风险水平,Risk Level,Risk Category,RL',
 1, 'finance_fund', 2, NOW(), NOW()),

-- 投资金额与阈值（B_UT_PROD 中多金额字段）
(1003, '投资金额与阈值',
 '投资相关最小金额与增量阈值（如最小申购、最小转换入、最低保留与加码增量等），对应 B_UT_PROD.FUND_RTAIN_MIN_AMT, FUND_SW_IN_MIN_AMT, INVST_INCRM_MIN_AMT, INVST_MIP_INCRM_MIN_AMT。',
 '最小申购金额,最小转换入,最低保留金额,加码增量,Investment Amount,Minimum Subscription,Switch In Min,Retain Min,Increment',
 1, 'finance_fund', 2, NOW(), NOW()),

-- 交易/渠道可用性（B_UT_PROD 与渠道表）
(1004, '产品交易与渠道可用性',
 '购买/赎回/转换可用性由指示字段决定（如 ALLOW_BUY_PROD_IND/ALLOW_SELL_PROD_IND/ALLOW_SW_IN_PROD_IND/ALLOW_SW_OUT_PROD_IND）。销售/办理渠道信息在 B_UT_PROD_OFER_CHANL.CHANL_COMN_CDE 与 B_UT_PROD_CHANL_ATTR.CHANL_CDE。',
 '可购买,可赎回,可转换入,可转换出,交易可用性,渠道,销售渠道,办理渠道,Channel',
 1, 'finance_fund', 2, NOW(), NOW()),

-- 货币/币种（B_UT_PROD.CCY_*）
(1005, '交易与产品币种',
 '基金交易与产品币种，常见如 USD/HKD/CNY 等。对应 B_UT_PROD.CCY_PROD_TRADE_CDE 与 CCY_PROD_CDE。',
 '币种,货币,交易币种,产品币种,Currency,CCY',
 1, 'finance_fund', 2, NOW(), NOW()),

-- 客户（CUSTOMER）
(1006, '客户基本信息',
 '客户标识、国家、集团成员与是否激活等字段，来自 CUSTOMER 表（如 CUSTOMER_ID, COUNTRY_CODE, GROUP_MEMBER, ACTIVE）。',
 '客户,客户ID,客户分组,国家代码,是否激活,Customer',
 1, 'finance_fund', 2, NOW(), NOW()),

-- 批处理/作业（ACTV_DATA）
(1007, '批处理作业与批次',
 '批处理作业标识与类型（JOB_BATCH_EXEC_ID, JOB_BATCH_TYPE_CDE）及更新时间 REC_UPDT_DT_TM 存于 ACTV_DATA，用于数据装载/清洗/同步等流程追踪。',
 '批处理,批次,作业,Job,Batch,更新时间,ETL,装载',
 1, 'finance_fund', 2, NOW(), NOW()),

-- 金融术语对照
(1008, '金融术语中英文对照',
 '常见基金/金融术语中英文对照及同义词归一（如 申购/Buy、赎回/Sell、转换/Switch、风险等级/Risk Level）。',
 '术语对照,中英对照,同义词,Term Mapping,Glossary',
 1, 'finance_fund', 2, NOW(), NOW());

-- 3) 新增语义模型（字段到真实库表字段的语义映射）
-- 表结构: semantic_model(id, agent_id, field_name, synonyms, origin_name, description, origin_description, type, created_time, updated_time, is_recall, status)
-- 约定: agent_id=2；status=1；is_recall=1
INSERT INTO semantic_model
(id, agent_id, field_name, synonyms, origin_name, description, origin_description, type, created_time, updated_time, is_recall, status)
VALUES
-- B_UT_PROD（产品主信息）
(2001, 2, 'prodId', '产品ID,基金ID,Product ID,Fund ID', 'B_UT_PROD.PROD_ID',
 '基金产品唯一标识', 'B_UT_PROD 表的主键之一', 'integer', NOW(), NOW(), 1, 1),

(2002, 2, 'prodName', '产品名称,基金名称,Product Name,Fund Name', 'B_UT_PROD.PROD_NAME',
 '基金产品中文名称', 'B_UT_PROD.PROD_NAME', 'string', NOW(), NOW(), 1, 1),

(2003, 2, 'productTypeCode', '产品类型,基金类型,Type,Fund Type,UT', 'B_UT_PROD.PROD_TYPE_CDE',
 'PROD_TYPE_CDE=UT(FUND)', 'B_UT_PROD.PROD_TYPE_CDE', 'string', NOW(), NOW(), 1, 1),

(2004, 2, 'productSubTypeCode', '产品子类型,Subtype,Sub Category', 'B_UT_PROD.PROD_SUBTP_CDE',
 '基金产品子类型代码', 'B_UT_PROD.PROD_SUBTP_CDE', 'string', NOW(), NOW(), 1, 1),

(2005, 2, 'tradeCurrencyCode', '交易币种,交易货币,Trade CCY', 'B_UT_PROD.CCY_PROD_TRADE_CDE',
 '交易相关币种代码（CHAR(3)）', 'B_UT_PROD.CCY_PROD_TRADE_CDE', 'string', NOW(), NOW(), 1, 1),

(2006, 2, 'productCurrencyCode', '产品币种,货币,CCY', 'B_UT_PROD.CCY_PROD_CDE',
 '产品币种（CHAR(3)）', 'B_UT_PROD.CCY_PROD_CDE', 'string', NOW(), NOW(), 1, 1),

(2007, 2, 'riskLevelCode', '风险等级,风险级别,Risk Level,RL', 'B_UT_PROD.RISK_LVL_CDE',
 '基金产品风险等级代码（常见: L/M/H）', 'B_UT_PROD.RISK_LVL_CDE', 'string', NOW(), NOW(), 1, 1),

(2008, 2, 'fundRetainMinAmount', '最低保留金额,最小持有,Retain Min', 'B_UT_PROD.FUND_RTAIN_MIN_AMT',
 '账户最低保留金额（离账后需保留的最小金额）', 'B_UT_PROD.FUND_RTAIN_MIN_AMT', 'decimal', NOW(), NOW(), 1, 1),

(2009, 2, 'fundSwitchInMinAmount', '最小转换入金额,Switch In Min', 'B_UT_PROD.FUND_SW_IN_MIN_AMT',
 '基金转换入最小金额', 'B_UT_PROD.FUND_SW_IN_MIN_AMT', 'decimal', NOW(), NOW(), 1, 1),

(2010, 2, 'investIncrementMinAmount', '投资增量最小金额,Increment Min', 'B_UT_PROD.INVST_INCRM_MIN_AMT',
 '投资增量最小金额', 'B_UT_PROD.INVST_INCRM_MIN_AMT', 'decimal', NOW(), NOW(), 1, 1),

(2011, 2, 'investMipIncrementMinAmount', 'MIP增量最小金额,MIP Increment', 'B_UT_PROD.INVST_MIP_INCRM_MIN_AMT',
 'MIP投资增量最小金额', 'B_UT_PROD.INVST_MIP_INCRM_MIN_AMT', 'decimal', NOW(), NOW(), 1, 1),

(2012, 2, 'allowBuyProdInd', '允许购买指示,可购买,Buy Allowed', 'B_UT_PROD.ALLOW_BUY_PROD_IND',
 '是否允许购买该产品的指示符', 'B_UT_PROD.ALLOW_BUY_PROD_IND', 'string', NOW(), NOW(), 1, 1),

(2013, 2, 'allowSellProdInd', '允许赎回指示,可赎回,Sell Allowed', 'B_UT_PROD.ALLOW_SELL_PROD_IND',
 '是否允许赎回该产品的指示符', 'B_UT_PROD.ALLOW_SELL_PROD_IND', 'string', NOW(), NOW(), 1, 1),

(2014, 2, 'allowSwitchInProdInd', '允许转换入指示,可转换入,Switch In', 'B_UT_PROD.ALLOW_SW_IN_PROD_IND',
 '是否允许转换入该产品的指示符', 'B_UT_PROD.ALLOW_SW_IN_PROD_IND', 'string', NOW(), NOW(), 1, 1),

(2015, 2, 'allowSwitchOutProdInd', '允许转换出指示,可转换出,Switch Out', 'B_UT_PROD.ALLOW_SW_OUT_PROD_IND',
 '是否允许从该产品转换出的指示符', 'B_UT_PROD.ALLOW_SW_OUT_PROD_IND', 'string', NOW(), NOW(), 1, 1),

-- CUSTOMER（客户信息）
(2016, 2, 'customerId', '客户ID,Customer ID', 'CUSTOMER.CUSTOMER_ID',
 '客户唯一标识', 'CUSTOMER.CUSTOMER_ID', 'string', NOW(), NOW(), 1, 1),

(2017, 2, 'countryCode', '国家代码,Country', 'CUSTOMER.COUNTRY_CODE',
 '客户所属国家代码', 'CUSTOMER.COUNTRY_CODE', 'string', NOW(), NOW(), 1, 1),

(2018, 2, 'groupMember', '集团成员,Group Member', 'CUSTOMER.GROUP_MEMBER',
 '是否为集团成员', 'CUSTOMER.GROUP_MEMBER', 'string', NOW(), NOW(), 1, 1),

(2019, 2, 'active', '是否激活,Active Status', 'CUSTOMER.ACTIVE',
 '客户账户是否激活', 'CUSTOMER.ACTIVE', 'string', NOW(), NOW(), 1, 1),

-- ACTV_DATA（活动数据/批处理）
(2020, 2, 'jobBatchExecId', '作业批次执行ID,Job Batch ID', 'ACTV_DATA.JOB_BATCH_EXEC_ID',
 '批处理作业执行标识', 'ACTV_DATA.JOB_BATCH_EXEC_ID', 'integer', NOW(), NOW(), 1, 1),

(2021, 2, 'jobBatchTypeCode', '作业批次类型,Job Type', 'ACTV_DATA.JOB_BATCH_TYPE_CDE',
 '批处理作业类型代码', 'ACTV_DATA.JOB_BATCH_TYPE_CDE', 'string', NOW(), NOW(), 1, 1),

(2022, 2, 'recUpdateDateTime', '记录更新时间,Update Time', 'ACTV_DATA.REC_UPDT_DT_TM',
 '记录最后更新时间', 'ACTV_DATA.REC_UPDT_DT_TM', 'datetime', NOW(), NOW(), 1, 1),

-- B_UT_PROD_OFER_CHANL（产品销售渠道）
(2023, 2, 'channelCommonCode', '渠道通用代码,Channel Code', 'B_UT_PROD_OFER_CHANL.CHANL_COMN_CDE',
 '产品销售渠道通用代码', 'B_UT_PROD_OFER_CHANL.CHANL_COMN_CDE', 'string', NOW(), NOW(), 1, 1),

-- B_UT_PROD_CHANL_ATTR（产品渠道属性）
(2024, 2, 'channelCode', '渠道代码,Channel', 'B_UT_PROD_CHANL_ATTR.CHANL_CDE',
 '产品办理渠道代码', 'B_UT_PROD_CHANL_ATTR.CHANL_CDE', 'string', NOW(), NOW(), 1, 1);

-- 4) 新增智能体知识（基金/UT 领域专业知识）
-- 表结构: agent_knowledge(id, agent_id, title, content, type, category, tags, status, source_url, file_type, embedding_status, creator_id, create_time, update_time)
-- 约定: agent_id=2；status='active'；embedding_status='completed'
INSERT INTO agent_knowledge
(id, agent_id, title, content, type, category, tags, status, source_url, file_type, embedding_status, creator_id, create_time, update_time)
VALUES
(3001, 2, '基金产品数据结构说明',
 '基金产品核心表 B_UT_PROD 包含以下关键字段：\n- PROD_ID：产品唯一标识\n- PROD_NAME：产品中文名称\n- PROD_TYPE_CDE：产品类型（EQTY股票型/DEBT债券型/MONEY货币型/MIX混合型/INDEX指数型）\n- PROD_SUBTP_CDE：产品子类型\n- RISK_LVL_CDE：风险等级（L低/M中/H高）\n- CCY_PROD_TRADE_CDE：交易币种\n- CCY_PROD_CDE：产品币种\n- FUND_RTAIN_MIN_AMT：最低保留金额\n- ALLOW_BUY_PROD_IND：允许购买指示\n- ALLOW_SELL_PROD_IND：允许赎回指示',
 'document', '数据字典', '基金产品,数据结构,B_UT_PROD,字段说明', 'active', NULL, 'text', 'completed', 2100246635, NOW(), NOW()),

(3002, 2, '基金交易与渠道业务规则',
 '基金交易业务规则：\n1. 购买条件：ALLOW_BUY_PROD_IND = ''Y'' 且满足最小申购金额\n2. 赎回条件：ALLOW_SELL_PROD_IND = ''Y'' 且保留金额 >= FUND_RTAIN_MIN_AMT\n3. 转换规则：转换入需 ALLOW_SW_IN_PROD_IND = ''Y''，转换出需 ALLOW_SW_OUT_PROD_IND = ''Y''\n4. 渠道限制：通过 B_UT_PROD_OFER_CHANL 和 B_UT_PROD_CHANL_ATTR 控制可用渠道\n5. 币种匹配：交易币种与产品币种需要匹配或支持汇率转换',
 'document', '业务规则', '基金交易,业务规则,渠道管理,交易条件', 'active', NULL, 'text', 'completed', 2100246635, NOW(), NOW()),

(3003, 2, '客户数据与批处理说明',
 '客户相关数据说明：\n1. CUSTOMER 表：包含客户基本信息（CUSTOMER_ID, COUNTRY_CODE, GROUP_MEMBER, ACTIVE）\n2. ACTV_DATA 表：记录批处理作业信息，用于数据同步和更新追踪\n3. 数据更新：通过 JOB_BATCH_EXEC_ID 和 REC_UPDT_DT_TM 追踪数据变更\n4. 客户状态：ACTIVE 字段标识客户是否激活\n5. 集团关系：GROUP_MEMBER 标识客户是否为集团成员',
 'document', '数据说明', '客户数据,CUSTOMER,ACTV_DATA,批处理', 'active', NULL, 'text', 'completed', 2100246635, NOW(), NOW()),

(3004, 2, '常见基金查询示例',
 '基金产品查询示例：\n\n问：查询所有股票型基金？\n答：SELECT * FROM B_UT_PROD WHERE PROD_TYPE_CDE = ''EQTY''\n\n问：查询风险等级为高的产品？\n答：SELECT * FROM B_UT_PROD WHERE RISK_LVL_CDE = ''H''\n\n问：查询允许购买的USD基金？\n答：SELECT * FROM B_UT_PROD WHERE ALLOW_BUY_PROD_IND = ''Y'' AND CCY_PROD_TRADE_CDE = ''USD''\n\n问：查询客户信息？\n答：SELECT * FROM CUSTOMER WHERE ACTIVE = ''Y''',
 'qa', '查询示例', '基金查询,SQL示例,常见问题,FAQ', 'active', NULL, 'text', 'completed', 2100246635, NOW(), NOW());

-- 5) 新增数据源（基金/UT 领域数据库）
-- 表结构: datasource(id, name, type, host, port, database_name, username, password, connection_url, status, test_status, description, creator_id, create_time, update_time)
INSERT INTO datasource
(id, name, type, host, port, database_name, username, password, connection_url, status, test_status, description, creator_id, create_time, update_time)
VALUES
(1, '基金产品数据库', 'mysql', 'localhost', 3306, 'nl2sql', 'nl2sql_user', 'nl2sql_pass',
 'jdbc:mysql://localhost:3306/nl2sql?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Shanghai',
 'active', 'success', '基金产品核心数据库，包含 B_UT_PROD、CUSTOMER、ACTV_DATA 等核心表', 2100246635, NOW(), NOW());

-- 6) 新增智能体数据源关联（基金分析智能体绑定到基金数据库）
-- 表结构: agent_datasource(id, agent_id, datasource_id, is_active, create_time, update_time)
INSERT INTO agent_datasource
(id, agent_id, datasource_id, is_active, create_time, update_time)
VALUES
(1, 2, 1, 1, NOW(), NOW());  -- 金融产品分析智能体使用基金产品数据库

-- 7) 新增智能体预设问题（基金/UT 领域常见查询）
-- 表结构: agent_preset_question(id, agent_id, question, sort_order, is_active, create_time, update_time)
INSERT INTO agent_preset_question
(id, agent_id, question, sort_order, is_active, create_time, update_time)
VALUES
(4001, 2, '显示所有基金产品信息', 1, 1, NOW(), NOW()),
(4002, 2, '查询风险等级为高的产品', 2, 1, NOW(), NOW()),
(4003, 2, '统计不同产品类型的数量', 3, 1, NOW(), NOW()),
(4004, 2, '查询允许购买的基金产品', 4, 1, NOW(), NOW()),
(4005, 2, '显示USD币种的基金产品', 5, 1, NOW(), NOW()),
(4006, 2, '查询股票型基金列表', 6, 1, NOW(), NOW()),
(4007, 2, '显示客户基本信息', 7, 1, NOW(), NOW()),
(4008, 2, '查询最低保留金额大于1000的产品', 8, 1, NOW(), NOW());

-- 恢复外键约束检查
SET FOREIGN_KEY_CHECKS = 1;
