package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.constant.CollectionName;
import lombok.Setter;
import org.bson.Document;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;

@Setter
public abstract class AbstractMongoReader extends AbstractPaginatedDataItemReader<Document> {
    protected MongoTemplate mongoTemplate;
    protected CollectionName collection;
    protected Document filter = new Document();
    protected Document sort = new Document();

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        super.open(executionContext);
        executionContext.put("count", mongoTemplate.count(new BasicQuery(filter), collection.name()));
    }
}
