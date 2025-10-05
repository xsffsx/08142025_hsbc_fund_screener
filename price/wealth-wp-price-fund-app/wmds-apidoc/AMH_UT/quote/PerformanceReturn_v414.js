/**
 * @api {get} /v4.0/performanceReturn Performance Return
 * @apiName Performance Return
 * @apiGroup Quote
 * @apiVersion 4.1.4
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {Object[]} productKeys 
 * @apiParam {String} productKey.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKey.prodAltNum Denotes the alternative number of the product. It support symbol or indexID.
 * @apiParam {String} productKey.market The investment market of the product.</br>
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
 * @apiParam {String} productKey.productType Product Type. If this field is null, then the productType require to define in productKey.
 * @apiParam {String} currency Currency type.
 * @apiParam {String} indicate The search type identifier, only support symbol/index.
 * 
 * @apiParamExample Request:
 *	{
 *		"currency": "USD",
 *		"indicate": "symbol",
 *		"productKeys": [{
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "U61679",
 *			"market": "HK",
 *			"productType": "UT"
 *		},
 *		{
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "U61593",
 *			"market": "HK",
 *			"productType": "UT"
 *		}]
 *	}
 * 
 * @apiSuccess {Object[]} products
 * @apiSuccess {String} products.product Symbol or IdexId.
 * @apiSuccess {String} products.currency Currency type.
 * @apiSuccess {Number} products.returnYTD Year-to-date trailing total return of the fund product, expressed as a percentage. 
 * @apiSuccess {Number} products.return1Yr Trailing total return of the fund for 1 year, expressed as a percentage.
 * @apiSuccess {Number} products.return2Yr Trailing total return of the fund for 2 year, expressed as a percentage.
 * @apiSuccess {Number} products.return3Yr Trailing total return of the fund for 3 year, expressed as a percentage.
 * @apiSuccess {Number} products.return4Yr Trailing total return of the fund for 4 year, expressed as a percentage.
 * @apiSuccess {Number} products.return5Yr Trailing total return of the fund for 5 year, expressed as a percentage.
 * @apiSuccess {Number} products.return6Yr Trailing total return of the fund for 6 year, expressed as a percentage.
 * @apiSuccess {Number} products.return7Yr Trailing total return of the fund for 7 year, expressed as a percentage.
 * @apiSuccess {Number} products.return8Yr Trailing total return of the fund for 8 year, expressed as a percentage.
 * @apiSuccess {Number} products.return9Yr Trailing total return of the fund for 9 year, expressed as a percentage.
 * @apiSuccess {String} products.return10Yr Trailing total return of the fund for 10 year, expressed as a percentage.
 * @apiSuccess {String} products.lastUpdatedDate The data updated date.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 *		{
 *			"products": [{
 *				"product": "U61679",		
 *				"currency": "USD",
 *				"returnYTD": 1.4821,
 *				"return1Yr": 34.73706,
 *				"return2Yr": -1.17296,
 *				"return3Yr": 0.30129,
 *				"return4Yr": 1.20897,
 *				"return5Yr": 22.14925,
 *				"return6Yr": 12.00941,
 *				"return7Yr": -12.28868,
 *				"return8Yr": 7.17222,
 *				"return9Yr": 30.99882,	
 *				"return10Yr": -53.74564,
 *				"lastUpdatedDate": "2018-09-10"
 *				},
 *				{
 *				"product": "U61593",		
 *				"currency": "USD",
 *				"returnYTD": -3.8293,
 *				"return1Yr": 34.73706,
 *				"return2Yr": -1.17296,
 *				"return3Yr": 0.30129,
 *				"return4Yr": 1.20897,
 *				"return5Yr": 22.14925,
 *				"return6Yr": 12.00941,
 *				"return7Yr": -12.28868,
 *				"return8Yr": 7.17222,
 *				"return9Yr": 30.99882,	
 *				"return10Yr": -53.74564,
 *				"lastUpdatedDate": "2018-09-10"
 *				}]
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */