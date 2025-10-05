package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Sequence;
import com.dummy.wpb.wpc.utils.constant.Table;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SysParamLoader implements DataLoader {
    private static final String TARGET_COLLECTION_NAME = CollectionName.sys_parm;
    protected MongoCollection<Document> collection;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected SequenceService sequenceService;

    public SysParamLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.collection = mongodb.getCollection(TARGET_COLLECTION_NAME);
        this.sequenceService = new SequenceService(mongodb);
    }

    @Override
    public void load() {
        if(collection.countDocuments() > 0){
            collection.drop();
            log.warn("Target collection [{}] is not empty, dropped", TARGET_COLLECTION_NAME);
        }

        log.info("Loading StaffLicenseCheck ...");
        long start = System.currentTimeMillis();
        // , , ,
        List<Document> sysParamDocs = load(Table.SYS_PARM);
        if(!sysParamDocs.isEmpty()) {
            collection.insertMany(sysParamDocs);
        }

        List<Document> finDocParamDocs = load(Table.FIN_DOC_PARM);
        if(!finDocParamDocs.isEmpty()) {
            collection.insertMany(finDocParamDocs);
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
            docList.add(new Document(row));
        });
        return docList;
    }
}
