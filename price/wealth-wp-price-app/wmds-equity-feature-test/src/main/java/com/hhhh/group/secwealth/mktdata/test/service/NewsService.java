package com.hhhh.group.secwealth.mktdata.test.service;

import com.hhhh.group.secwealth.mktdata.test.model.*;
import com.hhhh.group.secwealth.mktdata.test.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
public class NewsService extends BaseService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mds.news.detail-uri:}")
    String newsDetailUri;

    @Value("${mds.news.headlines-uri:}")
    String newsHeadlinesUri;

    public NewsDetail queryNewsDetail(NewsDetailQuery query) {
        HttpEntity<String> entity = getBaseHttpEntity();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(newsDetailUri);
        uriBuilder.queryParam("param", JSONUtil.toString(query));
        log.info("Query news detail request: {}", JSONUtil.toString(query));
        NewsDetail detail = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, NewsDetail.class).getBody();
        log.debug("Query news detail response: {}", JSONUtil.toString(detail));
        return detail;
    }

    public List<NewsHeadline> queryNewsHeadlines(NewsHeadlineQuery query) {
        HttpEntity<String> entity = getBaseHttpEntity();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(newsHeadlinesUri);
        uriBuilder.queryParam("param", JSONUtil.toString(query));
        log.info("Query news headlines request: {}", JSONUtil.toString(query));
        List<NewsHeadline> list = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, NewsHeadlineResponse.class).getBody().getNewsList();
        log.debug("Query news headlines response: {}", JSONUtil.toString(list));
        return list;
    }

}
