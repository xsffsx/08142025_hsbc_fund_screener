package com.hhhh.group.secwealth.mktdata.api.equity.chart.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service("labciChartHaseUS4CmbService")
@ConditionalOnProperty(value = "service.chart.Labci.injectEnabled")
public class LabciChartHaseUS4CmbService extends LabciChartHaseUSService {

    @Autowired
    @Getter
    @Setter
    @Qualifier("labciProtal4CmbProperties")
    private LabciProtalProperties labciProtalProperties;

    /**
     * Get data from TRIS by HTTP call Put result into map<symbol, jsonResult>
     */
    @Override
    protected Object execute(final Object serviceRequest) {
        super.setLabciProtalProperties(this.labciProtalProperties);
        return super.execute(serviceRequest);
    }
}