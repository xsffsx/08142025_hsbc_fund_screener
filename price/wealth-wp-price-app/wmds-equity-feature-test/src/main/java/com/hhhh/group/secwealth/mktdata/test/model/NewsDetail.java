package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsDetail {

    @JsonAlias("News id")
    private String id;

    @JsonAlias("Headline")
    private String headline;

    @JsonAlias("Content")
    private String content;

    @JsonAlias("As of date time")
    private String asOfDateTime;

}
