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
public class TopMoverService extends BaseService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mds.quotes.top-mover-uri:}")
    String topMoverUri;


    public List<TopMover> queryTopmovers(TopMoverQuery query) {
        HttpEntity<String> entity = getBaseHttpEntity();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(topMoverUri);
        uriBuilder.queryParam("param", JSONUtil.toString(query));
        log.info("Query top mover request: {}", JSONUtil.toString(query));
        List<TopMover> list = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, TopMoverResponse.class).getBody().getTopMovers();
        log.debug("Query top mover response: {}", JSONUtil.toString(list));
        return list;
    }


}
