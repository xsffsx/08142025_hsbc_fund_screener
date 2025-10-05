/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.HealthcheckService;

import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Controller
public class HealthcheckController {
    public static final String TIMEZONE_HKT = "HKT";
    public static final String DD_MMM_YYYY_HH_MM_SS_SS = "dd-MMM-yyyy HH:mm:ss.SS";
    @Setter
    private HealthcheckService healthcheckService;

    @RequestMapping("/healthcheck/dashboard")
    public String healthcheck(final Model model) {
        model.addAttribute("requestTimestamp", getDate());
        model.addAttribute("statusList", this.healthcheckService.getStatus());
        model.addAttribute("resposneTimestamp", getDate());
        model.addAttribute("version", "V1.0.0");
        return "dashboard";
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DD_MMM_YYYY_HH_MM_SS_SS);
        sdf.setTimeZone(TimeZone.getTimeZone(TIMEZONE_HKT));
        String format = sdf.format(new Date());
        return format;
    }

}
