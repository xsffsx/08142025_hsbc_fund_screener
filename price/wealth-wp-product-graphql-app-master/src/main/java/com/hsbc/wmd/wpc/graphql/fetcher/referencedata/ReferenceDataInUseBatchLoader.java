package com.dummy.wmd.wpc.graphql.fetcher.referencedata;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Service
public class ReferenceDataInUseBatchLoader implements BatchLoader<DataFetchingEnvironment, Boolean> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Cacheable(value = "referenceDataInUseCache", key = "#root.target.combineKey(#envs[0].executionStepInfo.parent.arguments , #envs[0].variables, #envs.size())")
    public CompletionStage<List<Boolean>> load(List<DataFetchingEnvironment> envs) {
        String path = envs.get(0).getArgument("path");
        Document productFilter = new Document(envs.get(0).getArgument("productFilter"));

        List<Document> referenceDatas = new ArrayList<>();
        List<String> cdvCdes = new ArrayList<>();
        Map<String, Document> entityAndFilters = new HashMap<>();
        for (DataFetchingEnvironment env : envs) {
            Document referenceData = env.getSource();
            referenceDatas.add(referenceData);
            cdvCdes.add(referenceData.getString(Field.cdvCde));
            String ctryRecCde = referenceData.getString(Field.ctryRecCde);
            String grpMembrRecCde = referenceData.getString(Field.grpMembrRecCde);
            entityAndFilters.putIfAbsent(ctryRecCde + grpMembrRecCde, new Document(Field.ctryRecCde, ctryRecCde).append(Field.grpMembrRecCde, grpMembrRecCde));
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(context -> productFilter.append(path, new Document("$in", cdvCdes)).append("$or", entityAndFilters.values())),
                Aggregation.project(Field.ctryRecCde, Field.grpMembrRecCde, path),
                Aggregation.unwind(path),
                Aggregation.match(Criteria.where(path).in(cdvCdes)),
                Aggregation.group(Field.ctryRecCde, Field.grpMembrRecCde).addToSet(path).as(path)
        );

        List<Document> mappedResults = mongoTemplate.aggregate(aggregation, CollectionName.product.name(), Document.class).getMappedResults();
        Map<String, List<Object>> entityAndInUseValues = mappedResults.stream()
                .collect(Collectors.toMap(
                        res -> res.getEmbedded(Arrays.asList(Field._id, Field.ctryRecCde), String.class) + res.getEmbedded(Arrays.asList(Field._id, Field.grpMembrRecCde), String.class),
                        res -> (List<Object>) res.get(path, new ArrayList<>())
                ));

        List<Boolean> result = new ArrayList<>();
        for (Document referenceData : referenceDatas) {
            String entity = referenceData.getString(Field.ctryRecCde) + referenceData.getString(Field.grpMembrRecCde);
            result.add(entityAndInUseValues.getOrDefault(entity, Collections.emptyList()).contains(referenceData.getString(Field.cdvCde)));
        }

        return CompletableFuture.completedFuture(result);
    }

    public Map<String, Object> combineKey(Map<String, Object> refFilter, Map<String, Object> inUseFilter, int size) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.putAll(refFilter);
        result.putAll(inUseFilter);
        result.put("refSize", size);
        return result;
    }
}
