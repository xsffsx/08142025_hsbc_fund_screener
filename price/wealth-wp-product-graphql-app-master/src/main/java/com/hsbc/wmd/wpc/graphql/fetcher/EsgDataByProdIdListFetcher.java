package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.EsgDataItem;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Retrieve report list
 */
@Slf4j
@Component
public class EsgDataByProdIdListFetcher implements DataFetcher<List<EsgDataItem>> {
    private final MongoTemplate mongoTemplate;

    public EsgDataByProdIdListFetcher(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<EsgDataItem> get(DataFetchingEnvironment environment) {
        List<Long> prodIdList = environment.getArgument(Field.prodIdList);

        Query q = new Query(Criteria.where(Field.prodId).in(prodIdList));
        q.with(Sort.by(Sort.Direction.ASC, Field.recCreatDtTm));
        List<EsgDataItem> list = mongoTemplate.find(q, EsgDataItem.class);

        // remove possible duplicate entries, which will happens at esg data loading time
        Map<Long, EsgDataItem> map = new LinkedHashMap<>();
        list.forEach(item -> map.put(item.getProdId(), item));
        return new ArrayList<>(map.values());
    }
}