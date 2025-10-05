/**
 * @api {get} /wmds/v4.0/predictiveSearch Predictive Search
 * @apiName Predictive Search
 * @apiGroup Common
 * @apiVersion 4.0.1
 * 
 * @apiUse QuoteHeaderParam
 * @apiParam {String} keyword The search word by customer.
 * @apiParam {String[]} assetClasses Denotes the classification of the product.
 * @apiParam {String[]} searchFields Denotes the fields used for search.
 * @apiParam {String[]} sortingFields The fields used for sorting.
 * @apiParam {String} [market] The investment market of the product.</br>
 * 										 GL - Global</br>	
 * 										 NA - North America</br>
 * 										 EU - Europe</br>	
 * 										 AP - Asia Pacific</br>
 * 										 AE - Asia (excl Japan)</br>
 * 									     EM - Emerging Market</br>
 * 										 UK - UK</br>
 * 										 JP - Japan</br>
 * 										 CN - China</br>
 * 										 HK - Hong Kong</br>	
 * 										 OS - Other Single Countries</br>
 * 										 OT - Others
 * @apiParam {Object[]} [filterCriterias] Detailed Criteria for search. 
 * @apiParam {String} filterCriteria.criteriaKey <p>possible values as below:</p>
 * 												  <ul>
 * 									              	<li>isrBndNme</li>
 * 									              	<li>fundHouseCde</li>
 * 									             	<li>allowSellMipProdInd</li>
 * 									              	<li>productKey</li>
 *  											  	<li>switchOutFund</li>
 *  											  	<li>exchange</li>
 * 												  </ul>
 * @apiParam {String} filterCriteria.criteriaValue <p>different value according to different criteriaKey such as : </p>
 * 												  <ul>
 * 									              	<li>013051CZ8 for isrBndNme</li>
 * 									              	<li>HIT for fundHouseCde</li>
 * 									              	<li>M:00005:HK:SEC for productKey</li>
 * 									              	<li>M:FJP:SG:UT for switchOutFund</li>
 * 								                  </ul>
 * @apiParam {String = "gt","ge","eq","lt","le","ne","in"} filterCriteria.operator
 * @apiParam {Number} [topNum] Numbers of records returned from MKD per request.
 * 
 * @apiParamExample Request:
 * {
 * 		"keyword":"00005",
 * 		"assetClasses":["SEC"],
 * 		"searchFields":["symbol","productName","productShortName"],
 * 		"sortingFields":["symbol","productName","productShortName"],
 * 		"topNum":10
 * }
 * 
 * @apiSuccess {Object[]} prodAltNumSegs
 * @apiSuccess {String} prodAltNumSeg.prodCdeAltClassCde Denotes the alternative classification of the product. It indicates whether this alternative product code is belong to International Securities Identification Number (ISIN) or Eurclear Common Code.
 * @apiSuccess {String} prodAltNumSeg.prodAltNum Denotes the alternative number of the product. It indicates the alternate code of a particular product code, which is used to map the product code used in the market to the internal code used in the system.
 * @apiSuccess {String} productType	Denotes the type of the product. The same meaning as the assetClass in request message.
 * @apiSuccess {String} productSubType Denotes the sub type of the product.
 * @apiSuccess {String} countryTradableCode Denotes the country that is tradable for the product. It can be separated by comma if more than 1 countries are tradable.
 * @apiSuccess {String} allowBuy Indicates whether the product is allowed to be bought.
 * @apiSuccess {String} allowSell Indicates whether the product is allowed to be sold.
 * @apiSuccess {String} productName Full name of the product, in required language according to the locale.
 * @apiSuccess {String} productShortName Short name of the product, in required language according to the locale
 * @apiSuccess {String} productCcy Currency product code.
 * @apiSuccess {String} market The investment market of the product.</br>
 * 								GL - Global</br>
 * 								NA - North America</br>
 * 								EU - Europe</br>
 * 								AP - Asia Pacific</br>
 * 								AE - Asia (excl Japan)</br>
 * 								EM - Emerging Market</br>
 * 								UK - UK</br>
 * 								JP - Japan</br>
 *								CN - China</br>
 *								HK - Hong Kong</br>
 *								OS - Other Single Countries</br>
 * 								OT - Others
 * @apiSuccess {String} exchange Only for Equities. Denotes the code for the market place where the products such as stocks were traded. Example codes include: AMS = Amsterdam Stock Exchange, ASE = American Stock Exchange, BER = Berlin Stock Exchange.
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
 * 
 * @apiSuccessExample Success-Response:
 * 		HTTP/1.1 200 OK
 *		[
 *			{
 *				"prodAltNumSegs": [
 *					{
 *						"prodCdeAltClassCde": "M",
 *						"prodAltNum": "XRE"
 *					},
 *					{
 *						"prodCdeAltClassCde": "O",
 *						"prodAltNum": "0P000080SU"
 *					},
 *					{
 *						"prodCdeAltClassCde": "P",
 *						"prodAltNum": "CA-XRE"
 *					},
 *					{
 *						"prodCdeAltClassCde": "U",
 *						"prodAltNum": "FOUSA06AZ4"
 *					},
 *					{
 *						"prodCdeAltClassCde": "T",
 *						"prodAltNum": "XRE.TO"
 *					},
 *					{
 *						"prodCdeAltClassCde": "W",
 *						"prodAltNum": "240122936"
 *					}
 *				],
 *				"productType": "ETF",
 *				"countryTradableCode": "CA",
 *				"allowBuy": "Y",
 *				"allowSell": "Y",
 *				"productName": "iShares S&ampP/TSX Capped REIT Index ETF",
 *				"productShortName": "iShares S&ampP/TSX Capped REIT",
 *				"exchange": "TOR",
 *				"assetCountries": ["CA","OT"],
 *				"assetSectors": ["55","98"],
 *				"symbol": "XRE",
 *				"productSubType": "ETF",
 *				"productCcy": "CAD",
 *				"market": "CA",
 *				"productCode": "240122936",	
 *				"allowSellMipProdInd": "N",
 *				"riskLvlCde": "2"
 *			}
 *		]
 * 
 * @apiUse ErrorMsgResponse
 */