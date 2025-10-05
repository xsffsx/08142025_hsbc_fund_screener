/**
 * @api {get} /wmds/v4.0/trailingReturnChart Trailing Return Chart
 * @apiName Trailing Return Chart
 * @apiGroup Quote
 * @apiVersion 4.0.3
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
 * 			"market":"CN",
 * 			"trailingReturnsPeriod":3,
 * 			"prodCdeAltClassCde":"M",
 * 			"prodAltNum":"540003",
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
 *			"trailingReturns": {
 *				"chartDatas": [{
 *					"jan": null,
 *					"feb": null,
 *					"mar": null,
 *					"apr": null,
 *					"may": null,
 *					"jun": null,
 *					"jul": 21.47252,
 *					"aug": 36.63746,
 *					"sep": 37.49098,
 *					"oct": 23.46463,
 *					"nov": -7.69126,
 *					"dec": -2.5535,
 *					"period": 2007
 *				},
 *				{
 *					"jan": -14.72101,
 *					"feb": -3.51175,
 *					"mar": -24.22364,
 *					"apr": -12.51336,
 *					"may": -15.74592,
 *					"jun": -14.26794,
 *					"jul": -15.8478,
 *					"aug": -19.10695,
 *					"sep": -9.43592,
 *					"oct": -22.23374,
 *					"nov": -12.96234,
 *					"dec": -11.28921,
 *					"period": 2008
 *				},
 *				{
 *					"jan": 8.5478,
 *					"feb": 5.42496,
 *					"mar": 16.88178,
 *					"apr": 18.21867,
 *					"may": 19.78681,
 *					"jun": 13.81405,
 *					"jul": 16.48134,
 *					"aug": -1.12509,
 *					"sep": 0.24255,
 *					"oct": -0.09813,
 *					"nov": 25.28189,
 *					"dec": 21.89315,
 *					"period": 2009
 *				},
 *				{
 *					"jan": 5.48312,
 *					"feb": 0.34678,
 *					"mar": -1.51057,
 *					"apr": 1.75967,
 *					"may": -4.32758,
 *					"jun": -11.51266,
 *					"jul": -0.93233,
 *					"aug": 8.32141,
 *					"sep": 17.33725,
 *					"oct": 15.328,
 *					"nov": 12.7654,
 *					"dec": 10.0629,
 *					"period": 2010
 *				},
 *				{
 *					"jan": -3.25604,
 *					"feb": -1.59375,
 *					"mar": -5.43249,
 *					"apr": -1.74545,
 *					"may": -10.24929,
 *					"jun": -5.07408,
 *					"jul": -1.79346,
 *					"aug": 0.7961,
 *					"sep": -10.54238,
 *					"oct": -8.66366,
 *					"nov": -9.11803,
 *					"dec": -6.66731,
 *					"period": 2011
 *				},
 *				{
 *					"jan": -15.38899,
 *					"feb": -8.3623,
 *					"mar": -6.32898,
 *					"apr": 2.95666,
 *					"may": -2.20232,
 *					"jun": 2.56438,
 *					"jul": -3.81813,
 *					"aug": -8.02716,
 *					"sep": -5.53322,
 *					"oct": -3.56255,
 *					"nov": -6.26758,
 *					"dec": 6.4557,
 *					"period": 2012
 *				},
 *				{
 *					"jan": 16.73508,
 *					"feb": 25.14687,
 *					"mar": 6.18312,
 *					"apr": -0.20092,
 *					"may": 8.83852,
 *					"jun": 1.91388,
 *					"jul": 6.36199,
 *					"aug": -0.13764,
 *					"sep": 20.47747,
 *					"oct": 0.57732,
 *					"nov": 2.83011,
 *					"dec": -11.45841,
 *					"period": 2013
 *				},
 *				{
 *					"jan": 4.40389,
 *					"feb": -3.19005,
 *					"mar": -4.43862,
 *					"apr": -9.46373,
 *					"may": -7.05188,
 *					"jun": 1.95982,
 *					"jul": 4.57939,
 *					"aug": 4.14101,
 *					"sep": 8.98606,
 *					"oct": 9.98572,
 *					"nov": 26.46134,
 *					"dec": 49.69137,
 *					"period": 2014
 *				},
 *				{
 *					"jan": 36.67995,
 *					"feb": 21.06772,
 *					"mar": 9.70839,
 *					"apr": 32.24417,
 *					"may": 46.94817,
 *					"jun": 15.16942,
 *					"jul": -5.08523,
 *					"aug": -24.40451,
 *					"sep": -17.99702,
 *					"oct": 3.259,
 *					"nov": 25.73446,
 *					"dec": 33.63657,
 *					"period": 2015
 *				},
 *				{
 *					"jan": -9.57104,
 *					"feb": -18.03264,
 *					"mar": -13.66575,
 *					"apr": 12.72049,
 *					"may": 11.49508,
 *					"jun": 4.92976,
 *					"jul": 3.80873,
 *					"aug": 6.27174,
 *					"sep": 1.09877,
 *					"oct": 0.70772,
 *					"nov": 1.89518,
 *					"dec": 0.0687,
 *					"period": 2016
 *				},
 *				{
 *					"jan": 1.38084,
 *					"feb": 2.64146,
 *					"mar": 5.7924,
 *					"apr": 0.83911,
 *					"may": -1.58777,
 *					"jun": null,
 *					"jul": null,
 *					"aug": null,
 *					"sep": null,
 *					"oct": null,
 *					"nov": null,
 *					"dec": null,
 *					"period": 2017
 *				}],
 *				"returnPeriod": "3"
 *			},
 *			"trailingReturnsAnalysis": {
 *				"items": [{
 *					"itemName": "BEST",
 *					"returnValue": "49.69137",
 *					"startPeriod": "2014-09-30",
 *					"endPeriod": "2014-12-31"
 *				},
 *				{
 *					"itemName": "WORST",
 *					"returnValue": "-24.40451",
 *					"startPeriod": "2015-05-31",
 *					"endPeriod": "2015-08-31"
 *				}],
 *				"lastUpdatedDate": "2017-05-31"
 *			}
 *		}		
 * 
 * @apiUse ErrorMsgResponse
 */