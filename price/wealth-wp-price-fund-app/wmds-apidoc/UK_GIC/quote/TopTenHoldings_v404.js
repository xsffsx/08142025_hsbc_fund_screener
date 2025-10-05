/**
 * @api {get} /wmds/v4.0/topTenHoldings Top Ten Holdings
 * @apiName Top Ten Holdings
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
 * @apiParam {String} productType Product Type. If this field is null, then the productType require to define in productKey.  
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * 
 * @apiParamExample Request:
 * {
 * 	"market":"CN",
 * 	"prodCdeAltClassCde":"M",
 * 	"prodAltNum":"968003",
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
 * @apiSuccess {String} lastUpdatedDate Date data were last updated.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 *			"top10Holdings": {
 *				"items": [{
 *					"market": null,
 *					"productType": "BQ",
 *					"prodCdeAltClassCde": "M",
 *					"prodAltNum": null,
 *					"securityName": "Indonesia Rep 4.75%",
 *					"weighting": 1.76184,
 *					"currency": "USD",
 *					"marketValue": 54404792
 *				},
 *				{
 *					"market": null,
 *					"productType": "B",
 *					"prodCdeAltClassCde": "M",
 *					"prodAltNum": null,
 *					"securityName": "Pt Pertamina Persero 4.3%",
 *					"weighting": 1.53057,
 *					"currency": "USD",
 *					"marketValue": 47263125
 *				},
 *				{
 *					"market": null,
 *					"productType": "B",
 *					"prodCdeAltClassCde": "M",
 *					"prodAltNum": null,
 *					"securityName": "Hkt Cap No. 4 Ltd. 3%",
 *					"weighting": 1.4769,
 *					"currency": "USD",
 *					"marketValue": 45605980
 *				},
 *				{
 *					"market": null,
 *					"productType": "BT",
 *					"prodCdeAltClassCde": "M",
 *					"prodAltNum": null,
 *					"securityName": "India(Govt Of) 7.8%",
 *					"weighting": 1.45748,
 *					"currency": "INR",
 *					"marketValue": 45006050
 *				},
 *				{
 *					"market": null,
 *					"productType": "B",
 *					"prodCdeAltClassCde": "M",
 *					"prodAltNum": null,
 *					"securityName": "Axis Bk 2.875%",
 *					"weighting": 1.37442,
 *					"currency": "USD",
 *					"marketValue": 42441361
 *				},
 *				{
 *					"market": null,
 *					"productType": "BT",
 *					"prodCdeAltClassCde": "M",
 *					"prodAltNum": null,
 *					"securityName": "Indonesia(Rep Of) 7.875%",
 *					"weighting": 1.21052,
 *					"currency": "IDR",
 *					"marketValue": 37380203
 *				},
 *				{
 *					"market": null,
 *					"productType": "B",
 *					"prodCdeAltClassCde": "M",
 *					"prodAltNum": null,
 *					"securityName": "Malayan Bkg Berhad N Y Brh FRN",
 *					"weighting": 1.2052,
 *					"currency": "USD",
 *					"marketValue": 37215900
 *				},
 *				{
 *					"market": null,
 *					"productType": "BT",
 *					"prodCdeAltClassCde": "M",
 *					"prodAltNum": null,
 *					"securityName": "Indonesia(Rep Of) 8.25%",
 *					"weighting": 1.16055,
 *					"currency": "IDR",
 *					"marketValue": 35837265
 *				},
 *				{
 *					"market": null,
 *					"productType": "BQ",
 *					"prodCdeAltClassCde": "M",
 *					"prodAltNum": null,
 *					"securityName": "Greenland Hong Kon 3.875%",
 *					"weighting": 1.11431,
 *					"currency": "USD",
 *					"marketValue": 34409374
 *				},
 *				{
 *					"market": null,
 *					"productType": "BT",
 *					"prodCdeAltClassCde": "M",
 *					"prodAltNum": null,
 *					"securityName": "New Zealand(Govt) 5%",
 *					"weighting": 1.07407,
 *					"currency": "NZD",
 *					"marketValue": 33166739
 *				}],
 *				"lastUpdatedDate": "2017-04-30"
 *			}
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */