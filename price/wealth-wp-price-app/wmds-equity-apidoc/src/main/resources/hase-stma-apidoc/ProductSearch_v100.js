/**
 * @api {get} /wealth/api/v1/market-data/product/multi-predictive-search Product Search
 * @apiName Product Search
 * @apiGroup Common
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String[]} keyword Array for product codes.
 * @apiParam {String[]="SEC", "WRTS"} assetClasses The asset classification of the product.
 * @apiParam {String} [market] The investment market of the product.</br>
 *                       <ul><li>CN - China</li>
 *                          <li>HK - Hong Kong</li>
 *                          <li>US - United States</li></ul>
 * @apiParam {Object[]} [filterCriterias] The criteria for filtering searched results.
 * @apiParam {String} filterCriteria.criteriaKey The key of filtering criteria.<p>Possible values as below:</p>
 * 												  <ul>
 * 									              	<li>countryTradableCode</li>
 *  											  	<li>exchange</li>
 * 												  </ul>
 * @apiParam {String} filterCriteria.criteriaValue The value of filtering criteria.<p>Different value according to different criteriaKey defined.</p>
 * 												  <ul>
 * 									              	<li>countryTradableCode sample usage: "HK:CN"</li>
 * 													<li>exchange sample usage: "SHAS:SZAS"</li>
 * 								                  </ul>
 * @apiParam {String = "gt","ge","eq","lt","le","ne","in"} filterCriteria.operator The operator of filtering criteria.
 * 
 * @apiParamExample Request:
 *  {
 *      "keyword":["000001","60006","00005"],
 *  	"assetClasses":["SEC","WRTS"]
 *  }
 * 
 * @apiSuccess {Object[]} prodAltNumSegs
 * @apiSuccess {String} prodAltNumSeg.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} prodAltNumSeg.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String} market The investment market of the product.</br>
 * @apiSuccess {String} exchange Denotes the code for the market place where the products such as stocks were traded. 
 * @apiSuccess {String} productType	Denotes the type of the product. The same meaning as the assetClasses in request message.
 * @apiSuccess {String} productSubType Denotes the sub type of the product.</p>
 * @apiSuccess {String} countryTradableCode Denotes the country that is tradable for the product. It can be separated by comma if more than 1 countries are tradable.
 * @apiSuccess {String} allowBuy Indicates whether the product is allowed to be bought.
 * @apiSuccess {String} allowSell Indicates whether the product is allowed to be sold.
 * @apiSuccess {String} allowSwInProdInd Indicates whether the product is allowed to be switchin.
 * @apiSuccess {String} allowSwOutProdInd Indicates whether the product is allowed to be switchout.
 * @apiSuccess {String} productName Full name of the product, in required language according to the locale.
 * @apiSuccess {String} productShortName Short name of the product, in required language according to the locale.
 * @apiSuccess {String} productCcy Currency product code.
 * @apiSuccess {String} fundHouseCde Only for Fund. Fund House Code: The company managing the fund.
 * @apiSuccess {String} bondIssuer Only for Bond. Bond issuer.
 * @apiSuccess {String} allowSellMipProdInd Only for Fund. Support RSP Indicator
 * @apiSuccess {String[]} swithableGroups Only for Fund.The switching rule is that Funds are only allowed to switch in the same Switchable Group.
 * @apiSuccess {String[]} assetCountries Denotes the country that is tradable for the product.
 * @apiSuccess {String[]} assetSectors Denotes the code to identify the underlying asset sector. If more than 1 record will be return, items will be separated by comma.
 * @apiSuccess {String[]} parentAssetClasses Denotes the classification of the product.
 * @apiSuccess {String} symbol The symbol of the product.
 * @apiSuccess {String} productCode The code of the product.
 * @apiSuccess {String} riskLvlCde The The risk Level code.
 * @apiSuccess {String} prodStatCde Product State code.
 * @apiSuccess {String} piFundInd Private Fund(PI Fund) Indicator.
 * @apiSuccess {String} deAuthFundInd De-Authorized Fund Indicator.
 * 
 * @apiSuccessExample Success-Response:
 * 	HTTP/1.1 200 OK
 *  [
 *    {
 *      "prodAltNumSegs": [
 *        {
 *          "prodCdeAltClassCde": "I",
 *          "prodAltNum": "GB0005405286"
 *        },
 *        {
 *          "prodCdeAltClassCde": "M",
 *          "prodAltNum": "0005"
 *        },
 *        {
 *          "prodCdeAltClassCde": "P",
 *          "prodAltNum": "HSBA.HK"
 *        },
 *        {
 *          "prodCdeAltClassCde": "T",
 *          "prodAltNum": "0005.HK"
 *        },
 *        {
 *          "prodCdeAltClassCde": "W",
 *          "prodAltNum": "20001968"
 *        }
 *      ],
 *      "productType": "SEC",
 *      "productSubType": "SEC",
 *      "countryTradableCode": "HK",
 *      "allowBuy": "Y",
 *      "allowSell": "Y",
 *      "productName": "hhhh HOLDINGS PLC",
 *      "productShortName": "hhhh HOLDINGS PLC",
 *      "productCcy": "HKD",
 *      "market": "HK",
 *      "exchange": "HKG",
 *      "fundHouseCde": null,
 *      "bondIssuer": null,
 *      "allowSellMipProdInd": "N",
 *      "allowTradeProdInd": null,
 *      "prodTaxFreeWrapActStaCde": "1",
 *      "allowSwInProdInd": "N",
 *      "allowSwOutProdInd": "N",
 *      "fundUnSwitchCode": null,
 *      "swithableGroups": null,
 *      "assetCountries": [
 *        "GB"
 *      ],
 *      "assetSectors": [
 *        "55"
 *      ],
 *      "parentAssetClasses": [
 *        "EME_EQTY"
 *      ],
 *      "channelRestrictList": null,
 *      "symbol": "0005",
 *      "productCode": "20001968",
 *      "riskLvlCde": "5",
 *      "prodStatCde": "A",
 *      "restrOnlScribInd": null,
 *      "piFundInd": null,
 *      "deAuthFundInd": null
 *    }
 *  ]
 *
 * @apiUse ErrorMsgResponse
 */