/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "vendor")
@Getter
@Setter
public class VenderProdCdeAltClassCdeProp {

	private Map<String, String> venderProdCdeAltClassCdeMap = new HashMap<>();

}
