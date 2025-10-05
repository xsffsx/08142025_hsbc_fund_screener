package com.dummy.wmd.wpc.graphql.fetcher;

import com.google.common.collect.ImmutableMap;
import com.dummy.wealth.jwt.SupportedAlgorithm;
import com.dummy.wealth.jwt.bean.JwtHeader;
import com.dummy.wealth.jwt.bean.JwtPayload;
import com.dummy.wealth.jwt.bean.JwtToken;
import com.dummy.wealth.jwt.generator.JwtTokenGenerator;
import com.dummy.wealth.jwt.generator.signer.PrivateKeySigner;
import com.dummy.wmd.wpc.graphql.productConfig;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.productConstants;
import com.dummy.wmd.wpc.graphql.error.ErrorCode;
import com.dummy.wmd.wpc.graphql.error.Errors;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import graphql.GraphQLContext;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Resolve user info from JWT token
 */
@SuppressWarnings("java:S1168")
@Slf4j
@Component
public class UserInfoFetcher implements DataFetcher<Map<String, Object>> {

    @Autowired
    private productConfig productConfig;

    @Value("${product.token.key-alias:dummy smart plus ca}")
    private String keyAlias;

    @Value("${product.token.timeout:24h}")
    private Duration duration;

    private static final PrivateKeyHolder holder = new PrivateKeyHolder();

    @PostConstruct
    private void initializePrivateKey() {
        String keystorePath = System.getProperty("javax.net.ssl.trustStore");
        try {
            String keystorePassword = System.getProperty("javax.net.ssl.trustStorePassword");
            KeyStore ks = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(keystorePath)) {
                ks.load(fis, keystorePassword.toCharArray());
            }
            holder.privateKey = (PrivateKey) ks.getKey(keyAlias, keystorePassword.toCharArray());
        } catch (Exception e) {
            Errors.log(ErrorCode.OTPSERR_ZGQ002, String.format("Failed to load the smart plus private key from keystore: %s, cause: %s", keystorePath, e.getMessage()));
        }
    }

    @Override
    public Map<String, Object> get(DataFetchingEnvironment environment) {
        // ctryRecCde: String!, grpMembrRecCde: String!
        String ctryRecCde = environment.getArgument(Field.ctryRecCde);
        String grpMembrRecCde = environment.getArgument(Field.grpMembrRecCde);
        // The userInfo has been injected into the GraphQLContext in productExecutionInputCustomizer
        GraphQLContext context =  environment.getContext();
        UserInfo userInfo = context.get(productConstants.userInfo);

        if (null == userInfo) {
            return null;
        }
        List<String> roles = userInfo.getRoles();   // the roles can be set with testing purpose
        if (null == roles) {
            roles = productConfig.groupsToRoles(userInfo, ctryRecCde, grpMembrRecCde);
        }

        return new ImmutableMap.Builder<String, Object>()
                .put("id", userInfo.getId())
                .put("name", null == userInfo.getName() ? "" : userInfo.getName())
                .put("roles", roles)
                .put("sessionToken", generateToken(userInfo))
                .build();
    }

    private String generateToken(UserInfo userInfo) {
        if (null == holder.privateKey) {
            return "";
        }

        try {
            JwtHeader header = JwtHeader.builder()
                    .algorithm(SupportedAlgorithm.RS256)
                    .keyId("E2E_TRUST_SAAS_EU01_PPROD_ALIAS")
                    .type("JWS")
                    .schemaVersion("1.0")
                    .tokenVersion("1.0")
                    .build();
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            LocalDateTime now = LocalDateTime.now();
            JwtPayload payload = JwtPayload.builder()
                    .issuedAt(now.toEpochSecond(zoneOffset))
                    .expirationTime(now.plus(duration).toEpochSecond(zoneOffset))
                    .subjectId("employeeId")
                    .groups(userInfo.getGroups())
                    .subject(userInfo.getId())
                    .jwtId(UUID.randomUUID().toString())
                    .issuer("https://www.dummy.com/rbwm/dtp")
                    .build();
            JwtToken<JwtHeader, JwtPayload> jwtToken = JwtToken.<JwtHeader, JwtPayload>builder()
                    .payload(payload)
                    .header(header)
                    .build();
            return new JwtTokenGenerator(new PrivateKeySigner(holder.privateKey)).encodeAndSign(jwtToken);
        } catch (Exception e) {
            Errors.log(ErrorCode.OTPSERR_ZGQ002, String.format("Error generating the JWT token for user: %s, cause: %s", userInfo.getId(), e.getMessage()));
            return "";
        }
    }

    protected static class PrivateKeyHolder {
        private PrivateKey privateKey;
    }
}