/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean.Status;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.component.SpringBeansHolder;

import lombok.Setter;

public class HealthcheckService {

    @Setter
    private SpringBeansHolder holder;

    public List<Status> getStatus() {
        final List<Status> statusList = new ArrayList<>();
        final Map<String, Healthcheck> beans = this.holder.getBeansOfType(Healthcheck.class);
        if (beans != null) {
            for (final Map.Entry<String, Healthcheck> entry : beans.entrySet()) {
                statusList.addAll(entry.getValue().getStatus());
            }
        }
        return statusList;
    }

}
