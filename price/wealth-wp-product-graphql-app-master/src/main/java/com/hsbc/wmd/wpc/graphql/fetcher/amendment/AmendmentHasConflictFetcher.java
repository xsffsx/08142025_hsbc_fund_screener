package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@Slf4j
public class AmendmentHasConflictFetcher implements DataFetcher<Boolean> {
    private AmendmentService service;

    public AmendmentHasConflictFetcher(AmendmentService service){
        this.service = service;
    }

    @Override
    public Boolean get(DataFetchingEnvironment environment) {
        Document source = environment.getSource();

        String docType = source.getString(Field.docType);
        Document doc = (Document)source.get(Field.doc);
        long docId = DocumentUtils.getLong(doc, Field._id);
        long docRevision = DocumentUtils.getLong(doc, Field.revision);

        return service.hasConflict(docType, docId, docRevision);
    }
}