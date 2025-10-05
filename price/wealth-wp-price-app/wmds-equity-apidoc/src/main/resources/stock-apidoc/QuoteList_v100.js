/**
 * @api {get} /wealth/api/v1/market-data/stock/quoteList Quote List
 * @apiName Quote List
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {Object[]} productKeys The key to uniquely locate a product.
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKeys.productType Product Type.
 * @apiParam {Boolean} delay <ul><li>true: Delay Quote</li></ul>
 * @apiParam {String} market The investment market of the product. <ul><li>HK: Hong Kong</li></ul>
 * 
 * @apiParamExample Request:
 *  {
 *		"market": "HK",
 *		"productKeys": [{
 *			"productType": "SEC",
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "00005"
 *		},
 *		{
 *			"productType": "SEC",
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "00006"
 *		}],
 *		"delay": true
 *	}
 * 
 * @apiUse quoteListResponse
 * 
 * @apiSuccessExample Success-Response:
 *  HTTP/1.1 200 OK
 *  {
 *  	"priceQuotes": [{
 *  		"symbol": "00005",
 *  		"companyName": "hhhh HOLDINGS",
 *  		"nominalPrice": 76.0000000,
 *  		"currency": "HKD",
 *  		"changeAmount": 0E-7,
 *  		"changePercent": 0.0000,
 *  		"dayLowPrice": 75.7000000,
 *  		"dayHighPrice": 76.4000000,
 *  		"peRatio": "",
 *  		"dividendYield": "",
 *  		"yearLowPrice": 56.6000000,
 *  		"yearHighPrice": 79.5500000,
 *  		"asOfDateTime": "2018-11-02T16:03:20.000+08:00",
 *  		"turnover": 1283295412.0000,
 *  		"volume": ""
 *  	},
 *  	{
 *  		"symbol": "00006",
 *  		"companyName": "POWER ASSETS",
 *  		"nominalPrice": 68.9500000,
 *  		"currency": "HKD",
 *  		"changeAmount": -0.0500000,
 *  		"changePercent": -0.0720,
 *  		"dayLowPrice": 68.3500000,
 *  		"dayHighPrice": 69.0000000,
 *  		"peRatio": "",
 *  		"dividendYield": "",
 *  		"yearLowPrice": 54.3300000,
 *  		"yearHighPrice": 70.5300000,
 *  		"asOfDateTime": "2018-11-02T16:03:22.000+08:00",
 *  		"turnover": 185372299.0000,
 *  		"volume": ""
 *  	}],
 *  	"totalFreeQuote": "",
 *  	"quoteRemaining": "",
 *  	"delay": ""
 *  }
 * 
 * @apiUse ErrorMsgResponse
 */

/**
 * @apiDefine quoteListResponse
 * 
 * @apiSuccess {Object[]} priceQuotes
 * @apiSuccess {String} priceQuotes.symbol Symbol of request stock.
 * @apiSuccess {String} priceQuotes.companyName The company name of the specified symbol.
 * @apiSuccess {Number} priceQuotes.nominalPrice Nominal price of the specified symbol. 7 decimal places.
 * @apiSuccess {String} [priceQuotes.currency] Trading currency of the specified symbol.
 * @apiSuccess {Number} [priceQuotes.changeAmount] Change in amount of the nominal price versus previous closing price. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.changePercent] Change in percentage of the nominal price versus previous closing price. 4 decimal places.
 * @apiSuccess {Number} [priceQuotes.dayLowPrice] Lowest traded price for this trading day. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.dayHighPrice] Highest traded price for this trading day. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.peRatio] P/E ration.
 * @apiSuccess {Number} [priceQuotes.dividendYield] Dividend yield percentage.
 * @apiSuccess {Number} [priceQuotes.yearLowPrice] Year low price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.yearHighPrice] Year high price of the specified symbol. 7 decimal places.
 * @apiSuccess {Date} [priceQuotes.asOfDateTime] Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSZ.
 * @apiSuccess {Number} [priceQuote.turnover] Turnover of the specified symbol. 4 decimal places.
 * @apiSuccess {Number} [priceQuote.volume] Number of shares traded in this trading day.
 * @apiSuccess {Number} [totalFreeQuote] Total free quote available.
 * @apiSuccess {Number} [quoteRemaining] The remaining free quote for the user.
 * @apiSuccess {Boolean} [delay] true: delayed quote; false: real-time quote.
 */