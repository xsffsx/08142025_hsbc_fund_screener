# HSBC 基金 NL2SQL 单表设计（U43051 全量数据）

创建时间：2025-08-14 10:30:30

- 文档类型：Technical
- 适用范围：HSBC Fund Screener（样本产品 U43051，拓展到所有产品同样适用）
- 目标摘要：将 8 份 JSON 响应中的基金全量信息统一落在一个 PostgreSQL 单表中，使自然语言查询可通过“单条 SQL”完成回答。

---

## 目录（TOC）

- 背景与目标
- 原始数据结构综述（8 份 JSON）
- 关键基金属性字段清单（代表性，含 fundSwInMinAmt 等）
- 单表模式总体设计与取舍
- 字段级数据字典（含数据类型与约束）
- 建表 SQL（PostgreSQL）
- JSON→表字段映射规则
- 示例 NL2SQL 场景与单条 SQL 查询
- Mermaid 架构与数据流示意
- 注意事项与扩展建议

---

## 背景与目标

- 背景：样本产品 U43051 具备 8 类数据源，涵盖基本产品信息、报价与收益、风格与配置、前十大持仓、时序 NAV、搜索维度等。
- 目标：
  1) 将上述多源结构化/半结构化信息，统一落至单个 PostgreSQL 表中；
  2) 以 JSONB 存储“列表/复杂结构”，并为常用关键属性抽取为标量列；
  3) 确保典型 NL 问题可用“单条 SQL”直接回答。

---

## 原始数据结构综述（8 份 JSON）

数据位置：./output/001_U43051.*.json

- amh_ut_product_response_body.json
  - 顶层 payload[0].productAlternativeNumber（产品码）
  - payload[0].attributeMap（大量业务属性，如 fundSwInMinAmt、allowBuyProdInd、riskLvlCde、finDocURL_*、ccyProdTradeCde[]、prodNavPrcAmt 等）
- wmds_fundQuoteSummary_response_body.json
  - summary（bid/offer、52周区间、年度与累计收益 items[]、lastUpdatedDate 等）
  - holdingDetails、toNewInvestors、feesAndExpenses、morningstarRatings、mgmtAndContactInfo、investmentStrategy、yieldAndCredit、rating、risk[]、prodAltNumSegs[]、profile
- wmds_quoteDetail_response_body.json
  - priceQuote（symbol、priceQuote、currency、change%、allowBuy/Sell、riskLvlCde、cumulativeReturns、calendarReturns、launchDate）
  - entityUpdatedTime，prodAltNumSegs[]
- wmds_holdingAllocation_response_body.json
  - holdingAllocation[]（methods: assetAllocations / globalStockSectors / regionalExposures / globalBondSectors / bondRegionalExposures；各自 breakdowns[]、portfolioDate），numberOfStockHoldings、lastUpdatedDate
- wmds_topTenHoldings_response_body.json
  - top10Holdings.items[]（securityName、weighting、marketValue、currency、market 等），lastUpdatedDate
- wmds_advanceChart_response_body.json
  - result[0].prodAltNumSegs[]，data[]（date、navPrice、cumulativeReturn=null）
- wmds_otherFundClasses_response_body.json
  - assetClasses（样本为 null，保留 JSONB 容器）
- wmds_fundSearchCriteria_response_body.json
  - minMaxCriterias[]、listCriterias[]（平台维度的可筛选项范围快照）

---

## 关键基金属性字段清单（代表性）

来自 attributeMap（样本）：
- productAlternativeNumber: U43051（产品码）
- fundSwInMinAmt: 1000（最小申购金额）
- fundSwOutMinAmt: 1000（最小转换出金额）
- invstInitMinAmt: 1000（初始申购最低额）
- invstMipMinAmt: 1000（MIP 最低额）
- invstMipIncrmMinAmt: 1000（MIP 递增最小额）
- rdmMinAmt: 1000（最小赎回额）
- utRtainMinNum: 0（最小持有份额）
- utSwOutRtainMinNum: 0（转换出后最低持有份额）
- dcmlPlaceTradeUnitNum: 2（交易单位小数位）
- riskLvlCde: 5（风险等级）
- ccyProdTradeCde: [HKD, USD]（可交易币种）
- ccyProdCde: USD（产品币种）
- ccyInvstCde: USD（申购币种）
- prodNavPrcAmt: 64.36（当前 NAV）
- allowBuyProdInd / allowSellProdInd / allowSwOutProdInd / allowSellMipProdInd（交易许可标志）
- fundHouseName: BlackRock，fundHouseCde: MERRL
- finDocURL_*（招募说明书、年报、事实说明书等）
- prcEffDt、setlLeadTmRdm/ Scrib（价格/结算相关）

