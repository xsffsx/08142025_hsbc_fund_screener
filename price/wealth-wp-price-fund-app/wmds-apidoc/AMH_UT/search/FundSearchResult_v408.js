/**
 * @api {get} /v4.0/fundSearchResult Fund Search Result
 * @apiName Fund Search Result
 * @apiGroup Search
 * @apiVersion 4.0.8
 * 
 * @apiUse QuoteHeaderParam_v402
 * 
 * @apiParam {String} productType Product Type. If this field is null, then the productType require to define in productKey.
 * @apiParam {Object[]} [detailedCriterias] Detailed Criteria for search</br>
 * @apiParam {String} detailedCriterias.criteriaKey <p>possible values as below:</p>
 * 												  	<ul>
 * 									              	<li>isrBndNme</li>
 * 									              	<li>fundHouseCde</li>
 * 									             	<li>allowSellMipProdInd</li>
 * 									              	<li>productKey</li>
 *  											  	<li>switchOutFund</li>
 *  											  	<li>exchange</li>
 * 												  	</ul>
 * @apiParam {String} detailedCriterias.criteriaValue <p>different value according to different criteriaKey such as : </p>
 * 												 	 <ul>
 * 									              	<li>013051CZ8 for isrBndNme</li>
 * 									              	<li>HIT for fundHouseCde</li>
 * 									              	<li>M:00005:HK:SEC for productKey</li>
 * 									              	<li>M:FJP:SG:UT for switchOutFund</li>
 * 								                  	</ul>
 * @apiParam {String = "gt","ge","eq","lt","le","ne","in"} detailedCriterias.operator
 * @apiParam {Object[]} [rangeCriterias] Range Criteria for search</br>
 * @apiParam {String} rangeCriterias.criteriaKey
 * @apiParam {Object[]} rangeCriterias.criteriaValues
 * @apiParam {String = "gt","ge","eq","lt","le","ne","in"} rangeCriterias.criteriaValues.minOperator
 * @apiParam {Number} rangeCriterias.criteriaValues.minCriteriaLimit The min criteria limit.
 * @apiParam {String = "gt","ge","eq","lt","le","ne","in"} rangeCriterias.criteriaValues.maxOperator
 * @apiParam {Number} rangeCriterias.criteriaValues.maxCriteriaLimit The min criteria limit.
 * @apiParam {Object[]} [productKeys] 
 * @apiParam {String} productKey.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKey.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKey.market The investment market of the product.</br>
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
 * @apiParam {String} productKey.productType Product Type. If this field is null, then the productType require to define in productKey.
 * @apiParam {String} [sortBy] Sort result by.
 * @apiParam {String} [sortOrder] Sorting order.
 * @apiParam {Number} [numberOfRecords] Record amount in each page.
 * @apiParam {Number} [startDetail] Key of first record returned on this page.
 * @apiParam {Number} [endDetail] Key of last record returned on this page.
 * @apiParam {String} [currencyCode] Currency Code.
 * @apiParam {String} [entityTimezone] Used to format time "EST".
 * @apiParam {Boolean} returnOnlyNumberOfMatches Indicator to indentify return number or detail of matched records.
 * 
 * @apiParamExample Request:
 *	{
 *		"productType": "UT",
 *		"returnOnlyNumberOfMatches": false,
 *		"numberOfRecords": 1,
 *		"startDetail": "1",
 *		"endDetail": "1",
 *		"productKeys": [{
 *			"prodCdeAltClassCde": "M",
 *			"prodAltNum": "540003",
 *			"market": "CN",
 *			"productType": "UT"
 *		}]
 *	}
 * 
 * @apiSuccess {Object} pagination
 * @apiSuccess {Number} pagination.totalNumberOfRecords Record amount which maches request criteria.
 * @apiSuccess {Number} pagination.numberOfRecords Record amount in each page.
 * @apiSuccess {Number} pagination.startDetail Key of first record returned on this page.
 * @apiSuccess {Number} pagination.endDetail Key of last record returned on this page.
 * @apiSuccess {Object[]} products
 * @apiSuccess {Object} products.header
 * @apiSuccess {String} products.header.name Mutual Fund / ETF Name.
 * @apiSuccess {String} products.header.market The market.
 * @apiSuccess {String} products.header.productType products Type. If this field is null, then the productsType require to define in productsKey. 
 * @apiSuccess {String} products.header.currency Currency of this products.
 * @apiSuccess {String} products.header.categoryCode Canadian Mutual Fund / ETF Category code.
 * @apiSuccess {String} products.header.categoryName Canadian Mutual Fund / ETF Category.
 * @apiSuccess {String} products.header.familyCode The family code.
 * @apiSuccess {String} products.header.familyName The family name.
 * @apiSuccess {Object[]} products.header.prodAltNumSeg
 * @apiSuccess {String} products.header.prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the products. It indicates whether this alternative products code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} products.header.prodAltNumSegs.prodAltNum Denotes the alternative number of the products. It indicates the alternate code of a particular products code, which is used to map the products code used in the market to the internal code used in the system.
 * @apiSuccess {Object} products.summary
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
 * @apiSuccess {Number} products.summary.assetsUnderManagement Assets Under Management , Market value of assets managed by the fund.
 * @apiSuccess {String} products.summary.assetsUnderManagementCurrencyCode Assets Under Management CurrencyCode.
 * @apiSuccess {Number} products.summary.totalNetAsset Total Net Assets, total asset base of the fund, net of fees and expenses.
 * @apiSuccess {String} products.summary.totalNetAssetCurrencyCode Total Net Assets CurrencyCode.
 * @apiSuccess {Number} products.summary.ratingOverall Overall Morningstar Rating of the fund, Morningstar Rating = risk-adjusted rating (or star rating).
 * @apiSuccess {Number} products.summary.mer MER , Management Expense Ratio.
 * @apiSuccess {Number} products.summary.yield1Yr Distribution Yield , Yield based on the most recent dividend distribution.
 * @apiSuccess {Number} products.summary.switchInMinAmount minimum switch-in amount for fund switch.
 * @apiSuccess {String} products.summary.switchInMinAmountCurrencyCode mininum switch-in amount currency for fund switch.
 * @apiSuccess {Number} products.summary.annualReportOngoingCharge AnnualReportOngoing Charge.
 * @apiSuccess {Number} products.summary.actualManagementFee Actual Management Fee.
 * @apiSuccess {Object} products.profile
 * @apiSuccess {String} products.profile.inceptionDate Fund Inception Date.
 * @apiSuccess {Number} products.profile.turnoverRatio Rate at which the fund replaces its holdings on an annual basis.
 * @apiSuccess {Number} products.profile.stdDev3Yr 3-year volatility (systemic risk) of the fund.
 * @apiSuccess {Number} products.profile.equityStylebox Morningstar Equity Style Box, a 9-square grid that graphically represents the fund's equity investment style.
 * @apiSuccess {Number} products.profile.expenseRatio The Expense Ratio.
 * @apiSuccess {Object} products.rating 
 * @apiSuccess {String} products.rating.morningstarRating Overall Morningstar Rating of the fund, Morningstar Rating = risk-adjusted rating (or star rating)</br>
 * @apiSuccess {Number} products.rating.taxAdjustedRating Overall Morningstar Tax-Adjusted Rating, Tax-Adjusted Ranking = Relates risk-adjusted and tax-adjusted performances of the fund to those of its peers over a 3-, 5- and 10-year period
 * @apiSuccess {Number} products.rating.averageCreditQuality Morningstar's assessment of overall credit quality of the portfolio (e.g. 'BBB')
 * @apiSuccess {Number} products.rating.rank1Yr Fund's 1-year total-return quartile rank relative to all funds that have the same Morningstar category.
 * @apiSuccess {Object} products.performance
 * @apiSuccess {Object} products.performance.annualizedReturns
 * @apiSuccess {Number} products.performance.annualizedReturns.return1day Trailing total return of the fund for 1 day, expressed as a percentage.
 * @apiSuccess {Number} products.performance.annualizedReturns.return1Mth Trailing total return of the fund for 1 month, expressed as a percentage.
 * @apiSuccess {Number} products.performance.annualizedReturns.return3Mth Trailing total return of the fund for 3 months, expressed as a percentage.
 * @apiSuccess {Number} products.performance.annualizedReturns.return6Mth Trailing total return of the fund for 6 months, expressed as a percentage.
 * @apiSuccess {Number} products.performance.annualizedReturns.return1Yr Trailing total return of the fund for 1 year, expressed as a percentage.
 * @apiSuccess {Number} products.performance.annualizedReturns.return3Yr Trailing total return of the fund for 3 year, expressed as a percentage.
 * @apiSuccess {Number} products.performance.annualizedReturns.return5Yr Trailing total return of the fund for 5 year, expressed as a percentage.
 * @apiSuccess {Number} products.performance.annualizedReturns.return10Yr Trailing total return of the fund for 10 year, expressed as a percentage.
 * @apiSuccess {Number} products.performance.annualizedReturns.returnSinceInception Trailing total return of the fund since inception, expressed as a percentage.
 * @apiSuccess {String} products.performance.annualizedReturns.inceptionDate Fund Inception Date.format:yyyy-MM-dd
 * @apiSuccess {Object} products.performance.calendarReturns 
 * @apiSuccess {Number} products.performance.calendarReturns.returnYTD Year-to-date trailing total return of the mutual fund, expressed as a percentage.
 * @apiSuccess {Object[]} products.performance.calendarReturns.years
 * @apiSuccess {Number} products.performance.calendarReturns.years.yearName The year name.
 * @apiSuccess {Number} products.performance.calendarReturns.years.yearValue Return mutual fund of specific year.
 * @apiSuccess {Object[]} products.risk
 * @apiSuccess {Object} products.risks.yearrisk
 * @apiSuccess {Number} products.risks.yearrisk.year Denote how many year for this risks. 
 * @apiSuccess {Number} products.risks.yearrisk.beta x-year volatility (systemic risks) of fund relative to the overall market.
 * @apiSuccess {Number} products.risks.yearrisk.stdDev x-year volatility (systemic risks) of portfolio.
 * @apiSuccess {Number} products.risks.yearrisk.alpha x-year excess risks-adjusted return of portfolio relative to the return of a benchmark index.
 * @apiSuccess {Number} products.risks.yearrisk.sharpeRatio Measure of x-year risks-adjusted performance of the portfolio.
 * @apiSuccess {Number} products.risks.yearrisk.rSquared x-year correlation of a portfolio's movements to that of a benchmark index.
 * @apiSuccess {Object} products.holdings
 * @apiSuccess {Object} products.holdings.sector
 * @apiSuccess {Number} products.holdings.sector.basicMaterials The basic materials.
 * @apiSuccess {Number} products.holdings.sector.basicMaterialsIndicator The basic materials indicator.
 * @apiSuccess {Number} products.holdings.sector.communicationServices The communication services.
 * @apiSuccess {Number} products.holdings.sector.communicationServicesIndicator The communication services indicator.
 * @apiSuccess {Number} products.holdings.sector.consumerCyclical The consumer cyclical.
 * @apiSuccess {Number} products.holdings.sector.consumerCyclicalIndicator The consumer cyclical indicator.
 * @apiSuccess {Number} products.holdings.sector.consumerDefensive The consumer defensive.
 * @apiSuccess {Number} products.holdings.sector.consumerDefensiveIndicator The consumer defensive indicator.
 * @apiSuccess {Number} products.holdings.sector.energy The energy.
 * @apiSuccess {Number} products.holdings.sector.energyIndicator The energy indicator.
 * @apiSuccess {Number} products.holdings.sector.excluded The excluded.
 * @apiSuccess {Number} products.holdings.sector.excludedIndicator The excluded indicator.
 * @apiSuccess {Number} products.holdings.sector.financialServices The financial services. 
 * @apiSuccess {Number} products.holdings.sector.financialServicesIndicator  The financial services indicator.
 * @apiSuccess {Number} products.holdings.sector.healthcare The healthcare.
 * @apiSuccess {Number} products.holdings.sector.healthcareIndicator The healthcare indicator. 
 * @apiSuccess {Number} products.holdings.sector.industrials The industrials.
 * @apiSuccess {Number} products.holdings.sector.industrialsIndicator The industrials indicator.
 * @apiSuccess {Number} products.holdings.sector.notClassified The not classified.
 * @apiSuccess {Number} products.holdings.sector.notClassifiedIndicator The not classified indicator. 
 * @apiSuccess {Number} products.holdings.sector.others The others.
 * @apiSuccess {Number} products.holdings.sector.othersIndicator The others indicator.
 * @apiSuccess {Number} products.holdings.sector.technology The technology.
 * @apiSuccess {Number} products.holdings.sector.technologyIndicator The technology indicator.
 * @apiSuccess {Number} products.holdings.sector.utilities  The utilities.
 * @apiSuccess {Number} products.holdings.sector.utilitiesIndicator The utilities indicator.
 * @apiSuccess {Object} products.holdings.geographicRegion
 * @apiSuccess {Number} products.holdings.geographicRegion.australia The australia.
 * @apiSuccess {Number} products.holdings.geographicRegion.australiaIndicator The australia indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.austria The austria.
 * @apiSuccess {Number} products.holdings.geographicRegion.austriaIndicator The austria indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.belgium The belgium.
 * @apiSuccess {Number} products.holdings.geographicRegion.belgiumIndicator The belgium indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.brazil The brazil.
 * @apiSuccess {Number} products.holdings.geographicRegion.brazilIndicator The brazil indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.canada The canada.
 * @apiSuccess {Number} products.holdings.geographicRegion.canadaIndicator The canada indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.chile The chile. 
 * @apiSuccess {Number} products.holdings.geographicRegion.chileIndicator The chile indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.china The china.
 * @apiSuccess {Number} products.holdings.geographicRegion.chinaIndicator The china indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.colombia The colombia.
 * @apiSuccess {Number} products.holdings.geographicRegion.colombiaIndicator The colombia indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.denmark The denmark.
 * @apiSuccess {Number} products.holdings.geographicRegion.denmarkIndicator The denmark indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.eMEMEA The e memea.
 * @apiSuccess {Number} products.holdings.geographicRegion.eMEMEAIndicator The e memea indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.eurozone The eurozone.
 * @apiSuccess {Number} products.holdings.geographicRegion.eurozoneIndicator The eurozone indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.finland The finland.
 * @apiSuccess {Number} products.holdings.geographicRegion.finlandIndicator The finland indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.france The france.
 * @apiSuccess {Number} products.holdings.geographicRegion.franceIndicator The france indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.germany The germany.
 * @apiSuccess {Number} products.holdings.geographicRegion.germanyIndicator The germany indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.greece The greece.
 * @apiSuccess {Number} products.holdings.geographicRegion.greeceIndicator The greece indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.hongKong The hong kong.
 * @apiSuccess {Number} products.holdings.geographicRegion.hongKongIndicator The hong kong indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.india The india.
 * @apiSuccess {Number} products.holdings.geographicRegion.indiaIndicator The india indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.indonesia The indonesia.
 * @apiSuccess {Number} products.holdings.geographicRegion.indonesiaIndicator The indonesia indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.ireland The ireland.
 * @apiSuccess {Number} products.holdings.geographicRegion.irelandIndicator The ireland indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.israel The israel.
 * @apiSuccess {Number} products.holdings.geographicRegion.israelIndicator The israel indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.italy The italy.
 * @apiSuccess {Number} products.holdings.geographicRegion.italyIndicator The italy indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.japan The japan.
 * @apiSuccess {Number} products.holdings.geographicRegion.japanIndicator The japan indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.korea The korea.
 * @apiSuccess {Number} products.holdings.geographicRegion.koreaIndicator The korea indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.malaysia The malaysia.
 * @apiSuccess {Number} products.holdings.geographicRegion.malaysiaIndicator The malaysia indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.mexico The mexico.
 * @apiSuccess {Number} products.holdings.geographicRegion.mexicoIndicator The mexico indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.netherlands The netherlands. 
 * @apiSuccess {Number} products.holdings.geographicRegion.netherlandsIndicator The netherlands indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.newZealand The new zealand.
 * @apiSuccess {Number} products.holdings.geographicRegion.newZealandIndicator The new zealand indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.norway The norway.
 * @apiSuccess {Number} products.holdings.geographicRegion.norwayIndicator The norway indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.notclassified The notclassified.
 * @apiSuccess {Number} products.holdings.geographicRegion.notclassifiedIndicator The notclassified indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.othercountries The othercountries.
 * @apiSuccess {Number} products.holdings.geographicRegion.othercountriesIndicator The othercountries indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.peru The peru.
 * @apiSuccess {Number} products.holdings.geographicRegion.peruIndicator The peru indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.philippines The philippines.
 * @apiSuccess {Number} products.holdings.geographicRegion.philippinesIndicator The philippines indicator. 
 * @apiSuccess {Number} products.holdings.geographicRegion.portugal The portugal.
 * @apiSuccess {Number} products.holdings.geographicRegion.portugalIndicator The portugal indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.singapore The singapore.
 * @apiSuccess {Number} products.holdings.geographicRegion.singaporeIndicator The singapore indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.spain The spain. 
 * @apiSuccess {Number} products.holdings.geographicRegion.spainIndicator The spain indicator. 
 * @apiSuccess {Number} products.holdings.geographicRegion.sweden The sweden.
 * @apiSuccess {Number} products.holdings.geographicRegion.swedenIndicator The sweden indicator. 
 * @apiSuccess {Number} products.holdings.geographicRegion.switzerland The switzerland.
 * @apiSuccess {Number} products.holdings.geographicRegion.switzerlandIndicator The switzerland indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.taiwan The taiwan.
 * @apiSuccess {Number} products.holdings.geographicRegion.taiwanIndicator The taiwan indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.thailand The thailand.
 * @apiSuccess {Number} products.holdings.geographicRegion.thailandIndicator The thailand indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.unitedArabEmirates The united arab emirates.
 * @apiSuccess {Number} products.holdings.geographicRegion.unitedArabEmiratesIndicator The united arab emirates indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.unitedKingdom The united kingdom.
 * @apiSuccess {Number} products.holdings.geographicRegion.unitedKingdomIndicator The united kingdom indicator.
 * @apiSuccess {Number} products.holdings.geographicRegion.unitedStates The united states.
 * @apiSuccess {String} products.holdings.geographicRegion.unitedStatesIndicator. The united states indicator.
 * @apiSuccess {Object[]} products.holdings.topHoldingsList 
 * @apiSuccess {String} products.holdings.topHoldingsList.holdingName The holding name.
 * @apiSuccess {String} products.holdings.topHoldingsList.holdingCompany The company of the holding.
 * @apiSuccess {Number} products.holdings.topHoldingsList.holdingPercent Allocation weight of geographic region in the fund, expressed as a percentage.
 * @apiSuccess {Object} products.purchase
 * @apiSuccess {Number} products.purchase.minInitInvst Minimum initial investment required.
 * @apiSuccess {String} products.purchase.minInitInvstCurrencyCode Minimum initial investment required CurrencyCode.
 * @apiSuccess {Number} products.purchase.minSubqInvst Minimum subsequent investment required.
 * @apiSuccess {String} products.purchase.minSubqInvstCurrencyCode Minimum subsequent investment required CurrencyCode.
 * @apiSuccess {Number} products.purchase.minInitRRSPInvst Minimum initial RRSP investment require.
 * @apiSuccess {String} products.purchase.minInitRRSPInvstCurrencyCode Minimum initial RRSP investment required CurrencyCode.
 * @apiSuccess {Number} products.purchase.hhhhMinInitInvst Minimum subsequent investment required by hhhh define.
 * @apiSuccess {String} products.purchase.hhhhMinInitInvstCurrencyCode Minimum subsequent investment required CurrencyCode by hhhh define.
 * @apiSuccess {Number} products.purchase.hhhhMinSubqInvst Minimum initial RRSP investment required by hhhh define.
 * @apiSuccess {String} products.purchase.hhhhMinSubqInvstCurrencyCode Minimum initial RRSP investment required CurrencyCode by hhhh define.
 * @apiSuccess {String} products.purchase.loadType Sales commission charged to holders of fund units.
 * @apiSuccess {String} products.purchase.RRSPEligibility The RRSP eligibility.
 * @apiSuccess {String[]} products.swithableGroup Only for fund.The switching rule is that Funds are only allowed to switch in the same Switchable Group.
 * @apiSuccess {String} entityUpdatedTime The entity updated time.format:yyyy-MM-dd'T'HH:mm:ss.SSSXXX
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 *		{
 *			"pagination": {
 *				"totalNumberOfRecords": 1,
 *				"numberOfRecords": 1,
 *				"startDetail": 1,
 *				"endDetail": 1
 *			},
 *			"products": [{
 *				"header": {
 *					"name": "汇丰晋信动�1�7�策略混合型证券投资基金",
 *					"market": "CN",
 *					"productType": "UT",
 *					"currency": "CNY",
 *					"categoryCode": "FUNDCAT-MUAS",
 *					"categoryName": "多元资产基金",
 *					"familyCode": "UT",
 *					"familyName": "汇丰晋信",
 *					"prodAltNumSeg": [{
 *						"prodCdeAltClassCde": "F",
 *						"prodAltNum": "540003"
 *					},
 *					{
 *						"prodCdeAltClassCde": "M",
 *						"prodAltNum": "540003"
 *					},
 *					{
 *						"prodCdeAltClassCde": "O",
 *						"prodAltNum": "0P00008XG2"
 *					},
 *					{
 *						"prodCdeAltClassCde": "P",
 *						"prodAltNum": "540003"
 *					},
 *					{
 *						"prodCdeAltClassCde": "U",
 *						"prodAltNum": "F0HKG070NP"
 *					},
 *					{
 *						"prodCdeAltClassCde": "T",
 *						"prodAltNum": "000000000000000000001000041236"
 *					},
 *					{
 *						"prodCdeAltClassCde": "W",
 *						"prodAltNum": "1000041236"
 *					}]
 *				},
 *				"summary": {
 *					"riskLvlCde": "4",
 *					"lastPrice": null,
 *					"dayEndNAV": 1.6821,
 *					"dayEndNAVCurrencyCode": "CNY",
 *					"changeAmountNAV": -0.003,
 *					"changePercentageNAV": -0.17803,
 *					"asOfDate": "2017-05-17",
 *					"averageDailyVolume": null,
 *					"assetsUnderManagement": 136572662,
 *					"assetsUnderManagementCurrencyCode": null,
 *					"totalNetAsset": 136572662,
 *					"totalNetAssetCurrencyCode": "CNY",
 *					"ratingOverall": 5,
 *					"mer": null,
 *					"yield1Yr": 0,
 *					"switchInMinAmount": null,
 *					"switchInMinAmountCurrencyCode": "CNY",
 *					"annualReportOngoingCharge":null,
 *					"actualManagementFee":1.5
 *				},
 *				"profile": {
 *					"inceptionDate": "2007-04-09",
 *					"turnoverRatio": 471.9,
 *					"stdDev3Yr": 30.497,
 *					"equityStylebox": 2,
 *					"expenseRatio": 1.88
 *				},
 *				"rating": {
 *					"morningstarRating": "5",
 *					"taxAdjustedRating": null,
 *					"averageCreditQuality": null,
 *					"rank1Yr": 2
 *				},
 *				"performance": {
 *					"annualizedReturns": {
 *						"return1day":-0.8529,
 *						"return1Mth": -2.15352,
 *						"return3Mth": 0.83911,
 *						"return6Mth": 2.23154,
 *						"return1Yr": 6.87633,
 *						"return3Yr": 30.44848,
 *						"return5Yr": 19.38837,
 *						"return10Yr": 9.22135,
 *						"returnSinceInception": 9.42373,
 *						"inceptionDate": "20070409000000000000+0000"
 *					},
 *					"calendarReturns": {
 *						"returnYTD": 3.51414,
 *						"year": [{
 *							"yearName": 1,
 *							"yearValue": -8.35139
 *						},
 *						{
 *							"yearName": 2,
 *							"yearValue": 38.46244
 *						},
 *						{
 *							"yearName": 3,
 *							"yearValue": 58.95683
 *						},
 *						{
 *							"yearName": 4,
 *							"yearValue": 15.43617
 *						},
 *						{
 *							"yearName": 5,
 *							"yearValue": -3.38381
 *						}]
 *					}
 *				},
 *				"risk": [{
 *					"yearRisk": {
 *						"year": 1,
 *						"beta": 0.58,
 *						"stdDev": 7.611,
 *						"alpha": 0.36,
 *						"sharpeRatio": 0.767,
 *						"rSquared": 48.59
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 3,
 *						"beta": 0.95,
 *						"stdDev": 30.497,
 *						"alpha": 13.59,
 *						"sharpeRatio": 0.975,
 *						"rSquared": 77.26
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 5,
 *						"beta": 0.93,
 *						"stdDev": 27.472,
 *						"alpha": 15.56,
 *						"sharpeRatio": 0.709,
 *						"rSquared": 72.06
 *					}
 *				},
 *				{
 *					"yearRisk": {
 *						"year": 10,
 *						"beta": 0.71,
 *						"stdDev": 25.912,
 *						"alpha": 9.34,
 *						"sharpeRatio": 0.383,
 *						"rSquared": 70.29
 *					}
 *				}],
 *				"holdings": {
 *					"sector": {
 *						"basicMaterials": 6.9746,
 *						"basicMaterialsIndicator": false,
 *						"communicationServices": null,
 *						"communicationServicesIndicator": null,
 *						"consumerCyclical": 16.15895,
 *						"consumerCyclicalIndicator": false,
 *						"consumerDefensive": 6.00043,
 *						"consumerDefensiveIndicator": false,
 *						"energy": 3.32553,
 *						"energyIndicator": false,
 *						"excluded": null,
 *						"excludedIndicator": null,
 *						"financialServices": 19.99148,
 *						"financialServicesIndicator": false,
 *						"healthcare": 12.16076,
 *						"healthcareIndicator": false,
 *						"industrials": 2.68897,
 *						"industrialsIndicator": false,
 *						"notClassified": 23.29808,
 *						"notClassifiedIndicator": false,
 *						"others": null,
 *						"othersIndicator": null,
 *						"technology": 8.4249,
 *						"technologyIndicator": false,
 *						"utilities": 0.9763,
 *						"utilitiesIndicator": false
 *					},
 *					"geographicRegion": {
 *						"australia": null,
 *						"australiaIndicator": null,
 *						"austria": null,
 *						"austriaIndicator": null,
 *						"belgium": null,
 *						"belgiumIndicator": null,
 *						"brazil": null,
 *						"brazilIndicator": null,
 *						"canada": null,
 *						"canadaIndicator": null,
 *						"chile": null,
 *						"chileIndicator": null,
 *						"china": null,
 *						"chinaIndicator": false,
 *						"colombia": null,
 *						"colombiaIndicator": null,
 *						"denmark": null,
 *						"denmarkIndicator": null,
 *						"eMEMEA": null,
 *						"eMEMEAIndicator": null,
 *						"eurozone": null,
 *						"eurozoneIndicator": null,
 *						"finland": null,
 *						"finlandIndicator": null,
 *						"france": null,
 *						"franceIndicator": null,
 *						"germany": null,
 *						"germanyIndicator": null,
 *						"greece": null,
 *						"greeceIndicator": null,
 *						"hongKong": null,
 *						"hongKongIndicator": null,
 *						"india": null,
 *						"indiaIndicator": null,
 *						"indonesia": null,
 *						"indonesiaIndicator": null,
 *						"ireland": null,
 *						"irelandIndicator": null,
 *						"israel": null,
 *						"israelIndicator": null,
 *						"italy": null,
 *						"italyIndicator": null,
 *						"japan": null,
 *						"japanIndicator": null,
 *						"korea": null,
 *						"koreaIndicator": null,
 *						"malaysia": null,
 *						"malaysiaIndicator": null,
 *						"mexico": null,
 *						"mexicoIndicator": null,
 *						"netherlands": null,
 *						"netherlandsIndicator": null,
 *						"newZealand": null,
 *						"newZealandIndicator": null,
 *						"norway": null,
 *						"norwayIndicator": null,
 *						"notclassified": null,
 *						"notclassifiedIndicator": null,
 *						"othercountries": null,
 *						"othercountriesIndicator": false,
 *						"peru": null,
 *						"peruIndicator": null,
 *						"philippines": null,
 *						"philippinesIndicator": null,
 *						"portugal": null,
 *						"portugalIndicator": null,
 *						"singapore": null,
 *						"singaporeIndicator": null,
 *						"spain": null,
 *						"spainIndicator": null,
 *						"sweden": null,
 *						"swedenIndicator": null,
 *						"switzerland": null,
 *						"switzerlandIndicator": null,
 *						"taiwan": null,
 *						"taiwanIndicator": null,
 *						"thailand": null,
 *						"thailandIndicator": null,
 *						"unitedArabEmirates": null,
 *						"unitedArabEmiratesIndicator": null,
 *						"unitedKingdom": null,
 *						"unitedKingdomIndicator": null,
 *						"unitedStates": null,
 *						"unitedStatesIndicator": null
 *					},
 *					"topHoldingsList": [{
 *						"holdingName": "1",
 *						"holdingCompany": "Tongkun Group Co Ltd",
 *						"holdingPercent": 4.7064
 *					},
 *					{
 *						"holdingName": "2",
 *						"holdingCompany": "Hua Xia Bank Co Ltd",
 *						"holdingPercent": 4.66015
 *					},
 *					{
 *						"holdingName": "3",
 *						"holdingCompany": "Bright Dairy & Food Co Ltd",
 *						"holdingPercent": 4.18347
 *					},
 *					{
 *						"holdingName": "4",
 *						"holdingCompany": "China Resources Double-Crane Pharmaceutical Co Ltd",
 *						"holdingPercent": 3.6703
 *					},
 *					{
 *						"holdingName": "5",
 *						"holdingCompany": "Tianjin Zhongxin Pharmaceutical Group Corp Ltd",
 *						"holdingPercent": 3.62294
 *					}]
 *				},
 *				"purchase": {
 *					"minInitInvst": 2000,
 *					"minInitInvstCurrencyCode": "CU$$$$$CNY ",
 *					"minSubqInvst": 1000,
 *					"minSubqInvstCurrencyCode": "CU$$$$$CNY ",
 *					"minInitRRSPInvst": null,
 *					"minInitRRSPInvstCurrencyCode": "CU$$$$$CNY ",
 *					"hhhhMinInitInvst": 1000,
 *					"hhhhMinInitInvstCurrencyCode": "CU$$$$$CNY ",
 *					"hhhhMinSubqInvst": null,
 *					"hhhhMinSubqInvstCurrencyCode": "CU$$$$$CNY ",
 *					"loadType": null,
 *					"rrspeligibility": null
 *				},
 *				"swithableGroup": null
 *			}],
 *			"entityUpdatedTime": null
 *		}
 * 
 * @apiUse ErrorMsgResponse
 */