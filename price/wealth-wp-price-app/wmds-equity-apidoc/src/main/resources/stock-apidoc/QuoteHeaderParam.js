/**
 * @apiDefine CommonHeaderGroup Header Parameter
 */

/**
 * @apiDefine QuoteHeaderParam
 * 
 * @apiHeader (CommonHeaderGroup) {String="en_US","zh_HK","zh_CN"} X-hhhh-Locale The locale code of the request. Possible values - en_US (English), zh_HK (Traditional Chinese), zh_CN (Simplified Chinese)
 * @apiHeader (CommonHeaderGroup) {String{2}} X-hhhh-Chnl-CountryCode The country code of this entity request.
 * @apiHeader (CommonHeaderGroup) {String{4}} X-hhhh-Chnl-Group-Member The group member code of this entity request.
 * @apiHeader (CommonHeaderGroup) {String} X-hhhh-Channel-Id="OHI" The channel id which identify the source of request.
 * @apiHeader (CommonHeaderGroup) {String} X-hhhh-App-Code The application code which identify the application of request.
 */