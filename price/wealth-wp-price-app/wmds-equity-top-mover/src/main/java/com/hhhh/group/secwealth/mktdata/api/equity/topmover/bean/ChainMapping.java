package com.hhhh.group.secwealth.mktdata.api.equity.topmover.bean;

import java.io.Serializable;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app")
public class ChainMapping implements Serializable {

    private static final long serialVersionUID = 523594807513144612L;
    private String mappingRule;
    private List<String> fields;

    @Override
    public String toString() {
        return "ChainMapping [mappingRule=" + this.mappingRule + ", fields=" + this.fields + "]";
    }

}
