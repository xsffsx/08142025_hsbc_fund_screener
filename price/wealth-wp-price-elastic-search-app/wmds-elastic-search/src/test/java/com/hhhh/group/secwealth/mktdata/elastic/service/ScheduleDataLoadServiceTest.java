package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.google.common.collect.Lists;
import com.hhhh.group.secwealth.mktdata.elastic.bean.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.elastic.bean.Version;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.Product;
import com.hhhh.group.secwealth.mktdata.elastic.component.ProductEntities;
import com.hhhh.group.secwealth.mktdata.elastic.dao.GRCompanyRepository;
import com.hhhh.group.secwealth.mktdata.elastic.dao.entiry.GRCompanyPo;
import com.hhhh.group.secwealth.mktdata.elastic.dao.spec.CompanySpecification;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler.BMCComponent;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequestBuilder;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleDataLoadServiceTest {
    @Mock
    private AppProperties appProperty;
    @Mock
    private VolumeService volumeService;
    @Mock
    private VerifyFilesService verifyFilesService;
    @Mock
    private DataHandlerService dataHandler;
    @Mock
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Mock
    private Client elasticsearchClient;
    @Mock
    private BMCComponent bmcComponent;
    @Mock
    private ProductEntities productEntities;
    @Mock
    private GRCompanyRepository grCompanyRepository;
    @Mock
    private PredsrchCommonService predsrchCommonService;
    @Mock
    private CompanySpecification companySpecification;
    @InjectMocks
    private ScheduleDataLoadService underTest;

    private Version version() {
        return new Version("20230530081158", "35");
    }

    private Map<String, Version> versionMap() {
        Map<String, Version> map = new HashMap();
        map.put("HK_HBAP", new Version("20230530081158", "35"));
        return map;
    }

    @Nested
    class WhenLoadingData {

        @BeforeEach
        void setup() throws Exception {
            doReturn(Arrays.asList("Default", "HK_HBAP")).when(appProperty).getSupportSites();
            doReturn(version()).when(dataHandler).getLatestVersionBySite(any());
            doReturn(versionMap()).when(dataHandler).getCurrentVerBySite();
        }

        @Test
        void test_loadingData() throws Exception {
            when(appProperty.getSupportSites()).thenReturn(Lists.newArrayList("Default", "HK_HBAP"));
            Version version = new Version("20230530081158", "35", false);
            when(dataHandler.getLatestVersionBySite("HK_HBAP")).thenReturn(version);
            when(dataHandler.getCurrentVerBySite()).thenReturn(new ConcurrentHashMap<>());
            File[] files = new File[1];
            File file1 = new File("src/test/resources/wpc/HK_HBAP_WMDS_SEC-HK_20230530081158_35.xml");
            files[0] = file1;
            when(dataHandler.getLatestFilesBySite("HK_HBAP", version, false)).thenReturn(files);
            DataSiteEntity dataSiteEntity = mock(DataSiteEntity.class);
            when(dataHandler.buildFileEntityBySite(anyString(), any(), any())).thenReturn(dataSiteEntity);
            List<DataSiteEntity.DataFileInfo> dataFileInfos = new ArrayList<>();
            DataSiteEntity.DataFileInfo dataFileInfo1 = new DataSiteEntity().new DataFileInfo();
            dataFileInfo1.setParseSuccess(true);
            dataFileInfo1.setDataFile(file1);
            dataFileInfo1.setProductType("SEC-HK");
            dataFileInfos.add(dataFileInfo1);
            when(dataSiteEntity.getDataFiles()).thenReturn(dataFileInfos);
            when(dataSiteEntity.getObjList()).thenReturn(Lists.newArrayList(getProduct()));
            ReflectionTestUtils.setField(underTest, "enableRetry", true);
            ReflectionTestUtils.setField(underTest, "allXmlParseSuccess", true);
            ReflectionTestUtils.setField(underTest, "esSetting", "/mapping/setting.json");
            ReflectionTestUtils.setField(underTest, "esMapping", "/mapping/mapping.json");
            ReflectionTestUtils.setField(underTest, "retryTimes", 2);
            elasticsearchClient();
            when(elasticsearchRestTemplate.indexOps(any(IndexCoordinates.class))).thenReturn(mock(IndexOperations.class));
            assertDoesNotThrow(() -> underTest.loadData());
        }

        @Test
        void test_loadingData_noNeedUpdated() throws Exception {
            when(appProperty.getSupportSites()).thenReturn(Lists.newArrayList("Default", "HK_HBAP"));
            Version version = new Version("20230530081158", "35", false);
            when(dataHandler.getLatestVersionBySite("HK_HBAP")).thenReturn(version);
            when(dataHandler.getCurrentVerBySite()).thenReturn(new ConcurrentHashMap<>());
            File[] files = new File[2];
            File file1 = new File("src/test/resources/wpc/HK_HBAP_WMDS_SEC-HK_20230530081158_35.xml");
            files[0] = file1;
            File file2 = new File("src/test/resources/wpc/HK_HBAP_WMDS_UT_20230530081158_35.xml");
            files[1] = file2;
            when(dataHandler.getLatestFilesBySite("HK_HBAP", version, false)).thenReturn(files);
            DataSiteEntity dataSiteEntity = mock(DataSiteEntity.class);
            when(dataHandler.buildFileEntityBySite(anyString(), any(), any())).thenReturn(dataSiteEntity);
            when(dataSiteEntity.getDataFiles()).thenReturn(getDataFileInfos(file1, file2));
            when(dataSiteEntity.getObjList()).thenReturn(Lists.newArrayList(getProduct()));
            when(productEntities.getProductEntities()).thenReturn(getProductEntity());
            ReflectionTestUtils.setField(underTest, "enableRetry", true);
            assertDoesNotThrow(() -> underTest.loadData());
        }

        @Test
        void test_loadingData_volumeServiceAndVerifyFilesErr() throws Exception {
            ReflectionTestUtils.setField(underTest, "allXmlParseSuccess", true);
            when(appProperty.getSupportSites()).thenReturn(Lists.newArrayList("Default", "HK_HBAP"));
            Version version = new Version("20230530081158", "35", false);
            when(dataHandler.getLatestVersionBySite("HK_HBAP")).thenReturn(version);
            when(dataHandler.getCurrentVerBySite()).thenReturn(new ConcurrentHashMap<>());
            File[] files = new File[2];
            File file1 = new File("src/test/resources/wpc/HK_HBAP_WMDS_SEC-HK_20230530081158_35.xml");
            files[0] = file1;
            when(dataHandler.getLatestFilesBySite("HK_HBAP", version, false)).thenReturn(files);
            DataSiteEntity dataSiteEntity = mock(DataSiteEntity.class);
            when(dataHandler.buildFileEntityBySite(anyString(), any(), any())).thenReturn(dataSiteEntity);
            when(dataSiteEntity.isParsingError()).thenReturn(true);
            when(dataSiteEntity.getObjList()).thenReturn(Lists.newArrayList(getProduct()));
            List<DataSiteEntity.DataFileInfo> dataFileInfos = new ArrayList<>();
            DataSiteEntity.DataFileInfo dataFileInfo1 = new DataSiteEntity().new DataFileInfo();
            dataFileInfo1.setParseSuccess(true);
            dataFileInfo1.setDataFile(file1);
            dataFileInfo1.setProductType("SEC-HK");
            dataFileInfos.add(dataFileInfo1);
            when(dataSiteEntity.getDataFiles()).thenReturn(dataFileInfos);
            doThrow(IllegalStateException.class).when(volumeService).copyToLocalData();
            doThrow(IllegalStateException.class).when(verifyFilesService).verifyFiles();
            elasticsearchClient();
            assertDoesNotThrow(() -> underTest.loadData());
        }

        private Map<String, Product> getProductEntity() {
            Map<String, Product> entities = new ConcurrentHashMap<>();
            Product stockInstm = new Product();
            stockInstm.setNodeName("stockInstm");
            Product ut = new Product();
            ut.setNodeName("utTrstInstm");
            entities.put("SEC", stockInstm);
            entities.put("UT", ut);
            return entities;
        }

        private List<DataSiteEntity.DataFileInfo> getDataFileInfos(File file1, File file2) {
            List<DataSiteEntity.DataFileInfo> dataFileInfos = new ArrayList<>();
            DataSiteEntity.DataFileInfo dataFileInfo1 = new DataSiteEntity().new DataFileInfo();
            dataFileInfo1.setParseSuccess(true);
            dataFileInfo1.setDataFile(file1);
            dataFileInfo1.setProductType("SEC-HK");
            dataFileInfos.add(dataFileInfo1);
            DataSiteEntity.DataFileInfo dataFileInfo2 = new DataSiteEntity().new DataFileInfo();
            dataFileInfo2.setParseSuccess(false);
            dataFileInfo2.setDataFile(file2);
            dataFileInfo2.setProductType("UT");
            dataFileInfos.add(dataFileInfo2);
            return dataFileInfos;
        }

        private CustomizedEsIndexForProduct getProduct(){
            CustomizedEsIndexForProduct product = new CustomizedEsIndexForProduct();
            List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
            ProdAltNumSeg prodAltNumSeg1 = new ProdAltNumSeg();
            prodAltNumSeg1.setProdCdeAltClassCde("I");
            prodAltNumSeg1.setProdAltNum("HK0003000038");
            prodAltNumSegs.add(prodAltNumSeg1);

            ProdAltNumSeg prodAltNumSeg2 = new ProdAltNumSeg();
            prodAltNumSeg2.setProdCdeAltClassCde("M");
            prodAltNumSeg2.setProdAltNum("00003");
            prodAltNumSegs.add(prodAltNumSeg2);

            ProdAltNumSeg prodAltNumSeg3 = new ProdAltNumSeg();
            prodAltNumSeg3.setProdCdeAltClassCde("P");
            prodAltNumSeg3.setProdAltNum("00003");
            prodAltNumSegs.add(prodAltNumSeg3);

            ProdAltNumSeg prodAltNumSeg4 = new ProdAltNumSeg();
            prodAltNumSeg4.setProdCdeAltClassCde("T");
            prodAltNumSeg4.setProdAltNum("0003.HK");
            prodAltNumSegs.add(prodAltNumSeg4);
            product.setProdAltNumSegs(prodAltNumSegs);
            product.setId("50005650SEC");
            product.setCountryTradableCode("HK");
            product.setVaEtfIndicator("N");
            product.setCtryRecCde("HK");
            product.setGrpMembrRecCde("HBAP");
            product.setProductType("SEC");
            product.setProductSubType("LEQU");
            product.setAllowBuy("Y");
            product.setAllowSell("Y");
            product.setSequence("20230530081158_35");
            product.setProductTypeWeight("0101");
            product.setProductCode("50005650");
            product.setIsin("HK0003000038");
            product.setSymbol("00003");
            product.setSymbolLowercase("00003");
            product.setAllowSwInProdInd("N");
            product.setAllowSwOutProdInd("N");
            product.setProductName("HKBO S83  SIT TEST 1");
            product.setProductShortName("HKBO S83  SIT TEST 1");
            product.setProductNameAnalyzed(Lists.newArrayList("HKBO S83  SIT TEST 1", "香港中華煤氣有限公司", "香港中华煤气有限公司"));
            product.setProductShrtNameAnalyzed(Lists.newArrayList("HKBO S83  SIT TEST 1", "香港中華煤氣有限公司", "香港中华煤气有限公司"));
            product.setExchange("HKEX");
            return product;
        }

        void elasticsearchClient() {
            AdminClient adminClient = mock(AdminClient.class);
            ClusterAdminClient clusterAdminClient = mock(ClusterAdminClient.class);
            ClusterStateRequestBuilder stateRequestBuilder = mock(ClusterStateRequestBuilder.class);
            ClusterStateResponse response = mock(ClusterStateResponse.class);
            ClusterState clusterState = mock(ClusterState.class);
            MetaData metaData = mock(MetaData.class);
            ImmutableOpenMap.Builder<String, IndexMetaData> builder = ImmutableOpenMap.builder();
            builder.put("hk_hbap_20230530081158_35_1691459995364", mock(IndexMetaData.class));
            ImmutableOpenMap<String, IndexMetaData> map = builder.build();
            when(elasticsearchClient.admin()).thenReturn(adminClient);
            when(adminClient.cluster()).thenReturn(clusterAdminClient);
            when(clusterAdminClient.prepareState()).thenReturn(stateRequestBuilder);
            when(stateRequestBuilder.get()).thenReturn(response);
            when(response.getState()).thenReturn(clusterState);
            when(clusterState.getMetaData()).thenReturn(metaData);
            when(metaData.getIndices()).thenReturn(map);
        }
    }

    @Nested
    class WhenFindLatestUpdateForGr {
        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void findLatestUpdateForGr(boolean needUpdate) {
            if (!needUpdate) {
                List<GRCompanyPo> latestUpdateCompanies = new ArrayList<>();
                GRCompanyPo grCompanyPo1 = new GRCompanyPo();
                grCompanyPo1.setSymbol("00001");
                grCompanyPo1.setMarket("HK");
                GRCompanyPo grCompanyPo2 = new GRCompanyPo();
                grCompanyPo2.setSymbol("WMT");
                grCompanyPo2.setMarket("US");
                latestUpdateCompanies.add(grCompanyPo1);
                latestUpdateCompanies.add(grCompanyPo2);
                List<CustomizedEsIndexForProduct> searchResults = new ArrayList<>();
                CustomizedEsIndexForProduct product01 = new CustomizedEsIndexForProduct();
                product01.setSymbol("00001");
                product01.setMarket("HK");
                product01.setHouseViewRating("Hold");
                searchResults.add(product01);
                when(predsrchCommonService.customizedEsIndexForProductSearch(anyString(), anyList())).thenReturn(searchResults);
                Specification spec = mock(Specification.class);
                when(companySpecification.getRecentUpdateSpec("")).thenReturn(spec);
                when(grCompanyRepository.findAll(spec)).thenReturn(latestUpdateCompanies);
                when(predsrchCommonService.getCurrentIndexName("hk_hbap")).thenReturn("hk_hbap_xxxx");
            }
            assertDoesNotThrow(() -> underTest.findLatestUpdateForGr(needUpdate));
        }

        @Test
        void findLatestUpdateForGr_searchNotFound() {
            List<GRCompanyPo> latestUpdateCompanies = new ArrayList<>();
            GRCompanyPo grCompanyPo1 = new GRCompanyPo();
            grCompanyPo1.setSymbol("00001");
            grCompanyPo1.setMarket("HK");
            latestUpdateCompanies.add(grCompanyPo1);
            List<CustomizedEsIndexForProduct> searchResults = new ArrayList<>();
            when(predsrchCommonService.customizedEsIndexForProductSearch(anyString(), anyList())).thenReturn(searchResults);
            Specification spec = mock(Specification.class);
            when(companySpecification.getRecentUpdateSpec("")).thenReturn(spec);
            when(grCompanyRepository.findAll(spec)).thenReturn(latestUpdateCompanies);
            assertDoesNotThrow(() -> underTest.findLatestUpdateForGr(false));
        }
    }

    @Nested
    class WhenGetObjectListBySite {

        @Test
        void test_handleIfDataUnavailable() {
            ScheduleDataLoadService.LoadStatus status = new ScheduleDataLoadService().new LoadStatus(true, true);
            assertDoesNotThrow(() -> underTest.handleIfDataUnavailable(status));
        }

        @Test
        void test_getObjectListBySite_emptyObjList() throws Exception {
            Version currentVer = new Version("20230530081158", "35");
            ScheduleDataLoadService.LoadStatus status = mock(ScheduleDataLoadService.LoadStatus.class);
            File[] files = new File[1];
            when(dataHandler.getLatestFilesBySite("HK_HBAP", currentVer, false)).thenReturn(files);
            DataSiteEntity dataSiteEntity = new DataSiteEntity();
            dataSiteEntity.setObjList(Lists.newLinkedList());
            when(dataHandler.buildFileEntityBySite("HK_HBAP", files, currentVer)).thenReturn(dataSiteEntity);
            assertDoesNotThrow(() -> underTest.getObjectListBySite("HK_HBAP", null, currentVer, status));
        }

        @Test
        void test_getObjectListBySite_productListInvalid() throws Exception {
            Version currentVer = new Version("20230530081158", "35");
            Version latestVer = new Version("20230530081158", "34");
            ScheduleDataLoadService.LoadStatus status = mock(ScheduleDataLoadService.LoadStatus.class);
            File[] files = new File[1];
            when(dataHandler.getLatestFilesBySite("HK_HBAP", latestVer, false)).thenReturn(files);
            DataSiteEntity dataSiteEntity = new DataSiteEntity();
            dataSiteEntity.setObjList(Lists.newLinkedList());
            when(dataHandler.buildFileEntityBySite("HK_HBAP", files, latestVer)).thenReturn(dataSiteEntity);
            assertDoesNotThrow(() -> underTest.getObjectListBySite("HK_HBAP", latestVer, currentVer, status));
        }

        @Test
        void test_getObjectListBySite_emptyFiles() throws Exception {
            Version latestVer = new Version("20230530081058", "34");
            Version currentVer = new Version("20230530081158", "35");
            ScheduleDataLoadService.LoadStatus status = mock(ScheduleDataLoadService.LoadStatus.class);
            when(dataHandler.getLatestFilesBySite("HK_HBAP", latestVer, false)).thenReturn(new File[0]);
            assertDoesNotThrow(() -> underTest.getObjectListBySite("HK_HBAP", latestVer, currentVer, status));
        }
    }


    @Nested
    class WhenLoadingFromFile {
        private final String FILE_NAME = "HK_HBAP_WMDS_SEC-CN_20210629011003_1923";

        @Test
        void test_loadingFromFile(){
            assertThrows(Exception.class, () -> underTest.loadFromFile(FILE_NAME));
        }
    }
}
