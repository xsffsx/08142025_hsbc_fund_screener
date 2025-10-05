package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.DiffType;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import com.dummy.wmd.wpc.graphql.utils.DocumentDiff;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S1168")
@Slf4j
@Component
public class DiffFromLatestFetcher implements DataFetcher<List<DiffType>> {
    private AmendmentService service;

    public DiffFromLatestFetcher(AmendmentService service){
        this.service = service;
    }

    @Override
    public List<DiffType> get(DataFetchingEnvironment environment) {
        Document source = environment.getSource();
        Map<String, Object> docBase = (Map<String, Object>)source.get(Field.docBase);

        DocType docType = DocType.valueOf(source.getString(Field.docType));
        Object docId = source.get(Field.docId);
        Map<String, Object> docLatest = service.getLatestDocument(docType, docId);

        if(null == docBase || null == docLatest) {   // `add` case has no docBase
            return null;
        }

        return DocumentDiff.diff(docBase, docLatest);
    }
}