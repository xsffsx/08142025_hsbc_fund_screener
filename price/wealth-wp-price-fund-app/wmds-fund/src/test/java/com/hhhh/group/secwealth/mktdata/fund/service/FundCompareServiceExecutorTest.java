package com.hhhh.group.secwealth.mktdata.fund.service;


import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundcompare.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundcompare.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundcompare.FundCompareData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundCompareResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.Silent.class)
public class FundCompareServiceExecutorTest {

    @InjectMocks
    @Spy
    FundCompareServiceExecutor fundCompareServiceExecutor;

    //String dataId = "dataId";
    String id = "externalKey";

    @Mock
    SiteFeature siteFeature;

    @Before
    public void init() throws Exception {
        Object[] sendResponse = new Object[2];
        FundCompareData fundCompareData = new FundCompareData();
        List<Data> datas = fundCompareData.getData();
        Data data = new Data();
        Api api = new Api();
        data.setApi(api);
        data.setId(id);
        datas.add(data);
        sendResponse[0] = fundCompareData;

        List<SearchProduct> list = new ArrayList<>();
        SearchProduct product = new SearchProduct();

        product.setExternalKey(id);
        String fundSrvCde = "fundSrvCde";
        SearchableObject searchableObject = new SearchableObject();
        searchableObject.setSymbol(fundSrvCde);
        product.setSearchObject(searchableObject);
        list.add(product);
        sendResponse[1] = list;
        doReturn(sendResponse).when(fundCompareServiceExecutor).sendFundCompareRequest(any(FundCompareRequest.class));
        doReturn("").when(siteFeature).getStringFeature(any(String.class), any(String.class));
    }

    @Test
    public void execute() throws Exception {
        FundCompareRequest request = new FundCompareRequest();
        String countryCode = "countryCode";
        String groupMember = "groupMember";
        String locale = "locale";
        request.addHeader(CommonConstants.REQUEST_HEADER_COUNTRYCODE, countryCode);
        request.addHeader(CommonConstants.REQUEST_HEADER_GROUPMEMBER, groupMember);

        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType(MstarConstants.PRODUCTTYPE_UT);
        productKeys.add(productKey);
        request.setProductKeys(productKeys);

        FundCompareResponse response = (FundCompareResponse) fundCompareServiceExecutor.execute(request);
        Assert.assertEquals(id, response.getProducts().get(0).getProdAltNumXCode());
        Assert.assertEquals(id, response.getSearchProductList().get(0).getExternalKey());
    }
}
