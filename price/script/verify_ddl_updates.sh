#!/bin/bash

# SGH基金问题验证系统 - DDL更新验证脚本
# 创建时间: 2025-08-07 08:30:00
# 目的: 通过curl等方式验证DDL更新是否反映在Price和Product系统中

echo "=========================================="
echo "SGH基金问题验证系统 - DDL更新验证"
echo "=========================================="

# 配置变量
PRICE_BASE_URL="http://localhost:9099"  # Price Fund App
PRODUCT_GRAPHQL_URL="http://localhost:8080/graphql"  # Product GraphQL App
PRODUCT_UTILS_URL="http://localhost:8081"  # Product Utils App

# 通用请求头
HEADERS=(
    -H "Content-Type: application/json"
    -H "Accept: application/json"
    -H "channelId: OHI"
    -H "countryCode: HK"
    -H "groupMember: HBAP"
)

echo "开始验证DDL更新..."
echo ""

# ==========================================
# 1. 验证Price系统健康状态和数据库连接
# ==========================================
echo "1. 验证Price系统状态"
echo "----------------------------------------"

echo "1.1 检查Price系统健康状态..."
curl -s "${HEADERS[@]}" \
    "$PRICE_BASE_URL/actuator/health" \
    | jq '.' 2>/dev/null || echo "Price系统健康检查失败或返回非JSON格式"

echo ""
echo "1.2 检查Price系统基础接口..."
curl -s "${HEADERS[@]}" \
    "$PRICE_BASE_URL/greeting" \
    2>/dev/null || echo "Price系统基础接口检查失败"

echo ""
echo "1.3 检查Price系统健康检查仪表板..."
curl -s "${HEADERS[@]}" \
    "$PRICE_BASE_URL/healthcheck/dashboard" \
    2>/dev/null | head -20 || echo "Price系统健康检查仪表板访问失败"

# ==========================================
# 2. 验证Price系统UT基金数据查询
# ==========================================
echo ""
echo "2. 验证Price系统UT基金数据"
echo "----------------------------------------"

echo "2.1 测试基金报价摘要查询..."
FUND_QUOTE_SUMMARY_PARAM='{"productType":"UT","prodAltNum":"U50002","prodCdeAltClassCde":"M","market":"HK"}'
curl -s "${HEADERS[@]}" \
    "$PRICE_BASE_URL/wmds/fundQuoteSummary?body=$(echo $FUND_QUOTE_SUMMARY_PARAM | jq -c .)" \
    | jq '.' 2>/dev/null || echo "基金报价摘要查询失败"

echo ""
echo "2.2 测试基金详细信息查询..."
FUND_DETAIL_PARAM='{"market":"HK","productType":"UT","prodCdeAltClassCde":"M","prodAltNum":"U50003","delay":true}'
curl -s "${HEADERS[@]}" \
    "$PRICE_BASE_URL/wmds/quoteDetail?body=$(echo $FUND_DETAIL_PARAM | jq -c .)" \
    | jq '.' 2>/dev/null || echo "基金详细信息查询失败"

echo ""
echo "2.3 测试基金搜索结果查询..."
FUND_SEARCH_PARAM='{"productType":"UT","returnOnlyNumberOfMatches":false,"sortBy":"name","sortOrder":"asc","startDetail":"1","endDetail":"10","numberOfRecords":"14","channelRestrictCode":"SI_I"}'
curl -s "${HEADERS[@]}" \
    "$PRICE_BASE_URL/wmds/fundSearchResult?body=$(echo $FUND_SEARCH_PARAM | jq -c .)" \
    | jq '.results[0] | keys' 2>/dev/null || echo "基金搜索结果查询失败"

# ==========================================
# 3. 验证Product系统GraphQL接口
# ==========================================
echo ""
echo "3. 验证Product系统GraphQL接口"
echo "----------------------------------------"

echo "3.1 测试Product GraphQL健康状态..."
curl -s "${HEADERS[@]}" \
    "$PRODUCT_GRAPHQL_URL/../actuator/health" \
    | jq '.' 2>/dev/null || echo "Product GraphQL健康检查失败"

echo ""
echo "3.2 测试TB_PROD表数据查询..."
PRODUCT_QUERY='{
  "query": "query productByFilter($filter: JSON!) { productByFilter(filter: $filter) { prodId prodName prodTypeCde riskLvlCde prodStatCde allowBuyUtProdInd allowSellUtProdInd prdRtrnAvgNum rtrnVoltlAvgPct yieldEnhnProdInd asetClassCde geoRiskInd prodTopPerfmRankNum prodTopYieldRankNum } }",
  "variables": {
    "filter": {
      "prodTypeCde": "UT",
      "prodStatCde": "A"
    }
  }
}'

curl -s "${HEADERS[@]}" \
    -d "$PRODUCT_QUERY" \
    "$PRODUCT_GRAPHQL_URL" \
    | jq '.data.productByFilter[0] | keys' 2>/dev/null || echo "TB_PROD表查询失败"

echo ""
echo "3.3 测试ESG数据查询..."
ESG_QUERY='{
  "query": "query esgDataByProdIdList($prodIdList: [Long!]!) { esgDataByProdIdList(prodIdList: $prodIdList) { id prodId isin productClassification classificationCode securityName year quarter } }",
  "variables": {
    "prodIdList": [1001, 1002, 1003]
  }
}'

curl -s "${HEADERS[@]}" \
    -d "$ESG_QUERY" \
    "$PRODUCT_GRAPHQL_URL" \
    | jq '.data.esgDataByProdIdList[0] | keys' 2>/dev/null || echo "ESG数据查询失败"

