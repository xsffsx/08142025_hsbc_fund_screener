package com.dummy.wpb.product.model;

import lombok.Data;

import java.util.List;

@Data
public class RowData {
    private List<Object> data;
    private long rowNum;
}
