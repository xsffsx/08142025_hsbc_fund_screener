package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.exception.RecordNotFoundException;
import com.dummy.wpb.product.model.FinDocHistPo;
import com.dummy.wpb.product.model.FinDocPo;
import com.dummy.wpb.product.model.FinDocULReqPo;
import com.dummy.wpb.product.model.SystemParmPo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.dummy.wpb.product.component.PDFFileServiceTest.createFinDocULReqPo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class FinDocReleaseServiceTest {

    @Mock
    private FinDocReleaseDao finDocReleaseDao;

    @InjectMocks
    @Spy
    private FinDocReleaseService finDocReleaseService;

    protected InputStream inputStream;


    @BeforeEach
    void setUp() throws FileNotFoundException {

        MockitoAnnotations.openMocks(this);
        inputStream = new FileInputStream("src/test/resources/test/test.txt");
    }


    @Test
    void retrieveFinDocSmryRecordByStatusRelease_Should_CallDaoAndReturnResult() {
        // Given
        String ctryCde = "CTRY";
        String orgnCde = "ORGN";
        FinDocHistPo[] expected = new FinDocHistPo[]{new FinDocHistPo()};

        when(finDocReleaseDao.retrieveFinDocSmryRecordByStatusFS(ctryCde, orgnCde, FinDocConstants.PENDING))
                .thenReturn(expected);

        // When
        FinDocHistPo[] result = finDocReleaseService.retrievePendingRecord(ctryCde, orgnCde);

        // Then
        assertEquals(1, result.length);
    }

    @Test
    void retrieveFinDocSmryRecordByStatusRelease_() throws IOException {
        String pdfChkPath = new ClassPathResource("/").getFile().getAbsolutePath() + "/findoc/pdf/chk";
        String pdfRejPath = new ClassPathResource("/").getFile().getAbsolutePath() + "/findoc/pdf/rej";

        ReflectionTestUtils.setField(finDocReleaseService, "pdfChkPath", pdfChkPath);
        ReflectionTestUtils.setField(finDocReleaseService, "pdfRejPath", pdfRejPath);


        // Given
        String ctryCde = "XXX";
        String orgnCde = "ORGN";
        FinDocULReqPo finDocULReqPo = new FinDocULReqPo();
        finDocULReqPo.setRecCreatDtTm(new Date());
        finDocULReqPo.setDocRecvName(ctryCde);
        FinDocULReqPo[] expected = new FinDocULReqPo[]{finDocULReqPo};

        when(finDocReleaseDao.retrieveByStatCde(ctryCde, orgnCde, FinDocConstants.PROC_REJECT))
                .thenReturn(expected);


        Assertions.assertDoesNotThrow(() -> {
            finDocReleaseService.aprvRecHandle(ctryCde, orgnCde);
        });
    }


    @Test
    void procAprvHandleTest() throws IOException, RecordNotFoundException {
        String pdfChkPath = new ClassPathResource("/").getFile().getAbsolutePath() + "/findoc/pdf/chk";
        String pdfAprvPath = new ClassPathResource("/").getFile().getAbsolutePath() + "/findoc/pdf/aprv";

        ReflectionTestUtils.setField(finDocReleaseService, "pdfChkPath", pdfChkPath);
        ReflectionTestUtils.setField(finDocReleaseService, "pdfAprvPath", pdfAprvPath);


        // Given
        String ctryCde = "pdf";
        String orgnCde = "ORGN";
        FinDocHistPo finDocHistPo = new FinDocHistPo();
        finDocHistPo.setRecCreatDtTm(new Date());
        finDocHistPo.setDocRecvName(ctryCde);
        finDocHistPo.setRsrcItemIdFinDoc(167l);
        finDocHistPo.setUrlLclSysText(orgnCde);
        FinDocHistPo[] expected = new FinDocHistPo[]{finDocHistPo};

        when(finDocReleaseDao.retrieveFinDocSmryRecordByStatCde(ctryCde, orgnCde, FinDocConstants.PROC_APPROVAL))
                .thenReturn(expected);


        when(finDocReleaseDao.retrieveByDocSerNum(expected[0]))
                .thenReturn(createFinDocULReqPo());


        Assertions.assertDoesNotThrow(() -> {
            finDocReleaseService.procAprvHandle(ctryCde, orgnCde);
        });
    }

    @Test
    void retrieveUploadReqTOWithException(){
        FinDocHistPo finDocHistPo = new FinDocHistPo();

        RecordNotFoundException recordNotFoundException = new RecordNotFoundException("Record not found: " + finDocHistPo.getCtryRecCde() + ":" + finDocHistPo.getGrpMembrRecCde()
                        + ":" + finDocHistPo.getDocFinTypeCde() + ":" + finDocHistPo.getDocFinCatCde() + ":"
                        + finDocHistPo.getDocFinCde() + ":" + finDocHistPo.getRsrcItemIdFinDoc());
        when(finDocReleaseDao.retrieveByDocSerNum(finDocHistPo))
                .thenThrow(recordNotFoundException);

        assertThrows(productBatchException.class, () -> {
            finDocReleaseService.retrieveUploadReqTO(finDocHistPo);
        });

    }


    @Test
    void updateUploadReqTOWithException(){
        FinDocULReqPo finDocULReqPo = new FinDocULReqPo();

        doThrow(new RecordNotFoundException("Record not found: " + finDocULReqPo.getCtryRecCde() + ":"
                + finDocULReqPo.getGrpMembrRecCde() + ":" + finDocULReqPo.getFileRqstName() + ":"
                + finDocULReqPo.getDocUpldSeqNum())).when(finDocReleaseDao).update(finDocULReqPo);

        assertThrows(productBatchException.class, () -> {
            finDocReleaseService.updateUploadReqTO(finDocULReqPo);
        });
    }

    @Test
    void testGetFSURL4NextRetrieveFinDocSysPramByProdType() {
        FinDocHistPo fds = createFindDocHistPo();

        SystemParmPo sps = new SystemParmPo();
        sps.setParmValueText("1215");

        Mockito.when(finDocReleaseDao
                        .retrieveFinDocSysPramByProdType(
                                fds.getCtryRecCde(),
                                fds.getGrpMembrRecCde(),
                                fds.getDocFinTypeCde(),
                                fds.getDocFinCatCde(),
                                FinDocConstants.FSURL))
                .thenReturn(null);
        Mockito.when(finDocReleaseDao
                        .retrieveFinDocSysPramByProdType(
                                fds.getCtryRecCde(),
                                fds.getGrpMembrRecCde(),
                                fds.getDocFinTypeCde(),
                                FinDocConstants.SUBTYPCDE_GN,
                                FinDocConstants.FSURL))
                .thenReturn(sps);

        finDocReleaseService.finDocReleaseDao = finDocReleaseDao;
        String fsurl = finDocReleaseService.getFSURL(fds);

        Mockito.verify(finDocReleaseDao, times(1))
                .retrieveFinDocSysPramByProdType(
                        fds.getCtryRecCde(),
                        fds.getGrpMembrRecCde(),
                        fds.getDocFinTypeCde(),
                        fds.getDocFinCatCde(),
                        FinDocConstants.FSURL);
        Mockito.verify(finDocReleaseDao, times(1))
                .retrieveFinDocSysPramByProdType(
                        fds.getCtryRecCde(),
                        fds.getGrpMembrRecCde(),
                        fds.getDocFinTypeCde(),
                        FinDocConstants.SUBTYPCDE_GN,
                        FinDocConstants.FSURL);

    }

    @Test
    void testUpdateFinDocSmry4Update() {

        FinDocHistPo fds = createFindDocHistPo();

        FinDocPo finDocPo = new FinDocPo();
        finDocPo.setRsrcItemIdFinDoc(12345L);
        Mockito.doNothing().when(finDocReleaseDao).update(fds);
        Mockito.when(finDocReleaseDao
                        .retrieveFinDocSmryRecordByDocCdeNLangCdeLatest(fds))
                .thenReturn(finDocPo);
        Mockito.doNothing().when(finDocReleaseDao).updateLatest(fds);
        Mockito.doNothing().when(finDocReleaseDao)
                .updateProdRLByDocSerNum(anyLong(), anyLong());
        finDocReleaseService.finDocReleaseDao = finDocReleaseDao;
        finDocReleaseService.updateFinDocSmryAndProdFinDoc(fds);

        Mockito.verify(finDocReleaseDao, times(1)).update(fds);
        Mockito.verify(finDocReleaseDao, times(1))
                .retrieveFinDocSmryRecordByDocCdeNLangCdeLatest(fds);
        Mockito.verify(finDocReleaseDao, times(1)).updateLatest(fds);
        Mockito.verify(finDocReleaseDao, times(1)).updateProdRLByDocSerNum(anyLong(), anyLong());
    }

    @Test
    void testUpdateFinDocSmry4Insert() throws IOException {
        FinDocHistPo fds = createFindDocHistPo();
        FinDocULReqPo fdu = new FinDocULReqPo();

        String pdfAprvPath = new ClassPathResource("/").getFile().getAbsolutePath() + "/findoc/pdf/aprv";
        String pdfDonePath = new ClassPathResource("/").getFile().getAbsolutePath() + "/findoc/pdf/done";

        ReflectionTestUtils.setField(finDocReleaseService, "pdfAprvPath", pdfAprvPath);
        ReflectionTestUtils.setField(finDocReleaseService, "pdfDonePath", pdfDonePath);
        ReflectionTestUtils.setField(finDocReleaseService, "dfltFsUrl", "http://localhost");

        Mockito.doNothing().when(finDocReleaseDao).update(fds);
        Mockito.when(finDocReleaseDao
                        .retrieveFinDocSmryRecordByDocCdeNLangCdeLatest(fds))
                .thenReturn(null);
        Mockito.when(finDocReleaseDao.retrieveByDocSerNum(fds))
                        .thenReturn(fdu);
        Mockito.doNothing().when(finDocReleaseDao).insertLatest(fds);
        finDocReleaseService.finDocReleaseDao = finDocReleaseDao;
        finDocReleaseService.updateDB(fds);

        Mockito.verify(finDocReleaseDao, times(1)).update(fds);
        Mockito.verify(finDocReleaseDao, times(1)).insertLatest(fds);
    }

    @Test
    void testRetrieveUploadReqTO(){
        FinDocHistPo fds = createFindDocHistPo();

        FinDocULReqPo finDocULReqPo = mock(FinDocULReqPo.class);
        Mockito.when(finDocReleaseDao.retrieveByDocSerNum(fds)).thenReturn(finDocULReqPo);
        finDocReleaseService.finDocReleaseDao = finDocReleaseDao;
        FinDocULReqPo finDocULReqPoResult = finDocReleaseService.retrieveUploadReqTO(fds);
        Mockito.verify(finDocReleaseDao, times(1)).retrieveByDocSerNum(fds);
    }


    private FinDocHistPo createFindDocHistPo() {
        FinDocHistPo fds = new FinDocHistPo();
        fds.setCtryRecCde("HK");
        fds.setGrpMembrRecCde("HBAP");
        fds.setDocFinTypeCde("FACTSHEET");
        fds.setDocFinCatCde("UT");
        fds.setDocFinCde("62815");
        fds.setLangFinDocCde("EN");
        fds.setRsrcItemIdFinDoc(800213L);
        fds.setDocRecvName("ut_fs_62815_en.pdf");
        fds.setUrlLclSysText("apr/00000000000800213.ut_fs_62815_en.pdf");
        return fds;
    }

    @Test
    void testIsEliAndSmallFile() {
        FinDocHistPo fds = new FinDocHistPo();
        fds.setDocFinCatCde("ELI");

        // Case 1: Small file
        assertTrue(finDocReleaseService.isEliAndSmallFile(fds, 1024));

        // Case 2: Large file
        assertFalse(finDocReleaseService.isEliAndSmallFile(fds, 4096));

        // Case 3: Non-ELI file
        fds.setDocFinCatCde("SN");
        assertFalse(finDocReleaseService.isEliAndSmallFile(fds, 1024));
    }

    @Test
    void testCopyFileToS3_Success() throws IOException, InterruptedException {
        String copyCommand = "aws s3 cp test.txt s3://bucket/test.txt";
        String filePath = "test.txt";

        doReturn(true).when(finDocReleaseService).executeCommand(copyCommand, "Copy process timed out for file: " + filePath);

        boolean result = finDocReleaseService.copyFileToS3(copyCommand, filePath);
        assertTrue(result);
    }

    @Test
    void testCopyFileToS3_Failure() throws IOException, InterruptedException {
        String copyCommand = "aws s3 cp test.txt s3://bucket/test.txt";
        String filePath = "test.txt";

        doReturn(false).when(finDocReleaseService).executeCommand(copyCommand, "Copy process timed out for file: " + filePath);

        boolean result = finDocReleaseService.copyFileToS3(copyCommand, filePath);
        assertFalse(result);
    }


    @Test
    void testDeleteFileFromS3_Success() throws IOException, InterruptedException {
        String deleteCommand = "aws s3 rm s3://bucket/test.txt";
        String filePath = "test.txt";

        doReturn(true).when(finDocReleaseService).executeCommand(deleteCommand, "Deletion process timed out for file: " + filePath);

        ReflectionTestUtils.invokeMethod(finDocReleaseService, "deleteFileFromS3", deleteCommand, filePath);

        verify(finDocReleaseService, times(1)).executeCommand(deleteCommand, "Deletion process timed out for file: " + filePath);
    }

    @Test
    void testDeleteFileFromS3_Failure() throws IOException, InterruptedException {
        String deleteCommand = "aws s3 rm s3://bucket/test.txt";
        String filePath = "test.txt";

        doReturn(false).when(finDocReleaseService).executeCommand(deleteCommand, "Deletion process timed out for file: " + filePath);

        ReflectionTestUtils.invokeMethod(finDocReleaseService, "deleteFileFromS3", deleteCommand, filePath);

        verify(finDocReleaseService, times(1)).executeCommand(deleteCommand, "Deletion process timed out for file: " + filePath);
    }


    @Test
    void testReleaseFilesAndUpdateDB_Success() throws IOException, InterruptedException {
         String ctryRecCde = "testCtry";
         String grpMembrRecCde = "testGrp";

         FinDocHistPo[] fds = new FinDocHistPo[1];
         FinDocHistPo finDocHistPo = new FinDocHistPo();
         finDocHistPo.setUrlLclSysText("testUrl");
         fds[0] = finDocHistPo;

         when(finDocReleaseDao.retrieveFinDocSmryRecordByStatusFS(ctryRecCde, grpMembrRecCde, "P")).thenReturn(fds);
         doReturn(0).when(finDocReleaseService).transferToS3(any(FinDocHistPo.class));
         doNothing().when(finDocReleaseService).updateDB(any(FinDocHistPo.class));

         boolean result = finDocReleaseService.releaseFilesAndUpdateDB(ctryRecCde, grpMembrRecCde);
         assertTrue(result);
         verify(finDocReleaseService, times(1)).updateDB(any(FinDocHistPo.class));
    }



    @Test
    void testGetDocPath() {
        String docName1 = "test.ut_file.pdf";
        String docName2 = "test.other_file.pdf";

        assertEquals("unit\\ut_file.pdf", finDocReleaseService.getDocPath(docName1));
        assertEquals("test\\other_file.pdf", finDocReleaseService.getDocPath(docName2));
    }


    @Test
    void testExecuteCommand_Success() throws IOException, InterruptedException {
        String command = "echo test";
        Runtime mockedRuntime = mock(Runtime.class);
        Process mockedProcess = mock(Process.class);
        Mockito.mockStatic(Runtime.class);
        when(Runtime.getRuntime()).thenReturn(mockedRuntime);
        when(mockedRuntime.exec(command)).thenReturn(mockedProcess);
        when(mockedProcess.waitFor(60, TimeUnit.SECONDS)).thenReturn(true);
        when(mockedProcess.exitValue()).thenReturn(0);

        // Mock getErrorStream to return a valid InputStream
        InputStream mockedErrorStream = new ByteArrayInputStream("".getBytes());
        when(mockedProcess.getErrorStream()).thenReturn(mockedErrorStream);

        boolean result = ReflectionTestUtils.invokeMethod(finDocReleaseService, "executeCommand", command, "Error message");
        assertTrue(result);
    }

    @Test
    void testExecuteCommand_Failure() throws IOException, InterruptedException {
        String command = "invalid_command";
        Runtime mockedRuntime = mock(Runtime.class);
        Process mockedProcess = mock(Process.class);
        Mockito.mockStatic(Runtime.class);

        when(Runtime.getRuntime()).thenReturn(mockedRuntime);
        when(mockedRuntime.exec(command)).thenReturn(mockedProcess);
        when(mockedProcess.waitFor(60, TimeUnit.SECONDS)).thenReturn(false);

        InputStream mockedErrorStream = new ByteArrayInputStream("Mocked error message".getBytes());
        when(mockedProcess.getErrorStream()).thenReturn(mockedErrorStream);

        boolean result = ReflectionTestUtils.invokeMethod(finDocReleaseService, "executeCommand", command, "Error message");
        assertFalse(result);
    }

    @Test
    void testReleaseFilesAndUpdateDB_Exception() throws IOException, InterruptedException {
        String ctryRecCde = "testCtry";
        String grpMembrRecCde = "testGrp";

        // Set a valid value for maxToPws
        ReflectionTestUtils.setField(finDocReleaseService, "maxToPws", "5");

        FinDocHistPo[] fds = new FinDocHistPo[1];
        FinDocHistPo finDocHistPo = new FinDocHistPo();
        finDocHistPo.setUrlLclSysText("testUrl");
        fds[0] = finDocHistPo;

        when(finDocReleaseDao.retrieveFinDocSmryRecordByStatusFS(ctryRecCde, grpMembrRecCde, "P")).thenReturn(fds);
        doThrow(new IOException("Mocked exception")).when(finDocReleaseService).transferToS3(any(FinDocHistPo.class));

        boolean result = finDocReleaseService.releaseFilesAndUpdateDB(ctryRecCde, grpMembrRecCde);
        assertFalse(result);
    }

    @Test
    void transferToS3() throws IOException, InterruptedException{

        FinDocHistPo fds = createFindDocHistPo();

        Runtime mockedRuntime = Mockito.mock(Runtime.class);
        Mockito.mockStatic(Runtime.class);
        Mockito.when(Runtime.getRuntime()).thenReturn(mockedRuntime);
        Process mockedProcess = Mockito.mock(Process.class);
        Mockito.when(mockedRuntime.exec(Mockito.anyString())).thenReturn(mockedProcess);
        Mockito.when(mockedProcess.getErrorStream()).thenReturn(inputStream);

        Assertions.assertEquals(Integer.MAX_VALUE, finDocReleaseService.transferToS3(fds));
        verify(mockedProcess).waitFor(60L,TimeUnit.SECONDS);
    }

    @Test
    void testReleaseFilesAndUpdateDB() throws IOException, InterruptedException {
        String ctryRecCde = "testCtry";
        String grpMembrRecCde = "testGrp";

        ReflectionTestUtils.setField(finDocReleaseService, "maxToPws", "7");
        FinDocHistPo[] fds = new FinDocHistPo[7];
        // Test case 1: SID and DOC_TYP_AP
        FinDocHistPo finDocHistPo = createFindDocHistPo();
        finDocHistPo.setUrlLclSysText("testUrl");
        finDocHistPo.setDocFinCatCde("SID");
        finDocHistPo.setDocFinTypeCde("APPENDIX");
        fds[0] = finDocHistPo;
        // Test case 2: SID and OTHER
        FinDocHistPo finDocHistPo2 = createFindDocHistPo();
        finDocHistPo2.setUrlLclSysText("testUrl");
        finDocHistPo2.setDocFinCatCde("SID");
        finDocHistPo2.setDocFinTypeCde("OTHER");
        fds[1] = finDocHistPo2;
        // Test case 3: ELI and DOC_TYP_TS
        FinDocHistPo finDocHistPo3 = createFindDocHistPo();
        finDocHistPo3.setDocRecvName("testRecvName");
        finDocHistPo3.setDocFinCatCde("ELI");
        finDocHistPo3.setDocFinTypeCde("TERMSHEET");
        fds[2] = finDocHistPo3;
        // Test case 4: ELI and DOC_TYP_PS
        FinDocHistPo finDocHistPo4 = createFindDocHistPo();
        finDocHistPo4.setDocFinCatCde("ELI");
        finDocHistPo4.setDocFinTypeCde(FinDocConstants.DOC_TYP_PS);
        fds[3] = finDocHistPo4;
        // Test case 5: SN and DOC_TYP_TS
        FinDocHistPo finDocHistPo5 = createFindDocHistPo();
        finDocHistPo5.setDocFinCatCde("SN");
        finDocHistPo5.setDocFinTypeCde("TERMSHEET");
        fds[4] = finDocHistPo5;
        // Test case 6: SN and OTHER
        FinDocHistPo finDocHistPo6 = createFindDocHistPo();
        finDocHistPo6.setDocFinCatCde("SN");
        finDocHistPo6.setDocFinTypeCde("OTHER");
        fds[5] = finDocHistPo6;
        // Test case 7: ELI and OTHER
        FinDocHistPo finDocHistPo7 = createFindDocHistPo();
        finDocHistPo7.setDocFinCatCde("ELI");
        finDocHistPo7.setDocFinTypeCde("OTHER");
        fds[6] = finDocHistPo7;


        when(finDocReleaseDao.retrieveFinDocSmryRecordByStatusFS(ctryRecCde, grpMembrRecCde, "P")).thenReturn(fds);
        doNothing().when(finDocReleaseService).updateDB(any(FinDocHistPo.class));
        doReturn(0).when(finDocReleaseService).transferToS3(any(FinDocHistPo.class));

        boolean result = finDocReleaseService.releaseFilesAndUpdateDB(ctryRecCde, grpMembrRecCde);

        assertFalse(result);//IOExeption
        verify(finDocReleaseDao, times(1)).retrieveFinDocSmryRecordByStatusFS(ctryRecCde, grpMembrRecCde, "P");
        verify(finDocReleaseService, times(7)).updateDB(any(FinDocHistPo.class));
        verify(finDocReleaseService, times(7)).transferToS3(any(FinDocHistPo.class));
    }

    @Test
    void testUpdateDBThrowsproductBatchException() throws IOException, InterruptedException {
        String ctryRecCde = "testCtry";
        String grpMembrRecCde = "testGrp";

        ReflectionTestUtils.setField(finDocReleaseService, "maxToPws", "5");
        FinDocHistPo[] fds = new FinDocHistPo[1];
        FinDocHistPo finDocHistPo = createFindDocHistPo();
        finDocHistPo.setUrlLclSysText("testUrl");
        fds[0] = finDocHistPo;

        when(finDocReleaseDao.retrieveFinDocSmryRecordByStatusFS(ctryRecCde, grpMembrRecCde, "P")).thenReturn(fds);
        doReturn(0).when(finDocReleaseService).transferToS3(any(FinDocHistPo.class));
        doThrow(new productBatchException("Mocked exception")).when(finDocReleaseService).updateDB(any(FinDocHistPo.class));

        boolean result = finDocReleaseService.releaseFilesAndUpdateDB(ctryRecCde, grpMembrRecCde);

        assertFalse(result);//productBatchException

        verify(finDocReleaseDao, times(1)).retrieveFinDocSmryRecordByStatusFS(ctryRecCde, grpMembrRecCde, "P");
        verify(finDocReleaseService, times(1)).updateDB(any(FinDocHistPo.class));

        doReturn(1).when(finDocReleaseService).transferToS3(any(FinDocHistPo.class));
        boolean result2 = finDocReleaseService.releaseFilesAndUpdateDB(ctryRecCde, grpMembrRecCde);

        assertTrue(result2);//productBatchException

    }
}