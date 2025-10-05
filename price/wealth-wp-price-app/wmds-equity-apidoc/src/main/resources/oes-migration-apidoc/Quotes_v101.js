/**
 * @api {get} /wealth/api/v1/market-data/quotes Quotes
 * @apiName Quotes
 * @apiGroup Quote
 * @apiVersion 1.0.1
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {String} market The investment market of the product. <ul><li>HK: Hong Kong</li><li>US: United States</li></ul>
 * @apiParam {String} productType Product Type. Stock is "SEC".
 * @apiParam {String} prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String[]} prodAltNums Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {Boolean} delay <ul><li>true: Delay,false:realy Quote</li></ul>
 * 
 * @apiParamExample Request:
 *  {
 *      "prodAltNums":["0001.HK","0005.HK"],
 *      "market": "US",
 *      "delay": true,
 *      "prodCdeAltClassCde": "M",
 *      "productType": "SEC"  
 *  }
 * 
 * @apiUse quotesResponse
 * @apiSuccessExample Success-Response:
 *       {
 *	 
 *	       "priceQuotes": [{
 *			  "prodAltNumSegs": [{
 *				"prodCdeAltClassCde": "M",
 *				"prodAltNum": "FFIC"
 *					 
 *				}
 *			],
 *			  "symbol": "6862",
 *			  "market": "HK",
 *			  "exchangeCode": "HKEX",
 *			  "productType": "SEC",
 *			  "companyName": "HAIDILAO INTERNATIONAL HOLDING LTD",
 *			  "marketClosed": true,
 *			  "nominalPrice": 17.100,
 *			  "currency": "HKD",
 *			  "changeAmount": -0.100,
 *			  "changePercent": -0.58,
 *			  "delay": true,
 *			  "asOfDateTime": "2018-12-28T10:07:42.000+08:00",
 *			  "bidPrice": 17.080,
 *			  "bidSize": 1000,
 *			  "askPrice": 17.180,
 *			  "askSize": 4000,
 *			  "openPrice": 17.220,
 *			  "previousClosePrice": 17.200,
 *			  "dayLowPrice": 17.100,
 *			  "dayHighPrice": 17.220,
 *			  "yearLowPrice": 15.500,
 *			  "yearHighPrice": 19.640,
 *			  "volume": 9000,
 *			  "upperLimitPrice": 11.3,
 *			  "lowerLimitPrice": 9.33,
 *			  "boardLotSize": 1000,
 *			  "marketCap": 90630,
 *			  "sharesOutstanding": 5300.000,
 *			  "peRatio": 56.27,
 *			  "eps": 0.304,
 *			  "turnover": 154760,
 *			  "dividend": "",
 *			  "dividendYield": "",
 *			  "bidAskQueues ": {
 *				  "bidOrder": "1",
 *				  "bidSize": "1.2"
 *				  "bidPrice": "1.4"
 *				  "askOrder": "1.3"
 *				  "askSize": "1.4"
 *				  "askPrice": "1.9"
 *				 
 *			}
 *			 
 *		},
 *		  {
 *			  "prodAltNumSegs": [{
 *				"prodCdeAltClassCde": "M",
 *				"prodAltNum": "FFIC"
 *					 
 *				}
 *			],
 *			  "symbol": "FFIC",
 *			  "market": "US",
 *			  "exchangeCode": "NAQ",
 *			  "productType": "SEC",
 *			  "companyName": "Flushing Financial Corp",
 *			  "marketClosed": true,
 *			  "nominalPrice": 20.920000,
 *			  "currency": "USD",
 *			  "changeAmount": -0.30,
 *			  "changePercent": -1.4138,
 *			  "delay": true,
 *			  "asOfDateTime": "2018-12-27T16:00:00.000-05:00",
 *			  "bidPrice": 0.00,
 *			  "bidSize": 0,
 *			  "askPrice": 22.48,
 *			  "askSize": 15,
 *			  "openPrice": 20.98,
 *			  "previousClosePrice": 21.22,
 *			  "dayLowPrice": 20.42,
 *			  "dayHighPrice": 21.32,
 *			  "yearLowPrice": 20.2700,
 *			  "yearHighPrice": 29.5500,
 *			  "volume": 54971,
 *			  "boardLotSize": 100,
 *			  "marketCap": "",
 *			  "sharesOutstanding": "",
 *			  "peRatio": 11.6762,
 *			  "eps": 1.8174,
 *			  "upperLimitPrice": 11.33,
 *			  "lowerLimitPrice": 9.33,
 *			  "turnover": "",
 *			  "dividend": 0.8000,
 *			  "dividendYield": 3.7700,
 *            "priceCode": "",
 *			  "bidAskQueues": {
 *				  "bidOrder": "1",
 *				  "bidSize": "1.2"
 *				  "bidPrice": "1.4"
 *				  "askOrder": "1.3"
 *				  "askSize": "1.4"
 *				  "askPrice": "1.9"
 *			}
 *			 
 *		}
 *	],
 *	  "messages": [{
 *		  "reasonCode": "MDSEQTY50003",
 *		  "text": "ITEM UNKNOW",
 *		  "traceCode": "",
 *		  "productType": "SEC",
 *		  "prodCdeAltClassCde": "R",
 *		  "prodAltNum": "0008.HK"
 *		},
 *		{
 *		  "reasonCode": "MDSEQTY50004",
 *		  "text": "ITEM UNKNOW",
 *		  "traceCode": "",
 *		  "productType": "SEC",
 *		  "prodCdeAltClassCde": "R",
 *		  "prodAltNum": "0009.HK"
 *			 
 *		}
 *	]
 *	 
 *  }
 *   
 * 
 * @apiUse ErrorMsgResponse
 */

