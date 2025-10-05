package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.constant.Field;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Iterator;
import java.util.List;

@Slf4j
public class SingleCollectionReader extends AbstractMongoReader {

    protected final Document latestIdSort = new Document(Field._id, 1);

    protected Object latestId;

    protected BasicQuery query;

    @Override
    protected void doOpen() {
        this.query = new BasicQuery(filter);
        if (null == sort || sort.isEmpty()) {
            sort = latestIdSort;
        }
        query.setSortObject(sort);
        query.limit(pageSize);
        query.cursorBatchSize(pageSize);
    }

    @Override
    protected Iterator<Document> doPageRead() {
        if (!latestIdSort.equals(sort)) {
            query.with(PageRequest.of(page, pageSize));
            return mongoTemplate.find(query, Document.class, collection.name()).iterator();
        }

        if (null != latestId) {
            query.addCriteria(Criteria.where(Field._id).gt(latestId));
        }

        List<Document> datas = mongoTemplate.find(query, Document.class, collection.name());
        if (!datas.isEmpty()) {
            latestId = datas.get(datas.size() - 1).getLong(Field._id);
        }
        return datas.iterator();
    }

    public void setSort(Document sort) {
        if (null != sort && !sort.isEmpty()) {
            this.sort = sort;
        }
    }
}
