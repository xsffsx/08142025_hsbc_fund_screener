package com.dummy.wpb.product.jobs.registration;

import com.dummy.wpb.product.constant.CollectionName;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import java.util.List;

@StepScope
@Component
public class ProductRegistrationWriter extends MongoItemWriter<Document> {

    private final ProductRegistrationService service;

    public ProductRegistrationWriter(ProductRegistrationService service, MongoOperations mongoTemplate) {
        this.service = service;
        this.setCollection(CollectionName.product.name());
        this.setTemplate(mongoTemplate);
    }

    @Override
    public void write(List<? extends Document> list) {
        service.registerProduct((List<Document>) list);
    }
}
