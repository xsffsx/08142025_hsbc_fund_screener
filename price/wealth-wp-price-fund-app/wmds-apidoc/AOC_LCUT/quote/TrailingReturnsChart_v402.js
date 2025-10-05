/**
 * @api {get} /wmds/v4.0/trailingReturnChart Trailing Return Chart
 * @apiName Trailing Return Chart
 * @apiGroup Quote
 * @apiVersion 4.0.2
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
 * @apiParam {Number} trailingReturnsPeriod Denotes the analysis period of the fund.sample values:3,12,36,60
 * 
 * @apiParamExample Request:
 * 		{
 * 			"market":"CA",
 * 			"trailingReturnsPeriod":3,
 * 			"prodCdeAltClassCde":"M",
 * 			"prodAltNum":"CAN048",
 * 			"productType":"UT"
 * 		}	
 * 
 * @apiSuccess {Object} trailingReturns
 * @apiSuccess {Object[]} trailingReturns.chartDatas
 * @apiSuccess {Number} trailingReturns.chartDatas.jan Provide month end data , the point from end of january.
 * @apiSuccess {Number} trailingReturns.chartDatas.feb Provide month end data , the point from end of february.
 * @apiSuccess {Number} trailingReturns.chartDatas.mar Provide month end data , the point from end of march.
 * @apiSuccess {Number} trailingReturns.chartDatas.apr Provide month end data , the point from end of april.
 * @apiSuccess {Number} trailingReturns.chartDatas.may Provide month end data , the point from end of may.
 * @apiSuccess {Number} trailingReturns.chartDatas.jun Provide month end data , the point from end of june.
 * @apiSuccess {Number} trailingReturns.chartDatas.jul Provide month end data , the point from end of july.
 * @apiSuccess {Number} trailingReturns.chartDatas.aug Provide month end data , the point from end of august.
 * @apiSuccess {Number} trailingReturns.chartDatas.sep Provide month end data , the point from end of september .
 * @apiSuccess {Number} trailingReturns.chartDatas.oct Provide month end data , the point from end of october .
 * @apiSuccess {Number} trailingReturns.chartDatas.nov Provide month end data , the point from end of november.
 * @apiSuccess {Number} trailingReturns.chartDatas.dec Provide month end data , the point from end of december.
 * @apiSuccess {Number} trailingReturns.chartDatas.period The period.
 * @apiSuccess {String} trailingReturns.returnPeriod A indicator to reporesent the trailing period that user has picked.
 * @apiSuccess {Object} trailingReturnsAnalysis
 * @apiSuccess {Object[]} trailingReturnsAnalysis.items
 * @apiSuccess {String} trailingReturnsAnalysis.items.itemsName The key to uniquely locate a items.
 * @apiSuccess {Number} trailingReturnsAnalysis.items.returnValue Best / Worse trailing returns (3-Month, 1-Year, 3-Year, 5-Year) % returns measured at the end of period.
 * @apiSuccess {Date} trailingReturnsAnalysis.items.startPeriod Best / Worst observation start periods, corresponding observation periods used.format: yyyy-MM-dd.
 * @apiSuccess {Date} trailingReturnsAnalysis.items.endPeriod Best / Worst observation end periods, corresponding observation periods used.format: yyyy-MM-dd.
 * @apiSuccess {String} trailingReturnsAnalysis.lastUpdatedDate Date data were last updated.format: yyyy-MM-dd.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"trailingReturns": {
 * 				"chartDatas": [
 * 					{
 * 						"jan": 0.79292,
 * 						"feb": -0.39068,
 * 						"mar": 0.30412,
 * 						"apr": -0.68298,
 * 						"may": -0.51268,
 * 						"jun": 1.58827,
 * 						"jul": 2.21662,
 * 						"aug": 4.04043,
 * 						"sep": 3.36408,
 * 						"oct": 3.19391,
 * 						"nov": -0.29429,
 * 						"dec": -0.59756,
 * 						"period": 2010	
 * 					},
 * 					{	
 * 						"jan": -1.14113,
 * 						"feb": 0.11286,	
 * 						"mar": -0.19333,
 * 						"apr": 0.5372,
 * 						"may": 1.86629,
 * 						"jun": 1.55158,
 * 						"jul": 1.85099,
 * 						"aug": 1.35444,
 * 						"sep": 2.66476,
 * 						"oct": 1.54753,
 * 						"nov": 1.14877,
 * 						"dec": 1.18872,
 * 						"period": 2011
 * 					},
 * 					{
 * 						"jan": -2.18732,
 * 						"feb": 0.81012,
 * 						"period": 2017	
 * 					}	
 * 				],
 * 				"returnPeriod": "3"
 * 			},	
 * 			"trailingReturnsAnalysis": {	
 * 				"items": [
 * 					{
 * 						"itemName": "BEST",
 * 						"returnValue": 4.66783,	
 * 						"startPeriod": "2014-10-31",
 * 						"endPeriod": "20150131"
 * 					},
 * 					{
 * 						"itemName": "WORST",	
 * 						"returnValue": -2.29118,
 * 						"startPeriod": "2016-09-30",
 * 						"endPeriod": "2016-12-31"
 * 					}
 * 				],
 * 				"lastUpdatedDate": "2017-02-28"
 * 			}
 * 		}	
 * 
 * @apiUse ErrorMsgResponse
 */