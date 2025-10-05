/**
 * @api {get} /wealth/api/v1/market-data/equity/quotes Quotes
 * @apiName Quotes
 * @apiGroup Quote
 * @apiVersion 1.0.1
 * 
 * @apiUse QuoteHeaderParam
 * 
 * @apiParam {Object[]} productKeys The key to uniquely locate a product.
 * @apiParam {String} productKeys.productType Product Type. Stock is "SEC".
 * @apiParam {String} productKeys.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiParam {String} productKeys.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiParam {String} productKeys.market The investment market of the product. <ul><li>HK: Hong Kong</li><li>US: United States</li></ul>
 * @apiParam {Boolean} delay <ul><li>true: Delay Quote</li></ul>
 * 
 * @apiParamExample Request:
 *  {
 *  	"productKeys": [{
 *  		"productType": "SEC",
 *  		"prodCdeAltClassCde": "M",
 *  		"prodAltNum": "6862",
 *  		"market": "HK"
 *  	},
 *  	{
 *  		"productType": "SEC",
 *  		"prodCdeAltClassCde": "M",
 *  		"prodAltNum": "FFIC",
 *  		"market": "US"
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
 *  		"prodAltNumSegs": [{
 *  			"prodCdeAltClassCde": "I",
 *  			"prodAltNum": "KYG4290A1013"
 *  		},
 *  		{
 *  			"prodCdeAltClassCde": "M",
 *  			"prodAltNum": "6862"
 *  		},
 *  		{
 *  			"prodCdeAltClassCde": "P",
 *  			"prodAltNum": "1001023"
 *  		},
 *  		{
 *  			"prodCdeAltClassCde": "T",
 *  			"prodAltNum": "6862.HK"
 *  		},
 *  		{
 *  			"prodCdeAltClassCde": "W",
 *  			"prodAltNum": "20000951"
 *  		}],
 *  		"ric": "6862.HK",
 *  		"symbol": "6862",
 *  		"market": "HK",
 *  		"exchange": "HKEX",
 *  		"productType": "SEC",
 *  		"companyName": "HAIDILAO INTERNATIONAL HOLDING LTD",
 *  		"marketClosed": true,
 *  		"sharePrice": 17.100,
 *  		"currency": "HKD",
 *  		"changeAmount": -0.100,
 *  		"changePercent": -0.58,
 *  		"delay": true,
 *  		"asOfDateTime": "2018-12-28T10:07:42.000+08:00",
 *  		"bidPrice": 17.080,
 *  		"bidSize": 1000,
 *  		"askPrice": 17.180,
 *  		"askSize": 4000,
 *  		"bidAskSpread": 0.020,
 *  		"openPrice": 17.220,
 *  		"previousClosePrice": 17.200,
 *  		"dayLowPrice": 17.100,
 *  		"dayHighPrice": 17.220,
 *  		"yearLowPrice": 15.500,
 *  		"yearHighPrice": 19.640,
 *  		"volume": 9000,
 *  		"averageVolume": "",
 *  		"beta": "",
 *  		"boardLotSize": 1000,
 *  		"marketCap": 90630,
 *  		"sharesOutstanding": 5300.000,
 *  		"peRatio": 56.27,
 *  		"eps": 0.304,
 *  		"turnover": 154760,
 *  		"dividend": "",
 *  		"dividendYield": "",
 *  		"sector": "",
 *  		"industry": ""
 *  	},
 *  	{
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
 *  		"ric": "FFIC.O",
 *  		"symbol": "FFIC",
 *  		"market": "US",
 *  		"exchange": "NAQ",
 *  		"productType": "SEC",
 *  		"companyName": "Flushing Financial Corp",
 *  		"marketClosed": true,
 *  		"sharePrice": 20.920000,
 *  		"currency": "USD",
 *  		"changeAmount": -0.30,
 *  		"changePercent": -1.4138,
 *  		"delay": true,
 *  		"asOfDateTime": "2018-12-27T16:00:00.000-05:00",
 *  		"bidPrice": 0.00,
 *  		"bidSize": 0,
 *  		"askPrice": 22.48,
 *  		"askSize": 15,
 *  		"bidAskSpread": "",
 *  		"openPrice": 20.98,
 *  		"previousClosePrice": 21.22,
 *  		"dayLowPrice": 20.42,
 *  		"dayHighPrice": 21.32,
 *  		"yearLowPrice": 20.2700,
 *  		"yearHighPrice": 29.5500,
 *  		"volume": 54971,
 *  		"averageVolume": "",
 *  		"beta": "",
 *  		"boardLotSize": 100,
 *  		"marketCap": "",
 *  		"sharesOutstanding": "",
 *  		"peRatio": 11.6762,
 *  		"eps": 1.8174,
 *  		"turnover": "",
 *  		"dividend": 0.8000,
 *  		"dividendYield": 3.7700,
 *  		"sector": "",
 *  		"industry": ""
 *  	}]
 *  }
 * 
 * @apiUse ErrorMsgResponse
 */

