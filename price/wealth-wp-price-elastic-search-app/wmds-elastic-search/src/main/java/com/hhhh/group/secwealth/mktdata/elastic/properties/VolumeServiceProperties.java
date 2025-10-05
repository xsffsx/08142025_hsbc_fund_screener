/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.properties;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "volume")
@Getter
@Setter
public class VolumeServiceProperties {

	private static Logger logger = LoggerFactory.getLogger(VolumeServiceProperties.class);

	public static final String DEFAULT = "DEFAULT";
	public static final String REMOTE_FILE_PATH_KEY = "remoteFilePath";
	public static final String PRIVATEKEY_PATH_KEY = "privateKeyPath";
    public static final String PRIVATEKEY_ID_KEY = "privateKeyId";
	public static final String REMOTE_FILE_DIR_KEY = "remoteFileDir";

	public static final String VOLUME_SERVICE_ENABLED = "enabled";
	public static final String VOLUME_SERVICE_DATATYPE = "dataType";
	public static final String PREDSRCH_IS_VERIFY = "isVerify";

	private Map<String, Map<String, String>> site;

	public String getValue(final String identifier, final String key) {
		if (this.site != null) {
			logger.debug("identifier: {}, key: {}", identifier, key);
			if (this.site.containsKey(identifier.trim()) && this.site.get(identifier.trim()).containsKey(key)) {
				return this.site.get(identifier).get(key);
			} else if (this.site.containsKey(VolumeServiceProperties.DEFAULT)
					&& this.site.get(VolumeServiceProperties.DEFAULT).containsKey(key)) {
				return this.site.get(VolumeServiceProperties.DEFAULT).get(key);
			} else {
				return null;
			}
		} else {
			logger.error("VolumeServiceConfig Property Confifuration is not correct");
			throw new ApplicationException(ExCodeConstant.ERRORMSG_VOLUMESERVICECONFIGERR);
		}
	}

}