来自 profile/quote/risk 等（样本）：
- profile.name、familyName/familyCode、categoryLevel1Code/Name、hsbcCategoryCode/Name、inceptionDate、nextDealDate
- summary.weekRangeLow/High、cumulativeTotalReturns.items[]、calendarYearTotalReturns.items[]
- risk[].yearRisk（1/3/5/10Y 的 beta、stdDev、alpha、sharpe、rSquared、totalReturn）
- feesAndExpenses（onGoingChargeFigure、annualManagementFee、maximumInitialSalesFees 等）
- mgmtInfos[]（基金经理与起始日期）
- holdingAllocation[] / top10Holdings.items[]
- advanceChart.result[0].data[]（日度 NAV）

---

## 单表模式总体设计与取舍

- 统一主键：product_code（如 U43051）。
- 设计原则：
  1) 高频查询/筛选的关键属性抽取为“标量列”；
  2) 多值/层级信息（时间序列、前十大、资产配置、收益分布等）以 JSONB 存储；
  3) 所有信息均落单表，使“单条 SQL”可通过 JSONB 操作获取复杂字段。
- 选型：PostgreSQL（JSONB 原生支持，适于 NL2SQL 单表查询）。

---

## 字段级数据字典（节选）

标量列（常用筛选/聚合）：
- product_code TEXT PK：产品码
- isin_code TEXT：ISIN（prodAltNumSegs['I'] 或 profile 内对应）
- family_code TEXT：基金公司代码（如 MERRL）
- family_name TEXT：基金公司名称
- name TEXT：基金名称
- risk_level SMALLINT：风险等级（riskLvlCde）
- category_lv1_code TEXT：一级分类代码（如 SPEC）
- category_lv1_name TEXT：一级分类名
- hsbc_category_code TEXT：HSBC 分类代码（如 CF）
- hsbc_category_name TEXT：HSBC 分类名
- currency CHAR(3)：产品/交易主币种（优先 profile.currency 或 attributeMap.ccyProdCde）
- tradable_ccy TEXT[]：可交易币种（ccyProdTradeCde[]）
- nav NUMERIC(20,6)：最新 NAV（attributeMap.prodNavPrcAmt 或 priceQuote.priceQuote）
- nav_date DATE：NAV 对应日期（profile.dayEndNAVDate 或 priceQuote.bidPriceDate）
- week_range_low NUMERIC(20,6)、week_range_high NUMERIC(20,6)、week_range_ccy CHAR(3)
- allow_buy BOOLEAN、allow_sell BOOLEAN、allow_sw_in BOOLEAN、allow_sw_out BOOLEAN、allow_sell_mip BOOLEAN
- inv_min_init NUMERIC(20,6)、inv_min_subq NUMERIC(20,6)：面向新投资者（toNewInvestors）
- invst_init_min_amt NUMERIC(20,6)：attributeMap.invstInitMinAmt
- fund_sw_in_min_amt NUMERIC(20,6)、fund_sw_out_min_amt NUMERIC(20,6)
- rdm_min_amt NUMERIC(20,6)
- mip_min_amt NUMERIC(20,6)、mip_incrm_min_amt NUMERIC(20,6)
- retain_min_units NUMERIC(20,6)、sw_out_retain_min_units NUMERIC(20,6)
- expense_ratio NUMERIC(10,4)、ongoing_charge NUMERIC(10,4)、annual_mgmt_fee NUMERIC(10,4)
- next_deal_date DATE、inception_date DATE、launch_date DATE
- assets_under_mgmt NUMERIC(20,6)、aum_ccy CHAR(3)
- shares_outstanding NUMERIC(20,6)

