package com.dummy.wpb.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.PriceReport;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.utils.BsonUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PriceLimitHolder extends JobExecutionListenerSupport {

    private final GraphQLService graphQLService;

    private static final Map<String, BigDecimal> tolLimitMap = new HashMap<>();

    private static final Map<String, BigDecimal> invLimitMap = new HashMap<>();

    public PriceLimitHolder(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();

        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(CommonUtils.readResource("/gql/sysParm-query.gql"));
        graphQLRequest.setDataPath("data.sysParmByFilter");
        graphQLRequest.setOperationName("sysParmByFilter");
        Bson filters = Filters.and(
                Filters.eq(Field.ctryRecCde, jobParameters.getString(Field.ctryRecCde)),
                Filters.eq(Field.grpMembrRecCde, jobParameters.getString(Field.grpMembrRecCde)),
                Filters.regex(Field.parmCde,"^PRC_UT")
        );
        graphQLRequest.setVariables(Collections.singletonMap("filter", BsonUtils.toMap(filters)));

        try {
            List<Document> paramList = graphQLService.executeRequest(graphQLRequest, new TypeReference<List<Document>>() {
            });

            for (Document param : paramList) {
                String parmCde = param.getString(Field.parmCde);
                String parmValue = param.getString(Field.parmValueText);
                String[] splits = StringUtils.split(parmCde, "_");
                if (splits.length == 4) {
                    if (StringUtils.equalsIgnoreCase(PriceReport.REPORT_TYPE_TOLERANCE, splits[2])) {
                        tolLimitMap.put(splits[3], NumberUtils.toScaledBigDecimal(parmValue));
                    } else if (StringUtils.equalsIgnoreCase(PriceReport.REPORT_TYPE_INVESTIGATION, splits[2])) {
                        invLimitMap.put(splits[3], NumberUtils.toScaledBigDecimal(parmValue));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to init Price Limit param", e);
        }
    }

    public static BigDecimal getTolLimit(String prcType){
        return tolLimitMap.get(prcType);
    }

    public static BigDecimal getInvLimit(String prcType){
        return invLimitMap.get(prcType);
    }
}
