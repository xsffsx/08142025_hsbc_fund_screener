/*
 */
package com.hhhh.group.secwealth.mktdata.common.bmc;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;

public class SiteExceptionEntitys {

    private ConcurrentHashMap<String, SiteExceptionEntity> siteEntityMap = new ConcurrentHashMap<String, SiteExceptionEntity>();

    private List<SiteExceptionEntity> siteEntityList;

    private String bmcLogFilePath;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("siteEntityMap", this.siteEntityMap).append("siteEntityList", this.siteEntityList)
            .append("bmcLogFilePath", this.bmcLogFilePath).toString();
    }

    public void initSiteEntityMap() {
        if (null != this.siteEntityList) {
            for (int i = 0; i < this.siteEntityList.size(); i++) {
                SiteExceptionEntity entity = this.siteEntityList.get(i);
                entity.initCounterMap();
                this.siteEntityMap.put(entity.getPrefix() + entity.getCountryCode() + entity.getGroupMember(), entity);
            }
        }
    }

    public String getBmcFilePath(final String insName) {
        String path = this.bmcLogFilePath.replace(CommonConstants.SYMBOL_LEFT_BRACE + CommonConstants.ARGUMENT_INSTANCENAME
            + CommonConstants.SYMBOL_RIGHT_BRACE, insName);
        return path;
    }

    /**
     * @return the siteEntityMap
     */
    public ConcurrentHashMap<String, SiteExceptionEntity> getSiteEntityMap() {
        return this.siteEntityMap;
    }

    /**
     * @param siteEntityMap
     *            the siteEntityMap to set
     */
    public void setSiteEntityMap(final ConcurrentHashMap<String, SiteExceptionEntity> siteEntityMap) {
        this.siteEntityMap = siteEntityMap;
    }

    /**
     * @return the siteEntityList
     */
    public List<SiteExceptionEntity> getSiteEntityList() {
        return this.siteEntityList;
    }

    /**
     * @param siteEntityList
     *            the siteEntityList to set
     */
    public void setSiteEntityList(final List<SiteExceptionEntity> siteEntityList) {
        this.siteEntityList = siteEntityList;
    }

    /**
     * @return the bmcLogFilePath
     */
    public String getBmcLogFilePath() {
        return this.bmcLogFilePath;
    }

    /**
     * @param bmcLogFilePath
     *            the bmcLogFilePath to set
     */
    public void setBmcLogFilePath(final String bmcLogFilePath) {
        this.bmcLogFilePath = bmcLogFilePath;
    }
}
