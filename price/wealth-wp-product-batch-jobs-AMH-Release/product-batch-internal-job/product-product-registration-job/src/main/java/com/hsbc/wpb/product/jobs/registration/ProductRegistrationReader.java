package com.dummy.wpb.product.jobs.registration;

import com.dummy.wpb.product.constant.Field;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import java.util.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@StepScope
@Component
@Slf4j
public class ProductRegistrationReader extends MongoItemReader<Document> {

    private MongoOperations mongoTemplate;

    private String regisColl;

    @Value("${page.prod.id:0}")
    private Long pageProdId;

    @Value("#{jobExecutionContext['latestRegisterDtTm']}")
    private String latestRegisterDtTm;


    public ProductRegistrationReader(MongoOperations mongoTemplate) {
        this.setCollection("product");
        this.setName("productItemReader");
        this.setTargetType(Document.class);
        this.setTemplate(mongoTemplate);
        this.setQuery(new Query());

        this.mongoTemplate = mongoTemplate;
        this.regisColl = "product";
    }

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    @Value("#{jobParameters['prodTypeCde']}")
    private String prodTypeCde;

    @Value("#{jobParameters['groupSize']}")
    private String groupSize;

    @Value("#{jobParameters['isDaltaSync']}")
    private String isDaltaSync;

    @Value("#{jobParameters['supportAltIdCdes']}")
    private String supportAltIdCdes;

    //override doPageRead() to avoid skip duplicate counts error
    @Override
    protected Iterator<Document> doPageRead() {
        Query regisQuery = getQuery();

        List<Document> docs = mongoTemplate.find(regisQuery, Document.class, regisColl);
        if (CollectionUtils.isEmpty(docs)) {
            return null;
        }
        pageProdId =docs.get(docs.size() -1).getLong(Field.prodId);
        return  docs.iterator();
    }

    private Query getQuery() {
        Query query = new Query();
        query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde));
        query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
        query.addCriteria(Criteria.where(Field.prodStatCde).ne("D"));
        query.addCriteria(Criteria.where(Field.prodId).gt(pageProdId));

        if (!StringUtils.equals("ALL", prodTypeCde) && StringUtils.isNotBlank(prodTypeCde)) {
            List<String> prodTypeCdeList = Arrays.asList(prodTypeCde.split(","));
            query.addCriteria(Criteria.where(Field.prodTypeCde).in(prodTypeCdeList));
        }

        if (StringUtils.isNotBlank(supportAltIdCdes)) {
            List<String> supportAltIdCdesList = Arrays.asList(supportAltIdCdes.split(","));
            Criteria altIdCriteria = Criteria.where(Field.prodCdeAltClassCde).in(supportAltIdCdesList);

            if (Boolean.parseBoolean(isDaltaSync)) {
                altIdCriteria.and(Field.recUpdtDtTm).gte(LocalDateTime.ofInstant(Instant.parse(latestRegisterDtTm), ZoneId.systemDefault()));
            }
            query.addCriteria(Criteria.where("altId").elemMatch(altIdCriteria));
        }

        query.fields().include("altId", "ctryRecCde", "grpMembrRecCde", "prodId", "prodName", "prodSubtpCde", "prodTypeCde", "ccyProdCde");
        query.with(Sort.by(Sort.Order.asc(Field.prodId)));
        query.limit(Integer.parseInt(groupSize));
        return query;
    }
}
