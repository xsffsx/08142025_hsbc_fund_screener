package com.hhhh.group.secwealth.mktdata.elastic.processor;

import com.google.common.collect.Lists;
import com.hhhh.group.secwealth.mktdata.elastic.bean.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.elastic.bean.Version;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.dao.GRCompanyRepository;
import com.hhhh.group.secwealth.mktdata.elastic.dao.entiry.GRCompanyPo;
import com.hhhh.group.secwealth.mktdata.elastic.dao.spec.CompanySpecification;
import com.hhhh.group.secwealth.mktdata.elastic.service.DataHandlerCommonService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockInstmProductProcessorTest {
    @Mock
    private GRCompanyRepository grCompanyRepository;
    @Mock
    private CompanySpecification companySpecification;
    @Mock
    private DataHandlerCommonService dataHandlerCommonService;
    @InjectMocks
    private StockInstmProductProcessor underTest;

    @Nested
    class WhenProcessing {

        @Test
        void test_process() throws Exception {
            String path = "src/test/resources/files/HK_HBAP_WMDS_SEC-HK_20230530081158_35.xml";
            File file = new File(path);
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            XMLStreamReader xmlr = xmlif.createXMLStreamReader(new FileReader(file));
            Version version = new Version("20230530081158", "35");
            List<GRCompanyPo> houseViews = getHouseViewList();
            when(grCompanyRepository.findAllByMarketEqualsAndExpireIsNull("HK")).thenReturn(houseViews);
            Specification<GRCompanyPo> spec = (Specification<GRCompanyPo>) (root, query, criteriaBuilder) -> null;
            when(companySpecification.getRecentUpdateSpec("HK")).thenReturn(spec);
            when(grCompanyRepository.findAll(spec)).thenReturn(houseViews);
            DataSiteEntity dataSiteEntity = mock(DataSiteEntity.class);
            when(dataSiteEntity.getObjList()).thenReturn(Lists.newArrayList());
            DataSiteEntity.DataFileInfo dataFileInfo = mock(DataSiteEntity.DataFileInfo.class);
            when(dataFileInfo.getProductType()).thenReturn("SEC-HK");
            File mockFile = mock(File.class);
            when(dataFileInfo.getDataFile()).thenReturn(mockFile);
            when(dataHandlerCommonService.handleCommonData(any(), any(), any(), any())).thenReturn(getCustomizedProduct());
            when(mockFile.getName()).thenReturn("HK_HBAP_WMDS_SEC-HK_20230530081158_35.xml");
            assertDoesNotThrow(() -> underTest.process(xmlr, dataSiteEntity, "HK_HBAP", version, dataFileInfo));
        }

        @Test
        void test_process_exception() throws XMLStreamException {
            XMLStreamReader xmlr = mock(XMLStreamReader.class);
            Version version = new Version("20230530081158", "35");
            DataSiteEntity dataSiteEntity = mock(DataSiteEntity.class);
            DataSiteEntity.DataFileInfo dataFileInfo = mock(DataSiteEntity.DataFileInfo.class);
            when(xmlr.nextTag()).thenThrow(XMLStreamException.class);
            when(dataFileInfo.getProductType()).thenReturn("SEC-HK");
            File mockFile = mock(File.class);
            when(dataFileInfo.getDataFile()).thenReturn(mockFile);
            when(mockFile.getName()).thenReturn("HK_HBAP_WMDS_SEC-HK_20230530081158_35.xml");
            assertDoesNotThrow(() -> underTest.process(xmlr, dataSiteEntity, "HK_HBAP", version, dataFileInfo));
        }

        @Test
        void test_getRecentlyUpdateList() {
            DataSiteEntity.DataFileInfo dataFileInfo = mock(DataSiteEntity.DataFileInfo.class);
            when(dataFileInfo.getProductType()).thenReturn("SEC-US");
            assertNotNull(underTest.getRecentlyUpdateList(dataFileInfo));
        }

        @Test
        void test_getHouseViewList() {
            DataSiteEntity.DataFileInfo dataFileInfo = mock(DataSiteEntity.DataFileInfo.class);
            when(dataFileInfo.getProductType()).thenReturn("SEC-US");
            assertNotNull(underTest.getHouseViewList(dataFileInfo));
        }

        private CustomizedEsIndexForProduct getCustomizedProduct() {
            CustomizedEsIndexForProduct product = new CustomizedEsIndexForProduct();
            product.setId("50005650SEC");
            product.setCtryRecCde("HK");
            product.setGrpMembrRecCde("HBAP");
            List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
            ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
            prodAltNumSeg.setProdAltNum("00003");
            prodAltNumSeg.setProdCdeAltClassCde("M");
            prodAltNumSegs.add(prodAltNumSeg);
            product.setProdAltNumSegs(prodAltNumSegs);
            product.setProductType("SEC");
            product.setProductSubType("LeQU");
            product.setCountryTradableCode("HK");
            product.setAllowSell("Y");
            product.setAllowBuy("Y");
            product.setProductName("HKBO S83  SIT TEST 1");
            product.setProductCcy("HKD");
            product.setAllowSellMipProdInd("A");
            product.setProdStatCde("A");
            product.setAllowSwOutProdInd("N");
            product.setAllowSwInProdInd("N");
            product.setSymbol("00003");
            product.setSymbolLowercase("00003");
            return product;
        }

        private List<GRCompanyPo> getHouseViewList() {
            List<GRCompanyPo> houseViews = Lists.newArrayList();
            GRCompanyPo grCompanyPo = new GRCompanyPo();
            grCompanyPo.setSymbol("00003");
            grCompanyPo.setRating("Hold");
            houseViews.add(grCompanyPo);
            return houseViews;
        }
    }
}
