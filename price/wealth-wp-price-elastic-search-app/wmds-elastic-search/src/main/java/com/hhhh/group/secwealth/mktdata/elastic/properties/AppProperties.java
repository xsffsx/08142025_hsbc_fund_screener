/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.properties;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
	
	private List<String> supportSites;
	
	private List<String> localeMapping;
	
	private Map<String, String> localeMappingsByCountry;
	
	private boolean sedolQuery;
	
	
	@PostConstruct
	public void init() {
		this.localeMappingsByCountry = new ConcurrentHashMap<>();
		if (ListUtil.isValid(this.localeMapping)) {
			for (String s : this.localeMapping) {
				String[] mappings = s.split(":");
				this.localeMappingsByCountry.put(mappings[0], mappings[1]);
			}
		}
	}
	
	public int getNameByIndex(final String key) {
		if (null != this.localeMappingsByCountry.get(key)) {
			return Integer.parseInt(this.localeMappingsByCountry.get(key));
		} else {
			return 0;
		}
	}

}
