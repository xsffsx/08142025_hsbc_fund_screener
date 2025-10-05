package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataSynchronizer;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Sequence;
import com.dummy.wpb.wpc.utils.constant.Table;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class StaffLicenceCheckSynchronizer implements DataSynchronizer {
    protected MongoCollection<Document> collection;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected String table;
    protected SequenceService sequenceService;

    public StaffLicenceCheckSynchronizer(String table, NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        this.table = table;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.collection = mongodb.getCollection(CollectionName.staff_license_check);
        this.sequenceService = new SequenceService(mongodb);
    }

    protected Map<String, Object> getMasterRecord(Map<String, Object> keys) {
        String sql;
        if (Table.PROD_TYPE_STAF_LIC_CHECK.equals(table)) {
            // PROD_TYPE_STAF_LIC_CHECK
            sql = "select ROWID, t.* from PROD_TYPE_STAF_LIC_CHECK t where CTRY_REC_CDE = :CTRY_REC_CDE and GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE and PROD_TYPE_CDE = :PROD_TYPE_CDE and EMPLOY_POSN_CDE = :EMPLOY_POSN_CDE";
        } else {
            // PROD_SUBTP_STAF_LIC_CHECK
            sql = "select ROWID, t.* from PROD_SUBTP_STAF_LIC_CHECK t where CTRY_REC_CDE = :CTRY_REC_CDE and GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE and PROD_SUBTP_CDE = :PROD_SUBTP_CDE and EMPLOY_POSN_CDE = :EMPLOY_POSN_CDE";
        }
        SqlParameterSource parameters = new MapSqlParameterSource(keys);
        List<Map<String, Object>> listStaffLicenceCheck = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, parameters,(RowCallbackHandler) rs ->
            listStaffLicenceCheck.add(CodeUtils.toCamelCase(DbUtils.getStringObjectMap(rs)))
        );
        return listStaffLicenceCheck.isEmpty() ? null : listStaffLicenceCheck.get(0);
    }

    @Override
    public void sync(Set<Map<String, Object>> keySet) {
        log.info("Updating {} StaffLicenceCheck(s) ...", keySet.size());
        keySet.forEach(keys -> {
            Map<String, Object> staffLicenceCheck = getMasterRecord(keys);
            if(null != staffLicenceCheck) {
                staffLicenceCheck.remove(Field.rowid);
                staffLicenceCheck.put(Field._id, getStaffLicenseCheckId(staffLicenceCheck));
                collection.replaceOne(Filters.eq(Field._id, staffLicenceCheck.get(Field._id)), new Document(staffLicenceCheck), new ReplaceOptions().upsert(true));
            }
        });
    }

    @Override
    public void delete(Set<String> rowidSet) {
        DeleteResult result = collection.deleteMany(Filters.in("_id", rowidSet));
        log.info("{} {} item has been deleted", result.getDeletedCount(), CollectionName.staff_license_check);
    }

    /**
     * Get Staff License Check Id
     * If record exists in DB, then return current Id
     * If record doesn't exist, then return a new Id
     */
    private Long getStaffLicenseCheckId(Map<String, Object> map) {
        Long id;
        Bson filter = Filters.and(
                Filters.eq(Field.ctryRecCde, map.get(Field.ctryRecCde)),
                Filters.eq(Field.grpMembrRecCde, map.get(Field.grpMembrRecCde)),
                Filters.eq(Field.prodTypeCde, map.get(Field.prodTypeCde)),
                Filters.eq(Field.prodSubtpCde, map.get(Field.prodSubtpCde)),
                Filters.eq(Field.employPosnCde, map.get(Field.employPosnCde))
        );
        Document doc = collection.find(filter).first();
        if (doc != null) {
            id = doc.getLong(Field._id);
        } else {
            id = sequenceService.nextId(Sequence.staffLicenseCheckId);
        }
        return id;
    }
}
