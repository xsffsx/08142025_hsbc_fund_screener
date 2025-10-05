/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "site-feature")
@Getter
@Setter
public class SiteFeatureProperties {
	
	private Map<String,Map<String,String>> fundSwitch;
	
	private static final String DEFAULT_SITE = "DEFAULT";
	
	public String getStringDefaultFeature(final String site, final String key) {
		String value="";
		if(this.fundSwitch.get(site)!=null) {
			value=  this.fundSwitch.get(site).get(key);
		}
        if (StringUtil.isInvalid(value)) {
            value = this.fundSwitch.get(DEFAULT_SITE).get(key);
        }
        return value;
    }
	
}
