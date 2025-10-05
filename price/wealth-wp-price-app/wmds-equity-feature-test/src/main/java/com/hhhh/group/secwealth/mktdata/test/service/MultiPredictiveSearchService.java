package com.hhhh.group.secwealth.mktdata.test.service;

import com.hhhh.group.secwealth.mktdata.test.model.MultiPredSrchQuery;
import com.hhhh.group.secwealth.mktdata.test.model.PredictiveSearch;
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
public class MultiPredictiveSearchService extends BaseService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mds.product.multi-predictive-search-uri:}")
    String productMultiPredictiveSearchUri;

    public PredictiveSearch[] queryMultiPredictiveSearch(MultiPredSrchQuery query) {
        HttpEntity<String> entity = getBaseHttpEntity();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(productMultiPredictiveSearchUri);
        uriBuilder.queryParam("param", JSONUtil.toString(query));
        log.info("Query multi predictive search request: {}", JSONUtil.toString(query));
        PredictiveSearch[] list = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity,PredictiveSearch[].class).getBody();
        log.info("Query multi predictive search response: {}", JSONUtil.toString(list));
        return list;
    }
}
