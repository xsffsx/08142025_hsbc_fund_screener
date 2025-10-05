package com.hhhh.group.secwealth.mktdata.test.stepdefinitions;

import com.hhhh.group.secwealth.mktdata.test.context.TestContext;
import com.hhhh.group.secwealth.mktdata.test.model.Charts;
import com.hhhh.group.secwealth.mktdata.test.model.ChartsQuery;
import com.hhhh.group.secwealth.mktdata.test.model.ErrorDetail;
import com.hhhh.group.secwealth.mktdata.test.service.ChartsService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Slf4j
public class ChartsStepDefinition {

    @Autowired
    TestContext testContext;

    @Autowired
    ChartsService chartsService;

    @When("^The following charts is query in (|[\\S]+) market:$")
    public void queryChartsPerformance(String market,ChartsQuery query) {
        query.setMarket(market);
        testContext.setChartsQuery(query);
    }

    @When("^charts is query by symbol(?:|s):$")
    public void queryChartsBySymbol(List<String> symbol) {
        testContext.getChartsQuery().setSymbol(symbol);
    }


    @When("^charts is query by filter(?:|s):$")
    public void queryChartsByfilters(List<String> filters) {
        if (!"CN".equals(testContext.getChartsQuery().getMarket())){
            testContext.getChartsQuery().setFilters(filters);
        }
    }

    @Then("^Customer should receive the following charts detail:$")
    public void receiveChartsDetail(Charts charts) {
        ChartsQuery query = testContext.getChartsQuery();
        List<Charts> chartsList = chartsService.queryChartsPerformance(query);
        assertTrue(chartsList.size() > 0);
        for (Charts chartsResult : chartsList){
            assertEquals(charts.getProductType(), chartsResult.getProductType());
            assertEquals(charts.getDisplayName(), chartsResult.getDisplayName());
        }
    }


    @Then("^Customer should receive charts performance$")
    public void receiveChartsPerformance() {
        ChartsQuery query = testContext.getChartsQuery();
        List<Charts> chartsList = chartsService.queryChartsPerformance(query);
        assertTrue(chartsList.size() > 0);
    }

    @Then("^Customer should receive the following charts detail error:$")
    public void receiveChartsPerformanceError(ErrorDetail expError) {
        ChartsQuery query = testContext.getChartsQuery();
        try {
            chartsService.queryChartsPerformance(query);
            fail();
        } catch (HttpClientErrorException exception) {
            ErrorDetail actError = new ErrorDetail(exception);
            assertEquals(expError.getStatusCode(), actError.getStatusCode());
            assertEquals(expError.getReasonCode(), actError.getReasonCode());
            assertEquals(expError.getText(), actError.getText());
        }
    }
}
