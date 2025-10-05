#!/bin/bash
# HSBC Fund NL2SQL 服务停止脚本

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
    echo "🛑 HSBC Fund NL2SQL 服务停止脚本"
    echo "================================================================================"
    echo "📅 停止时间: $(date '+%Y-%m-%d %H:%M:%S')"
    echo ""
}

print_banner

# 停止 NL2SQL API 服务
print_info "停止 NL2SQL API 服务..."
if pkill -f 'uvicorn.*hsbc_fund_api_server' >/dev/null 2>&1; then
    print_success "NL2SQL API 服务已停止"
else
    print_warning "NL2SQL API 服务未运行或已停止"
fi

# 停止 MCP Inspector (如果存在)
print_info "停止 MCP Inspector..."
if [ -f "/Users/paulo/IdeaProjects/20250707_MCP/text-to-graphql-mcp/start_02_inspector.sh" ]; then
    cd /Users/paulo/IdeaProjects/20250707_MCP/text-to-graphql-mcp
    ./start_02_inspector.sh stop >/dev/null 2>&1
    print_success "MCP Inspector 已停止"
    cd - >/dev/null
else
    print_warning "MCP Inspector 脚本未找到"
fi

# 可选：停止 PostgreSQL 容器
read -p "是否停止 PostgreSQL 容器？ (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    print_info "停止 PostgreSQL 容器..."
    cd ../docker/postgres
    if docker-compose down; then
        print_success "PostgreSQL 容器已停止"
    else
        print_error "PostgreSQL 容器停止失败"
    fi
    cd ../../scripts
else
    print_info "保持 PostgreSQL 容器运行"
fi

echo ""
print_success "🎉 服务停止完成！"
echo "================================================================================"
echo ""
echo "📊 停止的服务:"
echo "   • NL2SQL API 服务 (端口 8080)"
echo "   • MCP Inspector (端口 6274)"
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "   • PostgreSQL 容器 (端口 5433)"
fi
echo ""
echo "💡 重新启动服务:"
echo "   ./setup_nl2sql.sh"
echo ""
echo "================================================================================"
