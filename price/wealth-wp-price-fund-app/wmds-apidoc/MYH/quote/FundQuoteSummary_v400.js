/**
 * @api {get} /v4.0/market-data/fund/detail/related Fund Quote Summary
 * @apiName Fund Quote Summary
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
 * @apiParam {String} productType Product Type. If this field is null, then the productType require to define in productKey.
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * 
 * @apiParamExample Request:
 *  {
 *		"market":"CN",
 *		"productType": "UT",
 *		"prodCdeAltClassCde":"M",
 *		"prodAltNum":"540002"
 *	}	
 * 
 * @apiSuccess {Object} summary
 * @apiSuccess {Number} summary.bid The bid price.
 * @apiSuccess {Number} summary.weekRangeLow 52-Week Low price.
 * @apiSuccess {Number} summary.weekRangeHigh 52-Week High price.
 * @apiSuccess {Number} summary.offer The offer price.
 * @apiSuccess {String} summary.weekRangeCurrency Week range currency.
 * @apiSuccess {Object} summary.calendarYearTotalReturns
 * @apiSuccess {Object[]} summary.calendarYearTotalReturns.items
 * @apiSuccess {String} summary.calendarYearTotalReturns.items.year The year of return data.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.fundCalendarYearReturn Fund Returns(Monthly) by Calendar Year.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.fundCalendarYearDailyReturn Fund Returns(Daily) by Calendar Year.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.fundStubYearEndReturn Fund Stub Year End Return by Calendar Year.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.stubYearEndReturnIndicator Fund Stub Year End Return Indicator.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.bestFitIndexCalendarYearReturn Best-Fit Index Returns by Calendar Year.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.categoryCalendarYearReturn Category/Asset Class Returns(Monthly) by Calendar Year.
 * @apiSuccess {Number} summary.calendarYearTotalReturns.items.categoryCalendarYearDailyReturn Category/Asset Class Returns(Daily) by Calendar Year.
 * @apiSuccess {Object[]} summary.calendarYearTotalReturns.items.prospectusPrimaryIndexYearReturns Prospectus primary index year Returns by Calendar Year.
 * @apiSuccess {String} summary.calendarYearTotalReturns.bestFitIndex Market index showing the highest correlation with a fund over the most recent 36 months, as measured by the highest R-squared.
 * @apiSuccess {String} summary.calendarYearTotalReturns.lastUpdatedDate Date information(Monthly) was last updated.
 * @apiSuccess {String} summary.calendarYearTotalReturns.dailyLastUpdatedDate Date information(Daily) was last updated.
 * @apiSuccess {Object} summary.cumulativeTotalReturns
 * @apiSuccess {Object[]} summary.cumulativeTotalReturns.items
 * @apiSuccess {Number} summary.cumulativeTotalReturns.items.period The period of return data.
 * @apiSuccess {Number} summary.cumulativeTotalReturns.items.totalReturn Fund Returns(Monthly) by cumulative.
 * @apiSuccess {Number} summary.cumulativeTotalReturns.items.totalDailyReturn Fund Returns(Daily) by cumulative.
 * @apiSuccess {Number} summary.cumulativeTotalReturns.items.bestFitIndexReturn Best-Fit Index Returns by cumulative.
 * @apiSuccess {Number} summary.cumulativeTotalReturns.items.categoryReturn Category/Asset Class Returns(Monthly) by cumulative.
 * @apiSuccess {Number} summary.cumulativeTotalReturns.items.categoryDailyReturn Category/Asset Class Returns(Daily) by cumulative.
 * @apiSuccess {Object[]} summary.calendarYearTotalReturns.items.prospectusPrimaryIndexYearReturns Prospectus primary index Returns by cumulative.
 * @apiSuccess {String} summary.cumulativeTotalReturns.bestFitIndex Market index showing the highest correlation with a fund over the most recent 36 months, as measured by the highest R-squared.
 * @apiSuccess {String} summary.cumulativeTotalReturns.lastUpdatedDate Date information(Monthly) was last updated.
 * @apiSuccess {String} summary.cumulativeTotalReturns.dailyLastUpdatedDate Date information(Daily) was last updated.
 * @apiSuccess {Object} summary.profile
 * @apiSuccess {String} summary.profile.name The name of fund.
 * @apiSuccess {String} summary.profile.bestFitIndex Market index showing the highest correlation with a fund over the most recent 36 months, as measured by the highest R-squared.
 * @apiSuccess {String} summary.profile.bestFitIndexCode The best fit index code.
 * @apiSuccess {String} summary.profile.categoryCode 10 character code assigned to each Morningstar Category.
 * @apiSuccess {String} summary.profile.categoryName Mutual Fund Category.
 * @apiSuccess {String} summary.profile.categoryLevel1Code Level 1 category code.
 * @apiSuccess {String} summary.profile.categoryLevel1Name Level 1 category name.
 * @apiSuccess {String} summary.profile.familyName Fund House Name.
 * @apiSuccess {String} summary.profile.familyCode Fund House Code.
 * @apiSuccess {String} summary.profile.advisor Mutual Fund Advisor (or Sponsor).
 * @apiSuccess {String} summary.profile.subAdvisor Mutual Fund Sub-advisor(s).
 * @apiSuccess {String} summary.profile.inceptionDate Fund Inception Date.
 * @apiSuccess {String} summary.profile.investmentObjectiveAndStrategy Description of fund objective and strategy as provided by Morningstar.
 * @apiSuccess {String} summary.profile.hhhhCategoryCode hhhh Fund Category Code. 
 * @apiSuccess {String} summary.profile.hhhhCategoryName hhhh Fund Category Name.
 * @apiSuccess {String} summary.profile.dividendYield The dividend yield.
 * @apiSuccess {String} summary.profile.distributionFrequency Interval at which reqular dividends are distributed.
 * @apiSuccess {String} summary.profile.riskLvlCde The risk level code.
 * @apiSuccess {String} summary.profile.currency The currency.
 * @apiSuccess {Number} summary.profile.expenseRatio The Expense Ratio.
 * @apiSuccess {Number} summary.profile.marginRatio The Margin Ratio.
 * @apiSuccess {Number} summary.profile.initialCharge The Initial Charge Upon Subscription.
 * @apiSuccess {Number} summary.profile.annualManagementFee The Annual Management Fee.
 * @apiSuccess {Number} summary.profile.assetsUnderManagement Fund size.
 * @apiSuccess {String} summary.profile.assetsUnderManagementCurrencyCode Assets under management currencyCode.
 * @apiSuccess {String} summary.profile.topPerformersIndicator Top Performers Indicator.
 * @apiSuccess {String} summary.profile.topSellProdIndex hhhh BEST SELLER Indicator.
 * @apiSuccess {String} summary.profile.investmentRegionCode Investment Region Code.
 * @apiSuccess {String} summary.profile.investmentRegionName Investment Region Name.
 * @apiSuccess {String[]} summary.profile.channelRestrictList Channel restrict products.
 * @apiSuccess {String} summary.profile.fundClassCde Fund Class Code.
 * @apiSuccess {String} summary.profile.amcmIndicator AMCM Authorize Indicator.
 * @apiSuccess {String} summary.profile.nextDealDate Next Deal Date.
 * @apiSuccess {String} summary.profile.surveyedFundNetAssetsDate Fund Size Date.Format:yyyy-MM-dd
 * @apiSuccess {String} summary.profile.dayEndBidOfferPricesDate Day End Bid Offer Prices Date.format:yyyy-MM-dd
 * @apiSuccess {String} summary.profile.dayEndNAVDate Day End NAV Date.format:yyyy-MM-dd
 * @apiSuccess {String} summary.profile.riskFreeRateName MPT Risk Free Rate Name.
 * @apiSuccess {String} summary.profile.relativeRiskMeasuresIndexName Relative Risk Measures Index Name.
 * @apiSuccess {String} summary.profile.prodStatCde Product State Code.
 * @apiSuccess {String} summary.profile.allowBuy Indicates whether the product is allowed to be bought.
 * @apiSuccess {String} summary.profile.allowSell Indicates whether the product is allowed to be sold.
 * @apiSuccess {String} summary.profile.allowSwInProdInd Indicates whether the product is allowed to be switchin.
 * @apiSuccess {String} summary.profile.allowSwOutProdInd Indicates whether the product is allowed to be switchout.
 * @apiSuccess {String} summary.profile.allowSellMipProdInd Only for Fund. Support RSP Indicator.
 * @apiSuccess {String} summary.profile.annualReportDate Fees and Expense Annual Report Date. Format:yyyy-MM-dd
 * @apiSuccess {String} summary.profile.piFundInd Private Fund(PI Fund) Indicator.
 * @apiSuccess {String} summary.profile.deAuthFundInd De-Authorized Fund Indicator.
 * @apiSuccess {String} summary.profile.islamProInd islamic product indicator.
 * @apiSuccess {String} summary.profile.epfAppointedFunds EPF Appointed Fund。
 * @apiSuccess {String} summary.profile.restrInvstrProdInd Restricted invester product indicator.
 * @apiSuccess {Object} summary.holdingDetails
 * @apiSuccess {String} summary.holdingDetails.totalNetAssets Total asset base of the fund, net of fees and expenses. 
 * @apiSuccess {String} summary.holdingDetails.totalNetAssetsCurrencyCode The currency code of TotalNet Asset.
 * @apiSuccess {Number} summary.holdingDetails.annualPortfolioTurnover Yearly portfolio turnover.
 * @apiSuccess {String} summary.holdingDetails.equityStyle Morningstar Equity Style Box, a 9-square grid that graphically represents the fund's equity investment style.
 * @apiSuccess {String} summary.holdingDetails.fixedIncomeStyle  (label: Credit Quality/Duration)Morningstar Fixed Income Style Box , a 9-square grid that graphically represents the fund's fixed income investment style , Credit Quality/ Duration = Overall credit quality and sensitivity of price to interest rate changes.
 * @apiSuccess {Number} summary.holdingDetails.sharesOutstanding Shares outstanding.
 * @apiSuccess {Number} summary.holdingDetails.netAssetValue ETF's per share value.
 * @apiSuccess {String} summary.holdingDetails.netAssetValueCurrencyCode The currency code of Net Asset Value.
 * @apiSuccess {String} summary.holdingDetails.premiumDiscountToNAV Percentage at which market value is above or below NAV.
 * @apiSuccess {Number} summary.holdingDetails.priceEarningsRatio Compare price to earning per share.
 * @apiSuccess {Number} summary.holdingDetails.dividendYield Annual dividend payment relative to share price.
 * @apiSuccess {String} summary.holdingDetails.dividendPerShare Dividend payment.
 * @apiSuccess {String} summary.holdingDetails.dividendPerShareCurrency The currency code of dividend per share.
 * @apiSuccess {String} summary.holdingDetails.dividendPerShareCurrencyCode The currency code of dividend per share.
 * @apiSuccess {String} summary.holdingDetails.exDividendDate Last ex-dividend date.format: yyyy-MM-dd
 * @apiSuccess {String} summary.holdingDetails.dividendPaymentDate Dividend payment date.format: yyyy-MM-dd
 * @apiSuccess {Number} summary.holdingDetails.beta ETF's volatility (systemic risk) relative to the overall market.
 * @apiSuccess {String} summary.holdingDetails.taxAdjustedRating Tax-Adjusted rating.
 * @apiSuccess {String} summary.holdingDetails.lastUpdatedDate Date data were last updated.
 * @apiSuccess {Object} summary.toNewInvestors
 * @apiSuccess {Number} summary.toNewInvestors.minInitInvst Minimum initial investment required.
 * @apiSuccess {String} summary.toNewInvestors.minInitInvstCurrencyCode Minimum initial investment required currency code.
 * @apiSuccess {Number} summary.toNewInvestors.minSubqInvst Minimum subsequent investment required.
 * @apiSuccess {String} summary.toNewInvestors.minSubqInvstCurrencyCode Minimum subsequent investment required currency code.
 * @apiSuccess {Number} summary.toNewInvestors.minInitRRSPInvst Minimum initial RRSP investment required.
 * @apiSuccess {String} summary.toNewInvestors.minInitRRSPInvstCurrencyCode Minimum initial RRSP investment required currency code. 
 * @apiSuccess {Number} summary.toNewInvestors.minSubqRRSPInvst Minimum subsequent RRSP investment required.
 * @apiSuccess {String} summary.toNewInvestors.minSubqRRSPInvstCurrencyCode Minimum subsequent RRSP investment required currency code.
 * @apiSuccess {Number} summary.toNewInvestors.hhhhMinInitInvst Minimum initial investment required by hhhh define.
 * @apiSuccess {String} summary.toNewInvestors.hhhhMinInitInvstCurrencyCode Minimum initial investment required currency code by hhhh define.
 * @apiSuccess {Number} summary.toNewInvestors.hhhhMinSubqInvst Minimum subsequent investment required by define.
 * @apiSuccess {String} summary.toNewInvestors.hhhhMinSubqInvstCurrencyCode Minimum subsequent investment required currency code by define.
 * @apiSuccess {String} summary.toNewInvestors.purchaseCurId Purchase Currency Id.
 * @apiSuccess {String} summary.toNewInvestors.minInitUnit Initial Investment(s) Unit / Initial RRSP investment(s) Unit.
 * @apiSuccess {String} summary.toNewInvestors.minSubqUnit Subsequent Investment(s) Unit / Subsequent RRSP investment(s) Unit.
 * @apiSuccess {Boolean} summary.toNewInvestors.indicator Open / closed indicator for the holding.
 * @apiSuccess {Object} summary.feesAndExpenses
 * @apiSuccess {Number} summary.feesAndExpenses.maximumInitialSalesFees Maximum initial Sales Fees. 
 * @apiSuccess {Number} summary.feesAndExpenses.maximumDeferredSalesFees Maximum deferred Sales Fees.
 * @apiSuccess {Number} summary.feesAndExpenses.actualManagementFee Management and administrative fees paid over the fund's prior fiscal year.
 * @apiSuccess {String} summary.feesAndExpenses.prospectusCustodianUnit prospectus custodian unit.
 * @apiSuccess {Number} summary.feesAndExpenses.prospectusCustodianFee prospectus Custodian fee.
 * @apiSuccess {Number} summary.feesAndExpenses.actualMER Actual management expense ratio.
 * @apiSuccess {String} summary.feesAndExpenses.loadType Sales commission charged to holders of fund units.
 * @apiSuccess {String} summary.feesAndExpenses.lastUpdatedDate Date data were last updated.
 * @apiSuccess {Number} summary.feesAndExpenses.onGoingChargeFigure The Ongoing Charge represents the costs you can reasonably expect to pay as an investor from one year to the next, under normal circumstances.
 * @apiSuccess {Object} summary.morningstarRatings
 * @apiSuccess {String} summary.morningstarRatings.morningstarRatingOverall Overall Morningstar Rating of the fund, Morningstar Rating = risk-adjusted rating (or star rating).
 * @apiSuccess {Number} summary.morningstarRatings.morningstarRating3Yr 3-Year Morningstar Rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarRating5Yr 5-Year Morningstar Rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarRating10Yr 10-Year Morningstar Rating
 * @apiSuccess {String} summary.morningstarRatings.morningstarTaxAdjustedRatingOverall Overall Morningstar Tax-Adjusted Rating, Tax-Adjusted Ranking = Relates risk-adjusted and tax-adjusted performances of the fund to those of its peers over a 3-, 5- and 10-year period
 * @apiSuccess {Number} summary.morningstarRatings.morningstarTaxAdjustedRating3Yr 3-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarTaxAdjustedRating5Yr 5-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} summary.morningstarRatings.morningstarTaxAdjustedRating10Yr 10-Year Morningstar Tax-Adjusted rating.
 * @apiSuccess {Number} summary.morningstarRatings.lastUpdatedDate Date data were last updated.
 * @apiSuccess {Object} summary.mgmtAndContactInfo
 * @apiSuccess {String} summary.mgmtAndContactInfo.companyName Company Name.
 * @apiSuccess {String} summary.mgmtAndContactInfo.address The address.
 * @apiSuccess {String} summary.mgmtAndContactInfo.city The city.
 * @apiSuccess {String} summary.mgmtAndContactInfo.province The province.
 * @apiSuccess {String} summary.mgmtAndContactInfo.country The country.
 * @apiSuccess {String} summary.mgmtAndContactInfo.postalCode The postalCode.
 * @apiSuccess {String} summary.mgmtAndContactInfo.telephoneNo Telephone No.
 * @apiSuccess {String} summary.mgmtAndContactInfo.faxNo Fax No.
 * @apiSuccess {String} summary.mgmtAndContactInfo.website Website address.
 * @apiSuccess {Object[]} summary.mgmtAndContactInfo.mgmtInfos
 * @apiSuccess {String} summary.mgmtAndContactInfo.mgmtInfos.managerName Name of current portfolio manager(s).
 * @apiSuccess {String} summary.mgmtAndContactInfo.mgmtInfos.startDate Date current manager started managing the fund.
 * @apiSuccess {Object} summary.investmentStrategy
 * @apiSuccess {Number} summary.investmentStrategy.investmentStyle Investment style.
 * @apiSuccess {Number} summary.investmentStrategy.interestRateSensitivity Interest rate sensitivity.
 * @apiSuccess {Number} summary.investmentStrategy.assetAllocBondNet Asset alloc bond net.
 * @apiSuccess {Number} summary.investmentStrategy.preferredStockNet Preferred stock net.
 * @apiSuccess {Number} summary.investmentStrategy.assetAllocCashNet Asset alloc cash net.
 * @apiSuccess {Number} summary.investmentStrategy.otherNet other net.
 * @apiSuccess {Number} summary.investmentStrategy.assetAllocEquityNet Asset alloc equityNet.
 * @apiSuccess {Object} summary.yieldAndCredit
 * @apiSuccess {Number} summary.yieldAndCredit.averageCurrencyYield Average currency yield.
 * @apiSuccess {Number} summary.yieldAndCredit.averageYieldToMaturity Average yield to maturity.
 * @apiSuccess {Number} summary.yieldAndCredit.averageDuration Average duration.
 * @apiSuccess {String} summary.yieldAndCredit.lastUpdatedDate Date data were last updated.
 * @apiSuccess {Number} summary.yieldAndCredit.creditQualA Credit quality A.
 * @apiSuccess {Number} summary.yieldAndCredit.creditQualAA Credit quality AA.
 * @apiSuccess {Number} summary.yieldAndCredit.creditQualAAA Credit quality AAA.
 * @apiSuccess {Number} summary.yieldAndCredit.creditQualB Credit quality B.
 * @apiSuccess {Number} summary.yieldAndCredit.creditQualBB Credit quality BB.
 * @apiSuccess {Number} summary.yieldAndCredit.creditQualBBB Credit quality BBB.
 * @apiSuccess {Number} summary.yieldAndCredit.creditQualBelowB Credit quality BelowB.
 * @apiSuccess {Number} summary.yieldAndCredit.creditQualNotRated Credit quality Not Rated.
 * @apiSuccess {String} summary.yieldAndCredit.creditQualDate Credit quality Date.
 * @apiSuccess {Object} summary.rating
 * @apiSuccess {Number} summary.rating.rank1Yr 1 year quartile ranking.
 * @apiSuccess {Number} summary.rating.rank3Yr 3 year quartile ranking.
 * @apiSuccess {Number} summary.rating.rank5Yr 5 year quartile ranking.
 * @apiSuccess {Number} summary.rating.rank10Yr 10 year quartile ranking.
 * @apiSuccess {Number} summary.rating.averageCreditQuality Average credit quality.
 * @apiSuccess {String} summary.rating.averageCreditQualityName Average credit quality Name.
 * @apiSuccess {String} summary.rating.ratingDate Rating date data were last updated.
 * @apiSuccess {Object[]} summary.prodAltNumSegs
 * @apiSuccess {String} summary.prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the products. It indicates whether this alternative products code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} summary.prodAltNumSegs.prodAltNum Denotes the alternative number of the products. It indicates the alternate code of a particular products code, which is used to map the products code used in the market to the internal code used in the system.
 * @apiSuccess {Object[]} summary.risk
 * @apiSuccess {Object} summary.risks.yearrisk
 * @apiSuccess {Number} summary.risks.yearrisk.year Denote how many year for this risks. 
 * @apiSuccess {Number} summary.risks.yearrisk.beta x-year volatility (systemic risks) of fund relative to the overall market.
 * @apiSuccess {Number} summary.risks.yearrisk.stdDev x-year volatility (systemic risks) of portfolio.
 * @apiSuccess {Number} summary.risks.yearrisk.alpha x-year excess risks-adjusted return of portfolio relative to the return of a benchmark index.
 * @apiSuccess {Number} summary.risks.yearrisk.sharpeRatio Measure of x-year risks-adjusted performance of the portfolio.
 * @apiSuccess {Number} summary.risks.yearrisk.rSquared x-year correlation of a portfolio's movements to that of a benchmark index.
 * @apiSuccess {Number} summary.risks.yearrisk.totalReturn Annualised return.
 * @apiSuccess {String} summary.risks.yearrisk.endDate Date data were last updated.
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 *{
 *	"summary": {
 *		"bid": null,
 *		"weekRangeLow": 1278,
 *		"weekRangeHigh": 1410,
 *		"offer": null,
 *		"weekRangeCurrency": "SEK",
 *		"calendarYearTotalReturns": {
 *			"items": [{
 *				"year": "YTD",
 *				"fundCalendarYearReturn": 3.79464,
 *				"fundCalendarYearDailyReturn": 3.42262,
 *				"fundStubYearEndReturn": null,
 *				"stubYearEndReturnIndicator": null,
 *				"bestFitIndexCalendarYearReturn": 3.58606,
 *				"categoryCalendarYearReturn": 6.85572,
 *				"categoryCalendarYearDailyReturn": 9.72236,
 *				"prospectusPrimaryIndexYearReturns": [0.66891]
 *			},
 *			{
 *				"year": "2017",
 *				"fundCalendarYearReturn": 15.61086,
 *				"fundCalendarYearDailyReturn": null,
 *				"fundStubYearEndReturn": null,
 *				"stubYearEndReturnIndicator": null,
 *				"bestFitIndexCalendarYearReturn": 9.04542,
 *				"categoryCalendarYearReturn": 13.59281,
 *				"categoryCalendarYearDailyReturn": null,
 *				"prospectusPrimaryIndexYearReturns": [11.23977]
 *			},
 *			{
 *				"year": "2016",
 *				"fundCalendarYearReturn": 32.05151,
 *				"fundCalendarYearDailyReturn": null,
 *				"fundStubYearEndReturn": null,
 *				"stubYearEndReturnIndicator": null,
 *				"bestFitIndexCalendarYearReturn": 8.42734,
 *				"categoryCalendarYearReturn": 17.40751,
 *				"categoryCalendarYearDailyReturn": null,
 *				"prospectusPrimaryIndexYearReturns": [0.80004]
 *			},
 *			{
 *				"year": "2015",
 *				"fundCalendarYearReturn": 4.41084,
 *				"fundCalendarYearDailyReturn": null,
 *				"fundStubYearEndReturn": null,
 *				"stubYearEndReturnIndicator": null,
 *				"bestFitIndexCalendarYearReturn": 6.80344,
 *				"categoryCalendarYearReturn": 21.5799,
 *				"categoryCalendarYearDailyReturn": null,
 *				"prospectusPrimaryIndexYearReturns": [15.03947]
 *			},
 *			{
 *				"year": "2014",
 *				"fundCalendarYearReturn": 6.29163,
 *				"fundCalendarYearDailyReturn": null,
 *				"fundStubYearEndReturn": null,
 *				"stubYearEndReturnIndicator": null,
 *				"bestFitIndexCalendarYearReturn": 13.79354,
 *				"categoryCalendarYearReturn": 12.42291,
 *				"categoryCalendarYearDailyReturn": null,
 *				"prospectusPrimaryIndexYearReturns": [8.9806]
 *			},
 *			{
 *				"year": "2013",
 *				"fundCalendarYearReturn": 37.32915,
 *				"fundCalendarYearDailyReturn": null,
 *				"fundStubYearEndReturn": null,
 *				"stubYearEndReturnIndicator": null,
 *				"bestFitIndexCalendarYearReturn": 24.37251,
 *				"categoryCalendarYearReturn": 29.74868,
 *				"categoryCalendarYearDailyReturn": null,
 *				"prospectusPrimaryIndexYearReturns": [21.67017]
 *			}],
 *			"bestFitIndex": "MSCI Nordic Countries Small Cap NR USD",
 *			"lastUpdatedDate": "2018-07-31",
 *			"dailyLastUpdatedDate": "2018-08-01"
 *		},
 *		"cumulativeTotalReturns": {
 *			"items": [{
 *				"period": "1M",
 *				"totalReturn": 2.95203,
 *				"totalDailyReturn": 2.58303,
 *				"bestFitIndexReturn": 0.49095,
 *				"categoryReturn": -0.99099,
 *				"categoryDailyReturn": 1.8594,
 *				"prospectusPrimaryIndexReturns": [-0.8608]
 *			},
 *			{
 *				"period": "3M",
 *				"totalReturn": 1.82482,
 *				"totalDailyReturn": 2.20588,
 *				"bestFitIndexReturn": 3.72386,
 *				"categoryReturn": 7.32844,
 *				"categoryDailyReturn": 3.04371,
 *				"prospectusPrimaryIndexReturns": [2.68283]
 *			},
 *			{
 *				"period": "6M",
 *				"totalReturn": 2.1978,
 *				"totalDailyReturn": 1.98092,
 *				"bestFitIndexReturn": 3.58606,
 *				"categoryReturn": 6.85572,
 *				"categoryDailyReturn": 7.0067,
 *				"prospectusPrimaryIndexReturns": [0.66891]
 *			},
 *			{
 *				"period": "1Y",
 *				"totalReturn": 7.64787,
 *				"totalDailyReturn": 6.59509,
 *				"bestFitIndexReturn": 2.35182,
 *				"categoryReturn": 8.01369,
 *				"categoryDailyReturn": 13.88887,
 *				"prospectusPrimaryIndexReturns": [1.19412]
 *			},
 *			{
 *				"period": "3Y",
 *				"totalReturn": 13.14481,
 *				"totalDailyReturn": 13.00947,
 *				"bestFitIndexReturn": 6.31228,
 *				"categoryReturn": 14.78178,
 *				"categoryDailyReturn": 14.78736,
 *				"prospectusPrimaryIndexReturns": [3.92821]
 *			},
 *			{
 *				"period": "5Y",
 *				"totalReturn": 15.25257,
 *				"totalDailyReturn": 14.85582,
 *				"bestFitIndexReturn": 11.46942,
 *				"categoryReturn": 18.06122,
 *				"categoryDailyReturn": 17.29262,
 *				"prospectusPrimaryIndexReturns": [10.61468]
 *			},
 *			{
 *				"period": "10Y",
 *				"totalReturn": 9.53216,
 *				"totalDailyReturn": 9.67185,
 *				"bestFitIndexReturn": 10.12969,
 *				"categoryReturn": 11.65014,
 *				"categoryDailyReturn": 12.80597,
 *				"prospectusPrimaryIndexReturns": [8.00477]
 *			}],
 *			"bestFitIndex": "MSCI Nordic Countries Small Cap NR USD",
 *			"lastUpdatedDate": "2018-08-01",
 *			"dailyLastUpdatedDate": "2018-07-31"
 *		},
 *		"holdingDetails": {
 *			"totalNetAssets": "2954466533.04",
 *			"totalNetAssetsCurrencyCode": null,
 *			"annualPortfolioTurnover": null,
 *			"equityStyle": "7",
 *			"fixedIncomeStyle": null,
 *			"sharesOutstanding": 2136329.87,
 *			"netAssetValue": 1390,
 *			"netAssetValueCurrencyCode": null,
 *			"premiumDiscountToNAV": null,
 *			"priceEarningsRatio": 0.06555,
 *			"dividendYield": 0.44059,
 *			"dividendPerShare": "6.146300000",
 *			"dividendPerShareCurrency": "SEK",
 *			"dividendPerShareCurrencyCode": "Swedish Krona",
 *			"exDividendDate": "2017-08-01",
 *			"dividendPaymentDate": null,
 *			"beta": 0.79,
 *			"taxAdjustedRating": null,
 *			"lastUpdatedDate": "2018-06-30"
 *		},
 *		"toNewInvestors": {
 *			"minInitInvst": null,
 *			"minInitInvstCurrencyCode": "SEK",
 *			"minSubqInvst": null,
 *			"minSubqInvstCurrencyCode": "SEK",
 *			"minInitRRSPInvst": 1000,
 *			"minInitRRSPInvstCurrencyCode": "SEK",
 *			"minSubqRRSPInvst": 1000,
 *			"minSubqRRSPInvstCurrencyCode": "SEK",
 *			"hhhhMinInitInvst": 10000,
 *			"hhhhMinInitInvstCurrencyCode": "SEK",
 *			"hhhhMinSubqInvst": 10000,
 *			"hhhhMinSubqInvstCurrencyCode": "SEK",
 *			"purchaseCurId": "SEK",
 *			"minInitUnit": null,
 *			"minSubqUnit": null,
 *			"indicator": true
 *		},
 *		"feesAndExpenses": {
 *			"maximumInitialSalesFees": 5.25,
 *			"maximumDeferredSalesFees": null,
 *			"actualManagementFee": 1.5,
 *			"prospectusCustodianUnit": "Percentage",
 *			"prospectusCustodianFee": 0.35,
 *			"actualMER": null,
 *			"loadType": null,
 *			"lastUpdatedDate": "2018-01-16",
 *			"onGoingChargeFigure": 1.96
 *		},
 *		"morningstarRatings": {
 *			"morningstarRatingOverall": "2",
 *			"morningstarRating3Yr": 3,
 *			"morningstarRating5Yr": 2,
 *			"morningstarRating10Yr": 2,
 *			"morningstarTaxAdjustedRatingOverall": null,
 *			"morningstarTaxAdjustedRating3Yr": null,
 *			"morningstarTaxAdjustedRating5Yr": null,
 *			"morningstarTaxAdjustedRating10Yr": null,
 *			"lastUpdatedDate": "2018-06-30"
 *		},
 *		"mgmtAndContactInfo": {
 *			"companyName": "FIL Investment Management (Lux) S.A",
 *			"address": "Fidelity Investments Distributors,42 Crow Lane",
 *			"city": "Pembroke",
 *			"province": "",
 *			"country": null,
 *			"postalCode": "HM19",
 *			"telephoneNo": "+ 352 250 404 2400",
 *			"faxNo": "+ 352 26 38 39 38",
 *			"website": "www.fidelity-international.com",
 *			"mgmtInfos": [{
 *				"managerName": "Bertrand Puiffe",
 *				"startDate": "2011-08-01"
 *			}]
 *		},
 *		"investmentStrategy": {
 *			"investmentStyle": "7",
 *			"interestRateSensitivity": null,
 *			"assetAllocBondNet": 0,
 *			"preferredStockNet": 0,
 *			"assetAllocCashNet": -0.17951,
 *			"otherNet": 0,
 *			"assetAllocEquityNet": 100.17951
 *		},
 *		"yieldAndCredit": {
 *			"averageCurrentYield": null,
 *			"averageYieldToMaturity": null,
 *			"averageDuration": null,
 *			"lastUpdatedDate": null,
 *			"creditQualA": null,
 *			"creditQualAA": null,
 *			"creditQualAAA": null,
 *			"creditQualB": null,
 *			"creditQualBB": null,
 *			"creditQualBBB": null,
 *			"creditQualBelowB": null,
 *			"creditQualNotRated": null,
 *			"creditQualDate": null
 *		},
 *		"rating": {
 *			"averageCreditQualityName": null,
 *			"averageCreditQuality": null,
 *			"rank1Yr": 4,
 *			"rank3Yr": 3,
 *			"rank5Yr": 3,
 *			"rank10Yr": 4,
 *			"ratingDate": null
 *		},
 *		"risk": [{
 *			"yearRisk": {
 *				"year": 1,
 *				"beta": 0.72,
 *				"stdDev": 8.453,
 *				"alpha": -6.87,
 *				"sharpeRatio": 0.62,
 *				"rSquared": 60.19,
 *				"totalReturn": 4.48094,
 *				"endDate": "2018-06-30"
 *			}
 *		},
 *		{
 *			"yearRisk": {
 *				"year": 3,
 *				"beta": 0.79,
 *				"stdDev": 12.146,
 *				"alpha": 0.77,
 *				"sharpeRatio": 1.116,
 *				"rSquared": 60.56,
 *				"totalReturn": 13.05475,
 *				"endDate": "2018-06-30"
 *			}
 *		},
 *		{
 *			"yearRisk": {
 *				"year": 5,
 *				"beta": 0.87,
 *				"stdDev": 12.306,
 *				"alpha": -0.73,
 *				"sharpeRatio": 1.273,
 *				"rSquared": 70.24,
 *				"totalReturn": 15.87672,
 *				"endDate": "2018-06-30"
 *			}
 *		},
 *		{
 *			"yearRisk": {
 *				"year": 10,
 *				"beta": 0.78,
 *				"stdDev": 16.114,
 *				"alpha": -0.73,
 *				"sharpeRatio": 0.544,
 *				"rSquared": 86.67,
 *				"totalReturn": 8.68034,
 *				"endDate": "2018-06-30"
 *			}
 *		}],
 *		"prodAltNumSegs": [
 *			{
 *				"prodCdeAltClassCde": "F",
 *				"prodAltNum": "WAMG4"
 *			},
 *			{
 *				"prodCdeAltClassCde": "I",
 *				"prodAltNum": "LU1066050094"
 *			},
 *			{
 *				"prodCdeAltClassCde": "M",
 *				"prodAltNum": "U62607"
 *			},
 *			{
 *				"prodCdeAltClassCde": "O",
 *				"prodAltNum": "0P00013LH8"
 *			},
 *			{
 *				"prodCdeAltClassCde": "P",
 *				"prodAltNum": "U62607"
 *			},
 *			{
 *				"prodCdeAltClassCde": "T",
 *				"prodAltNum": "000000000000000000001000049978"
 *			},
 *			{
 *				"prodCdeAltClassCde": "W",
 *				"prodAltNum": "1000049978"
 *			}
 *		],
 *		"profile": {
 *			"name": "Fidelity Funds - Nordic Fund (Class A-ADIST Unit)",
 *			"bestFitIndex": "MSCI Nordic Countries Small Cap NR USD",
 *			"bestFitIndexCode": "F00000T62J",
 *			"categoryCode": "EUCA000920",
 *			"categoryName": "Nordic Small/Mid-Cap Equity",
 *			"categoryLevel1Code": "EDDM",
 *			"categoryLevel1Name": "Equity Developed Market",
 *			"familyName": "Fidelity Investment Managers",
 *			"familyCode": "FIDEL",
 *			"advisor": "FIL Investment Management (Lux) S.A",
 *			"subAdvisor": null,
 *			"inceptionDate": "1990-10-01",
 *			"investmentObjectiveAndStrategy": "The fund aims to provide long-term capital growth from investment primarily in the stockmarkets of Norway, Sweden, Denmark and Finland. The emphasis of the Fund is on a stockpicking approach, seeking to identify companies which investors may value more highly in the future once factors such as their earnings prospects are more fully appreciated.",
 *			"hhhhCategoryCode": "EE",
 *			"hhhhCategoryName": "European Equity",
 *			"dividendYield": 0.4536,
 *			"distributionFrequency": "Annually",
 *			"riskLvlCde": "5",
 *			"currency": "SEK",
 *			"expenseRatio": null,
 *			"marginRatio": null,
 *			"initialCharge": 3,
 *			"annualManagementFee": 0,
 *			"assetsUnderManagement": 687536798,
 *			"assetsUnderManagementCurrencyCode": "USD",
 *			"topPerformersIndicator": null,
 *			"topSellProdIndex": "N",
 *			"investmentRegionCode": "EU",
 *			"investmentRegionName": "Europe",
 *			"channelRestrictList": ["SRBPI"],
 *			"fundClassCde": "WIC",
 *			"amcmIndicator": "Y",
 *			"nextDealDate": "2018-08-13",
 *			"surveyedFundNetAssetsDate": null,
 *			"riskFreeRateName": null,,
 *			"relativeRiskMeasuresIndexName": null,
 *			"dayEndBidOfferPricesDate": null,
 *			"dayEndNAVDate": null,
 *			"prodStatCde": "A",
 *			"allowBuy": "Y",
 *			"allowSell": "Y",
 *			"allowSwInProdInd": "Y",
 *			"allowSwOutProdInd": "Y",
 *			"allowSellMipProdInd": "Y",
 *			"annualReportDate": "2017-08-31",
 *			"piFundInd": "N",
 *			"deAuthFundInd": "N"
 *		}
 *	}
 *}
 * 
 * @apiUse ErrorMsgResponse
 */