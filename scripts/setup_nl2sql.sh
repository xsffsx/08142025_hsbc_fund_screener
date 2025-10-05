#!/bin/bash
# HSBC Fund NL2SQL 完整启动脚本 - 包含服务启动和 MCP Inspector

set -e

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
    echo "🚀 HSBC Fund NL2SQL 完整启动脚本"
    echo "================================================================================"
    echo "📅 启动时间: $(date '+%Y-%m-%d %H:%M:%S')"
    echo "📁 项目目录: $(pwd)"
    echo ""
}

print_banner

# 检查端口是否被占用
check_port() {
    local port=$1
    lsof -i :$port >/dev/null 2>&1
    return $?
}

# 等待服务启动
wait_for_service() {
    local url=$1
    local service_name=$2
    local max_attempts=30

    print_info "等待 $service_name 启动..."
    for i in $(seq 1 $max_attempts); do
        if curl -s --max-time 5 "$url" >/dev/null 2>&1; then
            print_success "$service_name 启动成功"
            return 0
        fi
        sleep 2
        echo -n "."
    done
    echo ""
    print_error "$service_name 启动失败或超时"
    return 1
}

# 检查 Python 版本
print_info "检查 Python 版本..."
python_version=$(python3 --version 2>&1 | awk '{print $2}' | cut -d. -f1,2)
required_version="3.8"

if [ "$(printf '%s\n' "$required_version" "$python_version" | sort -V | head -n1)" != "$required_version" ]; then
    print_error "Python 版本过低，需要 Python 3.8+，当前版本: $python_version"
    exit 1
fi
print_success "Python 版本检查通过: $python_version"

# 检查 PostgreSQL 容器状态
print_info "检查 PostgreSQL 容器状态..."
if ! docker ps | grep -q "postgresql-hsbc-fund-screener"; then
    print_warning "PostgreSQL 容器未运行，正在启动..."
    cd ../docker/postgres
    docker-compose up -d
    print_info "等待数据库启动..."
    sleep 10
    cd ../../scripts
else
    print_success "PostgreSQL 容器正在运行"
fi

# 测试数据库连接
print_info "测试数据库连接..."
if docker exec postgresql-hsbc-fund-screener pg_isready -U hsbc_user -d hsbc_fund > /dev/null 2>&1; then
    print_success "数据库连接正常"
else
    print_error "数据库连接失败"
    exit 1
fi

# 检查数据是否存在
print_info "检查基金数据..."
fund_count=$(docker exec -i postgresql-hsbc-fund-screener psql -U hsbc_user -d hsbc_fund -t -c "SELECT COUNT(*) FROM hsbc_fund_unified;" 2>/dev/null | tr -d ' ')
if [ "$fund_count" -gt 0 ]; then
    print_success "发现 $fund_count 个基金产品"
else
    print_warning "数据库中没有基金数据，请先运行数据加载脚本"
    echo "   python load_all_product.py"
fi

# 检查虚拟环境
if [ ! -d "venv_api" ]; then
    print_error "虚拟环境 venv_api 不存在"
    print_info "请先运行: python3 -m venv venv_api"
    exit 1
fi

# 检查依赖文件
if [ ! -f "requirements_hsbc_fund.txt" ]; then
    print_error "依赖文件 requirements_hsbc_fund.txt 不存在"
    exit 1
fi

# 激活虚拟环境并检查依赖
print_info "检查 Python 依赖..."
source venv_api/bin/activate

# 检查关键依赖
required_packages=("fastapi" "langchain" "psycopg")
for pkg in "${required_packages[@]}"; do
    if python -c "import $pkg" 2>/dev/null; then
        print_success "依赖包 $pkg 已安装"
    else
        print_warning "依赖包 $pkg 未安装，正在安装..."
        pip install -r requirements_hsbc_fund.txt
        break
    fi
done

