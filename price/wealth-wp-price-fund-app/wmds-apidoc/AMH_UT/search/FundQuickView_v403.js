/**
 * @api {get} /wmds/v4.0/fundQuickView Fund Quick View
 * @apiName Fund Quick View
 * @apiGroup Search
 * @apiVersion 4.0.3
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {String} productType Product Type. If this field is null, then the productType require to define in productKey.
 * @apiParam {Boolean} returnOnlyNumberOfMatches Indicator to indentify return number or detail of matched records.
 * @apiParam {Object[]} criterias Detailed Criteria for search.
 * @apiParam {String} criterias.criteriaKey <p>Possible values as below:</p>
 * 												  <ul>
 * 													<li>tableName</li>
 * 									              	<li>typeOfMover</li>
 * 									              	<li>timeScale</li>
 * 									             	<li>supportMarket</li>
 * 									              	<li>typeOfETF</li>
 * 									              	<li>category</li>
 *  									            <li>productSubType</li>
 * 												  </ul>
 * @apiParam {String} criterias.criteriaValue <p>Different value according to different criteriaKey such as :</p>
 * 												  <ul>
 *  									            <li>TP, BP, hhhh_NEW_FUND for tableName</li>
 * 									              	<li>G for typeOfMover</li>
 * 									              	<li>1M for timeScale</li>
 * 									             	<li>CA for supportMarket</li>
 * 									              	<li>AE for typeOfETF</li>
 *  									            <li>ASFI for category</li>
 *   									            <li>LCUT for productSubType</li>
 * 												  </ul>  
 * @apiParam {String = "eq"} criterias.operator
 * 
 * 
 * 
 * @apiParamExample Request:
 * {
 *		"criterias": [{
 *			"criteriaKey": "tableName",
 *			"criteriaValue": "TP",
 *			"operator": "eq"
 *		},
 *		{
 *			"criteriaKey": "timeScale",
 *			"criteriaValue": "1M",
 *			"operator": "eq"
 *		},
 *		{
 *			"criteriaKey": "category",
 *			"criteriaValue": "ASFI",
 *			"operator": "eq"
 *		},
 *		{
 *			"criteriaKey": "productSubType",
 *			"criteriaValue": "LCUT",
 *			"operator": "eq"
 *		}],
 *		"returnOnlyNumberOfMatches": false,
 *		"productType": "UT"
 *	}
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
 * @apiSuccess {String} quickView.items.riskLvlCde The level of risk of the products</br>
 *												   1 = Low,</br>
 *												   2 = Low to Medium,</br> 
 *												   3 = Medium,</br> 
 *												   4 = Medium to High,</br> 
 *												   5 = High</br> 
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
 * 						"riskLvlCde": "4"
 * 					},
 * 					{
 * 						"name": "Sprott Silver Equities Class Series A",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "SPR421",
 * 						"trailingTotalReturn": 122.47653,
 * 						"riskLvlCde": "4"
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
 * 						"riskLvlCde": "3"
 * 					},
 * 					{
 * 						"name": "Sprott Resource Class Series A",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "SPR106",
 * 						"trailingTotalReturn": 88.31794,
 * 						"riskLvlCde": "3"
 * 					},	
 * 					{	
 * 						"name": "DMP Resource Class Series G",
 * 						"market": "CA",
 * 						"productType": "UT",	
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "DYN9152G",
 * 						"trailingTotalReturn": 87.71044,
 * 						"riskLvlCde": "3"
 * 					},
 * 					{
 * 						"name": "BetaPro Cdn Gold Miners 2x DlyBull ETF",
 * 						"market": "CA",	
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "JOVHGU",
 * 						"trailingTotalReturn": 87.26194,
 * 						"riskLvlCde": "3"
 * 					},	
 * 					{
 * 						"name": "BMO Junior Gold ETF",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",
 * 						"prodAltNum": "BMEZJG",
 * 						"trailingTotalReturn": 83.56313,
 * 						"riskLvlCde": "3"	
 * 					},
 * 					{
 * 						"name": "Mackenzie Precious Metals Cl PWX",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodAltNum": "MFC6090",	
 * 						"trailingTotalReturn": 78.6289,
 * 						"riskLvlCde": "3"
 * 					},
 * 					{
 * 						"name": "RBC Global Precious Metals Sr D",
 * 						"market": "CA",
 * 						"productType": "UT",
 * 						"prodCdeAltClassCde": "M",	
 * 						"prodAltNum": "RBF1038",
 * 						"trailingTotalReturn": 76.63937,
 * 						"riskLvlCde": "3"
 * 					}	
 * 				],
 * 				"lastUpdatedDate": "2017-02-27",
 * 				"totalNumberOfRecords": 905
 * 			}
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */