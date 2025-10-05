/**
 * @api {get} /wealth/api/v1/market-data/equity/charts/performance Charts Performance
 * @apiName Charts Performance
 * @apiGroup Charts
 * @apiVersion 1.0.9
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String} market The investment market of the product. 
 *											<ul>
 *												<li>US - United States of America</li>
 *											</ul>
 * @apiParam {String="SEC","INDEX"} productType Denotes the type of product.
 * @apiParam {String[]} symbol Array of product market code. Sample value: "APLE"
 * @apiParam {Boolean = true} delay Request delay for chart data points.<ul><li>true: Delay Quote</li></ul>
 * @apiParam {String[]} filters Array of desired returning data fields. "Actions" are the columns in a table of time-series data. The Action element is used to specify the desired actions from within a standard set supported by the ViewID. </br>
 *  For summarized data, the following actions are normally available: "CLOSE", "OPEN", "HIGH", "LOW", "COUNT", "VOLUME", "SMA=25,50,75", "WMA=25,50,75","BB=20","RSI=9","MACD=12,26,9","CCI=20","FKD=5,3","SKD=5,3","MOM=12","WR=10","TP=3". </br>
 *  "DATE","OPEN","HIGH","LOW","CLOSE","VOLUME" is the usage foe usual chart plotting without TA. </br>
 *  For tick data, the following actions are normally available: "VALUE", "COUNT", "VOLUME", "CONTRIB_ID" (contributor ID).
 * <p>For CN market, Technical Indicators possible values as below:</p>
 * <table>
 * 		<tr><th>TA : Request Field parameters : Response Field(s)</th><th>TA Name</th><th>TA display position</th></tr>
 * 		<tr><td>SMA : SMA=25,50,75 : SMA(25,50,75).25, SMA(25,50,75).50, SMA(25,50,75).75</td><td>SMA</td><td>Upper indicator</td></tr>
 * 		<tr><td>WMA : WMA=25,50,75 : WMA(25,50,75).25, WMA(25,50,75).50, WMA(25,50,75).75</td><td>WMA</td><td>Upper indicator</td></tr>
 * 		<tr><td>BB : BB=20 : BB(20).UB, BB(20), BB(20).LB</td><td>Bollinger bands</td><td>Upper indicator</td></tr>
 * 		<tr><td>FIB : FIB= : FIB.236.0, FIB.382.0, FIB.500.0, FIB.618.0, FIB.764.0</td><td>Fibonacci line</td><td>Upper indicator</td></tr>
 * 		<tr><td>RSI : RSI=9 : RSI(9)</td><td>RSI</td><td>Lower indicator</td></tr>
 * 		<tr><td>MACD: MACD=12,26,9 : MACD(12,26,9).MACD, MACD(12,26,9).Signal, MACD(12,26,9).Histogram</td><td>MACD</td><td>Lower indicator</td></tr>
 * 		<tr><td>ACD : ACD= : ACD</td><td>Accumlation/Dist.</td><td>Lower indicator</td></tr>
 * 		<tr><td>CCI : CCI=20 : CCI(20)</td><td>CCI</td><td>Lower indicator</td></tr>
 * 		<tr><td>FKD : FKD=5,3 : FKD(5,3).%K, FKD(5,3).%D</td><td>Fast stochastic</td><td>Lower indicator</td></tr>
 * 		<tr><td>SKD : SKD=5,3 : SKD(5,3).%K, SKD(5,3).%D</td><td>Slow stochastic</td><td>Lower indicator</td></tr> 
 * 		<tr><td>MOM : MOM=12 : MOM(12)</td><td>Momentum</td><td>Lower indicator</td></tr> 
 * 		<tr><td>OBV : OBV= : OBV</td><td>On Balance Volume</td><td>Lower indicator</td></tr>
 * 		<tr><td>WR : WR=10 : WR(10)</td><td>William %R</td><td>Lower indicator</td></tr>
 * 		<tr><td>TP: TP=3 : TP(3).Reverse, TP(3).Extreme, TP(3).Trend, TP(3).High, TP(3).Low</td><td>Turning Point</td><td>Upper indicator</td></tr>
 * 		<tr><td>ENV: ENV=14,6 : ENV(14,6).UB, ENV(14,6).LB</td><td>Envelope</td><td>Upper indicator</td></tr>
 * </table>
 * <p>For HK market, Technical Indicators possible values as below:</p>
 * <table>
 * 		<tr><th>TA : Request Field parameters</th><th>TA Name</th></tr>
 * 		<tr><td>SMA : SMA=10,20,50 (Only support SMA=10,20,50)</td><td>SMA</td></tr>
 * </table>
 * @apiParam {Number} period The time period as requested for chart data points.</br>
 * Noted: This field "period" will overwrite startTime/endTime, it means if there is period in request, then start time and end time will not work.
 * <p>For CN market, possible values as below:</p>
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
 * <p>For CN market, supported pairs of period and interval possible values as below:</p>
 * <table>
 * 		<tr><th>Period/Interval</th><th>1 min</th><th>5 min</th><th>15 min</th><th>hourly</th><th>daily</th><th>weekly</th><th>monthly</th><th>quarter monthly</th></tr>
 * 		<tr><td>1 day</td><td>Y</td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td></tr>
 * 		<tr><td>2 days</td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td></tr>
 * 		<tr><td>5 days</td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td></tr>
 * 		<tr><td>10 days</td><td></td><td></td><td>Y</td><td>Y</td><td></td><td></td><td></td><td></td></tr>
 * 		<tr><td>MTD</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td></td></tr>
 * 		<tr><td>1 month</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td></tr>
 * 		<tr><td>2 months</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td></tr>
 * 		<tr><td>3 months</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td></tr>
 * 		<tr><td>6 months</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td></tr>
 * 		<tr><td>QTD</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td></td></tr>
 * 		<tr><td>YTD</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>1 year</td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>2 years</td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>3 years</td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>4 years</td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>5 years</td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td><td>Y</td></tr>
 * 		<tr><td>10 years</td><td></td><td></td><td></td><td></td><td></td><td></td><td>Y</td><td>Y</td></tr>
 * </table> 
 * For HK market, supported pairs of period and interval possible values as below:</p>
 * <table>
 * 		<tr><th>Period/Interval</th><th>1 min</th><th>5 min</th><th>15 min</th><th>hourly</th><th>daily</th><th>weekly</th><th>monthly</th><th>quarter monthly</th></tr>
 * 		<tr><td>1 day</td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
 * 		<tr><td>5 days</td><td></td><td></td><td>Y</td><td></td><td></td><td></td><td></td><td></td></tr>
 * 		<tr><td>1 month</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td></td></tr>
 * 		<tr><td>3 months</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td></td></tr>
 * 		<tr><td>6 months</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td></td></tr>
 * 		<tr><td>1 year</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td></td></tr>
 * 		<tr><td>3 years</td><td></td><td></td><td></td><td></td><td>Y</td><td></td><td></td><td></td></tr>
 * </table> 
 * @apiParam {Date} [startTime] The start time of retrieve a snapshot chart data, format: yyyy-MM-dd'T'HH:mm:ss.SSSZ.
 * </br>Noted: For CN market only, "period" can overwrite the "startTime".
 * @apiParam {Date} [endTime] The end time of retrieve a snapshot chart data, format: yyyy-MM-dd'T'HH:mm:ss.SSSZ.
 * </br>Noted: For CN market only, "period" can overwrite the "endTime".
 * @apiParam {Number} [maxPts] Maximum number of data points to return. Supported values are from 1 to 3600. 
 * </br>Noted: For CN market only, The value can be "0" when user specific only "startTime" or "endTime".
 * @apiParam {Boolean} [timeZoneRequired] Required to return time zone. </br>Noted: For CN market only, and it is a string representing the local time zone at the exchange.
 * 
 * @apiParamExample Request:
 * {
 * 	"market": "US",
 * 	"productType": "SEC",
 * 	"delay": true,
 * 	"symbol": ["APLE"],
 * 	"period": 2,
 * 	"intCnt": 15,
 * 	"intType": "MINUTE",
 * 	"filter": ["DATE",
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
 * @apiSuccess {String} result.delay Delay or real time per request for chart data points.
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
 *       "market": "US",
 *       "productType": "SEC",
 *       "delay": true,
 *       "symbol": "APLE",
 *       "displayName": "APPLE HOSPITLTY",
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