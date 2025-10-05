/**
 * @api {get} /wmds/v4.0/fundQuoteSummary Fund Quote Summary
 * @apiName Fund Quote Summary
 * @apiGroup Quote
 * @apiVersion 4.0.2
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
 *		"market":"CA",
 *		"productType": "UT",
 *		"prodCdeAltClassCde":"M",
 *		"prodAltNum":"CAN048"
 *	}	
 * 
 * @apiSuccess {Object} summary
 * @apiSuccess {Number} summary.bid The bid price.
 * @apiSuccess {Number} summary.offer The offer price.
 * @apiSuccess {Number} summary.weekRangeLow 52-Week Low price.
 * @apiSuccess {Number} summary.weekRangeHigh 52-Week High price.
 * @apiSuccess {Object} summary.calendarYearTotalReturns
 * @apiSuccess {Object[]} summary.calendarYearTotalReturns.items
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.year The year of return data.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.fundCalendarYearReturn Fund Returns by Calendar Year.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.bestFitIndexCalendarYearReturn Best-Fit Index Returns by Calendar Year.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.categoryCalendarYearReturn Category/Asset Class Returns by Calendar Year.
 * @apiSuccess {String} summary.calendarYearTotalReturns.bestFitIndex Market index showing the highest correlation with a fund over the most recent 36 months, as measured by the highest R-squared.
 * @apiSuccess {Date} summary.calendarYearTotalReturns.lastUpdatedDate Date information was last updated.format: yyyy-MM-dd
 * @apiSuccess {Object} summary.profile
 * @apiSuccess {String} summary.profile.bestFitIndex Market index showing the highest correlation with a fund over the most recent 36 months, as measured by the highest R-squared.
 * @apiSuccess {String} summary.profile.bestFitIndexCode The best fit index code.
 * @apiSuccess {String} summary.profile.categoryCode 10 character code assigned to each Morningstar Category.
 * @apiSuccess {String} summary.profile.categoryName Mutual Fund Category.
 * @apiSuccess {String} summary.profile.familyName Fund House Name.
 * @apiSuccess {String} summary.profile.familyCode Fund House Code.
 * @apiSuccess {String} summary.profile.advisor Mutual Fund Advisor (or Sponsor).
 * @apiSuccess {String} summary.profile.subAdvisor Mutual Fund Sub-advisor(s).
 * @apiSuccess {Date} summary.profile.inceptionDate Fund Inception Date.format: yyyy-MM-dd
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
 * @apiSuccess {Date} summary.holdingDetails.exDividendDate Ex-dividend date.format: yyyy-MM-dd
 * @apiSuccess {Date} summary.holdingDetails.dividendPaymentDate Dividend payment date.format: yyyy-MM-dd
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
 * @apiSuccess {Number} summary.feesAndExpenses.actualMER Actual management expense ratio.
 * @apiSuccess {String} summary.feesAndExpenses.loadType Sales commission charged to holders of fund units.
 * @apiSuccess {Date} summary.feesAndExpenses.lastUpdatedDate Date data were last updated.format: yyyy-MM-dd
 * @apiSuccess {Object} summary.morningstarRatings
 * @apiSuccess {String} summary.morningstarRatings.morningstarRatingOverall Overall Morningstar Rating of the fund, Morningstar Rating = risk-adjusted rating (or star rating).
 * @apiSuccess {Number} summary.morningstarRatings.morningstarRating3Yr 3-Year Morningstar Rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarRating5Yr 5-Year Morningstar Rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarRating10Yr 10-Year Morningstar Rating
 * @apiSuccess {String} summary.morningstarRatings.morningstarTaxAdjustedRatingOverall Overall Morningstar Tax-Adjusted Rating, Tax-Adjusted Ranking = Relates risk-adjusted and tax-adjusted performances of the fund to those of its peers over a 3-, 5- and 10-year period
 * @apiSuccess {Number} summary.morningstarRatings.morningstarTaxAdjustedRating3Yr 3-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarTaxAdjustedRating5Yr 5-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarTaxAdjustedRating10Yr 10-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Date} summary.morningstarRatings.lastUpdatedDate Date data were last updated.format: yyyy-MM-dd
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
 * @apiSuccess {Date} summary.mgmtAndContactInfos.mgmtInfos.startDate Date current manager started managing the fund.format: yyyy-MM-dd
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"summary": {
 * 				"weekRangeLow": 12.30470,
 * 				"weekRangeHigh": 12.94080,
 * 				"calendarYearTotalReturns": {
 * 					"items": [
 * 						{
 * 							"year": 2016,
 * 							"fundCalendarYearReturn": 2.26267,
 * 							"bestFitIndexCalendarYearReturn": 4.81681,
 * 							"categoryCalendarYearReturn": 3.29512
 * 						},
 * 						{
 * 							"year": 2015,
 * 							"fundCalendarYearReturn": 2.12798,
 * 							"bestFitIndexCalendarYearReturn": 2.29729,
 * 							"categoryCalendarYearReturn": 1.53170
 * 						},
 * 						{
 * 							"year": 2014,
 * 							"fundCalendarYearReturn": 5.44011,	
 * 							"bestFitIndexCalendarYearReturn": 8.33948,
 * 							"categoryCalendarYearReturn": 6.86062
 * 						},
 * 						{	
 * 							"year": 2013,
 * 							"fundCalendarYearReturn": 1.64847,
 * 							"bestFitIndexCalendarYearReturn": 5.20938,
 * 							"categoryCalendarYearReturn": 4.75996
 * 						},
 * 						{
 * 							"year": 2012,
 * 							"fundCalendarYearReturn": 3.01638,
 * 							"bestFitIndexCalendarYearReturn": 4.11768,	
 * 							"categoryCalendarYearReturn": 4.01828
 * 						}	
 * 					],
 * 					"bestFitIndex": "Morningstar CAN Bal Cdn Con CAD",
 * 					"lastUpdatedDate": "2017-03-03"
 * 				},	
 * 				"profile": {
 * 					"hhhhCategoryCode": "CACA000115",
 * 					"hhhhCategoryName": "Canadian Fixed Income Balanced",
 * 					"bestFitIndex": "Morningstar CAN Bal Cdn Con CAD",	
 * 					"bestFitIndexCode": "FOUSA06PD6",
 * 					"categoryCode": "CACA000115",
 * 					"categoryName": "Canadian Fixed Income Balanced",
 * 					"family": "Canada Life Assurance Co",
 * 					"familyName": "Canada Life Assurance Co",
 * 					"familyCode": "0C00001LY0",
 * 					"advisor": "Canada Life Assurance Co",	
 * 					"inceptionDate": "2009-10-05",
 * 					"investmentObjectiveAndStrategy": "This fund's objective is to provide interest income with the potential for capital appreciation by investing in units of Canadian fixed income segregated funds with a smaller portion in units of equity segregated funds. The underlying funds may invest in a broad range of fixed income investments including bonds, debentures, mortgages, real estate, cash and short-term securities and to a lesser degree, in equities issued by a broad range of Canadian and foreign corporations. This fund usually divides its investments as follows: �? 10%- 30% equities �? 70% - 90% fixed income. This fund does not invest directly in derivatives. The underlying funds may invest in derivatives."
 * 				},
 * 				"holdingDetails": {
 * 					"totalNetAssets": "4626957",
 * 					"totalNetAssetsCurrencyCode": "CAD",
 * 					"annualPortfolioTurnover": 52.85,
 * 					"equityStyle": "2",
 * 					"fixedIncomeStyle": "6",
 * 					"netAssetValue": 12.70210,
 * 					"priceEarningsRatio": 0.05561,	
 * 					"dividendYield": 0.00000,
 * 					"dividendPerShareCurrencyCode": "Canadian Dollar",
 * 					"beta": 1.07,	
 * 				},
 * 				"toNewInvestors": {
 * 					"minInitRRSPInvstCurrencyCode": "CAD",	
 * 					"minSubqRRSPInvstCurrencyCode": "CAD",
 * 					"minInitInvst": 500,
 * 					"minInitInvstCurrencyCode": "CAD",
 * 					"minSubqInvst": 100,
 * 					"minSubqInvstCurrencyCode": "CAD",	
 * 					"minInitRRSPInvst": 1000,
 * 					"minSubqRRSPInvst": 1000,
 * 					"hhhhMinInitInvst": 1000,	
 * 					"hhhhMinInitInvstCurrencyCode": "CAD",
 * 					"hhhhMinSubqInvst": 1000,
 * 					"hhhhMinSubqInvstCurrencyCode": "CAD",
 * 					"purchaseCurId": "CAD",
 * 					"minInitUnit": "Monetary",
 * 					"minSubqUnit": "Monetary",
 * 					"indicator": true
 * 				},	
 * 				"feesAndExpenses": {
 * 					"actualManagementFee": 2.15000,	
 * 					"actualMER": 2.550,
 * 					"loadType": "Front-end Charge",
 * 					"lastUpdatedDate": "2015-12-31"
 * 				},
 * 				"morningstarRatings": {
 * 					"morningstarRatingOverall": 2,
 * 					"morningstarRating5Yr": 2,
 * 					"lastUpdatedDate": "2017-02-28"
 * 				},
 * 				"mgmtAndContactInfo": {
 * 					"companyName": "Canada Life Assurance Co",
 * 					"telephoneNo": "416 597 1456",
 * 					"faxNo": "866 861 9141",
 * 					"website": "www.canadalife.com",
 * 					"mgmtInfos": [
 * 						{
 * 							"managerName": "Susan Spence",
 * 							"startDate": "2013-07-08"
 * 						}
 * 					]
 * 				}				
 * 			}
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */