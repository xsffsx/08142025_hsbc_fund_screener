/**
 * @api {get} /wealth/api/v1/market-data/equity/quotes/same-sector-equity SameSectorEquity
 * @apiName SameSectorEquity
 * @apiGroup Quote
 * @apiVersion 1.0.7
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {String} productType Product Type. Stock is "SEC".
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} market The investment market of the product. <ul><li>HK: Hong Kong</li><li>US: United States</li></ul>
 * @apiParam {Boolean} delay <ul><li>true: Delay Quote</li></ul>
 * @apiParam {Number} [number] Default value is 10.
 * @apiParam {String} requestType Default value is 0, used to check if skip agreement check by request type and app code value.
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
 *	{
 *   "priceQuotes": [
 *       {
 *           "prodAltNumSegs": null,
 *           "ric": "6862.HK",
 *           "symbol": "6862",
 *           "market": "HK",
 *           "exchangeCode": "HKG",
 *           "companyName": "HAIDILAO",
 *           "currency": "HKD",
 *           "delay": true,
 *           "nominalPrice": 19.76,
 *           "price": 19.76,
 *           "changeAmount": -0.06,
 *           "changePercent": -0.3
 *       },
 *       {
 *           "prodAltNumSegs": null,
 *           "ric": "0493.HS",
 *           "symbol": "493",
 *           "market": "HK",
 *           "exchangeCode": "HKG",
 *           "companyName": "GOME RETAIL",
 *           "currency": "HKD",
 *           "delay": true,
 *           "nominalPrice": 0.77,
 *           "price": 0.77,
 *           "changeAmount": 0,
 *           "changePercent": 0
 *       },
 *       {
 *           "prodAltNumSegs": null,
 *           "ric": "0493.HK",
 *           "symbol": "493",
 *           "market": "HK",
 *           "exchangeCode": "HKG",
 *           "companyName": "GOME RETAIL",
 *           "currency": "HKD",
 *           "delay": true,
 *           "nominalPrice": 0.77,
 *           "price": 0.77,
 *           "changeAmount": 0,
 *           "changePercent": 0
 *       },
 *       {
 *           "prodAltNumSegs": null,
 *           "ric": "0419.HS",
 *           "symbol": "419",
 *           "market": "HK",
 *           "exchangeCode": "HKG",
 *           "companyName": "HUAYI TENCENT",
 *           "currency": "HKD",
 *           "delay": true,
 *           "nominalPrice": 0.235,
 *           "price": 0.235,
 *           "changeAmount": 0.001,
 *           "changePercent": 0.43
 *       },
 *       {
 *           "prodAltNumSegs": null,
 *           "ric": "0419.HK",
 *           "symbol": "419",
 *           "market": "HK",
 *           "exchangeCode": "HKG",
 *           "companyName": "HUAYI TENCENT",
 *           "currency": "HKD",
 *           "delay": true,
 *           "nominalPrice": 0.235,
 *           "price": 0.235,
 *           "changeAmount": 0.001,
 *           "changePercent": 0.43
 *       },
 *       {
 *           "prodAltNumSegs": null,
 *           "ric": "0787.HS",
 *           "symbol": "787",
 *           "market": "HK",
 *           "exchangeCode": "HKG",
 *           "companyName": "GLOBAL BRANDS",
 *           "currency": "HKD",
 *           "delay": true,
 *           "nominalPrice": 0.37,
 *           "price": 0.37,
 *           "changeAmount": 0,
 *           "changePercent": 0
 *       },
 *       {
 *           "prodAltNumSegs": null,
 *           "ric": "0787.HK",
 *           "symbol": "787",
 *           "market": "HK",
 *           "exchangeCode": "HKG",
 *           "companyName": "GLOBAL BRANDS",
 *           "currency": "HKD",
 *           "delay": true,
 *           "nominalPrice": 0.37,
 *           "price": 0.37,
 *           "changeAmount": 0,
 *           "changePercent": 0
 *       },
 *       {
 *           "prodAltNumSegs": [
 *               {
 *                   "prodCdeAltClassCde": "I",
 *                   "prodAltNum": "CNE100000Q35"
 *               },
 *               {
 *                   "prodCdeAltClassCde": "M",
 *                   "prodAltNum": "2238"
 *               },
 *               {
 *                   "prodCdeAltClassCde": "P",
 *                   "prodAltNum": "2238.HK"
 *               },
 *               {
 *                   "prodCdeAltClassCde": "T",
 *                   "prodAltNum": "2238.HK"
 *               },
 *               {
 *                   "prodCdeAltClassCde": "W",
 *                   "prodAltNum": "20001944"
 *               }
 *           ],
 *           "ric": "2238.HS",
 *           "symbol": "2238",
 *           "market": "HK",
 *           "exchangeCode": "HKG",
 *           "companyName": "GAC GROUP",
 *           "currency": "HKD",
 *           "delay": true,
 *           "nominalPrice": 9.25,
 *           "price": 9.25,
 *           "changeAmount": -0.43,
 *           "changePercent": -4.44
 *       },
 *       {
 *           "prodAltNumSegs": [
 *               {
 *                   "prodCdeAltClassCde": "I",
 *                   "prodAltNum": "CNE100000Q35"
 *               },
 *               {
 *                   "prodCdeAltClassCde": "M",
 *                   "prodAltNum": "2238"
 *               },
 *               {
 *                   "prodCdeAltClassCde": "P",
 *                   "prodAltNum": "2238.HK"
 *               },
 *               {
 *                   "prodCdeAltClassCde": "T",
 *                   "prodAltNum": "2238.HK"
 *               },
 *               {
 *                   "prodCdeAltClassCde": "W",
 *                   "prodAltNum": "20001944"
 *               }
 *           ],
 *           "ric": "2238.HK",
 *           "symbol": "2238",
 *           "market": "HK",
 *           "exchangeCode": "HKG",
 *           "companyName": "GAC GROUP",
 *           "currency": "HKD",
 *           "delay": true,
 *           "nominalPrice": 9.25,
 *           "price": 9.25,
 *           "changeAmount": -0.43,
 *           "changePercent": -4.44
 *       },
 *       {
 *           "prodAltNumSegs": null,
 *           "ric": "3344.HS",
 *           "symbol": "3344",
 *           "market": "HK",
 *           "exchangeCode": "HKG",
 *           "companyName": "GTI HLDGS",
 *           "currency": "HKD",
 *           "delay": true,
 *           "nominalPrice": 0.203,
 *           "price": 0.203,
 *           "changeAmount": 0,
 *           "changePercent": 0
 *       }
 *   ]
 * }
 * 
 * @apiUse ErrorMsgResponse
 */

/**
 * @apiDefine sameSectorResponse
 * 
 * @apiSuccess {Object[]} priceQuotes
 * @apiSuccess {Object[]} priceQuotes.prodAltNumSegs
 * @apiSuccess {String} priceQuotes.prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>WPC</td><td>prodCdeAltClassCde</td></tr></table>
 * @apiSuccess {String} priceQuotes.prodAltNumSegs.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>WPC</td><td>prodAltNum</td></tr></table>
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