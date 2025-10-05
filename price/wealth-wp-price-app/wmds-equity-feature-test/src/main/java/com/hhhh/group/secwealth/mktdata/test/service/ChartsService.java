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
public class ChartsService extends BaseService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mds.charts.performance-uri:}")
    String chartsPerformanceUri;


    public List<Charts> queryChartsPerformance(ChartsQuery query) {
        HttpEntity<String> entity = getBaseHttpEntity();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(chartsPerformanceUri);
        uriBuilder.queryParam("param", JSONUtil.toString(query));
        log.info("Query charts performance request: {}", JSONUtil.toString(query));
        List<Charts> chartsList = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, ChartResponse.class).getBody().getResult();
        log.debug("Query charts performance response: {}", JSONUtil.toString(chartsList));
        return chartsList;
    }
}
