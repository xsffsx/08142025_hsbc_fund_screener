package com.hhhh.group.secwealth.mktdata.test.context;

import com.hhhh.group.secwealth.mktdata.test.model.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TestContext {

    private String market;

    private NewsDetailQuery newsDetailQuery;

    private NewsHeadlineQuery newsHeadlineQuery;

    private ChartsQuery chartsQuery;

    private PredictiveSearchQuery predictiveSearchQuery;

    private MultiPredSrchQuery multiPredSrchQuery;

    private TopMoverQuery topMoverQuery;

    private QuotesQuery quotesQuery;

    private IndexQuotesQuery indexQuotesQuery;

    private ListedIPOQuery listedIPOQuery;


}