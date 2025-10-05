package com.dummy.wpb.product.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExcelColumnInfo implements Serializable {
    private String attrName;
    private String jsonPath;
    private String dataType;
    private String defaultValue;
    private String dateFormat;
    private String timeZone;
    private String value;

    private String desc;
    private String excelType;
    private String remark;
    private Boolean mandatory;
    private Integer column;


}
