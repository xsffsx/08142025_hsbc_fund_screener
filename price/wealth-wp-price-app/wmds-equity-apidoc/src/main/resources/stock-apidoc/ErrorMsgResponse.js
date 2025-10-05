 /**
 * @apiDefine ErrorMsgResponse
 *
 * @apiError {String} [responseCode] Indicate if the request was processed sucessfully or not
 * @apiError {String[]} reasonCode The reason code of the error response
 * @apiError {String} [text] The description of the error response
 * @apiError {String} traceCode Unique code returned for error tracing
 *
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 400 Bad Request
 *     {
 *        "reasonCode": ["MDSEX500"],
 *        "traceCode": "2219864155114419032"
 *     }
 */