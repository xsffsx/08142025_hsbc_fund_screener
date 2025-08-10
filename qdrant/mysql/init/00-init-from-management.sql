-- MySQL数据库初始化脚本 (基于management模块)
-- 创建NL2SQL数据库和用户，使用management模块的SQL文件

-- 创建数据库
CREATE DATABASE IF NOT EXISTS nl2sql CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户并授权
CREATE USER IF NOT EXISTS 'nl2sql_user'@'%' IDENTIFIED BY 'nl2sql_pass';
GRANT ALL PRIVILEGES ON nl2sql.* TO 'nl2sql_user'@'%';
FLUSH PRIVILEGES;

-- 使用数据库
USE nl2sql;

-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 注意：实际的表结构将从management模块的schema.sql文件中加载
-- 这个脚本主要负责数据库和用户的创建
