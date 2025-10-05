package com.dummy.wmd.wpc.graphql.fetcher.upload;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.RecStatCde;
import com.dummy.wmd.wpc.graphql.service.LogFileService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Component
public class UploadLogFileQueryFetcher implements DataFetcher<String> {

    public UploadLogFileQueryFetcher() {
        //do nothing
    }

    @Override
    public String get(DataFetchingEnvironment environment) {
        Document source = environment.getSource();

        // only approved upload record should have the log file
        String recStatCde = source.getString(Field.recStatCde);
        if(!RecStatCde.approved.toString().equalsIgnoreCase(recStatCde)) {
            return null;
        }

        String fileName = source.getString(Field.fileName);

        return LogFileService.mapLogFileByFileName(fileName);
    }
}
