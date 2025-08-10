#!/bin/bash

# =============================================================================
# 外部服务统一管理脚本
# 功能: 启动/重启/验证 PostgreSQL, Qdrant, LLM, Embedding 服务
# 作者: Augment Agent
# 日期: $(date +%Y%m%d)
# =============================================================================

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

# 服务配置
POSTGRESQL_CONTAINER="postgresql-nl2sql-mvp1"
QDRANT_CONTAINER="qdrant-nl2sql-mvp1"
LLM_SERVICE_URL="http://localhost:8089"  # proxy_openai 服务端口
LMSTUDIO_SERVICE_URL="http://localhost:1234"  # LM Studio 直接端口（用于 embedding）
QDRANT_SERVICE_URL="http://localhost:6333"
POSTGRESQL_SERVICE_URL="localhost:5432"

# Docker Compose 文件路径 - 支持从任何位置调用
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
DOCKER_COMPOSE_FILE="$PROJECT_ROOT/qdrant/docker-compose.yaml"

# 检查Docker是否运行
check_docker() {
    log_info "检查Docker服务状态..."
    if ! docker info >/dev/null 2>&1; then
        log_error "Docker未运行，请先启动Docker"
        exit 1
    fi
    log_success "Docker服务正常运行"
}

# 检查PostgreSQL服务
check_postgresql() {
    log_info "检查PostgreSQL服务状态..."

    # 检查容器状态
    if docker ps --format "table {{.Names}}\t{{.Status}}" | grep -q "$POSTGRESQL_CONTAINER.*Up"; then
        log_success "PostgreSQL容器运行正常 ($POSTGRESQL_CONTAINER)"
        return 0
    elif docker ps -a --format "table {{.Names}}\t{{.Status}}" | grep -q "$POSTGRESQL_CONTAINER"; then
        log_warning "PostgreSQL容器存在但未正常运行"
        return 1
    else
        log_warning "PostgreSQL容器不存在"
        return 1
    fi
}

# 检查Qdrant服务
check_qdrant() {
    log_info "检查Qdrant服务状态..."
    
    # 检查容器状态
    if docker ps --format "table {{.Names}}\t{{.Status}}" | grep -q "$QDRANT_CONTAINER.*Up"; then
        log_info "Qdrant容器正在运行，检查API连接..."
        
        # 检查API连接
        if curl -s "$QDRANT_SERVICE_URL/collections" >/dev/null 2>&1; then
            log_success "Qdrant服务运行正常 ($QDRANT_CONTAINER)"
            return 0
        else
            log_warning "Qdrant容器运行但API无响应"
            return 1
        fi
    else
        log_warning "Qdrant容器未运行"
        return 1
    fi
}

# 检查LLM服务 (proxy_openai)
check_llm() {
    log_info "检查 proxy_openai LLM服务状态..."

    # 检查 proxy_openai 健康状态
    local health_response=$(curl -s "$LLM_SERVICE_URL/health" 2>/dev/null)
    if [ $? -eq 0 ] && echo "$health_response" | grep -q '"status":"healthy"'; then
        local model=$(echo "$health_response" | jq -r '.lmstudio.model' 2>/dev/null)
        log_success "proxy_openai LLM服务运行正常，后端模型: $model"
        return 0
    else
        log_warning "proxy_openai LLM服务无响应 ($LLM_SERVICE_URL)"
        log_info "请确保 proxy_openai 服务已启动并连接到 LM Studio"
        return 1
    fi
}

# 检查Embedding服务 (LM Studio 直接)
check_embedding() {
    log_info "检查LM Studio Embedding服务状态..."

    # 检查LM Studio的embedding端点（直接连接，不通过proxy_openai）
    local test_response=$(curl -s "$LMSTUDIO_SERVICE_URL/v1/embeddings" \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer lm-studio" \
        -d '{"input":"test embedding","model":"text-embedding-nomic-embed-text-v1.5"}' 2>/dev/null)

    if [ $? -eq 0 ] && echo "$test_response" | grep -q "embedding"; then
        log_success "LM Studio Embedding服务运行正常"
        return 0
    else
        log_warning "LM Studio Embedding服务无响应或未加载embedding模型"
        log_info "请确保LM Studio已加载embedding模型 (如: text-embedding-nomic-embed-text-v1.5)"
        return 1
    fi
}

# 启动PostgreSQL和Qdrant (Docker Compose) - 智能启动
start_docker_services() {
    log_info "检查并启动Docker服务 (PostgreSQL + Qdrant)..."

    # 先检查服务状态
    local postgresql_ok=false
    local qdrant_ok=false

    if check_postgresql; then
        log_info "PostgreSQL服务已运行，跳过启动"
        postgresql_ok=true
    fi

    if check_qdrant; then
        log_info "Qdrant服务已运行，跳过启动"
        qdrant_ok=true
    fi

    # 如果都已运行，直接返回
    if [ "$postgresql_ok" = true ] && [ "$qdrant_ok" = true ]; then
        log_success "所有Docker服务已运行"
        return 0
    fi

    # 启动未运行的服务
    if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
        log_error "Docker Compose文件不存在: $DOCKER_COMPOSE_FILE"
        return 1
    fi

    log_info "启动未运行的Docker服务..."
    cd "$(dirname "$DOCKER_COMPOSE_FILE")"
    docker-compose up -d

    if [ $? -eq 0 ]; then
        log_success "Docker服务启动完成"

        # 等待服务就绪
        log_info "等待服务就绪..."
        sleep 10

        return 0
    else
        log_error "Docker服务启动失败"
        return 1
    fi
}

# 重启Docker服务
restart_docker_services() {
    log_info "重启Docker服务..."
    
    if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
        log_error "Docker Compose文件不存在: $DOCKER_COMPOSE_FILE"
        return 1
    fi
    
    cd "$(dirname "$DOCKER_COMPOSE_FILE")"
    docker-compose down
    sleep 3
    docker-compose up -d
    
    if [ $? -eq 0 ]; then
        log_success "Docker服务重启完成"
        
        # 等待服务就绪
        log_info "等待服务就绪..."
        sleep 15
        
        return 0
    else
        log_error "Docker服务重启失败"
        return 1
    fi
}

# 自动启动proxy_openai服务
start_proxy_openai() {
    local proxy_openai_dir="/Users/paulo/IdeaProjects/20250707_MCP/proxy_openai"

    if [ ! -d "$proxy_openai_dir" ]; then
        log_error "proxy_openai目录不存在: $proxy_openai_dir"
        return 1
    fi

    log_info "尝试启动proxy_openai服务..."
    cd "$proxy_openai_dir"

    # 检查是否有python环境
    if ! command -v python >/dev/null 2>&1; then
        log_error "未找到python命令"
        return 1
    fi

    # 检查run.py是否存在
    if [ ! -f "run.py" ]; then
        log_error "未找到run.py文件"
        return 1
    fi

    # 后台启动proxy_openai
    log_info "后台启动proxy_openai服务..."
    nohup python run.py > /tmp/proxy_openai.log 2>&1 &
    local proxy_pid=$!

    # 等待服务启动
    log_info "等待proxy_openai服务启动..."
    sleep 5

    # 验证服务是否启动成功
    if check_llm; then
        log_success "proxy_openai服务启动成功 (PID: $proxy_pid)"
        return 0
    else
        log_error "proxy_openai服务启动失败，请检查日志: /tmp/proxy_openai.log"
        return 1
    fi
}

# 启动LLM服务 - 智能检查 (proxy_openai + LM Studio)
start_llm_service() {
    log_info "检查 AI 服务链路状态..."

    # 检查 proxy_openai 服务是否已经运行
    if check_llm; then
        log_success "proxy_openai LLM服务已运行，跳过启动"

        # 检查Embedding服务（直接连接LM Studio）
        if check_embedding; then
            log_success "LM Studio Embedding服务已运行，跳过启动"
            return 0
        else
            log_warning "LM Studio Embedding服务未运行，请检查embedding模型加载"
            log_info "请确保LM Studio已加载embedding模型 (如: text-embedding-nomic-embed-text-v1.5)"
            return 1
        fi
    fi

    # 尝试自动启动proxy_openai
    log_info "尝试自动启动proxy_openai服务..."
    if start_proxy_openai; then
        # 再次检查embedding服务
        if check_embedding; then
            log_success "所有AI服务启动成功"
            return 0
        else
            log_warning "LM Studio Embedding服务仍未运行"
            log_info "请确保LM Studio已加载embedding模型 (如: text-embedding-nomic-embed-text-v1.5)"
            return 1
        fi
    fi

    log_warning "proxy_openai 或 LM Studio 服务未运行，请手动启动"
    log_info "启动步骤:"
    log_info "1. 启动 LM Studio 应用并加载模型:"
    log_info "   - LLM模型: qwen/qwen3-30b-a3b-2507"
    log_info "   - Embedding模型: text-embedding-nomic-embed-text-v1.5"
    log_info "   - 在Local Server页面启动服务器 (端口1234)"
    log_info "2. 启动 proxy_openai 服务:"
    log_info "   - cd /Users/paulo/IdeaProjects/20250707_MCP/proxy_openai"
    log_info "   - python run.py"
    return 1
}

# 验证所有服务
verify_all_services() {
    log_info "=== 验证所有外部服务状态 ==="
    
    local all_ok=true
    
    # 检查PostgreSQL
    if ! check_postgresql; then
        all_ok=false
    fi
    
    # 检查Qdrant
    if ! check_qdrant; then
        all_ok=false
    fi
    
    # 检查LLM
    if ! check_llm; then
        all_ok=false
    fi
    
    # 检查Embedding
    if ! check_embedding; then
        all_ok=false
    fi
    
    echo ""
    if [ "$all_ok" = true ]; then
        log_success "=== 所有外部服务运行正常 ==="
        return 0
    else
        log_error "=== 部分服务存在问题，请检查上述输出 ==="
        return 1
    fi
}

# 显示服务状态详情
show_service_details() {
    log_info "=== 服务详细状态 ==="
    
    echo ""
    log_info "Docker容器状态:"
    docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "(postgresql|qdrant)"

    echo ""
    log_info "服务端点测试:"
    echo "PostgreSQL: $POSTGRESQL_SERVICE_URL"
    echo "Qdrant: $QDRANT_SERVICE_URL"
    echo "proxy_openai: $LLM_SERVICE_URL"
    echo "LM Studio: $LMSTUDIO_SERVICE_URL"

    echo ""
    log_info "Qdrant集合信息:"
    curl -s "$QDRANT_SERVICE_URL/collections" | jq . 2>/dev/null || echo "无法获取Qdrant集合信息"

    echo ""
    log_info "proxy_openai 服务信息:"
    curl -s "$LLM_SERVICE_URL/health" | jq . 2>/dev/null || echo "无法获取 proxy_openai 服务信息"

    echo ""
    log_info "LM Studio模型信息:"
    curl -s "$LMSTUDIO_SERVICE_URL/v1/models" | jq '.data[] | {id: .id, object: .object}' 2>/dev/null || echo "无法获取LM Studio模型信息"
}

# 主函数
main() {
    echo "=== 外部服务管理脚本 ==="
    echo "支持的操作: start, restart, verify, status"
    echo ""
    
    # 检查Docker
    check_docker
    
    case "${1:-verify}" in
        "start")
            log_info "智能启动外部服务 (跳过已运行的服务)..."
            start_docker_services
            start_llm_service
            sleep 5
            verify_all_services
            ;;
        "restart")
            log_info "重启所有外部服务..."
            restart_docker_services
            start_llm_service
            sleep 5
            verify_all_services
            ;;
        "verify")
            verify_all_services
            ;;
        "status")
            verify_all_services
            show_service_details
            ;;
        *)
            echo "用法: $0 {start|restart|verify|status}"
            echo ""
            echo "  start   - 启动所有外部服务"
            echo "  restart - 重启所有外部服务"
            echo "  verify  - 验证所有服务状态"
            echo "  status  - 显示详细服务状态"
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