# ==========================================
# 4. 验证关键字段存在性
# ==========================================
echo ""
echo "4. 验证关键字段存在性"
echo "----------------------------------------"

echo "4.1 验证Price系统V_UT_PROD_INSTM视图字段..."
# 通过实际查询验证字段是否存在
FIELD_TEST_QUERY='{"productType":"UT","prodAltNum":"U50002","prodCdeAltClassCde":"M","market":"HK"}'
RESPONSE=$(curl -s "${HEADERS[@]}" \
    "$PRICE_BASE_URL/wmds/fundQuoteSummary?body=$(echo $FIELD_TEST_QUERY | jq -c .)" 2>/dev/null)

if echo "$RESPONSE" | jq -e '.summary.prodName' >/dev/null 2>&1; then
    echo "✅ prodName字段存在"
else
    echo "❌ prodName字段不存在或查询失败"
fi

if echo "$RESPONSE" | jq -e '.summary.return1yr' >/dev/null 2>&1; then
    echo "✅ return1yr字段存在"
else
    echo "❌ return1yr字段不存在或查询失败"
fi

echo ""
echo "4.2 验证Product系统TB_PROD表字段..."
PROD_FIELD_QUERY='{
  "query": "query { productByFilter(filter: {\"prodTypeCde\": \"UT\"}, limit: 1) { prodId prodName prodTypeCde riskLvlCde allowBuyUtProdInd allowSellUtProdInd prdRtrnAvgNum rtrnVoltlAvgPct yieldEnhnProdInd asetClassCde geoRiskInd } }"
}'

PROD_RESPONSE=$(curl -s "${HEADERS[@]}" \
    -d "$PROD_FIELD_QUERY" \
    "$PRODUCT_GRAPHQL_URL" 2>/dev/null)

if echo "$PROD_RESPONSE" | jq -e '.data.productByFilter[0].prodName' >/dev/null 2>&1; then
    echo "✅ PROD_NAME字段存在"
else
    echo "❌ PROD_NAME字段不存在或查询失败"
fi

if echo "$PROD_RESPONSE" | jq -e '.data.productByFilter[0].riskLvlCde' >/dev/null 2>&1; then
    echo "✅ RISK_LVL_CDE字段存在"
else
    echo "❌ RISK_LVL_CDE字段不存在或查询失败"
fi

# ==========================================
# 5. 数据库连接测试
# ==========================================
echo ""
echo "5. 数据库连接测试"
echo "----------------------------------------"

echo "5.1 测试Price系统数据库连接..."
# 通过健康检查接口测试数据库连接
curl -s "${HEADERS[@]}" \
    "$PRICE_BASE_URL/healthcheck/probing" \
    2>/dev/null | grep -q "success" && echo "✅ Price系统数据库连接正常" || echo "❌ Price系统数据库连接异常"

echo ""
echo "5.2 测试Product系统数据库连接..."
# 通过GraphQL查询测试数据库连接
DB_TEST_QUERY='{"query": "query { productByFilter(filter: {}, limit: 1) { prodId } }"}'
curl -s "${HEADERS[@]}" \
    -d "$DB_TEST_QUERY" \
    "$PRODUCT_GRAPHQL_URL" \
    | jq -e '.data.productByFilter[0].prodId' >/dev/null 2>&1 && echo "✅ Product系统数据库连接正常" || echo "❌ Product系统数据库连接异常"

# ==========================================
# 6. 生成验证报告
# ==========================================
echo ""
echo "6. 验证报告生成"
echo "----------------------------------------"

TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')
REPORT_FILE="ddl_verification_report_$(date '+%Y%m%d_%H%M%S').json"

cat > "$REPORT_FILE" << EOF
{
  "verificationReport": {
    "timestamp": "$TIMESTAMP",
    "systems": {
      "priceSystem": {
        "baseUrl": "$PRICE_BASE_URL",
        "healthCheck": "$(curl -s $PRICE_BASE_URL/actuator/health 2>/dev/null | jq -r '.status // "UNKNOWN"')",
        "keyFields": {
          "prodName": "$(echo "$RESPONSE" | jq -e '.summary.prodName' >/dev/null 2>&1 && echo "EXISTS" || echo "MISSING")",
          "return1yr": "$(echo "$RESPONSE" | jq -e '.summary.return1yr' >/dev/null 2>&1 && echo "EXISTS" || echo "MISSING")"
        }
      },
      "productSystem": {
        "graphqlUrl": "$PRODUCT_GRAPHQL_URL",
        "healthCheck": "$(curl -s ${PRODUCT_GRAPHQL_URL}/../actuator/health 2>/dev/null | jq -r '.status // "UNKNOWN"')",
        "keyFields": {
          "prodName": "$(echo "$PROD_RESPONSE" | jq -e '.data.productByFilter[0].prodName' >/dev/null 2>&1 && echo "EXISTS" || echo "MISSING")",
          "riskLvlCde": "$(echo "$PROD_RESPONSE" | jq -e '.data.productByFilter[0].riskLvlCde' >/dev/null 2>&1 && echo "EXISTS" || echo "MISSING")"
        }
      }
    },
    "summary": {
      "totalTests": 12,
      "passedTests": "$(echo "待统计")",
      "failedTests": "$(echo "待统计")",
      "recommendations": [
        "检查系统服务是否正常运行",
        "验证数据库连接配置",
        "确认DDL更新是否已部署",
        "检查字段映射是否正确"
      ]
    }
  }
}
EOF

echo "验证报告已生成: $REPORT_FILE"
echo ""
echo "=========================================="
echo "DDL更新验证完成"
echo "=========================================="
