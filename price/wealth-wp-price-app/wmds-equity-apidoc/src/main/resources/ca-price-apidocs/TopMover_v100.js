/**
 * @api {get} /wealth/api/v1/market-data/equity/quotes/top-mover Top Mover
 * @apiName Top Mover
 * @apiGroup Quotes
 * @apiVersion 1.0.0
 * 
 * @apiUse CommonHeaderParams
 * 
 * @apiParam {String} market The investment market of the product.
 *								<ul><li>CA - Canada</li>
 *									<li>US - United States</li>
 *									<li>HK - Hong Kong</li>
 *									<li>UK - United Kingdom</li>
 *									<li>FR - France</li>
 *									<li>DE - Germany</li></ul>
 * @apiParam {String} exchangeCode Denotes the code for the market place where the products such as stocks were traded.
 * @apiParam {String} productType Product type.
 * @apiParam {String} moverType Mover type.</br>
 * @apiParam {Boolean = true, false} delay <ul><li>true: Delay Quote</li><li>false: Real-time Quote</li></ul>
 * @apiParam {String} [topNum] Numbers of records returned. Maximum is 10.
 * 
 * 
 * @apiParamExample Request:
 *  {
 *  	"market": "HK",
 *  	"exchangeCode": "HKG",
 *      "productType": "SEC",
 *      "moverType": "TURN",
 *  	"delay": false,
 *  	"topNum": 3
 *  }
 * 
 * 
 * @apiSuccess {Object[]} topMovers
 * @apiSuccess {String} topMovers.chainKey The chain key for this table.
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
 * @apiSuccess {Number} topMovers.products.volume The volume of the specified symbol.
 * @apiSuccess {Number} topMovers.products.openPrice The day open of the specified symbol.
 * @apiSuccess {Number} topMovers.products.previousClosePrice The previous closing price of the specified symbol.
 * @apiSuccess {Number} topMovers.products.turnover The turnover of the specified symbol.
 * @apiSuccess {Number} topMovers.products.score Rating Score of the stock.
 * @apiSuccess {String} topMovers.products.productType The type of the product.
 * @apiSuccess {String} topMovers.products.currency Trading currency of the specified symbol.
 * @apiSuccess {Date} topMovers.products.asOfDateTime The latest updated time from Vendor. Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX.
 *
 *
 * @apiSuccessExample Success-Response:
 *  HTTP/1.1 200 OK
 *  {
 *  	"topMovers": [{
 *  		"chainKey": "1",
 *  		"tableKey": "TURN",
 *  		"product": [{
 *  			"productStatus": null,
 *  			"ric": "2800.HK",
 *  			"underlyStockRic": null,
 *  			"symbol": "2800",
 *  			"market": "HK",
 *  			"name": "Tracker Fund Of Hong Kong",
 *  			"price": "27.600",
 *  			"delay": "Y",
 *  			"change": "-0.700",
 *  			"quoteExchange": null,
 *  			"changePercent": "-2.47",
 *  			"volume": "74892357",
 *  			"turnover": null,
 *  			"currency": "HKD",
 *  			"recordType": "113",
 *  			"productType": "SEC",
 *  			"optionType": null,
 *  			"optionText": null,
 *  			"strikePrice": null,
 *  			"expirationDate": null,
 *  			"unsignedAgreementId": null,
 *  			"countryTradableCode": null,
 *  			"underlyingProductType": null,
 *  			"underlyingSymbol": null,
 *  			"underlyingMarket": null,
 *  			"rootSymbol": null,
 *  			"asOfDateTime": "2019-03-18T09:05:30.000+00:00"
 *  		},
 *  		{
 *  			"productStatus": null,
 *  			"ric": "2382.HK",
 *  			"underlyStockRic": null,
 *  			"symbol": "2382",
 *  			"market": "HK",
 *  			"name": "Sunny Optical Technology Group Co Ltd",
 *  			"price": "84.300",
 *  			"delay": "Y",
 *  			"change": "-7.100",
 *  			"quoteExchange": null,
 *  			"changePercent": "-7.77",
 *  			"volume": "18576831",
 *  			"turnover": null,
 *  			"currency": "HKD",
 *  			"recordType": "113",
 *  			"productType": "SEC",
 *  			"optionType": null,
 *  			"optionText": null,
 *  			"strikePrice": null,
 *  			"expirationDate": null,
 *  			"unsignedAgreementId": null,
 *  			"countryTradableCode": null,
 *  			"underlyingProductType": null,
 *  			"underlyingSymbol": null,
 *  			"underlyingMarket": null,
 *  			"rootSymbol": null,
 *  			"asOfDateTime": "2019-03-18T09:05:30.000+00:00"
 *  		},
 *  		{
 *  			"productStatus": null,
 *  			"ric": "2318.HK",
 *  			"underlyStockRic": null,
 *  			"symbol": "2318",
 *  			"market": "HK",
 *  			"name": "Ping An Insurance Group Co of China Ltd",
 *  			"price": "90.900",
 *  			"delay": "Y",
 *  			"change": "-2.300",
 *  			"quoteExchange": null,
 *  			"changePercent": "-2.47",
 *  			"volume": "47106453",
 *  			"turnover": null,
 *  			"currency": "HKD",
 *  			"recordType": "113",
 *  			"productType": "SEC",
 *  			"optionType": null,
 *  			"optionText": null,
 *  			"strikePrice": null,
 *  			"expirationDate": null,
 *  			"unsignedAgreementId": null,
 *  			"countryTradableCode": null,
 *  			"underlyingProductType": null,
 *  			"underlyingSymbol": null,
 *  			"underlyingMarket": null,
 *  			"rootSymbol": null,
 *  			"asOfDateTime": "2019-03-18T09:05:30.000+00:00"
 *  		}]
 *  	}]
 *  }
 *
 *
 * @apiUse ErrorMsgResponse
 */