package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.model.ReportListResult;
import com.dummy.wmd.wpc.graphql.service.ReportService;
import graphql.schema.DataFetchingEnvironment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportListFetcherTest {

    @InjectMocks
    private ReportListFetcher reportListFetcher;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private ReportService reportService;

    @Before
    public void setUp() {
        reportListFetcher = new ReportListFetcher(reportService);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsReportListResult() throws Exception {
        Mockito.when(environment.getArgument(anyString())).thenReturn("ctryRecCde").
                thenReturn("grpMembrRecCde").thenReturn("reportCode").
                thenReturn(LocalDate.now()).thenReturn(LocalDate.now()).
                thenReturn(new HashMap<>()).thenReturn(1).thenReturn(null);
        Mockito.when(reportService.listReport(anyString(),anyString(),
                any(LocalDate.class), any(LocalDate.class),
                anyMap(), any(Integer.class), any(Integer.class))).thenReturn(new ReportListResult());
        ReportListResult reportListResult = reportListFetcher.get(environment);
        Assert.assertNotNull(reportListResult);
    }
}
