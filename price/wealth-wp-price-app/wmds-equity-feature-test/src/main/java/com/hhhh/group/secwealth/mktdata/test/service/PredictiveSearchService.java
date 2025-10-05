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


@Slf4j
@Service
public class PredictiveSearchService extends BaseService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mds.product.predictive-search-uri:}")
    String productPredictiveSearchUri;

    public PredictiveSearch[] queryPredictiveSearch(PredictiveSearchQuery query) {
        HttpEntity<String> entity = getBaseHttpEntity();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(productPredictiveSearchUri);
        uriBuilder.queryParam("param", JSONUtil.toString(query));
        log.info("Query predictive search request: {}", JSONUtil.toString(query));
        PredictiveSearch[] list = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity,PredictiveSearch[].class).getBody();
        log.debug("Query predictive search response: {}", JSONUtil.toString(list));
        return list;
    }
}
