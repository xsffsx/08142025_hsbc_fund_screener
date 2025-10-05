# MYH

**文档创建时间**: 2025-08-06 00:34:28  
**源文件**: Fund Questions v2.xlsx  
**工作表**: 1/7

## 目录
- [数据概览](#数据概览)
- [数据表格](#数据表格)
- [数据统计](#数据统计)

## 数据概览

- **行数**: 46
- **列数**: 5
- **工作表名称**: MYH

### 列信息
1. **No.** - 类型: `float64`, 非空值: 15
2. **Most Common Question** - 类型: `object`, 非空值: 15
3. **Data Source (Morning *, WPC, Fund Fact Sheet, etc.)** - 类型: `object`, 非空值: 15
4. **Model Answers** - 类型: `object`, 非空值: 38
5. **Comments/Field/Logic (If Any)** - 类型: `object`, 非空值: 5

## 数据表格

| No. | Most Common Question | Data Source (Morning *, WPC, Fund Fact Sheet, etc.) | Model Answers | Comments/Field/Logic (If Any) |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
| 1.0 | What are the best and worst performance for specific timeframe (1M, 3M, etc.) for fund A? | Morningstar | Fund A recorded its best performance during the period [XX] to [XX] when the fund returned [X]% d... | The best and worst performance is compared against peers average in Morningstar. |
| 2.0 | What is the country A exposure for fund A? | Morningstar | Fund A has [X]% in country A, for the past 6 months, the allocation into country A ranges between... |  |
|  |  |  |  |  |
| 3.0 | What is the main sector exposure for fund A? | Morningstar | Fund A has [X]% in sector [X], which is its largest sector exposure. For the past 6 months, the a... |  |
|  |  |  |  |  |
| 4.0 | What are the top holdings (which funds have exposure to certain stocks/bonds) for fund A? | Morningstar | The top holdings of Fund A are P (X%), Q (Y%), R(Z%)… | Include up to 10 top holdings. |
|  |  |  |  |  |
| 5.0 | What are the funds with Product risk scoring (PRS) 1,2,3,4,5? | WPC | Here is a list of funds with a product risk scoring (PRS) of 1 in hhhh Malaysia |  |
|  |  |  | 1. XXX |  |
|  |  |  | 2. XXY |  |
|  |  |  | 3. XYY |  |
|  |  |  | 4.YYY |  |
| 6.0 | What is the distribution/payout frequency and mode for fund A? | Morningstar | Fund A pays distributions (Monthly/Quarterly/Annually) via cash/unit dividends. |  |
|  |  |  |  |  |
| 7.0 | What currency classes does fund A have? | WPC & Fund Fact Sheet | Fund A is available in USD,MYR and SGD Hedged Classes. |  |
|  |  |  |  |  |
| 8.0 | Show me a list of Shariah compliant funds | WPC | Here is the list of Shariah compliant Funds that is currently active for subscription on hhhh pla... |  |
|  |  |  | 1. XXX |  |
|  |  |  | 2. XXY |  |
| 9.0 | Show me a list of funds with their ESG / ESG score | WPC, Morningstar | Here is the list of selected ESG-labelled funds along with their respective ESG scores |  |
|  |  |  |  |  |
| 10.0 | Show me a list of EPF approved funds | WPC | Here is the list of EPF approved funds offered by hhhh: |  |
|  |  |  | 1. Fund A |  |
|  |  |  | 2. Fund B |  |
| 11.0 | Show me a list of newly launched funds (within the last 3 months) | WPC | Below are some newly launched funds by hhhh in the past 3 months |  |
|  |  |  | 1. Fund A |  |
|  |  |  | 2. Fund B |  |
| 12.0 | Show me the asset classes (stocks, bonds, commodities, mixed) for fund A | Morningstar | Fund A has assets classes of Fund A primarily invest in (equities/bonds/mixed assets,) with expos... |  |
|  |  |  | 1) Equities- X % |  |
|  |  |  | 2) Fixed Income -X% |  |
|  |  |  | 3) Cash -X% |  |
| 13.0 | Show me the styles (e.g. income, growth, or both) for fund A | Morningstar | Fund A follows a (Growth/Income/Value/Blended or combination) strategy is |  |
|  |  |  | 1. Growth 55% |  |
|  |  |  | 2. Value 45% |  |
| 14.0 | Which is the best performer fund for China/US/Asia Pacific Funds/ data source from Unit Trust Bro... | WPC | The best performer fund for China/Asia Pacific fund is |  |
|  |  |  | 1. XXY |  |
|  |  |  | 2. XXX |  |
| 15.0 | Which funds have exposure in ETF/ Japan/ Cryptocurrency/ ESG Related/ HealthCare Related? | Morningstar | Several funds have targeted exposure across various themes and asset types. Here is the list: |  |
|  |  |  | ETF Exposure: |  |
|  |  |  | 1) Fund A – XX% |  |
|  |  |  | 2) Fund B – XXX% |  |
|  |  |  |  |  |
|  |  |  | Japan Exposure: |  |
|  |  |  | 1) Fund A – XX% |  |
|  |  |  | 2) Fund B – XXX% |  |

## 数据统计

### 数值列统计

|       |      No. |
|-------|----------|
| count | 15       |
| mean  |  8       |
| std   |  4.47214 |
| min   |  1       |
| 25%   |  4.5     |
| 50%   |  8       |
| 75%   | 11.5     |
| max   | 15       |

### 文本列信息

- **Most Common Question**: 15 个唯一值, 最常见值: `Show me a list of EPF approved funds`
- **Data Source (Morning *, WPC, Fund Fact Sheet, etc.)**: 4 个唯一值, 最常见值: `Morningstar`
- **Model Answers**: 34 个唯一值, 最常见值: `1) Fund A – XX%`
- **Comments/Field/Logic (If Any)**: 3 个唯一值, 最常见值: ` `

---
*由 Excel转Markdown工具 生成于 2025-08-06 00:34:28*
