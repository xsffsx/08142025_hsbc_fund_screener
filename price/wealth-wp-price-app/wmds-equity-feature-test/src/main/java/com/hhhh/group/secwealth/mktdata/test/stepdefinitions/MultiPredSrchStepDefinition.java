package com.hhhh.group.secwealth.mktdata.test.stepdefinitions;

import com.hhhh.group.secwealth.mktdata.test.context.TestContext;
import com.hhhh.group.secwealth.mktdata.test.model.MultiPredSrchQuery;
import com.hhhh.group.secwealth.mktdata.test.model.PredictiveSearch;
import com.hhhh.group.secwealth.mktdata.test.service.MultiPredictiveSearchService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class MultiPredSrchStepDefinition {

    @Autowired
    TestContext testContext;

    @Autowired
    MultiPredictiveSearchService multiPredSrchService;

    @When("^The following multi predictive search is query in (|[\\S]+) market:$")
    public void queryMultiPredictiveSearch(String market, MultiPredSrchQuery query) {
        query.setMarket(market);
        testContext.setMultiPredSrchQuery(query);
    }

    @When("^multi predictive search is query by keyword(?:|s):$")
    public void queryMultiPredSrchByKeyword(List<String> keyword) {
        testContext.getMultiPredSrchQuery().setKeyword(keyword);
    }

    @When("^multi predictive search is query by assetClass(?:|s):$")
    public void queryMultiPredSrchByAssetClass(List<String> assetClass) {
        testContext.getMultiPredSrchQuery().setAssetClasses(assetClass);
    }

    @Then("^Customer should receive the following multi predictive search detail:$")
    public void receiveMultiPredictiveSearchdetail(PredictiveSearch predictiveSearch) {
        MultiPredSrchQuery query = testContext.getMultiPredSrchQuery();
        PredictiveSearch[] predictiveSearchList = multiPredSrchService.queryMultiPredictiveSearch(query);

        for (PredictiveSearch result : predictiveSearchList){
            assertEquals(predictiveSearch.getProductType(),result.getProductType());
            assertEquals(predictiveSearch.getCountryTradableCode(),result.getCountryTradableCode());
            assertEquals(predictiveSearch.getExchange(),result.getExchange());
        }
    }

    @Then("^Customer should receive multi predictive search$")
    public void receiveMultiPredSrchdetail() {
        MultiPredSrchQuery query = testContext.getMultiPredSrchQuery();
        PredictiveSearch[] predictiveSearchList = multiPredSrchService.queryMultiPredictiveSearch(query);
        assertTrue(predictiveSearchList.length >0);
    }

    @Then("^Customer should receive empty multi predictive search detail:$")
    public void receivePredictiveSearchError() {
        MultiPredSrchQuery query = testContext.getMultiPredSrchQuery();
        PredictiveSearch[] predictiveSearchList = multiPredSrchService.queryMultiPredictiveSearch(query);
        assertTrue(predictiveSearchList.length ==0);
    }
}
