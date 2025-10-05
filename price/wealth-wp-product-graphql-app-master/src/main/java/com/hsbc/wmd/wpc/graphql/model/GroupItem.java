package com.dummy.wmd.wpc.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupItem {
    private String code;
    private String name;
    private int count;

    public GroupItem(String code, Integer count) {
        this.code = code;
        this.count = count;
    }
}
