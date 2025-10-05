/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * <p>
 * <b> Provides a series of methods to get Labci related attribute values. </b>
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "labci-protal-cmb")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
@Getter
@Setter
public class LabciProtal4CmbProperties extends LabciProtalProperties {

	private static final Logger logger = LoggerFactory.getLogger(LabciProtal4CmbProperties.class);

}
