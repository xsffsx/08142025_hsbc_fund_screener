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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class QuoteService extends BaseService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mds.equity.current-ipo-uri:}")
    String currentIPOUri;

    @Value("${mds.quotes.quotes-uri:}")
    String quotesUri;

    @Value("${mds.quotes.index-quotes-uri:}")
    String indexQuotesUri;

    @Value("${mds.equity.listed-ipo-uri:}")
    String listedIPOUri;

    public List<CurrentIPO> queryCurrentIPO(String market) {
        HttpEntity<String> entity = getBaseHttpEntity();

        Map<String, String> param = new HashMap<>();
        param.put("market", market);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(currentIPOUri);
        uriBuilder.queryParam("param", JSONUtil.toString(param));
        log.info("Query current IPO request: {}", JSONUtil.toString(param));
        List<CurrentIPO> list = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, CurrentIPOResponse.class).getBody().getCurrentIPO();
        log.debug("Query current IPO response: {}", JSONUtil.toString(list));
        return list;
    }

    public List<Quotes> queryQuotes(QuotesQuery query) {
        HttpEntity<String> entity = getBaseHttpEntity();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(quotesUri);
        uriBuilder.queryParam("param", JSONUtil.toString(query));
        log.info("Query quotes request: {}", JSONUtil.toString(query));
        List<Quotes> quotesList = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, QuotesResponse.class).getBody().getPriceQuotes();
        log.debug("Query quotes response: {}", JSONUtil.toString(quotesList));
        return quotesList;
    }

    public IndexQuotesResponse queryIndexQuotes(IndexQuotesQuery indexQuotesQuery){
        HttpEntity<String> entity = getBaseHttpEntity();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(indexQuotesUri);
        uriBuilder.queryParam("param", JSONUtil.toString(indexQuotesQuery));
        log.info("Query index quotes request: {}", JSONUtil.toString(indexQuotesQuery));
        IndexQuotesResponse indexQuotesResponse = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, IndexQuotesResponse.class).getBody();
        log.debug("Query index quotes response: {}", JSONUtil.toString(indexQuotesResponse));
        return indexQuotesResponse;
    }

    public List<ListedIPO> queryListedIPO(ListedIPOQuery listedIPOQuery) {
        HttpEntity<String> entity = getBaseHttpEntity();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(listedIPOUri);
        uriBuilder.queryParam("param", JSONUtil.toString(listedIPOQuery));
        log.info("Query Listed IPO request: {}", JSONUtil.toString(listedIPOQuery));
        List<ListedIPO> list = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, ListedIPOResponse.class).getBody().getListedIPO();
        log.debug("Query Listed IPO response: {}", JSONUtil.toString(list));
        return list;
    }


}
