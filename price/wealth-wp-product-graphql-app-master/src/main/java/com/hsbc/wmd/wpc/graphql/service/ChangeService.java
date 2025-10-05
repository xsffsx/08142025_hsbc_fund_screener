package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.validator.Error;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public interface ChangeService {
    /**
     * Validate the amendment
     *
     * @param amendment
     * @return
     */
    List<Error> validate(Document amendment);

    void add(Document doc);

    void update(Document doc);

    void delete(Document doc);

    Document findFirst(Bson filter);

    default Document findFirst(Bson filter, Document docChange){
        return this.findFirst(filter);
    }
}
