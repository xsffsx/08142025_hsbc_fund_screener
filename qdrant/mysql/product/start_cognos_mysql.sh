#!/bin/bash

# =====================================================
# Cognos MySQL Database Startup Script
# 启动Cognos MySQL数据库脚本
# 创建时间: 2025-01-04 14:30:40
# =====================================================

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# 检查Docker是否运行
check_docker() {
    log_info "检查Docker状态..."
    if ! docker info > /dev/null 2>&1; then
        log_error "Docker未运行，请先启动Docker"
        exit 1
    fi
    log_success "Docker运行正常"
}

# 检查docker-compose文件
check_compose_file() {
    log_info "检查docker-compose.yaml文件..."
    if [ ! -f "../../docker-compose.yaml" ]; then
        log_error "docker-compose.yaml文件不存在"
        exit 1
    fi
    log_success "docker-compose.yaml文件存在"
}

# 创建必要的目录
create_directories() {
    log_info "创建必要的目录..."

    # 创建MySQL初始化目录
    mkdir -p ../../mysql/init
    mkdir -p ../../mysql/data

    # 创建Qdrant配置目录
    mkdir -p ../../qdrant/config

    log_success "目录创建完成"
}

# 停止并清理现有容器
cleanup_containers() {
    log_info "清理现有容器..."

    # 切换到docker-compose.yaml所在目录
    cd ../../

    # 停止容器
    docker-compose down --remove-orphans 2>/dev/null || true

    # 删除相关的volume（可选）
    read -p "是否删除现有数据卷？这将清除所有数据 (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        docker volume rm $(docker volume ls -q | grep -E "(mysql_data|qdrant_data)") 2>/dev/null || true
        log_warning "数据卷已删除"
    fi

    # 切换回原目录
    cd mysql/product/

    log_success "容器清理完成"
}

# 启动服务
start_services() {
    log_info "启动MySQL和Qdrant服务..."

    # 切换到docker-compose.yaml所在目录
    cd ../../

    # 启动服务
    docker-compose up -d

    # 切换回原目录
    cd mysql/product/

    log_success "服务启动命令已执行"
}

# 等待MySQL就绪
wait_for_mysql() {
    log_info "等待MySQL服务就绪..."
    
    local max_attempts=60
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if docker exec mysql-nl2sql-mvp1 mysqladmin ping -h localhost -u root -proot123 --silent 2>/dev/null; then
            log_success "MySQL服务已就绪"
            return 0
        fi
        
        echo -n "."
        sleep 2
        ((attempt++))
    done
    
    log_error "MySQL服务启动超时"
    return 1
}

# 验证数据库连接
verify_database() {
    log_info "验证数据库连接和表结构..."
    
    # 检查数据库是否存在
    if docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "USE nl2sql; SHOW TABLES;" > /dev/null 2>&1; then
        log_success "数据库nl2sql连接成功"
        
        # 显示表列表
        log_info "数据库表列表:"
        docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "USE nl2sql; SHOW TABLES;"
        
        # 检查关键表的记录数
        log_info "关键表记录数统计:"
        docker exec mysql-nl2sql-mvp1 mysql -u root -proot123 -e "
        USE nl2sql;
        SELECT 'TB_PROD' AS table_name, COUNT(*) AS record_count FROM TB_PROD
        UNION ALL
        SELECT 'CDE_DESC_VALUE' AS table_name, COUNT(*) AS record_count FROM CDE_DESC_VALUE
        UNION ALL
        SELECT 'TB_PROD_USER_DEFIN_EXT_FIELD' AS table_name, COUNT(*) AS record_count FROM TB_PROD_USER_DEFIN_EXT_FIELD;
        "
    else
        log_error "数据库连接失败"
        return 1
    fi
}

# 显示连接信息
show_connection_info() {
    log_info "数据库连接信息:"
    echo "=================================="
    echo "MySQL 连接信息:"
    echo "  主机: localhost"
    echo "  端口: 3306"
    echo "  数据库: nl2sql"
    echo "  用户名: nl2sql_user"
    echo "  密码: nl2sql_pass"
    echo "  Root密码: root123"
    echo ""
    echo "Qdrant 连接信息:"
    echo "  HTTP API: http://localhost:6333"
    echo "  gRPC API: localhost:6334"
    echo ""
    echo "Docker 容器:"
    echo "  MySQL: mysql-nl2sql-mvp1"
    echo "  Qdrant: qdrant-nl2sql-mvp1"
    echo "=================================="
}

# 显示有用的命令
show_useful_commands() {
    log_info "有用的命令:"
    echo "=================================="
    echo "连接MySQL:"
    echo "  docker exec -it mysql-nl2sql-mvp1 mysql -u root -proot123 nl2sql"
    echo ""
    echo "查看容器日志:"
    echo "  docker-compose logs -f mysql"
    echo "  docker-compose logs -f qdrant"
    echo ""
    echo "停止服务:"
    echo "  docker-compose down"
    echo ""
    echo "重启服务:"
    echo "  docker-compose restart"
    echo ""
    echo "查看容器状态:"
    echo "  docker-compose ps"
    echo "=================================="
}

# 主函数
main() {
    echo "========================================"
    echo "🚀 Cognos MySQL Database Setup"
    echo "========================================"
    
    # 检查前置条件
    check_docker
    check_compose_file
    
    # 创建目录
    create_directories
    
    # 询问是否清理现有容器
    read -p "是否清理现有容器？(y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        cleanup_containers
    fi
    
    # 启动服务
    start_services
    
    # 等待MySQL就绪
    if wait_for_mysql; then
        # 验证数据库
        verify_database
        
        # 显示连接信息
        show_connection_info
        
        # 显示有用命令
        show_useful_commands
        
        log_success "🎉 Cognos MySQL数据库启动完成！"
    else
        log_error "❌ 数据库启动失败"
        exit 1
    fi
}

# 执行主函数
main "$@"
