package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.productConfig;
import com.dummy.wmd.wpc.graphql.constant.RoleName;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AuthUtilsTest {

    @Mock
    private productConfig productConfig;
    @InjectMocks
    private AuthUtils authUtils;

    @Before
    public void setUp() {
        authUtils = new AuthUtils(productConfig);
    }

    @Test
    public void testHasRole_givenUserInfoAndArgs_returnsFalse() {
        // Setup
        UserInfo userInfo = new UserInfo("id", "name", Arrays.asList("value"));
        userInfo.setRoles(Arrays.asList("admin", "user"));
        // Run the test
        boolean result = authUtils.hasRole(userInfo, "HK", "HASE", RoleName.viewer);
        // Verify the results
        assertFalse(result);
    }

    @Test
    public void testHasRole_givenUserInfoAndArgs_returnsTrue() {
        // Setup
        UserInfo userInfo = new UserInfo("id", "name", Arrays.asList("value"));
        Mockito.when(productConfig.groupsToRoles(userInfo, "HK", "HASE")).thenReturn(Arrays.asList("viewer", "approver", "editor"));
        // Run the test
        boolean result = authUtils.hasRole(userInfo, "HK", "HASE", RoleName.viewer);
        // Verify the results
        assertTrue(result);
    }
}
