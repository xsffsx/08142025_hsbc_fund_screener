-- =====================================================
-- 为agent_id=2添加products表相关的语义映射配置
-- 基于现有数据库表结构，不使用模拟数据
-- =====================================================

-- 设置正确的字符集
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET CHARACTER SET utf8mb4;

-- 1. 在business_knowledge表中添加表名映射的业务知识
-- 表结构: business_knowledge(id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time)

INSERT INTO business_knowledge 
(id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time) 
VALUES 
(3001, 'B_UT_PROD', '基金产品主表，存储基金产品的基本信息', 'products,fund_products,基金产品,产品表,funds', 1, NULL, 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
description = VALUES(description), 
synonyms = VALUES(synonyms),
updated_time = NOW();

INSERT INTO business_knowledge 
(id, business_term, description, synonyms, is_recall, data_set_id, agent_id, created_time, updated_time) 
VALUES 
(3002, 'RETURN_RATE', '产品收益率字段，表示产品的年化收益率', 'return_rate,收益率,年化收益率,回报率', 1, NULL, 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
description = VALUES(description), 
synonyms = VALUES(synonyms),
updated_time = NOW();

-- 2. 在semantic_model表中添加products相关的字段映射配置
-- 表结构: semantic_model(id, agent_id, field_name, synonyms, origin_name, description, origin_description, type, created_time, updated_time, is_recall, status)

-- 添加product_id字段映射
INSERT INTO semantic_model 
(id, agent_id, field_name, synonyms, origin_name, description, origin_description, type, created_time, updated_time, is_recall, status) 
VALUES 
(3001, 2, 'product_id', 'product_id,产品ID,产品标识,Product ID', 'B_UT_PROD.PROD_ID', '产品唯一标识符，对应数据库中的PROD_ID字段', 'B_UT_PROD表的产品ID主键', 'integer', NOW(), NOW(), 1, 1)
ON DUPLICATE KEY UPDATE 
synonyms = VALUES(synonyms),
origin_name = VALUES(origin_name),
description = VALUES(description),
updated_time = NOW();

-- 添加product_name字段映射
INSERT INTO semantic_model 
(id, agent_id, field_name, synonyms, origin_name, description, origin_description, type, created_time, updated_time, is_recall, status) 
VALUES 
(3002, 2, 'product_name', 'product_name,产品名称,产品名,Product Name', 'B_UT_PROD.PROD_NAME', '产品名称，对应数据库中的PROD_NAME字段', 'B_UT_PROD表的产品名称字段', 'string', NOW(), NOW(), 1, 1)
ON DUPLICATE KEY UPDATE 
synonyms = VALUES(synonyms),
origin_name = VALUES(origin_name),
description = VALUES(description),
updated_time = NOW();

-- 添加return_rate字段映射（保持原字段名，因为数据库中确实有RETURN_RATE字段）
INSERT INTO semantic_model 
(id, agent_id, field_name, synonyms, origin_name, description, origin_description, type, created_time, updated_time, is_recall, status) 
VALUES 
(3003, 2, 'return_rate', 'return_rate,收益率,年化收益率,回报率,RETURN_RATE', 'RETURN_RATE', '产品收益率，年化收益率百分比', '产品的年化收益率字段', 'decimal', NOW(), NOW(), 1, 1)
ON DUPLICATE KEY UPDATE 
synonyms = VALUES(synonyms),
origin_name = VALUES(origin_name),
description = VALUES(description),
updated_time = NOW();

-- 添加products表名映射（通过同义词实现）
INSERT INTO semantic_model
(id, agent_id, field_name, synonyms, origin_name, description, origin_description, type, created_time, updated_time, is_recall, status)
VALUES
(3004, 2, 'products', 'products,fund_products,基金产品,产品表,funds,hk_funds,funds_table,基金表,香港基金,基金数据表', 'B_UT_PROD', '产品表名映射，将通用表名products映射到实际的B_UT_PROD表', 'B_UT_PROD基金产品主表', 'table', NOW(), NOW(), 1, 1)
ON DUPLICATE KEY UPDATE
synonyms = VALUES(synonyms),
origin_name = VALUES(origin_name),
description = VALUES(description),
updated_time = NOW();

-- 3. 验证插入的数据
-- 查询验证business_knowledge表中的新增数据
SELECT 'business_knowledge验证' as table_name, id, business_term, synonyms, agent_id 
FROM business_knowledge 
WHERE agent_id = 2 AND id >= 3001;

-- 查询验证semantic_model表中的新增数据
SELECT 'semantic_model验证' as table_name, id, field_name, synonyms, origin_name, agent_id 
FROM semantic_model 
WHERE agent_id = 2 AND id >= 3001;

-- 4. 显示agent_id=2的所有语义映射配置统计
SELECT 
    'agent_id=2语义映射统计' as info,
    (SELECT COUNT(*) FROM business_knowledge WHERE agent_id = 2) as business_knowledge_count,
    (SELECT COUNT(*) FROM semantic_model WHERE agent_id = 2 AND status = 1) as semantic_model_count;
