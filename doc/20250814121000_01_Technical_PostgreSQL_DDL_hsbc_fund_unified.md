# PostgreSQL DDL（hsbc_fund_unified）

创建时间：2025-08-14 12:10:00

- 文档类型：Technical
- 关联设计文档：./20250814103030_01_Technical_NL2SQL_Single_Table_Design_U43051.md
- 数据来源（U43051 8 个 JSON）：
  - amh_ut_product / wmds_advanceChart / wmds_fundQuoteSummary / wmds_fundSearchCriteria
  - wmds_holdingAllocation / wmds_otherFundClasses / wmds_quoteDetail / wmds_topTenHoldings

---

## 目录（TOC）
- 表结构设计原则
- 完整 DDL 脚本（可执行）
- 约束与索引说明
- 与字段映射规则一致性校验

---

## 表结构设计原则
- 高频查询字段抽取为标量列（如 product_code, risk_level, nav, fund_sw_in_min_amt 等）
- 复杂嵌套结构以 JSONB 列原样存储（top10_holdings, summary_cumulative, holding_allocation 等）
- 满足 NL2SQL 单条 SQL 查询能力（JSONB + LATERAL/包含匹配）

---

## 完整 DDL 脚本（可执行）

```sql
-- Schema: public (默认)
-- Table: hsbc_fund_unified

CREATE TABLE IF NOT EXISTS public.hsbc_fund_unified (
  -- 标识与基础信息
  product_code              TEXT PRIMARY KEY,                           -- 产品代码，如 U43051
  isin_code                 TEXT,                                        -- ISIN，来自 prodAltNumSegs['I']
  family_code               TEXT,                                        -- 基金公司代码，如 MERRL
  family_name               TEXT,                                        -- 基金公司名称，如 BlackRock
  name                      TEXT,                                        -- 基金名称
  risk_level                SMALLINT CHECK (risk_level BETWEEN 1 AND 5), -- 风险等级（1-5）

  -- 分类与币种
  category_lv1_code         TEXT,                                        -- 一级分类代码（如 SPEC）
  category_lv1_name         TEXT,                                        -- 一级分类名称（如 Specialties）
  hsbc_category_code        TEXT,                                        -- HSBC 分类代码（如 CF）
  hsbc_category_name        TEXT,                                        -- HSBC 分类名称（如 Commodity Funds）
  currency                  CHAR(3) CHECK (currency ~ '^[A-Z]{3}$'),     -- 主币种（USD/HKD...）
  tradable_ccy              TEXT[],                                      -- 可交易币种数组（如 {HKD,USD}）

  -- 市场价格与区间
  nav                       NUMERIC(20,6),                               -- 最新 NAV/Price
  nav_date                  DATE,                                        -- NAV 对应日期
  week_range_low            NUMERIC(20,6),                               -- 52 周最低
  week_range_high           NUMERIC(20,6),                               -- 52 周最高
  week_range_ccy            CHAR(3) CHECK (week_range_ccy IS NULL OR week_range_ccy ~ '^[A-Z]{3}$'),

  -- 交易许可（Y/N→BOOLEAN）
  allow_buy                 BOOLEAN,                                     -- 是否允许申购
  allow_sell                BOOLEAN,                                     -- 是否允许赎回
  allow_sw_in               BOOLEAN,                                     -- 是否允许转换入
  allow_sw_out              BOOLEAN,                                     -- 是否允许转换出
  allow_sell_mip            BOOLEAN,                                     -- 是否允许出售 MIP

  -- 最小金额与持有约束（attributeMap + toNewInvestors）
  inv_min_init              NUMERIC(20,6),                               -- HSBC 最小初始申购（toNewInvestors）
  inv_min_subq              NUMERIC(20,6),                               -- HSBC 最小追加申购（toNewInvestors）
  invst_init_min_amt        NUMERIC(20,6),                               -- 初始申购最低额（attributeMap.invstInitMinAmt）
  fund_sw_in_min_amt        NUMERIC(20,6),                               -- 最小转换入金额（attributeMap.fundSwInMinAmt）
  fund_sw_out_min_amt       NUMERIC(20,6),                               -- 最小转换出金额（attributeMap.fundSwOutMinAmt）
  rdm_min_amt               NUMERIC(20,6),                               -- 最小赎回金额（attributeMap.rdmMinAmt）
  mip_min_amt               NUMERIC(20,6),                               -- MIP 最低额（attributeMap.invstMipMinAmt）
  mip_incrm_min_amt         NUMERIC(20,6),                               -- MIP 递增最小额（attributeMap.invstMipIncrmMinAmt）
  retain_min_units          NUMERIC(20,6),                               -- 最小持有份额（attributeMap.utRtainMinNum）
  sw_out_retain_min_units   NUMERIC(20,6),                               -- 转出后最低持有份额（attributeMap.utSwOutRtainMinNum）

  -- 费用与规模
  expense_ratio             NUMERIC(10,4),                               -- 费用率（profile.expenseRatio）
  ongoing_charge            NUMERIC(10,4),                               -- Ongoing charge figure（feesAndExpenses）
  annual_mgmt_fee           NUMERIC(10,4),                               -- 年度管理费（feesAndExpenses/profile）
  assets_under_mgmt         NUMERIC(20,6),                               -- 资产规模（profile.assetsUnderManagement）
  aum_ccy                   CHAR(3) CHECK (aum_ccy IS NULL OR aum_ccy ~ '^[A-Z]{3}$'),
  shares_outstanding        NUMERIC(20,6),                               -- 流通份额（holdingDetails）

  -- 关键日期
  next_deal_date            DATE,                                        -- 下一交易日（profile.nextDealDate）
  inception_date            DATE,                                        -- 成立日（profile.inceptionDate）
  launch_date               DATE,                                        -- 类别/报价启动日（quoteDetail.priceQuote.launchDate）

  -- JSONB 原始结构（完整保留）
  attribute_map             JSONB,                                       -- amh_ut_product.attributeMap
  alt_codes                 JSONB,                                       -- prodAltNumSegs（含 P/F/M/I/O/T/W）
  summary_calendar_year     JSONB,                                       -- fundQuoteSummary.calendarYearTotalReturns
  summary_cumulative        JSONB,                                       -- fundQuoteSummary.cumulativeTotalReturns
  price_quote               JSONB,                                       -- quoteDetail.priceQuote（含其 returns）
  holding_details           JSONB,                                       -- holdingDetails
  to_new_investors          JSONB,                                       -- toNewInvestors
  fees_and_expenses         JSONB,                                       -- feesAndExpenses
  morningstar_ratings       JSONB,                                       -- morningstarRatings
  mgmt_contact              JSONB,                                       -- mgmtAndContactInfo（含经理列表）
  investment_strategy       JSONB,                                       -- investmentStrategy
  yield_and_credit          JSONB,                                       -- yieldAndCredit
  rating                    JSONB,                                       -- rating
  risk_json                 JSONB,                                       -- risk[]（1/3/5/10Y yearRisk）
  profile                   JSONB,                                       -- profile（名称/分类/地区/日期等）
  holding_allocation        JSONB,                                       -- holdingAllocation[]（methods+breakdowns）
  top10_holdings            JSONB,                                       -- top10Holdings（items+日期）
  chart_timeseries          JSONB,                                       -- advanceChart.result[0].data（date, navPrice）
  other_fund_classes        JSONB,                                       -- otherFundClasses.assetClasses（可能为 null）
  search_criteria           JSONB,                                       -- fundSearchCriteria（平台维度快照）
  raw_files                 JSONB,                                       -- 可选：文件名/抓取时间元信息

  -- 审计
  created_at                TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at                TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 索引（按典型 NL2SQL 查询优化）
-- PK 已隐含唯一索引
CREATE INDEX IF NOT EXISTS idx_hfu_risk_level            ON public.hsbc_fund_unified (risk_level);
CREATE INDEX IF NOT EXISTS idx_hfu_nav_date              ON public.hsbc_fund_unified (nav_date);
CREATE INDEX IF NOT EXISTS idx_hfu_allow_buy             ON public.hsbc_fund_unified (allow_buy);
CREATE INDEX IF NOT EXISTS idx_hfu_currency              ON public.hsbc_fund_unified (currency);

-- GIN 索引（JSONB 结构查询，支持 @>、键存在、路径检索等）
CREATE INDEX IF NOT EXISTS idx_hfu_summary_cum_gin       ON public.hsbc_fund_unified USING GIN (summary_cumulative);
CREATE INDEX IF NOT EXISTS idx_hfu_top10_gin             ON public.hsbc_fund_unified USING GIN (top10_holdings);
CREATE INDEX IF NOT EXISTS idx_hfu_hold_alloc_gin        ON public.hsbc_fund_unified USING GIN (holding_allocation);
CREATE INDEX IF NOT EXISTS idx_hfu_risk_json_gin         ON public.hsbc_fund_unified USING GIN (risk_json);
CREATE INDEX IF NOT EXISTS idx_hfu_chart_ts_gin          ON public.hsbc_fund_unified USING GIN (chart_timeseries);
-- 可选：若频繁按 attribute_map 键包含查询
-- CREATE INDEX IF NOT EXISTS idx_hfu_attr_map_gin      ON public.hsbc_fund_unified USING GIN (attribute_map);

-- 表与字段注释（用途与来源）
COMMENT ON TABLE  public.hsbc_fund_unified IS 'HSBC 基金统一单表（标量列 + JSONB 列）支撑 NL2SQL 单条查询';
COMMENT ON COLUMN public.hsbc_fund_unified.product_code            IS '产品代码（主键），如 U43051；来自 multiple sources（productAlternativeNumber / priceQuote.symbol）';
COMMENT ON COLUMN public.hsbc_fund_unified.isin_code               IS 'ISIN，来自 prodAltNumSegs 中 prodCdeAltClassCde=I';
COMMENT ON COLUMN public.hsbc_fund_unified.family_code             IS '基金公司代码，如 MERRL；来自 profile.familyCode 或 attributeMap.fundHouseCde';
COMMENT ON COLUMN public.hsbc_fund_unified.family_name             IS '基金公司名称；如 BlackRock';
COMMENT ON COLUMN public.hsbc_fund_unified.name                    IS '基金名称；来自 profile.name 或 priceQuote.companyName';
COMMENT ON COLUMN public.hsbc_fund_unified.risk_level              IS '风险等级（1-5）；来自 profile.riskLvlCde 或 attributeMap.riskLvlCde';
COMMENT ON COLUMN public.hsbc_fund_unified.category_lv1_code       IS '一级分类代码（如 SPEC）';
COMMENT ON COLUMN public.hsbc_fund_unified.category_lv1_name       IS '一级分类名称（如 Specialties）';
COMMENT ON COLUMN public.hsbc_fund_unified.hsbc_category_code      IS 'HSBC 分类代码（如 CF）';
COMMENT ON COLUMN public.hsbc_fund_unified.hsbc_category_name      IS 'HSBC 分类名称（如 Commodity Funds）';
COMMENT ON COLUMN public.hsbc_fund_unified.currency                IS '主币种（USD/HKD...）；优先 profile.currency 或 attributeMap.ccyProdCde';
COMMENT ON COLUMN public.hsbc_fund_unified.tradable_ccy            IS '可交易币种数组（attributeMap.ccyProdTradeCde）';
COMMENT ON COLUMN public.hsbc_fund_unified.nav                     IS '最新 NAV/Price；优先 attributeMap.prodNavPrcAmt 或 priceQuote.priceQuote';
COMMENT ON COLUMN public.hsbc_fund_unified.nav_date                IS 'NAV 日期；来自 profile.dayEndNAVDate 或 priceQuote.bidPriceDate';
COMMENT ON COLUMN public.hsbc_fund_unified.week_range_low          IS '52 周最低（summary.weekRangeLow）';
COMMENT ON COLUMN public.hsbc_fund_unified.week_range_high         IS '52 周最高（summary.weekRangeHigh）';
COMMENT ON COLUMN public.hsbc_fund_unified.week_range_ccy          IS '52 周范围币种（summary.weekRangeCurrency）';
COMMENT ON COLUMN public.hsbc_fund_unified.allow_buy               IS '允许申购；来自 profile.allowBuy / attributeMap.allowBuyProdInd / priceQuote.allowBuy';
COMMENT ON COLUMN public.hsbc_fund_unified.allow_sell              IS '允许赎回；来自 profile.allowSell / attributeMap.allowSellProdInd';
COMMENT ON COLUMN public.hsbc_fund_unified.allow_sw_in             IS '允许转换入（profile.allowSwInProdInd）';
COMMENT ON COLUMN public.hsbc_fund_unified.allow_sw_out            IS '允许转换出（profile.allowSwOutProdInd / attributeMap.allowSwOutProdInd）';
COMMENT ON COLUMN public.hsbc_fund_unified.allow_sell_mip          IS '允许出售 MIP（profile/attributeMap.allowSellMipProdInd）';
COMMENT ON COLUMN public.hsbc_fund_unified.inv_min_init            IS 'HSBC 最小初始申购（toNewInvestors.hsbcMinInitInvst）';
COMMENT ON COLUMN public.hsbc_fund_unified.inv_min_subq            IS 'HSBC 最小追加申购（toNewInvestors.hsbcMinSubqInvst）';
COMMENT ON COLUMN public.hsbc_fund_unified.invst_init_min_amt      IS '初始申购最低额（attributeMap.invstInitMinAmt）';
COMMENT ON COLUMN public.hsbc_fund_unified.fund_sw_in_min_amt      IS '最小转换入金额（attributeMap.fundSwInMinAmt）';
COMMENT ON COLUMN public.hsbc_fund_unified.fund_sw_out_min_amt     IS '最小转换出金额（attributeMap.fundSwOutMinAmt）';
COMMENT ON COLUMN public.hsbc_fund_unified.rdm_min_amt             IS '最小赎回金额（attributeMap.rdmMinAmt）';
COMMENT ON COLUMN public.hsbc_fund_unified.mip_min_amt             IS 'MIP 最低额（attributeMap.invstMipMinAmt）';
COMMENT ON COLUMN public.hsbc_fund_unified.mip_incrm_min_amt       IS 'MIP 递增最小额（attributeMap.invstMipIncrmMinAmt）';
COMMENT ON COLUMN public.hsbc_fund_unified.retain_min_units        IS '最小持有份额（attributeMap.utRtainMinNum）';
COMMENT ON COLUMN public.hsbc_fund_unified.sw_out_retain_min_units IS '转出后最低持有份额（attributeMap.utSwOutRtainMinNum）';
COMMENT ON COLUMN public.hsbc_fund_unified.expense_ratio           IS '费用率（profile.expenseRatio）';
COMMENT ON COLUMN public.hsbc_fund_unified.ongoing_charge          IS 'Ongoing charge figure（feesAndExpenses.onGoingChargeFigure）';
COMMENT ON COLUMN public.hsbc_fund_unified.annual_mgmt_fee         IS '年度管理费（feesAndExpenses.actualManagementFee 或 profile.annualManagementFee）';
COMMENT ON COLUMN public.hsbc_fund_unified.assets_under_mgmt       IS '资产规模（profile.assetsUnderManagement）';
COMMENT ON COLUMN public.hsbc_fund_unified.aum_ccy                 IS '资产规模币种（profile.assetsUnderManagementCurrencyCode）';
COMMENT ON COLUMN public.hsbc_fund_unified.shares_outstanding      IS '流通份额（holdingDetails.sharesOutstanding）';
COMMENT ON COLUMN public.hsbc_fund_unified.next_deal_date          IS '下一交易日（profile.nextDealDate）';
COMMENT ON COLUMN public.hsbc_fund_unified.inception_date          IS '基金成立日（profile.inceptionDate）';
COMMENT ON COLUMN public.hsbc_fund_unified.launch_date             IS '基金类别/报价启动日（quoteDetail.priceQuote.launchDate）';
COMMENT ON COLUMN public.hsbc_fund_unified.attribute_map           IS 'amh_ut_product.attributeMap 全量原样存储';
COMMENT ON COLUMN public.hsbc_fund_unified.alt_codes               IS 'prodAltNumSegs（P/F/M/I/O/T/W）代码集合';
COMMENT ON COLUMN public.hsbc_fund_unified.summary_calendar_year   IS '年度收益（calendarYearTotalReturns）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.summary_cumulative      IS '累计收益（cumulativeTotalReturns）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.price_quote             IS '报价详情（quoteDetail.priceQuote）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.holding_details         IS '持仓摘要（holdingDetails）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.to_new_investors        IS '面向新投资者限额/币种等（toNewInvestors）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.fees_and_expenses       IS '费用明细（feesAndExpenses）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.morningstar_ratings     IS '晨星评级（morningstarRatings）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.mgmt_contact            IS '管理人与联系信息（mgmtAndContactInfo）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.investment_strategy     IS '投资策略/风格（investmentStrategy）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.yield_and_credit        IS '收益率与信用（yieldAndCredit）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.rating                  IS '排名/平均信用质量（rating）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.risk_json               IS '风险（yearRisk：1/3/5/10Y）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.profile                 IS '概要 profile（名称/分类/地区/日期等）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.holding_allocation      IS '持仓分配（assetAllocations/regions/bonds...）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.top10_holdings          IS '前十大持仓（items+日期）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.chart_timeseries        IS '日度 NAV 时间序列（advanceChart.data）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.other_fund_classes      IS '其他基金类别（可能为空）';
COMMENT ON COLUMN public.hsbc_fund_unified.search_criteria         IS '平台搜索维度（minMaxCriterias/listCriterias）原样';
COMMENT ON COLUMN public.hsbc_fund_unified.raw_files               IS '原始文件/抓取元信息（可选）';
COMMENT ON COLUMN public.hsbc_fund_unified.created_at              IS '创建时间';
COMMENT ON COLUMN public.hsbc_fund_unified.updated_at              IS '更新时间';

-- JSONB 列精简路径提示（自发现友好，覆盖上面的通用说明）
COMMENT ON COLUMN public.hsbc_fund_unified.summary_cumulative IS 'fundQuoteSummary.cumulativeTotalReturns 累计收益。路径: ->''items'' 数组，筛选 period=''1Y''/''YTD'' 取 totalReturn';
COMMENT ON COLUMN public.hsbc_fund_unified.top10_holdings IS 'top10Holdings 前十大持仓。路径: ->''items'' 数组，包含 securityName/weighting/market 字段';
COMMENT ON COLUMN public.hsbc_fund_unified.holding_allocation IS 'holdingAllocation 持仓分配数组。methods=''regionalExposures''/''assetAllocations''，->''breakdowns'' 包含 name/weighting';
COMMENT ON COLUMN public.hsbc_fund_unified.risk_json IS 'risk 风险指标数组。->''yearRisk'' 包含 year/beta/stdDev/alpha/sharpeRatio';
COMMENT ON COLUMN public.hsbc_fund_unified.chart_timeseries IS 'advanceChart.data NAV 时间序列数组。包含 date/navPrice 字段';
COMMENT ON COLUMN public.hsbc_fund_unified.price_quote IS 'wmds_quoteDetail.priceQuote 报价详情。常用路径: ->''priceQuote'' (NAV), ->''currency'', ->''change''->''percent'' (涨跌幅), ->''cumulativeReturns''->''items'' (累计收益数组)';

```

