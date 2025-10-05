package com.hhhh.group.secwealth.mktdata.test.stepdefinitions;

import com.hhhh.group.secwealth.mktdata.test.context.TestContext;
import com.hhhh.group.secwealth.mktdata.test.model.ErrorDetail;
import com.hhhh.group.secwealth.mktdata.test.model.PredictiveSearch;
import com.hhhh.group.secwealth.mktdata.test.model.PredictiveSearchQuery;
import com.hhhh.group.secwealth.mktdata.test.service.PredictiveSearchService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Slf4j
public class PredSrchStepDefinition {

    @Autowired
    TestContext testContext;

    @Autowired
    PredictiveSearchService predictiveSearchService;

    @When("^The following predictive search is query in (|[\\S]+) market:$")
    public void queryPredictiveSearch(String market, PredictiveSearchQuery query) {
        query.setMarket(market);
        testContext.setPredictiveSearchQuery(query);
    }

    @When("^predictive search is query by assetClass(?:|s):$")
    public void queryPredictiveSearchByAssetClass(List<String> assetClass) {
        testContext.getPredictiveSearchQuery().setAssetClasses(assetClass);
    }

    @Then("^Customer should receive the following predictive search detail:$")
    public void receivePredictiveSearchdetail(PredictiveSearch predictiveSearch) {
        PredictiveSearchQuery query = testContext.getPredictiveSearchQuery();
        PredictiveSearch[] predictiveSearchList = predictiveSearchService.queryPredictiveSearch(query);

       for (PredictiveSearch result : predictiveSearchList){
           assertEquals(predictiveSearch.getProductType(), result.getProductType());
           assertEquals(predictiveSearch.getCountryTradableCode(), result.getCountryTradableCode());
           assertEquals(predictiveSearch.getExchange(), result.getExchange());
       }
    }

    @Then("^Customer should receive the following predictive search detail error:$")
    public void receivePredictiveSearchError(ErrorDetail expError) {
        PredictiveSearchQuery query = testContext.getPredictiveSearchQuery();
        try {
            predictiveSearchService.queryPredictiveSearch(query);
            fail();
        } catch (HttpClientErrorException exception) {
            ErrorDetail actError = new ErrorDetail(exception);
            assertEquals(expError.getStatusCode(), actError.getStatusCode());
            assertEquals(expError.getReasonCode(), actError.getReasonCode());
            assertEquals(expError.getText(), actError.getText());
        }
    }


}
