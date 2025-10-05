package com.hhhh.group.secwealth.mktdata.api.equity.index.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service("labciProtalIndices4CmbService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class IndexLabciProtal4CmbService
        extends IndexLabciProtalService {

    private static final Logger logger = LoggerFactory.getLogger(IndexLabciProtal4CmbService.class);

    @Autowired
    @Getter
    @Setter
    @Qualifier("labciProtal4CmbProperties")
    private LabciProtalProperties labciProtalProperties;

    @Override
    protected Object execute(final Object serviceRequest) throws Exception {
        super.setLabciProtalProperties(this.labciProtalProperties);
        return super.execute(serviceRequest);
    }
}