/**
 * @apiDefine quotesResponse
 * @apiSuccess {Object[]} priceQuotes
 * @apiSuccess {Object[]} priceQuotes.prodAltNumSegs
 * @apiSuccess {String} priceQuotes.prodAltNumSegs.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} priceQuotes.prodAltNumSegs.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {Object[]} priceQuotes.bidAskQueues
 * @apiSuccess {String} [priceQuotes.bidAskQuotes.bidOrder] Number of buyers in the first - tenth slot of bid queue.
 * @apiSuccess {String} [priceQuotes.bidAskQuotes.bidSize] Number of shares in the first - tenth slot of bid queue. 7 decimal places
 * @apiSuccess {String} [priceQuotes.bidAskQuotes.bidPrice] Price in the first - tenth slot of bid queue.
 * @apiSuccess {String} [priceQuotes.bidAskQuotes.askOrder] Number of buyers in the first - tenth slot of ask queue.
 * @apiSuccess {String} [priceQuotes.bidAskQuotes.askSize] Number of shares in the first - tenth slot of ask queue.
 * @apiSuccess {String} [priceQuotes.bidAskQuotes.askPrice] Price in the first - tenth slot of ask queue. 7 decimal places
 * @apiSuccess {Number} priceQuotes.symbol Symbol of request stock.(tris:same as prductAltNum;midfs:retrieve from pattern like stock_99999)
 * @apiSuccess {Number} [priceQuotes.exchangeCode] Exchange Code
 * @apiSuccess {Number} [priceQuotes.productType] prdouct type
 * @apiSuccess {Number} priceQuotes.askSize The ask size is the amount of a security that a market maker is offering to sell at the ask price(tris:ASKSIZE).
 * @apiSuccess {String} [priceQuotes.delay] Delay Quote or Realtime Quote.
 * @apiSuccess {String} [priceQuotes.companyName] The company name of the specified symbol.
 * @apiSuccess {Number} priceQuotes.nominalPrice -- Last trade price or value(tris:TRDPRC_1;midfs:sec_vrti.last_v).
 * @apiSuccess {Number} [priceQuotes.remainingQuota] The remaining free quota for the user.
 * @apiSuccess {String} [priceQuotes.currency] Trading currency of the specified symbol.
 * @apiSuccess {Number} [priceQuotes.changeAmount] Change in amount of the nominal price versus previous closing price. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.changePercent] Change in percentage of the nominal price versus previous closing price. 4 decimal places.
 * @apiSuccess {Number} priceQuotes.dayLowPrice  -- Today's lowest transaction value(tris:LOW_1;midfs:sec_vrti.low).
 * @apiSuccess {Number} priceQuotes.dayHighPrice  -- Today's highest transaction value.(tris:HIGH_1;midfs:sec_vrti.high)
 * @apiSuccess {Number} priceQuotes.peRatio PERATIO -- Ratio of stock price to earnings per share(tris:PERATIO;midfs:pe_ratio).
 * @apiSuccess {Number} [priceQuotes.dividend] 4 decimal places.
 * @apiSuccess {Number} priceQuotes.dividendYield  -- For equities the dividend per share expressed as a percentage of the price. For bonds this is the current or simple yield i.e. the interest expressed as a percentage of the price(tris:YIELD;midfs:yield).
 * @apiSuccess {Number} [priceQuotes.yearLowPrice] Year low price of the specified symbol. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.yearHighPrice] Year high price of the specified symbol. 7 decimal places.
 * @apiSuccess {Date} priceQuotes.asOfDateTime Last updated time for the last trade price default TimeZone(GMT+0:00). format: yyyy-MM-dd'T'HH:mm:ss.SSSZ.<ul><li>TRADE_DATE -- The date of the value in the field TRDPRC_1.</li><li>TRDTIM_1 -- Time of the value in the TRDPRC_1 in minutes.</li></ul>(tris:TRADE_DATE,TRDTIM_1;midfs:data_date,time_modified)
 * @apiSuccess {Number} priceQuotes.turnover  -- The daily turnover revenue or value of all shares for either a particular instrument or an exchange. Currently supplied by New Zealand SE Hong Kong SE and Copenhagen SE. Cleared during the pre-market clear. The value is scaled according to the field TN(TURNOVER)(tris:TURNOVER;midfs:traded_val).
 * @apiSuccess {Number} priceQuotes.volume  -- Accumulated number of shares, lots or contracts traded according to the market convention(tris:ACVOL_1;midfs:volume).
 * @apiSuccess {Number} priceQuotes.previousClosePrice  -- Historical unadjusted close or settlement price(tris:HST_CLOSE).
 * @apiSuccess {Number} priceQuotes.bidPrice -- Latest Bid Price (price willing to buy)(tris:BID;midfs:bbp).
 * @apiSuccess {Number} [priceQuotes.bidSize] The best bid size of the specified symbol.
 * @apiSuccess {Number} priceQuotes.askPrice  -- Latest Ask Price (price offering to sell)(tris:ASK;midfs:bap).
 * @apiSuccess {Number} priceQuotes.sharesOutstanding AMT_OS -- The number of shares outstanding(tris:AMT_OS;midfs:outstanding).
 * @apiSuccess {Number} priceQuotes.prevTradePrice  -- The previous trading day's or session's last value(tris:PRV_LAST;midfs:sec_vrti.prelast).
 * @apiSuccess {Number} priceQuotes.settlementPrice  -- Settlement price. The official closing price of a futures or option contract set by the clearing house at the end of the trading day(tris:SETTLE).
 * @apiSuccess {Number} priceQuotes.primaryLastActivity  -- Primary last activity fields the most recent held in PRIMACT_1(tris:PRIMACT_1).
 * @apiSuccess {Number} priceQuotes.accumulatedVolume  -- Accumulated Volume scaling factor(tris:TNOVER_SC).
 * @apiSuccess {Number} priceQuotes.unscaledTurnover  -- The unscaled turnover value (summation of the value of all trades during the market day) for a particular instrument(tris:TRNOVR_UNS).
 * @apiSuccess {Number} priceQuotes.totalSharesOutstanding  -- The unscaled value for the total number of shares outstanding(tris:AMT_OS_UNS).
 * @apiSuccess {Number} priceQuotes.totalIssuedShares  -- Total amount of issued share(tris:AMT_ISSUE).
 * @apiSuccess {Number} [priceQuotes.boardLotSize] Board lot size of the specified symbol.
 * @apiSuccess {Number} [priceQuotes.marketCap] Market capitalisation.
 * @apiSuccess {Number} [priceQuotes.eps] Earnings per share of the specified symbol.
 * @apiSuccess {Number} [priceQuotes.openPrice] First traded price for auto-trading session. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.iep] Indicative equilibrium price.
 * @apiSuccess {Number} [priceQuotes.iev] Indicative equilibrium volume.
 * @apiSuccess {String} [priceQuotes.brokerAskQueue] Broker IDs in the ask queue.
 * @apiSuccess {String} [priceQuotes.brokerBidQueue] Broker IDs in the bid queue.
 * @apiSuccess {Number} [priceQuotes.riskLvlCde] risk leval code.
 * @apiSuccess {String} [priceQuotes.vcmEligible] VCM eligible. <ul><li>Y: The instrument is in the scope of VCM.</li><li>N: The instrument is not in the scope of VCM.</li><li>Blank: VCM is not enabled in HKEx.</li></ul>
 * @apiSuccess {String} [priceQuotes.casEligible] CAS eligible. <ul><li>Y: The instrument is in the scope of CAS.</li><li>N: The instrument is not in the scope of CAS.</li><li>Blank: CAS is not enabled in HKEx.</li></ul>
 * @apiSuccess {String} [priceQuotes.vcmStatus] Indicate whether this stock is currently trade under VCM. <ul><li>Y: if and only if the current system time of MIDFS is between the VCM start time and end time (inclusive)</li><li>N</li></ul>
 * @apiSuccess {Number} [priceQuotes.casLowerLimitPrice] CAS lower price. 7 decimal places. <ul><li>During the order input period of CAS (first 5 minutes), this will be the -5% of reference price.</li><li>During the no-cancelation period of CAS (6th to 7th minutes), this will be the lower band of the allowed price band.</li><li>Blank if not available.</li></ul>
 * @apiSuccess {Number} [priceQuotes.casUpperLimitPrice] CAS upper price. 7 decimal places. <ul><li>During the order input period of CAS (first 5 minutes), this will be the +5% of reference price.</li><li>During the no-cancelation period of CAS (6th to 7th minutes), this will be the upper band of the allowed price band.</li><li>Blank if not available.</li></ul>
 * @apiSuccess {Date} [priceQuotes.vcmStartTime] Start time of cooling off period.
 * @apiSuccess {Date} [priceQuotes.vcmEndTime] End time of cooling off period.
 * @apiSuccess {Number} [priceQuotes.vcmLowerLimitPrice] Lower bound of the allowed price band for sale order input in VCM. Blank if not available. 7 decimal places.
 * @apiSuccess {Number} [priceQuotes.vcmUpperLimitPrice] Upper bound of the allowed price band for purchase order input in VCM. Blank if not available. 7 decimal places.
 * @apiSuccess {String} [priceQuotes.auctionIndicator] "Y" for auction period (when trading status = 'ORDER INPUT' or 'PRE-ORDER MATCHING' or 'ORDER MATCHING'): show iev / iep in frontend. "N" for non-auction period: hide iev / iep in frontend.
 * @apiSuccess {Number} [priceQuotes.bidSpread] A bid-ask spread is the amount by which the ask price exceeds the bid price for an asset in the market..
 * @apiSuccess {Number} [priceQuotes.askSpread] A bid-ask spread is the amount by which the ask price exceeds the bid price for an asset in the market..
 * @apiSuccess {Number} [priceQuotes.upperLimitPrice] upper price of limited
 * @apiSuccess {Number} [priceQuotes.lowerLimitPrice] lower price of limited
 * @apiSuccess {Number} [priceQuotes.totalQuota] Total free quota available.
 * @apiSuccess {Number} priceQuotes.daySecLowLimPrice --The second level lower trading limit for today's trading(tris:LOLIMIT_2).
 * @apiSuccess {Number} priceQuotes.daySecUpperLimPrice --The second level upper trading limit for today's trading(tris:UPLIMIT_2).
 * @apiSuccess {Number} priceQuotes.dayLowLimPrice  -- Lower trading limit for today's trading(tris: LOLIMIT).
 * @apiSuccess {Number} priceQuotes.dayUpperLimPrice --Upper trading limit for today's trading(tris:UPLIMIT).
 * @apiSuccess {Number} priceQuotes.limitReferencePrice -- limit Reference Price(tris:LMT_REFPR2).
 * @apiSuccess {String} priceQuotes.priceCode -- Price qualifier code for equities, bonds, and options(tris:PRC_QL_CD,midfs:mkt_stat).
 * @apiSuccess {String} messages return call tris or midfs error product infomation
 * @apiSuccess {String} messages.reasonCode The reason code of the error response
 * @apiSuccess {String} messages.text The description of the error response
 * @apiSuccess {String} messages.traceCode Unique code returned for error tracing
 * @apiSuccess {String} messages.productType Product Type. Stock is "SEC".
 * @apiSuccess {String} messages.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} messages.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * 
 */