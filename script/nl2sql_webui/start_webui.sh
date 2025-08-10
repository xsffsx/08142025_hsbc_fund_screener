#!/bin/bash

# =============================================================================
# NL2SQL Vue.js Web UI 启动脚本
# =============================================================================
# 功能: 自动启动Vue.js Web UI应用
# 作者: NL2SQL Team
# 版本: 1.0.0
# 日期: 2025-08-10
# =============================================================================

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
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

log_header() {
    echo -e "${CYAN}=== $1 ===${NC}"
}

# 脚本配置
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
WEBUI_DIR="$PROJECT_ROOT/spring-ai-alibaba/spring-ai-alibaba-nl2sql/spring-ai-alibaba-nl2sql-web-ui"
LOG_DIR="$PROJECT_ROOT/logs"
LOG_FILE="$LOG_DIR/webui.log"

# 创建日志目录
mkdir -p "$LOG_DIR"

# 显示脚本信息
show_banner() {
    log_header "NL2SQL Vue.js Web UI 启动脚本"
    echo "项目根目录: $PROJECT_ROOT"
    echo "Web UI目录: $WEBUI_DIR"
    echo "日志文件: $LOG_FILE"
    echo ""
}

# 检查依赖
check_dependencies() {
    log_header "检查系统依赖"
    
    # 检查Node.js
    if ! command -v node &> /dev/null; then
        log_error "Node.js 未安装，请先安装 Node.js (推荐版本 >= 16)"
        exit 1
    fi
    
    NODE_VERSION=$(node --version)
    log_success "Node.js 版本: $NODE_VERSION"
    
    # 检查npm
    if ! command -v npm &> /dev/null; then
        log_error "npm 未安装，请先安装 npm"
        exit 1
    fi
    
    NPM_VERSION=$(npm --version)
    log_success "npm 版本: $NPM_VERSION"
    
    # 检查Web UI目录
    if [ ! -d "$WEBUI_DIR" ]; then
        log_error "Web UI目录不存在: $WEBUI_DIR"
        exit 1
    fi
    
    log_success "系统依赖检查通过"
}

# 检查后端服务
check_backend() {
    log_header "检查后端服务状态"
    
    if curl -s http://localhost:8065/actuator/health &> /dev/null; then
        log_success "Spring Boot后端服务运行正常 (端口8065)"
    else
        log_warning "Spring Boot后端服务未启动或不可访问"
        log_warning "请确保后端服务在端口8065运行"
        log_warning "可以使用以下命令启动后端:"
        log_warning "  ./script/start_all_service.sh start"
    fi
}

# 安装依赖
install_dependencies() {
    log_header "安装npm依赖"
    
    cd "$WEBUI_DIR"
    
    if [ ! -f "package.json" ]; then
        log_error "package.json 文件不存在"
        exit 1
    fi
    
    # 检查是否需要安装依赖
    if [ ! -d "node_modules" ] || [ "package.json" -nt "node_modules" ]; then
        log_info "安装npm依赖..."
        npm install 2>&1 | tee -a "$LOG_FILE"
        
        if [ ${PIPESTATUS[0]} -eq 0 ]; then
            log_success "npm依赖安装成功"
        else
            log_error "npm依赖安装失败，请检查日志: $LOG_FILE"
            exit 1
        fi
    else
        log_success "npm依赖已是最新，跳过安装"
    fi
}

# 启动开发服务器
start_dev_server() {
    log_header "启动Vue.js开发服务器"
    
    cd "$WEBUI_DIR"
    
    log_info "启动配置:"
    log_info "- 本地访问: http://localhost:3000 (如端口被占用会自动切换)"
    log_info "- API代理: /api/* -> http://localhost:8065"
    log_info "- NL2SQL代理: /nl2sql/* -> http://localhost:8065"
    log_info "- 日志文件: $LOG_FILE"
    echo ""
    
    log_info "正在启动开发服务器..."
    log_info "按 Ctrl+C 停止服务"
    echo ""
    
    # 启动开发服务器并记录日志
    npm run dev 2>&1 | tee -a "$LOG_FILE"
}

# 清理函数
cleanup() {
    log_info "正在停止Web UI服务..."
    # 这里可以添加清理逻辑
}

# 注册清理函数
trap cleanup EXIT

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  -h, --help     显示帮助信息"
    echo "  -c, --check    仅检查依赖，不启动服务"
    echo "  -i, --install  仅安装依赖，不启动服务"
    echo "  --no-backend   跳过后端服务检查"
    echo ""
    echo "示例:"
    echo "  $0              # 完整启动流程"
    echo "  $0 --check     # 仅检查依赖"
    echo "  $0 --install   # 仅安装依赖"
}

# 主函数
main() {
    local check_only=false
    local install_only=false
    local skip_backend=false
    
    # 解析命令行参数
    while [[ $# -gt 0 ]]; do
        case $1 in
            -h|--help)
                show_help
                exit 0
                ;;
            -c|--check)
                check_only=true
                shift
                ;;
            -i|--install)
                install_only=true
                shift
                ;;
            --no-backend)
                skip_backend=true
                shift
                ;;
            *)
                log_error "未知选项: $1"
                show_help
                exit 1
                ;;
        esac
    done
    
    # 显示横幅
    show_banner
    
    # 检查依赖
    check_dependencies
    
    # 检查后端服务
    if [ "$skip_backend" = false ]; then
        check_backend
    fi
    
    # 如果只是检查，则退出
    if [ "$check_only" = true ]; then
        log_success "依赖检查完成"
        exit 0
    fi
    
    # 安装依赖
    install_dependencies
    
    # 如果只是安装，则退出
    if [ "$install_only" = true ]; then
        log_success "依赖安装完成"
        exit 0
    fi
    
    # 启动开发服务器
    start_dev_server
}

# 执行主函数
main "$@"
