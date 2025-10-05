package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.google.common.collect.Lists;
import com.hhhh.group.secwealth.mktdata.elastic.bean.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.elastic.bean.Version;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.Product;
import com.hhhh.group.secwealth.mktdata.elastic.component.ProductEntities;
import com.hhhh.group.secwealth.mktdata.elastic.dao.GRCompanyRepository;
import com.hhhh.group.secwealth.mktdata.elastic.dao.spec.CompanySpecification;
import com.hhhh.group.secwealth.mktdata.elastic.processor.StockInstmProductProcessor;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.PredsrchSortingOrderProperties;
import com.hhhh.group.secwealth.mktdata.elastic.properties.VolumeServiceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import javax.xml.stream.XMLInputFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataHandlerServiceTest {

    @Mock
    private AppProperties appProperty;
    @Mock
    private ProductEntities productEntities;
    @Mock
    private PredsrchSortingOrderProperties predsrchSortingOrderProperties;
    @Mock
    private Map<String, String[]> dataTypeBySiteMap;
    @Mock
    private XMLInputFactory xmlif;
    @Mock
    private GRCompanyRepository grCompanyRepository;
    @Mock
    private CompanySpecification companySpecification;
    @Mock
    private VolumeServiceProperties volumeServiceProperties;
    @Mock
    private DataHandlerContext dataHandlerContext;
    @InjectMocks
    private DataHandlerService underTest;

    @Nested
    class WhenGettingCurrentVerBySite {

        @Test
        void test_gettingCurrentVerBySite(){
            assertNotNull(underTest.getCurrentVerBySite());
        }
    }

    @Nested
    class WhenGettingLatestVersionBySite {

        @Test
        void test_getLatestVersionBySite() throws Exception {
            String path = "src/test/resources/files";
            ReflectionTestUtils.setField(underTest, "filePath", path);
            assertNotNull(underTest.getLatestVersionBySite("HK_HBAP"));
        }
    }

    @Nested
    class WhenComparingVersion {

        @Test
        void test_comparingVersion() throws Exception {
            Version version1 = new Version("20230530081158", "35");
            Version version2 = new Version("20230530081158", "34");
            assertTrue(underTest.compareVersion(version1, version2));
        }
    }

    @Nested
    class WhenIfDataExpired {

        @Test
        void test_ifDataExpired() throws ParseException {
            Map<String, Version> currentVerBySite = new ConcurrentHashMap<>();
            currentVerBySite.put("HK_HBAP", new Version("20230530081158", "35"));
            ReflectionTestUtils.setField(underTest, "currentVerBySite", currentVerBySite);
            assertTrue(underTest.ifDataExpired("HK_HBAP"));
        }

        @Test
        void test_ifDataExpired01() throws ParseException {
            Map<String, Version> currentVerBySite = new ConcurrentHashMap<>();
            ReflectionTestUtils.setField(underTest, "currentVerBySite", currentVerBySite);
            assertFalse(underTest.ifDataExpired("HK_hhhh"));
        }
    }

    @Nested
    class WhenGettingLatestFilesBySite {

        @Test
        void test_getLatestFilesBySite_disableVolumeService() {
            Version version = new Version("20230530081158", "35");
            ReflectionTestUtils.setField(underTest, "filePath", "src/test/resources/files");
            Map<String, String[]> dataTypeBySiteMap = new HashMap<>();
            dataTypeBySiteMap.put("HK_HBAP", new String[]{"SEC-HK"});
            ReflectionTestUtils.setField(underTest, "dataTypeBySiteMap", dataTypeBySiteMap);
            assertDoesNotThrow(() -> underTest.getLatestFilesBySite("HK_HBAP", version, false));
        }
    }

    @Nested
    class WhenBuildingFileEntityBySite {
        private XMLInputFactory xmlif;
        private File xmlFile;
        private Map<String, Product> entities = new ConcurrentHashMap<>();

        @BeforeEach
        void setup() {
            this.xmlif = XMLInputFactory.newInstance();
            ReflectionTestUtils.setField(underTest, "xmlif", this.xmlif);
            Map<String, String[]> dataTypeBySiteMap = new HashMap<>();
            dataTypeBySiteMap.put("HK_HBAP", new String[]{"SEC-CN", "SEC-HK", "SEC-US", "WRTS-HK", "UT"});
            ReflectionTestUtils.setField(underTest, "dataTypeBySiteMap", dataTypeBySiteMap);
            ReflectionTestUtils.setField(underTest, "filePath", "src/test/resources/files/");
            Product stockInstm = new Product();
            stockInstm.setNodeName("stockInstm");
            Product eli = new Product();
            eli.setNodeName("eli");
            Product dps = new Product();
            dps.setNodeName("dps");
            Product sid = new Product();
            sid.setNodeName("sid");
            Product ut = new Product();
            ut.setNodeName("utTrstInstm");
            Product sn = new Product();
            sn.setNodeName("gnrcProd");
            Product bond = new Product();
            bond.setNodeName("debtInstm");
            entities.put("SEC", stockInstm);
            entities.put("ELI", eli);
            entities.put("DPS", dps);
            entities.put("SID", sid);
            entities.put("UT", ut);
            entities.put("SN", sn);
            entities.put("BOND", bond);
        }

        @Test
        void test_buildFileEntityBySite_refData() throws Exception {
            String path = "src/test/resources/files/HK_HBAP_WMDS_ReferenceData_20230530081158_35.xml";
            File file = new File(path);
            this.xmlFile = file;
            File[] files = new File[]{file};
            Version version = new Version("20230530081158", "35");
            assertNotNull(underTest.buildFileEntityBySite("HK_HBAP", files, version));
        }

        @Test
        void test_buildFileEntityBySite_stockInstm() throws Exception {
            String path = "src/test/resources/files/HK_HBAP_WMDS_SEC-HK_20230530081158_35.xml";
            File file = new File(path);
            this.xmlFile = file;
            File[] files = new File[]{file};
            Version version = new Version("20230530081158", "35");
            Map<String, Product> productMap = new ConcurrentHashMap<>();
            Product product = new Product();
            product.setNodeName("stockInstm");
            product.setDataConverter("com.hhhh.group.secwealth.mktdata.predsrch.svc.converter.SecDataConverter");
            product.setProductPackage("com.hhhh.group.secwealth.mktdata.predsrch.dal.model.stock");
            product.setProductDescriptor("{0} {1}");
            product.setText("<strong>{2}</strong> <span title=\"{-1}\">{-1}</span>");
            productMap.put("SEC", product);
            when(productEntities.getProductEntities()).thenReturn(productMap);
            StockInstmProductProcessor processor = mock(StockInstmProductProcessor.class);
            when(dataHandlerContext.getProcessorByName("stockinstm")).thenReturn(processor);
            assertNotNull(underTest.buildFileEntityBySite("HK_HBAP", files, version));
        }

        @Test
        void test_processXMLFile() {
            DataSiteEntity dataSiteEntity = mock(DataSiteEntity.class);
            Version version = mock(Version.class);
            DataSiteEntity.DataFileInfo dataFileInfo = mock(DataSiteEntity.DataFileInfo.class);
            when(dataFileInfo.getDataFile()).thenReturn(new File("xxx"));
            assertDoesNotThrow(() -> underTest.processXMLFile("stockinstm", dataSiteEntity, "HK_HBAP", version, dataFileInfo));
        }
    }

    @Nested
    class WhenClearingDataFiles {

        @Test
        void test_clearingDataFiles() {
            ReflectionTestUtils.setField(underTest, "housekeepOn", true);
            ReflectionTestUtils.setField(underTest, "housekeepDate", null);
            ReflectionTestUtils.setField(underTest, "maxBackupDay", 1);
            ReflectionTestUtils.setField(underTest, "housekeepDate", "20230901");
            ReflectionTestUtils.setField(underTest, "filePath", "src/test/resources/wpc/");
            ReflectionTestUtils.setField(underTest, "sourceFilePath", "src/test/resources/wpc/");
            ReflectionTestUtils.setField(underTest, "rejectFilePath", "src/test/resources/wpc/");
            when(appProperty.getSupportSites()).thenReturn(Lists.newArrayList("Default", "HK_HBAP"));
            assertDoesNotThrow(() -> underTest.clearDataFiles());
        }

        @Test
        void test_clearingDataFiles_maxBackupDayLessThan0() {
            ReflectionTestUtils.setField(underTest, "housekeepOn", true);
            ReflectionTestUtils.setField(underTest, "maxBackupDay", -1);
            ReflectionTestUtils.setField(underTest, "sourceFilePath", "src/test/resources/wpc/");
            ReflectionTestUtils.setField(underTest, "rejectFilePath", "src/test/resources/wpc/");
            ReflectionTestUtils.setField(underTest, "filePath", "src/test/resources/wpc/");
            assertDoesNotThrow(() -> underTest.clearDataFiles());
        }

        @Test
        void test_deleteFiles_isFile() {
            File file = mock(File.class);
            when(file.isFile()).thenReturn(true);
            assertDoesNotThrow(() -> underTest.deleteFiles(file));
        }

        @Test
        void test_deleteFiles_isFolder() {
            File file = mock(File.class);
            when(file.isFile()).thenReturn(false);
            when(file.listFiles()).thenReturn(new File[0]);
            assertDoesNotThrow(() -> underTest.deleteFiles(file));
        }

        @Test
        void test_deleteFilesBySite_singleFile() {
            File folder = mock(File.class);
            File file = mock(File.class);
            File[] files = new File[]{file};
            when(folder.listFiles(any(FilenameFilter.class))).thenReturn(files);
            when(file.getName()).thenReturn("HK_HBAP_WMDS_20230530081158_35");
            when(file.isFile()).thenReturn(true);
            assertDoesNotThrow(() -> underTest.deleteFilesBySite("HK_HBAP", System.currentTimeMillis(), folder));
        }

        @Test
        void test_deleteFilesBySite_multiFiles() {
            File folder = mock(File.class);
            File file = new File("src/test/resources/files/HK_HBAP_WMDS_20990530081158_35.out");
            File[] files = new File[]{file, file};
            when(folder.listFiles(any(FilenameFilter.class))).thenReturn(files);
            assertDoesNotThrow(() -> underTest.deleteFilesBySite("HK_HBAP", System.currentTimeMillis(), folder));
        }
    }
}
