/**
 * @api {get} /wealth/api/v1/market-data/equity/quotes Equity Quotes
 * @apiName Equity Quotes
 * @apiGroup Quotes
 * @apiVersion 1.0.0
 * 
 * @apiUse CommonHeaderParams
 * 
 * @apiParam {Object[]} productKeys The key to uniquely locate a product.
 * @apiParam {String} productKeys.productType Product type.
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code. </br>Sample value: "M"
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKeys.market The investment market of the product.</br>
 *								<ul><li>CA - Canada</li>
 *									<li>US - United States</li>
 *									<li>HK - Hong Kong</li>
 *									<li>UK - United Kingdom</li>
 *									<li>FR - France</li>
 *									<li>DE - Germany</li></ul>
 * @apiParam {Boolean = true, false} delay <ul><li>true: Delay Quote</li><li>false: Real-time Quote</li></ul>
 * @apiParam {String} [requestType] The request type as special handling defined in BE.<ul><li>"0" - Default</li></ul>
 * @apiParam {String} [processingFlag] The flag to determine if skip using SAML user id. 
 *  
 * @apiParamExample Request:
 *  {
 *  	"productKeys": [{
 *  		"productType": "SEC",
 *  		"prodCdeAltClassCde": "M",
 *  		"market": "HK",
 *  		"prodAltNum": "5"
 *  	}],
 *  	"delay": true,
 *  	"requestType": "0"
 *  }
 *  
 * 
 * @apiSuccess {Object[]} priceQuotes
 * @apiSuccess {Object[]} priceQuotes.prodAltNumSegs
 * @apiSuccess {String} priceQuotes.prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} priceQuotes.prodAltNumSegs.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String} priceQuotes.symbol A stock symbol is a unique series of letters assigned to a security for trading purposes.
 * @apiSuccess {String} priceQuotes.market Stock market.
 * @apiSuccess {String} priceQuotes.exchangeCode Stock exchange. 
 * @apiSuccess {String} priceQuotes.productType Product type.
 * @apiSuccess {String} priceQuotes.companyName Company name.
 * @apiSuccess {String} priceQuotes.riskLvlCde Risk level code.
 * @apiSuccess {Number} priceQuotes.nominalPrice Price quotations on futures for a period in which no actual trading took place.
 * @apiSuccess {Number} priceQuotes.settlementPrice A settlement price, in the derivatives markets, is the price used for determining profit or loss for the day, as well as margin requirements.
 * @apiSuccess {String} priceQuotes.currency Trading currency.
 * @apiSuccess {Number} priceQuotes.changeAmount Change in amount of the share price versus previous closing price.
 * @apiSuccess {Number} priceQuotes.changePercent Change in percentage of the share price versus previous closing price.
 * @apiSuccess {Boolean} priceQuotes.delay Delay quote or Real-time quote.
 * @apiSuccess {Date} priceQuotes.asOfDateTime In local time based on the exchange. format: yyyy-MM-dd'T'HH:mm:ss.SSSZ.
 * @apiSuccess {Number} priceQuotes.bidPrice The bid price refers to the highest amount of money a prospective buyer is willing to spend for it.
 * @apiSuccess {Number} priceQuotes.bidSize The bid size represents the minimum quantity of a security an investor is willing to purchase at a specified bid price.
 * @apiSuccess {Number} priceQuotes.askPrice The ask price is the lowest price a seller of a stock is willing to accept for a share.
 * @apiSuccess {Number} priceQuotes.askSize The ask size is the amount of a security that a market maker is offering to sell at the ask price.
 * @apiSuccess {Number} priceQuotes.bidSpread A bid-ask spread is the amount by which the ask price exceeds the bid price for an asset in the market.
 * @apiSuccess {Number} priceQuotes.askSpread A bid-ask spread is the amount by which the ask price exceeds the bid price for an asset in the market.
 * @apiSuccess {Number} priceQuotes.openPrice The opening price is the price at which a stock first trades upon the opening of an exchange on a trading day.
 * @apiSuccess {Number} priceQuotes.previousClosePrice Stock's closing price on the preceding day of trading.
 * @apiSuccess {Number} priceQuotes.prevTradePrice The previous trading day's or session's last price.
 * @apiSuccess {Number} priceQuotes.dayLowPrice Lowest traded price for this trading day.
 * @apiSuccess {Number} priceQuotes.dayHighPrice Highest traded price for this trading day.
 * @apiSuccess {Number} priceQuotes.yearLowPrice The 52-Week Low indicates a stock is trading at its lowest price in the past 52 weeks.
 * @apiSuccess {Number} priceQuotes.yearHighPrice The 52-Week High indicates a stock is trading at its highest price in the past 52 weeks. 
 * @apiSuccess {Number} priceQuotes.volume A stock's volume refers to the number of shares that are sold, or traded, over a certain period of time (usually daily). 
 * @apiSuccess {Number} priceQuotes.boardLotSize A board lot is a standardized number of shares offered as a trading unit usually a minimum transaction size of 100 units/shares. 
 * @apiSuccess {Number} priceQuotes.marketCap Market capitalisation. 
 * @apiSuccess {Number} priceQuotes.sharesOutstanding Shares outstanding are all the shares of a corporation or financial asset that have been authorized, issued and purchased by investors and are held by them. 
 * @apiSuccess {Number} priceQuotes.peRatio The Price-to-Earnings Ratio or P/E ratio is a ratio for valuing a company that measures its current share price relative to its per-share earnings. 
 * @apiSuccess {Number} priceQuotes.eps Earnings per share (EPS) is the portion of a company's profit allocated to each share of common stock. 
 * @apiSuccess {Number} priceQuotes.iep Indicative Equilibrium Price. The IEP is the price at which the maximum number of shares can be traded if matching occurs at that time with the final IEP (if any) of each security determined at 09:20 HKT. 
 * @apiSuccess {Number} priceQuotes.iev Indicative Equilibrium Volume. The IEV is the quantity of shares that could be matched at the IEP. 
 * @apiSuccess {String} priceQuotes.auctionIndicator Y for auction period (when trading status = 'ORDER INPUT' or 'PRE-ORDER MATCHING' or 'ORDER MATCHING'), show IEP / IEV in FE. <br> N for non-auction period, hide IEP / IEV in FE. 
 * @apiSuccess {String} priceQuotes.brokerBidQueue Broker IDs in the bid queue. 
 * @apiSuccess {String} priceQuotes.brokerAskQueue Broker IDs in the ask queue. 
 * @apiSuccess {Number} priceQuotes.turnover Share turnover is a measure of stock liquidity calculated by dividing the total number of shares traded over a period by the average number of shares outstanding for the period. The higher the share turnover, the more liquid the share of the company. 
 * @apiSuccess {Number} priceQuotes.dividend A stock dividend is a dividend payment made in the form of additional shares rather than a cash payout. 
 * @apiSuccess {Number} priceQuotes.dividendYield The dividend yield is determined with this equation: dividend per share/price per share. 
 * @apiSuccess {Object[]} priceQuotes.bidAskQueues
 * @apiSuccess {Number} priceQuotes.bidAskQueues.bidPrice Price in the first - tenth slot of bid queue.
 * @apiSuccess {Number} priceQuotes.bidAskQueues.bidSize Number of shares in the first - tenth slot of bid queue. 7 decimal places
 * @apiSuccess {Number} priceQuotes.bidAskQueues.bidOrder Number of buyers in the first - tenth slot of bid queue.
 * @apiSuccess {Number} priceQuotes.bidAskQueues.askPrice Price in the first - tenth slot of ask queue. 7 decimal places
 * @apiSuccess {Number} priceQuotes.bidAskQueues.askSize Number of shares in the first - tenth slot of ask queue.
 * @apiSuccess {Number} priceQuotes.bidAskQueues.askOrder Number of buyers in the first - tenth slot of ask queue.
 * @apiSuccess {String} priceQuotes.vcmStatus Indicate the whether this stock is currently trade under VCM. <br> Y if and only if the current system time of MIDFS is between the VCM start time and end time (inclusive), otherwise N. <br> This field will be empty if the stock is not eligible for VCM. 
 * @apiSuccess {String} priceQuotes.vcmEligible <ul><li>Y – The instrument is in the scope of VCM.</li><li>N – The instrument is not in the scope of VCM.</li><li>Blank – VCM is not enabled in HKEx.</li></ul> 
 * @apiSuccess {Date}priceQuotes.vcmStartTime Start time of cooling off period. Blank if VCM is never triggered on the trading day. 
 * @apiSuccess {Date}priceQuotes.vcmEndTime End time of cooling off period. Blank if VCM is never triggered on the trading day. 
 * @apiSuccess {Number} priceQuotes.vcmLowerLimitPrice Lower bound of the allowed price band for sale order input in VCM. Blank if not available. 
 * @apiSuccess {Number} priceQuotes.vcmUpperLimitPrice Upper bound of the allowed price band for purchase order input in VCM. Blank if not available. 
 * @apiSuccess {String} priceQuotes.casElibible <ul><li>Y – The instrument is in the scope of CAS.</li><li>N – The instrument is not in the scope of CAS.</li><li>Blank – CAS is not enabled in HKEx.</li></ul> 
 * @apiSuccess {Number} priceQuotes.casLowerLimitPrice <ul><li>During the order input period of CAS (first 5 minutes), this will be the -5% of reference price.</li><li>During the no-cancelation period of CAS (6th – 7th minutes), this will be the lower band of the allowed price band.</li><li>Blank if not available.</li></ul> 
 * @apiSuccess {Number} priceQuotes.casUpperLimitPrice <ul><li>During the order input period of CAS (first 5 minutes), this will be the +5% of reference price.</li><li>During the no-cancelation period of CAS (6th – 7th minutes), this will be the upper band of the allowed price band.</li><li>Blank if not available.</li></ul> 
 * @apiSuccess {Number} priceQuotes.casReferencePrice <ul><li>Reference price for order input in CAS.</li><li>Blank if not available.</li></ul>
 * @apiSuccess {Number} priceQuotes.lowerLimitPrice This FID holds the Highest limit price.
 * @apiSuccess {Number} priceQuotes.upperLimitPrice This FID holds the Lowest limit price.
 * @apiSuccess {Number} priceQuotes.primaryLastActivity 
 * @apiSuccess {Number} priceQuotes.accumulatedVolume Accumulated volume scaling factor. 
 * @apiSuccess {Number} priceQuotes.unscaledTurnover The unscaled turnover value (summation of the value of all trades during the market day) for a particular instrument. 
 * @apiSuccess {Number} priceQuotes.totalSharesOutstanding The unscaled value for the total number of shares outstanding. 
 * @apiSuccess {Number} priceQuotes.totalIssuedShares Total amount of issued share. 
 * @apiSuccess {Number} priceQuotes.strikePrice Strike price.
 * @apiSuccess {Number} priceQuotes.callPrice Call price.
 * @apiSuccess {Number} priceQuotes.warrantType Warrant type (warrants and CBBC only). For non-warrant, a blank line is returned.
 * @apiSuccess {Number} priceQuotes.premium Premium.
 * @apiSuccess {Number} priceQuotes.maturityDate Maturity date. Format: yyyy-MM-dd.
 * @apiSuccess {Number} priceQuotes.callPutIndicator Call/Put flag (Warrants and CBBC only).
 * @apiSuccess {Number} priceQuotes.conversionRatio Conversion ratio (Warrants and CBBC only).
 * @apiSuccess {Number} remainingQuota Remaining quota. 
 * @apiSuccess {Number} totalQuota Total quota. 
 * @apiSuccess {Object[]} messages The warning messages returned once stock price quote is happening exceptions.
 * @apiSuccess {String} messages.reasonCode The reason code of the warning response.
 * @apiSuccess {String} messages.text The description of the warning response.
 * @apiSuccess {String} messages.traceCode The trace code of the warning response.
 * @apiSuccess {String} messages.productType The product type of stock which has exception in warning response.
 * @apiSuccess {String} messages.prodCdeAltClassCde The product class code of stock which has exception in warning response.
 * @apiSuccess {String} messages.prodAltNum The product number of stock which has exception in warning response.
 * 
 * @apiSuccessExample Success-Response:
 *  HTTP/1.1 200 OK
 *  {
 *    "priceQuotes": [
 *      {
 *        "prodAltNumSegs": [
 *          {
 *            "prodCdeAltClassCde": "I",
 *            "prodAltNum": "GB0005405286"
 *          },
 *          {
 *            "prodCdeAltClassCde": "M",
 *            "prodAltNum": "0005"
 *          },
 *          {
 *            "prodCdeAltClassCde": "P",
 *            "prodAltNum": "HSBA.HK"
 *          },
 *          {
 *            "prodCdeAltClassCde": "T",
 *            "prodAltNum": "0005.HK"
 *          },
 *          {
 *            "prodCdeAltClassCde": "W",
 *            "prodAltNum": "20001968"
 *          }
 *        ],
 *        "bidAskQueues": [
 *          {
 *            "bidPrice": null,
 *            "bidSize": 30000,
 *            "bidOrder": null,
 *            "askPrice": null,
 *            "askSize": 30800,
 *            "askOrder": null
 *          },
 *          {
 *            "bidPrice": null,
 *            "bidSize": null,
 *            "bidOrder": null,
 *            "askPrice": null,
 *            "askSize": null,
 *            "askOrder": null
 *          },
 *          {
 *            "bidPrice": null,
 *            "bidSize": null,
 *            "bidOrder": null,
 *            "askPrice": null,
 *            "askSize": null,
 *            "askOrder": null
 *          },
 *          {
 *            "bidPrice": null,
 *            "bidSize": null,
 *            "bidOrder": null,
 *            "askPrice": null,
 *            "askSize": null,
 *            "askOrder": null
 *          },
 *          {
 *            "bidPrice": null,
 *            "bidSize": null,
 *            "bidOrder": null,
 *            "askPrice": null,
 *            "askSize": null,
 *            "askOrder": null
 *          }
 *        ],
 *        "symbol": "0005",
 *        "currency": "HKD",
 *        "market": "HK",
 *        "exchangeCode": "HKG",
 *        "productType": "SEC",
 *        "companyName": "hhhh HOLDINGS PLC",
 *        "nominalPrice": 65.1,
 *        "dayLowPrice": 65,
 *        "dayHighPrice": 65.55,
 *        "peRatio": 12.02,
 *        "previousClosePrice": 65.2,
 *        "yearLowPrice": 60.35,
 *        "bidPrice": 65.1,
 *        "askPrice": 65.15,
 *        "asOfDateTime": "2019-05-21T07:51:22.000Z",
 *        "turnover": 761308548,
 *        "sharesOutstanding": null,
 *        "dividendYield": 6.09,
 *        "volume": 11665082,
 *        "changeAmount": -0.1,
 *        "changePercent": -0.15,
 *        "delay": true,
 *        "bidSize": 30000,
 *        "openPrice": 65.1,
 *        "askSize": 30800,
 *        "bidSpread": 0.05,
 *        "askSpread": 0.05,
 *        "yearHighPrice": 78.25,
 *        "boardLotSize": 400,
 *        "marketCap": 1317376,
 *        "brokerAskQueue": null,
 *        "brokerBidQueue": null,
 *        "vcmEligible": null,
 *        "casEligible": null,
 *        "vcmStatus": null,
 *        "casLowerPrice": null,
 *        "casUpperPrice": null,
 *        "vcmStartTime": null,
 *        "vcmEndTime": null,
 *        "vcmLowerLimitPrice": null,
 *        "vcmUpperLimitPrice": null,
 *        "upperLimitPrice": null,
 *        "lowerLimitPrice": null,
 *        "casLowerLimitPrice": null,
 *        "casUpperLimitPrice": null,
 *        "auctionIndicator": null,
 *        "eps": 5.416,
 *        "iep": null,
 *        "iev": null,
 *        "dividend": 3.9649,
 *        "prevTradePrice": null,
 *        "riskLvlCde": "5",
 *        "settlementPrice": null,
 *        "primaryLastActivity": null,
 *        "accumulatedVolume": null,
 *        "unscaledTurnover": null,
 *        "totalSharesOutstanding": null,
 *        "totalIssuedShares": null,
 *        "daySecLowLimPrice": null,
 *        "daySecUpperLimPrice": null,
 *        "dayLowLimPrice": null,
 *        "dayUpperLimPrice": null,
 *        "limitReferencePrice": null,
 *        "priceCode": null
 *      }
 *    ],
 *    "remainingQuota": -1,
 *    "totalQuota": -1,
 *    "messages": null
 *  }
 * 
 * @apiUse ErrorMsgResponse
 */
