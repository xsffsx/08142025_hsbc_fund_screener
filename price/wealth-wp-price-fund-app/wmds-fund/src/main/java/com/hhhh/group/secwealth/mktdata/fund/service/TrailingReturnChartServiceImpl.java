

package com.hhhh.group.secwealth.mktdata.fund.service;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.hhhh.group.secwealth.mktdata.common.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarExceptionConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ResponseCode;
import com.hhhh.group.secwealth.mktdata.common.system.constants.VendorType;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.Status;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.bestwrostreturn.BestReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.bestwrostreturn.BestWrostReturnData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.bestwrostreturn.WorstReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.dayenddate.DayEndDateData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.trailingreturnschart.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.trailingreturnschart.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.trailingreturnschart.R;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.trailingreturnschart.TrailingReturnsChartData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.TrailingReturnChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.TrailingReturnChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.trailingreturnschart.TrailingReturnItem;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.trailingreturnschart.TrailingReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.trailingreturnschart.TrailingReturnsAnalysis;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.trailingreturnschart.TrailingReturnsAnalysisItem;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarConvertorHelper;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;


@Service("trailingReturnChartService")
public class TrailingReturnChartServiceImpl extends AbstractMstarService {

    
    private final static String STARTDATE = "startdate";

    
    private final static String ENDDATE = "enddate";

    
    private final static String PERIODRETURN = "periodreturn";

    @Value("#{systemConfig['mstar.conn.url.dayenddate']}")
    private String url;

    @Value("#{systemConfig['trailingReturnChart.dataClass']}")
    private String dataClass;

    @Value("#{systemConfig['mstar.conn.url.trailingreturnchart']}")
    private String chartURL;

    
    @Value("#{systemConfig['trailingReturnChart.chartClass']}")
    private String chartClass;

    @Value("#{systemConfig['mstar.conn.url.bestwrostreturn']}")
    private String tableURL;

    @Value("#{systemConfig['trailingReturnChart.tableClass']}")
    private String tableClass;

    
    private JAXBContext dataClassJC;
    private JAXBContext chartClassJC;
    private JAXBContext tableClassJC;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @PostConstruct
    public void init() throws Exception {
        this.dataClassJC = JAXBContext.newInstance(Class.forName(this.dataClass));
        this.chartClassJC = JAXBContext.newInstance(Class.forName(this.chartClass));
        this.tableClassJC = JAXBContext.newInstance(Class.forName(this.tableClass));
    }

