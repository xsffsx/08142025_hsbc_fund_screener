package com.dummy.wmd.wpc.graphql.service;

import com.google.common.collect.ImmutableMap;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.model.CorrelationWithRate;
import com.dummy.wmd.wpc.graphql.validator.Error;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("java:S3740")
@Service
public class AssetVolatilityClassService {
    @Autowired private AssetVolatilityClassCorlChangeService corlChangeService;
    @Autowired private AssetVolatilityClassCharChangeService charChangeService;

    public AssetVolatilityClassService() {
        //do nothing
    }

    /**
     * Treat the same set of asetVoltlClassCde+asetVoltlClassRelCde+asetVoltlClassCorlRate as the same item, no matter order
     *
     * @param mapList
     * @return
     */
    private List<Map> dedupe(List<Map> mapList) {
        Set<CorrelationWithRate> set = new LinkedHashSet<>();
        return mapList.stream().filter(map -> {
            CorrelationWithRate item = new CorrelationWithRate((String) map.get(Field.asetVoltlClassCde), (String) map.get(Field.asetVoltlClassRelCde), (Double) map.get(Field.asetVoltlClassCorlRate));
            if(!set.contains(item)) {
                set.add(item);
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
    }

    public Document saveAssetVolatilityClassChar(Document doc) {
        validateDocument(charChangeService, doc);
        doc.put(Field.lastUpdatedBy, "batch");

        if(null == doc.get(Field._id)){
            return charChangeService.addDocument(doc);
        } else {
            return charChangeService.updateDocument(doc);
        }
    }

    public Document saveAssetVolatilityClassCorl(Document doc) {
        List<Map> mapList = doc.getList(Field.list, Map.class);
        if(CollectionUtils.isNotEmpty(mapList)) {
            // since the original xml file to be imported has duplicated items for 2 char codes
            List<Map> dedupeList = dedupe(mapList);
            doc.put(Field.list, dedupeList);
        }

        validateDocument(corlChangeService, doc);
        doc.put(Field.lastUpdatedBy, "batch");

        if(null == doc.get(Field._id)){
            return corlChangeService.addDocument(doc);
        } else {
            return corlChangeService.updateDocument(doc);
        }
    }

    private void validateDocument(AssetVolatilityClassBaseChangeService service, Document doc) {
        List<Error> errors = service.validateDocument(doc);
        if(!CollectionUtils.isEmpty(errors)) {
            Map<String, Object> extensions = new ImmutableMap.Builder<String, Object>()
                    .put(productErrorException.product_ERROR_CODE, productErrors.ValidationError)
                    .put("validationErrors", errors)
                    .build();
            throw new productErrorException(extensions, "Document validation error");
        }
    }
}
