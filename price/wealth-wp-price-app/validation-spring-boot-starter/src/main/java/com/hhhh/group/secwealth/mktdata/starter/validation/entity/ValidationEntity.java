/*
 */
package com.hhhh.group.secwealth.mktdata.starter.validation.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationEntity {

    private String countryCode;

    private String groupMember;

    private List<String> groups = new ArrayList<>();

}
