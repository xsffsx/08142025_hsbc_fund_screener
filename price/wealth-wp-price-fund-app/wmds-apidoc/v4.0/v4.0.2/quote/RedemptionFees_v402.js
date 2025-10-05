/**
 * @api {get} /wmds/v4.0/redemptionFees Redemption Fees
 * @apiName Redemption Fees
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
 * @apiParam {String} productType Product Type , If this field is null, then the productType require to define in productKey.
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * 
 * @apiParamExample Request:
 * 	{
 * 		"market" : "CA",
 * 		"productType" : "ETF",
 * 		"prodCdeAltClassCde" : "M",
 * 		"prodAltNum" : "XRE",
 * 	}
 *
 * @apiSuccess {Object} response
 * @apiSuccess {Object} response.status
 * @apiSuccess {String} response.status.code The status code.
 * @apiSuccess {String} response.status.message The status message.
 * @apiSuccess {Object} response.data
 * @apiSuccess {String} response.data.idtype The data type.
 * @apiSuccess {String} response.data.id The data id.
 * @apiSuccess {Object} response.data.api
 * @apiSuccess {String} response.data.api.name The name.
 * @apiSuccess {Number} response.data.api.MaximumFrontLoad The max imum Front Load.
 * @apiSuccess {Object} response.data.api.FrontLoads 
 * @apiSuccess {Object[]} response.data.api.FrontLoads.FrontLoad
 * @apiSuccess {String} response.data.api.FrontLoads.FrontLoad.Unit The frontloads unit display, percentage/monetary.
 * @apiSuccess {String} response.data.api.FrontLoads.FrontLoad.BreakpointUnit The break point unit.
 * @apiSuccess {Number} response.data.api.FrontLoads.FrontLoad.LowBreakpoint The low Breakpoint amount.
 * @apiSuccess {Number} response.data.api.FrontLoads.FrontLoad.HighBreakpoint The high Breakpoint amount.
 * @apiSuccess {Number} response.data.api.FrontLoads.FrontLoad.Value The amount.
 * @apiSuccess {Number} response.data.api.MaximumRedemptionFee The Max imumRedemption fee.
 * @apiSuccess {Object} response.data.api.RedemptionFees
 * @apiSuccess {Object[]} response.data.api.RedemptionFees.RedemptionFee
 * @apiSuccess {String} response.data.api.RedemptionFees.RedemptionFee.Unit The redemptionFee unit display, percentage/monetary.
 * @apiSuccess {String} response.data.api.RedemptionFees.RedemptionFee.BreakpointUnit The break point unit.
 * @apiSuccess {Number} response.data.api.RedemptionFees.RedemptionFee.LowBreakpoint The low breakpoint amount.
 * @apiSuccess {Number} response.data.api.RedemptionFees.RedemptionFee.HighBreakpoint The high breakpoint amount.
 * @apiSuccess {Number} response.data.api.RedemptionFees.RedemptionFee.Value The amount. 
 * @apiSuccess {Number} response.data.api.MaximumManagementFee The Max imumManagement fee.
 * @apiSuccess {Object} response.data.api.ManagementFees
 * @apiSuccess {Object[]} response.data.api.ManagementFees.ManagementFee
 * @apiSuccess {String} response.data.api.ManagementFees.ManagementFee.Unit The management Fee unit.
 * @apiSuccess {String} response.data.api.ManagementFees.ManagementFee.BreakpointUnit The break point unit.
 * @apiSuccess {Number} response.data.api.ManagementFees.ManagementFee.LowBreakpoint The low Breakpoint amount.
 * @apiSuccess {Number} response.data.api.ManagementFees.ManagementFee.Value The amount.  
 * @apiSuccess {Date} response.data.api.CreationUnitDate
 * @apiSuccess {Object} response.data.api.ProspectusCustodianFee
 * @apiSuccess {Object} response.data.api.ProspectusCustodianFee.FeeSchedule
 * @apiSuccess {String} response.data.api.ProspectusCustodianFee.FeeSchedule.Unit The feeSchedule unit.
 * @apiSuccess {String} response.data.api.ProspectusCustodianFee.FeeSchedule.BreakpointUnit The break point unit.
 * @apiSuccess {Number} response.data.api.ProspectusCustodianFee.FeeSchedule.LowBreakpoint The low Breakpoint amount.
 * @apiSuccess {Number} response.data.api.ProspectusCustodianFee.FeeSchedule.Value The amount.  
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		"response" :{
 * 			"status" : {
 * 				"code":"0",
 * 				"message":"OK"
 * 			},
 * 			"data" : {
 * 				"idtype" : "mstarid",
 * 				"id" : "F00000UMMA",
 * 				"api" : {
 * 					"name" : "LoadSchedules",
 * 					"MaximumFrontLoad" : 1000.00000,
 * 					"FrontLoads" : {
 * 						"FrontLoad" : [
 * 							{
 * 								"Unit" : "Percentage",
 * 								"BreakpointUnit" : "Monetary",
 * 								"LowBreakpoint" : 0,
 * 								"HighBreakpoint" : 500000,
 * 								"Value" : 1.50000
 * 							},
 * 							{
 * 								"Unit" : "Percentage",
 * 								"BreakpointUnit" : "Monetary",
 * 								"LowBreakpoint" : 500000,
 * 								"HighBreakpoint" : 1000000,
 * 								"Value" : 1.20000
 * 							},
 * 							{
 * 								"Unit" : "Percentage",
 * 								"BreakpointUnit" : "Monetary",
 * 								"LowBreakpoint" : 1000000,
 * 								"HighBreakpoint" : 5000000,
 * 								"Value" : 0.80000
 * 							}, 
 * 							{
 * 								"Unit" : "Monetary",
 * 								"BreakpointUnit" : "Monetary",
 * 								"LowBreakpoint" : 5000000,
 * 								"Value" : 1000.00000
 * 							}
 * 						]
 * 					},
 * 					"MaximumRedemptionFee" : 1.50000,
 * 					"RedemptionFees" : {
 * 						"RedemptionFee" : [
 * 							{
 * 								"Unit" : "Percentage",
 * 								"BreakpointUnit" : "Days",		
 * 								"LowBreakpoint" : 0,
 * 								"HighBreakpoint" : 7,
 * 								"Value" : 1.50000
 * 							},
 * 							{
 * 								"Unit" : "Percentage",
 * 								"BreakpointUnit" : "Days",		
 * 								"LowBreakpoint" : 7,
 * 								"HighBreakpoint" : 7,
 * 								"Value" : 1.50000
 * 							},
 * 							{
 * 								"Unit" : "Percentage",
 * 								"BreakpointUnit" : "Days",		
 * 								"LowBreakpoint" : 0,
 * 								"HighBreakpoint" :30,
 * 								"Value" : 0.75000
 * 							},
 * 							{
 * 								"Unit" : "Percentage",
 * 								"BreakpointUnit" : "Days",		
 * 								"LowBreakpoint" : 30,
 * 								"HighBreakpoint" : 180,
 * 								"Value" : 0.50000
 * 							},
 * 							{
 * 								"Unit" : "Percentage",
 * 								"BreakpointUnit" : "Days",		
 * 								"LowBreakpoint" : 180,
 * 								"HighBreakpoint" : 365,
 * 								"Value" : 0.25000
 * 							},
 * 							{
 * 								"Unit" : "Percentage",
 * 								"BreakpointUnit" : "Days",		
 * 								"LowBreakpoint" : 365,
 * 								"Value" : 0.15000
 * 							}
 * 						]
 * 					},						
 * 					"MaximumManagementFee" : 1.50000,
 * 					"ManagementFees" : {
 * 						"ManagementFee" : [
 * 							{
 * 								"Unit" : "Percentage",
 * 								"BreakpointUnit" : "Monetary",
 * 								"LowBreakpoint" : 0,
 * 								"Value" : 1.50000
 * 							}
 * 						]
 * 					
 * 					},
 * 					"CreationUnitDate" : "2014-10-23",
 * 					"ProspectusCustodianFee" : {
 * 						"FeeSchedule" : {
 * 							"Unit" : "Percentage",
 * 							"BreakpointUnit" : Monetary,
 * 							"LowBreakpoint" : 0,
 * 							"Value" : 0.25000
 * 						}
 * 					}
 * 				}
 * 			}
 * 		}
 * 
 * @apiUse ErrorMsgResponse
 */
