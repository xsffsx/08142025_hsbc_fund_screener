package com.dummy.wmd.wpc.graphql.validator;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConditionalRules {
    private String condition;
    private List<Validator> validators = new ArrayList<>();
}
