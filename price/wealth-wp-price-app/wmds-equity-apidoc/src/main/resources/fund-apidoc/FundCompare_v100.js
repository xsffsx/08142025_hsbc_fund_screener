/**
 * @api {get} /wealth/api/v1/market-data/fund/detail/compare FundCompare 
 * @apiName FundCompare
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
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
 * @apiSuccess {Object} products.profile
 * @apiSuccess {String} products.profile.inceptionDate Fund inception.format:yyyy-MM-dd
 * @apiSuccess {Number} products.profile.turnoverRatio Annual portfolio turnover.
 * @apiSuccess {Number} products.profile.stdDev3Yr Risk Measures.
 * @apiSuccess {String} products.profile.equityStylebox Equity style.
 * @apiSuccess {String} products.profile.hhhhCategoryName Fund category name define by hhhh.
 * @apiSuccess {String} products.profile.hhhhCategoryCode Fund category code define by hhhh.
 * @apiSuccess {Number} products.profile.distributionYield Distribution yield.
 * @apiSuccess {String} products.profile.distributionFrequency Fund share class basic information.
 * @apiSuccess {String} products.profile.piFundInd Private Fund(PI Fund) Indicator.
 * @apiSuccess {String} products.profile.deAuthFundInd De-Authorized Fund Indicator.
 * @apiSuccess {String} products.profile.surveyedFundNetAssetsDate Fund Size Date.Format:yyyy-MM-dd
 * @apiSuccess {Object} products.rating
 * @apiSuccess {String} products.rating.morningstarRating Morningstar rating.
 * @apiSuccess {String} products.rating.taxAdjustedRating Tax-Adjusted rating.
 * @apiSuccess {String} products.rating.averageCreditQualityName Portfolio statistics(most recent port).
 * @apiSuccess {String} products.rating.averageCreditQuality Credit quality/rating.
 * @apiSuccess {Number} products.rating.rank1Yr 1-Year quartile ranking.
 * @apiSuccess {Number} products.rating.rank3Yr 3-Year quartile ranking.
 * @apiSuccess {Number} products.rating.rank5Yr 5-Year quartile ranking.
 * @apiSuccess {Number} products.rating.rank10Yr 10-Year quartile ranking.
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
 *			"products": [{
 *				"header": {
 *					"name": "hhhh Jintrust Dragon Growth Equity Fund",
 *					"prodAltNum": "540002",
 *					"currency": "CNY"
 *				},
 *				"summary": {
 *					"categoryName": "Aggressive Allocation Fund",
 *					"dayEndNAV": 1.5001,
 *					"exchangeUpdatedTime": "2017-07-17",	
 *					"dayEndNAVCurrencyCode": "CNY",
 *					"changeAmountNAV": -0.0023,
 *					"changePercentageNAV": -0.15309,
 *					"assetsUnderManagement": 224155567,
 *					"assetsUnderManagementCurrencyCode": "CNY",
 *					"totalNetAsset": 1544835334,
 *					"totalNetAssetCurrencyCode": "CNY",
 *					"ratingOverall": "5",
 *					"mer": null,
 *					"yield1Yr": 39.38848,
 *					"riskLvlCde": "4",
 *					"actualManagementFee" : 0.38,
 *					"annualReportOngoingCharge" : null
 *				},
 *				"profile": {
 *					"inceptionDate": "2006-09-27",
 *					"turnoverRatio": 497.46,
 *					"stdDev3Yr": 30.291,
 *					"equityStylebox": "2",
 *					"hhhhCategoryName": "Multi Asset",
 *					"hhhhCategoryCode": "FUNDCAT-MUAS",
 *					"distributionYield": 1.34677,
 *					"distributionFrequency": "Annually",
 *					"piFundInd": "N",
 *					"deAuthFundInd": "N",
 *					"surveyedFundNetAssetsDate": "2019-01-23"
 *				},
 *				"rating": {
 *					"morningstarRating": "5",
 *					"taxAdjustedRating": null,
 *					"averageCreditQualityName": null,
 *					"averageCreditQuality": null,
 *					"rank1Yr": 4,
 *					"rank3Yr": 4,
 *					"rank5Yr": 4,
 *					"rank10Yr": 4
 *				},
 *				"performance": {
 *					"annualizedReturns": {
 *						"return1Mth": -1.75128,
 *						"return3Mth": -4.1248,
 *						"return6Mth": 2.68697,
 *						"return1Yr": 20.59045,
 *						"return3Yr": 28.45171,
 *						"return5Yr": 18.40113,
 *						"return10Yr": 8.18932,
 *						"returnSinceInception": 15.81194
 *					},
 *					"calendarReturns": {
 *						"returnYTD": 4.36943,
 *						"year1": 0.71814,
 *						"year2": 17.06415,
 *						"year3": 55.00924,
 *						"year4": 25.1564,
 *						"year5": 0.79228
 *					}
 *				},
 *				"risk": [{
 *					"yearRisk": {
 *						"year": 1,
 *						"beta": 0.72,
 *						"stdDev": 8.938,
 *						"alpha": 10.86,
 *						"sharpeRatio": 2.029,
 *						"rSquared": 53.76
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 3,
 *						"beta": 0.98,
 *						"stdDev": 30.291,
 *						"alpha": 11.36,
 *						"sharpeRatio": 0.926,
 *						"rSquared": 83.22
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 5,
 *						"beta": 0.92,
 *						"stdDev": 26.603,
 *						"alpha": 14.05,
 *						"sharpeRatio": 0.691,
 *						"rSquared": 76.18
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 10,
 *						"beta": 0.79,
 *						"stdDev": 27.152,
 *						"alpha": 9.52,
 *						"sharpeRatio": 0.344,
 *						"rSquared": 78.56
 *					}
 *				}],
 *				"purchaseInfo": {
 *					"minimumInitial": 2000,
 *					"minimumInitialCurrencyCode": "CNY",
 *					"minimumSubsequent": 1000,
 *					"minimumSubsequentCurrencyCode": "CNY",
 *					"minimumIRA": null,
 *					"minimumIRACurrencyCode": "CNY",
 *					"rrsp": false,
 *					"loadType": null
 *				},
 *				"prodAltNumXCode": "0P00007KN5"
 *			},
 *			{
 *				"header": {
 *					"name": "hhhh Jintrust China Consumption Equity",
 *					"prodAltNum": "540009",
 *					"currency": "CNY"
 *				},
 *				"summary": {
 *					"categoryName": "Equity Funds",
 *					"dayEndNAV": 1.0244,
 *					"exchangeUpdatedTime": "2017-07-17",
 *					"dayEndNAVCurrencyCode": "CNY",
 *					"changeAmountNAV": -0.0058,
 *					"changePercentageNAV": -0.563,
 *					"assetsUnderManagement": 25234637,
 *					"assetsUnderManagementCurrencyCode": "CNY",
 *					"totalNetAsset": 173912074,
 *					"totalNetAssetCurrencyCode": "CNY",
 *					"ratingOverall": "2",
 *					"mer": null,
 *					"yield1Yr": 0,
 *					"riskLvlCde": "4"
 *				},
 *				"profile": {
 *					"inceptionDate": "2010-12-08",
 *					"turnoverRatio": 309.51,
 *					"stdDev3Yr": 31.541,
 *					"equityStylebox": "3",
 *					"hhhhCategoryName": "Specialised Sector Equity",
 *					"hhhhCategoryCode": "FUNDCAT-SSDQ",
 *					"distributionYield": 1.34677,
 *					"distributionFrequency": "Annually"
 *				},
 *				"rating": {
 *					"morningstarRating": "2",
 *					"taxAdjustedRating": null,
 *					"averageCreditQualityName": null,
 *					"averageCreditQuality": null,
 *					"rank1Yr": 1,
 *					"rank3Yr": 4,
 *					"rank5Yr": 4,
 *					"rank10Yr": 4
 *				},
 *				"performance": {
 *					"annualizedReturns": {
 *						"return1Mth": 2.97813,
 *						"return3Mth": 8.88168,
 *						"return6Mth": 11.37507,
 *						"return1Yr": 24.69151,
 *						"return3Yr": 9.81303,
 *						"return5Yr": 5.23663,
 *						"return10Yr": null,
 *						"returnSinceInception": 1.65728
 *					},
 *					"calendarReturns": {
 *						"returnYTD": 16.12183,
 *						"year1": -6.33295,
 *						"year2": 17.17248,
 *						"year3": -5.54113,
 *						"year4": 8.57814,
 *						"year5": 3.97068
 *					}
 *				},
 *				"risk": [{
 *					"yearRisk": {
 *						"year": 1,
 *						"beta": 0.68,
 *						"stdDev": 9.507,
 *						"alpha": 14.66,
 *						"sharpeRatio": 2.271,
 *						"rSquared": 43.2
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 3,
 *						"beta": 0.69,
 *						"stdDev": 31.541,
 *						"alpha": 1.28,
 *						"sharpeRatio": 0.409,
 *						"rSquared": 37.26
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 5,
 *						"beta": 0.68,
 *						"stdDev": 26.619,
 *						"alpha": 3.55,
 *						"sharpeRatio": 0.253,
 *						"rSquared": 41.63
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 10,
 *						"beta": null,
 *						"stdDev": null,
 *						"alpha": null,
 *						"sharpeRatio": null,
 *						"rSquared": null
 *					}
 *				}],
 *				"purchaseInfo": {
 *					"minimumInitial": 1000,
 *					"minimumInitialCurrencyCode": "CNY",
 *					"minimumSubsequent": 1000,
 *					"minimumSubsequentCurrencyCode": "CNY",
 *					"minimumIRA": null,
 *					"minimumIRACurrencyCode": "CNY",
 *					"rrsp": false,
 *					"loadType": null,
 *					"minInitInvst": 1000,
 *					"minInitInvstCurrencyCode": "CNY",
 *					"minSubqInvst": 1000,
 *					"minSubqInvstCurrencyCode": "CNY",
 *					"hhhhMinInitInvst": 10000,
 *					"hhhhMinInitInvstCurrencyCode": "CNY",
 *					"hhhhMinSubqInvst": 1000,
 *					"hhhhMinSubqInvstCurrencyCode": "CNY"
 *				}
 *				"prodAltNumXCode": "0P0000Q720"
 *			}]
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */