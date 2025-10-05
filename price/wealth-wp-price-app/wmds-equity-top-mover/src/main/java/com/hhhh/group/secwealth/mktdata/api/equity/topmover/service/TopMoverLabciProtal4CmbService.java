/**
 * @Title TopMoverLabciService.java
 * @description TODO
 * @author OJim
 * @time Jun 27, 2019 7:45:05 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service("labciProtalTopmover4CmbService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class TopMoverLabciProtal4CmbService extends TopMoverLabciProtalService {


    private static final Logger logger = LoggerFactory.getLogger(TopMoverLabciProtal4CmbService.class);

    @Autowired
    @Getter
    @Setter
    @Qualifier("labciProtal4CmbProperties")
    private LabciProtalProperties labciProtalProperties;

    @SuppressWarnings("unchecked")
    @Override
    protected Object execute(final Object serviceRequest) throws Exception {

        super.setLabciProtalProperties(this.labciProtalProperties);

        return super.execute(serviceRequest);
    }
}
