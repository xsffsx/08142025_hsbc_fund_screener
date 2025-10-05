/*
 */
package com.hhhh.group.secwealth.mktdata.starter.validation.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.starter.validation.constant.Constant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationEntitys {

    private final Map<String, ValidationEntity> validationEntityMap = new HashMap<>();

    private List<ValidationEntity> validationEntityList = new ArrayList<>();

    public void initValidationEntityMap() {
        if (this.validationEntityList != null && !this.validationEntityList.isEmpty()) {
            for (int i = 0; i < this.validationEntityList.size(); i++) {
                final ValidationEntity entity = this.validationEntityList.get(i);
                this.validationEntityMap.put(entity.getCountryCode() + Constant.SYMBOL_UNDERLINE + entity.getGroupMember(), entity);
            }
        }
    }

}
