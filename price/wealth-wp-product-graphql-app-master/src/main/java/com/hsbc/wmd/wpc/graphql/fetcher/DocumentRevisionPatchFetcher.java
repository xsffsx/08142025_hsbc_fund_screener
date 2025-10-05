package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.service.DocumentRevisionService;
import com.dummy.wmd.wpc.graphql.utils.DocumentPatch;
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
public class DocumentRevisionPatchFetcher implements DataFetcher<List<OperationInput>> {
    private DocumentRevisionService revisionService;

    public DocumentRevisionPatchFetcher(DocumentRevisionService revisionService){
        this.revisionService = revisionService;
    }

    @Override
    public List<OperationInput> get(DataFetchingEnvironment environment) throws IOException {
        List<OperationInput> opList = new ArrayList<>();
        // docType:"product", docId:40000299, leftRevision:1, rightRevision:2
        String docType = environment.getArgument(Field.docType);
        Long docId = environment.getArgument(Field.docId);
        Long leftRevision = environment.getArgument("leftRevision");
        Long rightRevision = environment.getArgument("rightRevision");

        Document docLeft = revisionService.getDocument(docType, docId, leftRevision);
        Document docRight = revisionService.getDocument(docType, docId, rightRevision);
        if(null == docLeft || null == docRight) {
            return opList;  // if any of the left / right is null, return empty opList
        }

        DocumentPatch diff = new DocumentPatch();
        opList = diff.patch(docLeft, docRight);

        return opList;
    }
}