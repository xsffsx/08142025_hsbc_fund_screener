/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.SearchCriteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PredSrchRequest {

    private String[] keyword;

    private String[] assetClasses;

    private String[] searchFields;

    private String[] sortingFields;

    private String market;

    private List<SearchCriteria> filterCriterias;

    private String topNum;

    private String channelRestrictCode;

    private String restrOnlScribInd;

    private boolean isPreciseSrch;

}
