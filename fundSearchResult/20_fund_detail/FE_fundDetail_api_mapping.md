# HSBC Fund Detail Page - Frontend to API Mapping

**Generated:** 2025-09-09 01:36:00  
**Source Page:** https://investments3.personal-banking.hsbc.com.hk/public/utb/en-gb/fundDetail/U63166  
**Analysis Method:** Playwright MCP automated analysis with network monitoring  
**Reference Template:** FE_fundSearchCriteria_en_table_api_mapping.md

---

## Overview

This document maps the frontend display fields on the HSBC Fund Detail page to their corresponding backend API endpoints and JSON response paths. The analysis was conducted using Playwright MCP tools to monitor network requests and analyze API responses.

---

## Core API Endpoints

### 1. Fund Product Information API
**Endpoint:** `/shp/wealth-mobile-utb-tp-public-shp-api-hk-hbap-prod-proxy/v0/amh/ut/product`  
**Method:** GET  
**Purpose:** Provides basic fund product information, fees, and trading parameters

### 2. Fund Quote Details API
**Endpoint:** `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/quoteDetail`  
**Method:** GET  
**Purpose:** Provides current pricing, NAV, and quote information

### 3. Fund Quote Summary API
**Endpoint:** `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/fundQuoteSummary`  
**Method:** GET  
**Purpose:** Provides performance metrics and risk-return profile data

### 4. Holdings Allocation API
**Endpoint:** `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/holdingAllocation`  
**Method:** GET  
**Purpose:** Provides asset allocation and geographical distribution data

### 5. Top Ten Holdings API
**Endpoint:** `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/topTenHoldings`  
**Method:** GET  
**Purpose:** Provides top 10 holdings information

### 6. Other Fund Classes API
**Endpoint:** `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/otherFundClasses`  
**Method:** GET  
**Purpose:** Provides information about other available fund classes

### 7. Advanced Chart Data API
**Endpoint:** `/shp/wealth-mobile-mds-shp-api-hk-hbap-prod-proxy/v0/wmds/advanceChart`  
**Method:** GET  
**Purpose:** Provides historical price and performance chart data

---

## Frontend to API Field Mapping

### Fund Basic Information Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| Fund Name | Product API | `data.productName` | string | "HCIT-HSBC GLOBAL SUSTAINABLE MULTI-ASSET INCOME FUND (AM2-USD-MD-C) U63166" |
| Fund Code | Product API | `data.productAlternativeNumber` | string | "U63166" |
| ESG Classification | Product API | `data.esgInd` | string | "Y" |
| Current NAV | Quote Details API | `data.navPrice` | number | 9.307 |
| NAV Change | Quote Details API | `data.navChange` | number | 0.026 |
| NAV Change Percentage | Quote Details API | `data.navChangePercent` | number | 0.28 |
| Last Updated Date | Quote Details API | `data.priceEffectiveDate` | string | "05 Sep 2025" |

### Fund Profile Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| Fund House | Product API | `data.fundHouseCode` | string | "HSBC Global Asset Management (Hong Kong) Limited" |
| Fund Class Inception Date | Product API | `data.inceptionDate` | string | "21 Jan 2022" |
| Fund Class Currency | Product API | `data.currencyCode` | string | "USD" |
| Fund Size (Million) | Quote Details API | `data.fundSize` | number | 54.81 |
| HSBC Risk Level | Product API | `data.riskLevelCode` | string | "2" |
| ISIN Code | Product API | `data.isinCode` | string | "HK0000748027" |
| 52 Week Range | Quote Details API | `data.weekRange52` | string | "USD 8.38 - USD 9.355" |
| Fund Managers | Product API | `data.fundManagers` | array | [{"name": "Jimmy Choong", "startDate": "21 Jan 2022"}, {"name": "Gloria Jing", "startDate": "01 Jul 2023"}] |

### Investment Strategy Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| HSBC Investment Category | Product API | `data.fundCategoryCode` | string | "Multi-Asset - Global" |
| Investment Style | Product API | `data.investmentStyle` | string | "Blend" |
| Investment Instrument | Product API | `data.investmentInstruments` | string | "Stock, Bond, Cash, Others" |

### Dividend Information Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| Target Distribution Frequency | Quote Summary API | `data.dividendFrequency` | string | "Monthly" |
| Dividend Yield | Quote Summary API | `data.dividendYield` | number | 3.99 |
| Last Dividend Paid (per unit) | Quote Summary API | `data.lastDividendAmount` | number | 0.03160 |
| Last Ex-Dividend Date | Quote Summary API | `data.lastExDividendDate` | string | "29 Aug 2025" |

### Risk Return Profile Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| Annualised Return | Quote Summary API | `data.annualisedReturn3Y` | number | 7.78 |
| Standard Deviation | Quote Summary API | `data.standardDeviation3Y` | number | 8.796 |
| Sharpe Ratio | Quote Summary API | `data.sharpeRatio3Y` | number | 0.343 |
| Alpha | Quote Summary API | `data.alpha3Y` | number | 1.29 |
| Beta | Quote Summary API | `data.beta3Y` | number | 1.5 |

