package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.Report;
import com.dummy.wmd.wpc.graphql.model.ReportListResult;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"java:S107", "java:S1126", "java:S6353", "java:S5852"})
@Slf4j
@Service
public class ReportService {
    @Value("${product.report.path}")
    private String reportPath;

    public Optional<Path> getPath(String countryCode, String groupMember, String reportCode, String reportDate) throws IOException {
        String name = String.format("%s%s%s%s", countryCode, groupMember, reportCode, reportDate);
        Path root = Paths.get(reportPath);
        try (Stream<Path> pathStream = Files.find(root, 1, (p, basicFileAttributes) -> p.getFileName().toString().startsWith(name))){
            return pathStream.findFirst();
        }
    }

    public Path getPath(String filename) {
        if(filename.contains("/") || filename.contains("\\")) {
            throw new IllegalArgumentException("Invalid filename: " + filename);
        }
        return CommonUtils.toCanonicalPath(Paths.get(reportPath, filename));
    }

    /**
     * List reports with given criteria
     *
     * @param entity
     * @param reportCode
     * @param dateFrom
     * @param dateTo
     * @return
     */
    public ReportListResult listReport(String entity, String reportCode, LocalDate dateFrom, LocalDate dateTo,
                                       Map<String, Object> sortMap, Integer skip, Integer limit) throws IOException {
        List<Report> list = new ArrayList<>();
        Path root = Paths.get(reportPath);
        addToReportList(entity, root, list);

        // filter by date range
        List<Report> fullList;
        try(Stream<Report> stream = list.stream()) {
            fullList = stream.filter(r -> {
                if(null == r) return false;
                if(null != reportCode && !Objects.equals(r.getReportCode(), reportCode)) return false;
                if(0 == r.getSize()) return false;  // ignore empty report files
                if(null != dateFrom && r.getReportDate().isBefore(dateFrom)) return false;
                if(null != dateTo && r.getReportDate().isAfter(dateTo)) return false;

                return true;
            }).collect(Collectors.toList());
        }
        // sort the result by lastModifiedTime in DESC order
        sortResult(fullList, sortMap);
        List<Report> pageList = retrivePageResult(fullList, skip, limit);

        ReportListResult listResult = new ReportListResult();
        listResult.setTotal(fullList.size());
        listResult.setSkip(skip);
        listResult.setLimit(limit);
        listResult.setList(pageList);
        return listResult;
    }

    private void addToReportList(String entity, Path root, List<Report> list) throws IOException {
        try(Stream<Path> stream = Files.find(root, 1, (p, attrs) -> p.getFileName().toString().startsWith(entity))){
            stream.forEach(path -> {
                try {
                    if(!Files.isDirectory(path)) {
                        Report report = getReportFile(path);
                        if(null != report) {
                            list.add(report);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error retrieving report file data, file: {}, message: {}", path.getFileName().toString(), e.getMessage());
                }
            });
        }
    }

    /**
     * Handle pagination
     *
     * @param result
     * @param skip
     * @param limit
     * @return
     */
    private List<Report> retrivePageResult(List<Report> result, Integer skip, Integer limit) {
        int from = 0;
        if(null != skip && skip >=0 && skip < result.size()) {
            from = skip;
        }
        int to = result.size();
        if(null != limit && limit != 0 && from + limit <= result.size()) {
            to = from + limit;
        }
        return result.subList(from, to);
    }

    /**
     * sort the result list according to the sort map
     *
     * @param result
     * @param sortMap
     */
    private void sortResult(List<Report> result, Map<String, Object> sortMap) {
        if(null == sortMap || sortMap.size() == 0) {
            return;
        }
        sortMap.forEach((k,v) -> {
            int dir = v instanceof BigInteger ? ((BigInteger) v).intValue() : (Integer) v;
            result.sort(getComparator(k, dir));
        });
    }

    private Comparator<Report> getComparator(String field, int dir) {
        Comparator<Report> comparator;
        switch(field) {
            case Field.ctryRecCde:
                comparator = Comparator.comparing(Report::getCtryRecCde);
                break;
            case Field.grpMembrRecCde:
                comparator = Comparator.comparing(Report::getGrpMembrRecCde);
                break;
            case Field.filename:
                comparator = Comparator.comparing(Report::getFilename);
                break;
            case Field.reportCode:
                comparator = Comparator.comparing(Report::getReportCode);
                break;
            case Field.ext:
                comparator = Comparator.comparing(Report::getExt);
                break;
            case Field.reportDate:
                comparator = Comparator.comparing(Report::getReportDate);
                break;
            case Field.lastModifiedTime:
                comparator = Comparator.comparing(Report::getLastModifiedTime);
                break;
            case Field.size:
                comparator = Comparator.comparing(Report::getSize);
                break;
            default:
                comparator = (o1, o2) -> 0;// in case the field doesn't exist
                break;
        }

        if(dir < 0) {   // -1 DESC, 1 ASC
            return comparator.reversed();
        }
        return comparator;
    }

    private static Pattern pattern = Pattern.compile("(\\w{2})(\\w{4})_?(.+?)_?(\\d{8,})_?([0-9_]+)?\\_?[0-9]*?\\.(\\w+)");

    private Report getReportFile(Path path) throws IOException {
        String filename = path.getFileName().toString();
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            Report report = new Report();
            report.setFilename(filename);
            report.setCtryRecCde(matcher.group(1));
            report.setGrpMembrRecCde(matcher.group(2));
            report.setReportCode(matcher.group(3));
            String date = matcher.group(4);
            if(date.length() > 8) { // no time part
                date = date.substring(0, 8);
            }
            DateTime dt = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(date);
            report.setReportDate(LocalDate.of(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth()));
            report.setExt(matcher.group(6));
            report.setLastModifiedTime(Files.getLastModifiedTime(path).toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime());
            report.setSize(Files.size(path));
            return report;
        } else {
            log.warn("Filename pattern not match, pattern={}, path={}", pattern.toString(), path);
            return null;
        }
    }
}
