/**
 * @api {get} /wealth/api/v1/market-data/index/quotes Index Quotes
 * @apiName Index Quotes
 * @apiGroup Quotes
 * @apiVersion 1.0.0
 * 
 * @apiUse CommonHeaderParams
 * 
 * @apiParam {Object[]} productKeys The key to uniquely locate a product.
 * @apiParam {String} productKeys.productType Product type.
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code. </br>Sample value: "M"
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKeys.market The investment market of the product.</br>
 * @apiParam {Boolean = true, false} delay <ul><li>true: Delay Quote</li><li>false: Real-time Quote</li></ul>
 *                    
 * @apiParamExample Request:
 *  {
 *  	"productKeys": [{
 *  		"productType": "INDEX",
 *  		"prodCdeAltClassCde": "M",
 *  		"market": "US",
 *  		"prodAltNum": "SPX"
 *  	}],
 *  	"delay": true
 *  }
 *  
 *  
 * @apiSuccess {Object[]} indices
 * @apiSuccess {String} indices.symbol Symbol of requested indices.
 * @apiSuccess {String} indices.name Name of the index.
 * @apiSuccess {Number} indices.lastPrice Last trade price or value.
 * @apiSuccess {Number} indices.changeAmount Difference between the latest trading price or value and the adjusted historical closing value or settlement price.
 * @apiSuccess {Number} indices.changePercent Percentage change of the latest trade price or value from the adjusted historical close.
 * @apiSuccess {Number} indices.openPrice Today's opening price or value. The source of this field depends upon the market and instrument type.
 * @apiSuccess {Number} indices.previousClosePrice Historical close or settlement price.
 * @apiSuccess {Number} indices.dayRangeHigh Today's highest transaction value.
 * @apiSuccess {Number} indices.dayRangeLow  Today's lowest transaction value.
 * @apiSuccess {Number} indices.changePercent1M  Percentage change of current close price comparing to 1 month historic close.
 * @apiSuccess {Number} indices.changePercent2M  Percentage change of current close price comparing to 2 month historic close.
 * @apiSuccess {Number} indices.changePercent3M  Percentage change of current close price comparing to 3 month historic close.
 * @apiSuccess {Number} indices.changePercent1Y  Percentage change of current close price comparing to 1 year historic close.
 * @apiSuccess {Number} indices.oneMonthLowPrice  Lowest transaction value of pass 1 month.
 * @apiSuccess {Number} indices.twoMonthLowPrice  Lowest transaction value of pass 2 month.
 * @apiSuccess {Number} indices.threeMonthLowPrice Lowest transaction value of pass 3 month.
 * @apiSuccess {Number} indices.oneMonthHighPrice  Highest transaction value of pass 1 month.
 * @apiSuccess {Number} indices.twoMonthHighPrice  Highest transaction value of pass 2 month.
 * @apiSuccess {Number} indices.threeMonthHighPrice  Highest transaction value of pass 3 month.
 * @apiSuccess {Number} indices.yearHighPrice  The highest value this year or period.
 * @apiSuccess {Number} indices.yearLowPrice  The lowest value this year or period.
 * @apiSuccess {Date} indices.asOfDateTime  The time of the lastPrice, Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX.
 * @apiSuccess {Object[]} messages The warning messages returned once indices quote is happening exceptions.
 * @apiSuccess {String} messages.reasonCode The reason code of the warning response.
 * @apiSuccess {String} messages.text The description of the warning response.
 * @apiSuccess {String} messages.traceCode The trace code of the warning response.
 * @apiSuccess {String} messages.symbol The symbol of indices which has exception in warning response.
 *
 *  
 * @apiSuccessExample Success-Response:
 * {
 *   "indices": [{
 *     "symbol": "HSI",	
 *     "name": "Hang Seng Index",	
 *     "lastPrice": 29357.69,	
 *     "changeAmount": 61.64,	
 *     "changePercent": 0.21,	
 *     "openPrice": 29215.54,	
 *     "previousClosePrice": 29296.05,	
 *     "dayRangeHigh": 29436.84,	
 *     "dayRangeLow": 29089.39,	
 *     "changePercent1M": -6.01,	
 *     "changePercent2M": -3.49,	
 *     "changePercent3M": -5.51,	
 *     "changePercent1Y": 14.35,	
 *     "oneMonthLowPrice": 29285.71,	
 *     "twoMonthLowPrice": 29285.71,	
 *     "threeMonthLowPrice": 29285.71,	
 *     "oneMonthHighPrice": 31521.13,	
 *     "twoMonthHighPrice": 31592.56,
 *     "threeMonthHighPrice": 31686.67,	
 *     "asOfDateTime": "2018-06-22T15:51:00.000+08:00",
 *     "yearHighPrice": 33484.08,	
 *     "yearLowPrice": 25199.86	
 *   }],
 *   "messages": null
 * }
 * 
 * @apiUse ErrorMsgResponse
 */
