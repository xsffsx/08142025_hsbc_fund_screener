package com.dummy.wpb.product;

import com.dummy.wpb.product.config.FlexRateApiConfig;
import com.dummy.wpb.product.config.TokenizedGoldProductConfig;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.entity.MarketRate;
import com.dummy.wpb.product.entity.TokenizedGoldProduct;
import com.dummy.wpb.product.helper.NumberHelper;
import com.dummy.wpb.product.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class GoldPriceUpdateJobService {

    @Autowired
    private FlexRateApiConfig flexRateApiConfig;

    @Autowired
    private TokenizedGoldProductConfig tokenizedGoldProductConfig;

    @Autowired
    private RestTemplate restTemplate;

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String BODY_REQUEST_ID = "clientsOwnRequestId";

    private static final String BODY_RATE_TIME = "rateTime";

    private static final String BODY_CURRENCY_PAIRS = "currencyPairs";

    @Value("${product.tokenized-gold.replace-currency.target:}")
    private String currencyTarget;

    @Value("${product.tokenized-gold.replace-currency.replacement:}")
    private String currencyReplacement;

    /**
     * Get market rate from FlexRate API
     * 
     * curl --location --request POST 'https://flexrateint.systems.example.com/api/mvp/v1/marketRate' \
     * --header 'Content-Type: application/json' \
     * --header 'Authorization: Basic c21hcnRsb2NhbGFtaGRldjp3ZWFsdGhpdHdwY2hzYmMxMjM0NTY=' \
     * --data '{"clientsOwnRequestId": "smartlocalamhdev", "rateTime": "2023-07-24T11:41:35.567", "currencyPairs": "HKDXGT;XGTHKD"}'
     */
    @SneakyThrows
    public List<MarketRate> getMarketRate(Integer prodId) {
        List<MarketRate> marketRates = new ArrayList<>();
        if (tokenizedGoldProductConfig != null) {
            for (TokenizedGoldProduct gold : tokenizedGoldProductConfig.getProducts()) {
                if (prodId.equals(gold.getProdId().intValue())) {
                    Map<String, Object> body = new HashMap<>();
                    body.put(BODY_REQUEST_ID, flexRateApiConfig.getAccount());
                    body.put(BODY_RATE_TIME, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    body.put(BODY_CURRENCY_PAIRS, gold.getCurrencyPairs());

                    RequestEntity<Map<String, Object>> requestEntity = RequestEntity.post(flexRateApiConfig.getUrl())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HEADER_AUTHORIZATION, flexRateApiConfig.getAuthorization())
                            .body(body);
                    log.debug("MarketRate request = {}", JsonUtil.convertObject2Json(body));
                    Document response = restTemplate.exchange(requestEntity, Document.class).getBody();
                    log.debug("MarketRate response = {}", JsonUtil.convertObject2Json(response));
                    if (response != null) {
                        List<Map<String, Object>> rates = (List<Map<String, Object>>) response.get("rates");
                        for (Map<String, Object> rate : rates) {
                            // update gold market rate
                            MarketRate marketRate = new MarketRate();
                            marketRate.setCurrencyPair(replaceCurrencyCode((String) rate.get(Field.currencyPair))); // replace currency code
                            marketRate.setCurveType((String) rate.get(Field.curveType));
                            marketRate.setMidRate(roundHalfUp(NumberHelper.toDouble((String) rate.get(Field.midRate))));
                            marketRate.setXpeTime((String) rate.get(Field.xpeTime));
                            marketRates.add(marketRate);
                        }
                    }
                    break;
                }
            }
        }
        return marketRates;
    }

    /**
     * Update product market rate
     * @param product
     * @param marketRates
     * @return
     */
    @SneakyThrows
    public Document updateProductMarketRate(Document product, List<MarketRate> marketRates) {
        Document updatedProduct = JsonUtil.deepCopy(product);
        // override exiting marketRate data
        Map<String, Object> digtlAssetCcy = (Map<String, Object>) updatedProduct.get(Field.digtlAssetCcy);
        if (digtlAssetCcy == null) {
            digtlAssetCcy = new HashMap<>();
        }
        digtlAssetCcy.put(Field.marketRate, marketRates);
        updatedProduct.put(Field.digtlAssetCcy, digtlAssetCcy);
        updatedProduct.put(Field.prodPrcUpdtDtTm, new Date());

        return updatedProduct;
    }

    /**
     * Round half up if double value > 1. Keep 2 decimal digits
     */
    private Double roundHalfUp(Double d) {
        if (d !=null && d > 1.0) {
            BigDecimal bigDecimal = BigDecimal.valueOf(d);
            d = bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return d;
    }

    /**
     * Replace currency code in currency pair
     * 
     * [GPBDM-9297] Change currency code from XGTHKD to XAUHKD
     * Call market price API with XAU, but still keep XGT in database 
     */
    private String replaceCurrencyCode(String currencyPair) {
        if (StringUtils.isNotBlank(currencyTarget) && StringUtils.isNotBlank(currencyReplacement)) {
            currencyPair = StringUtils.replace(currencyPair, currencyTarget, currencyReplacement);
        }
        return currencyPair;
    }
}
