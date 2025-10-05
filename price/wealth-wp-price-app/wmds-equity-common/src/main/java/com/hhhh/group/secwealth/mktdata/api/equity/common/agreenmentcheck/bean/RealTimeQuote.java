package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean;

import java.io.Serializable;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app.real-time-quote")
@Getter
@Setter
/**
 *
 * <p>
 * <b> The bean for real time quote configuration. </b>
 * </p>
 */
public class RealTimeQuote implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -254649380099861295L;

    /**
     * This flag meas can invoke real time quote or not.
     */
    private boolean enable = false;

    /**
     * The exchange list which need to do agreement check.
     */
    private List<String> exchangeCodes;
}
