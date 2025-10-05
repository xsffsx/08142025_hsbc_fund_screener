#!/bin/bash

# SGH基金问题验证系统 - DDL更新验证演示脚本
# 创建时间: 2025-08-07 08:30:00
# 目的: 演示如何通过curl等方式验证DDL更新是否反映在系统中

echo "=========================================="
echo "SGH基金问题验证系统 - DDL更新验证演示"
echo "=========================================="

# 配置变量 (实际环境中需要修改为真实的服务地址)
PRICE_BASE_URL="https://your-price-service.domain.com"
PRODUCT_GRAPHQL_URL="https://your-product-service.domain.com/graphql"
PRODUCT_UTILS_URL="https://your-product-utils.domain.com"

echo "注意: 这是一个演示脚本，展示验证DDL更新的方法"
echo "实际使用时请修改服务地址为真实环境"
echo ""

# ==========================================
# 1. 验证Price系统健康状态和数据库连接
# ==========================================
echo "1. 验证Price系统状态"
echo "----------------------------------------"

echo "1.1 检查Price系统健康状态..."
echo "命令示例:"
echo "curl -H 'Content-Type: application/json' \\"
echo "     -H 'channelId: OHI' \\"
echo "     -H 'countryCode: HK' \\"
echo "     -H 'groupMember: HBAP' \\"
echo "     '$PRICE_BASE_URL/actuator/health'"
echo ""
echo "预期响应: {\"status\":\"UP\",\"components\":{...}}"
echo ""

echo "1.2 检查Price系统基础接口..."
echo "命令示例:"
echo "curl '$PRICE_BASE_URL/greeting'"
echo ""
echo "预期响应: 'Hello, This is wmds-fund-app!'"
echo ""

# ==========================================
# 2. 验证Price系统UT基金数据查询
# ==========================================
echo "2. 验证Price系统UT基金数据"
echo "----------------------------------------"

echo "2.1 测试基金报价摘要查询..."
echo "命令示例:"
cat << 'EOF'
curl -H 'Content-Type: application/json' \
     -H 'channelId: OHI' \
     -H 'countryCode: HK' \
     -H 'groupMember: HBAP' \
     "$PRICE_BASE_URL/wmds/fundQuoteSummary?body={\"productType\":\"UT\",\"prodAltNum\":\"U50002\",\"prodCdeAltClassCde\":\"M\",\"market\":\"HK\"}"
EOF
echo ""
echo "预期响应包含字段:"
echo "- summary.prodName (产品名称)"
echo "- summary.riskLvlCde (风险等级)"
echo "- summary.return1yr (一年收益率)"
echo "- summary.allowBuyUtProdInd (允许买入指标)"
echo "- summary.allowSellUtProdInd (允许卖出指标)"
echo ""

echo "2.2 测试基金搜索结果查询..."
echo "命令示例:"
cat << 'EOF'
curl -H 'Content-Type: application/json' \
     -H 'channelId: OHI' \
     -H 'countryCode: HK' \
     -H 'groupMember: HBAP' \
     "$PRICE_BASE_URL/wmds/fundSearchResult?body={\"productType\":\"UT\",\"returnOnlyNumberOfMatches\":false,\"sortBy\":\"name\",\"sortOrder\":\"asc\",\"startDetail\":\"1\",\"endDetail\":\"10\"}"
EOF
echo ""
echo "预期响应包含:"
echo "- results[] 数组"
echo "- 每个结果包含新增的DDL字段"
echo ""

# ==========================================
# 3. 验证Product系统GraphQL接口
# ==========================================
echo "3. 验证Product系统GraphQL接口"
echo "----------------------------------------"

echo "3.1 测试TB_PROD表数据查询..."
echo "命令示例:"
cat << 'EOF'
curl -H 'Content-Type: application/json' \
     -H 'channelId: OHI' \
     -H 'countryCode: HK' \
     -H 'groupMember: HBAP' \
     -d '{
       "query": "query productByFilter($filter: JSON!) { productByFilter(filter: $filter) { prodId prodName prodTypeCde riskLvlCde prodStatCde allowBuyUtProdInd allowSellUtProdInd prdRtrnAvgNum rtrnVoltlAvgPct yieldEnhnProdInd asetClassCde geoRiskInd prodTopPerfmRankNum prodTopYieldRankNum } }",
       "variables": {
         "filter": {
           "prodTypeCde": "UT",
           "prodStatCde": "A"
         }
       }
     }' \
     "$PRODUCT_GRAPHQL_URL"
EOF
echo ""
echo "预期响应包含新增字段:"
echo "- allowBuyUtProdInd (允许买入UT产品指标)"
echo "- allowSellUtProdInd (允许卖出UT产品指标)"
echo "- prdRtrnAvgNum (产品收益率平均数)"
echo "- rtrnVoltlAvgPct (收益率波动性平均百分比)"
echo "- yieldEnhnProdInd (收益增强产品指标)"
echo "- geoRiskInd (地理风险指标)"
echo "- prodTopPerfmRankNum (产品顶级表现排名数)"
echo "- prodTopYieldRankNum (产品顶级收益排名数)"
echo ""

# ==========================================
# 4. 验证关键字段存在性
# ==========================================
echo "4. 验证关键字段存在性"
echo "----------------------------------------"

echo "4.1 验证Price系统V_UT_PROD_INSTM视图字段..."
echo "通过实际查询验证字段是否存在:"
echo ""
echo "检查字段列表:"
echo "✅ 应该存在: prodName, riskLvlCde, allowBuyUtProdInd, allowSellUtProdInd"
echo "✅ 应该存在: prdRtrnAvgNum, rtrnVoltlAvgPct, yieldEnhnProdInd"
echo "✅ 应该存在: asetClassCde, geoRiskInd, prodTopPerfmRankNum"
echo ""

