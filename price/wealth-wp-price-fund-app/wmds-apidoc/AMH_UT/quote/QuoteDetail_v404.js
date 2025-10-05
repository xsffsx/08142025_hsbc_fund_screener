/**
 * @api {get} /wmds/v4.0/quoteDetail Quote Detail
 * @apiName Quote Detail
 * @apiGroup Quote
 * @apiVersion 4.0.4
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
 * @apiParam {Boolean} delay Base on this to select table to retrieve data from realtime or delay table , true = Delay Quote; false = Real Time Quote
 * @apiParam {Boolean} [tradeDay] Indicates whether it is a trading day.
 * @apiParam {String} [tradeHours] Time range in local time. sample value : 09001200:14001600
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
 * 
 *  {
 *  	"market":"CN",
 *  	"productType":"UT",
 *  	"prodCdeAltClassCde":"M",
 *  	"prodAltNum":"540002",
 *  	"delay":true,
 *  	"entityTimezone":"Asia/Hong_Kong"
 *  }
 *
 * @apiSuccess {Object[]} prodAltNumSegs
 * @apiSuccess {String} prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} prodAltNumSegs.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String} requestType Sample value : 0 , 1 , 2 , 3 , 4.
 * @apiSuccess {Number} remainingQuote The remaining free quote for the user.   
 * @apiSuccess {number} totalQuote Total free quote available.
 * @apiSuccess {String} entityUpdatedTime Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS.
 * @apiSuccess {Object[]} indexQuotes 
 * @apiSuccess {String} indexQuotes.symbol The symbol. 
 * @apiSuccess {Number} indexQuotes.quote The quote.
 * @apiSuccess {String} indexQuotes.name The name. 
 * @apiSuccess {Number} indexQuotes.change The change.
 * @apiSuccess {Number} indexQuotes.changePercentage The change percentage.
 * @apiSuccess {Object[]} bidAskQuotes 
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
 * @apiSuccess {Number} priceQuote.quoteIndicator The quote indicator. sample value : Y-delayed quote, N-realtime quote, O-out of trading hour
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
 * @apiSuccess {String} priceQuote.exDividendDate The ex dividend date.
 * @apiSuccess {Number} priceQuote.boardLot The board lot.
 * @apiSuccess {String} priceQuote.status The status.
 * @apiSuccess {String} priceQuote.historyCloseDate The history close date.
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
 * @apiSuccess {String} priceQuote.exchangeUpdatedTime Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS.
 * @apiSuccess {String} priceQuote.unsignedAgreementId The unsigned agreement id.
 * @apiSuccess {Number} priceQuote.quoteVMA50D Volume Moving Averages - 50 days (ETFs Only)
 * @apiSuccess {String} priceQuote.prodShoreLocCde Only for Fund.Denote the product shore location code
 * @apiSuccess {String} priceQuote.allowSellMipProdInd The allow sell mip prod ind.  
 * @apiSuccess {String} priceQuote.upperTradingLimit This FID holds the Highest limit price.
 * @apiSuccess {String} priceQuote.lowerTradingLimit This FID holds the Lowest limit price.
 * @apiSuccess {String} priceQuote.riskAlert true = it's a risk alert stock; false = it's a normal stock. Please note that only support CN market.
 * @apiSuccess {String} priceQuote.riskRating The risk rating.
 * @apiSuccess {String} priceQuote.riskLvlCde The risk level code.
 * @apiSuccess {Object} priceQuote.option 
 * @apiSuccess {String} priceQuote.option.underlyingSymbol The underlying symbol.
 * @apiSuccess {String} priceQuote.option.underlyingMarket The underlying market.
 * @apiSuccess {String} priceQuote.option.underlyingProductType Denotes the type of the underlying product.
 * @apiSuccess {String} priceQuote.option.rootSymbol The root symbol.
 * @apiSuccess {Number} priceQuote.option.derivativeOpenInterest The derivative open interest.
 * @apiSuccess {Number} priceQuote.option.derivativeStrike  The derivative strike.
 * @apiSuccess {String} priceQuote.option.derivativeContractSize The derivative contract size.
 * @apiSuccess {String} priceQuote.option.derivativePeriod The derivative period.
 * @apiSuccess {String} priceQuote.option.derivativeCallPut The derivative call put.
 * @apiSuccess {String} priceQuote.option.derivativeMoneyness The derivative moneyness.
 * @apiSuccess {String} priceQuote.option.derivativeContractMonth The derivative contract month.
 * @apiSuccess {String} priceQuote.option.derivativeExpiryDate The derivative expiry date.format:yyyy-MM-dd
 * @apiSuccess {String} priceQuote.option.derivativeDaysToMaturity The derivative days to maturity.
 * @apiSuccess {String} priceQuote.option.derivativeDelta The derivative delta.
 * @apiSuccess {Number} priceQuote.option.derivativeRho The derivative rho.
 * @apiSuccess {Number} priceQuote.option.derivativeTheta The derivative theta.
 * @apiSuccess {Number} priceQuote.option.derivativeGamma The derivative gamma.
 * @apiSuccess {Number} priceQuote.option.derivativeVega The derivative vega.
 * @apiSuccess {Number} priceQuote.option.derivativeContractRangeLow The derivative Contract range low.
 * @apiSuccess {Number} priceQuote.option.derivativeContractRangeHigh The derivative Contract range high.
 * @apiSuccess {String} priceQuote.option.prefillExpirationDate The prefill expiration Date.format:yyyy-MM-dd
 * @apiSuccess {Number} priceQuote.option.prefillStrikePrice The prefill Strike Price.
 * @apiSuccess {Date[]} priceQuote.option.expirationDates The expiration Date.format:yyyy-MM-dd
 * @apiSuccess {Number[]} priceQuote.option.strikePrices The strike Price.
 * @apiSuccess {String[]} priceQuote.option.expirationMonths The expiration Month.
 * @apiSuccess {Object} priceQuote.bond
 * @apiSuccess {Object} priceQuote.bond.summary
 * @apiSuccess {String} priceQuote.bond.summary.buyQuoteId Quote id for smart trade buy request.
 * @apiSuccess {String} priceQuote.bond.summary.sellQuoteId Quote id for smart trade sell request.
 * @apiSuccess {Number} priceQuote.bond.summary.buyPrice The buy price. 
 * @apiSuccess {Number} priceQuote.bond.summary.sellPrice The sell price.
 * @apiSuccess {Number} priceQuote.bond.summary.buyYield The buy yield.
 * @apiSuccess {Number} priceQuote.bond.summary.sellYield The sell yield. 
 * @apiSuccess {Number} priceQuote.bond.summary.buyQuantityAvailable The buy quantity available.
 * @apiSuccess {Number} priceQuote.bond.summary.sellQuantityAvailable The sell quantity available.
 * @apiSuccess {Number} priceQuote.bond.summary.buyMarkUp The buy markup.
 * @apiSuccess {Number} priceQuote.bond.summary.sellMarkUp The sell markup.
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
 * @apiSuccess {Object} priceQuote.bond.couponAccuredInterest
 * @apiSuccess {Number} priceQuote.bond.couponAccuredInterest.accuredInterest The accured interest.
 * @apiSuccess {String} priceQuote.bond.couponAccuredInterest.couponPaymentFrequency The coupon currency. 
 * @apiSuccess {String} priceQuote.bond.couponAccuredInterest.couponPaidIn The couponPaidIn. MDSBE Missing.
 * @apiSuccess {Number} priceQuote.bond.couponAccuredInterest.couponRate The coupon rate.
 * @apiSuccess {Number} priceQuote.bond.couponAccuredInterest.nextCouponPayment The next coupon payment.
 * @apiSuccess {Object} priceQuote.bond.maturityFeatures 
 * @apiSuccess {String} priceQuote.bond.maturityFeatures.actualMaturity From Bond Service. format:yyyy-MMdd
 * @apiSuccess {String} priceQuote.bond.maturityFeatures.callable Indicate whether the bond is callable
 * @apiSuccess {String} priceQuote.bond.maturityFeatures.effectiveMaturity From Bond Service. format:yyyy-MM-dd
 * @apiSuccess {String} priceQuote.bond.maturityFeatures.comments The comments.
 * @apiSuccess {Object} priceQuote.bond.riskMeasures
 * @apiSuccess {Number} priceQuote.bond.riskMeasures.effectiveDuration  The effective duration.
 * @apiSuccess {String} priceQuote.bond.riskMeasures.riskLvlCde The risk Level code. 
 * @apiSuccess {Object[]} priceQuote.bond.riskMeasures.ratings 
 * @apiSuccess {String} priceQuote.bond.riskMeasures.ratings.rating The rating. 
 * @apiSuccess {String} priceQuote.bond.riskMeasures.ratings.ratingAgency The ating agency.
 * @apiSuccess {String} priceQuote.bond.riskMeasures.ratings.ratingAgencyCode The rating agency code.
 * @apiSuccess {String} priceQuote.bond.riskMeasures.ratings.ratingDate format:yyyy-MM--dd
 * @apiSuccess {Object} priceQuote.bond.tradingInfo
 * @apiSuccess {Number} priceQuote.bond.tradingInfo.minimumTradeIncrementalSell Increment ask amount.
 * @apiSuccess {Number} priceQuote.bond.tradingInfo.minimumTradeAmountSell Increment bid amount.
 * @apiSuccess {Number} priceQuote.bond.tradingInfo.minimumTradeIncrementalBuy Minimum ask amount.
 * @apiSuccess {Number} priceQuote.bond.tradingInfo.minimumTradeAmountBuy Minimum bid amount.
 * @apiSuccess {String} priceQuote.bond.tradingInfo.settlementCalendar Settlement according to a country calendar.
 * @apiSuccess {String} priceQuote.bond.tradingInfo.standardSettlement Settlement period.
 * @apiSuccess {Object} priceQuote.interestRate
 * @apiSuccess {Number} priceQuote.interestRate.interestRateAmount Interest Rate Amount.
 * @apiSuccess {Number} priceQuote.interestRate.interestRateChangePercent Interest Rage Change Percentage.
 * @apiSuccess {Number} priceQuote.interestRate.weekagoRateAmount Week ago rage amount.
 * @apiSuccess {String} priceQuote.interestRate.tenor The tenor.
 * @apiSuccess {Object} priceQuote.commodity
 * @apiSuccess {Number} priceQuote.commodity.dayBidLow The day bid low.
 * @apiSuccess {Number} priceQuote.commodity.dayBidHigh The day bid high.
 * @apiSuccess {Number} priceQuote.commodity.dayAskLow The day ask low.
 * @apiSuccess {Number} priceQuote.commodity.dayAskHigh The day ask high.
 * @apiSuccess {Number} priceQuote.commodity.previousCloseBidPrice The previous close bid price.
 * @apiSuccess {Number} priceQuote.commodity.previousCloseAskPrice The previous close ask price.
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
 *			"requestType": null,
 *			"remainingQuote": null,
 *			"totalQuote": null,
 *			"indexQuotes": null,
 *			"bidAskQuotes": null,
 *			"priceQuote": {
 *				"productStatus": null,
 *				"ric": "000000000000000000001000041233",
 *				"symbol": "540002",
 *				"market": "CN",
 *				"productType": "UT",
 *				"productSubTypeCode": "LCUT",
 *				"prodCdeAltClassCde": null,
 *				"quoteIndicator": true,
 *				"priceQuote": 1.5001,
 *				"currency": "CNY",
 *				"companyName": "hhhh Jintrust Dragon Growth Equity Fund",
 *				"securityDescription": null,
 *				"changeAmount": -0.0023,
 *				"changePercent": -0.15309,
 *				"bidPrice": null,
 *				"bidSize": null,
 *				"askPrice": null,
 *				"askSize": null,
 *				"dayRangeLow": null,
 *				"dayRangeHigh": null,
 *				"tradingVolume": null,
 *				"openPrice": null,
 *				"yearLowPrice": null,
 *				"yearHighPrice": null,
 *				"averageVolume": null,
 *				"peRatio": null,
 *				"marketCap": null,
 *				"sharesOutstanding": null,
 *				"beta": null,
 *				"previousClosePrice": null,
 *				"dividend": null,
 *				"dividendYield": null,
 *				"exDividendDate": null,
 *				"boardLot": null,
 *				"casEligibleFlag": null,
 *				"status": null,
 *				"historyCloseDate": null,
 *				"turnOver": null,
 *				"marketClosed": null,
 *				"nominalPriceType": null,
 *				"spreadBid": null,
 *				"spreadAsk": null,
 *				"eps": null,
 *				"iep": null,
 *				"iev": null,
 *				"turnoverAmount": null,
 *				"turnoverIncludeAmount": null,
 *				"quoteSector": null,
 *				"quoteIndustry": null,
 *				"quoteExchange": null,
 *				"quote1MPercChange": null,
 *				"quote3MPercChange": null,
 *				"quote6MPercChange": null,
 *				"quote12MPercChange": null,
 *				"exchangeTimezone": null,
 *				"exchangeUpdatedTime": "2017-05-17",
 *				"unsignedAgreementId": null,
 *				"quoteVMA50D": null,
 *				"prodShoreLocCde": "ONSHR",
 *				"allowSellMipProdInd": "Y",
 *				"upperTradingLimit": null,
 *				"lowerTradingLimit": null,
 *				"riskAlert": null,
 *				"riskRating": null,
 *				"riskLvlCde": "4",
 *				"option": null,
 *				"bond": null,
 *				"interestRate": null,
 *				"commodity": null,
 *				"performanceId": "0P00007KN5",
 *				"tradableCurrency": null,
 *				"agreementSignStatus": null,
 *				"agreementId": null,
 *				"inDaylightTime": null,
 *				"tradeUnits": null
 *			},
 *			"entityUpdatedTime": "2017-06-06T14:47:53.000+08:00",
 *			"prodAltNumSegs": [{
 *				"prodCdeAltClassCde": "F",
 *				"prodAltNum": "540002"
 *			},
 *			{
 *				"prodCdeAltClassCde": "M",
 *				"prodAltNum": "540002"
 *			},
 *			{
 *				"prodCdeAltClassCde": "O",
 *				"prodAltNum": "0P00007KN5"
 *			},
 *			{
 *				"prodCdeAltClassCde": "P",
 *				"prodAltNum": "540002"
 *			},
 *			{
 *				"prodCdeAltClassCde": "U",
 *				"prodAltNum": "F0HKG06YIN"
 *			},
 *			{
 *				"prodCdeAltClassCde": "T",
 *				"prodAltNum": "000000000000000000001000041233"
 *			},
 *			{
 *				"prodCdeAltClassCde": "W",
 *				"prodAltNum": "1000041233"
 *			}]
 *		}
 *    
 *    	
 *
 * @apiUse ErrorMsgResponse
 */