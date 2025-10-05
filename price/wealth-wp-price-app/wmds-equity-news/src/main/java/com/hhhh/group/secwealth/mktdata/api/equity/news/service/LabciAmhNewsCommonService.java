package com.hhhh.group.secwealth.mktdata.api.equity.news.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.news.constants.NewsConstant;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsDetailRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.News;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.NewsDetailResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci.Body;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci.Envelop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Component
public class LabciAmhNewsCommonService {
    private static final Logger logger = LoggerFactory.getLogger(LabciAmhNewsCommonService.class);


    public static final String NORMAL_RESPONSE_CODE = "000";

    private static final int MINUTES = 15;
    private static final String OBO = "N";
    private static final String APP_CODE = "MDS_HASE";
    private static final String DURATION = "15";
    private static final String MARKET_FLAG = "Y";
    private static final String NUMBER_OF_MARKET = "1";

    public static final Map<String, String> LABCI_LOCALE_MAPPING = new HashMap<String, String>() {
        {
//            put("en", "en");
//            put("en_US", "en");
//            put("zh_HK", "en");
//            put("zh_CN", "en");
//            put("zh", "en");

            put("en", "en");
            put("en_US", "en");
            put("zh_HK", "hk");
            put("zh_CN", "cn");
            put("zh", "hk");
        }
    };

    public String assembleRequest(NewsDetailRequest newsContentSvcRequest, String locale) {
        StringBuffer sb = new StringBuffer(256);
        sb.append("<envelop><header>")
                .append("<msgid>newsstory</msgid>")
                .append("<marketid>")
                .append("US")
                .append("</marketid>")
                .append("</header><body>")
                .append("<newsid>")
                .append(newsContentSvcRequest.getId())
                .append("</newsid>")
                .append("<locale>")
                .append(LABCI_LOCALE_MAPPING.get(locale))
                .append("</locale>")
                .append("<translate>")
                .append(newsContentSvcRequest.isTranslate() == true? "Y":"N")
                .append("</translate>")
                .append("</body></envelop>");
        return sb.toString();
    }

    /**
     * Parse the LabCI responded string into object Envelop.
     *
     * @param responseStr
     * @return
     */
    @SuppressWarnings("restriction")
    public Envelop getResponseEnvelop(String responseStr) {
        InputStream inputStream = new ByteArrayInputStream(
                responseStr.getBytes());
        InputStreamReader inputStreamReader = null;
        Envelop envelop = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream,
                    NewsConstant.CODING_UTF8);
            envelop = (Envelop) unmarShallerClass(Envelop.class).unmarshal(
                    inputStreamReader);
        } catch (JAXBException | UnsupportedEncodingException e) {
            logger.error("parse envelop error");
        } finally {
            if (null != inputStreamReader)
                try {
                    inputStreamReader.close();
                } catch (IOException e1) {
                    logger.error("inputStreamReader.close() fail");
                }

            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    logger.error("inputStreamReader.close() fail");
                }
            }
        }

        return envelop;
    }


    public Unmarshaller unmarShallerClass(final Class Class) {
        Unmarshaller unmarshaller = null;
        try {
            JAXBContext jsxbContext = JAXBContext.newInstance(Class);
            unmarshaller = jsxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            logger.error("Unmarshaller init fail.");
        }
        return unmarshaller;
    }


    public LabciToken getTokenModel(String channelId, String market) {
        LabciToken labciToken = new LabciToken();
        labciToken.setChannelId(channelId);
        labciToken.setCustomerId(null);
        labciToken.setMarketId(market);
        //todo
        labciToken.setAppCode(APP_CODE);
//        labciToken.setAppCode("PIB");
        labciToken.setNumberOfMarket(NUMBER_OF_MARKET);
        labciToken.setDuration(DURATION);
        labciToken.setMarketFlag(MARKET_FLAG);
        labciToken.setObo(OBO);
        String timeStamp = DateUtil.afterMinutesOfCurrent(DateUtil.DATE_DAY_PATTERN_yyyyMMddHHmmss, TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID),
                MINUTES);
        labciToken.setTimeStamp(timeStamp);
        return labciToken;
    }

    public NewsDetailResponse convertNewsDetailResponse(Envelop obj) throws ParseException {
        Envelop responseEnvelop = obj;
        Body body = responseEnvelop.getBody();
        NewsDetailResponse response = new NewsDetailResponse();
        response.setId(body.getNewsid());
        response.setContent(removeCdataTag(body.getNewscontent()));
        response.setNewsLang(body.getNewslang());
        response.setHeadline(removeCdataTag(body.getNewstopic()));
        String newsUpdateTime = body.getLastupdatedtime();
        String newsUpdateDate = body.getLastupdateddate();
        String newsTime = getFormatDataString(newsUpdateDate, newsUpdateTime, body.getTimezone());
        response.setAsOfDateTime(newsTime);
        response.setAsOfDate(newsUpdateDate);
        response.setAsOfTime(newsUpdateTime);
        String newscompany = body.getNewscompany();
                if (StringUtil.isValid(newscompany) && !"--".equals(newscompany)) {
                    String[] relatedCodes = newscompany.split("\\ ");
                    response.setRelatedCodes(relatedCodes);
                }
        return response;
    }

    public String getFormatDataString(String newsUpdateDate, String newsUpdateTime, String timezone) throws ParseException {
        String newsTime = null;
        if (StringUtil.isValid(newsUpdateDate) && StringUtil.isValid(newsUpdateTime)) {
            String timeStr = newsUpdateDate + " " + newsUpdateTime;
            TimeZone timeZone = TimeZone.getTimeZone(timezone);
            newsTime = DateUtil.convertToISO8601Format(timeStr , DateUtil.DATE_HOUR_PATTERN, timeZone, null);
        }
        return newsTime;
    }

    public String removeCdataTag( String newsContent){
//        newsContent=newsContent
//                .replaceAll("<\\!\\[CDATA\\[", "")
//                .replaceAll("\\]\\]>", "").replace("\t", "").replace("’", "'")
//                .replace("\n", "")
////                .replace("\n ", "<BR>")
//                .replace("<p>", "1####1")
//                .replace("</p>", "2#####2")
//                .replace("<p/>", "3######3")
//                .replaceAll("<[^>]+>", "")
//                .replace("1####1", "<p>")
//                .replace("2#####2", "</p>")
//                .replace("3######3", "<p/>");



        newsContent=newsContent.replaceAll("<\\!\\[CDATA\\[", "")
                .replaceAll("\\]\\]>", "").replace("\t", "").replace("’", "'")
                .replace("\n", "<BR>")
                .replace("<Vp>", "<p>")
                .replace("\n", "");
        return newsContent;
    }
}
