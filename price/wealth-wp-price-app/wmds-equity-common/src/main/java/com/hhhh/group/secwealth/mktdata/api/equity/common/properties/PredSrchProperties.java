/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "multi-predsrch")
@Getter
@Setter
public class PredSrchProperties {

    private static final Logger logger = LoggerFactory.getLogger(PredSrchProperties.class);

    private String url;

    private String bodyPrefix;

    private PredSrchRequest requestParams;

    public String getPredSrchUrl() {
        if (StringUtil.isInValid(this.url)) {
            PredSrchProperties.logger
                .error("Please check your configuration: \"multi-predsrch.url\", the returned value is invalid");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_PREDSRCH_ILLEGAL_CONFIGURATION);
        }
        return this.url;
    }

    public String getPredSrchBodyPrefix() {
        if (StringUtil.isInValid(this.bodyPrefix)) {
            PredSrchProperties.logger
                .error("Please check your configuration: \"multi-predsrch.body-prefix\", the returned value is invalid");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_PREDSRCH_ILLEGAL_CONFIGURATION);
        }
        return this.bodyPrefix;
    }

}
