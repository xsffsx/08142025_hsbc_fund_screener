/**
 * @api {get} /v4.0/categoryOverview Category Overview
 * @apiName Category Overview
 * @apiGroup Quote
 * @apiVersion 4.1.3
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {String} productType The Product Type.
 *
 * @apiParamExample Request:
 * 	{
 * 		"productType" : "UT"
 * 	}
 *
 * @apiSuccess {Object[]} categoryOver 
 * @apiSuccess {string} categoryOver.categoryLevel1Code Level 1 Fund Category Code.
 * @apiSuccess {string} categoryOver.categoryLevel1Name Level 1 Fund Category Name.
 * @apiSuccess {Object[]} categoryOver.items
 * @apiSuccess {String} categoryOver.items.categoryCode Level 2 Fund Category Code.
 * @apiSuccess {String} categoryOver.items.categoryName Level 2 Fund Category Code.
 * @apiSuccess {Number} categoryOver.items.return1Y 1-year average performance.
 * @apiSuccess {Number} categoryOver.items.return3Y 3-year average performance.
 * @apiSuccess {Number} categoryOver.items.return5Y 5-year average performance.
 * @apiSuccess {Number} categoryOver.items.return10Y 10-year average performance.
 * @apiSuccess {Number} categoryOver.items.stdDev3Y Standard Devation.
 * @apiSuccess {String} lastUpdatedDate The Last Updated Date.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 *			"categoryOver": [{
 *				"categoryLevel1Code": "ALTS",
 *				"categoryLevel1Name": "Alternatives",
 *				"items": [{
 *					"categoryCode": "CMFD",
 *					"categoryName": "Commodity Funds",
 *					"return1Y": 0.67084,
 *					"return3Y": -0.9804,
 *					"return5Y": -4.20482,
 *					"return10Y": -5.69242,
 *					"stdDev3Y": 22.00075
 *				},
 *				{
 *					"categoryCode": "GLPT",
 *					"categoryName": "Global Property",
 *					"return1Y": -1.4897,
 *					"return3Y": -0.35033,
 *					"return5Y": 3.41443,
 *					"return10Y": 2.31104,
 *					"stdDev3Y": 10.05064
 *				},
 *				{
 *					"categoryCode": "HDFD",
 *					"categoryName": "Hedge Funds",
 *					"return1Y": -1.21244,
 *					"return3Y": -4.89485,
 *					"return5Y": 1.93809,
 *					"return10Y": 0.21326,
 *					"stdDev3Y": 13.609
 *				},
 *				{
 *					"categoryCode": "SSDQ",
 *					"categoryName": "Specialised Sector Equity",
 *					"return1Y": 20.54524,
 *					"return3Y": 8.35986,
 *					"return5Y": 12.29987,
 *					"return10Y": 7.81765,
 *					"stdDev3Y": 14.59507
 *				}]
 * 			},
 * 			{
 *				"categoryLevel1Code": "EDDM",
 *				"categoryLevel1Name": "Equity Developed Market",
 *				"items": [{
 *					"categoryCode": "APJE",
 *					"categoryName": "Asia Pacific ex Japan Equity",
 *					"return1Y": 25.65245,
 *					"return3Y": 8.08032,
 *					"return5Y": 7.00336,
 *					"return10Y": 4.87519,
 *					"stdDev3Y": 15.66836
 *				},
 *				{
 *					"categoryCode": "EUEQ",
 *					"categoryName": "European Equity",
 *					"return1Y": 11.68059,
 *					"return3Y": 4.41266,
 *					"return5Y": 9.30837,
 *					"return10Y": 4.67692,
 *					"stdDev3Y": 13.04927
 *				},
 *				{
 *					"categoryCode": "GLEQ",
 *					"categoryName": "Global Equity",
 *					"return1Y": 14.12826,
 *					"return3Y": 5.47282,
 *					"return5Y": 8.5203,
 *					"return10Y": 3.66138,
 *					"stdDev3Y": 11.73786
 *				},
 *				{
 *					"categoryCode": "JPEQ",
 *					"categoryName": "Japan Equity",
 *					"return1Y": 27.44746,
 *					"return3Y": 11.57446,
 *					"return5Y": 15.04489,
 *					"return10Y": 5.64598,
 *					"stdDev3Y": 16.45806
 *				},
 *				{
 *					"categoryCode": "USEQ",
 *					"categoryName": "US Equity",
 *					"return1Y": 14.58643,
 *					"return3Y": 8.1986,
 *					"return5Y": 12.85133,
 *					"return10Y": 7.6552,
 *					"stdDev3Y": 12.32692
 *				}]
 *	  		},
 *	  		{
 *				"categoryLevel1Code": "EDIM",
 *				"categoryLevel1Name": "Equity Developing Market",
 *				"items": [{
 *					"categoryCode": "DOEQ",
 *					"categoryName": "Domestic Equity",
 *					"return1Y": 35.5032,
 *					"return3Y": 10.81106,
 *					"return5Y": 10.10029,
 *					"return10Y": 4.11121,
 *					"stdDev3Y": 21.93048
 *				},
 *				{
 *					"categoryCode": "GEEQ",
 *					"categoryName": "Global Emerging Markets (GEM) Equity",
 *					"return1Y": 24.5795,
 *					"return3Y": 8.28823,
 *					"return5Y": 3.78758,
 *					"return10Y": 0.60479,
 *					"stdDev3Y": 17.1166
 *				},
 *				{
 *					"categoryCode": "GCEQ",
 *					"categoryName": "Greater China Equity",
 *					"return1Y": 36.42399,
 *					"return3Y": 11.13604,
 *					"return5Y": 10.81589,
 *					"return10Y": 5.08579,
 *					"stdDev3Y": 19.94686
 *				},
 *				{
 *					"categoryCode": "SCEQ",
 *					"categoryName": "Single Country Equity",
 *					"return1Y": 20.42353,
 *					"return3Y": 6.16185,
 *					"return5Y": 4.50111,
 *					"return10Y": 3.83345,
 *					"stdDev3Y": 16.18832
 *				}]
 *			},
 *			{
 *				"categoryLevel1Code": "FI",
 *				"categoryLevel1Name": "Fixed Income",
 *				"items": [{
 *					"categoryCode": "ASFI",
 *					"categoryName": "Asian Fixed Income",
 *					"return1Y": 2.42015,
 *					"return3Y": 2.18012,
 *					"return5Y": 2.15933,
 *					"return10Y": 3.57962,
 *					"stdDev3Y": 4.64302
 *				},
 *				{
 *					"categoryCode": "EUFI",
 *					"categoryName": "European Fixed Income",
 *					"return1Y": 1.3088,
 *					"return3Y": 1.63558,
 *					"return5Y": 3.73274,
 *					"return10Y": 4.83987,
 *					"stdDev3Y": 4.53595
 *				},
 *				{
 *					"categoryCode": "GEFI",
 *					"categoryName": "GEM Fixed Income",
 *					"return1Y": 1.78181,
 *					"return3Y": 3.87119,
 *					"return5Y": 2.71351,
 *					"return10Y": 6.01107,
 *					"stdDev3Y": 6.99683
 *				},
 *				{
 *					"categoryCode": "GLFI",
 *					"categoryName": "Global Fixed Income",
 *					"return1Y": 2.3361,
 *					"return3Y": 1.29759,
 *					"return5Y": 1.8019,
 *					"return10Y": 3.70736,
 *					"stdDev3Y": 5.17003
 *				},
 *				{
 *					"categoryCode": "HYBN",
 *					"categoryName": "High Yield Bond",
 *					"return1Y": 3.44165,
 *					"return3Y": 3.70089,
 *					"return5Y": 3.96074,
 *					"return10Y": 6.08421,
 *					"stdDev3Y": 6.4838
 *				},
 *				{
 *					"categoryCode": "SDFU",
 *					"categoryName": "Short Duration Fund",
 *					"return1Y": 2.044,
 *					"return3Y": 1.18647,
 *					"return5Y": 0.21471,
 *					"return10Y": 1.7675,
 *					"stdDev3Y": 5.0258
 *				},
 *				{
 *					"categoryCode": "USFI",
 *					"categoryName": "US Fixed Income",
 *					"return1Y": 0.58961,
 *					"return3Y": 1.11884,
 *					"return5Y": 1.51696,
 *					"return10Y": 3.40297,
 *					"stdDev3Y": 4.39017
 *				}]
 *			},
 *			{
 *				"categoryLevel1Code": "MUAS",
 *				"categoryLevel1Name": "Multi Asset",
 *				"items": [{
 *					"categoryCode": "MUAS",
 *					"categoryName": "Multi Asset",
 *					"return1Y": 9.56467,
 *					"return3Y": 4.18252,
 *					"return5Y": 4.83189,
 *					"return10Y": 4.00492,
 *					"stdDev3Y": 9.31764
 *				}]
 *			},
 *			{
 *				"categoryLevel1Code": "MRFEMA",
 *				"categoryLevel1Name": "Mutual Recognition of Funds - Equity/Mixed Asset",
 *				"items": [{
 *					"categoryCode": "MRFEMA",
 *					"categoryName": "Mutual Recognition of Funds - Equity/Mixed Asset",
 *					"return1Y": 18.84447,
 *					"return3Y": null,
 *					"return5Y": null,
 *					"return10Y": null,
 *					"stdDev3Y": null
 *				}]
 *			}],
 *			"lastUpdatedDate": "2018-04-04"
 * 		}
 *
 * @apiUse ErrorMsgResponse
 */