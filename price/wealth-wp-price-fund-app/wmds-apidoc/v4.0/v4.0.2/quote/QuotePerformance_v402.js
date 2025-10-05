/**
 * @api {get} /wmds/v4.0/quotePerformance Quote Performance
 * @apiName Quote Performance
 * @apiGroup Quote
 * @apiVersion 4.0.2
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
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
 * 
 * @apiParamExample Request:
 *		{
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "CAN048",
 *			"market":"US",
 *			"productType": "SEC"
 *		}
 * 
 * @apiSuccess {Object} performance
 * @apiSuccess {Object} performance.performanceOverMultipleTimeHorizons
 * @apiSuccess {Object[]} performance.performanceOverMultipleTimeHorizons.items
 * @apiSuccess {String} performance.performanceOverMultipleTimeHorizons.items.itemName The key to uniquely locate a item.
 * @apiSuccess {Number} performance.performanceOverMultipleTimeHorizons.items.return1Mth % Return (1 Month)</br> 
 * 																						Trailing total return of the fund for 1 month, expressed as a percentage</br>
 * 																						% Return (Category - 1 Month)</br>
 *																						Trailing total return of the category for 1 month, expressed as a percentage</br>		
 * 																						1-Month Quartile Ranking</br>
 *																						Fund's 1-month total-return quartile rank relative to all funds that have the same Morningstar category</br>
 * 																						Total No. of Funds (1 Month)</br>
 *																						Total no. of funds in the same Morningstar category when the rankings were conducted</br>
 * @apiSuccess {Number} performance.performanceOverMultipleTimeHorizons.items.return3Mth % Return (3 Month)</br>
 *																						Trailing total return of the fund for 3 months, expressed as a percentage</br>
 * 																						% Return (Category - 3 Month)</br>
 *																						Trailing total return of the category for 3 months, expressed as a percentage</br>	
 * 																						3-Month Quartile Ranking</br>
 *																						Fund's 3-month total-return quartile rank relative to all funds that have the same Morningstar category</br>
 * 																						Total No. of Funds (3 Month)</br>
 *																						Total no. of funds in the same Morningstar category when the rankings were conducted</br>
 * @apiSuccess {Number} performance.performanceOverMultipleTimeHorizons.items.return6Mth % Return (6 Month)</br>
 *																						Trailing total return of the fund for 6 months, expressed as a percentage</br>
 * 																						% Return (Category - 6 Month)</br>
 *																						Trailing total return of the category for 6 months, expressed as a percentage</br>
 * 																						6-Month Quartile Ranking</br>
 *																						Fund's 6-month total-return quartile rank relative to all funds that have the same Morningstar category</br>	
 * 																						Total No. of Funds (6 Month)</br>
 *																						Total no. of funds in the same Morningstar category when the rankings were conducted</br>
 * @apiSuccess {Number} performance.performanceOverMultipleTimeHorizons.items.return1Yr	% Return (1 Year)</br>
 *																						Trailing total return of the fund for 1 year, expressed as a percentage</br>
 * 																						% Return (Category - 1 Year)</br>
 *																						Trailing total return of the category for 1 year, expressed as a percentage</br>		
 * 																						1-Year Quartile Ranking</br>
 *																						Fund's 1-year total-return quartile rank relative to all funds that have the same Morningstar category</br>
 * 																						Total No. of Funds (1 Year)</br>
 *																						Total no. of funds in the same Morningstar category when the rankings were conducted</br>
 * @apiSuccess {Number} performance.performanceOverMultipleTimeHorizons.items.return3Yr  % Return (3 Year)</br>
 *																						Trailing total return of the fund for 3 years, expressed as a percentage</br>
 * 																						% Return (Category - 3 Year)</br>
 *																						Trailing total return of the category for 3 years, expressed as a percentage</br>
 * 																						3-Year Quartile Ranking</br>
 *																						Fund's 3-year total-return quartile rank relative to all funds that have the same Morningstar category</br>
 * 																						Total No. of Funds (3 Year)</br>
 *																						Total no. of funds in the same Morningstar category when the rankings were conducted</br>
 * @apiSuccess {Number} performance.performanceOverMultipleTimeHorizons.items.return5Yr	% Return (5 Year)</br>
 *																						Trailing total return of the fund for 5 years, expressed as a percentage</br>
 * 																						% Return (Category - 5 Year)</br>
 *																						Trailing total return of the category for 5 years, expressed as a percentage</br>
 * 																						5-Year Quartile Ranking</br>
 *																					    Fund's 5-year total-return quartile rank relative to all funds that have the same Morningstar category</br>
 * 																						Total No. of Funds (5 Year)</br>
 *																						Total no. of funds in the same Morningstar category when the rankings were conducted</br>
 * @apiSuccess {Date} performance.performanceOverMultipleTimeHorizons.lastUpdatedDate Date data were last updated, format: yyyy-MM-dd.
 * @apiSuccess {Object} performance.riskAndReturn
 * @apiSuccess {String} performance.riskAndReturn.riskRating3Yr Morningstar Risk Rating (3 Year)Morningstar's assessment of the variations in a fund's monthly returns, with an emphasis on downside variations, in comparison to funds in its category.
 * @apiSuccess {String} performance.riskAndReturn.returnRating3Yr Morningstar Return Rating (3 Year) Morningstar's assessment of a fund's excess return over a risk-free rate (the return of the 90-day Treasury bill) in comparison to funds in its category, after adjusting for all applicable loads and sales charges.
 * @apiSuccess {String} performance.riskAndReturn.lastUpdatedDate Date data were last updated , format: yyyy-MM-dd
 * @apiSuccess {Object} performance.riskMeasures
 * @apiSuccess {Object[]} performance.riskMeasures.items
 * @apiSuccess {String} performance.riskMeasures.items.itemName The key to uniquely locate a item.
 * @apiSuccess {Number} performance.riskMeasures.items.riskMeasures1Yr Beta (1 Year)</br>
 *																	  1-year volatility (systemic risk) of fund relative to the overall market</br>
 * 																	  Standard Deviation (1 Year)</br>
 *																	  1-year volatility (systemic risk) of the fund</br>
 * 																	  Alpha (1 Year)</br>
 *																	  1-year excess risk-adjusted return of fund relative to the return of a benchmark index</br>
 * 																	  Sharpe Ratio (1 Year)</br>
 *																	  Measure of 1-year risk-adjusted performance of the fund</br>	   
 * 																	  R-Squared (1 Year)</br>
 *																	  1-year correlation of a fund's movements to that of a benchmark index</br>	 
 * @apiSuccess {Number} performance.riskMeasures.items.riskMeasures3Yr Beta (3 Year)</br>
 *																	  3-year volatility (systemic risk) of fund relative to the overall market</br>
 * 																	  Standard Deviation (3 Year)</br>
 *																	  3-year volatility (systemic risk) of the fund</br>
 * 																	  Alpha (3 Year)</br>
 *																      3-year excess risk-adjusted return of fund relative to the return of a benchmark index</br>
 * 																  	  Sharpe Ratio (3 Year)</br>
 *																	  Measure of 3-year risk-adjusted performance of the fund</br>
 * 																	  R-Squared (3 Year)</br>
 *																	  3-year correlation of a fund's movements to that of a benchmark index</br>
 * @apiSuccess {Number} performance.riskMeasures.items.riskMeasures5Yr Beta (5 Year)</br>
 *																	  5-year volatility (systemic risk) of fund relative to the overall market</br>
 * 																	  Standard Deviation (5 Year)</br>
 *																	  5-year volatility (systemic risk) of the fund</br>
 * 																	  Alpha (5 Year)</br>
 *																	  5-year excess risk-adjusted return of fund relative to the return of a benchmark index</br>
 * 																	  Sharpe Ratio (5 Year)</br>
 *																	  Measure of 5-year risk-adjusted performance of the fund</br>	
 * 																	  R-Squared (5 Year)</br>
 *																	  5-year correlation of a fund's movements to that of a benchmark index</br>
 * @apiSuccess {Number} performance.riskMeasures.items.riskMeasures10Yr Beta (10 Year)</br>
 *																	   10-year volatility (systemic risk) of fund relative to the overall market</br>
 * 																	   Standard Deviation (10 Year)</br>
 *																	   10-year volatility (systemic risk) of the fund</br>
 * 																	   Alpha (10 Year)</br>
 *																	   10-year excess risk-adjusted return of fund relative to the return of a benchmark index</br>
 * 																	   Sharpe Ratio (10 Year)</br>
 *																	   Measure of 10-year risk-adjusted performance of the fund</br>
 * 																	   R-Squared (10 Year)</br>
 *																	   10-year correlation of a fund's movements to that of a benchmark index</br>
 * @apiSuccess {Number} performance.riskMeasures.items.riskMeasuresCategoryAvg1Yr Beta (Category Avg - 1 Year)</br>
 *																				 Average 1-year beta of the funds in the category</br>
 * 																				 Standard Deviation (Category Avg - 1 Year)</br>
 *																				 Average 1-year standard deviation of the funds in the category</br>
 * 																				 Alpha (Category Avg - 1 Year)</br>
 *																				 Average 1-year alpha of the funds in the category</br>	
 * 																				 Sharpe Ratio (Category Avg - 1 Year)</br>
 *																				 Average 1-year Sharpe ratio of the funds in the category</br>
 * 																				 R-Squared (Category Avg - 1 Year)</br>
 *																				 Average 1-year R-squared of the funds in the category</br>	
 * @apiSuccess {Number} performance.riskMeasures.items.riskMeasuresCategoryAvg3Yr Beta (Category Avg - 3 Year)</br>
 *																				 Average 3-year beta of the funds in the category</br>
 * 																				 Standard Deviation (Category Avg - 3 Year)</br>
 *																				 Average 3-year standard deviation of the funds in the category</br>
 * 																				 Alpha (Category Avg - 3 Year)</br>
 *																				 Average 3-year alpha of the funds in the category</br>	
 * 																				 Sharpe Ratio (Category Avg - 3 Year)</br>
 *																				 Average 3-year Sharpe ratio of the funds in the category</br>
 * 																				 R-Squared (Category Avg - 3 Year)</br>
 *																				 Average 3-year R-squared of the funds in the category
 * @apiSuccess {Number} performance.riskMeasures.items.riskMeasuresCategoryAvg5Yr Beta (Category Avg - 5 Year)</br>
 *																				 Average 5-year beta of the funds in the category</br>	
 * 																				 Standard Deviation (Category Avg - 5 Year)</br>
 *																				 Average 5-year standard deviation of the funds in the category	</br>
 * 																				 Alpha (Category Avg - 5 Year)</br>
 *																				 Average 5-year alpha of the funds in the category</br>
 * 																				 Sharpe Ratio (Category Avg - 5 Year)</br>
 *																				 Average 5-year Sharpe ratio of the funds in the category</br>
 * 																				 R-Squared (Category Avg - 5 Year)</br>
 *																				 Average 5-year R-squared of the funds in the category</br>
 * @apiSuccess {Number} performance.riskMeasures.items.riskMeasuresCategoryAvg10Yr Beta (Category Avg - 10 Year)</br>
 *																				  Average 10-year beta of the funds in the category</br>
 * 																				  Standard Deviation (Category Avg - 10 Year)</br>
 *																				  Average 10-year standard deviation of the funds in the category</br>
 * 																				  Alpha (Category Avg - 10 Year)</br>
 *																				  Average 10-year alpha of the funds in the category</br>	
 * 																				  Sharpe Ratio (Category Avg - 10 Year)</br>
 *																				  Average 10-year Sharpe ratio of the funds in the category</br>
 * 																				  R-Squared (Category Avg - 10 Year)</br>
 *																				  Average 10-year R-squared of the funds in the category</br>	
 * @apiSuccess {Date} performance.riskMeasures.lastUpdatedDate Date data were last updated , format: yyyy-MM-dd.
 * @apiSuccess {Object[]} multiTimeChartDatas
 * @apiSuccess {String} multiTimeChartDatas.multipleTimePeriod The time period.
 * @apiSuccess {Number} multiTimeChartDatas.q0
 * @apiSuccess {Number} multiTimeChartDatas.q25	
 * @apiSuccess {Number} multiTimeChartDatas.q50
 * @apiSuccess {Number} multiTimeChartDatas.q75
 * @apiSuccess {Number} multiTimeChartDatas.q100
 * @apiSuccess {Number} multiTimeChartDatas.div
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"performance": {
 * 				"performanceOverMultipleTimeHorizons": {
 * 					"items": [
 * 						{
 * 							"itemName": "FUND",
 * 							"return1Mth": 1.24350,	
 * 							"return3Mth": 0.81013,
 * 							"return6Mth": -1.29536,
 * 							"return1Yr": 3.70035,
 * 							"return3Yr": 2.51233,	
 * 							"return5Yr": 2.96066
 * 						},
 * 						{
 * 							"itemName": "CAT",
 * 							"return1Mth": -0.25388,
 * 							"return3Mth": -0.82579,
 * 							"return6Mth": -0.83644,
 * 							"return1Yr": 3.84002,
 * 							"return3Yr": 3.39226,
 * 							"return5Yr": 3.67950
 * 						},
 * 						{
 * 							"itemName": "QTL",
 * 						},	
 * 						{
 * 							"itemName": "TTL",
 * 							"return1Mth": 544,
 * 							"return3Mth": 544,
 * 							"return6Mth": 544,
 * 							"return1Yr": 508,
 * 							"return3Yr": 423,
 * 							"return5Yr": 266
 * 						}
 * 					],
 * 					"lastUpdatedDate": "2017-02-28"
 * 				},	
 * 				"riskAndReturn": {	
 * 					"riskRating3Yr": "Below Average",	
 * 					"returnRating3Yr": "Below Average",
 * 					"lastUpdatedDate": "2017-01-31"
 * 				},	
 * 				"riskMeasures": {
 * 					"items": [
 * 						{
 * 							"itemName": "BETA",
 * 							"riskMeasures1Yr": 1.32,
 * 							"riskMeasures3Yr": 1.05,
 * 							"riskMeasures5Yr": 1.00,
 * 							"riskMeasuresCategoryAvg1Yr": 1.35,
 * 							"riskMeasuresCategoryAvg3Yr": 1.23,
 * 							"riskMeasuresCategoryAvg5Yr": 1.20,
 * 							"riskMeasuresCategoryAvg10Yr": 1.07
 * 						},
 * 						{
 * 							"itemName": "STD_DVIAT",			
 * 							"riskMeasures1Yr": 3.326,	
 * 							"riskMeasures3Yr": 3.476,
 * 							"riskMeasures5Yr": 3.107,
 * 							"riskMeasuresCategoryAvg1Yr": 3.277,
 * 							"riskMeasuresCategoryAvg3Yr": 3.868,
 * 							"riskMeasuresCategoryAvg5Yr": 3.455,
 * 							"riskMeasuresCategoryAvg10Yr": 4.192	
 * 						},
 * 						{
 * 							"itemName": "ALPHA",
 * 							"riskMeasures1Yr": -4.18,
 * 							"riskMeasures3Yr": -2.31,	
 * 							"riskMeasures5Yr": -1.90,
 * 							"riskMeasuresCategoryAvg1Yr": -2.34,
 * 							"riskMeasuresCategoryAvg3Yr": -2.04,	
 * 							"riskMeasuresCategoryAvg5Yr": -1.70,
 * 							"riskMeasuresCategoryAvg10Yr": -1.66"
 * 						},	
 * 						{
 * 							"itemName": "SHRP_RATIO",
 * 							"riskMeasuresCategoryAvg1Yr": 1.021,
 * 							"riskMeasuresCategoryAvg3Yr": 0.713,
 * 							"riskMeasuresCategoryAvg5Yr": 0.837,
 * 							"riskMeasuresCategoryAvg10Yr": 0.375		
 * 						},
 * 						{
 * 							"itemName": "R_SQUARED",
 * 							"riskMeasures1Yr": 81.19,
 * 							"riskMeasures3Yr": 81.08,
 * 							"riskMeasures5Yr": 76.39,
 * 							"riskMeasuresCategoryAvg1Yr": 83.93,		
 * 							"riskMeasuresCategoryAvg3Yr": 88.57,
 * 							"riskMeasuresCategoryAvg5Yr": 87.37,	
 * 							"riskMeasuresCategoryAvg10Yr": 81.23
 * 						}	
 * 					],
 * 					"lastUpdatedDate": "2017-02-28"
 * 				}	
 * 			},
 * 			"multiTimeChartDatas": [
 * 				{
 * 					"multipleTimePeriod": "1M",
 * 						"q0": -0.17465,
 * 						"q50": -0.49308,
 * 						"div": 1.24350	
 * 				},
 * 				{
 * 					"multipleTimePeriod": "3M",
 * 					"q0": 3.08456,
 * 					"q50": 2.59923,
 * 					"div": 0.81013
 * 				},
 * 				{
 * 					"multipleTimePeriod": "6M",
 * 					"q0": 0.12370,
 * 					"q50": -0.26084,
 * 					"div": -1.29536
 * 				},
 * 				{		
 * 					"multipleTimePeriod": "1Y",
 * 					"q0": 10.69749,
 * 					"q25": 8.10207,
 * 					"q50": 7.99804,
 * 					"q75": 7.76706,
 * 					"div": 3.70035
 * 				},
 * 				{
 * 					"multipleTimePeriod": "3Y",
 * 					"q0": -4.44376,
 * 					"div": 2.51233
 * 				},	
 * 				{
 * 					"multipleTimePeriod": "5Y",
 * 					"q0": -5.72777,
 * 					"div": 2.96066
 * 				}	
 * 			],	
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */