/**
 * @api {get} /wealth/api/v1/market-data/fund/list QuoteList
 * @apiName QuoteList
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {Object[]} productKeys 
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKeys.productType Product Type. If this field is null, then the productType require to define in productKey.
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
 * @apiParam {Boolean} delay Base on this to select table to retrieve data from realtime or delay table,true = Delay Quote; false = Real Time Quote
 * @apiParam {Boolean} [tradeDay] Indicates whether it is a trading day.
 * @apiParam {String} [tradeHours] Time range in local time.sample value:09001200:14001600
 * @apiParam {Number} [freeQuote] Free quote granted to customer.
 * @apiParam {String} [requestType] Char:-1 - no limit</br> 
 * 										0 - level 0</br>
 *                                      1 - level 1</br>
 *                                      2 - level 2</br>
 *                                      3 - level 3</br>
 *                                      4 - level 4</br>
 *                                      sample value for Bond: ALL, PRICE	   
 * @apiParam {String} [skipAgreementCheck] Indicator to indentify if agreement checking can be skipped. Which provides convenient for order placement to get real-time qoute for awalys.                                      
 * @apiParam {String} [entityTimezone] Used to format time "EST".
 * 
 * @apiParamExample Request:
 * {
 *		"productKeys": [{
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "540003",
 *			"productType": "UT"
 *		},
 *		{
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "540009",
 *			"productType": "UT"
 *		}],
 *		"market":"CN",
 *		"entityTimezone":"Asia/Hong_Kong",
 *		"delay": true
 *	}	
 * 
 * @apiSuccess {Number} remainingQuote The remaining free quote for the user.
 * @apiSuccess {Number} totalQuote Total free quote available.
 * @apiSuccess {String} requestType Sample value:0 , 1 , 2 , 3 , 4.
 * @apiSuccess {Object[]} prodAltNumSegs
 * @apiSuccess {String} prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} prodAltNumSegs.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {Object[]} priceQuotes
 * @apiSuccess {String} priceQuotes.ric The ric code of the product.
 * @apiSuccess {String} priceQuotes.symbol The symbol of the product.
 * @apiSuccess {String} priceQuotes.market The market of the product. 
 * @apiSuccess {String} priceQuotes.productType Product Type , If this field is null, then the productType require to define in productKey.
 * @apiSuccess {String} priceQuotes.underlyingProductType Denotes the type of the underlying product.
 * @apiSuccess {String} priceQuotes.underlyingSymbol The underlying symbol.
 * @apiSuccess {String} priceQuotes.underlyingMarket The underlying market.
 * @apiSuccess {String} priceQuotes.rootSymbol The root symbol.
 * @apiSuccess {String} priceQuotes.optionType Denotes the type of the option.
 * @apiSuccess {String} priceQuotes.expiryDate Denotes the expiration date of option.
 * @apiSuccess {Number} priceQuotes.strike Denotes the strike price of option.
 * @apiSuccess {Number} priceQuotes.priceQuote The price of the product.
 * @apiSuccess {Number} priceQuotes.bidPrice The bid price.
 * @apiSuccess {Number} priceQuotes.askPrice The ask price.
 * @apiSuccess {Boolean} priceQuotes.delay BE use this field to indicate if it is returning a real-time or delay quote.sample value:true-delayed quote, false-realtime quote
 * @apiSuccess {String} priceQuotes.currency The currency of the qoute.
 * @apiSuccess {String} priceQuotes.companyName The product company name.
 * @apiSuccess {Number} priceQuotes.changeAmount The change number.
 * @apiSuccess {Number} priceQuotes.changePercent The change percent.
 * @apiSuccess {String} priceQuotes.exchange The exchange.
 * @apiSuccess {Number} priceQuotes.dayRangeLow The day low of the specified symbol.
 * @apiSuccess {Number} priceQuotes.dayRangeHigh The day high of the specified symbol.
 * @apiSuccess {Number} priceQuotes.yearLowPrice The year low of the specified symbol.
 * @apiSuccess {Number} priceQuotes.yearHighPrice The year high of the specified symbol.
 * @apiSuccess {String} priceQuotes.unsignedAgreementId T&C Indicator
 * @apiSuccess {Number} priceQuotes.quoteVMA50D Volume Moving Averages - 50 days (ETFs Only)
 * @apiSuccess {Date} priceQuotes.exchangeUpdatedTime Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd
 * @apiSuccess {String} priceQuotes.inDaylightTime The in daylight time.
 * @apiSuccess {String} priceQuotes.exchangeTimezone The timezone of exchangeUpdatedTime. 
 * @apiSuccess {String} priceQuotes.tradeUnits The trade units.
 * @apiSuccess {Number} priceQuotes.previousClosePrice The previous close price.
 * @apiSuccess {Number} priceQuotes.openPrice The open price.
 * @apiSuccess {Number} priceQuotes.turnoverAmount The turnover amount.
 * @apiSuccess {String} priceQuotes.productStatus The product status.
 * @apiSuccess {Number} priceQuotes.dividendYield Yield based on the most recent dividend distribution.
 * @apiSuccess {String} priceQuotes.distributionFrequency Interval at which reqular dividends are distributed.
 * @apiSuccess {String} priceQuotes.topPerformersIndicator Top Performers Indicator.
 * @apiSuccess {String} priceQuotes.topSellProdIndex hhhh BEST SELLER Indicator.
 * @apiSuccess {String} priceQuotes.categoryLevel1Code Level category code.
 * @apiSuccess {String} priceQuotes.categoryLevel1Name Level category name.
 * @apiSuccess {Object} priceQuotes.cumulativeReturns
 * @apiSuccess {Object[]} priceQuotes.cumulativeReturns.items
 * @apiSuccess {Number} priceQuotes.cumulativeReturns.items.period The period of return data.
 * @apiSuccess {Number} priceQuotes.cumulativeReturns.items.trailingTotalReturn Fund performance Returns(Monthly) by cumulative.
 * @apiSuccess {Number} priceQuotes.cumulativeReturns.items.dailyPerformanceNAV Fund performance Returns(Daily) by cumulative.
 * @apiSuccess {String} priceQuotes.cumulativeReturns.monthEndDate Last updat Date of Fund performance Returns(Monthly).
 * @apiSuccess {String} priceQuotes.cumulativeReturns.dayEndDate Last updat Date of Fund performance Returns(Daily).
 * @apiSuccess {Object} priceQuotes.calendarReturns
 * @apiSuccess {Object[]} priceQuotes.calendarReturns.items
 * @apiSuccess {Number} priceQuotes.calendarReturns.items.period The period of return data.
 * @apiSuccess {Number} priceQuotes.calendarReturns.items.annualCalendarYearReturn Fund performance Returns(Monthly) by calendar.
 * @apiSuccess {String} priceQuotes.calendarReturns.monthEndDate Last updat Date of Fund performance Returns(Monthly).
 * @apiSuccess {String} priceQuotes.riskLvlCde hhhh BEST SELLER Indicator.
 * @apiSuccess {String} priceQuotes.launchDate hhhh BEST SELLER Indicator.
 * @apiSuccess {Object[]} signedExchanges
 * @apiSuccess {String} signedExchanges.exchange The exchange.
 * @apiSuccess {String} signedExchanges.agreementId The agreement id.
 * @apiSuccess {Object[]} unSignedExchanges
 * @apiSuccess {String} unSignedExchanges.exchange The exchange. 
 * @apiSuccess {String} unSignedExchanges.agreementId The agreement id.
 * @apiSuccess {String} entityUpdatedTime Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX
 * @apiSuccess {String} entityTimezone Used to format time "EST".
 * @apiSuccess {String} inDaylightTime Indicator to indentify if exchangeUpdateTime is in daylight saving.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 *			"requestType": null,
 *			"remainingQuote": null,
 *			"totalQuote": null,
 *			"priceQuotes": [{
 *				"ric": "000000000000000000001000041236",
 *				"symbol": "540003",
 *				"market": "CN",
 *				"productType": "UT",
 *				"underlyingProductType": null,
 *				"priceQuote": 1.6821,
 *				"delay": true,
 *				"currency": "CNY",
 *				"companyName": "hhhh Jintrust Dyanmic Strategy Balanced Fund",
 *				"changeAmount": -0.003,
 *				"changePercent": -0.17803,
 *				"unsignedAgreementId": null,
 *				"quoteVMA50D": null,
 *				"prodAltNum": null,
 *				"prodCdeAltClassCde": null,
 *				"productStatus": null,
 *				"dayRangeLow": null,
 *				"dayRangeHigh": null,
 *				"yearLowPrice": null,
 *				"yearHighPrice": null,
 *				"exchange": null,
 *				"underlyingSymbol": null,
 *				"underlyingMarket": null,
 *				"rootSymbol": null,
 *				"optionType": null,
 *				"expiryDate": null,
 *				"strike": null,
 *				"exchangeUpdatedTime": "2017-05-17",
 *				"inDaylightTime": null,
 *				"exchangeTimezone": null,
 *				"tradeUnits": null,
 *				"previousClosePrice": null,
 *				"openPrice": null,
 *				"turnoverAmount": null,
 *				"bidPrice": null,
 *				"askPrice": null,
 *				"dividendYield": 1.3612,
 *				"distributionFrequency": "Annually",
 *				"topPerformersIndicator": null,
 *				"topSellProdIndex": null,
 *				"categoryLevel1Code": "FI",
 *				"categoryLevel1Name": "Fixed Income",
 *				"calendarReturns": {
 *				    "items": [
 *				        {
 *				            "year": 2017,
 *				            "annualCalendarYearReturn": 3.3371
 *				        },
 *				        {
 *				            "year": 2016,
 *				            "annualCalendarYearReturn": 2.28603
 *				        },
 *				        {
 *				            "year": 2015,
 *				            "annualCalendarYearReturn": 0.2892
 *				        },
 *				        {
 *				            "year": 2014,
 *				            "annualCalendarYearReturn": 6.81214
 *				        },
 *				        {
 *				            "year": 2013,
 *				            "annualCalendarYearReturn": -3.28249
 *				        }
 *					],
 *					"monthEndDate": "2018-08-31"
 *				},
 *				"cumulativeReturns": {
 *					"items": [
 *				        {
 *				            "period": "YTD",
 *				            "trailingTotalReturn": -0.83548,
 *				            "dailyPerformanceNAV": -1.59752
 *				        },
 *				        {
 *				            "period": "1M",
 *				            "trailingTotalReturn": 0.7368,
 *				            "dailyPerformanceNAV": -0.32876
 *				        },
 *				        {
 *				            "period": "3M",
 *				            "trailingTotalReturn": 0.63658,
 *				            "dailyPerformanceNAV": 0.21932
 *				        },
 *				        {
 *				            "period": "6M",
 *				            "trailingTotalReturn": 1.34235,
 *				            "dailyPerformanceNAV": 0.20503
 *				        },
 *				        {
 *				            "period": "1Y",
 *				            "trailingTotalReturn": -1.34289,
 *				            "dailyPerformanceNAV": -1.84312
 *				        },
 *				        {
 *				            "period": "3Y",
 *				            "trailingTotalReturn": 1.40125,
 *				            "dailyPerformanceNAV": 1.06878
 *				        },
 *				        {
 *				            "period": "5Y",
 *				            "trailingTotalReturn": 2.41111,
 *				            "dailyPerformanceNAV": 2.43838
 *				        },
 *				        {
 *				            "period": "10Y",
 *				            "trailingTotalReturn": 3.97705,
 *				            "dailyPerformanceNAV": 3.89687
 *				        }
 *					],
 *					"monthEndDate": "2018-08-31",
 *					"dayEndDate": "2018-09-14"
 *				},
 *				"riskLvlCde": "3",
 *				"launchDate": "2003-02-24"
 *			},
 *			{
 *				"ric": "000000000000000000001000041330",
 *				"symbol": "540009",
 *				"market": "CN",
 *				"productType": "UT",
 *				"underlyingProductType": null,
 *				"priceQuote": 1.0244,
 *				"delay": true,
 *				"currency": "CNY",
 *				"companyName": "hhhh Jintrust  China Consumption Equity Fund",
 *				"changeAmount": -0.0058,
 *				"changePercent": -0.563,
 *				"unsignedAgreementId": null,
 *				"quoteVMA50D": null,
 *				"prodAltNum": null,
 *				"prodCdeAltClassCde": null,
 *				"productStatus": null,
 *				"dayRangeLow": null,
 *				"dayRangeHigh": null,
 *				"yearLowPrice": null,
 *				"yearHighPrice": null,
 *				"exchange": null,
 *				"underlyingSymbol": null,
 *				"underlyingMarket": null,
 *				"rootSymbol": null,
 *				"optionType": null,
 *				"expiryDate": null,
 *				"strike": null,
 *				"exchangeUpdatedTime": "2017-05-17",
 *				"inDaylightTime": null,
 *				"exchangeTimezone": null,
 *				"tradeUnits": null,
 *				"previousClosePrice": null,
 *				"openPrice": null,
 *				"turnoverAmount": null,
 *				"bidPrice": null,
 *				"askPrice": null,
 *				"dividendYield": 0,
 *				"distributionFrequency": "Annually",
 *				"topPerformersIndicator": null,
 *				"categoryLevel1Code": "FI",
 *				"categoryLevel1Name": "Fixed Income",
 *				"calendarReturns": {
 *				    "items": [
 *				        {
 *				            "year": 2017,
 *				            "annualCalendarYearReturn": 3.3371
 *				        },
 *				        {
 *				            "year": 2016,
 *				            "annualCalendarYearReturn": 2.28603
 *				        },
 *				        {
 *				            "year": 2015,
 *				            "annualCalendarYearReturn": 0.2892
 *				        },
 *				        {
 *				            "year": 2014,
 *				            "annualCalendarYearReturn": 6.81214
 *				        },
 *				        {
 *				            "year": 2013,
 *				            "annualCalendarYearReturn": -3.28249
 *				        }
 *					],
 *					"monthEndDate": "2018-08-31"
 *				},
 *				"cumulativeReturns": {
 *					"items": [
 *				        {
 *				            "period": "YTD",
 *				            "trailingTotalReturn": -0.83548,
 *				            "dailyPerformanceNAV": -1.59752
 *				        },
 *				        {
 *				            "period": "1M",
 *				            "trailingTotalReturn": 0.7368,
 *				            "dailyPerformanceNAV": -0.32876
 *				        },
 *				        {
 *				            "period": "3M",
 *				            "trailingTotalReturn": 0.63658,
 *				            "dailyPerformanceNAV": 0.21932
 *				        },
 *				        {
 *				            "period": "6M",
 *				            "trailingTotalReturn": 1.34235,
 *				            "dailyPerformanceNAV": 0.20503
 *				        },
 *				        {
 *				            "period": "1Y",
 *				            "trailingTotalReturn": -1.34289,
 *				            "dailyPerformanceNAV": -1.84312
 *				        },
 *				        {
 *				            "period": "3Y",
 *				            "trailingTotalReturn": 1.40125,
 *				            "dailyPerformanceNAV": 1.06878
 *				        },
 *				        {
 *				            "period": "5Y",
 *				            "trailingTotalReturn": 2.41111,
 *				            "dailyPerformanceNAV": 2.43838
 *				        },
 *				        {
 *				            "period": "10Y",
 *				            "trailingTotalReturn": 3.97705,
 *				            "dailyPerformanceNAV": 3.89687
 *				        }
 *					],
 *					"monthEndDate": "2018-08-31",
 *					"dayEndDate": "2018-09-14"
 *				},
 *				"riskLvlCde": "3",
 *				"launchDate": "2003-02-24"
 *			}],
 *			"entityUpdatedTime": null,
 *			"entityTimezone": null,
 *			"inDaylightTime": null,
 *			"signedExchanges": null,
 *			"unSignedExchanges": null,
 *			"prodAltNumSegs": null
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */