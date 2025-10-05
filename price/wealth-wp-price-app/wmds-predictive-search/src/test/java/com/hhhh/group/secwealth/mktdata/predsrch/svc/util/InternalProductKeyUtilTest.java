package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;

import com.hhhh.group.secwealth.mktdata.common.common.ProdCdeAltClassCdeEnum;
import com.hhhh.group.secwealth.mktdata.common.convertor.ServiceKeyToProdCdeAltClassCdeConvertor;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.system.constants.VendorType;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.PredictiveSearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class InternalProductKeyUtilTest {

    @Mock
    @Qualifier("predictiveSearchService")
    private PredictiveSearchService predictiveSearchService;

    @Mock
    @Qualifier("serviceKeyToProdCdeAltClassCdeConvertor")
    private ServiceKeyToProdCdeAltClassCdeConvertor prodCdeAltClassCdeConvertor;


    @InjectMocks
    private InternalProductKeyUtil internalProductKeyUtil;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProductInfo(){
        Boolean flag=false;
        try {
            Mockito.when(predictiveSearchService.getBySymbolMarket(Mockito.any(InternalProductKey.class), Mockito.any(String.class))).thenReturn(new SearchableObject());
            internalProductKeyUtil.getProductInfo(Mockito.any(InternalProductKey.class));
        } catch (Exception e) {
            flag=true;
        }
         org.junit.Assert.assertTrue(flag);
    }

    @Test
    public void testGetProductBySearchWithAltClassCde(){
        Boolean flag=true;
        try {

            Mockito.when(predictiveSearchService.getBySymbolMarket(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class),
                    Mockito.any(String.class), Mockito.any(String.class))).thenReturn(new SearchableObject());
            internalProductKeyUtil.getProductBySearchWithAltClassCde("M","countryCode"
                    ,"groupMember","prodAltNum","countryTradableCode","productType","locale");

            internalProductKeyUtil.getProductBySearchWithAltClassCde("T","countryCode"
                    ,"groupMember","prodAltNum","countryTradableCode","productType","locale");
            internalProductKeyUtil.getProductBySearchWithAltClassCde("W","countryCode"
                    ,"groupMember","prodAltNum","countryTradableCode","productType","locale");
            internalProductKeyUtil.getProductBySearchWithAltClassCde("R","countryCode"
                    ,"groupMember","prodAltNum","countryTradableCode","productType","locale");
        } catch (Exception e) {
            flag=false;
        }
         org.junit.Assert.assertTrue(flag);
    }

    @Test
    public void testGetProductBySearchWithAltClassCdeBranch(){
        Boolean flag=true;

        try {

            Mockito.when(predictiveSearchService.getBySymbolMarket(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class),
                    Mockito.any(String.class), Mockito.any(String.class))).thenReturn(new SearchableObject());
            Mockito.when(prodCdeAltClassCdeConvertor.doConvert(VendorType.MSTAR)).thenReturn("M");
            internalProductKeyUtil.getProductBySearchWithAltClassCde("M","countryCode"
                    ,"groupMember","prodAltNum","countryTradableCode","productType","locale");
        } catch (Exception e) {
            flag=false;
        }
         org.junit.Assert.assertTrue(flag);

    }

    @Test
    public void testGetProductBySearchWithAltClassCdeBranchByT(){
        Boolean flag=true;

        try {

            Mockito.when(predictiveSearchService.getBySymbolMarket(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class),
                    Mockito.any(String.class), Mockito.any(String.class))).thenReturn(new SearchableObject());
            Mockito.when(prodCdeAltClassCdeConvertor.doConvert(VendorType.MSTAR)).thenReturn("T");
            internalProductKeyUtil.getProductBySearchWithAltClassCde("M","countryCode"
                    ,"groupMember","prodAltNum","countryTradableCode","productType","locale");
        } catch (Exception e) {
            flag=false;
        }
         org.junit.Assert.assertTrue(flag);
    }

    @Test
    public void testgetProductCodeValueByCodeType(){
        Boolean flag=true;
        try {
            SearchableObject searchableObject = new SearchableObject();
            searchableObject.setProdAltNumSeg(new ArrayList<ProdAltNumSeg>(){{
                ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
                prodAltNumSeg.setProdCdeAltClassCde("T");
                add(prodAltNumSeg);
            }});
            internalProductKeyUtil.getProductCodeValueByCodeType(ProdCdeAltClassCdeEnum.T,searchableObject);
        } catch (Exception e) {
            flag=false;
        }
         org.junit.Assert.assertTrue(flag);
    }
    @Test
    public void testgetProductCodeValueByCodeTypeParamNull(){
        Boolean flag=true;
        try {
            internalProductKeyUtil.getProductCodeValueByCodeType(ProdCdeAltClassCdeEnum.T,null);
        } catch (Exception e) {
            flag=false;
        }
         org.junit.Assert.assertTrue(flag);
    }

}
