package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.model.Report;
import com.dummy.wmd.wpc.graphql.model.ReportListResult;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {
    @InjectMocks
    private ReportService reportService;
    private String countryCode = "HK";
    private String groupMember = "HASE";
    private String reportCode = "WRTS";
    private String reportDate = "2023-08-22";

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(reportService, "reportPath", "reportPath");
    }

    @Test
    public void testGetPath_givenArgs_returnsOptional() {
        try {
            URL url = CommonUtils.class.getResource("/files");
            URI uri = url.toURI();
            Path root = Paths.get(uri);
            MockedStatic<Paths> paths = Mockito.mockStatic(Paths.class);
            paths.when(() -> Paths.get(anyString())).thenReturn(root);
            Optional<Path> path = reportService.getPath(countryCode, groupMember, reportCode, reportDate);
            paths.close();
            assertNotNull(path);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPath_givenFileName_throwsException() {
        reportService.getPath("filename/");
    }

    @Test
    public void testGetPath_givenFileName_returnsPath() {
        try {
            ReflectionTestUtils.setField(reportService, "reportPath", "reportPath");
            URL url = CommonUtils.class.getResource("/files");
            URI uri = url.toURI();
            Path root = Paths.get(uri);
            MockedStatic<CommonUtils> commonUtils = Mockito.mockStatic(CommonUtils.class);
            commonUtils.when(() -> CommonUtils.toCanonicalPath(null)).thenReturn(root);
            reportService.getPath("filename");
            commonUtils.close();
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testListReport_GivenArgs_returnsReportListResult() {
        // Setup
        Map<String, Object> sortMap = new HashMap<>();
        sortMap.put("key", 1);
        ReportListResult expectedResult = new ReportListResult();
        expectedResult.setTotal(0);
        expectedResult.setSkip(0);
        expectedResult.setLimit(0);
        Report report = new Report();
        report.setCtryRecCde("ctryRecCde");
        report.setGrpMembrRecCde("grpMembrRecCde");
        report.setFilename("filename");
        report.setReportCode("reportCode");
        report.setExt("ext");
        report.setReportDate(LocalDate.of(2020, 1, 1));
        report.setLastModifiedTime(OffsetDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0), ZoneOffset.UTC));
        report.setSize(0L);
        try {
            URL url = CommonUtils.class.getResource("/files");
            URI uri = url.toURI();
            Path root = Paths.get(uri);
            MockedStatic<Paths> paths = Mockito.mockStatic(Paths.class);
            paths.when(() -> Paths.get(anyString())).thenReturn(root);
            ReportListResult result = reportService.listReport(countryCode + groupMember, reportCode,
                    LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), sortMap, 1, 1);
            paths.close();
            assertNotNull(result);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetComparator_givenFieldAndInt_returnsComparator() throws Exception {
        Method method = reportService.getClass().getDeclaredMethod("getComparator", String.class, int.class);
        method.setAccessible(true);
        Comparator<Report> comparator = (Comparator<Report>) method.invoke(reportService, "ctryRecCde", -1);
        comparator = (Comparator<Report>) method.invoke(reportService, "grpMembrRecCde", 1);
        comparator = (Comparator<Report>) method.invoke(reportService, "filename", 1);
        comparator = (Comparator<Report>) method.invoke(reportService, "reportCode", 1);
        comparator = (Comparator<Report>) method.invoke(reportService, "ext", 1);
        comparator = (Comparator<Report>) method.invoke(reportService, "reportDate", 1);
        comparator = (Comparator<Report>) method.invoke(reportService, "lastModifiedTime", 1);
        comparator = (Comparator<Report>) method.invoke(reportService, "size", 1);
        assertNotNull(comparator);
    }


}
