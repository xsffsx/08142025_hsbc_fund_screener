/*
 */
package com.hhhh.group.secwealth.mktdata.starter.datasource.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "datasource")
@Getter
@Setter
public class DatasourceProperties {

	private String name = "";

	private String jndiName = "";

	private String defaultName = "";

	private String pattern = "X-hhhh-Chnl-CountryCode~~<_>~~X-hhhh-Chnl-Group-Member~~HEADER";

	private String urlPatterns = "/wealth/api/v1/market-data/**";

	private String defaultExCode = "IllegalConfiguration";

	private Map<String, Map<String, String>> multipleSources;

}
