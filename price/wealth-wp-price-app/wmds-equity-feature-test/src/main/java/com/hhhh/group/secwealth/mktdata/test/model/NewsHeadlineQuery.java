package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewsHeadlineQuery {

    @JsonAlias("Market")
    private String market;

    @JsonAlias("Category")
    private String category;

    @JsonAlias("Product code indicator")
    private String productCodeIndicator;

    @JsonAlias("Symbol")
    private List<String> symbol;

    @JsonAlias("Page id")
    private String pageId;

    @JsonAlias("Records per page")
    private String recordsPerPage;

}
