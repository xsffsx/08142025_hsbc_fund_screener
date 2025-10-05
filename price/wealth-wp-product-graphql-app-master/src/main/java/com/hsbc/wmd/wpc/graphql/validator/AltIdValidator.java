package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.ConfigurationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class AltIdValidator implements ChangeValidator {

    private String prodkeyPrimary;
    private String prodkeyAll;
    private String prodkeyOther;

    private final MongoTemplate mongoTemplate;

    public AltIdValidator(MongoTemplate mongoTemplate, ConfigurationService configurationService) {
        this.mongoTemplate = mongoTemplate;
        Map<String, Object> configMap = configurationService.getLegacyConfigByFilter();
        if (null == configMap) {
            return;
        }
        prodkeyPrimary = (String) configMap.get("BATCH.PRODKEY.PRIMARY");
        prodkeyAll = (String) configMap.get("BATCH.PRODKEY.ALL");
        prodkeyOther = (String) configMap.get("BATCH.PRODKEY.OTHER");
    }


    @Override
    public List<Error> validate(Map<String, Object> oldProduct, Map<String, Object> newProduct) {
        Map<String, String> verifiedAltIds = new HashMap<>();

        Map<String, String> newAltIdMaps = toAltIdMap(newProduct);

        String prodStatCde = (String) newProduct.get(Field.prodStatCde);

        if (StringUtils.equals(prodStatCde, "D")) {
            return Collections.emptyList();
        }

        if (MapUtils.isEmpty(oldProduct)) {
            verifiedAltIds.putAll(newAltIdMaps);
        } else {
            Map<String, String> oldAltIdMaps = toAltIdMap(oldProduct);
            for (Map.Entry<String, String> entry : newAltIdMaps.entrySet()) {
                if (!entry.getValue().equals(oldAltIdMaps.get(entry.getKey()))) {
                    verifiedAltIds.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return doValidate(newProduct, verifiedAltIds);
    }

    public List<Error> doValidate(Map<String, Object> product, Map<String, String> altIds) {
        if (MapUtils.isEmpty(altIds)) {
            return Collections.emptyList();
        }

        List<Error> errors = new ArrayList<>();
        for (Map.Entry<String, String> entry : altIds.entrySet()) {
            String prodCdeAltClassCde = entry.getKey();

            if (!needToValidate(prodCdeAltClassCde)) {
                continue;
            }

            String prodAltNum = entry.getValue();

            Query query = buildQuery(product, prodCdeAltClassCde, prodAltNum);
            List<Document> existProductList = mongoTemplate.find(query, Document.class, CollectionName.product.name());

            if (CollectionUtils.isNotEmpty(existProductList)) {
                StringBuilder message = new StringBuilder();
                message.append(String.format("This altId {prodTypeCde: %s,prodCdeAltClassCde: %s, prodAltPrimNum: %s} conflicts with: [",
                        product.get(Field.prodTypeCde), prodCdeAltClassCde, prodAltNum));
                for (Document existProduct : existProductList) {
                    String prodInfo = String.format(
                            "{ctryRecCde: %s, grpMembrRecCde: %s, prodTypeCde: %s, prodAltPrimNum: %s}, ",
                            product.get(Field.ctryRecCde),
                            product.get(Field.grpMembrRecCde),
                            product.get(Field.prodTypeCde),
                            existProduct.get("prodAltPrimNum"));
                    message.append(prodInfo);
                }
                message.delete(message.length() - 2, message.length());
                message.append("]");

                Error error = new Error();
                String jsonPath = String.format("altId[prodCdeAltClassCde=%s]", prodCdeAltClassCde);
                error.setJsonPath(jsonPath);
                error.setCode(jsonPath + "@duplicated");
                error.setMessage(message.toString());

                errors.add(error);
            }
        }
        return errors;
    }

    private boolean needToValidate(String prodCdeAltClassCde) {
        return Pattern.matches(prodkeyPrimary, prodCdeAltClassCde)
                || Pattern.matches(prodkeyOther, prodCdeAltClassCde)
                || Pattern.matches(prodkeyAll, prodCdeAltClassCde);
    }

    private Query buildQuery(Map<String, Object> product, String prodCdeAltClassCde, String prodAltNum) {
        Criteria altIdCriteria = new Criteria()
                .and(Field.prodCdeAltClassCde).is(prodCdeAltClassCde)
                .and(Field.prodAltNum).is(prodAltNum);
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(product.get(Field.ctryRecCde))
                .and(Field.grpMembrRecCde).is(product.get(Field.grpMembrRecCde))
                .and(Field.prodTypeCde).is(product.get(Field.prodTypeCde))
                .and(Field.prodStatCde).ne("D")
                .and(Field.prodId).ne(product.get(Field.prodId))
                .and(Field.altId).elemMatch(altIdCriteria)
                .and("altId.prodAltNum").is(prodAltNum);

        if (!Pattern.matches(prodkeyPrimary, prodCdeAltClassCde)) {
            criteria.and("ctryProdTradeCde.0").is(getCtryProdTradeCde1(product));
        }

        if (Pattern.matches(prodkeyAll, prodCdeAltClassCde)) {
            String ccyProdCde = (String) product.get(Field.ccyProdCde);
            if (StringUtils.isNotBlank(ccyProdCde)) {
                criteria.and(Field.ccyProdCde).is(ccyProdCde);
            }
        }

        Query query = new Query(criteria);
        query.fields().include(Field.prodAltPrimNum);
        return query;
    }

    private String getCtryProdTradeCde1(Map<String, Object> product) {
        List<String> ctryProdTradeCdes = (List<String>) product.get("ctryProdTradeCde");

        if (CollectionUtils.isEmpty(ctryProdTradeCdes)) {
            return null;
        }

        return ctryProdTradeCdes.get(0);
    }

    private Map<String, String> toAltIdMap(Map<String, Object> product) {
        Map<String, String> altIdMaps = new HashMap<>();
        List<Map<String, Object>> altIds = (List<Map<String, Object>>) MapUtils.getObject(product, Field.altId, Collections.emptyList());
        for (Map<String, Object> altId : altIds) {
            String prodCdeAltClassCde = (String) altId.get(Field.prodCdeAltClassCde);
            String prodAltNum = (String) altId.get(Field.prodAltNum);
            if (StringUtils.isNoneBlank(prodCdeAltClassCde, prodAltNum)) {
                altIdMaps.put(prodCdeAltClassCde, prodAltNum);
            }
        }
        return altIdMaps;
    }

}
