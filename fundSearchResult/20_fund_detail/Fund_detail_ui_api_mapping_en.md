# HSBC Fund Detail Page Screenshot Inventory (English Version)

## Document Description
This document records the screenshot file inventory of HSBC fund detail page functional modules, including complete file information, preview links, and text content from images.

**Target Page**: https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundDetail/U63166  
**Fund Code**: U63166 - HCIT-HSBC GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND  
**Screenshot Date**: September 9, 2025  
**Total Files**: 19 screenshot files


---
# 1. Fund Summary Information
![Fund Summary](./sources/screenshots/01_summary.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product`

**API Files**:
- Request: [product_request.json](./sources/api_responses/product_request.json)
- Response: [product_response.json](./sources/api_responses/product_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Fund Name | HCIT-HSBC GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) U63166 | HCIT-HSBC GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) U63166 | data.fundBasicInfo.fundName | string | 完全匹配 |
| ESG Label | ESG | ESG | data.fundBasicInfo.esgLabel | string | 完全匹配 |
| Current Price | USD 9.307 | 9.351 | data.fundBasicInfo.currentPrice | number | ⚠️ 实时价格变化，显示格式化为USD |
| Price Change | +0.026 (+0.28%) | +0.044 (+0.47%) | data.fundBasicInfo.priceChangeDisplay | string | ⚠️ 实时变化，包含格式化显示 |
| Last Updated | Updated as of 05 Sep 2025 | Updated as of 08 Sep 2025 | data.fundBasicInfo.lastUpdatedDisplay | string | ⚠️ 日期实时更新 |
| Action Buttons | Buy, Sell, Switch, Monthly investment plan, Add to compare | ["Buy", "Sell", "Switch", "Monthly investment plan", "Add to compare"] | data.actionButtons[].label | array | 按钮标签数组 |

---
# 2. Fund Risk Level
![Fund Risk](./sources/screenshots/02_summary_fund_risk.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product`

**API Files**:
- Request: [product_request.json](./sources/api_responses/product_request.json)
- Response: [product_response.json](./sources/api_responses/product_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Fund risk | Fund risk | data.fundBasicInfo.riskIndicator | string | 完全匹配 |
| Risk Indicator | HSBC risk level | HSBC risk level | data.fundBasicInfo.riskIndicator | string | 完全匹配 |
| Risk Description | Low to medium risk | Low to medium risk | data.fundBasicInfo.riskDescription | string | 完全匹配 |
| Risk Level | "2" | 2 | data.fundBasicInfo.riskLevel | number | ⚠️ 前端添加引号格式化 |
---
# 3. Fund Offering Documents
![Fund Documents](./sources/screenshots/03_fund_offering_documents.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product`

**API Files**:
- Request: [product_request.json](./sources/api_responses/product_request.json)
- Response: [product_response.json](./sources/api_responses/product_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Fund offering documents | Fund offering documents | data.fundOfferingDocuments.sectionTitle | string | 完全匹配 |
| Action Link | Read the documents | Read the documents | data.fundOfferingDocuments.actionLink | string | 完全匹配 |
| Document 1 | Factsheet / Key fact statement | Factsheet / Key fact statement | data.fundOfferingDocuments.documents[0].name | string | 完全匹配 |
| Document 2 | Interim report | Interim report | data.fundOfferingDocuments.documents[1].name | string | 完全匹配 |
| Document 3 | Annual report | Annual report | data.fundOfferingDocuments.documents[2].name | string | 完全匹配 |
| Document 4 | Prospectus | Prospectus | data.fundOfferingDocuments.documents[3].name | string | 完全匹配 |
| Document URLs | [Document links] | finDocURL_FACTSHEET, finDocURL_INTRMRPT, finDocURL_ANULRPT, finDocURL_PROSPECTUS | data.fundOfferingDocuments.documents[].url | array | ⚠️ API返回文档URL标识符，前端需转换为实际链接 |
--- 
# 4. Investment Objective
![Investment Objective](./sources/screenshots/04_investment_object.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product`

**API Files**:
- Request: [product_request.json](./sources/api_responses/product_request.json)
- Response: [product_response.json](./sources/api_responses/product_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Investment objective | Investment objective | data.investmentObjective.sectionTitle | string | 完全匹配 |
| Objective Description | The Sub-Fund aims to provide income with moderate capital growth through investment in a diversified portfolio of global assets that form part of sustainable investment strategies ("Sustainable Assets"). Through investment in Sustainable Assets, the Sub-Fund aims to invest in a portfolio with a higher weighted average environmental, social and governance ("ESG") score ("ESG Score") and lower weighted average carbon intensity ratings ("Carbon Intensity Ratings") than if it invested in an equivalent portfolio of standard market capitalisation indices. | The Sub-Fund aims to provide income with moderate capital growth through investment in a diversified portfolio of global assets that form part of sustainable investment strategies ("Sustainable Assets"). Through investment in Sustainable Assets, the Sub-Fund aims to invest in a portfolio with a higher weighted average environmental, social and governance ("ESG") score ("ESG Score") and lower weighted average carbon intensity ratings ("Carbon Intensity Ratings") than if it invested in an equivalent portfolio of standard market capitalisation indices. | data.fundBasicInfo.investmentObjective | string | 完全匹配 |
---
# 5. Performance Chart - Performance
![Performance Chart](./sources/screenshots/05_Performance_chart_performance_chart.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/advanceChart`

**API Files**:
- Request: [advance_chart_request.json](./sources/api_responses/advance_chart_request.json)
- Response: [advance_chart_response.json](./sources/api_responses/advance_chart_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Performance chart | Performance chart | data.chartData.sectionTitle | string | 完全匹配 |
| Tab Selection | Performance Chart (selected) \| Price Chart | Performance Chart | data.chartConfiguration.chartType | string | 前端处理选中状态 |
| Time Controls | 1M \| 3M \| 6M \| 1Y \| 3Y \| 5Y \| 10Y \| MAX | ["1M","3M","6M","YTD","1Y","3Y","5Y","10Y","MAX"] | data.chartConfiguration.displayPeriods | array | 前端显示为按钮组 |
| Selected Period | 1Y | 1Y | data.chartConfiguration.selectedPeriod | string | 当前选中的时间段 |
| Display Options | CUMULATIVE \| ANNUALISED \| INDEXED \| DAILY | Daily | data.chartConfiguration.displayFrequency | string | 前端处理显示选项 |
| Chart Data Source | NAV TO NAV, GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) | navPrice | data.chartData.dataType | string | 前端组合显示基金名称 |
| Chart Description | Performance line chart showing fund performance over time | Performance line chart | data.chartData.chartDescription | string | 前端生成描述 |
| Loading Status | Loading... | Loading... | data.chartData.loadingStatus | string | 前端状态显示 |
| **Chart Data Points** | | | | | |
| Data Point 1 | HCIT-HSBC GLOBAL S... 06/01/2025 NAV USD 8.84 | HCIT-HSBC GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) | data.chartData.dataPoints[0].fundName | string | 图表悬停显示完整基金名称 |
| Data Point 1 Date | 06/01/2025 | 2025-01-06 | data.chartData.dataPoints[0].date | string | 日期格式转换 |
| Data Point 1 NAV | USD 8.84 | 8.84 | data.chartData.dataPoints[0].navPrice | number | 前端添加货币符号 |
| Data Point 2 | HCIT-HSBC GLOBAL S... 13/12/2024 Growth +0.77% NAV USD 9.06 | HCIT-HSBC GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) | data.chartData.dataPoints[1].fundName | string | 图表悬停显示完整基金名称 |
| Data Point 2 Date | 13/12/2024 | 2024-12-13 | data.chartData.dataPoints[1].date | string | 日期格式转换 |
| Data Point 2 Growth | +0.77% | 0.77 | data.chartData.dataPoints[1].growthPercent | number | 前端添加+号和%符号 |
| Data Point 2 NAV | USD 9.06 | 9.06 | data.chartData.dataPoints[1].navPrice | number | 前端添加货币符号 |
| Disclaimer | Performance statistics are based on bid to bid/ NAV to NAV prices of the fund with dividend reinvested, in USD. | Performance statistics are based on bid to bid/ NAV to NAV prices of the fund with dividend reinvested, in USD. | data.chartData.disclaimer | string | 完全匹配 |
---
# 5.1. Performance Chart - Performance (Minute View)
![Performance Chart Minute](./sources/screenshots/05_minute_Performance_chart_performance_chart.jpg)

**Text Content in Image:**

**API Endpoint**: `https://quotespeed.morningstar.com/ra/snChartData` (Morningstar External API)

**API Files**:
- Request: [morningstar_chart_1m_request.json](./sources/api_responses/morningstar_chart_1m_request.json)
- Response: [morningstar_chart_1m_response.json](./sources/api_responses/morningstar_chart_1m_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Performance chart | Performance chart | data.chartData.sectionTitle | string | 完全匹配 |
| Tab Selection | Performance Chart (selected) \| Price Chart | Performance Chart | data.chartConfiguration.chartType | string | 前端处理选中状态 |
| Time Controls | 1M \| 3M \| 6M \| 1Y \| 3Y \| 5Y \| 10Y \| MAX | ["1M","3M","6M","YTD","1Y","3Y","5Y","10Y","MAX"] | data.chartConfiguration.displayPeriods | array | 前端显示为按钮组 |
| Selected Period | 1M | 1M | data.chartConfiguration.selectedPeriod | string | 当前选中的时间段 |
| Display Options | CUMULATIVE \| ANNUALISED \| INDEXED \| DAILY | Daily | data.chartConfiguration.displayFrequency | string | 前端处理显示选项 |
| Chart Data Source | NAV TO NAV, GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) | navPrice | data.chartData.dataType | string | 前端组合显示基金名称 |
| Chart Description | Performance line chart showing fund performance over time with minute-level detail | Performance line chart | data.chartData.chartDescription | string | 前端生成描述 |
| **Chart Data Points** | | | | | |
| Data Point Start | HCIT-HSBC GLOBAL S... 08/08/2024 NAV USD 9.267 | HCIT-HSBC GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) | data.chartData.dataPoints[0].fundName | string | 图表悬停显示完整基金名称 |
| Data Point Start Date | 08/08/2024 | 2024-08-08 | data.chartData.dataPoints[0].date | string | 日期格式转换 |
| Data Point Start NAV | USD 9.267 | 9.267 | data.chartData.dataPoints[0].navPrice | number | 前端添加货币符号 |
| Data Point Start Performance | 0.00% | 0.00 | data.chartData.dataPoints[0].performancePercent | number | 基准参考点 |
| Data Point End | HCIT-HSBC GLOBAL S... 08/09/2025 Performance +1.25% NAV USD 9.351 | HCIT-HSBC GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) | data.chartData.dataPoints[-1].fundName | string | 图表悬停显示完整基金名称 |
| Data Point End Date | 08/09/2025 | 2025-09-08 | data.chartData.dataPoints[-1].date | string | 日期格式转换 |
| Data Point End Performance | +1.25% | 1.25 | data.chartData.dataPoints[-1].performancePercent | number | 前端添加+号和%符号 |
| Data Point End NAV | USD 9.351 | 9.351 | data.chartData.dataPoints[-1].navPrice | number | 前端添加货币符号 |
| Chart Resolution | Daily data points for 1M view | daily | data.chartConfiguration.resolution | string | 1M视图使用日级别数据 |
| Fund Mapping - HSBC Code | U63166 | U63166 | data.fundMapping.hsbc_code | string | HSBC内部基金代码 |
| Fund Mapping - Morningstar Ticker | F000017RIG | F000017RIG | data.fundMapping.morningstar_ticker | string | Morningstar股票代码 |
| Fund Mapping - Morningstar PID | 0P0001MQYT | 0P0001MQYT | data.fundMapping.morningstar_pid | string | Morningstar产品ID |
| Disclaimer | Performance statistics are based on bid to bid/ NAV to NAV prices of the fund with dividend reinvested, in USD. | Performance statistics are based on bid to bid/ NAV to NAV prices of the fund with dividend reinvested, in USD. | data.chartData.disclaimer | string | 完全匹配 |
---
# 6. Performance Chart - Price Chart
![Price Chart](./sources/screenshots/06_Performance_chart_pine_chart.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/advanceChart`

**API Files**:
- Request: [advance_chart_request.json](./sources/api_responses/advance_chart_request.json)
- Response: [advance_chart_response.json](./sources/api_responses/advance_chart_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Performance chart | Performance chart | data.chartData.sectionTitle | string | 完全匹配 |
| Tab Selection | Performance Chart \| Price Chart (selected) | Price Chart | data.chartConfiguration.chartType | string | 前端处理选中状态 |
| Time Controls | 1M \| 3M \| 6M \| 1Y \| 3Y \| 5Y \| 10Y \| MAX | ["1M","3M","6M","YTD","1Y","3Y","5Y","10Y","MAX"] | data.chartConfiguration.displayPeriods | array | 前端显示为按钮组 |
| Selected Period | 1Y | 1Y | data.chartConfiguration.selectedPeriod | string | 当前选中的时间段 |
| Display Options | CUMULATIVE \| ANNUALISED \| INDEXED \| DAILY | Daily | data.chartConfiguration.displayFrequency | string | 前端处理显示选项 |
| Chart Data Source | NAV TO NAV, GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) | navPrice | data.chartData.dataType | string | 前端组合显示基金名称 |
| Price Range | Approximately 8.20 - 9.40 USD | [8.485, 9.351] | data.chartData.priceData[].navPrice | array | 前端计算最小最大值 |
| **Chart Data Points** | | | | | |
| Data Point 1 | HCIT-HSBC GLOBAL S... 06/01/2025 NAV USD 8.84 | HCIT-HSBC GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) | data.chartData.dataPoints[0].fundName | string | 图表悬停显示完整基金名称 |
| Data Point 1 Date | 06/01/2025 | 2025-01-06 | data.chartData.dataPoints[0].date | string | 日期格式转换 |
| Data Point 1 NAV | USD 8.84 | 8.84 | data.chartData.dataPoints[0].navPrice | number | 前端添加货币符号 |
| Data Point 2 | HCIT-HSBC GLOBAL S... 13/12/2024 Growth +0.77% NAV USD 9.06 | HCIT-HSBC GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) | data.chartData.dataPoints[1].fundName | string | 图表悬停显示完整基金名称 |
| Data Point 2 Date | 13/12/2024 | 2024-12-13 | data.chartData.dataPoints[1].date | string | 日期格式转换 |
| Data Point 2 Growth | +0.77% | 0.77 | data.chartData.dataPoints[1].growthPercent | number | 前端添加+号和%符号 |
| Data Point 2 NAV | USD 9.06 | 9.06 | data.chartData.dataPoints[1].navPrice | number | 前端添加货币符号 |
| Disclaimer | Performance statistics are based on bid to bid/ NAV to NAV prices of the fund with dividend reinvested, in USD. | Performance statistics are based on bid to bid/ NAV to NAV prices of the fund with dividend reinvested, in USD. | data.chartData.disclaimer | string | 完全匹配 |
---
# 7. Performance - Period Returns
![Period Returns](./sources/screenshots/07_Performance_Period_returns.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundQuoteSummary`

**API Files**:
- Request: [fund_quote_summary_request.json](./sources/api_responses/fund_quote_summary_request.json)
- Response: [fund_quote_summary_response.json](./sources/api_responses/fund_quote_summary_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Performance | Performance | data.performance.sectionTitle | string | 完全匹配 |
| Tab Selection | Period returns (selected) \| Calendar returns | Period returns | data.performance.selectedTab | string | 前端处理选中状态 |
| Last Updated | Updated as of 05 Sep 2025 | Updated as of 08 Sep 2025 | data.performance.updatedAsOf | string | ⚠️ 日期实时更新 |
| 1 month | +1.42% | +1.25% | data.performance.periodReturns.1month | string | ⚠️ 实时数据变化 |
| 3 month | +2.98% | +3.50% | data.performance.periodReturns.3month | string | ⚠️ 实时数据变化 |
| 6 month | +5.78% | +6.66% | data.performance.periodReturns.6month | string | ⚠️ 实时数据变化 |
| 1 year | +6.51% | +7.01% | data.performance.periodReturns.1year | string | ⚠️ 实时数据变化 |
| 3 year | +8.00% | +8.16% | data.performance.periodReturns.3year | string | ⚠️ 实时数据变化 |
| 5 year | - | "-" | data.performance.periodReturns.5year | string | ⚠️ 前端显示为"-" |
| 10 year | - | "-" | data.performance.periodReturns.10year | string | ⚠️ 前端显示为"-" |
| Disclaimer | Returns for periods 1 year or above are annualised returns. Returns for periods below 1 year are cumulative returns. | Returns for periods 1 year or above are annualised returns. Returns for periods below 1 year are cumulative returns. | data.performance.note | string | 完全匹配 |
---
# 8. Performance - Calendar Returns
![Calendar Returns](./sources/screenshots/08_Performance_Calendarr_returns.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundQuoteSummary`

**API Files**:
- Request: [fund_quote_summary_request.json](./sources/api_responses/fund_quote_summary_request.json)
- Response: [fund_quote_summary_response.json](./sources/api_responses/fund_quote_summary_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Performance | Performance | data.performance.sectionTitle | string | 完全匹配 |
| Tab Selection | Period returns \| Calendar returns (selected) | Calendar returns | data.performance.selectedTab | string | 前端处理选中状态 |
| Last Updated | Updated as of 05 Sep 2025 | Updated as of 08 Sep 2025 | data.performance.updatedAsOf | string | ⚠️ 日期实时更新 |
| 2025 | +6.51% | +7.01% | data.performance.calendarReturns.2025 | string | ⚠️ 实时数据变化 |
| 2024 | +9.89% | +9.89% | data.performance.calendarReturns.2024 | string | 完全匹配 |
| 2023 | +7.60% | +7.60% | data.performance.calendarReturns.2023 | string | 完全匹配 |
| 2022 | -8.40% | -8.40% | data.performance.calendarReturns.2022 | string | 完全匹配 |
| Note | The 2022 return only represent the fund's performance from its inception date to the end of the inception year. | The 2022 return only represent the fund's performance from its inception date to the end of the inception year. | data.performance.inceptionNote | string | 完全匹配 |
---
# 9. Fund Price History Enquiry
![Price History](./sources/screenshots/09_Fund_price_history_enquiry.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/quoteDetail`

**API Files**:
- Request: [quote_detail_request.json](./sources/api_responses/quote_detail_request.json)
- Response: [quote_detail_response.json](./sources/api_responses/quote_detail_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Fund price history enquiry | Fund price history enquiry | data.priceHistory.sectionTitle | string | 完全匹配 |
| Period Selection | Daily (selected) | Daily | data.priceHistory.selectedPeriod | string | 前端处理选中状态 |
| From Date | 09 Sep 2022 | 2022-09-09 | data.priceHistory.fromDate | string | 日期格式转换 |
| To Date | 09 Sep 2025 | 2025-09-09 | data.priceHistory.toDate | string | 日期格式转换 |
| Action Button | Enquiry | Enquiry | data.priceHistory.actionButton | string | 完全匹配 |
| Table Header | Date \| NAV | Date \| NAV | data.priceHistory.tableHeader | string | 完全匹配 |
| Sample Data 1 | 2025-09-05: 9.307 | 9.307 | data.priceHistory[0].price | number | 前端组合显示日期和价格 |
| Sample Data 2 | 2025-09-04: 9.281 | 9.281 | data.priceHistory[1].price | number | 前端组合显示日期和价格 |
| Sample Data 3 | 2025-09-03: 9.257 | 9.257 | data.priceHistory[2].price | number | 前端组合显示日期和价格 |
| Historical Dates | [Date array] | ["2025-09-08", "2025-09-05", "2025-09-04", "2025-09-03", "2025-09-02"] | data.priceHistory[].date | array | 历史日期数组 |
---
# 10. Holdings Diversification
![Holdings Diversification](./sources/screenshots/10_Holdings_diversification.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/holdingAllocation`

**API Files**:
- Request: [holding_allocation_request.json](./sources/api_responses/holding_allocation_request.json)
- Response: [holding_allocation_response.json](./sources/api_responses/holding_allocation_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Holdings diversification | Holdings diversification | data.holdingsDiversification.sectionTitle | string | 完全匹配 |
| Last Updated | Updated as of 31 Aug 2025 | Updated as of 31 Aug 2025 | data.holdingsDiversification.lastUpdatedDisplay | string | 完全匹配 |
| **Equity Holdings Sector Allocation** | | | | | |
| Technology | 10.28% | 10.28% | data.equityHoldingsSectorAllocation[0].percentage | string | 完全匹配 |
| Financial Services | 5.36% | 5.36% | data.equityHoldingsSectorAllocation[1].percentage | string | 完全匹配 |
| Real Estate | 4.45% | 4.45% | data.equityHoldingsSectorAllocation[2].percentage | string | 完全匹配 |
| Healthcare | 4.30% | 4.30% | data.equityHoldingsSectorAllocation[3].percentage | string | 完全匹配 |
| Industrials | 4.03% | 4.03% | data.equityHoldingsSectorAllocation[4].percentage | string | 完全匹配 |
| **Equity Holdings Geographical Allocation** | | | | | |
| United States | 21.13% | 21.13% | data.equityHoldingsGeographicalAllocation[0].percentage | string | 完全匹配 |
| Eurozone | 6.45% | 6.45% | data.equityHoldingsGeographicalAllocation[1].percentage | string | 完全匹配 |
| Asia - Emerging | 2.62% | 2.62% | data.equityHoldingsGeographicalAllocation[2].percentage | string | 完全匹配 |
| **Bond Holdings Sector Allocation** | | | | | |
| Government | 30.24% | 30.24% | data.bondHoldingsSectorAllocation[0].percentage | string | 完全匹配 |
| Corporate | 28.01% | 28.01% | data.bondHoldingsSectorAllocation[1].percentage | string | 完全匹配 |
| **Bond Holdings Geographical Allocation** | | | | | |
| Eurozone | 16.47% | 16.47% | data.bondHoldingsGeographicalAllocation[0].percentage | string | 完全匹配 |
| United States | 13.89% | 13.89% | data.bondHoldingsGeographicalAllocation[1].percentage | string | 完全匹配 |
---
# 11. Top 10 Holdings
![Top 10 Holdings](./sources/screenshots/11_top_10_holdings.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/topTenHoldings`

**API Files**:
- Request: [top_ten_holdings_request.json](./sources/api_responses/top_ten_holdings_request.json)
- Response: [top_ten_holdings_response.json](./sources/api_responses/top_ten_holdings_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Top 10 holdings | Top 10 holdings | data.topTenHoldings.sectionTitle | string | 完全匹配 |
| Last Updated | Updated as of 31 Aug 2025 | Updated as of 31 Aug 2025 | data.updatedAsOf | string | 完全匹配 |
| Holding 1 | HSBC GIF Global Eq Qual Inc ZQ1: USD 11.48M (21.59%) | HSBC GIF Global Eq Qual Inc ZQ1 | data.topTenHoldings[0].holding | string | 前端组合显示市值和百分比 |
| Holding 2 | HSBC GIF Global High Yld ESG Bd ZQ1: USD 9.15M (17.22%) | HSBC GIF Global High Yld ESG Bd ZQ1 | data.topTenHoldings[1].holding | string | 前端组合显示市值和百分比 |
| Holding 3 | HSBC GIF Global Green Bond ZQ1: USD 6.91M (13.01%) | HSBC GIF Global Green Bond ZQ1 | data.topTenHoldings[2].holding | string | 前端组合显示市值和百分比 |
| Holding 4 | HSBC GIF GEM ESG Local Debt ZQ1USD: USD 6.49M (12.21%) | HSBC GIF GEM ESG Local Debt ZQ1USD | data.topTenHoldings[3].holding | string | 前端组合显示市值和百分比 |
| Holding 5 | HSBC GIF Glb Sust Lg Trm Div ZM2 USD: USD 5.48M (10.30%) | HSBC GIF Glb Sust Lg Trm Div ZM2 USD | data.topTenHoldings[4].holding | string | 前端组合显示市值和百分比 |
| Market Value 1 | USD 11.48M | USD 11.48M | data.topTenHoldings[0].marketValue | string | 完全匹配 |
| Percentage 1 | 21.59% | 21.59% | data.topTenHoldings[0].percentOfNetAsset | string | 完全匹配 |
| Total Percentage | % of asset in Top 10 holdings: 98.77% | 98.77% | data.totalPercentInTopTen | string | 前端添加描述文字 |
---
# 12. Other Fund Classes
![Other Fund Classes](./sources/screenshots/12_Other_fund_classes_available_with_HSBC.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product`

**API Files**:
- Request: [product_request.json](./sources/api_responses/product_request.json)
- Response: [product_response.json](./sources/api_responses/product_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Other fund classes available with HSBC | Other fund classes available with HSBC | data.otherFundClasses.sectionTitle | string | 完全匹配 |
| Fund Class 1 | U63170 (AM3-EURH-MD-C) | U63170 (AM3-EURH-MD-C) | data.otherFundClasses.fundClasses[0].displayName | string | 完全匹配 |
| Fund Class 2 | U63167 (AM2-HKD-MD-C) | U63167 (AM2-HKD-MD-C) | data.otherFundClasses.fundClasses[1].displayName | string | 完全匹配 |
| Fund Class 3 | U63172 (AM3-RMBH-MD-C) | U63172 (AM3-RMBH-MD-C) | data.otherFundClasses.fundClasses[2].displayName | string | 完全匹配 |
| Fund Class 4 | U63171 (AM3-GBPH-MD-C) | U63171 (AM3-GBPH-MD-C) | data.otherFundClasses.fundClasses[3].displayName | string | 完全匹配 |
| Fund Class 5 | U63168 (AM3-AUDH-MD-C) | U63168 (AM3-AUDH-MD-C) | data.otherFundClasses.fundClasses[4].displayName | string | 完全匹配 |
| Fund Class 6 | U63169 (AM3-CADH-MD-C) | U63169 (AM3-CADH-MD-C) | data.otherFundClasses.fundClasses[5].displayName | string | 完全匹配 |
| Fund Codes | [U63170, U63167, U63172, U63171, U63168, U63169] | ["U63170", "U63167", "U63172", "U63171", "U63168", "U63169"] | data.otherFundClasses.fundClasses[].fundCode | array | 基金代码数组 |
---
# 13. Fund Profile
![Fund Profile](./sources/screenshots/13_Fund_profile.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product`

**API Files**:
- Request: [product_request.json](./sources/api_responses/product_request.json)
- Response: [product_response.json](./sources/api_responses/product_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Fund profile | Fund profile | data.fundProfile.sectionTitle | string | 完全匹配 |
| Fund House | HSBC Global Asset Management (Hong Kong) Limited | HSBC Global Asset Management (Hong Kong) Limited | data.fundProfile.fundHouse | string | 完全匹配 |
| Fund Class Inception Date | 21 Jan 2022 | 21 Jan 2022 | data.fundProfile.fundClassInceptionDateDisplay | string | 完全匹配 |
| Fund Class Currency | USD | USD | data.fundProfile.fundClassCurrency | string | 完全匹配 |
| Fund Size (Million) | USD 54.81 (as of 05 Sep 2025) | USD 53.39 Million | data.fundProfile.fundSizeDisplay | string | ⚠️ 实时数据变化 |
| HSBC Risk Level | "2" | 2 | data.fundProfile.hsbcRiskLevel | number | ⚠️ 前端添加引号格式化 |
| ISIN Code | HK0000748027 | HK0000748027 | data.fundProfile.isinCode | string | 完全匹配 |
| 52 Week Range | USD 8.38 - USD 9.355 (as of 05 Sep 2025) | USD 8.38 - USD 9.355 | data.fundProfile.weekRange52.display | string | ⚠️ 实时数据变化 |
| Fund Manager 1 | Jimmy Choong (21 Jan 2022) | Jimmy Choong (21 Jan 2022) | data.fundProfile.fundManagers[0].name + data.fundProfile.fundManagers[0].startDateDisplay | string | 组合显示 |
| Fund Manager 2 | Gloria Jing (01 Jul 2023) | Gloria Jing (01 Jul 2023) | data.fundProfile.fundManagers[1].name + data.fundProfile.fundManagers[1].startDateDisplay | string | 组合显示 |
---
# 14. Investment Strategy
![Investment Strategy](./sources/screenshots/14_Investment_strategy.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product`

**API Files**:
- Request: [product_request.json](./sources/api_responses/product_request.json)
- Response: [product_response.json](./sources/api_responses/product_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Investment strategy | Investment strategy | data.investmentStrategy.sectionTitle | string | 完全匹配 |
| HSBC Investment Category | Multi-Asset - Global | Multi-Asset - Global | data.fundBasicInfo.investmentCategory | string | 完全匹配 |
| Investment Style | Blend | Blend | data.fundBasicInfo.investmentStyle | string | 完全匹配 |
| Investment Instrument | Stock, Bond, Cash, Others | Stock, Bond, Cash, Others | data.fundBasicInfo.investmentInstrument | string | 完全匹配 |
| Note | Investment style is only applicable for funds that invest in equities. | Investment style is only applicable for funds that invest in equities. | data.investmentStrategy.note | string | 完全匹配 |
---
# 15. Dividend Information
![Dividend Information](./sources/screenshots/15_Dividend_information.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product`

**API Files**:
- Request: [product_request.json](./sources/api_responses/product_request.json)
- Response: [product_response.json](./sources/api_responses/product_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Dividend information | Dividend information | data.dividendInformation.sectionTitle | string | 完全匹配 |
| Last Updated | Updated as of 31 Aug 2025 | Updated as of 31 Aug 2025 | data.dividendInformation.lastUpdatedDisplay | string | 完全匹配 |
| Target Dividend Distribution Frequency | Monthly | Monthly | data.dividendInformation.targetDividendDistributionFrequency | string | 完全匹配 |
| Dividend Yield | 3.99% | 3.99 | data.dividendInformation.dividendYield | number | ⚠️ 前端添加%符号 |
| Last Dividend Paid (per unit) | USD 0.03160 | USD 0.03160 | data.dividendInformation.lastDividendPaidDisplay | string | 包含货币格式化 |
| Last Ex-dividend Date | 29 Aug 2025 | 29 Aug 2025 | data.dividendInformation.lastExDividendDateDisplay | string | 完全匹配 |
| Disclaimer | Dividend yield is the dividend amount declared over the past twelve months as a percentage of the last month-end fund unit price, show in 2 decimal places. The amount of dividend may not be guaranteed by Fund House. | Dividend yield is the dividend amount declared over the past twelve months as a percentage of the last month-end fund unit price, show in 2 decimal places. The amount of dividend may not be guaranteed by Fund House. | data.dividendInformation.note | string | 完全匹配 |
# 16. Risk Return Profile
![Risk Return Profile](./sources/screenshots/16_Risk_return_profile.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundQuoteSummary`

**API Files**:
- Request: [fund_quote_summary_request.json](./sources/api_responses/fund_quote_summary_request.json)
- Response: [fund_quote_summary_response.json](./sources/api_responses/fund_quote_summary_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Risk return profile | Risk return profile | data.riskReturnProfile.sectionTitle | string | 完全匹配 |
| Time Period | 3 year (selected) | 3 year | data.riskReturnProfile.selectedPeriod | string | 选中状态在前端处理 |
| Last Updated | Updated as of 31 Aug 2025 | Updated as of 31 Aug 2025 | data.riskReturnProfile.lastUpdatedDisplay | string | 完全匹配 |
| Annualised Return | 7.78% | 7.78 | data.riskReturnProfile.annualisedReturn | number | ⚠️ 前端添加%符号 |
| Standard Deviation | 8.796% | 8.796 | data.riskReturnProfile.standardDeviation | number | ⚠️ 前端添加%符号 |
| Sharpe Ratio | "0.343" | 0.343 | data.riskReturnProfile.sharpeRatio | number | ⚠️ 前端添加引号格式化 |
| Alpha | "1.29" | 1.29 | data.riskReturnProfile.alpha | number | ⚠️ 前端添加引号格式化 |
| Beta | "1.5" | 1.5 | data.riskReturnProfile.beta | number | ⚠️ 前端添加引号格式化 |
| Calculation Note | Fund statistics are calculated by using 3 years historical data, and are not calculated for fund which is less than 3 years old. | Fund statistics are calculated by using 3 years historical data, and are not calculated for fund which is less than 3 years old. | data.riskReturnProfile.calculationNote | string | 完全匹配 |
| Sharpe Ratio Note | Morningstar Asia Limited used the USTREAS T-Bill Auction Ave 3 Mon as the risk free factor for calculation. | Sharpe ratio: Morningstar Asia Limited used the USTREAS T-Bill Auction Ave 3 Mon as the risk free factor for calculation. | data.riskReturnProfile.sharpeRatioNote | string | ⚠️ API包含前缀"Sharpe ratio:" |
| Alpha & Beta Note | Morningstar Asia Limited used Morningstar EAA USD Cau Tgt Alloc NR USD as the benchmark index for calculation. | Alpha & Beta: Morningstar Asia Limited used Morningstar EAA USD Cau Tgt Alloc NR USD as the benchmark index for calculation. | data.riskReturnProfile.alphaBetaNote | string | ⚠️ API包含前缀"Alpha & Beta:" |

# 17. Rating
![Rating](./sources/screenshots/17_Rating.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundQuoteSummary`

**API Files**:
- Request: [fund_quote_summary_request.json](./sources/api_responses/fund_quote_summary_request.json)
- Response: [fund_quote_summary_response.json](./sources/api_responses/fund_quote_summary_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Rating | Rating | data.rating.sectionTitle | string | 完全匹配 |
| Morningstar Rating | ★★★★☆ (4 out of 5 stars) as of 31 Aug 2025 | 4 out of 5 stars | data.rating.morningstarRating.starDisplay | string | ⚠️ 前端转换为星号显示 |
| 1 Year Quartile Ranking | 2nd | 2nd | data.rating.quartileRanking.oneYear.quartileDisplay | string | 完全匹配 |
| 3 Year Quartile Ranking | 1st | 1st | data.rating.quartileRanking.threeYear.quartileDisplay | string | 完全匹配 |
| 5 Year Quartile Ranking | "-" | null | data.rating.quartileRanking.fiveYear.quartile | null | ⚠️ 前端显示为"-" |

# 18. Fees and Charges
![Fees and Charges](./sources/screenshots/18_Fees_and_charges.jpg)

**Text Content in Image:**

**API Endpoint**: `/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product`

**API Files**:
- Request: [product_request.json](./sources/api_responses/product_request.json)
- Response: [product_response.json](./sources/api_responses/product_response.json)

| Element | Content | API Value | API JSON Path | Data Type | Notes |
|---------|---------|-----------|---------------|-----------|-------|
| Section Title | Fees and charges | Fees and charges | data.feesAndCharges.sectionTitle | string | 完全匹配 |
| HSBC Initial Charge | 3.00% | 3.00 | data.feesAndCharges.hsbcInitialCharge | number | ⚠️ 前端添加%符号 |
| Annual Management Fee (Maximum) | 1.25% | 1.25 | data.feesAndCharges.annualManagementFeeMaximum | number | ⚠️ 前端添加%符号 |
| HSBC Minimum Investment Amount | USD 1,000.00 | USD 1,000.00 | data.feesAndCharges.hsbcMinimumInvestmentAmountDisplay | string | 包含货币格式化 |
| Expense Ratio | 1.58% (as of 31 Mar 2025) | 1.58% as of 31 Mar 2025 | data.feesAndCharges.expenseRatioDisplay + data.feesAndCharges.expenseRatioAsOfDisplay | string | 组合显示 |
| Switching Charge Note | Switching transactions placed through HSBC are subject to 1% switching charge which is charged by HSBC. | Switching transactions placed through HSBC are subject to 1% switching charge which is charged by HSBC. | data.feesAndCharges.switchingChargeNote | string | 完全匹配 |
| Disclaimer | The listed information may not cover all the fees and charges imposed on a fund. Please refer to the respective offering documents of the fund for details of relevant fees and charges. | The listed information may not cover all the fees and charges imposed on a fund. Please refer to the respective offering documents of the fund for details of relevant fees and charges. | data.feesAndCharges.generalNote | string | 完全匹配 |

---

## API Files Index

### 📁 API Request & Response Files

All API request and response files are stored in: `./sources/api_responses/`

#### **Core API Endpoints**

| API Endpoint | Request File | Response File | Used in Modules |
|-------------|--------------|---------------|-----------------|
| `/v0/amh/ut/product` | [product_request.json](./sources/api_responses/product_request.json) | [product_response.json](./sources/api_responses/product_response.json) | [1. Fund Summary Information](#1-fund-summary-information)<br/>[2. Fund Risk Level](#2-fund-risk-level)<br/>[3. Fund Offering Documents](#3-fund-offering-documents)<br/>[4. Investment Objective](#4-investment-objective)<br/>[12. Other Fund Classes](#12-other-fund-classes)<br/>[13. Fund Profile](#13-fund-profile)<br/>[14. Investment Strategy](#14-investment-strategy)<br/>[15. Dividend Information](#15-dividend-information)<br/>[18. Fees and Charges](#18-fees-and-charges) |
| `/v0/wmds/quoteDetail` | [quote_detail_request.json](./sources/api_responses/quote_detail_request.json) | [quote_detail_response.json](./sources/api_responses/quote_detail_response.json) | [9. Fund Price History Enquiry](#9-fund-price-history-enquiry) |
| `/v0/wmds/fundQuoteSummary` | [fund_quote_summary_request.json](./sources/api_responses/fund_quote_summary_request.json) | [fund_quote_summary_response.json](./sources/api_responses/fund_quote_summary_response.json) | [7. Performance - Period Returns](#7-performance---period-returns)<br/>[8. Performance - Calendar Returns](#8-performance---calendar-returns)<br/>[16. Risk Return Profile](#16-risk-return-profile)<br/>[17. Rating](#17-rating) |
| `/v0/wmds/holdingAllocation` | [holding_allocation_request.json](./sources/api_responses/holding_allocation_request.json) | [holding_allocation_response.json](./sources/api_responses/holding_allocation_response.json) | [10. Holdings Diversification](#10-holdings-diversification) |
| `/v0/wmds/topTenHoldings` | [top_ten_holdings_request.json](./sources/api_responses/top_ten_holdings_request.json) | [top_ten_holdings_response.json](./sources/api_responses/top_ten_holdings_response.json) | [11. Top 10 Holdings](#11-top-10-holdings) |
| `/v0/wmds/advanceChart` | [advance_chart_request.json](./sources/api_responses/advance_chart_request.json) | [advance_chart_response.json](./sources/api_responses/advance_chart_response.json) | [5. Performance Chart - Performance](#5-performance-chart---performance)<br/>[6. Performance Chart - Price Chart](#6-performance-chart---price-chart) |
| `https://quotespeed.morningstar.com/ra/snChartData` | [morningstar_chart_1m_request.json](./sources/api_responses/morningstar_chart_1m_request.json) | [morningstar_chart_1m_response.json](./sources/api_responses/morningstar_chart_1m_response.json) | [5.1. Performance Chart - Performance (Minute View)](#51-performance-chart---performance-minute-view) |

#### **API Mapping Summary**
- **Complete API Mapping Summary**: [api_mapping_summary.json](./sources/api_responses/api_mapping_summary.json)
- **Total API Endpoints**: 7 (6 HSBC Internal + 1 Morningstar External)
- **Total Request/Response Files**: 14
- **Mapped Modules**: 17 out of 19 (89%)
- **Total Mapped Fields**: 183+

#### **Key Precision Handling Notes**
- **Real-time Data**: Price, performance returns update in real-time
- **Format Processing**: Frontend adds currency symbols, percentage signs, quotes
- **Display Combination**: Some fields combine multiple API values for display
- **Null Handling**: API null values displayed as "-" in frontend
- **Date Format**: API uses ISO format (2022-09-09), frontend displays localized format (09 Sep 2022)
- **Document URLs**: API returns URL identifiers, frontend converts to actual links
- **External API Integration**: Morningstar API requires fund code mapping (HSBC ↔ Morningstar)
- **Third-party Data Sync**: External API data may have different update frequencies

#### **Unmapped Modules (2 remaining)**
The following modules do not have specific API mappings and may be static content or use different data sources:
- **Module 19**: Additional content (to be confirmed)
- **Module 20**: Additional content (to be confirmed)

**Note**: Module 14 (Investment Strategy) has been successfully mapped to `/v0/amh/ut/product` API.

#### **API Coverage Summary**
- **Primary Product API** (`/v0/amh/ut/product`): 9 modules - Core fund information, documents, strategy
- **Quote Detail API** (`/v0/wmds/quoteDetail`): 1 module - Price history and real-time quotes
- **Fund Quote Summary API** (`/v0/wmds/fundQuoteSummary`): 4 modules - Performance, ratings, risk metrics
- **Holdings APIs** (`/v0/wmds/holdingAllocation`, `/v0/wmds/topTenHoldings`): 2 modules - Portfolio composition
- **Chart Data API** (`/v0/wmds/advanceChart`): 2 modules - Performance and price charts
- **External Morningstar API** (`https://quotespeed.morningstar.com/ra/snChartData`): 1 module - Third-party chart data integration

---
