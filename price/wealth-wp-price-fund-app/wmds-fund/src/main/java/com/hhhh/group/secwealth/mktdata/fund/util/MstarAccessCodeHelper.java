
package com.hhhh.group.secwealth.mktdata.fund.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;

import com.hhhh.group.secwealth.mktdata.common.util.AdaptorUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarExceptionConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ResponseCode;
import com.hhhh.group.secwealth.mktdata.common.system.constants.VendorType;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.accesscode.AccessCodeData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.accesscode.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.accesscode.MStarIDSearchData;



@Component("mstarAccessCodeHelper")
public class MstarAccessCodeHelper {

    private final String ACCESSCODEDATA_CLASS = "com.hhhh.group.secwealth.mktdata.fund.beans.mstar.accesscode.AccessCodeData";
    private final String MSTARIDSEARCHDATA_CLASS = "com.hhhh.group.secwealth.mktdata.fund.beans.mstar.accesscode.MStarIDSearchData";

    
    protected final static String SUCCESS_STATS_CODE = "0";

    
    protected final static String ACCESSCODE = "accesscode";

    
    @Resource(name = "mstarConnManager")
    protected HttpConnectionManager connManager;

    
    protected String accesscode = "";

    
    private final static String ACCOUNT_CODE = "account_code";

    
    private final static String SUCCESS_CODE = "<code>0</code>";

    
    @Value("#{systemConfig['mstar.conn.accesscode.accountCode']}")
    private String account_code;

    
    private final static String ACCOUNT_PASSWORD = "account_password";

    
    @Value("#{systemConfig['mstar.conn.accesscode.accountPassword']}")
    private String account_password;

    
    private String decryptPassword;

    
    @Value("#{systemConfig['mstar.conn.accesscode.days']}")
    private String accessCode_days;

    
    @Value("#{systemConfig['mstar.conn.url.accesscodeCreation']}")
    private String accesscodeCreation;

    
    @Value("#{systemConfig['mstar.conn.url.accesscodeDeletion']}")
    private String accesscodeDeletion;

    
    @Value("#{systemConfig['mstar.conn.url.accesscodeVerification']}")
    private String accesscodeVerification;

    
    protected JAXBContext accessCodeJC;

    
    protected JAXBContext mstarIDSJC;

    @Autowired
    @Qualifier("authenticationService")
    protected AuthenticationService encryptService;


    
    @PostConstruct
    public void init() throws Exception {
        try {
            this.accessCodeJC = JAXBContext.newInstance(Class.forName(this.ACCESSCODEDATA_CLASS));
            this.mstarIDSJC = JAXBContext.newInstance(Class.forName(this.MSTARIDSEARCHDATA_CLASS));

            this.decryptPassword = this.encryptService.decrypt(this.account_password);

            // createAccessCode();
        } catch (Exception e) {
            LogUtil.error(MstarAccessCodeHelper.class, "Can't init MstarAccessCodeHelper|exception=" + e.getMessage(), e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        }
    }

    
    public synchronized void createAccessCode() throws Exception {
        if (StringUtil.isInvalid(this.accesscode) || (!verifyAccessCode(this.accesscode))) {
            AccessCodeData accessCodeData = null;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(MstarAccessCodeHelper.ACCOUNT_CODE, this.account_code));
            params.add(new BasicNameValuePair(MstarAccessCodeHelper.ACCOUNT_PASSWORD, this.decryptPassword));
            String reqParams = URLEncodedUtils.format(params, CommonConstants.CODING_UTF8);
            String reqUrl = new StringBuilder(this.accesscodeCreation).append(CommonConstants.SYMBOL_SLASH)
                .append(this.accessCode_days).toString();
            try {
                String accessCodeStr = this.connManager.sendRequest(reqUrl, reqParams, AdaptorUtil.convertNameValuePairListToString(params));
                accessCodeData = (AccessCodeData) this.accessCodeJC.createUnmarshaller().unmarshal(new StringReader(accessCodeStr));
                LogUtil.debug(MstarAccessCodeHelper.class, "The response for Create Access Code:" + accessCodeStr);
                LogUtil.infoBeanToJson(MstarAccessCodeHelper.class, "The response for Create accessCodeData Object: {}",
                    accessCodeData != null ? accessCodeData : "");
                List<Data> dataList = accessCodeData.getData();
                if (MstarAccessCodeHelper.SUCCESS_STATS_CODE.equals(accessCodeData.getStatus().getCode()) && null != dataList
                    && dataList.size() > 0) {
                    setAccesscode(accessCodeData.getData().get(0).getApi().getAccessCode());
                } else {
                    LogUtil.errorBeanToJson(MstarAccessCodeHelper.class, "Fail to Create accessCodeData Object: {}",
                        accessCodeData != null ? accessCodeData : "");
                    String code = StringUtil.isValid(accessCodeData.getStatus().getCode()) ? accessCodeData.getStatus().getCode()
                        : ErrTypeConstants.VENDOREXCEPTION_DEFAULT_CODE;
                    LogUtil.error(MstarAccessCodeHelper.class, "Fail to create access code, code: {}", code);
                    throw new VendorException(CommonConstants.EMPTY_STRING, ResponseCode.ERROR, VendorType.MSTAR + code,
                        "Fail to create access code");
                }
            } catch (Exception e) {
                LogUtil.error(MstarAccessCodeHelper.class, "Fail to create access code", e);
                String message = e.getMessage();
                if (null != message && message.toLowerCase().contains("timeout")) {
                    throw new CommonException(MstarExceptionConstants.TIMEOUT, e.getMessage());
                } else if (null != message && message.toLowerCase().contains("nodata")) {
                    throw new CommonException(MstarExceptionConstants.NO_DATA, e.getMessage());
                } else {
                    throw new CommonException(MstarExceptionConstants.UNDEFINED, e.getMessage());
                }
            }
        }
    }

    
    public void deleteAccessCode(final String accessCode) throws Exception {

    }

    
    public boolean verifyAccessCode(final String accessCode) throws Exception {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(MstarAccessCodeHelper.ACCESSCODE, accessCode));
        String reqParams = URLEncodedUtils.format(params, CommonConstants.CODING_UTF8);
        try {
            MStarIDSearchData mstartidSearchData = null;
            String mstarIDSStr = this.connManager.sendRequest(this.accesscodeVerification, reqParams, AdaptorUtil.convertNameValuePairListToString(params));
            mstartidSearchData = (MStarIDSearchData) this.mstarIDSJC.createUnmarshaller().unmarshal(new StringReader(mstarIDSStr));
            if (MstarAccessCodeHelper.SUCCESS_STATS_CODE.equals(mstartidSearchData.getStatus().getCode())) {
                return true;
            } else {
                // deleteAccessCode(accessCode);

                return false;
            }
        } catch (Exception e) {
            LogUtil.error(MstarAccessCodeHelper.class, "verify Access Code", e);
            String message = e.getMessage();
            if (null != message && message.toLowerCase().contains("timeout")) {
                throw new CommonException(MstarExceptionConstants.TIMEOUT, e.getMessage());
            } else {
                throw new CommonException(MstarExceptionConstants.UNDEFINED, e.getMessage());
            }
        }
    }

    
    public String getDecryptPassword() {
        return this.decryptPassword;
    }

    
    public void setDecryptPassword(final String decryptPassword) {
        this.decryptPassword = decryptPassword;
    }

    public synchronized void setAccesscode(final String accesscode) {
        this.accesscode = accesscode;
    }

    public String getAccesscode() {
        return this.accesscode;
    }
}
