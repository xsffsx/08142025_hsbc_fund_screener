package com.dummy.wpb.product.thymeleaf.expression;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class RefDatas {

    @Value("#{exportRequest.ctryRecCde}")
    private String ctryRecCde;

    @Value("#{exportRequest.grpMembrRecCde}")
    private String grpMembrRecCde;

    private static final List<String> cdvTypeCdes = Arrays.asList("PRODTYP", "PRODSUBTP", "AC", "UA", "PRDSTUS", "CCY", "RISKLVL", "TEN",
            "IVSTMKT", "ISECT", "GOALPRTY", "SPOMSPRODTYP", "SIDCONVIND", "OFFERTYP", "QUALRULTYP", "BONUSRTTYP", "BONUSDTTYP",
            "FH", "BONDISSUER", "CRMOODYS", "CRSP", "DPSTYPE");

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Map<String, Map<String, Document>> referenceMap = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        Query query = new Query()
                .addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde))
                .addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde))
                .addCriteria(Criteria.where(Field.cdvTypeCde).in(cdvTypeCdes));
        mongoTemplate.find(query, Document.class, CollectionName.reference_data.name())
                .forEach(reference -> referenceMap
                        .computeIfAbsent(reference.getString(Field.cdvTypeCde), k -> new HashMap<>())
                        .put(reference.getString(Field.cdvCde), reference));
    }

    public String desc(String cdvTypeCde, String cdvCde) {
        return referenceMap
                .getOrDefault(cdvTypeCde, Collections.emptyMap())
                .getOrDefault(cdvCde, new Document())
                .getString(Field.cdvDesc);
    }

    public String pllDesc(String cdvTypeCde, String cdvCde) {
        return referenceMap
                .getOrDefault(cdvTypeCde, Collections.emptyMap())
                .getOrDefault(cdvCde, new Document())
                .getString(Field.cdvPllDesc);
    }
    public String sllDesc(String cdvTypeCde, String cdvCde) {
        return referenceMap
                .getOrDefault(cdvTypeCde, Collections.emptyMap())
                .getOrDefault(cdvCde, new Document())
                .getString(Field.cdvSllDesc);
    }
    public Double cdvDispSeqNum(String cdvTypeCde, String cdvCde) {
        return referenceMap
                .getOrDefault(cdvTypeCde, Collections.emptyMap())
                .getOrDefault(cdvCde, new Document())
                .getDouble(Field.cdvDispSeqNum);
    }
}