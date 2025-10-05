/**
 * @api {get} /wmds/v4.0/fundTopAndBottomCategoryChart Fund Top And Bottom CatChart
 * @apiName Fund Top And Bottom Category Chart
 * @apiGroup Search
 * @apiVersion 4.0.2
 * 
 * @apiUse QuoteHeaderParam_v402
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
 * @apiParam {String} [categoryCode] 10 character code assigned to each morningstar Category with different Asset Class.
 * @apiParam {String} marketPeriod The market Period.sample values:5D,1M,1Y,3Y,5Y,10Y
 * @apiParamExample Request:
 * 	{
 *		"market":"CN",
 *		"marketPeriod":"1M",
 *		"categoryCode":"ALL" 
 *	}
 * 
 * @apiSuccess {String} marketPeriod The market Period.
 * @apiSuccess {Object[]} categories
 * @apiSuccess {String} categories.chartId Define 10 performing caregories.
 * @apiSuccess {String} categories.categoryName Canadian Mutual Fund Category.
 * @apiSuccess {String} categories.categoryCode 10 character code assigned to each Morningstar Category.
 * @apiSuccess {Object[]} topBottomChartDatas
 * @apiSuccess {String} topBottomChartDatas.period The period.
 * @apiSuccess {Number} topBottomChartDatas.key1 Data come from MDS B/E.
 * @apiSuccess {Number} topBottomChartDatas.key2 Data come from MDS B/E.
 * @apiSuccess {Number} topBottomChartDatas.key3 Data come from MDS B/E.
 * @apiSuccess {Number} topBottomChartDatas.key4 Data come from MDS B/E. 
 * @apiSuccess {Number} topBottomChartDatas.key5 Data come from MDS B/E.    
 * @apiSuccess {Number} topBottomChartDatas.key6 Data come from MDS B/E.
 * @apiSuccess {Number} topBottomChartDatas.key7 Data come from MDS B/E.
 * @apiSuccess {Number} topBottomChartDatas.key8 Data come from MDS B/E. 
 * @apiSuccess {Number} topBottomChartDatas.key9 Data come from MDS B/E.
 * @apiSuccess {Number} topBottomChartDatas.key10 Data come from MDS B/E.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 * 			"marketPeriod": "1M",
 * 			"categories": [],
 * 			"topBottomChartDatas": [],
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */