/**
 * @api {get} /wealth/api/v1/market-data/product/predictive-search PredictiveSearch
 * @apiName PredictiveSearch
 * @apiGroup Common
 * @apiVersion 1.0.0
 * 
 * @apiUse PredsrchHeaderParam
 * 
 * @apiParam {String} keyword The search word by customer.
 * @apiParam {String[]} assetClasses Denotes the classification of the product.
 * @apiParam {String[]} [searchFields] Denotes the fields used for search.
 * @apiParam {String[]} [sortingFields] The fields used for sorting.
 * @apiParam {Number} [topNum] Numbers of records returned per request.
 * 
 * @apiParamExample Request:
 * {
 * 		"keyword":"XX",
 * 		"assetClasses":["SEC"],
 * 		"searchFields":["symbol"],
 * 		"sortingFields":["symbol"],
 * 		"topNum":5
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
 * @apiSuccess {String} productShortName Short name of the product, in required language according to the locale.
 * @apiSuccess {String} productCcy Currency product code.
 * @apiSuccess {String} market The investment market of the product.</br>
 * 								UK - UK</br>
 *                              US - US</br>
 *								HK - Hong Kong</br>
 * @apiSuccess {String} exchange Only for Equities. Denotes the code for the market place where the products such as stocks were traded.
 * @apiSuccess {String} fundHouseCde Only for Fund. Fund House Code: The company managing the fund.
 * @apiSuccess {String} bondIssuer Only for Bond. Bond issuer.
 * @apiSuccess {String} allowSellMipProdInd Only for Fund. Support RSP Indicator
 * @apiSuccess {String} allowSwInProdInd Indicates whether the product is allowed to be switchin.
 * @apiSuccess {String} allowSwOutProdInd Indicates whether the product is allowed to be switchout.
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
 * 		HTTP/1.1 200 OK
 *      [
 *          {
 *              "prodAltNumSegs": [
 *                  {
 *                      "prodCdeAltClassCde": "I",
 *                      "prodAltNum": "US45071R1095"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "M",
 *                      "prodAltNum": "XXIA"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "P",
 *                      "prodAltNum": "US-XXIA"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "T",
 *                      "prodAltNum": "XXIA.O"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "W",
 *                      "prodAltNum": "10012092"
 *                  }
 *              ],
 *              "productType": "SEC",
 *              "productSubType": "SEC",
 *              "countryTradableCode": "US",
 *              "allowBuy": "Y",
 *              "allowSell": "Y",
 *              "productName": "Ixia",
 *              "productShortName": "Ixia",
 *              "productCcy": "USD",
 *              "market": "US",
 *              "exchange": "NAQ",
 *              "fundHouseCde": null,
 *              "bondIssuer": null,
 *              "allowSellMipProdInd": "N",
 *              "allowTradeProdInd": null,
 *              "prodTaxFreeWrapActStaCde": "1",
 *              "allowSwInProdInd": "N",
 *              "allowSwOutProdInd": "N",
 *              "fundUnSwitchCode": null,
 *              "swithableGroups": null,
 *              "assetCountries": null,
 *              "assetSectors": null,
 *              "parentAssetClasses": null,
 *              "channelRestrictList": null,
 *              "chanlCdeList": null,
 *              "symbol": "XXIA",
 *              "productCode": "10012092",
 *              "riskLvlCde": null,
 *              "prodStatCde": "A",
 *              "restrOnlScribInd": null,
 *              "piFundInd": null,
 *              "deAuthFundInd": null
 *          },
 *          {
 *              "prodAltNumSegs": [
 *                  {
 *                      "prodCdeAltClassCde": "I",
 *                      "prodAltNum": "US90137F1030"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "M",
 *                      "prodAltNum": "XXII"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "P",
 *                      "prodAltNum": "US-XXII"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "T",
 *                      "prodAltNum": "XXII.K"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "W",
 *                      "prodAltNum": "10010445"
 *                  }
 *              ],
 *              "productType": "SEC",
 *              "productSubType": "SEC",
 *              "countryTradableCode": "US",
 *              "allowBuy": "Y",
 *              "allowSell": "Y",
 *              "productName": "22nd Century Group Inc",
 *              "productShortName": "22nd Century",
 *              "productCcy": "USD",
 *              "market": "US",
 *              "exchange": "ASQ",
 *              "fundHouseCde": null,
 *              "bondIssuer": null,
 *              "allowSellMipProdInd": "N",
 *              "allowTradeProdInd": null,
 *              "prodTaxFreeWrapActStaCde": "1",
 *              "allowSwInProdInd": "N",
 *              "allowSwOutProdInd": "N",
 *              "fundUnSwitchCode": null,
 *              "swithableGroups": null,
 *              "assetCountries": null,
 *              "assetSectors": null,
 *              "parentAssetClasses": null,
 *              "channelRestrictList": null,
 *              "chanlCdeList": null,
 *              "symbol": "XXII",
 *              "productCode": "10010445",
 *              "riskLvlCde": null,
 *              "prodStatCde": "A",
 *              "restrOnlScribInd": null,
 *              "piFundInd": null,
 *              "deAuthFundInd": null
 *          },
 *          {
 *              "prodAltNumSegs": [
 *                  {
 *                      "prodCdeAltClassCde": "I",
 *                      "prodAltNum": "US06740L5921"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "M",
 *                      "prodAltNum": "XXV"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "P",
 *                      "prodAltNum": "US-XXV"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "T",
 *                      "prodAltNum": "XXV"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "W",
 *                      "prodAltNum": "10011665"
 *                  }
 *              ],
 *              "productType": "SEC",
 *              "productSubType": "SEC",
 *              "countryTradableCode": "US",
 *              "allowBuy": "Y",
 *              "allowSell": "Y",
 *              "productName": "Barclays PLC",
 *              "productShortName": "Barclays",
 *              "productCcy": "USD",
 *              "market": "US",
 *              "exchange": "ASQ",
 *              "fundHouseCde": null,
 *              "bondIssuer": null,
 *              "allowSellMipProdInd": "N",
 *              "allowTradeProdInd": null,
 *              "prodTaxFreeWrapActStaCde": "1",
 *              "allowSwInProdInd": "N",
 *              "allowSwOutProdInd": "N",
 *              "fundUnSwitchCode": null,
 *              "swithableGroups": null,
 *              "assetCountries": null,
 *              "assetSectors": null,
 *              "parentAssetClasses": null,
 *              "channelRestrictList": null,
 *              "chanlCdeList": null,
 *              "symbol": "XXV",
 *              "productCode": "10011665",
 *              "riskLvlCde": null,
 *              "prodStatCde": "A",
 *              "restrOnlScribInd": null,
 *              "piFundInd": null,
 *              "deAuthFundInd": null
 *          },
 *          {
 *              "prodAltNumSegs": [
 *                  {
 *                      "prodCdeAltClassCde": "I",
 *                      "prodAltNum": "US29276K1016"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "M",
 *                      "prodAltNum": "EXXI"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "P",
 *                      "prodAltNum": "US-EXXI"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "T",
 *                      "prodAltNum": "EGC.O"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "W",
 *                      "prodAltNum": "10012957"
 *                  }
 *              ],
 *              "productType": "SEC",
 *              "productSubType": "SEC",
 *              "countryTradableCode": "US",
 *              "allowBuy": "Y",
 *              "allowSell": "Y",
 *              "productName": "Energy XXI Gulf Coast Inc",
 *              "productShortName": "Energy XXI Gulf",
 *              "productCcy": "USD",
 *              "market": "US",
 *              "exchange": "NAQ",
 *              "fundHouseCde": null,
 *              "bondIssuer": null,
 *              "allowSellMipProdInd": "N",
 *              "allowTradeProdInd": null,
 *              "prodTaxFreeWrapActStaCde": "1",
 *              "allowSwInProdInd": "N",
 *              "allowSwOutProdInd": "N",
 *              "fundUnSwitchCode": null,
 *              "swithableGroups": null,
 *              "assetCountries": null,
 *              "assetSectors": null,
 *              "parentAssetClasses": null,
 *              "channelRestrictList": null,
 *              "chanlCdeList": null,
 *              "symbol": "EXXI",
 *              "productCode": "10012957",
 *              "riskLvlCde": null,
 *              "prodStatCde": "A",
 *              "restrOnlScribInd": null,
 *              "piFundInd": null,
 *              "deAuthFundInd": null
 *          },
 *          {
 *              "prodAltNumSegs": [
 *                  {
 *                      "prodCdeAltClassCde": "I",
 *                      "prodAltNum": "US4380831077"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "M",
 *                      "prodAltNum": "FIXX"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "P",
 *                      "prodAltNum": "US-FIXX"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "T",
 *                      "prodAltNum": "FIXX.O"
 *                  },
 *                  {
 *                      "prodCdeAltClassCde": "W",
 *                      "prodAltNum": "10010341"
 *                  }
 *              ],
 *              "productType": "SEC",
 *              "productSubType": "SEC",
 *              "countryTradableCode": "US",
 *              "allowBuy": "Y",
 *              "allowSell": "Y",
 *              "productName": "Homology Medicines Inc",
 *              "productShortName": "Homology",
 *              "productCcy": "USD",
 *              "market": "US",
 *              "exchange": "NAQ",
 *              "fundHouseCde": null,
 *              "bondIssuer": null,
 *              "allowSellMipProdInd": "N",
 *              "allowTradeProdInd": null,
 *              "prodTaxFreeWrapActStaCde": "1",
 *              "allowSwInProdInd": "N",
 *              "allowSwOutProdInd": "N",
 *              "fundUnSwitchCode": null,
 *              "swithableGroups": null,
 *              "assetCountries": null,
 *              "assetSectors": null,
 *              "parentAssetClasses": null,
 *              "channelRestrictList": null,
 *              "chanlCdeList": null,
 *              "symbol": "FIXX",
 *              "productCode": "10010341",
 *              "riskLvlCde": null,
 *              "prodStatCde": "A",
 *              "restrOnlScribInd": null,
 *              "piFundInd": null,
 *              "deAuthFundInd": null
 *          }
 *      ]
 *
 * @apiUse ErrorMsgResponse
 */