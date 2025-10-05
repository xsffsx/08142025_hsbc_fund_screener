package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.DiffType;
import com.dummy.wmd.wpc.graphql.service.DocumentRevisionService;
import com.dummy.wmd.wpc.graphql.utils.DocumentDiff;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DocumentRevisionDiffFetcher implements DataFetcher<List<DiffType>> {
    private DocumentRevisionService revisionService;

    public DocumentRevisionDiffFetcher(DocumentRevisionService revisionService){
        this.revisionService = revisionService;
    }

    @Override
    public List<DiffType> get(DataFetchingEnvironment environment) throws IOException {
        List<DiffType> diffList = new ArrayList<>();
        // docType:"product", docId:40000299, leftRevision:1, rightRevision:2
        String docType = environment.getArgument(Field.docType);
        Long docId = environment.getArgument(Field.docId);
        Long leftRevision = environment.getArgument("leftRevision");
        Long rightRevision = environment.getArgument("rightRevision");

        Document docLeft = revisionService.getDocument(docType, docId, leftRevision);
        Document docRight = revisionService.getDocument(docType, docId, rightRevision);
        if(null == docLeft || null == docRight) {
            return diffList;  // if any of the left / right is null, return empty opList
        }

        diffList = DocumentDiff.diff(docLeft, docRight);

        return diffList;
    }
}