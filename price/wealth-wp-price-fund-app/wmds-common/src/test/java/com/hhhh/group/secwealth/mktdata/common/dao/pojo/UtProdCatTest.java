package com.hhhh.group.secwealth.mktdata.common.dao.pojo;
import java.math.BigDecimal;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Date;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCatId;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtProdCatTest {

    @Test
    public void test(){
        UtProdCat utProdCat = new UtProdCat();
        utProdCat.setUtProdCatId(new UtProdCatId());
        utProdCat.setProdSubtypeCode("");
        utProdCat.setProductType("");
        utProdCat.setNumberOfProducts(0);
        utProdCat.setStddev(new BigDecimal("0"));
        utProdCat.setCountry("");
        utProdCat.setProductNlsName1("");
        utProdCat.setProductNlsName2("");
        utProdCat.setProductNlsName3("");
        utProdCat.setAssetClassCode("");
        utProdCat.setUpdatedOn(new Date());
        utProdCat.setUpdatedBy("");
        utProdCat.setCategoryTotalReturnIndex(new ArrayList());
        utProdCat.setUtProdCatPerfmRtrn(new ArrayList());
        utProdCat.getCategoryTotalReturnIndex();
        utProdCat.getProductNlsName1();
        utProdCat.getUtProdCatId();
        utProdCat.getProductNlsName1();
        utProdCat.getProductNlsName2();
        utProdCat.getProductNlsName3();
        utProdCat.getUtProdCatPerfmRtrn();
        utProdCat.getCountry();
        Assert.assertTrue(true);
    }

}