---

## 约束与索引说明
- 风险等级限定在 1..5；货币字段（currency/aum_ccy/week_range_ccy）正则约束为三位大写
- B-Tree：risk_level、nav_date、allow_buy、currency 用于高频筛选
- GIN：summary_cumulative、top10_holdings、holding_allocation、risk_json、chart_timeseries 支持 @> 与键存在匹配

---

## 与映射规则一致性
- 标量列来自 attributeMap、profile、priceQuote、toNewInvestors、holdingDetails 等，满足文档的字段映射
- JSONB 列完整保留原结构，便于单条 SQL 使用 jsonb_array_elements/jsonb_to_recordset 等函数展开



---

## JSONB 列自发现字段策略（供 NL2SQL 使用）

采用 LangChain 的"按需自发现"机制，避免一次性注入过多 token 导致 429 限流。Agent 通过 jsonb_object_keys() 等函数动态探测 JSONB 结构，再生成具体查询。

### 精简 COMMENT（仅路径提示）
```sql
COMMENT ON COLUMN public.hsbc_fund_unified.summary_cumulative IS 'fundQuoteSummary.cumulativeTotalReturns 累计收益。路径: ->''items'' 数组，筛选 period=''1Y''/''YTD'' 取 totalReturn';
COMMENT ON COLUMN public.hsbc_fund_unified.top10_holdings IS 'top10Holdings 前十大持仓。路径: ->''items'' 数组，包含 securityName/weighting/market 字段';
COMMENT ON COLUMN public.hsbc_fund_unified.holding_allocation IS 'holdingAllocation 持仓分配数组。methods=''regionalExposures''/''assetAllocations''，->''breakdowns'' 包含 name/weighting';
COMMENT ON COLUMN public.hsbc_fund_unified.risk_json IS 'risk 风险指标数组。->''yearRisk'' 包含 year/beta/stdDev/alpha/sharpeRatio';
COMMENT ON COLUMN public.hsbc_fund_unified.chart_timeseries IS 'advanceChart.data NAV 时间序列数组。包含 date/navPrice 字段';
COMMENT ON COLUMN public.hsbc_fund_unified.price_quote IS 'wmds_quoteDetail.priceQuote 报价详情。常用路径: ->''priceQuote'' (NAV), ->''currency'', ->''change''->''percent'' (涨跌幅), ->''cumulativeReturns''->''items'' (累计收益数组)';
```

