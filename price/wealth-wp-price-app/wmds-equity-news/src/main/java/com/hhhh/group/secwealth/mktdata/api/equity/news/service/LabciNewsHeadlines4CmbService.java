package com.hhhh.group.secwealth.mktdata.api.equity.news.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "service.newsHeadlines.Labci.injectEnabled")
public class LabciNewsHeadlines4CmbService extends LabciNewsHeadlinesService {

    private static final Logger logger = LoggerFactory.getLogger(LabciNewsHeadlines4CmbService.class);

    @Autowired
    @Getter
    @Setter
    @Qualifier("labciProtal4CmbProperties")
    private LabciProtalProperties labciProtalProperties;

    @Override
    protected Object execute(Object obj) throws Exception {
        super.setLabciProtalProperties(this.labciProtalProperties);
        return super.execute(obj);
    }
}
