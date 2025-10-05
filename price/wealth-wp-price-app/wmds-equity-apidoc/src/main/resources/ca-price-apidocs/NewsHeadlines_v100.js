/**
 * @api {get} /wealth/api/v1/market-data/news/equity/headlines News Headlines
 * @apiName News Headlines
 * @apiGroup News
 * @apiVersion 1.0.0
 * 
 * @apiUse CommonHeaderParams
 * 
 * @apiParam {String} category News categories.
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>TN</td><td>Top News</td></tr>
 * 		<tr><td>LN</td><td>Latest News</td></tr>
 * </table>
 * @apiParam {String} [productCodeIndicator] Indicator for symbol as product code.
 * <p>Possible values as below:</p>
 * <table>
 * 		<tr><th>Value</th><th>Description</th></tr>
 * 		<tr><td>R</td><td>The parameter "symbol" required to input RIC code  </td></tr>
 * 		<tr><td>M</td><td>The parameter "symbol" required to input M code</td></tr>
 * </table>
 * @apiParam {String[]} [symbol] Symbol of requested stock. Max number of symbols is 20.
 * @apiParam {String} [headline] Search the news by news headline.
 * @apiParam {String} [content] Search the news by news content.
 * @apiParam {Number} [recordsPerPage=10] Number of records returned per page. Maximum number of returned news records is 100. </br>
 * 						<strong>Please note that: when there are multiple stock codes input, the pagination is not available.</strong>
 * @apiParam {Number} [pageId=1] Maximum page ID is 10. Maximum number of returned news records is 100.</br>
 * 						<strong>Please note that: when there are multiple stock codes input, the pagination is not available.</strong>           
 *                   
 * @apiParamExample Request:
 * 		{
 * 			"category":"LN",
 * 			"productCodeIndicator": "M",
 * 			"symbol":["00005","00006","00007"]
 * 		}
 *
 * @apiSuccess {Object[]} newsList
 * @apiSuccess {String} newsList.id News ID.
 * @apiSuccess {String} newsList.source News source.
 * @apiSuccess {String} newsList.headline News headline. 
 * @apiSuccess {String} newsList.brief News brief.
 * @apiSuccess {Date} newsList.asOfDateTime News time. Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX</br>
 * 
 * @apiSuccessExample Success-Response:
 *      HTTP/1.1 200 OK
 *{
 *  "newsList": [
 *    {
 *      "id": "294607",
 *      "source": "HK6",
 *      "headline": "hhhh Keeps Prime Rate Unchanged at 5%",
 *      "asOfDateTime": "2016-09-22T14:24:00.000+08:00",
 *      "brief": "hhhh HOLDINGS announced that it maintained the prime rate at 5%. Meanwhile, the bank kept its rate for HKD saving accounts unchanged."
 *    },
 *    {
 *      "id": "293325",
 *      "source": "HK6",
 *      "headline": "<Research Report>C Suisse: Be Selective in Buying PRC, HK Utilities; Recommends PAH, HNR, CYP",
 *      "asOfDateTime": "2016-09-13T12:00:00.000+08:00",
 *      "brief": "Credit Suisse, in its report, said the classification of \"utility\" no longer means such stocks are likely to be a 'safe haven' for investors during market turmoil. Given the ever-changing industry outlook and earnings gaps across the Hong Kong & China utilities spectrum, the broker recommended those stocks with a more certain earnings outlook and inexpensive valuation."
 *    },
 *    {
 *      "id": "292058",
 *      "source": "HK6",
 *      "headline": "<Resumption Ann>HOIFU ENERGY Subsidiary Control Changed, Cashing in About RMB140M",
 *      "asOfDateTime": "2016-09-05T09:18:00.000+08:00",
 *      "brief": "HOIFU ENERGY (00007.HK) announced that it held 55% of its subsidiary Hebei Panbao Zeolite Technology. Zhang Ling, which held the remaining 45% of the subsidiary, disposed her shareholding in the subsidiary to the company before the company resold it to Hoifu United Group. The company's consideration in buying Zhang Ling's stakes in the subsidiary amounted to RMB200 million, including about RMB140 million cash."
 *    }
 *  ]
 *}
 * 
 * @apiUse ErrorMsgResponse
 * 
 */
