--
-- PostgreSQL database dump
--

-- Dumped from database version 16.9 (Debian 16.9-1.pgdg120+1)
-- Dumped by pg_dump version 16.9 (Debian 16.9-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: hsbc_fund_unified; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hsbc_fund_unified (
    product_code text NOT NULL,
    isin_code text,
    family_code text,
    family_name text,
    name text,
    risk_level smallint,
    category_lv1_code text,
    category_lv1_name text,
    hsbc_category_code text,
    hsbc_category_name text,
    currency character(3),
    tradable_ccy text[],
    nav numeric(20,6),
    nav_date date,
    week_range_low numeric(20,6),
    week_range_high numeric(20,6),
    week_range_ccy character(3),
    allow_buy boolean,
    allow_sell boolean,
    allow_sw_in boolean,
    allow_sw_out boolean,
    allow_sell_mip boolean,
    inv_min_init numeric(20,6),
    inv_min_subq numeric(20,6),
    invst_init_min_amt numeric(20,6),
    fund_sw_in_min_amt numeric(20,6),
    fund_sw_out_min_amt numeric(20,6),
    rdm_min_amt numeric(20,6),
    mip_min_amt numeric(20,6),
    mip_incrm_min_amt numeric(20,6),
    retain_min_units numeric(20,6),
    sw_out_retain_min_units numeric(20,6),
    expense_ratio numeric(10,4),
    ongoing_charge numeric(10,4),
    annual_mgmt_fee numeric(10,4),
    assets_under_mgmt numeric(20,6),
    aum_ccy character(3),
    shares_outstanding numeric(20,6),
    next_deal_date date,
    inception_date date,
    launch_date date,
    attribute_map jsonb,
    alt_codes jsonb,
    summary_calendar_year jsonb,
    summary_cumulative jsonb,
    price_quote jsonb,
    holding_details jsonb,
    to_new_investors jsonb,
    fees_and_expenses jsonb,
    morningstar_ratings jsonb,
    mgmt_contact jsonb,
    investment_strategy jsonb,
    yield_and_credit jsonb,
    rating jsonb,
    risk_json jsonb,
    profile jsonb,
    holding_allocation jsonb,
    top10_holdings jsonb,
    chart_timeseries jsonb,
    other_fund_classes jsonb,
    search_criteria jsonb,
    raw_files jsonb,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT hsbc_fund_unified_aum_ccy_check CHECK (((aum_ccy IS NULL) OR (aum_ccy ~ '^[A-Z]{3}$'::text))),
    CONSTRAINT hsbc_fund_unified_currency_check CHECK ((currency ~ '^[A-Z]{3}$'::text)),
    CONSTRAINT hsbc_fund_unified_risk_level_check CHECK (((risk_level >= 1) AND (risk_level <= 5))),
    CONSTRAINT hsbc_fund_unified_week_range_ccy_check CHECK (((week_range_ccy IS NULL) OR (week_range_ccy ~ '^[A-Z]{3}$'::text)))
);


--
-- Name: TABLE hsbc_fund_unified; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.hsbc_fund_unified IS 'HSBC 基金统一单表（标量列 + JSONB 列）支撑 NL2SQL 单条查询';


--
-- Name: COLUMN hsbc_fund_unified.product_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.product_code IS '产品代码（主键），如 U43051；来自 multiple sources（productAlternativeNumber / priceQuote.symbol）';


--
-- Name: COLUMN hsbc_fund_unified.isin_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.isin_code IS 'ISIN，来自 prodAltNumSegs 中 prodCdeAltClassCde=I';


--
-- Name: COLUMN hsbc_fund_unified.family_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.family_code IS '基金公司代码，如 MERRL；来自 profile.familyCode 或 attributeMap.fundHouseCde';


--
-- Name: COLUMN hsbc_fund_unified.family_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.family_name IS '基金公司名称；如 BlackRock';


--
-- Name: COLUMN hsbc_fund_unified.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.name IS '基金名称；来自 profile.name 或 priceQuote.companyName';


--
-- Name: COLUMN hsbc_fund_unified.risk_level; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.risk_level IS '风险等级（1-5）；来自 profile.riskLvlCde 或 attributeMap.riskLvlCde';


--
-- Name: COLUMN hsbc_fund_unified.category_lv1_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.category_lv1_code IS '一级分类代码（如 SPEC）';


--
-- Name: COLUMN hsbc_fund_unified.category_lv1_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.category_lv1_name IS '一级分类名称（如 Specialties）';


--
-- Name: COLUMN hsbc_fund_unified.hsbc_category_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.hsbc_category_code IS 'HSBC 分类代码（如 CF）';


--
-- Name: COLUMN hsbc_fund_unified.hsbc_category_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.hsbc_category_name IS 'HSBC 分类名称（如 Commodity Funds）';


--
-- Name: COLUMN hsbc_fund_unified.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.currency IS '主币种（USD/HKD...）；优先 profile.currency 或 attributeMap.ccyProdCde';


--
-- Name: COLUMN hsbc_fund_unified.tradable_ccy; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.tradable_ccy IS '可交易币种数组（attributeMap.ccyProdTradeCde）';


--
-- Name: COLUMN hsbc_fund_unified.nav; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.nav IS '最新 NAV/Price；优先 attributeMap.prodNavPrcAmt 或 priceQuote.priceQuote';


--
-- Name: COLUMN hsbc_fund_unified.nav_date; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.nav_date IS 'NAV 日期；来自 profile.dayEndNAVDate 或 priceQuote.bidPriceDate';


--
-- Name: COLUMN hsbc_fund_unified.week_range_low; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.week_range_low IS '52 周最低（summary.weekRangeLow）';


--
-- Name: COLUMN hsbc_fund_unified.week_range_high; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.week_range_high IS '52 周最高（summary.weekRangeHigh）';


--
-- Name: COLUMN hsbc_fund_unified.week_range_ccy; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.week_range_ccy IS '52 周范围币种（summary.weekRangeCurrency）';


--
-- Name: COLUMN hsbc_fund_unified.allow_buy; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.allow_buy IS '允许申购；来自 profile.allowBuy / attributeMap.allowBuyProdInd / priceQuote.allowBuy';


--
-- Name: COLUMN hsbc_fund_unified.allow_sell; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.allow_sell IS '允许赎回；来自 profile.allowSell / attributeMap.allowSellProdInd';


--
-- Name: COLUMN hsbc_fund_unified.allow_sw_in; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.allow_sw_in IS '允许转换入（profile.allowSwInProdInd）';


--
-- Name: COLUMN hsbc_fund_unified.allow_sw_out; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.allow_sw_out IS '允许转换出（profile.allowSwOutProdInd / attributeMap.allowSwOutProdInd）';


--
-- Name: COLUMN hsbc_fund_unified.allow_sell_mip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.allow_sell_mip IS '允许出售 MIP（profile/attributeMap.allowSellMipProdInd）';


--
-- Name: COLUMN hsbc_fund_unified.inv_min_init; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.inv_min_init IS 'HSBC 最小初始申购（toNewInvestors.hsbcMinInitInvst）';


--
-- Name: COLUMN hsbc_fund_unified.inv_min_subq; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.inv_min_subq IS 'HSBC 最小追加申购（toNewInvestors.hsbcMinSubqInvst）';


--
-- Name: COLUMN hsbc_fund_unified.invst_init_min_amt; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.invst_init_min_amt IS '初始申购最低额（attributeMap.invstInitMinAmt）';


--
-- Name: COLUMN hsbc_fund_unified.fund_sw_in_min_amt; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.fund_sw_in_min_amt IS '最小转换入金额（attributeMap.fundSwInMinAmt）';


--
-- Name: COLUMN hsbc_fund_unified.fund_sw_out_min_amt; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.fund_sw_out_min_amt IS '最小转换出金额（attributeMap.fundSwOutMinAmt）';


--
-- Name: COLUMN hsbc_fund_unified.rdm_min_amt; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.rdm_min_amt IS '最小赎回金额（attributeMap.rdmMinAmt）';


--
-- Name: COLUMN hsbc_fund_unified.mip_min_amt; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.mip_min_amt IS 'MIP 最低额（attributeMap.invstMipMinAmt）';


--
-- Name: COLUMN hsbc_fund_unified.mip_incrm_min_amt; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.mip_incrm_min_amt IS 'MIP 递增最小额（attributeMap.invstMipIncrmMinAmt）';


--
-- Name: COLUMN hsbc_fund_unified.retain_min_units; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.retain_min_units IS '最小持有份额（attributeMap.utRtainMinNum）';


--
-- Name: COLUMN hsbc_fund_unified.sw_out_retain_min_units; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.sw_out_retain_min_units IS '转出后最低持有份额（attributeMap.utSwOutRtainMinNum）';


--
-- Name: COLUMN hsbc_fund_unified.expense_ratio; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.expense_ratio IS '费用率（profile.expenseRatio）';


--
-- Name: COLUMN hsbc_fund_unified.ongoing_charge; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.ongoing_charge IS 'Ongoing charge figure（feesAndExpenses.onGoingChargeFigure）';


--
-- Name: COLUMN hsbc_fund_unified.annual_mgmt_fee; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.annual_mgmt_fee IS '年度管理费（feesAndExpenses.actualManagementFee 或 profile.annualManagementFee）';


--
-- Name: COLUMN hsbc_fund_unified.assets_under_mgmt; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.assets_under_mgmt IS '资产规模（profile.assetsUnderManagement）';


--
-- Name: COLUMN hsbc_fund_unified.aum_ccy; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.aum_ccy IS '资产规模币种（profile.assetsUnderManagementCurrencyCode）';


--
-- Name: COLUMN hsbc_fund_unified.shares_outstanding; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.shares_outstanding IS '流通份额（holdingDetails.sharesOutstanding）';


--
-- Name: COLUMN hsbc_fund_unified.next_deal_date; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.next_deal_date IS '下一交易日（profile.nextDealDate）';


--
-- Name: COLUMN hsbc_fund_unified.inception_date; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.inception_date IS '基金成立日（profile.inceptionDate）';


--
-- Name: COLUMN hsbc_fund_unified.launch_date; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.launch_date IS '基金类别/报价启动日（quoteDetail.priceQuote.launchDate）';


--
-- Name: COLUMN hsbc_fund_unified.attribute_map; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.attribute_map IS 'amh_ut_product.attributeMap 全量原样存储';


--
-- Name: COLUMN hsbc_fund_unified.alt_codes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.alt_codes IS 'prodAltNumSegs（P/F/M/I/O/T/W）代码集合';


--
-- Name: COLUMN hsbc_fund_unified.summary_calendar_year; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.summary_calendar_year IS '年度收益（calendarYearTotalReturns）原样';


--
-- Name: COLUMN hsbc_fund_unified.summary_cumulative; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.summary_cumulative IS 'fundQuoteSummary.cumulativeTotalReturns 累计收益。路径: ->''items'' 数组，筛选 period=''1Y''/''YTD'' 取 totalReturn';


--
-- Name: COLUMN hsbc_fund_unified.price_quote; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.price_quote IS 'wmds_quoteDetail.priceQuote 报价详情。常用路径: ->''priceQuote'' (NAV), ->''currency'', ->''change''->''percent'' (涨跌幅), ->''cumulativeReturns''->''items'' (累计收益数组)';


--
-- Name: COLUMN hsbc_fund_unified.holding_details; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.holding_details IS '持仓摘要（holdingDetails）原样';


--
-- Name: COLUMN hsbc_fund_unified.to_new_investors; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.to_new_investors IS '面向新投资者限额/币种等（toNewInvestors）原样';


--
-- Name: COLUMN hsbc_fund_unified.fees_and_expenses; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.fees_and_expenses IS '费用明细（feesAndExpenses）原样';


--
-- Name: COLUMN hsbc_fund_unified.morningstar_ratings; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.morningstar_ratings IS '晨星评级（morningstarRatings）原样';


--
-- Name: COLUMN hsbc_fund_unified.mgmt_contact; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.mgmt_contact IS '管理人与联系信息（mgmtAndContactInfo）原样';


--
-- Name: COLUMN hsbc_fund_unified.investment_strategy; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.investment_strategy IS '投资策略/风格（investmentStrategy）原样';


--
-- Name: COLUMN hsbc_fund_unified.yield_and_credit; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.yield_and_credit IS '收益率与信用（yieldAndCredit）原样';


--
-- Name: COLUMN hsbc_fund_unified.rating; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.rating IS '排名/平均信用质量（rating）原样';


--
-- Name: COLUMN hsbc_fund_unified.risk_json; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.risk_json IS 'risk 风险指标数组。->''yearRisk'' 包含 year/beta/stdDev/alpha/sharpeRatio';


--
-- Name: COLUMN hsbc_fund_unified.profile; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.profile IS '概要 profile（名称/分类/地区/日期等）原样';


--
-- Name: COLUMN hsbc_fund_unified.holding_allocation; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.holding_allocation IS 'holdingAllocation 持仓分配数组。methods=''regionalExposures''/''assetAllocations''，->''breakdowns'' 包含 name/weighting';


--
-- Name: COLUMN hsbc_fund_unified.top10_holdings; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.top10_holdings IS 'top10Holdings 前十大持仓。路径: ->''items'' 数组，包含 securityName/weighting/market 字段';


--
-- Name: COLUMN hsbc_fund_unified.chart_timeseries; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.chart_timeseries IS 'advanceChart.data NAV 时间序列数组。包含 date/navPrice 字段';


--
-- Name: COLUMN hsbc_fund_unified.other_fund_classes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.other_fund_classes IS '其他基金类别（可能为空）';


--
-- Name: COLUMN hsbc_fund_unified.search_criteria; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.search_criteria IS '平台搜索维度（minMaxCriterias/listCriterias）原样';


--
-- Name: COLUMN hsbc_fund_unified.raw_files; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.raw_files IS '原始文件/抓取元信息（可选）';


--
-- Name: COLUMN hsbc_fund_unified.created_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.created_at IS '创建时间';


--
-- Name: COLUMN hsbc_fund_unified.updated_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hsbc_fund_unified.updated_at IS '更新时间';


--
-- Name: hsbc_fund_unified hsbc_fund_unified_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hsbc_fund_unified
    ADD CONSTRAINT hsbc_fund_unified_pkey PRIMARY KEY (product_code);


--
-- Name: idx_hfu_allow_buy; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hfu_allow_buy ON public.hsbc_fund_unified USING btree (allow_buy);


--
-- Name: idx_hfu_chart_ts_gin; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hfu_chart_ts_gin ON public.hsbc_fund_unified USING gin (chart_timeseries);


--
-- Name: idx_hfu_currency; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hfu_currency ON public.hsbc_fund_unified USING btree (currency);


--
-- Name: idx_hfu_hold_alloc_gin; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hfu_hold_alloc_gin ON public.hsbc_fund_unified USING gin (holding_allocation);


--
-- Name: idx_hfu_nav_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hfu_nav_date ON public.hsbc_fund_unified USING btree (nav_date);


--
-- Name: idx_hfu_risk_json_gin; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hfu_risk_json_gin ON public.hsbc_fund_unified USING gin (risk_json);


--
-- Name: idx_hfu_risk_level; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hfu_risk_level ON public.hsbc_fund_unified USING btree (risk_level);


--
-- Name: idx_hfu_summary_cum_gin; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hfu_summary_cum_gin ON public.hsbc_fund_unified USING gin (summary_cumulative);


--
-- Name: idx_hfu_top10_gin; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hfu_top10_gin ON public.hsbc_fund_unified USING gin (top10_holdings);


--
-- PostgreSQL database dump complete
--

