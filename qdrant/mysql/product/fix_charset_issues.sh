#!/bin/bash

# =====================================================
# MySQL中文乱码修复脚本
# 修复nl2sql数据库中的字符集问题
# 创建时间: 2025-01-04 15:00:00
# =====================================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查MySQL容器状态
check_mysql_container() {
    log_info "检查MySQL容器状态..."
    if ! docker ps | grep -q mysql-nl2sql-mvp1; then
        log_error "MySQL容器未运行"
        exit 1
    fi
    log_success "MySQL容器运行正常"
}

# 备份现有数据库结构
backup_database_schema() {
    log_info "备份数据库结构..."
    docker exec mysql-nl2sql-mvp1 mysqldump -u root -proot123 --no-data --routines --triggers nl2sql > nl2sql_schema_backup.sql
    log_success "数据库结构备份完成: nl2sql_schema_backup.sql"
}

# 备份现有数据 (仅结构化数据，忽略乱码数据)
backup_important_data() {
    log_info "备份重要的非中文数据..."
    
    # 备份数据源配置 (通常不包含中文)
    docker exec mysql-nl2sql-mvp1 mysqldump -u root -proot123 --where="1" nl2sql datasource > datasource_backup.sql 2>/dev/null || true
    
    # 备份agent_datasource关联 (数字ID，无中文)
    docker exec mysql-nl2sql-mvp1 mysqldump -u root -proot123 --where="1" nl2sql agent_datasource > agent_datasource_backup.sql 2>/dev/null || true
    
    log_success "重要数据备份完成"
}

# 删除并重建数据库
recreate_database() {
    log_warning "即将删除并重建nl2sql数据库..."
    read -p "确认继续？这将删除所有现有数据 (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        log_info "用户取消操作"
        exit 0
    fi
    
    log_info "删除现有数据库..."
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "DROP DATABASE IF EXISTS nl2sql;"
    
    log_info "创建新数据库 (使用正确字符集)..."
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
    CREATE DATABASE nl2sql 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;
    "
    
    log_success "数据库重建完成"
}

# 重新执行SQL脚本 (使用正确字符集)
execute_sql_with_correct_charset() {
    local sql_file=$1
    local description=$2
    
    log_info "执行SQL脚本: $description"
    
    if [ ! -f "$sql_file" ]; then
        log_warning "SQL文件不存在: $sql_file，跳过"
        return 0
    fi
    
    # 使用正确的字符集执行SQL
    docker exec -i mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 nl2sql < "$sql_file"
    log_success "$description 完成"
}

# 创建修复后的SQL脚本
create_fixed_sql_scripts() {
    log_info "创建修复后的SQL脚本..."
    
    # 从原始SQL目录复制并修复
    local original_sql_dir="../../../spring-ai-alibaba-nl2sql-management/src/main/resources/sql"
    
    if [ -d "$original_sql_dir" ]; then
        # 复制schema.sql
        if [ -f "$original_sql_dir/schema.sql" ]; then
            cp "$original_sql_dir/schema.sql" "./schema_fixed.sql"
            # 在文件开头添加字符集设置
            sed -i '1i\
SET NAMES utf8mb4;\
SET CHARACTER SET utf8mb4;\
SET character_set_connection=utf8mb4;' "./schema_fixed.sql"
            log_success "创建修复的schema.sql"
        fi
        
        # 复制data.sql
        if [ -f "$original_sql_dir/data.sql" ]; then
            cp "$original_sql_dir/data.sql" "./data_fixed.sql"
            # 在文件开头添加字符集设置
            sed -i '1i\
SET NAMES utf8mb4;\
SET CHARACTER SET utf8mb4;\
SET character_set_connection=utf8mb4;' "./data_fixed.sql"
            log_success "创建修复的data.sql"
        fi
    else
        log_warning "原始SQL目录不存在: $original_sql_dir"
        log_info "将创建基础的修复脚本..."
        create_basic_fixed_scripts
    fi
}

