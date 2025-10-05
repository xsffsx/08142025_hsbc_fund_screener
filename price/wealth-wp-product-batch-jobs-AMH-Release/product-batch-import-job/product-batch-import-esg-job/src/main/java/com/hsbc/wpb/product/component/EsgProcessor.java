package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.ProductStatus;
import com.dummy.wpb.product.model.EsgDataItem;
import com.dummy.wpb.product.model.EsgDocument;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dummy.wpb.product.constant.BatchConstants.ESG_SI_CLASS_CDE_REF_TYPE_CDE;
import static com.dummy.wpb.product.constant.ConfigConstants.PRODUCT_CODE_ALTERNATED_CLASS_CODE_ISIN;
import static com.dummy.wpb.product.constant.EsgConstants.*;

@Slf4j
@Component
@StepScope
public class EsgProcessor implements InitializingBean, ItemProcessor<EsgDataItem, List<Document>> {

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    @Value("#{jobParameters['prodTypeCde']}")
    private String prodTypeCde;

    private MongoTemplate mongoTemplate;

    private ReferenceDataService referenceDataService;


    @Value("#{jobExecutionContext['prodNotFound']}")
    private AtomicInteger prodNotFoundNum;

    @Value("#{jobExecutionContext['validateFail']}")
    private AtomicInteger validateFailNum;

    private static final List<String> siClassificationList = new ArrayList<>();

    public EsgProcessor(MongoTemplate mongoTemplate, ReferenceDataService referenceDataService) {
        this.mongoTemplate = mongoTemplate;
        this.referenceDataService = referenceDataService;

    }

    @Override
    @SneakyThrows
    public void afterPropertiesSet() {
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.cdvTypeCde).is(ESG_SI_CLASS_CDE_REF_TYPE_CDE);
        List<ReferenceData> referenceDataList = referenceDataService.referenceDataByFilter(criteria.getCriteriaObject());
        referenceDataList.forEach(referenceData -> siClassificationList.add(referenceData.getCdvCde()));
    }

    @Override
    public List<Document> process(EsgDataItem esg) {
        if (!validate(esg)) {
            validateFailNum.incrementAndGet();
            return null;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde));
        query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
        query.addCriteria(Criteria.where(Field.prodStatCde).nin(ProductStatus.D.name()));
        query.addCriteria(Criteria.where("altId.prodAltNum").is(esg.getIsin()));
        query.addCriteria(Criteria.where("altId.prodCdeAltClassCde").is(PRODUCT_CODE_ALTERNATED_CLASS_CODE_ISIN));
        if (!StringUtils.equals("ALL", prodTypeCde) && StringUtils.isNotBlank(prodTypeCde)) {
            List<String> prodTypeCdeList = Arrays.asList(prodTypeCde.split(","));
            query.addCriteria(Criteria.where(Field.prodTypeCde).in(prodTypeCdeList));
        }
        List<Document> documentList = mongoTemplate.find(query, Document.class, CollectionName.product.name());
        if (CollectionUtils.isEmpty(documentList)) {
            prodNotFoundNum.incrementAndGet();
            log.error("ISIN : {}, product not found, will skip!", esg.getIsin());
            return null;
        } else {
            documentList.forEach(doc -> handleEsgData(doc, esg));
        }
        return documentList;
    }

    @SneakyThrows
    public void handleEsgData(Document doc, EsgDataItem esg) {
        EsgDocument esgDocument = JsonUtil.convertType(esg, EsgDocument.class);
        Map<String, Object> esgMap = Objects.isNull(doc.get(ESG_NODE)) ? new HashMap<>() : (Map) doc.get(ESG_NODE);
        Arrays.stream(fieldList).forEach(fieldName -> {
            try {
                Object fieldValue = PropertyUtils.getProperty(esgDocument, fieldName);
                if (fieldValue != null && StringUtils.isNotBlank(fieldValue.toString())) {
                    esgMap.put(fieldName, fieldValue);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("ISIN code: {} get property error!", esg.getIsin());
            }
        });
        //only siClass is THEMATIC need to add sdg theme fields
        if (!"THEMATIC".equals(esgMap.get("siClass"))) {
            esgMap.keySet().removeIf(sdgThemeFieldList::contains);
        }
        doc.put(ESG_NODE, esgMap);
    }

    private boolean validate(EsgDataItem esg) {
        String isin = esg.getIsin();
        boolean valSiClassifi = validateSiClassfications(isin, esg.getSiClass());
        boolean valEsgScore = validateIsNumber(isin, esg.getEsgScore(), "esgScore");
        boolean valCarbonIntensity = validateIsNumber(isin, esg.getCarbonIntens(), "carbonIntensity");
        final String[] sdgThemeRanks = new String[] {
                esg.getSdgThmRkBscNeeds(),
                esg.getSdgThmRkClimChg(),
                esg.getSdgThmRkEmp(),
                esg.getSdgThmRkGov(),
                esg.getSdgThmRkNatuCapt()
        };
        boolean valSdgThemeRank = validateSdgThemeRank(isin, sdgThemeRanks);
        return valSiClassifi && valEsgScore && valCarbonIntensity && valSdgThemeRank;
    }

    private boolean validateSiClassfications(String isinCode, String siClassification) {
        if(StringUtils.isBlank(siClassification)) {
            return true;
        }
        boolean result = siClassificationList.contains(siClassification);
        if (!result) {
            log.error("ISIN code: {}, siClassification: {} is not exist", isinCode, siClassification);
        }
        return result;
    }

    private boolean validateIsNumber(String isinCode, String columnVal, String columnName) {
        try {
            if (StringUtils.isNotBlank(columnVal) && !columnVal.equals("NULL")) {
                new BigDecimal(columnVal);
            }
            return true;
        } catch (NumberFormatException e) {
            log.error("ISIN code: {} columnName: {}({}) is not a BigDecimal", isinCode, columnName, columnVal);
            return false;
        }
    }

    private boolean validateSdgThemeRank(String isinCode, String[] sdgThemeRank) {
        boolean result = true;
        for (String sdgTR : sdgThemeRank) {
            try {
                if (StringUtils.isNotBlank(sdgTR) && !sdgTR.equals("NULL")) {
                    new BigDecimal(sdgTR);
                }
            } catch (NumberFormatException e) {
                log.error("ISIN code: {}, sdgThemeRank: {} is not a BigDecimal", isinCode, sdgTR);
                result = false;
                break;
            }
        }
        return result;
    }

}
