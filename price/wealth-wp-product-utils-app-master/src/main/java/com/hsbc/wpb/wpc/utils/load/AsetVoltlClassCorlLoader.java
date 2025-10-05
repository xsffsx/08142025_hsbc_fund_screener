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
public class AsetVoltlClassCorlLoader extends AsetVoltlClassBase implements DataLoader {
    private static final String TARGET_COLLECTION_NAME = CollectionName.aset_voltl_class_corl;
    protected MongoCollection<Document> asetvoltlclassCol;
    private SequenceService sequenceService;

    public AsetVoltlClassCorlLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb){
        super(namedParameterJdbcTemplate);
        this.asetvoltlclassCol = mongodb.getCollection(TARGET_COLLECTION_NAME);
        this.sequenceService = new SequenceService(mongodb);
    }

    @Override
    public void load() {
        if(asetvoltlclassCol.countDocuments() > 0){
            asetvoltlclassCol.drop();
            log.warn("Target collection [{}] is not empty, dropped", TARGET_COLLECTION_NAME);
        }

        log.info("Loading AsetVoltlClassCorl ...");
        long start = System.currentTimeMillis();

        Map<Map<String, Object>, List<Document>> groupedMap = loadAndGroupDocuments(Table.ASET_VOLTL_CLASS_CORL);
        groupedMap.forEach((key, list) -> {
            Document doc = toDocument(list);
            doc.put(Field._id, sequenceService.nextId(Sequence.aset_voltl_class_corl_id));
            asetvoltlclassCol.insertOne(doc);
        });

        long cost = (System.currentTimeMillis() - start) / 1000;
        log.info("{} seconds consumed", cost);
    }

}
