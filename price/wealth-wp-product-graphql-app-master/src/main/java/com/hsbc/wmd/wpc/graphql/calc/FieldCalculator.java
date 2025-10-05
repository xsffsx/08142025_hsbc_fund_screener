package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;

@SuppressWarnings("java:S1116")
public interface FieldCalculator {
    Object calculate(Document prod);

    default void setReferenceDataService(ReferenceDataService referenceDataService){};

    default void setMongoTemplate(MongoTemplate mongoTemplate){};

    /**
     * For better performance, this method is used to describe the fields that the listener is interested in.
     * <p>
     *  When calling the listener method, it will be decided whether to call or not by judging whether the path updated
     * here is in the path of interest.
     * <p>
     * If we want to call it every time, then return an empty collection.
     * @return interest json path
     */
    Collection<String> interestJsonPaths();
}
