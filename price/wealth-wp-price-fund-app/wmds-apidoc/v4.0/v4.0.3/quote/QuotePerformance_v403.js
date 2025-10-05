/**
 * @api {get} /wmds/v4.0/quotePerformance Quote Performance
 * @apiName Quote Performance
 * @apiGroup Quote
 * @apiVersion 4.0.3
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
 *			"prodAltNum": "540002",
 *			"market":"US",
 *			"productType": "UT"
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
 *		{
 *			"performance": {
 *				"performanceOverMultipleTimeHorizons": {
 *					"items": [{
 *						"itemName": "FUND",
 *						"return1Mth": -1.75128,
 *						"return3Mth": -4.1248,
 *						"return6Mth": 2.68697,
 *						"return1Yr": 20.59045,
 *						"return3Yr": 28.45171,
 * 						"return5Yr": 18.40113
 *					},
 *					{
 *						"itemName": "CAT",
 *						"return1Mth": -2.76554,
 *						"return3Mth": -2.30054,
 *						"return6Mth": -4.68199,
 *						"return1Yr": 3.07824,
 *						"return3Yr": 17.05157,
 *						"return5Yr": 11.40315
 *					},
 *					{
 *						"itemName": "QTL",
 *						"return1Mth": 2,
 *						"return3Mth": 3,
 *						"return6Mth": 1,
 *						"return1Yr": 1,
 *						"return3Yr": 1,
 *						"return5Yr": 1
 *					},
 *					{
 *						"itemName": "TTL",
 *						"return1Mth": 631,
 *						"return3Mth": 621,
 *						"return6Mth": 610,
 *						"return1Yr": 602,
 *						"return3Yr": 502,
 *						"return5Yr": 394
 *					}],
 *					"lastUpdatedDate": "2017-05-31"
 *				},
 *				"riskAndReturn": {
 *					"riskRating3Yr": "Below Average",
 *					"returnRating3Yr": "High",
 *					"lastUpdatedDate": "2017-05-31"
 *				},
 *				"riskMeasures": {
 *					"items": [{
 *						"itemName": "BETA",
 *						"riskMeasures1Yr": 0.72,
 *						"riskMeasures3Yr": 0.98,
 *						"riskMeasures5Yr": 0.92,
 *						"riskMeasures10Yr": 0.79,
 *						"riskMeasuresCategoryAvg1Yr": 0.54,
 *						"riskMeasuresCategoryAvg3Yr": 0.82,
 *						"riskMeasuresCategoryAvg5Yr": 0.8,
 *						"riskMeasuresCategoryAvg10Yr": 0.75
 *					},
 *					{
 *						"itemName": "STD_DVIAT",
 *						"riskMeasures1Yr": 8.938,
 *						"riskMeasures3Yr": 30.291,
 *						"riskMeasures5Yr": 26.603,
 *						"riskMeasures10Yr": 27.152,
 *						"riskMeasuresCategoryAvg1Yr": 11.175,
 *						"riskMeasuresCategoryAvg3Yr": 33.208,
 *						"riskMeasuresCategoryAvg5Yr": 28.443,
 *						"riskMeasuresCategoryAvg10Yr": 27.766
 *					},
 *					{
 *						"itemName": "ALPHA",
 *						"riskMeasures1Yr": 10.86,
 *						"riskMeasures3Yr": 11.36,
 *						"riskMeasures5Yr": 14.05,
 *						"riskMeasures10Yr": 9.52,
 *						"riskMeasuresCategoryAvg1Yr": -3.46,
 *						"riskMeasuresCategoryAvg3Yr": 5.9,
 *						"riskMeasuresCategoryAvg5Yr": 9.14,
 *						"riskMeasuresCategoryAvg10Yr": 5.53
 *					},
 *					{
 *						"itemName": "SHRP_RATIO",
 *						"riskMeasures1Yr": 2.029,
 *						"riskMeasures3Yr": 0.926,
 *						"riskMeasures5Yr": 0.691,
 *						"riskMeasures10Yr": 0.344,
 *						"riskMeasuresCategoryAvg1Yr": 0.32,
 *						"riskMeasuresCategoryAvg3Yr": 0.614,
 *						"riskMeasuresCategoryAvg5Yr": 0.458,
 *						"riskMeasuresCategoryAvg10Yr": 0.196
 *					},
 *					{
 *						"itemName": "R_SQUARED",
 *						"riskMeasures1Yr": 53.76,
 *						"riskMeasures3Yr": 83.22,
 *						"riskMeasures5Yr": 76.18,
 *						"riskMeasures10Yr": 78.56,
 *						"riskMeasuresCategoryAvg1Yr": 24.84,
 *						"riskMeasuresCategoryAvg3Yr": 50,
 *						"riskMeasuresCategoryAvg5Yr": 51.95,
 *						"riskMeasuresCategoryAvg10Yr": 67.84
 *					}],
 *					"lastUpdatedDate": "2017-05-31"
 *				}
 *			},
 *			"multiTimeChartDatas": [{
 *				"multipleTimePeriod": "1M",
 *				"q0": 3.80383,
 *				"q25": 0.35895,
 *				"q50": -1.56545,
 *				"q75": -3.59232,
 *				"q100": -14.60109,
 *				"div": -1.75128
 *			},
 *			{
 *				"multipleTimePeriod": "3M",
 *				"q0": 10.74074,
 *				"q25": 2.70002,
 *				"q50": -1.39934,
 *				"q75": -5.51929,
 *				"q100": -22.31016,
 *				"div": -4.1248
 *			},
 *			{
 *				"multipleTimePeriod": "6M",
 *				"q0": 13.82616,
 *				"q25": 1.63841,
 *				"q50": -3.48577,
 *				"q75": -9.1129,
 *				"q100": -27.25857,
 *				"div": 2.68697
 *			},
 *			{
 *				"multipleTimePeriod": "1Y",
 *				"q0": 19.88826,
 *				"q25": 6.10263,
 *				"q50": -0.49424,
 *				"q75": -6.98694,
 *				"q100": -35.61845,
 *				"div": 20.59045
 *			},
 *			{
 *				"multipleTimePeriod": "3Y",
 *				"q0": 127.52044,
 *				"q25": 70.16875,
 *				"q50": 45.92319,
 *				"q75": 28.62453,
 *				"q100": -25.3251,
 *				"div": 28.45171
 *			},
 *			{
 *				"multipleTimePeriod": "5Y",
 *				"q0": 201.99889,
 *				"q25": 93.46999,
 *				"q50": 61.65392,
 *				"q75": 33.27851,
 *				"q100": -21.42188,
 *				"div": 18.40113
 *			}]
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */