package com.hhhh.group.secwealth.mktdata.elastic.configuration;

import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.xml.stream.XMLInputFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class DataHandlerConfig {

    @Value("${predsrch.dataTypeBySite}")
    private String dataTypeBySiteStr;

    @Bean
    public Map<String, String[]> dataTypeBySiteMap() {
        Map<String, String[]> dataTypeBySiteMap = new HashMap<>();
        // Config load data type by site
        if (StringUtil.isValid(this.dataTypeBySiteStr)) {
            String[] dataTypeBySites = this.dataTypeBySiteStr.split(CommonConstants.SYMBOL_SEPARATOR);
            if (null != dataTypeBySites && dataTypeBySites.length > 0) {
                for (String siteDataTypeStr : dataTypeBySites) {
                    this.updateDataTypeBySiteMap(siteDataTypeStr, dataTypeBySiteMap);
                }
            }
        }
        return dataTypeBySiteMap;
    }

    @Bean
    public XMLInputFactory xmlInputFactory() {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        return xmlInputFactory;
    }

    private void updateDataTypeBySiteMap(String siteDataTypeStr, Map<String, String[]> dataTypeBySiteMap) {
        if (StringUtil.isValid(siteDataTypeStr)) {
            String[] siteDataTypes = siteDataTypeStr.split(CommonConstants.SYMBOL_COLON);
            if (ArrayUtils.isNotEmpty(siteDataTypes) && StringUtil.isValid(siteDataTypes[1])) {
                String[] dataTypes = siteDataTypes[1].split(CommonConstants.SYMBOL_COMMA);
                if (null != dataTypes && dataTypes.length > 0) {
                    dataTypeBySiteMap.put(siteDataTypes[0], dataTypes);
                }
            }
        }
    }
}
