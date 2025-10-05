
package com.hhhh.group.secwealth.mktdata.fund;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.hhhh.group.secwealth.mktdata.common.util.AdaptorUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.common.service.AbstractService;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarExceptionConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ResponseCode;
import com.hhhh.group.secwealth.mktdata.common.system.constants.VendorType;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.Status;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.newapi.DataSet;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarAccessCodeHelper;





@Component("abstractMstarService")
public abstract class AbstractMstarService extends AbstractService {

    
    protected final static String AUTHENTICATION_FAILED_CODE = "101";

    
    protected final static String ACCESSCODE = "accesscode";

    
    protected final static String SUCCESS_STATS_CODE = "0";

    
    protected final static String SUCCESS_STATS_MSG = "OK";

    
    protected final static String IDS = "ids";

    
    protected final static String CODE = "code";

    
    protected final static String SITETYPE = "SiteType";

    
    protected final static String CURRENCY = "currency";

    
    protected final static String DEFAULT_SITETYPE = "public";

    @Resource(name = "mstarConnManager")
    protected HttpConnectionManager connManager;

    @Value("#{systemConfig['mstar.conn.instid']}")
    protected String instid;

    @Value("#{systemConfig['mstar.conn.instuid']}")
    private String instuid;

    @Value("#{systemConfig['mstar.conn.email']}")
    private String email;

    @Value("#{systemConfig['mstar.conn.group']}")
    private String group;

    @Value("#{systemConfig['quotesummary.dataClass']}")
    private String sessionURL;

    @Autowired
    @Qualifier("mstarAccessCodeHelper")
    protected MstarAccessCodeHelper accessCodeHelper;

