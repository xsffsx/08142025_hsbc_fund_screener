/**
 * @api {get} /wmds/v4.0/quoteList Quote List
 * @apiName Quote List
 * @apiGroup Quote
 * @apiVersion 4.0.2
 * 
 * @apiUse QuoteHeaderParam_v402
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
 *			"prodAltNum": "MSFT",
 *			"productType": "SEC"
 *		},
 *		{
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "GOOG",
 *			"productType": "SEC"
 *		}],
 *		"market":"US",
 *		"entityTimezone":"EST",
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
 * @apiSuccess {Date} priceQuotes.expiryDate Denotes the expiration date of option.format:yyyy-MM-dd
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
 * @apiSuccess {Date} priceQuotes.exchangeUpdatedTime Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX
 * @apiSuccess {String} priceQuotes.inDaylightTime The in daylight time.
 * @apiSuccess {String} priceQuotes.exchangeTimezone The timezone of exchangeUpdatedTime. 
 * @apiSuccess {String} priceQuotes.tradeUnits The trade units.
 * @apiSuccess {Number} priceQuotes.previousClosePrice The previous close price.
 * @apiSuccess {Number} priceQuotes.openPrice The open price.
 * @apiSuccess {Number} priceQuotes.turnoverAmount The turnover amount.
 * @apiSuccess {String} priceQuotes.productStatus The product status.
 * @apiSuccess {Object[]} signedExchanges
 * @apiSuccess {String} signedExchanges.exchange The exchange.
 * @apiSuccess {String} signedExchanges.agreementId The agreement id.
 * @apiSuccess {Object[]} unSignedExchanges
 * @apiSuccess {String} unSignedExchanges.exchange The exchange. 
 * @apiSuccess {String} unSignedExchanges.agreementId The agreement id.
 * @apiSuccess {Date} entityUpdatedTime Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX
 * @apiSuccess {String} entityTimezone Used to format time "EST".
 * @apiSuccess {String} inDaylightTime Indicator to indentify if exchangeUpdateTime is in daylight saving.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"priceQuote": [
 * 				{
 * 					"exchangeTimezone": "Canada/Eastern",
 * 					"tradeUnits": "2DP ",
 * 					"previousClosePrice": 16.38,
 * 					"turnoverAmount": 0.1638,
 * 					"unsignedAgreementId": "",
 * 					"symbol": "XRE",
 * 					"market": "CA",
 * 					"productType": "ETF",
 * 					"priceQuote": 16.38,
 * 					"delay": true,
 * 					"currency": "CAD",
 * 					"companyName": "iShares S&ampP/TSX Capped REIT Index ETF",
 * 					"ric": "XRE.TO",
 * 					"prodCdeAltClassCde": "M",
 * 					"productStatus": "   ",	
 * 					"quoteVMA50D": "",		
 * 					"yearLowPrice": 14.6000,
 * 					"yearHighPrice": 17.3700,
 * 					"exchange": "TOR",
 * 				},
 * 				{
 * 					"exchangeTimezone": "Canada/Eastern",
 * 					"tradeUnits": "2DP ",
 * 					"previousClosePrice": 20.79,
 * 					"turnoverAmount": 0.2079,
 * 					"unsignedAgreementId": "",
 * 					"symbol": "ZWA",
 * 					"market": "CA",
 * 					"productType": "ETF",
 * 					"priceQuote": 20.79,
 * 					"delay": true,
 * 					"currency": "CAD",
 * 					"companyName": "BMO Covered Call DJIA Hedged to CAD ETF",
 * 					"ric": "ZWA.TO",
 * 					"prodCdeAltClassCde": "M",
 * 					"productStatus": "   ",
 * 					"quoteVMA50D": "",
 * 					"yearLowPrice": 17.3800,	
 * 					"yearHighPrice": 20.9100,
 * 					"exchange": "TOR",
 * 				}
 * 			],
 * 			"entityUpdatedTime": "2017-02-27T04:12:21.000+8:00",
 * 			"signedExchanges": [],	
 * 			"unSignedExchanges": [
 * 				{
 * 					"exchange": "TOR",
 * 					"agreementId": "1"
 * 				},
 * 				{
 * 					"exchange": "TOR",
 * 					"agreementId": "1"	
 * 				}
 * 			],	
 * 			"entityTimezone": "Canada/Eastern",
 * 			"inDaylightTime": "N",	
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */
