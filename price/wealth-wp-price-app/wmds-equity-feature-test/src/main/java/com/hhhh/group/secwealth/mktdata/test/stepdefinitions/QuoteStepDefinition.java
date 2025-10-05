package com.hhhh.group.secwealth.mktdata.test.stepdefinitions;

import com.hhhh.group.secwealth.mktdata.test.context.TestContext;
import com.hhhh.group.secwealth.mktdata.test.model.*;
import com.hhhh.group.secwealth.mktdata.test.service.NewsService;
import com.hhhh.group.secwealth.mktdata.test.service.QuoteService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class QuoteStepDefinition {

    @Autowired
    TestContext testContext;

    @Autowired
    QuoteService quoteService;

    @When("^The current IPO is query in (|[\\S]+) market$")
    public void queryCurrentIPO(String market) {
        testContext.setMarket(market);
    }

    @Then("^Customer should receive the current IPO list$")
    public void receiveCurrentIPOList() {
        List<CurrentIPO> list = quoteService.queryCurrentIPO(testContext.getMarket());
        assertFalse(list.isEmpty());
    }

    @Then("^Customer should receive the following current IPS error:$")
    public void receiveCurrentIPOListError(ErrorDetail expError) {
        try {
            quoteService.queryCurrentIPO(testContext.getMarket());
            fail();
        } catch (HttpClientErrorException exception) {
            ErrorDetail actError = new ErrorDetail(exception);
            assertEquals(expError.getStatusCode(), actError.getStatusCode());
            assertEquals(expError.getReasonCode(), actError.getReasonCode());
            assertEquals(expError.getText(), actError.getText());
        }
    }

    @When("^The following quotes is query in (|[\\S]+) market:$")
    public void queryQuotesDetail(String market,QuotesQuery query) {
        query.setMarket(market);
        testContext.setQuotesQuery(query);
    }

    @When("^quotes is query by productKeys(?:|s):$")
    public void queryQuotesByProductKeys(List<ProductKeys> productKeys) {
        testContext.getQuotesQuery().setProductKeys(productKeys);
    }

    @Then("^Customer should receive the following quotes detail:$")
    public void receiveQuotesDetail(Quotes quotes) {
        QuotesQuery query = testContext.getQuotesQuery();
        List<Quotes> quotesList = quoteService.queryQuotes(query);
        assertTrue(quotesList.size() > 0);
        for (Quotes result : quotesList) {
            assertEquals(quotes.getSymbol(), result.getSymbol());
            assertEquals(quotes.getProductSubType(), result.getProductSubType());
            assertEquals(quotes.getExchangeCode(), result.getExchangeCode());
        }
    }

    @Then("^Customer should receive the following quotes detail error:$")
    public void receiveChartsPerformanceError(ErrorDetail expError) {
        QuotesQuery query = testContext.getQuotesQuery();
        try {
            quoteService.queryQuotes(query);
            fail();
        } catch (HttpClientErrorException exception) {
            ErrorDetail actError = new ErrorDetail(exception);
            assertEquals(expError.getStatusCode(), actError.getStatusCode());
            assertEquals(expError.getReasonCode(), actError.getReasonCode());
            assertEquals(expError.getText(), actError.getText());
        }
    }

    @When("^The listed IPO is query in (|[\\S]+) market:$")
    public void queryListedIPO(String market,ListedIPOQuery listedIPOQuery) {
        listedIPOQuery.setMarket(market);
        testContext.setListedIPOQuery(listedIPOQuery);
    }

    @Then("^Customer should receive the Listed IPO list:$")
    public void receiveListedIPOList() {
        List<ListedIPO> list = quoteService.queryListedIPO(testContext.getListedIPOQuery());
        assertFalse(list.isEmpty());
    }

    @Then("^Customer should receive the following listed IPO error:$")
    public void receiveListedIPOListError(ErrorDetail expError) {
        try {
            quoteService.queryListedIPO(testContext.getListedIPOQuery());
            fail();
        } catch (HttpClientErrorException exception) {
            ErrorDetail actError = new ErrorDetail(exception);
            assertEquals(expError.getStatusCode(), actError.getStatusCode());
            assertEquals(expError.getReasonCode(), actError.getReasonCode());
            assertEquals(expError.getText(), actError.getText());
        }
    }

}
