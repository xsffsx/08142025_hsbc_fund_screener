/**
 * @Title MarketHourProperties.java
 * @description TODO
 * @author OJim
 * @time Jul 2, 2019 2:26:14 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.properties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.bean.DelayProdInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
/**
 * @Title MarketHourProperties.java
 * @description TODO
 * @author OJim
 * @time Jul 2, 2019 2:26:14 PM
 */
@Component
@ConfigurationProperties(prefix = "marketHour")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
@Getter
@Setter
public class MarketHourProperties {

	private static final Logger logger = LoggerFactory.getLogger(MarketHourProperties.class);

	private Map<String, Map<String, String>> sessionInfo = new LinkedHashMap<>();

	private Map<String, String> timezone = new LinkedHashMap<>();
	
	private Map<String, String> marketDelayClose = new LinkedHashMap<>();

	private Map<String, Map<String, Map<String, Map<String, String>>>> holiday = new LinkedHashMap<>();

	private Map<String, Map<String, Map<String, Map<String, String>>>> special = new LinkedHashMap<>();

	private Map<String, Map<String, String>> delayed = new LinkedHashMap<>();

	private String marketTypeCode = "";

	private String timezoneStr = "";
	
	private String marketDelayCloseStr = "";

	private static String OPEN_TIME = "openTime";

	private static String CLOSE_TIME = "closeTime";

	private static String TRADING_HOUR = "tradinghour";

	private static String DESC = "desc";

	private static String CHECK_DATE = "yyyyMMdd";

