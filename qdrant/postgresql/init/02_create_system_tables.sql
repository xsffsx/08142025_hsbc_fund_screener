-- PostgreSQL 系统表初始化脚本
-- 创建NL2SQL系统表结构

-- 启用pgvector扩展 (如果还没有启用)
CREATE EXTENSION IF NOT EXISTS vector;

-- 创建系统表

-- 1. AI代理配置表
CREATE TABLE IF NOT EXISTS agent (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    config JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 代理数据源关联表
CREATE TABLE IF NOT EXISTS agent_datasource (
    id BIGSERIAL PRIMARY KEY,
    agent_id BIGINT REFERENCES agent(id),
    datasource_id BIGINT,
    config JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. 代理知识库表
CREATE TABLE IF NOT EXISTS agent_knowledge (
    id BIGSERIAL PRIMARY KEY,
    agent_id BIGINT REFERENCES agent(id),
    knowledge_type VARCHAR(50),
    content TEXT,
    vector_embedding vector(1536), -- OpenAI embedding维度
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. 代理预设问题表
CREATE TABLE IF NOT EXISTS agent_preset_question (
    id BIGSERIAL PRIMARY KEY,
    agent_id BIGINT REFERENCES agent(id),
    question TEXT NOT NULL,
    expected_sql TEXT,
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. 业务知识表
CREATE TABLE IF NOT EXISTS business_knowledge (
    id BIGSERIAL PRIMARY KEY,
    domain VARCHAR(100),
    concept VARCHAR(200),
    definition TEXT,
    synonyms JSONB,
    vector_embedding vector(1536),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. 数据源配置表
CREATE TABLE IF NOT EXISTS datasource (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    connection_config JSONB,
    schema_info JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. 语义模型表
CREATE TABLE IF NOT EXISTS semantic_model (
    id BIGSERIAL PRIMARY KEY,
    table_name VARCHAR(100),
    column_name VARCHAR(100),
    semantic_type VARCHAR(50),
    business_meaning TEXT,
    synonyms JSONB,
    vector_embedding vector(1536),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_agent_name ON agent(name);
CREATE INDEX IF NOT EXISTS idx_agent_datasource_agent_id ON agent_datasource(agent_id);
CREATE INDEX IF NOT EXISTS idx_agent_knowledge_agent_id ON agent_knowledge(agent_id);
CREATE INDEX IF NOT EXISTS idx_agent_knowledge_type ON agent_knowledge(knowledge_type);
CREATE INDEX IF NOT EXISTS idx_agent_preset_question_agent_id ON agent_preset_question(agent_id);
CREATE INDEX IF NOT EXISTS idx_agent_preset_question_category ON agent_preset_question(category);
CREATE INDEX IF NOT EXISTS idx_business_knowledge_domain ON business_knowledge(domain);
CREATE INDEX IF NOT EXISTS idx_business_knowledge_concept ON business_knowledge(concept);
CREATE INDEX IF NOT EXISTS idx_datasource_name ON datasource(name);
CREATE INDEX IF NOT EXISTS idx_datasource_type ON datasource(type);
CREATE INDEX IF NOT EXISTS idx_semantic_model_table ON semantic_model(table_name);
CREATE INDEX IF NOT EXISTS idx_semantic_model_column ON semantic_model(column_name);
CREATE INDEX IF NOT EXISTS idx_semantic_model_type ON semantic_model(semantic_type);

-- 创建向量索引 (提高向量搜索性能)
CREATE INDEX IF NOT EXISTS idx_agent_knowledge_vector ON agent_knowledge 
USING ivfflat (vector_embedding vector_cosine_ops) WITH (lists = 100);

CREATE INDEX IF NOT EXISTS idx_business_knowledge_vector ON business_knowledge 
USING ivfflat (vector_embedding vector_cosine_ops) WITH (lists = 100);

CREATE INDEX IF NOT EXISTS idx_semantic_model_vector ON semantic_model 
USING ivfflat (vector_embedding vector_cosine_ops) WITH (lists = 100);

-- 创建更新时间触发器函数
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 为agent表创建更新时间触发器
CREATE TRIGGER update_agent_updated_at 
    BEFORE UPDATE ON agent 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- 插入初始数据
INSERT INTO agent (name, description, config) VALUES 
('NL2SQL Agent', 'Default NL2SQL conversion agent', '{"model": "gpt-4", "temperature": 0.1, "max_tokens": 1024}')
ON CONFLICT DO NOTHING;

INSERT INTO datasource (name, type, connection_config) VALUES 
('Business Oracle DB', 'oracle', '{"host": "localhost", "port": 1521, "database": "XE", "schema": "nl2sql_user"}'),
('System PostgreSQL DB', 'postgresql', '{"host": "localhost", "port": 5432, "database": "nl2sql", "schema": "public"}')
ON CONFLICT DO NOTHING;

INSERT INTO business_knowledge (domain, concept, definition, synonyms) VALUES 
('Finance', 'Customer', 'Bank customer information and profiles', '["client", "account holder", "user", "客户"]'),
('Finance', 'Product', 'Financial products and services offered', '["service", "offering", "instrument", "产品"]'),
('Finance', 'Transaction', 'Financial transactions and activities', '["activity", "operation", "trade", "交易"]'),
('Finance', 'Fund', 'Investment funds and mutual funds', '["mutual fund", "investment fund", "基金"]'),
('Finance', 'Account', 'Bank accounts and financial accounts', '["banking account", "financial account", "账户"]')
ON CONFLICT DO NOTHING;

INSERT INTO semantic_model (table_name, column_name, semantic_type, business_meaning, synonyms) VALUES 
('CUSTOMER', 'CUSTOMER_ID', 'identifier', 'Unique customer identifier', '["客户ID", "客户编号", "customer code"]'),
('CUSTOMER', 'CUSTOMER_NAME', 'name', 'Customer full name', '["客户姓名", "客户名称", "name"]'),
('CUSTOMER', 'CUSTOMER_TYPE', 'category', 'Customer classification type', '["客户类型", "客户分类", "type"]'),
('B_UT_PROD', 'PROD_ID', 'identifier', 'Product unique identifier', '["产品ID", "产品编号", "product code"]'),
('B_UT_PROD', 'PROD_NAME', 'name', 'Product name', '["产品名称", "产品名字", "product name"]'),
('B_UT_PROD', 'PROD_TYPE', 'category', 'Product type classification', '["产品类型", "产品分类", "product type"]'),
('STG_FUNDS_CSV', 'FUND_CODE', 'identifier', 'Fund code identifier', '["基金代码", "基金编号", "fund id"]'),
('STG_FUNDS_CSV', 'FUND_NAME', 'name', 'Fund name', '["基金名称", "基金名字", "fund name"]'),
('STG_FUNDS_CSV', 'NAV_VALUE', 'amount', 'Net Asset Value', '["净值", "基金净值", "NAV"]')
ON CONFLICT DO NOTHING;

-- 显示初始化完成信息
SELECT 'PostgreSQL system tables initialization completed!' as status;

-- 显示创建的表
SELECT 'Created system tables:' as info;
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
  AND table_name IN ('agent', 'agent_datasource', 'agent_knowledge', 'agent_preset_question', 
                     'business_knowledge', 'datasource', 'semantic_model')
ORDER BY table_name;
