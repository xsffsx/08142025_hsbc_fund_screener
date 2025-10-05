/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExResponseEntitys {

    private Map<String, ExResponseEntity> exResponseEntityMap = new HashMap<>();

}
