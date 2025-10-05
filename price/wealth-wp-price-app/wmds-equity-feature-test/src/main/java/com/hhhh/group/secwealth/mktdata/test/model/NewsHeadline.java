package com.hhhh.group.secwealth.mktdata.test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsHeadline {

    private String id;

    private String source;

    private String headline;

    private String brief;

    private String asOfDateTime;

}
