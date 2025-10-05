/**
 * @api {get} /wealth/api/v1/market-data/equity/quotes/same-sector-equity SameSectorEquity
 * @apiName SameSectorEquity
 * @apiGroup Quote
 * @apiVersion 1.0.2
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {String} productType Product Type. Stock is "SEC".
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} market The investment market of the product. <ul><li>HK: Hong Kong</li><li>US: United States</li></ul>
 * @apiParam {Boolean} delay <ul><li>true: Delay Quote</li></ul>
 * @apiParam {String} [sortBy] Default value is "Volume".
 * @apiParam {String} [sortOrder] Default value is "desc".
 * @apiParam {Number} [number] Default value is 10.
 * 
 * @apiParamExample Request:
 *  {
 *  	"productType": "SEC",
 *  	"prodCdeAltClassCde": "M",
 *  	"prodAltNum": "FFIC",
 *  	"market": "US",
 *  	"delay": true
 *  }
 * 
 * @apiUse sameSectorResponse
 * 
 * @apiSuccessExample Success-Response:
 *  HTTP/1.1 200 OK
 *  {
 *  	"priceQuotes": [{
 *  		"ric": "FFIC.O",
 *  		"symbol": "FFIC",
 *  		"market": "US",
 *  		"exchange": "NSQ",
 *  		"companyName": "FLUSHING FINL",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"sharePrice": 22.19,
 *  		"changeAmount": 0.18,
 *  		"changePercent": 0.8178
 *  	},
 *  	{
 *  		"ric": "AABA.O",
 *  		"symbol": "AABA",
 *  		"market": "US",
 *  		"exchange": "NSQ",
 *  		"companyName": "ALTABA INC ORD",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"sharePrice": 62.78,
 *  		"changeAmount": 0.49,
 *  		"changePercent": 0.7866
 *  	},
 *  	{
 *  		"ric": "HBAN.O",
 *  		"symbol": "HBAN",
 *  		"market": "US",
 *  		"exchange": "NSQ",
 *  		"companyName": "HUNTGTN BKSHR",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"sharePrice": 12.88,
 *  		"changeAmount": 0.05,
 *  		"changePercent": 0.3897
 *  	},
 *  	{
 *  		"ric": "FITB.O",
 *  		"symbol": "FITB",
 *  		"market": "US",
 *  		"exchange": "NSQ",
 *  		"companyName": "FIFTH THR BNCP",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"sharePrice": 25.46,
 *  		"changeAmount": 0.09,
 *  		"changePercent": 0.3547
 *  	},
 *  	{
 *  		"ric": "AGNC.O",
 *  		"symbol": "AGNC",
 *  		"market": "US",
 *  		"exchange": "NSQ",
 *  		"companyName": "AGNC INVESTMENT",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"sharePrice": 17.95,
 *  		"changeAmount": "",
 *  		"changePercent": 0
 *  	},
 *  	{
 *  		"ric": "GLPI.O",
 *  		"symbol": "GLPI",
 *  		"market": "US",
 *  		"exchange": "NSQ",
 *  		"companyName": "GAMING AND LEISU",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"sharePrice": 34.68,
 *  		"changeAmount": 0.15,
 *  		"changePercent": 0.4344
 *  	},
 *  	{
 *  		"ric": "PBCT.O",
 *  		"symbol": "PBCT",
 *  		"market": "US",
 *  		"exchange": "NSQ",
 *  		"companyName": "PEOPLE UNTD FIN",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"sharePrice": 15.54,
 *  		"changeAmount": 0.16,
 *  		"changePercent": 1.0403
 *  	},
 *  	{
 *  		"ric": "NAVI.O",
 *  		"symbol": "NAVI",
 *  		"market": "US",
 *  		"exchange": "NSQ",
 *  		"companyName": "NAVIENT CORP",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"sharePrice": 10.46,
 *  		"changeAmount": 0.05,
 *  		"changePercent": 0.4803
 *  	},
 *  	{
 *  		"ric": "SLM.O",
 *  		"symbol": "SLM",
 *  		"market": "US",
 *  		"exchange": "NSQ",
 *  		"companyName": "SLM CORPORATION",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"sharePrice": 8.99,
 *  		"changeAmount": -0.03,
 *  		"changePercent": -0.3326
 *  	},
 *  	{
 *  		"ric": "ZION.O",
 *  		"symbol": "ZION",
 *  		"market": "US",
 *  		"exchange": "NSQ",
 *  		"companyName": "ZION BNCORPRT NA",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"sharePrice": 44.81,
 *  		"changeAmount": 0.49,
 *  		"changePercent": 1.1056
 *  	}]
 *  }
 * 
 * @apiUse ErrorMsgResponse
 */

/**
 * @apiDefine sameSectorResponse
 * 
 * @apiSuccess {Object[]} priceQuotes
 * @apiSuccess {String} priceQuotes.ric [Source: TRIS] <p> Reuters Instrument Code.
 * @apiSuccess {String} priceQuotes.symbol [Source: TRIS] <p> A stock symbol is a unique series of letters assigned to a security for trading purposes.
 * @apiSuccess {String} priceQuotes.market [Source: WPC] <p> Stock Market.
 * @apiSuccess {String} priceQuotes.exchange [Source: TRIS] <p> Stock Exchange.
 * @apiSuccess {String} priceQuotes.companyName [Source: TRIS] <p> Company Name.
 * @apiSuccess {String} priceQuotes.currency [Source: TRIS] <p> Trading currency.
 * @apiSuccess {Boolean} priceQuotes.delay [Source: MDS] <p> Delay Quote or Realtime Quote.
 * @apiSuccess {Number} priceQuotes.sharePrice [Source: TRIS] <p> A share price is the price of a single share of a number of saleable stocks of a company, derivative or other financial asset.
 * @apiSuccess {Number} priceQuotes.changeAmount [Source: TRIS] <p> Change in amount of the share price versus previous closing price.
 * @apiSuccess {Number} priceQuotes.changePercent [Source: TRIS] <p> Change in percentage of the share price versus previous closing price.
 * 
 */