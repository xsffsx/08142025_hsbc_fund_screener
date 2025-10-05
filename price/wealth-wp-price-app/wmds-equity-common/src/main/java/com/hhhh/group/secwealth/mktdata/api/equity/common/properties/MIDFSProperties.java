/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.midfs.RequestPattern;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.midfs.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "midfs")
@Getter
@Setter
public class MIDFSProperties {

    private static final String DELAY = "delay";

    private static final String REALTIME = "realtime";

    private Map<String, Map<String, Map<String, String>>> commandId = new HashMap<>();

    private Map<String, String> companyField = new HashMap<>();

    private Map<String, RequestPattern> requestPattern = new HashMap<>();

    private ResponseStatus responseStatus;

    private Map<String, String> filedConvert = new HashMap<>();

    // seems config only contains delay ,no realtime.
    public String getCommandId(final String api, final String locale, final boolean delay) {
        return this.commandId.get(api).get(locale).get(delay ? MIDFSProperties.DELAY : MIDFSProperties.REALTIME);
    }

    public String getCompanyField(final String locale) {
        return this.companyField.get(locale);
    }

    public RequestPattern getRequestPattern(final String api) {
        return this.requestPattern.get(api);
    }

    public String getResponseStatusKey() {
        return this.responseStatus.getKey();
    }

    public boolean isCorrectResponseStatus(final String responseCode) {
        return this.responseStatus.getCorrectStatus().equals(responseCode);
    }

    public String getResponseMessage(final String responseCode) {
        return this.responseStatus.getValue().get(responseCode);
    }

    public String getFiledConvert(final String filed) {
        return this.filedConvert.get(filed);
    }

}
