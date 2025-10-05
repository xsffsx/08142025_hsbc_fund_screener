package com.hhhh.group.secwealth.mktdata.api.equity.quotes.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EquityHealtheckProbingController {

    private final static Logger logger = LoggerFactory.getLogger(EquityHealtheckProbingController.class);
    public static final String TIMEZONE_HKT = "HKT";
    public static final String DateFormat_MMddyyyyHHmmss_withSymbol = "MM-dd-yyyy HH:mm:ss";


    @RequestMapping(value = "healthcheck/probing", method = RequestMethod.GET)
    public String probing(final Model model) {
        String result = "success";
        String systemTime = "";
        try {
            Calendar todayDate = Calendar.getInstance(TimeZone.getTimeZone(EquityHealtheckProbingController.TIMEZONE_HKT));
            DateFormat df = new SimpleDateFormat(EquityHealtheckProbingController.DateFormat_MMddyyyyHHmmss_withSymbol);
            systemTime = df.format(todayDate.getTime());
        } catch (Exception e) {
            EquityHealtheckProbingController.logger.error("request probing", e);
            result = "failure";
            systemTime = e.getClass().getName();
        }
        model.addAttribute("result", result);
        model.addAttribute("systemTime", systemTime);
        return "probing";
    }

}
