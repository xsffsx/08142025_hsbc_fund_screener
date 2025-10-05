package com.hhhh.group.secwealth.mktdata.test.stepdefinitions;

import com.hhhh.group.secwealth.mktdata.test.context.TestContext;
import com.hhhh.group.secwealth.mktdata.test.model.*;
import com.hhhh.group.secwealth.mktdata.test.service.QuoteService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class IndexQuoteStepDefinition {

    @Autowired
    TestContext testContext;

    @Autowired
    QuoteService quoteService;

    @When("^The index quotes is query in (|[\\S]+) market:$")
    public void queryIndexQuotes(String market) {
        IndexQuotesQuery query = new IndexQuotesQuery();
        query.setMarket(market);
        testContext.setIndexQuotesQuery(query);
    }
    @When("^index quotes is query by symbol(?:|s):$")
    public void queryIndexQuotesBySymbol(List<String> symbol) {
        testContext.getIndexQuotesQuery().setSymbol(symbol);
    }

    @Then("^Customer should receive the index quotes list:$")
    public void receiveIndexQuotesList() {
        IndexQuotesResponse indexQuotesResponse = quoteService.queryIndexQuotes(testContext.getIndexQuotesQuery());
        assertFalse(indexQuotesResponse.getIndices().isEmpty());
    }

    @Then("^Customer should receive the following index quotes error:$")
    public void receiveCurrentIPOListError(ErrorDetail expError) {
        try {
            quoteService.queryIndexQuotes(testContext.getIndexQuotesQuery());
            fail();
        } catch (HttpClientErrorException exception) {
            ErrorDetail actError = new ErrorDetail(exception);
            assertEquals(expError.getStatusCode(), actError.getStatusCode());
            assertEquals(expError.getReasonCode(), actError.getReasonCode());
            assertEquals(expError.getText(), actError.getText());
        }
    }

}
