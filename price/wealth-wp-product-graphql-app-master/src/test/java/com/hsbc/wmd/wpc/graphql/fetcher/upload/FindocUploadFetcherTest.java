package com.dummy.wmd.wpc.graphql.fetcher.upload;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.productConstants;
import com.dummy.wmd.wpc.graphql.service.FileUploadService;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import static graphql.schema.DataFetchingEnvironmentImpl.newDataFetchingEnvironment;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class FindocUploadFetcherTest {

    @InjectMocks
    private FindocUploadFetcher findocUploadFetcher;

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private StandardMultipartHttpServletRequest multiRequest;

    private DataFetchingEnvironment env;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(findocUploadFetcher, "fileUploadService", fileUploadService);
        ReflectionTestUtils.setField(findocUploadFetcher, "ingressPath", "src/test/resources/files");
        ReflectionTestUtils.setField(findocUploadFetcher, "underEntity", false);
    }

    @Test
    public void testGet_givenExcelListFile_returnNotNull() throws IOException {
        env = getDfEnv("EXCEL_LIST");
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/files/HFIE0137.D230227.T170000.xls");
        LinkedMultiValueMap<String, MultipartFile> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.put("file", Collections.singletonList(new MockMultipartFile("HFIE0137.D230227.T170000.xls", fileInputStream)));
        Mockito.when(multiRequest.getMultiFileMap()).thenReturn(multiValueMap);
        Mockito.when(fileUploadService.getFilename(any(MultipartFile.class))).thenReturn("HFIE0137.D230227.T170000.xls");
        Assert.assertNotNull(findocUploadFetcher.get(env));
    }

    @Test
    public void testGet_givenExcelMapFile_returnNotNull() throws IOException {
        env = getDfEnv("EXCEL_MAP");
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/files/MAP.HFIE0108.D220525.T210000.xls");
        LinkedMultiValueMap<String, MultipartFile> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.put("file", Collections.singletonList(new MockMultipartFile("MAP.HFIE0108.D220525.T210000.xls", fileInputStream)));
        Mockito.when(multiRequest.getMultiFileMap()).thenReturn(multiValueMap);
        Mockito.when(fileUploadService.getFilename(any(MultipartFile.class))).thenReturn("MAP.HFIE0108.D220525.T210000.xls");
        Assert.assertNotNull(findocUploadFetcher.get(env));
    }

    @Test
    public void testGet_givenPdfFile_returnNotNull() throws IOException {
        env = getDfEnv("PDF");
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/files/ut_fs_U5000_en.pdf");
        LinkedMultiValueMap<String, MultipartFile> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.put("file", Collections.singletonList(new MockMultipartFile("ut_fs_U5000_en.pdf", fileInputStream)));
        Mockito.when(multiRequest.getMultiFileMap()).thenReturn(multiValueMap);
        Mockito.when(fileUploadService.getFilename(any(MultipartFile.class))).thenReturn("ut_fs_U5000_en.pdf");
        Assert.assertNotNull(findocUploadFetcher.get(env));
    }

    @Test
    public void testGet_givenOtherFile_returnNotNull() throws IOException {
        env = getDfEnv("UT");
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/files/UT-40004996.json");
        LinkedMultiValueMap<String, MultipartFile> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.put("file", Collections.singletonList(new MockMultipartFile("UT-40004996.json", fileInputStream)));
        Mockito.when(multiRequest.getMultiFileMap()).thenReturn(multiValueMap);
        Mockito.when(fileUploadService.getFilename(any(MultipartFile.class))).thenReturn("UT-40004996.json");
        Assert.assertNotNull(findocUploadFetcher.get(env));
    }

    @Test
    public void testGet_noFile() throws IOException {
        env = getDfEnv("UT");
        Mockito.when(multiRequest.getMultiFileMap()).thenReturn(new LinkedMultiValueMap<>());
        Assert.assertNotNull(findocUploadFetcher.get(env));
    }

    private DataFetchingEnvironment getDfEnv(String uploadType) {
        HashMap<String, Object> arg = new HashMap<>();
        arg.put(Field.uploadType, uploadType);
        arg.put(Field.ctryRecCde, "HK");
        arg.put(Field.grpMembrRecCde, "HBAP");

        HttpServletResponse response = mock(HttpServletResponse.class);
        GraphQLContext graphQLContext = GraphQLContext.newContext().of(productConstants.webRequest, new ServletWebRequest(multiRequest, response)).build();
        return newDataFetchingEnvironment()
                .context(graphQLContext)
                .arguments(arg)
                .build();

    }
}
