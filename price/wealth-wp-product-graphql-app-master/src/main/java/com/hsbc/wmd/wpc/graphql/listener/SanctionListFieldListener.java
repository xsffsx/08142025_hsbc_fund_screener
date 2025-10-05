package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.productConfig;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.model.UnderlyingConfig;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dummy.wmd.wpc.graphql.constant.ProdTypeCde.WARRANT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond.when;

@Component
public class SanctionListFieldListener extends BaseChangeListener {

    private MongoTemplate mongoTemplate;

    private final Map<String, UnderlyingConfig> supportTypes;

    private final List<String> interestJsonPaths;

    public SanctionListFieldListener(MongoTemplate mongoTemplate, productConfig productConfig) {
        this.mongoTemplate = mongoTemplate;

        supportTypes = productConfig.getUnderlying()
                .stream()
                .collect(Collectors.toMap(UnderlyingConfig::getProdTypeCde, Function.identity()));

        interestJsonPaths = productConfig.getUnderlying()
                .stream()
                .map(UnderlyingConfig::getPath)
                .collect(Collectors.toList());
    }

    @Override
    public void beforeInsert(Document prod) {
        calcSanctionList(prod);
    }


    @Override
    public void beforeUpdate(Document prod, List<OperationInput> operations) {
        calcSanctionList(prod);
    }

    /**
     * mongo query statement: <br/>
     * <pre>
     * {@code <script>}
     db.product.aggregate([
        {
            $match: {
                _id: {$in: []}
            }
        },
        {
            "$unwind": {
                "path": "$sanctionBuyList",
                "preserveNullAndEmptyArrays": true
            }
        },
        {
            "$unwind": {
                "path": "$sanctionSellList",
                "preserveNullAndEmptyArrays": true
            }
        },
        {
            "$group": {
                "_id": null,
                "sanctionBuyList": {
                    "$addToSet": {
                        "$cond": {
                            "if": {"$ne": ["$sanctionBuyList", null]},
                            "then": "$sanctionBuyList",
                            "else": "$unset"
                        }
                    }
                },
                "sanctionSellList": {
                    "$addToSet": {
                        "$cond": {
                            "if": {"$ne": ["$sanctionSellList", null]},
                            "then": "$sanctionSellList",
                            "else": "$unset"
                        }
                    }
                },
            }
        }
    ])
     {@code </script>}
     </pre>
     */
    private void calcSanctionList(Document prod) {
        String prodTypeCde = prod.getString(Field.prodTypeCde);
        UnderlyingConfig underlyingConfig = supportTypes.get(prodTypeCde);
        // that means this product doesn't allow to have underlying products
        if (null == underlyingConfig) {
            return;
        }
        List<Long> undlProdIdList;
        try {
            undlProdIdList = JsonPath.read(prod, underlyingConfig.getPath() + "[*].prodIdUndlInstm");
        } catch (PathNotFoundException e) {
            return;
        }
        if (WARRANT.equals(prodTypeCde) && undlProdIdList.isEmpty()) {
            return;
        }

        Aggregation aggregation = Aggregation.newAggregation(
                match(new Criteria().and(Field.prodId).in(undlProdIdList)),
                unwind(Field.sanctionSellList, true),
                unwind(Field.sanctionBuyList, true),
                              group().addToSet(when(Criteria.where(Field.sanctionSellList).ne(null))
                                .thenValueOf(Field.sanctionSellList)
                                .otherwise("$unset"))
                        .as(Field.sanctionSellList)
                        .addToSet(when(Criteria.where(Field.sanctionBuyList).ne(null))
                                .thenValueOf(Field.sanctionBuyList)
                                .otherwise("$unset"))
                        .as(Field.sanctionBuyList)
        );

        List<Document> mappedResults = mongoTemplate.aggregate(aggregation, CollectionName.product.name(), Document.class).getMappedResults();

        List<String> sanctionBuyList = null;
        List<String> sanctionSellList = null;
        if (!mappedResults.isEmpty()) {
            Document result = mappedResults.get(0);
            sanctionSellList = result.get(Field.sanctionSellList, Collections.emptyList());
            sanctionBuyList = result.get(Field.sanctionBuyList, Collections.emptyList());
        }

        prod.put(Field.sanctionSellList, sanctionSellList);
        prod.put(Field.sanctionBuyList, sanctionBuyList);
    }

    @Override
    public Collection<String> interestJsonPaths() {
        return interestJsonPaths;
    }


}
