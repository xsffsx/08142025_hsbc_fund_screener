package com.dummy.wmd.wpc.graphql;

import com.dummy.ca.aim.security.token.InvalidTokenException;
import com.dummy.ca.aim.security.token.SignedToken;
import com.dummy.ca.aim.security.token.parser.JwtTokenParser;
import com.dummy.wmd.wpc.graphql.constant.Header;
import com.dummy.wmd.wpc.graphql.error.ErrorCode;
import com.dummy.wmd.wpc.graphql.constant.productConstants;
import com.dummy.wmd.wpc.graphql.error.Errors;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import com.dummy.wmd.wpc.graphql.service.LdapService;

import com.dummy.wmd.wpc.graphql.utils.ObjectMapperUtils;
import graphql.ExecutionInput;
import graphql.GraphQLContext;
import graphql.spring.web.servlet.ExecutionInputCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import javax.naming.NamingException;

@Slf4j
@Component("executionInputCustomizer")
public class productExecutionInputCustomizer implements ExecutionInputCustomizer {
    @Autowired
    private LdapService ldapService;
    @Autowired
    private productConfig productConfig;

    @Override
    public CompletableFuture<ExecutionInput> customizeExecutionInput(ExecutionInput executionInput, WebRequest webRequest) {
        GraphQLContext graphQLContext = (GraphQLContext) executionInput.getContext();
        graphQLContext.put(productConstants.webRequest, webRequest);

        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.clear();    // reset the possible states left in the last call for the same thread

        UserInfo userInfo = getUserInfo(webRequest);
        if(null != userInfo) {
            graphQLContext.put(productConstants.userInfo, userInfo);
            ctx.setUserInfo(userInfo);
        }

        String ctryRecCde = webRequest.getHeader(Header.X_dummy_USER_COUNTRYCODE);
        if(null != ctryRecCde) {
            ctx.setCountryCode(ctryRecCde);
        }

        String grpMembrRecCde = webRequest.getHeader(Header.X_dummy_USER_GROUP_MEMBER);
        if(null != grpMembrRecCde) {
            ctx.setGroupMember(grpMembrRecCde);
        }

        String correlationId = webRequest.getHeader(Header.X_dummy_Request_Correlation_Id);
        if(null != correlationId) {
            ctx.setRequestCorrelationId(correlationId);
        }

        return CompletableFuture.completedFuture(executionInput);
    }

    private UserInfo getUserInfo(WebRequest webRequest) {
        // to support run as user http header, the value expected to be a json
        if (productConfig.isTestingEnabled()) {
            String runAsUser = webRequest.getHeader(Header.X_dummy_RUN_AS_USER);
            if (StringUtils.hasText(runAsUser)) {
                log.info("run as user: {}", runAsUser);
                return ObjectMapperUtils.readValue(runAsUser, UserInfo.class);
            }
        }

        String token = webRequest.getHeader(Header.X_dummy_E2E_TRUST_TOKEN_HEADER);
        if (!StringUtils.hasText(token)) {
            return null;
        }

        SignedToken jwtToken;
        try {
            JwtTokenParser jwtTokenParser = new JwtTokenParser();
            jwtToken = jwtTokenParser.parse(token);
            String staffId = jwtToken.getSubject().getSubject();
            UserInfo userInfo = getUserInfoById(staffId);
            if (userInfo != null && CollectionUtils.isNotEmpty(jwtToken.getGroups())) {
                userInfo.setGroups(jwtToken.getGroups());
            }
            return userInfo;
        } catch (InvalidTokenException e){
            Errors.log(ErrorCode.OTPSERR_ZGQ002, String.format("Error parsing the JWT token, message = %s, token = %s", e.getMessage(), token));
            return null;
        }
    }

    /**
     * Get user info by Staff ID from LDAP
     * @param staffId
     * @return
     */
    private UserInfo getUserInfoById(String staffId) {
        UserInfo userInfo;
        try {
            userInfo = ldapService.getLdapUserById(staffId);
        } catch (NamingException e) {
            Errors.log(ErrorCode.OTPSERR_ZGQ001, "Error getting LDAP user info, message = " + e.getMessage());
            userInfo = new UserInfo(staffId, null, null);
        }
        return userInfo;
    }
}
