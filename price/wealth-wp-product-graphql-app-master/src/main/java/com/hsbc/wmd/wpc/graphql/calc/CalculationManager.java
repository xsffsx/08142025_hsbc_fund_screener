package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.service.MetadataService;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import com.dummy.wmd.wpc.graphql.utils.OperationInputUtils;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CalculationManager {
    @Autowired
    private ReferenceDataService referenceDataService;
    @Autowired
    private MongoTemplate mongoTemplate;
    private final List<Document> calculatedFields;

    public CalculationManager(MetadataService metadataService) {
        calculatedFields = metadataService.getMetadataList(Filters.exists("calculatedBy.className"));
    }

    public void updateCalculatedFields(Document prod) {
        this.updateCalculatedFields(null, prod, Collections.emptyList());
    }

    public void updateCalculatedFields(Document oldProduct, Document product, List<OperationInput> operations) {
        calculatedFields.forEach(field -> {
            String jsonPath = field.getString(Field.jsonPath);
            if (-1 != jsonPath.indexOf('.')) {
                //need more effort to support a.b
                throw new IllegalArgumentException("Supports only attributes under ROOT by far: " + jsonPath);
            }
            Document calculatedBy = (Document) field.get(Field.calculatedBy);
            String className = calculatedBy.getString(Field.className);
            FieldCalculator calculator = newCalculatorInstance(className);

            List<String> changePaths = OperationInputUtils.extractAllPaths(operations);
            jsonPath = jsonPath.replace("[*]", "");
            if (MapUtils.isEmpty(oldProduct)
                    || calculator.interestJsonPaths().isEmpty()
                    || CollectionUtils.containsAny(changePaths, calculator.interestJsonPaths())) {
                // force calculated fields
                calculator.setReferenceDataService(referenceDataService);
                calculator.setMongoTemplate(mongoTemplate);
                Object value = calculator.calculate(product);
                product.put(jsonPath, value);
            }else {
                product.put(jsonPath, oldProduct.get(jsonPath));
            }
        });
    }

    @SneakyThrows
    private FieldCalculator newCalculatorInstance(String className) {
        return (FieldCalculator) Class.forName(className).getDeclaredConstructor().newInstance();
    }
}
