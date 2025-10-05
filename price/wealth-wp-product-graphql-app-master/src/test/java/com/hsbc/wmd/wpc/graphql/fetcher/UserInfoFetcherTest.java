package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.productConfig;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.productConstants;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserInfoFetcherTest {

    private UserInfoFetcher userInfoFetcher;

    @Mock
    private productConfig productConfig;

    @Mock
    private DataFetchingEnvironment environment;

    @Mock
    private GraphQLContext context;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userInfoFetcher = new UserInfoFetcher();
        ReflectionTestUtils.setField(userInfoFetcher, "productConfig", productConfig);
    }

    @Test
    public void testGet_WithValidUserInfo() throws Exception {
        // Mock arguments
        when(environment.getArgument(Field.ctryRecCde)).thenReturn("HK");
        when(environment.getArgument(Field.grpMembrRecCde)).thenReturn("123");

        // Mock context and userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setId("user123");
        userInfo.setName("John Doe");
        userInfo.setRoles(Arrays.asList("editor", "viewer", "approver"));
        when(environment.getContext()).thenReturn(context);
        when(context.get(productConstants.userInfo)).thenReturn(userInfo);

        ReflectionTestUtils.setField(userInfoFetcher, "keyAlias", "dummy smart plus ca");
        ReflectionTestUtils.setField(userInfoFetcher, "duration", Duration.ofDays(1));
        System.setProperty("javax.net.ssl.trustStore", new ClassPathResource("/product-input-schema.graphqls").getFile().getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair pair = keyPairGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        // Mock keystore and private key
        KeyStore keyStore = mock(KeyStore.class);
        when(keyStore.getKey("dummy smart plus ca", "password".toCharArray())).thenReturn(privateKey);

        try (MockedStatic<KeyStore> keyStoreMockedStatic = mockStatic(KeyStore.class)) {
            keyStoreMockedStatic.when(() -> KeyStore.getInstance("JKS")).thenReturn(keyStore);
            ReflectionTestUtils.invokeMethod(userInfoFetcher, "initializePrivateKey");
        }

        // Mock productConfig behavior
        when(productConfig.groupsToRoles(userInfo, "HK", "HBAP")).thenReturn(Arrays.asList("editor", "viewer", "approver"));

        // Call the method
        Map<String, Object> result = userInfoFetcher.get(environment);

        // Verify the result
        assertNotNull(result);
        assertEquals("user123", result.get("id"));
        assertEquals("John Doe", result.get("name"));
        assertEquals(Arrays.asList("editor", "viewer", "approver"), result.get("roles"));
        assertTrue(StringUtils.isNotBlank((CharSequence) result.get("sessionToken")));
    }

    @Test
    public void testGet_WithNullUserInfo() {
        // Mock context with null userInfo
        when(environment.getContext()).thenReturn(context);
        when(context.get(productConstants.userInfo)).thenReturn(null);

        // Call the method
        Map<String, Object> result = userInfoFetcher.get(environment);

        // Verify the result
        assertNull(result);
    }

}