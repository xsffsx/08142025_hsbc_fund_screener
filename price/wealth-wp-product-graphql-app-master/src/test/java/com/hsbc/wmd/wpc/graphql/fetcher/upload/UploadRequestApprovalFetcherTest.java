package com.dummy.wmd.wpc.graphql.fetcher.upload;

import com.dummy.wmd.wpc.graphql.constant.productConstants;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import com.dummy.wmd.wpc.graphql.service.FileUploadService;
import graphql.Assert;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class UploadRequestApprovalFetcherTest {

    @InjectMocks
    private UploadRequestApprovalFetcher uploadRequestApprovalFetcher;
    @Mock
    private FileUploadService fileUploadService;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private GraphQLContext context;
    @Mock
    private ServletWebRequest webRequest;
    @Mock
    private StandardMultipartHttpServletRequest request;
    @Mock
    private MultiValueMap<String, MultipartFile> multipartFileMultiValueMap;
    @Mock
    private List<MultipartFile> multipartFiles;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private UserInfo userInfo;

    @Before
    public void setUp() {
        uploadRequestApprovalFetcher = new UploadRequestApprovalFetcher(fileUploadService);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDocument() {
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"FINDOC", "HKHASE_MAP.WPCE1.D123456.T123456.txt"});
        list.add(new String[]{"ILI_UT", "HKHASE_ILI-UT_1.txt"});
        list.add(new String[]{"MSUT", "HK_HASE_MSUT_1.txt"});
        for (String[] arr : list) {
            prepareMockData(arr[0], arr[1], multipartFiles, false);
            Document document = uploadRequestApprovalFetcher.get(environment);
            Assert.assertNotNull(document);
        }
    }

    @Test(expected = productErrorException.class)
    public void testGet_givenDataFetchingEnvironment_throwException1() {
        prepareMockData("A", "ABC.txt", multipartFiles, false);
        uploadRequestApprovalFetcher.get(environment);
    }

    @Test(expected = productErrorException.class)
    public void testGet_givenDataFetchingEnvironment_throwException2() {
        prepareMockData("A", "ABC.txt", null, false);
        uploadRequestApprovalFetcher.get(environment);
    }

    @Test(expected = productErrorException.class)
    public void testGet_givenDataFetchingEnvironment_throwException3() {
        prepareMockData("A", "ABC.txt", null, true);
        uploadRequestApprovalFetcher.get(environment);
    }

    private void prepareMockData(String uploadType, String fileName, List<MultipartFile> list, Boolean hasError) {
        Mockito.when(environment.getArgument(anyString())).thenReturn("HK").thenReturn("HASE").thenReturn(uploadType).thenReturn("comment");
        Mockito.when(environment.getContext()).thenReturn(context);
        if(hasError) {
            Mockito.when(context.get(productConstants.webRequest)).thenThrow(productErrorException.class);
        }else {
            Mockito.when(context.get(productConstants.webRequest)).thenReturn(webRequest);
        }
        Mockito.when(webRequest.getRequest()).thenReturn(request);
        Mockito.when(request.getMultiFileMap()).thenReturn(multipartFileMultiValueMap);
        Mockito.when(multipartFileMultiValueMap.get("file")).thenReturn(list);
        Mockito.when(multipartFiles.get(0)).thenReturn(multipartFile);
        Mockito.when(fileUploadService.getFilename(any(MultipartFile.class))).thenReturn(fileName);
        Mockito.when(context.get(productConstants.userInfo)).thenReturn(userInfo);
        Mockito.when(fileUploadService.uploadRequestApproval(any(), any(), any(), any(),
                any(), any())).thenReturn(new Document());
    }
}
