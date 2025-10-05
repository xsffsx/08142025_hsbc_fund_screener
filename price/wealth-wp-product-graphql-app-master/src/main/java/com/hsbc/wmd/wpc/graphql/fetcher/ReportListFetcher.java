package com.dummy.wmd.wpc.graphql.fetcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dummy.wmd.wpc.graphql.Configuration;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.model.ReportListResult;
import com.dummy.wmd.wpc.graphql.service.ReportService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Retrieve report list
 */
@Slf4j
@Component
public class ReportListFetcher implements DataFetcher<ReportListResult> {
    private ReportService reportService;
    private static Integer defaultLimit = Configuration.getDefaultLimit();

    public ReportListFetcher(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public ReportListResult get(DataFetchingEnvironment environment) throws JsonProcessingException {
        // ctryRecCde: String!, grpMembrRecCde: String!, reportCode: String, dateFrom: Date, dateTo: Date
        String ctryRecCde = environment.getArgument(Field.ctryRecCde);
        String grpMembrRecCde = environment.getArgument(Field.grpMembrRecCde);
        String reportCode = environment.getArgument("reportCode");
        LocalDate dateFrom = environment.getArgument("dateFrom");
        LocalDate dateTo = environment.getArgument("dateTo");

        // sort: JSON, skip: Int = 0, limit: Int
        Map<String, Object> sortMap = environment.getArgument("sort");

        Integer skip = environment.getArgument("skip");
        Integer limit = environment.getArgument("limit");
        if(null == limit) limit = defaultLimit;

        try {
            return reportService.listReport(ctryRecCde + grpMembrRecCde, reportCode, dateFrom, dateTo, sortMap, skip, limit);
        } catch (IOException e) {
            throw new productErrorException(productErrors.RuntimeException, e.getMessage());
        }
    }
}