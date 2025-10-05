package com.dummy.wmd.wpc.graphql.fetcher.upload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.FileIngressStatus;
import com.dummy.wmd.wpc.graphql.model.PageResult;
import com.dummy.wmd.wpc.graphql.service.DataProcessingService;
import com.dummy.wmd.wpc.graphql.service.FileUploadService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UploadResultQueryFetcher implements DataFetcher<FileIngressStatus> {
    private final DataProcessingService service;

    public UploadResultQueryFetcher(DataProcessingService dataProcessingService) {
        this.service = dataProcessingService;
    }

    @Override
    public FileIngressStatus get(DataFetchingEnvironment environment) throws JsonProcessingException {
        Document source = environment.getSource();
        Object id = source.get(Field._id);
        String filename = source.getString(Field.fileName);
        String fileMd5 = source.getString(Field.fileMd5);

        String newFilename = FileUploadService.newFilename(filename, id);
        PageResult<FileIngressStatus> result = service.queryFileIngressStatus(null, null, null, newFilename, fileMd5, null, Collections.singletonMap("createTime", 1), 0, 1);
        List<FileIngressStatus> list = result.getList();
        if(!list.isEmpty()) {
            // maybe multiple records with the same md5, take the on that has min(createTime - lastUpdateTime)
            // After approval in WPS, the file_upload.recUpdtDtTm will be updated, and then the file will be transfer to NAS folder,
            // db-file-trigger will then discover the new file and handle it, tt_file_ingress_stat record will then be created.
            // thus the first record should be the one matched
            return list.get(0);
        }
        return null;
    }
}
