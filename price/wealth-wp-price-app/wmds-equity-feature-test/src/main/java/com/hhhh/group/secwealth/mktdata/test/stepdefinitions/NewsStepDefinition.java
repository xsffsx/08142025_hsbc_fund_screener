package com.hhhh.group.secwealth.mktdata.test.stepdefinitions;

import com.hhhh.group.secwealth.mktdata.test.context.TestContext;
import com.hhhh.group.secwealth.mktdata.test.model.*;
import com.hhhh.group.secwealth.mktdata.test.service.NewsService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class NewsStepDefinition {

    @Autowired
    TestContext testContext;

    @Autowired
    NewsService newsService;


    @When("^The following news is query in (|[\\S]+) market:$")
    public void queryNewsDetail(String market, NewsDetailQuery query) {
        query.setMarket(market);
        testContext.setNewsDetailQuery(query);
    }

    @Then("^Customer should receive the following news detail:$")
    public void receiveNewsDetail(NewsDetail expNews) {
        NewsDetailQuery query = testContext.getNewsDetailQuery();
        NewsDetail actNews = newsService.queryNewsDetail(query);

        assertEquals(expNews.getId(), actNews.getId());
        assertEquals(expNews.getHeadline(), actNews.getHeadline());
        assertEquals(expNews.getAsOfDateTime(), actNews.getAsOfDateTime());
        assertNotNull(actNews.getContent());
    }

    @Then("^Customer should receive the following news detail error:$")
    public void receiveNewsDetailError(ErrorDetail expError) {
        NewsDetailQuery query = testContext.getNewsDetailQuery();
        try {
            newsService.queryNewsDetail(query);
            fail();
        } catch (HttpClientErrorException exception) {
            ErrorDetail actError = new ErrorDetail(exception);
            assertEquals(expError.getStatusCode(), actError.getStatusCode());
            assertEquals(expError.getReasonCode(), actError.getReasonCode());
            assertEquals(expError.getText(), actError.getText());
        }
    }

    @When("^Query news headlines by category (|[\\S]+) in the (|[\\S]+) market:$")
    public void queryNewsHeadlines(String category, String market) {
        NewsHeadlineQuery query = new NewsHeadlineQuery();
        query.setCategory(category);
        query.setMarket(market);
        testContext.setNewsHeadlineQuery(query);
    }

    @When("^News headlines include following symbol(?:|s):$")
    public void queryNewsHeadlinesWithSymbols(List<String> symbols) {
        NewsHeadlineQuery query = testContext.getNewsHeadlineQuery();
        query.setSymbol(symbols);
    }

    @When("^News headlines with product code indicator (|[\\S]+) include following symbol(?:|s):$")
    public void queryNewsHeadlinesWithSymbols(String productCodeIndicator, List<String> symbols) {
        NewsHeadlineQuery query = testContext.getNewsHeadlineQuery();
        query.setProductCodeIndicator(productCodeIndicator);
        query.setSymbol(symbols);
    }

    @When("^Get (|[\\S]+) page of the news headlines by (|[\\S]+) items per page$")
    public void queryNewsHeadlinesWithPagination(String pageId, String recordsPerPage) {
        NewsHeadlineQuery query = testContext.getNewsHeadlineQuery();
        query.setPageId(pageId);
        query.setRecordsPerPage(recordsPerPage);
    }

    @Then("^Customer should receive news headlines$")
    public void receiveNewsHeadlines() {
        NewsHeadlineQuery query = testContext.getNewsHeadlineQuery();
        List<NewsHeadline> headlines = newsService.queryNewsHeadlines(query);
        assertTrue(headlines.size() > 0);
    }

    @Then("^Customer should receive news headlines less than or equal to (|[\\d]+) records$")
    public void receiveNewsHeadlinesWithSpecifyRecords(int num) {
        NewsHeadlineQuery query = testContext.getNewsHeadlineQuery();
        List<NewsHeadline> headlines = newsService.queryNewsHeadlines(query);
        assertTrue(headlines.size() > 0);
        assertTrue(headlines.size() <= num);
    }

    @When("^The following news headlines is query in (|[\\S]+) market:$")
    public void queryNewsHeadlinesWithPagination(String market, NewsHeadlineQuery query) {
        query.setMarket(market);
        testContext.setNewsHeadlineQuery(query);
    }

    @Then("Customer should receive the following news headlines error:")
    public void receiveNewsHeadlinesError(ErrorDetail expError){
        NewsHeadlineQuery query = testContext.getNewsHeadlineQuery();
        try {
            newsService.queryNewsHeadlines(query);
            fail();
        } catch (HttpClientErrorException exception) {
            ErrorDetail actError = new ErrorDetail(exception);
            assertEquals(expError.getStatusCode(), actError.getStatusCode());
            assertEquals(expError.getReasonCode(), actError.getReasonCode());
            assertEquals(expError.getText(), actError.getText());
        }
    }

}
