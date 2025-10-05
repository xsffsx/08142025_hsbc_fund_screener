文件: `/Users/paulo/PycharmProjects/20250809_MCP/08132025_hsbc_fund_screener/20251005_fund_view_sql/Fund attributes analysis.xlsx`

## 工作表: Sheet1
- 维度: 行 118, 列 8
- 缺失值（前10列）:
  - `Need enum?`: 118 (100.00%)
  - `Keep?`: 115 (97.46%)
  - `Unnamed: 3`: 9 (7.63%)
  - `Unnamed: 2`: 8 (6.78%)
  - `Unnamed: 6`: 7 (5.93%)
  - `## FUND_PRODUCTS_NL2SQL`: 0 (0.00%)
  - `Unnamed: 1`: 0 (0.00%)
  - `Unnamed: 7`: 0 (0.00%)
- 数值型摘要（样例）:
  - `Need enum?`: count:0.0000, mean:nan, std:nan, min:nan, 25%:nan, 50%:nan, 75%:nan, max:nan
- 低基数分布（样例）:
  - `Unnamed: 1`: numeric(70), character varying(200)(12), text(7), bigint(6), character(1)(5), date(3), numeric(38,0)(2), numeric(19,5)(2), character varying(20)(2), character varying(30)(2)
  - `Keep?`: NULL(115), N(2), Y(1)
  - `Need enum?`: NULL(118)
  - `Unnamed: 7`: 2(47), 1(31), 1420(12), 0(7), 5(3), 39(2), 36(2), 4(2), distinct_count(1), 209(1)

## 工作表: fund_master_view
- 维度: 行 132, 列 9
- 缺失值（前10列）:
  - `ENUM?`: 128 (96.97%)
  - `Field Name`: 0 (0.00%)
  - `Column Name`: 0 (0.00%)
  - `Data Type`: 0 (0.00%)
  - `Description`: 0 (0.00%)
  - `Table`: 0 (0.00%)
  - `MDS API`: 0 (0.00%)
  - `MDS API attributes`: 0 (0.00%)
  - `Unnamed: 8`: 0 (0.00%)
- 低基数分布（样例）:
  - `Data Type`: Decimal(101), Text(20), Integer(7), Date(4)
  - `Table`: fund_quote_summary(59), fund_holding_allocation(52), fund_quote_detail(13), fund_product(8)
  - `MDS API`: /fundQuoteSummary(59), /holdingAllocation(52), /quoteDetail(13), /product(8)
  - `ENUM?`: NULL(128), Y(4)

## 工作表: fund_class_view
- 维度: 行 3, 列 8
- 缺失值（前10列）:
  - `ENUM?`: 3 (100.00%)
  - `MDS API`: 1 (33.33%)
  - `MDS API attributes`: 1 (33.33%)
  - `Field Name`: 0 (0.00%)
  - `Column Name`: 0 (0.00%)
  - `Data Type`: 0 (0.00%)
  - `Description`: 0 (0.00%)
  - `Table`: 0 (0.00%)
- 数值型摘要（样例）:
  - `ENUM?`: count:0.0000, mean:nan, std:nan, min:nan, 25%:nan, 50%:nan, 75%:nan, max:nan
- 低基数分布（样例）:
  - `Field Name`: Fund code(1), fund class name(1), fund class fund code(1)
  - `Column Name`: fund_code(1), fund_class_name(1), fund_class_fund_code(1)
  - `Data Type`: Text(3)
  - `Description`: Foreign key from fund_master_view(1), The name assigned to the specific class of the fund(1), The fund code of  the specific class of the fund(1)
  - `Table`: fund_class(2), fund_product(1)
  - `MDS API`: /otherFundClasses(2), NULL(1)

## 工作表: fund_top_ten_holding_view
- 维度: 行 5, 列 8
- 缺失值（前10列）:
  - `ENUM?`: 5 (100.00%)
  - `MDS API`: 1 (20.00%)
  - `MDS API attributes`: 1 (20.00%)
  - `Field Name`: 0 (0.00%)
  - `Column Name`: 0 (0.00%)
  - `Data Type`: 0 (0.00%)
  - `Description`: 0 (0.00%)
  - `Table`: 0 (0.00%)
- 数值型摘要（样例）:
  - `ENUM?`: count:0.0000, mean:nan, std:nan, min:nan, 25%:nan, 50%:nan, 75%:nan, max:nan
- 低基数分布（样例）:
  - `Field Name`: Fund code(1), Holding name(1), Market value(1), Market value currency(1), Weighting(1)
  - `Column Name`: fund_code(1), holding_name(1), market_value(1), market_value_currency(1), weighting(1)
  - `Data Type`: Text(3), Decimal(2)
  - `Description`: Foreign key from fund_master_view(1), The name of the specific asset or security held within the fund.(1), The total value of the holding in the fund. This value must be shown with market value currency.(1), The currency in which the market value of the holding is denominated(1), The percentage of the total fund's assets that is allocated to a particular holding(1)
  - `Table`: fund_top_ten_holdings(4), Foreign key from fund_master_view(1)
  - `MDS API`: /topTenHolding(4), NULL(1)

## 工作表: fund_calendar_returns_view
- 维度: 行 3, 列 8
- 缺失值（前10列）:
  - `ENUM?`: 3 (100.00%)
  - `MDS API`: 1 (33.33%)
  - `MDS API attributes`: 1 (33.33%)
  - `Field Name`: 0 (0.00%)
  - `Column Name`: 0 (0.00%)
  - `Data Type`: 0 (0.00%)
  - `Description`: 0 (0.00%)
  - `Table`: 0 (0.00%)
- 数值型摘要（样例）:
  - `ENUM?`: count:0.0000, mean:nan, std:nan, min:nan, 25%:nan, 50%:nan, 75%:nan, max:nan
- 低基数分布（样例）:
  - `Field Name`: Fund code(1), Calendar year(1), Calendar year return(1)
  - `Column Name`: fund_code(1), calendar_year(1), calendar_year_return(1)
  - `Data Type`: Text(1), Date(1), Decimal(1)
  - `Description`: Foreign key from fund_master_view(1), Denote the calendar year of the return figure(1), Denote the the performance of that calendar year in percentage(1)
  - `Table`: fund_quote_detail(2), fund_product(1)
  - `MDS API`: /quoteDetail(2), NULL(1)

## 工作表: fund_manager_view
- 维度: 行 3, 列 8
- 缺失值（前10列）:
  - `ENUM?`: 3 (100.00%)
  - `MDS API`: 1 (33.33%)
  - `MDS API attributes`: 1 (33.33%)
  - `Field Name`: 0 (0.00%)
  - `Column Name`: 0 (0.00%)
  - `Data Type`: 0 (0.00%)
  - `Description`: 0 (0.00%)
  - `Table`: 0 (0.00%)
- 数值型摘要（样例）:
  - `ENUM?`: count:0.0000, mean:nan, std:nan, min:nan, 25%:nan, 50%:nan, 75%:nan, max:nan
- 低基数分布（样例）:
  - `Field Name`: Fund code(1), Fund manager(1), Fund manager start date(1)
  - `Column Name`: fund_code(1), fund_manager(1), fund_manager_start_date(1)
  - `Data Type`: Text(2), Date(1)
  - `Description`: Foreign key from fund_master_view(1), Fund manager of the fund(1), Denote when the fund manager start managing the fund(1)
  - `Table`: fund_quote_summary(2), fund_product(1)
  - `MDS API`: /fundQuoteSummary(2), NULL(1)