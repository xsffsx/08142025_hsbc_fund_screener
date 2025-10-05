package com.dummy.wmd.wpc.graphql.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wmd.wpc.graphql.config.DefaultFieldConfig;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.model.CustEligConfig;
import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import com.dummy.wmd.wpc.graphql.utils.ObjectMapperUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class DefaultCustomerEligibilityCreateService extends DefaultFieldConfigCreateService {
    private DefaultFieldConfig defaultConfig;

    @Override
    protected Map<String, Object> buildAmendmentDocument(Map<String, Object> filter, DocType docType) {
        Document docChanged = new Document();
        fillProductInfo(filter, docChanged);
        return docChanged;
    }

    private void fillProductInfo(Map<String, Object> filter, Document docChanged) {
        MongoCollection<Document> prodCollection = mongoDatabase.getCollection(CollectionName.product.name());
        BsonDocument revisedFilterBson = new FilterUtils(CollectionName.product).toBsonDocument(filter);
        Bson projections = Projections.include(Field.ctryRecCde, Field.grpMembrRecCde, Field.prodName, Field.prodTypeCde,
                Field.prodSubtpCde, Field.prodAltPrimNum, Field.revision);
        List<Document> productList = prodCollection.find(revisedFilterBson)
                                                   .projection(projections)
                                                   .into(new ArrayList<>());
        if (productList.size() != 1) {
            throw new productErrorException(productErrors.DocumentNotFound, "Can not find document by filter or duplicated document searched");
        }
        Document product = productList.get(0);

        docChanged.put(Field._id, product.get(Field._id));
        docChanged.put(Field.ctryRecCde, product.get(Field.ctryRecCde));
        docChanged.put(Field.grpMembrRecCde, product.get(Field.grpMembrRecCde));
        docChanged.put(Field.prodName, product.get(Field.prodName));
        docChanged.put(Field.prodTypeCde, product.get(Field.prodTypeCde));
        docChanged.put(Field.prodSubtpCde, product.get(Field.prodSubtpCde));
        docChanged.put(Field.prodAltPrimNum, product.get(Field.prodAltPrimNum));
        docChanged.put(Field.revision, product.get(Field.revision));
        CustEligConfig config = defaultConfig.custElig(product.getString(Field.prodTypeCde));
        if(Objects.nonNull(config)) {
            fillRowId(config);
            docChanged.putAll(ObjectMapperUtils.convertValue(config, new TypeReference<Document>() {}));
        }
    }

    private void fillRowId(CustEligConfig config) {
        config.getRestrCustCtry().forEach(it -> it.put(Field.rowid, UUID.randomUUID().toString()));
        config.getRestrCustGroup().forEach(it -> it.put(Field.rowid, UUID.randomUUID().toString()));
        config.getFormReqmt().forEach(it -> it.put(Field.rowid, UUID.randomUUID().toString()));
    }
}
