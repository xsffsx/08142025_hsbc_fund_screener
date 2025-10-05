package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ActionCde;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import com.dummy.wmd.wpc.graphql.utils.RevisionUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.dummy.wmd.wpc.graphql.utils.CommonUtils.extractStringValue;
import static com.dummy.wmd.wpc.graphql.utils.DocumentUtils.extractDocList;


@Service
public class ChanlRelatedFieldsChangeService implements ChangeService {

    private final MongoCollection<Document> colProductData;

    public static final String INVALID_INPUT = "@invalid";
    private static final List<String> fieldList = Arrays.asList("ctryRecCde", "grpMembrRecCde", "prodName", "prodSubtpCde", "prodTypeCde", "prodAltPrimNum",
            "allowBuyProdInd", "allowSellProdInd", "allowSellMipProdInd", "allowSwInProdInd", "allowSwOutProdInd", "revision", Field.chanlAttr,
            "recCreatDtTm", "recUpdtDtTm");
    private static final List<String> fieldCdeList = Arrays.asList("ALLOW_BUY_PROD_IND", "ALLOW_SELL_PROD_IND", "ALLOW_SELL_MIP_PROD_IND",
            "ALLOW_SW_IN_PROD_IND", "ALLOW_SW_OUT_PROD_IND");
    private static final List<String> chanlIdList = Arrays.asList("CMB_I", "CMB_B");
    private static final List<String> indicators = Arrays.asList("Y", "N");

    public ChanlRelatedFieldsChangeService(MongoDatabase mongoDatabase) {
        this.colProductData = mongoDatabase.getCollection(CollectionName.product.toString());
    }

    @Override
    public List<Error> validate(Document amendment) {
        List<Error> errors = new ArrayList<>();
        Document doc = (Document) amendment.get(Field.doc);
        ActionCde actionCde = ActionCde.valueOf(extractStringValue(amendment, Field.actionCde));
        for (Map<String, Object> chanlAttrField : extractDocList(doc, Field.chanlAttr)) {
            String chanlId = extractStringValue(chanlAttrField, Field.chanlCde);
            String fieldCde = extractStringValue(chanlAttrField, Field.fieldCde);
            String charValue = extractStringValue(chanlAttrField, Field.fieldCharValueText);
            if (!chanlIdList.contains(chanlId)) {
                errors.add(new Error("$chanlAttr.chanlCde", INVALID_INPUT, "chanlCde in Channel Attribute Field is invalid."));
            } else if (!fieldCdeList.contains(fieldCde)) {
                errors.add(new Error("$chanlAttr.fieldCde", INVALID_INPUT, "fieldCode in Channel Attribute Field is invalid."));
            } else if (!(indicators.contains(charValue) || actionCde.equals(ActionCde.delete))) {
                errors.add(new Error("$chanlAttr.fieldCharValueText", INVALID_INPUT, "fieldCharValueText in Channel Attribute Field is invalid."));
            }
        }
        return errors;
    }

    @Override
    public void add(Document doc) {
        update(doc);
    }

    @Override
    public void update(Document doc) {
        /*
         * FE sends the fullset data to BE and then BE saves the fullset data (channel attribute items) into DB
         * As it is a fullset data, then no need to merge data as well.
         */
        Object id = doc.get(Field._id);
        Bson filter = Filters.eq(Field._id, id);
        Document existedDoc = findExistedDocument(filter);
        doUpdate(filter, existedDoc, extractDocList(doc, Field.chanlAttr));
    }

    @Override
    public void delete(Document doc) {
        Object id = doc.get(Field._id);
        Bson filter = Filters.eq(Field._id, id);
        Document dbDoc = findExistedDocument(filter);

        List<Map<String, Object>> deletedDataList = extractDocList(doc, Field.chanlAttr);
        List<Map<String, Object>> existedDataList = extractDocList(dbDoc, Field.chanlAttr);
        List<String> deletedKeyList = deletedDataList.stream().map(this::buildItemKey).collect(Collectors.toList());
        existedDataList.removeIf(it -> deletedKeyList.contains(buildItemKey(it)));
        doUpdate(filter, dbDoc, existedDataList);
    }

    private Document findExistedDocument(Bson filter) {
        Document dbDoc = colProductData.find(filter).first();
        if (Objects.isNull(dbDoc)) {
            throw new productErrorException(productErrors.RuntimeException, "Product record not found, id=" + filter);
        }
        return dbDoc;
    }

    private void doUpdate(Bson filter, Document document, List<Map<String, Object>> chanlAttrList) {
        RevisionUtils.increaseRevisionNumber(document);
        Bson updateOpt = Updates.combine(
                chanlAttrList.isEmpty() ? Updates.unset(Field.chanlAttr) : Updates.set(Field.chanlAttr, chanlAttrList),
                Updates.set(Field.revision, DocumentUtils.getLong(document, Field.revision)),
                Updates.set(Field.recUpdtDtTm, new Date())
        );
        colProductData.updateOne(filter, updateOpt);
    }

    @Override
    public Document findFirst(Bson filter) {
        return findFirst(filter, new Document());
    }

    @Override
    public Document findFirst(Bson filter, Document docChange) {
        Document fullsetDocument = colProductData.find(filter).projection(Projections.include(fieldList)).first();
        if (Objects.isNull(fullsetDocument)) {
            throw new productErrorException(productErrors.RuntimeException, "Product record not found, filter =" + filter);
        }
        // generate rowid for absent ones -- compatible with old data
        Map<String, String> itemKeyToRowIdMap =
                getChannelFields(fullsetDocument).stream()
                                                 .collect(Collectors.toMap(this::buildItemKey, this::getRowId));
        for (Map<String, Object> changedItem : getChannelFields(docChange)) {
            String rowId = itemKeyToRowIdMap.computeIfAbsent(
                    buildItemKey(changedItem),
                    key -> UUID.randomUUID().toString()
            );
            changedItem.putIfAbsent(Field.rowid, rowId);
        }
        return fullsetDocument;
    }

    private List<Map<String, Object>> getChannelFields(Document document) {
        return DocumentUtils.extractDocList(document, Field.chanlAttr);
    }

    private String buildItemKey(Map<String, Object> item) {
        return extractStringValue(item, Field.chanlCde) + extractStringValue(item, Field.fieldCde);
    }

    private String getRowId(Map<String, Object> item) {
        return item.containsKey(Field.rowid) ? item.get(Field.rowid).toString() : UUID.randomUUID().toString();
    }
}
