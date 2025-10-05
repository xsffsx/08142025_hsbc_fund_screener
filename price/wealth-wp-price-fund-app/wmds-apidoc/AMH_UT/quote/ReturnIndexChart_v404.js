/**
 * @api {get} /v4.0/returnIndexChart Return Index Chart
 * @apiName Return Index Chart
 * @apiGroup Quote
 * @apiVersion 4.0.4
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
 *		     "date": "2016-12-01",
 *		     "value": 24.76000
 *		 },
 *		 {
 *		     "date": "2016-12-02",
 *		     "value": 24.81000
 *		 },
 *		 {
 *		     "date": "2016-12-03",
 *		     "value": 24.81000
 *		 },
 *		 {
 *		     "date": "2016-12-04",
 *		     "value": 24.81000
 *		 },
 *		 {
 *		     "date": "2016-12-05",
 *		     "value": 24.62000
 *		 },
 *		 {
 *		     "date": "2016-12-06",
 *		     "value": 24.76000
 *		 },
 *		 {
 *		     "date": "2016-12-07",
 *		     "value": 24.77000
 *		 },
 *		 {
 *		     "date": "2016-12-08",
 *		     "value": 25.10000
 *		 }
 * 	  ]
 *}
 *
 * @apiUse ErrorMsgResponse
 */