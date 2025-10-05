package com.dummy.wmd.wpc.graphql.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Setter
@ToString
public class CustEligConfig {
    @Getter
    private Map<String, Object> tradeElig;
    private List<Map<String, String>> restrCustCtry;
    private List<Map<String, String>> restrCustGroup;
    private List<Map<String, String>> formReqmt;

    public List<Map<String, String>> getRestrCustCtry() {
        return Objects.isNull(restrCustCtry) ? Collections.emptyList() : restrCustCtry;
    }

    public List<Map<String, String>> getRestrCustGroup() {
        return Objects.isNull(restrCustGroup) ? Collections.emptyList() : restrCustGroup;
    }

    public List<Map<String, String>> getFormReqmt() {
        return Objects.isNull(formReqmt) ? Collections.emptyList() : formReqmt;
    }
}
