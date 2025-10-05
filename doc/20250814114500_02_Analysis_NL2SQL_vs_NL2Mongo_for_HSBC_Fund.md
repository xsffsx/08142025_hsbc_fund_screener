# NL2SQL vs NL2Mongo 技术对比（基于 hsbc_fund_unified 单表/单文档设计）

创建时间：2025-08-14 11:45:00

- 文档类型：Analysis
- 背景：已完成 PostgreSQL 单表设计（标量列 + JSONB 列）。本稿对比：基于该结构的 LangChain NL2SQL 与（假设迁移为）MongoDB 单文档的 NL2Mongo 两方案。

---

## 1. 对比分析表（开发时间、复杂性、稳定性评分）

| 维度 | NL2SQL（PostgreSQL 单表+JSONB） | NL2Mongo（Mongo 单文档+预计算） |
|---|---|---|
| 数据存储设计 | 单表+JSONB；复杂信息 JSONB 原样 | 单文档；嵌套对象/数组+预计算字段（cum_returns、risk_years、has_CA_in_top10 等） |
| 索引策略 | B-Tree（标量）+ GIN（JSONB） | 单键/复合索引 + 多键索引（数组字段）；对预计算键建索引 |
| 查询复杂度 | 依赖 JSONB 操作符/函数（->, ->>, @>, jsonb_array_elements 等）；常需 LATERAL | 聚合管道 $match/$project/$unwind/$filter；有预计算可显著简化为 $match/$project |
| 开发时间（原型） | 1-2 天（已有单表；增强 Prompt 与少量视图可选） | 2-3 天（ETL 预计算+索引+定制 Agent/工具与校验） |
| 技术复杂性 | 中等偏上：JSONB 路径与 LATERAL 细节易错；Prompt 需示例强化 | 中等：若预计算充分则管道简单；需自建 Tool/校验器 |
| LangChain 生态 | 成熟（SQLDatabaseToolkit、Query Checker 现成） | 无官方 NL2Mongo；需自定义 Tool（aggregate 执行/校验） |
| 查询准确率（样本类问题） | 中-高（强化 JSONB 示例后稳定在 80-90%） | 高（预计算+白名单提示后 85-95%） |
| 性能（典型查询） | 良好：标量过滤快；JSONB @>/路径配合 GIN 有效；LATERAL 复杂时受限 | 良好：多键索引对数组字段有效；预计算避免 $unwind，延迟更低 |
| 扩展性 | 高：新增字段可入 JSONB；可加标量列/视图 | 高：文档天然灵活；预计算可按需追加并建索引 |

注：准确率/性能因 Prompt/索引/预计算覆盖度而变动。

---

## 2. 典型 NL 查询示例对比

以下示例以 U43051 数据结构为基准，分别给出 SQL 与聚合管道（Mongo 优先使用预计算字段）。

### 2.1 风险等级=5 且前十大持仓包含加拿大市场

- SQL（PostgreSQL）
```sql
SELECT f.product_code, f.name
FROM hsbc_fund_unified f
WHERE f.risk_level = 5
  AND EXISTS (
    SELECT 1 FROM jsonb_array_elements(f.top10_holdings->'items') AS item
    WHERE item->>'market' = 'CA'
  )
LIMIT 50;
```

- Mongo（预计算字段 has_CA_in_top10=true）
```json
[
  { "$match": { "risk_level": 5, "has_CA_in_top10": true } },
  { "$project": { "_id": 0, "product_code": 1, "name": 1 } },
  { "$limit": 50 }
]
```

### 2.2 最近 1 年累计收益率 > 40%

- SQL（从 summary_cumulative.items 解析）
```sql
SELECT f.product_code, f.name,
       (item->>'totalReturn')::NUMERIC AS rtn_1y
FROM hsbc_fund_unified f
CROSS JOIN LATERAL jsonb_array_elements(f.summary_cumulative->'items') AS item
WHERE item->>'period' = '1Y'
  AND (item->>'totalReturn')::NUMERIC > 40
ORDER BY rtn_1y DESC
LIMIT 50;
```

- Mongo（预计算 cum_returns.1Y）
```json
[
  { "$match": { "cum_returns.1Y": { "$gt": 40 } } },
  { "$project": { "_id": 0, "product_code": 1, "name": 1, "rtn_1y": "$cum_returns.1Y" } },
  { "$sort": { "rtn_1y": -1 } },
  { "$limit": 50 }
]
```

### 2.3 地区暴露（CA/US/UK）

- SQL（从 holding_allocation 取 methods='regionalExposures' 展开）
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
GROUP BY equity_net
LIMIT 50;
```

- Mongo（预计算 region_exposure 字段）
```json
[
  { "$project": {
      "_id": 0, "product_code": 1, "name": 1,
      "equity_net": { "$toDouble": "$investment_strategy.assetAllocEquityNet" },
      "CA": "$region_exposure.CA",
      "US": "$region_exposure.US",
      "UK": "$region_exposure.UK"
  }},
  { "$limit": 50 }
]
```

### 2.4 近 30 天 NAV 时序

- SQL（chart_timeseries 过滤最近 30 天）
```sql
SELECT (d->>'date')::DATE AS dt,
       (d->>'navPrice')::NUMERIC AS nav
FROM hsbc_fund_unified f,
     LATERAL jsonb_array_elements(f.chart_timeseries) AS d
WHERE f.product_code='U43051'
  AND (d->>'date')::DATE >= CURRENT_DATE - INTERVAL '30 days'
ORDER BY dt;
```

- Mongo（$project+$filter）
```json
[
  { "$match": { "product_code": "U43051" } },
  { "$project": {
      "_id": 0,
      "series": {
        "$filter": {
          "input": "$chart_timeseries",
          "as": "d",
          "cond": { "$gte": [ { "$toDate": "$$d.date" }, { "$dateSubtract": { "startDate": "$$NOW", "unit": "day", "amount": 30 } } ] }
        }
      }
  }}
]
```

---

## 3. 技术选型推荐

- 若当前数据已在 PostgreSQL：优先 NL2SQL。原因：
  - 工期最短（1-2 天即可上手展示）
  - 复用既有单表与 JSONB 设计；仅需增强 Prompt（加入 JSONB 示例/指南）与可选视图
  - LangChain SQL 工具成熟，迭代成本低

- 若计划迁移/新存 Mongo：优先 NL2Mongo（配合预计算字段）。原因：
  - 预计算字段可显著降低聚合复杂度与 LLM 生成难度，提升稳定性与性能
  - 多键索引对典型数组内匹配（如前十大持仓市场）更直观有效

总体建议：短期演示以“现状为王”。已有 PG 就选 NL2SQL；若必须 Mongo，则务必做“预计算+索引+白名单管道”三件套。

---

## 4. 3-7 天快速原型实施计划（优先级）

1) P0（Day 1-2）：快速可用演示（二选一）
   - PG 方案：
     - 增强 SQL Prompt：加入 JSONB 操作符/函数/LATERAL 示例（1-2 页）
     - 定制 Query Checker 提示点（cast、路径、@>、LIMIT）
     - 准备 10-20 条黄金 NL→SQL 用例并回归
   - Mongo 方案：
     - 设计并生成预计算字段（cum_returns、risk_years、has_CA_in_top10、region_exposure）
     - 建立关键索引（risk_level、cum_returns.1Y、has_CA_in_top10 等）
     - 定制 NL2Mongo Agent（aggregate 工具+JSONSchema 校验+白名单阶段）

2) P1（Day 3-4）：稳定性提升
   - 补充同义词/别名映射（中文→字段）
   - 失败重试策略（先预计算字段路径→再原始数组展开）
   - 增加 10+ 回归样例（边界与负例）

3) P2（Day 5-7）：体验与性能
   - 视图（PG）或衍生字段（Mongo）进一步覆盖高频问法
   - 指标与日志（错误类型、修复率、P95 延时）
   - 小规模用户演示与反馈迭代

---

## 5. 风险评估与缓解

- LLM 生成不稳定（路径/类型/运算符误用）
  - 缓解：强化示例；白名单阶段/操作符；执行前 explain/校验；自动重试
- JSONB/聚合性能问题
  - 缓解：PG 用 GIN 与 @>；避免过度 LATERAL；Mongo 用多键索引与预计算减少 $unwind
- 语义歧义（“收益”口径不同）
  - 缓解：字段词典与口径约定；在 Prompt 中显式约束“默认口径”（如用 cumulative ‘1Y’）
- 数据异构/缺失
  - 缓解：ETL 统一标准化；空值处理策略；保底回退逻辑（无数据即解释说明）

---

以上内容面向“核心查询功能快速演示”，暂不覆盖运维域（监控/备份/安全）。