    @Override
    public Object execute(final Object object) throws Exception {
        // Prepare Request
        TrailingReturnChartRequest request = (TrailingReturnChartRequest) object;
        String periodReturn = request.getTrailingReturnsPeriod();

        // Get the day end date as end date
        String endDate = null;
        DayEndDateData dayEndDateData = (DayEndDateData) sendRequest(request);
        if (null != dayEndDateData) {
            if (null != dayEndDateData.getData()) {
                if (null != dayEndDateData.getData().getApi()) {
                    endDate = dayEndDateData.getData().getApi().getDPDayEndDate();
                }
            }
        }

        // Set the date depends on the period
        String startDate = null;
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMdd_withHyphen);
        Calendar cal = Calendar.getInstance();
        if (null != endDate) {
            date = df.parse(endDate);
            cal.setTime(date);
        } else {
            endDate = df.format(cal.getTime());
        }
        if (periodReturn.equals("3")) {
            cal.add(Calendar.YEAR, -10);
            cal.add(Calendar.MONTH, -2);
            startDate = df.format(cal.getTime());
        } else if (periodReturn.equals("12")) {
            cal.add(Calendar.YEAR, -11);
            cal.add(Calendar.MONTH, +1);
            startDate = df.format(cal.getTime());
        } else if (periodReturn.equals("36")) {
            cal.add(Calendar.YEAR, -13);
            cal.add(Calendar.MONTH, +1);
            startDate = df.format(cal.getTime());
        } else if (periodReturn.equals("60")) {
            cal.add(Calendar.YEAR, -15);
            cal.add(Calendar.MONTH, +1);
            startDate = df.format(cal.getTime());
        }
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
            LogUtil.error(TrailingReturnChartServiceImpl.class, "No record found for TrailingReturnChart|symbol={}", prodAltNum);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND, "No record found for " + prodAltNum);
        }
        String id = searchProduct.getExternalKey();

        ResponseData response = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // Set parameters for chart
        params.add(new BasicNameValuePair(TrailingReturnChartServiceImpl.STARTDATE, startDate));
        params.add(new BasicNameValuePair(TrailingReturnChartServiceImpl.ENDDATE, endDate));
        params.add(new BasicNameValuePair(TrailingReturnChartServiceImpl.PERIODRETURN, periodReturn));
        params.add(new BasicNameValuePair(AbstractMstarService.ACCESSCODE, this.accessCodeHelper.getAccesscode()));
        String reqParams = URLEncodedUtils.format(params, CommonConstants.CODING_UTF8);
        String reqUrl = new StringBuilder(this.chartURL).append(CommonConstants.SYMBOL_SLASH).append(MstarConstants.MSTARID)
            .append(CommonConstants.SYMBOL_SLASH).append(id).toString();

        LogUtil.infoOutboundMsg(TrailingReturnChartServiceImpl.class, VendorType.MSTAR, reqUrl, reqParams);

        try {
            String resp = this.connManager.sendRequest(reqUrl, reqParams, AdaptorUtil.convertNameValuePairListToString(params));
            LogUtil.infoInboundMsg(TrailingReturnChartServiceImpl.class, VendorType.MSTAR, resp);
            response = (ResponseData) this.chartClassJC.createUnmarshaller().unmarshal(new StringReader(resp));
        } catch (JAXBException e) {
            LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar MstarUnmarshalFail", e);
            throw new CommonException(MstarExceptionConstants.UNMARSHAL_FAIL, e.getMessage());
        } catch (Exception e) {
            String message = e.getMessage();
            if (null != message && message.toLowerCase().contains("timeout")) {
                LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar timeout error", e);
                throw new CommonException(MstarExceptionConstants.TIMEOUT, e.getMessage());
            } else if (null != message && message.toLowerCase().contains("nodata")) {
                LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar no data error", e);
                throw new CommonException(MstarExceptionConstants.NO_DATA, e.getMessage());
            } else {
                LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar undefined error", e);
                throw new CommonException(MstarExceptionConstants.UNDEFINED, e.getMessage());
            }
        }
        if (null == response || null == response.getStatus()) {
            LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar response is null");
            throw new CommonException(MstarExceptionConstants.UNDEFINED, "response is null");
        } else {
            Status status = response.getStatus();
            String code = status.getCode();
            String msg = status.getMessage();
            if (!AbstractMstarService.SUCCESS_STATS_CODE.equals(code) || !AbstractMstarService.SUCCESS_STATS_MSG.equals(msg)) {
                if (StringUtil.isInvalid(code)) {
                    code = StringUtil.isValid(status.getErrorCode()) ? status.getErrorCode()
                        : ErrTypeConstants.VENDOREXCEPTION_DEFAULT_CODE;
                }
                LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar VendorException ResponseCode is: {}", VendorType.MSTAR
                    + code);
                throw new VendorException(CommonConstants.EMPTY_STRING, ResponseCode.ERROR, VendorType.MSTAR + code, msg);
            }
        }

        // Prepare response
        TrailingReturnChartResponse preResponse = new TrailingReturnChartResponse();
        TrailingReturnsChartData trailingReturnsChartData = (TrailingReturnsChartData) response;
        TrailingReturns trailingReturns = new TrailingReturns();
        String lastUpdateDate = null;
        String[] values = new String[] {};
        String[] dates = new String[] {};
        int firstYear = 0, lastYear = 0;
        if (null != trailingReturnsChartData) {
            Data data = trailingReturnsChartData.getData();
            if (null != data) {
                Api api = data.getApi();
                if (null != api) {
                    List<R> rList = api.getR();
                    List<String> valueList = new ArrayList<String>();
                    List<String> datesList = new ArrayList<String>();
                    if (ListUtil.isValid(rList)) {
                        for (R rItems : rList) {
                            valueList.add(rItems.getV());
                            datesList.add(rItems.getD());
                        }
                        values = valueList.toArray((new String[valueList.size()]));
                        dates = datesList.toArray((new String[valueList.size()]));

                        firstYear = Integer.valueOf(rList.get(0).getD().split(CommonConstants.SYMBOL_LINE_CONNECTIVE)[0]);
                        lastYear = Integer
                            .valueOf(rList.get(rList.size() - 1).getD().split(CommonConstants.SYMBOL_LINE_CONNECTIVE)[0]);
                    }
                }
            }
        }
        int showDataYear = lastYear - firstYear + 1;
        if (showDataYear > 11) {
            showDataYear = 11;
        }
        TrailingReturnItem[] chartDatas = new TrailingReturnItem[showDataYear];

        String currentYear = null;
        String years[] = new String[showDataYear];
        int yearsCount = 0;
        for (int i = 0; i < dates.length; i++) {
            String[] dateStrs = dates[i].split(CommonConstants.SYMBOL_LINE_CONNECTIVE);
            if (i == 0) {
                years[0] = dateStrs[0];
            }
            if (!years[yearsCount].equals(dateStrs[0]) && yearsCount < showDataYear - 1) {
                yearsCount = yearsCount + 1;
                years[yearsCount] = dateStrs[0];
            }
        }
        yearsCount = 0;
        chartDatas[yearsCount] = new TrailingReturnItem();
        for (int i = 0; i < dates.length; i++) {
            String[] dateStrs = dates[i].split(CommonConstants.SYMBOL_LINE_CONNECTIVE);
            currentYear = dateStrs[0];
            if (!currentYear.equals(years[yearsCount]) && yearsCount < showDataYear - 1) {
                chartDatas[yearsCount].setPeriod(MiscUtil.safeBigDecimalValue(years[yearsCount]));
                yearsCount = yearsCount + 1;
                chartDatas[yearsCount] = new TrailingReturnItem();
            }
            if (currentYear.equals(years[yearsCount])) {
                if ("01".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setJan(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("02".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setFeb(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("03".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setMar(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("04".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setApr(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("05".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setMay(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("06".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setJun(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("07".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setJul(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("08".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setAug(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("09".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setSep(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("10".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setOct(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("11".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setNov(MiscUtil.safeBigDecimalValue(values[i]));
                }
                if ("12".equals(dateStrs[1])) {
                    chartDatas[yearsCount].setDec(MiscUtil.safeBigDecimalValue(values[i]));
                }
            }
            if (i == dates.length - 1) {
                chartDatas[yearsCount].setPeriod(MiscUtil.safeBigDecimalValue(years[yearsCount]));
                // set the last update date here
                lastUpdateDate = MstarConvertorHelper.covertDateString(dates[i]);
            }
        }

        trailingReturns.setReturnPeriod(request.getTrailingReturnsPeriod());
        trailingReturns.setChartDatas(chartDatas);
        preResponse.setTrailingReturns(trailingReturns);

        // Get the table data
        response = null;
        params = new ArrayList<NameValuePair>();
        // Set parameters for chart
        params.add(new BasicNameValuePair(TrailingReturnChartServiceImpl.PERIODRETURN, periodReturn));
        params.add(new BasicNameValuePair(TrailingReturnChartServiceImpl.STARTDATE, startDate));
        params.add(new BasicNameValuePair(TrailingReturnChartServiceImpl.ENDDATE, endDate));
        params.add(new BasicNameValuePair(AbstractMstarService.ACCESSCODE, this.accessCodeHelper.getAccesscode()));
        reqParams = URLEncodedUtils.format(params, CommonConstants.CODING_UTF8);
        reqUrl = new StringBuilder(this.tableURL).append(CommonConstants.SYMBOL_SLASH).append(MstarConstants.MSTARID)
            .append(CommonConstants.SYMBOL_SLASH).append(id).toString();

        LogUtil.infoOutboundMsg(TrailingReturnChartServiceImpl.class, VendorType.MSTAR, reqUrl, reqParams);
        try {
            String resp = this.connManager.sendRequest(reqUrl, reqParams, AdaptorUtil.convertNameValuePairListToString(params));
            String respMsg = new StringBuilder(CommonConstants.INBOUND_MSG_PREFIX).append(resp).toString();
            LogUtil.debug(TrailingReturnChartServiceImpl.class, respMsg);
            response = (ResponseData) this.tableClassJC.createUnmarshaller().unmarshal(new StringReader(resp));
        } catch (JAXBException e) {
            LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar MstarUnmarshalFail", e);
            throw new CommonException(MstarExceptionConstants.UNMARSHAL_FAIL, e.getMessage());
        } catch (Exception e) {
            String message = e.getMessage();
            if (null != message && message.toLowerCase().contains("timeout")) {
                LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar timeout error", e);
                throw new CommonException(MstarExceptionConstants.TIMEOUT, e.getMessage());
            } else if (null != message && message.toLowerCase().contains("nodata")) {
                LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar no data error", e);
                throw new CommonException(MstarExceptionConstants.NO_DATA, e.getMessage());
            } else {
                LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar undefined error", e);
                throw new CommonException(MstarExceptionConstants.UNDEFINED, e.getMessage());
            }
        }
        if (null == response || null == response.getStatus()) {
            LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar response is null");
            throw new CommonException(MstarExceptionConstants.UNDEFINED, "response is null");
        } else {
            Status status = response.getStatus();
            String code = status.getCode();
            String msg = status.getMessage();
            if (!AbstractMstarService.SUCCESS_STATS_CODE.equals(code) || !AbstractMstarService.SUCCESS_STATS_MSG.equals(msg)) {
                if (StringUtil.isInvalid(code)) {
                    code = StringUtil.isValid(status.getErrorCode()) ? status.getErrorCode()
                        : ErrTypeConstants.VENDOREXCEPTION_DEFAULT_CODE;
                }
                LogUtil.error(TrailingReturnChartServiceImpl.class, "Mstar VendorException ResponseCode is: {}", VendorType.MSTAR
                    + code);
                throw new VendorException(CommonConstants.EMPTY_STRING, ResponseCode.ERROR, VendorType.MSTAR + code, msg);
            }
        }

        // Prepare Table response
        BestWrostReturnData bestWrostReturnData = (BestWrostReturnData) response;
        TrailingReturnsAnalysis trailingReturnsAnalysis = new TrailingReturnsAnalysis();
        List<TrailingReturnsAnalysisItem> item = new ArrayList<TrailingReturnsAnalysisItem>();

        if (null != bestWrostReturnData) {
            com.hhhh.group.secwealth.mktdata.fund.beans.mstar.bestwrostreturn.Data data = bestWrostReturnData.getData();
            LogUtil
                .debug(TrailingReturnChartServiceImpl.class, "TrailingReturnChart BestWrostReturnData is null: {}", data == null);
            if (null != data) {
                com.hhhh.group.secwealth.mktdata.fund.beans.mstar.bestwrostreturn.Api api = data.getApi();
                if (null != api) {
                    BestReturn bestReturn = api.getBestReturn();
                    WorstReturn worstReturn = api.getWorstReturn();
                    LogUtil.debug(TrailingReturnChartServiceImpl.class,
                        "TrailingReturnChart BestReturn is null: {}, WorstReturn is null: {}", bestReturn == null,
                        worstReturn == null);
                    if (null != bestReturn) {
                        TrailingReturnsAnalysisItem trailingReturnsAnalysisItem = new TrailingReturnsAnalysisItem();
                        trailingReturnsAnalysisItem.setItemName("BEST");
                        trailingReturnsAnalysisItem.setReturnValue(bestReturn.getV());
                        trailingReturnsAnalysisItem
                            .setStartPeriod(MstarConvertorHelper.covertDateString(bestReturn.getStartdate()));
                        trailingReturnsAnalysisItem.setEndPeriod(MstarConvertorHelper.covertDateString(bestReturn.getEnddate()));
                        item.add(trailingReturnsAnalysisItem);
                    }
                    if (null != worstReturn) {
                        TrailingReturnsAnalysisItem trailingReturnsAnalysisItem = new TrailingReturnsAnalysisItem();
                        trailingReturnsAnalysisItem.setItemName("WORST");
                        trailingReturnsAnalysisItem.setReturnValue(worstReturn.getV());
                        trailingReturnsAnalysisItem
                            .setStartPeriod(MstarConvertorHelper.covertDateString(worstReturn.getStartdate()));
                        trailingReturnsAnalysisItem.setEndPeriod(MstarConvertorHelper.covertDateString(worstReturn.getEnddate()));
                        item.add(trailingReturnsAnalysisItem);
                    }
                }
            }
        }
        trailingReturnsAnalysis.setLastUpdatedDate(lastUpdateDate);
        trailingReturnsAnalysis.setItems(item);
        preResponse.setTrailingReturnsAnalysis(trailingReturnsAnalysis);

        return preResponse;
    }

    protected Object sendRequest(final TrailingReturnChartRequest request) throws Exception {
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
            LogUtil.error(AbstractMstarService.class, "No record found for Mstar|ProdAltNum={}", prodAltNum);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND, "No record found for " + prodAltNum);
        }
        String id = searchProduct.getExternalKey();
        return super.sendRequest(MstarConstants.MSTARID, id, this.url, this.dataClassJC.createUnmarshaller());
    }
}