# 创建基础修复脚本
create_basic_fixed_scripts() {
    # 创建基础的schema脚本
    cat > "./schema_fixed.sql" << 'EOF'
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;

-- 智能体表
CREATE TABLE IF NOT EXISTS `agent` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '智能体名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '智能体描述',
  `status` tinyint DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能体表';

-- 数据源表
CREATE TABLE IF NOT EXISTS `datasource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据源名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据源类型',
  `connection_info` json COMMENT '连接信息',
  `status` tinyint DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据源表';

-- 智能体数据源关联表
CREATE TABLE IF NOT EXISTS `agent_datasource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agent_id` bigint NOT NULL COMMENT '智能体ID',
  `datasource_id` bigint NOT NULL COMMENT '数据源ID',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_agent_datasource` (`agent_id`,`datasource_id`),
  KEY `idx_agent_id` (`agent_id`),
  KEY `idx_datasource_id` (`datasource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能体数据源关联表';

-- 智能体知识表
CREATE TABLE IF NOT EXISTS `agent_knowledge` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agent_id` bigint NOT NULL COMMENT '智能体ID',
  `knowledge_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '知识类型',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '知识内容',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_agent_id` (`agent_id`),
  KEY `idx_knowledge_type` (`knowledge_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能体知识表';

-- 智能体预设问题表
CREATE TABLE IF NOT EXISTS `agent_preset_question` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agent_id` bigint NOT NULL COMMENT '智能体ID',
  `question` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '预设问题',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '预设答案',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_agent_id` (`agent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能体预设问题表';

-- 业务知识表
CREATE TABLE IF NOT EXISTS `business_knowledge` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `business_term` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '业务术语',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '术语描述',
  `category` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '分类',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_business_term` (`business_term`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务知识表';
EOF

    # 创建基础的数据脚本
    cat > "./data_fixed.sql" << 'EOF'
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;

-- 插入智能体数据
INSERT INTO `agent` (`name`, `description`) VALUES
('中国人口GDP数据智能体', '专门处理中国人口和GDP相关数据查询分析的智能体'),
('销售数据分析智能体', '专注于销售数据分析和业务指标计算的智能体'),
('财务报表智能体', '专门处理财务数据和报表分析的智能体');

-- 插入数据源数据
INSERT INTO `datasource` (`name`, `type`, `connection_info`) VALUES
('MySQL-nl2sql', 'mysql', '{"host":"localhost","port":3306,"database":"nl2sql","username":"nl2sql_user"}');

-- 插入智能体数据源关联
INSERT INTO `agent_datasource` (`agent_id`, `datasource_id`) VALUES
(1, 1),
(2, 1),
(3, 1);

-- 插入业务知识
INSERT INTO `business_knowledge` (`business_term`, `description`, `category`) VALUES
('净资产收益率', '净利润与平均净资产的比率，衡量企业盈利能力', '财务指标'),
('毛利率', '毛利润占营业收入的百分比，反映产品盈利能力', '财务指标'),
('流动比率', '流动资产与流动负债的比率，衡量短期偿债能力', '财务指标');
EOF

    log_success "创建基础修复脚本完成"
}

# 验证修复结果
verify_fix() {
    log_info "验证修复结果..."
    
    # 检查字符集设置
    log_info "检查数据库字符集..."
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
    SELECT 
        SCHEMA_NAME as database_name,
        DEFAULT_CHARACTER_SET_NAME as charset,
        DEFAULT_COLLATION_NAME as collation
    FROM information_schema.SCHEMATA 
    WHERE SCHEMA_NAME = 'nl2sql';
    "
    
    # 检查表字符集
    log_info "检查表字符集..."
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
    USE nl2sql;
    SELECT 
        TABLE_NAME,
        TABLE_COLLATION
    FROM information_schema.TABLES 
    WHERE TABLE_SCHEMA = 'nl2sql' AND TABLE_TYPE = 'BASE TABLE';
    "
    
    # 测试中文数据显示
    log_info "测试中文数据显示..."
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 -e "
    USE nl2sql;
    SELECT name, description FROM agent LIMIT 3;
    "
    
    log_success "修复验证完成"
}

# 主函数
main() {
    echo "========================================"
    echo "🔧 MySQL中文乱码修复工具"
    echo "========================================"
    
    # 检查前置条件
    check_mysql_container
    
    # 备份数据
    backup_database_schema
    backup_important_data
    
    # 重建数据库
    recreate_database
    
    # 创建修复脚本
    create_fixed_sql_scripts
    
    # 执行修复脚本
    execute_sql_with_correct_charset "schema_fixed.sql" "创建表结构"
    execute_sql_with_correct_charset "data_fixed.sql" "插入基础数据"
    
    # 如果有Cognos数据，重新插入
    if [ -f "01_create_cognos_tables_mysql.sql" ]; then
        log_info "重新创建Cognos表结构..."
        execute_sql_with_correct_charset "01_create_cognos_tables_mysql.sql" "Cognos表结构"
    fi
    
    if [ -f "02_insert_sample_data.sql" ]; then
        log_info "重新插入Cognos示例数据..."
        execute_sql_with_correct_charset "02_insert_sample_data.sql" "Cognos示例数据"
    fi
    
    # 验证修复结果
    verify_fix
    
    echo ""
    log_success "🎉 MySQL中文乱码修复完成！"
    echo "========================================"
    echo "📝 修复总结:"
    echo "  • 数据库字符集: utf8mb4"
    echo "  • 表字符集: utf8mb4_unicode_ci"
    echo "  • 客户端连接: 使用 --default-character-set=utf8mb4"
    echo "  • 备份文件: nl2sql_schema_backup.sql"
    echo ""
    echo "📋 后续建议:"
    echo "  • 更新应用程序连接字符串"
    echo "  • 配置MySQL容器默认字符集"
    echo "  • 验证所有中文数据显示正常"
    echo "========================================"
}

# 执行主函数
main "$@"
