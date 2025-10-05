package com.hhhh.group.secwealth.mktdata.common.util;


import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListUtilTest {

    @Test
    public void  testAll(){
        Boolean flag=true;
        try {
            List<InternalProductKey> req = new ArrayList() {{
               InternalProductKey param = new InternalProductKey();
               param.setCountryCode("2543");
               add(param);
           }};
            ListUtil.isValid(req);
            ListUtil.isInvalid(req);
            ListUtil.toString(req);
            ListUtil.toString(req,"[{\"countryCode\":\"2543\",\"groupMember\":null,\"countryTradableCode\":null,\"productType\":null,\"prodCdeAltClassCde\":null,\"prodAltNum\":null}]");
        } catch (Exception e) {
            flag=false;
        }
        Assert.assertTrue(flag);
    }
}