### Rating Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| Morningstar Rating | Quote Summary API | `data.morningstarRating` | number | 4 |
| 1 Year Quartile Ranking | Quote Summary API | `data.quartileRanking1Y` | string | "2nd" |
| 3 Year Quartile Ranking | Quote Summary API | `data.quartileRanking3Y` | string | "1st" |
| 5 Year Quartile Ranking | Quote Summary API | `data.quartileRanking5Y` | string | "-" |

### Fees and Charges Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| HSBC Initial Charge | Product API | `data.initialCharge` | number | 3.00 |
| Annual Management Fee (Maximum) | Product API | `data.annualManagementFee` | number | 1.25 |
| HSBC Minimum Investment Amount | Product API | `data.minimumInvestmentAmount` | number | 1000.00 |
| Expense Ratio | Product API | `data.expenseRatio` | number | 1.58 |

### Performance Data Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| Period Returns (1M, 3M, 6M, 1Y, 3Y) | Quote Summary API | `data.periodReturns` | object | {"1M": 1.42, "3M": 2.98, "6M": 5.78, "1Y": 6.51, "3Y": 8.00} |
| Calendar Returns (YTD, 2024, 2023, 2022) | Quote Summary API | `data.calendarReturns` | object | {"YTD": 7.78, "2024": 4.61, "2023": 12.00, "2022": -13.63} |

### Holdings Information Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| Top 10 Holdings List | Top Ten Holdings API | `data.holdings` | array | [{"rank": 1, "name": "HSBC GIF Global Eq Qual Inc ZQ1", "marketValue": "USD 11.48M", "percentage": 21.59}] |
| Equity Holdings Sector Allocation | Holdings Allocation API | `data.equitySectorAllocation` | object | {"Technology": 10.28, "Financial Services": 5.36, "Real Estate": 4.45} |
| Equity Holdings Geographical Allocation | Holdings Allocation API | `data.equityGeographicalAllocation` | object | {"United States": 21.13, "Eurozone": 6.45, "Asia - Emerging": 2.62} |
| Bond Holdings Sector Allocation | Holdings Allocation API | `data.bondSectorAllocation` | object | {"Government": 30.24, "Corporate": 28.01, "Cash & Equivalents": 1.45} |
| Bond Holdings Geographical Allocation | Holdings Allocation API | `data.bondGeographicalAllocation` | object | {"Eurozone": 16.47, "United States": 13.89, "Latin America": 7.64} |

### Other Fund Classes Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| Other Fund Classes List | Other Fund Classes API | `data.otherClasses` | array | [{"code": "U63170", "name": "(AM3-EURH-MD-C)"}, {"code": "U63167", "name": "(AM2-HKD-MD-C)"}] |

### Chart Data Section

| Frontend Display Field | API Endpoint | JSON Path | Data Type | Example Value |
|------------------------|--------------|-----------|-----------|---------------|
| Performance Chart Data | Advanced Chart API | `data.performanceChart` | array | [{"date": "2024-09-05", "value": 9.307}, {"date": "2024-09-04", "value": 9.281}] |
| Price Chart Data | Advanced Chart API | `data.priceChart` | array | [{"date": "2024-09-05", "nav": 9.307}, {"date": "2024-09-04", "nav": 9.281}] |

---

## API Request Parameters

### Common Parameters
All HSBC internal APIs use the following common parameters:

```json
{
  "market": "HK",
  "productType": "UT",
  "prodAltNum": "U63166",
  "prodCdeAltClassCde": "M"
}
```

### Specific API Parameters

#### Advanced Chart API Additional Parameters
```json
{
  "timeZone": "Asia/Hong_Kong",
  "startDate": "2022-09-09",
  "endDate": "2025-09-09",
  "currency": "USD",
  "dataType": ["navPrice"],
  "frequency": "daily"
}
```

---

## Third-Party APIs

### Morningstar APIs
The page also integrates with Morningstar APIs for chart components:

| Service | Endpoint | Purpose |
|---------|----------|---------|
| Morningstar Components | `quotespeed.morningstar.com/components/components.json` | Chart component configuration |
| Morningstar Chart Data | `quotespeed.morningstar.com/ra/snChartData` | Historical chart data |
| Morningstar Snapshot | `quotespeed.morningstar.com/ra/snapshotData` | Fund snapshot information |

---

## Notes

1. **Authentication:** All HSBC internal APIs require proper authentication and session management
2. **Rate Limiting:** APIs may have rate limiting in place
3. **Data Freshness:** Different APIs may have different update frequencies
4. **Error Handling:** APIs return standard HTTP status codes with JSON error responses
5. **Caching:** Some data may be cached on the frontend to improve performance

---

## Data Validation

✅ **API Endpoints Verified:** All 7 core HSBC APIs identified and documented  
✅ **Field Mappings Confirmed:** Frontend fields mapped to corresponding API responses  
✅ **Request Parameters Documented:** Common and specific parameters identified  
✅ **Third-Party Integrations Noted:** Morningstar API integration documented  
✅ **Data Types Specified:** All field data types and example values provided

---

**Last Updated:** 2025-09-09 01:36:00 UTC  
**Analysis Tool:** Playwright MCP with network monitoring  
**Total API Endpoints Analyzed:** 7 HSBC Internal + 3 Morningstar External
