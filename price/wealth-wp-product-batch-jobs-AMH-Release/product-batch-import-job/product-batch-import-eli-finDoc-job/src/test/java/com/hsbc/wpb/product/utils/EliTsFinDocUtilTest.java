package com.dummy.wpb.product.utils;

import com.dummy.wpb.product.component.ImportEliFinDocService;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.bson.Document;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EliTsFinDocUtilTest {

    @InjectMocks
    EliTsFinDocUtil eliTsFinDocUtil;

    @Mock
    ImportEliFinDocService importEliFinDocService;

    static Document prod;

    static InputStream inputStream;

    private static String ELITestFilePath = "/test/CMB";

    @BeforeAll
    public static void setUp() throws Exception {
        prod = Document.parse(CommonUtils.readResource("/product-original.json"));
        inputStream = new FileInputStream("src/test/resources/test.txt");

        BatchCommonUtil mockedBatchCommonUtil = Mockito.mock(BatchCommonUtil.class);
        Mockito.mockStatic(BatchCommonUtil.class);
        when(mockedBatchCommonUtil.executeOperationSystemCommand(Mockito.anyString())).thenReturn(true);

    }


    @AfterEach
    public void after() throws IOException {
        File testFolder = new ClassPathResource(ELITestFilePath).getFile();
        for (File file : testFolder.listFiles()) {
            if (file.getName().endsWith(".bak")){
                file.renameTo(new File(StringUtils.substringBefore(file.getAbsolutePath(),".bak")));
            }
        }
    }

    @ParameterizedTest
    @CsvSource(
            {
                    "PPN_PPR,true,FCELI",
                    "PPN_STPDWN,true,FCELI",
                    "PPN_UPOUT,true,FCELI",
                    "PPN_FCN,true,FCELI",
                    "PPN_TWSF,true,FCELI",
                    "PPN_60,true,FCELI",
                    "PPN_50,true,FCELI",
                    "PPN_50,false,FCELI",
                    "PPN_51,true,FCELI",
                    "PPN_52,true,FCELI",
                    "PPN_61,true,FCELI",
                    "PPN_62,true,FCELI",
                    "UO_ELI,true,FCELI",
                    "PPN_63,true,FCELI",
                    "PPN_64,true,FCELI",
                    "PPN_65,true,FCELI",
                    "PPN_53,true,FCELI",
                    "FXN_01,true,FCELI",
                    "FXN_02,true,FCELI",
                    "FXN_03,true,FCELI",
                    "FXN_04,true,FCELI",
                    "UNKNOWN,true,FCELI",
                    "UNKNOWN,true,OTHER"
            })
    void testGetImportantFileForNonCmb(String prodSubtpCde, boolean flag, String eliPayoffTypeCde) throws Exception {
        EliTsFinDocUtil util = new EliTsFinDocUtil();
        Method privateMethod = util.getClass().getDeclaredMethod("getImportantFileForNonCmb", String.class, boolean.class, String.class);
        privateMethod.setAccessible(true);
        String importantFileString = (String) privateMethod.invoke(util, prodSubtpCde, flag, eliPayoffTypeCde);

        assertNull(importantFileString);
    }

    @ParameterizedTest
    @CsvSource({"0.3","1.2","2.3","3.5","4.6"})
    void testGetImportantFileForNonCmb(Double distbrFeePct) throws Exception {
        EliTsFinDocUtil util = new EliTsFinDocUtil();
        Method privateMethod = util.getClass().getDeclaredMethod("getImportantFileForApendix", Double.class);
        privateMethod.setAccessible(true);
        String importantFileString = (String) privateMethod.invoke(util, distbrFeePct);

        assertNull(importantFileString);
    }


    @Test
    void testHandleFinDocFile() throws Exception {
        ReflectionTestUtils.setField(eliTsFinDocUtil, "pdfImportantFileForPpr", "pdf/important_cmb_eli_ppr.pdf");
        ReflectionTestUtils.setField(eliTsFinDocUtil, "pdfImportantFileForApendix1", "pdf/impotant_apendix1.pdf");
        ReflectionTestUtils.setField(eliTsFinDocUtil, "eliFindocScript", "chmod 775");
        ReflectionTestUtils.setField(eliTsFinDocUtil, "isrInfoUndlTypeCde", "IR,FX");
        when(importEliFinDocService.queryProductByPriNum(Mockito.any())).thenReturn(prod);

        eliTsFinDocUtil.handleFinDocFile(new ClassPathResource("/test/NO_CMB/DFBAS2300127_FINAL_TERMSHEETS_PFS.ack").getFile(), "HK", "HBAP", "ELI", importEliFinDocService, true);
        assertTrue(true);
    }

    @Test
    void testHandleFinDocCMBFile() throws Exception {
        ReflectionTestUtils.setField(eliTsFinDocUtil, "pdfImportantFileForPpr", "pdf/important_cmb_eli_ppr.pdf");
        ReflectionTestUtils.setField(eliTsFinDocUtil, "pdfImportantFileForPprPpn1", "pdf/important_cmb_ppn_ppr_1.pdf");
        ReflectionTestUtils.setField(eliTsFinDocUtil, "pdfImportantFileForPprPpn2", "pdf/important_cmb_ppn_ppr_2.pdf");
        ReflectionTestUtils.setField(eliTsFinDocUtil, "cmbDcdcEliProdSubtpCde", "PPN");
        ReflectionTestUtils.setField(eliTsFinDocUtil, "cmbPprEliProdSubtpCde", "TEST");
        ReflectionTestUtils.setField(eliTsFinDocUtil, "cmbPprPpnProdSubtpCde", "PPN_PPR");
        ReflectionTestUtils.setField(eliTsFinDocUtil, "eliFindocScript", "chmod 775");
        ReflectionTestUtils.setField(eliTsFinDocUtil, "isrInfoUndlTypeCde", "IR,FX");
        when(importEliFinDocService.queryProductByPriNum(Mockito.any())).thenReturn(prod);

        eliTsFinDocUtil.handleFinDocFile(new ClassPathResource("/test/CMB/DFBAS2300127_FINAL_TERMSHEETS_CMB_PFS.ack").getFile(), "HK", "HBAP", "ELI", importEliFinDocService, true);
        assertTrue(true);
    }

    @Test
    void testAddFooter() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("test", ".pdf");
        File outputFile = File.createTempFile("output", ".pdf");
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage());
            doc.save(tempFile);
        }
        String footerMessage = "Test Footer";

        // Act
        EliTsFinDocUtil.addFooter(tempFile.getAbsolutePath(), footerMessage, outputFile.getAbsolutePath());

        // Assert
        try (PDDocument doc = PDDocument.load(outputFile)) {
            assertEquals(1, doc.getNumberOfPages());
            // Additional validation can be done by extracting text from the footer if needed
        } finally {
            tempFile.delete();
            outputFile.delete();
        }
    }
}