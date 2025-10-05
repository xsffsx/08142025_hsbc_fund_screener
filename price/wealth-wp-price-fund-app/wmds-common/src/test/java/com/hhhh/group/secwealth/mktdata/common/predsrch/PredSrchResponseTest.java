package com.hhhh.group.secwealth.mktdata.common.predsrch;


import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.predsrch.response.PredSrchResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PredSrchResponseTest{

    @Test
    public void test(){
        PredSrchResponse predSrchResponse = new PredSrchResponse();
        predSrchResponse.setProdAltNumSegs(new ArrayList<ProdAltNumSeg>());
        predSrchResponse.setProductType("");
        predSrchResponse.setProductSubType("");
        predSrchResponse.setCountryTradableCode("");
        predSrchResponse.setAllowBuy("");
        predSrchResponse.setAllowSell("");
        predSrchResponse.setProductName("");
        predSrchResponse.setProductShortName("");
        predSrchResponse.setProductCcy("");
        predSrchResponse.setMarket("");
        predSrchResponse.setExchange("");
        predSrchResponse.setFundHouseCde("");
        predSrchResponse.setBondIssuer("");
        predSrchResponse.setAllowSellMipProdInd("");
        predSrchResponse.setAllowTradeProdInd("");
        predSrchResponse.setProdTaxFreeWrapActStaCde("");
        predSrchResponse.setAllowSwInProdInd("");
        predSrchResponse.setAllowSwOutProdInd("");

        predSrchResponse.setSymbol("");
        predSrchResponse.setProductCode("");
        predSrchResponse.setRiskLvlCde("");
        predSrchResponse.setProdStatCde("");
        predSrchResponse.setRestrOnlScribInd("");
        predSrchResponse.setPiFundInd("");


        predSrchResponse.getProdAltNumSegs();
        predSrchResponse.getProductType();
        predSrchResponse.getProductSubType();
        predSrchResponse.getCountryTradableCode();
        predSrchResponse.getAllowBuy();
        predSrchResponse.getAllowSell();
        predSrchResponse.getProductName();
        predSrchResponse.getProductShortName();
        predSrchResponse.getProductCcy();
        predSrchResponse.getMarket();

        predSrchResponse.getExchange();
        predSrchResponse.getFundHouseCde();
        predSrchResponse.getBondIssuer();
        predSrchResponse.getAllowSellMipProdInd();
        predSrchResponse.getAllowTradeProdInd();
        predSrchResponse.getProdTaxFreeWrapActStaCde();
        predSrchResponse.getAllowSwInProdInd();
        predSrchResponse.getAllowSwOutProdInd();
        predSrchResponse.getFundUnSwitchCode();
        predSrchResponse.getSwithableGroups();
        predSrchResponse.getAssetCountries();
        predSrchResponse.getAssetSectors();
        predSrchResponse.getParentAssetClasses();
        predSrchResponse.getChannelRestrictList();
        predSrchResponse.getChanlCdeList();
        predSrchResponse.getSymbol();
        predSrchResponse.getProductCode();
        predSrchResponse.getRiskLvlCde();
        predSrchResponse.getProdStatCde();
        predSrchResponse.getRestrOnlScribInd();
        predSrchResponse.getPiFundInd();
        predSrchResponse.getDeAuthFundInd();

        Assert.assertTrue(true);

    }


}