echo "4.2 验证Product系统TB_PROD表字段..."
echo "通过GraphQL schema查询验证字段:"
cat << 'EOF'
curl -H 'Content-Type: application/json' \
     -d '{
       "query": "query { __type(name: \"ProductType\") { fields { name type { name } } } }"
     }' \
     "$PRODUCT_GRAPHQL_URL"
EOF
echo ""

# ==========================================
# 5. 数据库连接测试
# ==========================================
echo "5. 数据库连接测试"
echo "----------------------------------------"

echo "5.1 测试Price系统数据库连接..."
echo "命令示例:"
echo "curl '$PRICE_BASE_URL/healthcheck/probing'"
echo ""
echo "预期响应包含: 'success' 或 'OK'"
echo ""

echo "5.2 测试Product系统数据库连接..."
echo "命令示例:"
cat << 'EOF'
curl -H 'Content-Type: application/json' \
     -d '{"query": "query { productByFilter(filter: {}, limit: 1) { prodId } }"}' \
     "$PRODUCT_GRAPHQL_URL"
EOF
echo ""
echo "预期响应: 返回至少一个产品ID"
echo ""

# ==========================================
# 6. 直接数据库验证 (如果有数据库访问权限)
# ==========================================
echo "6. 直接数据库验证 (可选)"
echo "----------------------------------------"

echo "6.1 Oracle数据库验证命令:"
echo "sqlplus username/password@database << EOF"
echo "-- 检查TB_PROD表新增字段"
echo "SELECT COLUMN_NAME, DATA_TYPE, NULLABLE"
echo "FROM USER_TAB_COLUMNS"
echo "WHERE TABLE_NAME = 'TB_PROD'"
echo "AND COLUMN_NAME IN ("
echo "  'ALLOW_BUY_UT_PROD_IND',"
echo "  'ALLOW_SELL_UT_PROD_IND',"
echo "  'PRD_RTRN_AVG_NUM',"
echo "  'RTRN_VOLTL_AVG_PCT',"
echo "  'YIELD_ENHN_PROD_IND',"
echo "  'GEO_RISK_IND',"
echo "  'PROD_TOP_PERFM_RANK_NUM',"
echo "  'PROD_TOP_YIELD_RANK_NUM'"
echo ");"
echo ""
echo "-- 检查V_UT_PROD_INSTM视图"
echo "SELECT COUNT(*) FROM V_UT_PROD_INSTM WHERE ROWNUM <= 10;"
echo "EOF"
echo ""

# ==========================================
# 7. 验证结果分析
# ==========================================
echo "7. 验证结果分析"
echo "----------------------------------------"

echo "7.1 成功指标:"
echo "✅ 所有API调用返回HTTP 200状态码"
echo "✅ 响应JSON包含预期的新增字段"
echo "✅ 字段值不为null且符合业务逻辑"
echo "✅ 数据库查询返回正确的字段结构"
echo ""

echo "7.2 失败指标:"
echo "❌ API调用返回4xx或5xx错误"
echo "❌ 响应中缺少新增字段"
echo "❌ 字段值全部为null或异常"
echo "❌ 数据库查询报错或字段不存在"
echo ""

echo "7.3 故障排查步骤:"
echo "1. 检查服务是否正常运行"
echo "2. 验证数据库连接配置"
echo "3. 确认DDL脚本是否已执行"
echo "4. 检查应用程序是否已重启"
echo "5. 验证缓存是否已清理"
echo "6. 检查字段映射配置"
echo ""

# ==========================================
# 8. 自动化验证脚本示例
# ==========================================
echo "8. 自动化验证脚本示例"
echo "----------------------------------------"

echo "8.1 创建验证函数:"
cat << 'EOF'
verify_field_exists() {
    local api_url="$1"
    local field_name="$2"
    local response=$(curl -s "$api_url")
    
    if echo "$response" | jq -e ".$field_name" > /dev/null 2>&1; then
        echo "✅ $field_name 字段存在"
        return 0
    else
        echo "❌ $field_name 字段不存在"
        return 1
    fi
}

# 使用示例
verify_field_exists "$PRICE_BASE_URL/wmds/fundQuoteSummary?body=..." "summary.allowBuyUtProdInd"
EOF
echo ""

echo "8.2 批量验证脚本:"
cat << 'EOF'
#!/bin/bash
FIELDS=("allowBuyUtProdInd" "allowSellUtProdInd" "prdRtrnAvgNum" "rtrnVoltlAvgPct")
PASSED=0
TOTAL=${#FIELDS[@]}

for field in "${FIELDS[@]}"; do
    if verify_field_exists "$API_URL" "$field"; then
        ((PASSED++))
    fi
done

echo "验证结果: $PASSED/$TOTAL 字段通过验证"
if [ $PASSED -eq $TOTAL ]; then
    echo "✅ DDL更新验证成功"
    exit 0
else
    echo "❌ DDL更新验证失败"
    exit 1
fi
EOF
echo ""

echo "=========================================="
echo "DDL更新验证演示完成"
echo ""
echo "实际使用步骤:"
echo "1. 修改脚本中的服务地址为真实环境"
echo "2. 配置正确的认证信息和请求头"
echo "3. 根据实际API接口调整请求参数"
echo "4. 运行验证脚本并分析结果"
echo "5. 根据验证结果进行问题排查"
echo "=========================================="
