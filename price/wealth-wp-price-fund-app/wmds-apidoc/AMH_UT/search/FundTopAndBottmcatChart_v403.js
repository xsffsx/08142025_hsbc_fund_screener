/**
 * @api {get} /wmds/v4.0/fundTopAndBottomCategoryChart Fund Top And Bottom CatChart
 * @apiName Fund Top And Bottom Category Chart
 * @apiGroup Search
 * @apiVersion 4.0.3
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
 * @apiParam {String} marketPeriod The market Period.sample values:5D,1M,1Y,3Y,5Y,10Y.
 * @apiParam {String} [productType] Product Type.
 * @apiParam {String} [productSubType] The product sub type.
 * @apiParamExample Request:
 * 	{
 *		"market":"HK",
 *		"marketPeriod":"1M",
 *		"categoryCode":"ALL",
 *		"productType":"UT",
 *		"productSubType":"MRF"  
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
 *			"marketPeriod": "1M",
 *			"categories": [{
 *				"chartId": "key1",
 *				"categoryName": "Asian Fixed Income",
 *				"categoryCode": "FUNDCAT-ASFI"
 *			},
 *			{
 *				"chartId": "key2",
 *				"categoryName": "Asian Fixed Income",
 *				"categoryCode": "FUNDCAT-ASFI"
 *			}],
 *			"topBottomChartData": [{
 *				"period": "2017-05-01",
 *				"key1": 0,
 *				"key2": 0,
 *				"key3": null,
 *				"key4": null,
 *				"key5": null,
 *				"key6": null,
 *				"key7": null,
 *				"key8": null,
 *				"key9": null,
 *				"key10": null
 *			}]
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */