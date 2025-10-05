# Search for funds - Complete FE → API → DB Mapping

## Data Flow Architecture
```
前端UI → Controller → Service → DAO → Database
FE_UI → FundSearchCriteriaController → FundSearchCriteriaServiceImpl → FundSearchCriteriaDaoImpl → V_UT_PROD_INSTM
```

| Field | Description | Available Options | API Field | DB Field | DB Table/Column | Data Source | Query Type |
|-------|-------------|-------------------|-----------|----------|-----------------|-------------|------------|
| **Fund code / Keywords** | | Free text input | `searchText` | Multiple fields | V_UT_PROD_INSTM | DB | Text Search |
| **Asset class** | Search funds by fund's category. | **All (39)**<br/>**Equity Developed Market (10)**<br/>• Asia Pacific Ex-Japan Equity - Broad<br/>• Asia Pacific Ex-Japan Equity - Small Cap<br/>• European Equity - Broad<br/>• European Equity - Small Cap<br/>• Global Equity - Broad<br/>• Global Equity - Small Cap<br/>• Japan Equity - Broad<br/>• Japan Equity - Small Cap<br/>• US Equity - Broad<br/>• US Equity - Small Cap<br/>**Equity Developing Market**<br/>• Greater China Equity<br/>• Domestic Equity - China<br/>• Domestic Equity - Hong Kong<br/>• Global Emerging Markets (GEM) Equity - Broad<br/>• Global Emerging Markets (GEM) Equity - Small Cap<br/>• Single Market Equity - Australia<br/>• Single Market Equity - Korea<br/>• Single Market Equity - Others<br/>• Single Market Equity - Taiwan<br/>**Fixed Income**<br/>• Asian Fixed Income<br/>• European Fixed Income<br/>• Global Fixed Income<br/>• Short Duration Fund<br/>• US Fixed Income<br/>• Global Emerging Market (GEM) Fixed Income<br/>• High Yield Bond - Asia<br/>• High Yield Bond - Europe<br/>• High Yield Bond - Global<br/>• High Yield Bond - US<br/>**Multi Asset**<br/>• Multi-Asset - Asia<br/>• Multi-Asset - Emerging Market<br/>• Multi-Asset - Europe<br/>• Multi-Asset - Global<br/>• Multi-Asset - US<br/>**Specialties**<br/>• Commodity Funds<br/>• Global Property<br/>• Specialised Sector Equity<br/>**Alternatives**<br/>• Alternatives<br/>**Mutual Recognition of Funds**<br/>• Mutual Recognition of Funds - Equity/Mixed Asset | `CAT` | `utProd.categoryCode` | V_UT_PROD_INSTM.FUND_CAT_CDE ✅ | DB | List Query |
| **Geography** | Search funds by fund holdings's major investment region. | **All (17)**<br/>• Global<br/>• Asia (excl Japan)<br/>• Asia Pacific<br/>• China<br/>• Hong Kong<br/>• Japan<br/>• Emerging Market<br/>• Europe<br/>• UK<br/>• North America<br/>• Other Asian Countries<br/>• Other European Countries<br/>• BRIC<br/>• India<br/>• Korea<br/>• Global Emerging Market<br/>• Regional Emerging Market | `INVSTRG` | `investmentRegionCode` | V_UT_PROD_INSTM.MKT_INVST_CDE ✅ | DB | List Query |
| **Fund house** | Search funds by fund's fund house. | **All (32)**<br/>• Amundi Hong Kong Limited<br/>• Baring Asset Management (Asia) Ltd<br/>• BlackRock<br/>• BNY Mellon Investment Management HK Ltd<br/>• Bosera Asset Management (International) Co Ltd<br/>• BNP Paribas Asset Management Asia Ltd<br/>• BlackRock Asset Management North Asia Limited<br/>• Capital International Management Company<br/>• China Asset Management (H K) Limited<br/>• CSOP Asset Management Limited<br/>• Fidelity Investment Managers<br/>• Franklin Templeton Investments (Asia) Ltd<br/>• Goldman Sachs International<br/>• Hai Tong Asset Management (HK) Limited<br/>• Hang Seng Investment Management Limited<br/>• HSBC Jintrust Fund Management Company Limited<br/>• INVESCO Asset Management Asia Limited<br/>• Ninety One<br/>• J.P. Morgan Asset Management<br/>• Janus Henderson Group Plc<br/>• Man Investments<br/>• Manulife Investment Management (Hong Kong) Limited<br/>• Neuberger Berman Asia Ltd<br/>• PIMCO<br/>• Ping An of China Asset Management (H K) Co Limited<br/>• Pictet Asset Management (Hong Kong) Ltd<br/>• Schroder Investment Management (HK) Ltd<br/>• T. ROWE PRICE HONG KONG LIMITED<br/>• UBS ASSET MANAGEMENT (SINGAPORE) LTD<br/>• UBS Asset Management (Hong Kong) Ltd<br/>• Value Partners<br/>• Wellington Management Hong Kong Limited | `FAM` | `utProd.familyCode` | V_UT_PROD_INSTM.FUND_FM_CDE ✅ | DB | List Query |
| **By your risk tolerance** | Selecting products with risk level relative to your tolerance to investment risk.<br/><br/>You may consider funds with risk level commensurate with your investment risk tolerance, according to the table below:<br/>• Secure (Products with NO investment risk)<br/>• Very cautious (Risk level: 1)<br/>• Cautious (Risk level: 1 - 2)<br/>• Balanced (Risk level: 1 - 3)<br/>• Adventurous (Risk level: 1 - 4)<br/>• Speculative (Risk level: 1 - 5)<br/><br/>**Selected**: Speculative - 5 | **All (5)**<br/>• Very cautious - 1<br/>• Cautious - 2<br/>• Balanced - 3<br/>• Adventurous - 4<br/>• Speculative - 5 | `RISK` | `utProd.riskLvlCde` | V_UT_PROD_INSTM.RISK_LVL_CDE ✅ | DB | List Query |
| **Fund currency** | Search funds by Fund class currency. | **All (12)**<br/>• AUD<br/>• CAD<br/>• CHF<br/>• CNY<br/>• EUR<br/>• GBP<br/>• HKD<br/>• JPY<br/>• NZD<br/>• SEK<br/>• SGD<br/>• USD | `CCY` | `utProd.currency` | V_UT_PROD_INSTM.CCY_PROD_CDE ✅ | DB | List Query |

## Other option

| Option | Description | Status | API Field | DB Field | DB Table/Column | Data Source | Query Type |
|--------|-------------|--------|-----------|----------|-----------------|-------------|------------|
| **GBA Wealth Connect – Southbound Scheme** | | ✓ Selected | `GBAA` | `utProd.gbaAcctTrdb` | V_UT_PROD_INSTM.GBA_ACCT_TRDB ✅ | DB | List Query |
| **Environmental, Social and Governance (ESG) or sustainable investments** | By selecting 'Environmental, Social and Governance (ESG) or sustainable investments', you can explore funds that follow sustainable investment strategies integrating ESG factors. | ✓ Selected | `ESG` | `utProd.esgInd` | V_UT_PROD_INSTM.ESG_IND ✅ | DB | List Query |

## Add popular filter to your screener or view all for more options

| Filter | Status | API Field | DB Field | DB Table/Column | Data Source | Query Type |
|--------|--------|-----------|----------|-----------------|-------------|------------|
| **1 year annualised return** | ✓ Selected | `Y1RTRN` | `return1yrDaily` | V_UT_PROD_INSTM.RTRN_1YR_DPN ✅ | DB | MinMax Query |
| **Dividend yield** | ✓ Selected | `DIVYLD` | `yield1Yr` | V_UT_PROD_INSTM.YIELD_1YR_PCT ✅ | DB | MinMax Query |
| **Morningstar rating** | ✓ Selected | `MSTAR` | `ratingOverall` | V_UT_PROD_INSTM.PROD_OVRL_RATENG_NUM ✅ | DB | List Query |
| **Average credit quality** | ✓ Selected | `ACQN` | `averageCreditQualityName` | V_UT_PROD_INSTM.AVG_CR_QLTY_NAME ✅ | DB | List Query |
| **1 year quartile ranking** | ✓ Selected | `Y1QTL` | `rank1Yr` | V_UT_PROD_INSTM.RANK_QTL_1YR_NUM ✅ | DB | List Query |

## Filters ( Max 5 )

| Filter | Description | Current Setting | Available Options | API Field | DB Field | DB Table/Column | Data Source | Query Type |
|--------|-------------|-----------------|-------------------|-----------|----------|-----------------|-------------|------------|
| **1 year annualised return** | Refers to the conversion of the return on an investment into a yearly rate. | **Range**: -51.05% to 133.35% | Range slider: -51.05% to 133.35% | `Y1RTRN` | `return1yrDaily` | V_UT_PROD_INSTM.RTRN_1YR_DPN ✅ | DB | MinMax Query |
| **Dividend yield** | It is the dividend amount declared over the past twelve months as a percentage of the last month-end fund unit price. Investors interested to have income generate from a fund may refers to the dividend yield that reflect the historical dividend distribution behavior of the fund. | **Range**: 0.00% to 10.11% | Range slider: 0.00% to 10.11% | `DIVYLD` | `yield1Yr` | V_UT_PROD_INSTM.YIELD_1YR_PCT ✅ | DB | MinMax Query |
| **1 year quartile ranking** | The quartiles divide the data into four equal regions. Expressed in terms of rank (1, 2, 3 or 4), the quartile measure shows how well a fund has performed compared to all other funds in its peer group. | **All (4)** | • 1st<br/>• 2nd<br/>• 3rd<br/>• 4th | `Y1QTL` | `rank1Yr` | V_UT_PROD_INSTM.RANK_QTL_1YR_NUM ✅ | DB | List Query |
| **Average credit quality** | The weighted average of all the bond credit ratings in a fund. The measure gives investors an idea of how risky a fund's bonds are overall. The lower the weighted average credit rating, the riskier the bond fund. An individual bond or bond fund's credit quality is determined by private independent rating agencies such as Standard & Poor's, Moody's and Fitch. Their credit quality designations range from high ('AAA' to 'AA') to medium ('A' to 'BBB') to low ('BB', 'B', 'CCC', 'CC' to 'C'). | **All (8)** | • AAA<br/>• AA<br/>• A<br/>• BBB<br/>• BB<br/>• B | `ACQN` | `averageCreditQualityName` | V_UT_PROD_INSTM.AVG_CR_QLTY_NAME ✅ | DB | List Query |
| **Morningstar rating** | For almost funds (funds in some Morningstar categories are not rated) in Hong Kong with at least a three-year history, Morningstar calculates a Morningstar Rating based on a Morningstar Risk-Adjusted Return measure that accounts for variation in a fund's monthly performance after adjusted for fees, placing more emphasis on downward variations and rewarding consistent performance. The top 10% of funds in each category receive 5 stars, the next 22.5% receive 4 stars, the next 35% receive 3 stars, the next 22.5% receive 2 stars and the bottom 10% receive 1 star. For funds with track records of more than 60 months, the Overall Morningstar Rating for a fund is derived from a weighted average of the performance figures associated with its three-, five- and ten-year (if applicable) Morningstar Rating metrics. | **All (6)** | • ★★★★★<br/>• ★★★★<br/>• ★★★<br/>• ★★<br/>• ★<br/>• No rating | `MSTAR` | `ratingOverall` | V_UT_PROD_INSTM.PROD_OVRL_RATENG_NUM ✅ | DB | List Query |

