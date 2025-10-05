# JSONB 自发现字段策略（用于 hsbc_fund_unified 的 NL2SQL）

创建时间：2025-08-14 12:30:00

- 文档类型：Guide
- 目标：在不注入大量 JSONB 结构样例的前提下，指导 SQL Agent 采用“按需自发现”方式（jsonb_object_keys/jsonb_array_elements）生成查询，降低 Token 与 429 限流风险。
- 关联：./20250814121000_01_Technical_PostgreSQL_DDL_hsbc_fund_unified.md

---

## 一、策略总览
- 优先使用标量列进行初筛（risk_level, allow_buy, nav_date 等）
- JSONB 列仅在必要时按需探测
- 自发现顺序：顶层键 → 子对象键/数组元素 → 构造路径与类型转换

---

## 二、精简 COMMENT（仅路径提示）
可选，在数据库中执行以下 COMMENT，提供极简路径提示（避免大体量样例）。

```sql
-- 累计收益（fundQuoteSummary.cumulativeTotalReturns）
COMMENT ON COLUMN public.hsbc_fund_unified.summary_cumulative IS 'fundQuoteSummary.cumulativeTotalReturns 累计收益。路径: ->''items'' 数组，筛选 period=''1Y''/''YTD'' 取 totalReturn';

-- 前十大持仓（top10Holdings）
COMMENT ON COLUMN public.hsbc_fund_unified.top10_holdings IS 'top10Holdings 前十大持仓。路径: ->''items'' 数组，包含 securityName/weighting/market 字段';

-- 持仓分配（holdingAllocation）
COMMENT ON COLUMN public.hsbc_fund_unified.holding_allocation IS 'holdingAllocation 持仓分配数组。methods=''regionalExposures''/''assetAllocations''，->''breakdowns'' 包含 name/weighting';

-- 风险指标（risk）
COMMENT ON COLUMN public.hsbc_fund_unified.risk_json IS 'risk 风险指标数组。->''yearRisk'' 包含 year/beta/stdDev/alpha/sharpeRatio';

-- NAV 时间序列（advanceChart.data）
COMMENT ON COLUMN public.hsbc_fund_unified.chart_timeseries IS 'advanceChart.data NAV 时间序列数组。包含 date/navPrice 字段';

-- 报价详情（wmds_quoteDetail.priceQuote）
COMMENT ON COLUMN public.hsbc_fund_unified.price_quote IS 'wmds_quoteDetail.priceQuote 报价详情。常用路径: ->''priceQuote'' (NAV), ->''currency'', ->''change''->''percent'' (涨跌幅), ->''cumulativeReturns''->''items'' (累计收益数组)';
```

---

## 三、自发现字段 SQL 模板

- 顶层键
```sql
SELECT DISTINCT jsonb_object_keys(summary_cumulative) AS top_keys
FROM hsbc_fund_unified
LIMIT 1;
```

- 数组元素结构（items）
```sql
SELECT jsonb_array_elements(summary_cumulative->'items') AS item_sample
FROM hsbc_fund_unified
LIMIT 1;
```

- 子对象键（如 change）
```sql
SELECT DISTINCT jsonb_object_keys(price_quote->'change') AS change_keys
FROM hsbc_fund_unified
LIMIT 1;
```

- 构建实际查询（1Y 累计收益）
```sql
SELECT (item->>'totalReturn')::NUMERIC AS rtn_1y
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.summary_cumulative->'items') AS item
WHERE f.product_code = 'U43051' AND item->>'period' = '1Y';
```

---

## 四、LangChain NL2SQL Prompt 要点
- 指导模型：
  - 对 JSONB 列先用 jsonb_object_keys() 探测键
  - 数组用 LATERAL jsonb_array_elements() 展开后过滤
  - 必要时 ::NUMERIC/::DATE 做类型转换
  - 默认 LIMIT 50，必要时 ORDER BY
- 失败重试：遇到类型错误/路径错误时，先打印一次顶层键/子键再重试

---

## 五、中文别名映射（轻量版）
- 最近1年累计收益率 → summary_cumulative.items[period='1Y'].totalReturn
- 前十大含加拿大 → top10_holdings.items 存在 market='CA'
- 地区暴露-加拿大 → holding_allocation[methods='regionalExposures'].breakdowns[name='CA'].weighting
- 1年beta/标准差 → risk_json.yearRisk[year=1].beta/stdDev
- 最新价格/NAV → price_quote->>'priceQuote'
- 今日涨跌幅(%) → price_quote->'change'->>'percent'

---

## 六、实践建议
- 缓存最近一次自发现结果（内存/Redis），降低重复探测
- 控制 include_tables=['hsbc_fund_unified']，减少无关信息
- sample_rows_in_table_info 可设 0~1，控制 Token

