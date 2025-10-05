package com.dummy.wpb.product.model;

import lombok.Data;

import java.util.Map;

@Data
public class SystemDefaultValues {

    private String systemCde;

    private String typeCde;

    private Map<String,Object> defaultValues;

}
