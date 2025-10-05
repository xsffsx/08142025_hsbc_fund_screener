package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.utils.CommonUtils;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.SanctionUpdateJobConfiguration.JOB_NAME;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregationOptions;

@Component
@Slf4j
public class SanctionUpdateService {

    private final MongoTemplate mongoTemplate;

    public SanctionUpdateService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Document> aggregateSanctionList(String ctryRecCde, String grpMembrRecCde, String prodTypeCde) {
        // replace placeholder with entity codes
        String[] searchList = {"%ctryRecCde%", "%grpMembrRecCde%"};
        String[] replacementList = {ctryRecCde, grpMembrRecCde};
        String pipelineJson = StringUtils.replaceEach(
                CommonUtils.readResource("/aggregate-" + prodTypeCde + ".json"),
                searchList,
                replacementList
        );
        List<AggregationOperation> aggregationOperationList = BsonArray.parse(pipelineJson)
                                                                       .stream()
                                                                       .map(BsonValue::asDocument)
                                                                       .map(BsonDocument::toJson)
                                                                       .map(Document::parse)
                                                                       .map(stageDoc -> (AggregationOperation) context -> stageDoc)
                                                                       .collect(Collectors.toList());
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperationList)
                                             .withOptions(newAggregationOptions().allowDiskUse(true).build());
        return mongoTemplate.aggregate(aggregation, CollectionName.product.name(), Document.class).getMappedResults();
    }

    public UpdateResult mongoUpdate(Long prodId, List<String> sanctionBuyList, List<String> sanctionSellList) {
        // query prodId
        Criteria criteria = Criteria.where(Field.prodId).is(prodId);
        // update sanctionBuyList & sanctionSellList
        Update update = new Update();
        updateSanctionList(update, Field.sanctionBuyList, sanctionBuyList);
        updateSanctionList(update, Field.sanctionSellList, sanctionSellList);
        update.set(Field.recUpdtDtTm, new Date());
        update.set(Field.lastUpdatedBy, JOB_NAME);

        return mongoTemplate.updateFirst(new Query(criteria), update, CollectionName.product.name());
    }

    private void updateSanctionList(Update update, String fieldName, List<String> sanctionList) {
        if (CollectionUtils.isEmpty(sanctionList)) {
            update.unset(fieldName);
        } else {
            update.set(fieldName, sanctionList);
        }
    }
}
