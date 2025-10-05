/**
 * @api {get} /wmds/v4.0/fundHoldingsDiversification Fund Holdings Diversification
 * @apiName Fund Holdings Diversification
 * @apiGroup Quote
 * @apiVersion 4.0.2
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {Object[]} productKeys 
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKeys.market The investment market of the product.</br>
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
 * @apiParam {String} productKeys.productType Product Type. If this field is null, then the productType require to define in productKey.
 * 
 * @apiParamExample Request:
 * 	 {
 *		"productKeys": [
 *			{
 *				"prodCdeAltClassCde": "M",
 *				"prodAltNum": "CAN048",
 *				"market":"CA",
 *				"productType": "UT"
 *			},
 *			{
 *				"prodCdeAltClassCde": "M",
 *				"prodAltNum": "MGS2691",
 *				"market":"CA",	
 *				"productType": "UT"
 *			}
 *		]
 *	}
 * 
 * @apiSuccess {Object[]} holdingsDiversifications
 * @apiSuccess {Object} holdingsDiversifications.header
 * @apiSuccess {String} holdingsDiversifications.header.name Mutual fund name / exchange traded funds name.
 * @apiSuccess {String} holdingsDiversifications.header.prodAltNum Mutual fund symbol / exchange traded funds symbol.
 * @apiSuccess {String} holdingsDiversifications.header.currency Currency of this product.
 * @apiSuccess {Object[]} holdingsDiversifications.assetClasses
 * @apiSuccess {String} holdingsDiversifications.assetClasses.assetClassName Asset class diversification of the fund, as classified by Morningstar.
 * @apiSuccess {Number} holdingsDiversifications.assetClasses.weighting Allocation weight of asset class in the fund, expressed as a percentage.
 * @apiSuccess {Number} holdingsDiversifications.assetClasses.allocationCategoryAverage Average % allocation of asset class in the fund's category.
 * @apiSuccess {String} holdingsDiversifications.assetClasses.weightingIndicator Short / long indicator for the holding.
 * @apiSuccess {String} holdingsDiversifications.assetClasses.allocationCategoryAverageIndicator Short / long indicator for the holding category allocation.
 * @apiSuccess {Object[]} holdingsDiversifications.sectorClasses
 * @apiSuccess {String} holdingsDiversifications.sectorClasses.sectorClassName Market sector diversification by the fund, as classified by Morningstar.
 * @apiSuccess {Number} holdingsDiversifications.sectorClasses.weighting Allocation weight of sector class in the fund, expressed as a percentage
 * @apiSuccess {Number} holdingsDiversifications.sectorClasses.allocationCategoryAverage Average % allocation of sector class in the fund's category.
 * @apiSuccess {String} holdingsDiversifications.sectorClasses.weightingIndicator Short / long indicator for the holding.
 * @apiSuccess {String} holdingsDiversifications.sectorClasses.allocationCategoryAverageIndicator Short / long indicator for the holding category allocation.
 * @apiSuccess {Object[]} holdingsDiversifications.geographicRegions
 * @apiSuccess {String} holdingsDiversifications.geographicRegions.geographicRegionName Geographic region diversification by the fund, as classified by morningstar.
 * @apiSuccess {Number} holdingsDiversifications.geographicRegions.weighting Allocation weight of geographic region in the fund, expressed as a percentage.
 * @apiSuccess {Number} holdingsDiversifications.geographicRegions.allocationCategoryAverage Average % allocation of geographic region in the fund's category.
 * @apiSuccess {Boolean} holdingsDiversifications.geographicRegions.weightingIndicator Short / long indicator for the holding allocation.
 * @apiSuccess {Boolean} holdingsDiversifications.geographicRegions.allocationCategoryAverageIndicator Short / long indicator for the holding category allocation.
 * @apiSuccess {Data} holdingsDiversifications.lastUpdatedDate Date data were last updated.format: yyyy-MM-dd.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"holdingsDiversifications": [
 * 				{
 * 					"header": {		
 * 						"name": "CAN Income Focus (PSG) 75/75",
 * 						"prodAltNum": "CAN048",
 * 						"currency": "CAD"
 * 					},
 * 					"assetClasses": [],
 * 					"sectorClasses": [],
 * 					"geographicRegion": [],
 * 					"lastUpdatedDate": "2017-02-27"
 * 				}
 * 			]
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */