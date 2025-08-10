#!/bin/bash

# =============================================================================
# NL2SQL API 测试脚本
# 功能: 快速测试语义增强功能和API响应
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

# API基础URL
API_BASE="http://localhost:8065"

# 检查服务状态
check_service() {
    log_info "检查NL2SQL服务状态..."
    
    if curl -f "$API_BASE/nl2sql.html" > /dev/null 2>&1; then
        log_success "NL2SQL服务运行正常"
        return 0
    else
        log_error "NL2SQL服务未运行或无响应"
        log_info "请先启动服务: ./script/start_all_service.sh start"
        return 1
    fi
}

# 测试单个查询
test_query() {
    local query="$1"
    local description="$2"
    
    echo ""
    log_info "=== 测试: $description ==="
    log_info "查询: $query"
    
    # URL编码查询
    local encoded_query=$(python3 -c "import urllib.parse; print(urllib.parse.quote('$query'))")
    local url="$API_BASE/nl2sql/nl2sql?query=$encoded_query"
    
    log_info "请求URL: $url"
    
    # 发送请求并获取响应
    local response=$(curl -s "$url")
    local curl_exit_code=$?
    
    if [ $curl_exit_code -eq 0 ] && [ -n "$response" ]; then
        log_success "API响应成功"
        echo "生成的SQL:"
        echo "----------------------------------------"
        echo "$response"
        echo "----------------------------------------"
        
        # 检查是否包含语义映射的特征
        if echo "$response" | grep -q "B_UT_PROD"; then
            log_success "✅ 语义映射: 表名映射正常 (funds → B_UT_PROD)"
        fi
        
        if echo "$response" | grep -q "CCY_PROD_TRADE_CDE"; then
            log_success "✅ 语义映射: 字段名映射正常 (currency → CCY_PROD_TRADE_CDE)"
        fi
        
        return 0
    else
        log_error "API请求失败 (退出码: $curl_exit_code)"
        if [ -n "$response" ]; then
            echo "错误响应: $response"
        fi
        return 1
    fi
}

# 预定义测试用例
run_test_cases() {
    log_info "=== 运行预定义测试用例 ==="
    
    # 测试用例数组
    local test_cases=(
        "查询港币基金|测试货币和基金类型查询"
        "查找美元产品|测试美元货币查询"
        "显示所有基金信息|测试基础查询"
        "查询高风险基金|测试风险等级查询"
        "找出收益率大于5%的产品|测试数值条件查询"
    )
    
    local success_count=0
    local total_count=${#test_cases[@]}
    
    for test_case in "${test_cases[@]}"; do
        IFS='|' read -r query description <<< "$test_case"
        
        if test_query "$query" "$description"; then
            ((success_count++))
        fi
        
        sleep 1  # 避免请求过于频繁
    done
    
    echo ""
    log_info "=== 测试结果汇总 ==="
    log_info "成功: $success_count/$total_count"
    
    if [ $success_count -eq $total_count ]; then
        log_success "所有测试用例通过！"
        return 0
    else
        log_warning "部分测试用例失败"
        return 1
    fi
}

# 交互式测试
interactive_test() {
    log_info "=== 交互式测试模式 ==="
    log_info "输入自然语言查询，输入 'quit' 退出"
    
    while true; do
        echo ""
        read -p "请输入查询 > " query
        
        if [ "$query" = "quit" ] || [ "$query" = "exit" ] || [ "$query" = "q" ]; then
            log_info "退出交互式测试"
            break
        fi
        
        if [ -z "$query" ]; then
            log_warning "查询不能为空"
            continue
        fi
        
        test_query "$query" "用户自定义查询"
    done
}

# 性能测试
performance_test() {
    log_info "=== 性能测试 ==="
    
    local test_query="查询港币基金"
    local test_count=5
    local total_time=0
    
    log_info "执行 $test_count 次查询测试..."
    
    for i in $(seq 1 $test_count); do
        log_info "第 $i 次测试..."
        
        local start_time=$(date +%s.%N)
        test_query "$test_query" "性能测试 #$i" > /dev/null
        local end_time=$(date +%s.%N)
        
        local duration=$(echo "$end_time - $start_time" | bc)
        total_time=$(echo "$total_time + $duration" | bc)
        
        log_info "响应时间: ${duration}s"
    done
    
    local avg_time=$(echo "scale=3; $total_time / $test_count" | bc)
    log_info "平均响应时间: ${avg_time}s"
}

# 显示帮助信息
show_help() {
    echo "NL2SQL API 测试脚本"
    echo ""
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  test     - 运行预定义测试用例"
    echo "  interactive - 交互式测试模式"
    echo "  performance - 性能测试"
    echo "  check    - 检查服务状态"
    echo "  help     - 显示帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 test                    # 运行所有测试用例"
    echo "  $0 interactive             # 进入交互式测试"
    echo "  $0 performance             # 运行性能测试"
}

# 主函数
main() {
    echo "=== NL2SQL API 测试脚本 ==="
    echo ""
    
    case "${1:-test}" in
        "test")
            check_service || exit 1
            run_test_cases
            ;;
        "interactive")
            check_service || exit 1
            interactive_test
            ;;
        "performance")
            check_service || exit 1
            performance_test
            ;;
        "check")
            check_service
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            log_error "未知选项: $1"
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
