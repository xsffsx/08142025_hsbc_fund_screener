# HSBC Fund Screener 前端字段与API数据映射分析报告

生成时间: 2025-09-06 13:14:07

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

**API请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`

**请求方法**: POST

## 相关文件链接

### API数据文件
- 📤 [API请求数据](01_summary/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](01_summary/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](01_summary/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](01_summary/result/fund_screener_result_20250906_095633.png) - 基金筛选结果页面截图
- 📄 [页面源码](01_summary/result/fund_screener_page_20250906_095633.html) - 完整的HTML源码
- 📋 [页面信息](01_summary/result/page_info_20250906_095633.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](01_summary/mapping/frontend_fields_20250906_131422.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](01_summary/mapping/frontend_fields_20250906_131422.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金筛选器的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Screener Interface](01_summary/result/fund_screener_result_20250906_095633.png)

*图：HSBC基金筛选器界面 - 显示了基金列表的各个字段*

## 字段映射详情

### 1. Fund (基金名称)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund | header.name | 基金完整名称，包含基金公司和份额类别 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | BlackRock World Gold Fund (Class A2) | `BlackRock World Gold Fund (Class A2)` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.name` | 从响应数据中获取字段值的路径 |
| **备注** | 基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息 | | |

### 2. NAV (净资产值)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | NAV | summary.dayEndNAV | 净资产值，显示当日收盘价格 |
| **数据类型** | formatted number + currency | number | 需要组合数值和货币代码 |
| **示例对比** | 71.76 USD | `73.28` + `"USD"` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `summary.dayEndNAV`, `summary.dayEndNAVCurrencyCode` | 从响应数据中获取字段值的路径 |
| **备注** | NAV是基金每份额的净资产价值，投资者买卖基金的参考价格，通常每日更新 | | |
| **货币处理** | 数值 + 货币代码 | 分别从数值字段和货币字段获取 | 前端需要将数值和货币代码组合显示 |

### 3. YTD return (年初至今回报)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | YTD return | performance.calendarReturns.returnYTD | 从年初到当前日期的投资回报率 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 85.33% | `89.2562` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.calendarReturns.returnYTD` | 从响应数据中获取字段值的路径 |
| **备注** | YTD回报反映基金在当前年度的表现，是投资者评估短期业绩的重要指标 | | |

### 4. 1Y return (1年回报)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 1Y return | performance.annualizedReturns.return1Yr | 过去12个月的年化投资回报率 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 76.36% | `76.91936` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.annualizedReturns.return1Yr` | 从响应数据中获取字段值的路径 |
| **备注** | 1年回报是评估基金中期表现的关键指标，剔除了短期市场波动的影响 | | |

### 5. Fund class currency (基金类别货币)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund class currency | header.currency | 基金份额的计价货币 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | USD | `USD` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.currency` | 从响应数据中获取字段值的路径 |
| **备注** | 基金计价货币影响汇率风险，投资者需要考虑货币兑换成本 | | |

### 6. 1 year sharpe ratio (1年夏普比率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 1 year sharpe ratio | risk[year=1].yearRisk.sharpeRatio | 1年期夏普比率，衡量风险调整后收益 |
| **数据类型** | number | number | 直接映射，无需转换 |
| **示例对比** | 1.635 | `1.635` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `risk[year=1].yearRisk.sharpeRatio` | 从响应数据中获取字段值的路径 |
| **备注** | 夏普比率越高表示单位风险获得的超额回报越多，是重要的风险调整收益指标 | | |

### 7. Fund size (USD/Million) (基金规模(美元/百万))

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund size (USD/Million) | summary.assetsUnderManagement | 基金管理的总资产规模 |
| **数据类型** | formatted number + currency | number_millions | 需要组合数值和货币代码 |
| **示例对比** | 6911.59 (Million USD) | `6911586496` + `"USD"` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `summary.assetsUnderManagement`, `summary.assetsUnderManagementCurrencyCode` | 从响应数据中获取字段值的路径 |
| **备注** | 基金规模影响流动性和管理效率，过大或过小都可能影响投资表现 | | |
| **货币处理** | 数值 + 货币代码 | 分别从数值字段和货币字段获取 | 前端需要将数值和货币代码组合显示 |
| **计算公式** | 显示值 = 原始值 / 1000000 | 原始数值 | 需要进行数学运算转换 |

### 8. HSBC risk level (汇丰风险等级)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | HSBC risk level | summary.riskLvlCde | 汇丰银行内部风险评级 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | 5 | `5` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `summary.riskLvlCde` | 从响应数据中获取字段值的路径 |
| **备注** | 汇丰风险等级帮助投资者了解产品的风险特征，数值越高风险越大 | | |

### 9. Morningstar rating (晨星评级)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Morningstar rating | rating.morningstarRating | 晨星公司的基金评级 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | 3 (stars) | `3` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `rating.morningstarRating` | 从响应数据中获取字段值的路径 |
| **备注** | 晨星评级是业界权威的基金评价体系，星级越高表示历史表现越好 | | |

## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 |
|---------|------------|----------|--------|
| **Fund** | `header.name` | string | BlackRock World Gold Fund (Class A2) |
| **NAV** | `summary.dayEndNAV` | number | 71.76 USD |
| **YTD return** | `performance.calendarReturns.returnYTD` | percentage | 85.33% |
| **1Y return** | `performance.annualizedReturns.return1Yr` | percentage | 76.36% |
| **Fund class currency** | `header.currency` | string | USD |
| **1 year sharpe ratio** | `risk[year=1].yearRisk.sharpeRatio` | number | 1.635 |
| **Fund size (USD/Million)** | `summary.assetsUnderManagement` | number_millions | 6911.59 (Million USD) |
| **HSBC risk level** | `summary.riskLvlCde` | string | 5 |
| **Morningstar rating** | `rating.morningstarRating` | string | 3 (stars) |

## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐
│ Fund                │  ←──  │ header.name                      │
│ NAV                 │  ←──  │ summary.dayEndNAV +              │
│                     │       │ summary.dayEndNAVCurrencyCode   │
│ YTD return          │  ←──  │ performance.calendarReturns.     │
│                     │       │ returnYTD                        │
│ 1Y return           │  ←──  │ performance.annualizedReturns.   │
│                     │       │ return1Yr                        │
│ Fund class currency │  ←──  │ header.currency                  │
│ 1 year sharpe ratio │  ←──  │ risk[year=1].yearRisk.sharpeRatio│
│ Fund size           │  ←──  │ summary.assetsUnderManagement    │
│ HSBC risk level     │  ←──  │ summary.riskLvlCde               │
│ Morningstar rating  │  ←──  │ rating.morningstarRating         │
└─────────────────────┘       └──────────────────────────────────┘
```

## 数据转换说明

### 百分比字段
- YTD return 和 1Y return 字段在API中以小数形式存储（如 85.33058），前端显示时需要添加 % 符号

### 货币字段
- NAV 和 Fund size 字段都有对应的货币代码字段
- Fund size 需要从原始值除以1,000,000转换为百万单位

### 评级字段
- Morningstar rating 在API中以字符串形式存储（如 "3"），前端可能显示为星级图标

### 风险数据
- 1年夏普比率需要从 risk 数组中查找 year=1 的数据

## 使用建议

1. 前端渲染时应根据数据类型进行相应的格式化
2. 百分比字段需要添加 % 符号
3. 货币字段需要显示货币代码
4. 大数值字段（如基金规模）需要转换为合适的单位
5. 评级字段可以考虑使用图标或星级显示

