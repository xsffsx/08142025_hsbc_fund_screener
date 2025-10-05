# SGH

**文档创建时间**: 2025-08-06 00:34:28  
**源文件**: Fund Questions v2.xlsx  
**工作表**: 5/7

## 目录
- [数据概览](#数据概览)
- [数据表格](#数据表格)
- [数据统计](#数据统计)

## 数据概览

- **行数**: 47
- **列数**: 8
- **工作表名称**: SGH

### 列信息
1. **No.** - 类型: `int64`, 非空值: 47
2. **Frequently/commonly asked questions** - 类型: `object`, 非空值: 47
3. **Expected Response from AI model** - 类型: `object`, 非空值: 44
4. **Data source  (Morning *, WPC, Fund Fact Sheet, etc.)** - 类型: `object`, 非空值: 44
5. **Comments/Field/Logic (If Any)** - 类型: `object`, 非空值: 30
6. **Remark** - 类型: `object`, 非空值: 5
7. **Category** - 类型: `object`, 非空值: 45
8. **Unnamed: 7** - 类型: `object`, 非空值: 3

## 数据表格

| No. | Frequently/commonly asked questions | Expected Response from AI model | Data source  (Morning *, WPC, Fund Fact Sheet, etc.) | Comments/Field/Logic (If Any) | Remark | Category | Unnamed: 7 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 32 | What is the current CIO view and some products fulfilment | Current CIO view is : asset class, sector / region etc… Product fulfilment: top few funds that ad... | CIO View Morningstar |  | WPS Next Gen Leaders | CIO Views + product related category (depending on the CIO view) |  |
| 38 | based on this months' House View what are the funds I should look at ? | There are 3 key theme for CIO views this <Month> data as at DDMMYY theme A outlines <Topic name> ... | CIO View Morningstar | pull products aligned to Themes in IM classify under overweight neutral underweight |  | CIO Views + product related category (depending on the CIO view) |  |
| 6 | What funds have the highest exposure to small caps/China/Apple/NVIDIA/silicon valley | The following funds below has the highest exposure based on the criteria you have indicated: - <F... | Morningstar | holdings sector review top 10 holdings section |  | Exposure |  |
| 7 | What funds do not have exposure to small caps/China/Apple/NVIDIA/CoCos | These are the list of funds without exposure to the criteria you have indicated - <Fund A> <Table... | Morningstar | holdings sector review top 10 holdings section |  | Exposure |  |
| 20 | Which fund has the highest exposure to Apple? | These are the list of funds with a high exposure to Apple sorted by the Funds product risk rating... | Morningstar | review field top 10 holdings sort against hhhh risk rating field |  | Exposure |  |
| 26 | Give me a list of 10 funds with highest exposure to Emerging markets in the world | As of DDMMYY these are the list of 10 funds ranked by highest exposure to EM Table format 1 ABC- ... | Morningstar | Asset class field sort by EM exposure and filter to top 10 ranked |  | Exposure |  |
| 28 | Which funds have more than 40% of Exposure in Europe | these are the list of funds with more than 40% exposure to Europe based on a geographical allocat... | Morningstar | assey class field geography filter categorise sort via highest exposure to Europe <IshaniK> - Sho... |  | Exposure |  |
| 30 | I'm interested in funds with a focus on technology and healthcare sectors, where both sector expo... | these are the list of funds ranked by sector exposure to both technology + healthcare exceeds 15%... | Morningstar | table format, ranked by both factors being equal use holdings sctor allocation |  | Exposure |  |
| 34 | Show me # of funds that are in <sector> or <region> | Fund A: description of fund and explanation of sector/region Fund B: description …. Fund C: descr... | Morningstar |  | WPS Next Gen Leaders | Exposure |  |
| 42 | Tell me funds that has no correlation to Trump's comment | Based on the Unit Trust Quarterly Focus Funds list, these are the focus funds for Quarter <Number... | Market News sources / Special coverage? | <IshaniK> - Do you mean Focus Funds list? Needs follow up questions Follow up clarifying question... |  | External factors | Need to understand the source of maket news as the product / Asset class is based on the CIO hous... |
| 31 | Show me funds with expense ratio between 1 and 2 and annualized 3 year returns of more than 10% | These are the list of funds to the criteria you have indicated - <Fund A> <Table of exposure of f... | Morningstar |  |  | Fees & charges, Performance | Could we specifiy the fee & charge mention here? |
| 35 | What is the track record of <fund manager> | by total return, NAV performance | Morningstar |  | WPS Next Gen Leaders | Fund Management | This question may not in current scope as the fund manger track record is not covered in the curr... |
| 39 | are there any funds I should look at? | Based on the Unit Trust Quarterly Focus Funds list, these are the focus funds for Quarter <Number... |  | this data source I believe is internal, @shawn? |  | Generic |  |
| 40 | whats good about fund ABC? | PRovide historical return, and performance ratio, total return, yield return | Morning star CIO view |  |  | Generic |  |
| 15 | How many holdings does the Fund have | <Morningstar description of the Fund>. The Fund has X holdings as of <Date> | Morningstar |  |  | Holdings |  |
| 18 | Which funds have 'Microsoft' in their 10 holdings? | Here's a list of funds with <Stock Name> in their top ten holdings: - <Fund A \| % of fund in <Sto... | Morningstar | review field top 10 holdings |  | Holdings |  |
| 19 | Which funds have 'Meta' in their top 10 holdings? | Here's a list of funds with <Stock Name> in their top ten holdings: - <Fund A \| % of fund in <Sto... | Morningstar | review field top 10 holdings |  | Holdings |  |
| 1 | What are the top funds with the highest distribution payout? | Top Multi-Asset funds by Yield (as of <Date>) - <Fund A> - <Fund B> Top Fixed Income funds by Yie... | Morningstar | All information should be found from Morningstar |  | Income and distribution |  |
| 25 | Show me funds with expense ratio under 1 but dividend yield more than 5% | As of DDMMYY these are the list funds fitered by a expense ratio of under 1 and with a dividend y... | Morningstar | use section: fees and charges pull expense ratio pull dividend yield under Dividend information. ... |  | Income and distribution |  |
| 43 | What is the average dividend payment of X fund in the past 5 years | in term of % and dollar | Morning star |  |  | Income and distribution |  |
| 29 | Suggest funds that are suitable for investors who are looking for a balanced approach to investing. | a balanced apporach to investing would refer to funds on a multi asset category these are the mul... | Morningstar | filter via asset class field sort via PRS ,5 highest show correspondng YTD performance |  | Investment Strategy |  |
| 2 | What is the ex-dividend/payment date of Fund X? | The ex-dividend date for <Fund> was <Date> and the last dividend payment date was <Date>. The fun... | Morningstar | Dividend information section |  | Performance |  |
| 8 | What are some growth/value oriented funds | Growth funds invest in stocks of companies expected to grow faster than the market, focusing on c... | Morningstar | <IshaniK> - Check with Shawn |  | Performance |  |
| 9 | Which funds are 5 star morningstar rated and beat their benchmarks? | 5-Star Morningstar funds are funds in the top 10% of their category. They reflect strong past per... | Morningstar |  |  | Performance |  |
| 10 | Which funds performed the best on the upside? | Upside capture is a measure of how well a fund performs compared to a benchmark during periods wh... | Morningstar |  |  | Performance |  |
| 11 | Which funds performed the best on the downside? | Downside capture is a measure of how well a fund performs compared to a benchmark during periods ... | Morningstar | <IshaniK> - Definition of terminology (Upside) is good to have |  | Performance |  |
| 12 | Which funds have sharpe ratios better than the benchmark | Sharpe ratio measures a fund's risk-adjusted returns by dividng its excess returns (above the ris... | Morningstar | <IshaniK> - Definition of terminology (Downside) is good to have |  | Performance |  |
| 21 | I'm looking for funds that have a Morningstar rating of 4 or 5 stars. | As of DDMMYY these are the list of funds with a minimum rating of 4 stars 5 star- 4 star - | Morningstar | From Morning star rating, filter 4 & 5 stars, present in 2 groups |  | Performance |  |
| 22 | Give a list of 5 Blackrock funds with highest 5-year return | As of DDMMYY these are the list Blackrock funds sorted by the top 10 highest 5 year returns Black... | Morningstar | filter by Fund House sort by 5 year return growth |  | Performance |  |
| 23 | Which funds have beaten their benchmark in each of recent 3 years | data as at ddmmyy these are the list of funds that have beaten their respective benchmarks in the... | Morningstar | locate benachmark group via benchmark sort via alpha in each group |  | Performance |  |
| 24 | Give me a list of 10 funds that have highest 1 year return | As of DDMMYY these are the list funds sorted by the highest year returns 1 ABC- 1 year return % 2... | Morningstar | extract & sort by 1 year return growth |  | Performance |  |
| 33 | what is the historical performance of fund xxx | Provide historical return, and performance ratio, total return, yield return | Morningstar |  | WPS Next Gen Leaders | Performance |  |
| 36 | how much did the fund perform between <start date> and <end date>, and what are some of the factors | Fund performance (NAV) impacted by : whichever stock underlying stock price has shifted , or any ... | Morningstar | <IshaniK> - Factors to be sourced from CIO Houseviews (if any) | WPS Next Gen Leaders | Performance |  |
| 37 | Which Fund has the highest alpha in the past 1 year ? | Currently as at <date>, fund A, B & C has the highest alpha, performance against their respective... | Morningstar | highlighting the benchmark would be key as benchmarks are different a cross comparison would be i... |  | Performance |  |
| 41 | Which multi-asset fund has the best performance since inception and actively managed | these are the top 2 multi-asset funds Fund A incepted in <Date> Performance in percentage PRS Fun... | Morning star CIO view | to show 2 given that RM may use this filtered data for client presentation show PRS to allow for ... |  | Performance |  |
| 44 | Which are the best performing funds with AI exposure | these are the top 2 Funds with AI exposure Fund A incepted in <Date> Performance in percentage PR... | Morning star | <IshaniK> - Sector Exposure |  | Performance |  |
| 45 | Can you list the funds with the highest ytd performance but with the lowest fund charges? | these are the top 3 funds as top performance in the past 1 year Fund A incepted in <Date> Perform... | Morning star |  |  | Performance, Fees and Charges |  |
| 14 | What is the duration of Fund X | A fund's duration measures sensitivity to interest rate changes, expressed in years, indicating h... | Morningstar | <IshaniK> - Definition of terminology (Duration) is good to have |  | Portfolio metrics |  |
| 3 | What are the list of funds with PRS 3 and below? | Here is a list of funds with a product risk scoring (PRS) of <PRS Criteria> in hhhh Singapore 1. ... | Morningstar |  |  | Risk |  |
| 4 | What are the list of funds with a volatility below X%? | Here's a list of funds with volatility below <X%> over 3 years: 1. <Fund> \| Standard Deviation: X... | Morningstar | section risk return profile, filter via 3 years |  | Risk |  |
| 5 | What is the maximum drawdown ? | Here is the information you requested: - 1Y Max Drawdown: - 3Y Max Drawdown: - 5Y Max Drawdown: | Morningstar | risk measure peak value-trough value/trough value |  | Risk |  |
| 13 | What is the credit rating of Fund X | The credit rating of a fund reflects the average credit quality of the bonds it hoklds, based on ... | Morningstar | <IshaniK> - Definition of terminology (Credit rating) is good to have |  | Risk |  |
| 17 | Show me funds with risk rating 2, asset class X’ and aligned to House views. | <Description of Risk Rating> Risk rating 2 is suitable for clients with a low risk tolerance Here... | Morningstar |  |  | Risk, Asset class, CIO views |  |
| 16 | Show me funds with risk rating 3 and below but aligned to latest CIO House views. | CIO Views <as of date> Overweight: - A - B Neutral: Underweight: Here are the list of <PRS> rated... | Morningstar |  |  | Risk, CIO views |  |
| 27 | I'm interested in funds that invest in companies that are committed to reducing their carbon foot... |  | Morningstar |  |  | Sustainability |  |
| 46 | Show me funds that align with House views overweight (asset class) and have expense ratio under 1? |  |  |  |  |  |  |
| 47 | Show me funds that align with House views themes and have expense ratio under 1? |  |  |  |  |  |  |

## 数据统计

### 数值列统计

|       |     No. |
|-------|---------|
| count | 47      |
| mean  | 24      |
| std   | 13.7113 |
| min   |  1      |
| 25%   | 12.5    |
| 50%   | 24      |
| 75%   | 35.5    |
| max   | 47      |

### 文本列信息

- **Frequently/commonly asked questions**: 47 个唯一值, 最常见值: `Can you list the funds with the highest ytd performance but with the lowest fund charges? `
- **Expected Response from AI model**: 43 个唯一值, 最常见值: `Here's a list of funds with <Stock Name> in their top ten holdings:
- <Fund A | % of fund in <Stock>: >`
- **Data source  (Morning *, WPC, Fund Fact Sheet, etc.)**: 5 个唯一值, 最常见值: `Morningstar`
- **Comments/Field/Logic (If Any)**: 28 个唯一值, 最常见值: `holdings sector review
top 10 holdings  section `
- **Remark**: 1 个唯一值, 最常见值: `WPS Next Gen Leaders`
- **Category**: 17 个唯一值, 最常见值: `Performance`
- **Unnamed: 7**: 3 个唯一值, 最常见值: `Could we specifiy the fee & charge mention here?`

---
*由 Excel转Markdown工具 生成于 2025-08-06 00:34:28*