/**
 * @apiDefine quotesResponse
 * 
 * @apiSuccess {Object[]} priceQuotes
 * @apiSuccess {Object[]} priceQuotes.prodAltNumSegs
 * @apiSuccess {String} priceQuotes.prodAltNumSegs.prodCdeAltClassCde [Source: WPC] <p> Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} priceQuotes.prodAltNumSegs.prodAltNum [Source: WPC] <p> Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String} priceQuotes.ric [Source: WPC] <p> Reuters Instrument Code.
 * @apiSuccess {String} priceQuotes.symbol [Source: WPC] <p> A stock symbol is a unique series of letters assigned to a security for trading purposes.
 * @apiSuccess {String} priceQuotes.market [Source: WPC] <p> Stock Market.
 * @apiSuccess {String} priceQuotes.exchange [Source: WPC] <p> Stock Exchange.
 * @apiSuccess {String} priceQuotes.productType [Source: WPC] <p> Product Type. Stock is "SEC".
 * @apiSuccess {String} priceQuotes.companyName [Source: WPC] <p> Company Name.
 * @apiSuccess {Boolean} priceQuotes.marketClosed [Source: MDS] <p> Market opening or closing.
 * @apiSuccess {Number} priceQuotes.sharePrice [Source: TRIS] <p> [Mapping: HKEX -> OFF_CLOSE, TRDPRC_1, ADJUST_CLS, Others -> TRDPRC_1] <p> A share price is the price of a single share of a number of saleable stocks of a company, derivative or other financial asset.
 * @apiSuccess {String} priceQuotes.currency [Source: TRIS] <p> [Mapping: CURRENCY] <p> Trading currency.
 * @apiSuccess {Number} priceQuotes.changeAmount [Source: TRIS] <p> [Mapping: HKEX&NAQ -> OFF_CLOSE - ADJUST_CLS, NETCHNG_1, Others -> NETCHNG_1] <p> Change in amount of the share price versus previous closing price.
 * @apiSuccess {Number} priceQuotes.changePercent [Source: TRIS] <p> [Mapping: HKEX&NAQ -> (OFF_CLOSE - ADJUST_CLS) / ADJUST_CLS * 100, PCTCHNG, Others -> PCTCHNG] <p> Change in percentage of the share price versus previous closing price.
 * @apiSuccess {String} priceQuotes.delay [Source: MDS] <p> Delay Quote or Realtime Quote.
 * @apiSuccess {Date} priceQuotes.asOfDateTime [Source: TRIS] <p> [Mapping: TRADE_DATE + TRDTIM_1] <p> In local time based on the exchange - timezone mapping config in MDS. format: yyyy-MM-dd'T'HH:mm:ss.SSSZ.
 * @apiSuccess {Number} priceQuotes.bidPrice [Source: TRIS] <p> [Mapping: BID] <p> The bid price refers to the highest amount of money a prospective buyer is willing to spend for it.
 * @apiSuccess {Number} priceQuotes.bidSize [Source: TRIS] <p> [Mapping: BIDSIZE] <p> The bid size represents the minimum quantity of a security an investor is willing to purchase at a specified bid price.
 * @apiSuccess {Number} priceQuotes.askPrice [Source: TRIS] <p> [Mapping: ASK] <p> The ask price is the lowest price a seller of a stock is willing to accept for a share.
 * @apiSuccess {Number} priceQuotes.askSize [Source: TRIS] <p> [Mapping: ASKSIZE] <p> The ask size is the amount of a security that a market maker is offering to sell at the ask price.
 * @apiSuccess {Number} priceQuotes.bidAskSpread [Source: TRIS] <p> [Mapping: UPPER_SPRD, LOWER_SPRD] <p> A bid-ask spread is the amount by which the ask price exceeds the bid price for an asset in the market.
 * @apiSuccess {Number} priceQuotes.openPrice [Source: TRIS] <p> [Mapping: OPEN_PRC] <p> The opening price is the price at which a stock first trades upon the opening of an exchange on a trading day.
 * @apiSuccess {Number} priceQuotes.previousClosePrice [Source: TRIS] <p> [Mapping: HST_CLOSE] <p> Stock's closing price on the preceding day of trading.
 * @apiSuccess {Number} priceQuotes.dayLowPrice [Source: TRIS] <p> [Mapping: LOW_1] <p> Lowest traded price for this trading day.
 * @apiSuccess {Number} priceQuotes.dayHighPrice [Source: TRIS] <p> [Mapping: HIGH_1] <p> Highest traded price for this trading day.
 * @apiSuccess {Number} priceQuotes.yearLowPrice [Source: TRIS] <p> [Mapping: HKEX -> 52WK_LOW, Others -> YRLOW] <p> The 52-Week Low indicates a stock is trading at its lowest price in the past 52 weeks.
 * @apiSuccess {Number} priceQuotes.yearHighPrice [Source: TRIS] <p> [Mapping: HKEX -> 52WK_HIGH, Others -> YRHIGH] <p> The 52-Week High indicates a stock is trading at its highest price in the past 52 weeks.
 * @apiSuccess {Number} priceQuotes.volume [Source: TRIS] <p> [Mapping: ACVOL_1] <p> A stock's volume refers to the number of shares that are sold, or traded, over a certain period of time (usually daily).
 * @apiSuccess {Number} priceQuotes.averageVolume [Source: TRIS] <p> [Mapping: DSS_AVG_VOLUME_90DAYS] <p> The average volume of a stock over a longer period of time(90 days) is the total amount traded in that period, divided by the length of the period. Therefore, the unit of measurement for average volume is shares per unit of time, typically per trading day.
 * @apiSuccess {Number} priceQuotes.beta [Source: TRIS] <p> [Mapping: DSS_BETA] <p> Beta is a measure of the volatility, or systematic risk, of a security or a portfolio in comparison to the entire market or a benchmark.
 * @apiSuccess {Number} priceQuotes.boardLotSize [Source: TRIS] <p> [Mapping: LOT_SIZE_A] <p> A board lot is a standardized number of shares offered as a trading unit usually a minimum transaction size of 100 units/shares.
 * @apiSuccess {Number} priceQuotes.marketCap [Source: TRIS] <p> [Mapping: HKEX -> MKT_CAP, Others -> DSS_MKT_CAP] <p> Market capitalisation.
 * @apiSuccess {Number} priceQuotes.sharesOutstanding [Source: TRIS] <p> [Mapping: HKEX -> AMT_OS, Others -> DSS_OUT_SHARES] <p> Shares outstanding are all the shares of a corporation or financial asset that have been authorized, issued and purchased by investors and are held by them.
 * @apiSuccess {Number} priceQuotes.peRatio [Source: TRIS] <p> [Mapping: PERATIO] <p> The Price-to-Earnings Ratio or P/E ratio is a ratio for valuing a company that measures its current share price relative to its per-share earnings.
 * @apiSuccess {Number} priceQuotes.eps [Source: TRIS] <p> [Mapping: EARNINGS] <p> Earnings per share (EPS) is the portion of a company's profit allocated to each share of common stock.
 * @apiSuccess {Number} priceQuotes.turnover [Source: TRIS] <p> [Mapping: TURNOVER] <p> Share turnover is a measure of stock liquidity calculated by dividing the total number of shares traded over a period by the average number of shares outstanding for the period. The higher the share turnover, the more liquid the share of the company.
 * @apiSuccess {Number} priceQuotes.dividend [Source: TRIS] <p> [Mapping: DIVIDEND] <p> A stock dividend is a dividend payment made in the form of additional shares rather than a cash payout.
 * @apiSuccess {Number} priceQuotes.dividendYield [Source: TRIS] <p> [Mapping: YIELD] <p> The dividend yield is determined with this equation: dividend per share/price per share.
 * @apiSuccess {String} priceQuotes.sector [Source: TRIS] <p> [Mapping: DSS_TRBC_ECONOMIC_SECTOR_DESC] <p> A sector is an area of the economy in which businesses share the same or a related product or service.
 * @apiSuccess {String} priceQuotes.industry [Source: TRIS] <p> [Mapping: DSS_TRBC_INDUSTRY_DESC] <p> An industry is a group of companies that are related based on their primary business activities.
 * 
 */