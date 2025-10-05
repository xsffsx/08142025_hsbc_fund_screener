
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.dao.impl.CustomerContextHolder;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.service.AbstractService;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.service.ProductKeyListWithEligibilityCheckService;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckImplDelegate;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckResponse;


@Service("wpcWebServiceDashboard")
public class WpcWebServiceDashboard extends AbstractService {

    @Value("#{systemConfig['app.supportSites']}")
    private String[] supportSites;

    @Autowired
    @Qualifier("productKeyListWithEligibilityCheckService")
    ProductKeyListWithEligibilityCheckService productKeyListWithEligibilityCheckService;

    @Resource(name = "productKeyListWithEligibilityCheckImplDelegate")
    private ProductKeyListWithEligibilityCheckImplDelegate productKeyListWithEligibilityCheckImplDelegate;

    // for Health check DashBoard
    public ProductKeyListWithEligibilityCheckResponse execute(final Object object) throws Exception {

        Map<String, String> headerMap = (Map<String, String>) object;
        CustomerContextHolder.setHeaderMap(headerMap);
        // ProductKeyListWithEligibilityCheckRequest request = new
        // ProductKeyListWithEligibilityCheckRequest();
        // ProductKeyListWithEligibilityCheckResponse response =
        // this.productKeyListWithEligibilityCheckImplDelegate.enquire(request);
        String[] site = this.getCountryCodeAndGroupMember();
        ProductKeyListWithEligibilityCheckResponse response = this.productKeyListWithEligibilityCheckService
            .getProductKeyListWithEligibilityCheck("en_US", site[0], site[1], "UT", null, "quickview.newfunds", null);
        return response;
    }

    private String[] getCountryCodeAndGroupMember() throws Exception {
        String[] str = null;
        if (this.supportSites != null && this.supportSites.length > 0) {
            for (String site : this.supportSites) {
                if (site != null && !CommonConstants.DEFAULT.equals(site)) {
                    int index = site.indexOf(CommonConstants.SYMBOL_UNDERLINE);
                    if (index > -1) {
                        str = new String[2];
                        str[0] = site.substring(0, index);
                        str[1] = site.substring(index + 1);
                        break;
                    }
                }
            }
        }

        if (str == null || StringUtil.isInvalid(str[0]) || StringUtil.isInvalid(str[1])) {
            LogUtil.errorBeanToJson(WpcWebServiceDashboard.class, "WPC Webservice health check error, supportSites: ",
                this.supportSites);
            throw new CommonException(ErrTypeConstants.SITE_NOT_SUPPORT);
        }

        return str;
    }
}
