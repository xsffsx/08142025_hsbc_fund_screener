package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DataLoader;
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

import java.util.List;
import java.util.Map;

@Slf4j
public class AsetVoltlClassCharLoader extends AsetVoltlClassBase implements DataLoader {
    private static final String TARGET_COLLECTION_NAME = CollectionName.aset_voltl_class_char;
    protected MongoCollection<Document> asetVoltlClassCol;
    private SequenceService sequenceService;

    public AsetVoltlClassCharLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb){
        super(namedParameterJdbcTemplate);
        this.asetVoltlClassCol = mongodb.getCollection(TARGET_COLLECTION_NAME);
        this.sequenceService = new SequenceService(mongodb);
    }

    @Override
    public void load() {
        if(asetVoltlClassCol.countDocuments() > 0){
            asetVoltlClassCol.drop();
            log.warn("Target collection [{}] is not empty, dropped", TARGET_COLLECTION_NAME);
        }

        log.info("Loading AsetVoltlClassChar ...");
        long start = System.currentTimeMillis();

        Map<Map<String, Object>, List<Document>> groupedMap = loadAndGroupDocuments(Table.ASET_VOLTL_CLASS_CHAR);
        groupedMap.forEach((key, list) -> {
            Document doc = toDocument(list);
            doc.put(Field._id, sequenceService.nextId(Sequence.aset_voltl_class_char_id));
            asetVoltlClassCol.insertOne(doc);
        });

        long cost = (System.currentTimeMillis() - start) / 1000;
        log.info("{} seconds consumed", cost);
    }

}
