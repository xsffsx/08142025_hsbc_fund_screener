package com.hhhh.group.secwealth.mktdata.elastic.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean.Status;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.Healthcheck;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;

@Service
public class PredSrchHealthCheckService implements Healthcheck {

    private static Logger logger = LoggerFactory.getLogger(PredSrchHealthCheckService.class);

    private static final String HEALTHCHECK_ELASTIC = "elasticsearchData";

    private static final String HEALTHCHECK_ELASTIC_SERVER = "elasticsearchServer";

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private Client elasticsearchClient;
    @Autowired
    private AppProperties appProperty;
    @Value("${predsrch.siteConvertRule}")
    private String siteConvertRule;

    @Override
    public List<Status> getStatus() {
        List<Status> statusList = new ArrayList<>();
        statusList.add(checkElasticSrch());
        statusList.add(checkElasticServer());
        return statusList;
    }

    private Status checkElasticServer() {
        long startTime = System.currentTimeMillis();
        try {
            ClusterHealthResponse clusterHealthResponse =
                this.elasticsearchClient.admin().cluster().prepareHealth().get();
            ClusterHealthStatus status = clusterHealthResponse.getStatus();
            if (status.equals(ClusterHealthStatus.RED)) {
                throw new ApplicationException("Index is in " + status + " state");
            } else {
                long endTime = System.currentTimeMillis();
                return new Status(Status.SUCCESS, PredSrchHealthCheckService.HEALTHCHECK_ELASTIC_SERVER, (endTime - startTime));
            }
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            return new Status(Status.FAILURE, PredSrchHealthCheckService.HEALTHCHECK_ELASTIC_SERVER, (endTime - startTime));
        }

    }

    public Status checkElasticSrch() {
        long startTime = System.currentTimeMillis();
        String indexName = "";
        boolean indexHealth = true;
        for (String siteKey : this.appProperty.getSupportSites()) {
            if (CommonConstants.DEFAULT.equalsIgnoreCase(siteKey)) {
                continue;
            } else {
                indexName = siteKey;
            }
            try {
                BoolQueryBuilder tempQuery = QueryBuilders.boolQuery();
                tempQuery.must(QueryBuilders.termQuery("popularity", CommonConstants.POPULARITY));
                Query searchQuery = new NativeSearchQueryBuilder().withQuery(tempQuery)
                    .withPageable(PageRequest.of(0, 1)).build();
                IndexCoordinates realName=IndexCoordinates.of(getCurrentIndexName(indexName.toLowerCase()));
                SearchHits<CustomizedEsIndexForProduct> sampleEntities=
                        this.elasticsearchTemplate.search(searchQuery,CustomizedEsIndexForProduct.class,realName);
                List<CustomizedEsIndexForProduct>  tempList = (List) SearchHitSupport.unwrapSearchHits(sampleEntities);
                if (ListUtil.isInvalid(tempList)) {
                    indexHealth = false;
                }
            } catch (Exception e) {
                indexHealth = false;
            }
        }
        long endTime = System.currentTimeMillis();
        if (indexHealth) {
            return new Status(Status.SUCCESS, PredSrchHealthCheckService.HEALTHCHECK_ELASTIC, (endTime - startTime));
        } else {
            return new Status(Status.FAILURE, PredSrchHealthCheckService.HEALTHCHECK_ELASTIC, (endTime - startTime));
        }
    }

    public String getCurrentIndexName(String siteKey) {
        Iterator<String> keysIt = this.elasticsearchClient.admin().cluster().prepareState().get().getState()
            .getMetaData().getIndices().keysIt();
        if (StringUtils.isNotBlank(this.siteConvertRule) && this.siteConvertRule.contains(siteKey)) {
            siteKey = this.siteConvertRule.substring(this.siteConvertRule.indexOf(PredsrchCommonService.COLON) + 1);
        }
        String currentIndexName = this.getCurrentIndexName(siteKey, keysIt);
        if (!StringUtil.isInvalid(currentIndexName)) {
            return currentIndexName;
        } else {
            PredSrchHealthCheckService.logger.error("no index doc in: {}", siteKey);
            throw new ParameterException(ExCodeConstant.INDEX_DATA_NOT_EXIST);
        }
    }

    private String getCurrentIndexName(String siteKey, Iterator<String> keysIt) {
        String currentIndexName = "";
        long currentStamp = 0;
        while (keysIt.hasNext()) {
            String indexName = keysIt.next();
            if (indexName.contains(siteKey)) {
                long tmpStamp = Long.parseLong(indexName.substring(indexName.lastIndexOf(PredsrchCommonService.UNDERSCORE) + 1));
                if (currentStamp == 0) {
                    currentStamp = tmpStamp;
                    currentIndexName = indexName;
                } else {
                    if (currentStamp > tmpStamp) {
                        currentStamp = tmpStamp;
                        currentIndexName = indexName;
                    }
                }
            }
        }
        return currentIndexName;
    }

}
