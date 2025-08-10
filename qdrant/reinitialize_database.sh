#!/bin/bash
# MySQL 向量数据库重新初始化脚本
# 基于 management 模块的 SQL 文件重新初始化数据库

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_banner() {
    echo "================================================================================"
    echo "🔄 MySQL 向量数据库重新初始化脚本"
    echo "================================================================================"
    echo "⏰ 执行时间: $(date '+%Y-%m-%d %H:%M:%S')"
    echo "📁 项目根目录: $PROJECT_ROOT"
    echo "🗄️ 基于 management 模块 SQL 文件重新初始化"
    echo ""
}

# 检查Docker是否运行
check_docker() {
    log_info "检查Docker服务状态..."
    if ! docker info >/dev/null 2>&1; then
        log_error "Docker未运行，请先启动Docker"
        exit 1
    fi
    log_success "Docker服务正常运行"
}

# 停止并删除现有容器
cleanup_containers() {
    log_info "清理现有MySQL容器..."
    
    # 停止容器
    if docker ps -q -f name=mysql-nl2sql-mvp1 | grep -q .; then
        log_info "停止MySQL容器..."
        docker stop mysql-nl2sql-mvp1
    fi
    
    # 删除容器
    if docker ps -aq -f name=mysql-nl2sql-mvp1 | grep -q .; then
        log_info "删除MySQL容器..."
        docker rm mysql-nl2sql-mvp1
    fi
    
    # 删除数据卷
    if docker volume ls -q -f name=qdrant_mysql_data | grep -q .; then
        log_warning "删除MySQL数据卷..."
        docker volume rm qdrant_mysql_data 2>/dev/null || true
    fi
    
    log_success "容器清理完成"
}

# 验证SQL文件
verify_sql_files() {
    log_info "验证SQL文件..."
    
    local management_schema="$PROJECT_ROOT/spring-ai-alibaba-nl2sql-management/src/main/resources/sql/schema.sql"
    local management_data="$PROJECT_ROOT/spring-ai-alibaba-nl2sql-management/src/main/resources/sql/data.sql"
    local init_schema="$SCRIPT_DIR/mysql/init/03-management-schema.sql"
    local init_data="$SCRIPT_DIR/mysql/init/04-management-data.sql"
    
    if [ ! -f "$management_schema" ]; then
        log_error "Management模块schema.sql文件不存在: $management_schema"
        exit 1
    fi
    
    if [ ! -f "$management_data" ]; then
        log_error "Management模块data.sql文件不存在: $management_data"
        exit 1
    fi
    
    if [ ! -f "$init_schema" ]; then
        log_error "初始化schema文件不存在: $init_schema"
        exit 1
    fi
    
    if [ ! -f "$init_data" ]; then
        log_error "初始化data文件不存在: $init_data"
        exit 1
    fi
    
    log_success "SQL文件验证通过"
}

# 启动MySQL容器
start_mysql() {
    log_info "启动MySQL容器..."
    
    cd "$SCRIPT_DIR"
    
    # 只启动MySQL服务
    docker-compose up -d mysql
    
    if [ $? -eq 0 ]; then
        log_success "MySQL容器启动成功"
    else
        log_error "MySQL容器启动失败"
        exit 1
    fi
}

# 等待MySQL就绪
wait_for_mysql() {
    log_info "等待MySQL服务就绪..."
    
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if docker exec mysql-nl2sql-mvp1 mysqladmin ping -h localhost -u root -proot123 >/dev/null 2>&1; then
            log_success "MySQL服务已就绪"
            return 0
        fi
        
        log_info "等待MySQL启动... ($attempt/$max_attempts)"
        sleep 2
        ((attempt++))
    done
    
    log_error "MySQL服务启动超时"
    exit 1
}

# 验证数据库初始化
verify_database() {
    log_info "验证数据库初始化..."

    # 检查数据库是否存在
    local db_exists=$(docker exec mysql-nl2sql-mvp1 mysql -u nl2sql_user -pnl2sql_pass -e "SHOW DATABASES LIKE 'nl2sql';" 2>/dev/null | grep nl2sql | wc -l)
    if [ "$db_exists" -eq 0 ]; then
        log_error "数据库 nl2sql 不存在"
        exit 1
    fi

    # 检查表是否存在
    local table_count=$(docker exec mysql-nl2sql-mvp1 mysql -u nl2sql_user -pnl2sql_pass -D nl2sql -e "SHOW TABLES;" 2>/dev/null | wc -l)
    if [ "$table_count" -lt 7 ]; then
        log_error "数据库表数量不足，期望7个表，实际 $((table_count-1)) 个"
        exit 1
    fi

    # 检查数据是否存在
    local agent_count=$(docker exec mysql-nl2sql-mvp1 mysql -u nl2sql_user -pnl2sql_pass -D nl2sql -e "SELECT COUNT(*) FROM agent;" 2>/dev/null | tail -n 1)
    if [ "$agent_count" -lt 4 ]; then
        log_error "智能体数据不足，期望4个，实际 $agent_count 个"
        exit 1
    fi

    log_success "数据库初始化验证通过"
    log_info "数据库表数量: $((table_count-1))"
    log_info "智能体数量: $agent_count"
}

# 显示数据库信息
show_database_info() {
    log_info "数据库信息:"
    echo ""

    log_info "=== 数据库连接信息 ==="
    echo "  主机: localhost:3306"
    echo "  数据库: nl2sql"
    echo "  用户名: nl2sql_user"
    echo "  密码: nl2sql_pass"
    echo "  Root密码: root123"
    echo ""

    log_info "=== 数据库表结构 ==="
    docker exec mysql-nl2sql-mvp1 mysql -u nl2sql_user -pnl2sql_pass -D nl2sql -e "SHOW TABLES;" 2>/dev/null | tail -n +2 | while read table; do
        echo "  📋 $table"
    done
    echo ""

    log_info "=== 智能体列表 ==="
    docker exec mysql-nl2sql-mvp1 mysql -u nl2sql_user -pnl2sql_pass -D nl2sql -e "SELECT id, name, status FROM agent;" 2>/dev/null | tail -n +2 | while read line; do
        echo "  🤖 $line"
    done
    echo ""
}

# 主函数
main() {
    print_banner
    
    case "${1:-reinit}" in
        "reinit"|"restart"|"reset")
            log_info "开始重新初始化数据库..."
            check_docker
            verify_sql_files
            cleanup_containers
            start_mysql
            wait_for_mysql
            verify_database
            show_database_info
            log_success "数据库重新初始化完成！"
            ;;
        "verify"|"check")
            log_info "验证数据库状态..."
            check_docker
            wait_for_mysql
            verify_database
            show_database_info
            ;;
        "info"|"status")
            log_info "显示数据库信息..."
            check_docker
            show_database_info
            ;;
        "help"|"-h"|"--help")
            echo "用法: $0 [命令]"
            echo ""
            echo "命令:"
            echo "  reinit    - 重新初始化数据库 (默认)"
            echo "  verify    - 验证数据库状态"
            echo "  info      - 显示数据库信息"
            echo "  help      - 显示帮助信息"
            echo ""
            echo "示例:"
            echo "  $0 reinit   # 重新初始化数据库"
            echo "  $0 verify   # 验证数据库状态"
            echo "  $0 info     # 显示数据库信息"
            ;;
        *)
            log_error "未知命令: $1"
            echo "使用 '$0 help' 查看帮助信息"
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
