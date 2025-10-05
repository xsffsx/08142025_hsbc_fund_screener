package com.hhhh.group.secwealth.mktdata.test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsHeadlineResponse {

    private List<NewsHeadline> newsList;

}
