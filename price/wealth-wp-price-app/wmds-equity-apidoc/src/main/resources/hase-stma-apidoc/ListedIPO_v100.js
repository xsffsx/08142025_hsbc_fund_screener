/**
 * @api {get} /wealth/api/v1/market-data/equity/listedIPO ListedIPO
 * @apiName ListedIPO
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String} market The investment market of the product. 
 *											<ul>
 *												<li>HK - Hong Kong</li></ul>
 * @apiParam {Number} [recordsPerPage=10] Number of records returned per page. Maximum number of returned records is 20.
 * @apiParam {Number} [pageId=1] Number of page. 
 *                            
 * @apiParamExample Request:
 * 		{	
 * 			"market":"HK",
 * 			"recordsPerPage": 10,
 * 			"pageId": 1
 * 		}
 *  
 * @apiSuccess {Object[]} listedIPO
 * @apiSuccess {String} listedIPO.symbol  Index code. 
 * @apiSuccess {String} listedIPO.name  Index/stock name.
 * @apiSuccess {Date} listedIPO.listDate  Listing Date. Format: yyyy-MM-dd.
 * @apiSuccess {Number} listedIPO.listPrice  Listing Price of the listed IPO. 
 * @apiSuccess {Number} listedIPO.lastPrice  Last price. 
 * @apiSuccess {Number} listedIPO.accumulatePerChange  Accumulative percentage change of the stock price of the list IPO.
 * @apiSuccess {Number} listedIPO.changeAmount  Difference between last price and previous close price.
 * @apiSuccess {Number} listedIPO.changePercent  Percentage change of the last price from the previous close price.
 * @apiSuccess {String} listedIPO.currency  Trading currency.
 * @apiSuccess {String} listedIPO.overSubscriptionRate  Open Offer Subscription Rate. Example: 1414x
 * @apiSuccess {String} listedIPO.ipoSponsor  IPO Sponsor.
 * @apiSuccess {Number} listedIPO.peRatio  Price per earning (PE) ratio.
 * @apiSuccess {Number} listedIPO.firstDayPerformance  First day performance.
 * @apiSuccess {Number} listedIPO.ipoStatus  Indication of the status of IPO. 
 * <ul>
 *												<li>0 - Normal</li>
 *												<li>1 - Cancelled IPO</li></ul>
 * @apiSuccess {String} listedIPO.asOfDateTime  SEHK time (i.e. timestamp from HKEx alert line). </br>
 * 										Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX.</br>
 * 										Note: second field is always 00.
 *  
 * @apiSuccessExample Success-Response:
 *      HTTP/1.1 200 OK
 * {
 *     "listedIPO": [
 *         {
 *             "symbol": "06813",
 *             "name": "大喜屋集團",
 *             "listDate": "2020-02-14",
 *             "listPrice": 2.0,
 *             "lastPrice": null,
 *             "accumulatePerChange": null,
 *             "changeAmount": null,
 *             "changePercent": null,
 *             "currency": "HKD",
 *             "overSubscriptionRate": null,
 *             "ipoSponsor": "同人融資",
 *             "peRatio": null,
 *             "firstDayPerformance": null,
 *             "ipoStatus": 1,
 *             "asOfDateTime": "2020-04-01T11:49:00.000+0800"
 *         },
 *         {
 *             "symbol": "09969",
 *             "name": "諾誠健華－Ｂ",
 *             "listDate": "2020-03-23",
 *             "listPrice": 8.95,
 *             "lastPrice": 11.88,
 *             "accumulatePerChange": 32.737,
 *             "changeAmount": -0.1,
 *             "changePercent": -0.83,
 *             "currency": "HKD",
 *             "overSubscriptionRate": "298.8x",
 *             "ipoSponsor": "摩根士丹利亞洲、高盛（亞洲）",
 *             "peRatio": null,
 *             "firstDayPerformance": 9.609,
 *             "ipoStatus": 0,
 *             "asOfDateTime": "2020-04-01T11:49:00.000+0800"
 *         },
 *         {
 *             "symbol": "01861",
 *             "name": "保寶龍科技",
 *             "listDate": "2019-06-21",
 *             "listPrice": null,
 *             "lastPrice": 1.26,
 *             "accumulatePerChange": null,
 *             "changeAmount": 0.03,
 *             "changePercent": 2.38,
 *             "currency": "HKD",
 *             "overSubscriptionRate": null,
 *             "ipoSponsor": "中國通海企業融資",
 *             "peRatio": 8.25,
 *             "firstDayPerformance": null,
 *             "ipoStatus": 0,
 *             "asOfDateTime": "2020-04-01T11:49:00.000+0800"
 *         }
 *     ]
 * }
 *
 *
 * @apiUse ErrorMsgResponse
 * 
 */
