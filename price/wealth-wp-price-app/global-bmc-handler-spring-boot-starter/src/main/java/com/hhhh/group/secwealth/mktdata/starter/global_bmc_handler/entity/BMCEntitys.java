/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.constant.Constant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BMCEntitys {

    private final Map<String, BMCEntity> bmcEntityMap = new HashMap<>();
    private List<BMCEntity> bmcEntityList = new ArrayList<>();

    private String bmcFilePath;
    private String printFiles;

    public void initBMCEntityMap() {
        if (this.bmcEntityList != null && !this.bmcEntityList.isEmpty()) {
            for (int i = 0; i < this.bmcEntityList.size(); i++) {
                final BMCEntity entity = this.bmcEntityList.get(i);
                entity.initExceptionCounterMap();
                if (StringUtils.isEmpty(entity.getPrefix())) {
                    this.bmcEntityMap.put(entity.getCountryCode() + Constant.SYMBOL_UNDERLINE + entity.getGroupMember(), entity);
                } else {
                    this.bmcEntityMap.put(entity.getPrefix(), entity);
                }
            }
        }
    }

}
