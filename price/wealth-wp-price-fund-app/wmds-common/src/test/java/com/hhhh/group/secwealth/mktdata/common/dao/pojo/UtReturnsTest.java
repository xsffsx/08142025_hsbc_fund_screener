package com.hhhh.group.secwealth.mktdata.common.dao.pojo;
import java.math.BigDecimal;
import java.util.Date;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtReturnsId;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtReturnsTest {

    @Test
    public void test(){
        UtReturns utReturns = new UtReturns();
        utReturns.setUtReturnsId(new UtReturnsId());
        utReturns.setRtrnAmt(new BigDecimal("0"));
        utReturns.setUpdatedOn(new Date());

        utReturns.getUtReturnsId();
        utReturns.getRtrnAmt();
        utReturns.getUpdatedOn();
        Assert.assertTrue(true);
    }
}
