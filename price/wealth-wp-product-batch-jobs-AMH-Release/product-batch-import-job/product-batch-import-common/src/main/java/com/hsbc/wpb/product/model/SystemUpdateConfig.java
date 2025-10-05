package com.dummy.wpb.product.model;

import lombok.Data;

import java.util.List;

@Data
public class SystemUpdateConfig {

    private String systemCde;

    List<UpdateConfig> config;

    @Data
    public static class UpdateConfig {
        private String collection;
        private String typeCde;
        private List<String> updateAttrs;
    }
}
