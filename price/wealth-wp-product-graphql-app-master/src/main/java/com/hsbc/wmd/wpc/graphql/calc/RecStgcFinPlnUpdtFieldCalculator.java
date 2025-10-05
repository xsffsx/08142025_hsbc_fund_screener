package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.DBUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.apache.commons.collections4.MapUtils;
import org.bson.Document;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static com.mongodb.client.model.Projections.include;

public class RecStgcFinPlnUpdtFieldCalculator implements FieldCalculator {
    @Override
    public Object calculate(Document newProd) {
        MongoCollection<Document> productCollection = DBUtils.getCollection(CollectionName.product.name());

        Document oldProd = productCollection
                .find(Filters.eq(Field.prodId, newProd.get(Field.prodId)))
                .projection(include(Field.recStgcFinPlnUpdtDtTm))
                .first();

        if (MapUtils.isNotEmpty(oldProd)){
            return oldProd.get(Field.recStgcFinPlnUpdtDtTm);
        }
        return new Date();
    }

    @Override
    public Collection<String> interestJsonPaths() {
        return Collections.singleton(Field.recStgcFinPlnUpdtDtTm);
    }
}
