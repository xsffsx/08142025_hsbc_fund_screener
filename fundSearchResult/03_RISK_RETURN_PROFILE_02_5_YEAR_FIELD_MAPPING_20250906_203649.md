# HSBC Fund Screener - Risk return profile -> 5 year 字段映射分析报告

生成时间: 2025-09-06 20:36:49

## 概述

本报告分析了HSBC基金筛选器前端显示的字段与 `fundSearchResult` API 响应数据的对应关系。

**API请求URL**: `https://investments3.personal-banking.hsbc.com.hk/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundSearchResult`

**请求方法**: POST

## 相关文件链接

### API数据文件
- 📤 [API请求数据](03_risk_return_profile_02_5_year/result/fundSearchResult_request.json) - 完整的API请求参数
- 📥 [API响应数据](03_risk_return_profile_02_5_year/result/fundSearchResult_response.json) - 完整的API响应数据
- 🔍 [纯净响应数据](03_risk_return_profile_02_5_year/result/fundSearchResult_data.json) - 提取的核心响应数据

### 页面截图和源码
- 📸 [页面截图](03_risk_return_profile_02_5_year/result/fund_screener_result_20250906_203523.png) - 基金 Fund Profile 页面截图
- 📄 [页面源码](03_risk_return_profile_02_5_year/result/fund_screener_page_20250906_203523.html) - 完整的HTML源码
- 📋 [页面信息](03_risk_return_profile_02_5_year/result/page_info_20250906_203523.json) - 页面元数据信息

### 提取的字段数据
- 📊 [前端字段数据 (JSON)](03_risk_return_profile_02_5_year/mapping/frontend_fields_20250906_203649.json) - 提取的所有产品字段数据
- 📈 [前端字段数据 (CSV)](03_risk_return_profile_02_5_year/mapping/frontend_fields_20250906_203649.csv) - CSV格式的表格数据

## 前端界面截图

下图展示了HSBC基金 Fund Profile 页面的前端界面，包含了我们需要映射的所有字段：

![HSBC Fund Profile Interface](03_risk_return_profile_02_5_year/result/fund_screener_result_20250906_203523.png)

*图：HSBC基金 Fund Profile 界面 - 显示了基金详情的各个字段*

## 字段映射详情

### 1. Fund (基金名称)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Fund | header.name | 基金完整名称，包含基金公司和份额类别 |
| **数据类型** | string | string | 直接映射，无需转换 |
| **示例对比** | BlackRock World Gold Fund (Class A2) | `BlackRock World Gold Fund (Class A2)` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `header.name` | 从响应数据中获取字段值的路径 |
| **备注** | 基金名称是用户识别产品的主要标识，通常包含基金公司、投资策略和份额类别信息 | | |

### 2. Annualised return (年化收益率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Annualised return | performance.annualizedReturns.return1Yr | 年化收益率，基金的年化投资回报表现 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 76.92% | `76.91936` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `performance.annualizedReturns.return1Yr` | 从响应数据中获取字段值的路径 |
| **备注** | 年化收益率是评估基金历史表现的核心指标，帮助投资者了解基金的盈利能力 | | |

### 3. Standard deviation (标准差)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Standard deviation | risk[year=5].yearRisk.stdDev | 标准差，衡量基金收益率波动性的风险指标 |
| **数据类型** | percentage | percentage | 需要将小数转换为百分比格式 |
| **示例对比** | 28.68% | `29.384` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `risk[year=5].yearRisk.stdDev` | 从响应数据中获取字段值的路径 |
| **备注** | 标准差越高表示基金收益波动越大，风险越高，适合风险承受能力强的投资者 | | |

### 4. Sharpe ratio (夏普比率)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Sharpe ratio | risk[year=5].yearRisk.sharpeRatio | 夏普比率，衡量基金风险调整后收益的重要指标 |
| **数据类型** | number | number | 直接映射，无需转换 |
| **示例对比** | 1.635 | `0.256` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `risk[year=5].yearRisk.sharpeRatio` | 从响应数据中获取字段值的路径 |
| **备注** | 夏普比率越高表示基金在承担单位风险下获得的超额收益越多，是衡量投资效率的重要工具 | | |

### 5. Alpha (阿尔法值)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Alpha | risk[year=5].yearRisk.alpha | 阿尔法值，衡量基金相对基准的超额收益能力 |
| **数据类型** | number | number | 直接映射，无需转换 |
| **示例对比** | 0.93 | `-1.64` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `risk[year=5].yearRisk.alpha` | 从响应数据中获取字段值的路径 |
| **备注** | 正的阿尔法值表示基金经理具备超越市场的选股能力，是主动管理基金的重要评价指标 | | |

### 6. Beta (贝塔值)

| 属性 | 前端显示值 | API原始值 | 说明 |
|------|-----------|-----------|------|
| **字段名称** | Beta | risk[year=5].yearRisk.beta | 贝塔值，衡量基金相对市场的系统性风险敏感度 |
| **数据类型** | number | number | 直接映射，无需转换 |
| **示例对比** | 0.93 | `0.94` | 前端显示格式化后的值，API返回原始数据 |
| **API路径** | - | `risk[year=5].yearRisk.beta` | 从响应数据中获取字段值的路径 |
| **备注** | 贝塔值大于1表示基金比市场更敏感，小于1表示相对稳定，帮助投资者了解系统性风险暴露 | | |

## 字段映射汇总表

| 前端字段 | API数据路径 | 数据类型 | 示例值 |
|---------|------------|----------|--------|
| **Fund** | `header.name` | string | BlackRock World Gold Fund (Class A2) |
| **Annualised return** | `performance.annualizedReturns.return1Yr` | percentage | 76.92% |
| **Standard deviation** | `risk[year=5].yearRisk.stdDev` | percentage | 28.68% |
| **Sharpe ratio** | `risk[year=5].yearRisk.sharpeRatio` | number | 1.635 |
| **Alpha** | `risk[year=5].yearRisk.alpha` | number | 0.93 |
| **Beta** | `risk[year=5].yearRisk.beta` | number | 0.93 |

## 前端字段与API数据对应关系图

```
前端表格字段                    API响应数据路径
┌─────────────────────┐       ┌──────────────────────────────────┐
│ Fund                │  ←──  │ header.name                      │
│ Annualised return   │  ←──  │ performance.annualizedReturns    │
│                     │       │ return1Yr                        │
│ Standard deviation  │  ←──  │ risk[year=5].yearRisk.stdDev     │
│ Sharpe ratio        │  ←──  │ risk[year=5].yearRisk            │
│                     │       │ sharpeRatio                      │
│ Alpha               │  ←──  │ risk[year=5].yearRisk.alpha      │
│ Beta                │  ←──  │ risk[year=5].yearRisk.beta       │
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

