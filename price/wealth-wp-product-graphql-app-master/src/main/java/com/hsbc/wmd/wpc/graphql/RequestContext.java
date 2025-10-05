package com.dummy.wmd.wpc.graphql;

import com.dummy.wmd.wpc.graphql.constant.RoleName;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import com.dummy.wmd.wpc.graphql.utils.AuthUtils;
import com.dummy.wmd.wpc.graphql.utils.CheckmarxUtils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RequestContext extends ConcurrentHashMap<String, Object> {
    public static final String COUNTRY_CODE = "country_code";
    public static final String GROUP_MEMBER = "group_member";
    public static final String CORRELATION_ID = "CORRELATION_ID";
    public static final String USER_INFO = "USER_INFO";

    protected static Class<? extends RequestContext> contextClass = RequestContext.class;

    protected static final ThreadLocal<? extends RequestContext> threadLocal = ThreadLocal.withInitial(() -> {
        try {
            return contextClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new productErrorException(productErrors.RuntimeException, e.getMessage());
        }
    });


    public RequestContext() {
        super();
    }

    /**
     * Get the current RequestContext
     *
     * @return the current RequestContext
     */
    public static RequestContext getCurrentContext() {
        return threadLocal.get();
    }

    public void setCountryCode(String countryCode){
        put(COUNTRY_CODE, countryCode);
    }

    public void setGroupMember(String groupMember){
        put(GROUP_MEMBER, groupMember);
    }

    public void setRequestCorrelationId(String correlationId){
        put(CORRELATION_ID, correlationId);
    }

    public String getCountryCode(){
        return (String) get(COUNTRY_CODE);
    }

    public String getGroupMember(){
        return (String) get(GROUP_MEMBER);
    }

    public String getRequestCorrelationId(){
        return (String) get(CORRELATION_ID);
    }

    public void setUserInfo(UserInfo userInfo) {
        put(USER_INFO, userInfo);
    }

    public UserInfo getUserInfo() {
        return (UserInfo) get(USER_INFO);
    }

    public String getUserId(){
        UserInfo userInfo = getUserInfo();
        if(null != userInfo) {
            return CheckmarxUtils.preventCGIReflectedXSSAllClients(userInfo.getId());
        }
        // in case can't get the staff id from JWT
        return "";
    }

    /**
     * Check if the current user has a given role, raise an productErrorException in case not
     *
     * @param roleName
     */
    public void assertUserHasRole(RoleName roleName) {
        UserInfo userInfo = getUserInfo();
        if(null == userInfo) {  // authorization only apply to WPS request, which can retrieve UserInfo from the JWT token.
            return;
        }

        String ctryRecCde = getCountryCode();
        String grpMembrRecCde = getGroupMember();
        if(!AuthUtils.hasRole(userInfo, ctryRecCde, grpMembrRecCde, roleName)) {
            String message = String.format("User %s has no role '%s' in entity %s%s to perform the operation", userInfo.getId(), roleName, ctryRecCde, grpMembrRecCde);
            throw new productErrorException(productErrors.AuthorizationError, message);
        }
    }
}
