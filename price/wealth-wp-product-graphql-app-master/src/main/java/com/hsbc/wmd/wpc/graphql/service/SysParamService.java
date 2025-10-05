package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Service
public class SysParamService {

	private static final FindOneAndUpdateOptions findOneAndUpdateOptions = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).upsert(false);
    private MongoCollection<Document> colSysParam;

    public SysParamService(MongoDatabase mongoDatabase) {
        this.colSysParam = mongoDatabase.getCollection(CollectionName.sys_parm.toString());
    }

    public SysParamBatchUpdateResult batchUpdate(List<Map<String, Object>> sysParams) {

        List<Document> updatedSysParams = new ArrayList<>();
        List<Document> notExistingSysParams = new ArrayList<>();
        SysParamBatchUpdateResult result = new SysParamBatchUpdateResult();
        result.setUpdatedSysParams(updatedSysParams);
        result.setNotExistingSysParams(notExistingSysParams);
        if (sysParams == null || sysParams.isEmpty()) {
            return result;
        }

        sysParams.forEach(sysParam -> {
            Bson query = and(
                eq(Field.ctryRecCde, sysParam.get(Field.ctryRecCde)),
                eq(Field.grpMembrRecCde, sysParam.get(Field.grpMembrRecCde)),
                eq(Field.parmCde, sysParam.get(Field.parmCde)));

            sysParam.remove(Field._id);
            sysParam.remove(Field.rowid);
            sysParam.remove(Field.recCreatDtTm);
            sysParam.remove(Field.createdBy);
            sysParam.put(Field.recUpdtDtTm, new Date());

            Update update = new Update();
            sysParam.forEach(update::set);

            Document updatedSysParam = colSysParam.findOneAndUpdate(query, update.getUpdateObject(), findOneAndUpdateOptions);

            if (updatedSysParam != null) {
                updatedSysParams.add(updatedSysParam);
            } else {
                Document notExistingSysParam = new Document();
                notExistingSysParam.put(Field.ctryRecCde, sysParam.get(Field.ctryRecCde));
                notExistingSysParam.put(Field.grpMembrRecCde, sysParam.get(Field.grpMembrRecCde));
                notExistingSysParam.put(Field.parmCde, sysParam.get(Field.parmCde));
                notExistingSysParams.add(notExistingSysParam);
            }
        });
        return result;
    }

}

