#!/bin/bash
# HSBC Fund NL2SQL Inspector - 服务监控与调试工具
# 基于 text-to-graphql-mcp inspector 设计

# 操作系统检测
detect_os() {
    case "$(uname -s)" in
        MINGW*|MSYS*|CYGWIN*)
            OS_TYPE="windows"
            ;;
        Darwin*)
            OS_TYPE="macos"
            ;;
        Linux*)
            OS_TYPE="linux"
            ;;
        *)
            OS_TYPE="unknown"
            ;;
    esac
}

# 初始化操作系统检测
detect_os

# 项目根目录
PROJECT_ROOT="/Users/paulo/PycharmProjects/20250809/08132025_hsbc_fund_screener"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TEMP_DIR="/tmp"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_banner() {
    echo "================================================================================"
    echo "🚀 HSBC Fund NL2SQL Inspector - 服务监控与调试工具"
    echo "================================================================================"
    echo "📅 启动时间: $(date '+%Y-%m-%d %H:%M:%S')"
    echo "📁 项目根目录: $PROJECT_ROOT"
    echo "🔧 操作系统: $OS_TYPE"
    echo ""
}

print_service_info() {
    echo "🎯 管理的服务:"
    echo "   nl2sql_api       - FastAPI NL2SQL 服务     (端口 8080)"
    echo "   postgresql       - PostgreSQL 数据库      (端口 5433)"
    echo "   lm_studio        - LM Studio 本地 LLM     (端口 1234)"
    echo ""
}

# 检查端口是否被占用
check_port() {
    local port=$1

    if [ "$OS_TYPE" = "windows" ]; then
        netstat -an | grep ":$port " | grep -q "LISTENING"
        return $?
    else
        lsof -i :$port >/dev/null 2>&1
        return $?
    fi
}

# 获取占用端口的进程PID
get_port_pid() {
    local port=$1

    if [ "$OS_TYPE" = "windows" ]; then
        local line=$(netstat -ano | grep ":$port " | grep "LISTENING" | head -1)
        if [ -n "$line" ]; then
            echo "$line" | awk '{print $5}'
        fi
    else
        lsof -ti :$port 2>/dev/null
    fi
}

# 检查服务健康状态
check_service_health() {
    local url=$1
    curl -s --max-time 5 "$url" >/dev/null 2>&1
    return $?
}

# 检查 PostgreSQL 容器状态
check_postgresql() {
    local container_name="postgresql-hsbc-fund-screener"

    if docker ps | grep -q "$container_name"; then
        print_success "PostgreSQL 容器运行中"

        # 检查数据库连接
        if docker exec -i "$container_name" psql -U hsbc_user -d hsbc_fund -c "SELECT 1;" >/dev/null 2>&1; then
            print_success "PostgreSQL 数据库连接正常"

            # 检查基金数据
            local fund_count=$(docker exec -i "$container_name" psql -U hsbc_user -d hsbc_fund -t -c "SELECT COUNT(*) FROM hsbc_fund_unified;" 2>/dev/null | tr -d ' ')
            if [ "$fund_count" -gt 0 ]; then
                print_success "基金数据: $fund_count 条记录"
            else
                print_warning "基金数据为空"
            fi
        else
            print_error "PostgreSQL 数据库连接失败"
        fi
    else
        print_error "PostgreSQL 容器未运行"
        print_info "启动命令: cd $PROJECT_ROOT/docker/postgres && docker-compose up -d"
    fi
}