JSONB 列（保存复杂结构）：
- attribute_map JSONB：完整 attributeMap（原样）
- alt_codes JSONB：prodAltNumSegs[]
- summary_calendar_year JSONB：calendarYearTotalReturns
- summary_cumulative JSONB：cumulativeTotalReturns
- price_quote JSONB：wmds_quoteDetail.priceQuote（含 calendar/cumulative Returns）
- holding_details JSONB：holdingDetails
- to_new_investors JSONB：toNewInvestors
- fees_and_expenses JSONB：feesAndExpenses
- morningstar_ratings JSONB：morningstarRatings
- mgmt_contact JSONB：mgmtAndContactInfo（含经理列表）
- investment_strategy JSONB：investmentStrategy
- yield_and_credit JSONB：yieldAndCredit
- rating JSONB：rating
- risk_json JSONB：risk[]
- profile JSONB：profile
- holding_allocation JSONB：holdingAllocation[]（含各 methods）
- top10_holdings JSONB：top10Holdings
- chart_timeseries JSONB：advanceChart.result[0].data[]
- other_fund_classes JSONB：otherFundClasses.assetClasses
- search_criteria JSONB：fundSearchCriteria（平台维度快照）
- raw_files JSONB：可选，存储文件名/etag/抓取时间等元信息

约束：
- NOT NULL：product_code
- 唯一：product_code
- 检查：risk_level BETWEEN 1 AND 5；currency 正则 ^[A-Z]{3}$

---

## 建表 SQL（PostgreSQL）

```sql
CREATE TABLE IF NOT EXISTS hsbc_fund_unified (
  product_code              TEXT PRIMARY KEY,
  isin_code                 TEXT,
  family_code               TEXT,
  family_name               TEXT,
  name                      TEXT,
  risk_level                SMALLINT CHECK (risk_level BETWEEN 1 AND 5),
  category_lv1_code         TEXT,
  category_lv1_name         TEXT,
  hsbc_category_code        TEXT,
  hsbc_category_name        TEXT,
  currency                  CHAR(3) CHECK (currency ~ '^[A-Z]{3}$'),
  tradable_ccy              TEXT[] ,
  nav                       NUMERIC(20,6),
  nav_date                  DATE,
  week_range_low            NUMERIC(20,6),
  week_range_high           NUMERIC(20,6),
  week_range_ccy            CHAR(3),
  allow_buy                 BOOLEAN,
  allow_sell                BOOLEAN,
  allow_sw_in               BOOLEAN,
  allow_sw_out              BOOLEAN,
  allow_sell_mip            BOOLEAN,
  inv_min_init              NUMERIC(20,6),
  inv_min_subq              NUMERIC(20,6),
  invst_init_min_amt        NUMERIC(20,6),
  fund_sw_in_min_amt        NUMERIC(20,6),
  fund_sw_out_min_amt       NUMERIC(20,6),
  rdm_min_amt               NUMERIC(20,6),
  mip_min_amt               NUMERIC(20,6),
  mip_incrm_min_amt         NUMERIC(20,6),
  retain_min_units          NUMERIC(20,6),
  sw_out_retain_min_units   NUMERIC(20,6),
  expense_ratio             NUMERIC(10,4),
  ongoing_charge            NUMERIC(10,4),
  annual_mgmt_fee           NUMERIC(10,4),
  next_deal_date            DATE,
  inception_date            DATE,
  launch_date               DATE,
  assets_under_mgmt         NUMERIC(20,6),
  aum_ccy                   CHAR(3),
  shares_outstanding        NUMERIC(20,6),

  attribute_map             JSONB,
  alt_codes                 JSONB,
  summary_calendar_year     JSONB,
  summary_cumulative        JSONB,
  price_quote               JSONB,
  holding_details           JSONB,
  to_new_investors          JSONB,
  fees_and_expenses         JSONB,
  morningstar_ratings       JSONB,
  mgmt_contact              JSONB,
  investment_strategy       JSONB,
  yield_and_credit          JSONB,
  rating                    JSONB,
  risk_json                 JSONB,
  profile                   JSONB,
  holding_allocation        JSONB,
  top10_holdings            JSONB,
  chart_timeseries          JSONB,
  other_fund_classes        JSONB,
  search_criteria           JSONB,
  raw_files                 JSONB,

  created_at                TIMESTAMP DEFAULT NOW(),
  updated_at                TIMESTAMP DEFAULT NOW()
);
```