### 自发现字段的 SQL 模板
```sql
-- 顶层键
SELECT DISTINCT jsonb_object_keys(price_quote) AS top_keys
FROM hsbc_fund_unified LIMIT 1;

-- 数组元素
SELECT item
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.summary_cumulative->'items') AS item
LIMIT 5;

-- 基于发现路径的实际查询示例（1Y 累计收益）
SELECT (item->>'totalReturn')::NUMERIC AS rtn_1y
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.summary_cumulative->'items') AS item
WHERE f.product_code = 'U43051' AND item->>'period' = '1Y';
```

### LangChain NL2SQL 配置要点
- SQL_PREFIX：指示“对 JSONB 先用 jsonb_object_keys() 探测，再构造路径；数组用 LATERAL jsonb_array_elements() 展开；::NUMERIC/::DATE 做类型转换”
- 分阶段：先标量列筛选，再按需探测 JSONB
- 控制表范围：include_tables=['hsbc_fund_unified']；sample_rows_in_table_info=0~1
- 可缓存自发现结果，避免重复探测

### 中文别名映射（供 Agent 参考）
- 最近1年累计收益率 → summary_cumulative.items[period='1Y'].totalReturn
- 前十大含加拿大 → top10_holdings.items 存在 market='CA'
- 地区暴露-加拿大 → holding_allocation[methods='regionalExposures'].breakdowns[name='CA'].weighting
- 1年beta/标准差 → risk_json.yearRisk[year=1].beta/stdDev
- 最新价格/NAV → price_quote->>'priceQuote'
- 今日涨跌幅(%) → price_quote->'change'->>'percent'