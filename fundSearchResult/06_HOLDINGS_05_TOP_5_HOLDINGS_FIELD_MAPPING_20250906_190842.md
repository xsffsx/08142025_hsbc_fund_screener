# HSBC Fund Screener - Holdings -> Top 5 holdings 字段映射分析报告

生成时间: 2025-09-06 19:08:42

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

**API请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`

**请求方法**: POST

## 相关文件链接

### API数据文件
- 📤 [API请求数据](06_holdings_05_Top_5_holdings/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](06_holdings_05_Top_5_holdings/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](06_holdings_05_Top_5_holdings/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](06_holdings_05_Top_5_holdings/result/fund_screener_result_20250906_190346.png) - 基金 Fund Profile 页面截图
- 📄 [页面源码](06_holdings_05_Top_5_holdings/result/fund_screener_page_20250906_190346.html) - 完整的HTML源码
- 📋 [页面信息](06_holdings_05_Top_5_holdings/result/page_info_20250906_190346.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](06_holdings_05_Top_5_holdings/mapping/frontend_fields_20250906_190842.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](06_holdings_05_Top_5_holdings/mapping/frontend_fields_20250906_190842.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金 Fund Profile 页面的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Profile Interface](06_holdings_05_Top_5_holdings/result/fund_screener_result_20250906_190346.png)

*图：HSBC基金 Fund Profile 界面 - 显示了基金详情的各个字段*

## 字段映射详情

### 1. Fund (基金名称)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund | header.name | 基金完整名称，包含基金公司和份额类别 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | AB - AMERICAN INCOME PORTFOLIO (CLASS AT-NZDH-MDIST CASH) | `AB - AMERICAN INCOME PORTFOLIO (CLASS AT-NZDH-MDIST CASH)` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.name` | 从响应数据中获取字段值的路径 |
| **备注** | 基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息 | | |

### 2. 1st (第一大持仓配置，包含持仓名称和权重百分比)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 1st | holdings.topHoldingsList[1] | 第一大持仓配置，基金投资组合中权重最高的单一持仓 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比1** | 2 Year Treasury Note Future Sept 25 | `2 Year Treasury Note Future Sept 25` | 持仓名称 |
| **API路径1** | - | `holdings.topHoldingsList[1].holdingCompany` | 持仓名称的API路径 |
| **示例对比2** | 23.04% | `23.04%` | 权重百分比 |
| **API路径2** | - | `holdings.topHoldingsList[1].holdingPercent` | 权重百分比的API路径 |
| **备注** | 第一大持仓反映基金的核心投资策略，通常占投资组合的最大比重，是基金投资风格和重点配置的重要体现 | | |

### 3. 2nd (第二大持仓配置，包含持仓名称和权重百分比)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 2nd | holdings.topHoldingsList[2] | 第二大持仓配置，基金投资组合中权重第二的单一持仓 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比1** | 5 Year Treasury Note Future Sept 25 | `5 Year Treasury Note Future Sept 25` | 持仓名称 |
| **API路径1** | - | `holdings.topHoldingsList[2].holdingCompany` | 持仓名称的API路径 |
| **示例对比2** | 15.70% | `15.70%` | 权重百分比 |
| **API路径2** | - | `holdings.topHoldingsList[2].holdingPercent` | 权重百分比的API路径 |
| **备注** | 第二大持仓显示基金的次要投资重点，与第一大持仓形成投资组合的主要构成，有助于分散单一持仓风险 | | |

### 4. 3rd (第三大持仓配置，包含持仓名称和权重百分比)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 3rd | holdings.topHoldingsList[3] | 第三大持仓配置，基金投资组合中权重第三的单一持仓 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比1** | United States Treasury Bonds 6.25% | `United States Treasury Bonds 6.25%` | 持仓名称 |
| **API路径1** | - | `holdings.topHoldingsList[3].holdingCompany` | 持仓名称的API路径 |
| **示例对比2** | 3.36% | `3.36%` | 权重百分比 |
| **API路径2** | - | `holdings.topHoldingsList[3].holdingPercent` | 权重百分比的API路径 |
| **备注** | 第三大持仓进一步丰富基金的投资分布，提供额外的投资机会和风险分散效果 | | |

