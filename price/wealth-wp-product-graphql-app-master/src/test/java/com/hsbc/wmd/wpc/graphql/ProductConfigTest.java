package com.dummy.wmd.wpc.graphql;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class productConfigTest {
    private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory()).disable(JsonGenerator.Feature.IGNORE_UNKNOWN);

    @Test
    public void testRoleMapping() throws IOException {
        URL url = productConfigTest.class.getResource("/application.yml");
        Map<String, Object> appConfig = yamlMapper.readValue(url, Map.class);
        Map<String, List<String>> roleMapping = JsonPath.read(appConfig, "$.product.role-mapping");
        productConfig config = new productConfig();
        config.setTestingEnabled(false);
        config.setRoleMapping(roleMapping);

        List<String> groups = Arrays.asList(
                "CN=BR_GR_HBGR_ENQ,OU=BUSINESS_ROLES,OU=HBGR,OU=GR,OU=ROLES,OU=GROUPS,DC=INFODIR,DC=PROD,DC=dummy",
                "CN=BR_GB_HBEU_ENQ,OU=BUSSINESS_ROLES,OU=HTSE,OU=EU,OU=ROLES,OU=GROUPS,DC=INFODIR,DC=PROD,DC=dummy",
                "CN=BR_GB_HBEU_ENQ,OU=BUSSINESS_ROLES,OU=HTSE,OU=EU,OU=ROLES,OU=GROUPS,DC=INFODIR,DC=Prod,DC=dummy",
                "CN=BR_GB_HBEU_MNT,OU=BUSINESS_ROLES,OU=HTSE,OU=EU,OU=ROLES,OU=GROUPS,DC=INFODIR,DC=PROD,DC=dummy",
                "CN=BR_GB_HBEU_App,OU=BUSINESS_ROLES,OU=HTSE,OU=EU,OU=ROLES,OU=GROUPS,DC=INFODIR,DC=PROD,DC=dummy"
        );
        UserInfo user = new UserInfo("11111111", "Test User", groups);
        List<String> roles = config.groupsToRoles(user, "GB", "HBEU");
        assertEquals(3, roles.size());
        assertTrue(roles.contains("viewer"));
        assertTrue(roles.contains("approver"));
        assertTrue(roles.contains("editor"));
    }
}