# 检查 LM Studio 状态
check_lm_studio() {
    if check_service_health "http://localhost:1234/v1/models"; then
        print_success "LM Studio API 服务正常"

        # 获取模型列表
        local models=$(curl -s http://localhost:1234/v1/models | jq -r '.data[].id' 2>/dev/null | head -3)
        if [ -n "$models" ]; then
            print_info "可用模型:"
            echo "$models" | while read model; do
                echo "    - $model"
            done
        fi
    else
        print_error "LM Studio API 服务不可用"
        print_info "请确保 LM Studio 已启动并开启 OpenAI 兼容 API 服务"
    fi
}

# 检查 NL2SQL API 状态
check_nl2sql_api() {
    if check_port 8080; then
        local pid=$(get_port_pid 8080)
        print_success "NL2SQL API 服务运行中 (PID: $pid)"

        # 检查健康状态
        if check_service_health "http://localhost:8080/health"; then
            print_success "NL2SQL API 健康检查通过"
        else
            print_warning "NL2SQL API 健康检查失败"
        fi
    else
        print_error "NL2SQL API 服务未运行"
        print_info "启动命令: cd $SCRIPT_DIR && ./run_api.sh"
    fi
}

# 检查虚拟环境
check_venv() {
    if [ -d "$SCRIPT_DIR/venv_api" ]; then
        print_success "虚拟环境存在: $SCRIPT_DIR/venv_api"

        # 检查关键依赖
        if [ -f "$SCRIPT_DIR/venv_api/bin/python" ]; then
            local python_version=$("$SCRIPT_DIR/venv_api/bin/python" --version 2>&1)
            print_info "Python 版本: $python_version"

            # 检查关键包
            local packages=("langchain" "fastapi" "psycopg")
            for pkg in "${packages[@]}"; do
                if "$SCRIPT_DIR/venv_api/bin/python" -c "import $pkg" 2>/dev/null; then
                    print_success "依赖包 $pkg 已安装"
                else
                    print_error "依赖包 $pkg 未安装"
                fi
            done
        fi
    else
        print_error "虚拟环境不存在"
        print_info "创建命令: cd $SCRIPT_DIR && python3 -m venv venv_api"
    fi
}

# 显示日志文件
show_logs() {
    print_info "📋 可用的日志文件:"
    echo "--------------------------------------------------------------------------------"

    local logs_found=false

    # 统一 NL2SQL 日志
    if [ -f "$SCRIPT_DIR/logs/hsbc_fund_nl2sql.log" ]; then
        local nl2sql_lines=$(wc -l < "$SCRIPT_DIR/logs/hsbc_fund_nl2sql.log" 2>/dev/null || echo "0")
        echo "🔗 NL2SQL 统一日志: $SCRIPT_DIR/logs/hsbc_fund_nl2sql.log ($nl2sql_lines 行)"
        logs_found=true
    fi

    echo "--------------------------------------------------------------------------------"

    if [ "$logs_found" = "false" ]; then
        print_warning "未找到日志文件"
        print_info "请先启动 NL2SQL API 服务"
        return 1
    fi

    # 显示最近的错误和警告
    echo ""
    print_info "🚨 最近的错误和警告:"
    echo "--------------------------------------------------------------------------------"

    if [ -f "$SCRIPT_DIR/logs/hsbc_fund_nl2sql.log" ]; then
        echo "📄 hsbc_fund_nl2sql.log:"
        grep -E "(ERROR|WARNING)" "$SCRIPT_DIR/logs/hsbc_fund_nl2sql.log" 2>/dev/null | tail -10 | sed 's/^/   /' || echo "   无错误或警告"
    fi

    echo "--------------------------------------------------------------------------------"

    # 提供查看完整日志的建议
    echo ""
    print_info "📖 查看完整日志的命令:"
    echo "   tail -f $SCRIPT_DIR/logs/hsbc_fund_nl2sql.log   # 实时 NL2SQL 统一日志"
    echo "   grep ERROR $SCRIPT_DIR/logs/*.log               # 查看所有错误"
    echo "   grep 'nl2sql.api' $SCRIPT_DIR/logs/hsbc_fund_nl2sql.log    # 仅查看 API 日志"
    echo "   grep 'nl2sql.core' $SCRIPT_DIR/logs/hsbc_fund_nl2sql.log   # 仅查看核心日志"
    echo ""
}

# 日志归档（在重启/启动前调用）：将 logs/*.log 打包至 logs/archive/<timestamp>_logs.tar.gz，并清空原日志内容
archive_logs() {
    local LOG_DIR="$SCRIPT_DIR/logs"
    local ARCHIVE_DIR="$LOG_DIR/archive"
    mkdir -p "$ARCHIVE_DIR"

    # 收集日志文件
    shopt -s nullglob
    local log_files=("$LOG_DIR"/*.log)
    shopt -u nullglob

    if [ ${#log_files[@]} -eq 0 ]; then
        print_warning "无可归档的日志文件"
        return 0
    fi

    local ts
    ts=$(date '+%Y%m%d%H%M%S')
    local tarball="$ARCHIVE_DIR/${ts}_logs.tar.gz"

    print_info "归档日志到: $tarball"
    # 兼容 macOS 的 bsdtar：不使用 --remove-files；成功后再清空源日志
    tar -czf "$tarball" -C "$LOG_DIR" "${log_files[@]##$LOG_DIR/}" 2>/dev/null
    local rc=$?
    if [ $rc -eq 0 ] && [ -f "$tarball" ]; then
        # 清空原日志内容（保留文件以便后续继续写入）
        for f in "${log_files[@]}"; do
            : > "$f" || true
        done
        print_success "日志归档完成"
    else
        print_error "日志归档失败（tar 不可用或归档出错）"
        return 1
    fi
}


# 执行系统诊断
run_diagnostics() {
    print_info "🔍 执行系统诊断..."
    echo ""

    print_info "1️⃣  检查 PostgreSQL 数据库..."
    check_postgresql
    echo ""

    print_info "2️⃣  检查 LM Studio 服务..."
    check_lm_studio
    echo ""

    print_info "3️⃣  检查 NL2SQL API 服务..."
    check_nl2sql_api
    echo ""

    print_info "4️⃣  检查虚拟环境..."
    check_venv
    echo ""

    print_success "🎉 系统诊断完成"
}

# 启动所有服务
start_services() {
    print_info "🚀 启动 NL2SQL 服务..."

    # 1. 启动 PostgreSQL
    print_info "启动 PostgreSQL 容器..."
    cd "$PROJECT_ROOT/docker/postgres"
    if docker-compose up -d; then
        print_success "PostgreSQL 容器启动成功"
        sleep 3  # 等待数据库启动
    else
        print_error "PostgreSQL 容器启动失败"
        return 1
    fi

    # 2. 启动 NL2SQL API
    print_info "启动 NL2SQL API 服务..."
    cd "$SCRIPT_DIR"
    if [ -f "run_api.sh" ]; then
        nohup ./run_api.sh > "$TEMP_DIR/nl2sql_startup.log" 2>&1 &
        local api_pid=$!
        print_success "NL2SQL API 启动中 (PID: $api_pid)"

        # 等待服务启动
        print_info "等待服务启动..."
        for i in {1..30}; do
            if check_service_health "http://localhost:8080/health"; then
                print_success "NL2SQL API 服务启动成功"
                break
            fi
            sleep 1
        done
    else
        print_error "未找到 run_api.sh 启动脚本"
        return 1
    fi

    print_success "🎉 所有服务启动完成"
}

# 停止所有服务
stop_services() {
    print_info "🛑 停止 NL2SQL 服务..."

    # 停止 API 服务
    if check_port 8080; then
        local pid=$(get_port_pid 8080)
        if [ -n "$pid" ]; then
            print_info "停止 NL2SQL API 服务 (PID: $pid)..."
            kill "$pid" 2>/dev/null
            sleep 2
            print_success "NL2SQL API 服务已停止"
        fi
    fi

    # 停止 PostgreSQL 容器
    print_info "停止 PostgreSQL 容器..."
    cd "$PROJECT_ROOT/docker/postgres"
    if docker-compose down; then
        print_success "PostgreSQL 容器已停止"
    fi

    print_success "🎉 所有服务已停止"
}

# 显示服务状态
show_status() {
    echo "📊 服务状态:"
    echo "--------------------------------------------------------------------------------"

    # PostgreSQL
    if docker ps | grep -q "postgresql-hsbc-fund-screener"; then
        echo "postgresql       | PostgreSQL 数据库      | 端口 5433 | ✅ 运行中"
    else
        echo "postgresql       | PostgreSQL 数据库      | 端口 5433 | ❌ 未运行"
    fi

    # LM Studio
    if check_service_health "http://localhost:1234/v1/models"; then
        echo "lm_studio        | LM Studio 本地 LLM     | 端口 1234 | ✅ 运行中"
    else
        echo "lm_studio        | LM Studio 本地 LLM     | 端口 1234 | ❌ 未运行"
    fi

    # NL2SQL API
    if check_port 8080; then
        local pid=$(get_port_pid 8080)
        echo "nl2sql_api       | FastAPI NL2SQL 服务     | 端口 8080 | ✅ 运行中 (PID: $pid)"
    else
        echo "nl2sql_api       | FastAPI NL2SQL 服务     | 端口 8080 | ❌ 未运行"
    fi

    echo "--------------------------------------------------------------------------------"
}

# 显示使用说明
show_usage() {
    echo "📖 使用说明:"
    echo "   ./start_nl2sql_inspector.sh start        - 启动所有服务"
    echo "   ./start_nl2sql_inspector.sh stop         - 停止所有服务"
    echo "   ./start_nl2sql_inspector.sh restart      - 重启所有服务"
    echo "   ./start_nl2sql_inspector.sh quick        - 快速重启 NL2SQL API 服务"
    echo "   ./start_nl2sql_inspector.sh status       - 查看服务状态"
    echo "   ./start_nl2sql_inspector.sh diagnostics  - 执行系统诊断"
    echo "   ./start_nl2sql_inspector.sh logs         - 显示日志文件"
    echo "   ./start_nl2sql_inspector.sh archive      - 归档当前日志"
    echo "   ./start_nl2sql_inspector.sh help         - 显示帮助"
    echo ""
    echo "🔗 服务访问地址:"
    echo "   NL2SQL API:      http://localhost:8080"
    echo "   NL2SQL 测试页面: http://localhost:8080/"
    echo "   PostgreSQL:      localhost:5433"
    echo "   LM Studio:       http://localhost:1234"
    echo ""
    echo "📋 日志文件位置:"
    echo "   NL2SQL 统一日志: $SCRIPT_DIR/logs/hsbc_fund_nl2sql.log"
    echo ""
}

# 快速重启 NL2SQL API 服务
quick_restart() {
    print_info "🔄 快速重启 NL2SQL API 服务..."

    # 归档日志
    print_info "归档日志..."
    archive_logs || true

    # 停止 API 服务
    if check_port 8080; then
        local pid=$(get_port_pid 8080)
        if [ -n "$pid" ]; then
            print_info "停止 NL2SQL API 服务 (PID: $pid)..."
            kill "$pid" 2>/dev/null
            sleep 2
            print_success "NL2SQL API 服务已停止"
        fi
    fi

    # 启动 NL2SQL API
    print_info "启动 NL2SQL API 服务..."
    cd "$SCRIPT_DIR"
    if [ -f "run_api.sh" ]; then
        nohup ./run_api.sh > "$TEMP_DIR/nl2sql_startup.log" 2>&1 &
        local api_pid=$!
        print_success "NL2SQL API 启动中 (PID: $api_pid)"

        # 等待服务启动
        print_info "等待服务启动..."
        for i in {1..30}; do
            if check_service_health "http://localhost:8080/health"; then
                print_success "NL2SQL API 服务启动成功"
                break
            fi
            sleep 1
        done
    else
        print_error "未找到 run_api.sh 启动脚本"
        return 1
    fi

    print_success "🎉 快速重启完成"
}

# 主函数
main() {
    case "${1:-help}" in
        "start")
            print_banner
            print_service_info
            print_info "归档日志..."
            archive_logs || true
            start_services
            echo ""
            show_status
            ;;
        "stop")
            print_banner
            stop_services
            echo ""
            show_status
            ;;
        "restart")
            print_banner
            print_service_info
            print_info "归档日志..."
            archive_logs || true
            stop_services
            echo ""
            start_services
            echo ""
            show_status
            ;;
        "quick")
            print_banner
            quick_restart
            echo ""
            show_status
            ;;
        "archive")
            print_banner
            print_info "手动归档日志..."
            archive_logs
            ;;
        "status")
            print_banner
            show_status
            ;;
        "diagnostics")
            print_banner
            run_diagnostics
            ;;
        "logs")
            print_banner
            show_logs
            ;;
        "help"|"-h"|"--help"|"")
            print_banner
            print_service_info
            show_usage
            show_status
            ;;
        *)
            print_error "未知命令: $1"
            show_usage
            exit 1
            ;;
    esac
}

# 信号处理
trap 'echo ""; print_info "用户中断，正在停止所有服务..."; stop_services; exit 0' INT

# 执行主函数
main "$@"
