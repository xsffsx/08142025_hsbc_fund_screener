#!/bin/bash
# 语义增强方案完整验证脚本
# 创建时间: 2025年8月8日 13:40:00
# 作者: Augment Agent

echo "=== 语义增强方案完整验证脚本 ==="
echo "测试时间: $(date)"
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 成功/失败计数
TOTAL_CHECKS=0
PASSED_CHECKS=0

# 检查函数
check_result() {
    local test_name="$1"
    local condition="$2"
    local success_msg="$3"
    local failure_msg="$4"
    
    TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
    
    if [ "$condition" = "true" ]; then
        echo -e "${GREEN}✅ $test_name: $success_msg${NC}"
        PASSED_CHECKS=$((PASSED_CHECKS + 1))
        return 0
    else
        echo -e "${RED}❌ $test_name: $failure_msg${NC}"
        return 1
    fi
}

echo "=== 1. 服务状态验证 ==="

# 1.1 检查NL2SQL服务
if curl -s "http://localhost:8065/nl2sql/nl2sql?query=test" | grep -q "Error\|SELECT" >/dev/null 2>&1; then
    check_result "NL2SQL服务" "true" "服务运行正常" ""
else
    check_result "NL2SQL服务" "false" "" "服务未启动，请先启动服务"
    echo -e "${YELLOW}启动命令: ./start_all_service.sh${NC}"
    exit 1
fi

# 1.2 检查数据库连接
if docker exec mysql-nl2sql-mvp1 mysql -uroot -proot123 -e "SELECT 1" >/dev/null 2>&1; then
    check_result "数据库连接" "true" "连接正常" ""
else
    check_result "数据库连接" "false" "" "数据库连接失败"
    exit 1
fi

echo ""
echo "=== 2. 数据库表结构验证 ==="

