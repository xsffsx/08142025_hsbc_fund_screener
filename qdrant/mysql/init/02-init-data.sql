-- MySQL数据库初始化数据脚本
USE nl2sql;

-- 智能体示例数据 (先插入，因为其他表有外键引用)
INSERT INTO agent (id, name, description, avatar, status, prompt, category, admin_id, tags, create_time, update_time) VALUES 
(1, '中国人口GDP数据智能体', '专门处理中国人口和GDP相关数据查询分析的智能体', '/avatars/china-gdp-agent.png', 'published', '你是一个专业的数据分析助手，专门处理中国人口和GDP相关的数据查询。请根据用户的问题，生成准确的SQL查询语句。', '数据分析', 2100246635, '人口数据,GDP分析,经济统计', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
name = VALUES(name), description = VALUES(description), update_time = NOW();

INSERT INTO agent (id, name, description, avatar, status, prompt, category, admin_id, tags, create_time, update_time) VALUES
(2, '金融产品分析智能体', '专注于金融产品数据分析和基金业务指标计算的智能体', '/avatars/finance-agent.png', 'published', '你是一个金融产品分析专家，能够帮助用户分析基金产品、投资组合和金融业务指标。你可以查询产品信息、风险等级、投资金额、货币类型等相关数据。', '金融分析', 2100246635, '金融产品,基金分析,投资组合,风险管理', NOW(), NOW())
ON DUPLICATE KEY UPDATE
name = VALUES(name), description = VALUES(description), prompt = VALUES(prompt), tags = VALUES(tags), update_time = NOW();

INSERT INTO agent (id, name, description, avatar, status, prompt, category, admin_id, tags, create_time, update_time) VALUES 
(3, '财务报表智能体', '专门处理财务数据和报表分析的智能体', '/avatars/finance-agent.png', 'draft', '你是一个财务分析专家，专门处理财务数据查询和报表生成。', '财务分析', 2100246635, '财务数据,报表分析,会计', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
name = VALUES(name), description = VALUES(description), update_time = NOW();

INSERT INTO agent (id, name, description, avatar, status, prompt, category, admin_id, tags, create_time, update_time) VALUES 
(4, '库存管理智能体', '专注于库存数据管理和供应链分析的智能体', '/avatars/inventory-agent.png', 'published', '你是一个库存管理专家，能够帮助用户查询库存状态、分析供应链数据。', '供应链', 2100246635, '库存管理,供应链,物流', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
name = VALUES(name), description = VALUES(description), update_time = NOW();

-- 业务知识数据 - 基金/UT 领域
INSERT INTO business_knowledge (id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time) VALUES
(1, '基金产品类型', '基金产品类型与子类型用于标识产品品类（如股票型、债券型、货币型、混合型、指数型等），对应 B_UT_PROD.PROD_TYPE_CDE 与 PROD_SUBTP_CDE。', '产品类型,基金类型,产品子类型,Fund Type,Product Type,Subtype,Unit Trust,UT', 1, 'finance_fund', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE
description = VALUES(description), updated_time = NOW();

INSERT INTO business_knowledge (id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time) VALUES
(2, '风险等级', '风险等级用于衡量基金产品的风险水平（如低/中/高），对应 B_UT_PROD.RISK_LVL_CDE。', '风险级别,风险水平,Risk Level,Risk Category,RL', 1, 'finance_fund', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE
description = VALUES(description), updated_time = NOW();

INSERT INTO business_knowledge (id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time) VALUES
(3, '投资金额与阈值', '投资相关最小金额与增量阈值（如最小申购、最小转换入、最低保留与加码增量等），对应 B_UT_PROD.FUND_RTAIN_MIN_AMT, FUND_SW_IN_MIN_AMT, INVST_INCRM_MIN_AMT, INVST_MIP_INCRM_MIN_AMT。', '最小申购金额,最小转换入,最低保留金额,加码增量,Investment Amount,Minimum Subscription,Switch In Min,Retain Min,Increment', 1, 'finance_fund', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE
description = VALUES(description), updated_time = NOW();

INSERT INTO business_knowledge (id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time) VALUES
(4, '产品交易与渠道可用性', '购买/赎回/转换可用性由指示字段决定（如 ALLOW_BUY_PROD_IND/ALLOW_SELL_PROD_IND/ALLOW_SW_IN_PROD_IND/ALLOW_SW_OUT_PROD_IND）。销售/办理渠道信息在 B_UT_PROD_OFER_CHANL.CHANL_COMN_CDE 与 B_UT_PROD_CHANL_ATTR.CHANL_CDE。', '可购买,可赎回,可转换入,可转换出,交易可用性,渠道,销售渠道,办理渠道,Channel', 1, 'finance_fund', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE
description = VALUES(description), updated_time = NOW();

INSERT INTO business_knowledge (id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time) VALUES
(5, '交易与产品币种', '基金交易与产品币种，常见如 USD/HKD/CNY 等。对应 B_UT_PROD.CCY_PROD_TRADE_CDE 与 CCY_PROD_CDE。', '币种,货币,交易币种,产品币种,Currency,CCY', 1, 'finance_fund', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE
description = VALUES(description), updated_time = NOW();

INSERT INTO business_knowledge (id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time) VALUES
(6, '客户基本信息', '客户标识、国家、集团成员与是否激活等字段，来自 CUSTOMER 表（如 CUSTOMER_ID, COUNTRY_CODE, GROUP_MEMBER, ACTIVE）。', '客户,客户ID,客户分组,国家代码,是否激活,Customer', 1, 'finance_fund', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE
description = VALUES(description), updated_time = NOW();

INSERT INTO business_knowledge (id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time) VALUES
(7, '批处理作业与批次', '批处理作业标识与类型（JOB_BATCH_EXEC_ID, JOB_BATCH_TYPE_CDE）及更新时间 REC_UPDT_DT_TM 存于 ACTV_DATA，用于数据装载/清洗/同步等流程追踪。', '批处理,批次,作业,Job,Batch,更新时间,ETL,装载', 1, 'finance_fund', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE
description = VALUES(description), updated_time = NOW();

INSERT INTO business_knowledge (id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time) VALUES
(8, '金融术语中英文对照', '常见基金/金融术语中英文对照及同义词归一（如 申购/Buy、赎回/Sell、转换/Switch、风险等级/Risk Level）。', '术语对照,中英对照,同义词,Term Mapping,Glossary', 1, 'finance_fund', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE
description = VALUES(description), updated_time = NOW();

-- 语义模型示例数据
INSERT INTO semantic_model (id, agent_id, field_name, synonyms, origin_name, description, origin_description, type, created_time, updated_time, is_recall, status) VALUES 
(1, 1, 'customerSatisfactionScore', 'satisfaction score, customer rating', 'csat_score', 'Customer satisfaction rating from 1-10', 'Customer satisfaction score', 'integer', NOW(), NOW(), 1, 1)
ON DUPLICATE KEY UPDATE 
description = VALUES(description), updated_time = NOW();

INSERT INTO semantic_model (id, agent_id, field_name, synonyms, origin_name, description, origin_description, type, created_time, updated_time, is_recall, status) VALUES 
(2, 1, 'netPromoterScore', 'NPS, promoter score', 'nps_value', 'Net Promoter Score from -100 to 100', 'NPS calculation result', 'integer', NOW(), NOW(), 1, 1)
ON DUPLICATE KEY UPDATE 
description = VALUES(description), updated_time = NOW();

INSERT INTO semantic_model (id, agent_id, field_name, synonyms, origin_name, description, origin_description, type, created_time, updated_time, is_recall, status) VALUES 
(3, 2, 'customerRetentionRate', 'retention rate, loyalty rate', 'retention_pct', 'Percentage of retained customers', 'Customer retention percentage', 'decimal', NOW(), NOW(), 1, 1)
ON DUPLICATE KEY UPDATE 
description = VALUES(description), updated_time = NOW();

-- 智能体知识示例数据
INSERT INTO agent_knowledge (id, agent_id, title, content, type, category, tags, status, source_url, file_type, embedding_status, creator_id, create_time, update_time) VALUES 
(1, 1, '中国人口统计数据说明', '中国人口统计数据包含了历年的人口总数、性别比例、年龄结构、城乡分布等详细信息。数据来源于国家统计局，具有权威性和准确性。查询时请注意数据的时间范围和统计口径。', 'document', '数据说明', '人口统计,数据源,统计局', 'active', 'http://stats.gov.cn/population', 'text', 'completed', 2100246635, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
content = VALUES(content), update_time = NOW();

INSERT INTO agent_knowledge (id, agent_id, title, content, type, category, tags, status, source_url, file_type, embedding_status, creator_id, create_time, update_time) VALUES 
(2, 1, 'GDP数据使用指南', 'GDP（国内生产总值）数据反映了国家经济发展水平。包含名义GDP、实际GDP、GDP增长率等指标。数据按季度和年度进行统计，支持按地区、行业进行分类查询。', 'document', '使用指南', 'GDP,经济指标,增长率', 'active', 'http://stats.gov.cn/gdp', 'text', 'completed', 2100246635, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
content = VALUES(content), update_time = NOW();

INSERT INTO agent_knowledge (id, agent_id, title, content, type, category, tags, status, source_url, file_type, embedding_status, creator_id, create_time, update_time) VALUES 
(3, 1, '常见查询问题', '问：如何查询2023年的人口数据？\n答：可以使用"SELECT * FROM population WHERE year = 2023"进行查询。\n\n问：如何计算GDP增长率？\n答：GDP增长率 = (当年GDP - 上年GDP) / 上年GDP * 100%', 'qa', '常见问题', '查询示例,FAQ,SQL语句', 'active', NULL, 'text', 'completed', 2100246635, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
content = VALUES(content), update_time = NOW();

INSERT INTO agent_knowledge (id, agent_id, title, content, type, category, tags, status, source_url, file_type, embedding_status, creator_id, create_time, update_time) VALUES 
(4, 2, '销售数据字段说明', '销售数据表包含以下关键字段：\n- sales_amount：销售金额\n- customer_id：客户ID\n- product_id：产品ID\n- sales_date：销售日期\n- region：销售区域\n- sales_rep：销售代表', 'document', '数据字典', '销售数据,字段说明,数据结构', 'active', NULL, 'text', 'completed', 2100246635, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
content = VALUES(content), update_time = NOW();

-- 数据源示例数据
INSERT INTO datasource (id, name, type, host, port, database_name, username, password, connection_url, status, test_status, description, creator_id, create_time, update_time) VALUES 
(1, '本地MySQL数据库', 'mysql', 'localhost', 3306, 'nl2sql', 'nl2sql_user', 'nl2sql_pass', 'jdbc:mysql://localhost:3306/nl2sql', 'active', 'success', '本地MySQL数据库，包含核心业务数据', 2100246635, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
description = VALUES(description), update_time = NOW();

INSERT INTO datasource (id, name, type, host, port, database_name, username, password, connection_url, status, test_status, description, creator_id, create_time, update_time) VALUES 
(2, '数据仓库MySQL', 'mysql', 'localhost', 3306, 'data_warehouse', 'nl2sql_user', 'nl2sql_pass', 'jdbc:mysql://localhost:3306/data_warehouse', 'active', 'success', '数据仓库，用于数据分析和报表生成', 2100246635, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
description = VALUES(description), update_time = NOW();

-- 智能体数据源关联示例数据
INSERT INTO agent_datasource (id, agent_id, datasource_id, is_active, create_time, update_time) VALUES 
(1, 1, 2, 1, NOW(), NOW())  -- 中国人口GDP数据智能体使用数据仓库
ON DUPLICATE KEY UPDATE 
is_active = VALUES(is_active), update_time = NOW();

INSERT INTO agent_datasource (id, agent_id, datasource_id, is_active, create_time, update_time) VALUES 
(2, 2, 1, 1, NOW(), NOW())  -- 销售数据分析智能体使用本地数据库
ON DUPLICATE KEY UPDATE 
is_active = VALUES(is_active), update_time = NOW();

-- 智能体预设问题示例数据
INSERT INTO agent_preset_question (id, agent_id, question, sort_order, is_active, create_time, update_time) VALUES 
(1, 1, '查询2023年中国总人口数量', 1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
question = VALUES(question), update_time = NOW();

INSERT INTO agent_preset_question (id, agent_id, question, sort_order, is_active, create_time, update_time) VALUES 
(2, 1, '显示近5年GDP增长趋势', 2, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
question = VALUES(question), update_time = NOW();

INSERT INTO agent_preset_question (id, agent_id, question, sort_order, is_active, create_time, update_time) VALUES 
(3, 2, '查询本月销售额排名前10的产品', 1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
question = VALUES(question), update_time = NOW();

INSERT INTO agent_preset_question (id, agent_id, question, sort_order, is_active, create_time, update_time) VALUES 
(4, 2, '分析客户满意度变化趋势', 2, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
question = VALUES(question), update_time = NOW();
