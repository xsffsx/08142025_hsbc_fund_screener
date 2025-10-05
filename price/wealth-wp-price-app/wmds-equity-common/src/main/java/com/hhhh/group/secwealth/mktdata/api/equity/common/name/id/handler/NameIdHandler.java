package com.hhhh.group.secwealth.mktdata.api.equity.common.name.id.handler;

/*
 * 1. saml token's customer id 2. channle id --> staff , customer OHI->staff OHB->customer 3. if nothing , reject staff and customer
 * must get nameId by header key, and check 4. if staff & customer both have value and same values reject 5. Before saving to DB or
 * passing to vendor, please make sure we encrypt the ID
 *
 */


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.component.encryptor.EncryptedResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

import com.google.gson.reflect.TypeToken;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "name.id")
@Getter
@Setter
public class NameIdHandler {

    private static final Logger logger = LoggerFactory.getLogger(NameIdHandler.class);

    /**
     * The flag means whether fetching user id in saml taken.
     */
    private static final String PROCESSING_FLAG_SKIP_SAML_USER = "SKIP";

    public List<String> staffChannel;

    public List<String> customerChannel;

    @Autowired
    private HttpClientHelper httpClientHelper;

    private boolean enable;

    private boolean disableEncryptor;

    private String encryptorUrl;

    private String decryptorUrl;


    /**
     * <p>
     * <b> Check and encrypt staff id or customer id. </b>
     * </p>
     *
     * @param header
     * @param samlNameId
     * @param processingFlag:
     *            skip: do not extract nameid from smal token
     */
    public void checkAndEncrypt(final CommonRequestHeader header, final String samlNameId, final String processingFlag) {
        if (!this.enable) {
            return;
        }

        String nameId = validateAndExtractNameId(header, samlNameId, processingFlag);
        if (this.disableEncryptor) {
            return;
        }

        EncryptedResponse encryptedResponse = getEncryptedCustomerId(header, nameId);
        String encryptedCustomerId = "";
        if (null != encryptedResponse && "0".equals(encryptedResponse.getStatus())) {
            encryptedCustomerId = encryptedResponse.getEncryptedId();
        } else {
            NameIdHandler.logger.error("Encrypted Customer Id fail, encryptedResponse" + encryptedResponse.toString());
            throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ENCRYPTOR_ERROR);
        }
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RAW_NAME_ID, nameId);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_ENCRYPTED_CUSTOMER_ID, encryptedCustomerId);
    }

    /**
     *
     * <p>
     * <b> Check and Extract name id, without encrypting. </b>
     * </p>
     *
     * @param header
     * @param samlNameId
     * @param processingFlag
     */
    public void checkAndNotEncrypt(final CommonRequestHeader header, final String samlNameId, final String processingFlag) {
        if (!this.enable) {
            return;
        }

        String nameId = validateAndExtractNameId(header, samlNameId, processingFlag);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RAW_NAME_ID, nameId);
    }

    private String validateAndExtractNameId(final CommonRequestHeader header, final String samlNameId,
        final String processingFlag) {
        String nameId = "";
        // If processingFlag != SKIP,samlName is valid, use saml user id at first, then fetch name id in request header.
        if (!NameIdHandler.PROCESSING_FLAG_SKIP_SAML_USER.equalsIgnoreCase(processingFlag) && StringUtil.isValid(samlNameId)) {
            nameId = samlNameId;
        } else {
            String staffNameId = header.getStaffId();
            String customerNameId = header.getCustomerId();

            if (StringUtil.isInValid(staffNameId) && StringUtil.isInValid(customerNameId)) {
                NameIdHandler.logger.error("staff name id and customer name id both are null...reject");
                throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
            }
            if (StringUtil.isValid(staffNameId) && StringUtil.isValid(customerNameId) && staffNameId.equals(customerNameId)) {
                NameIdHandler.logger.error("Value of staff name id same with customer name id...reject");
                throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
            }

            String channelId = header.getChannelId();
            if (!this.staffChannel.isEmpty() && this.staffChannel.contains(channelId)) {
                nameId = staffNameId;
            } else if (!this.customerChannel.isEmpty() && this.customerChannel.contains(channelId)) {
                nameId = customerNameId;
            } else {
                NameIdHandler.logger.error(" channelId regx validate fail");
                throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
            }
        }
        if (StringUtil.isInValid(nameId)) {
            NameIdHandler.logger.error("name id can not be null...reject");
            throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
        }
        return nameId;
    }

    public EncryptedResponse getEncryptedCustomerId(final CommonRequestHeader header, final String nameId) {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Constant.REQUEST_HEADER_KEY_COUNTRYCODE, header.getCountryCode());
        headers.put(Constant.REQUEST_HEADER_KEY_GROUP_MEMBER, header.getGroupMember());
        headers.put(Constant.REQUEST_HEADER_KEY_CUSTOMER_ID, nameId);
        String params = "";
        String response = "";
        try {
            response = this.httpClientHelper.doGet(this.encryptorUrl, params, headers);
        } catch (Exception e) {
            NameIdHandler.logger.error("Encrypted Customer Id fail, error is " + e.getMessage());
            throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_ENCRYPTOR_ERROR, e);
        }

        return JsonUtil.fromJson(response, new TypeToken<EncryptedResponse>() {}.getType());
    }


}
