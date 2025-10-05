package com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl;

import com.hhhh.group.secwealth.mktdata.predsrch.svc.cache.LabciSymbolSearchCache;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.SymbolSearchCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("symbolSearchCacheService")
public class SymbolSearchCacheServiceImpl implements SymbolSearchCacheService {

    private static final Logger logger = LoggerFactory.getLogger(SymbolSearchCacheServiceImpl.class);
    @Override
    public void cleanupAll() {
        LabciSymbolSearchCache labciSymbolSearchCache = LabciSymbolSearchCache.getInstance();
        labciSymbolSearchCache.removeAll();
        logger.info("Cleanup the Symbol Search Cache.");
    }
}
