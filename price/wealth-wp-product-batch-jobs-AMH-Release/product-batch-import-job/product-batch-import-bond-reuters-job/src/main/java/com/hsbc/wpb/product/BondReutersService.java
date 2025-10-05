package com.dummy.wpb.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.helper.DateHelper;
import com.dummy.wpb.product.model.ExcelColumnInfo;
import com.dummy.wpb.product.error.ErrorCode;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import com.dummy.wpb.product.utils.YamlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.*;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dummy.wpb.product.ImportReutersBondJobApplication.JOB_NAME;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class BondReutersService {

    @Value("${BOND.REUTERS.LOCALPATH}")
    private String wpcLocalPath;

    @Value("${BOND.REUTERS.FILENAME}")
    private String fileName = null;

    @Value("${BOND.REUTERS.USERNAME}")
    private String username = null;

    @Value("${BOND.REUTERS.PASSWORD}")
    private String password = null;

    @Value("${BOND.REUTERS.PROXYHOST}")
    private String proxyHost = null;

    @Value("${BOND.REUTERS.PROXYPORT}")
    private int proxyPort = 0;

    @Value("${BOND.REUTERS.CONFIGFILE}")
    private String configFilePath;

    @Autowired
    private RestTemplate bondRestTemplate;

    private static final String HOST = "https://selectapi.datascope.refinitiv.com/RestApi/v1/";

    private static final String TOKEN_URL = "Authentication/RequestToken";

    private static final String SCHEDULE_ID_URL = "Extractions/ScheduleGetByName";

    private static final String SCHEDULE_NAME = "AMH%20WPC%20Reuters%20Schedule";

    private static final String REPORT_EXTRACTION_ID_URL = "Extractions/ReportExtractionGetCompletedByDateRangeByScheduleId";

    private static final String DOWNLOAD_REPORT_URL = "Extractions/ReportExtractions";

    private static final String DELIMITER = ",";

    private static final String BEFORE_REPLACE_DELIMITER = ",,";

    private static final String REPLACE_DELIMITER = ", ,";

    private static final Pattern CSV_READER_PATTERN = Pattern.compile("\"([^\"]+?)\",?|([^,]+),?|,");

    protected static final String EMPTY_STR = "";

    /**
     * Run job
     *
     * @throws IOException
     */
    public List<List<ExcelColumnInfo>> run() throws productBatchException {
        log.info("WPC Batch download TR file Start.");

        String token = getRequestToken();
        if (null == token) {
            throw new productBatchException(ErrorCode.OTPSERR_EBJ101 + " " + JOB_NAME + ": " + "Can not get request token.");
        }

        // add perfix
        token = "Token " + token;
        log.debug("auth token:" + token);

        String scheduleId = getScheduleId(token);
        if (null == scheduleId) {
            throw new productBatchException(ErrorCode.OTPSERR_EBJ101 + " " + JOB_NAME + ": " + "Can not get schedule Id.");
        }
        log.info("scheduleId:" + scheduleId);

        String reportExtractionId = getReportExtractionId(scheduleId, token);
        if (null == reportExtractionId) {
            throw new productBatchException(ErrorCode.OTPSERR_EBJ101 + " " + JOB_NAME + ": " + "Can not get report extraction Id.");
        }
        log.info("reportExtractionId:" + reportExtractionId);

        // download file
        List<List<ExcelColumnInfo>> reader = null;
        try {
            reader = downloadReport(reportExtractionId, token);
        } catch (Exception e) {
            throw new productBatchException(ErrorCode.OTPSERR_EBJ101 + " " + JOB_NAME + ": " + "Failed to download, cause: " + e.getMessage());
        }
        log.info("WPC Batch download TR file Run Completed.");
        return reader;
    }

    private String getRequestToken() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("Username", username);
        credentials.put("Password", password);

        RequestEntity<Map<String, Object>> requestEntity = RequestEntity
                .post(HOST + TOKEN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Collections.singletonMap("Credentials", credentials));

        ResponseEntity<Map<String, Object>> responseEntity = bondRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {
        });

        if (hasSuccessBody(responseEntity)) {
            return MapUtils.getString(responseEntity.getBody(), "value", null);
        } else {
            log.error("TR file token error");
            return null;
        }
    }

    private String getScheduleId(String token) {
        URI uri = URI.create(HOST + SCHEDULE_ID_URL + "(ScheduleName='" + SCHEDULE_NAME + "')");

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, token);

        ResponseEntity<Map<String, Object>> responseEntity = bondRestTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        if (hasSuccessBody(responseEntity)) {
            log.debug("GetScheduleId Response: {}", responseEntity.getBody());
            return MapUtils.getString(responseEntity.getBody(), "ScheduleId", null);
        } else {
            log.error("TR file ScheduleId error");
            return null;
        }
    }

    private String getReportExtractionId(String scheduleId, String token) {
        Date curDate = DateHelper.getCurrentDate();
        String startDate = DateFormatUtils.formatUTC(DateUtils.addDays(curDate, -1), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String endDate = DateFormatUtils.formatUTC(curDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String uri = HOST + REPORT_EXTRACTION_ID_URL + "(ScheduleId='" + scheduleId + "',StartDate=" + startDate + ",EndDate=" + endDate + ")";

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, token);
        ResponseEntity<Map<String, Object>> responseEntity = bondRestTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        if (hasSuccessBody(responseEntity)) {
            Map<String, Object> resultMap = responseEntity.getBody();
            log.debug("GetScheduleId Response:" + resultMap);
            return JsonPathUtils.readValue(resultMap, "value[0].ReportExtractionId");
        } else {
            log.error("TR file ReportExtractionId error");
            return null;
        }
    }

    private List<List<ExcelColumnInfo>> downloadReport(String reportExtractionId, String token) throws Exception {

        String uri = HOST + DOWNLOAD_REPORT_URL + "('" + reportExtractionId + "')/$value";
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, token);
        ResponseEntity<Resource> responseEntity = bondRestTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Resource.class);

        List<List<ExcelColumnInfo>> rowInfoList = new LinkedList<>();

        Resource resource = responseEntity.getBody();
        if (hasSuccessBody(responseEntity) && null != resource) {
            //Load column info
            List<ExcelColumnInfo> excelColumnInfoList = getExcelColumnInfoList();

            //Load response value
            String dateStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss");
            String filePath = wpcLocalPath + File.separator + fileName + "_" + dateStr + ".bak";

            try (InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {

                String line;
                int inrecCnt = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    inrecCnt++;

                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                    if (inrecCnt == 1) {
                        continue; //skip the header line
                    }

                    List<ExcelColumnInfo> rowInfo = JsonUtil.deepCopy(excelColumnInfoList, new TypeReference<List<ExcelColumnInfo>>() {
                    });
                    if (convertRowInfo(rowInfo, line)) {
                        rowInfoList.add(rowInfo);
                    } else {
                        log.error("Skip current bond: (row number: " + inrecCnt
                                + "). Because cell number each row != header column number.");
                    }
                }
            }
        } else {
            log.error("TR file download error.");

        }
        return rowInfoList;
    }

    private boolean convertRowInfo(List<ExcelColumnInfo> rowInfo, String line) {
        String[] cells = spiltRowToCells(line);
        if (rowInfo.size() == cells.length) {
            for (int i = 0; i < cells.length; i++) {
                String value = cells[i];
                rowInfo.get(i).setValue(value != null ? value.trim() : EMPTY_STR);
            }
            return true;
        } else {
            return false;
        }
    }

    private String[] spiltRowToCells(String content) {
        //if last column value is blank(content end with ,), append blank after content
        if (content.endsWith(DELIMITER)) {
            content = content + " ";
        }
        String rowContent = StringUtils.replace(content, BEFORE_REPLACE_DELIMITER, REPLACE_DELIMITER);
        List<String> columnValues = new ArrayList<>();
        //handle currency in cell, ex "16,830,671,000", need change to 16,830,671,000,
        //before put them into BigDecimal need remove comma

        Matcher matcher = CSV_READER_PATTERN.matcher(rowContent);
        // For each field
        while (matcher.find()) {
            String cellValue = matcher.group();
            if (cellValue == null) {
                break;
            }
            if (cellValue.endsWith(",")) { // trim trailing ,
                cellValue = cellValue.substring(0, cellValue.length() - 1);
            }
            if (cellValue.startsWith("\"")) { // assume also ends with
                cellValue = cellValue.substring(1, cellValue.length() - 1);
            }
            if (cellValue.length() == 0) {
                cellValue = null;
            }
            columnValues.add(StringUtils.stripToNull(cellValue));
        }

        return columnValues.toArray(new String[0]);
    }

    protected List<ExcelColumnInfo> getExcelColumnInfoList() {
        return new LinkedList<>(YamlUtils.readResource(configFilePath, new TypeReference<List<ExcelColumnInfo>>() {
        }));
    }


    private boolean hasSuccessBody(ResponseEntity<?> responseEntity) {
        return responseEntity.getStatusCodeValue() == HttpStatus.SC_OK && responseEntity.getBody() != null;
    }
}
