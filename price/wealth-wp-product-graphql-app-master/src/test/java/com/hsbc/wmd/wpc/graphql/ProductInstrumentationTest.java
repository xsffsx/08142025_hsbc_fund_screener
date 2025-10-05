package com.dummy.wmd.wpc.graphql;

import com.dummy.wealth.jwt.SupportedAlgorithm;
import com.dummy.wealth.jwt.bean.JwtHeader;
import com.dummy.wealth.jwt.bean.JwtPayload;
import com.dummy.wealth.jwt.bean.JwtToken;
import com.dummy.wealth.jwt.generator.JwtTokenGenerator;
import com.dummy.wealth.jwt.generator.signer.PrivateKeySigner;
import com.dummy.wmd.wpc.graphql.constant.productConstants;
import graphql.ExecutionInput;
import graphql.GraphQLContext;
import graphql.execution.AbortExecutionException;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import io.micrometer.core.instrument.config.validate.Validated;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.UUID;

import static com.dummy.wmd.wpc.graphql.constant.Header.X_dummy_SMARTPLUS_TRUST_TOKEN_HEADER;

public class productInstrumentationTest {

    productInstrumentation instrumentation = new productInstrumentation();

    productDataLoaderRegistryFactory productDataLoaderRegistryFactory = new productDataLoaderRegistryFactory();

    PrivateKey privateKey;

    ExecutionInput executionInput;

    @Before
    public void before() throws NoSuchAlgorithmException {
        ReflectionTestUtils.setField(instrumentation, "registryFactory", productDataLoaderRegistryFactory);
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair pair = keyPairGen.generateKeyPair();
        privateKey = pair.getPrivate();

        executionInput = new ExecutionInput.Builder()
                .variables(new HashMap<>())
                .operationName("productByFilter")
                .query("query productByFilter($filter: JSON!) {\n" +
                        "  productByFilter(filter: $filter) {\n" +
                        "    prodId\n" +
                        "  }\n" +
                        "}\n")
                .build();
    }

    @Test
    public void testInstrumentExecutionInput_givenCalculatedFields() {
        InstrumentationExecutionParameters parameters = new InstrumentationExecutionParameters(executionInput, null, null);

        ExecutionInput transformedInput = instrumentation.instrumentExecutionInput(executionInput, parameters);

        Assertions.assertNotNull(transformedInput);
    }

    @Test
    public void testBeginExecution_ExpiredToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(X_dummy_SMARTPLUS_TRUST_TOKEN_HEADER, generateToken(-10L));
        ExecutionInput executionInputWithContext = executionInput.transform(
                builder -> builder.context(GraphQLContext.newContext().of(productConstants.webRequest, new ServletWebRequest(request)).build()));
        InstrumentationExecutionParameters parameters = new InstrumentationExecutionParameters(executionInputWithContext, null, null);
        Assertions.assertThrows(AbortExecutionException.class, () -> {
            instrumentation.beginExecution(parameters);
        });
    }

    @Test
    public void testBeginExecution_ValidToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(X_dummy_SMARTPLUS_TRUST_TOKEN_HEADER, generateToken(10L));
        ExecutionInput executionInputWithContext = executionInput.transform(
                builder -> builder.context(GraphQLContext.newContext().of(productConstants.webRequest, new ServletWebRequest(request)).build()));
        InstrumentationExecutionParameters parameters = new InstrumentationExecutionParameters(executionInputWithContext, null, null);
        Assertions.assertDoesNotThrow(() -> {
            instrumentation.beginExecution(parameters);
        });
    }

    @Test
    public void testBeginExecution_InvalidToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(X_dummy_SMARTPLUS_TRUST_TOKEN_HEADER, "InvalidToken");
        ExecutionInput executionInputWithContext = executionInput.transform(
                builder -> builder.context(GraphQLContext.newContext().of(productConstants.webRequest, new ServletWebRequest(request)).build()));
        InstrumentationExecutionParameters parameters = new InstrumentationExecutionParameters(executionInputWithContext, null, null);
              Assertions.assertDoesNotThrow(() -> {
            instrumentation.beginExecution(parameters);
        });
    }

    private String generateToken(Long expireMins) {
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
                .expirationTime(now.plusMinutes(expireMins).toEpochSecond(zoneOffset))
                .subjectId("employeeId")
                .subject("45244712")
                .jwtId(UUID.randomUUID().toString())
                .issuer("https://www.dummy.com/rbwm/dtp")
                .build();
        JwtToken<JwtHeader, JwtPayload> jwtToken = JwtToken.<JwtHeader, JwtPayload>builder()
                .payload(payload)
                .header(header)
                .build();
        return new JwtTokenGenerator(new PrivateKeySigner(privateKey)).encodeAndSign(jwtToken);
    }
}
