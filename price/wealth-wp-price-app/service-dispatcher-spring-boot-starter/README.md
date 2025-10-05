dispatcher: 
  service-api: 
    QUOTES: 
      request-parameters: appCode, market, productType
      rules: 
        appCode_market_productType: HCBS-SRBP_HK_OTPS
        appCode_market: HCBS-SRBP_HK
        appCode: HCBS-SRBP
      end-point: 
        HCBS-SRBP_HK_OTPS: oesTris
        HCBS-SRBP_HK: midfs
        HCBS-SRBP: oesTris
        DEFAULT: oesTris