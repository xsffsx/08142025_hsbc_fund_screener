/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean.Status;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.Healthcheck;

public class ApplicationHealthcheckService implements Healthcheck {

    private static final String HEALTHCHECK_APPLICATION = "APPLICATION";

    @Override
    public List<Status> getStatus() {
        final List<Status> statusList = new ArrayList<>();
        statusList.add(new Status(Status.SUCCESS, ApplicationHealthcheckService.HEALTHCHECK_APPLICATION, 0));
        return statusList;
    }

}
