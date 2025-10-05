/**
 * @api {get} /v4.0/returnIndexChart Return Index Chart
 * @apiName Return Index Chart
 * @apiGroup Quote
 * @apiVersion 4.0.5
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
 * @apiParam {String} productType Product Type , If this field is null, then the productType require to define in productKey.
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} timeZone Provided a time zone,it is a string representing the user timezone. Used to format time,sample value :</br>
 * 										 Asia/Hong_Kong</br>
 * @apiParam {String} [startDate]  Start date to retrieve a snapshot index chart data.</br>
 * 										 Noted:  "startDate" will overwrite the "period", "startDate" must appear together with "endDate".</br>                                      
 * @apiParam {String} [endDate]  End date to retrieve a snapshot index chart data.</br>
 * 										 Noted:  "endDate" will overwrite the "period", "endDate" must appear together with "startDate".</br> 
 * @apiParam {String} [period] Time Period,possible values as below:</br>
 * 										 1M - 1 Month</br>
 * 										 3M - 3 Month</br>
 * 										 6M - 6 Month</br>
 * 										 1Y - 1 Year</br>
 * 										 3Y - 3 Year</br>
 *                                       5Y - 5 Year</br>
 * 										 YTD - Year to Date</br>
 * @apiParamExample Request:
 * 	{
 * 		"market" : "HK",
 * 		"productType" : "UT",
 * 		"prodCdeAltClassCde" : "M",
 * 		"prodAltNum" : "U61041",
 * 		"timeZone" : "Asia/Hong_Kong",
 * 		"Period" : "3M"
 * 	}
 *
 * @apiSuccess {Object[]} result 
 * @apiSuccess {string} result.date 
 * @apiSuccess {Number} result.value
 * 
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 *{
 *	  "result": [
 *		 {
 *		     "date": "2017-07-31",
 *		     "value": 5.58824
 *		 },
 *		 {
 *		     "date": "2017-08-31",
 *		     "value": 4.05166
 *		 },
 *		 {
 *		     "date": "2017-09-30",
 *		     "value": 0.29113
 *		 },
 *		 {
 *		     "date": "2017-10-31",
 *		     "value": 6.07533
 *		 },
 *		 {
 *		     "date": "2017-11-30",
 *		     "value": 2.24513
 *		 },
 *		 {
 *		     "date": "2017-12-31",
 *		     "value": 2.66637
 *		 },
 *		 {
 *		     "date": "2018-01-31",
 *		     "value": 8.42427
 *		 },
 *		 {
 *		     "date": "2018-02-28",
 *		     "value": -4.40821
 *		 }
 * 	  ]
 *}
 *
 * @apiUse ErrorMsgResponse
 */