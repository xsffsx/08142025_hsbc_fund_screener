/**
 * @api {get} /wealth/api/v1/market-data/equity/charts/performance Charts Performance
 * @apiName Charts Performance
 * @apiGroup Charts
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String} market The investment market of the product. 
 *											<ul><li>CN - China</li>
 *												<li>HK - Hong Kong</li>
 *											<li>US - United States</li>	</ul>
 * @apiParam {String="SEC","INDEX"} productType Denotes the type of product.
 * @apiParam {String[]} symbol Array of product market code. Sample value: "00005".
 * <p>For productType is "INDEX" only:</p>
 * <p>CN market supported index list as below:</p>
 * <table>
 * 		<tr><th>Code</th><th>Name(English)</th><th>Name(T.Chinese)</th><th>Name(S.Chinese)</th></tr>
 * 		<tr><td>SSEC</td><td>SSE Composite Index</td><td>上證綜合指數</td><td>上证综合指数</td></tr>
 * 		<tr><td>CSI300</td><td>CSI 300 Index</td><td>滬深300指數</td><td>沪深300指数</td></tr>
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
  * 	<tr><td>.GSPC</td><td>S&P 500</td><td>--</td><td>--</td></tr>
 * </table>
 * @apiParam {String[]} filters For CN and US market, A string containing the names of desired actions. "Actions" are the columns in a table of time-series data.</br>
 * The Action element is used to specify the desired actions from within a standard set supported by the ViewID. For summarized data, the following actions are normally available: "CLOSE", "OPEN", "HIGH", "LOW", "COUNT", "VOLUME", "SMA=25,50,75", "WMA=25,50,75","BB=20","RSI=9","MACD=12,26,9","CCI=20","FKD=5,3","SKD=5,3","MOM=12","WR=10","TP=3". "DATE","OPEN","HIGH","LOW","CLOSE","VOLUME" is the usage foe usual chart plotting without TA.</br>
 * For tick data, the following actions are normally available: "VALUE", "COUNT", "VOLUME", "CONTRIB_ID" (contributor ID).</br>
 * For HK market, Array of desired returning data fields. "DATE","OPEN","HIGH","LOW","CLOSE","VOLUME" is the usage foe usual chart plotting without TA. </br>
 * <p>For US/CN market, Technical Indicators, possible values as below:</p>
 * <table>
 * 		<tr><th>TA : Request Field parameters</th><th>TA Name</th><th>TA display position</th></tr>
 * 		<tr><td>SMA : SMA=10,20,50 (Only support SMA=10,20,50)</td><td>SMA</td><td>Upper indicator</td></tr>
 * 	<tr><td>WMA : WMA=25,50,75 : WMA(25,50,75).25, WMA(25,50,75).50, WMA(25,50,75).75</td><td>WMA</td><td>Upper indicator</td></tr>
 * 	<tr><td>BB : BB=20 : BB(20).UB, BB(20), BB(20).LB</td><td>Bollinger bands</td><td>Upper indicator</td></tr>
 * 	<tr><td>FIB : FIB= : FIB.236.0, FIB.382.0, FIB.500.0, FIB.618.0, FIB.764.0</td><td>	Fibonacci line</td><td>Upper indicator</td></tr>
 * 	<tr><td>RSI : RSI=9 : RSI(9)</td><td>RSI</td><td>Lower indicator</td></tr>
 * 	<tr><td>MACD: MACD=12,26,9 : MACD(12,26,9).MACD, MACD(12,26,9).Signal, MACD(12,26,9).Histogram</td><td>MACD</td><td>Lower indicator</td></tr>
 * 	<tr><td>ACD : ACD= : ACD</td><td>Accumlation/Dist.</td><td>Lower indicator</td></tr>
 * 	<tr><td>CCI : CCI=20 : CCI(20)</td><td>CCI</td><td>Lower indicator</td></tr>
 * 	<tr><td>FKD : FKD=5,3 : FKD(5,3).%K, FKD(5,3).%D</td><td>	Fast stochastic</td><td>Lower indicator</td></tr>
 * 	<tr><td>SKD : SKD=5,3 : SKD(5,3).%K, SKD(5,3).%D</td><td>Slow stochastic</td><td>Lower indicator</td></tr>
 * 	<tr><td>MOM : MOM=12 : MOM(12)</td><td>Momentum</td><td>Lower indicator</td></tr>
 * 	<tr><td>OBV : OBV= : OBV</td><td>On Balance Volume</td><td>Lower indicator</td></tr>
 * 	<tr><td>WR : WR=10 : WR(10)</td><td>William %R</td><td>Lower indicator</td></tr>
 * 	<tr><td>TP: TP=3 : TP(3).Reverse, TP(3).Extreme, TP(3).Trend, TP(3).High, TP(3).Low</td><td>Turning Point</td><td>Lower indicator</td></tr>
 * </table>
 * <p>For HK market, Technical Indicators possible values as below:</p>
 * <table>
 * 		<tr><th>TA : Request Field parameters</th><th>TA Name</th></tr>
 * 		<tr><td>SMA : SMA=10,20,50 (Only support SMA=10,20,50)</td><td>SMA</td></tr>
 * </table>
 * @apiParam {Number} period The time period as requested for chart data points.</br>
 * Noted: This field "period" will overwrite startTime/endTime, it means if there is period in request, then start time and end time will not work.
 * <p>For CN and US market, possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>0</td><td>1 day</td></tr>
 * 		<tr><td>1</td><td>2 days</td></tr>
 * 		<tr><td>2</td><td>5 days</td></tr>
 * 		<tr><td>3</td><td>10 days</td></tr>
 * 		<tr><td>4</td><td>Month to Date</td></tr>
 * 		<tr><td>5</td><td>1 Month</td></tr>
 * 		<tr><td>6</td><td>2 Month</td></tr>
 * 		<tr><td>7</td><td>3 Month</td></tr>
 * 		<tr><td>8</td><td>6 Month</td></tr>
 * 		<tr><td>9</td><td>Quarter to Date</td></tr>
 * 		<tr><td>10</td><td>Year to Date</td></tr>
 * 		<tr><td>11</td><td>1 Year</td></tr>
 * 		<tr><td>12</td><td>2 Year</td></tr>
 * 		<tr><td>13</td><td>3 Year</td></tr>
 * 		<tr><td>14</td><td>4 Year</td></tr>
 * 		<tr><td>15</td><td>5 Year</td></tr>
 * 		<tr><td>16</td><td>10 Years</td></tr>
 * </table>
 * <p>For HK market, possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>0</td><td>1D - Intra-day (5 mins)</td></tr>
 * 		<tr><td>2</td><td>5D - 5 days (15 mins)</td></tr>
 * 		<tr><td>5</td><td>1M - 1 Month (daily)</td></tr>
 * 		<tr><td>7</td><td>3M - 3 Months (daily)</td></tr>
 * 		<tr><td>8</td><td>6M - 6 Months (daily)</td></tr>
 * 		<tr><td>11</td><td>1Y - 1 Year (daily)</td></tr>
 * 		<tr><td>13</td><td>3Y - 3 Years (daily)</td></tr>
 * </table>
 * @apiParam {Number} intCnt The interval of tick for chart data points.
 * @apiParam {String="MINUTE","DAILY","WEEKLY","MONTHLY"} intType The interval type, is pairing with "inCnt".
 * <p>For CN and US market, supported pairs of period and interval possible values as below:</p>
 * <table>
 * 		<tr><th>Period/Interval</th><th>1 min</th><th>5 min</th><th>15 min</th><th>hourly</th><th>daily</th><th>weekly</th><th>monthly</th></tr>
 * 		<tr><td>1 day</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td></tr>
 * 		<tr><td>2 days</td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td></tr>
 * 		<tr><td>5 days</td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td></tr>
 * 		<tr><td>10 days</td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td></tr>
 * 		<tr><td>MTD</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td></tr>
 * 		<tr><td>1 month</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>2 months</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>3 months</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>6 months</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>QTD</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>YTD</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>1 year</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>2 years</td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>3 years</td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>4 years</td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>5 years</td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>10 years</td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td></tr>
 * </table> 
 * For HK market, supported pairs of period and interval possible values as below:</p>
 * <table>
 * 		<tr><th>Period/Interval</th><th>1 min</th><th>5 min</th><th>15 min</th><th>hourly</th><th>daily</th><th>weekly</th><th>monthly</th></tr>
 * 		<tr><td>1 day</td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td></td></tr>
 * 		<tr><td>5 days</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td></tr>
 * 		<tr><td>1 month</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td></tr>
 * 		<tr><td>3 months</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td></tr>
 * 		<tr><td>6 months</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td></tr>
 * 		<tr><td>1 year</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td></tr>
 * 		<tr><td>3 years</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td></tr>
 * </table> 
 * @apiParam {Date} [startTime] Period start time, format in GMT+0 is yyyy-MM-dd'T'HH:mm:ss (e.g. 2017-01-01T00:00:00).
 * </br>Noted: For CN market only, if "period" is not defined, startTime and endTime must be presented to define the time range.
 * @apiParam {Date} [endTime] Period end time, format in GMT+0 is yyyy-MM-dd'T'HH:mm:ss (e.g. 2017-01-01T08:00:00)
 * </br>Noted: For CN market only, if "period" is not defined, startTime and endTime must be presented to define the time range.
 * 
 * @apiParamExample Request:
 * {
 * 	"market": "HK",
 * 	"productType": "SEC",
 * 	"symbol": ["00005"],
 * 	"period": 2,
 * 	"intCnt": 15,
 * 	"intType": "MINUTE",
 * 	"filters": ["DATE",
 * 	"OPEN",
 * 	"HIGH",
 * 	"LOW",
 * 	"CLOSE",
 * 	"VOLUME"]
 * }
 * 
 * 
 * @apiSuccess {Object[]} result
 * @apiSuccess {String} result.market The investment market of the product. 
 * @apiSuccess {String} result.productType The product type as request.
 * @apiSuccess {String} result.symbol The product market code as request.
 * @apiSuccess {String} result.displayName The display name for requested product.
 * @apiSuccess {Object[]} result.data Array of chart data points per requested.
 * @apiSuccess {Object[]} result.fields Array of field names of corresponding chart data points.
 * 
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 * {
 *   "result": [
 *     {
 *       "market": "HK",
 *       "productType": "SEC",
 *       "symbol": "00005",
 *       "displayName": "hhhh HOLDINGS",
 *       "data": [
 *         [
 *           "2019-06-25T01:45:00.000Z",
 *           64.5,
 *           64.55,
 *           64.5,
 *           64.4,
 *           337214
 *         ],
 *         [
 *           "2019-06-25T02:00:00.000Z",
 *           64.5,
 *           64.55,
 *           64.5,
 *           64.55,
 *           332435
 *         ],
 *         [
 *           "2019-06-25T02:15:00.000Z",
 *           64.5,
 *           64.5,
 *           64.45,
 *           64.5,
 *           131601
 *         ],
 *         [
 *           "2019-06-25T02:30:00.000Z",
 *           64.45,
 *           64.5,
 *           64.4,
 *           64.45,
 *           755064.9999999999
 *         ],
 *         [
 *           "2019-06-25T02:45:00.000Z",
 *           64.3,
 *           64.4,
 *           64.3,
 *           64.4,
 *           237864
 *         ],
 *         [
 *           "2019-06-25T03:00:00.000Z",
 *           64.3,
 *           64.35,
 *           64.25,
 *           64.25,
 *           660621
 *         ]
 *       ],
 *       "fields": [
 *         "DATE",
 *         "OPEN",
 *         "HIGH",
 *         "LOW",
 *         "CLOSE",
 *         "VOLUME"
 *       ]
 *     }
 *   ]
 * }
 * 
 * @apiUse ErrorMsgResponse
 * 
 */