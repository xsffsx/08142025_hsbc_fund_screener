/**
 * @api {post} /wealth/api/v1/market-data/admin/agreement/update-user-agreement UpdateUserAgreement
 * @apiName UpdateUserAgreement
 * @apiGroup Quote
 * @apiVersion 1.0.3
 * 
 * @apiUse QuotePostHeaderParam
 * 
 * @apiParam {String} documentId Denotes user agreement document Id.
 * @apiParam {Date} expiryDate Denotes the expiry date of document.The expiry date shoud not be earlier than current date. Expiry Date Format: yyyy-MM-dd
 * @apiParam {Boolean} status The status of user agreement. <ul><li>true</li><li>false</li></ul>
 * 
 * @apiParamExample Request:
 *  {
 *  	"documentId": "1",
 *  	"expiryDate": "2022-03-31",
 *  	"status": true
 *  }
 * 
 * @apiUse UpdateUserAgreementResponse
 * 
 * @apiSuccessExample Success-Response:
 *  HTTP/1.1 200 OK
 *  {
 *  	"exchangeCode": [
 *  		"TOR",
 *  		"NLB",
 *  		"TSX"
 *  	],
 *  	"documentId": "1",
 *  	"expiryDate": "2022-03-31",
 *  	"responseCode": "0",
 *  	"status": true
 *  }
 * 
 * @apiUse ErrorMsgResponse
 */

/**
 * @apiDefine UpdateUserAgreementResponse
 * 
 * @apiSuccess {Object[]} exchangeCode Market Product Exchange Codes.
 * @apiSuccess {String} documentId user agreement document Id.
 * @apiSuccess {Date} expiryDate The expiry date of user agreement.
 * @apiSuccess {String} responseCode The code of response.<table><tr><td>Value</td><td>Description</td></tr><tr><td>0</td><td>success code</td></table>
 * @apiSuccess {Boolean} status The status of user agreement.
 * 
 */