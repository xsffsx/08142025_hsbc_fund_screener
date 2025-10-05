/**
 * @api {get} /wmds/v4.0/fundQuickView Fund Quick View
 * @apiName Fund Quick View
 * @apiGroup Search
 * @apiVersion 4.0.2
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {String} productType Product Type. If this field is null, then the productType require to define in productKey.
 * @apiParam {Object[]} criterias Detailed Criteria for search.
 * @apiParam {String} criterias.criteriaKey <p>Possible values as below:</p>
 * 												  <ul>
 * 									              	<li>typeOfMover</li>
 * 									              	<li>timeScale</li>
 * 									             	<li>supportMarket</li>
 * 									              	<li>typeOfETF</li>
 * 												  </ul>
 * @apiParam {String} criterias.criteriaValue <p>Different value according to different criteriaKey such as :</p>
 * 												  <ul>
 * 									              	<li>G for typeOfMover</li>
 * 									              	<li>1M for timeScale</li>
 * 									             	<li>CA for supportMarket</li>
 * 									              	<li>AE for typeOfETF</li>
 * 												  </ul>  
 * @apiParam {String = "gt","ge","eq","lt","le","ne","in"} criterias.operator
 * 
 * 
 * 
 * @apiParamExample Request:
 * {
 * 		"criterias":[
 * 			{
 * 				"criteriaKey":"timeScale",
 * 				"criteriaValue":"1M",
 * 				"operator":"eq"
 * 			}
 * 		],
 * 		"productType":"UT"
 * }	
 * 
 * @apiSuccess {Object} quickView
 * @apiSuccess {Object[]} quickView.items
 * @apiSuccess {String} quickView.items.name Mutual Fund Name / ETF Name.
 * @apiSuccess {String} quickView.items.market The investment market of the product.</br>
 * 										 		GL - Global</br>	
 * 										 		NA - North America</br>
 * 												EU - Europe</br>	
 * 										 		AP - Asia Pacific</br>
 * 										 		AE - Asia (excl Japan)</br>
 * 									     		EM - Emerging Market</br>
 * 										 		UK - UK</br>
 * 										 		JP - Japan</br>
 * 										 		CN - China</br>
 * 										 		HK - Hong Kong</br>	
 * 										 		OS - Other Single Countries</br>
 * 										 		OT - Others
 * @apiSuccess {String} quickView.items.productType Product Type. If this field is null, then the productType require to define in productKey. 
 * @apiSuccess {String} quickView.items.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to international securities identification number (ISIN) or eurclear common code.
 * @apiSuccess {String} quickView.items.prodAltNum Mutual Fund symbol / exchange traded funds symbol (market code) , denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {Number} quickView.items.trailingTotalReturn Trailing total return of the fund according to the request timescale.</br>
 *															E.g.</br>
 *															1 month, 1 year, 3 years, 5 years, 10 years</br>
 *															Trailing total return of the exchange traded funds according to the request timescale.</br>
 *															E.g.</br>
 *															1 month, 3 months, 6 months, 1 year, 3 years, 5 years, 10 years</br>
 * @apiSuccess {Number} quickView.items.changePercent The % change of the specified symbol.
 * @apiSuccess {Number} quickView.items.price The price.
 * @apiSuccess {Date} quickView.lastUpdatedDate Date data were last updated,format: yyyy-MM-dd.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"quickView": {
 * 				"items": [
 * 					{
 * 						"name": "Sprott Silver Equities Class Series A",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "SPR422",
 * 						"trailingTotalReturn": 122.47653,
 * 					},
 * 					{
 * 						"name": "Sprott Silver Equities Class Series A",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "SPR421",
 * 						"trailingTotalReturn": 122.47653,
 * 					},
 * 					{
 * 						"name": "BMO S&P/TSX EqWt Glb BM Hdgd to CAD ETF",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "BMEZMT",
 * 						"trailingTotalReturn": 107.59829,
 * 					},	
 * 					{
 * 						"name": "Sprott Resource Class Series A",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "SPR114",
 * 						"trailingTotalReturn": 88.31794,
 * 					},
 * 					{
 * 						"name": "Sprott Resource Class Series A",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "SPR106",
 * 						"trailingTotalReturn": 88.31794,
 * 					},	
 * 					{	
 * 						"name": "DMP Resource Class Series G",
 * 						"market": "CA",
 * 						"productType": "UT",	
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "DYN9152G",
 * 						"trailingTotalReturn": 87.71044,
 * 					},
 * 					{
 * 						"name": "BetaPro Cdn Gold Miners 2x DlyBull ETF",
 * 						"market": "CA",	
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "JOVHGU",
 * 						"trailingTotalReturn": 87.26194,
 * 					},	
 * 					{
 * 						"name": "BMO Junior Gold ETF",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "BMEZJG",
 * 						"trailingTotalReturn": 83.56313,	
 * 					},
 * 					{
 * 						"name": "Mackenzie Precious Metals Cl PWX",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodAltNum": "MFC6090",	
 * 						"trailingTotalReturn": 78.6289,
 * 					},
 * 					{
 * 						"name": "RBC Global Precious Metals Sr D",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",	
 * 						"prodAltNum": "RBF1038",
 * 						"trailingTotalReturn": 76.63937,
 * 					}	
 * 				],
 * 				"lastUpdatedDate": "2017-02-27"	
 * 			}
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */