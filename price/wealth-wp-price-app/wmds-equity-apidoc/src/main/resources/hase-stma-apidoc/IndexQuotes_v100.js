/**
 * @api {get} /wealth/api/v1/market-data/index/quotes Index Quotes
 * @apiName Index Quotes
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String} market The investment market of the product. 
 *											<ul><li>CN - China</li>
 *												<li>HK - Hong Kong</li>
 *											<li>US - United States</li>	</ul>
 * @apiParam {String[]} symbol Symbol of requested indices.
 * <p>CN market supported index list as below:</p>
 * <table>
 * 		<tr><th>Code</th><th>Name(English)</th><th>Name(T.Chinese)</th><th>Name(S.Chinese)</th></tr>
 * 		<tr><td>CSI300</td><td>CSI 300 Index</td><td>滬深300指數</td><td>沪深300指数</td></tr>
 * 		<tr><td>SSEC</td><td>SSE Composite Index</td><td>上證綜合指數</td><td>上证综合指数</td></tr>
 * 		<tr><td>SZSC</td><td>SZSE Composite Index</td><td>深證綜合指數</td><td>深证综合指数</td></tr>
 * 		<tr><td>SZI</td><td>SZSE Component Index</td><td>深證成份指數</td><td>深证成份指数</td></tr>
 * 		<tr><td>SZ100</td><td>SZSE 100 Index</td><td>深證100指數</td><td>深证100 指数</td></tr>
 * 		<tr><td>CNT</td><td>ChiNext Index</td><td>創業板指數</td><td>创业板指数</td></tr>
 * </table>
 * <p>HK market supported index list as below:</p>
 * <table>
 * 		<tr><th>Code</th><th>Name(English)</th><th>Name(T.Chinese)</th><th>Name(S.Chinese)</th></tr>
 * 		<tr><td>HSI</td><td>Hang Seng Index</td><td>恒生指數</td><td>恒生指数</td></tr>
 * 		<tr><td>FSI</td><td>HS Finance Sub-Index</td><td>恒生金融分類指數</td><td>恒生金融分类指数</td></tr>
 * 		<tr><td>PSI</td><td>HS Properties Sub-Index</td><td>恒生地產分類指數</td><td>恒生地产分类指数</td></tr>
 * 		<tr><td>USI</td><td>HS Utilities Sub-Index</td><td>恒生公用事業分類指數</td><td>恒生公用事业分类指数</td></tr>
 * 		<tr><td>CSI</td><td>HS Commerce & Industry Sub-Index</td><td>恒生工商業分類指數</td><td>恒生工商业分类指数</td></tr>
 * 		<tr><td>CEI</td><td>HS China Enterprises Index</td><td>恒生中國企業指數</td><td>恒生中国企业指数</td></tr>
 * 		<tr><td>CCI</td><td>HS China-Aff Corporations Index</td><td>恒生香港中資企業指數</td><td>恒生香港中资企业指数</td></tr>
 * 		<tr><td>HSC</td><td>HS Composite Index</td><td>恒生綜合指數</td><td>恒生综合指数</td></tr>
 * 		<tr><td>HFI</td><td>HS China H-Financials Index</td><td>恒生中國H股金融行業指數</td><td>恒生中国H股金融行业指数</td></tr>
 * 		<tr><td>GEM</td><td>S&P/HKEx GEM Index</td><td>標普香港GEM指數</td><td>标普香港GEM指数</td></tr>
 * 	    <tr><td>TEH</td><td>Hang Seng TECH Index</td><td>恒生科技指數</td><td>恒生科技指数</td></tr>
 * </table>
 * <p>US market supported index list as below:</p>
 * <table>
 * 		<tr><th>Code</th><th>Name(English)</th><th>Name(T.Chinese)</th><th>Name(S.Chinese)</th></tr>
 * 		<tr><td>.DJI</td><td>Dow Jones</td><td>--</td><td>--</td></tr>
 * 	    <tr><td>.IXIC</td><td>NASDAQ</td><td>--</td><td>--</td></tr>
 * 	    <tr><td>.GSPC</td><td>S&P 500</td><td>--</td><td>--</td></tr>
 * </table>
 * <p>Supported world index list as below:</p>
 * <table>
 * 		<tr><th>Code</th><th>Name(English)</th><th>Name(T.Chinese)</th><th>Name(S.Chinese)</th></tr>
 * 		<tr><td>DJI</td><td>Dow Jones Industrial Average</td><td>道瓊斯工業平均指數</td><td>道琼斯工业平均指数</td></tr>
 * 		<tr><td>NDI</td><td>Nasdaq Composite</td><td>納斯達克綜合指數</td><td>纳斯达克综合指数</td></tr>
 * 		<tr><td>FTSE</td><td>London FT100</td><td>倫敦富時100指數</td><td>伦敦富时100指数</td></tr>
 * 		<tr><td>GDAX</td><td>Frankfurt DAX</td><td>法蘭克福DAX指數</td><td>法兰克福DAX指数</td></tr>
 * 		<tr><td>FCAC</td><td>Paris CAC40</td><td>巴黎CAC40指數</td><td>巴黎CAC40指数</td></tr>
 * 		<tr><td>NKI</td><td>Tokyo Nikkei 225</td><td>東京日經225指數</td><td>东京日经225指数</td></tr>
 * 		<tr><td>KSI</td><td>Seoul KOSPI</td><td>首爾KOSPI指數</td><td>首尔KOSPI指数</td></tr>
 * 		<tr><td>KLSE</td><td>Kuala Lumpur Composite</td><td>吉隆坡綜合指數</td><td>吉隆坡综合指数</td></tr>
 * </table>
 *                    
 * @apiParamExample Request:
 *  {
 *  	"market":"HK",
 *  	"symbol":["HSI"]
 *  }
 *  
 * @apiSuccess {Object[]} indices
 * @apiSuccess {String} indices.symbol Symbol of requested indices.
 * @apiSuccess {String} indices.name Name of the index.
 * @apiSuccess {Number} indices.lastPrice Last trade price or value.
 * @apiSuccess {Number} indices.changeAmount Difference between the latest trading price or value and the adjusted historical closing value or settlement price.
 * @apiSuccess {Number} indices.changePercent Percentage change of the latest trade price or value from the adjusted historical close.
 * @apiSuccess {Number} indices.openPrice Today's opening price or value. The source of this field depends upon the market and instrument type.
 * @apiSuccess {Number} indices.previousClosePrice Historical close or settlement price.
 * @apiSuccess {Number} indices.dayRangeHigh Today's highest transaction value.
 * @apiSuccess {Number} indices.dayRangeLow  Today's lowest transaction value.
 * @apiSuccess {Number} indices.changePercent1M  Percentage change of current close price comparing to 1 month historic close.
 * @apiSuccess {Number} indices.changePercent2M  Percentage change of current close price comparing to 2 month historic close.
 * @apiSuccess {Number} indices.changePercent3M  Percentage change of current close price comparing to 3 month historic close.
 * @apiSuccess {Number} indices.changePercent1Y  Percentage change of current close price comparing to 1 year historic close.
 * @apiSuccess {Number} indices.oneMonthLowPrice  Lowest transaction value of pass 1 month.
 * @apiSuccess {Number} indices.twoMonthLowPrice  Lowest transaction value of pass 2 month.
 * @apiSuccess {Number} indices.threeMonthLowPrice Lowest transaction value of pass 3 month.
 * @apiSuccess {Number} indices.oneMonthHighPrice  Highest transaction value of pass 1 month.
 * @apiSuccess {Number} indices.twoMonthHighPrice  Highest transaction value of pass 2 month.
 * @apiSuccess {Number} indices.threeMonthHighPrice  Highest transaction value of pass 3 month.
 * @apiSuccess {Number} indices.yearHighPrice  The highest value this year or period.
 * @apiSuccess {Number} indices.yearLowPrice  The lowest value this year or period.
 * @apiSuccess {Date} indices.asOfDateTime  The time of the lastPrice, Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX.
 * @apiSuccess {Object[]} messages The warning messages returned once indices quote is happening exceptions.
 * @apiSuccess {String} messages.reasonCode The reason code of the warning response.
 * @apiSuccess {String} messages.text The description of the warning response.
 * @apiSuccess {String} messages.traceCode The trace code of the warning response.
 * @apiSuccess {String} messages.symbol The symbol of indices which has exception in warning response.
 *
 *  
 * @apiSuccessExample Success-Response:
 * {
 *   "indices": [{
 *     "symbol": "HSI",	
 *     "name": "Hang Seng Index",	
 *     "lastPrice": 29357.69,	
 *     "changeAmount": 61.64,	
 *     "changePercent": 0.21,	
 *     "openPrice": 29215.54,	
 *     "previousClosePrice": 29296.05,	
 *     "dayRangeHigh": 29436.84,	
 *     "dayRangeLow": 29089.39,	
 *     "changePercent1M": -6.01,	
 *     "changePercent2M": -3.49,	
 *     "changePercent3M": -5.51,	
 *     "changePercent1Y": 14.35,	
 *     "oneMonthLowPrice": 29285.71,	
 *     "twoMonthLowPrice": 29285.71,	
 *     "threeMonthLowPrice": 29285.71,	
 *     "oneMonthHighPrice": 31521.13,	
 *     "twoMonthHighPrice": 31592.56,
 *     "threeMonthHighPrice": 31686.67,	
 *     "asOfDateTime": "2018-06-22T15:51:00.000+08:00",
 *     "yearHighPrice": 33484.08,	
 *     "yearLowPrice": 25199.86	
 *   }],
 *   "messages": null
 * }
 * 
 * @apiUse ErrorMsgResponse
 */
