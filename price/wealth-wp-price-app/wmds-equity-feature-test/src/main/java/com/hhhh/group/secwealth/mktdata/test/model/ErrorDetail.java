package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.hhhh.group.secwealth.mktdata.test.utils.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {

    @JsonAlias("Status code")
    private int statusCode;

    @JsonAlias("Text")
    private String text;

    @JsonAlias("Reason code")
    private String reasonCode;

    public ErrorDetail(HttpClientErrorException exception){
        this.statusCode = exception.getStatusCode().value();
        String response = exception.getResponseBodyAsString();
        Map<String, String> errorMap = JSONUtil.readValue(response, HashMap.class);
        this.text = errorMap.get("text");
        this.reasonCode = errorMap.get("reasonCode");
    }

}
