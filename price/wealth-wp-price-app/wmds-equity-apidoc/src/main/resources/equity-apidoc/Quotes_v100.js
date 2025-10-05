/**
 * @api {get} /wealth/api/v1/market-data/equity/quotes Quotes
 * @apiName Quotes
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {Object[]} productKeys The key to uniquely locate a product.
 * @apiParam {String} productKeys.productType Product Type.
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKeys.market The investment market of the product. <ul><li>HK: Hong Kong</li></ul>
 * @apiParam {Boolean} delay <ul><li>true: Delay Quote</li></ul>
 * 
 * @apiParamExample Request:
 *  {
 *  	"productKeys": [{
 *  		"productType": "SEC",
 *  		"prodCdeAltClassCde": "M",
 *  		"prodAltNum": "6862",
 *  		"market": "HK"
 *  	}],
 *  	"delay": true
 *  }
 * 
 * @apiUse quotesResponse
 * 
 * @apiSuccessExample Success-Response:
 *  HTTP/1.1 200 OK
 *  {
 *  	"priceQuotes": [{
 *  		"symbol": "6862.HK",
 *  		"companyName": "",
 *  		"productStatus": "",
 *  		"currency": "HKD",
 *  		"nominalPrice": 18.800,
 *  		"changeAmount": -0.020,
 *  		"changePercent": -0.11,
 *  		"dayLowPrice": 18.520,
 *  		"dayHighPrice": 18.820,
 *  		"volume": 153148,
 *  		"previousClosePrice": 18.820,
 *  		"bidPrice": 18.760,
 *  		"askPrice": 18.820,
 *  		"boardLotSize": 1000,
 *  		"asOfDateTime": "",
 *  		"turnover": 2868070,
 *  		"yearLowPrice": "",
 *  		"yearHighPrice": "",
 *  		"marketCap": 99640,
 *  		"peRatio": 61.86,
 *  		"eps": 0.304,
 *  		"dividendYield": "",
 *  		"securityType": "",
 *  		"maturityDate": "",
 *  		"callPrice": "",
 *  		"strikePrice": "",
 *  		"openPrice": 18.520,
 *  		"iep": 0,
 *  		"iev": 0,
 *  		"spreadBidPrice": 0.020,
 *  		"spreadAskPrice": 0.020,
 *  		"warrantType": "",
 *  		"underlyingStock": "",
 *  		"lastTradeDate": "",
 *  		"callPutIndicator": "",
 *  		"conversionRatio": "",
 *  		"premium": "",
 *  		"nominalPriceType": "",
 *  		"brokerAskQueue": "",
 *  		"brokerBidQueue": "",
 *  		"vcmEligibleIndicator": "",
 *  		"casEligibleIndicator": "CA    ",
 *  		"vcmStatus": "",
 *  		"orderImbalanceDirection": "",
 *  		"orderImbalanceQuantity": "",
 *  		"casLowerLimitPrice": "",
 *  		"casUpperPrice": "",
 *  		"vcmStartTime": "",
 *  		"vcmEndTime": "",
 *  		"vcmLowerPrice": "",
 *  		"vcmUpperPrice": "",
 *  		"sessionType": "",
 *  		"spreadCode": ""
 *  	}],
 *  	"totalFreeQuote": "",
 *  	"quoteRemaining": "",
 *  	"delay": ""
 *  }
 * 
 * @apiUse ErrorMsgResponse
 */

/**
 * @apiDefine quotesResponse
 * 
 * @apiSuccess {Object[]} priceQuotes
 * @apiSuccess {String} priceQuotes.symbol Symbol of request stock.
 * @apiSuccess {String} priceQuotes.companyName The company name of the specified symbol.
 * @apiSuccess {String} [priceQuotes.productStatus] Trading status of the product. <ul><li>normal</li><li>halted: if and only if the stock is suspended for trading</li></ul>
 * @apiSuccess {String} [priceQuotes.currency] Trading currency of the specified symbol.
 * @apiSuccess {Number} priceQuotes.nominalPrice Nominal price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.changeAmount] Change in amount of the nominal price versus previous closing price. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.changePercent] Change in percentage of the nominal price versus previous closing price. 4 decimal places.
 * @apiSuccess {Number} [priceQuotes.dayLowPrice] Lowest traded price for this trading day. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.dayHighPrice] Highest traded price for this trading day. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.volume] Number of shares traded in this trading day.
 * @apiSuccess {Number} [priceQuotes.previousClosePrice] Previous closing price. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.bidPrice] Best bid price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.askPrice] Best ask price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.boardLotSize] Board lot size of the specified symbol.
 * @apiSuccess {Date} [priceQuotes.asOfDateTime] Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSZ.
 * @apiSuccess {Number} [priceQuotes.turnover] Turnover of the specified symbol. 4 decimal places.
 * @apiSuccess {Number} [priceQuotes.yearLowPrice] Year low price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.yearHighPrice] Year high price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.marketCap] Market capitalisation.
 * @apiSuccess {Number} [priceQuotes.peRatio] P/E ration.
 * @apiSuccess {Number} [priceQuotes.eps] Earnings per share of the specified symbol.
 * @apiSuccess {Number} [priceQuotes.dividendYield] Dividend yield percentage.
 * @apiSuccess {String} [priceQuotes.securityType] Security Type. <ul><li>CBND: Bonds</li><li>COMN: Equities</li><li>TRST: Trusts</li><li>WRNT: Warrants</li><li>PREF: Preferred stocks</li><li>DWAR: Derivative Warrants</li><li>RGHT: Rights</li><li>CBBC: Callable Bull/Bear Contracts</li></ul>
 * @apiSuccess {Date} [priceQuotes.maturityDate] Maturity date of derivatives.
 * @apiSuccess {Number} [priceQuotes.callPrice] Call price of derivatives.
 * @apiSuccess {Number} [priceQuotes.strikePrice] Strike price of derivatives
 * @apiSuccess {Number} [priceQuotes.openPrice] First traded price for auto-trading session. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.iep] Indicative equilibrium price.
 * @apiSuccess {Number} [priceQuotes.iev] Indicative equilibrium volume.
 * @apiSuccess {Number} [priceQuotes.spreadBidPrice] Spread of bid price.
 * @apiSuccess {Number} [priceQuotes.spreadAskPrice] Spread of ask price.
 * @apiSuccess {String} [priceQuotes.warrantType] Warrant type (warrants and CBBC only).
 * @apiSuccess {String} [priceQuotes.underlyingStock] Underlying stock name, code and nominal price (warrants and CBBC only).
 * @apiSuccess {Date} [priceQuotes.lastTradeDate] Last trading date (warrants and CBBC only).
 * @apiSuccess {String} [priceQuotes.callPutIndicator] Call/Put flag (warrants and CBBC only). <ul><li>C - Call</li><li>P - Put</li></ul>
 * @apiSuccess {Number} [priceQuotes.conversionRatio] Conversion ratio (warrants and CBBC only).
 * @apiSuccess {Number} [priceQuotes.premium] Premium.
 * @apiSuccess {String} [priceQuotes.nominalPriceType] Nominal price type of the specified symbol. <ul><li>N|X: Nominal</li><li>C|Y: Closing</li></ul>
 * @apiSuccess {Number} [priceQuotes.brokerAskQueue] Broker IDs in the ask queue.
 * @apiSuccess {Number} [priceQuotes.brokerBidQueue] Broker IDs in the bid queue.
 * @apiSuccess {String} [priceQuotes.vcmEligibleIndicator] VCM eligible. <ul><li>Y: The instrument is in the scope of VCM.</li><li>N: The instrument is not in the scope of VCM.</li><li>Blank: VCM is not enabled in HKEx.</li></ul>
 * @apiSuccess {String} [priceQuotes.casEligibleIndicator] CAS eligible. <ul><li>Y: The instrument is in the scope of CAS.</li><li>N: The instrument is not in the scope of CAS.</li><li>Blank: CAS is not enabled in HKEx.</li></ul>
 * @apiSuccess {String} [priceQuotes.vcmStatus] Indicate whether this stock is currently trade under VCM. <ul><li>Y: if and only if the current system time of MIDFS is between the VCM start time and end time (inclusive)</li><li>N</li></ul>
 * @apiSuccess {String} [priceQuotes.orderImbalanceDirection] Indicates the imbalance direction when the matchable by quantity and sell quantity at IEP are not equal. <ul><li>N: Matchable buy and sell quantities are equal.</li><li>B: Buy surplus</li><li>S: Sell surplus</li><li>Space: N/A</li><li>Blank: No value is populated from OMD-C</li></ul>
 * @apiSuccess {Number} [priceQuotes.orderImbalanceQuantity] The absolute difference between matchable buy and sell quantity at IEP. Immaterial if order imbalance direction is blank.
 * @apiSuccess {Number} [priceQuotes.casLowerLimitPrice] CAS lower price. 7 decimal places. <ul><li>During the order input period of CAS (first 5 minutes), this will be the -5% of reference price.</li><li>During the no-cancelation period of CAS (6th to 7th minutes), this will be the lower band of the allowed price band.</li><li>Blank if not available.</li></ul>
 * @apiSuccess {Number} [priceQuotes.casUpperPrice] CAS upper price. 7 decimal places. <ul><li>During the order input period of CAS (first 5 minutes), this will be the +5% of reference price.</li><li>During the no-cancelation period of CAS (6th to 7th minutes), this will be the upper band of the allowed price band.</li><li>Blank if not available.</li></ul>
 * @apiSuccess {Date} [priceQuotes.vcmStartTime] Start time of cooling off period.
 * @apiSuccess {Date} [priceQuotes.vcmEndTime] End time of cooling off period.
 * @apiSuccess {Number} [priceQuotes.vcmLowerPrice] Lower bound of the allowed price band for sale order input in VCM. Blank if not available. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.vcmUpperPrice] Upper bound of the allowed price band for purchase order input in VCM. Blank if not available. 7 decimal places.
 * @apiSuccess {String} [priceQuotes.sessionType] Trading session type of the market which the stock belongs to. <ul><li>A: Auction (pre-opening/closing)</li><li>C: Continuous trading (including lunch break)</li><li>Blank: Other time period</li></ul>
 * @apiSuccess {String} [priceQuotes.spreadCode] Spread table code.
 * @apiSuccess {Number} [quoteRemaining] Quote count remaining.
 * @apiSuccess {Number} [totalFreeQuote] Total free quote count.
 * @apiSuccess {Boolean} [delay] true: delayed quote; false: real-time quote.
 */