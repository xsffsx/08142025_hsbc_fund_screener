package com.dummy.wmd.wpc.graphql;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.ErrorCode;
import com.dummy.wmd.wpc.graphql.error.Errors;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import com.dummy.wmd.wpc.graphql.service.*;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.utils.CheckmarxUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings({"java:S3752", "java:S5122"})
@Slf4j
@RestController
public class productRestController {
    @Autowired
    private productConfig productConfig;

    @Autowired
    private LdapService ldapService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private LogFileService logFileService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FinDocFileService finDocFileService;

    @Autowired
    private FileUploadService fileUploadService;

    private static final String CONTENT_HEADER = "Content-Disposition";
    private static final String ATTACHMENT = "attachment;filename=";

    @RequestMapping("/echo-headers")
    public Map<String, Object> getHeaders(HttpServletRequest request){
        log.info("Role mapping config: {}", productConfig.getRoleMapping());
        Map<String, Object> headers = new LinkedHashMap<>();
        Enumeration<String> enum1 = request.getHeaderNames();
        while(enum1.hasMoreElements()){
            String name = enum1.nextElement();
            List<String> list = new ArrayList<>();
            Enumeration<String> enum2 = request.getHeaders(name);
            while(enum2.hasMoreElements()){
                list.add(enum2.nextElement());
            }
            headers.put(name, list);
        }
        return headers;
    }

    @SneakyThrows
    @RequestMapping("/getLdapUser")
    public UserInfo getLdapUser(String staffId) {
    	return ldapService.getLdapUserById(staffId);
    }

    /**
     * Retrieve report file with given parameters
     *
     * @param filename
     * @param response
     */
    @GetMapping(value = "/report/{filename}")
    public void retrieveReport(@PathVariable("filename") String filename, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            filename = CheckmarxUtils.preventHTTPResponseSplitting(filename);
            Path path = reportService.getPath(filename);
            response.setHeader(CONTENT_HEADER, ATTACHMENT + path.getFileName());
            Files.copy(path, out);
        } catch (FileNotFoundException e) {
            response.setStatus(404);
        } catch (IOException e) {
            response.setStatus(500);
            Errors.log(ErrorCode.OTPSERR_EGQ101, e.getMessage(), e, log);
        }
    }

    /**
     * Retrieve upload log file with given parameters
     *
     * @param filename
     * @param response
     */
    @GetMapping(value = "/log/{filename}")
    public void retrieveLogFile(@PathVariable("filename") String filename, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            filename = CheckmarxUtils.preventHTTPResponseSplitting(filename);
            Path path = logFileService.getPath(filename);
            response.setHeader(CONTENT_HEADER, ATTACHMENT + path.getFileName());
            Files.copy(path, out);
        } catch (FileNotFoundException e) {
            response.setStatus(404);
        } catch (IOException e) {
            response.setStatus(500);
            Errors.log(ErrorCode.OTPSERR_EGQ101, e.getMessage(), e, log);
        }
    }

    /**
     * Retrieve report file with given parameters
     *
     * @param countryCode
     * @param groupMember
     * @param reportCode
     * @param reportDate
     * @param response
     */
    @GetMapping(value = "/report")
    public void retrieveReport(@RequestParam("countryCode") String countryCode,
                               @RequestParam("groupMember") String groupMember,
                               @RequestParam("reportCode") String reportCode,
                               @RequestParam("reportDate") String reportDate,
                               HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            Optional<Path> oPath = reportService.getPath(countryCode, groupMember, reportCode, reportDate);
            if(!oPath.isPresent()){
                response.setStatus(404);
            } else {
                Path path = CommonUtils.toCanonicalPath(oPath.get());
                response.setHeader(CONTENT_HEADER, ATTACHMENT + path.getFileName());
                Files.copy(path, out);
            }
        } catch (FileNotFoundException e) {
            response.setStatus(404);
        } catch (IOException e) {
            response.setStatus(500);
            Errors.log(ErrorCode.OTPSERR_EGQ101, e.getMessage(), e, log);
        }
    }

    @GetMapping(value = "/files/{fileMd5}")
    public void viewFile(@PathVariable("fileMd5") String fileMd5, HttpServletResponse response) {
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        fileMd5 = CheckmarxUtils.preventHTTPResponseSplitting(fileMd5);
        fileMd5 = CheckmarxUtils.preventCGIReflectedXSSAllClients(fileMd5);
        Document fileDoc = fileUploadService.getUploadFile(fileMd5);
        if(null == fileDoc) {
            response.setStatus(404);
            return;
        }

        String fileName = (String) fileDoc.get(Field.fileName);
        response.setHeader(CONTENT_HEADER, ATTACHMENT + fileName);
        try (OutputStream out = response.getOutputStream()) {
            fileService.retrieveFile(fileMd5, out);
        } catch (IOException e) {
            response.setStatus(500);
            Errors.log(ErrorCode.OTPSERR_EGQ101, e.getMessage(), e, log);
        }
    }

    /**
     * Retrieve fin doc file with given parameters
     *
     * @param filename
     * @param response
     */
    @GetMapping(value = "/finDoc/{filename}")
    @CrossOrigin
    public void retrieveFinDocFile(@PathVariable("filename") String filename, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            filename = CheckmarxUtils.preventHTTPResponseSplitting(filename);
            Path path = finDocFileService.getPath(filename);
            response.setHeader(CONTENT_HEADER, "inline;filename=" + path.getFileName());
            response.setContentType("application/pdf");
            response.setHeader("X-Frame-Options","SAMEORIGIN");
            Files.copy(path, out);
        } catch (FileNotFoundException e) {
            response.setStatus(404);
        } catch (IOException e) {
            response.setStatus(500);
            Errors.log(ErrorCode.OTPSERR_EGQ101, e.getMessage(), e, log);
        }
    }
}
