/*
 */
package com.hhhh.group.secwealth.mktdata.common.filter.e2etrust.properties;

import java.util.HashMap;
import java.util.Map;


public class Parameters {

    private String nameId = "customerId";
    private String channelId = "channelId";
    private String guid = "guid"; // tbc
    private String userId = "staffId";
    private String roles = "roles"; // tbc
    private String deviceId = "userDeviceId";
    private String ipId = "staffId"; // tbc
    private String delegateId = "delegateId"; // tbc
    private String cam = "camText"; // tbc
    private String companyId = "companyId"; // tbc
    private String appId = "channelAppId";
    private String ip = "ip"; // tbc
    private String authenticationContext = "authenticationContext";
    // attribute name to http request parameter name
    private Map<String, String> customAttributes = new HashMap<>();
    /**
     * @return the nameId
     */
    public String getNameId() {
        return this.nameId;
    }
    /**
     * @param nameId the nameId to set
     */
    public void setNameId(final String nameId) {
        this.nameId = nameId;
    }
    /**
     * @return the channelId
     */
    public String getChannelId() {
        return this.channelId;
    }
    /**
     * @param channelId the channelId to set
     */
    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }
    /**
     * @return the guid
     */
    public String getGuid() {
        return this.guid;
    }
    /**
     * @param guid the guid to set
     */
    public void setGuid(final String guid) {
        this.guid = guid;
    }
    /**
     * @return the userId
     */
    public String getUserId() {
        return this.userId;
    }
    /**
     * @param userId the userId to set
     */
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    /**
     * @return the roles
     */
    public String getRoles() {
        return this.roles;
    }
    /**
     * @param roles the roles to set
     */
    public void setRoles(final String roles) {
        this.roles = roles;
    }
    /**
     * @return the deviceId
     */
    public String getDeviceId() {
        return this.deviceId;
    }
    /**
     * @param deviceId the deviceId to set
     */
    public void setDeviceId(final String deviceId) {
        this.deviceId = deviceId;
    }
    /**
     * @return the ipId
     */
    public String getIpId() {
        return this.ipId;
    }
    /**
     * @param ipId the ipId to set
     */
    public void setIpId(final String ipId) {
        this.ipId = ipId;
    }
    /**
     * @return the delegateId
     */
    public String getDelegateId() {
        return this.delegateId;
    }
    /**
     * @param delegateId the delegateId to set
     */
    public void setDelegateId(final String delegateId) {
        this.delegateId = delegateId;
    }
    /**
     * @return the cam
     */
    public String getCam() {
        return this.cam;
    }
    /**
     * @param cam the cam to set
     */
    public void setCam(final String cam) {
        this.cam = cam;
    }
    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return this.companyId;
    }
    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(final String companyId) {
        this.companyId = companyId;
    }
    /**
     * @return the appId
     */
    public String getAppId() {
        return this.appId;
    }
    /**
     * @param appId the appId to set
     */
    public void setAppId(final String appId) {
        this.appId = appId;
    }
    /**
     * @return the ip
     */
    public String getIp() {
        return this.ip;
    }
    /**
     * @param ip the ip to set
     */
    public void setIp(final String ip) {
        this.ip = ip;
    }
    /**
     * @return the authenticationContext
     */
    public String getAuthenticationContext() {
        return this.authenticationContext;
    }
    /**
     * @param authenticationContext the authenticationContext to set
     */
    public void setAuthenticationContext(final String authenticationContext) {
        this.authenticationContext = authenticationContext;
    }
    /**
     * @return the customAttributes
     */
    public Map<String, String> getCustomAttributes() {
        return this.customAttributes;
    }
    /**
     * @param customAttributes the customAttributes to set
     */
    public void setCustomAttributes(final Map<String, String> customAttributes) {
        this.customAttributes = customAttributes;
    }

}
