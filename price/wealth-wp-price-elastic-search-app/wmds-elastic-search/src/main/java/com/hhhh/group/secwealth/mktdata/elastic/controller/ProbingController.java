package com.hhhh.group.secwealth.mktdata.elastic.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.SystemException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.web.bind.annotation.*;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.service.PredsrchCommonService;
import com.hhhh.group.secwealth.mktdata.elastic.service.ScheduleDataLoadService;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean.Status;


@RestController
public class ProbingController {

    public static final String TIMEZONE_HKT = "HKT";

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private Client elasticsearchClient;
    @Autowired
    private AppProperties appProperty;

    @Autowired
    private ScheduleDataLoadService scheduleDataLoadService;

    @Autowired
    private PredsrchCommonService predsrchCommonService;

    private static final String HEALTHCHECK_ELASTIC = "predictivesearchData";

    private static final String HEALTHCHECK_ELASTIC_SERVER = "predictivesearchServer";

    @GetMapping(value = "healthcheck/probing")
    public String probing() {
        Calendar todayDate = Calendar.getInstance(TimeZone.getTimeZone(ProbingController.TIMEZONE_HKT));
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return df.format(todayDate.getTime());
    }

    @GetMapping(value = "healthcheck/checkPredictiveServer")
    public Status checkPredictiveServer() {
        long startTime = System.currentTimeMillis();
        try {
            ClusterHealthResponse clusterHealthResponse =
                this.elasticsearchClient.admin().cluster().prepareHealth().get();
            ClusterHealthStatus status = clusterHealthResponse.getStatus();
            if (status.equals(ClusterHealthStatus.RED)) {
                throw new SystemException("Index is in " + status + " state");
            } else {
                long endTime = System.currentTimeMillis();
                return new Status(Status.SUCCESS, ProbingController.HEALTHCHECK_ELASTIC_SERVER, (endTime - startTime));
            }
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            return new Status(Status.FAILURE, ProbingController.HEALTHCHECK_ELASTIC_SERVER, (endTime - startTime));
        }

    }

    @GetMapping(value = "healthcheck/checkPredSrch")
    public Status checkPredSrch() {
        long startTime = System.currentTimeMillis();
        String indexName;
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
                SearchHits<CustomizedEsIndexForProduct> sampleEntities=
                        this.elasticsearchTemplate.search(searchQuery,CustomizedEsIndexForProduct.class,IndexCoordinates.of(this.predsrchCommonService.getCurrentIndexName(indexName.toLowerCase())));
                List<CustomizedEsIndexForProduct> tempList=(List) SearchHitSupport.unwrapSearchHits(sampleEntities);
                if (ListUtil.isInvalid(tempList)) {
                    indexHealth = false;
                }
            } catch (Exception e) {
                indexHealth = false;
            }
        }
        long endTime = System.currentTimeMillis();
        if (indexHealth) {
            return new Status(Status.SUCCESS, ProbingController.HEALTHCHECK_ELASTIC, (endTime - startTime));
        } else {
            return new Status(Status.FAILURE, ProbingController.HEALTHCHECK_ELASTIC, (endTime - startTime));
        }

    }

    @RequestMapping("/loadData")
    @ResponseBody
    public String loadData() throws Exception {
        this.scheduleDataLoadService.loadData();
        return "success";
    }

    @RequestMapping("/listIndices")
    @ResponseBody
    public List<String> listIndices() {
        Iterator<String> keysIt = this.elasticsearchClient.admin().cluster().prepareState().get().getState()
            .getMetaData().getIndices().keysIt();
        List<String> indexList = new ArrayList<>();
        while (keysIt.hasNext()) {
            String oldIndexName = keysIt.next();
            indexList.add(oldIndexName);
        }
        return indexList;
    }

}
