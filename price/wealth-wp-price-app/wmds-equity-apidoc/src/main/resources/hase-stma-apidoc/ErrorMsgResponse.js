 /**
 * @apiDefine ErrorMsgResponse
 *
 * @apiError {String} [responseCode] Indicate if the request was processed successful or not
 * @apiError {String[]} reasonCode The reason code of the error response
 * @apiError {String} [text] The description of the error response
 * @apiError {String} traceCode Unique code returned for error tracing
 *
 * @apiErrorExample Error-Response:
 *  HTTP/1.1 400 BadRequest
 *  {
 *  	{
 *  		"reasonCode": "MDSEQTY40001",
 *  		"text": "Request parameter is not standard JSON format",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY40002",
 *  		"text": "Unsupported Site",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY40003",
 *  		"text": "Request parameter is mandatory and can't be empty",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY40004",
 *  		"text": "Request parameter don't match the rules",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY40005",
 *  		"text": "Request parameter violate minimum constraint",
 *  		"traceCode": "**"
 *  	}
 *  }
 *  HTTP/1.1 500 InternalServerError
 *  {
 *  	{
 *  		"reasonCode": "MDSEQTY50001",
 *  		"text": "Internal Error",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50002",
 *  		"text": "Internal Error",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50003",
 *  		"text": "Internal Error",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50011",
 *  		"text": "Internal Error",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50012",
 *  		"text": "Internal Error",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50013",
 *  		"text": "Internal Error",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50014",
 *  		"text": "Internal Error",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50021",
 *  		"text": "Internal Error",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50022",
 *  		"text": "Internal Error",
 *  		"traceCode": "**"
 *  	}
 *  }
 *  HTTP/1.1 504 GatewayTimeout
 *  {
 *  	{
 *  		"reasonCode": "MDSEQTY50401",
 *  		"text": "Access vendor encounter error",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50402",
 *  		"text": "Can't access PredSrch service",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50403",
 *  		"text": "Can't access Tris service",
 *  		"traceCode": "**"
 *  	}
 *  }
 *  HTTP/1.1 502 BadGateway
 *  {
 *  	{
 *  		"reasonCode": "MDSEQTY50201",
 *  		"text": "Vendor invalid response",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50202",
 *  		"text": "PredSrch invalid response",
 *  		"traceCode": "**"
 *  	},
 *  	{
 *  		"reasonCode": "MDSEQTY50203",
 *  		"text": "Tris invalid response",
 *  		"traceCode": "**"
 *  	}
 *  }
 */