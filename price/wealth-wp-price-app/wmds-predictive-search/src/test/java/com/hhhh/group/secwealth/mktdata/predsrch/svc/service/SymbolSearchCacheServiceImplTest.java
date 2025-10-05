package com.hhhh.group.secwealth.mktdata.predsrch.svc.service;

import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.cache.LabciSymbolSearchCache;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl.SymbolSearchCacheServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class SymbolSearchCacheServiceImplTest {

    @Test
    public void testCleanupAll() {
        LabciSymbolSearchCache labciSymbolSearchCache = LabciSymbolSearchCache.getInstance();
        PredSrchResponse predSrchResponse = new PredSrchResponse();
        predSrchResponse.setSymbol("NCNC");

        labciSymbolSearchCache.add("NCNC", predSrchResponse, 3L, TimeUnit.SECONDS);
        SymbolSearchCacheServiceImpl symbolSearchCacheService = new SymbolSearchCacheServiceImpl();
        symbolSearchCacheService.cleanupAll();

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        labciSymbolSearchCache.getCacheSize();
        Assert.assertNull(labciSymbolSearchCache.get("NCNC"));
    }
}
