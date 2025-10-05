# HSBC Fund Screener - Holdings -> Top 5 bond geographic 字段映射分析报告

生成时间: 2025-09-06 18:52:58

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

**API请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`

**请求方法**: POST

## 相关文件链接

### API数据文件
- 📤 [API请求数据](06_holdings_04_Top_5_bond_geographic/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](06_holdings_04_Top_5_bond_geographic/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](06_holdings_04_Top_5_bond_geographic/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](06_holdings_04_Top_5_bond_geographic/result/fund_screener_result_20250906_184753.png) - 基金 Fund Profile 页面截图
- 📄 [页面源码](06_holdings_04_Top_5_bond_geographic/result/fund_screener_page_20250906_184753.html) - 完整的HTML源码
- 📋 [页面信息](06_holdings_04_Top_5_bond_geographic/result/page_info_20250906_184753.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](06_holdings_04_Top_5_bond_geographic/mapping/frontend_fields_20250906_185258.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](06_holdings_04_Top_5_bond_geographic/mapping/frontend_fields_20250906_185258.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金 Fund Profile 页面的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Profile Interface](06_holdings_04_Top_5_bond_geographic/result/fund_screener_result_20250906_184753.png)

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

### 2. 1st (第一大债券地理区域配置，包含债券地理区域名称和权重百分比)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 1st | holdings.bondRegional.bondRegionalExposures[name=US] | 第一大债券地理区域配置，基金在债券投资中权重最高的地理区域 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比1** | United States | `United States` | 债券地理区域名称 |
| **API路径1** | - | `holdings.bondRegional.bondRegionalExposures[name=US].name` | 债券地理区域名称的API路径 |
| **示例对比2** | 126.48% | `126.48%` | 权重百分比 |
| **API路径2** | - | `holdings.bondRegional.bondRegionalExposures[name=US].weighting` | 权重百分比的API路径 |
| **备注** | 第一大债券地理区域配置反映基金的核心债券投资地域策略，通常占债券投资的最大比重，是基金固定收益地域分布的重要体现 | | |

### 3. 2nd (第二大债券地理区域配置，包含债券地理区域名称和权重百分比)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 2nd | holdings.bondRegional.bondRegionalExposures[name=LA] | 第二大债券地理区域配置，基金在债券投资中权重第二的地理区域 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比1** | Latin America | `Latin America` | 债券地理区域名称 |
| **API路径1** | - | `holdings.bondRegional.bondRegionalExposures[name=LA].name` | 债券地理区域名称的API路径 |
| **示例对比2** | 9.97% | `9.97%` | 权重百分比 |
| **API路径2** | - | `holdings.bondRegional.bondRegionalExposures[name=LA].weighting` | 权重百分比的API路径 |
| **备注** | 第二大债券地理区域配置显示基金的次要债券投资市场，与第一大区域形成投资组合的主要构成，有助于分散单一地域风险 | | |

### 4. 3rd (第三大债券地理区域配置，包含债券地理区域名称和权重百分比)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 3rd | holdings.bondRegional.bondRegionalExposures[name=UK] | 第三大债券地理区域配置，基金在债券投资中权重第三的地理区域 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比1** | United Kingdom | `United Kingdom` | 债券地理区域名称 |
| **API路径1** | - | `holdings.bondRegional.bondRegionalExposures[name=UK].name` | 债券地理区域名称的API路径 |
| **示例对比2** | 5.00% | `5.00%` | 权重百分比 |
| **API路径2** | - | `holdings.bondRegional.bondRegionalExposures[name=UK].weighting` | 权重百分比的API路径 |
| **备注** | 第三大债券地理区域配置进一步丰富基金的债券地域分布，提供额外的投资机会和地域风险分散效果 | | |

### 5. 4th (第四大债券地理区域配置，包含债券地理区域名称和权重百分比)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 4th | holdings.bondRegional.bondRegionalExposures[name=EZ] | 第四大债券地理区域配置，基金在债券投资中权重第四的地理区域 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比1** | Eurozone | `Eurozone` | 债券地理区域名称 |
| **API路径1** | - | `holdings.bondRegional.bondRegionalExposures[name=EZ].name` | 债券地理区域名称的API路径 |
| **示例对比2** | 4.60% | `4.60%` | 权重百分比 |
| **API路径2** | - | `holdings.bondRegional.bondRegionalExposures[name=EZ].weighting` | 权重百分比的API路径 |
| **备注** | 第四大债券地理区域配置增强基金的多元化债券投资特征，有助于捕捉不同地区的收益机会 | | |

### 6. 5th (第五大债券地理区域配置，包含债券地理区域名称和权重百分比)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 5th | holdings.bondRegional.bondRegionalExposures[name=AE] | 第五大债券地理区域配置，基金在债券投资中权重第五的地理区域 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比1** | Asia - Emerging | `Asia - Emerging` | 债券地理区域名称 |
| **API路径1** | - | `holdings.bondRegional.bondRegionalExposures[name=AE].name` | 债券地理区域名称的API路径 |
| **示例对比2** | 3.09% | `3.09%` | 权重百分比 |
| **API路径2** | - | `holdings.bondRegional.bondRegionalExposures[name=AE].weighting` | 权重百分比的API路径 |
| **备注** | 第五大债券地理区域配置完善基金的债券配置结构，展示基金在不同地理市场的投资布局和风险管理能力 | | |

## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 | 字段类型 |
|---------|------------|----------|--------|----------|
| **Fund** | `header.name` | string | AB - AMERICAN INCOME PORTFOLIO (CLASS AT-NZDH-MDIST CASH) | 🟢 **API映射字段** |
| **1st** | `holdings.bondRegional.bondRegionalExposures[name=US]` | string | United States | 🟢 **API映射字段** |
| **2nd** | `holdings.bondRegional.bondRegionalExposures[name=LA]` | string | Latin America | 🟢 **API映射字段** |
| **3rd** | `holdings.bondRegional.bondRegionalExposures[name=UK]` | string | United Kingdom | 🟢 **API映射字段** |
| **4th** | `holdings.bondRegional.bondRegionalExposures[name=EZ]` | string | Eurozone | 🟢 **API映射字段** |
| **5th** | `holdings.bondRegional.bondRegionalExposures[name=AE]` | string | Asia - Emerging | 🟢 **API映射字段** |

## 📋 API路径详细说明

### 🟢 API映射字段
这些字段直接从API响应数据中获取，无需额外计算：

- **Fund**: `header.name`
  - 📝 **说明**: 基金名称
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **1st**: `holdings.bondRegional.bondRegionalExposures[name=US]`
  - 📝 **说明**: 第一大债券地理区域配置，包含债券地理区域名称和权重百分比
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **2nd**: `holdings.bondRegional.bondRegionalExposures[name=LA]`
  - 📝 **说明**: 第二大债券地理区域配置，包含债券地理区域名称和权重百分比
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **3rd**: `holdings.bondRegional.bondRegionalExposures[name=UK]`
  - 📝 **说明**: 第三大债券地理区域配置，包含债券地理区域名称和权重百分比
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **4th**: `holdings.bondRegional.bondRegionalExposures[name=EZ]`
  - 📝 **说明**: 第四大债券地理区域配置，包含债券地理区域名称和权重百分比
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **5th**: `holdings.bondRegional.bondRegionalExposures[name=AE]`
  - 📝 **说明**: 第五大债券地理区域配置，包含债券地理区域名称和权重百分比
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

### 🔴 计算字段 (Calculated Fields)
这些字段不直接存在于API响应中，需要通过计算得出：


## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐
│ Fund                │  ←──  │ header.name                      │
│ 1st                 │  ←──  │ holdings.bondRegional            │
│                     │       │ bondRegionalExposures[name=US]   │
│ 2nd                 │  ←──  │ holdings.bondRegional            │
│                     │       │ bondRegionalExposures[name=LA]   │
│ 3rd                 │  ←──  │ holdings.bondRegional            │
│                     │       │ bondRegionalExposures[name=UK]   │
│ 4th                 │  ←──  │ holdings.bondRegional            │
│                     │       │ bondRegionalExposures[name=EZ]   │
│ 5th                 │  ←──  │ holdings.bondRegional            │
│                     │       │ bondRegionalExposures[name=AE]   │
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

