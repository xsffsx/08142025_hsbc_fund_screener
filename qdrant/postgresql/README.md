# PostgreSQL + pgvector 配置文档

## 版本信息
- **PostgreSQL**: 13.21 (Debian 13.21-1.pgdg120+1) on aarch64-unknown-linux-gnu
- **pgvector扩展**: 0.8.0
- **Docker镜像**: pgvector/pgvector:pg13

## 连接信息
```yaml
Host: localhost
Port: 5432
Database: nl2sql
Username: nl2sql_user
Password: nl2sql_pass
```

## 服务管理

### 启动服务
```bash
docker-compose up -d postgresql
```

### 停止服务
```bash
docker-compose stop postgresql
```

### 查看日志
```bash
docker-compose logs -f postgresql
```

### 连接数据库
```bash
# 使用psql客户端连接
docker exec -it postgresql-nl2sql-mvp1 psql -U nl2sql_user -d nl2sql

# 或者使用外部客户端
psql -h localhost -p 5432 -U nl2sql_user -d nl2sql
```

## 数据库结构

### 1. employees表 (员工信息 + 技能向量)
```sql
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE,
    department VARCHAR(50),
    salary DECIMAL(10,2),
    hire_date DATE DEFAULT CURRENT_DATE,
    skills_vector vector(384),  -- 384维技能向量
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. document_embeddings表 (文档嵌入)
```sql
CREATE TABLE document_embeddings (
    id SERIAL PRIMARY KEY,
    document_name VARCHAR(200) NOT NULL,
    content TEXT,
    embedding vector(1536),  -- OpenAI embedding维度
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. query_stats表 (查询统计)
```sql
CREATE TABLE query_stats (
    id SERIAL PRIMARY KEY,
    query_text TEXT NOT NULL,
    query_vector vector(384),
    execution_time_ms INTEGER,
    result_count INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 向量操作示例

### 1. 向量相似度搜索 (L2距离)
```sql
SELECT 
    name, 
    department,
    skills_vector <-> array_fill(0.15, ARRAY[384])::vector as distance
FROM employees 
ORDER BY skills_vector <-> array_fill(0.15, ARRAY[384])::vector
LIMIT 3;
```

### 2. 余弦相似度搜索
```sql
SELECT 
    name,
    1 - (skills_vector <=> array_fill(0.15, ARRAY[384])::vector) as cosine_similarity
FROM employees 
ORDER BY skills_vector <=> array_fill(0.15, ARRAY[384])::vector
LIMIT 3;
```

### 3. 内积相似度
```sql
SELECT 
    name,
    skills_vector <#> array_fill(0.15, ARRAY[384])::vector as inner_product
FROM employees 
ORDER BY skills_vector <#> array_fill(0.15, ARRAY[384])::vector
LIMIT 3;
```

## 索引优化

### IVFFlat索引 (适合大数据集)
```sql
CREATE INDEX employees_skills_vector_idx ON employees 
USING ivfflat (skills_vector vector_cosine_ops) WITH (lists = 100);
```

### HNSW索引 (适合高精度搜索)
```sql
CREATE INDEX employees_skills_hnsw_idx ON employees 
USING hnsw (skills_vector vector_cosine_ops) WITH (m = 16, ef_construction = 64);
```

## 性能监控

### 查看索引使用情况
```sql
SELECT 
    schemaname,
    tablename,
    indexname,
    indexdef
FROM pg_indexes 
WHERE indexname LIKE '%vector%';
```

### 查看向量维度
```sql
SELECT 
    'employees.skills_vector' as table_column,
    vector_dims(skills_vector) as dimensions
FROM employees 
LIMIT 1;
```

## 测试脚本
运行完整功能测试：
```bash
docker exec -i postgresql-nl2sql-mvp1 psql -U nl2sql_user -d nl2sql < postgresql/test_postgresql.sql
```

## 注意事项
1. **向量维度一致性**: 确保插入的向量维度与表定义一致
2. **索引创建时机**: 建议在有足够数据后再创建向量索引
3. **内存配置**: 向量操作需要足够的内存，建议至少2GB
4. **备份策略**: 定期备份向量数据，注意备份文件大小
