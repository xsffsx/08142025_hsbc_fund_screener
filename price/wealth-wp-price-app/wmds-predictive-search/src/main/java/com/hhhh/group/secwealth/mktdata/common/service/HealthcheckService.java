/*
 */
package com.hhhh.group.secwealth.mktdata.common.service;


public interface HealthcheckService {

    public String getSystemTime() throws Exception;

    public boolean authenticate(String userid, String password);

    public String healthDashboardSite(/* String samlString */);
}