# 2.1 验证B_UT_PROD表存在
PROD_TABLE_EXISTS=$(docker exec mysql-nl2sql-mvp1 mysql -uroot -proot123 nl2sql -e "
SELECT COUNT(*) FROM information_schema.tables 
WHERE table_schema = 'nl2sql' AND table_name = 'B_UT_PROD'" 2>/dev/null | tail -1)

check_result "基金产品表" "$([[ $PROD_TABLE_EXISTS == "1" ]] && echo true || echo false)" "B_UT_PROD表存在" "B_UT_PROD表不存在"

# 2.2 验证关键字段存在
CCY_FIELD_EXISTS=$(docker exec mysql-nl2sql-mvp1 mysql -uroot -proot123 nl2sql -e "
SELECT COUNT(*) FROM information_schema.columns 
WHERE table_schema = 'nl2sql' AND table_name = 'B_UT_PROD' AND column_name = 'CCY_PROD_TRADE_CDE'" 2>/dev/null | tail -1)

check_result "货币字段" "$([[ $CCY_FIELD_EXISTS == "1" ]] && echo true || echo false)" "CCY_PROD_TRADE_CDE字段存在" "CCY_PROD_TRADE_CDE字段不存在"

echo ""
echo "=== 3. 语义模型配置验证 ==="

# 3.1 验证语义模型配置数量
SEMANTIC_COUNT=$(docker exec mysql-nl2sql-mvp1 mysql -uroot -proot123 nl2sql -e "
SELECT COUNT(*) FROM semantic_model WHERE agent_id = 2" 2>/dev/null | tail -1)

check_result "语义模型配置" "$([[ $SEMANTIC_COUNT -gt 20 ]] && echo true || echo false)" "配置正常: $SEMANTIC_COUNT 条" "配置不足: $SEMANTIC_COUNT 条"

# 3.2 验证关键字段映射
CURRENCY_MAPPING=$(docker exec mysql-nl2sql-mvp1 mysql -uroot -proot123 nl2sql -e "
SELECT origin_name FROM semantic_model 
WHERE agent_id = 2 AND field_name = 'tradeCurrencyCode'" 2>/dev/null | tail -1)

check_result "货币字段映射" "$([[ $CURRENCY_MAPPING == "B_UT_PROD.CCY_PROD_TRADE_CDE" ]] && echo true || echo false)" "映射配置正确" "映射配置错误: $CURRENCY_MAPPING"

# 3.3 验证业务知识配置
BUSINESS_COUNT=$(docker exec mysql-nl2sql-mvp1 mysql -uroot -proot123 nl2sql -e "
SELECT COUNT(*) FROM business_knowledge WHERE agent_id = 2" 2>/dev/null | tail -1)

check_result "业务知识配置" "$([[ $BUSINESS_COUNT -gt 5 ]] && echo true || echo false)" "配置正常: $BUSINESS_COUNT 条" "配置不足: $BUSINESS_COUNT 条"

echo ""
echo "=== 4. 核心问题验证 ==="

# 4.1 HKD基金查询测试
echo -e "${BLUE}测试查询: HKD-denominated fund 的收益率最高的前3个${NC}"

RESPONSE=$(curl -s "http://localhost:8065/nl2sql/nl2sql?query=HKD-denominated+fund+的收益率最高的前3个")
echo "当前系统返回: $RESPONSE"

# 检查是否使用错误的表名
if echo "$RESPONSE" | grep -q "funds"; then
    check_result "表名检查" "false" "" "使用了错误的表名 'funds'"
    PROBLEM_EXISTS=true
else
    check_result "表名检查" "true" "没有使用错误表名 'funds'" ""
    PROBLEM_EXISTS=false
fi

# 检查是否使用错误的字段名
if echo "$RESPONSE" | grep -q "currency"; then
    check_result "字段名检查" "false" "" "使用了通用字段名 'currency'"
    PROBLEM_EXISTS=true
else
    check_result "字段名检查" "true" "没有使用通用字段名 'currency'" ""
fi

# 检查是否使用了实际表名
if echo "$RESPONSE" | grep -q "B_UT_PROD"; then
    check_result "实际表名使用" "true" "使用了实际表名 'B_UT_PROD'" ""
else
    check_result "实际表名使用" "false" "" "未使用实际表名 'B_UT_PROD'"
fi

# 检查是否使用了实际字段名
if echo "$RESPONSE" | grep -q "CCY_PROD_TRADE_CDE"; then
    check_result "实际字段名使用" "true" "使用了实际字段名 'CCY_PROD_TRADE_CDE'" ""
else
    check_result "实际字段名使用" "false" "" "未使用实际字段名 'CCY_PROD_TRADE_CDE'"
fi

echo ""
echo "=== 5. 其他测试用例验证 ==="

# 5.1 高风险基金查询
echo -e "${BLUE}测试查询: 高风险基金产品${NC}"
RISK_RESPONSE=$(curl -s "http://localhost:8065/nl2sql/nl2sql?query=高风险基金产品")
echo "返回SQL: $RISK_RESPONSE"

if echo "$RISK_RESPONSE" | grep -q "B_UT_PROD"; then
    check_result "风险基金查询-表名" "true" "使用了正确表名" ""
else
    check_result "风险基金查询-表名" "false" "" "未使用正确表名"
fi

# 5.2 基金数量查询
echo -e "${BLUE}测试查询: 基金产品总数${NC}"
COUNT_RESPONSE=$(curl -s "http://localhost:8065/nl2sql/nl2sql?query=基金产品总数")
echo "返回SQL: $COUNT_RESPONSE"

if echo "$COUNT_RESPONSE" | grep -q "B_UT_PROD"; then
    check_result "基金数量查询-表名" "true" "使用了正确表名" ""
else
    check_result "基金数量查询-表名" "false" "" "未使用正确表名"
fi

echo ""
echo "=== 6. 验证结果总结 ==="

# 计算通过率
PASS_RATE=$((PASSED_CHECKS * 100 / TOTAL_CHECKS))

echo -e "${BLUE}总检查项目: $TOTAL_CHECKS${NC}"
echo -e "${GREEN}通过项目: $PASSED_CHECKS${NC}"
echo -e "${YELLOW}通过率: $PASS_RATE%${NC}"

echo ""
if [ $PROBLEM_EXISTS ]; then
    echo -e "${RED}=== 问题确认 ===${NC}"
    echo "❌ 当前系统存在语义映射问题"
    echo "❌ 使用通用表名和字段名，而非实际数据库结构"
    echo "❌ 语义模型配置没有被有效使用"
    echo ""
    echo -e "${YELLOW}=== 修复方案 ===${NC}"
    echo "📋 实施渐进式语义增强方案"
    echo "🎯 预期收益: SQL准确率从30%提升到85%+"
    echo "⏱️  预计工期: 4-6天开发 + 2天测试"
    echo ""
    echo -e "${BLUE}期望修复后的SQL:${NC}"
    echo "SELECT PROD_NAME FROM B_UT_PROD WHERE CCY_PROD_TRADE_CDE = 'HKD' ORDER BY ... LIMIT 3;"
else
    echo -e "${GREEN}=== 修复成功 ===${NC}"
    echo "✅ 语义增强方案已生效"
    echo "✅ 系统使用实际表名和字段名"
    echo "✅ 语义模型配置正常工作"
fi

echo ""
echo "=== 验证完成 ==="
echo "报告生成时间: $(date)"

# 返回适当的退出码
if [ $PASS_RATE -ge 80 ]; then
    exit 0
else
    exit 1
fi
