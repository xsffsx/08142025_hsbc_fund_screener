package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.model.BatchJobExecutionParams;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BatchExecutionParamsFetcher implements DataFetcher<List<BatchJobExecutionParams>> {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Override
    public List<BatchJobExecutionParams> get(DataFetchingEnvironment environment) {
        Long jobExecutionId = environment.getArgument("jobExecutionId");
        StringBuilder sql = new StringBuilder();
        sql.append("select * from batch_job_execution_params where job_execution_id = " + jobExecutionId);
        return namedParameterJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(BatchJobExecutionParams.class));
    }
}
