#!/bin/bash

# =============================================================================
# NL2SQL 数据库初始化脚本
# 功能: 重新初始化数据库，解决中文乱码问题
# 作者: Augment Agent
# 创建时间: $(date '+%Y-%m-%d %H:%M:%S')
# =============================================================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 项目根目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
MYSQL_INIT_DIR="$PROJECT_ROOT/qdrant/mysql/init"

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

# 检查Docker服务
check_docker() {
    log_info "检查Docker服务状态..."
    if ! docker info >/dev/null 2>&1; then
        log_error "Docker服务未运行，请先启动Docker"
        exit 1
    fi
    log_success "Docker服务正常运行"
}

# 检查MySQL容器
check_mysql_container() {
    log_info "检查MySQL容器状态..."
    if ! docker ps | grep -q "mysql-nl2sql-mvp1"; then
        log_error "MySQL容器未运行，请先启动服务"
        log_info "运行命令: bash $PROJECT_ROOT/script/start_all_service.sh start"
        exit 1
    fi
    log_success "MySQL容器运行正常"
}

# 等待MySQL服务就绪
wait_mysql_ready() {
    log_info "等待MySQL服务就绪..."
    local max_attempts=30
    local attempt=0
    
    while [ $attempt -lt $max_attempts ]; do
        if docker exec mysql-nl2sql-mvp1 mysqladmin ping -h localhost -u nl2sql_user -pnl2sql_pass --silent >/dev/null 2>&1; then
            log_success "MySQL服务已就绪"
            return 0
        fi
        
        attempt=$((attempt + 1))
        echo -n "."
        sleep 2
    done
    
    echo
    log_error "MySQL服务启动超时"
    exit 1
}

# 备份现有数据
backup_existing_data() {
    log_info "备份现有数据..."
    local backup_file="nl2sql_backup_$(date +%Y%m%d_%H%M%S).sql"
    
    docker exec mysql-nl2sql-mvp1 mysqldump -u nl2sql_user -pnl2sql_pass \
        --default-character-set=utf8mb4 \
        --single-transaction \
        --routines \
        --triggers \
        nl2sql > "$PROJECT_ROOT/backup/$backup_file" 2>/dev/null
    
    if [ $? -eq 0 ]; then
        log_success "数据备份完成: $backup_file"
    else
        log_warning "数据备份失败，继续执行初始化"
    fi
}

# 重新初始化数据库
reinit_database() {
    log_info "=== 重新初始化数据库 ==="
    
    # 1. 删除并重新创建数据库
    log_info "重新创建数据库..."
    docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 --default-character-set=utf8mb4 -e "
        DROP DATABASE IF EXISTS nl2sql;
        CREATE DATABASE nl2sql CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
        GRANT ALL PRIVILEGES ON nl2sql.* TO 'nl2sql_user'@'%';
        FLUSH PRIVILEGES;
    " 2>/dev/null
    
    if [ $? -eq 0 ]; then
        log_success "数据库重新创建完成"
    else
        log_error "数据库创建失败"
        exit 1
    fi
    
    # 2. 执行初始化脚本（按顺序）
    local init_scripts=(
        "01-init-database.sql"
        "02-init-data.sql"
        "03-management-schema.sql"
        "04-management-data.sql"
        "90_finance_mds.ddl"
    )
    
    for script in "${init_scripts[@]}"; do
        local script_path="$MYSQL_INIT_DIR/$script"
        if [ -f "$script_path" ]; then
            log_info "执行初始化脚本: $script"
            docker exec -i mysql-nl2sql-mvp1 mysql -u nl2sql_user -pnl2sql_pass \
                --default-character-set=utf8mb4 \
                -D nl2sql < "$script_path" 2>/dev/null

            if [ $? -eq 0 ]; then
                log_success "$script 执行完成"
            else
                log_error "$script 执行失败"
                exit 1
            fi
        else
            log_warning "脚本文件不存在: $script"
        fi
    done
}

