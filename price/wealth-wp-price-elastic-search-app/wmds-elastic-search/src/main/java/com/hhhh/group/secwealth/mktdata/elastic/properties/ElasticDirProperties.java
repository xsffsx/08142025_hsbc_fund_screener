/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "elastic")
@Getter
@Setter
public class ElasticDirProperties {
	
	private Map<String,Map<String,String>> dirConfig;

}
