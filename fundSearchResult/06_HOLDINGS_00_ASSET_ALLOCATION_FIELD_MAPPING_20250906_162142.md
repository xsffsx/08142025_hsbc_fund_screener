# HSBC Fund Screener 前端字段与API数据映射分析报告

生成时间: 2025-09-06 16:21:42

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

**API请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`

**请求方法**: POST

## 相关文件链接

### API数据文件
- 📤 [API请求数据](06_holdings_00_Asset_allocation/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](06_holdings_00_Asset_allocation/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](06_holdings_00_Asset_allocation/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](06_holdings_00_Asset_allocation/result/fund_screener_result_20250906_145908.png) - 基金 Fund Profile 页面截图
- 📄 [页面源码](06_holdings_00_Asset_allocation/result/fund_screener_page_20250906_145908.html) - 完整的HTML源码
- 📋 [页面信息](06_holdings_00_Asset_allocation/result/page_info_20250906_145908.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](06_holdings_00_Asset_allocation/mapping/frontend_fields_20250906_162142.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](06_holdings_00_Asset_allocation/mapping/frontend_fields_20250906_162142.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金 Fund Profile 页面的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Profile Interface](06_holdings_00_Asset_allocation/result/fund_screener_result_20250906_145908.png)

*图：HSBC基金 Fund Profile 界面 - 显示了基金详情的各个字段*

## 字段映射详情

### 1. Fund (基金名称)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund | header.name | 基金完整名称，包含基金公司和份额类别 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | BlackRock Asian Tiger Bond Fund (Class A2) | `BlackRock Asian Tiger Bond Fund (Class A2)` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.name` | 从响应数据中获取字段值的路径 |
| **备注** | 基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息 | | |

### 2. Stock (股票资产配置比例)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Stock | holdings.assetAlloc.assetAllocations[name=Stock].weighting | 股票资产配置比例，反映基金投资于股票市场的资金占比 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | -0.23% | `-0.22746` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `holdings.assetAlloc.assetAllocations[name=Stock].weighting` | 从响应数据中获取字段值的路径 |
| **备注** | 股票配置比例直接影响基金的风险收益特征，高股票配置通常意味着更高的潜在收益和风险 | | |

### 3. Bond (债券资产配置比例)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Bond | holdings.assetAlloc.assetAllocations[name=Bond].weighting | 债券资产配置比例，反映基金投资于债券市场的资金占比 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 79.58% | `79.58465` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `holdings.assetAlloc.assetAllocations[name=Bond].weighting` | 从响应数据中获取字段值的路径 |
| **备注** | 债券配置比例反映基金的稳健程度，债券通常提供相对稳定的收益和较低的波动性 | | |

### 4. Cash (现金资产配置比例)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Cash | holdings.assetAlloc.assetAllocations[name=Cash].weighting | 现金资产配置比例，反映基金持有现金及现金等价物的资金占比 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 16.96% | `16.96025` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `holdings.assetAlloc.assetAllocations[name=Cash].weighting` | 从响应数据中获取字段值的路径 |
| **备注** | 现金配置比例体现基金的流动性管理策略，适度的现金配置有助于应对赎回需求和投资机会 | | |

### 5. Others (其他资产配置比例 (通过计算得出: 100% - Stock% - Bond% - Cash%))

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Others | calculated:100-Stock-Bond-Cash | 其他资产配置比例，反映基金投资于其他类型资产的资金占比 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 3.68% | `3.6825599999999987` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `calculated:100-Stock-Bond-Cash` | 从响应数据中获取字段值的路径 |
| **备注** | 其他资产配置包括商品、房地产投资信托基金(REITs)、衍生品等，用于分散投资风险和增强收益 | | |

## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 | 字段类型 |
|---------|------------|----------|--------|----------|
| **Fund** | `header.name` | string | BlackRock Asian Tiger Bond Fund (Class A2) | 🟢 **API映射字段** |
| **Stock** | `holdings.assetAlloc.assetAllocations[name=Stock].weighting` | percentage | -0.23% | 🟢 **API映射字段** |
| **Bond** | `holdings.assetAlloc.assetAllocations[name=Bond].weighting` | percentage | 79.58% | 🟢 **API映射字段** |
| **Cash** | `holdings.assetAlloc.assetAllocations[name=Cash].weighting` | percentage | 16.96% | 🟢 **API映射字段** |
| **Others** | `calculated:100-Stock-Bond-Cash` <br/> 📋 **计算公式**: 100% - Stock% - Bond% - Cash% | percentage | 3.68% | 🔴 **计算字段 (Calculated)** |

## 📋 API路径详细说明

### 🟢 API映射字段
这些字段直接从API响应数据中获取，无需额外计算：

- **Fund**: `header.name`
  - 📝 **说明**: 基金名称
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **Stock**: `holdings.assetAlloc.assetAllocations[name=Stock].weighting`
  - 📝 **说明**: 股票资产配置比例
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **Bond**: `holdings.assetAlloc.assetAllocations[name=Bond].weighting`
  - 📝 **说明**: 债券资产配置比例
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **Cash**: `holdings.assetAlloc.assetAllocations[name=Cash].weighting`
  - 📝 **说明**: 现金资产配置比例
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

### 🔴 计算字段 (Calculated Fields)
这些字段不直接存在于API响应中，需要通过计算得出：

- **Others**: `calculated:100-Stock-Bond-Cash`
  - 📝 **说明**: 其他资产配置比例 (通过计算得出: 100% - Stock% - Bond% - Cash%)
  - 🧮 **计算公式**: `100% - Stock% - Bond% - Cash%`
  - 📊 **依赖字段**: Stock, Bond, Cash
  - 🔍 **计算逻辑**: 从API获取依赖字段的值，然后按公式计算得出最终结果


## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐
│ Fund                │  ←──  │ header.name                      │
│ Stock               │  ←──  │ holdings.assetAlloc              │
│                     │       │ assetAllocations[name=Stock]     │
│                     │       │ weighting                        │
│ Bond                │  ←──  │ holdings.assetAlloc              │
│                     │       │ assetAllocations[name=Bond]      │
│                     │       │ weighting                        │
│ Cash                │  ←──  │ holdings.assetAlloc              │
│                     │       │ assetAllocations[name=Cash]      │
│                     │       │ weighting                        │
│ Others              │  ←──  │ calculated:100-Stock-Bond-Cash   │
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

