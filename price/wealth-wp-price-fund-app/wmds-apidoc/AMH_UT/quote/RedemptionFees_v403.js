/**
 * @api {get} /wmds/v4.0/redemptionFees Redemption Fees
 * @apiName Redemption Fees
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
 * @apiParam {String} productType Product Type , If this field is null, then the productType require to define in productKey.
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * 
 * @apiParamExample Request:
 * 	{
 * 		"market" : "HK",
 * 		"productType" : "ETF",
 * 		"prodCdeAltClassCde" : "M",
 * 		"prodAltNum" : "XRE",
 * 	}
 *
 * @apiSuccess {Number} maximumFrontLoad The max imum Front Load.
 * @apiSuccess {Object} frontLoads 
 * @apiSuccess {Object[]} frontLoads.frontLoad
 * @apiSuccess {String} frontLoads.frontLoad.unit The frontloads unit display, percentage/monetary.
 * @apiSuccess {String} frontLoads.frontLoad.breakpointUnit The break point unit.
 * @apiSuccess {Number} frontLoads.frontLoad.lowBreakpoint The low Breakpoint amount.
 * @apiSuccess {Number} frontLoads.frontLoad.highBreakpoint The high Breakpoint amount.
 * @apiSuccess {Number} frontLoads.frontLoad.value The amount.
 * @apiSuccess {Number} maximumRedemptionFee The Max imumRedemption fee.
 * @apiSuccess {Object} redemptionFees
 * @apiSuccess {Object[]} redemptionFees.redemptionFee
 * @apiSuccess {String} redemptionFees.redemptionFee.unit The redemptionFee unit display, percentage/monetary.
 * @apiSuccess {String} redemptionFees.redemptionFee.breakpointUnit The break point unit.
 * @apiSuccess {Number} redemptionFees.redemptionFee.lowBreakpoint The low breakpoint amount.
 * @apiSuccess {Number} redemptionFees.redemptionFee.highBreakpoint The high breakpoint amount.
 * @apiSuccess {Number} redemptionFees.redemptionFee.value The amount. 
 * @apiSuccess {Number} maximumManagementFee The Max imumManagement fee.
 * @apiSuccess {Object} managementFees
 * @apiSuccess {Object[]} managementFees.managementFee
 * @apiSuccess {String} managementFees.managementFee.unit The management Fee unit.
 * @apiSuccess {String} managementFees.managementFee.breakpointUnit The break point unit.
 * @apiSuccess {Number} managementFees.managementFee.lowBreakpoint The low Breakpoint amount.
 * @apiSuccess {Number} managementFees.managementFee.value The amount.  
 * @apiSuccess {Date} creationUnitDate
 * @apiSuccess {Object} prospectusCustodianFee
 * @apiSuccess {Object} prospectusCustodianFee.feeSchedule
 * @apiSuccess {String} prospectusCustodianFee.feeSchedule.unit The feeSchedule unit.
 * @apiSuccess {String} prospectusCustodianFee.feeSchedule.breakpointUnit The break point unit.
 * @apiSuccess {Number} prospectusCustodianFee.feeSchedule.lowBreakpoint The low Breakpoint amount.
 * @apiSuccess {Number} prospectusCustodianFee.feeSchedule.value The amount.  
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 *		  "frontLoads": [
 *		    {
 *		      "unit": "Percentage",
 *		      "breakpointUnit": "Monetary",
 *		      "lowBreakpoint": 0,
 *		      "highBreakpoint": 500000,
 *		      "value": 1.5
 *		    },
 *		    {
 *		      "unit": "Percentage",
 *		      "breakpointUnit": "Monetary",
 *		      "lowBreakpoint": 500000,
 *		      "highBreakpoint": 1000000,
 *		      "value": 1.2
 *		    },
 *		    {
 *		      "unit": "Percentage",
 *		      "breakpointUnit": "Monetary",
 *		      "lowBreakpoint": 1000000,
 *		      "highBreakpoint": 5000000,
 *		      "value": 0.8
 *		    },
 *		    {
 *		      "unit": "Monetary",
 *		      "breakpointUnit": "Monetary",
 *		      "lowBreakpoint": 5000000,
 *		      "value": 1000
 *		    }
 *		  ],
 *		  "maximumRedemptionFee": 0.3,
 *		  "redemptionFees": [
 *		    {
 *		      "unit": "Percentage",
 *		      "breakpointUnit": "Months",
 *		      "lowBreakpoint": 0,
 *		      "value": 0.3
 *		    }
 *		  ],
 *		  "maximumManagementFee": 1.5,
 *		  "managementFees": [
 *		    {
 *		      "unit": "Percentage",
 *		      "breakpointUnit": "Monetary",
 *		      "lowBreakpoint": 0,
 *		      "value": 1.5
 *		    }
 *		  ],
 *		  "creationUnitDate": "2016-03-15",
 *		  "prospectusCustodianFee": [
 *		    {
 *		      "unit": "Percentage",
 *		      "breakpointUnit": "Monetary",
 *		      "lowBreakpoint": 0,
 *		      "value": 0.25
 *		    }
 *		  ]
 *		}
 * @apiUse ErrorMsgResponse
 */