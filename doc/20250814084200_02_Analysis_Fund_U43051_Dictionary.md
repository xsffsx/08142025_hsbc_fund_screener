# U43051 attributeMap 字段业务含义与取值对照（amh_ut_product）

创建时间：2025-08-14 08:42:00
来源文件：output/001_U43051.done_amh_ut_product_response_body.json

## 目录
- 字段对照表
- 备注

## 子标题与结构规范（Request / Response / 耗时（秒））
- Request｜请求：接口请求的关键信息（URL/参数/方法等）。本文件以 response_body 为主，若未捕获请求信息，统一标注“暂无（请参考同名 .response.json 原始文件）”。
- Response｜响应：字段业务含义与实际取值的对照表（当前文件的主要内容）。
- 耗时（秒）｜Latency：若无显式时长字段，统一标注“暂无（可从同名 .response.json 的 headers/时间戳估算）”。



## 字段对照表

| 字段名 | 业务含义 | 值 |
| --- | --- | --- |
| allowSellProdInd | 是否允许卖出该产品 | Y |
| prodBidPrcAmt | 买入/买价净值（若为空由其他字段补充） |  |
| prcEffDt | 价格生效日期（NAV/报价对应日期） | 2025-08-12 |
| piFundInd | 私人银行/特定投资者基金标识 |  |
| utRdmMinNum | 基金赎回最小份额（单位） |  |
| fundSwOutMinAmt | 基金转换（换出）最低金额 | 1000 |
| fundCatCde | 基金内部类别代码 |  |
| fundSwInMinAmt | 基金转换（换入）最低金额 | 1000 |
| minMipAmountCurrency | MIP（定投）最低金额的币种 | HKD |
| topSellProdInd | 是否热门销售产品 | N |
| ccyProdTradeCde | 可交易币种列表 | ["HKD","USD"] |
| gbaAcctTrdb | 大湾区账户可交易标识 | N |
| finDocURL_INTRMRPT | 中期报告（Interim Report）链接 | https://www.hfi.hsbc.com.hk/data/hk/invest/unit/ut_ir_merrl001_en.pdf |
| finDocURL_FACTSHEET | 基金资料概要（Factsheet）链接 | https://www.hfi.hsbc.com.hk/data/hk/invest/unit/ut_fs_43051_en.pdf |
| prodStatCde | 产品状态代码（A=可用/在售等） | A |
| ccyInvstCde | 投资/计价货币代码 | USD |
| utSwOutRtainMinNum | 转换后最低保留份额（单位） | 0 |
| productDocumentSid | 产品文档内部标识 |  |
| allowSellMipProdInd | 是否允许卖出MIP产品 | Y |
| siFundInd | 智能投顾/系统投资基金标识 | N |
| fundCatDesc | 基金类别描述 | null |
| dcmlPlaceTradeUnitNum | 交易单位小数位数 | 2 |
| productPerformanceId | 第三方业绩ID/跟踪ID | 0P00000B0I |
| riskLvlCde | 风险等级代码 | 5 |
| prodName | 产品名称 | BlackRock World Gold Fund (Class A2) |
| prodNavPrcAmt | 最新净值/报价 | 64.36 |
| utRtainMinNum | 账户最低保留份额（单位） | 0 |
| restrOnlScribInd | 网上认购限制标识 |  |
| fundHouseName | 基金公司名称 | BlackRock |
| fundHouseCde | 基金公司代码 | MERRL |
| rdmMinAmt | 赎回最低金额 | 1000 |
| ccyProdCde | 产品货币代码 | USD |
| invstMipIncrmMinAmt | MIP（定投）最小递增金额 | 1000 |
| deAuthFundInd | 非授权基金标识 |  |
| finDocURL_PROSPECTUS | 招募说明书（Prospectus）链接 | https://www.hfi.hsbc.com.hk/data/hk/invest/unit/ut_pr_merrl001_en.pdf |
| fundSwOutRtainMinAmt | 转换后最低保留金额 | 1000 |
| setlLeadTmScrib | 申购结算提前期（工作日） | 1 |
| invstMipMinAmt | MIP（定投）最低金额 | 1000 |
| fundRtainMinAmt | 账户最低保留金额 | 1000 |
| allowBuyProdInd | 是否允许买入该产品 | Y |
| ccyProdMktPrcCde | 产品市场价格货币代码 | USD |
| setlLeadTmRdm | 赎回结算提前期（工作日） | 3 |
| allowSwOutProdInd | 是否允许基金转换（换出） | Y |
| finDocURL_ANULRPT | 年度报告（Annual Report）链接 | https://www.hfi.hsbc.com.hk/data/hk/invest/unit/ut_ar_merrl001_en.pdf |
| esgInd | ESG 标识（若有则表示ESG相关） |  |
| invstInitMinAmt | 初始最低申购金额 | 1000 |

## 备注
- 值来自当前快照，后续可能随银行侧数据更新而变化。
- 空字符串/Null 表示接口当前未返回具体值或不适用；若需业务默认值，请参考基金销售细则或界面规则。


