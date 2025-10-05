package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AmendmentDocLatestFetcher implements DataFetcher<Document> {
    private AmendmentService service;

    public AmendmentDocLatestFetcher(AmendmentService service){
        this.service = service;
    }

    @Override
    public Document get(DataFetchingEnvironment environment) {
        Document source = environment.getSource();

        DocType docType = DocType.valueOf(source.getString(Field.docType));
        Object docId = source.get(Field.docId);

        return service.getLatestDocument(docType, docId, (Document) source.get("doc"));
    }
}