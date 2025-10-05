package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProdTypeFinDocLoader implements DataLoader {
    private static final String TARGET_COLLECTION_NAME = CollectionName.prod_type_fin_doc;
    protected MongoCollection<Document> collection;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProdTypeFinDocLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.collection = mongodb.getCollection(TARGET_COLLECTION_NAME);
    }

    @Override
    public void load() {
        if (collection.countDocuments() > 0) {
            collection.drop();
            log.warn("Target collection [{}] is not empty, dropped", TARGET_COLLECTION_NAME);
        }

        log.info("Loading Financial Document ...");
        long start = System.currentTimeMillis();

        List<Document> prodTypeDocs = load("PROD_TYPE_FIN_DOC");
        if (!prodTypeDocs.isEmpty()) {
            collection.insertMany(prodTypeDocs);
        }

        List<Document> prodSubtpDocs = load("PROD_SUBTP_FIN_DOC");
        if (!prodSubtpDocs.isEmpty()) {
            collection.insertMany(prodSubtpDocs);
        }

        long cost = (System.currentTimeMillis() - start) / 1000;
        log.info("{} seconds consumed", cost);
    }


    private List<Document> load(String table) {
        String sql = String.format("select ROWID, t.* from %s t", table);
        List<Document> docList = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            row = CodeUtils.toCamelCase(row);
            row.put(Field._id, row.get(Field.rowid));
            row.remove(Field.rowid);
            docList.add(new Document(row));
        });
        return docList;
    }
}
