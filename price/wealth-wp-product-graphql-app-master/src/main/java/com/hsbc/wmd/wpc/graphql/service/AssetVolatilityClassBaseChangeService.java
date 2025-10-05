package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.validator.Error;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("java:S3740")
public abstract class AssetVolatilityClassBaseChangeService implements ChangeService{
    /**
     * Fill in rowid if not exist or empty
     *
     * @param list
     */
    protected void fillListRowid(List<Map> list) {
        list.forEach(item -> {
            if(!StringUtils.hasText((String) item.get(Field.rowid))){
                item.put(Field.rowid, UUID.randomUUID().toString());
            }
        });
    }

    public abstract List<Error> validateDocument(Document doc);
    public abstract Document updateDocument(Document doc);
    public abstract Document addDocument(Document doc);
}