	public boolean isMarketOpen(final String marketTypeCode, final DelayProdInfo... delayProd) {
		try {
			if (StringUtil.isInValid(marketTypeCode) || this.sessionInfo.get(marketTypeCode) == null) {
				MarketHourProperties.logger.info("The marketTypeCode is empty...");
				return false;
			} else {
				this.marketTypeCode = marketTypeCode;
			}
			this.timezoneStr = this.timezone.get(marketTypeCode);
			if (StringUtil.isInValid(this.timezoneStr)) {
				this.timezoneStr = this.timezone.get(Constant.DEFAULT_OPTION);
			}
			this.marketDelayCloseStr = this.marketDelayClose.get(marketTypeCode);
			if (StringUtil.isInValid(this.marketDelayCloseStr)) {
				this.marketDelayCloseStr = this.marketDelayClose.get(Constant.DEFAULT_OPTION);
			}
			Date currentTime = new Date();
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(this.timezoneStr));
			cal.setTime(currentTime);
			String yearStr = cal.get(Calendar.YEAR) + "";
			int weekStr = cal.get(Calendar.DAY_OF_WEEK);
			// currentDateStr formate is yyyy-MM-dd
			String currentDateStr = DateUtil.formatToString(DateUtil.DATE_DAY_PATTERN, currentTime, this.timezoneStr);

			String checkDateStr = DateUtil.formatToString(MarketHourProperties.CHECK_DATE, currentTime,
					this.timezoneStr);
			// currentTimeStr formate is yyyy-MM-dd HH:mm:ss
			String currentTimeStr = DateUtil.formatToString(DateUtil.DATE_HOUR_PATTERN, currentTime, this.timezoneStr);
			boolean isWeekend = this.isWeekend(cal);
			if (isWeekend) {
				MarketHourProperties.logger.info("{} is weekend...", currentDateStr);
				return false;
			}
			boolean isHoliday = this.isHoliday(yearStr, currentDateStr, currentTimeStr, checkDateStr);
			if (isHoliday) {
				MarketHourProperties.logger.info("{} is holiday...", currentDateStr);
				return false;
			}
			boolean isSpecial = this.isSpecial(yearStr, currentDateStr, currentTimeStr, checkDateStr);
			if (isSpecial) {
				MarketHourProperties.logger.info("{} is holiday...", currentDateStr);
				return false;
			}
			boolean isTradHour = this.isTradHour(weekStr - 1 + "", currentDateStr, currentTimeStr,this.marketDelayCloseStr);
			if (isTradHour) {
				MarketHourProperties.logger.debug("{} is in trading hour...", currentTimeStr);
				return true;
			}
			if (delayProd.length > 0 && delayProd[0] != null && StringUtil.isValid(delayProd[0].getAsetClassCde())
					&& StringUtil.isValid(delayProd[0].getProdCode())) {
				boolean isDelayed = this.isDelayed(currentDateStr, currentTimeStr, delayProd);
				if (isDelayed) {
					MarketHourProperties.logger.info("{} is in delay trading hour , delay ProdCode : {}",
							currentTimeStr, delayProd[0].getProdCode());
					return true;
				}
			}
		} catch (Exception e) {
			MarketHourProperties.logger.info("isMarketOpen has some error :" + e);
			return false;
		}
		return false;
	}

	/**
	 * @Title isTradHour
	 * @Description
	 * @param currentTime
	 * @param string
	 * @param exchCloseTime
	 * @param currentTimeStr
	 * @param currentDateStr
	 * @return
	 * @return boolean
	 * @Author OJim
	 * @Date Jul 2, 2019 3:57:09 PM
	 */
	private boolean isTradHour(String weekStr, String currentDateStr, String currentTimeStr,String marketDelayCloseStr) {
		String tradingHour = "";
		if (this.sessionInfo.get(this.marketTypeCode) != null) {
			tradingHour = this.sessionInfo.get(this.marketTypeCode).get(weekStr);
		} else {
			MarketHourProperties.logger.info("The tradingHour is empty...");
			return false;
		}
		String[] tradHours = tradingHour.split(SymbolConstant.SYMBOL_COMMA);
		List<Map<String, String>> exchCloseTime = new ArrayList<Map<String, String>>();
		for (String tradHour : tradHours) {
			String[] tH = tradHour.split(SymbolConstant.SYMBOL_LINE);
			Map<String, String> closeTimeMap = new LinkedHashMap<String, String>();
			closeTimeMap.put(MarketHourProperties.OPEN_TIME, tH[0]);
			closeTimeMap.put(MarketHourProperties.CLOSE_TIME, tH[1]);
			exchCloseTime.add(closeTimeMap);
		}
		for (Map<String, String> closeTimeMap : exchCloseTime) {
			boolean isTradHour = this.checkTime(currentTimeStr,
					currentDateStr + " " + closeTimeMap.get(MarketHourProperties.OPEN_TIME),
					currentDateStr + " " + closeTimeMap.get(MarketHourProperties.CLOSE_TIME), this.timezoneStr,Integer.valueOf(marketDelayCloseStr)*60*1000);
			if (isTradHour) {
				MarketHourProperties.logger.debug("{} is in trading hour...", currentTimeStr);
				return true;
			}
		}
		return false;
	}

	/**
	 * @Title isDelayed
	 * @Description
	 * @param currentTime
	 * @param currentDateStr2
	 * @return
	 * @return boolean
	 * @Author OJim
	 * @Date Jul 2, 2019 3:38:05 PM
	 */
	private boolean isDelayed(String currentDateStr, String currentTimeStr, DelayProdInfo[] delayProd) {
		if (this.delayed.get(delayProd[0].getAsetClassCde()) != null) {
			Map<String, String> delayedProdCode = this.delayed.get(delayProd[0].getAsetClassCde());
			if (delayedProdCode.containsKey(delayProd[0].getProdCode())) {
				if (StringUtil.isValid(delayedProdCode.get(delayProd[0].getProdCode()))) {
					String[] delayed = delayedProdCode.get(delayProd[0].getProdCode())
							.split(SymbolConstant.SYMBOL_COMMA);
					for (String dD : delayed) {
						String[] dH = dD.split(SymbolConstant.SYMBOL_LINE);
						boolean isDelayed = this.checkTime(currentTimeStr, currentDateStr + " " + dH[0],
								currentDateStr + " " + dH[1], this.timezoneStr);
						if (isDelayed) {
							MarketHourProperties.logger.info("{} is in delay trading hour , delay ProdCode : {}",
									currentTimeStr, delayProd[0].getProdCode());
							return true;
						}
					}
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * @param currentTime
	 * @Title isSpecial
	 * @Description
	 * @param exchCloseTime
	 * @param yearStr
	 * @return
	 * @return boolean
	 * @Author OJim
	 * @Date Jul 2, 2019 3:38:03 PM
	 */
	private boolean isSpecial(String yearStr, String currentDateStr, String currentTimeStr, String checkDataStr) {
		if (this.special.get(this.marketTypeCode) != null
				&& this.special.get(this.marketTypeCode).get(yearStr) != null) {
			Map<String, Map<String, String>> specials = this.special.get(this.marketTypeCode).get(yearStr);
			if (specials.containsKey(checkDataStr)) {
				Map<String, String> special = specials.get(checkDataStr);
				if (StringUtil.isValid(special.get(MarketHourProperties.TRADING_HOUR))) {
					String[] spec = special.get(MarketHourProperties.TRADING_HOUR).split(SymbolConstant.SYMBOL_COMMA);
					for (String sD : spec) {
						String[] sH = sD.split(SymbolConstant.SYMBOL_LINE);
						boolean isSpecial = this.checkTime(currentTimeStr, currentDateStr + " " + sH[0],
								currentDateStr + " " + sH[1], this.timezoneStr);
						if (isSpecial) {
							MarketHourProperties.logger.info(
									"{} is holiday , holiday description : {} , but is trading hour now",
									currentDateStr, special.get(MarketHourProperties.DESC));
							return false;
						}
					}
				} else {
					MarketHourProperties.logger.info("{} is holiday , holiday description : {}", currentDateStr,
							special.get(MarketHourProperties.DESC));
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param currentTime
	 * @Title isHoliday
	 * @Description
	 * @param exchCloseTime
	 * @param yearStr
	 * @return
	 * @return boolean
	 * @Author OJim
	 * @Date Jul 2, 2019 3:38:01 PM
	 */
	private boolean isHoliday(String yearStr, String currentDateStr, String currentTimeStr, String checkDataStr) {
		if (this.holiday.get(this.marketTypeCode) != null
				&& this.holiday.get(this.marketTypeCode).get(yearStr) != null) {
			Map<String, Map<String, String>> holidays = this.holiday.get(this.marketTypeCode).get(yearStr);
			if (holidays.containsKey(checkDataStr)) {
				Map<String, String> holiday = holidays.get(checkDataStr);
				if (StringUtil.isValid(holiday.get(MarketHourProperties.TRADING_HOUR))) {
					String[] holi = holiday.get(MarketHourProperties.TRADING_HOUR).split(SymbolConstant.SYMBOL_COMMA);
					for (String hD : holi) {
						String[] hH = hD.split(SymbolConstant.SYMBOL_LINE);
						boolean isHoliday = this.checkTime(currentTimeStr, currentDateStr + " " + hH[0],
								currentDateStr + " " + hH[1], this.timezoneStr);
						if (isHoliday) {
							MarketHourProperties.logger.info(
									"{} is holiday , holiday description : {} , but is trading hour now",
									currentDateStr, holiday.get(MarketHourProperties.DESC));
							return false;
						}
					}
				} else {
					MarketHourProperties.logger.info("{} is holiday , holiday description : {}", currentDateStr,
							holiday.get(MarketHourProperties.DESC));
					return true;
				}
			}
		}
		return false;
	}

	private boolean isWeekend(Calendar cal) {
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return true;
		}
		return false;
	}

	private boolean checkTime(final String currentTime, final String openTime, final String closeTime,
			final String timeZone, final Integer... delayTime) {
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
			MarketHourProperties.logger.error("Conver the current time error....");
			MarketHourProperties.logger.error("currentTime : {} , openTime : {} , closeTime : {} , timeZone : {}",
					currentTime, openTime, closeTime, timeZone, e);
		}
		return false;
	}

	public boolean checkTradingHoursByExchange(final String exchangeCode) {
		if (StringUtil.isValid(exchangeCode)) {
			boolean isExchangeOpen = this.isMarketOpen(exchangeCode);
			return !isExchangeOpen;
		}
		return true;
	}

	public boolean hasExchangeCode(final String exchangeCode) {
		if (StringUtil.isValid(exchangeCode) && this.sessionInfo.get(exchangeCode) != null) {
			return true;
		}
		return false;
	}
	
	public String getTimeZone(String marketTypeCode){
		return this.timezone.get(marketTypeCode);
	}
}
