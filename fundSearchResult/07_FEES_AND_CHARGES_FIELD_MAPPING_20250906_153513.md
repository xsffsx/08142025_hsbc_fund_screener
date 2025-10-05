# HSBC Fund Screener 前端字段与API数据映射分析报告

生成时间: 2025-09-06 15:35:13

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

**API请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`

**请求方法**: POST

## 相关文件链接

### API数据文件
- 📤 [API请求数据](07_fees_and_charges/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](07_fees_and_charges/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](07_fees_and_charges/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](07_fees_and_charges/result/fund_screener_result_20250906_152120.png) - 基金 Fund Profile 页面截图
- 📄 [页面源码](07_fees_and_charges/result/fund_screener_page_20250906_152120.html) - 完整的HTML源码
- 📋 [页面信息](07_fees_and_charges/result/page_info_20250906_152120.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](07_fees_and_charges/mapping/frontend_fields_20250906_153513.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](07_fees_and_charges/mapping/frontend_fields_20250906_153513.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金 Fund Profile 页面的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Profile Interface](07_fees_and_charges/result/fund_screener_result_20250906_152120.png)

*图：HSBC基金 Fund Profile 界面 - 显示了基金详情的各个字段*

## 字段映射详情

### 1. Fund (基金名称)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund | header.name | 基金完整名称，包含基金公司和份额类别 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | HSBC GIF - ULTRA SHORT DURATION BOND (CLASS PC-HKD-ACC) | `HSBC GIF - ULTRA SHORT DURATION BOND (CLASS PC-HKD-ACC)` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.name` | 从响应数据中获取字段值的路径 |
| **备注** | 基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息 | | |

### 2. HSBC initial charge (HSBC初始费用/认购费)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | HSBC initial charge | profile.initialCharge | HSBC初始费用/认购费，投资者购买基金时需要支付的一次性费用 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 2.00% | `2` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `profile.initialCharge` | 从响应数据中获取字段值的路径 |
| **备注** | 初始费用直接影响投资成本，较低的初始费用有利于提高投资收益率，部分基金可能提供费用优惠 | | |

### 3. Annual management fee (maximum) (年度管理费(最高))

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Annual management fee (maximum) | profile.annualManagementFee | 年度管理费(最高)，基金管理公司收取的年度管理费用上限 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 0.30% | `0.3` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `profile.annualManagementFee` | 从响应数据中获取字段值的路径 |
| **备注** | 管理费是基金的主要运营成本，较低的管理费有助于提高长期投资回报，投资者应关注费用与基金表现的性价比 | | |

### 4. HSBC minimum investment amount (HSBC最低投资金额)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | HSBC minimum investment amount | summary.switchInMinAmount | HSBC最低投资金额，通过HSBC投资该基金的最低金额要求 |
| **数据类型** | currency | currency | 直接映射，无需转换 |
| **示例对比** | HKD 10,000 | `10000` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `summary.switchInMinAmount` | 从响应数据中获取字段值的路径 |
| **备注** | 最低投资金额决定了投资门槛，不同币种和地区可能有不同的最低投资要求，影响投资者的准入条件 | | |

### 5. Expense ratio (费用比率/总费用率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Expense ratio | profile.expenseRatio | 费用比率/总费用率，基金运营的总费用占基金资产净值的比例 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 0.45% | `0.45` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `profile.expenseRatio` | 从响应数据中获取字段值的路径 |
| **备注** | 费用比率是衡量基金成本效率的重要指标，包含管理费、托管费、审计费等各项费用，较低的费用比率通常有利于长期投资表现 | | |

## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 | 字段类型 |
|---------|------------|----------|--------|----------|
| **Fund** | `header.name` | string | HSBC GIF - ULTRA SHORT DURATION BOND (CLASS PC-HKD-ACC) | 🟢 **API映射字段** |
| **HSBC initial charge** | `profile.initialCharge` | percentage | 2.00% | 🟢 **API映射字段** |
| **Annual management fee (maximum)** | `profile.annualManagementFee` | percentage | 0.30% | 🟢 **API映射字段** |
| **HSBC minimum investment amount** | `summary.switchInMinAmount` | currency | HKD 10,000 | 🟢 **API映射字段** |
| **Expense ratio** | `profile.expenseRatio` | percentage | 0.45% | 🟢 **API映射字段** |

## 📋 API路径详细说明

### 🟢 API映射字段
这些字段直接从API响应数据中获取，无需额外计算：

- **Fund**: `header.name`
  - 📝 **说明**: 基金名称
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **HSBC initial charge**: `profile.initialCharge`
  - 📝 **说明**: HSBC初始费用/认购费
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **Annual management fee (maximum)**: `profile.annualManagementFee`
  - 📝 **说明**: 年度管理费(最高)
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **HSBC minimum investment amount**: `summary.switchInMinAmount`
  - 📝 **说明**: HSBC最低投资金额
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **Expense ratio**: `profile.expenseRatio`
  - 📝 **说明**: 费用比率/总费用率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

### 🔴 计算字段 (Calculated Fields)
这些字段不直接存在于API响应中，需要通过计算得出：


## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐
│ Fund                │  ←──  │ header.name                      │
│ HSBC initial charge │  ←──  │ profile.initialCharge            │
│ Annual management fee (maximum) │  ←──  │ profile.annualManagementFee      │
│ HSBC minimum investment amount │  ←──  │ summary.switchInMinAmount        │
│ Expense ratio       │  ←──  │ profile.expenseRatio             │
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

