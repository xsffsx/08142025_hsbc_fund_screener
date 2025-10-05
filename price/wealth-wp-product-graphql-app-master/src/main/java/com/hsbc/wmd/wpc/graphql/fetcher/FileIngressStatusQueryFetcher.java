package com.dummy.wmd.wpc.graphql.fetcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dummy.wmd.wpc.graphql.Configuration;
import com.dummy.wmd.wpc.graphql.model.FileIngressStatus;
import com.dummy.wmd.wpc.graphql.model.PageResult;
import com.dummy.wmd.wpc.graphql.service.DataProcessingService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Component
public class FileIngressStatusQueryFetcher implements DataFetcher<PageResult<FileIngressStatus>> {
    private final DataProcessingService service;
    private static Integer defaultLimit = Configuration.getDefaultLimit();

    public FileIngressStatusQueryFetcher(DataProcessingService dataProcessingService) {
        this.service = dataProcessingService;
    }

    @Override
    public PageResult<FileIngressStatus> get(DataFetchingEnvironment environment) throws JsonProcessingException {
        Long id = environment.getArgument("id");
        OffsetDateTime timeFrom = environment.getArgument("fromTime");
        OffsetDateTime timeTo = environment.getArgument("toTime");
        String filename = environment.getArgument("filename");
        String md5 = environment.getArgument("md5");
        List<String> statusIn = environment.getArgument("statusIn");

        Map<String, Object> sortMap = environment.getArgument("sort");
        Integer skip = environment.getArgument("skip");
        Integer limit = environment.getArgument("limit");
        if(null == limit) limit = defaultLimit;

        return service.queryFileIngressStatus(id, timeFrom, timeTo, filename, md5, statusIn, sortMap, skip, limit);
    }
}
