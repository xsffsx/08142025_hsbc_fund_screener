# HSBC Fund Screener - Performance -> Period returns 字段映射分析报告

生成时间: 2025-09-06 19:17:08

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

**API请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`

**请求方法**: POST

## 相关文件链接

### API数据文件
- 📤 [API请求数据](05_performance_00_Period_returns/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](05_performance_00_Period_returns/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](05_performance_00_Period_returns/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](05_performance_00_Period_returns/result/fund_screener_result_20250906_144036.png) - 基金 Fund Profile 页面截图
- 📄 [页面源码](05_performance_00_Period_returns/result/fund_screener_page_20250906_144036.html) - 完整的HTML源码
- 📋 [页面信息](05_performance_00_Period_returns/result/page_info_20250906_144036.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](05_performance_00_Period_returns/mapping/frontend_fields_20250906_191708.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](05_performance_00_Period_returns/mapping/frontend_fields_20250906_191708.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金 Fund Profile 页面的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Profile Interface](05_performance_00_Period_returns/result/fund_screener_result_20250906_144036.png)

*图：HSBC基金 Fund Profile 界面 - 显示了基金详情的各个字段*

## 字段映射详情

### 1. Fund (基金名称)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund | header.name | 基金完整名称，包含基金公司和份额类别 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | HSBC ASIAN HIGH YIELD BOND FUND (CLASS AM3-EURH-MDIST-CASH) | `HSBC ASIAN HIGH YIELD BOND FUND (CLASS AM3-EURH-MDIST-CASH)` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.name` | 从响应数据中获取字段值的路径 |
| **备注** | 基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息 | | |

### 2. 1 month (1个月年化收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 1 month | performance.annualizedReturns.return1Mth | 1个月年化收益率，基金短期表现指标 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 1.24% | `1.23709` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.annualizedReturns.return1Mth` | 从响应数据中获取字段值的路径 |
| **备注** | 1个月收益率反映基金最近期的表现，但波动性较大，不宜作为长期投资决策依据 | | |

### 3. 3 month (3个月年化收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 3 month | performance.annualizedReturns.return3Mth | 3个月年化收益率，基金短期表现指标 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 3.25% | `3.24671` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.annualizedReturns.return3Mth` | 从响应数据中获取字段值的路径 |
| **备注** | 3个月收益率能够平滑短期波动，更好地反映基金近期的投资策略效果 | | |

### 4. 6 month (6个月年化收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 6 month | performance.annualizedReturns.return6Mth | 6个月年化收益率，基金中短期表现指标 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 0.90% | `0.89675` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.annualizedReturns.return6Mth` | 从响应数据中获取字段值的路径 |
| **备注** | 6个月收益率是评估基金中短期表现的重要指标，能够反映基金在不同市场环境下的适应能力 | | |

### 5. 1 year (1年年化收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 1 year | performance.annualizedReturns.return1Yr | 1年年化收益率，基金年度表现指标 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 1.85% | `1.85406` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.annualizedReturns.return1Yr` | 从响应数据中获取字段值的路径 |
| **备注** | 1年收益率是最常用的基金表现评估指标，能够涵盖完整的市场周期 | | |

### 6. 3 year (3年年化收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 3 year | performance.annualizedReturns.return3Yr | 3年年化收益率，基金中期表现指标 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 1.82% | `1.81547` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.annualizedReturns.return3Yr` | 从响应数据中获取字段值的路径 |
| **备注** | 3年收益率反映基金的中期投资能力，是评估基金管理团队稳定性的重要指标 | | |

### 7. 5 year (5年年化收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 5 year | performance.annualizedReturns.return5Yr | 5年年化收益率，基金长期表现指标 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | -7.36% | `-7.36271` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.annualizedReturns.return5Yr` | 从响应数据中获取字段值的路径 |
| **备注** | 5年收益率展示基金的长期投资价值，适合长期投资者评估基金的持续盈利能力 | | |

### 8. 10 year (10年年化收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | 10 year | performance.annualizedReturns.return10Yr | 10年年化收益率，基金长期稳定性指标 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | -1.36% | `-1.35852` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.annualizedReturns.return10Yr` | 从响应数据中获取字段值的路径 |
| **备注** | 10年收益率是评估基金长期稳定性的黄金标准，能够跨越多个市场周期 | | |

