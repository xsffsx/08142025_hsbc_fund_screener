/**
 * @api {get} /v4.0/advanceChart Advance Chart
 * @apiName Advance Chart
 * @apiGroup Quote
 * @apiVersion 4.1.5
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {String} market The investment market of the product.</br>
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
 * @apiParam {Object[]} productKeys 
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKeys.market The investment market of the product.
 * @apiParam {String} productKeys.productType Product Type. If this field is null, then the productType require to define in productKey.
 * @apiParam {String} timeZone Provided a time zone,it is a string representing the user timezone. Used to format time,sample value :</br>
 * 										 Asia/Hong_Kong</br>
 * @apiParam {String} [startDate]  Start date to retrieve a snapshot index chart data.</br>
 * 										 Noted:  "startDate" will overwrite the "period", "startDate" must appear together with "endDate".</br>                                      
 * @apiParam {String} [endDate]  End date to retrieve a snapshot index chart data.</br>
 * 										 Noted:  "endDate" will overwrite the "period", "endDate" must appear together with "startDate".</br> 
 * @apiParam {String} [period] Time Period,possible values as below:</br>
 * 										 1M - 1 Month</br>
 * 										 3M - 3 Months</br>
 * 										 6M - 6 Months</br>
 * 										 1Y - 1 Year</br>
 * 										 3Y - 3 Years</br>
 *                                       5Y - 5 Years</br>
 *                                       10Y - 10 Years</br>
 * 										 YTD - Year to Date</br>
 * 										 MAX - From Fund establish
 * @apiParam {String} currency Currency of this products.
 * @apiParam {Object[]} dataType dataType values as below:</br>
 * 										 cumulativeReturn - performance growth</br>
 * 										 navPrice - NAV Price
 * @apiParam {String} Frequency Frequency values as below:</br>
 * 										 daily - Daily data</br>
 * 										 monthly - Monthly data
 * 
 * 
 * @apiParamExample Request:
 * 	{
 * 		"productKeys": [{
 * 			"market": "HK",
 * 			"productType": "UT",
 * 			"prodCdeAltClassCde": "M",
 * 			"prodAltNum": "U61689"
 * 			}],
 * 		"timeZone": "Asia/Hong_Kong",
 * 		"startDate": "2004-01-22",
 * 		"endDate": "2016-02-22",
 * 		"period": "YTD",
 * 		"currency":"EUR",
 * 		"dataType": ["cumulativeReturn", "navPrice"],
 * 		"frequency": "daily"
 * 	}
 *
 * @apiSuccess {Object[]} result 
 * @apiSuccess {Object[]} result.prodAltNumSegs
 * @apiSuccess {String} result.prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} result.prodAltNumSegs.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {Object[]} result.data
 * @apiSuccess {string} result.data.date
 * @apiSuccess {Number} result.data.cumulativeReturn performance growth
 * @apiSuccess {Number} result.data.navPrice NAV Price
 * @apiSuccess {Number} result.productName The name of fund.
 * @apiSuccess {Number} result.currency The currency.
 *  
 *  
 *  
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 *{
 *	"result": [{
 *		"prodAltNumSegs": [{
 *			"prodCdeAltClassCde": "F",
 *			"prodAltNum": "519692"
 *		},
 *		{
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "519692"
 *		},
 *		{
 *			"prodCdeAltClassCde": "O",
 *			"prodAltNum": "0P00007KNF"
 *		},
 *		{
 *			"prodCdeAltClassCde": "P",
 *			"prodAltNum": "519692"
 *		},
 *		{
 *			"prodCdeAltClassCde": "U",
 *			"prodAltNum": "F0HKG06YYM"
 *		},
 *		{
 *			"prodCdeAltClassCde": "T",
 *			"prodAltNum": "000000000000000000000060002195"
 *		},
 *		{
 *			"prodCdeAltClassCde": "W",
 *			"prodAltNum": "60002195"
 *		}],
 *		"data": [{
 *			"date": "2016-03-31",
 *			"cumulativeReturn": -1.57068,
 *			"navPrice": -1.6
 *		},
 *		{
 *			"date": "2016-04-30",
 *			"cumulativeReturn": 1.28546,
 *			"navPrice": 1.28
 *		},
 *		{
 *			"date": "2016-05-31",
 *			"cumulativeReturn": 2.58206,
 *			"navPrice": 2.6
 *		},
 *		{
 *			"date": "2016-06-30",
 *			"cumulativeReturn": 2.21843,
 *			"navPrice": 2.3
 *		}]
 *	}]
 *}
 *
 * @apiUse ErrorMsgResponse
 */