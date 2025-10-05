# product-gold-price-update-job

This is an internal job calling FlexRate API to get `Market Rate (digtlAssetCcy.marketRate[*])` and update in Smart+ local DB for Tokenized Gold products.

### Command
/appvol/product-spring-batch/bin/HKHBAP/cron/wpc_tokenized_gold_price_update.sh HK HBAP

### Implement

#### (1) Call FlexRate API
> com.dummy.wpb.product.GoldPriceUpdateJobService.getMarketRate

> curl --location --request POST 'https://flexrateint.systems.example.com/api/mvp/v1/marketRate' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic c21hcnRsb2NhbGFtaGRldjp3ZWFsdGhpdHdwY2hzYmMxMjM0NTY=' \
--data '{"clientsOwnRequestId": "smartlocalamhdev", "rateTime": "2023-07-24T11:41:35.567", "currencyPairs": "HKDXGT;XGTHKD"}'

https://confluence.hk.dummy/pages/viewpage.action?pageId=527117017

#### (2) Update market rate in DB
> com.dummy.wpb.product.GoldPriceUpdateJobService.updateProductMarketRate

### Reference
https://wpb-confluence.systems.example.com/display/WWS/SMART+calls+FlexRate+API+to+get+latest+EOD+price
