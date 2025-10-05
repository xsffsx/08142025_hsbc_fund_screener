/**
 * @api {get} /wmds/v4.0/fundTopAndBottomCategory Fund Top And Bottom Category
 * @apiName Fund Top And Bottom Category
 * @apiGroup Search
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
 * @apiParam {String} assetAllocation The selected asset allocation.
 * @apiParam {String} timeScale Denote what time scale (month / year) for this item.
 * 
 * @apiParamExample Request:
 * {
 * 		"market":"SG",
 * 		"timeScale":"1Y",
 * 		"assetAllocation":"ALL"
 * }		
 * 
 * @apiSuccess {Object} fundCategories
 * @apiSuccess {Object[]} fundCategories.categories
 * @apiSuccess {String} fundCategories.categories.itemName The key to uniquely locate a item.
 * @apiSuccess {Object[]} fundCategories.categories.items
 * @apiSuccess {String} fundCategories.categories.items.categoriesCode 10 character code assigned to each Morningstar categories.
 * @apiSuccess {String} fundCategories.categories.items.categoriesName Canadian Mutual Fund categories.
 * @apiSuccess {Number} fundCategories.categories.items.numberOfProducts The number of funds / ETTs.
 * @apiSuccess {Number} fundCategories.categories.items.trailingTotalReturn Trailing total return of the fund according to the request timescale. E.g 1 month, 1 year, 3 years, 5 years, 10 years , Trailing total return of the ETF according to the request timescale.E.g.1 day, 5 days, 1 month, 1 year, 3 years, 5 years, 10 years
 * @apiSuccess {Number} fundCategories.categories.items.averageRisk 3-Year Average Risk (category), 3-year standard deviation of the category.
 * @apiSuccess {Date} fundCategories.lastUpdatedDate Date data were last updated.format:yyyy-MM-dd.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"fundCategories": {
 * 				"categories": [
 * 					{
 * 						"itemName": "TPC",
 * 						"items": []
 * 					},
 * 					{
 * 						"itemName": "BPC",
 * 						"items": []	
 * 					}
 * 				],
 * 				"lastUpdatedDate": "2016-09-30"
 * 			},		
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */