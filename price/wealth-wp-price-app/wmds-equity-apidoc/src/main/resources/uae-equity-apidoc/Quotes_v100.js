/**
 * @api {get} /wealth/api/v1/market-data/equity/quotes Quotes
 * @apiName Quotes
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {Object[]} productKeys The key to uniquely locate a product.
 * @apiParam {String} productKeys.productType Product Type. Stock is "SEC".
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} market The investment market of the product. <ul><li>UK: United Kingdom</li><li>HK: Hong Kong</li><li>US: United States</li></ul>
 * @apiParam {Boolean} delay <ul><li>true: Delay Quote</li><li>false: Realtime Quote</li></ul>
 * @apiParam {String} requestType <ul><li>0 - Nominal price, All fields</li><li>1 - special request type for reserve only</li></ul>
 * @apiParam {String} processingFlag check if need to skip using SAML user id. 
 * @apiParamExample Request:
 *  {
 *  	"productKeys": [{
 *  		"productType": "SEC",
 *  		"prodCdeAltClassCde": "M",
 *  		"prodAltNum": "FFIC",
 *  	}],
 *  	"market": "US",
 *  	"delay": true
 *  }
 * 
 * @apiUse quotesResponse
 * 
 * @apiSuccessExample Success-Response:
 *  HTTP/1.1 200 OK
 *  {
 *  	"priceQuotes": [{
 *  		"prodAltNumSegs": [{
 *  			"prodCdeAltClassCde": "I",
 *  			"prodAltNum": "US3438731057"
 *  		},
 *  		{
 *  			"prodCdeAltClassCde": "M",
 *  			"prodAltNum": "FFIC"
 *  		},
 *  		{
 *  			"prodCdeAltClassCde": "P",
 *  			"prodAltNum": "US-FFIC"
 *  		},
 *  		{
 *  			"prodCdeAltClassCde": "T",
 *  			"prodAltNum": "FFIC.O"
 *  		},
 *  		{
 *  			"prodCdeAltClassCde": "W",
 *  			"prodAltNum": "20000798"
 *  		}],
 *  	    "bidAskQueues": [{
 *  			"bidPrice": null,
 *  			"bidSize": null,
 *  			"bidOrder": null,
 *  			"askPrice": null,
 *  			"askSize": null,
 *  			"askOrder": null
 *  		},
 *  		{
 *  			"bidPrice": null,
 *  			"bidSize": null,
 *  			"bidOrder": null,
 *  			"askPrice": null,
 *  			"askSize": null,
 *  			"askOrder": null
 *  		},
 *  		{
 *  			"bidPrice": null,
 *  			"bidSize": null,
 *  			"bidOrder": null,
 *  			"askPrice": null,
 *  			"askSize": null,
 *  			"askOrder": null
 *  		},
 *  		{
 *  			"bidPrice": null,
 *  			"bidSize": null,
 *  			"bidOrder": null,
 *  			"askPrice": null,
 *  			"askSize": null,
 *  			"askOrder": null
 *  		},
 *  		{
 *  			"bidPrice": null,
 *  			"bidSize": null,
 *  			"bidOrder": null,
 *  			"askPrice": null,
 *  			"askSize": null,
 *  			"askOrder": null
 *  		}],
 *  		"symbol": "FFIC",
 *  		"market": "US",
 *  		"exchangeCode": "NAQ",
 *  		"productType": "SEC",
 *  		"companyName": "Flushing Financial Corp",
 *  		"riskLvlCde": "3",
 *  		"nominalPrice": 22.08,
 *  		"settlementPrice": null,
 *  		"currency": "USD",
 *  		"changeAmount": 0,
 *  		"changePercent": 0,
 *  		"delay": true,
 *  		"asOfDateTime": "2019-01-22T16:00:00.000-05:00",
 *  		"bidPrice": 0.00,
 *  		"bidSize": 0,
 *  		"askPrice": 24.00,
 *  		"askSize": 4,
 *  		"bidSpread": null,
 *  		"askSpread": null,
 *  		"openPrice": 22.40,
 *  		"previousClosePrice": 22.42,
 *  		"prevTradePrice": null,
 *  		"dayLowPrice": 21.88,
 *  		"dayHighPrice": 22.50,
 *  		"yearLowPrice": 20.27,
 *  		"yearHighPrice": 29.975,
 *  		"volume": 60548,
 *  		"boardLotSize": 100,
 *  		"marketCap": null,
 *  		"peRatio": 12.1494,
 *  		"eps": 1.8174,
 *  		"iep": null,
 *  		"iev": null,
 *  		"auctionIndicator": null,
 *  		"brokerBidQueue": null,
 *  		"brokerAskQueue": null,
 *  		"turnover": "",
 *  		"dividend": 0,
 *  		"dividendYield": 3.6232,
 *  		"vcmStatus": null,
 *  		"vcmEligible": null,
 *  		"vcmStartTime": null,
 *  		"vcmEndTime": null,
 *  		"vcmLowerLimitPrice": null,
 *  		"vcmUpperLimitPrice": null,
 *  		"casElibible": null,
 *  		"casLowerLimitPrice": null,
 *  		"casUpperLimitPrice": null,
 *  		"lowerLimitPrice": null,
 *  		"upperLimitPrice": null,
 *  		"primaryLastActivity": null,
 *  		"accumulatedVolume": null,
 *  		"unscaledTurnover": null,
 *  		"totalSharesOutstanding": null,
 *  		"totalIssuedShares": null,
 *  		"remainingQuota": -1,
 *  		"totalQuota": -1
 *  	}],
 *  	"messages": null
 *  }
 * 
 * @apiUse ErrorMsgResponse
 */