# 检查 LM Studio 服务
print_info "检查 LM Studio 服务..."
if curl -s --max-time 5 "http://localhost:1234/v1/models" >/dev/null 2>&1; then
    print_success "LM Studio API 服务正常"

    # 获取可用模型
    models=$(curl -s http://localhost:1234/v1/models | jq -r '.data[].id' 2>/dev/null | head -3)
    if [ -n "$models" ]; then
        print_info "可用模型:"
        echo "$models" | while read model; do
            echo "    - $model"
        done
    fi
else
    print_warning "LM Studio API 服务不可用"
    print_info "请确保 LM Studio 已启动并开启 OpenAI 兼容 API 服务"
    print_info "访问地址: http://localhost:1234"
fi

# 启动 NL2SQL API 服务
print_info "启动 NL2SQL API 服务..."

# 停止已存在的服务
pkill -f 'uvicorn.*hsbc_fund_api_server' >/dev/null 2>&1 || true

# 启动 API 服务
if [ -f "hsbc_fund_api_server.py" ]; then
    export AGENT_TIMEOUT=60
    nohup python -m uvicorn hsbc_fund_api_server:app --host 0.0.0.0 --port 8080 --log-level info > logs/api_startup.log 2>&1 &
    API_PID=$!

    # 等待服务启动
    if wait_for_service "http://localhost:8080/health" "NL2SQL API"; then
        print_success "NL2SQL API 服务启动成功 (PID: $API_PID)"
    else
        print_error "NL2SQL API 服务启动失败"
        print_info "查看日志: tail -f logs/api_startup.log"
        exit 1
    fi
else
    print_error "未找到 hsbc_fund_api_server.py 文件"
    exit 1
fi

# 启动 MCP Inspector (如果存在)
print_info "检查 MCP Inspector..."
if [ -f "/Users/paulo/IdeaProjects/20250707_MCP/text-to-graphql-mcp/start_02_inspector.sh" ]; then
    print_info "启动 MCP Inspector..."
    cd /Users/paulo/IdeaProjects/20250707_MCP/text-to-graphql-mcp

    # 启动 Inspector (快速模式)
    nohup ./start_02_inspector.sh start > /tmp/mcp_inspector_startup.log 2>&1 &
    INSPECTOR_PID=$!

    # 等待 Inspector 启动
    sleep 5
    if wait_for_service "http://localhost:6274" "MCP Inspector"; then
        print_success "MCP Inspector 启动成功 (PID: $INSPECTOR_PID)"
    else
        print_warning "MCP Inspector 启动失败或超时"
        print_info "查看日志: tail -f /tmp/mcp_inspector_startup.log"
    fi

    # 返回原目录
    cd - >/dev/null
else
    print_warning "MCP Inspector 脚本未找到，跳过启动"
fi

# 创建日志目录
mkdir -p logs

echo ""
print_success "🎉 HSBC Fund NL2SQL 系统启动完成！"
echo "================================================================================"
echo ""
echo "🌐 服务访问地址 (Endpoints):"
echo "--------------------------------------------------------------------------------"
echo "📊 NL2SQL API 服务:"
echo "   • 主页面:          http://localhost:8080"
echo "   • 测试页面:        http://localhost:8080/"
echo "   • API 文档:        http://localhost:8080/docs"
echo "   • 健康检查:        http://localhost:8080/health"
echo "   • 同步查询:        POST http://localhost:8080/query"
echo "   • 流式查询:        GET  http://localhost:8080/stream"
echo ""
echo "🔍 MCP Inspector (如果启动):"
echo "   • Inspector 界面:  http://localhost:6274"
echo "   • 代理服务器:      localhost:6277"
echo ""
echo "🗄️  数据库服务:"
echo "   • PostgreSQL:      localhost:5433"
echo "   • 数据库名:        hsbc_fund"
echo "   • 用户名:          hsbc_user"
echo "   • 基金数据量:      $fund_count 条记录"
echo ""
echo "🤖 LLM 服务:"
echo "   • LM Studio API:   http://localhost:1234"
echo "   • OpenAI 兼容:     http://localhost:1234/v1"
echo ""
echo "📋 管理工具:"
echo "   • Inspector 工具:  ./start_nl2sql_inspector.sh"
echo "   • 服务状态:        ./start_nl2sql_inspector.sh status"
echo "   • 查看日志:        ./start_nl2sql_inspector.sh logs"
echo "   • 系统诊断:        ./start_nl2sql_inspector.sh diagnostics"
echo ""
echo "� 日志文件位置:"
echo "   • API 日志:        ./logs/hsbc_fund_api.log"
echo "   • 核心日志:        ./logs/hsbc_fund_core.log"
echo "   • 启动日志:        ./logs/api_startup.log"
echo ""
echo "🧪 测试命令:"
echo "   # 测试同步查询"
echo "   curl -X POST http://localhost:8080/query \\"
echo "        -H \"Content-Type: application/json\" \\"
echo "        -d '{\"query\": \"数据库中有多少个基金？\"}'"
echo ""
echo "   # 测试流式查询"
echo "   curl -N http://localhost:8080/stream?query=数据库中有多少个基金？"
echo ""
echo "💡 使用提示:"
echo "   1. 访问 http://localhost:8080 进行交互式测试"
echo "   2. 使用 ./start_nl2sql_inspector.sh 管理服务"
echo "   3. 查看日志文件排查问题"
echo "   4. 确保 LM Studio 已加载合适的模型"
echo ""
echo "================================================================================"
