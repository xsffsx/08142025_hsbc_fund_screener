package com.hhhh.group.secwealth.mktdata.fund.service;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.hhhh.group.secwealth.mktdata.common.util.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarExceptionConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.VendorType;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.returnIndexChart.CalendarReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.returnIndexChart.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.returnIndexChart.R;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.returnIndexChart.ReturnIndexChartData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.ReturnIndexChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.ReturnIndexChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.returnindexchart.ReturnIndexChart;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;

@Service("returnIndexChartService")
public class ReturnIndexChartServiceImpl extends AbstractMstarService {


    private final static String STARTDATE = "startdate";


    private final static String ENDDATE = "enddate";

    @Value("#{systemConfig['mstar.conn.url.returnIndexChart']}")
    private String url;

    @Value("#{systemConfig['returnIndexChart.dataClass']}")
    private String dataClass;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    private JAXBContext dataClassJC;

    @PostConstruct
    public void init() throws Exception {
        this.dataClassJC = JAXBContext.newInstance(Class.forName(this.dataClass));
    }

    @Override
    public Object execute(final Object object) throws Exception {

        // Prepare Request
        ReturnIndexChartRequest request = (ReturnIndexChartRequest) object;
        String period = request.getPeriod();

        // Set the date depends on the period
        String startdate = request.getStartDate();
        String enddate = request.getEndDate();
        String timeZone = request.getTimeZone();
        SimpleDateFormat df = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMdd_withHyphen);
        Calendar cal = Calendar.getInstance();

        String oldFormatPattern = DateConstants.DateFormat_yyyyMMdd_withHyphen;
        String newFormatPattern = DateConstants.DateFormat_yyyyMMdd_withHyphen;
        TimeZone oldTimezone = TimeZone.getDefault();
        TimeZone newTimezone = TimeZone.getTimeZone(timeZone);

