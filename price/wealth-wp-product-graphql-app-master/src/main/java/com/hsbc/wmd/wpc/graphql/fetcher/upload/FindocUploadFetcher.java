package com.dummy.wmd.wpc.graphql.fetcher.upload;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.productConstants;
import com.dummy.wmd.wpc.graphql.error.ErrorCode;
import com.dummy.wmd.wpc.graphql.error.Errors;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.model.FileValadationResult;
import com.dummy.wmd.wpc.graphql.service.FileUploadService;
import graphql.GraphQLContext;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
public class FindocUploadFetcher implements DataFetcher<List<FileValadationResult>> {
    private FileUploadService fileUploadService;

    public FindocUploadFetcher(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    public static final Pattern EXCEL_LLIS_PATTERN = Pattern.compile("^(HFIE|SPOMS).*\\.(xlsx|xls|csv)$");
    public static final String EXCEL_MAP_PATTERN = "^(%s%s_MAP.WPCE|MAP.HFIE).*\\.(xlsx|xls|csv)$";
    @Value("${batch.findoc-ingress.path}")
    private String ingressPath;
    @Value("${batch.findoc-ingress.under-entity:false}")
    private Boolean underEntity;

    @Override
    public List<FileValadationResult> get(DataFetchingEnvironment environment) throws IOException {
        List<MultipartFile> fileList = getMultipartFile(environment);
        String uploadType = environment.getArgument(Field.uploadType);
        String ctryRecCde = environment.getArgument(Field.ctryRecCde);
        String grpMembrRecCde = environment.getArgument(Field.grpMembrRecCde);
        String excelMapFormat = String.format(EXCEL_MAP_PATTERN, ctryRecCde, grpMembrRecCde);
        List<FileValadationResult> resultList = new ArrayList<>();

        log.debug("File save path:{}", fileList);
        for (MultipartFile file : fileList) {
            String fileName = fileUploadService.getFilename(file);

            if (!validFinDocFIles(uploadType, excelMapFormat, fileName)) {
                resultList.add(new FileValadationResult(fileName, false, "File name cannot match! uploadType: " + uploadType));
                continue;
            }

            try {
                String ackFileName = removeFileExtension(fileUploadService.getFilename(file))+ ".ack";
                String targetDirectory = Boolean.TRUE.equals(underEntity) ? Paths.get(ingressPath, ctryRecCde + grpMembrRecCde).toString() : ingressPath;

                File targetFile = new File(targetDirectory, fileName);
                File ackFile = new File(targetDirectory, ackFileName);
                if (ackFile.getCanonicalPath().startsWith(targetDirectory)) {
                    ackFile.deleteOnExit();
                    if(!ackFile.createNewFile()) {
                        log.error("The ack file already exists, cannot create again.");
                    }
                }

                if (targetFile.getCanonicalPath().startsWith(targetDirectory)) {
                    file.transferTo(targetFile);
                }
                log.debug("File save path:{}", targetFile.getCanonicalPath());
                resultList.add(new FileValadationResult(fileName, true, ""));
            } catch (IOException e) {
                Errors.log(ErrorCode.OTPSERR_EGQ101, e.getMessage(), e, log);
                resultList.add(new FileValadationResult(fileName, false, e.getMessage()));
            }
        }
        return resultList;
    }

    private boolean validFinDocFIles(String uploadType, String excelMapFormat, String fileName) {
        boolean isValid = false;
        switch (uploadType) {
            case "EXCEL_LIST":
                isValid = EXCEL_LLIS_PATTERN.matcher(fileName).find();
                break;
            case "EXCEL_MAP":
                isValid = Pattern.compile(excelMapFormat).matcher(fileName).find();
                break;
            case "PDF":
                isValid = StringUtils.endsWithIgnoreCase(fileName, ".pdf");
                break;
            default:
                break;
        }
        return isValid;
    }

    private List<MultipartFile> getMultipartFile(DataFetchingEnvironment environment) {
        try {
            GraphQLContext context = environment.getContext();
            ServletWebRequest webRequest = context.get(productConstants.webRequest);
            StandardMultipartHttpServletRequest request = (StandardMultipartHttpServletRequest) webRequest.getRequest();
            if (request.getMultiFileMap().get("file") == null) {
                return Collections.emptyList();
            }
            return request.getMultiFileMap().get("file");
        } catch (Exception e) {
            throw new productErrorException(productErrors.RuntimeException, "Can't retrieve file from the request: " + e.getMessage());
        }
    }

    public static String removeFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if(lastDotIndex > 0) {
            return fileName.substring(0,lastDotIndex);
        }
        return fileName;
    }
}