package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.util.List;

@Data
public class PageResult <T> {
    private Long total;
    private Integer skip;
    private Integer limit;
    private List<T> list;
}
