/**
 * @apiDefine CommonPostHeaderGroup Header Parameter
 */

/**
 * @apiDefine QuotePostHeaderParam
 * 
 * @apiHeader (CommonPostHeaderGroup) {String="en_US","zh_HK","zh_CN","zh_TW"} X-hhhh-Locale The locale code of the request. Possible values - en_US (English), zh_HK (Traditional Chinese), zh_TW (Traditional Chinese), zh_CN (Simplified Chinese)
 * @apiHeader (CommonPostHeaderGroup) {String{2}} X-hhhh-Chnl-CountryCode The country code of this entity request.
 * @apiHeader (CommonPostHeaderGroup) {String{4}} X-hhhh-Chnl-Group-Member The group member code of this entity request.
 * @apiHeader (CommonPostHeaderGroup) {String{4}} X-hhhh-Channel-Id The channel id which identify the source of request.
 * @apiHeader (CommonPostHeaderGroup) {String} X-hhhh-App-Code The application code which identify the application of request.
 * @apiHeader (CommonPostHeaderGroup) {String} X-hhhh-Saml The SAML end to end trust token.
 * @apiHeader (CommonPostHeaderGroup) {String} Content-Type Content-Type:application/json
 * @apiHeader (CommonPostHeaderGroup) {String{30}} [X-hhhh-User-Id] User Id (SaaS service id / perm id).
 * @apiHeader (CommonPostHeaderGroup) {String{2}} [X-hhhh-CAM-Level] CAM Level - 30/40.
 * @apiHeader (CommonPostHeaderGroup) {String} [X-hhhh-Src-Device-Id] Device ID.
 * @apiHeader (CommonPostHeaderGroup) {String} [X-hhhh-Session-Correlation-Id] Session Correlation ID.
 * @apiHeader (CommonPostHeaderGroup) {String} [X-hhhh-Src-UserAgent] User Agent. Example - "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405".
 * @apiHeader (CommonPostHeaderGroup) {String} [X-hhhh-Request-Correlation-Id] Request Correlation ID.
 * @apiHeader (CommonPostHeaderGroup) {String} [X-hhhh-IP-Id] Currently populated as the IP address of the application server which created the SAML-like trust token.
 */