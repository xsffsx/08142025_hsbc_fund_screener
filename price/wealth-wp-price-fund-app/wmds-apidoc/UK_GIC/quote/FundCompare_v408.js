/**
 * @api {get} /v4.0/fundCompare Fund Compare
 * @apiName Fund Compare
 * @apiGroup Quote
 * @apiVersion 4.0.8
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {Object[]} productKeys 
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKeys.market The investment market of the product.</br>
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
 * @apiParam {String} productKeys.productType Product Type. If this field is null, then the productType require to define in productKey.
 * 
 * @apiParamExample Request:
 * {
 *		"productKeys": [
 *			{
 *				"prodCdeAltClassCde": "M",
 *				"prodAltNum": "540002",
 *				"market":"CN",
 *				"productType": "UT"
 *			},
 *			{
 *				"prodCdeAltClassCde": "M",
 *				"prodAltNum": "540009",
 *				"market":"CN",
 *				"productType": "UT"
 *			}
 *		]
 *	}
 * 
 * @apiSuccess {Object[]} products
 * @apiSuccess {Object} products.header
 * @apiSuccess {String} products.header.name Mutual Fund Name.
 * @apiSuccess {String} products.header.prodAltNum Mutual Fund symbol / exchange traded funds symbol (market code) , denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String} products.header.currency Currency of this product.
 * @apiSuccess {Object} products.summary
 * @apiSuccess {String} products.summary.categoryName Fund Category.
 * @apiSuccess {Number} products.summary.dayEndNAV Daily Performance NAV.
 * @apiSuccess {String} products.summary.exchangeUpdatedTime Last updated time for the specified symbol.
 * @apiSuccess {String} products.summary.dayEndNAVCurrencyCode Daily Performance NAV currency code.
 * @apiSuccess {Number} products.summary.changeAmountNAV Change in NAV from previous close, expressed as an absolute amount.
 * @apiSuccess {Number} products.summary.changePercentageNAV Change in NAV from previous close, expressed as a percentage.
 * @apiSuccess {Number} products.summary.assetsUnderManagement Assets under management, market value of assets managed by the fund.
 * @apiSuccess {String} products.summary.assetsUnderManagementCurrencyCode Assets under management currencyCode.
 * @apiSuccess {Number} products.summary.totalNetAsset Total asset base of the fund, net of fees and expenses.
 * @apiSuccess {String} products.summary.totalNetAssetCurrencyCode Total net assets currencyCode.
 * @apiSuccess {String} products.summary.ratingOverall Overall morningstar rating of the fund, morningstar rating = risk-adjusted rating (or star rating).
 * @apiSuccess {Number} products.summary.mer Annual report fees.
 * @apiSuccess {Number} products.summary.yield1Yr Distribution yield.
 * @apiSuccess {String} products.summary.riskLvlCde Risk level code.
 * @apiSuccess {Number} products.summary.actualManagementFee Actual Management Fee.
 * @apiSuccess {Number} products.summary.annualReportOngoingCharge AnnualReportOngoing Charge.
 * @apiSuccess {String} products.summary.endDate for riskLvlCde as riskDate.
 * @apiSuccess {String} products.summary.DayEndDate for dayEndNAV as exchangeUpdatedTime.
 * @apiSuccess {Object} products.profile
 * @apiSuccess {String} products.profile.inceptionDate Fund inception.format:yyyy-MM-dd
 * @apiSuccess {Number} products.profile.turnoverRatio Annual portfolio turnover.
 * @apiSuccess {Number} products.profile.stdDev3Yr Risk Measures.
 * @apiSuccess {String} products.profile.equityStylebox Equity style.
 * @apiSuccess {String} products.profile.hhhhCategoryName Fund category name define by hhhh.
 * @apiSuccess {String} products.profile.hhhhCategoryCode Fund category code define by hhhh
 * @apiSuccess {Object} products.rating
 * @apiSuccess {String} products.rating.morningstarRating Morningstar rating.
 * @apiSuccess {String} products.rating.taxAdjustedRating Tax-Adjusted rating.
 * @apiSuccess {String} products.rating.averageCreditQuality Credit quality/rating.
 * @apiSuccess {String} products.rating.ratingDate rating as ratingDate.
 * 
 * @apiSuccess {Object} products.performance
 * @apiSuccess {Object} products.performance.annualizedReturns
 * @apiSuccess {Number} products.performance.annualizedReturns.return1Mth % Return (Fund - 1 Month).
 * @apiSuccess {Number} products.performance.annualizedReturns.return3Mth % Return (Fund - 3 Month).
 * @apiSuccess {Number} products.performance.annualizedReturns.return6Mth % Return (Fund - 6 Month).
 * @apiSuccess {Number} products.performance.annualizedReturns.return1Yr % Return (Fund - 1 Year).
 * @apiSuccess {Number} products.performance.annualizedReturns.return3Yr % Return (Fund - 3 Year).
 * @apiSuccess {Number} products.performance.annualizedReturns.return5Yr % Return (Fund - 5 Year).
 * @apiSuccess {Number} products.performance.annualizedReturns.return10Yr % Return (Fund - 10 Year).
 * @apiSuccess {Number} products.performance.annualizedReturns.returnSinceInception % Return (Fund - Since Inception).
 * @apiSuccess {String} products.performance.annualizedReturns.monthEndDate Trailing Total Return for annualizedReturns as monthEndDate.
 * @apiSuccess {Object} products.performance.calendarReturns
 * @apiSuccess {Number} products.performance.calendarReturns.returnYTD Trailing total returns.
 * @apiSuccess {Number} products.performance.calendarReturns.year1 Calendar Return - 1 Year Ago.
 * @apiSuccess {Number} products.performance.calendarReturns.year2 Calendar Return - 2 Years Ago.
 * @apiSuccess {Number} products.performance.calendarReturns.year3 Calendar Return - 3 Years Ago.
 * @apiSuccess {number} products.performance.calendarReturns.year4 Calendar Return - 4 Years Ago.
 * @apiSuccess {Number} products.performance.calendarReturns.year5 Calendar Return - 5 Years Ago.
 * @apiSuccess {Object[]} products.risk
 * @apiSuccess {Object} products.risks.yearRisk 
 * @apiSuccess {Number} products.risks.yearRisk.year Denote how many years for this risk. 
 * @apiSuccess {Number} products.risks.yearRisk.beta X-year volatility (systemic risk) of fund relative to the overall market.
 * @apiSuccess {Number} products.risks.yearRisk.stdDev X-year volatility (systemic risk) of portfolio.
 * @apiSuccess {Number} products.risks.yearRisk.alpha X-year excess risk-adjusted return of portfolio relative to the return of a benchmark index.
 * @apiSuccess {String} products.risks.yearRisk.sharpeRatio Measure of x-year risk-adjusted performance of the portfolio.
 * @apiSuccess {String} products.risks.yearRisk.rSquared X-year correlation of a portfolio's movements to that of a benchmark index.
 * @apiSuccess {String} products.risks.yearRisk.endDate for yearRisk as riskMeasureDate.
 * @apiSuccess {Object} products.purchaseInfo
 * @apiSuccess {Number} products.purchaseInfo.minimumInitial Minimum Investment.
 * @apiSuccess {String} products.purchaseInfo.minimumInitialCurrencyCode Minimum investment currencyCode.
 * @apiSuccess {Number} products.purchaseInfo.minimumSubsequent Subsequent investment(s).
 * @apiSuccess {String} products.purchaseInfo.minimumSubsequentCurrencyCode Subsequent investment(s) currencyCode.
 * @apiSuccess {Number} products.purchaseInfo.minimumIRA Fund Purchase Details.
 * @apiSuccess {String} products.purchaseInfo.minimumIRACurrencyCode Fund purchase code.
 * @apiSuccess {Boolean} products.purchaseInfo.rrsp Fund attributes.
 * @apiSuccess {String} products.purchaseInfo.loadType Fund Share Class Basic Information.
 * @apiSuccess {Number} products.purchaseInfo.minInitInvst Minimum initial investment required.
 * @apiSuccess {String} products.purchaseInfo.minInitInvstCurrencyCode Minimum initial investment required currency code.
 * @apiSuccess {Number} products.purchaseInfo.minSubqInvst Minimum subsequent investment required.
 * @apiSuccess {String} products.purchaseInfo.minSubqInvstCurrencyCode Minimum subsequent investment required currency code.
 * @apiSuccess {Number} products.purchaseInfo.hhhhMinInitInvst Minimum initial investment required by hhhh define.
 * @apiSuccess {String} products.purchaseInfo.hhhhMinInitInvstCurrencyCode Minimum initial investment required currency code by hhhh define.
 * @apiSuccess {Number} products.purchaseInfo.hhhhMinSubqInvst Minimum subsequent investment required by define.
 * @apiSuccess {String} products.purchaseInfo.hhhhMinSubqInvstCurrencyCode Minimum subsequent investment required currency code by define.
 * 
 * @apiSuccess {String} products.prodAltNumXCode Mutual Fund symbol / exchange traded funds symbol (market code) , denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 *		{
 *           "products": [
 *             {
 *              "header": {
 *                "name": "Investec Global Energy I Acc Net GBP",
 *                "prodAltNum": "B049PC9",
 *                "currency": "GBP"
 *              },
 *              "summary": {
 *                "categoryName": "Sector Equity Energy",
 *                "dayEndNAV": 2.0927,
 *                "exchangeUpdatedTime": "2017-10-13",
 *                "dayEndNAVCurrencyCode": "GBP",
 *                "changeAmountNAV": -0.0263,
 *                "changePercentageNAV": -1.24115,
 *                "assetsUnderManagement": 72063815,
 *                "assetsUnderManagementCurrencyCode": "GBP",
 *                 "totalNetAsset": 36701987,
 *                 "totalNetAssetCurrencyCode": "GBP",
 *                 "ratingOverall": "2",
 *                 "mer": null,
 *                 "yield1Yr": 2.01171,
 *                 "riskLvlCde": null,
 *                 "annualReportOngoingCharge": 0.88,
 *                 "actualManagementFee": 0.75,
 *                 "endDate": "2017-10-17",
 *                 "dayEndDate": "2017-10-17"
 *               },
 *               "profile": {
 *                 "inceptionDate": "2004-11-29",
 *                 "turnoverRatio": null,
 *                 "stdDev3Yr": 24.986,
 *                 "equityStylebox": "1",
 *                 "hhhhCategoryName": "Sector Equity Energy",
 *                 "hhhhCategoryCode": "EUCA000535"
 *               },
 *               "rating": {
 *                 "morningstarRating": "2",
 *                 "taxAdjustedRating": null,
 *                 "averageCreditQuality": null,
 *                 "rank1Yr": "3",
 *                 "ratingDate": "2017-09-30"
 *               },
 *               "performance": {
 *                 "annualizedReturns": {
 *                   "return1Mth": 6.70148,
 *                   "return3Mth": 5.06099,
 *                   "return6Mth": -8.30217,
 *                   "return1Yr": -3.3661,
 *                   "return3Yr": -9.25017,
 *                   "return5Yr": -2.70445,
 *                   "return10Yr": 0.34566,
 *                   "returnSinceInception": 5.99443,
 *                   "monthEndDate": "2017-09-30"
 *                 },
 *                 "calendarReturns": {
 *                   "returnYTD": -16.47832,
 *                   "year1": 50.24915,
 *                   "year2": -24.11018,
 *                   "year3": -17.60632,
 *                   "year4": 15.29501,
 *                   "year5": -2.04215
 *                 }
 *               },
 *               "risk": [
 *                 {
 *                   "yearRisk": {
 *                     "year": 1,
 *                     "beta": 1.18,
 *                     "stdDev": 18.982,
 *                     "alpha": -5.98,
 *                     "sharpeRatio": 0.033,
 *                     "rSquared": 71.86,
 *                     "endDate": "2017-09-30"
 *                   }
 *                 },
 *                 {
 *                   "yearRisk": {
 *                     "year": 3,
 *                     "beta": 1.24,
 *                     "stdDev": 24.986,
 *                     "alpha": -6.9,
 *                     "sharpeRatio": -0.531,
 *                     "rSquared": 83.72,
 *                     "endDate": "2017-09-30"
 *                   }
 *                 },
 *                 {
 *                   "yearRisk": {
 *                     "year": 5,
 *                     "beta": 1.23,
 *                     "stdDev": 21.966,
 *                     "alpha": -5.19,
 *                     "sharpeRatio": -0.196,
 *                     "rSquared": 84.86,
 *                     "endDate": "2017-09-30"
 *                   }
 *                 },
 *                 {
 *                   "yearRisk": {
 *                     "year": 10,
 *                     "beta": 1.15,
 *                     "stdDev": 26.5,
 *                     "alpha": -2.08,
 *                     "sharpeRatio": -0.026,
 *                     "rSquared": 85.44,
 *                     "endDate": "2017-09-30"
 *                   }
 *                 }
 *               ],
 *               "purchaseInfo": {
 *                 "minimumInitial": 1000000,
 *                 "minimumInitialCurrencyCode": "GBP",
 *                 "minimumSubsequent": 250000,
 *                 "minimumSubsequentCurrencyCode": "GBP",
 *                 "minimumIRA": null,
 *                 "minimumIRACurrencyCode": "GBP",
 *                 "rrsp": false,
 *                 "loadType": null,
 *                 "minInitInvst": 1000000,
 *                 "minInitInvstCurrencyCode": "GBP",
 *                 "minSubqInvst": 250000,
 *                 "minSubqInvstCurrencyCode": "GBP",
 *                 "hhhhMinInitInvst": 100,
 *                 "hhhhMinInitInvstCurrencyCode": "GBP",
 *                 "hhhhMinSubqInvst": 100,
 *                 "hhhhMinSubqInvstCurrencyCode": "GBP"
 *               },
 *               "prodAltNumXCode": "0P0000175C"
 *             }
 *           ]
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */