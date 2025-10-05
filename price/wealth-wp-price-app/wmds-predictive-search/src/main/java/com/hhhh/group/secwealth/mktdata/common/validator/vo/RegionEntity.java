package com.hhhh.group.secwealth.mktdata.common.validator.vo;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegionEntity implements Serializable {

    private static final long serialVersionUID = 7846222014934471231L;

    private String siteKey;
    private Map<String, ServiceEntity> serviceEntities = new ConcurrentHashMap<String, ServiceEntity>();

    public String getSiteKey() {
        return this.siteKey;
    }

    public void setSiteKey(final String siteKey) {
        this.siteKey = siteKey;
    }

    public Map<String, ServiceEntity> getServiceEntities() {
        return this.serviceEntities;
    }

    public void setServiceEntities(final Map<String, ServiceEntity> serviceEntities) {
        this.serviceEntities = serviceEntities;
    }

}
