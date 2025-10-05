/**
 * @api {get} /v4.0/holdingAllocation Holding Allocation
 * @apiName Holding Allocation
 * @apiGroup Quote
 * @apiVersion 4.1.3
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
 * 
 * @apiParamExample Request:
 *  {
 *		"market":"HK",
 *		"productType": "UT",
 *		"prodCdeAltClassCde":"M",
 *		"prodAltNum":"U90319"
 *	}	
 * 
 * @apiSuccess {Object[]} holdingAllocation
 * @apiSuccess {String} holdingAllocation.methods The key to uniquely locate a item.
 * @apiSuccess {Object[]} holdingAllocation.breakdowns
 * @apiSuccess {String} holdingAllocation.breakdowns.name The name of breakdowns.</br>
 * 										 US - United States/美国/美國</br>
 * 										 CA - Canada/加拿大/加拿大</br>
 * 										 LA - Latin America/拉丁美洲/拉丁美洲</br>
 * 										 UK - United Kingdom/英国/英國</br>
 * 										 EZ - Eurozone/欧元区/歐元區</br>
 * 										 EURO - Europe - ex Euro/欧洲(不包括欧元区)/歐洲(不包括歐元區)</br>
 * 										 EMER - Europe - Emerging/新兴欧洲/新興歐洲</br>
 * 										 AFRICA - Africa/非洲/非洲</br>
 * 										 ME - Middle East/中东/中東</br>
 * 										 JP - Japan/日本/日本</br>
 * 										 AU - Australasia/大洋洲/大洋洲</br>
 * 										 AD - Asia - Developed/已发展亚洲/已發展亞洲</br>
 * 										 AE - Asia - Emerging/新兴亚洲/新興亞洲</br>
 * 										 NC - Not Classified/其他/其他</br>
 * 										 BM - Basic Materials/基础材料/基本物料</br>
 * 										 CS - Communication Services/通信服务/電訊服務</br>
 * 										 CC - Consumer Cyclical/周期性消费/周期性消費</br>
 * 										 CD - Consumer Defensive/非周期性消费/防守性消費</br>
 * 										 HC - Healthcare/医疗保健/健康護理</br>
 * 										 INDUS - Industrials/工业/工業</br>
 * 										 RE - Real Estate/房地产/房地產</br>
 * 										 TECH - Technology/科技/科技</br>
 * 										 ENER - Energy/能源/能源</br>
 * 										 FS - Financial Services/金融服务/金融服務</br> 
 * 										 UTIL - Utilities/公用事业/公用</br>
 * 										 GVT - Government/政府債券/政府债券</br>
 * 										 MNCPL - Municipal/市政债券/市政債券</br>
 * 										 CORP - Corporate/企业债券/企業債券</br>
 * 										 SECZ - Securitized/证券化债券/證券化債券</br>
 * 										 CEQV - Cash & Equivalents/现金及现金等价物/現金及現金等價物</br>
 * 										 DRVT - Derivative/衍生工具/衍生工具</br>
 * @apiSuccess {Number} holdingAllocation.breakdowns.rescaledWeighting For Pie chart.
 * @apiSuccess {Number} holdingAllocation.breakdowns.weighting For Figure column of Pie chart.
 * @apiSuccess {Date} holdingAllocation.portfolioDate Date data were last updated.format:yyyy-MM-dd.
 * @apiSuccess {Number} numberOfStockHoldings The number Of Stock Holdings
 * @apiSuccess {Number} numberOfBondHoldings The number Of Bond Holdings
 * @apiSuccess {Date} lastUpdatedDate The last Updated Date
 * 
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 * {
 * 	"holdingAllocation": [{
 * 		"methods": "assetAllocations",
 * 		"breakdowns": [{
 * 			"name": "Bond",
 * 			"rescaledWeighting": 60.79926,
 * 			"weighting": 60.79926
 * 		},
 * 		{
 * 			"name": "Cash",
 * 			"rescaledWeighting": 7.55108,
 * 			"weighting": 7.55108
 * 		},
 * 		{
 * 			"name": "Others",
 * 			"rescaledWeighting": 0.56206,
 * 			"weighting": 0.56206
 * 		},
 * 		{
 * 			"name": "Preferred",
 * 			"rescaledWeighting": 0,
 * 			"weighting": 0
 * 		},
 * 		{
 * 			"name": "Stock",
 * 			"rescaledWeighting": 31.0876,
 * 			"weighting": 31.0876
 * 		}],
 * 		"portfolioDate": "2018-05-31"
 * 	},
 * 	{
 * 		"methods": "globalStockSectors",
 * 		"breakdowns": [{
 * 			"name": "BM",
 * 			"rescaledWeighting": 2.48588,
 * 			"weighting": 0.7728
 * 		},
 * 		{
 * 			"name": "CC",
 * 			"rescaledWeighting": 4.90038,
 * 			"weighting": 1.52341
 * 		},
 * 		{
 * 			"name": "CD",
 * 			"rescaledWeighting": 0,
 * 			"weighting": 0
 * 		},
 * 		{
 * 			"name": "CS",
 * 			"rescaledWeighting": 3.12559,
 * 			"weighting": 0.97167
 * 		},
 * 		{
 * 			"name": "ENER",
 * 			"rescaledWeighting": 11.61864,
 * 			"weighting": 3.61196
 * 		},
 * 		{
 * 			"name": "FS",
 * 			"rescaledWeighting": 26.37864,
 * 			"weighting": 8.20049
 * 		},
 * 		{
 * 			"name": "HC",
 * 			"rescaledWeighting": 2.26376,
 * 			"weighting": 0.70375
 * 		},
 * 		{
 * 			"name": "INDUS",
 * 			"rescaledWeighting": 7.72114,
 * 			"weighting": 2.40032
 * 		},
 * 		{
 * 			"name": "RE",
 * 			"rescaledWeighting": 0,
 * 			"weighting": 0
 * 		},
 * 		{
 * 			"name": "TECH",
 * 			"rescaledWeighting": 37.14372,
 * 			"weighting": 11.5471
 * 		},
 * 		{
 * 			"name": "UTIL",
 * 			"rescaledWeighting": 4.36225,
 * 			"weighting": 1.35612
 * 		}],
 * 		"portfolioDate": "2018-05-31"
 * 	},
 * 	{
 * 		"methods": "regionalExposures",
 * 		"breakdowns": [{
 * 			"name": "AD",
 * 			"rescaledWeighting": 10.489,
 * 			"weighting": 3.26075
 * 		},
 * 		{
 * 			"name": "AE",
 * 			"rescaledWeighting": 24.493,
 * 			"weighting": 7.61438
 * 		},
 * 		{
 * 			"name": "AFRICA",
 * 			"rescaledWeighting": 0,
 * 			"weighting": 0
 * 		},
 * 		{
 * 			"name": "AU",
 * 			"rescaledWeighting": 0,
 * 			"weighting": 0
 * 		},
 * 		{
 * 			"name": "CA",
 * 			"rescaledWeighting": 3.483,
 * 			"weighting": 1.08291
 * 		},
 * 		{
 * 			"name": "EMER",
 * 			"rescaledWeighting": 0,
 * 			"weighting": 0
 * 		},
 * 		{
 * 			"name": "EURO",
 * 			"rescaledWeighting": 0,
 * 			"weighting": 0
 * 		},
 * 		{
 * 			"name": "EZ",
 * 			"rescaledWeighting": 7.207,
 * 			"weighting": 2.24062
 * 		},
 * 		{
 * 			"name": "JP",
 * 			"rescaledWeighting": 16.99,
 * 			"weighting": 5.2817
 * 		},
 * 		{
 *			"name": "LA",
 *			"rescaledWeighting": 1.302,
 *			"weighting": 0.4049
 *		},
 *		{
 *			"name": "ME",
 *			"rescaledWeighting": 0,
 *			"weighting": 0
 *		},
 *		{
 *			"name": "NC",
 *			"rescaledWeighting": 0,
 *			"weighting": 0
 *		},
 *		{
 *			"name": "UK",
 *			"rescaledWeighting": 4.756,
 *			"weighting": 1.47857
 *		},
 *		{
 *			"name": "US",
 *			"rescaledWeighting": 31.279,
 *			"weighting": 9.72379
 *		}],
 *		"portfolioDate": "2018-05-31"
 *	},
 *	{
 *		"methods": "globalBondSectors",
 *		"breakdowns": [{
 *			"name": "CEQV",
 *			"rescaledWeighting": 10.84947,
 *			"weighting": 7.38081
 *		},
 *		{
 *			"name": "CORP",
 *			"rescaledWeighting": 18.04793,
 *			"weighting": 12.4689
 *		},
 *		{
 *			"name": "DRVT",
 *			"rescaledWeighting": 0.30587,
 *			"weighting": 0.16413
 *		},
 *		{
 *			"name": "GVT",
 *			"rescaledWeighting": 70.42756,
 *			"weighting": 48.64302
 *		},
 *		{
 *			"name": "MNCPL",
 *			"rescaledWeighting": 0,
 *			"weighting": 0
 *		},
 *		{
 *			"name": "SECZ",
 *			"rescaledWeighting": 0.36917,
 *			"weighting": 0.25505
 *		}],
 *		"portfolioDate": "2018-05-31"
 *	},
 *	{
 *		"methods": "bondRegionalExposures",
 *		"breakdowns": [
 *		    {
 *		        "name": "AD",
 *		        "rescaledWeighting": 0.04635,
 *		        "weighting": 0.02817
 *		    },
 *		    {
 *		        "name": "AE",
 *		        "rescaledWeighting": 0.62227,
 *		        "weighting": 0.37822
 *		    },
 *		    {
 *		        "name": "AFRICA",
 *		        "rescaledWeighting": 0.63281,
 *		        "weighting": 0.38463
 *		    },
 *		    {
 *		        "name": "AU",
 *		        "rescaledWeighting": 6.09107,
 *		        "weighting": 3.70221
 *		    },
 *		    {
 *		        "name": "CA",
 *		        "rescaledWeighting": 2.22568,
 *		        "weighting": 1.35279
 *		    },
 *		    {
 *		        "name": "EMER",
 *		        "rescaledWeighting": 0.6841,
 *		        "weighting": 0.4158
 *		    },
 *		    {
 *		        "name": "EURO",
 *		        "rescaledWeighting": 0.70451,
 *		        "weighting": 0.42821
 *		    },
 *		    {
 *		        "name": "EZ",
 *		        "rescaledWeighting": 17.07543,
 *		        "weighting": 10.37861
 *		    },
 *		    {
 *		        "name": "JP",
 *		        "rescaledWeighting": 1.69621,
 *		        "weighting": 1.03097
 *		    },
 *		    {
 *		        "name": "LA",
 *		        "rescaledWeighting": 1.90132,
 *		        "weighting": 1.15564
 *		    },
 *		    {
 *		        "name": "ME",
 *		        "rescaledWeighting": 0.3829,
 *		        "weighting": 0.23273
 *		    },
 *		    {
 *		        "name": "NC",
 *		        "rescaledWeighting": 0,
 *		        "weighting": 0
 *		    },
 *		    {
 *		        "name": "UK",
 *		        "rescaledWeighting": 12.24777,
 *		        "weighting": 7.44431
 *		    },
 *		    {
 *		        "name": "US",
 *		        "rescaledWeighting": 55.6896,
 *		        "weighting": 33.84867
 *		    }
 *		],
 *		"portfolioDate": "2018-05-31"
 *  }],
 *	"numberOfStockHoldings": 49,
 *	"numberOfBondHoldings": 24,
 *	"lastUpdatedDate": "2018-05-31"
 * }
 * 
 * @apiUse ErrorMsgResponse
 */