package com.dummy.wpb.product.model.graphql;

import lombok.Data;

@Data
public class LegacyConfigItem {
    private String key;
    private Object value;
    private String desc;
}
