/**
 * @api {get} /v4.0/fundQuoteSummary Fund Quote Summary
 * @apiName Fund Quote Summary
 * @apiGroup Quote
 * @apiVersion 4.0.7
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {String} market The investment market of the product.</br>
 * 										 GL - Global</br>	
 * 										 NA - North America</br>
 * 										 EU - Europe</br>	
 * 										 AP - Asia Pacific</br>
 * 										 AE - Asia (excl Japan)</br>
 * 									     EM - Emerging Market</br>
 * 										 UK - UK</br>
 * 										 JP - Japan</br>
 * 										 CN - China</br>
 * 										 HK - Hong Kong</br>	
 * 										 OS - Other Single Countries</br>
 * 										 OT - Others
 * @apiParam {String} productType Product Type. If this field is null, then the productType require to define in productKey.
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * 
 * @apiParamExample Request:
 *  {
 *		"market":"CN",
 *		"productType": "UT",
 *		"prodCdeAltClassCde":"M",
 *		"prodAltNum":"540002"
 *	}	
 * 
 * @apiSuccess {Object} summary
 * @apiSuccess {Number} summary.bid The bid price.
 * @apiSuccess {Number} summary.weekRangeLow 52-Week Low price.
 * @apiSuccess {Number} summary.weekRangeHigh 52-Week High price.
 * @apiSuccess {Number} summary.offer The offer price.
 * @apiSuccess {Object} summary.calendarYearTotalReturns
 * @apiSuccess {Object[]} summary.calendarYearTotalReturns.items
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.year The year of return data.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.fundCalendarYearReturn Fund Returns by Calendar Year.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.bestFitIndexCalendarYearReturn Best-Fit Index Returns by Calendar Year.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.categoryCalendarYearReturn Category/Asset Class Returns by Calendar Year.
 * @apiSuccess {String} summary.calendarYearTotalReturns.bestFitIndex Market index showing the highest correlation with a fund over the most recent 36 months, as measured by the highest R-squared.
 * @apiSuccess {String} summary.calendarYearTotalReturns.lastUpdatedDate Date information was last updated.
 * @apiSuccess {Object} summary.profile
 * @apiSuccess {String} summary.profile.bestFitIndex Market index showing the highest correlation with a fund over the most recent 36 months, as measured by the highest R-squared.
 * @apiSuccess {String} summary.profile.bestFitIndexCode The best fit index code.
 * @apiSuccess {String} summary.profile.categoryCode 10 character code assigned to each Morningstar Category.
 * @apiSuccess {String} summary.profile.categoryName Mutual Fund Category.
 * @apiSuccess {String} summary.profile.familyName Fund House Name.
 * @apiSuccess {String} summary.profile.familyCode Fund House Code.
 * @apiSuccess {String} summary.profile.advisor Mutual Fund Advisor (or Sponsor).
 * @apiSuccess {String} summary.profile.subAdvisor Mutual Fund Sub-advisor(s).
 * @apiSuccess {String} summary.profile.inceptionDate Fund Inception Date.
 * @apiSuccess {String} summary.profile.investmentObjectiveAndStrategy Description of fund objective and strategy as provided by Morningstar.
 * @apiSuccess {String} summary.profile.hhhhCategoryCode hhhh Fund Category Code. 
 * @apiSuccess {String} summary.profile.hhhhCategoryName hhhh Fund Category Name.
 * @apiSuccess {Object} summary.holdingDetails
 * @apiSuccess {String} summary.holdingDetails.totalNetAssets Total asset base of the fund, net of fees and expenses. 
 * @apiSuccess {String} summary.holdingDetails.totalNetAssetsCurrencyCode The currency code of TotalNet Asset.
 * @apiSuccess {Number} summary.holdingDetails.annualPortfolioTurnover Rate at which the fund replaces its holdings on an annual basis.
 * @apiSuccess {String} summary.holdingDetails.equityStyle Morningstar Equity Style Box, a 9-square grid that graphically represents the fund's equity investment style.
 * @apiSuccess {String} summary.holdingDetails.fixedIncomeStyle  (label: Credit Quality/Duration)Morningstar Fixed Income Style Box , a 9-square grid that graphically represents the fund's fixed income investment style , Credit Quality/ Duration = Overall credit quality and sensitivity of price to interest rate changes.
 * @apiSuccess {Number} summary.holdingDetails.sharesOutstanding Shares outstanding.
 * @apiSuccess {Number} summary.holdingDetails.netAssetValue ETF's per share value.
 * @apiSuccess {String} summary.holdingDetails.netAssetValueCurrencyCode The currency code of Net Asset Value.
 * @apiSuccess {String} summary.holdingDetails.premiumDiscountToNAV Percentage at which market value is above or below NAV.
 * @apiSuccess {Number} summary.holdingDetails.priceEarningsRatio Compare price to earning per share.
 * @apiSuccess {Number} summary.holdingDetails.dividendYield Annual dividend payment relative to share price.
 * @apiSuccess {String} summary.holdingDetails.dividendPerShare Dividend payment.
 * @apiSuccess {String} summary.holdingDetails.dividendPerShareCurrencyCode The currency code of dividend per share.
 * @apiSuccess {String} summary.holdingDetails.exDividendDate Ex-dividend date.format: yyyy-MM-dd
 * @apiSuccess {String} summary.holdingDetails.dividendPaymentDate Dividend payment date.format: yyyy-MM-dd
 * @apiSuccess {Number} summary.holdingDetails.beta ETF's volatility (systemic risk) relative to the overall market.
 * @apiSuccess {String} summary.holdingDetails.taxAdjustedRating Tax-Adjusted rating.
 * @apiSuccess {Object} summary.toNewInvestors
 * @apiSuccess {Number} summary.toNewInvestors.minInitInvst Minimum initial investment required.
 * @apiSuccess {String} summary.toNewInvestors.minInitInvstCurrencyCode Minimum initial investment required currency code.
 * @apiSuccess {Number} summary.toNewInvestors.minSubqInvst Minimum subsequent investment required.
 * @apiSuccess {String} summary.toNewInvestors.minSubqInvstCurrencyCode Minimum subsequent investment required currency code.
 * @apiSuccess {Number} summary.toNewInvestors.minInitRRSPInvst Minimum initial RRSP investment required.
 * @apiSuccess {String} summary.toNewInvestors.minInitRRSPInvstCurrencyCode Minimum initial RRSP investment required currency code. 
 * @apiSuccess {Number} summary.toNewInvestors.minSubqRRSPInvst Minimum subsequent RRSP investment required.
 * @apiSuccess {String} summary.toNewInvestors.minSubqRRSPInvstCurrencyCode Minimum subsequent RRSP investment required currency code.
 * @apiSuccess {Number} summary.toNewInvestors.hhhhMinInitInvst Minimum initial investment required by hhhh define.
 * @apiSuccess {String} summary.toNewInvestors.hhhhMinInitInvstCurrencyCode Minimum initial investment required currency code by hhhh define.
 * @apiSuccess {Number} summary.toNewInvestors.hhhhMinSubqInvst Minimum subsequent investment required by define.
 * @apiSuccess {String} summary.toNewInvestors.hhhhMinSubqInvstCurrencyCode Minimum subsequent investment required currency code by define.
 * @apiSuccess {String} summary.toNewInvestors.purchaseCurId Purchase Currency Id.
 * @apiSuccess {String} summary.toNewInvestors.minInitUnit Initial Investment(s) Unit / Initial RRSP investment(s) Unit.
 * @apiSuccess {String} summary.toNewInvestors.minSubqUnit Subsequent Investment(s) Unit / Subsequent RRSP investment(s) Unit.
 * @apiSuccess {Boolean} summary.toNewInvestors.indicator Open / closed indicator for the holding.
 * @apiSuccess {Object} summary.feesAndExpenses
 * @apiSuccess {Number} summary.feesAndExpenses.maximumInitialSalesFees Maximum initial Sales Fees. 
 * @apiSuccess {Number} summary.feesAndExpenses.maximumDeferredSalesFees Maximum deferred Sales Fees.
 * @apiSuccess {Number} summary.feesAndExpenses.actualManagementFee Management and administrative fees paid over the fund's prior fiscal year.
 * @apiSuccess {String} summary.feesAndExpenses.prospectusCustodianUnit prospectus custodian unit.
 * @apiSuccess {Number} summary.feesAndExpenses.prospectusCustodianFee prospectus Custodian fee.
 * @apiSuccess {Number} summary.feesAndExpenses.actualMER Actual management expense ratio.
 * @apiSuccess {String} summary.feesAndExpenses.loadType Sales commission charged to holders of fund units.
 * @apiSuccess {String} summary.feesAndExpenses.lastUpdatedDate Date data were last updated.
 * @apiSuccess {Number} summary.feesAndExpenses.onGoingChargeFigure The Ongoing Charge represents the costs you can reasonably expect to pay as an investor from one year to the next, under normal circumstances.
 * @apiSuccess {Object} summary.morningstarRatings
 * @apiSuccess {String} summary.morningstarRatings.morningstarRatingOverall Overall Morningstar Rating of the fund, Morningstar Rating = risk-adjusted rating (or star rating).
 * @apiSuccess {Number} summary.morningstarRatings.morningstarRating3Yr 3-Year Morningstar Rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarRating5Yr 5-Year Morningstar Rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarRating10Yr 10-Year Morningstar Rating
 * @apiSuccess {String} summary.morningstarRatings.morningstarTaxAdjustedRatingOverall Overall Morningstar Tax-Adjusted Rating, Tax-Adjusted Ranking = Relates risk-adjusted and tax-adjusted performances of the fund to those of its peers over a 3-, 5- and 10-year period
 * @apiSuccess {Number} summary.morningstarRatings.morningstarTaxAdjustedRating3Yr 3-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarTaxAdjustedRating5Yr 5-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarTaxAdjustedRating10Yr 10-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} summary.morningstarRatings.lastUpdatedDate Date data were last updated.
 * @apiSuccess {Object} summary.mgmtAndContactInfo
 * @apiSuccess {String} summary.mgmtAndContactInfo.companyName Company Name.
 * @apiSuccess {String} summary.mgmtAndContactInfo.address The address.
 * @apiSuccess {String} summary.mgmtAndContactInfo.city The city.
 * @apiSuccess {String} summary.mgmtAndContactInfo.province The province.
 * @apiSuccess {String} summary.mgmtAndContactInfo.postalCode The postalCode.
 * @apiSuccess {String} summary.mgmtAndContactInfo.telephoneNo Telephone No.
 * @apiSuccess {String} summary.mgmtAndContactInfo.faxNo Fax No.
 * @apiSuccess {String} summary.mgmtAndContactInfo.website Website address.
 * @apiSuccess {Object[]} summary.mgmtAndContactInfos.mgmtInfos
 * @apiSuccess {String} summary.mgmtAndContactInfos.mgmtInfos.managerName Name of current portfolio manager(s).
 * @apiSuccess {String} summary.mgmtAndContactInfos.mgmtInfos.startDate Date current manager started managing the fund.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 *		{
 *			"summary": {
 *				"bid": null,
 *				"weekRangeLow": 1.4617,
 *				"weekRangeHigh": 2.1791,
 *				"offer": null,
 *				"calendarYearTotalReturns": {
 *					"items": [{
 *						"year": 2016,
 *						"fundCalendarYearReturn": 0.71814,
 *						"bestFitIndexCalendarYearReturn": null,
 *						"categoryCalendarYearReturn": -14.12368
 *					},
 *					{
 *						"year": 2015,
 *						"fundCalendarYearReturn": 17.06415,
 *						"bestFitIndexCalendarYearReturn": null,
 *						"categoryCalendarYearReturn": 46.67766
 *					},
 *					{
 *						"year": 2014,
 *						"fundCalendarYearReturn": 55.00924,
 *						"bestFitIndexCalendarYearReturn": null,
 *						"categoryCalendarYearReturn": 20.46441
 *					},
 *					{
 *						"year": 2013,
 *						"fundCalendarYearReturn": 25.1564,
 *						"bestFitIndexCalendarYearReturn": null,
 *						"categoryCalendarYearReturn": 12.60022
 *					},
 *					{
 *						"year": 2012,
 *						"fundCalendarYearReturn": 0.79228,
 *						"bestFitIndexCalendarYearReturn": null,
 *						"categoryCalendarYearReturn": 4.12102
 *					}],
 *					"bestFitIndex": "Morningstar China Large Cap CNY",
 *					"lastUpdatedDate": "2017-05-31"
 *				},
 *				"holdingDetails": {
 *					"totalNetAssets": "1544835334",
 *					"totalNetAssetsCurrencyCode": "CNY",
 *					"annualPortfolioTurnover": 497.46,
 *					"equityStyle": "2",
 *					"fixedIncomeStyle": null,
 *					"sharesOutstanding": 998337662.93,
 *					"netAssetValue": 1.4882,
 *					"netAssetValueCurrencyCode": null,
 *					"premiumDiscountToNAV": null,
 *					"priceEarningsRatio": 0.04367,
 *					"dividendYield": 39.38848,
 *					"dividendPerShare": "0.590000000",
 *					"dividendPerShareCurrencyCode": "Yuan Renminbi",
 *					"exDividendDate": null,
 *					"dividendPaymentDate": null,
 *					"beta": 0.98,
 *					"taxAdjustedRating": null
 *				},
 *				"toNewInvestors": {
 *					"minInitInvst": 2000,
 *					"minInitInvstCurrencyCode": "CU$$$$$CNY ",
 *					"minSubqInvst": 1000,
 *					"minSubqInvstCurrencyCode": "CU$$$$$CNY ",
 *					"minInitRRSPInvst": 1000,
 *					"minInitRRSPInvstCurrencyCode": "CNY",
 *					"minSubqRRSPInvst": 1000,
 *					"minSubqRRSPInvstCurrencyCode": "CNY",
 *					"hhhhMinInitInvst": 10000,
 *					"hhhhMinInitInvstCurrencyCode": "CU$$$$$CNY ",
 *					"hhhhMinSubqInvst": null,
 *					"hhhhMinSubqInvstCurrencyCode": "CU$$$$$CNY ",
 *					"purchaseCurId": "CNY",
 *					"minInitUnit": "Monetary",
 *					"minSubqUnit": "Monetary",
 *					"indicator": true
 *				},
 *				"feesAndExpenses": {
 *					"maximumInitialSalesFees": 1000,
 *					"maximumDeferredSalesFees": null,
 *					"actualManagementFee": 1.5,
 * 					"prospectusCustodianUnit" : "Percentage",
 *					"prospectusCustodianFee" : 0.25,
 *					"actualMER": null,
 *					"loadType": null,
 *					"lastUpdatedDate": "2016-12-31",
 *					"onGoingChargeFigure": 0.84
 *				},
 *				"morningstarRatings": {
 *					"morningstarRatingOverall": "5",
 *					"morningstarRating3Yr": 5,
 *					"morningstarRating5Yr": 5,
 *					"morningstarRating10Yr": 4,
 *					"morningstarTaxAdjustedRatingOverall": null,
 *					"morningstarTaxAdjustedRating3Yr": null,
 *					"morningstarTaxAdjustedRating5Yr": null,
 *					"morningstarTaxAdjustedRating10Yr": null,
 *					"lastUpdatedDate": "2017-05-31"
 *				},
 *				"mgmtAndContactInfo": {
 *					"companyName": "hhhh JinTrust Fund Mgmt Co.,Ltd",
 *					"address": "china",
 *					"city": "Guangzhou",
 *					"province": null,
 *					"postalCode": null,
 *					"telephoneNo": "(86)021-2037 6868",
 *					"faxNo": "(86)021-2037 6999",
 *					"website": "www.hhhhjt.cn",
 *					"mgmtInfos": [{
 *						"managerName": "Qing Cao",
 *						"startDate": "2016-04-30"
 *					}]
 *				},
 *				"profile": {
 *					"bestFitIndex": "Morningstar China Large Cap CNY",
 *					"bestFitIndexCode": null,
 *					"categoryCode": "PGSZB4TTTT",
 *					"categoryName": "Aggressive Allocation Fund",
 *					"familyName": "hhhh Jintrust",
 *					"familyCode": "UT",
 *					"advisor": "hhhh JinTrust Fund Mgmt Co.,Ltd",
 *					"subAdvisor": null,
 *					"inceptionDate": "2006-09-27",
 *					"investmentObjectiveAndStrategy": "The fund picks these companies which benefit from China economics fast growth and standing competitive advantages,have bright future in growth and have potential growth ability.The fund seeks for better than the benchmark 's return ,mid and long term stead",
 *					"hhhhCategoryCode": "FUNDCAT-MUAS",
 *					"hhhhCategoryName": "Multi Asset"
 *				}
 *			}
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */
