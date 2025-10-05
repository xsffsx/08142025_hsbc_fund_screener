
package com.hhhh.group.secwealth.mktdata.common.dao.impl;

import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

public class CustomerRoutingDataSource extends AbstractRoutingDataSource {
    private Map<Object, Object> targetDataSources;

    
    public void setTargetDataSources(final Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
        super.setTargetDataSources(targetDataSources);
        super.setDefaultTargetDataSource(targetDataSources.get(targetDataSources.keySet().toArray()[0]));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String siteKey = CustomerContextHolder.getSiteKeyFromHeaderMap();
        if (siteKey != null) {
            Object obj = this.targetDataSources.get(siteKey);
            if (obj == null) {
                LogUtil.error(CustomerRoutingDataSource.class, "Can not get target DataSource: {}", siteKey);
                throw new CommonException(ErrTypeConstants.SITE_NOT_SUPPORT);
            }
        }
        return siteKey;
    }
}