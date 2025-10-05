/**
 * @api {get} /wealth/api/v1/market-data/equity/currentIPO CurrentIPO
 * @apiName CurrentIPO
 * @apiGroup Quote
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String} market The investment market of the product. 
 *											<ul>
 *												<li>HK - Hong Kong</li></ul>
 *                            
 * @apiParamExample Request:
 * 		{	
 * 			"market":"HK"
 * 		}
 *  
 *  
 * @apiSuccess {Object[]} currentIPO
 * @apiSuccess {String} currentIPO.symbol  Index code. 
 * @apiSuccess {Number} currentIPO.boardLot  Board Lot.
 * @apiSuccess {String} currentIPO.nature  Industry sector the company in.
 * @apiSuccess {String} currentIPO.ipoSponsor  IPO Sponsor.
 * @apiSuccess {Date} currentIPO.asOfDateTime  SEHK time (i.e. timestamp from HKEx alert line). </br>
 * 										Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX.</br>
 * 										Note: second field is always 00.
 *  
 * @apiSuccessExample Success-Response:
 *      HTTP/1.1 200 OK
 *{
 *	"currentIPO":[
 *		{
 *			"symbol": "08616",
 *			"boardlot": 10000,
 *			"nature": "建築"
 *			"ipoSponsor": "豐盛融資"
 *			"asOfDateTime": "2020-04-01T11:49:00.000+0800"
 *		}
 *	]
 *}
 *
 *
 * @apiUse ErrorMsgResponse
 * 
 */
