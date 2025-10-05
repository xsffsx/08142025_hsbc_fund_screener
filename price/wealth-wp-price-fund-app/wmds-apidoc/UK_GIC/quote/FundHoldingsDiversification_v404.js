/**
 * @api {get} /wmds/v4.0/fundHoldingsDiversification Fund Holdings Diversification
 * @apiName Fund Holdings Diversification
 * @apiGroup Quote
 * @apiVersion 4.0.4
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
 *				"prodAltNum": "540002",
 *				"market":"CN",
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
 * @apiSuccess {Number} holdingsDiversifications.assetClasses.weightingIndicator Short / long indicator for the holding.
 * @apiSuccess {Number} holdingsDiversifications.assetClasses.allocationCategoryAverageIndicator Short / long indicator for the holding category allocation.
 * @apiSuccess {Object[]} holdingsDiversifications.sectorClasses
 * @apiSuccess {String} holdingsDiversifications.sectorClasses.sectorClassName Market sector diversification by the fund, as classified by Morningstar.
 * @apiSuccess {Number} holdingsDiversifications.sectorClasses.weighting Allocation weight of sector class in the fund, expressed as a percentage
 * @apiSuccess {Number} holdingsDiversifications.sectorClasses.allocationCategoryAverage Average % allocation of sector class in the fund's category.
 * @apiSuccess {Number} holdingsDiversifications.sectorClasses.weightingIndicator Short / long indicator for the holding.
 * @apiSuccess {Number} holdingsDiversifications.sectorClasses.allocationCategoryAverageIndicator Short / long indicator for the holding category allocation.
 * @apiSuccess {Object[]} holdingsDiversifications.geographicRegions
 * @apiSuccess {String} holdingsDiversifications.geographicRegions.geographicRegionName Geographic region diversification by the fund, as classified by morningstar.
 * @apiSuccess {Number} holdingsDiversifications.geographicRegions.weighting Allocation weight of geographic region in the fund, expressed as a percentage.
 * @apiSuccess {Number} holdingsDiversifications.geographicRegions.allocationCategoryAverage Average % allocation of geographic region in the fund's category.
 * @apiSuccess {Number} holdingsDiversifications.geographicRegions.weightingIndicator Short / long indicator for the holding allocation.
 * @apiSuccess {Number} holdingsDiversifications.geographicRegions.allocationCategoryAverageIndicator Short / long indicator for the holding category allocation.
 * @apiSuccess {String} holdingsDiversifications.lastUpdatedDate Date data were last updated.format: yyyy-MM-dd.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 *		  "holdingsDiversifications": [
 *		    {
 *		      "header": {
 *		        "name": "hhhh Jintrust Dragon Growth Equity Fund",
 *		        "prodAltNum": "540002",
 *		        "currency": "CNY"
 *		      },
 *		      "assetClasses": [
 *		        {
 *		          "assetClassName": "Domestic Equity - Others",
 *		          "weighting": 100,
 *		          "allocationCategoryAverage": null,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": false
 *		        }
 *		      ],
 *		      "sectorClasses": [
 *		        {
 *		          "sectorClassName": "Basic Materials",
 *		          "weighting": 27.77739,
 *		          "allocationCategoryAverage": 58227.90896,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": true
 *		        },
 *		        {
 *		          "sectorClassName": "Industrials",
 *		          "weighting": 18.1906,
 *		          "allocationCategoryAverage": 58227.90896,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": true
 *		        },
 *		        {
 *		          "sectorClassName": "Consumer Cyclical",
 *		          "weighting": 12.84814,
 *		          "allocationCategoryAverage": 58227.90896,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": true
 *		        },
 *		        {
 *		          "sectorClassName": "Healthcare",
 *		          "weighting": 10.15081,
 *		          "allocationCategoryAverage": 58227.90896,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": true
 *		        },
 *		        {
 *		          "sectorClassName": "Not Classified",
 *		          "weighting": 9.90228,
 *		          "allocationCategoryAverage": 58227.90896,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": true
 *		        },
 *		        {
 *		          "sectorClassName": "Financial Services (include Real Estate)",
 *		          "weighting": 8.85218,
 *		          "allocationCategoryAverage": 58227.90896,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": true
 *		        },
 *		        {
 *		          "sectorClassName": "Technology",
 *		          "weighting": 7.21073,
 *		          "allocationCategoryAverage": 58227.90896,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": true
 *		        },
 *		        {
 *		          "sectorClassName": "Consumer Defensive",
 *		          "weighting": 3.55022,
 *		          "allocationCategoryAverage": 58227.90896,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": true
 *		        },
 *		        {
 *		          "sectorClassName": "Utilities",
 *		          "weighting": 1.02497,
 *		          "allocationCategoryAverage": 58227.90896,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": true
 *		        },
 *		        {
 *		          "sectorClassName": "Communication Services",
 *		          "weighting": 0.49268,
 *		          "allocationCategoryAverage": 58227.90896,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": true
 *		        }
 *		      ],
 *		      "geographicRegions": [
 *		        {
 *		          "geographicRegionName": "China",
 *		          "weighting": 91.57605,
 *		          "allocationCategoryAverage": null,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": false
 *		        },
 *		        {
 *		          "geographicRegionName": "Other countries",
 *		          "weighting": 8.42395,
 *		          "allocationCategoryAverage": null,
 *		          "weightingIndicator": false,
 *		          "allocationCategoryAverageIndicator": false
 *		        }
 *		      ],
 *		      "lastUpdatedDate": "2017-05-17"
 *		    }
 *		  ]
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */