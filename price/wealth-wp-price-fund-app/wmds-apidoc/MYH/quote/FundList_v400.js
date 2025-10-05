/**
 * @api {get} /v4.0/market-data/fund/list/related Fund List
 * @apiName Fund List
 * @apiGroup Quote
 * @apiVersion 4.0.0
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
 *  								     MY - Malaysia</br>
 * 										 OS - Other Single Countries</br>
 * 										 OT - Others
 * @apiParam {Object[]} productKeys 
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKeys.market The investment market of the product.
 * @apiParam {String} productKeys.productType Product Type. If this field is null, then the productType require to define in productKey.
 * @apiParam {Object[]} [criterias]
 * @apiParam {String} criterias.criteriaKey Display key - topTenHoldings, assetAlloc, stockSectors, equityRegional, bondSectors, bondRegional.
 * @apiParam {Boolean} criterias.criteriaValue If display, default is true.
 * 
 * @apiParamExample Request:
 * {
 *	"productKeys": [{
 * 		"prodCdeAltClassCde": "M",
 * 		"prodAltNum": "U62509",
 * 		"market": "HK",
 * 		"productType": "UT"
 * 	},
 * 	{
 * 		"prodCdeAltClassCde": "M",
 * 		"prodAltNum": "U61624",
 * 		"market": "HK",
 * 		"productType": "UT"
 * 	}],
 * 	"criterias": [{
 * 		"criteriaKey": "topTenHoldings",
 * 		"criteriaValue": true
 *	}]
 * }	
 * 
 * @apiSuccess {Number} totalNumberOfRecords Total number of records.
 * @apiSuccess {Object} products.header
 * @apiSuccess {String} products.header.market The market.
 * @apiSuccess {String} products.header.productType products Type. If this field is null, then the productsType require to define in productsKey. 
 * @apiSuccess {String} products.header.currency Currency of this products.
 * @apiSuccess {Object[]} products.header.prodAltNumSeg
 * @apiSuccess {String} products.header.prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the products. It indicates whether this alternative products code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} products.header.prodAltNumSegs.prodAltNum Denotes the alternative number of the products. It indicates the alternate code of a particular products code, which is used to map the products code used in the market to the internal code used in the system.
 * @apiSuccess {Object} products.summary
 * @apiSuccess {String} products.summary.bid The bid price.
 * @apiSuccess {String} products.summary.offer The offer price.
 * @apiSuccess {String} products.summary.weekRangeLow 52-Week Low price.
 * @apiSuccess {String} products.summary.weekRangeHigh 52-Week High price.
 * @apiSuccess {String} products.summary.weekRangeCurrency Week range currency.
 * @apiSuccess {String} products.summary.riskLvlCde The level of risk of the products</br>
 *												   1 = Low,</br>
 *												   2 = Low to Medium,</br> 
 *												   3 = Medium,</br> 
 *												   4 = Medium to High,</br> 
 *												   5 = High</br> 
 * @apiSuccess {Number} products.summary.lastPrice The last Price
 * @apiSuccess {Number} products.summary.dayEndNAV NAVPS (label: NAV) , Net Asset Value Per Share mutual fund's price per share.
 * @apiSuccess {String} products.summary.dayEndNAVCurrencyCode NAVPS (label: NAV) CurrencyCode.
 * @apiSuccess {Number} products.summary.changeAmountNAV Change($), Change in NAV from previous close, expressed as an absolute amount.
 * @apiSuccess {Number} products.summary.changePercentageNAV Change(%) Change in NAV from previous close, expressed as a percentage.
 * @apiSuccess {String} products.summary.asOfDate As of date of the fund price.
 * @apiSuccess {Number} products.summary.averageDailyVolume Average daily volume.
 * @apiSuccess {Number} products.summary.totalNetAsset Total Net Assets, total asset base of the fund, net of fees and expenses.
 * @apiSuccess {String} products.summary.totalNetAssetCurrencyCode Total Net Assets CurrencyCode.
 * @apiSuccess {Number} products.summary.ratingOverall Overall Morningstar Rating of the fund, Morningstar Rating = risk-adjusted rating (or star rating).
 * @apiSuccess {Number} products.summary.mer MER , Management Expense Ratio.
 * @apiSuccess {Number} products.summary.yield1Yr Distribution Yield , Yield based on the most recent dividend distribution.
 * @apiSuccess {Number} products.summary.switchInMinAmount minimum switch-in amount for fund switch.
 * @apiSuccess {String} products.summary.switchInMinAmountCurrencyCode mininum switch-in amount currency for fund switch.
 * @apiSuccess {Number} products.summary.annualReportOngoingCharge AnnualReportOngoing Charge.
 * @apiSuccess {Number} products.summary.actualManagementFee Actual Management Fee.
 * @apiSuccess {String} products.summary.endDate endDate.format:yyyy-MM-dd
 * @apiSuccess {Object} products.profile
 * @apiSuccess {String} products.profile.name Mutual Fund Name.
 * @apiSuccess {String} products.profile.categoryCode 10 character code assigned to each Morningstar Category.
 * @apiSuccess {String} products.profile.categoryName Mutual Fund Category.
 * @apiSuccess {String} products.profile.familyName Fund House Name.
 * @apiSuccess {String} products.profile.familyCode Fund House Code.
 * @apiSuccess {String} products.profile.categoryLevel1Code The Level 1 Fund Category Code.
 * @apiSuccess {String} products.profile.categoryLevel1Name The Level 1 Fund Category Name.
 * @apiSuccess {String} products.profile.companyName The company name.
 * @apiSuccess {String} products.profile.investmentRegionCode Investment Region Code.
 * @apiSuccess {String} products.profile.investmentRegionName Investment Region Name.
 * @apiSuccess {Number} products.profile.priceQuote The price quote.
 * @apiSuccess {String} products.profile.priceQuoteCurrency The price currency.
 * @apiSuccess {Number} products.profile.changeAmount The change amount. 
 * @apiSuccess {Number} products.profile.changePercent The change percent.
 * @apiSuccess {Number} products.profile.assetsUnderManagement The assets under management. 
 * @apiSuccess {Number} products.profile.assetsUnderManagementCurrencyCode The currency of assets under management.
 * @apiSuccess {String} products.profile.riskLvlCde The level of risk of the products
 * @apiSuccess {String} products.profile.inceptionDate Fund Inception Date.
 * @apiSuccess {String} products.profile.productCurrency Product currency.
 * @apiSuccess {Number} products.profile.turnoverRatio Rate at which the fund replaces its holdings on an annual basis.
 * @apiSuccess {Number} products.profile.stdDev3Yr 3-year volatility (systemic risk) of the fund.
 * @apiSuccess {Number} products.profile.equityStylebox Morningstar Equity Style Box, a 9-square grid that graphically represents the fund's equity investment style.
 * @apiSuccess {Number} products.profile.expenseRatio The Expense Ratio.
 * @apiSuccess {Number} products.profile.marginRatio The Margin Ratio.
 * @apiSuccess {Number} products.profile.initialCharge The Initial Charge Upon Subscription.
 * @apiSuccess {Number} products.profile.annualManagementFee The Annual Management Fee.
 * @apiSuccess {Number} products.profile.distributionYield Fund share class basic information.
 * @apiSuccess {String} products.profile.distributionFrequency Fund share class basic information.
 * @apiSuccess {String} products.profile.dividendYield Dividend Yield.
 * @apiSuccess {Number} products.profile.prodTopSellRankNum Best Seller Rank Number.
 * @apiSuccess {String} products.profile.topSellProdIndex Best Seller Indicator.
 * @apiSuccess {String} products.profile.topPerformersIndicator Top Performers Indicator.
 * @apiSuccess {String} products.profile.prodLaunchDate New Fund Launch Date.format:yyyy-MM-dd
 * @apiSuccess {String} products.profile.investmentObjectiveAndStrategy Description of fund objective and strategy as provided by Morningstar.
 * @apiSuccess {String} products.profile.dailyPerformanceDate Format:yyyy-MM-dd
 * @apiSuccess {String} products.profile.endDateYearRisk Relative Risk Measure Broad Asset Class Date.format:yyyy-MM-dd
 * @apiSuccess {String} products.profile.endDateRiskLvlCde Risk Measure End Date.format:yyyy-MM-dd
 * @apiSuccess {String} products.profile.dayEndBidOfferPricesDate Day End Bid Offer Prices Date.format:yyyy-MM-dd
 * @apiSuccess {String} products.profile.dayEndNAVDate Day End NAV Date.format:yyyy-MM-dd
 * @apiSuccess {String} products.profile.riskFreeRateName MPT Risk Free Rate Name.
 * @apiSuccess {String} products.profile.relativeRiskMeasuresIndexName Relative Risk Measures Index Name.
 * @apiSuccess {String} products.profile.prodStatCde Product State Code.
 * @apiSuccess {String} products.profile.amcmIndicator AMCM Authorize Indicator.
 * @apiSuccess {String} products.profile.nextDealDate Next Deal Date.
 * @apiSuccess {String} products.profile.allowBuy Indicates whether the product is allowed to be bought.
 * @apiSuccess {String} products.profile.allowSell Indicates whether the product is allowed to be sold.
 * @apiSuccess {String} products.profile.allowSwInProdInd Indicates whether the product is allowed to be switchin.
 * @apiSuccess {String} products.profile.allowSwOutProdInd Indicates whether the product is allowed to be switchout.
 * @apiSuccess {String} products.profile.allowSellMipProdInd Only for Fund. Support RSP Indicator.
 * @apiSuccess {String} products.profile.piFundInd Private Fund(PI Fund) Indicator.
 * @apiSuccess {String} products.profile.deAuthFundInd De-Authorized Fund Indicator.
 * @apiSuccess {String} products.profile.annualReportDate Fees and Expense Annual Report Date.Format:yyyy-MM-dd
 * @apiSuccess {String} products.profile.surveyedFundNetAssetsDate Fund Size Date.Format:yyyy-MM-dd
 * @apiSuccess {Object} products.performance
 * 
 * @apiSuccess {Object} products.performance.calendarYearTotalReturns
 * @apiSuccess {Object[]} products.performance.calendarYearTotalReturns.items
 * @apiSuccess {String} products.performance.calendarYearTotalReturns.items.year The year of return data.
 * @apiSuccess {Number} products.performance.calendarYearTotalReturns.items.fundCalendarYearReturn Fund Returns(Monthly) by Calendar Year.
 * @apiSuccess {Number} products.performance.calendarYearTotalReturns.items.fundCalendarYearDailyReturn Fund Returns(Daily) by Calendar Year.
 * @apiSuccess {Number} products.performance.calendarYearTotalReturns.items.fundStubYearEndReturn Fund Stub Year End Return by Calendar Year.
 * @apiSuccess {Number} products.performance.calendarYearTotalReturns.items.stubYearEndReturnIndicator Fund Stub Year End Return Indicator.
 * @apiSuccess {Number} products.performance.calendarYearTotalReturns.items.bestFitIndexCalendarYearReturn Best-Fit Index Returns by Calendar Year.
 * @apiSuccess {Number} products.performance.calendarYearTotalReturns.items.categoryCalendarYearReturn Category/Asset Class Returns(Monthly) by Calendar Year.
 * @apiSuccess {Number} products.performance.calendarYearTotalReturns.items.categoryCalendarYearDailyReturn Category/Asset Class Returns(Daily) by Calendar Year.
 * @apiSuccess {Object[]} products.performance.calendarYearTotalReturns.items.prospectusPrimaryIndexYearReturns Prospectus primary index year Returns by Calendar Year.
 * @apiSuccess {String} products.performance.calendarYearTotalReturns.bestFitIndex Market index showing the highest correlation with a fund over the most recent 36 months, as measured by the highest R-squared.
 * @apiSuccess {String} products.performance.calendarYearTotalReturns.lastUpdatedDate Date information(Monthly) was last updated.
 * @apiSuccess {String} products.performance.calendarYearTotalReturns.dailyLastUpdatedDate Date information(Daily) was last updated.
 * @apiSuccess {Object} products.performance.cumulativeTotalReturns
 * @apiSuccess {Object[]} products.performance.cumulativeTotalReturns.items
 * @apiSuccess {Number} products.performance.cumulativeTotalReturns.items.period The period of return data.
 * @apiSuccess {Number} products.performance.cumulativeTotalReturns.items.totalReturn Fund Returns(Monthly) by cumulative.
 * @apiSuccess {Number} products.performance.cumulativeTotalReturns.items.totalDailyReturn Fund Returns(Daily) by cumulative.
 * @apiSuccess {Number} products.performance.cumulativeTotalReturns.items.bestFitIndexReturn Best-Fit Index Returns by cumulative.
 * @apiSuccess {Number} products.performance.cumulativeTotalReturns.items.categoryReturn Category/Asset Class Returns(Monthly) by cumulative.
 * @apiSuccess {Number} products.performance.cumulativeTotalReturns.items.categoryDailyReturn Category/Asset Class Returns(Daily) by cumulative.
 * @apiSuccess {Object[]} products.performance.calendarYearTotalReturns.items.prospectusPrimaryIndexYearReturns Prospectus primary index Returns by cumulative.
 * @apiSuccess {String} products.performance.cumulativeTotalReturns.bestFitIndex Market index showing the highest correlation with a fund over the most recent 36 months, as measured by the highest R-squared.
 * @apiSuccess {String} products.performance.cumulativeTotalReturns.lastUpdatedDate Date information(Monthly) was last updated.
 * @apiSuccess {String} products.performance.cumulativeTotalReturns.dailyLastUpdatedDate Date information(Daily) was last updated.
 * @apiSuccess {Object} products.morningstarRatings
 * @apiSuccess {String} products.morningstarRatings.morningstarRatingOverall Overall Morningstar Rating of the fund, Morningstar Rating = risk-adjusted rating (or star rating).
 * @apiSuccess {Number} products.morningstarRatings.morningstarRating3Yr 3-Year Morningstar Rating.
 * @apiSuccess {Number} products.morningstarRatings.morningstarRating5Yr 5-Year Morningstar Rating.
 * @apiSuccess {Number} products.morningstarRatings.morningstarRating10Yr 10-Year Morningstar Rating
 * @apiSuccess {String} products.morningstarRatings.morningstarTaxAdjustedRatingOverall Overall Morningstar Tax-Adjusted Rating, Tax-Adjusted Ranking = Relates risk-adjusted and tax-adjusted performances of the fund to those of its peers over a 3-, 5- and 10-year period
 * @apiSuccess {Number} products.morningstarRatings.morningstarTaxAdjustedRating3Yr 3-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} products.morningstarRatings.morningstarTaxAdjustedRating5Yr 5-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} products.morningstarRatings.morningstarTaxAdjustedRating10Yr 10-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} products.morningstarRatings.lastUpdatedDate Date data were last updated.
 * @apiSuccess {Object} products.mgmtAndContactInfo
 * @apiSuccess {String} products.mgmtAndContactInfo.companyName Company Name.
 * @apiSuccess {String} products.mgmtAndContactInfo.address The address.
 * @apiSuccess {String} products.mgmtAndContactInfo.city The city.
 * @apiSuccess {String} products.mgmtAndContactInfo.province The province.
 * @apiSuccess {String} products.mgmtAndContactInfo.postalCode The postalCode.
 * @apiSuccess {String} products.mgmtAndContactInfo.telephoneNo Telephone No.
 * @apiSuccess {String} products.mgmtAndContactInfo.faxNo Fax No.
 * @apiSuccess {String} products.mgmtAndContactInfo.website Website address.
 * @apiSuccess {Object[]} products.mgmtAndContactInfo.mgmtInfos
 * @apiSuccess {String} products.mgmtAndContactInfo.mgmtInfos.managerName Name of current portfolio manager(s).
 * @apiSuccess {String} products.mgmtAndContactInfo.mgmtInfos.startDate Date current manager started managing the fund.
 * @apiSuccess {Object} products.investmentStrategy
 * @apiSuccess {Number} products.investmentStrategy.investmentStyle Investment style.
 * @apiSuccess {Number} products.investmentStrategy.interestRateSensitivity Interest rate sensitivity.
 * @apiSuccess {Number} products.investmentStrategy.assetAllocBondNet Asset alloc bond net.
 * @apiSuccess {Number} products.investmentStrategy.preferredStockNet Preferred stock net.
 * @apiSuccess {Number} products.investmentStrategy.assetAllocCashNet Asset alloc cash net.
 * @apiSuccess {Number} products.investmentStrategy.otherNet other net.
 * @apiSuccess {Number} products.investmentStrategy.assetAllocEquityNet Asset alloc equityNet.
 * @apiSuccess {Object} products.yieldAndCredit
 * @apiSuccess {Number} products.yieldAndCredit.averageCurrencyYield Average currency yield.
 * @apiSuccess {Number} products.yieldAndCredit.averageYieldToMaturity Average yield to maturity.
 * @apiSuccess {Number} products.yieldAndCredit.averageDuration Average duration.
 * @apiSuccess {String} products.yieldAndCredit.lastUpdatedDate Date data were last updated.
 * @apiSuccess {Number} products.yieldAndCredit.creditQualA Credit quality A.
 * @apiSuccess {Number} products.yieldAndCredit.creditQualAA Credit quality AA.
 * @apiSuccess {Number} products.yieldAndCredit.creditQualAAA Credit quality AAA.
 * @apiSuccess {Number} products.yieldAndCredit.creditQualB Credit quality B.
 * @apiSuccess {Number} products.yieldAndCredit.creditQualBB Credit quality BB.
 * @apiSuccess {Number} products.yieldAndCredit.creditQualBBB Credit quality BBB.
 * @apiSuccess {Number} products.yieldAndCredit.creditQualBelowB Credit quality BelowB.
 * @apiSuccess {Number} products.yieldAndCredit.creditQualNotRated Credit quality Not Rated.
 * @apiSuccess {String} products.yieldAndCredit.creditQualDate Credit quality Date.
 * @apiSuccess {Object} products.holdingDetails
 * @apiSuccess {String} products.holdingDetails.totalNetAssets Total asset base of the fund, net of fees and expenses. 
 * @apiSuccess {String} products.holdingDetails.totalNetAssetsCurrencyCode The currency code of TotalNet Asset.
 * @apiSuccess {Number} products.holdingDetails.annualPortfolioTurnover Yearly portfolio turnover.
 * @apiSuccess {String} products.holdingDetails.equityStyle Morningstar Equity Style Box, a 9-square grid that graphically represents the fund's equity investment style.
 * @apiSuccess {String} products.holdingDetails.fixedIncomeStyle  (label: Credit Quality/Duration)Morningstar Fixed Income Style Box , a 9-square grid that graphically represents the fund's fixed income investment style , Credit Quality/ Duration = Overall credit quality and sensitivity of price to interest rate changes.
 * @apiSuccess {Number} products.holdingDetails.sharesOutstanding Shares outstanding.
 * @apiSuccess {Number} products.holdingDetails.netAssetValue ETF's per share value.
 * @apiSuccess {String} products.holdingDetails.netAssetValueCurrencyCode The currency code of Net Asset Value.
 * @apiSuccess {String} products.holdingDetails.premiumDiscountToNAV Percentage at which market value is above or below NAV.
 * @apiSuccess {Number} products.holdingDetails.priceEarningsRatio Compare price to earning per share.
 * @apiSuccess {Number} products.holdingDetails.dividendYield Annual dividend payment relative to share price.
 * @apiSuccess {String} products.holdingDetails.dividendPerShare Dividend payment.
 * @apiSuccess {String} products.holdingDetails.dividendPerShareCurrency The currency code of dividend per share.
 * @apiSuccess {String} products.holdingDetails.dividendPerShareCurrencyCode The currency code of dividend per share.
 * @apiSuccess {String} products.holdingDetails.exDividendDate Last ex-dividend date.format: yyyy-MM-dd
 * @apiSuccess {String} products.holdingDetails.dividendPaymentDate Dividend payment date.format: yyyy-MM-dd
 * @apiSuccess {Number} products.holdingDetails.beta ETF's volatility (systemic risk) relative to the overall market.
 * @apiSuccess {String} products.holdingDetails.taxAdjustedRating Tax-Adjusted rating.
 * @apiSuccess {String} products.holdingDetails.lastUpdatedDate Date data were last updated.
 * @apiSuccess {Object} products.purchaseInfo
 * @apiSuccess {Number} products.purchaseInfo.minimumInitial Minimum Investment.
 * @apiSuccess {String} products.purchaseInfo.minimumInitialCurrencyCode Minimum investment currencyCode.
 * @apiSuccess {Number} products.purchaseInfo.minimumSubsequent Subsequent investment(s).
 * @apiSuccess {String} products.purchaseInfo.minimumSubsequentCurrencyCode Subsequent investment(s) currencyCode.
 * @apiSuccess {Number} products.purchaseInfo.minimumIRA Fund Purchase Details.
 * @apiSuccess {String} products.purchaseInfo.minimumIRACurrencyCode Fund purchase code.
 * @apiSuccess {Boolean} products.purchaseInfo.rrsp Fund attributes.
 * @apiSuccess {String} products.purchaseInfo.loadType Fund Share Class Basic Information. 
 * @apiSuccess {Object} products.rating
 * @apiSuccess {Number} products.rating.rank1Yr 1 year quartile ranking.
 * @apiSuccess {Number} products.rating.rank3Yr 3 year quartile ranking.
 * @apiSuccess {Number} products.rating.rank5Yr 5 year quartile ranking.
 * @apiSuccess {Number} products.rating.rank10Yr 10 year quartile ranking.
 * @apiSuccess {Number} products.rating.averageCreditQuality Average credit quality.
 * @apiSuccess {String} products.rating.averageCreditQualityName Average credit quality Name.
 * @apiSuccess {String} products.rating.ratingDate Rating date data were last updated.
 * @apiSuccess {Object[]} products.risk
 * @apiSuccess {Object} products.risks.yearrisk
 * @apiSuccess {Number} products.risks.yearrisk.year Denote how many year for this risks. 
 * @apiSuccess {Number} products.risks.yearrisk.beta x-year volatility (systemic risks) of fund relative to the overall market.
 * @apiSuccess {Number} products.risks.yearrisk.stdDev x-year volatility (systemic risks) of portfolio.
 * @apiSuccess {Number} products.risks.yearrisk.alpha x-year excess risks-adjusted return of portfolio relative to the return of a benchmark index.
 * @apiSuccess {Number} products.risks.yearrisk.sharpeRatio Measure of x-year risks-adjusted performance of the portfolio.
 * @apiSuccess {Number} products.risks.yearrisk.rSquared x-year correlation of a portfolio's movements to that of a benchmark index.
 * @apiSuccess {Number} products.risks.yearrisk.totalReturn Annualised return.
 * @apiSuccess {String} products.risks.yearrisk.endDate Date data were last updated.
 * @apiSuccess {Object[]} products.topTenHoldings 
 * @apiSuccess {String} products.topTenHoldings.holdingName The holding name.
 * @apiSuccess {String} products.topTenHoldings.holdingCompany The company of the holding.
 * @apiSuccess {Number} products.topTenHoldings.holdingPercent Allocation weight of geographic region in the fund, expressed as a percentage. 
 * @apiSuccess {Object} products.assetAlloc
 * @apiSuccess {Object[]} products.assetAlloc.assetAllocations
 * @apiSuccess {String} products.assetAlloc.assetAllocations.name name.
 * @apiSuccess {Number} products.assetAlloc.assetAllocations.rescaledWeighting RescaledWeighting.
 * @apiSuccess {Number} products.assetAlloc.assetAllocations.weighting Weighting.
 * @apiSuccess {String} products.assetAlloc.portfolioDate Portfolio Date.format:yyyy-MM-dd.
 * @apiSuccess {Object} products.equityRegional
 * @apiSuccess {Object[]} products.stockSectors.globalStockSectors
 * @apiSuccess {String} products.stockSectors.globalStockSectors.name name.
 * @apiSuccess {Number} products.stockSectors.globalStockSectors.rescaledWeighting RescaledWeighting.
 * @apiSuccess {Number} products.stockSectors.globalStockSectors.weighting Weighting.
 * @apiSuccess {String} products.stockSectors.portfolioDate Portfolio Date.format:yyyy-MM-dd.
 * @apiSuccess {Object} products.equityRegional
 * @apiSuccess {Object[]} products.equityRegional.regionalExposures
 * @apiSuccess {String} products.equityRegional.regionalExposures.name name.
 * @apiSuccess {Number} products.equityRegional.regionalExposures.rescaledWeighting RescaledWeighting.
 * @apiSuccess {Number} products.equityRegional.regionalExposures.weighting Weighting.
 * @apiSuccess {String} products.equityRegional.portfolioDate Portfolio Date.format:yyyy-MM-dd.
 * @apiSuccess {Object} products.bondSectors
 * @apiSuccess {Object[]} products.bondSectors.globalBondSectors
 * @apiSuccess {String} products.bondSectors.globalBondSectors.name name.
 * @apiSuccess {Number} products.bondSectors.globalBondSectors.rescaledWeighting RescaledWeighting.
 * @apiSuccess {Number} products.bondSectors.globalBondSectors.weighting Weighting.
 * @apiSuccess {String} products.bondSectors.portfolioDate Portfolio Date.format:yyyy-MM-dd.
 * @apiSuccess {Object} products.bondRegional
 * @apiSuccess {Object[]} products.bondRegional.bondRegionalExposures
 * @apiSuccess {String} products.bondRegional.bondRegionalExposures.name name.
 * @apiSuccess {Number} products.bondRegional.bondRegionalExposures.rescaledWeighting RescaledWeighting.
 * @apiSuccess {Number} products.bondRegional.bondRegionalExposures.weighting Weighting.
 * @apiSuccess {String} products.bondRegional.portfolioDate Portfolio Date.format:yyyy-MM-dd.
 * @apiSuccess {String} products.prodAltNumXCode Mutual Fund symbol / exchange traded funds symbol (market code) , denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String[]} products.swithableGroup Only for fund.The switching rule is that Funds are only allowed to switch in the same Switchable Group.
 *
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 *		{
 *			"totalNumberOfRecords": 1,
 *			"products": [{
 *				"header": {
 *					"market": null,
 *					"productType": null,
 *					"currency": "HKD",
 *					"prodAltNumSeg": [{
 *						"prodCdeAltClassCde": "F",
 *						"prodAltNum": "HFAMH"
 *					},
 *					{
 *						"prodCdeAltClassCde": "I",
 *						"prodAltNum": "LU0762542818"
 *					},
 *					{
 *						"prodCdeAltClassCde": "M",
 *						"prodAltNum": "U62509"
 *					},
 *					{
 *						"prodCdeAltClassCde": "O",
 *						"prodAltNum": "0P0000WA0U"
 *					},
 *					{
 *						"prodCdeAltClassCde": "P",
 *						"prodAltNum": "U62509"
 *					},
 *					{
 *						"prodCdeAltClassCde": "T",
 *						"prodAltNum": "000000000000000000001000047633"
 *					},
 *					{
 *						"prodCdeAltClassCde": "W",
 *						"prodAltNum": "1000047633"
 *					}]
 *				},
 *				"summary": {
 *					"bid": 10.246,
 *					"offer": 10.564,
 *					"weekRangeLow": 10.246,
 *					"weekRangeHigh": 11.246,
 *					"weekRangeCurrency": "HKD",
 *					"lastPrice": null,
 *					"dayEndNAV": null,
 *					"dayEndNAVCurrencyCode": null,
 *					"changeAmountNAV": null,
 *					"changePercentageNAV": null,
 *					"asOfDate": null,
 *					"averageDailyVolume": null,
 *					"totalNetAsset": null,
 *					"totalNetAssetCurrencyCode": null,
 *					"ratingOverall": null,
 *					"mer": null,
 *					"yield1Yr": null,
 *					"switchInMinAmount": null,
 *					"switchInMinAmountCurrencyCode": null,
 *					"annualReportOngoingCharge": null,
 *					"actualManagementFee": null,
 *					"endDate": null
 *				},
 *				"profile": {
 *					"name": "hhhh GIF - MANAGED SOLUTION-ASIA FOCUSED INCOME (AM2-HKD-MDIST-CASH)",
 *					"categoryCode": "MS",
 *					"categoryName": "Multi Asset",
 *					"familyCode": "hhhhA",
 *					"familyName": "hhhh Global Asset Management  (Hong Kong) Limited",
 *					"categoryLevel1Code": "MUAS",
 *					"categoryLevel1Name": "Multi Asset",
 *					"companyName": "hhhh GIF - MANAGED SOLUTION-ASIA FOCUSED INCOME (AM2-HKD-MDIST-CASH)",
 *					"investmentRegionCode": "EU",
 *					"investmentRegionName": "Europe",
 *					"priceQuote": 10.4,
 *					"priceQuoteCurrency": "HKD",
 *					"changeAmount": -0.004,
 *					"changePercent": -0.03845,
 *					"assetsUnderManagement": 1000241908,
 *					"assetsUnderManagementCurrencyCode": "USD",
 *					"riskLvlCde": "2",
 *					"inceptionDate": "2012-05-25",
 *					"productCurrency": "HKD",
 *					"turnoverRatio": null,
 *					"stdDev3Yr": null,
 *					"equityStylebox": null,
 *					"expenseRatio": null,
 *					"marginRatio": null,
 *					"initialCharge": null,
 *					"annualManagementFee": null,
 *					"distributionYield": null,
 *					"distributionFrequency": "Monthly",
 *					"dividendYield": 3.71416,
 *					"prodTopSellRankNum": null,
 *					"topSellProdIndex": "N",
 *					"topPerformersIndicator": null,
 *					"prodLaunchDate": null,
 *					"investmentObjectiveAndStrategy": "The sub-fund invests for income and moderate capital growth through an active asset allocation in a diversified portfolio of fixed income and equity securities as well as money market and cash instruments. The sub-fund will normally invest a minimum of 70% of its net assets in Asian (including Asia-Pacific and excluding Japan) based income oriented assets in both fixed income and equity markets including, but not limited to corporate bonds, sovereign bonds and higher yielding equities. The sub-fund may also invest in other non- Asian based assets such as global emerging market bonds, US Treasuries and eligible closed-ended Real Estate Investment Trusts (\"REITs\"). Exposure to these assets may be achieved through direct investments and/or investment in units or shares of UCITS and/or other Eligible UCIs.",
 *					"dailyPerformanceDate": "2018-09-07",
 *					"endDateYearRisk": "2018-08-31",
 *					"endDateRiskLvlCde": "2018-08-31",
 *					"riskFreeRateName": null,
 *					"relativeRiskMeasuresIndexName": null,
 *					"dayEndBidOfferPricesDate": null,
 *					"dayEndNAVDate": null,
 *					"prodStatCde": "A",
 *					"amcmIndicator": "Y",
 *					"nextDealDate": "2018-08-13",
 *					"allowBuy": "Y",
 *					"allowSell": "Y",
 *					"allowSwInProdInd": "Y",
 *					"allowSwOutProdInd": "Y",
 *					"allowSellMipProdInd": "Y",
 *					"piFundInd": "N",
 *					"deAuthFundInd": "N",
 *					"annualReportDate": "2017-08-31",
 *					"surveyedFundNetAssetsDate": "2019-01-23"
 *				},
 *				"performance": {
 *					"calendarYearTotalReturns": {
 *						"items": [{
 *							"year": "YTD",
 *							"fundCalendarYearReturn": 0.7222,
 *							"fundCalendarYearDailyReturn": 2.26288,
 *							"fundStubYearEndReturn": null,
 *							"stubYearEndReturnIndicator": null,
 *							"bestFitIndexCalendarYearReturn": -4.64523,
 *							"categoryCalendarYearReturn": -0.47802,
 *							"categoryCalendarYearDailyReturn": 1.28473,
 *							"prospectusPrimaryIndexYearReturns": [-0.47658]
 *						},
 *						{
 *							"year": "2017",
 *							"fundCalendarYearReturn": 8.34637,
 *							"fundCalendarYearDailyReturn": null,
 *							"fundStubYearEndReturn": null,
 *							"stubYearEndReturnIndicator": null,
 *							"bestFitIndexCalendarYearReturn": 23.25072,
 *							"categoryCalendarYearReturn": 10.178,
 *							"categoryCalendarYearDailyReturn": null,
 *							"prospectusPrimaryIndexYearReturns": [10.24192]
 *						},
 *						{
 *							"year": "2016",
 *							"fundCalendarYearReturn": 0.78864,
 *							"fundCalendarYearDailyReturn": null,
 *							"fundStubYearEndReturn": null,
 *							"stubYearEndReturnIndicator": null,
 *							"bestFitIndexCalendarYearReturn": 4.28755,
 *							"categoryCalendarYearReturn": -0.34643,
 *							"categoryCalendarYearDailyReturn": null,
 *							"prospectusPrimaryIndexYearReturns": [2.57663]
 *						},
 *						{
 *							"year": "2015",
 *							"fundCalendarYearReturn": 7.39695,
 *							"fundCalendarYearDailyReturn": null,
 *							"fundStubYearEndReturn": null,
 *							"stubYearEndReturnIndicator": null,
 *							"bestFitIndexCalendarYearReturn": -9.64369,
 *							"categoryCalendarYearReturn": 10.79321,
 *							"categoryCalendarYearDailyReturn": null,
 *							"prospectusPrimaryIndexYearReturns": [8.22418]
 *						},
 *						{
 *							"year": "2014",
 *							"fundCalendarYearReturn": 3.20513,
 *							"fundCalendarYearDailyReturn": null,
 *							"fundStubYearEndReturn": null,
 *							"stubYearEndReturnIndicator": null,
 *							"bestFitIndexCalendarYearReturn": -7.27218,
 *							"categoryCalendarYearReturn": 5.28444,
 *							"categoryCalendarYearDailyReturn": null,
 *							"prospectusPrimaryIndexYearReturns": [6.83954]
 *						},
 *						{
 *							"year": "2013",
 *							"fundCalendarYearReturn": 33.54086,
 *							"fundCalendarYearDailyReturn": null,
 *							"fundStubYearEndReturn": null,
 *							"stubYearEndReturnIndicator": null,
 *							"bestFitIndexCalendarYearReturn": 26.82805,
 *							"categoryCalendarYearReturn": 19.61344,
 *							"categoryCalendarYearDailyReturn": null,
 *							"prospectusPrimaryIndexYearReturns": [19.8221]
 *						}],
 *						"bestFitIndex": "MSCI Europe NR EUR",
 *						"lastUpdatedDate": "2018-08-01",
 *						"dailyLastUpdatedDate": "2018-07-31"
 *					},
 *					"cumulativeTotalReturns": {
 *						"items": [{
 *							"period": "1M",
 *							"totalReturn": -1.69173,
 *							"totalDailyReturn": 2.31214,
 *							"bestFitIndexReturn": -0.75005,
 *							"categoryReturn": -1.09701,
 *							"categoryDailyReturn": 2.28127,
 *							"prospectusPrimaryIndexReturns": [-0.69145]
 *						},
 *						{
 *							"period": "3M",
 *							"totalReturn": 3.667,
 *							"totalDailyReturn": -0.88661,
 *							"bestFitIndexReturn": -2.79563,
 *							"categoryReturn": 3.65559,
 *							"categoryDailyReturn": 1.95801,
 *							"prospectusPrimaryIndexReturns": [3.99505]
 *						},
 *						{
 *							"period": "6M",
 *							"totalReturn": 0.7222,
 *							"totalDailyReturn": -1.89376,
 *							"bestFitIndexReturn": -4.64523,
 *							"categoryReturn": -0.47802,
 *							"categoryDailyReturn": -0.12523,
 *							"prospectusPrimaryIndexReturns": [-0.47658]
 *						},
 *						{
 *							"period": "1Y",
 *							"totalReturn": 3.71839,
 *							"totalDailyReturn": 4.21982,
 *							"bestFitIndexReturn": 3.99662,
 *							"categoryReturn": 2.43187,
 *							"categoryDailyReturn": 4.32674,
 *							"prospectusPrimaryIndexReturns": [2.84588]
 *						},
 *						{
 *							"period": "3Y",
 *							"totalReturn": 0.43395,
 *							"totalDailyReturn": 0.70021,
 *							"bestFitIndexReturn": 3.05864,
 *							"categoryReturn": 1.99222,
 *							"categoryDailyReturn": 1.46708,
 *							"prospectusPrimaryIndexReturns": [2.60648]
 *						},
 *						{
 *							"period": "5Y",
 *							"totalReturn": 8.41093,
 *							"totalDailyReturn": 7.17585,
 *							"bestFitIndexReturn": 5.35829,
 *							"categoryReturn": null,
 *							"categoryDailyReturn": 1.46708,
 *							"prospectusPrimaryIndexReturns": [8.51285]
 *						},
 *						{
 *							"period": "10Y",
 *							"totalReturn": 7.04505,
 *							"totalDailyReturn": 7.40194,
 *							"bestFitIndexReturn": 0.93031,
 *							"categoryReturn": 4.49912,
 *							"categoryDailyReturn": 5.28826,
 *							"prospectusPrimaryIndexReturns": [5.47386]
 *						}],
 *						"bestFitIndex": "MSCI Europe NR EUR",
 *						"lastUpdatedDate": "2018-08-01",
 *						"dailyLastUpdatedDate": "2018-07-31"
 *					}
 *				},
 *				"morningstarRatings": {
 *					"morningstarRatingOverall": "3",
 *					"morningstarRating3Yr": 4,
 *					"morningstarRating5Yr": 3,
 *					"morningstarRating10Yr": null,
 *					"morningstarTaxAdjustedRatingOverall": null,
 *					"morningstarTaxAdjustedRating3Yr": null,
 *					"morningstarTaxAdjustedRating5Yr": null,
 *					"morningstarTaxAdjustedRating10Yr": null,
 *					"lastUpdatedDate": "2018-05-31"
 *				},
 *				"mgmtAndContactInfo": {
 *					"companyName": null,
 *					"address": null,
 *					"city": null,
 *					"province": null,
 *					"country": null,
 *					"postalCode": null,
 *					"telephoneNo": null,
 *					"faxNo": null,
 *					"website": null,
 *					"mgmtInfos": [{
 *						"managerName": "Denis Gould",
 *						"startDate": "2012-05-25"
 *					}]
 *				},
 *				"investmentStrategy": {
 *					"investmentStyle": "2",
 *					"interestRateSensitivity": null,
 *					"assetAllocBondNet": 55.65434,
 *					"assetAllocEquityNet": 35.62796,
 *					"preferredStockNet": 0.05274,
 *					"assetAllocCashNet": 8.44057,
 *					"otherNet": 0.27705
 *				},
 *				"yieldAndCredit": {
 *					"averageCurrentYield": 4.74522,
 *					"averageYieldToMaturity": 4.49002,
 *					"averageDuration": null,
 *					"averageCreditQualityName": null,
 *					"lastUpdatedDate": null,
 *					"creditQualA": null,
 *					"creditQualAA": null,
 *					"creditQualAAA": null,
 *					"creditQualB": null,
 *					"creditQualBB": null,
 *					"creditQualBBB": null,
 *					"creditQualBelowB": null,
 *					"creditQualNotRated": null,
 *					"creditQualDate": null
 *				},
 *				"holdingDetails": {
 *					"totalNetAssets": null,
 *					"totalNetAssetsCurrencyCode": null,
 *					"annualPortfolioTurnover": null,
 *					"equityStyle": null,
 *					"fixedIncomeStyle": null,
 *					"sharesOutstanding": null,
 *					"netAssetValue": null,
 *					"netAssetValueCurrencyCode": null,
 *					"premiumDiscountToNAV": null,
 *					"priceEarningsRatio": null,
 *					"dividendYield": null,
 *					"dividendPerShare": "0.037587000",
 *					"dividendPerShareCurrency": "HKD",
 *					"dividendPerShareCurrencyCode": null,
 *					"exDividendDate": "2018-06-29",
 *					"dividendPaymentDate": null,
 *					"beta": null,
 *					"taxAdjustedRating": null,
 *					"lastUpdatedDate": "2018-05-31"
 *				},
 *				"purchaseInfo": {
 *					"rrsp": null,
 *					"loadType": null,
 *					"expenseRatio": 1.6,
 *					"initialCharge": 3,
 *					"annualManagementFee": 0,
 *					"minimumInitial": 5000,
 *					"minimumInitialCurrencyCode": "HKD",
 *					"minimumSubsequent": null,
 *					"minimumSubsequentCurrencyCode": "HKD",
 *					"minimumIRA": null,
 *					"minimumIRACurrencyCode": null,
 *					"minInitInvst": 5000,
 *					"minInitInvstCurrencyCode": "HKD",
 *					"minSubqInvst": null,
 *					"minSubqInvstCurrencyCode": "HKD",
 *					"hhhhMinInitInvst": 10000,
 *					"hhhhMinInitInvstCurrencyCode": "HKD",
 *					"hhhhMinSubqInvst": 10000,
 *					"hhhhMinSubqInvstCurrencyCode": "HKD"
 *				},
 *				"rating": {
 *					"morningstarRating": null,
 *					"taxAdjustedRating": null,
 *					"averageCreditQualityName": null,
 *					"averageCreditQuality": null,
 *					"rank1Yr": 4,
 *					"rank3Yr": 2,
 *					"rank5Yr": 2,
 *					"rank10Yr": null,
 *					"ratingDate": null
 *				},
 *				"risk": [{
 *					"yearRisk": {
 *						"year": 1,
 *						"beta": null,
 *						"stdDev": 4.74,
 *						"alpha": null,
 *						"sharpeRatio": 0.429,
 *						"rSquared": null,
 *						"totalReturn": 4.0736,
 *						"endDate": "2018-05-31"
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 3,
 *						"beta": null,
 *						"stdDev": 6.476,
 *						"alpha": null,
 *						"sharpeRatio": 0.534,
 *						"rSquared": null,
 *						"totalReturn": 4.42756,
 *						"endDate": "2018-05-31"
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 5,
 *						"beta": null,
 *						"stdDev": 6.166,
 *						"alpha": null,
 *						"sharpeRatio": 0.518,
 *						"rSquared": null,
 *						"totalReturn": 3.70598,
 *						"endDate": "2018-05-31"
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 10,
 *						"beta": null,
 *						"stdDev": null,
 *						"alpha": null,
 *						"sharpeRatio": null,
 *						"rSquared": null,
 *						"totalReturn": null,
 *						"endDate": "2018-05-31"
 *					}
 *				}],
 *				"topTenHoldings": [{
 *					"holdingName": "1",
 *					"holdingCompany": "hhhh GIF Global Em Mkts Local Dbt ZD",
 *					"holdingPercent": 10.21815
 *				},
 *				{
 *					"holdingName": "2",
 *					"holdingCompany": "hhhh FTSE All-World Index Instl Acc",
 *					"holdingPercent": 9.60886
 *				},
 *				{
 *					"holdingName": "3",
 *					"holdingCompany": "Tencent Holdings Ltd",
 *					"holdingPercent": 2.2061
 *				},
 *				{
 *					"holdingName": "4",
 *					"holdingCompany": "hhhh Japan Index Instl Acc",
 *					"holdingPercent": 1.76938
 *				},
 *				{
 *					"holdingName": "5",
 *					"holdingCompany": "hhhh GIF India Fixed Income ZD",
 *					"holdingPercent": 1.67835
 *				},
 *				{
 *					"holdingName": "6",
 *					"holdingCompany": "Taiwan Semiconductor Manufacturing Co Ltd",
 *					"holdingPercent": 1.38862
 *				},
 *				{
 *					"holdingName": "7",
 *					"holdingCompany": "Samsung Electronics Co Ltd",
 *					"holdingPercent": 1.09633
 *				},
 *				{
 *					"holdingName": "8",
 *					"holdingCompany": "China Construction Bank Corp H",
 *					"holdingPercent": 1.09633
 *				},
 *				{
 *					"holdingName": "9",
 *					"holdingCompany": "Industrial And Commercial Bank Of China Ltd H",
 *					"holdingPercent": 0.99728
 *				},
 *				{
 *					"holdingName": "10",
 *					"holdingCompany": "hhhh European Index Institutional Acc",
 *					"holdingPercent": 0.85003
 *				}],
 *				"assetAlloc": {
 *					"assetAllocations": [{
 *						"name": "Bond",
 *						"rescaledWeighting": 0.86385,
 *						"weighting": 0.8639
 *					},
 *					{
 *						"name": "Cash",
 *						"rescaledWeighting": 1.04668,
 *						"weighting": 1.04674
 *					},
 *					{
 *						"name": "Others",
 *						"rescaledWeighting": 0.04405,
 *						"weighting": 0.04406
 *					},
 *					{
 *						"name": "Preferred",
 *						"rescaledWeighting": 0.00609,
 *						"weighting": 0.00609
 *					},
 *					{
 *						"name": "Stock",
 *						"rescaledWeighting": 98.03933,
 *						"weighting": 98.0453
 *					}],
 *					"portfolioDate": "2018-06-30"
 *				},
 *				"stockSectors": {
 *					"globalStockSectors": [{
 *						"name": "BM",
 *						"rescaledWeighting": 11.92655,
 *						"weighting": 11.69342
 *					},
 *					{
 *						"name": "CC",
 *						"rescaledWeighting": 4.72692,
 *						"weighting": 4.63452
 *					},
 *					{
 *						"name": "CD",
 *						"rescaledWeighting": 4.9111,
 *						"weighting": 4.8151
 *					},
 *					{
 *						"name": "CS",
 *						"rescaledWeighting": 9.08376,
 *						"weighting": 8.9062
 *					},
 *					{
 *						"name": "ENER",
 *						"rescaledWeighting": 16.4685,
 *						"weighting": 16.14659
 *					},
 *					{
 *						"name": "FS",
 *						"rescaledWeighting": 24.97087,
 *						"weighting": 24.48276
 *					},
 *					{
 *						"name": "HC",
 *						"rescaledWeighting": 6.92132,
 *						"weighting": 6.78603
 *					},
 *					{
 *						"name": "INDUS",
 *						"rescaledWeighting": 13.23274,
 *						"weighting": 12.97408
 *					},
 *					{
 *						"name": "RE",
 *						"rescaledWeighting": 1.03492,
 *						"weighting": 1.01469
 *					},
 *					{
 *						"name": "TECH",
 *						"rescaledWeighting": 6.72333,
 *						"weighting": 6.59191
 *					},
 *					{
 *						"name": "UTIL",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					}],
 *					"portfolioDate": "2018-06-30"
 *				},
 *				"equityRegional": {
 *					"regionalExposures": [{
 *						"name": "AD",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "AE",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "AFRICA",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "AU",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "CA",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "EMER",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "EURO",
 *						"rescaledWeighting": 11.401,
 *						"weighting": 11.17808
 *					},
 *					{
 *						"name": "EZ",
 *						"rescaledWeighting": 53.629,
 *						"weighting": 52.58097
 *					},
 *					{
 *						"name": "JP",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "LA",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "ME",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "NC",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "UK",
 *						"rescaledWeighting": 34.3,
 *						"weighting": 33.6296
 *					},
 *					{
 *						"name": "US",
 *						"rescaledWeighting": 0.67,
 *						"weighting": 0.65665
 *					}],
 *					"portfolioDate": "2018-03-31"
 *				},
 *				"bondSectors": {
 *					"globalBondSectors": [{
 *						"name": "CEQV",
 *						"rescaledWeighting": 67.76241,
 *						"weighting": 0.98372
 *					},
 *					{
 *						"name": "CORP",
 *						"rescaledWeighting": 27.82391,
 *						"weighting": 0.43523
 *					},
 *					{
 *						"name": "DRVT",
 *						"rescaledWeighting": 3.94251,
 *						"weighting": 0.06167
 *					},
 *					{
 *						"name": "GVT",
 *						"rescaledWeighting": 0.47116,
 *						"weighting": 0.00737
 *					},
 *					{
 *						"name": "MNCPL",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					},
 *					{
 *						"name": "SECZ",
 *						"rescaledWeighting": 0,
 *						"weighting": 0
 *					}],
 *					"portfolioDate": "2018-06-30"
 *				},
 *				"bondRegional": {
 *					"bondRegionalExposures": [{
 *						"name": "AD",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "AE",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "AFRICA",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "AU",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "CA",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "EMER",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "EURO",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "EZ",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "JP",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "LA",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "ME",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "NC",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "UK",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					},
 *					{
 *						"name": "US",
 *						"rescaledWeighting": null,
 *						"weighting": null
 *					}],
 *					"portfolioDate": "2018-06-30"
 *				},
 *				"lastUpdatedDate": "2018-06-30",
 *				"prodAltNumXCode": "0P0000WA0U",
 *				"swithableGroup": null
 *			}]
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */