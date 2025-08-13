# 最终版本：HSBC基金详情页面网络请求捕获实现

创建时间：2025-08-13 19:51:00

## 🎯 最终实现效果

### ✅ 完美过滤结果
经过精确过滤，现在只捕获**8个核心HSBC基金API**，完全符合要求：

```
📊 最终统计:
   📤 总请求数: 8个
   📥 总响应数: 8个
   📁 API分组数: 8个
   🌐 涉及域名: 1个 (investments3.personal-banking.hsbc.com.hk)
   🔧 请求方法: GET
   📊 响应状态码: 200
```

### 🚫 成功过滤掉的内容
1. **静态资源**: JS、CSS、图片、字体、HTML文件
2. **第三方服务**: Google Analytics、Facebook像素、Optimizely、LivePerson等
3. **广告追踪**: DoubleClick、Bing广告、Yahoo分析等
4. **特定API**: `/amh/ut/sdkToken` 等不需要的API
5. **主页面请求**: fundDetail页面本身的HTML请求

## 📁 最终目录结构

```
data_20250813_195004/
├── 20250813_195004_U43051_01/
│   ├── amh_ut_product.request.json          # 产品信息API
│   ├── amh_ut_product.response.json
│   ├── wmds_quoteDetail.request.json        # 报价详情API
│   ├── wmds_quoteDetail.response.json
│   ├── wmds_fundQuoteSummary.request.json   # 基金摘要API
│   ├── wmds_fundQuoteSummary.response.json
│   ├── wmds_holdingAllocation.request.json  # 持仓分配API
│   ├── wmds_holdingAllocation.response.json
│   ├── wmds_topTenHoldings.request.json     # 前十大持仓API
│   ├── wmds_topTenHoldings.response.json
│   ├── wmds_otherFundClasses.request.json   # 其他基金类别API
│   ├── wmds_otherFundClasses.response.json
│   ├── wmds_fundSearchCriteria.request.json # 基金搜索条件API
│   ├── wmds_fundSearchCriteria.response.json
│   ├── wmds_advanceChart.request.json       # 高级图表API
│   ├── wmds_advanceChart.response.json
│   └── summary.json                         # 汇总信息
└── fund_detail_U43051_20250813_195004.png   # 页面截图
```

## 🔍 捕获的8个核心HSBC API

### 1. **产品信息API** (`amh_ut_product`)
```
/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product
```
- **功能**: 获取基金产品的基本信息
- **包含**: 风险等级、投资货币、最低投资金额、基金公司代码等

### 2. **报价详情API** (`wmds_quoteDetail`)
```
/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/quoteDetail
```
- **功能**: 获取基金的实时报价信息
- **包含**: 当前价格、价格变动、交易时间等

### 3. **基金摘要API** (`wmds_fundQuoteSummary`)
```
/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundQuoteSummary
```
- **功能**: 获取基金的摘要信息
- **包含**: 基金概览、关键指标等

### 4. **持仓分配API** (`wmds_holdingAllocation`)
```
/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/holdingAllocation
```
- **功能**: 获取基金的资产配置信息
- **包含**: 按地区、行业、资产类别的分配比例

### 5. **前十大持仓API** (`wmds_topTenHoldings`)
```
/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/topTenHoldings
```
- **功能**: 获取基金的前十大持仓股票
- **包含**: 持仓股票名称、比例、行业等

### 6. **其他基金类别API** (`wmds_otherFundClasses`)
```
/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/otherFundClasses
```
- **功能**: 获取同一基金的其他类别信息
- **包含**: 不同货币类别、不同费用结构的基金版本

### 7. **基金搜索条件API** (`wmds_fundSearchCriteria`)
```
/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchCriteria
```
- **功能**: 获取基金搜索的可用条件
- **包含**: 可用的筛选条件、分类标准等

### 8. **高级图表API** (`wmds_advanceChart`)
```
/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/advanceChart
```
- **功能**: 获取基金的历史价格图表数据
- **包含**: 3年历史价格数据、净值变化等

## 🔧 核心技术特性

### 智能过滤机制
```python
# 只保留HSBC核心API
hsbc_api_patterns = [
    'investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-',
    'quotespeed.morningstar.com/ra/'  # 晨星数据API（已在此次过滤中排除）
]
```

### 精确排除列表
- 主页面HTML请求
- 所有第三方追踪服务
- 广告和分析服务
- 客服和存储服务
- SDK令牌API

## 📊 数据质量

### 完整性
- ✅ 每个API都有完整的请求和响应数据
- ✅ 保留所有重要的HTTP头信息
- ✅ 包含完整的响应体数据
- ✅ 记录精确的时间戳

### 结构化
- ✅ 每个API独立的文件
- ✅ 统一的JSON格式
- ✅ 清晰的命名规范
- ✅ 完整的元数据

### 可用性
- ✅ 便于API逆向工程
- ✅ 支持业务流程分析
- ✅ 适合系统集成开发
- ✅ 方便数据挖掘分析

## 🚀 使用方法

```bash
# 基本使用
python scripts/02_capture_hsbc_fund_detail_request.py U43051

# 指定其他产品代码
python scripts/02_capture_hsbc_fund_detail_request.py U42401

# 可视化模式（调试用）
python scripts/02_capture_hsbc_fund_detail_request.py U43051 false
```

## 🎯 应用价值

这个最终版本的脚本提供了：

1. **纯净的API数据**: 只包含核心业务API，无噪音干扰
2. **完整的业务流程**: 8个API覆盖了基金详情页面的完整数据流
3. **高质量数据**: 每个API都有完整的请求/响应信息
4. **易于分析**: 结构化的文件组织，便于后续处理

这是进行HSBC基金系统分析、API逆向工程和业务集成开发的理想数据源！