    @Value("#{systemConfig['mstar.conn.retryTimes']}")
    private int retryTimes;

    
    private Object sendRequest(final String reqUrl, final List<NameValuePair> params, int retryCount,
        final Unmarshaller unmarshaller) {
        ResponseData response = null;
        List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
        try {
            if (StringUtil.isInvalid(this.accessCodeHelper.getAccesscode())) {
                this.accessCodeHelper.createAccessCode();
            }
            requestParams.add(new BasicNameValuePair(AbstractMstarService.ACCESSCODE, this.accessCodeHelper.getAccesscode()));// "gios59j1ldzjtuuc742wdzq2launjcth"));
            requestParams.addAll(params);
            String reqParams = URLEncodedUtils.format(requestParams, CommonConstants.CODING_UTF8);
            LogUtil.infoOutboundMsg(AbstractMstarService.class, VendorType.MSTAR, reqUrl, reqParams);

            String resp = this.connManager.sendRequest(reqUrl, reqParams, AdaptorUtil.convertNameValuePairListToString(requestParams));

            // String resp = MstarData.mastarData;
            LogUtil.infoInboundMsg(AbstractMstarService.class, VendorType.MSTAR, resp);
            response = (ResponseData) unmarshaller.unmarshal(new StringReader(resp));
            if (retryCount < this.retryTimes && null != response
                && AbstractMstarService.AUTHENTICATION_FAILED_CODE.equals(response.getStatus().getCode())) {
                ++retryCount;
                this.accessCodeHelper.createAccessCode();
                // Retry to aovid death lock
                return sendRequest(reqUrl, params, retryCount, unmarshaller);
            }
        } catch (JAXBException e) {
            LogUtil.error(AbstractMstarService.class, "Mstar MstarUnmarshalFail", e);
            throw new CommonException(MstarExceptionConstants.UNMARSHAL_FAIL, e.getMessage());
        } catch (Exception e) {
            String message = e.getMessage();
            if (null != message && message.toLowerCase().contains("timeout")) {
                LogUtil.error(AbstractMstarService.class, "Mstar timeout error", e);
                throw new CommonException(MstarExceptionConstants.TIMEOUT, e.getMessage());
            } else if (null != message && message.toLowerCase().contains("nodata")) {
                LogUtil.error(AbstractMstarService.class, "Mstar no data error", e);
                throw new CommonException(MstarExceptionConstants.NO_DATA, e.getMessage());
            } else {
                LogUtil.error(AbstractMstarService.class, "Mstar undefined error", e);
                throw new CommonException(MstarExceptionConstants.UNDEFINED, e.getMessage());
            }
        }

        if (null == response || null == response.getStatus()) {
            LogUtil.error(AbstractMstarService.class, "Mstar response is null");
            throw new CommonException(MstarExceptionConstants.UNDEFINED);
        } else {
            Status status = response.getStatus();
            String code = StringUtil.isValid(status.getCode()) ? status.getCode() : ErrTypeConstants.VENDOREXCEPTION_DEFAULT_CODE;
            String msg = status.getMessage();
            if (!AbstractMstarService.SUCCESS_STATS_CODE.equals(code) || !AbstractMstarService.SUCCESS_STATS_MSG.equals(msg)) {
                LogUtil.error(AbstractMstarService.class, "Mstar VendorException ResponseCode is: {}", VendorType.MSTAR + code);
                throw new VendorException(CommonConstants.EMPTY_STRING, ResponseCode.ERROR, VendorType.MSTAR + code, msg);
            }
        }

        return response;
    }

    
    private Object sendRequest2NewApi(final String idType, final String ids, final String url, int retryCount,
        final Unmarshaller unmarshaller, final String siteType) {

        DataSet response = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            if (StringUtil.isInvalid(this.accessCodeHelper.getAccesscode())) {
                this.accessCodeHelper.createAccessCode();
            }
            params.add(new BasicNameValuePair(AbstractMstarService.ACCESSCODE, this.accessCodeHelper.getAccesscode()));
            params.add(new BasicNameValuePair(AbstractMstarService.CODE, ids));
            params.add(new BasicNameValuePair(AbstractMstarService.SITETYPE, this.getSiteType(siteType)));
            if (StringUtil.isValid(this.getCurrencyAlignKey())) {
                params.add(new BasicNameValuePair(AbstractMstarService.CURRENCY, this.getCurrencyAlignKey()));
            }
            String reqParams = URLEncodedUtils.format(params, CommonConstants.CODING_UTF8);
            String reqUrl = new StringBuilder(url).toString();
            LogUtil.infoOutboundMsg(AbstractMstarService.class, VendorType.MSTAR, reqUrl, reqParams);

            String resp = this.connManager.sendRequest(reqUrl, reqParams, AdaptorUtil.convertNameValuePairListToString(params));

            LogUtil.infoInboundMsg(AbstractMstarService.class, VendorType.MSTAR, resp);

            resp = handleXmlStr(resp);
            Reader reader = new StringReader(resp);
            XMLInputFactory xif = XMLInputFactory.newInstance();
            XMLStreamReader xsr = xif.createXMLStreamReader(reader);

            xsr = xif.createFilteredReader(xsr, new StreamFilter() {
                @Override
                public boolean accept(final XMLStreamReader xsr) {
                    if (xsr.isStartElement() || xsr.isEndElement()) {
                        return !"urn:schemas-microsoft-com:xml-diffgram-v1".equals(xsr.getNamespaceURI());
                    }
                    return true;
                }
            });
            LogUtil.debug(AbstractMstarService.class, resp);

            response = (DataSet) unmarshaller.unmarshal(xsr);
            if (retryCount < this.retryTimes && response == null) {
                ++retryCount;
                this.accessCodeHelper.createAccessCode();
                // Retry to aovid death lock
                return sendRequest2NewApi(idType, ids, url, retryCount, unmarshaller, siteType);
            }
        } catch (JAXBException e) {
            LogUtil.error(AbstractMstarService.class, "sendRequest2NewApi error", e);
            throw new CommonException(MstarExceptionConstants.UNMARSHAL_FAIL, e.getMessage());
        } catch (Exception e) {
            String message = e.getMessage();
            if (null != message && message.toLowerCase().contains("timeout")) {
                LogUtil.error(AbstractMstarService.class, "Mstar timeout error", e);
                throw new CommonException(MstarExceptionConstants.TIMEOUT, e.getMessage());
            } else if (null != message && message.toLowerCase().contains("nodata")) {
                LogUtil.error(AbstractMstarService.class, "Mstar no data error", e);
                throw new CommonException(MstarExceptionConstants.NO_DATA, e.getMessage());
            } else {
                LogUtil.error(AbstractMstarService.class, "Mstar undefined error", e);
                throw new CommonException(MstarExceptionConstants.UNDEFINED, e.getMessage());
            }
        }

