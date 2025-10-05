package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.util.List;

@Data
public class Pagination<T> {
    private List<T> resultList;
    private Long total;
}
