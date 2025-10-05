/**
 * @api {get} /wmds/v4.0/quoteDetail Quote Detail
 * @apiName Quote Detail
 * @apiGroup Quote
 * @apiVersion 4.0.1
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
 * @apiParam {Boolean} delay Base on this to select table to retrieve data from realtime or delay table , true = Delay Quote; false = Real Time Quote
 * @apiParam {Boolean} [tradeDay] Indicates whether it is a trading day.
 * @apiParam {Boolean} [tradeHours] Time range in local time. sample value : 09001200:14001600
 * @apiParam {Number} [freeQuote] Free quote granted to customer. 
 * @apiParam {String} [requestType] Char: -1 - no limit</br> 
 * 										 0 - level 0</br>
 *                                       1 - level 1</br>
 *                                       2 - level 2</br>
 *                                       3 - level 3</br>
 *                                       4 - level 4</br>
 *                                       sample value for Bond: ALL, PRICE	
 * @apiParam {String} [skipAgreementCheck] Indicator to indentify if agreement checking can be skipped. Which provides convenient for order placement to get real-time qoute for awalys.                                         
 * @apiParam {String} [entityTimezone] Used to format time "EST". </br>
 * 									   sample value : US-America/New_York , UK-Europe/London                                    
 * @apiParam {String} [adjustedCusSegment] Customer Segment, overwrite the cusSegment in token.                                        
 * @apiParam {String} [side] Bid or ask of the request. sample value : "1" - Buy , "2" - Sell
 * @apiParam {Number} [quantity] The quantity of the bond.
 * 
 * @apiParamExample Request:
 * 	{
 * 		"market" : "HK",
 * 		"productType" : "SEC",
 * 		"prodCdeAltClassCde" : "M",
 * 		"prodAltNum" : "00005",
 * 		"delay" : true
 * 	}
 *
 * @apiSuccess {Object[]} prodAltNumSegs
 * @apiSuccess {String} prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} prodAltNumSegs.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String} requestType Sample value : 0 , 1 , 2 , 3 , 4.
 * @apiSuccess {Number} remainingQuote The remaining free quote for the user.   
 * @apiSuccess {number} totalQuote Total free quote available.
 * @apiSuccess {Date} entityUpdatedTime Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX
 * @apiSuccess {Object[]} indexQuotes 
 * @apiSuccess {String} indexQuotes.symbol The symbol. 
 * @apiSuccess {Number} indexQuotes.quote The quote.
 * @apiSuccess {String} indexQuotes.name The name. 
 * @apiSuccess {Number} indexQuotes.change The change.
 * @apiSuccess {Number} indexQuotes.changePercentage The change percentage.
 * @apiSuccess {BidAskQuote[]} bidAskQuotes 
 * @apiSuccess {Number} bidAskQuotes.bidOrder The bid order.
 * @apiSuccess {Number} bidAskQuotes.bidBroker The bid broker. 
 * @apiSuccess {Number} bidAskQuotes.bidPrice The bid price.
 * @apiSuccess {Number} bidAskQuotes.bidSize The bid size.
 * @apiSuccess {Number} bidAskQuotes.askOrder The ask order. 
 * @apiSuccess {Number} bidAskQuotes.askBroker The ask broker.
 * @apiSuccess {Number} bidAskQuotes.askPrice The ask price.
 * @apiSuccess {Number} bidAskQuotes.askSize The ask size.
 * @apiSuccess {Object} priceQuote
 * @apiSuccess {Object} priceQuote.productStatus
 * @apiSuccess {String} priceQuote.productStatus.value The value.
 * @apiSuccess {String} priceQuote.productStatus.statusCode The status code.
 * @apiSuccess {String} priceQuote.productStatus.message The status message 
 * @apiSuccess {String} priceQuote.ric The ric code of the product.
 * @apiSuccess {String} priceQuote.symbol The symbol of the product.
 * @apiSuccess {String} priceQuote.market The market of the product. 
 * @apiSuccess {String} priceQuote.productType Product Type , If this field is null, then the productType require to define in productKey.
 * @apiSuccess {String} priceQuote.productSubTypeCode The product sub type code.
 * @apiSuccess {String} priceQuote.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} priceQuote.quoteIndicator The quote indicator. sample value : Y-delayed quote, N-realtime quote, O-out of trading hour
 * @apiSuccess {Number} priceQuote.priceQuote The price quote.
 * @apiSuccess {String} priceQuote.currency The currency.
 * @apiSuccess {String} priceQuote.companyName The company name.
 * @apiSuccess {String} priceQuote.securityDescription The security description.
 * @apiSuccess {Number} priceQuote.changeAmount The change amount. 
 * @apiSuccess {Number} priceQuote.changePercent The change percent.
 * @apiSuccess {Number} priceQuote.bidPrice The bid price.
 * @apiSuccess {Number} priceQuote.bidSize The bid size.
 * @apiSuccess {Number} priceQuote.askPrice The ask price. 
 * @apiSuccess {Number} priceQuote.askSize The ask size.
 * @apiSuccess {Number} priceQuote.dayRangeLow The day range low.
 * @apiSuccess {Number} priceQuote.dayRangeHigh The day range high.
 * @apiSuccess {Number} priceQuote.tradingVolume The trading volume.
 * @apiSuccess {Number} priceQuote.openPrice The open price.
 * @apiSuccess {Number} priceQuote.yearLowPrice The year low of the specified symbol.
 * @apiSuccess {Number} priceQuote.yearHighPrice The year high of the specified symbol.
 * @apiSuccess {Number} priceQuote.averageVolume The average volume.
 * @apiSuccess {Number} priceQuote.peRatio  The pe ratio.
 * @apiSuccess {Number} priceQuote.marketCap The market cap.
 * @apiSuccess {Number} priceQuote.sharesOutstanding The shares outstanding.
 * @apiSuccess {Number} priceQuote.beta The beta.
 * @apiSuccess {Number} priceQuote.previousClosePrice The previous close price.
 * @apiSuccess {Number} priceQuote.dividend The dividend.
 * @apiSuccess {Number} priceQuote.dividendYield The dividend yield.
 * @apiSuccess {Date} priceQuote.exDividendDate The ex dividend date. format:yyyy-MM-dd
 * @apiSuccess {Number} priceQuote.boardLot The board lot.
 * @apiSuccess {String} priceQuote.status The status.
 * @apiSuccess {Date} priceQuote.historyCloseDate The history close date.format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX
 * @apiSuccess {Number} priceQuote.turnOver The turn over.
 * @apiSuccess {String} priceQuote.marketClosed The market closed.
 * @apiSuccess {String} priceQuote.nominalPriceType The nominal price type.
 * @apiSuccess {Number} priceQuote.spreadBid The spread bid.
 * @apiSuccess {Number} priceQuote.spreadAsk The spread ask.
 * @apiSuccess {Number} priceQuote.eps Stock price-earnings ratio.
 * @apiSuccess {Number} priceQuote.iep Reference balance price.
 * @apiSuccess {Number} priceQuote.iev Reference balance trading volume.
 * @apiSuccess {Number} priceQuote.turnoverAmount The turnover amount.
 * @apiSuccess {Number} priceQuote.turnoverIncludeAmount  The turnover include amount. 
 * @apiSuccess {String} priceQuote.quoteSector The quote sector.
 * @apiSuccess {String} priceQuote.quoteIndustry The quote industry.
 * @apiSuccess {String} priceQuote.quoteExchange The quote exchange.
 * @apiSuccess {Number} priceQuote.quote1MPercChange The quote amount percent change.
 * @apiSuccess {Number} priceQuote.quote3MPercChange The quote amount percent change.
 * @apiSuccess {Number} priceQuote.quote6MPercChange The quote amount percent change.
 * @apiSuccess {Number} priceQuote.quote12MPercChange The quote amount percent change.
 * @apiSuccess {String} priceQuote.exchangeTimezone The exchange timezone.
 * @apiSuccess {Date} priceQuote.exchangeUpdatedTime Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX 
 * @apiSuccess {String} priceQuote.unsignedAgreementId The unsigned agreement id.
 * @apiSuccess {Number} priceQuote.quoteVMA50D Volume Moving Averages - 50 days (ETFs Only)
 * @apiSuccess {String} priceQuote.prodShoreLocCde Only for Fund.Denote the product shore location code
 * @apiSuccess {String} priceQuote.allowSellMipProdInd The allow sell mip prod ind.  
 * @apiSuccess {String} priceQuote.upperTradingLimit This FID holds the Highest limit price.
 * @apiSuccess {String} priceQuote.lowerTradingLimit This FID holds the Lowest limit price.
 * @apiSuccess {Boolean} priceQuote.riskAlert true = it's a risk alert stock; false = it's a normal stock. Please note that only support CN market.
 * @apiSuccess {String} priceQuote.riskRating The risk rating.
 * @apiSuccess {String} priceQuote.riskLvlCde The risk level code.
 * @apiSuccess {Object} priceQuote.option 
 * @apiSuccess {String} priceQuote.option.underlyingSymbol The underlying symbol.
 * @apiSuccess {String} priceQuote.option.underlyingMarket The underlying market.
 * @apiSuccess {Number} priceQuote.option.underlyingProductType Denotes the type of the underlying product.
 * @apiSuccess {String} priceQuote.option.rootSymbol The root symbol.
 * @apiSuccess {Number} priceQuote.option.derivativeOpenInterest The derivative open interest.
 * @apiSuccess {Number} priceQuote.option.derivativeStrike  The derivative strike.
 * @apiSuccess {String} priceQuote.option.derivativeContractSize The derivative contract size.
 * @apiSuccess {String} priceQuote.option.derivativePeriod The derivative period.
 * @apiSuccess {String} priceQuote.option.derivativeCallPut The derivative call put.
 * @apiSuccess {String} priceQuote.option.derivativeMoneyness The derivative moneyness.
 * @apiSuccess {String} priceQuote.option.derivativeContractMonth The derivative contract month.
 * @apiSuccess {Date} priceQuote.option.derivativeExpiryDate The derivative expiry date.format:yyyy-MM-dd
 * @apiSuccess {String} priceQuote.option.derivativeDaysToMaturity The derivative days to maturity.
 * @apiSuccess {String} priceQuote.option.derivativeDelta The derivative delta.
 * @apiSuccess {Number} priceQuote.option.derivativeRho The derivative rho.
 * @apiSuccess {Number} priceQuote.option.derivativeTheta The derivative theta.
 * @apiSuccess {Number} priceQuote.option.derivativeGamma The derivative gamma.
 * @apiSuccess {Number} priceQuote.option.derivativeVega The derivative vega.
 * @apiSuccess {Number} priceQuote.option.derivativeContractRangeLow The derivative Contract range low.
 * @apiSuccess {Number} priceQuote.option.derivativeContractRangeHigh The derivative Contract range high.
 * @apiSuccess {Date} priceQuote.option.prefillExpirationDate The prefill expiration Date.format:yyyy-MM-dd
 * @apiSuccess {Number} priceQuote.option.prefillStrikePrice The prefill Strike Price.
 * @apiSuccess {Date[]} priceQuote.option.expirationDates The expiration Date.format:yyyy-MM-dd
 * @apiSuccess {Number[]} priceQuote.option.strikePrices The strike Price.
 * @apiSuccess {String[]} priceQuote.option.expirationMonths The expiration Month.
 * @apiSuccess {Object} priceQuote.bond
 * @apiSuccess {Object} priceQuote.bond.summaries
 * @apiSuccess {String} priceQuote.bond.summaries.buyQuoteId Quote id for smart trade buy request.
 * @apiSuccess {String} priceQuote.bond.summaries.sellQuoteId Quote id for smart trade sell request.
 * @apiSuccess {Number} priceQuote.bond.summaries.buyPrice The buy price. 
 * @apiSuccess {Number} priceQuote.bond.summaries.sellPrice The sell price.
 * @apiSuccess {Number} priceQuote.bond.summaries.buyYield The buy yield.
 * @apiSuccess {Number} priceQuote.bond.summaries.sellYield The sell yield. 
 * @apiSuccess {Number} priceQuote.bond.summaries.buyQuantityAvailable The buy quantity available.
 * @apiSuccess {Number} priceQuote.bond.summaries.sellQuantityAvailable The sell quantity available.
 * @apiSuccess {Number} priceQuote.bond.summaries.buyMarkUp The buy markup.
 * @apiSuccess {Number} priceQuote.bond.summaries.sellMarkUp The sell markup.
 * @apiSuccess {Object} priceQuote.bond.bondDetails
 * @apiSuccess {String} priceQuote.bond.bondDetails.bondType The bond type.
 * @apiSuccess {String} priceQuote.bond.bondDetails.issuer The issuer.
 * @apiSuccess {String} priceQuote.bond.bondDetails.bondCurrency The bond currency.
 * @apiSuccess {String} priceQuote.bond.bondDetails.cusip Committee on uniform security identification procedures.
 * @apiSuccess {String} priceQuote.bond.bondDetails.initialIssueDate The initial issue date.format:yyyy-MM-dd
 * @apiSuccess {String} priceQuote.bond.bondDetails.isin International securities identification number.
 * @apiSuccess {String} priceQuote.bond.bondDetails.mktStatus The market status.
 * @apiSuccess {Number} priceQuote.bond.bondDetails.mktBidDiscountMargin The market bid discount margin.
 * @apiSuccess {Number} priceQuote.bond.bondDetails.mktOfferDiscountMargin The market offer discount margin.
 * @apiSuccess {Number} priceQuote.bond.bondDetails.totalOutstanding The total out standing.
 * @apiSuccess {String} priceQuote.bond.bondDetails.instrumentStatus The instrument status.
 * @apiSuccess {Object} priceQuote.bond.couponAccuredInterests
 * @apiSuccess {Number} priceQuote.bond.couponAccuredInterests.accuredInterest The accured interest.
 * @apiSuccess {String} priceQuote.bond.couponAccuredInterests.couponPaymentFrequency The coupon currency. 
 * @apiSuccess {String} priceQuote.bond.couponAccuredInterests.couponPaidIn The couponPaidIn. MDSBE Missing.
 * @apiSuccess {Number} priceQuote.bond.couponAccuredInterests.couponRate The coupon rate.
 * @apiSuccess {Number} priceQuote.bond.couponAccuredInterests.nextCouponPayment The next coupon payment.
 * @apiSuccess {Object} priceQuote.bond.maturityFeatures 
 * @apiSuccess {Date} priceQuote.bond.maturityFeatures.actualMaturity From Bond Service. format:yyyy-MMdd
 * @apiSuccess {String} priceQuote.bond.maturityFeatures.callable Indicate whether the bond is callable
 * @apiSuccess {Date} priceQuote.bond.maturityFeatures.effectiveMaturity From Bond Service. format:yyyy-MM-dd
 * @apiSuccess {String} priceQuote.bond.maturityFeatures.comments The comments.
 * @apiSuccess {Object} priceQuote.bond.riskMeasures
 * @apiSuccess {Number} priceQuote.bond.riskMeasures.effectiveDuration  The effective duration.
 * @apiSuccess {String} priceQuote.bond.riskMeasures.riskLvlCde The risk Level code. 
 * @apiSuccess {Object[]} priceQuote.bond.riskMeasures.ratings 
 * @apiSuccess {String} priceQuote.bond.riskMeasures.ratings.rating The rating. 
 * @apiSuccess {String} priceQuote.bond.riskMeasures.ratings.ratingAgency The ating agency.
 * @apiSuccess {String} priceQuote.bond.riskMeasures.ratings.ratingAgencyCode The rating agency code.
 * @apiSuccess {String} priceQuote.bond.riskMeasures.ratings.ratingDate format:yyyy-MM--dd
 * @apiSuccess {Object} priceQuote.bond.tradingInfos
 * @apiSuccess {Number} priceQuote.bond.tradingInfos.minimumTradeIncrementalSell Increment ask amount.
 * @apiSuccess {Number} priceQuote.bond.tradingInfos.minimumTradeAmountSell Increment bid amount.
 * @apiSuccess {Number} priceQuote.bond.tradingInfos.minimumTradeIncrementalBuy Minimum ask amount.
 * @apiSuccess {Number} priceQuote.bond.tradingInfos.minimumTradeAmountBuy Minimum bid amount.
 * @apiSuccess {String} priceQuote.bond.tradingInfos.settlementCalendar Settlement according to a country calendar.
 * @apiSuccess {String} priceQuote.bond.tradingInfos.standardSettlement Settlement period.
 * @apiSuccess {Object} priceQuote.interestRate
 * @apiSuccess {Number} priceQuote.interestRate.interestRateAmount Interest Rate Amount.
 * @apiSuccess {Number} priceQuote.interestRate.interestRateChangePercent Interest Rage Change Percentage.
 * @apiSuccess {Number} priceQuote.interestRate.weekagoRateAmount Week ago rage amount.
 * @apiSuccess {String} priceQuote.interestRate.tenor The tenor.
 * @apiSuccess {String} priceQuote.interestRate.lastUpdateTime Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX
 * @apiSuccess {Object} priceQuote.interestRate.commodities
 * @apiSuccess {Number} priceQuote.interestRate.commodities.dayBidLow The day bid low.
 * @apiSuccess {Number} priceQuote.interestRate.commodities.dayBidHigh The day bid high.
 * @apiSuccess {Number} priceQuote.interestRate.commodities.dayAskLow The day ask low.
 * @apiSuccess {Number} priceQuote.interestRate.commodities.dayAskHigh The day ask high.
 * @apiSuccess {Number} priceQuote.interestRate.commodities.previousCloseBidPrice The previous close bid price.
 * @apiSuccess {Number} priceQuote.interestRate.commodities.previousCloseAskPrice The previous close ask price.
 * @apiSuccess {String} priceQuote.suptMipInd Only for UT. An indicator to indicate whether the fund support Monthly Investment Program (MIP, a service provided in AMH)
 * @apiSuccess {String} priceQuote.performanceId The performance id.
 * @apiSuccess {String} priceQuote.casEligibleFlag CAS eligible flag.
 * @apiSuccess {String} priceQuote.tradableCurrency Ttradable Currency Code.
 * @apiSuccess {String} priceQuote.agreementSignStatus agreement sign status. 
 * @apiSuccess {String} priceQuote.agreementId The agreement id. 
 * @apiSuccess {String} priceQuote.inDaylightTime Indicator to indentify if exchangeUpdateTime is in daylight saving. 
 * @apiSuccess {String} priceQuote.tradeUnits The trade units. 
 *
 * @apiSuccessExample Success-Response:
 *    HTTP/1.1 200 OK
 *    {
 *    		"prodAltNumSegs": [
 *    			{
 *    				"prodCdeAltClassCde": "M",
 *    				"prodAltNum": "XRE"
 *    			},
 *    			{
 *    				"prodCdeAltClassCde": "O",
 *    				"prodAltNum": "0P000080SU"
 *    			},
 *    			{
 *    				"prodCdeAltClassCde": "P",
 *    				"prodAltNum": "CA-XRE"
 *    			},
 *    			{
 *    				"prodCdeAltClassCde": "U",
 *    				"prodAltNum": "FOUSA06AZ4"
 *    			},
 *    			{
 *    				"prodCdeAltClassCde": "T",
 *    				"prodAltNum": "XRE.TO"
 *    			},
 *    			{
 *    				"prodCdeAltClassCde": "W",
 *    				"prodAltNum": "240122936"
 *    			}
 *    		],
 *    		"remainingQuote": 1,
 *    		"totalQuote": 1,
 *    		"entityUpdatedTime": "2017-02-27T01:14:37.000+8:00",
 *    		"priceQuote": {
 *    			"performanceId": "0P000080SU",
 *    			"ric": "XRE.TO",
 *    			"symbol": "XRE",
 *    			"market": "CA",
 *    			"productType": "ETF",
 *    			"productSubTypeCode": "ETF",
 *    			"quoteIndicator": "Y",
 *    			"priceQuote": 16.38,
 *    			"currency": "CAD",
 *    			"companyName": "iShares S&ampP/TSX Capped REIT Index ETF",
 *    			"changeAmount": -0.07,
 *    			"changePercent": -0.43,
 *    			"bidPrice": 16.37,
 *    			"bidSize": 1,
 *    			"askPrice": 16.41,
 *    			"askSize": 4,
 *    			"dayRangeLow": 16.335,
 *    			"dayRangeHigh": 16.48,
 *    			"tradingVolume": 72753,
 *    			"openPrice": 16.45,
 *    			"yearLowPrice": 14.6000,
 *    			"yearHighPrice": 17.3700,
 *    			"averageVolume": 251734,
 *    			"marketCap": 1006074866.31016,
 *    			"sharesOutstanding": 80400000,
 *    			"beta": 0.441097962,
 *    			"dividend": 0.82209,
 *    			"dividendYield": 5.02,
 *    			"boardLot": 100,
 *    			"status": "   ",
 *    			"previousClosePrice": 16.45,
 *    			"marketClosed": "Y",
 *    			"quoteSector": "55",
 *    			"quoteIndustry": "55501010",
 *    			"quoteExchange": "TOR",
 *    			"exchangeTimezone": "Canada/Eastern",
 *    			"exchangeUpdatedTime": "2017-02-24T15:59:00.000+8:00",
 *    			"agreementSignStatus": "N",
 *    			"agreementId": "1",
 *    			"inDaylightTime": "N"
 *    		}
 *    }
 *
 * @apiUse ErrorMsgResponse
 */