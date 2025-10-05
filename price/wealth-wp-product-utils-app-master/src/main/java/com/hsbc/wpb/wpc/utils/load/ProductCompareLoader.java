package com.dummy.wpb.wpc.utils.load;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.wpc.utils.ProductCompareUtils;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.model.Difference;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

@Component
public class ProductCompareLoader extends ProductDataHandler {
    private static final String LEFT = "oracle";
    private static final String RIGHT = "mongo";
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    public ProductCompareLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        super(namedParameterJdbcTemplate, mongodb);
    }


    @Override
    protected boolean showTableLoading() {
        return false;
    }

    @Override
    protected void saveProducts(Map<Long, Map<String, Object>> prodMap) {
        //this method is empty
    }

    /**
     * Get 100 prodId list, sort by lastSyncTime DESC, skip startIndex
     *
     * @param startIndex
     * @return
     * @throws JsonProcessingException
     */
    public List<Long> retrieveProdIds(Integer startIndex) throws JsonProcessingException {
        Map<String, Object> sortMap = new LinkedHashMap<>();
        sortMap.put(Field.lastSyncTime, -1);
        BsonDocument sortBson = BsonDocument.parse(objectMapper.writeValueAsString(sortMap));
        List<Document> list = colProduct
                .find()
                .sort(sortBson)
                .skip(startIndex)
                .limit(100)
                .projection(Projections.include(Field.prodId))
                .into(new LinkedList<>());
        List<Long> prodIds = new ArrayList<>();
        list.forEach(doc -> {
            Long prodId = (Long) doc.get(Field.prodId);
            prodIds.add(prodId);
        });
        return prodIds;
    }

    public Map<String, Object> getProductDocument(Long prodId) {
        Map<Long, Map<String, Object>> prodDataMap = getProdData(Collections.singletonList(prodId));
        return prodDataMap.get(prodId);
    }

    public Map<Long, List<Map<String, Object>>> compare(List<Long> prodIdList) {
        Map<Long, List<Map<String, Object>>> compareResults = new HashMap<>();
        Map<Long, Map<String, Object>> productOracleDatas = getProdData(prodIdList);

        productOracleDatas.forEach((prodId, productOracleData) -> {
            Bson filter = eq(Field._id, prodId);
            Document productMongoData = colProduct.find(filter).first();

            List<Map<String, Object>> diffMaps = doCompare(productOracleData, productMongoData);
            if (!diffMaps.isEmpty()) {
                compareResults.put(prodId, diffMaps);
            }

        });
        return compareResults;
    }

    public List<Map<String, Object>> compare(Long oracleId, Long mongoId) {
        Map<Long, Map<String, Object>> productOracleDatas = getProdData(Collections.singletonList(oracleId));
        Document productMongoData = colProduct.find(eq(Field._id, mongoId)).first();
        return doCompare(productOracleDatas.get(oracleId), productMongoData);
    }

    private List<Map<String, Object>> doCompare(Map<String, Object> productOracleData, Map<String, Object> productMongoData) {
        Set<Difference> differences = ProductCompareUtils.compare(productOracleData, productMongoData);
        return differences.stream().map(difference -> difference.toMap(LEFT, RIGHT)).collect(Collectors.toList());
    }


}
