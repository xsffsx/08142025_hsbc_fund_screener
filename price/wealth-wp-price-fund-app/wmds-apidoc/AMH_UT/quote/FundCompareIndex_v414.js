/**
 * @api {get} /v4.0/fundCompareIndex Fund Compare Index
 * @apiName Fund Compare Index
 * @apiGroup Quote
 * @apiVersion 4.1.4
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
 * @apiSuccess {Object[]} compareIndex 
 * @apiSuccess {string} compareIndex.categoryCode Level 2 Fund Category Code.
 * @apiSuccess {string} compareIndex.categoryName Level 2 Fund Category Name.
 * @apiSuccess {Object[]} compareIndex.items
 * @apiSuccess {String} compareIndex.items.indexId Level 2 Fund IndexID.
 * @apiSuccess {String} compareIndex.items.indexName Level 2 Fund Index Name.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 *			"compareIndex": [{
 *				"categoryCode": "AP",
 *				"categoryName": "Asia Pacific ex Japan Equity",
 *				"items": [{
 *					"indexId": "FOUSA06XRO",
 *					"indexName": "EMIX Global Mining Global Gold TR USD"
 *				},
 *				{
 *					"indexId": "FOUSA08O06",
 *					"indexName": "FTSE EPRA Nareit Developed Asia TR USD"
 *				},
 *				{
 *					"indexId": "FOUSA09H0J",
 *					"indexName": "FTSE EPRA Nareit Global TR USD"
 *				},
 *				{
 *					"indexId": "XIUSA000PM",
 *					"indexName": "MSCI World NR USD"
 *				}]
 * 			},
 * 			{
 *				"categoryCode": "EE",
 *				"categoryName": "European Equity",
 *				"items": [{
 *					"indexId": "XIUSA04GTB",
 *					"indexName": "Cat 50%Barclays EurAgg TR&50%FTSE Wld TR"
 *				},
 *				{
 *					"indexId": "FOUSA08QSW",
 *					"indexName": "Cat 50%MSCI Wld/CD NR&50%MSCI Wld/CS NR"
 *				},
 *				{
 *					"indexId": "FOUSA06CGU",
 *					"indexName": "Euronext Paris CAC Mid 60 NR EUR"
 *				},
 *				{
 *					"indexId": "XIUSA0001M",
 *					"indexName": "FSE DAX TR EUR"
 *				}]
 *	  		},
 *	  		{
 *				"categoryCode": "GN",
 *				"categoryName": "Global Equity",
 *				"items": [{
 *					"indexId": "XIUSA000PO",
 *					"indexName": "MSCI Australia NR USD"
 *				},
 *				{
 *					"indexId": "FOUSA09K50",
 *					"indexName": "MSCI BRIC NR USD"
 *				},
 *				{
 *					"indexId": "XIUSA04F13",
 *					"indexName": "MSCI Brazil NR USD"
 *				},
 *				{
 *					"indexId": "F00000VE8Q",
 *					"indexName": "MSCI China A Onshore NR CNY"
 *				}]
 *			},
 *			{
 *				"categoryCode": "JE",
 *				"categoryName": "Japan Equity",
 *				"items": [{
 *					"indexId": "FOUSA06EGK",
 *					"indexName": "BBgBarc Euro Agg 1-3 Yr TR EUR"
 *				},
 *				{
 *					"indexId": "XIUSA00108",
 *					"indexName": "BBgBarc Euro Agg Bond TR EUR"
 *				},
 *				{
 *					"indexId": "FOUSA06DWM",
 *					"indexName": "BBgBarc Euro Agg Corps TR EUR"
 *				},
 *				{
 *					"indexId": "F00000M90J",
 *					"indexName": "MSCI World NR USD"
 *				}]
 *			},
 *			{
 *				"categoryCode": "UQ",
 *				"categoryName": "US Equity",
 *				"items": [{
 *					"indexId": "XIUSA04GTA",
 *					"indexName": "Cat 25%Barclays EurAgg TR&75%FTSE Wld TR"
 *				},
 *				{
 *					"indexId": "XIUSA000PM",
 *					"indexName": "MSCI World NR USD"
 *				},
 *				{
 *					"indexId": "F00000ZOR2",
 *					"indexName": "Morningstar UK Moderate"
 *				},
 *				{
 *					"indexId": "F00000ZOR1",
 *					"indexName": "Morningstar UK Moderately Adventurous"
 *				},
 *				{
 *					"indexId": "F00000ZOR0",
 *					"indexName": "Morningstar UK Moderately Cautious"
 *				}]
 *			},
 *			{
 *				"categoryCode": "ME",
 *				"categoryName": "Mutual Recognition of Funds - Equity/Mixed Asset",
 *				"items": [{	
 *					"indexId": "XIUSA04GWF",
 *					"indexName": "Morningstar China Large Cap CNY"
 *				}]
 *			}]
 * 		}
 *
 * @apiUse ErrorMsgResponse
 */