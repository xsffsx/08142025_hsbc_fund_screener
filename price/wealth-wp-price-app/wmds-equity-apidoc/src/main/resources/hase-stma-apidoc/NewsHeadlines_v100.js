/**
 * @api {get} /wealth/api/v1/market-data/news/equity/headlines News Headlines
 * @apiName News Headlines
 * @apiGroup News
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String} market The investment market of the product. 
 *											<ul>
 *												<li>HK - Hong Kong</li>
 *											<li>US - United States</li>	</ul>
 * @apiParam {String} category
 * <p>For US market, possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>ALL</td><td>All topics</td></tr>
 * 		<tr><td>overalleconomic</td><td>Overall Economic</td></tr>
 * 		<tr><td>interestrate</td><td>Interest Rate</td></tr>
 * 		<tr><td>economicindicators</td><td>Economic Indicators</td></tr>
 * 		<tr><td>fund</td><td>Fund</td></tr>
 *  	<tr><td>ipodebtissue</td><td>IPO/Debt Issue</td></tr>
 *  	<tr><td>creditrating</td><td>Credit Rating</td></tr>
 *  	<tr><td>corporateresults</td><td>Corporate Results</td></tr>
 *  	<tr><td>ma</td><td>M & A</td></tr>
 *  	<tr><td>internationaltrade</td><td>International Trade</td></tr>
 *  	<tr><td>stockmarket</td><td>Stock Market</td></tr>
 *  	<tr><td>moneymarket</td><td>Money Market</td></tr>
 *  	<tr><td>bondmarket</td><td>Bond Market</td></tr>
 *  	<tr><td>energymarket</td><td>Energy Market</td></tr>
 *  	<tr><td>goldpreciousmetals</td><td>Gold & Precious Metals</td></tr>
 *  	<tr><td>basicmetals</td><td>Basic Metals</td></tr>
 *  	<tr><td>financeInsurance</td><td>Finance & Insurance</td></tr>
 *  	<tr><td>realestate</td><td>Real Estate</td></tr>
 *  	<tr><td>automobiles</td><td>Automobiles</td></tr>
 *  	<tr><td>steel</td><td>Steel</td></tr>
 *  	<tr><td>electronicelectrical</td><td>Electronic & Electrical</td></tr>
 *  	<tr><td>telecommunications</td><td>Telecommunications</td></tr>
 *  	<tr><td>airtransportshipping</td><td>Air Transport / Shipping</td></tr>
 *  	<tr><td>chemical</td><td>Chemical</td></tr>
 *  	<tr><td>food</td><td>Food</td></tr>
 *  	<tr><td>electricalappliances</td><td>Electrical Appliances</td></tr>
 *  	<tr><td>healthmedicines</td><td>Health / Medicines</td></tr>
 *  	<tr><td>internetwww</td><td>Internet / World Wide Web</td></tr>
 *  	<tr><td>multiindustry</td><td>Multi-industry</td></tr>
 * </table>
 * <p>For HK market, possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th><th></th></tr>
 * 		<tr><td>all</td><td>All Real Time News</td><td>所有即時新聞</td></tr>
 * 		<tr><td>china</td><td>China News</td><td>中國新聞</td></tr>
 * 		<tr><td>blocktrade</td><td>Block Trade</td><td>大手成交</td></tr>
 * 		<tr><td>economics</td><td>Economics Activities</td><td>經濟活動/數據</td></tr>
 * 		<tr><td>regulatory</td><td>Regulatory Bodies</td><td>監管機構</td></tr>
 *  	<tr><td>result</td><td>Result Announcements</td><td>業績公佈(派息)</td></tr> 
 *  	<tr><td>business</td><td>Business Operations</td><td>業務狀況</td></tr> 
 *  	<tr><td>market</td><td>Market Trend</td><td>市場走勢/活動</td></tr> 
 *   	<tr><td>brokerage</td><td>Brokerage Activities</td><td>經濟活動</td></tr>
 *     	<tr><td>company</td><td>Company Structure</td><td>公司結構</td></tr>
 *     	<tr><td>finance</td><td>Financing Activities</td><td>財務融資活動</td></tr>
 *     	<tr><td>lawsuits</td><td>Lawsuits</td><td>訴訟</td></tr>
 *     	<tr><td>research</td><td>Brokerage Report</td><td>公司研究報告</td></tr>
 *     	<tr><td>others</td><td>Others</td><td>其他</td></tr>
 *      <tr><td>ipo</td><td>IPO News</td><td>新股新聞</td></tr>
 *     	<tr><td>hkex</td><td>HKEX News</td><td>港交所新聞</td></tr>
 *     	<tr><td>commentary_Benny</td><td>NA</td><td>梁業豪應市策略部署 (Chinese version only)</td></tr>
 *     	<tr><td>commentary_Dennis</td><td>NA</td><td>羅國森 (Chinese version only)</td></tr>
 *     	<tr><td>relatednews</td><td>Related News</td><td>相關新聞</td></tr>
 * </table>
 * @apiParam {String} [productCodeIndicator] Indicator for symbol as product code.
 * <p>Possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>R</td><td>The parameter "symbol" required to input RIC code  </td></tr>
 * 		<tr><td>M</td><td>The parameter "symbol" required to input M code</td></tr>
 * </table>
 * @apiParam {String[]} [symbol] For HK market, Symbol of requested stock, Max number of symbols is 20.
 * 								</br>it's expected to input while news category is "relatednews", and required for category "relatednews", other categories will ignore it.
 *                               </br>For US market, Symbol of requested stock, Max number of symbols is 1.
 * @apiParam {String} [location]
 * <p>For US market only, possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>ALL</td><td>All Locations</td></tr>
 * 		<tr><td>europe</td><td>Europe</td></tr>
 * 		<tr><td>asia</td><td>Asia</td></tr>
 * 		<tr><td>emergingmarkets</td><td>Emerging Markets</td></tr>
 * 		<tr><td>greaterchina</td><td>Greater China</td></tr>
 * 		<tr><td>unitedstate</td><td>United State</td></tr>
 *  	<tr><td>hongkong</td><td>Hong Kong</td></tr>
 *  	<tr><td>taiwan</td><td>Taiwan</td></tr>
 *  	<tr><td>china</td><td>China</td></tr>
 *  	<tr><td>japan</td><td>Japan</td></tr>
 * </table>
 * @apiParam {Boolean = true, false} translate  Toggle (enable / disable) the news translation. For US market only.
 * @apiParam {Number} [recordsPerPage=10] For HK market, when the "category = all", Maximum page ID is 50, otherwise Maximum page ID is 25. If the pageId is more than the limitation (let say 50), MDS will return empty result.The Maximum records per page is 20. If the recordsPerPageoptional is more than 20, MDS will returned HTTP 400.</br>
 * 					            For US market, The Maximum records per page is 20. If the recordsPerPage is more than 20, MDS will returned HTTP 400.There is no limitation to the page ID. But please note that the maximum returned news is 100 in total.
 * @apiParam {Number} [pageId=1] Number of page. For Hk market, if category=’all’ Maximum page ID is 50, otherwise Maximum page ID is 25.</br>
 *                                 For US market, there is no limitation to the page ID. But please note that the maximum returned news is 100 in total.
 *
 *                   
 * @apiParamExample Request:
 * 		{
 * 			"market":"HK",
 * 			"category":"relatednews",
 * 			"productCodeIndicator": "M",
 * 			"symbol":["00005","00006","00007"]
 * 		}
 *
 * @apiSuccess {Object[]} newsList
 * @apiSuccess {String} newsList.id News ID.
 * @apiSuccess {String} newsList.source For HK market only, News source.
 * @apiSuccess {String} newsList.headline News headline. 
 * @apiSuccess {String[]} newsList.relatedCodes For HK market only, Related stock codes.
 * @apiSuccess {String} newsList.brief  News brief.
 * @apiSuccess {Date} newsList.asOfDateTime News time. Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX</br>
 * 
 * @apiSuccessExample Success-Response:
 *      HTTP/1.1 200 OK
 *{
 *  "newsList": [
 *    {
 *      "id": "294607",
 *      "source": "ETNet",
 *      "headline": "hhhh Keeps Prime Rate Unchanged at 5%",
 *      "relatedCodes": ["1997”, “12”, “16”, “17”, “5”, “700”, “1299”, “388”, “83”, “101”, “173”, “683”, “1113”, “1972"],
 *      "asOfDateTime": "2016-09-22T14:24:00.000+08:00",
 *      "brief": "hhhh HOLDINGS announced that it maintained the prime rate at 5%. Meanwhile, the bank kept its rate for HKD saving accounts unchanged."
 *    },
 *    {
 *      "id": "293325",
 *      "source": "HK6",
 *      "headline": "<Research Report>C Suisse: Be Selective in Buying PRC, HK Utilities; Recommends PAH, HNR, CYP",
 *      "relatedCodes": ["1997”, “12”, “16”, “17”, “5”, “700”, “1299”, “388”, “83”, “101”, “173”, “683”, “1113”, “1972"],
 *      "asOfDateTime": "2016-09-13T12:00:00.000+08:00",
 *      "brief": "Credit Suisse, in its report, said the classification of \"utility\" no longer means such stocks are likely to be a 'safe haven' for investors during market turmoil. Given the ever-changing industry outlook and earnings gaps across the Hong Kong & China utilities spectrum, the broker recommended those stocks with a more certain earnings outlook and inexpensive valuation."
 *    },
 *    {
 *      "id": "292058",
 *      "source": "HK6",
 *      "headline": "<Resumption Ann>HOIFU ENERGY Subsidiary Control Changed, Cashing in About RMB140M",
 *      "relatedCodes": ["1997”, “12”, “16”, “17”, “5”, “700”, “1299”, “388”, “83”, “101”, “173”, “683”, “1113”, “1972"],
 *      "asOfDateTime": "2016-09-05T09:18:00.000+08:00",
 *      "brief": "HOIFU ENERGY (00007.HK) announced that it held 55% of its subsidiary Hebei Panbao Zeolite Technology. Zhang Ling, which held the remaining 45% of the subsidiary, disposed her shareholding in the subsidiary to the company before the company resold it to Hoifu United Group. The company's consideration in buying Zhang Ling's stakes in the subsidiary amounted to RMB200 million, including about RMB140 million cash."
 *    }
 *  ]
 *}
 * 
 * @apiUse ErrorMsgResponse
 * 
 */