# 验证字符集设置
verify_charset() {
    log_info "验证数据库字符集设置..."
    
    # 检查数据库字符集
    local db_charset=$(docker exec mysql-nl2sql-mvp1 mysql -u nl2sql_user -pnl2sql_pass \
        --default-character-set=utf8mb4 -D nl2sql -e \
        "SELECT DEFAULT_CHARACTER_SET_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME='nl2sql';" \
        --skip-column-names 2>/dev/null)
    
    if [ "$db_charset" = "utf8mb4" ]; then
        log_success "数据库字符集: $db_charset"
    else
        log_error "数据库字符集错误: $db_charset (应为 utf8mb4)"
        exit 1
    fi
    
    # 检查关键表的字符集
    local tables=("semantic_model" "business_knowledge" "agent")
    for table in "${tables[@]}"; do
        local table_charset=$(docker exec mysql-nl2sql-mvp1 mysql -u nl2sql_user -pnl2sql_pass \
            --default-character-set=utf8mb4 -D nl2sql -e \
            "SELECT TABLE_COLLATION FROM information_schema.TABLES WHERE TABLE_SCHEMA='nl2sql' AND TABLE_NAME='$table';" \
            --skip-column-names 2>/dev/null)
        
        if [[ "$table_charset" == *"utf8mb4"* ]]; then
            log_success "表 $table 字符集: $table_charset"
        else
            log_warning "表 $table 字符集: $table_charset"
        fi
    done
}

# 验证中文数据
verify_chinese_data() {
    log_info "验证中文数据..."
    
    # 测试语义模型表中的中文数据
    local chinese_test=$(docker exec mysql-nl2sql-mvp1 mysql -u nl2sql_user -pnl2sql_pass \
        --default-character-set=utf8mb4 -D nl2sql -e \
        "SELECT field_name, synonyms FROM semantic_model WHERE id = 2003;" \
        --skip-column-names 2>/dev/null | head -1)
    
    if [[ "$chinese_test" == *"产品类型"* ]]; then
        log_success "中文数据验证通过: $chinese_test"
    else
        log_error "中文数据验证失败: $chinese_test"
        exit 1
    fi
}

# 验证字段API
verify_fields_api() {
    log_info "验证字段API..."
    
    # 等待Spring Boot应用启动
    local max_attempts=30
    local attempt=0
    
    while [ $attempt -lt $max_attempts ]; do
        if curl -s "http://localhost:8065/actuator/health" >/dev/null 2>&1; then
            log_success "Spring Boot应用已启动"
            break
        fi
        
        attempt=$((attempt + 1))
        echo -n "."
        sleep 2
    done
    
    if [ $attempt -eq $max_attempts ]; then
        log_error "Spring Boot应用启动超时"
        exit 1
    fi
    
    # 测试字段API
    log_info "测试字段API..."
    local api_response=$(curl -s -H "Accept: application/json; charset=utf-8" \
        "http://localhost:8065/api/fields" | jq -r '.[0].fieldSynonyms' 2>/dev/null)
    
    if [[ "$api_response" == *"产品"* ]]; then
        log_success "字段API验证通过: $api_response"
    else
        log_error "字段API验证失败: $api_response"
        exit 1
    fi
}

# 主函数
main() {
    echo -e "${BLUE}===========================================${NC}"
    echo -e "${BLUE}  NL2SQL 数据库重新初始化脚本${NC}"
    echo -e "${BLUE}===========================================${NC}"
    echo ""
    
    # 创建备份目录
    mkdir -p "$PROJECT_ROOT/backup"
    
    # 执行步骤
    check_docker
    check_mysql_container
    wait_mysql_ready
    backup_existing_data
    reinit_database
    verify_charset
    verify_chinese_data
    
    log_success "=== 数据库初始化完成 ==="
    echo ""
    log_info "下一步: 重启Spring Boot应用以加载新数据"
    log_info "运行命令: bash $PROJECT_ROOT/script/start_all_service.sh restart"
    echo ""
    log_info "然后验证字段API:"
    log_info "curl -H 'Accept: application/json; charset=utf-8' http://localhost:8065/api/fields"
}

# 执行主函数
main "$@"
