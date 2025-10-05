package com.dummy.wmd.wpc.graphql.fetcher;
import com.google.common.base.CaseFormat;
import com.dummy.wmd.wpc.graphql.model.BatchJobExecution;
import com.dummy.wmd.wpc.graphql.model.Pagination;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

@Slf4j
@Component

public class BatchExecutionFetcher implements DataFetcher<Pagination<BatchJobExecution>> {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String STATUS = "status";
    private static final String JOB_NAME = "jobName";
    private static final String DATE_FROM = "dateFrom";
    private static final String DATE_TO = "dateTo";
    private static final String JOB_EXECUTION_ID = "jobExecutionId";
    private static final String SYSTEM_CDE = "systemCde";
    @Value("#{${product.graphql.default-limit}}")
    private Integer defaultLimit;
    private static final List<String> paramKeys = Arrays.asList(STATUS, JOB_NAME, JOB_EXECUTION_ID, DATE_FROM, DATE_TO, SYSTEM_CDE);

    @Override
    public Pagination<BatchJobExecution> get(DataFetchingEnvironment environment) throws ParseException {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        Map<String, Object> filterMap = environment.getArgument("filter");
        String jobName = (String) filterMap.get(JOB_NAME);
        String systemCde = (String) filterMap.get(SYSTEM_CDE);
        String status = (String) filterMap.get(STATUS);
        Integer jobExecutionId = (Integer) filterMap.get(JOB_EXECUTION_ID);
        Pagination<BatchJobExecution> pagination = new Pagination<>();
        if (!filterMap.isEmpty() && filterMap.keySet().stream().noneMatch(paramKeys::contains)) {
            pagination.setTotal(0L);
            pagination.setResultList(Collections.emptyList());
            return pagination;
        }
        StringBuilder sql = new StringBuilder("select a.job_execution_id,d.string_val as system_Cde,a.create_time,a.start_time,a.end_time,a.status,a.exit_code,a.exit_message,a.last_updated,b.job_name,b.job_instance_id,c.short_context,c.serialized_context from batch_job_execution a left join batch_job_instance b on a.job_instance_id = b.job_instance_id  left join batch_job_execution_context c on a.job_execution_id = c.job_execution_id left join batch_job_execution_params d on d.job_execution_id = c.job_execution_id and d.key_name = 'systemCde'");
        StringBuilder totalSql = new StringBuilder("SELECT COUNT(*) FROM batch_job_execution a left join batch_job_instance b on a.job_instance_id = b.job_instance_id  left join batch_job_execution_context c on a.job_execution_id = c.job_execution_id left join batch_job_execution_params d on d.job_execution_id = c.job_execution_id and d.key_name = 'systemCde'");
        StringBuilder querySql = new StringBuilder(" where 1=1 ");
        Map<String, Integer> sortMap = environment.getArgument("sort");
        Integer skip = environment.getArgument("skip");
        Integer limit = environment.getArgument("limit");
        if (Objects.nonNull(jobName)){
            querySql.append("and job_name ILike '%").append(jobName).append("%'");
            parameters.addValue(JOB_NAME, jobName);
        }
        if (Objects.nonNull(systemCde)){
            querySql.append("and d.string_val ILike '%").append(systemCde).append("%'");
            parameters.addValue(SYSTEM_CDE, systemCde);
        }
        if (Objects.nonNull(status)){
            querySql.append("and a.status = '").append(status).append("' ");
            parameters.addValue(STATUS, status);
        }
        if (filterMap.containsKey(DATE_FROM)){
            Date dateFrom = DateUtils.parseDate((String) filterMap.get(DATE_FROM), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            querySql.append("and start_time > :dateFrom ");
            parameters.addValue(DATE_FROM, dateFrom);
        }
        if (filterMap.containsKey(DATE_TO)){
            Date dateTo = DateUtils.parseDate((String) filterMap.get(DATE_TO), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            querySql.append("and start_time < :dateTo ");
            parameters.addValue(DATE_TO, dateTo);
        }

        if (Objects.nonNull(jobExecutionId)){
            querySql.append("and a.job_execution_id = :jobExecutionId");
            parameters.addValue(JOB_EXECUTION_ID, jobExecutionId);
        }
        totalSql.append(querySql);
        if (MapUtils.isNotEmpty(sortMap)) {
            querySql.append(" order by");
            sortMap.forEach((sortKey, direInt) -> {
                String sortField = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortKey);
                String direction = direInt > 0 ? "ASC" : "DESC";
                querySql.append(" ").append(sortField).append(" ").append(direction).append(",");
            });
            querySql.deleteCharAt(querySql.length() - 1);
        }
        if(null == limit) limit = defaultLimit;
        sql.append(querySql);
        sql.append(" LIMIT ").append(limit);
        sql.append(" OFFSET ").append(skip);
        List<BatchJobExecution> resultList = namedParameterJdbcTemplate.query(sql.toString(), parameters, new BeanPropertyRowMapper<>(BatchJobExecution.class));
        pagination.setResultList(resultList);
        pagination.setTotal(namedParameterJdbcTemplate.queryForObject(totalSql.toString(), parameters, Long.class));
        return pagination;
    }
}
