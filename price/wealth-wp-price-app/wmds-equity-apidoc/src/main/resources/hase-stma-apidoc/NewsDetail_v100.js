/**
 * @api {get} /wealth/api/v1/market-data/news/equity/detail News Detail
 * @apiName News Detail
 * @apiGroup News
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String} market The investment market of the product. 
 *											<ul>
 *												<li>HK - Hong Kong</li>
 *											<li>US - United States</li>	</ul>
 * @apiParam {String} id  News ID used to retrieve its detail.
 * @apiParam {Boolean="true","false"} translate  Toggle (enable / disable) the news translation. For US market only.
 * @apiParam {String} source  News source. For HK market only. E.g. ETNet.
 *                            
 * @apiParamExample Request:
 * 		{	
 * 			"market":"HK",
 * 			"id":"326416",
 * 			"source":"ETNet"
 * 		}
 *  
 *  
 * @apiSuccess {String} id  News ID.
 * @apiSuccess {String} headline  News headline. 
 * @apiSuccess {String} content  News content.
 * @apiSuccess {Boolean="true","false"} isTableFormat  Identify if news content is with table formatting.  For HK market only.
 * @apiSuccess {Number} numOfCharPerLine  Number of characters per line. To be used by front-end to calculate the min-width of the content for table format news. </br>
 * 											Noted: This field is empty if "isTableFormat=false".  For HK market only.
 * @apiSuccess {Date} asOfDateTime  News time. Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX.
 *  
 * @apiSuccessExample Success-Response:
 *      HTTP/1.1 200 OK
 *	{
 * 		"id": "326416",
 * 		"headline": "TENCENT Becomes One of Top 10 Listed Companies Worldwide",
 * 		"content": "Bloomberg News reported that with a market cap of US$279 billion, TENCENT (00700.HK) beat Wells Fargo to become one of the top ten listed companies in the world in terms of market value.<Br><Br>Of the top ten companies, six are IT companies and the top four are all IT companies. Apple Inc. is the company with the highest market value in the world, followed by Google's parent company Alphabet (2nd), Microsoft (3rd), Amazon (4th) and Facebook (6th).",
 * 		"isTableFormat": false,
 *      "numOfCharPerLine": null,
 *      "asOfDateTime": "2017-04-07T08:53:00.000+08:00"
 *	}
 *
 * @apiUse ErrorMsgResponse
 * 
 */
