package com.dummy.wpb.product.jobs.registration;

import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.jobs.registration.ProductRegistrationApplication.JOB_NAME;


@Slf4j
@Service
public class ProductRegistrationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProductRegistrationUrlHandler urlHandler;

    public void registerProduct(List<Document> prods) {
        if (CollectionUtils.isEmpty(prods)) {
            log.warn("Empty product list");
            return;
        }
        //1, build requestBody
        List<Map<String, Object>> requestBody = buildRequestBody(prods);
        //2, get smart token and call Smart register Product API to get respond
        Map<String, Object> responseBody = urlHandler.getGlobalId(requestBody);
        //3, process respond data and encapsulation in prods
        updateProducts(prods, responseBody);
    }

    private List<Map<String, Object>> buildRequestBody(List<Document> prods) {
        List<Map<String, Object>> requestBody = new ArrayList<>();
        for (Document prod : prods) {
            List<Map<String, Object>> altIds = new ArrayList<>();
            //send all of the code, except "Q" code
            prod.getList("altId", Document.class).stream().filter(altIdMap -> !"Q".equals(altIdMap.getString(Field.prodCdeAltClassCde)))
                    .forEach(altIdMap -> {
                        String code = altClassCodeMap.get(altIdMap.getString(Field.prodCdeAltClassCde));
                        if (StringUtils.isNotBlank(code)) {
                            altIds.add(ImmutableMap.of(Field.prodCdeAltClassCde, code, Field.prodAltNum, altIdMap.get(Field.prodAltNum)));
                        }
                    });
            LinkedHashMap<String, Object> param = new LinkedHashMap<>();
            param.put(Field.altId, altIds);
            param.put(Field.ctryRecCde, prod.get(Field.ctryRecCde));
            param.put(Field.grpMembrRecCde, prod.get(Field.grpMembrRecCde));
            param.put(Field.prodId, prod.get("_id"));
            param.put(Field.prodName, prod.get(Field.prodName));
            param.put(Field.prodSubtpCde, prod.get(Field.prodSubtpCde));
            param.put(Field.prodTypeCde, prod.get(Field.prodTypeCde));
            param.put(Field.ccyProdCde, prod.get(Field.ccyProdCde));
            requestBody.add(param);
        }
        return requestBody;
    }

    public void updateProducts(List<Document> prods, Map<String, Object> responseBody) {
        if(CollectionUtils.isEmpty(responseBody)) {
            return;
        }
        List<Map<String, Object>> globalIds = (List) responseBody.get("data");
        if(CollectionUtils.isEmpty(globalIds)) {
            return;
        }
        Map<Long, Document> prodId2DocMap = new LinkedHashMap<>();
        prods.forEach(prod -> prodId2DocMap.put(prod.getLong(Field.prodId), prod));
        globalIds.forEach(globalId -> {
            if (StringUtils.isEmpty((String)globalId.get("globalId"))) {
                log.warn("prodId:[{}], errorMsg:[{}]", globalId.get("prodId"), globalId.get("errorMsg"));
            } else {
                Long prodId = Long.parseLong((String) globalId.get("prodId"));
                Document prod = prodId2DocMap.get(prodId);
                updateProductQCode(prod, globalId);
            }
        });
    }

    private void updateProductQCode(Document prod, Map<String, Object> globalId) {
        String smartId = (String) globalId.get("globalId");
        List<Document> altIds = prod.getList(Field.altId, Document.class);
        // if old Q code exist, remove it first, and then add a new one.
        List<Document> newAltIds = altIds.stream().filter(item -> !"Q".equals(item.getString(Field.prodCdeAltClassCde))).collect(Collectors.toList());
        newAltIds.add(newQCode(prod.getString(Field.prodTypeCde), smartId));

        // update product with Q code
        Update update = new Update();
        update.set(Field.altId, newAltIds);
        update.set(Field.recUpdtDtTm, new Date());
        update.set(Field.lastUpdatedBy, JOB_NAME);

        Query query = new Query();
        query.addCriteria(Criteria.where(Field.prodId).is(prod.getLong(Field.prodId)));

        UpdateResult result = mongoTemplate.updateFirst(query, update, String.valueOf(CollectionName.product));
        if(result.getModifiedCount() == 0) {
            log.warn("Update failed");
        }
    }

    private Document newQCode(String prodTypeCde, String smartId) {
        Date dateTime = new Date();
        Document doc = new Document();
        doc.put("rowid", UUID.randomUUID().toString());
        doc.put("recCreatDtTm", dateTime);
        doc.put("prodCdeAltClassCde", "Q");
        doc.put("prodTypeCde", prodTypeCde);
        doc.put(Field.prodAltNum, smartId);
        doc.put("recUpdtDtTm", dateTime);

        return doc;
    }

    private static Map<String, String> altClassCodeMap;

    static {
        altClassCodeMap = new ImmutableMap.Builder<String, String>()
                .put("M", "MARKET")
                .put("I", "ISIN")
                .put("O", "MORNING_STAR_PERFORMANCE_ID")
                .put("P", "PRIMARY")
                .put("R", "RIC")
                .put("S", "SEDOL")
                .put("G", "INTERNAL_FUND_ID")
                .put("K", "TICKER")
                .put("U", "MORNING_STAR_SECURITY_ID")
                .put("T", "TR_COMMUNICATION_CODE")
                .put("Y", "BLOOMBERG")
                .put("B", "BACK_OFFICE")
                .put("C", "RIC_CODE_2")
                .put("E", "EUROCLEAR_COMMON_CODE")
                .put("F", "FUND_EXTERNAL_NUMBER")
                .build();
    }
}
