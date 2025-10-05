/**
 * @api {get} /wealth/api/v1/market-data/product/predictive-search Predictive Search
 * @apiName Predictive Search
 * @apiGroup Common
 * @apiVersion 1.0.0
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String} market The investment market of the product.</br>
 *								<ul><li>CN - China</li>
 *									<li>HK - Hong Kong</li>
 *									<li>US - United States</li></ul>
 * @apiParam {String} keyword The searching keyword as inputed by customer.</br>
 * 								Sample value: "5", "hhhh", "滙豐", "汇丰"
 * @apiParam {String[]="SEC", "WRTS"} assetClasses The asset classification of the product.
 * @apiParam {Object[]} [filterCriterias] The criteria for filtering searched results.
 * @apiParam {String} filterCriteria.criteriaKey The key of filtering criteria.<p>Possible values as below:</p>
 * 												  <ul>
 * 									              	<li>productKey</li>
 *  											  	<li>exchange</li>
 * 												  </ul>
 * @apiParam {String} filterCriteria.criteriaValue The value of filtering criteria.<p>Different value according to different criteriaKey defined.</p>
 * 												  <ul>
 * 									              	<li>productKey sample usage: "M:00005:HK:SEC"</li>
 * 													<li>exchange sample usage: "SHAS:SZAS"</li>
 * 								                  </ul>
 * @apiParam {String = "gt","ge","eq","lt","le","ne","in"} filterCriteria.operator The operator of filtering criteria.
 * @apiParam {Number} [topNum] Numbers of records returned per request. Max is 100.</br>
 * 									Sample value: 10
 * @apiParam {String} [channelRestrictCode] Filter products using Channel Restriction Code.
 * @apiParam {String} [restrOnlScribInd] Filter products using Channel Restriction Indicator.
 * 
 * @apiParamExample Request:
 *  {
 *      "market": "HK",
 *  	"keyword": "5",
 *  	"assetClasses": ["SEC"],
 *  	"topNum": 1
 *  }
 * 
 * @apiSuccess {Object[]} prodAltNumSegs
 * @apiSuccess {String} prodAltNumSeg.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} prodAltNumSeg.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String} market The investment market of the product.</br>
 * @apiSuccess {String} exchange Denotes the code for the market place where the products such as stocks were traded. 
 * </br>Possible values as below:</p>
 * <table>
 * 		<tr><th>Exchange Code</th><th>Description</th></tr>
 * 		<tr><td>SHAS</td><td>Shanghai A Share in StockConnect list</td></tr>
 * 		<tr><td>CHSHMK</td><td>Shanghai A Share out of StockConnect list</td></tr>
 * 		<tr><td>SZAS</td><td>Shenzhen A Share in StockConnect list</td></tr>
 * 		<tr><td>CHSZMK</td><td>Shenzhen A Share out of StockConnect list</td></tr>
 * 		<tr><td>SEHK</td><td>HK exchange</td></tr>
 * </table>
 * @apiSuccess {String} productType	Denotes the type of the product. The same meaning as the assetClasses in request message.
 * @apiSuccess {String} productSubType Denotes the sub type of the product.</p>
 * For HK market(exchange="SEHK") only.
 * <table>
 * 		<tr><th>FE Grouping</th><th>Product Type</th><th>Product Sub Type</th></tr>
 * 		<tr><td>Warrant</td><td>WRTS</td><td>LWTS</td></tr>
 *      <tr><td>CBBC</td><td>WRTS</td><td>LCBB</td></tr>
 * </table>
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