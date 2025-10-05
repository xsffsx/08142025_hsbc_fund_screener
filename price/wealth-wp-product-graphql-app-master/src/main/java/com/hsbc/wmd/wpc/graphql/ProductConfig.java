package com.dummy.wmd.wpc.graphql;

import com.dummy.wmd.wpc.graphql.model.UnderlyingConfig;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import com.dummy.wmd.wpc.graphql.service.TestUserService;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "product")
public class productConfig {
    // role --> group list
    private List<Map<String, String>> supportEntities;
    private Map<String, List<String>> roleMapping;
    private boolean testingEnabled;

    private List<UnderlyingConfig> underlying = Collections.emptyList();

    @Autowired
    private TestUserService testUserService;

    public void setRoleMapping(Map<String, List<String>> roleMapping) {
        this.roleMapping = roleMapping;
    }

    public void setTestingEnabled(boolean testingEnabled) {
        this.testingEnabled = testingEnabled;
    }

    public void setTestUserService(TestUserService testUserService) {
        this.testUserService = testUserService;
    }

    /**
     * Map a group to role, base on the role mapping config.
     * a group name looks like this: CN=BR_GB_HBEU_ENQ,OU=Bussiness_Roles,OU=HTSE,OU=EU,OU=Roles,OU=Groups,DC=InfoDir,DC=Prod,DC=dummy
     * Since a user may have different role in different entity, so we need to further check the entity info, make sure only reply correct roles for the user.
     * @param userInfo
     * @param ctryRecCde
     * @param grpMembrRecCde
     * @return
     */
    public List<String> groupsToRoles(UserInfo userInfo, String ctryRecCde, String grpMembrRecCde){
        if(testingEnabled) {
            // test user roles setting in the test_user collection
            List<String> testRoles = testUserService.getRoles(userInfo.getId());
            if (null != testRoles) {
                log.info("Testing roles from DB: {} - {}", userInfo.getId(), testRoles);
                return testRoles;
            }
        }

        String entity = String.format("%s_%s", ctryRecCde, grpMembrRecCde);
        Set<String> configRoles = new LinkedHashSet<>();

        if(testingEnabled) {
            // for test users, the group name expected to be the staff id
            roleMapping.forEach((role, groupList) -> {
                if (groupList.contains("any")   // this role assign to any staff
                        || groupList.contains(userInfo.getId())) { // exactly match staff id
                    configRoles.add(role);
                }
            });
            if(!configRoles.isEmpty()) {
                log.info("Testing roles from config: {} - {}", userInfo.getId(), configRoles);
            }
        }

        List<String> roles = getLdapRoles(userInfo, entity);
        roles.addAll(configRoles);
        return roles;
    }

    private List<String> getLdapRoles(UserInfo userInfo, String entity) {
        Set<String> ldapRoles = new LinkedHashSet<>();
        if (userInfo.getGroups() != null) {
            userInfo.getGroups().forEach(group -> {
                if(StringUtils.containsIgnoreCase(group, entity)) {    // only check group name contains the entity info, eg CN=BR_GB_HBEU_ENQ
                    int idx = group.indexOf(',');
                    String cn = group.substring(0, idx).toUpperCase();
                    roleMapping.forEach((role, groupList) -> {
                        // match group name with the CN part only, ignore case
                        boolean hasMatch = groupList.stream().anyMatch(g -> StringUtils.startsWithIgnoreCase(g, cn));
                        if (hasMatch) {
                            ldapRoles.add(role);
                        }
                    });
                }
            });
            log.info("Roles from LDAP groups: {} - {}", userInfo.getId(), ldapRoles);
        } else {
            log.error("User group is empty: {}", userInfo.getId());
        }

        return new ArrayList<>(ldapRoles);
    }
}
