package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class MD5UtilTest {

    @Test
    public void  testGetMd5ByFile(){
        Boolean flag=true;
        try {
            MD5Util.getMd5ByFile(new File("./test/md5.test"));
        } catch (Exception e) {
            flag=false;
        }
         org.junit.Assert.assertFalse(flag);
    }

}
