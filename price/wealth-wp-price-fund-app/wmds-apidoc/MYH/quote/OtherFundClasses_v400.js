/**
 * @api {get} /v4.0/market-data/fund/related Other Fund Classes
 * @apiName Other Fund Classes
 * @apiGroup Quote
 * @apiVersion 4.0.0
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
 *  								     MY - Malaysia</br>	
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
 * @apiSuccess {Object[]} assetClasses
 * @apiSuccess {Object[]} assetClasses.prodAltNumSegs
 * @apiSuccess {String} assetClasses.prodAltNumSegs.prodCdeAltClassCde
 * @apiSuccess {String} assetClasses.prodAltNumSegs.prodAltNum
 * @apiSuccess {String} assetClasses.productName
 * @apiSuccess {String} assetClasses.productShortName
 * @apiSuccess {String} assetClasses.prodStatCde Product State Code
 * 
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 *{
 *	  "assetClasses": [{
 *		 "prodAltNumSegs": [{
 *		     "prodCdeAltClassCde": "F",
 *		     "prodAltNum": "519692"
 *		 },
 *		 {
 *		     "prodCdeAltClassCde": "M",
 *		     "prodAltNum": "519692"
 *		 },
 *		 {
 *		     "prodCdeAltClassCde": "P",
 *		     "prodAltNum": "519692"
 *		 },
 *		 {
 *		     "prodCdeAltClassCde": "T",
 *		     "prodAltNum": "000000000000000000000060002195"
 *		 },
 *		 {
 *		     "prodCdeAltClassCde": "W",
 *		     "prodAltNum": "60002195"
 *		 }],
 *                 "productName": "Bocom Schroders Growth Balanced Fund",
 *                 "productShortName": "Bocom Schroders Growth",
 *                 "prodStatCde": "A"
 * 	  },
 *           {
 *		 "prodAltNumSegs": [{
 *		     "prodCdeAltClassCde": "F",
 *		     "prodAltNum": "540001"
 *		 },
 *		 {
 *		     "prodCdeAltClassCde": "M",
 *		     "prodAltNum": "540001"
 *		 },
 *		 {
 *		     "prodCdeAltClassCde": "O",
 *		     "prodAltNum": "0P00007KLQ"
 *		 },
 *		 {
 *		     "prodCdeAltClassCde": "P",
 *		     "prodAltNum": "540001"
 *		 },
 *		 {
 *		     "prodCdeAltClassCde": "U",
 *		     "prodAltNum": "F0HKG06TD7"
 *		 },
 *		 {
 *		     "prodCdeAltClassCde": "T",
 *		     "prodAltNum": "000000000000000000000060009255"
 *		 },
 *		 {
 *		     "prodCdeAltClassCde": "W",
 *		     "prodAltNum": "60009255"
 *		 }],
 *                  "productName": "hhhh Jintrust Equity Fund",
 *                  "productShortName": "hhhh Equity",
 *                  "prodStatCde": "A"
 * 	  }] 
 *}
 *
 * @apiUse ErrorMsgResponse
 */
