package com.dummy.wpb.wpc.utils.model;

import lombok.Data;

import java.util.List;

@Data
public class MongoIndexInfo {
    private String collection;
    private String name;
    private Integer order;
    private List<String> columns;
}
