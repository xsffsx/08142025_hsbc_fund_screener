/**
 * @api {get} /wmds/v4.0/fundCompare Fund Compare
 * @apiName Fund Compare
 * @apiGroup Quote
 * @apiVersion 4.0.1
 * 
 * @apiUse QuoteHeaderParam
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
 *		"productKeys": [{
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "CAN048",
 *			"market":"CA",
 *			"productType": "UT"
 *		}]
 *	}
 * 
 * @apiSuccess {Object[]} products
 * @apiSuccess {Object} products.header
 * @apiSuccess {String} products.header.name Mutual Fund Name.
 * @apiSuccess {String} products.header.prodAltNum Mutual Fund symbol / exchange traded funds symbol (market code) , denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String} products.header.currency Currency of this product.
 * @apiSuccess {String} products.summary
 * @apiSuccess {String} products.summary.categoryName Fund Category.
 * @apiSuccess {Number} products.summary.dayEndNAV Daily Performance NAV.
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
 * @apiSuccess {String} products.rating.rank1Yr 1-Year quartile ranking.
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
 * @apiSuccess {Object[]} products.risks
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
 * @apiSuccess {String} products.prodAltNumXCode Mutual Fund symbol / exchange traded funds symbol (market code) , denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"products": [
 * 				{
 * 					"prodAltNumXCode": "0P0000M5QH",
 * 					"header": {
 * 						"name": "CAN Income Focus (PSG) 75/75",
 * 						"prodAltNum": "CAN048",
 * 						"currency": "CAD"
 * 					},
 * 					"summary": {
 * 						"categoryName": "Canadian Fixed Income Balanced",
 * 						"dayEndNAV": 12.6414,
 * 						"dayEndNAVCurrencyCode": "CAD",
 * 						"changeAmountNAV": -0.0209,
 * 						"changePercentageNAV": -0.16506,
 * 						"assetsUnderManagement": 47922470,						
 * 						"assetsUnderManagementCurrencyCode": "CAD",
 * 						"totalNetAsset": 4405396,
 * 						"totalNetAssetCurrencyCode": "CAD",
 * 						"ratingOverall": "2",	
 * 						"mer": 2.550,
 * 						"yield1Yr": 0.00000,
 * 						"riskLvlCde": "2"
 * 					},
 * 					"profile": {
 * 						"hhhhCategoryName": "Canadian Fixed Income Balanced",
 * 						"hhhhCategoryCode": "CACA000115",
 * 						"inceptionDate": "2009-10-05",
 * 						"turnoverRatio": 52.85,
 * 						"stdDev3Yr": 3.476,
 * 						"equityStylebox": "2"
 * 					},
 * 					"rating": {
 * 						"morningstarRating": "2",		
 * 						"averageCreditQuality": "rating3",
 * 						"rank1Yr": "4"
 * 					},
 * 					"performance": {
 * 						"annualizedReturns": {	
 * 						"return1Mth": 1.24350,
 * 						"return3Mth": 0.81013,
 * 						"return6Mth": -1.29536,	
 * 						"return1Yr": 3.70035,
 * 						"return3Yr": 2.51233,
 * 						"return5Yr": 2.96066,
 * 						"returnSinceInception": 3.30172
 * 					},
 * 					"calendarReturns": {
 * 						"returnYTD": 0.74784,
 * 						"year1": 2.26267,	
 * 						"year2": 2.12798,
 * 						"year3": 5.44011,
 * 						"year4": 1.64847,
 * 						"year5": 3.01638
 * 					}
 * 				},	
 * 				"risks": [
 * 					{
 * 						"yearRisk": {
 * 							"year": 1,
 * 							"beta": 1.36,	
 * 							"stdDev": 3.326,
 * 							"alpha": -4.05,
 * 							"sharpeRatio": 0.962,
 * 							"rSquared": 80.84
 * 						}
 * 					},
 * 					{	
 * 						"yearRisk": {
 * 							"year": 3,
 * 							"beta": 1.07,
 * 							"stdDev": 3.476,
 * 							"alpha": -2.14,
 * 							"sharpeRatio": 0.552,
 * 							"rSquared": 81.30
 * 						}
 * 					},	
 * 					{
 * 						"yearRisk": {
 * 							"year": 5,
 * 							"beta": 1.01,
 * 							"stdDev": 3.107,	
 * 							"alpha": -1.77,	
 * 							"sharpeRatio": 0.714,
 * 							"rSquared": 76.66
 * 						}
 * 					},
 * 					{
 * 						"yearRisk": {
 * 							"year": 10,
 * 						}
 * 					}
 * 				],
 * 				"purchaseInfo": {
 * 					"minimumInitial": 500,
 * 					"minimumInitialCurrencyCode": "CAD",	
 * 					"minimumSubsequent": 100,
 * 					"minimumSubsequentCurrencyCode": "CAD",
 * 					"minimumIRA": 500,
 * 					"rrsp": true,
 * 					"loadType": "Front-end Charge",
 * 					"minimumIRACurrencyCode": "CAD"
 * 					}
 * 				}
 * 			],	
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */