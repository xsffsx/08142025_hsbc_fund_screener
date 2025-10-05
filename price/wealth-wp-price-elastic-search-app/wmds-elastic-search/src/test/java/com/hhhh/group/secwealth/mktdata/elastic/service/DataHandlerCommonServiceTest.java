package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.*;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.properties.PredsrchSortingOrderProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataHandlerCommonServiceTest {
    @Mock
    private PredsrchSortingOrderProperties predsrchSortingOrderProperties;
    @InjectMocks
    private DataHandlerCommonService underTest;

    @Test
    void test_handleCommonData() {
        ProdKeySeg prodKeySeg = new ProdKeySeg();
        prodKeySeg.setCtryRecCde("HK");
        prodKeySeg.setGrpMembrRecCde("HBAP");
        prodKeySeg.setProdTypeCde("SEC");
        prodKeySeg.setProdCde("50137952");
        List<ProdAltNumSeg> prodAltNumSegs = getProdAltNumSegs();
        ProdInfoSeg prodInfoSeg = mock(ProdInfoSeg.class);
        when(prodInfoSeg.getProdName()).thenReturn("UNTRADE-MASTERGLORY-OLD");
        when(prodInfoSeg.getProdPllName()).thenReturn("凱華集團（舊）");
        when(prodInfoSeg.getProdSllName()).thenReturn("凯华集团（旧）");
        when(prodInfoSeg.getProdShrtName()).thenReturn("UNTRADE-MASTERGLORY-OLD");
        when(prodInfoSeg.getProdShrtPllName()).thenReturn("凱華集團（舊）");
        List<AsetGeoLocAllocSeg> asetGeoLocAllocSegs = new ArrayList<>();
        AsetGeoLocAllocSeg asetGeoLocAllocSeg = new AsetGeoLocAllocSeg();
        asetGeoLocAllocSeg.setAsetAllocWghtPct("xxx");
        asetGeoLocAllocSeg.setGeoLocCde("xxx");
        asetGeoLocAllocSegs.add(asetGeoLocAllocSeg);
        when(prodInfoSeg.getAsetGeoLocAllocSeg()).thenReturn(asetGeoLocAllocSegs);
        List<AsetSicAllocSeg> asetSicAllocSegs = new ArrayList<>();
        AsetSicAllocSeg asetSicAllocSeg = new AsetSicAllocSeg();
        asetSicAllocSeg.setAsetAllocWghtPct("xxx");
        asetSicAllocSeg.setSicCde("xxx");
        asetSicAllocSegs.add(asetSicAllocSeg);
        when(prodInfoSeg.getAsetSicAllocSeg()).thenReturn(asetSicAllocSegs);
        boolean[] errors = new boolean[]{false, false};
        Map<String, Map<String, String>> productTypePriorityMap = new HashMap<>();
        Map<String, String> equityPriorityMap = new HashMap<>();
        equityPriorityMap.put("SEC", "01");
        equityPriorityMap.put("ETF", "02");
        equityPriorityMap.put("UT", "03");
        equityPriorityMap.put("INDEX", "04");
        equityPriorityMap.put("BOND", "05");
        productTypePriorityMap.put("Default", equityPriorityMap);
        when(predsrchSortingOrderProperties.getSiteProductTypePriorityMap()).thenReturn(productTypePriorityMap);
        assertNotNull(underTest.handleCommonData(prodKeySeg, prodAltNumSegs, prodInfoSeg, errors));
    }

    private List<ProdAltNumSeg> getProdAltNumSegs() {
        List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
        ProdAltNumSeg prodAltNumSeg1 = new ProdAltNumSeg();
        prodAltNumSeg1.setProdCdeAltClassCde("I");
        prodAltNumSeg1.setProdAltNum("BMG5891T1181");
        ProdAltNumSeg prodAltNumSeg2 = new ProdAltNumSeg();
        prodAltNumSeg2.setProdCdeAltClassCde("M");
        prodAltNumSeg2.setProdAltNum("02927");
        ProdAltNumSeg prodAltNumSeg3 = new ProdAltNumSeg();
        prodAltNumSeg3.setProdCdeAltClassCde("P");
        prodAltNumSeg3.setProdAltNum("02927");
        ProdAltNumSeg prodAltNumSeg4 = new ProdAltNumSeg();
        prodAltNumSeg4.setProdCdeAltClassCde("R");
        prodAltNumSeg4.setProdAltNum("2927.HK");
        ProdAltNumSeg prodAltNumSeg5 = new ProdAltNumSeg();
        prodAltNumSeg5.setProdCdeAltClassCde("Q");
        prodAltNumSeg5.setProdAltNum("810564");
        ProdAltNumSeg prodAltNumSeg6 = new ProdAltNumSeg();
        prodAltNumSeg6.setProdCdeAltClassCde("S");
        prodAltNumSeg6.setProdAltNum("xxx");
        ProdAltNumSeg prodAltNumSeg7 = new ProdAltNumSeg();
        prodAltNumSeg7.setProdCdeAltClassCde("J");
        prodAltNumSeg7.setProdAltNum("xxx");
        ProdAltNumSeg prodAltNumSeg8 = new ProdAltNumSeg();
        prodAltNumSeg8.setProdCdeAltClassCde("F");
        prodAltNumSeg8.setProdAltNum("xxx");
        ProdAltNumSeg prodAltNumSeg9 = new ProdAltNumSeg();
        prodAltNumSeg9.setProdCdeAltClassCde("O");
        prodAltNumSeg9.setProdAltNum("xxx");
        ProdAltNumSeg prodAltNumSeg10 = new ProdAltNumSeg();
        prodAltNumSeg10.setProdCdeAltClassCde("O");
        prodAltNumSeg10.setProdAltNum("xxx");
        prodAltNumSegs.add(prodAltNumSeg1);
        prodAltNumSegs.add(prodAltNumSeg2);
        prodAltNumSegs.add(prodAltNumSeg3);
        prodAltNumSegs.add(prodAltNumSeg4);
        prodAltNumSegs.add(prodAltNumSeg5);
        prodAltNumSegs.add(prodAltNumSeg6);
        prodAltNumSegs.add(prodAltNumSeg7);
        prodAltNumSegs.add(prodAltNumSeg8);
        prodAltNumSegs.add(prodAltNumSeg9);
        prodAltNumSegs.add(prodAltNumSeg10);
        return prodAltNumSegs;
    }

    @Test
    void test_updateObjListAndFileInfo() {
        DataSiteEntity.DataFileInfo info = mock(DataSiteEntity.DataFileInfo.class);
        List<CustomizedEsIndexForProduct> objList = new ArrayList<>();
        List<CustomizedEsIndexForProduct> utList = new ArrayList<>();
        File mockFile = mock(File.class);
        when(info.getDataFile()).thenReturn(mockFile);
        when(mockFile.getName()).thenReturn("HK_HBAP_WMDS_SEC-HK_20230530081158_35.xml");
        assertDoesNotThrow(() -> underTest.updateObjListAndFileInfo(info, 10, false, objList, utList));
    }

    @Test
    void test_handleCommonLog() {
        DataSiteEntity entity = mock(DataSiteEntity.class);
        assertDoesNotThrow(() -> underTest.handleCommonLog(10, 1, 1, entity, "xxx"));
    }

    @Test
    void test_addToListIfValid() {
        List<CustomizedEsIndexForProduct> objList = new ArrayList<>();
        CustomizedEsIndexForProduct product = new CustomizedEsIndexForProduct();
        boolean[] errors = new boolean[]{false};
        assertDoesNotThrow(() -> underTest.addToListIfValid(objList, errors, product));
    }
}
