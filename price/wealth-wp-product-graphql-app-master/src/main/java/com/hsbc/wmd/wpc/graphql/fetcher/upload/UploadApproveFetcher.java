package com.dummy.wmd.wpc.graphql.fetcher.upload;

import com.dummy.wmd.wpc.graphql.RequestContext;
import com.dummy.wmd.wpc.graphql.service.FileUploadService;
import com.dummy.wmd.wpc.graphql.utils.CheckmarxUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UploadApproveFetcher implements DataFetcher<Document> {
    private FileUploadService fileUploadService;

    public UploadApproveFetcher(FileUploadService fileUploadService){
        this.fileUploadService = fileUploadService;
    }

    @Override
    public Document get(DataFetchingEnvironment environment) {
        Long uploadId = environment.getArgument("uploadId");
        String approvalAction = environment.getArgument("approvalAction");
        String comment = environment.getArgument("comments");
        String uploadType = environment.getArgument("uploadType");
        RequestContext ctx = RequestContext.getCurrentContext();
        String emplyNum = CheckmarxUtils.preventCGIReflectedXSSAllClients(ctx.getUserId());

        return fileUploadService.approve(emplyNum, uploadId, approvalAction, comment, uploadType);
    }
}