/**
 * @api {get} /v4.0/assetAllocation Asset Allocation
 * @apiName Asset Allocation
 * @apiGroup Quote
 * @apiVersion 4.0.1
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
 * 
 * @apiParamExample Request:
 * 	{
 * 		"market" : "HK",
 * 		"productType" : "UT",
 * 		"prodCdeAltClassCde" : "M",
 * 		"prodAltNum" : "U61041",
 * 	}
 *
 * @apiSuccess {Object[]} assetAllocations
 * @apiSuccess {String} assetAllocations.assetClass
 * @apiSuccess {Number} assetAllocations.assetWeight 
 * @apiSuccess {String} portfolioDate
 * 
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 *{
 *	  "assetAllocations": [
 *		 {
 *		    "assetClass": "Stock",
 *		     "assetWeight": 0.1244
 *		 },
 *		 {
 *		     "assetClass": "Bond",
 *		     "assetWeight": 96.83626
 *		 },
 *		 {
 *		     "assetClass": "Preferred",
 *		     "assetWeight": 0.18252
 *		 },
 *		 {
 *		     "assetClass": "Convertible",
 *		     "assetWeight": 2.3686
 *		 },
 *		 {
 *		     "assetClass": "Cash",
 *		     "assetWeight": 0.00229
 *		 },
 *		 {
 *		     "assetClass": "Other",
 *		     "assetWeight": 0.48593
 *		 }
 * 	  ]
 *           "portfolioDate": "2017-12-31"
 *}
 *
 * @apiUse ErrorMsgResponse
 */