---

## JSON→表字段映射规则

- 基本键与标量：
  - product_code ← payload[0].productAlternativeNumber 或 priceQuote.symbol
  - name ← profile.name 或 priceQuote.companyName
  - family_code ← profile.familyCode 或 attributeMap.fundHouseCde；family_name 类似
  - risk_level ← COALESCE(profile.riskLvlCde, attributeMap.riskLvlCde, priceQuote.riskLvlCde)::SMALLINT
  - currency ← COALESCE(profile.currency, attributeMap.ccyProdCde, priceQuote.currency)
  - nav ← COALESCE(attributeMap.prodNavPrcAmt, priceQuote.priceQuote)::NUMERIC
  - nav_date ← COALESCE(profile.dayEndNAVDate, priceQuote.bidPriceDate)::DATE
  - week_range_low/high/ccy ← summary.weekRangeLow/High/weekRangeCurrency
  - 交易许可：将 'Y'/'N' 映射为 BOOLEAN：
    - allow_buy ← profile.allowBuy='Y' OR priceQuote.allowBuy='Y' OR attributeMap.allowBuyProdInd='Y'
    - allow_sell ← 同理
    - allow_sw_in ← profile.allowSwInProdInd='Y'
    - allow_sw_out ← profile.allowSwOutProdInd='Y' OR attributeMap.allowSwOutProdInd='Y'
    - allow_sell_mip ← profile.allowSellMipProdInd='Y' OR attributeMap.allowSellMipProdInd='Y'
  - 最小金额类（均转 NUMERIC）：
    - invst_init_min_amt ← attributeMap.invstInitMinAmt
    - fund_sw_in_min_amt ← attributeMap.fundSwInMinAmt
    - fund_sw_out_min_amt ← attributeMap.fundSwOutMinAmt
    - rdm_min_amt ← attributeMap.rdmMinAmt
    - mip_min_amt ← attributeMap.invstMipMinAmt
    - mip_incrm_min_amt ← attributeMap.invstMipIncrmMinAmt
    - retain_min_units ← attributeMap.utRtainMinNum
    - sw_out_retain_min_units ← attributeMap.utSwOutRtainMinNum
  - 面向新投资者（toNewInvestors）：
    - inv_min_init ← toNewInvestors.hsbcMinInitInvst
    - inv_min_subq ← toNewInvestors.hsbcMinSubqInvst
  - 费用：
    - expense_ratio ← profile.expenseRatio
    - ongoing_charge ← feesAndExpenses.onGoingChargeFigure
    - annual_mgmt_fee ← COALESCE(feesAndExpenses.actualManagementFee, profile.annualManagementFee)
  - 其它：
    - isin_code ← 从 prodAltNumSegs 中 item.prodCdeAltClassCde='I' 的 prodAltNum
    - assets_under_mgmt / aum_ccy ← profile.assetsUnderManagement / assetsUnderManagementCurrencyCode
    - shares_outstanding ← holdingDetails.sharesOutstanding
    - next_deal_date / inception_date / launch_date ← profile.nextDealDate / profile.inceptionDate / priceQuote.launchDate
  - tradable_ccy ← attributeMap.ccyProdTradeCde[] 转 TEXT[]

- JSONB 列：
  - attribute_map ← 完整 attributeMap 结构
  - alt_codes ← prodAltNumSegs[]（quoteSummary/profile/quoteDetail/advanceChart 处一致）
  - summary_calendar_year ← summary.calendarYearTotalReturns（含 items[] 等）
  - summary_cumulative ← summary.cumulativeTotalReturns
  - price_quote ← wmds_quoteDetail.priceQuote（包含自身的 cumulativeReturns/calendarReturns）
  - holding_details / to_new_investors / fees_and_expenses / morningstar_ratings / mgmt_contact / investment_strategy / yield_and_credit / rating / risk_json / profile ← 各块原样
  - holding_allocation ← holdingAllocation[]（含 methods、breakdowns、portfolioDate）
  - top10_holdings ← top10Holdings（含 items[]、lastUpdatedDate）
  - chart_timeseries ← advanceChart.result[0].data[]（日度 NAV）
  - other_fund_classes ← otherFundClasses.assetClasses（可为 null）
  - search_criteria ← fundSearchCriteria（平台筛选项快照）

---

## 示例 NL2SQL 场景与单条 SQL 查询

1) 这只基金的最小申购金额（fundSwInMinAmt）与当前 NAV？
```sql
SELECT product_code,
       fund_sw_in_min_amt,
       nav,
       nav_date
FROM hsbc_fund_unified
WHERE product_code = 'U43051';
```

2) 最近 1 年累计收益率（%）与 1/3/5 年风险指标（beta、stdDev）？
```sql
-- 累计收益（来自 summary_cumulative.items[]）
WITH cum AS (
  SELECT (item->>'period') AS period,
         (item->>'totalReturn')::NUMERIC AS total_return
  FROM hsbc_fund_unified f,
       LATERAL jsonb_array_elements(f.summary_cumulative->'items') AS item
  WHERE f.product_code='U43051'
)
SELECT (SELECT total_return FROM cum WHERE period='1Y') AS rtn_1y,
       (SELECT (yr->'yearRisk'->>'beta')::NUMERIC
          FROM hsbc_fund_unified f,
               LATERAL jsonb_array_elements(f.risk_json) AS yr
         WHERE f.product_code='U43051' AND (yr->'yearRisk'->>'year')='1') AS beta_1y,
       (SELECT (yr->'yearRisk'->>'stdDev')::NUMERIC
          FROM hsbc_fund_unified f,
               LATERAL jsonb_array_elements(f.risk_json) AS yr
         WHERE f.product_code='U43051' AND (yr->'yearRisk'->>'year')='1') AS std_1y,
       (SELECT (yr->'yearRisk'->>'beta')::NUMERIC FROM hsbc_fund_unified f,
               LATERAL jsonb_array_elements(f.risk_json) AS yr
         WHERE f.product_code='U43051' AND (yr->'yearRisk'->>'year')='3') AS beta_3y,
       (SELECT (yr->'yearRisk'->>'stdDev')::NUMERIC FROM hsbc_fund_unified f,
               LATERAL jsonb_array_elements(f.risk_json) AS yr
         WHERE f.product_code='U43051' AND (yr->'yearRisk'->>'year')='3') AS std_3y,
       (SELECT (yr->'yearRisk'->>'beta')::NUMERIC FROM hsbc_fund_unified f,
               LATERAL jsonb_array_elements(f.risk_json) AS yr
         WHERE f.product_code='U43051' AND (yr->'yearRisk'->>'year')='5') AS beta_5y,
       (SELECT (yr->'yearRisk'->>'stdDev')::NUMERIC FROM hsbc_fund_unified f,
               LATERAL jsonb_array_elements(f.risk_json) AS yr
         WHERE f.product_code='U43051' AND (yr->'yearRisk'->>'year')='5') AS std_5y;
```

3) 前十大持仓中权重 > 6% 的证券名称与权重？
```sql
SELECT item->>'securityName' AS security,
       (item->>'weighting')::NUMERIC AS weight_pct
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.top10_holdings->'items') AS item
WHERE f.product_code='U43051'
  AND (item->>'weighting')::NUMERIC > 6
ORDER BY weight_pct DESC;
```

4) 股票净比重（assetAllocEquityNet）与地区暴露（regionalExposures）中“CA/US/UK”的权重？
```sql
SELECT (f.investment_strategy->>'assetAllocEquityNet')::NUMERIC AS equity_net,
       SUM(CASE WHEN br->>'name'='CA' THEN (br->>'weighting')::NUMERIC ELSE 0 END) AS region_ca,
       SUM(CASE WHEN br->>'name'='US' THEN (br->>'weighting')::NUMERIC ELSE 0 END) AS region_us,
       SUM(CASE WHEN br->>'name'='UK' THEN (br->>'weighting')::NUMERIC ELSE 0 END) AS region_uk
FROM hsbc_fund_unified f,
     LATERAL (
       SELECT breakdowns
       FROM jsonb_to_recordset(f.holding_allocation) AS x(methods TEXT, breakdowns JSONB, portfolioDate TEXT)
       WHERE x.methods='regionalExposures'
     ) t,
     LATERAL jsonb_array_elements(t.breakdowns) AS br
WHERE f.product_code='U43051'
GROUP BY equity_net;
```

