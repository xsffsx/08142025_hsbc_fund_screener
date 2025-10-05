/**
 * @api {get} /wealth/api/v1/market-data/equity/quotes/top-mover Top Mover
 * @apiName Top Mover
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String} market The investment market of the product. 
 *											<ul><li>CN - China</li>
 *												<li>HK - Hong Kong</li>
 *											<li>US - United States</li>	</ul>
 * @apiParam {String} exchangeCode Denotes the code for the market place where the products such as stocks were traded.
 * 								<p>For CN market, possible value:</p>
 * 								<ul>
 * 									<li>SHAS - Shanghai Stock Exchange</li>
 * 									<li>SZSE - Shenzhen Stock Exchange</li>
 * 								</ul>
 * 								<p>For HK market, possible value:</p> 
 * 								<ul>
 * 									<li>HKEX - Hong Kong Stock Exchange</li>
 * 								</ul>
 * 								<p>For US market, possible value:</p>
 * 								<ul>
 * 									<li>NYSE - New York Exchange</li>
 * 									<li>NASDAQ </li>
 * 									<li>AMEX </li>
 * 									<li>NYSEARCA </li>
 * 								</ul>
 * @apiParam {String} productType Product Type. Stock is "SEC".</br>
 * @apiParam {String} moverType Mover Type.</br>
 * <p>For CN market, possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>VOL</td><td>Top 10 active stocks by Volume</td></tr>
 * 		<tr><td>GAINPCT</td><td>Top 10 active stocks by Gainers (%)</td></tr>
 * 		<tr><td>LOSEPCT</td><td>Top 10 active stocks by Losers (%)</td></tr>
 * </table>											
 * <p>For HK market, possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>TURN</td><td>Top 10 active stocks by Turnover</td></tr>
 * 		<tr><td>VOL</td><td>Top 10 active stocks by Volume</td></tr>
 * 		<tr><td>GAINPCT</td><td>Top 10 active stocks by Gainers (%)</td></tr>
 * 		<tr><td>LOSEPCT</td><td>Top 10 active stocks by Losers (%)</td></tr>
 * </table>
 * <p>For US market, possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>VOL</td><td>Top 10 active stocks by Volume</td></tr>
 * 		<tr><td>GAINPCT</td><td>Top 10 active stocks by Gainers (%)</td></tr>
 * 		<tr><td>LOSEPCT</td><td>Top 10 active stocks by Losers (%)</td></tr>
 * 		<tr><td>RATEUP</td><td>Rating Upgrades (RATEUP is only for NYSE, NASDAQ and AMEX)</td></tr>
 * </table>
 * @apiParam {String} boardType Board Type.
 * <p>For HK market only, possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>MAIN</td><td>Main board</td></tr>
 * 		<tr><td>WARRANT</td><td>Warrants</td></tr>
 * 		<tr><td>GEM</td><td>GEM</td></tr>
 * </table>
 * @apiParam {Boolean=true, false} delay For HK market only as Top Movers always return Real Time Quote for CN market.
 * 										<ul><li>true: Delay Quote</li><li>false: Real-time Quote</li></ul></br>
 * @apiParam {String} [topNum] Numbers of records returned, Maximum is 10.
 * 
 * 
 * @apiParamExample Request:
 *  {
 *  	"market": "HK",
 *  	"exchangeCode": "HKEX",
 *      "productType": "SEC",
 *      "moverType": "VOL",
 *  	"boardType": "MAIN",
 *  	"delay": false,
 *  	"topNum": 3
 *  }
 * 
 * 
 * @apiSuccess {Object[]} topMovers
 * @apiSuccess {String} topMovers.chainKey The chain key for this table.
 * @apiSuccess {String} topMovers.boardType The board type for this table.
 * @apiSuccess {String} topMovers.tableKey The table key for this table.
 * @apiSuccess {Object[]} topMovers.products 
 * @apiSuccess {String} topMovers.products.ric The Ric code. 
 * @apiSuccess {String} topMovers.products.symbol Wealth market code.
 * @apiSuccess {String} topMovers.products.market The investment market of the product.
 * @apiSuccess {String} topMovers.products.name The full name of product.
 * @apiSuccess {Number} topMovers.products.price The sale price as last recorded.
 * @apiSuccess {Boolean} topMovers.products.delay Denote this quote is a delayed quote or a real-time quote.
 * @apiSuccess {Number} topMovers.products.changeAmount The change amount of the specified symbol.
 * @apiSuccess {Number} topMovers.products.changePercent The % change of the specified symbol.
 * @apiSuccess {Number} topMovers.products.openPrice The day open of the specified symbol.
 * @apiSuccess {Number} topMovers.products.previousClosePrice The previous closing price of the specified symbol.
 * @apiSuccess {Number} topMovers.products.turnover The turnover of the specified symbol.
 * @apiSuccess {Number} topMovers.products.volume The volume of the specified symbol.
 * @apiSuccess {Number} topMovers.products.openPrice The day open of the specified symbol.
 * @apiSuccess {Number} topMovers.products.previousClosePrice The previous closing price of the specified symbol.
 * @apiSuccess {Number} topMovers.products.turnover The turnover of the specified symbol.
 * @apiSuccess {Number} topMovers.products.score Rating Score of the stock.
 * @apiSuccess {String} topMovers.products.productType The type of the product.
 * @apiSuccess {String} topMovers.products.currency Trading currency of the specified symbol.
 * @apiSuccess {Boolean} topMovers.products.isQuotable For CN market only, true = Indicate the stock is able to quote; false = Indicate the stock is unable to quote.</br>
 * 													  Noted: It depends on ALL products WPC-GSOPS sent to MDS are able to be quoted.
 * @apiSuccess {Date} topMovers.products.asOfDateTime The latest updated time from Vendor. Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX.
 *
 *
 * @apiSuccessExample Success-Response:
 *  HTTP/1.1 200 OK
 *{
 *  "topMovers": [
 *      {
 *          "chainKey": "2",
 *          "boardType": null,
 *          "tableKey": "VOLUME",
 *          "products": [
 *              {
 *                  "ric": "THOS_t.SI",
 *                  "symbol": "CSIW",
 *                  "market": "HK",
 *                  "name": "THOMSON -W190424",
 *                  "price": 0.002,
 *                  "delay": false,
 *                  "changeAmount": "",
 *                  "changePercent": "",
 *                  "volume": 118821500,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "asOfDateTime": "2019-03-18T09:05:30.000+00:00"
 *              },
 *              {
 *                  "ric": "THOS.SI",
 *                  "symbol": "A50",
 *                  "market": "HK",
 *                  "name": "THOMSON MEDICAL",
 *                  "price": 0.081,
 *                  "delay": false,
 *                  "changeAmount": 0.006,
 *                  "changePercent": 8,
 *                  "volume": 104999300,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "asOfDateTime": "2019-03-18T09:03:30.000+00:00"
 *              },
 *              {
 *                  "ric": "HSML_tw.SI",
 *                  "symbol": "ZH2W",
 *                  "market": "HK",
 *                  "name": "HSImblECW190328",
 *                  "price": 0.094,
 *                  "delay": false,
 *                  "changeAmount": -0.002,
 *                  "changePercent": -2.08,
 *                  "volume": 94480600,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "asOfDateTime": "2019-03-18T09:03:30.000+00:00"
 *              }
 *          ]
 *      }
 *  ]
 *}
 *
 *
 * @apiUse ErrorMsgResponse
 */