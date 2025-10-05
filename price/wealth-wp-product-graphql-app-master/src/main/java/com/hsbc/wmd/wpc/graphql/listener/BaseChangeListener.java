package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.model.OperationInput;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public abstract class BaseChangeListener implements ChangeListener{
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void beforeInsert(Document doc) {
        // DO NOTHING BY DEFAULT
    }

    @Override
    public void beforeUpdate(Document doc, List<OperationInput> operations) {
        // DO NOTHING BY DEFAULT
    }

    @Override
    public void afterInsert(Document doc) {
        // DO NOTHING BY DEFAULT
    }

    @Override
    public void afterAllUpdate(List<Document> docs) {
         // DO NOTHING BY DEFAULT
    }

    @Override
    public void beforeValidation(Map<String, Object> oldProd, Map<String, Object> newProd) {
        // DO NOTHING BY DEFAULT
    }
}