/**
 * @apiDefine quotesResponse
 * 
 * @apiSuccess {Object[]} priceQuotes
 * @apiSuccess {Object[]} priceQuotes.prodAltNumSegs
 * @apiSuccess {String} priceQuotes.prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>WPC</td><td>prodCdeAltClassCde</td></tr></table>
 * @apiSuccess {String} priceQuotes.prodAltNumSegs.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>WPC</td><td>prodAltNum</td></tr></table>
 * @apiSuccess {String} priceQuotes.symbol A stock symbol is a unique series of letters assigned to a security for trading purposes. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>WPC</td><td>symbol</td></tr></table>
 * @apiSuccess {String} priceQuotes.market Stock Market. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>WPC</td><td>market</td></tr></table>
 * @apiSuccess {String} priceQuotes.exchangeCode Stock Exchange. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>WPC</td><td>exchange</td></tr></table>
 * @apiSuccess {String} priceQuotes.productType Product Type. Stock is "SEC". <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>WPC</td><td>productType</td></tr></table>
 * @apiSuccess {String} priceQuotes.companyName Company Name. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>WPC</td><td>productName</td></tr></table>
 * @apiSuccess {String} priceQuotes.riskLvlCde Risk Level Code. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>WPC</td><td>riskLvlCde</td></tr></table>
 * @apiSuccess {Number} priceQuotes.nominalPrice Price quotations on futures for a period in which no actual trading took place. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>HKEX -> OFF_CLOSE, TRDPRC_1, ADJUST_CLS <br> Others -> TRDPRC_1, ADJUST_CLS</td></tr></table>
 * @apiSuccess {Number} priceQuotes.settlementPrice A settlement price, in the derivatives markets, is the price used for determining profit or loss for the day, as well as margin requirements. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr></table>
 * @apiSuccess {String} priceQuotes.currency Trading currency. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>CURRENCY</td></tr></table>
 * @apiSuccess {Number} priceQuotes.changeAmount Change in amount of the share price versus previous closing price. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>HKEX&NAQ -> OFF_CLOSE - ADJUST_CLS, NETCHNG_1 <br> Others -> NETCHNG_1</td></tr></table>
 * @apiSuccess {Number} priceQuotes.changePercent Change in percentage of the share price versus previous closing price. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>HKEX&NAQ -> (OFF_CLOSE - ADJUST_CLS) / ADJUST_CLS * 100, PCTCHNG <br> Others -> PCTCHNG</td></tr></table>
 * @apiSuccess {Boolean} priceQuotes.delay Delay Quote or Realtime Quote. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>MDS</td><td>N/A</td></tr></table>
 * @apiSuccess {Date} priceQuotes.asOfDateTime In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSZ. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>TRADE_DATE + TRDTIM_1</td></tr></table>
 * @apiSuccess {Number} priceQuotes.bidPrice The bid price refers to the highest amount of money a prospective buyer is willing to spend for it. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>BID</td></tr></table>
 * @apiSuccess {Number} priceQuotes.bidSize The bid size represents the minimum quantity of a security an investor is willing to purchase at a specified bid price. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>BIDSIZE</td></tr></table>
 * @apiSuccess {Number} priceQuotes.askPrice The ask price is the lowest price a seller of a stock is willing to accept for a share. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>ASK</td></tr></table>
 * @apiSuccess {Number} priceQuotes.askSize The ask size is the amount of a security that a market maker is offering to sell at the ask price. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>ASKSIZE</td></tr></table>
 * @apiSuccess {Number} priceQuotes.bidSpread A bid-ask spread is the amount by which the ask price exceeds the bid price for an asset in the market. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>UPPER_SPRD</td></tr></table>
 * @apiSuccess {Number} priceQuotes.askSpread A bid-ask spread is the amount by which the ask price exceeds the bid price for an asset in the market. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>LOWER_SPRD</td></tr></table>
 * @apiSuccess {Number} priceQuotes.openPrice The opening price is the price at which a stock first trades upon the opening of an exchange on a trading day. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>OPEN_PRC</td></tr></table>
 * @apiSuccess {Number} priceQuotes.previousClosePrice Stock's closing price on the preceding day of trading. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>HST_CLOSE</td></tr></table>
 * @apiSuccess {Number} priceQuotes.prevTradePrice The previous trading day's or session's last price. 
 * @apiSuccess {Number} priceQuotes.dayLowPrice Lowest traded price for this trading day. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>LOW_1</td></tr></table>
 * @apiSuccess {Number} priceQuotes.dayHighPrice Highest traded price for this trading day. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>HIGH_1</td></tr></table>
 * @apiSuccess {Number} priceQuotes.yearLowPrice The 52-Week Low indicates a stock is trading at its lowest price in the past 52 weeks. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>HKEX -> 52WK_LOW <br> Others -> YRLOW</td></tr></table>
 * @apiSuccess {Number} priceQuotes.yearHighPrice The 52-Week High indicates a stock is trading at its highest price in the past 52 weeks. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>HKEX -> 52WK_HIGH <br> Others -> YRHIGH</td></tr></table>
 * @apiSuccess {Number} priceQuotes.volume A stock's volume refers to the number of shares that are sold, or traded, over a certain period of time (usually daily). <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>ACVOL_1</td></tr></table>
 * @apiSuccess {Number} priceQuotes.boardLotSize A board lot is a standardized number of shares offered as a trading unit usually a minimum transaction size of 100 units/shares. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>LOT_SIZE_A</td></tr></table>
 * @apiSuccess {Number} priceQuotes.marketCap Market capitalisation. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>HKEX -> MKT_CAP <br> Others -> DSS_MKT_CAP</td></tr></table>
 * @apiSuccess {Number} priceQuotes.sharesOutstanding Shares outstanding are all the shares of a corporation or financial asset that have been authorized, issued and purchased by investors and are held by them.
 * @apiSuccess {Number} priceQuotes.peRatio The Price-to-Earnings Ratio or P/E ratio is a ratio for valuing a company that measures its current share price relative to its per-share earnings. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>PERATIO</td></tr></table>
 * @apiSuccess {Number} priceQuotes.eps Earnings per share (EPS) is the portion of a company's profit allocated to each share of common stock. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>EARNINGS</td></tr></table>
 * @apiSuccess {Number} priceQuotes.iep Indicative Equilibrium Price. The IEP is the price at which the maximum number of shares can be traded if matching occurs at that time with the final IEP (if any) of each security determined at 09:20 HKT. 
 * @apiSuccess {Number} priceQuotes.iev Indicative Equilibrium Volume. The IEV is the quantity of shares that could be matched at the IEP. 
 * @apiSuccess {String} priceQuotes.auctionIndicator Y for auction period (when trading status = 'ORDER INPUT' or 'PRE-ORDER MATCHING' or 'ORDER MATCHING'), show IEP / IEV in FE. <br> N for non-auction period, hide IEP / IEV in FE.
 * @apiSuccess {String} priceQuotes.brokerBidQueue Broker IDs in the bid queue. 
 * @apiSuccess {String} priceQuotes.brokerAskQueue Broker IDs in the ask queue. 
 * @apiSuccess {Number} priceQuotes.turnover Share turnover is a measure of stock liquidity calculated by dividing the total number of shares traded over a period by the average number of shares outstanding for the period. The higher the share turnover, the more liquid the share of the company. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>TURNOVER</td></tr></table>
 * @apiSuccess {Number} priceQuotes.dividend A stock dividend is a dividend payment made in the form of additional shares rather than a cash payout. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>DIVIDEND</td></tr></table>
 * @apiSuccess {Number} priceQuotes.dividendYield The dividend yield is determined with this equation: dividend per share/price per share. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>YIELD</td></tr></table>
 * @apiSuccess {Object[]} priceQuotes.bidAskQueues
 * @apiSuccess {Number} priceQuotes.bidAskQueues.bidPrice <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>BEST_BID1 - BEST_BID10</td></tr></table>
 * @apiSuccess {Number} priceQuotes.bidAskQueues.bidSize <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>BEST_BSIZ1 - BEST_BSIZ10</td></tr></table>
 * @apiSuccess {Number} priceQuotes.bidAskQueues.bidOrder <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>NO_BIDMMKR1 - NO_BIDMMKR10</td></tr></table>
 * @apiSuccess {Number} priceQuotes.bidAskQueues.askPrice <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>BEST_ASK1 - BEST_ASK10</td></tr></table>
 * @apiSuccess {Number} priceQuotes.bidAskQueues.askSize <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>BEST_ASIZ1 - BEST_ASIZ10</td></tr></table>
 * @apiSuccess {Number} priceQuotes.bidAskQueues.askOrder <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>TRIS</td><td>NO_ASKMMKR1 - NO_ASKMMKR10</td></tr></table>
 * @apiSuccess {String} priceQuotes.vcmStatus Indicate the whether this stock is currently trade under VCM. <br> Y if and only if the current system time of MIDFS is between the VCM start time and end time (inclusive), otherwise N. <br> This field will be empty if the stock is not eligible for VCM.
 * @apiSuccess {String} priceQuotes.vcmEligible <ul><li>Y – The instrument is in the scope of VCM.</li><li>N – The instrument is not in the scope of VCM.</li><li>Blank – VCM is not enabled in HKEx.</li></ul> 
 * @apiSuccess {Date}priceQuotes.vcmStartTime Start time of cooling off period. Blank if VCM is never triggered on the trading day. 
 * @apiSuccess {Date}priceQuotes.vcmEndTime End time of cooling off period. Blank if VCM is never triggered on the trading day. 
 * @apiSuccess {Number} priceQuotes.vcmLowerLimitPrice Lower bound of the allowed price band for sale order input in VCM. Blank if not available. 
 * @apiSuccess {Number} priceQuotes.vcmUpperLimitPrice Upper bound of the allowed price band for purchase order input in VCM. Blank if not available. 
 * @apiSuccess {String} priceQuotes.casElibible <ul><li>Y – The instrument is in the scope of CAS.</li><li>N – The instrument is not in the scope of CAS.</li><li>Blank – CAS is not enabled in HKEx.</li></ul> 
 * @apiSuccess {Number} priceQuotes.casLowerLimitPrice <ul><li>During the order input period of CAS (first 5 minutes), this will be the -5% of reference price.</li><li>During the no-cancelation period of CAS (6th – 7th minutes), this will be the lower band of the allowed price band.</li><li>Blank if not available.</li></ul> 
 * @apiSuccess {Number} priceQuotes.casUpperLimitPrice <ul><li>During the order input period of CAS (first 5 minutes), this will be the +5% of reference price.</li><li>During the no-cancelation period of CAS (6th – 7th minutes), this will be the upper band of the allowed price band.</li><li>Blank if not available.</li></ul> 
 * @apiSuccess {Number} priceQuotes.lowerLimitPrice 
 * @apiSuccess {Number} priceQuotes.upperLimitPrice 
 * @apiSuccess {Number} priceQuotes.primaryLastActivity
 * @apiSuccess {Number} priceQuotes.accumulatedVolume Accumulated Volume scaling factor.
 * @apiSuccess {Number} priceQuotes.unscaledTurnover The unscaled turnover value (summation of the value of all trades during the market day) for a particular instrument.
 * @apiSuccess {Number} priceQuotes.totalSharesOutstanding The unscaled value for the total number of shares outstanding.
 * @apiSuccess {Number} priceQuotes.totalIssuedShares Total amount of issued share.
 * @apiSuccess {Number} priceQuotes.remainingQuota Remaining Quota. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>MDS</td><td>N/A</td></tr></table>
 * @apiSuccess {Number} priceQuotes.totalQuota Total Quota. <table><tr><td>Effective To</td><td>Data Source</td><td>Field Mapping</td></tr><tr><td>UAE EQTY</td><td>MDS</td><td>N/A</td></tr></table>
 * @apiSuccess {Object[]} priceQuotes.messages
 */