#!/bin/bash
# HSBC Fund Database Restore Script
# 从备份文件恢复数据库

set -e

CONTAINER_NAME="postgresql-hsbc-fund-screener"
DB_USER="hsbc_user"
DB_NAME="hsbc_fund"
BACKUP_FILE="hsbc_fund_full_backup_20250814_183955.sql"

echo "🔄 恢复 HSBC Fund 数据库"
echo "========================"

# 检查备份文件
if [ ! -f "$BACKUP_FILE" ]; then
    echo "❌ 备份文件不存在: $BACKUP_FILE"
    exit 1
fi

# 检查容器
if ! docker ps | grep -q "$CONTAINER_NAME"; then
    echo "❌ PostgreSQL 容器未运行"
    exit 1
fi

echo "⚠️  警告: 此操作将删除现有数据库并重新创建"
read -p "确认继续? (y/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "操作已取消"
    exit 1
fi

# 删除现有数据库
echo "🗑️  删除现有数据库..."
docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d postgres -c "DROP DATABASE IF EXISTS $DB_NAME;"

# 创建新数据库
echo "🆕 创建新数据库..."
docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d postgres -c "CREATE DATABASE $DB_NAME;"

# 恢复数据
echo "📥 恢复数据..."
docker exec -i "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" < "$BACKUP_FILE"

echo "✅ 数据库恢复完成"

# 验证数据
FUND_COUNT=$(docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT COUNT(*) FROM hsbc_fund_unified;" | tr -d ' ')
echo "📊 验证: 恢复了 $FUND_COUNT 个基金产品"
