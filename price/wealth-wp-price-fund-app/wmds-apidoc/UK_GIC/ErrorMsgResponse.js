 /**
 * @apiDefine ErrorMsgResponse
 *
 * @apiError {String} 	text The Description of the error response
 * @apiError {String} 	responseCode Indicate if the request was processed sucessfully or not
 * @apiError {String} 	reasonCode Error code returned for a specific error
 * @apiError {String} 	traceCode unique code returned for error tracing.
 *
 *
 *
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 400 Bad Request
 *     {
 *        "text": "the Description of the error",
 *        "responseCode": "2",
 *        "reasonCode": "MDSCM01",
 *        "traceCode": "7306481589450764286"
 *     }
 */