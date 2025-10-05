package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstmId;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.OtherFundClassesRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.OtherFundClassesResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.otherfundclass.AssetClass;
import com.hhhh.group.secwealth.mktdata.fund.dao.OtherFundClassesDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtherFundClassesServiceImplTest {

    @Mock
    private OtherFundClassesDao otherFundClassesDao;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private OtherFundClassesResponse response;

    @InjectMocks
    private OtherFundClassesServiceImpl underTest;

    @Nested
    class WhenExecuting {
        @Mock
        private OtherFundClassesRequest request;
        @Mock
        private SearchProduct searchProduct;
        @Mock
        private UtProdInstm utProdInstm;

        @BeforeEach
        void setup() throws Exception{
            Mockito.when(request.getLocale()).thenReturn("en");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            searchProduct = DataBuilder.buildSearchProduct();
            Mockito.when(otherFundClassesDao.getUtProdInstmList(any(OtherFundClassesRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList());
            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(searchProduct);
//            Mockito.when(underTest.execute(request)).thenReturn(DataBuilder.buildReponse());
        }
        @Test
         void test_WhenExecuting() throws Exception {
            underTest.execute(request);
            Assertions.assertEquals("HK",request.getCountryCode());
        }
    }

    private static class DataBuilder {

        public static OtherFundClassesResponse buildReponse(){
            OtherFundClassesResponse response = new OtherFundClassesResponse();
            List<AssetClass> assetClasses = new ArrayList<>();
            AssetClass assetClass = new AssetClass();
            assetClass.setFundShreClsName("Bocom Schroders Growth");
            assetClass.setProdStatCde("A");
            assetClass.setProductName("Bocom Schroders Growth Balanced Fund");
            assetClass.setProductShortName("Bocom");
            List<ProdAltNumSeg> prodAltNumSegList = new ArrayList<>();
            ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
            prodAltNumSeg.setProdAltNum("60002195");
            prodAltNumSeg.setProdCdeAltClassCde("W");
            prodAltNumSegList.add(prodAltNumSeg);
            assetClass.setProdAltNumSegs(prodAltNumSegList);
            assetClasses.add(assetClass);
            response.setAssetClasses(assetClasses);
            return response;
        }

        public static List<UtProdInstm> buildUtProdInstmList(){
            List<UtProdInstm> utProdInstmList  = new ArrayList<>();
            UtProdInstm utProdInstm =  new UtProdInstm();
            UtProdInstmId utProdInstmId = new UtProdInstmId();
            utProdInstmId.setProductId(2);
            utProdInstmId.setBatchId(4);
            utProdInstm.setMarket("HK");
            utProdInstm.setProductType("UT");
            utProdInstm.setAllowReguCntbInd("Y");
            utProdInstm.setUtProdInstmId(utProdInstmId);
            utProdInstm.setPerformanceId("0P00007KN5");
            utProdInstm.setSymbol("540002");
            utProdInstm.setCcyProdTradeCde("HKD");
            utProdInstm.setCcyInvstCde("HKD");
            utProdInstmList.add(utProdInstm);
            return utProdInstmList;
        }

        public static SearchProduct buildSearchProduct(){
            SearchProduct searchProduct = new SearchProduct();
            searchProduct.setExternalKey("0P0001BFG7");
            SearchableObject searchObject = new SearchableObject();
            searchObject.setSymbol("540002");
            searchProduct.setSearchObject(searchObject);
            return searchProduct;
        }
    }
}