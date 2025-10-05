# HSBC Fund Screener - Performance -> Calendar returns 字段映射分析报告

生成时间: 2025-09-06 19:43:00

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

**API请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`

**请求方法**: POST

## 相关文件链接

### API数据文件
- 📤 [API请求数据](05_performance_01_Calendar_returns/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](05_performance_01_Calendar_returns/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](05_performance_01_Calendar_returns/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](05_performance_01_Calendar_returns/result/fund_screener_result_20250906_193854.png) - 基金 Fund Profile 页面截图
- 📄 [页面源码](05_performance_01_Calendar_returns/result/fund_screener_page_20250906_193854.html) - 完整的HTML源码
- 📋 [页面信息](05_performance_01_Calendar_returns/result/page_info_20250906_193854.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](05_performance_01_Calendar_returns/mapping/frontend_fields_20250906_194300.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](05_performance_01_Calendar_returns/mapping/frontend_fields_20250906_194300.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金 Fund Profile 页面的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Profile Interface](05_performance_01_Calendar_returns/result/fund_screener_result_20250906_193854.png)

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

### 2. YTD (年初至今收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | YTD | performance.calendarReturns.returnYTD | 年初至今收益率，基金当年表现指标 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 5.72% | `5.72371` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.calendarReturns.returnYTD` | 从响应数据中获取字段值的路径 |
| **备注** | 年初至今收益率反映基金在当前年度的表现，是评估基金短期表现的重要指标 | | |

### 3. 2024 (2024年收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 2024 | performance.calendarReturns.year[yearName=1].yearValue | 2024年收益率，基金2024年度表现 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 2.41% | `2.40799` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.calendarReturns.year[yearName=1].yearValue` | 从响应数据中获取字段值的路径 |
| **备注** | 2024年收益率反映基金在2024年度的完整表现，用于评估基金的年度投资效果 | | |

### 4. 2023 (2023年收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 2023 | performance.calendarReturns.year[yearName=2].yearValue | 2023年收益率，基金2023年度表现 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 7.80% | `7.80235` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.calendarReturns.year[yearName=2].yearValue` | 从响应数据中获取字段值的路径 |
| **备注** | 2023年收益率反映基金在2023年度的完整表现，用于历史年度对比分析 | | |

### 5. 2022 (2022年收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 2022 | performance.calendarReturns.year[yearName=3].yearValue | 2022年收益率，基金2022年度表现 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | -13.79% | `-13.79101` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.calendarReturns.year[yearName=3].yearValue` | 从响应数据中获取字段值的路径 |
| **备注** | 2022年收益率反映基金在2022年度的完整表现，用于历史年度对比分析 | | |

### 6. 2021 (2021年收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 2021 | performance.calendarReturns.year[yearName=4].yearValue | 2021年收益率，基金2021年度表现 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | -0.40% | `-0.39503` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.calendarReturns.year[yearName=4].yearValue` | 从响应数据中获取字段值的路径 |
| **备注** | 2021年收益率反映基金在2021年度的完整表现，用于历史年度对比分析 | | |

### 7. 2020 (2020年收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 2020 | performance.calendarReturns.year[yearName=5].yearValue | 2020年收益率，基金2020年度表现 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 3.00% | `3.00414` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.calendarReturns.year[yearName=5].yearValue` | 从响应数据中获取字段值的路径 |
| **备注** | 2020年收益率反映基金在2020年度的完整表现，包含疫情期间的市场表现 | | |

## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 | 字段类型 |
|---------|------------|----------|--------|----------|
| **Fund** | `header.name` | string | AB - AMERICAN INCOME PORTFOLIO (CLASS AT-NZDH-MDIST CASH) | 🟢 **API映射字段** |
| **YTD** | `performance.calendarReturns.returnYTD` | percentage | 5.72% | 🟢 **API映射字段** |
| **2024** | `performance.calendarReturns.year[yearName=1].yearValue` | percentage | 2.41% | 🟢 **API映射字段** |
| **2023** | `performance.calendarReturns.year[yearName=2].yearValue` | percentage | 7.80% | 🟢 **API映射字段** |
| **2022** | `performance.calendarReturns.year[yearName=3].yearValue` | percentage | -13.79% | 🟢 **API映射字段** |
| **2021** | `performance.calendarReturns.year[yearName=4].yearValue` | percentage | -0.40% | 🟢 **API映射字段** |
| **2020** | `performance.calendarReturns.year[yearName=5].yearValue` | percentage | 3.00% | 🟢 **API映射字段** |

## 📋 API路径详细说明

### 🟢 API映射字段
这些字段直接从API响应数据中获取，无需额外计算：

- **Fund**: `header.name`
  - 📝 **说明**: 基金名称
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **YTD**: `performance.calendarReturns.returnYTD`
  - 📝 **说明**: 年初至今收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **2024**: `performance.calendarReturns.year[yearName=1].yearValue`
  - 📝 **说明**: 2024年收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **2023**: `performance.calendarReturns.year[yearName=2].yearValue`
  - 📝 **说明**: 2023年收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **2022**: `performance.calendarReturns.year[yearName=3].yearValue`
  - 📝 **说明**: 2022年收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **2021**: `performance.calendarReturns.year[yearName=4].yearValue`
  - 📝 **说明**: 2021年收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **2020**: `performance.calendarReturns.year[yearName=5].yearValue`
  - 📝 **说明**: 2020年收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

### 🔴 计算字段 (Calculated Fields)
这些字段不直接存在于API响应中，需要通过计算得出：


## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐
│ Fund                │  ←──  │ header.name                      │
│ YTD                 │  ←──  │ performance.calendarReturns      │
│                     │       │ returnYTD                        │
│ 2024                │  ←──  │ performance.calendarReturns      │
│                     │       │ year[yearName=1].yearValue       │
│ 2023                │  ←──  │ performance.calendarReturns      │
│                     │       │ year[yearName=2].yearValue       │
│ 2022                │  ←──  │ performance.calendarReturns      │
│                     │       │ year[yearName=3].yearValue       │
│ 2021                │  ←──  │ performance.calendarReturns      │
│                     │       │ year[yearName=4].yearValue       │
│ 2020                │  ←──  │ performance.calendarReturns      │
│                     │       │ year[yearName=5].yearValue       │
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