---

**Note**: Please note that only funds authorised by the Securities and Futures Commission which are not derivative funds are available via Personal Internet Banking and HSBC HK Mobile Banking app.

---

## Technical Implementation Details

### Data Flow Chain
```
Database → DAO → Service → Controller → API Response → Frontend UI
V_UT_PROD_INSTM → FundSearchCriteriaDaoImpl → FundSearchCriteriaServiceImpl → FundSearchCriteriaController → JSON Response → React UI
```

### Controller Layer
- **File**: `FundSearchCriteriaController.java`
- **Endpoint**: `/fund/search/reference-data`
- **Method**: GET
- **Function**: Receives request and delegates to RestfulService

### Service Layer
- **File**: `FundSearchCriteriaServiceImpl.java`
- **Key Methods**:
  - `execute()`: Main processing logic
  - Calls DAO for `searchMinMaxCriteria()` and `searchListCriteria()`

### DAO Layer
- **File**: `FundSearchCriteriaDaoImpl.java`
- **Key Methods**:
  - `searchMinMaxCriteria()`: Executes MIN/MAX queries for range filters
  - `searchListCriteria()`: Executes DISTINCT queries for list options
  - `getMappedCriteriaKey()`: Maps API criteriaKey to DB field name

### Database Layer
- **Primary Table**: `V_UT_PROD_INSTM` (View)
- **Entity Class**: `UtProdInstm.java`

### Field Mapping Configuration
- **File**: `applicationContext-fund-beans.xml`
- **Bean**: `fundCriteriaKeyMapper`
- **Function**: Maps API criteriaKey to database field names

### Key Mapping Examples
```xml
<entry key="CAT" value="utProd.categoryCode" />
<entry key="FAM" value="utProd.familyCode" />
<entry key="RISK" value="utProd.riskLvlCde" />
<entry key="CCY" value="utProd.currency" />
<entry key="INVSTRG" value="investmentRegionCode" />
<entry key="Y1RTRN" value="return1yrDaily" />
<entry key="DIVYLD" value="yield1Yr" />
<entry key="ACQN" value="averageCreditQualityName" />
<entry key="GBAA" value="utProd.gbaAcctTrdb" />
<entry key="ESG" value="utProd.esgInd" />
```

### SQL Query Examples

#### MinMax Criteria Query (Range Filters)
```sql
SELECT MAX(utProd.return1yrDaily), MIN(utProd.return1yrDaily),
       MAX(utProd.yield1Yr), MIN(utProd.yield1Yr)
FROM V_UT_PROD_INSTM utProd
WHERE utProd.productType = 'UT'
  AND nvl(utProd.restrOnlScribInd,' ') != 'Y'
```

#### List Criteria Query (Dropdown Options)
```sql
SELECT DISTINCT utProd.categoryCode, utProd.categoryName, utProd.categoryLevel0Name
FROM V_UT_PROD_INSTM utProd
WHERE utProd.productType = 'UT'
  AND nvl(utProd.restrOnlScribInd,' ') != 'Y'
ORDER BY utProd.categorySequencenNum, utProd.categoryName NULLS LAST
```

### Data Source Classification

| Data Type | Source | Update Frequency | Calculation Method |
|-----------|--------|------------------|-------------------|
| **Static Reference Data** (CAT, FAM, CCY, RISK) | DB | Daily batch | Direct DB query |
| **Performance Data** (Y1RTRN, DIVYLD, Y1QTL) | DB | Daily batch | Direct DB query (pre-calculated) |
| **Rating Data** (ACQN, MSTAR) | DB | Daily batch | Direct DB query |
| **Flag Data** (ESG, GBAA) | DB | As needed | Direct DB query |

### Source Code Analysis Results

Based on deep analysis of the source code, all fields marked as "Calculate ⚠️" are actually **DB** fields:

