#!/bin/bash
# HSBC Fund Database Export Script
# 导出所有 DDL 和 DML 到当前目录

set -e

CONTAINER_NAME="postgresql-hsbc-fund-screener"
DB_USER="hsbc_user"
DB_NAME="hsbc_fund"
EXPORT_DIR="$(pwd)"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

echo "🗄️  HSBC Fund Database Export"
echo "================================"
echo "容器名称: $CONTAINER_NAME"
echo "数据库: $DB_NAME"
echo "导出目录: $EXPORT_DIR"
echo "时间戳: $TIMESTAMP"
echo ""

# 检查容器是否运行
if ! docker ps | grep -q "$CONTAINER_NAME"; then
    echo "❌ PostgreSQL 容器未运行"
    echo "请先启动容器: docker-compose up -d"
    exit 1
fi

echo "✅ PostgreSQL 容器正在运行"

# 检查数据库连接
if ! docker exec "$CONTAINER_NAME" pg_isready -U "$DB_USER" -d "$DB_NAME" > /dev/null 2>&1; then
    echo "❌ 数据库连接失败"
    exit 1
fi

echo "✅ 数据库连接正常"

# 获取数据统计
echo ""
echo "📊 数据库统计信息:"
FUND_COUNT=$(docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT COUNT(*) FROM hsbc_fund_unified;" | tr -d ' ')
echo "   基金产品数量: $FUND_COUNT"

DB_SIZE=$(docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT pg_size_pretty(pg_database_size('$DB_NAME'));" | tr -d ' ')
echo "   数据库大小: $DB_SIZE"

TABLE_SIZE=$(docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT pg_size_pretty(pg_total_relation_size('hsbc_fund_unified'));" | tr -d ' ')
echo "   表大小: $TABLE_SIZE"

echo ""
echo "🚀 开始导出..."

# 1. 导出完整备份（DDL + DML）
echo "📦 导出完整备份..."
docker exec "$CONTAINER_NAME" pg_dump \
    -U "$DB_USER" \
    -d "$DB_NAME" \
    --no-owner \
    --no-privileges \
    --verbose \
    > "${EXPORT_DIR}/hsbc_fund_full_backup_${TIMESTAMP}.sql"

echo "✅ 完整备份导出完成: hsbc_fund_full_backup_${TIMESTAMP}.sql"

# 2. 导出仅结构（DDL）
echo "🏗️  导出数据库结构..."
docker exec "$CONTAINER_NAME" pg_dump \
    -U "$DB_USER" \
    -d "$DB_NAME" \
    --schema-only \
    --no-owner \
    --no-privileges \
    --verbose \
    > "${EXPORT_DIR}/hsbc_fund_schema_${TIMESTAMP}.sql"

echo "✅ 数据库结构导出完成: hsbc_fund_schema_${TIMESTAMP}.sql"

# 3. 导出仅数据（DML）- 使用 COPY 格式（更快）
echo "📊 导出数据（COPY 格式）..."
docker exec "$CONTAINER_NAME" pg_dump \
    -U "$DB_USER" \
    -d "$DB_NAME" \
    --data-only \
    --no-owner \
    --no-privileges \
    --verbose \
    > "${EXPORT_DIR}/hsbc_fund_data_copy_${TIMESTAMP}.sql"

echo "✅ 数据导出完成（COPY 格式）: hsbc_fund_data_copy_${TIMESTAMP}.sql"

# 4. 导出仅数据（INSERT 格式）- 用于兼容性
echo "📊 导出数据（INSERT 格式）..."
docker exec "$CONTAINER_NAME" pg_dump \
    -U "$DB_USER" \
    -d "$DB_NAME" \
    --data-only \
    --no-owner \
    --no-privileges \
    --column-inserts \
    --verbose \
    > "${EXPORT_DIR}/hsbc_fund_data_inserts_${TIMESTAMP}.sql"

echo "✅ 数据导出完成（INSERT 格式）: hsbc_fund_data_inserts_${TIMESTAMP}.sql"

# 5. 导出样本数据（前100条记录）
echo "🔬 导出样本数据..."
docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -c "
COPY (
    SELECT * FROM hsbc_fund_unified 
    ORDER BY product_code 
    LIMIT 100
) TO STDOUT WITH CSV HEADER
" > "${EXPORT_DIR}/hsbc_fund_sample_${TIMESTAMP}.csv"

echo "✅ 样本数据导出完成: hsbc_fund_sample_${TIMESTAMP}.csv"

# 6. 导出表结构信息
echo "📋 导出表结构信息..."
docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -c "
SELECT 
    column_name,
    data_type,
    is_nullable,
    column_default,
    character_maximum_length
FROM information_schema.columns 
WHERE table_name = 'hsbc_fund_unified' 
ORDER BY ordinal_position;
" > "${EXPORT_DIR}/hsbc_fund_table_info_${TIMESTAMP}.txt"

echo "✅ 表结构信息导出完成: hsbc_fund_table_info_${TIMESTAMP}.txt"

# 7. 导出索引信息
echo "🔍 导出索引信息..."
docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -c "
SELECT 
    schemaname,
    tablename,
    indexname,
    indexdef
FROM pg_indexes 
WHERE tablename = 'hsbc_fund_unified'
ORDER BY indexname;
" > "${EXPORT_DIR}/hsbc_fund_indexes_${TIMESTAMP}.txt"

echo "✅ 索引信息导出完成: hsbc_fund_indexes_${TIMESTAMP}.txt"

# 8. 创建恢复脚本
echo "🔧 创建恢复脚本..."
cat > "${EXPORT_DIR}/restore_database_${TIMESTAMP}.sh" << EOF
#!/bin/bash
# HSBC Fund Database Restore Script
# 从备份文件恢复数据库

set -e

CONTAINER_NAME="postgresql-hsbc-fund-screener"
DB_USER="hsbc_user"
DB_NAME="hsbc_fund"
BACKUP_FILE="hsbc_fund_full_backup_${TIMESTAMP}.sql"

echo "🔄 恢复 HSBC Fund 数据库"
echo "========================"

# 检查备份文件
if [ ! -f "\$BACKUP_FILE" ]; then
    echo "❌ 备份文件不存在: \$BACKUP_FILE"
    exit 1
fi

# 检查容器
if ! docker ps | grep -q "\$CONTAINER_NAME"; then
    echo "❌ PostgreSQL 容器未运行"
    exit 1
fi

echo "⚠️  警告: 此操作将删除现有数据库并重新创建"
read -p "确认继续? (y/N): " -n 1 -r
echo
if [[ ! \$REPLY =~ ^[Yy]\$ ]]; then
    echo "操作已取消"
    exit 1
fi

# 删除现有数据库
echo "🗑️  删除现有数据库..."
docker exec "\$CONTAINER_NAME" psql -U "\$DB_USER" -d postgres -c "DROP DATABASE IF EXISTS \$DB_NAME;"

# 创建新数据库
echo "🆕 创建新数据库..."
docker exec "\$CONTAINER_NAME" psql -U "\$DB_USER" -d postgres -c "CREATE DATABASE \$DB_NAME;"

# 恢复数据
echo "📥 恢复数据..."
docker exec -i "\$CONTAINER_NAME" psql -U "\$DB_USER" -d "\$DB_NAME" < "\$BACKUP_FILE"

echo "✅ 数据库恢复完成"

# 验证数据
FUND_COUNT=\$(docker exec "\$CONTAINER_NAME" psql -U "\$DB_USER" -d "\$DB_NAME" -t -c "SELECT COUNT(*) FROM hsbc_fund_unified;" | tr -d ' ')
echo "📊 验证: 恢复了 \$FUND_COUNT 个基金产品"
EOF

chmod +x "${EXPORT_DIR}/restore_database_${TIMESTAMP}.sh"
echo "✅ 恢复脚本创建完成: restore_database_${TIMESTAMP}.sh"

# 显示导出文件列表
echo ""
echo "📁 导出文件列表:"
echo "================================"
ls -lh "${EXPORT_DIR}"/*"${TIMESTAMP}"* | while read line; do
    echo "   $line"
done

# 显示文件大小统计
echo ""
echo "📊 文件大小统计:"
echo "================================"
TOTAL_SIZE=$(ls -l "${EXPORT_DIR}"/*"${TIMESTAMP}"* | awk '{sum += $5} END {print sum}')
TOTAL_SIZE_MB=$((TOTAL_SIZE / 1024 / 1024))
echo "   总大小: ${TOTAL_SIZE_MB} MB"

echo ""
echo "🎉 数据库导出完成！"
echo "================================"
echo ""
echo "📝 文件说明:"
echo "   - hsbc_fund_full_backup_${TIMESTAMP}.sql     : 完整备份（DDL + DML）"
echo "   - hsbc_fund_schema_${TIMESTAMP}.sql          : 仅结构（DDL）"
echo "   - hsbc_fund_data_copy_${TIMESTAMP}.sql       : 仅数据（COPY 格式，快速）"
echo "   - hsbc_fund_data_inserts_${TIMESTAMP}.sql    : 仅数据（INSERT 格式，兼容）"
echo "   - hsbc_fund_sample_${TIMESTAMP}.csv          : 样本数据（100条记录）"
echo "   - hsbc_fund_table_info_${TIMESTAMP}.txt      : 表结构信息"
echo "   - hsbc_fund_indexes_${TIMESTAMP}.txt         : 索引信息"
echo "   - restore_database_${TIMESTAMP}.sh           : 恢复脚本"
echo ""
echo "💡 使用建议:"
echo "   - 开发环境: 使用 schema + sample 文件"
echo "   - 生产备份: 使用 full_backup 文件"
echo "   - 数据迁移: 使用 data_copy 文件（更快）"
echo ""