### 5. 4th (第四大持仓配置，包含持仓名称和权重百分比)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 4th | holdings.topHoldingsList[4] | 第四大持仓配置，基金投资组合中权重第四的单一持仓 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比1** | Federal National Mortgage Association 5.5% | `Federal National Mortgage Association 5.5%` | 持仓名称 |
| **API路径1** | - | `holdings.topHoldingsList[4].holdingCompany` | 持仓名称的API路径 |
| **示例对比2** | 2.00% | `2.00%` | 权重百分比 |
| **API路径2** | - | `holdings.topHoldingsList[4].holdingPercent` | 权重百分比的API路径 |
| **备注** | 第四大持仓增强基金的多元化投资特征，有助于捕捉不同投资标的的收益机会 | | |

### 6. 5th (第五大持仓配置，包含持仓名称和权重百分比)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 5th | holdings.topHoldingsList[5] | 第五大持仓配置，基金投资组合中权重第五的单一持仓 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比1** | Ultra US Treasury Bond Future Sept 25 | `Ultra US Treasury Bond Future Sept 25` | 持仓名称 |
| **API路径1** | - | `holdings.topHoldingsList[5].holdingCompany` | 持仓名称的API路径 |
| **示例对比2** | 1.85% | `1.85%` | 权重百分比 |
| **API路径2** | - | `holdings.topHoldingsList[5].holdingPercent` | 权重百分比的API路径 |
| **备注** | 第五大持仓完善基金的投资配置结构，展示基金在不同投资标的的布局和风险管理能力 | | |

## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 | 字段类型 |
|---------|------------|----------|--------|----------|
| **Fund** | `header.name` | string | AB - AMERICAN INCOME PORTFOLIO (CLASS AT-NZDH-MDIST CASH) | 🟢 **API映射字段** |
| **1st** | `holdings.topHoldingsList[1]` | string | 2 Year Treasury Note Future Sept 25 | 🟢 **API映射字段** |
| **2nd** | `holdings.topHoldingsList[2]` | string | 5 Year Treasury Note Future Sept 25 | 🟢 **API映射字段** |
| **3rd** | `holdings.topHoldingsList[3]` | string | United States Treasury Bonds 6.25% | 🟢 **API映射字段** |
| **4th** | `holdings.topHoldingsList[4]` | string | Federal National Mortgage Association 5.5% | 🟢 **API映射字段** |
| **5th** | `holdings.topHoldingsList[5]` | string | Ultra US Treasury Bond Future Sept 25 | 🟢 **API映射字段** |

## 📋 API路径详细说明

### 🟢 API映射字段
这些字段直接从API响应数据中获取，无需额外计算：

- **Fund**: `header.name`
  - 📝 **说明**: 基金名称
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **1st**: `holdings.topHoldingsList[1]`
  - 📝 **说明**: 第一大持仓配置，包含持仓名称和权重百分比
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **2nd**: `holdings.topHoldingsList[2]`
  - 📝 **说明**: 第二大持仓配置，包含持仓名称和权重百分比
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **3rd**: `holdings.topHoldingsList[3]`
  - 📝 **说明**: 第三大持仓配置，包含持仓名称和权重百分比
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **4th**: `holdings.topHoldingsList[4]`
  - 📝 **说明**: 第四大持仓配置，包含持仓名称和权重百分比
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **5th**: `holdings.topHoldingsList[5]`
  - 📝 **说明**: 第五大持仓配置，包含持仓名称和权重百分比
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

### 🔴 计算字段 (Calculated Fields)
这些字段不直接存在于API响应中，需要通过计算得出：


## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐
│ Fund                │  ←──  │ header.name                      │
│ 1st                 │  ←──  │ holdings.topHoldingsList[1]      │
│ 2nd                 │  ←──  │ holdings.topHoldingsList[2]      │
│ 3rd                 │  ←──  │ holdings.topHoldingsList[3]      │
│ 4th                 │  ←──  │ holdings.topHoldingsList[4]      │
│ 5th                 │  ←──  │ holdings.topHoldingsList[5]      │
└─────────────────────┘       └──────────────────────────────────┘
```

## 数据转换说明

### 评级字段
- Morningstar rating 在API中以字符串形式存储（如 "3", "4", "5"），前端可能显示为星级图标

### 四分位排名字段
- 四分位排名在API中以数字形式存储（1, 2, 3, 4），前端需要转换为序数格式：
  - 1 → "1st"
  - 2 → "2nd"
  - 3 → "3rd"
  - 4 → "4th"

### 信用质量字段
- Average credit quality 在API中可能为null，前端应显示为 "N/A" 或 "-"

## 使用建议

1. 前端渲染时应根据数据类型进行相应的格式化
2. 评级字段可以考虑使用图标或星级显示
3. 四分位排名需要转换为用户友好的序数格式
4. 对于null值字段，应显示合适的占位符（如 "N/A"）
5. 晨星评级可以用星级图标增强视觉效果

