package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DataSynchronizer;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Sequence;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Slf4j
@SuppressWarnings("java:S117")
public class AsetVoltlClassCharSynchronizer extends AsetVoltlClassBase implements DataSynchronizer {
    private static final String TARGET_COLLECTION_NAME = CollectionName.aset_voltl_class_char;
    protected MongoCollection<Document> asetvoltlclassCol;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected SequenceService sequenceService;
    public AsetVoltlClassCharSynchronizer(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.asetvoltlclassCol = mongodb.getCollection(TARGET_COLLECTION_NAME);
        this.sequenceService = new SequenceService(mongodb);
    }

    /**
     * Considering the data size is small and to simplify sync logic, the whole table will be reloaded, input keySet will be ignored
     *
     * @param keySet
     */
    @Override
    public void sync(Set<Map<String, Object>> keySet) {
        log.info("Updating {} aset_voltl_class_char ...", keySet.size());
        sync();
    }

    @Override
    public void delete(Set<String> rowidSet) {
        sync();
        log.info("{} {} item has been deleted", rowidSet.size(), CollectionName.aset_voltl_class_char);
    }

    private void sync(){
        Date lastSyncTime = new Date();
        Map<Map<String, Object>, List<Document>> groupedMap = loadAndGroupDocuments("ASET_VOLTL_CLASS_CHAR");
        groupedMap.forEach((key, list) -> {
            Document doc = toDocument(list);
            Bson filter = and(
                    eq(Field.ctryRecCde, doc.getString(Field.ctryRecCde)),
                    eq(Field.grpMembrRecCde, doc.getString(Field.grpMembrRecCde))
            );
            Document oldDoc = asetvoltlclassCol.find(filter).first();
            Long _id;
            if(null != oldDoc) {
                _id = oldDoc.getLong(Field._id);
                doc.put(Field._id, _id);  // use the old _id for revision concern
                doc.put(Field.revision, oldDoc.getLong(Field.revision) + 1);
            } else {
                _id = sequenceService.nextId(Sequence.aset_voltl_class_char_id);
            }
            doc.put(Field.lastSyncTime, lastSyncTime);
            asetvoltlclassCol.replaceOne(eq(Field._id, _id), doc, new ReplaceOptions().upsert(true));
            log.info("aset_voltl_class_char updated: id={}", _id);
        });
    }
}
