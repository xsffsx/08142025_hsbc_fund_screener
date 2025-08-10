# 数据库服务总览

## 🎯 服务状态概览

| 数据库 | 状态 | 端口 | 版本 | 特性 |
|--------|------|------|------|------|
| **MySQL** | ✅ 健康 | 3306 | 8.0 | 关系型数据库 |
| **Oracle** | ✅ 健康 | 1521 | 23-slim | 企业级数据库 |
| **PostgreSQL** | ✅ 健康 | 5432 | 13.21 + pgvector 0.8.0 | 向量数据库 |
| **Qdrant** | ⚠️ 不健康 | 6333-6334 | v1.7.4 | 向量搜索引擎 |

## 🚀 PostgreSQL + pgvector 配置详情

### 版本信息
- **PostgreSQL**: 13.21 (Debian 13.21-1.pgdg120+1) on aarch64-unknown-linux-gnu
- **pgvector扩展**: 0.8.0 (比您截图中的0.7.4更新)
- **Docker镜像**: pgvector/pgvector:pg13

### 连接信息
```yaml
Host: localhost
Port: 5432
Database: nl2sql
Username: nl2sql_user
Password: nl2sql_pass
```

### 数据表结构
1. **employees** - 员工信息 + 384维技能向量
2. **document_embeddings** - 文档嵌入 + 1536维向量 (OpenAI格式)
3. **query_stats** - 查询统计 + 384维查询向量

### 向量操作能力
- ✅ L2距离搜索 (`<->`)
- ✅ 余弦相似度搜索 (`<=>`)
- ✅ 内积相似度搜索 (`<#>`)
- ✅ IVFFlat索引优化
- ✅ HNSW索引支持

## 📊 性能测试结果

### 向量相似度搜索测试
```sql
-- 测试查询结果
    name     |  department  |      distance      
-------------+--------------+--------------------
 Jane Smith  | Data Science | 0.9797955819944689
 John Doe    | Engineering  | 0.9797956124113462
 Bob Johnson | Product      |  2.939388723079792
```

## 🛠️ 管理命令

### 启动所有服务
```bash
docker-compose up -d
```

### 启动PostgreSQL服务
```bash
docker-compose up -d postgresql
```

### 连接PostgreSQL
```bash
docker exec -it postgresql-nl2sql-mvp1 psql -U nl2sql_user -d nl2sql
```

### 运行测试脚本
```bash
docker exec -i postgresql-nl2sql-mvp1 psql -U nl2sql_user -d nl2sql < postgresql/test_postgresql.sql
```

## 📁 文件结构
```
postgresql/
├── README.md                    # 详细配置文档
├── spring-boot-config.yml       # Spring Boot配置示例
├── Employee.java                # JPA实体类示例
├── init/
│   └── 01_init_database.sql    # 数据库初始化脚本
└── test_postgresql.sql         # 功能验证脚本
```

## 🔧 Spring Boot集成

### 依赖配置
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-pgvector-store-spring-boot-starter</artifactId>
</dependency>
```

### 应用配置
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/nl2sql
    username: nl2sql_user
    password: nl2sql_pass
  ai:
    vectorstore:
      pgvector:
        index-type: HNSW
        distance-type: COSINE_DISTANCE
        dimensions: 1536
```

## 🎯 使用场景

### 1. NL2SQL向量搜索
- 存储SQL查询模板的向量表示
- 基于自然语言查询找到最相似的SQL模板
- 支持语义搜索和相似度排序

### 2. 文档检索增强生成 (RAG)
- 存储文档片段的向量嵌入
- 快速检索相关文档内容
- 支持多模态向量搜索

### 3. 推荐系统
- 用户行为向量化
- 商品特征向量化
- 实时相似度计算和推荐

## ⚡ 性能优化建议

1. **索引策略**: 数据量大时使用IVFFlat，精度要求高时使用HNSW
2. **内存配置**: 建议至少2GB内存用于向量操作
3. **批量操作**: 使用批量插入提高向量数据导入效率
4. **维度选择**: 根据实际需求选择合适的向量维度

## 🔍 下一步计划

1. **修复Qdrant服务**: 检查Qdrant配置和健康状态
2. **性能基准测试**: 对比PostgreSQL和Qdrant的向量搜索性能
3. **集成测试**: 编写Spring Boot集成测试用例
4. **监控配置**: 添加向量数据库性能监控
