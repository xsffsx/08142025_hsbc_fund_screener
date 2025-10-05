package com.dummy.wmd.wpc.graphql.fetcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dummy.wmd.wpc.graphql.Configuration;
import com.dummy.wmd.wpc.graphql.model.FileIngressRecord;
import com.dummy.wmd.wpc.graphql.model.PageResult;
import com.dummy.wmd.wpc.graphql.service.DataProcessingService;
import com.dummy.wmd.wpc.graphql.utils.GraphQLSchemaUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class FileIngressRecordQueryFetcher implements DataFetcher<PageResult<FileIngressRecord>> {
    private final DataProcessingService service;
    private static Integer defaultLimit = Configuration.getDefaultLimit();

    public FileIngressRecordQueryFetcher(DataProcessingService dataProcessingService) {
        this.service = dataProcessingService;
    }

    @Override
    public PageResult<FileIngressRecord> get(DataFetchingEnvironment environment) throws JsonProcessingException {
        Long fisid = environment.getArgument("fisid");
        List<String> statusIn = environment.getArgument("statusIn");

        Map<String, Object> sortMap = environment.getArgument("sort");
        Integer skip = environment.getArgument("skip");
        Integer limit = environment.getArgument("limit");
        if(null == limit) limit = defaultLimit;

        Set<String> selectFields = GraphQLSchemaUtils.getSelectedFields(environment);
        return service.queryFileIngressRecord(fisid, statusIn, sortMap, skip, limit, selectFields);
    }
}
