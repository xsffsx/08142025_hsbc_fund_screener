package com.dummy.wpb.product.component;

import com.dummy.wpb.product.model.FinDocSmry;
import com.dummy.wpb.product.model.FinDocULReqPo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;


@SpringJUnitConfig
@SpringBootTest(classes = PDFFileServiceTest.class)
@ActiveProfiles("test")
public class PDFFileServiceTest {

    @MockBean
    FinDocSmry finDocSmry;


    @Test
    void testCheckDirExist() throws IOException {
        File dir = new File(new ClassPathResource("/").getFile().getAbsolutePath() + "/checkDir");
        PDFFileService.checkDir(dir);
        Assertions.assertTrue(dir.exists());
    }

    @Test
    void shouldCreateNewFileIfDirectoryDoesNotExist() throws IOException {
        File dir = mock(File.class);
        when(dir.exists()).thenReturn(false);
        when(dir.createNewFile()).thenReturn(true);

        PDFFileService.checkDir(dir);

        verify(dir).createNewFile();
    }

    @Test
    void shouldNotCreateNewFileIfDirectoryExists() throws IOException {
        File dir = mock(File.class);
        when(dir.exists()).thenReturn(true);

        PDFFileService.checkDir(dir);

        verify(dir, Mockito.never()).createNewFile();
    }

    @Test
    void testFileToDoneToArchToDelMatch() throws IOException{
        PDFFileService pdfFileService = new PDFFileService(finDocSmry);

        String src =  new ClassPathResource("/").getFile().getAbsolutePath() + "/findoc/pdf/chk";
        String desc =  new ClassPathResource("/").getFile().getAbsolutePath() + "/findoc/pdf/aprv";
        String docSerNum = "123";
        String docIncmName = "test.pdf";
        String name = docSerNum + "." + docIncmName.trim();

        File reqFile = new File(src, name);
        PDFFileService.checkDir(reqFile);
        when(finDocSmry.getDocSerNum()).thenReturn(Long.valueOf(docSerNum));
        when(finDocSmry.getDocIncmName()).thenReturn(docIncmName);
        File result = pdfFileService.fileToAprvToDone(src, desc);
        Assertions.assertNotNull(result);
        verify(finDocSmry, times(1)).getDocSerNum();
        verify(finDocSmry, times(1)).getDocIncmName();
        reqFile.delete();
    }

    @Test
    void testSetFileUrl() throws IOException {
        String pdfChkPath = new ClassPathResource("/").getFile().getAbsolutePath() + "/findoc/pdf/chk";
        File reqFile = new File(pdfChkPath);
        String result = PDFFileService.setFileUrl(reqFile, true);
        Assertions.assertEquals("pdf/chk", result);
        result = PDFFileService.setFileUrl(reqFile, false);
        Assertions.assertEquals("pdf/chk", result);

    }


    public static FinDocULReqPo createFinDocULReqPo() {
        FinDocULReqPo finDocULReqPo = new FinDocULReqPo();
        finDocULReqPo.setCtryRecCde("HK");
        finDocULReqPo.setGrpMembrRecCde("HBAP");
        finDocULReqPo.setDocFinTypeCde("USERDOC-5");
        finDocULReqPo.setDocFinCatCde("FC007");
        finDocULReqPo.setDocFinCde("ABC");
        finDocULReqPo.setLangFinDocCde("En");
        return finDocULReqPo;
    }


}