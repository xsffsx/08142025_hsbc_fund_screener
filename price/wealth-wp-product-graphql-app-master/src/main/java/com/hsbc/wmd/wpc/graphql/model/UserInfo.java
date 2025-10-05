package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserInfo implements Serializable {
    private String id;
    private String name;
    private List<String> roles;
    private List<String> groups = new ArrayList<>();

    public UserInfo(String id, String name, List<String> groups) {
        this.id = id;
        this.name = name;
        this.groups = groups;
    }
}
