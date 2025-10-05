package com.dummy.wmd.wpc.graphql.fetcher.upload;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.productConstants;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import com.dummy.wmd.wpc.graphql.service.FileUploadService;
import graphql.GraphQLContext;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

@Slf4j
@Component
public class UploadRequestApprovalFetcher implements DataFetcher<Document> {
    private FileUploadService fileUploadService;

    public UploadRequestApprovalFetcher(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @Override
    public Document get(DataFetchingEnvironment environment) {
        String ctryRecCde = environment.getArgument(Field.ctryRecCde);
        String grpMembrRecCde = environment.getArgument(Field.grpMembrRecCde);
        String uploadType = environment.getArgument(Field.uploadType);
        String comments = environment.getArgument("comments");
        String pattern = String.format("%s%s_?%s_?\\d+\\.\\w+", ctryRecCde, grpMembrRecCde, uploadType);
        //special for Financial Document file
        if ("FINDOC".equals(uploadType)) {
            pattern = String.format("%s%s_MAP.WPCE\\d+\\.D\\d{6}\\.T\\d{6}\\.\\w+", ctryRecCde, grpMembrRecCde);
        }
        //special for UploadFileType cannot support symbol '-' in "ILI-UT"
        if ("ILI_UT".equals(uploadType)) {
            pattern = String.format("%s%s_?%s_?\\d+\\.\\w+", ctryRecCde, grpMembrRecCde, "ILI-UT");
        }
        if ("MSUT".equals(uploadType)) {
            pattern = String.format("%s_%s_?%s_?\\d+\\.\\w+", ctryRecCde, grpMembrRecCde, "MSUT");
        }
        MultipartFile file = getMultipartFile(environment);
        // validate the filename against uploadType here, the file name should be <ctryRecCde><grpMembrRecCde><uploadType><timestamp>.<ext>
        if(null == file) {
            String message = "Upload file is missing";
            throw new productErrorException(productErrors.UploadError, message);
        } else {
            String filename = fileUploadService.getFilename(file);
            if (!filename.matches(pattern)) {
                log.warn("Upload filename not match with expected pattern [{}]: {}", pattern, filename);
                String message = String.format("Upload filename not match with expected pattern: %s", filename);
                throw new productErrorException(productErrors.UploadError, message);
            }
        }
        UserInfo userInfo = ((GraphQLContext) environment.getContext()).get(productConstants.userInfo);
        String emplyNum = null == userInfo? "": userInfo.getId();

        return fileUploadService.uploadRequestApproval(ctryRecCde, grpMembrRecCde, emplyNum, uploadType, file, comments);
    }

    private MultipartFile getMultipartFile(DataFetchingEnvironment environment) {
        try {
            GraphQLContext context = environment.getContext();
            ServletWebRequest webRequest = context.get(productConstants.webRequest);
            StandardMultipartHttpServletRequest request = (StandardMultipartHttpServletRequest) webRequest.getRequest();
            if (request.getMultiFileMap().get("file") == null) {
                return null;
            }
            return request.getMultiFileMap().get("file").get(0);
        } catch (Exception e) {
            throw new productErrorException(productErrors.RuntimeException, "Can't retrieve file from the request: " + e.getMessage());
        }
    }
}