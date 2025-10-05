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
public class StaffLicenseCheckLoader implements DataLoader {
    private static final String TARGET_COLLECTION_NAME = CollectionName.staff_license_check;
    protected MongoCollection<Document> collectionStaffLicens;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected SequenceService sequenceService;

    public StaffLicenseCheckLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.collectionStaffLicens = mongodb.getCollection(TARGET_COLLECTION_NAME);
        this.sequenceService = new SequenceService(mongodb);
    }

    @Override
    public void load() {
        if (collectionStaffLicens.countDocuments() > 0) {
            collectionStaffLicens.drop();
            log.warn("Target collectionStaffLicens [{}] is not empty, dropped", TARGET_COLLECTION_NAME);
        }

        log.info("Loading StaffLicenseCheck ...");
        long start = System.currentTimeMillis();
        List<Document> prodTypeDocs = loadStaffLicens(Table.PROD_TYPE_STAF_LIC_CHECK);
        if (!prodTypeDocs.isEmpty()) {
            collectionStaffLicens.insertMany(prodTypeDocs);
        }
        List<Document> prodSubtpDocs = loadStaffLicens(Table.PROD_SUBTP_STAF_LIC_CHECK);
        if (!prodSubtpDocs.isEmpty()) {
            collectionStaffLicens.insertMany(prodSubtpDocs);
        }
        long cost = (System.currentTimeMillis() - start) / 1000;
        log.info("{} seconds consumed", cost);
    }

    private List<Document> loadStaffLicens(String table) {
        String sql = String.format("select ROWID, t.* from %s t", table);
        List<Document> docList = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, resultSet -> {
            Map<String, Object> rowStaffLicens = DbUtils.getStringObjectMap(resultSet);
            rowStaffLicens = CodeUtils.toCamelCase(rowStaffLicens);
            rowStaffLicens.put(Field._id, sequenceService.nextId(Sequence.staffLicenseCheckId));
            rowStaffLicens.remove(Field.rowid);
            rowStaffLicens.put(Field.revision, 1L);
            rowStaffLicens.put(Field.createdBy, "load");
            docList.add(new Document(rowStaffLicens));
        });
        return docList;
    }
}
