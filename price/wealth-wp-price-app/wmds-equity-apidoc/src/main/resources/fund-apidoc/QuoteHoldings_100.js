/**
 * @api {get} /wealth/api/v1/market-data/fund/analysis/fundamental QuoteHoldings
 * @apiName QuoteHoldings
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
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
 * 		"market" : "CN",
 * 		"productType" : "UT",
 * 		"prodCdeAltClassCde" : "M",
 * 		"prodAltNum" : "540002",
 * 	}
 *
 * @apiSuccess {Object} holdings 
 * @apiSuccess {Object} holdings.fundamentals 
 * @apiSuccess {Object} holdings.fundamentals.allHoldings
 * @apiSuccess {Number} holdings.fundamentals.allHoldings.assetsUnderManagement Assets under management, market value of assets managed by the fund.
 * @apiSuccess {String} holdings.fundamentals.allHoldings.assetsUnderManagementCurrencyCode The currency code of assets under management.
 * @apiSuccess {Number} holdings.fundamentals.allHoldings.assetsUnderManagementCategoryAvg Assets under management (category avg)average market value of assets managed by funds in the category.
 * @apiSuccess {String} holdings.fundamentals.allHoldings.assetsUnderManagementCategoryAvgCurrencyCode The currency code of assets under management (category avg).
 * @apiSuccess {Number} holdings.fundamentals.allHoldings.annualPortfolioTurnover Annual portfolio turnover , rate at which the portfolio replaces its holdings on an annual basis.
 * @apiSuccess {Number} holdings.fundamentals.allHoldings.annualPortfolioTurnoverCategoryAvg Annual portfolio turnover (category avg), average annual portfolio turnover of the funds in the category.
 * @apiSuccess {Object} holdings.fundamentals.stockHoldings
 * @apiSuccess {Number} holdings.fundamentals.stockHoldings.priceEarnings Price/Earnings , Price-Earnings ratio compares price per share to earnings per share.
 * @apiSuccess {Number} holdings.fundamentals.stockHoldings.priceEarningsCategoryAvg Price/Earnings (category avg)average P/E ratio of the fund companies in the category.
 * @apiSuccess {Number} holdings.fundamentals.stockHoldings.priceBook Price/Book, Price-to-Book ratio compares market value to book value.
 * @apiSuccess {Number} holdings.fundamentals.stockHoldings.priceBookCategoryAvg Price/Book (Category Avg) average P/B of the fund companies in the category.
 * @apiSuccess {Number} holdings.fundamentals.stockHoldings.returnOnAssets Return on Assets (%) Profit generated from assets, expressed as a percentage.
 * @apiSuccess {Number} holdings.fundamentals.stockHoldings.returnOnAssetsCategoryAvg Return on Assets (%) (category avg)average ROA of the fund companies in the category.
 * @apiSuccess {Number} holdings.fundamentals.stockHoldings.returnOnEquity On equity (%) Profit as a percentage of equity.
 * @apiSuccess {Number} holdings.fundamentals.stockHoldings.returnOnEquityCategoryAvg Return on equity (%) (aategory avg.)average ROE of the fund companies in the category.
 * @apiSuccess {Number} holdings.fundamentals.stockHoldings.dividendYield Dividend Yield (%) Annual dividend payment relative to share price, expressed as a percentage.
 * @apiSuccess {Number} holdings.fundamentals.stockHoldings.dividendYieldCategoryAvg Dividend yield (%) (category avg)average dividend yield of the fund companies in the category.
 * @apiSuccess {Object} holdings.fundamentals.fixedIncomeHoldings
 * @apiSuccess {String} holdings.fundamentals.fixedIncomeHoldings.creditRating Credit rating , morningstar credit rating (e.g. 'BBB')
 * @apiSuccess {String} holdings.fundamentals.fixedIncomeHoldings.creditRatingCategoryAvg Credit rating (category avg.) , average morningstar credit rating of the fund companies in the category.
 * @apiSuccess {Number} holdings.fundamentals.fixedIncomeHoldings.effectiveMaturity Effective Maturity, weighted average of the maturities of underlying bonds in a portfolio, accounting for mortgage prepayments, puts and adjustable coupons.
 * @apiSuccess {String} holdings.fundamentals.fixedIncomeHoldings.effectiveMaturityCategoryAvg Effective maturity (category avg.)average effective maturity of the funds in the category.
 * @apiSuccess {Number} holdings.fundamentals.fixedIncomeHoldings.yieldToMaturity Yield to maturity , estimated internal rate of return on a fixed-interest security if held until maturity date.
 * @apiSuccess {String} holdings.fundamentals.fixedIncomeHoldings.yieldToMaturityCategoryAvg Yield to maturity (category avg)average YTM of the funds in the category.
 * @apiSuccess {Date} holdings.fundamentals.lastUpdatedDate Date data were last updated, format: yyyy-MM-dd.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * 		{
 *			"holdings": {
 *				"fundamentals": {
 *					"allHoldings": {
 *						"assetsUnderManagement": 224155567,
 *						"assetsUnderManagementCurrencyCode": "CNY",
 *						"assetsUnderManagementCategoryAvg": 224155567.0507,
 *						"assetsUnderManagementCategoryAvgCurrencyCode": "USD",
 *						"annualPortfolioTurnover": 497.46,
 *						"annualPortfolioTurnoverCategoryAvg": 490.28
 *					},
 *					"stockHoldings": {
 *						"priceEarnings": 0.04367,
 *						"priceEarningsCategoryAvg": 0.03517,
 *						"priceBook": 0.52184,
 *						"priceBookCategoryAvg": 0.29413,
 *						"returnOnAssets": 4.11977,
 *						"returnOnAssetsCategoryAvg": 7.30733,
 *						"returnOnEquity": 9.39661,
 *						"returnOnEquityCategoryAvg": 14.43248,
 *						"dividendYield": 39.38848,
 *						"dividendYieldCategoryAvg": null
 *					},
 *					"fixedIncomeHoldings": {
 *						"creditRating": null,
 *						"creditRatingCategoryAvg": null,
 *						"effectiveMaturity": 0,
 *						"effectiveMaturityCategoryAvg": null,
 *						"yieldToMaturity": null,
 *						"yieldToMaturityCategoryAvg": null
 *					},
 *					"lastUpdatedDate": "2016-12-31"
 *				}
 *			}
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */
