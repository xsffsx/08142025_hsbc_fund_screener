/**
 * @api {get} /wealth/api/v1/market-data/equity/quotes/same-sector-equity SameSectorEquity
 * @apiName SameSectorEquity
 * @apiGroup Quote
 * @apiVersion 1.0.4
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {String} productType Product Type. Stock is "SEC".
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} market The investment market of the product. <ul><li>HK: Hong Kong</li><li>US: United States</li></ul>
 * @apiParam {Boolean} delay <ul><li>true: Delay Quote</li></ul>
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
 *  		"exchangeCode": "NSQ",
 *  		"companyName": "FLUSHING FINL",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"nominalPrice": 22.19,
 *  		"changeAmount": 0.18,
 *  		"changePercent": 0.8178
 *  	},
 *  	{
 *  		"ric": "AABA.O",
 *  		"symbol": "AABA",
 *  		"market": "US",
 *  		"exchangeCode": "NSQ",
 *  		"companyName": "ALTABA INC ORD",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"nominalPrice": 62.78,
 *  		"changeAmount": 0.49,
 *  		"changePercent": 0.7866
 *  	},
 *  	{
 *  		"ric": "HBAN.O",
 *  		"symbol": "HBAN",
 *  		"market": "US",
 *  		"exchangeCode": "NSQ",
 *  		"companyName": "HUNTGTN BKSHR",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"nominalPrice": 12.88,
 *  		"changeAmount": 0.05,
 *  		"changePercent": 0.3897
 *  	},
 *  	{
 *  		"ric": "FITB.O",
 *  		"symbol": "FITB",
 *  		"market": "US",
 *  		"exchangeCode": "NSQ",
 *  		"companyName": "FIFTH THR BNCP",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"nominalPrice": 25.46,
 *  		"changeAmount": 0.09,
 *  		"changePercent": 0.3547
 *  	},
 *  	{
 *  		"ric": "AGNC.O",
 *  		"symbol": "AGNC",
 *  		"market": "US",
 *  		"exchangeCode": "NSQ",
 *  		"companyName": "AGNC INVESTMENT",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"nominalPrice": 17.95,
 *  		"changeAmount": null,
 *  		"changePercent": 0
 *  	},
 *  	{
 *  		"ric": "GLPI.O",
 *  		"symbol": "GLPI",
 *  		"market": "US",
 *  		"exchangeCode": "NSQ",
 *  		"companyName": "GAMING AND LEISU",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"nominalPrice": 34.68,
 *  		"changeAmount": 0.15,
 *  		"changePercent": 0.4344
 *  	},
 *  	{
 *  		"ric": "PBCT.O",
 *  		"symbol": "PBCT",
 *  		"market": "US",
 *  		"exchangeCode": "NSQ",
 *  		"companyName": "PEOPLE UNTD FIN",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"nominalPrice": 15.54,
 *  		"changeAmount": 0.16,
 *  		"changePercent": 1.0403
 *  	},
 *  	{
 *  		"ric": "NAVI.O",
 *  		"symbol": "NAVI",
 *  		"market": "US",
 *  		"exchangeCode": "NSQ",
 *  		"companyName": "NAVIENT CORP",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"nominalPrice": 10.46,
 *  		"changeAmount": 0.05,
 *  		"changePercent": 0.4803
 *  	},
 *  	{
 *  		"ric": "SLM.O",
 *  		"symbol": "SLM",
 *  		"market": "US",
 *  		"exchangeCode": "NSQ",
 *  		"companyName": "SLM CORPORATION",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"nominalPrice": 8.99,
 *  		"changeAmount": -0.03,
 *  		"changePercent": -0.3326
 *  	},
 *  	{
 *  		"ric": "ZION.O",
 *  		"symbol": "ZION",
 *  		"market": "US",
 *  		"exchangeCode": "NSQ",
 *  		"companyName": "ZION BNCORPRT NA",
 *  		"currency": "USD",
 *  		"delay": true,
 *  		"nominalPrice": 44.81,
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
 * @apiSuccess {String} priceQuotes.ric Reuters Instrument Code. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>prodAltNum</td></tr></table>
 * @apiSuccess {String} priceQuotes.symbol A stock symbol is a unique series of letters assigned to a security for trading purposes. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>symbol</td></tr></table>
 * @apiSuccess {String} priceQuotes.market Stock Market. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>WPC</td><td>market</td></tr></table>
 * @apiSuccess {String} priceQuotes.exchangeCode Stock Exchange. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>market</td></tr></table>
 * @apiSuccess {String} priceQuotes.companyName Company Name. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>companyName</td></tr></table>
 * @apiSuccess {String} priceQuotes.currency Trading currency. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>currency</td></tr></table>
 * @apiSuccess {Boolean} priceQuotes.delay Delay Quote or Realtime Quote. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>MDS</td><td>N/A</td></tr></table>
 * @apiSuccess {Number} priceQuotes.nominalPrice Price quotations on futures for a period in which no actual trading took place. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>priceQuote</td></tr></table>
 * @apiSuccess {Number} priceQuotes.changeAmount Change in amount of the share price versus previous closing price. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>changeAmount</td></tr></table>
 * @apiSuccess {Number} priceQuotes.changePercent Change in percentage of the share price versus previous closing price. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>changePercent</td></tr></table>
 * 
 */