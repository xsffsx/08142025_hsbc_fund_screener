/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response;

import java.net.URL;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntitys;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.util.converter.CastorConverter;

import lombok.Getter;
import lombok.Setter;

public class ExResponseComponent {

    private Map<String, ExResponseEntity> exResponseEntityMap;

    @Setter
    private String exResponsePath;

    @Setter
    private String exResponseMapping;

    @Setter
    private String exResponseConfig;

    @Setter
    @Getter
    private String defaultExCode;

    @PostConstruct
    public void init() throws Exception {
        final ResourceLoader loader = new DefaultResourceLoader();
        final URL mappingURL = loader.getResource(this.exResponsePath + this.exResponseMapping).getURL();
        final URL configURL = loader.getResource(this.exResponsePath + this.exResponseConfig).getURL();
        final ExResponseEntitys entitys =
            (ExResponseEntitys) CastorConverter.convertXMLToBean(mappingURL, configURL, ExResponseEntitys.class, true);
        this.exResponseEntityMap = entitys.getExResponseEntityMap();
    }

    public ExResponseEntity getDefaultExResponse() {
        return getExResponse(this.defaultExCode);
    }

    public ExResponseEntity getExResponse(final String exCode) {
        ExResponseEntity entity = this.exResponseEntityMap.get(exCode);
        if (entity == null) {
            entity = this.exResponseEntityMap.get(this.defaultExCode);
        }
        return entity;
    }

}
