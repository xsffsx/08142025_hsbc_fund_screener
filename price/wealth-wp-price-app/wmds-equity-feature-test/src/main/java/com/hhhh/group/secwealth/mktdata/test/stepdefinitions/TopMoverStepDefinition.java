package com.hhhh.group.secwealth.mktdata.test.stepdefinitions;

import com.hhhh.group.secwealth.mktdata.test.context.TestContext;
import com.hhhh.group.secwealth.mktdata.test.model.*;
import com.hhhh.group.secwealth.mktdata.test.service.NewsService;
import com.hhhh.group.secwealth.mktdata.test.service.TopMoverService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

import static org.junit.Assert.*;

@Slf4j
public class TopMoverStepDefinition {

    @Autowired
    TestContext testContext;

    @Autowired
    TopMoverService topMoverService;


    @When("^The following top mover is query in (|[\\S]+) market:$")
    public void queryTopMover(String market,TopMoverQuery query ) {
        query.setMarket(market);
        testContext.setTopMoverQuery(query);
    }

    @Then("^Customer should receive the following top mover:$")
    public void receiveTopMover() throws ParseException {
        TopMoverQuery query = testContext.getTopMoverQuery();
        List<TopMover> actTopMovers = topMoverService.queryTopmovers(query);
        String format = "HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date nowTime = new SimpleDateFormat(format).parse(simpleDateFormat.format(new Date()));
        Date startTime = new SimpleDateFormat(format).parse("15:30:00");
        Date endTime = new SimpleDateFormat(format).parse("21:30:00");
        if("US".equalsIgnoreCase(testContext.getTopMoverQuery().getMarket())){
            if(isEffectiveDate(nowTime,startTime,endTime)){
                assertFalse(actTopMovers.isEmpty());
            }else{
                assertFalse(actTopMovers.get(0).getTopMoverProducts().isEmpty());
            }
        }else {
            assertFalse(actTopMovers.get(0).getTopMoverProducts().isEmpty());
        }
    }
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    @Then("^Customer should receive the following top mover detail error:$")
    public void receiveTopMoverError(ErrorDetail expError) {
        TopMoverQuery query = testContext.getTopMoverQuery();
        try {
            topMoverService.queryTopmovers(query);
            fail();
        } catch (HttpClientErrorException exception) {
            ErrorDetail actError = new ErrorDetail(exception);
            assertEquals(expError.getStatusCode(), actError.getStatusCode());
            assertEquals(expError.getReasonCode(), actError.getReasonCode());
            assertEquals(expError.getText(), actError.getText());
        }
    }


}