### 9. Since inception (成立以来年化收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Since inception | performance.annualizedReturns.returnSinceInception | 成立以来年化收益率，基金历史总体表现 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | -1.12% | `-1.1207` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.annualizedReturns.returnSinceInception` | 从响应数据中获取字段值的路径 |
| **备注** | 成立以来收益率反映基金的历史总体表现，是评估基金长期投资价值的终极指标 | | |

## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 | 字段类型 |
|---------|------------|----------|--------|----------|
| **Fund** | `header.name` | string | HSBC ASIAN HIGH YIELD BOND FUND (CLASS AM3-EURH-MDIST-CASH) | 🟢 **API映射字段** |
| **1 month** | `performance.annualizedReturns.return1Mth` | percentage | 1.24% | 🟢 **API映射字段** |
| **3 month** | `performance.annualizedReturns.return3Mth` | percentage | 3.25% | 🟢 **API映射字段** |
| **6 month** | `performance.annualizedReturns.return6Mth` | percentage | 0.90% | 🟢 **API映射字段** |
| **1 year** | `performance.annualizedReturns.return1Yr` | percentage | 1.85% | 🟢 **API映射字段** |
| **3 year** | `performance.annualizedReturns.return3Yr` | percentage | 1.82% | 🟢 **API映射字段** |
| **5 year** | `performance.annualizedReturns.return5Yr` | percentage | -7.36% | 🟢 **API映射字段** |
| **10 year** | `performance.annualizedReturns.return10Yr` | percentage | -1.36% | 🟢 **API映射字段** |
| **Since inception** | `performance.annualizedReturns.returnSinceInception` | percentage | -1.12% | 🟢 **API映射字段** |

## 📋 API路径详细说明

### 🟢 API映射字段
这些字段直接从API响应数据中获取，无需额外计算：

- **Fund**: `header.name`
  - 📝 **说明**: 基金名称
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **1 month**: `performance.annualizedReturns.return1Mth`
  - 📝 **说明**: 1个月年化收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **3 month**: `performance.annualizedReturns.return3Mth`
  - 📝 **说明**: 3个月年化收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **6 month**: `performance.annualizedReturns.return6Mth`
  - 📝 **说明**: 6个月年化收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **1 year**: `performance.annualizedReturns.return1Yr`
  - 📝 **说明**: 1年年化收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **3 year**: `performance.annualizedReturns.return3Yr`
  - 📝 **说明**: 3年年化收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **5 year**: `performance.annualizedReturns.return5Yr`
  - 📝 **说明**: 5年年化收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **10 year**: `performance.annualizedReturns.return10Yr`
  - 📝 **说明**: 10年年化收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

- **Since inception**: `performance.annualizedReturns.returnSinceInception`
  - 📝 **说明**: 成立以来年化收益率
  - 🔍 **获取方式**: 直接从API响应数据的嵌套JSON结构中提取

### 🔴 计算字段 (Calculated Fields)
这些字段不直接存在于API响应中，需要通过计算得出：


## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐
│ Fund                │  ←──  │ header.name                      │
│ 1 month             │  ←──  │ performance.annualizedReturns    │
│                     │       │ return1Mth                       │
│ 3 month             │  ←──  │ performance.annualizedReturns    │
│                     │       │ return3Mth                       │
│ 6 month             │  ←──  │ performance.annualizedReturns    │
│                     │       │ return6Mth                       │
│ 1 year              │  ←──  │ performance.annualizedReturns    │
│                     │       │ return1Yr                        │
│ 3 year              │  ←──  │ performance.annualizedReturns    │
│                     │       │ return3Yr                        │
│ 5 year              │  ←──  │ performance.annualizedReturns    │
│                     │       │ return5Yr                        │
│ 10 year             │  ←──  │ performance.annualizedReturns    │
│                     │       │ return10Yr                       │
│ Since inception     │  ←──  │ performance.annualizedReturns    │
│                     │       │ returnSinceInception             │
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

