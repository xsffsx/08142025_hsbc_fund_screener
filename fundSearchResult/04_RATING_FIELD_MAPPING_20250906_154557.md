# HSBC Fund Screener 前端字段与API数据映射分析报告

生成时间: 2025-09-06 15:45:57

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

**API请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`

**请求方法**: POST

## 相关文件链接

### API数据文件
- 📤 [API请求数据](04_rating/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](04_rating/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](04_rating/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](04_rating/result/fund_screener_result_20250906_141818.png) - 基金 Fund Profile 页面截图
- 📄 [页面源码](04_rating/result/fund_screener_page_20250906_141818.html) - 完整的HTML源码
- 📋 [页面信息](04_rating/result/page_info_20250906_141818.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](04_rating/mapping/frontend_fields_20250906_154557.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](04_rating/mapping/frontend_fields_20250906_154557.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金 Fund Profile 页面的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Profile Interface](04_rating/result/fund_screener_result_20250906_141818.png)

*图：HSBC基金 Fund Profile 界面 - 显示了基金详情的各个字段*

## 字段映射详情

### 1. Fund (基金名称)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund | header.name | 基金完整名称，包含基金公司和份额类别 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | BlackRock World Gold Fund (Class A2) | `UBS (Lux) Key Selection SICAV-China Allocation Opportunity(P-HKD-MD-C)` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.name` | 从响应数据中获取字段值的路径 |
| **备注** | 基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息 | | |

### 2. Morningstar rating (晨星评级)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Morningstar rating | rating.morningstarRating | 晨星评级，基于基金历史表现的专业评级系统 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | ★★★★★ | `2` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `rating.morningstarRating` | 从响应数据中获取字段值的路径 |
| **备注** | 晨星评级采用1-5星评级系统，5星为最高评级，帮助投资者快速识别优质基金 | | |

### 3. Average credit quality (平均信用质量)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Average credit quality | rating.averageCreditQualityName | 平均信用质量，债券基金的信用风险评估指标 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | B | `B` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `rating.averageCreditQualityName` | 从响应数据中获取字段值的路径 |
| **备注** | 信用质量评级从AAA到D，反映债券基金持仓的信用风险水平，影响基金的违约风险 | | |

### 4. 1 year quartile ranking (1年四分位排名)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 1 year quartile ranking | rating.rank1Yr | 1年四分位排名，基金在同类产品中的相对表现排名 |
| **数据类型** | number | number | 直接映射，无需转换 |
| **示例对比** | 4th | `2` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `rating.rank1Yr` | 从响应数据中获取字段值的路径 |
| **备注** | 四分位排名将基金分为4个等级，第1四分位表示表现最佳的25%基金 | | |

### 5. 3 year quartile ranking (3年四分位排名)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 3 year quartile ranking | rating.rank3Yr | 3年四分位排名，基金在同类产品中的长期表现排名 |
| **数据类型** | number | number | 直接映射，无需转换 |
| **示例对比** | 3rd | `2` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `rating.rank3Yr` | 从响应数据中获取字段值的路径 |
| **备注** | 3年四分位排名反映基金的中期表现稳定性，是评估基金管理能力的重要指标 | | |

### 6. 5 year quartile ranking (5年四分位排名)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 5 year quartile ranking | rating.rank5Yr | 5年四分位排名，基金在同类产品中的长期稳定性排名 |
| **数据类型** | number | number | 直接映射，无需转换 |
| **示例对比** | 4th | `4` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `rating.rank5Yr` | 从响应数据中获取字段值的路径 |
| **备注** | 5年四分位排名展示基金的长期投资价值，适合长期投资者参考 | | |

## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 |
|---------|------------|----------|--------|
| **Fund** | `header.name` | string | BlackRock World Gold Fund (Class A2) |
| **Morningstar rating** | `rating.morningstarRating` | string | ★★★★★ |
| **Average credit quality** | `rating.averageCreditQualityName` | string | B |
| **1 year quartile ranking** | `rating.rank1Yr` | number | 4th |
| **3 year quartile ranking** | `rating.rank3Yr` | number | 3rd |
| **5 year quartile ranking** | `rating.rank5Yr` | number | 4th |

## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐
│ Fund                │  ←──  │ header.name                      │
│ Morningstar rating  │  ←──  │ rating.morningstarRating         │
│ Average credit quality │  ←──  │ rating.averageCreditQualityName  │
│ 1 year quartile ranking │  ←──  │ rating.rank1Yr                   │
│ 3 year quartile ranking │  ←──  │ rating.rank3Yr                   │
│ 5 year quartile ranking │  ←──  │ rating.rank5Yr                   │
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

