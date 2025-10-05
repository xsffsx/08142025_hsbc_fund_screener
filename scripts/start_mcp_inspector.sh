#!/bin/bash
# 简化的MCP Inspector启动脚本 - 不需要proxy token

set -e

# 路径与日志目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOG_DIR="$SCRIPT_DIR/logs"
mkdir -p "$LOG_DIR" 2>/dev/null


# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# 检查端口是否被占用
check_port() {
    local port=$1
    lsof -i :$port >/dev/null 2>&1
    return $?
}

# 停止现有的MCP Inspector进程
stop_inspector() {
    print_info "停止现有的MCP Inspector进程..."
    pkill -f "mcp-inspector" 2>/dev/null || true
    pkill -f "@modelcontextprotocol/inspector" 2>/dev/null || true
    sleep 2
    print_success "已停止现有进程"
}

# 启动MCP Inspector (简化版本)
start_inspector() {
    print_info "启动MCP Inspector (简化版本)..."

    # 若传入 SERVER_URL，则用于Inspector默认链接
    local url_param=""
    if [ -n "${SERVER_URL:-}" ]; then
        url_param="&serverUrl=${SERVER_URL}"
        print_info "将预填 serverUrl: ${SERVER_URL}"
    fi

    # 创建临时配置文件
    local config_file="/tmp/mcp_config.json"
    cat > "$config_file" << 'EOF'
{
  "mcpServers": {
    "hsbc-nl2sql": {
      "command": "python",
      "args": ["-c", "print('MCP Server for HSBC NL2SQL')"],
      "env": {}
    }
  }
}
EOF

    # 启动MCP Inspector，指定配置文件
    print_info "使用配置文件: $config_file"

    # 在后台启动MCP Inspector
    # 禁用鉴权，避免 Proxy Token 要求
export DANGEROUSLY_OMIT_AUTH=true
# 显式设置默认端口（UI:6274, Proxy:6277）
export CLIENT_PORT=${CLIENT_PORT:-6274}
export SERVER_PORT=${SERVER_PORT:-6277}
nohup npx @modelcontextprotocol/inspector --config "$config_file" \
  > "$LOG_DIR/mcp_inspector.out.log" 2>&1 &
    local inspector_pid=$!

    print_info "MCP Inspector 启动中 (PID: $inspector_pid)..."

    # 等待服务启动
    local max_attempts=30
    for i in $(seq 1 $max_attempts); do
        # 先检测默认 UI 端口 6274，再兼容 5173
        if check_port 6274; then
            print_success "MCP Inspector 启动成功"
            print_info "访问地址: http://localhost:6274"
            # 预置更大的超时以避免复杂查询早停（10s -> 60s / 120s）
            local qs_timeout='&requestTimeout=60000&maximumTotalTimeout=120000'
            print_info "默认Transport=stream的链接: http://localhost:6274/?transport=streamable-http${url_param}${qs_timeout}"
            if command -v open >/dev/null 2>&1; then
                open "http://localhost:6274/?transport=streamable-http${url_param}${qs_timeout}" >/dev/null 2>&1 || true
            fi
            return 0
        fi
        if check_port 5173; then
            print_success "MCP Inspector 启动成功 (vite端口)"
            print_info "访问地址: http://localhost:5173"
            local qs_timeout='&requestTimeout=60000&maximumTotalTimeout=120000'
            print_info "默认Transport=stream的链接: http://localhost:5173/?transport=streamable-http${url_param}${qs_timeout}"
            if command -v open >/dev/null 2>&1; then
                open "http://localhost:5173/?transport=streamable-http${url_param}${qs_timeout}" >/dev/null 2>&1 || true
            fi
            return 0
        fi
        sleep 1
        echo -n "."
    done

    echo ""
    print_error "MCP Inspector 启动超时"
    print_info "查看日志: tail -f $LOG_DIR/mcp_inspector.out.log"
    return 1
}

# 显示状态
show_status() {
    echo "📊 MCP Inspector 状态:"
    echo "--------------------------------------------------------------------------------"

    if check_port 6274; then
        local pid=$(lsof -ti :6274 2>/dev/null)
        echo "MCP Inspector    | 调试界面           | 端口 6274 | ✅ 运行中 (PID: $pid)"
        echo "访问地址: http://localhost:6274"
    elif check_port 5173; then
        local pid=$(lsof -ti :5173 2>/dev/null)
        echo "MCP Inspector    | 调试界面(vite)     | 端口 5173 | ✅ 运行中 (PID: $pid)"
        echo "访问地址: http://localhost:5173"
    else
        echo "MCP Inspector    | 调试界面           | 端口 6274/5173 | ❌ 未运行"
    fi

    echo "--------------------------------------------------------------------------------"
}

# 主函数
main() {
    case "${1:-help}" in
        "start")
            echo "🚀 启动 MCP Inspector (简化版本)"
            echo "================================================================================"
            stop_inspector
            start_inspector
            echo ""
            show_status
            ;;
        "stop")
            echo "🛑 停止 MCP Inspector"
            echo "================================================================================"
            stop_inspector
            show_status
            ;;
        "restart")
            echo "🔄 重启 MCP Inspector"
            echo "================================================================================"
            stop_inspector
            start_inspector
            echo ""
            show_status
            ;;
        "status")
            echo "📊 MCP Inspector 状态"
            echo "================================================================================"
            show_status
            ;;
        "logs")
            echo "📋 MCP Inspector 日志"
            echo "================================================================================"
            if [ -f "/tmp/mcp_inspector.log" ]; then
                tail -20 /tmp/mcp_inspector.log
            else
                print_warning "日志文件不存在"
            fi
            ;;
        "help"|"-h"|"--help"|"")
            echo "📖 MCP Inspector 管理工具"
            echo "================================================================================"
            echo "使用说明:"
            echo "   ./start_mcp_inspector.sh start    - 启动 MCP Inspector"
            echo "   ./start_mcp_inspector.sh stop     - 停止 MCP Inspector"
            echo "   ./start_mcp_inspector.sh restart  - 重启 MCP Inspector"
            echo "   ./start_mcp_inspector.sh status   - 查看状态"
            echo "   ./start_mcp_inspector.sh logs     - 查看日志"
            echo ""
            show_status
            ;;
        *)
            print_error "未知命令: $1"
            echo "使用 './start_mcp_inspector.sh help' 查看帮助"
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
