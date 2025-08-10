-- PostgreSQL + pgvector 功能验证脚本

-- 显示PostgreSQL版本
SELECT version();

-- 显示pgvector扩展信息
SELECT extname, extversion FROM pg_extension WHERE extname = 'vector';

-- 显示当前数据库和用户
SELECT current_database(), current_user;

-- 测试基本表查询
SELECT 'Testing basic table operations...' as test_phase;
SELECT * FROM employees ORDER BY id;

-- 测试向量相似度搜索
SELECT 'Testing vector similarity search...' as test_phase;
SELECT 
    name, 
    department,
    skills_vector <-> '[0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]'::vector as distance
FROM employees 
ORDER BY skills_vector <-> '[0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]'::vector
LIMIT 3;

-- 测试余弦相似度
SELECT 'Testing cosine similarity...' as test_phase;
SELECT 
    name,
    1 - (skills_vector <=> '[0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]'::vector) as cosine_similarity
FROM employees 
ORDER BY skills_vector <=> '[0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]'::vector
LIMIT 3;

-- 测试文档嵌入表
SELECT 'Testing document embeddings...' as test_phase;
SELECT document_name, content, metadata FROM document_embeddings;

-- 测试聚合函数
SELECT 'Testing aggregation functions...' as test_phase;
SELECT 
    department,
    COUNT(*) as employee_count,
    AVG(salary) as avg_salary,
    MAX(salary) as max_salary,
    MIN(salary) as min_salary
FROM employees 
GROUP BY department
ORDER BY department;

-- 测试JSON操作
SELECT 'Testing JSONB operations...' as test_phase;
SELECT 
    document_name,
    metadata->>'category' as category,
    metadata->>'difficulty' as difficulty
FROM document_embeddings;

-- 显示表结构
SELECT 'Showing table structures...' as test_phase;
\d employees
\d document_embeddings

-- 显示索引信息
SELECT 'Showing vector indexes...' as test_phase;
SELECT 
    schemaname,
    tablename,
    indexname,
    indexdef
FROM pg_indexes 
WHERE indexname LIKE '%vector%';

-- 测试向量维度
SELECT 'Testing vector dimensions...' as test_phase;
SELECT 
    'employees.skills_vector' as table_column,
    vector_dims(skills_vector) as dimensions
FROM employees 
LIMIT 1;

SELECT 
    'document_embeddings.embedding' as table_column,
    vector_dims(embedding) as dimensions
FROM document_embeddings 
LIMIT 1;

-- 最终状态确认
SELECT 'PostgreSQL + pgvector verification completed successfully!' as final_status;