5) 最近 30 天的 NAV 时序（date, navPrice），只用单条 SQL：
```sql
SELECT (d->>'date')::DATE AS dt,
       (d->>'navPrice')::NUMERIC AS nav
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.chart_timeseries) AS d
WHERE f.product_code='U43051'
  AND (d->>'date')::DATE >= CURRENT_DATE - INTERVAL '30 days'
ORDER BY dt;
```

6) 可交易币种列表（数组展开）：
```sql
SELECT unnest(tradable_ccy) AS ccy
FROM hsbc_fund_unified
WHERE product_code='U43051';
```

---

## Mermaid 架构与数据流示意

```mermaid
%% 参考样式模板：.augment/20250802140100_01_Level1_Architecture_Diagram.md
flowchart TD
  subgraph Ingestion[数据采集 📥]
    A1[AMH 产品信息\nJSON]:::ui
    A2[WMDS 报价摘要\nJSON]:::ui
    A3[WMDS 报价详情\nJSON]:::ui
    A4[WMDS 持仓分配\nJSON]:::ui
    A5[WMDS 前十持仓\nJSON]:::ui
    A6[WMDS 图表NAV\nJSON]:::ui
    A7[WMDS 其他类别\nJSON]:::ui
    A8[WMDS 搜索维度\nJSON]:::ui
  end

  subgraph Transform[标准化转换 🟡]
    T1[标量抽取\n(风险/币种/金额...)]:::ai
    T2[JSONB 直存\n(时序/持仓/收益...)]:::ai
  end

  subgraph Storage[单表存储 🧊]
    S1[(hsbc_fund_unified)]:::data
  end

  subgraph Query[自然语言查询 🔎]
    Q1[单条 SQL\nJSONB 操作]:::ui
  end

  A1-->T1-->S1
  A2-->T1
  A3-->T1
  A2-->T2-->S1
  A3-->T2
  A4-->T2
  A5-->T2
  A6-->T2
  A7-->T2
  A8-->T2
  S1-->|->, ->>|Q1

  classDef ui fill:transparent,stroke:#af52de,stroke-width:3px,color:#ffffff;
  classDef ai fill:transparent,stroke:#eab308,stroke-width:3px,color:#ffffff;
  classDef data fill:transparent,stroke:#06b6d4,stroke-width:3px,color:#ffffff;
```

---

## 注意事项与扩展建议

- 时间序列/前十持仓等数据体量较大，JSONB 列建议创建 GIN 索引按需优化：
```sql
CREATE INDEX IF NOT EXISTS idx_fund_unified_product_code ON hsbc_fund_unified(product_code);
CREATE INDEX IF NOT EXISTS idx_fund_unified_summary_cum ON hsbc_fund_unified USING GIN (summary_cumulative);
CREATE INDEX IF NOT EXISTS idx_fund_unified_chart ON hsbc_fund_unified USING GIN (chart_timeseries);
```
- 布尔标志统一：'Y'/'N' → BOOLEAN。导入时注意空值和 null。
- 数字统一：字符串数字（如 "1000"）统一转 NUMERIC。
- 去重与合并：若同字段在多个来源出现，采用“优先级合并”策略（quoteDetail > quoteSummary > product attributeMap）。
- 数据新鲜度：profile.dayEndNAVDate、summary.dailyLastUpdatedDate、priceQuote.exchangeUpdatedTime 可用于刷新策略。
- 可扩展性：追加更多 JSON 字段直接入 JSONB 列，不破坏既有 SQL。

---

以上方案已在样本 U43051 上验证，适用于批量产品。按此单表设计，绝大多数自然语言问题可由“单条 SQL + JSONB 操作”回答。