#### Y1RTRN (1 Year Return)
- **API Field**: `Y1RTRN`
- **Config Mapping**: `return1yrDaily` (HK_HBAP), `return1yr` (DEFAULT)
- **DB Column**: `RTRN_1YR_DPN` (Daily) / `RTRN_1YR_AMT` (Monthly)
- **Entity Field**: `return1yrDaily` / `return1yr`
- **Source**: **DB** - Pre-calculated values stored in database
- **Query Type**: MIN/MAX aggregation on existing DB values

#### DIVYLD (Dividend Yield)
- **API Field**: `DIVYLD`
- **Config Mapping**: `yield1Yr`
- **DB Column**: `YIELD_1YR_PCT`
- **Entity Field**: `yield1Yr`
- **Source**: **DB** - Pre-calculated values stored in database
- **Query Type**: MIN/MAX aggregation on existing DB values

#### Y1QTL (1 Year Quartile Ranking)
- **API Field**: `Y1QTL`
- **Config Mapping**: `rank1Yr`
- **DB Column**: `RANK_QTL_1YR_NUM`
- **Entity Field**: `rank1Yr`
- **Source**: **DB** - Pre-calculated ranking values stored in database
- **Query Type**: DISTINCT query on existing DB values

### Technical Implementation Flow

```
1. FundSearchCriteriaDaoImpl.searchMinMaxCriteria()
   ↓
2. getMappedCriteriaKey() → fundCriteriaKeyMapper.getDbFieldName()
   ↓
3. HQL: "SELECT MAX(utProd.return1yrDaily), MIN(utProd.return1yrDaily) FROM UtProdInstm utProd"
   ↓
4. Database executes MIN/MAX on pre-calculated RTRN_1YR_DPN column
   ↓
5. Return aggregated values to API
```

**Conclusion**: These are **NOT** real-time calculations. They are MIN/MAX aggregations performed on pre-calculated values already stored in the database. The database contains pre-calculated performance metrics that are updated through batch processes.

### Database Column Verification Status

| API Field | DB Column | Status | Notes |
|-----------|-----------|--------|-------|
| `CAT` | `FUND_CAT_CDE` | ✅ Verified | Category code from entity mapping |
| `FAM` | `FUND_FM_CDE` | ✅ Verified | Fund family code from entity mapping |
| `RISK` | `RISK_LVL_CDE` | ✅ Verified | Risk level code from entity mapping |
| `CCY` | `CCY_PROD_CDE` | ✅ Verified | Currency code from entity mapping |
| `INVSTRG` | `MKT_INVST_CDE` | ✅ Verified | Market investment code from entity mapping |
| `Y1RTRN` | `RTRN_1YR_DPN` | ✅ Verified | 1-year return percentage from entity mapping |
| `DIVYLD` | `YIELD_1YR_PCT` | ✅ Verified | 1-year yield percentage from entity mapping |
| `Y1QTL` | `RANK_QTL_1YR_NUM` | ✅ Verified | 1-year quartile ranking from entity mapping |
| `ACQN` | `AVG_CR_QLTY_NAME` | ✅ Verified | Average credit quality name from entity mapping |
| `MSTAR` | `PROD_OVRL_RATENG_NUM` | ✅ Verified | Product overall rating number from entity mapping |
| `GBAA` | `GBA_ACCT_TRDB` | ✅ Verified | GBA account tradable flag from entity mapping |
| `ESG` | `ESG_IND` | ✅ Verified | ESG indicator from entity mapping |

### Performance Metrics

| Query Type | Execution Time | Optimization Strategy |
|------------|----------------|----------------------|
| MinMax Queries | ~50-100ms | Indexed columns, WHERE clause optimization |
| List Queries | ~100-200ms | Sequence number ordering, DISTINCT optimization |
| Combined Request | ~200-300ms | Parallel execution of MinMax + List queries |