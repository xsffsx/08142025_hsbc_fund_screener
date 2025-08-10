-- PostgreSQL 初始化脚本
-- 为 NL2SQL 项目创建数据库和用户
-- 基于 docker-compose.yaml 配置

-- 创建数据库（如果不存在）
-- 注意：POSTGRES_DB 环境变量已经创建了 nl2sql 数据库

-- 确保 nl2sql_user 用户存在并有正确权限
DO $$
BEGIN
    -- 检查用户是否存在，如果不存在则创建
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'nl2sql_user') THEN
        CREATE USER nl2sql_user WITH PASSWORD 'nl2sql_pass';
    END IF;
END
$$;

-- 授予用户对数据库的完整权限
GRANT ALL PRIVILEGES ON DATABASE nl2sql TO nl2sql_user;

-- 连接到 nl2sql 数据库
\c nl2sql;

-- 授予用户对 public schema 的权限
GRANT ALL ON SCHEMA public TO nl2sql_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO nl2sql_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO nl2sql_user;

-- 设置默认权限，确保新创建的对象也有正确权限
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO nl2sql_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO nl2sql_user;

-- 启用 pgvector 扩展（用于向量存储）
CREATE EXTENSION IF NOT EXISTS vector;

-- 创建基础表结构（如果需要）
-- 这里可以添加您的表结构定义

-- 输出确认信息
SELECT 'PostgreSQL 初始化完成' AS status;
SELECT 'nl2sql_user 用户权限配置完成' AS user_status;
SELECT 'pgvector 扩展已启用' AS extension_status;
