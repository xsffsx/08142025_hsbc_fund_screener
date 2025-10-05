
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.service.ServiceExecutor;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.PerformanceReturnRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.PerformanceReturnResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.performancereturn.PerformanceReturn;
import com.hhhh.group.secwealth.mktdata.fund.dao.PerformanceReturnDao;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;

@Service("performanceReturnService")
public class PerformanceReturnServiceImpl extends AbstractMstarService {

    @Autowired
    @Qualifier("performanceReturnServiceExecutor")
    private ServiceExecutor performanceReturnServiceExecutor;

    @Autowired
    @Qualifier("performanceReturnDao")
    private PerformanceReturnDao performanceReturnDao;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @Autowired
    @Qualifier("localeMappingUtil")
    protected LocaleMappingUtil localeMappingUtil;

    @Override
    public Object execute(final Object object) throws Exception {

        // Prepare Request
        PerformanceReturnRequest request = (PerformanceReturnRequest) object;
        // get data from Mstar API
        PerformanceReturnResponse performanceReturnResponse = (PerformanceReturnResponse) this.performanceReturnServiceExecutor
            .execute(request);
        // get product List from DB
        // List<Object[]> resultList =
        // this.performanceReturnDao.searchProductList(request);

        int index = this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT
            + request.getLocale());
        // merge Mstar and DB data
        // mergeResponseFromeDB(performanceReturnResponse,
        // request.getProductKeys(), resultList, index);

        return performanceReturnResponse;
    }

    private void mergeResponseFromeDB(final PerformanceReturnResponse mstarResponse, final List<ProductKey> productKeys,
        final List<Object[]> resultList, final int index) throws Exception {

        if (null != mstarResponse && ListUtil.isValid(resultList)) {
            List<PerformanceReturn> products = mstarResponse.getProducts();
            if (ListUtil.isValid(products)) {
                for (ProductKey productKey : productKeys) {
                    for (PerformanceReturn performance : products) {
                        for (Object[] result : resultList) {
                            if (null != result && productKey.getProdAltNum().equalsIgnoreCase(MiscUtil.safeString(result[3]))) {
                                // performance.setSymbol(MiscUtil.safeString(result[3]));
                                // if (0 == index) {
                                // performance.setName(MiscUtil.safeString(result[0]));
                                // } else if (1 == index) {
                                // performance.setName(MiscUtil.safeString(result[1]));
                                // } else if (2 == index) {
                                // performance.setName(MiscUtil.safeString(result[2]));
                                // } else {
                                // performance.setName(MiscUtil.safeString(result[0]));
                                // }
                            }
                        }
                    }
                }
            }
        }
    }
}