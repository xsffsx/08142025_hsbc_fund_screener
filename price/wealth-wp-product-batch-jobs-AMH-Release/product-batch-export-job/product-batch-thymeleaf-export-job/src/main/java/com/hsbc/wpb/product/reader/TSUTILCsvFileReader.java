package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregationOptions;

public class TSUTILCsvFileReader extends AbstractMongoReader {

    protected List<Long> prodIdList = new ArrayList<>();

    @Override
    protected void doOpen() throws Exception {
        BasicQuery query = new BasicQuery(filter);
        query.fields().include(Field.prodId);
        query.setSortObject(sort);
        mongoTemplate.find(query, Document.class, collection.name()).forEach(prod -> prodIdList.add(prod.getLong(Field.prodId)));
        super.doOpen();
    }

    @Override
    protected Iterator<Document> doPageRead() {
        if (page * pageSize >= prodIdList.size()) {
            return null;
        }

        List<Long> currentProdIdList = prodIdList.subList(page * pageSize, Math.min(prodIdList.size(), (page + 1) * pageSize));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria
                        .where(Field.prodId).in(currentProdIdList)
                        .and(Field.prodNavPrcAmt).ne(null)
                        .andOperator(
                                Criteria.where(Field.prcEffDt).gte(LocalDate.now().minusYears(5)),
                                Criteria.where(Field.prcEffDt).lte(LocalDate.now())
                        )),
                Aggregation.lookup(CollectionName.product.name(), Field.prodId, Field.prodId, CollectionName.product.name()),
                Aggregation.unwind(CollectionName.product.name()),
                Aggregation.project(Field.prodId, Field.prcEffDt, Field.prodNavPrcAmt)
                        .and(
                                ArrayOperators.ArrayElemAt.arrayOf(
                                        ArrayOperators.Filter.filter("product.altId")
                                                .as("item")
                                                .by(ComparisonOperators.Eq.valueOf("$$item.prodCdeAltClassCde").equalToValue("T"))
                                ).elementAt(0)
                        ).as("altIdT"),
                Aggregation.sort(Sort.Direction.ASC, Field.prcEffDt),
                Aggregation.group(Fields.fields(Field.prodId).and("prodAltNumT", "altIdT.prodAltNum"))
                        .push(new Document(Field.prcEffDt, "$prcEffDt").append(Field.prodNavPrcAmt, "$prodNavPrcAmt"))
                        .as("prices"),
                Aggregation.project(Fields
                        .from(Fields.field(Field.prodId, "$_id.prodId"))
                        .and("prodAltNumT", "$_id.prodAltNumT")
                        .and("prices")
                )
        ).withOptions(newAggregationOptions().allowDiskUse(true).build());

        List<Document> result = new ArrayList<>(mongoTemplate.aggregate(aggregation, CollectionName.prod_prc_hist.name(), Document.class).getMappedResults());

        currentProdIdList.stream()
                .filter(prodId -> result.stream().noneMatch(product -> product.get(Field.prodId).equals(prodId)))
                .forEach(prodId -> result.add(new Document(Field.prodId, prodId)));
        return result.iterator();
    }
}
