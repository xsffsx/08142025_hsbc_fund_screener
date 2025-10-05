package com.dummy.wpb.product.service;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import org.bson.Document;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Iterator;
import java.util.List;


/**
 *
 * This class is used to query large amounts of product data.
 * <br/>
 * Compare with the paging way using skip and limit, this class is using product as paging conditions, which can improve a lot of performance.
 *
 * */
public class ProductItemReader extends AbstractPaginatedDataItemReader<Document> {

    @Autowired
    MongoTemplate mongoTemplate;

    private final Query baseQuery;

    private long latestProductId = 0L;

    public ProductItemReader(Query baseQuery) {
        this.setName("productItemReader");
        this.baseQuery = baseQuery;
    }

    @Override
    protected Iterator<Document> doPageRead() {
        Query query = Query.of(baseQuery);
        query.addCriteria(Criteria.where(Field._id).gt(latestProductId));
        query.with(Sort.by(Sort.Order.asc(Field._id)));
        List<Document> prodList = mongoTemplate.find(query, Document.class, CollectionName.product.name());
        if (!prodList.isEmpty()) {
            latestProductId = prodList.get(prodList.size() - 1).getLong(Field._id);
        }
        return prodList.iterator();
    }
}
