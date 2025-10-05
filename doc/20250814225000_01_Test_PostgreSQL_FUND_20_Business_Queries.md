# PostgreSQL 基金表（hsbc_fund_unified）20 个业务查询测试用例

创建时间：2025-08-14 22:50:00

- 文档类型：Test
- 目标：为 NL2SQL/MCP Inspector 提供可直接验证的 SQL 与期望结果描述（覆盖 JSONB、SELF JOIN、MAX、SUM、窗口等）
- 目标表：public.hsbc_fund_unified（简称 hfu）
- 备注：实际期望值以“结果形态/约束”为主，避免与数据变动强耦合；若需硬数值可优先验证用例 #1（1407）

---

## 使用说明
- 运行前设置 schema 前缀（若非 public）：将下述 SQL 内的 public.hsbc_fund_unified 改为实际表名
- 核对索引：JSONB 查询建议启用 DDL 文档中给出的 GIN 索引
- SQL 中均使用标准 PostgreSQL 语法；JSONB 操作符参考 -> ->> @> jsonb_array_elements jsonb_each

---

## 测试用例总览（20 条）

| # | 业务问题 | Expected SQL | 期望答案（形态/断言） |
|---|---|---|---|
| 1 | 全库共有多少只基金？ | ```sql\nSELECT COUNT(*) AS cnt FROM public.hsbc_fund_unified;\n``` | 返回 1 行 1 列，cnt ≈ 1407（当前环境日志显示为 1407） |
| 2 | 允许申购(allow_buy)的基金数量？ | ```sql\nSELECT COUNT(*) FROM public.hsbc_fund_unified WHERE allow_buy;\n``` | 返回整数，≥ 0；用于与 UI“可申购”数量对账 |
| 3 | 最近 NAV 日期（全库最大 nav_date）与对应基金数量？ | ```sql\nSELECT MAX(nav_date) AS max_nav_date, COUNT(*) FILTER (WHERE nav_date = MAX(nav_date))\n  OVER () AS fund_on_max_date\nFROM public.hsbc_fund_unified;\n``` | 返回 1 行，给出最新 NAV 日期与当日有 NAV 的基金个数 |
| 4 | 资产管理规模(AUM)前 10 名基金 | ```sql\nSELECT product_code, name, assets_under_mgmt AS aum, aum_ccy\nFROM public.hsbc_fund_unified\nORDER BY aum DESC NULLS LAST\nLIMIT 10;\n``` | 返回 ≤10 行，按 AUM 降序；字段非空占比高 |
| 5 | 各币种基金数量分布 | ```sql\nSELECT currency, COUNT(*) AS cnt\nFROM public.hsbc_fund_unified\nGROUP BY currency\nORDER BY cnt DESC;\n``` | 多行，currency 非空分布；可与前端筛选项对齐 |
| 6 | 各风险等级(risk_level)的平均费用率(expense_ratio) | ```sql\nSELECT risk_level, ROUND(AVG(expense_ratio)::numeric,4) AS avg_expense\nFROM public.hsbc_fund_unified\nGROUP BY risk_level\nORDER BY risk_level;\n``` | 多行；各等级平均费用率（忽略空值自动） |
| 7 | 同一基金公司(family_name)内费用率最低的前 3 只（每家公司） | ```sql\nSELECT family_name, product_code, name, expense_ratio\nFROM (\n  SELECT family_name, product_code, name, expense_ratio,\n         ROW_NUMBER() OVER(PARTITION BY family_name ORDER BY expense_ratio NULLS LAST) AS rn\n  FROM public.hsbc_fund_unified\n) t\nWHERE rn <= 3\nORDER BY family_name, rn;\n``` | 多行；每个 family_name 至多 3 行，费用率从低到高 |
| 8 | SELF JOIN：找出同公司同币种且 AUM 显著差异的基金对（AUM 相差 > 10x） | ```sql\nSELECT a.family_name, a.currency, a.product_code AS big_code, b.product_code AS small_code,\n       a.assets_under_mgmt AS big_aum, b.assets_under_mgmt AS small_aum\nFROM public.hsbc_fund_unified a\nJOIN public.hsbc_fund_unified b\n  ON a.family_name=b.family_name AND a.currency=b.currency\nWHERE a.assets_under_mgmt > 0 AND b.assets_under_mgmt > 0\n  AND a.assets_under_mgmt >= 10*b.assets_under_mgmt;\n``` | 多行或空；验证自连接逻辑与阈值过滤 |
| 9 | tradable_ccy（text[]）包含 USD 的基金比例 | ```sql\nSELECT ROUND(100.0*COUNT(*) FILTER (WHERE tradable_ccy @> ARRAY['USD'])/COUNT(*),2) AS pct_usd\nFROM public.hsbc_fund_unified;\n``` | 返回 1 行 1 列，单位 %，两位小数 |
| 10 | 52 周区间窄幅基金（高低价比值 < 1.05） | ```sql\nSELECT product_code, name, week_range_low, week_range_high,\n       CASE WHEN week_range_low>0 THEN week_range_high/week_range_low END AS ratio\nFROM public.hsbc_fund_unified\nWHERE week_range_low>0 AND week_range_high>0\n  AND week_range_high < 1.05*week_range_low\nORDER BY ratio ASC;\n``` | 多行或空；ratio 接近 1 代表波动小 |
| 11 | JSONB：存在 attribute_map 中键 'fundSwInMinAmt' 的基金 | ```sql\nSELECT product_code, (attribute_map ? 'fundSwInMinAmt') AS has_key\nFROM public.hsbc_fund_unified\nWHERE attribute_map ? 'fundSwInMinAmt';\n``` | 多行；每行 has_key=true，用于检验 JSONB 键存在查询 |
| 12 | JSONB：读取 attribute_map.fundSwInMinAmt 的数值并排序 | ```sql\nSELECT product_code, name,\n       (attribute_map->>'fundSwInMinAmt')::numeric AS sw_in_min_amt\nFROM public.hsbc_fund_unified\nWHERE attribute_map ? 'fundSwInMinAmt'\nORDER BY sw_in_min_amt DESC NULLS LAST\nLIMIT 20;\n``` | ≤20 行；验证 ->> 取文本并强转 numeric 的流程 |
| 13 | JSONB：top10_holdings 展平，统计每只基金的持仓条目数 | ```sql\nSELECT f.product_code, COUNT(*) AS holding_items\nFROM public.hsbc_fund_unified f\n     LEFT JOIN LATERAL jsonb_array_elements(f.top10_holdings->'items') AS it(elem) ON TRUE\nGROUP BY f.product_code\nORDER BY holding_items DESC, f.product_code\nLIMIT 20;\n``` | 多行；检查 jsonb_array_elements + LATERAL 的正确性 |
| 14 | JSONB：筛选前十大持仓里包含 'Apple' 的基金 | ```sql\nSELECT product_code, name\nFROM public.hsbc_fund_unified\nWHERE top10_holdings @> jsonb_build_object('items', jsonb_build_array(jsonb_build_object('name','Apple')));\n``` | 多行或空；存在性匹配，键名需与实际字段匹配（可调整为 ILIKE 模糊匹配） |
| 15 | JSONB：晨星总体评级(overall)≥4 的基金（示例结构） | ```sql\nSELECT product_code, name\nFROM public.hsbc_fund_unified\nWHERE (morningstar_ratings->>'overall')::int >= 4;\n``` | 多行或空；若实际键不同，改为 jsonb_path_query 等同义判断 |
| 16 | 费用率最高的前 5 只且允许申购 | ```sql\nSELECT product_code, name, expense_ratio\nFROM public.hsbc_fund_unified\nWHERE allow_buy\nORDER BY expense_ratio DESC NULLS LAST\nLIMIT 5;\n``` | ≤5 行；降序 |
| 17 | 各公司 AUM 总和 Top 10 | ```sql\nSELECT family_name, SUM(assets_under_mgmt) AS total_aum\nFROM public.hsbc_fund_unified\nGROUP BY family_name\nORDER BY total_aum DESC NULLS LAST\nLIMIT 10;\n``` | ≤10 行；验证 SUM 聚合 |
| 18 | 最近 1 年有 NAV（nav_date 在近 365 天）的基金数 | ```sql\nSELECT COUNT(*)\nFROM public.hsbc_fund_unified\nWHERE nav_date >= CURRENT_DATE - INTERVAL '365 days';\n``` | 返回整数；时间边界可按实际调整 |
| 19 | 每类一级分类(category_lv1_name)下 AUM 最大的基金 | ```sql\nSELECT DISTINCT ON (category_lv1_name) category_lv1_name, product_code, name, assets_under_mgmt\nFROM public.hsbc_fund_unified\nWHERE category_lv1_name IS NOT NULL\nORDER BY category_lv1_name, assets_under_mgmt DESC NULLS LAST;\n``` | 每个分类 1 行；使用 DISTINCT ON + 排序挑最大 |
| 20 | 允许申购且 AUM > 10 亿（本币）的高费用率 Top 3（业务综合） | ```sql\nSELECT product_code, name, assets_under_mgmt AS aum, expense_ratio\nFROM public.hsbc_fund_unified\nWHERE allow_buy AND assets_under_mgmt > 1e9\nORDER BY expense_ratio DESC NULLS LAST\nLIMIT 3;\n``` | ≤3 行；用于回归“可申购+规模阈值+排序”综合查询 |

---

## 说明
- JSONB 字段的真实键名请参考《DDL 文档》与源 JSON 示例；若字段名有差异，可将用例 #14/#15 的匹配方式替换为 jsonb_path_exists 或 ILIKE 模糊匹配：
  - 模糊持仓名：`EXISTS (SELECT 1 FROM jsonb_array_elements(top10_holdings->'items') it WHERE it->>'name' ILIKE '%Apple%')`
- 若需将“期望答案”固定为具体数值，可在导入/初始化数据后执行一次性校准并记录结果版本号
- 建议将本表用于 MCP Inspector 的工具回归：每行“问题”即用户 query，“Expected SQL” 用于校验 NL2SQL 生成的 SQL 是否一致/等价

