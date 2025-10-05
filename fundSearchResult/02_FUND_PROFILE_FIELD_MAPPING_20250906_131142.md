# HSBC Fund Screener 前端字段与API数据映射分析报告

生成时间: 2025-09-06 13:11:42

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

**API请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`

**请求方法**: POST

## 相关文件链接

### API数据文件
- 📤 [API请求数据](02_fund_profile/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](02_fund_profile/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](02_fund_profile/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](02_fund_profile/result/fund_screener_result_20250906_102907.png) - 基金 Fund Profile 页面截图
- 📄 [页面源码](02_fund_profile/result/fund_screener_page_20250906_102907.html) - 完整的HTML源码
- 📋 [页面信息](02_fund_profile/result/page_info_20250906_102907.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](02_fund_profile/mapping/frontend_fields_20250906_131204.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](02_fund_profile/mapping/frontend_fields_20250906_131204.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金 Fund Profile 页面的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Profile Interface](02_fund_profile/result/fund_screener_result_20250906_102907.png)

*图：HSBC基金 Fund Profile 界面 - 显示了基金详情的各个字段*

## 字段映射详情

### 1. Fund (基金名称)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund | header.name | 基金完整名称，包含基金公司和份额类别 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | BlackRock World Gold Fund (Class A2) | `MRF-HSBC Jintrust Dynamic Strategy Mixed Sec Inv FD (Class H DIST CASH)` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.name` | 从响应数据中获取字段值的路径 |
| **备注** | 基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息 | | |

### 2. ISIN code (国际证券识别编码)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | ISIN code | header.prodAltNumSeg[prodCdeAltClassCde=I].prodAltNum | 国际证券识别编码，全球唯一的基金标识符 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | LU0055631609 | `CNE100002425` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.prodAltNumSeg[prodCdeAltClassCde=I].prodAltNum` | 从响应数据中获取字段值的路径 |
| **备注** | ISIN代码是国际标准的证券识别码，用于全球范围内唯一识别金融工具 | | |

### 3. Fund house (基金管理公司)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund house | header.familyName | 基金管理公司名称 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | BlackRock | `HSBC Jintrust Fund Management Company Limited` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.familyName` | 从响应数据中获取字段值的路径 |
| **备注** | 基金管理公司的声誉和管理能力是投资者选择基金的重要考虑因素 | | |

### 4. Fund class inception date (基金份额成立日期)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund class inception date | profile.inceptionDate | 基金份额类别的成立日期 |
| **数据类型** | date | date | 直接映射，无需转换 |
| **示例对比** | 30 Dec 1994 | `2016-06-27` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `profile.inceptionDate` | 从响应数据中获取字段值的路径 |
| **备注** | 成立时间影响基金的历史业绩数据可用性，较新的基金可能缺乏长期表现记录 | | |

### 5. HSBC investment category (汇丰投资类别)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | HSBC investment category | header.categoryName | 汇丰银行的投资类别分类 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | Commodity Funds | `Mutual Recognition of Funds - Equity/Mixed Asset` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.categoryName` | 从响应数据中获取字段值的路径 |
| **备注** | 汇丰的分类体系帮助投资者了解基金的投资策略和风险特征 | | |

### 6. Target dividend distribution frequency (目标分红频率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Target dividend distribution frequency | profile.distributionFrequency | 目标分红频率 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | Monthly | `Monthly` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `profile.distributionFrequency` | 从响应数据中获取字段值的路径 |
| **备注** | 分红频率影响现金流规划，适合不同收入需求的投资者 | | |

### 7. Dividend yield (股息收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Dividend yield | profile.distributionYield | 股息收益率，年化分红与净值的比率 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 0.00% | `0` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `profile.distributionYield` | 从响应数据中获取字段值的路径 |
| **备注** | 股息收益率是评估基金收入能力的重要指标，特别适合追求稳定收入的投资者 | | |

## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 |
|---------|------------|----------|--------|
| **Fund** | `header.name` | string | BlackRock World Gold Fund (Class A2) |
| **ISIN code** | `header.prodAltNumSeg[prodCdeAltClassCde=I].prodAltNum` | string | LU0055631609 |
| **Fund house** | `header.familyName` | string | BlackRock |
| **Fund class inception date** | `profile.inceptionDate` | date | 30 Dec 1994 |
| **HSBC investment category** | `header.categoryName` | string | Commodity Funds |
| **Target dividend distribution frequency** | `profile.distributionFrequency` | string | Monthly |
| **Dividend yield** | `profile.distributionYield` | percentage | 0.00% |

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

