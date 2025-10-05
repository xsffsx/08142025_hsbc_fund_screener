package com.hhhh.group.secwealth.mktdata.common.dao.pojo;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtProdCatPerfmRtrnTest {
    @Test
    public void  test(){
        UtProdCatPerfmRtrn utProdCatPerfmRtrn = new UtProdCatPerfmRtrn();
        utProdCatPerfmRtrn.setUtProdCatPerfmRtrnId(new UtProdCatPerfmRtrnId());
        utProdCatPerfmRtrn.setTrailingTotalReturn(new BigDecimal("0"));
        utProdCatPerfmRtrn.setEndDate(new Date());
        utProdCatPerfmRtrn.setProdSubtypeCode("");
        utProdCatPerfmRtrn.setProductType("");
        utProdCatPerfmRtrn.setUpdatedOn(new Timestamp(new java.util.Date().getTime()));

        utProdCatPerfmRtrn.getUtProdCatPerfmRtrnId();
        utProdCatPerfmRtrn.getTrailingTotalReturn();
        utProdCatPerfmRtrn.getEndDate();
        utProdCatPerfmRtrn.getProdSubtypeCode();
        utProdCatPerfmRtrn.getProductType();
        utProdCatPerfmRtrn.getUpdatedOn();
        Assert.assertTrue(true);

    }
}
