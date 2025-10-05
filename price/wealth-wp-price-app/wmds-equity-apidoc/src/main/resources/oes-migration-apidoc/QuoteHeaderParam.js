/**
 * @apiDefine CommonHeaderGroup Header Parameter
 */

/**
 * @apiDefine QuoteHeaderParam
 * 
 * @apiHeader (CommonHeaderGroup) {String="en_US","zh_HK","zh_CN"} X-hhhh-Locale The locale code of the request. Possible values - en_US (English), zh_HK (Traditional Chinese), zh_CN (Simplified Chinese)
 * @apiHeader (CommonHeaderGroup) {String{2}} X-hhhh-Chnl-CountryCode The country code of this entity request HK.
 * @apiHeader (CommonHeaderGroup) {String{4}} X-hhhh-Chnl-Group-Member The group member code of this entity request HCBS.
 * @apiHeader (CommonHeaderGroup) {String{4}} X-hhhh-Channel-Id="OHI/OHB/OHCC/MOB" The channel id which identify the source of request OHI/MOB mapping X-hhhh-CUSTOMER-ID,OHB/OHCC mapping X-hhhh-STAFF-ID.
 * @apiHeader (CommonHeaderGroup) {String} X-hhhh-App-Code The application code which identify the application of request.
 * @apiHeader (CommonHeaderGroup) {String} X-hhhh-Saml The SAML end to end trust token
 * @apiHeader (CommonHeaderGroup) {String} [X-hhhh-CUSTOMER-ID]  customer id
 * @apiHeader (CommonHeaderGroup) {String} [X-hhhh-STAFF-ID]  staff id
 * @apiHeader (CommonHeaderGroup) {String} [X-hhhh-IP-Id] Currently populated as the IP address of the application server which created the SAML-like trust token
 * @apiHeader (CommonHeaderGroup) {String} [X-hhhh-Session-Correlation-Id] 
 * @apiHeader (CommonHeaderGroup) {String} [X-hhhh-Src-UserAgent] User Agent. Example - "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405"
 * @apiHeader (CommonHeaderGroup) {String} [X-hhhh-Session-Correlation-Id] Session Correlation ID
 * @apiHeader (CommonHeaderGroup) {String} [X-hhhh-Src-Device-Id]  Device ID
 * @apiHeader (CommonHeaderGroup) {String{30}} [X-hhhh-User-Id]   (SaaS service id / perm id)
 */