/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.bean.*;
import com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.repository.*;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Component
@Transactional(readOnly = true)
public class MarketHourService {

    private static final Logger logger = LoggerFactory.getLogger(MarketHourService.class);
    @Autowired
    private MarketTypeInfoRepository marketTypeInfoRepository;
    @Autowired
    private ExchHldayRepository exchHldayRepository;
    @Autowired
    private ExchSpecDayRepository exchSpecDayRepository;
    @Autowired
    private ExchTradHourRepository exchTradHourRepository;
    @Autowired
    private DelayProdInfoRepository delayProdInfoRepository;

    public boolean isMarketOpen(final String marketTypeCode, final DelayProdInfo... delayProd) {

        if (StringUtils.isBlank(marketTypeCode)) {
            MarketHourService.logger.info("The marketTypeCode is empty...");
            return false;
        }
        MarketTypeInfo marketTypeInfo = this.marketTypeInfoRepository.getExchTimeZone(marketTypeCode);
        SimpleDateFormat sdf1 = null;
        Date currentTime = new Date();
        if (marketTypeInfo != null && StringUtils.isNotBlank(marketTypeInfo.getMarketTimeZone())) {
            String timeZone = marketTypeInfo.getMarketTimeZone();
            // currentDateStr formate is yyyy-MM-dd
            String currentDateStr = DateUtil.formatToString(DateUtil.DATE_DAY_PATTERN, currentTime, timeZone);
            // currentTimeStr formate is yyyy-MM-dd HH:mm:ss
            String currentTimeStr = DateUtil.formatToString(DateUtil.DATE_HOUR_PATTERN, currentTime, timeZone);
            // determine the current day is weekend or not
            boolean isWeekend = this.isWeekend(timeZone, currentTime);
            if (isWeekend) {
                MarketHourService.logger.info("{} is weekend...", currentDateStr);
                return false;
            }
            // determine the current day is holiday or not
            List<ExchHlday> holidays = this.exchHldayRepository.findHoliday(currentDateStr, marketTypeCode);
            if (holidays != null && holidays.size() > 0) {
                MarketHourService.logger.debug("Begin the holiday validation ...");
                for (ExchHlday holiday : holidays) {
                    if (holiday != null && StringUtils.isNoneBlank(holiday.getOpenTime())
                        && StringUtils.isNoneBlank(holiday.getCloseTime())) {
                        // isHoliday is true then the exchange is closed ;
                        // isHoliday is false then go to special day
                        // validation;
                        boolean isHoliday = this.checkTime(currentTimeStr, currentDateStr + " " + holiday.getOpenTime(),
                            currentDateStr + " " + holiday.getCloseTime(), timeZone);
                        if (isHoliday) {
                            MarketHourService.logger.info("{} is holiday , holiday description : {}", currentTimeStr,
                                holiday.getHolidayDesc());
                            return false;
                        }
                    } else {
                        MarketHourService.logger.info("The holiday is empty , holiday : {}...", holiday);
                        return false;
                    }
                }
                MarketHourService.logger.debug("End of the holiday validation ...");
            }
            // determine the current day is special day or not
            List<ExchSpecDay> specDays = this.exchSpecDayRepository.findSpecDay(currentDateStr, marketTypeCode);
            if (specDays != null && specDays.size() > 0) {
                MarketHourService.logger.debug("Begin the special day validation ...");
                for (ExchSpecDay specDay : specDays) {
                    if (specDay != null && StringUtils.isNoneBlank(specDay.getOpenTime())
                        && StringUtils.isNotBlank(specDay.getCloseTime())) {
                        // isSpecDay is true then the exchange is closed ;
                        // isSpecDay is false then go to trading hour
                        // validation;
                        boolean isSpecDay = this.checkTime(currentTimeStr, currentDateStr + " " + specDay.getOpenTime(),
                            currentDateStr + " " + specDay.getCloseTime(), timeZone);
                        if (isSpecDay) {
                            MarketHourService.logger.info("{} is holiday , holiday description : {}", currentTimeStr,
                                specDay.getSpecDayDesc());
                            return false;
                        }
                    }
                }
                MarketHourService.logger.debug("End of the special day validation ...");
            }
            // determine the current time is in trading hour or not
            List<ExchTradHour> tradHours =
                this.exchTradHourRepository.findTradHour(marketTypeCode, DateUtil.getDayOfWeek(currentDateStr));
            // the close time of the exchange
            List<Map<String, String>> exchCloseTime = new ArrayList<Map<String, String>>();
            if (tradHours != null && tradHours.size() > 0) {
                MarketHourService.logger.debug("Begin the trading hour validation ...");
                for (ExchTradHour tradHour : tradHours) {
                    if (tradHour != null && StringUtils.isNoneBlank(tradHour.getOpenTime())
                        && StringUtils.isNotBlank(tradHour.getCloseTime())) {
                        Map<String, String> closeTimeMap = new HashMap<String, String>();
                        closeTimeMap.put("openTime", currentDateStr + " " + tradHour.getOpenTime());
                        closeTimeMap.put("closeTime", currentDateStr + " " + tradHour.getCloseTime());
                        exchCloseTime.add(closeTimeMap);
                        // isTradHour is true then the exchange is open ;
                        // isTradHour is false then go to delay product
                        // validation;
                        boolean isTradHour = this.checkTime(currentTimeStr, currentDateStr + " " + tradHour.getOpenTime(),
                            currentDateStr + " " + tradHour.getCloseTime(), timeZone);
                        if (isTradHour) {
                            MarketHourService.logger.debug("{} is in trading hour...", currentTimeStr);
                            return true;
                        }
                    }
                }
                MarketHourService.logger.debug("End of the trading hour validation ...");
            }
            // determine the product is delay product or not
            if (delayProd.length > 0 && delayProd[0] != null && StringUtils.isNotBlank(delayProd[0].getAsetClassCde())
                && StringUtils.isNotBlank(delayProd[0].getProdCode()) && exchCloseTime != null && exchCloseTime.size() > 0) {
                MarketHourService.logger.debug("Begin the delay product validation ...");
                DelayProdInfo delayProdInfo =
                    this.delayProdInfoRepository.findDelayProd(delayProd[0].getAsetClassCde(), delayProd[0].getProdCode());
                if (delayProdInfo != null && delayProdInfo.getDelayTime() != null && delayProdInfo.getDelayTime() > 0) {
                    for (Map<String, String> openCloseTime : exchCloseTime) {
                        String openTime = openCloseTime.getOrDefault("openTime", "");
                        String closeTime = openCloseTime.getOrDefault("closeTime", "");
                        if (StringUtils.isNotBlank(openTime) && StringUtils.isNotBlank(closeTime)) {
                            boolean isDelay =
                                this.checkTime(currentTimeStr, openTime, closeTime, timeZone, delayProdInfo.getDelayTime());
                            if (isDelay) {
                                MarketHourService.logger.info("{} is in delay trading hour...", currentTimeStr);
                                return true;
                            }
                        } else {
                            MarketHourService.logger.info("The open time or closed time is empty...");
                            MarketHourService.logger.info("open time : {} , closed time : {} ...", openTime, closeTime);
                        }
                    }
                } else {
                    MarketHourService.logger.info("This product is not delay product , product type : {} , product code : {} ...",
                        delayProd[0].getAsetClassCde(), delayProd[0].getProdCode());
                    return false;
                }
            } else {
                MarketHourService.logger.info("The product type or product code is empty...");
                return false;
            }
        } else {
            MarketHourService.logger.info("The exchange time zone is empty...");
            return false;
        }
        return false;
    }


