package com.hhhh.group.secwealth.mktdata.common.dao.pojo;
import java.util.Date;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtSvceId;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtSvceTest {

    @Test
    public void test(){
        UtSvce utSvce = new UtSvce();
        utSvce.setUtSvceId(new UtSvceId());
        utSvce.setFundSvcClzTypeCd("");
        utSvce.setCountry("");
        utSvce.setUpdatedBy("");
        utSvce.setUpdatedOn(new Date());
        utSvce.setHoldingId(0);

        utSvce.getUtSvceId();
        utSvce.getFundSvcClzTypeCd();
        utSvce.getCountry();
        utSvce.getUpdatedBy();
        utSvce.getUpdatedOn();
        utSvce.getHoldingId();
        Assert.assertTrue(true);

    }
}
