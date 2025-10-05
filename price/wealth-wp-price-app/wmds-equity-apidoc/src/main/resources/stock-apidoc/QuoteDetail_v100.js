/**
 * @api {get} /wealth/api/v1/market-data/stock/quoteDetail Quote Detail
 * @apiName Quote Detail
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {String} market The investment market of the product. <ul><li>HK: Hong Kong</li></ul>
 * @apiParam {String} productType Product Type. Product Type.
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {Boolean} delay <ul><li>true: Delay Quote</li></ul>
 * 
 * @apiParamExample Request:
 *	{
 * 		"market" : "HK",
 * 		"productType" : "SEC",
 * 		"prodCdeAltClassCde" : "M",
 * 		"prodAltNum" : "00005",
 * 		"delay" : true
 * 	}
 *
 * @apiUse priceQuote
 * @apiUse bidAskQuotes
 *
 * @apiSuccessExample Success-Response:
 *  HTTP/1.1 200 OK
 *  {
 *  	"priceQuote": {
 *  		"symbol": "00005",
 *  		"companyName": "hhhh HOLDINGS",
 *  		"productStatus": "normal",
 *  		"currency": "HKD",
 *  		"nominalPrice": 76.1500000,
 *  		"changeAmount": 0.1500000,
 *  		"changePercent": 0.1970,
 *  		"dayLowPrice": 75.7000000,
 *  		"dayHighPrice": 76.4000000,
 *  		"volume": 16883480,
 *  		"previousClosePrice": 76.0000000,
 *  		"bidPrice": 76.1500000,
 *  		"askPrice": 76.1500000,
 *  		"boardLotSize": 400,
 *  		"asOfDateTime": "2018-11-02T16:01:06.000+08:00",
 *  		"turnover": 1283295412.0000,
 *  		"yearLowPrice": 56.6000000,
 *  		"yearHighPrice": 79.5500000,
 *  		"marketCap": 1556113066000,
 *  		"peRatio": 140.2910,
 *  		"eps": 0.5428,
 *  		"dividendYield": 5.0910,
 *  		"securityType": "COMN",
 *  		"maturityDate": "",
 *  		"callPrice": "",
 *  		"strikePrice": "",
 *  		"openPrice": 76.0000000,
 *  		"iep": 76.1500000,
 *  		"iev": 5057600,
 *  		"spreadBidPrice": 0.050,
 *  		"spreadAskPrice": 0.050,
 *  		"warrantType": "",
 *  		"underlyingStock": "",
 *  		"lastTradeDate": "",
 *  		"callPutIndicator": "",
 *  		"conversionRatio": "",
 *  		"premium": "",
 *  		"nominalPriceType": "N",
 *  		"brokerAskQueue": "",
 *  		"brokerBidQueue": "",
 *  		"vcmEligibleIndicator": "Y",
 *  		"casEligibleIndicator": "Y",
 *  		"vcmStatus": "N",
 *  		"orderImbalanceDirection": "S",
 *  		"orderImbalanceQuantity": 53600,
 *  		"casLowerPrice": 72.1500000,
 *  		"casUpperPrice": 79.6500000,
 *  		"vcmStartTime": "",
 *  		"vcmEndTime": "",
 *  		"vcmLowerPrice": "",
 *  		"vcmUpperPrice": "",
 *  		"sessionType": "A",
 *  		"spreadCode": "01",
 *  		"quoteRemaining": "",
 *  		"totalFreeQuote": "",
 *  		"delay": ""
 *  	},
 *  	"bidAskQuotes": []
 *  }
 *  
 * @apiUse ErrorMsgResponse
 */

/**
 * @apiDefine priceQuote
 * 
 * @apiSuccess {Object} priceQuote 
 * @apiSuccess {String} priceQuote.symbol Symbol of request stock.
 * @apiSuccess {String} priceQuote.companyName The company name of the specified symbol.
 * @apiSuccess {String} [priceQuote.productStatus] Trading status of the product. <ul><li>normal</li><li>halted: if and only if the stock is suspended for trading</li></ul>
 * @apiSuccess {String} [priceQuote.currency] Trading currency of the specified symbol.
 * @apiSuccess {Number} priceQuote.nominalPrice Nominal price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.changeAmount] Change in amount of the nominal price versus previous closing price. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.changePercent] Change in percentage of the nominal price versus previous closing price. 4 decimal places.
 * @apiSuccess {Number} [priceQuote.dayLowPrice] Lowest traded price for this trading day. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.dayHighPrice] Highest traded price for this trading day. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.volume] Number of shares traded in this trading day.
 * @apiSuccess {Number} [priceQuote.previousClosePrice] Previous closing price. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.bidPrice] Best bid price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.askPrice] Best ask price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.boardLotSize] Board lot size of the specified symbol.
 * @apiSuccess {Date} [priceQuote.asOfDateTime] Last updated time for the specified symbol. In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSZ.
 * @apiSuccess {Number} [priceQuote.turnover] Turnover of the specified symbol. 4 decimal places.
 * @apiSuccess {Number} [priceQuote.yearLowPrice] Year low price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.yearHighPrice] Year high price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.marketCap] Market capitalisation.
 * @apiSuccess {Number} [priceQuote.peRatio] P/E ration.
 * @apiSuccess {Number} [priceQuote.eps] Earnings per share of the specified symbol.
 * @apiSuccess {Number} [priceQuote.dividendYield] Dividend yield percentage.
 * @apiSuccess {String} [priceQuote.securityType] Security Type. <ul><li>CBND: Bonds</li><li>COMN: Equities</li><li>TRST: Trusts</li><li>WRNT: Warrants</li><li>PREF: Preferred stocks</li><li>DWAR: Derivative Warrants</li><li>RGHT: Rights</li><li>CBBC: Callable Bull/Bear Contracts</li></ul>
 * @apiSuccess {Date} [priceQuote.maturityDate] Maturity date of derivatives.
 * @apiSuccess {Number} [priceQuote.callPrice] Call price of derivatives.
 * @apiSuccess {Number} [priceQuote.strikePrice] Strike price of derivatives
 * @apiSuccess {Number} [priceQuote.openPrice] First traded price for auto-trading session. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.iep] Indicative equilibrium price.
 * @apiSuccess {Number} [priceQuote.iev] Indicative equilibrium volume.
 * @apiSuccess {Number} [priceQuote.spreadBidPrice] Spread of bid price.
 * @apiSuccess {Number} [priceQuote.spreadAskPrice] Spread of ask price.
 * @apiSuccess {String} [priceQuote.warrantType] Warrant type (warrants and CBBC only).
 * @apiSuccess {String} [priceQuote.underlyingStock] Underlying stock name, code and nominal price (warrants and CBBC only).
 * @apiSuccess {Date} [priceQuote.lastTradeDate] Last trading date (warrants and CBBC only).
 * @apiSuccess {String} [priceQuote.callPutIndicator] Call/Put flag (warrants and CBBC only). <ul><li>C - Call</li><li>P - Put</li></ul>
 * @apiSuccess {Number} [priceQuote.conversionRatio] Conversion ratio (warrants and CBBC only).
 * @apiSuccess {Number} [priceQuote.premium] Premium.
 * @apiSuccess {String} [priceQuote.nominalPriceType] Nominal price type of the specified symbol. <ul><li>N|X: Nominal</li><li>C|Y: Closing</li></ul>
 * @apiSuccess {Number} [priceQuote.brokerAskQueue] Broker IDs in the ask queue.
 * @apiSuccess {Number} [priceQuote.brokerBidQueue] Broker IDs in the bid queue.
 * @apiSuccess {String} [priceQuote.vcmEligibleIndicator] VCM eligible. <ul><li>Y: The instrument is in the scope of VCM.</li><li>N: The instrument is not in the scope of VCM.</li><li>Blank: VCM is not enabled in HKEx.</li></ul>
 * @apiSuccess {String} [priceQuote.casEligibleIndicator] CAS eligible. <ul><li>Y: The instrument is in the scope of CAS.</li><li>N: The instrument is not in the scope of CAS.</li><li>Blank: CAS is not enabled in HKEx.</li></ul>
 * @apiSuccess {String} [priceQuote.vcmStatus] Indicate whether this stock is currently trade under VCM. <ul><li>Y: if and only if the current system time of MIDFS is between the VCM start time and end time (inclusive)</li><li>N</li></ul>
 * @apiSuccess {String} [priceQuote.orderImbalanceDirection] Indicates the imbalance direction when the matchable by quantity and sell quantity at IEP are not equal. <ul><li>N: Matchable buy and sell quantities are equal.</li><li>B: Buy surplus</li><li>S: Sell surplus</li><li>Space: N/A</li><li>Blank: No value is populated from OMD-C</li></ul>
 * @apiSuccess {Number} [priceQuote.orderImbalanceQuantity] The absolute difference between matchable buy and sell quantity at IEP. Immaterial if order imbalance direction is blank.
 * @apiSuccess {Number} [priceQuote.casLowerPrice] CAS lower price. 7 decimal places. <ul><li>During the order input period of CAS (first 5 minutes), this will be the -5% of reference price.</li><li>During the no-cancelation period of CAS (6th to 7th minutes), this will be the lower band of the allowed price band.</li><li>Blank if not available.</li></ul>
 * @apiSuccess {Number} [priceQuote.casUpperPrice] CAS upper price. 7 decimal places. <ul><li>During the order input period of CAS (first 5 minutes), this will be the +5% of reference price.</li><li>During the no-cancelation period of CAS (6th to 7th minutes), this will be the upper band of the allowed price band.</li><li>Blank if not available.</li></ul>
 * @apiSuccess {Date} [priceQuote.vcmStartTime] Start time of cooling off period.
 * @apiSuccess {Date} [priceQuote.vcmEndTime] End time of cooling off period.
 * @apiSuccess {Number} [priceQuote.vcmLowerPrice] Lower bound of the allowed price band for sale order input in VCM. Blank if not available. 7 decimal places.
 * @apiSuccess {Number} [priceQuote.vcmUpperPrice] Upper bound of the allowed price band for purchase order input in VCM. Blank if not available. 7 decimal places.
 * @apiSuccess {String} [priceQuote.sessionType] Trading session type of the market which the stock belongs to. <ul><li>A: Auction (pre-opening/closing)</li><li>C: Continuous trading (including lunch break)</li><li>Blank: Other time period</li></ul>
 * @apiSuccess {String} [priceQuote.spreadCode] Spread table code.
 * @apiSuccess {Number} [priceQuote.quoteRemaining] Quote count remaining.
 * @apiSuccess {Number} [priceQuote.totalFreeQuote] Total free quote count.
 * @apiSuccess {Boolean} [priceQuote.delay] true: delayed quote; false: real-time quote.
 */

/**
 * @apiDefine bidAskQuotes
 *
 * @apiSuccess {Object[]} [bidAskQuotes]
 * @apiSuccess {Number} bidAskQuotes.bidOrder Order count - bid queue.
 * @apiSuccess {Number} bidAskQuotes.bidSize Order volume - bid queue.
 * @apiSuccess {Number} bidAskQuotes.bidPrice Order price - bid queue. 7 decimal places.
 * @apiSuccess {Number} bidAskQuotes.askOrder Order count - ask queue.
 * @apiSuccess {Number} bidAskQuotes.askSize Order volume - ask queue.
 * @apiSuccess {Number} bidAskQuotes.askPrice Order price - ask queue. 7 decimal places.
 */