    /**
     *
     * <p>
     * <b> TODO : determine the date is weekend or not. </b>
     * </p>
     *
     * @param timeZone
     * @return
     */
    public boolean isWeekend(final String timeZone, final Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }

    /**
     *
     * <p>
     * <b> TODO : determine the current time is between the openTime and
     * closeTime or not. </b>
     * </p>
     *
     * @param currentTime
     * @param openTime
     * @param closeTime
     * @param timeZone
     * @return
     */
    private boolean checkTime(final String currentTime, final String openTime, final String closeTime, final String timeZone,
        final Integer... delayTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_HOUR_PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        long currentTimeMilliseconds = 0;
        long openTimeMilliseconds = 0;
        long closeTimeMilliseconds = 0;
        try {
            currentTimeMilliseconds = sdf.parse(currentTime).getTime();
            openTimeMilliseconds = sdf.parse(openTime).getTime();
            closeTimeMilliseconds = sdf.parse(closeTime).getTime();
            if (delayTime != null && delayTime.length > 0) {
                closeTimeMilliseconds = closeTimeMilliseconds + delayTime[0];
            }
            if (currentTimeMilliseconds >= openTimeMilliseconds && currentTimeMilliseconds < closeTimeMilliseconds) {
                return true;
            }
        } catch (ParseException e) {
            MarketHourService.logger.error("Conver the current time error....");
            MarketHourService.logger.error("currentTime : {} , openTime : {} , closeTime : {} , timeZone : {}", currentTime,
                openTime, closeTime, timeZone, e);
        }
        return false;
    }

}
