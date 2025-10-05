package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.productConfig;
import com.dummy.wmd.wpc.graphql.constant.RoleName;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@SuppressWarnings({"java:S1118", "java:S3010"})
@Component
public class AuthUtils {
    private static productConfig productConfig;

    public AuthUtils(productConfig config) {
        productConfig = config;
    }

    public static boolean hasRole(UserInfo userInfo, String ctryRecCde, String grpMembrRecCde, RoleName role) {
        List<String> roles = userInfo.getRoles();
        // if testing=true, the roles has been set, otherwise need to calculate the roles from adam groups
        if(CollectionUtils.isEmpty(roles) && CollectionUtils.isNotEmpty(userInfo.getGroups())) {
            roles = productConfig.groupsToRoles(userInfo, ctryRecCde, grpMembrRecCde);
        }
        return roles.contains(role.toString());
    }
}