        if (null == response || null == response.getQuickTake()) {
            LogUtil.error(AbstractMstarService.class, "Mstar response is null");
            throw new CommonException(MstarExceptionConstants.UNDEFINED);
        }
        return response;
    }

    
    private String handleXmlStr(final String xmlStr) {
        String result = null;
        // Start String will be remove
        String startStrIndex = "<xs:schema";
        // End String will be remove
        String endStrIndex = "</xs:schema>";

        int a = xmlStr.indexOf(startStrIndex);
        int b = xmlStr.indexOf(endStrIndex);
        if (a > -1 && b > -1) {
            result = xmlStr.substring(0, a) + xmlStr.substring(b + endStrIndex.length(), xmlStr.length());

            // Start String will be remove
            String startStrIndex2 = "<ChartLink";
            // End String will be remove
            String endStrIndex2 = "</ChartLink>";

            int a2 = result.indexOf(startStrIndex2);
            int b2 = result.indexOf(endStrIndex2);
            if (a2 > -1 && b2 > -1) {
                result = result.substring(0, a2) + result.substring(b2 + endStrIndex.length(), result.length());
                result = result.replace("http://tempuri.org/", "");
            }
        }
        if (StringUtil.isInvalid(result)) {
            LogUtil.error(AbstractMstarService.class, "Mstar response is Invalid: " + xmlStr);
            throw new CommonException(MstarExceptionConstants.INVALID_RESPONSE);
        }
        return result;
    }

    
    protected Object sendRequest(final String idType, final String ids, final String url, final Unmarshaller unmarshaller) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(AbstractMstarService.IDS, ids));
        if (StringUtil.isValid(this.getCurrencyAlignKey())) {
            params.add(new BasicNameValuePair(AbstractMstarService.CURRENCY, this.getCurrencyAlignKey()));
        }
        String reqUrl = new StringBuilder().append(url).append(CommonConstants.SYMBOL_SLASH).append(idType).toString();
        return this.sendRequest(reqUrl, params, 0, unmarshaller);
    }

    
    protected Object sendRequest(final String idType, final String currency, final String ids, final String url,
        final Unmarshaller unmarshaller) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(AbstractMstarService.IDS, ids));
        params.add(new BasicNameValuePair(AbstractMstarService.CURRENCY, currency));
        if (StringUtil.isValid(this.getCurrencyAlignKey())) {
            params.add(new BasicNameValuePair(AbstractMstarService.CURRENCY, this.getCurrencyAlignKey()));
        }
        String reqUrl = new StringBuilder(url).append(CommonConstants.SYMBOL_SLASH).append(idType).toString();
        return this.sendRequest(reqUrl, params, 0, unmarshaller);
    }

    
    protected Object sendRequest(final String reqUrl, final List<NameValuePair> params, final Unmarshaller unmarshaller) {
        return this.sendRequest(reqUrl, params, 0, unmarshaller);
    }

    
    protected Object sendRequest2NewApi(final String idType, final String ids, final String url, final Unmarshaller unmarshaller,
        final String siteType) {
        return this.sendRequest2NewApi(idType, ids, url, 0, unmarshaller, siteType);
    }

    
    protected String getCurrencyAlignKey() {
        return null;
    }

    private String getSiteType(final String siteType) {
        if (StringUtil.isValid(siteType)) {
            return siteType;
        }
        return AbstractMstarService.DEFAULT_SITETYPE;
    }
}
