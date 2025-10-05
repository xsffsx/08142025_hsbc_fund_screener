package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.model.OperationInput;
import org.bson.Document;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface ChangeListener {
    /**
     * The running order of listeners, the smaller will be run first
     *
     * @return
     */
    int getOrder();
    void beforeInsert(Document doc);
    void beforeUpdate(Document doc, List<OperationInput> operations);
    void afterInsert(Document doc);
    void afterAllUpdate(List<Document> docs);

    /**
     * Before validation, something can be changed
     *
     * @param oldProd
     * @param newProd
     */
    void beforeValidation(Map<String, Object> oldProd, Map<String, Object> newProd);

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

    Collection<String> allInterestJsonPaths = Collections.emptyList();
}
