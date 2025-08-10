-- PostgreSQL 13.18 with pgvector 0.7.4 初始化脚本
-- 创建扩展和测试数据

-- 创建pgvector扩展
CREATE EXTENSION IF NOT EXISTS vector;

-- 验证扩展版本
SELECT extname, extversion FROM pg_extension WHERE extname = 'vector';

-- 创建测试表（包含向量字段）
CREATE TABLE IF NOT EXISTS employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE,
    department VARCHAR(50),
    salary DECIMAL(10,2),
    hire_date DATE DEFAULT CURRENT_DATE,
    skills_vector vector(384),  -- 384维向量，常用于文本嵌入
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建向量索引（提高查询性能）
CREATE INDEX IF NOT EXISTS employees_skills_vector_idx ON employees 
USING ivfflat (skills_vector vector_cosine_ops) WITH (lists = 100);

-- 插入测试数据
INSERT INTO employees (name, email, department, salary, skills_vector) VALUES 
('John Doe', 'john.doe@company.com', 'Engineering', 75000.00, 
 '[0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]'::vector),
('Jane Smith', 'jane.smith@company.com', 'Data Science', 80000.00,
 '[0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.1]'::vector),
('Bob Johnson', 'bob.johnson@company.com', 'Product', 70000.00,
 '[0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.1, 0.2]'::vector)
ON CONFLICT (email) DO NOTHING;

-- 创建文档向量表（用于NL2SQL场景）
CREATE TABLE IF NOT EXISTS document_embeddings (
    id SERIAL PRIMARY KEY,
    document_name VARCHAR(200) NOT NULL,
    content TEXT,
    embedding vector(1536),  -- OpenAI embedding维度
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建文档向量索引
CREATE INDEX IF NOT EXISTS document_embeddings_vector_idx ON document_embeddings 
USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);

-- 插入示例文档嵌入
INSERT INTO document_embeddings (document_name, content, embedding, metadata) VALUES 
('SQL基础教程', '这是一个关于SQL基础语法的教程文档', 
 array_fill(0.1, ARRAY[1536])::vector, 
 '{"category": "tutorial", "language": "zh", "difficulty": "beginner"}'::jsonb),
('数据库设计指南', '介绍数据库设计的最佳实践和规范',
 array_fill(0.2, ARRAY[1536])::vector,
 '{"category": "guide", "language": "zh", "difficulty": "intermediate"}'::jsonb)
ON CONFLICT DO NOTHING;

-- 创建查询统计表
CREATE TABLE IF NOT EXISTS query_stats (
    id SERIAL PRIMARY KEY,
    query_text TEXT NOT NULL,
    query_vector vector(384),
    execution_time_ms INTEGER,
    result_count INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 授权给应用用户
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO nl2sql_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO nl2sql_user;

-- 显示初始化完成信息
SELECT 'PostgreSQL with pgvector initialization completed!' as status;
