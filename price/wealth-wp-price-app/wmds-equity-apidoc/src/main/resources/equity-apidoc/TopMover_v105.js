/**
 * @api {get} /wealth/api/v1/market-data/equity/quotes/top-mover TopMover
 * @apiName TopMover
 * @apiGroup Quote
 * @apiVersion 1.0.5
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {String} productType Product Type. Stock is "SEC".
 * @apiParam {String} market The investment market of the product.<ul><li>HK: Hong Kong</li><li>US: United States</li></ul>
 * @apiParam {String} delay Base on this to select table to retrieve data from realtime or delay table.<ul><li>true: delay</li></ul>
 * @apiParam {String} topNum Numbers of records returned from TRIS for each segment.
 * @apiParam {String} exchangeCode Stock Exchange.<ul><li>HKEX: Hong Kong Stock Exchange</li><li>NAQ: NASDAQ Consolidated</li><li>ASQ: American Stock Exchange</li><li>NYSE: New York Exchange</li></ul>
 * @apiParam {String}  [moverType] Mover Type</br></br>For US market, possible values as below: <table><tr><td>Value</td><td>Description</td></tr><tr><td>TURN</td><td>Top 10 active stocks by Turnover</td></tr><tr><td>VOL</td><td>Top 10 active stocks by Volume</td></tr><tr><td>GAINPCT</td><td>Losers (%)</td></tr><tr><td>RATEUP</td><td>Rating Upgrades (RATEUP is only for NYSE, NASDAQ and AMEX)</td></tr></table></br><p>For HK market, possible values as below:</p>  <table><tr><td>Value</td><td>Description</td></tr><tr><td>TURN</td><td>Top 10 active stocks by Turnover</td></tr><tr><td>GAINPCT</td><td>Top 10 active stocks by Gainers (%)</td></tr><tr><td>LOSEPCT</td><td>Top 10 active stocks by Losers (%)</td></tr></table>
 * </br><p>For CN market, possible values as below:</p>  <table><tr><td>Value</td><td>Description</td></tr><tr><td>VOL</td><td>Top 10 active stocks by Volume</td></tr><tr><td>GAINPCT</td><td>Top 10 active stocks by Gainers (%)</td></tr><tr><td>LOSEPCT</td><td>Top 10 active stocks by Losers (%)</td></tr></table>
 *
 * 
 * 
 * 
 * @apiParamExample Request:
 *  {
 *  	"market": "HK",
 *  	"exchangeCode": "HKEX",
 *  	"delay": true,
 *  	"productType": "SEC",
 *  	"topNum": 10
 *  }
 * 
 * @apiUse topMover
 * 
 * @apiSuccessExample Success-Response:
 *  HTTP/1.1 200 OK
 *{
 *  "topMovers": [
 *      {
 *          "chainKey": "4",
 *          "tableKey": "LOSERS",
 *          "products": [
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "SECD_t.SI",
 *                  "symbol": "CDWW",
 *                  "market": "HK",
 *                  "name": "SECOND W200123",
 *                  "price": 0.001,
 *                  "delay": false,
 *                  "changeAmount": -0.001,
 *                  "changePercent": -50,
 *                  "volume": 1000000,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:06:00.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "MDRT.SI",
 *                  "symbol": "A27",
 *                  "market": "HK",
 *                  "name": "MDR LIMITED",
 *                  "price": 0.001,
 *                  "delay": false,
 *                  "changeAmount": -0.001,
 *                  "changePercent": -50,
 *                  "volume": 200,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:06:00.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "OCEA.SI",
 *                  "symbol": "1B6",
 *                  "market": "HK",
 *                  "name": "OCEAN SKY INTERN",
 *                  "price": 0.035,
 *                  "delay": false,
 *                  "changeAmount": -0.015,
 *                  "changePercent": -30,
 *                  "volume": 112400,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:06:00.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "P5CA.SI",
 *                  "symbol": "5AI",
 *                  "market": "HK",
 *                  "name": "P5 CAPITAL HLD",
 *                  "price": 0.005,
 *                  "delay": false,
 *                  "changeAmount": -0.002,
 *                  "changePercent": -28.57,
 *                  "volume": 100,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:06:00.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "ACCR.SI",
 *                  "symbol": "5RJ",
 *                  "market": "HK",
 *                  "name": "ACCRELIST LTD.",
 *                  "price": 0.003,
 *                  "delay": false,
 *                  "changeAmount": -0.001,
 *                  "changePercent": -25,
 *                  "volume": 105000,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:05:59.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "OCML_t1.SI",
 *                  "symbol": "LYVW",
 *                  "market": "HK",
 *                  "name": "OCBmblECW200102",
 *                  "price": 0.28,
 *                  "delay": false,
 *                  "changeAmount": -0.07,
 *                  "changePercent": -20,
 *                  "volume": 50000,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:05:40.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "UOML_t7.SI",
 *                  "symbol": "XNHW",
 *                  "market": "HK",
 *                  "name": "UOBmblECW190415",
 *                  "price": 0.033,
 *                  "delay": false,
 *                  "changeAmount": -0.007,
 *                  "changePercent": -17.5,
 *                  "volume": 2722900,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:05:40.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "DBML_tb.SI",
 *                  "symbol": "HIFW",
 *                  "market": "HK",
 *                  "name": "DBSmblECW190304",
 *                  "price": 0.025,
 *                  "delay": false,
 *                  "changeAmount": -0.005,
 *                  "changePercent": -16.67,
 *                  "volume": 3324000,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:05:40.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "HLGE.SI",
 *                  "symbol": "AVX",
 *                  "market": "HK",
 *                  "name": "HL GLOBAL ENTERP",
 *                  "price": 0.41,
 *                  "delay": false,
 *                  "changeAmount": -0.08,
 *                  "changePercent": -16.33,
 *                  "volume": 200,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:05:40.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "ASIP.SI",
 *                  "symbol": "5WV",
 *                  "market": "HK",
 *                  "name": "ASIAPHOS LIMITED",
 *                  "price": 0.011,
 *                  "delay": false,
 *                  "changeAmount": -0.002,
 *                  "changePercent": -15.38,
 *                  "volume": 100600,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:05:30.000Z"
 *              }
 *          ]
 *      },
 *      {
 *          "chainKey": "2",
 *          "tableKey": "VOLUME",
 *          "products": [
 *              {
 *                  "unsignedAgreementId": "",
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
 *                  "exchangeUpdatedTime": "2019-03-18T09:05:30.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
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
 *                  "exchangeUpdatedTime": "2019-03-18T09:03:30.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
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
 *                  "exchangeUpdatedTime": "2019-03-18T09:03:30.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "HSML_t28.SI",
 *                  "symbol": "EOXW",
 *                  "market": "HK",
 *                  "name": "HSImblECW190328",
 *                  "price": 0.138,
 *                  "delay": false,
 *                  "changeAmount": -0.004,
 *                  "changePercent": -2.82,
 *                  "volume": 88204100,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:03:30.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "HSML_t26.SI",
 *                  "symbol": "9N6W",
 *                  "market": "HK",
 *                  "name": "HSImblEPW190328",
 *                  "price": 0.065,
 *                  "delay": false,
 *                  "changeAmount": "",
 *                  "changePercent": "",
 *                  "volume": 64945200,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:06:30.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "REXI.SI",
 *                  "symbol": "5WH",
 *                  "market": "HK",
 *                  "name": "REX INTERNATIONA",
 *                  "price": 0.095,
 *                  "delay": false,
 *                  "changeAmount": 0.002,
 *                  "changePercent": 2.15,
 *                  "volume": 31315800,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:09:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "EZHL.SI",
 *                  "symbol": "5ME",
 *                  "market": "HK",
 *                  "name": "EZION HOLDINGS",
 *                  "price": 0.052,
 *                  "delay": false,
 *                  "changeAmount": "",
 *                  "changePercent": "",
 *                  "volume": 29459100,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:09:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "HSVT_t4.SI",
 *                  "symbol": "RMJW",
 *                  "market": "HK",
 *                  "name": "HSIvtlEPW190328",
 *                  "price": 0.103,
 *                  "delay": false,
 *                  "changeAmount": -0.005,
 *                  "changePercent": -4.63,
 *                  "volume": 27765800,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:09:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "GENS.SI",
 *                  "symbol": "G13",
 *                  "market": "HK",
 *                  "name": "GENTING SINGAPOR",
 *                  "price": 1.09,
 *                  "delay": false,
 *                  "changeAmount": "",
 *                  "changePercent": "",
 *                  "volume": 27534600,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:09:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "YAZG.SI",
 *                  "symbol": "BS6",
 *                  "market": "HK",
 *                  "name": "YANGZIJIANG SHIP",
 *                  "price": 1.4,
 *                  "delay": false,
 *                  "changeAmount": 0.03,
 *                  "changePercent": 2.19,
 *                  "volume": 22354500,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:09:36.000Z"
 *              }
 *          ]
 *      },
 *      {
 *          "chainKey": "3",
 *          "tableKey": "GAINERS",
 *          "products": [
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "HLEC.SI",
 *                  "symbol": "H20",
 *                  "market": "HK",
 *                  "name": "HOE LEONG CORP",
 *                  "price": 0.003,
 *                  "delay": false,
 *                  "changeAmount": 0.001,
 *                  "changePercent": 50,
 *                  "volume": 1300000,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:15:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "ISRC.SI",
 *                  "symbol": "5EC",
 *                  "market": "HK",
 *                  "name": "ISR CAPITAL LTD",
 *                  "price": 0.003,
 *                  "delay": false,
 *                  "changeAmount": 0.001,
 *                  "changePercent": 50,
 *                  "volume": 201500,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:15:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "SOGF.SI",
 *                  "symbol": "T4B",
 *                  "market": "HK",
 *                  "name": "SINO GRANDNESS",
 *                  "price": 0.064,
 *                  "delay": false,
 *                  "changeAmount": 0.017,
 *                  "changePercent": 36.17,
 *                  "volume": 8593600,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:15:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "UOML_t6.SI",
 *                  "symbol": "KHBW",
 *                  "market": "HK",
 *                  "name": "UOBmblEPW190610",
 *                  "price": 0.092,
 *                  "delay": false,
 *                  "changeAmount": 0.023,
 *                  "changePercent": 33.33,
 *                  "volume": 358200,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:15:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "STHO.SI",
 *                  "symbol": "5UA",
 *                  "market": "HK",
 *                  "name": "STARLAND HOLDING",
 *                  "price": 0.09,
 *                  "delay": false,
 *                  "changeAmount": 0.017,
 *                  "changePercent": 23.29,
 *                  "volume": 75200,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:15:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "MMPR.SI",
 *                  "symbol": "F3V",
 *                  "market": "HK",
 *                  "name": "MMP RESOURCES",
 *                  "price": 0.006,
 *                  "delay": false,
 *                  "changeAmount": 0.001,
 *                  "changePercent": 20,
 *                  "volume": 5300000,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:15:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "HSEN.SI",
 *                  "symbol": "510",
 *                  "market": "HK",
 *                  "name": "HIAP SENG ENGINE",
 *                  "price": 0.084,
 *                  "delay": false,
 *                  "changeAmount": 0.014,
 *                  "changePercent": 20,
 *                  "volume": 400,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:15:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "VEML_t1.SI",
 *                  "symbol": "GFRW",
 *                  "market": "HK",
 *                  "name": "VENMQGECW190415",
 *                  "price": 0.152,
 *                  "delay": false,
 *                  "changeAmount": 0.018,
 *                  "changePercent": 13.43,
 *                  "volume": 81900,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:15:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "DLCVENSG_ta.SI",
 *                  "symbol": "DLAW",
 *                  "market": "HK",
 *                  "name": "DLC SG5XLONG VEN",
 *                  "price": 1.13,
 *                  "delay": false,
 *                  "changeAmount": 0.125,
 *                  "changePercent": 12.44,
 *                  "volume": 134500,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:15:36.000Z"
 *              },
 *              {
 *                  "unsignedAgreementId": "",
 *                  "ric": "UOML_t11.SI",
 *                  "symbol": "PFDW",
 *                  "market": "HK",
 *                  "name": "UOBmblEPW190730",
 *                  "price": 0.19,
 *                  "delay": false,
 *                  "changeAmount": 0.021,
 *                  "changePercent": 12.43,
 *                  "volume": 127200,
 *                  "productType": "SEC",
 *                  "currency": "SGD",
 *                  "exchangeUpdatedTime": "2019-03-18T09:15:36.000Z"
 *              }
 *          ]
 *      }
 *  ]
 *}
 *
 *
 *
 * @apiUse ErrorMsgResponse
 */

/**
 * @apiDefine topMover
 * 
 * @apiSuccess {Object[]} topMovers
 * @apiSuccess {String} topMovers.chainKey The Chain code for TR. 
 * @apiSuccess {String} topMovers.tableKey The Chain type for TR.
 * @apiSuccess {Object[]} topMovers.products 
 * @apiSuccess {String} topMovers.products.unsignedAgreementId User agreement ID.
 * @apiSuccess {String} topMovers.products.ric Tris code. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>_item</td></tr></table>
 * @apiSuccess {String} topMovers.products.symbol Wealth market code. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>MNEMONIC</td></tr></table>
 * @apiSuccess {String} topMovers.products.market The investment market of the product
 * @apiSuccess {String} topMovers.products.name The stock name , get the name from TR. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>DSPLY_NAME</td></tr></table>
 * @apiSuccess {Number} topMovers.products.price The stock price. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>TRDPRC_1</td></tr></table>
 * @apiSuccess {Boolean} topMovers.products.delay Denote this quote is a delayed quote or a realtime quote.
 * @apiSuccess {Number} topMovers.products.changeAmount  The stock gains. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>NETCHNG_1</td></tr></table>
 * @apiSuccess {Number} topMovers.products.changePercent The percentage of stock gains. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>PCTCHNG</td></tr></table>
 * @apiSuccess {Number} topMovers.products.volume The stock trading volume. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>TW EQTY</td><td>TRIS</td><td>ACVOL_1</td></tr></table>
 * @apiSuccess {String} topMovers.products.productType the type of the product.
 * @apiSuccess {String} topMovers.products.currency Trading currency of the specified symbol.
 * @apiSuccess {String} topMovers.products.exchangeUpdatedTime The latest updated time from Vendor.
 * 
 */