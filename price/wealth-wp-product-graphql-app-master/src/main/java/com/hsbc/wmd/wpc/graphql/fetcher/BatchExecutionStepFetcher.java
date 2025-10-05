package com.dummy.wmd.wpc.graphql.fetcher;

import com.google.common.base.CaseFormat;
import com.dummy.wmd.wpc.graphql.model.BatchJobExecutionStep;
import com.dummy.wmd.wpc.graphql.model.Pagination;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S3740")
@Component
public class BatchExecutionStepFetcher implements DataFetcher<Pagination<BatchJobExecutionStep>> {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Override
    public Pagination<BatchJobExecutionStep> get(DataFetchingEnvironment environment) {
        Long jobExecutionId = environment.getArgument("jobExecutionId");
        Map<String, Integer> sortMap = environment.getArgument("sort");
        StringBuilder sql = new StringBuilder();
        sql.append("select * from batch_step_execution a left join batch_step_execution_context b on a.step_execution_id = b.step_execution_id where job_execution_id = " + jobExecutionId);
        if (MapUtils.isNotEmpty(sortMap)) {
            sql.append(" order by");
            sortMap.forEach((sortKey, direInt) -> {
                String sortField = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortKey);
                String direction = direInt > 0 ? "ASC" : "DESC";
                sql.append(" ").append(sortField).append(" ").append(direction).append(",");
            });
            sql.deleteCharAt(sql.length() - 1);
        }
        List<BatchJobExecutionStep> resultList = namedParameterJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(BatchJobExecutionStep.class));
        Pagination<BatchJobExecutionStep> pagination = new Pagination();
        pagination.setResultList(resultList);
        pagination.setTotal((long) resultList.size());
        return pagination;
    }
}
