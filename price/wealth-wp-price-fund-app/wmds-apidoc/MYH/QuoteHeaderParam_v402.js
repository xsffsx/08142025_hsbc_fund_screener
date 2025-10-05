/**
 * @apiDefine CommonHeaderGroup Header Parameter
 */

/**
 * @apiDefine QuoteHeaderParam_v402
 * 
 * @apiHeader (CommonHeaderGroup) {String} X-hhhh-Locale The locale code of the request. Sample values- en (English), zh_HK (Traditional Chinese), zh (Simplified Chinese)
 * @apiHeader (CommonHeaderGroup) {String{2}} 	X-hhhh-Chnl-CountryCode The country code of this entity request.
 * @apiHeader (CommonHeaderGroup) {String{4}} 	X-hhhh-Chnl-Group-Member The group member code of this entity request.
 * @apiHeader (CommonHeaderGroup) {String} 	X-hhhh-Channel-Id The channel id which identify the channel of request.
 * @apiHeader (CommonHeaderGroup) {String} 	X-hhhh-App-Code  The application code which identify the application of request.
 * @apiHeader (CommonHeaderGroup) {String} 	[X-hhhh-Line-Of-Business] The line of business.
 * @apiHeader (CommonHeaderGroup) {String} 	Authorization Authorization key for call from TP.
 * @apiHeader (CommonHeaderGroup) {String} 	X-hhhh-Saml X-hhhh-Saml key for call from WAG and PCF.
 */