        if (StringUtil.isValid(startdate) && StringUtil.isValid(enddate)) {
            startdate = request.getStartDate();
            enddate = request.getEndDate();
        } else if (StringUtil.isValid(period) && StringUtil.isValid(timeZone)) {
            try {
                enddate = df.format(cal.getTime());
                if (period.equals("YTD")) {
                    cal.set(cal.get(Calendar.YEAR), 1 - 1, 1);
                    String dateStr = df.format(cal.getTime());
                    startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone, newTimezone);
                } else {
                    String dateType = period.substring(period.length() - 1).toUpperCase();
                    int number = Integer.parseInt(period.substring(0, period.length() - 1));
                    if (CommonConstants.YEAR_PERIOD.equals(dateType)) {
                        cal.add(Calendar.YEAR, -number);
                        cal.add(Calendar.MONTH, -1);
                    } else if (CommonConstants.MONTH_PERIOD.equals(dateType)) {
                        cal.add(Calendar.MONTH, -number - 1);
                    } else {
                        LogUtil.error(ReturnIndexChartServiceImpl.class, "period is invalid, period: " + period);
                        throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
                    }
                    String dateStr = df.format(cal.getTime());
                    startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone, newTimezone);
                }
            } catch (Exception e) {
                LogUtil.error(ReturnIndexChartServiceImpl.class, "InputParameterInvalid, period: " + period + ", timeZone: "
                    + timeZone, e);
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
            }
        } else {
            timeZone = request.getTimeZone();
            cal.add(Calendar.YEAR, -3);
            cal.add(Calendar.MONTH, -1);
            String dateStr = df.format(cal.getTime());
            startdate = DateUtil.dateByTimeZone(dateStr, oldFormatPattern, newFormatPattern, oldTimezone, newTimezone);
        }
        LogUtil.info(ReturnIndexChartServiceImpl.class, "startdate: " + startdate + ", enddate" + enddate);


        // Set parameters for chart
        String altClassCde = request.getProdCdeAltClassCde();
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String prodAltNum = request.getProdAltNum();
        String countryTradableCode = request.getMarket();
        String productType = request.getProductType();
        String locale = request.getLocale();
        SearchProduct searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde(altClassCde, countryCode,
            groupMember, prodAltNum, countryTradableCode, productType, locale);
        if (null == searchProduct || StringUtil.isInvalid(searchProduct.getExternalKey())) {
            LogUtil.error(ReturnIndexChartServiceImpl.class, "No record found for TrailingReturnChart|symbol={}", prodAltNum);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND, "No record found for " + prodAltNum);
        }

        String id = searchProduct.getExternalKey();
        ResponseData response = null;
        List<NameValuePair> params = new ArrayList<>();

        if (StringUtil.isInvalid(this.accessCodeHelper.getAccesscode())) {
            this.accessCodeHelper.createAccessCode();
        }
        params.add(new BasicNameValuePair(AbstractMstarService.ACCESSCODE, this.accessCodeHelper.getAccesscode()));
        params.add(new BasicNameValuePair(ReturnIndexChartServiceImpl.STARTDATE, startdate));
        params.add(new BasicNameValuePair(ReturnIndexChartServiceImpl.ENDDATE, enddate));
        String reqParams = URLEncodedUtils.format(params, CommonConstants.CODING_UTF8);
        String reqUrl = new StringBuilder(this.url).append(CommonConstants.SYMBOL_SLASH).append(MstarConstants.MSTARID)
            .append(CommonConstants.SYMBOL_SLASH).append(id).toString();

        LogUtil.infoOutboundMsg(ReturnIndexChartServiceImpl.class, VendorType.MSTAR, reqUrl, reqParams);

        try {
            String resp = this.connManager.sendRequest(reqUrl, reqParams, AdaptorUtil.convertNameValuePairListToString(params));
            // Mock local test data
            // String resp = MstarData.mastarData;
            LogUtil.infoInboundMsg(ReturnIndexChartServiceImpl.class, VendorType.MSTAR, resp);
            response = (ResponseData) this.dataClassJC.createUnmarshaller().unmarshal(new StringReader(resp));
        } catch (JAXBException e) {
            LogUtil.error(ReturnIndexChartServiceImpl.class, "Mstar MstarUnmarshalFail", e);
            throw new CommonException(MstarExceptionConstants.UNMARSHAL_FAIL, e.getMessage());
        } catch (Exception e) {
            String message = e.getMessage();
            if (null != message && message.toLowerCase().contains("timeout")) {
                LogUtil.error(ReturnIndexChartServiceImpl.class, "Mstar timeout error", e);
                throw new CommonException(MstarExceptionConstants.TIMEOUT, e.getMessage());
            } else if (null != message && message.toLowerCase().contains("nodata")) {
                LogUtil.error(ReturnIndexChartServiceImpl.class, "Mstar no data error", e);
                throw new CommonException(MstarExceptionConstants.NO_DATA, e.getMessage());
            } else {
                LogUtil.error(ReturnIndexChartServiceImpl.class, "Mstar undefined error", e);
                throw new CommonException(MstarExceptionConstants.UNDEFINED, e.getMessage());
            }
        }
        if (null == response) {
            LogUtil.error(ReturnIndexChartServiceImpl.class, "Mstar response is null");
            throw new CommonException(MstarExceptionConstants.UNDEFINED, "response is null");
        }
        // Prepare response
        ReturnIndexChartResponse preResponse = new ReturnIndexChartResponse();

        ReturnIndexChartData returnIndexChartData = (ReturnIndexChartData) response;

        List<ReturnIndexChart> list = new ArrayList<ReturnIndexChart>();
        if (returnIndexChartData != null) {
            Data data = returnIndexChartData.getData();
            if (data != null) {
                CalendarReturn calendarReturn = data.getCalendarReturn();
                if (null != calendarReturn) {
                    List<R> rList = calendarReturn.getR();
                    if (ListUtil.isValid(rList)) {
                        if (StringUtil.isValid(period)) {
                            String dateType = period.substring(period.length() - 1).toUpperCase();
                            int number = Integer.parseInt(period.substring(0, period.length() - 1));
                            if ((CommonConstants.MONTH_PERIOD.equals(dateType) && rList.size() > number)
                                || (CommonConstants.YEAR_PERIOD.equals(dateType) && rList.size() > number * 12)) {
                                rList.remove(0);
                            }
                        } else if (rList.size() > 3 * 12) {
                            rList.remove(0);
                        }
                        for (R rItems : rList) {
                            ReturnIndexChart returnIndexChart = new ReturnIndexChart();
                            returnIndexChart.setValue(MiscUtil.safeBigDecimalValue(rItems.getV()));
                            Calendar calendar = Calendar.getInstance();
                            calendar.clear();
                            calendar.set(Calendar.YEAR, Integer.parseInt(rItems.getY()));
                            calendar.set(Calendar.MONTH, Integer.parseInt(rItems.getM()) - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                            returnIndexChart.setDate(DateUtil.getSimpleDateFormat(calendar.getTime(),
                                DateConstants.DateFormat_yyyyMMdd_withHyphen).toString());
                            list.add(returnIndexChart);
                        }
                    }
                }
            }
        }

        // Set response
        preResponse.setResult(list);
        return preResponse;
    }
}
