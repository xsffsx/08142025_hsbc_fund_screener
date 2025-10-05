/**
 * @api {get} /wmds/v4.0/topTenHoldings Top Ten Holdings
 * @apiName Top Ten Holdings
 * @apiGroup Quote
 * @apiVersion 4.0.1
 * 
 * @apiUse QuoteHeaderParam
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
 * @apiParam {String} productType Product Type. If this field is null, then the productType require to define in productKey.  
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * 
 * @apiParamExample Request:
 * {
 * 	"market":"CA",
 * 	"prodCdeAltClassCde":"M",
 * 	"prodAltNum":"CAN048",
 * 	"productType":"UT"
 * }	
 * 
 * @apiSuccess {Object} top10Holdings
 * @apiSuccess {Object[]} top10Holdings.items
 * @apiSuccess {String} top10Holdings.items.market The investment market of the product.</br>
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
 * @apiSuccess {String} top10Holdings.items.productType Product Type , If this field is null, then the productType require to define in productKey.
 * @apiSuccess {String} top10Holdings.items.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} top10Holdings.items.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String} top10Holdings.items.securityName Name of security held in the fund.
 * @apiSuccess {Number} top10Holdings.items.weighting Weight of the security held in the fund, expressed as a percentage of net fund assets.
 * @apiSuccess {String} top10Holdings.items.currency The base currency of the security. 
 * @apiSuccess {Number} top10Holdings.items.marketValue Market value of the security held in the fund.
 * @apiSuccess {Date} lastUpdatedDate Date data were last updated.format:yyyy-MM-dd
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"top10Holdings": {
 * 				"items": [
 * 					{
 * 						"market": "CA",
 * 						"productType": "FV",
 * 						"prodCdeAltClassCde": "M",
 * 						"securityName": "CAN Core Plus Bond (P) 75/75",
 * 						"weighting": 32,
 * 						"marketValue": 320
 * 					},
 * 					{
 * 						"market": "CA",
 * 						"productType": "FV",
 * 						"prodCdeAltClassCde": "M",		
 * 						"securityName": "CAN International Bond (CLI) 75/75",
 * 						"weighting": 11,
 * 						"marketValue": 110
 * 					},
 * 					{
 * 						"market": "CA",
 * 						"productType": "FV",
 * 						"prodCdeAltClassCde": "M",
 * 						"securityName": "CAN Core Bond (P) 75/75",
 * 						"weighting": 8,
 * 						"marketValue": 80
 * 					}
 * 					{
 * 						"market": "CA",
 * 						"productType": "FV",
 * 						"prodCdeAltClassCde": "M",	
 * 						"securityName": "CAN Enhanced Dividend (LK) 75/75",
 * 						"weighting": 4,
 * 						"marketValue": 40
 * 					}	
 * 				],
 * 				"lastUpdatedDate": "2017-01-31"
 * 			}
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */