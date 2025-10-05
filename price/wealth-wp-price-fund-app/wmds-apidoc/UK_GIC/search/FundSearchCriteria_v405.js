/**
 * @api {get} /v4.0/fundSearchCriteria Fund Search Criteria
 * @apiName Fund Search Criteria
 * @apiGroup Search
 * @apiVersion 4.0.5
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
 * 	"productType":"UT",
 * 	"predefinedCriterias":[
 * 		{
 * 			"criteriaKey":"PST",
 * 			"criteriaValue":"MRF",
 * 			"operator":"in"		
 * 		},
 * 		{
 * 			"criteriaKey":"CTRY",
 * 			"criteriaValue":"CN",
 * 			"operator":"in"		
 * 		}
 *  ],
 *  "listCriteriaKeys":["ALL"],
 * 	"minMaxCriteriaKeys":["ALL"]
 * }
 * 
 * @apiSuccess {Object[]} minMaxCriterias
 * @apiSuccess {String} minMaxCriterias.criteriaKey Criteria key.
 * @apiSuccess {Object} minMaxCriterias.minimum Minimum of the criteria key.
 * @apiSuccess {Object} minMaxCriterias.maximum Maximum of the criteria key.
 * @apiSuccess {Object[]} listCriterias
 * @apiSuccess {String} listCriterias.criteriaKey Criteria key.
 * @apiSuccess {Object[]} listCriterias.items
 * @apiSuccess {String} listCriterias.items.itemKey Item key.
 * @apiSuccess {String} listCriterias.items.itemValue Item value.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 *		{
 *		  "minMaxCriterias": [
 *		    {
 *		      "criteriaKey": "MER",
 *		      "minimum": null,
 *		      "maximum": null
 *		    },
 *		    {
 *		      "criteriaKey": "BETA",
 *		      "minimum": 0.64,
 *		      "maximum": 0.94
 *		    },
 *		    {
 *		      "criteriaKey": "STDDVIAT",
 *		      "minimum": 3.745,
 *		      "maximum": 31.004
 *		    },
 *		    {
 *		      "criteriaKey": "ALPH",
 *		      "minimum": 0.09,
 *		      "maximum": 17.83
 *		    },
 *		    {
 *		      "criteriaKey": "SHPRATIO",
 *		      "minimum": 0.244,
 *		      "maximum": 0.84
 *		    },
 *		    {
 *		      "criteriaKey": "RSQR",
 *		      "minimum": 24.54,
 *		      "maximum": 82.86
 *		    },
 *		    {
 *		      "criteriaKey": "MEAN",
 *		      "minimum": null,
 *		      "maximum": null
 *		    },
 *		    {
 *		      "criteriaKey": "DLYCHNG",
 *		      "minimum": null,
 *		      "maximum": null
 *		    },
 *		    {
 *		      "criteriaKey": "D1RTRN",
 *		      "minimum": -0.71411,
 *		      "maximum": 0.74472
 *		    },
 *		    {
 *		      "criteriaKey": "M1RTRN",
 *		      "minimum": -2.96118,
 *		      "maximum": 1.70778
 *		    },
 *		    {
 *		      "criteriaKey": "M3RTRN",
 *		      "minimum": -0.94915,
 *		      "maximum": 9.62039
 *		    },
 *		    {
 *		      "criteriaKey": "M6RTRN",
 *		      "minimum": -6.44656,
 *		      "maximum": 13.02973
 *		    },
 *		    {
 *		      "criteriaKey": "YTDRTRN",
 *		      "minimum": -1.32193,
 *		      "maximum": 12.76358
 *		    },
 *		    {
 *		      "criteriaKey": "Y1RTRN",
 *		      "minimum": -2.40906,
 *		      "maximum": 28.20607
 *		    },
 *		    {
 *		      "criteriaKey": "Y3RTRN",
 *		      "minimum": 3.76791,
 *		      "maximum": 41.37465
 *		    },
 *		    {
 *		      "criteriaKey": "Y5RTRN",
 *		      "minimum": 3.1713,
 *		      "maximum": 22.75588
 *		    },
 *		    {
 *		      "criteriaKey": "Y10RTRN",
 *		      "minimum": 9.22135,
 *		      "maximum": 9.40973
 *		    },
 *		    {
 *		      "criteriaKey": "DIVYLD",
 *		      "minimum": 0,
 *		      "maximum": 38.69868
 *		    }
 * 		  ],
 *		  "listCriterias": [
 *		    {
 *		      "criteriaKey": "FAM",
 *		      "items": [
 *		        {
 *		          "itemKey": "FH0012",
 *		          "itemValue": "JP Morgan"
 *		        },
 *		        {
 *		          "itemKey": "UT",
 *		          "itemValue": "hhhh Jintrust"
 *		        }
 *		      ]
 *		    },
 *		    {
 *		      "criteriaKey": "CAT",
 *		      "items": [
 *		        {
 *		          "itemKey": "FUNDCAT-SSDQ",
 *		          "itemValue": "Specialised Sector Equity"
 *		        },
 *		        {
 *		          "itemKey": "FUNDCAT-ASFI",
 *		          "itemValue": "Asian Fixed Income"
 *		        },
 *		        {
 *		          "itemKey": "FUNDCAT-MUAS",
 *		          "itemValue": "Multi Asset"
 *		        },
 *				{
 *		          "itemKey": "FUNDCAT-DOEQ",
 *		          "itemValue": "Domestic Equity"
 *		        }
 *		      ]
 *		    }
 *		  ]
 *		}
 *
 * @apiUse ErrorMsgResponse
 */