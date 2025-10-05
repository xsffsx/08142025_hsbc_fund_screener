/**
 * @api {get} /wmds/v4.0/fundSearchCriteria Fund Search Criteria
 * @apiName Fund Search Criteria
 * @apiGroup Search
 * @apiVersion 4.0.2
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {String} productType Denotes the type of the product.
 * @apiParam {Object[]} [predefinedCriterias] Detailed Criteria for search. </br> 
 * @apiParam {String} predefinedCriterias.criteriaKey <p>Possible values as below:</p>
 * 												  	 <ul>
 * 														<li>exchangeCountryCode</li>
 * 									              		<li>country</li>
 * 									              		<li>price</li>
 * 												  	 </ul>
 * @apiParam {String} predefinedCriterias.criteriaValue <p>Different value according to different criteriaKey such as : </p>
 * 												  	    <ul>
 * 															<li>CAN</li>
 * 									              	 		<li>china:england for country</li>
 * 									              			<li>10 for price</li>
 * 								                  		</ul>
 * @apiParam {String = "gt","ge","eq","lt","le","ne","in"} predefinedCriterias.operator
 * 
 * @apiParam {String[]} minMaxCriteriaKeys Criteria key with predefined format for search.sample value : [ALL]
 * @apiParam {String[]} listCriteriaKeys Criteria key with predefined format for search.sample value : [ALL]
 * 
 * @apiParamExample Request:
 * {
 * 	"productType":"SEC",
 * 	"predefinedCriterias":[
 * 		{
 * 			"criteriaKey":"exchangeCountryCode",
 * 			"criteriaValue":"CAN",
 * 			"operator":"in"		
 * 		},
 * 		{
 * 			"criteriaKey":"exchange",
 * 			"criteriaValue":"TSE",
 * 			"operator":"in"		
 * 		},
 * 		{
 * 			"criteriaKey":"sector",
 * 			"criteriaValue":"59",
 * 			"operator":"in"		
 * 		}
 *  ],
 *  "listCriteriaKeys":["ALL"],
 * 	"minMaxCriteriaKeys":["ALL"]
 * }
 * 
 * @apiSuccess {Object[]} minMaxCriterias
 * @apiSuccess {String} minMaxCriterias.criteriaKey Criteria key.
 * @apiSuccess {Number} minMaxCriterias.minimum Minimum of the criteria key.
 * @apiSuccess {Number} minMaxCriterias.maximum Maximum of the criteria key.
 * @apiSuccess {Object[]} listCriterias
 * @apiSuccess {String} listCriterias.criteriaKey Criteria key.
 * @apiSuccess {Object[]} listCriterias.items
 * @apiSuccess {String} listCriterias.items.itemKey Item key.
 * @apiSuccess {String} listCriterias.items.itemValue Item value.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"minMaxCriterias": [
 * 				{		
 * 					"criteriaKey": "priceSalesTTM",
 * 					"minimum": 0.036270000040531,
 * 					"maximum": 624.02880859375
 * 				},
 * 				{
 * 					"criteriaKey": "priceEarningsTTM",
 * 					"minimum": 3.038850069046021,
 * 					"maximum": 1969.697021484375
 * 				},
 * 				{
 * 					"criteriaKey": "priceEarningsPriorTTM",
 * 					"minimum": 5.684710025787354,
 * 					"maximum": 2160.036376953125
 * 				},
 * 				{
 * 					"criteriaKey": "priceCashFlowTTM",
 * 					"minimum": 0.107330001890659,
 * 					"maximum": 76.619270324707031	
 * 				},
 * 				{
 * 					"criteriaKey": "priceBook",
 * 					"minimum": 0.094489999115467,
 * 					"maximum": 4.702469825744629
 * 				},
 * 				{
 * 					"criteriaKey": "pegRatioNextYear",
 * 					"minimum": 49.82308667023689,
 * 					"maximum": 721.15829975705628
 * 				},	
 * 				{
 * 					"criteriaKey": "normalizedPriceEarnings",
 * 					"minimum": 5.684710025787354,
 * 					"maximum": 2160.036376953125
 * 				},
 * 				{
 * 					"criteriaKey": "bookValuePerShare",
 * 					"minimum": "-1.418149948120117",
 * 					"maximum": "37.816978454589844"
 * 				},
 * 				{
 * 					"criteriaKey": "totalLiabilitiesLastYear",
 * 					"minimum": 15.103947639465332,
 * 					"maximum": 25349.72265625
 * 				},
 * 				{
 * 					"criteriaKey": "buyRecommendations",
 * 					"minimum": 0,
 * 					"maximum": 2
 * 				}
 * 			]
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */
