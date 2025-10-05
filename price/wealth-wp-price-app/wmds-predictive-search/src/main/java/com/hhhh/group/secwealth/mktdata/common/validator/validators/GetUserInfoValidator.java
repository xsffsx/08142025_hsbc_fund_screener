/*
 */

package com.hhhh.group.secwealth.mktdata.common.validator.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.RuleSet;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

import net.sf.json.JSONObject;

/**
 *
 * <p>
 * <b> The validator for Get User Info checking. </b>
 * </p>
 */
public class GetUserInfoValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = 7343702253897589128L;

    private String[] userinfo;

    private boolean checkInvalid = false;

    @Override
    public void preValidate(final Object obj) throws Exception {
        this.userinfo = ((String) obj).split(CommonConstants.SYMBOL_COMMA);
        try {
            this.checkInvalid = Boolean.parseBoolean(this.userinfo[0]);
        } catch (Exception e) {
            LogUtil.error(GetUserInfoValidator.class, "Boolean.parseBoolean error, value: " + this.userinfo[0], e);
        }
    }

    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {
        String channelId = (String) headerMap.get(CommonConstants.REQUEST_HEADER_CHANNELID);
        String sb =
            new StringBuilder(CommonConstants.SYMBOL_VERTICAL).append(channelId).append(CommonConstants.SYMBOL_VERTICAL).toString();
        String eid = null;
        for (String info : this.userinfo) {
            if (info.contains(sb)) {
                json.put(CommonConstants.USERTYPE, info.substring(info.lastIndexOf(CommonConstants.SYMBOL_VERTICAL) + 1));
                eid = (String) headerMap.get(info.substring(0, info.indexOf(CommonConstants.SYMBOL_VERTICAL)));
                json.put(CommonConstants.EID, eid);
                break;
            }
        }
        List<ValidatorError> validatorList = new ArrayList<ValidatorError>();
        if (this.checkInvalid) {
            if (StringUtil.isInvalid(eid)) {
                validatorList.add(new ValidatorError(ErrTypeConstants.GET_USERINFO_INVALID, channelId, "The user id is invalid",
                    this.getClass().getSimpleName()));
            }
        }
        return validatorList;
    }

    /**
     * @return the userinfo
     */
    public String[] getUserinfo() {
        return this.userinfo;
    }

    /**
     * @param userinfo
     *            the userinfo to set
     */
    public void setUserinfo(final String[] userinfo) {
        this.userinfo = userinfo;
    }

    /**
     * @return the checkInvalid
     */
    public boolean isCheckInvalid() {
        return this.checkInvalid;
    }

    /**
     * @param checkInvalid
     *            the checkInvalid to set
     */
    public void setCheckInvalid(final boolean checkInvalid) {
        this.checkInvalid = checkInvalid;
    }

}
