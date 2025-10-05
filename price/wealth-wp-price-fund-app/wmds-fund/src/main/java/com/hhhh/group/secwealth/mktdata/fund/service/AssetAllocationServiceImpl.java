package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.assetallocation.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.assetallocation.AssetAllocationData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.assetallocation.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.AssetAllocationRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.AssetAllocationResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.assetallocation.AssetAllocation;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;

@Service("assetAllocationService")
public class AssetAllocationServiceImpl extends AbstractMstarService {

    @Value("#{systemConfig['mstar.conn.url.assetallocation']}")
    private String url;

    @Value("#{systemConfig['assetallocation.dataClass']}")
    private String dataClass;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    private JAXBContext dataClassJC;

    @PostConstruct
    public void init() throws Exception {
        try {
            this.dataClassJC = JAXBContext.newInstance(Class.forName(this.dataClass));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Object execute(final Object object) throws Exception {

        // Prepare Request
        AssetAllocationRequest request = (AssetAllocationRequest) object;

        // Invoke Service
        AssetAllocationData assetData = (AssetAllocationData) sendRequest(request);

        // Set Response
        AssetAllocationResponse response = new AssetAllocationResponse();
        List<AssetAllocation> assetAllocation = new ArrayList<AssetAllocation>();

        // Set value from api
        if (null != assetData) {
            Data data = assetData.getData();
            if (null != data) {
                Api api = data.getApi();
                if (null != api) {
                    AssetAllocation assetStock = new AssetAllocation();
                    assetStock.setAssetClass("Stock");
                    assetStock.setAssetWeight(BigDecimalUtil.fromStringAndCheckNull(api.getAssetAllocEquityNet()));

                    AssetAllocation assetBond = new AssetAllocation();
                    assetBond.setAssetClass("Bond");
                    assetBond.setAssetWeight(BigDecimalUtil.fromStringAndCheckNull(api.getAssetAllocBondNet()));

                    AssetAllocation assetPreferred = new AssetAllocation();
                    assetPreferred.setAssetClass("Preferred");
                    assetPreferred.setAssetWeight(BigDecimalUtil.fromStringAndCheckNull(api.getPreferredStockNet()));

                    AssetAllocation assetCash = new AssetAllocation();
                    assetCash.setAssetClass("Cash");
                    assetCash.setAssetWeight(BigDecimalUtil.fromStringAndCheckNull(api.getAssetAllocCashNet()));

                    AssetAllocation assetOther = new AssetAllocation();
                    assetOther.setAssetClass("Other");
                    assetOther.setAssetWeight(BigDecimalUtil.fromStringAndCheckNull(api.getOtherNet()));

                    assetAllocation.add(assetStock);
                    assetAllocation.add(assetBond);
                    assetAllocation.add(assetPreferred);
                    assetAllocation.add(assetCash);
                    assetAllocation.add(assetOther);
                    response.setAssetAllocations(assetAllocation);

                    response.setPortfolioDate(api.getPortfolioDate());
                } else {
                    LogUtil.errorBeanToJson(AssetAllocationServiceImpl.class,
                        "Can not get record from morningstar, No record found for AssetAllocationl|ipkList=", request);
                }
            } else {
                LogUtil.errorBeanToJson(AssetAllocationServiceImpl.class,
                    "Can not get record from morningstar, No record found for AssetAllocationl|ipkList=", request);
            }
        } else {
            LogUtil.errorBeanToJson(AssetAllocationServiceImpl.class,
                "Can not get record from morningstar, No record found for AssetAllocationl|request=", request);
        }
        return response;
    }

    protected Object sendRequest(final AssetAllocationRequest request) throws Exception {
        String altClassCde = request.getProdCdeAltClassCde();
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String prodAltNum = request.getProdAltNum();
        String countryTradableCode = request.getMarket();
        String productType = request.getProductType();
        String locale = request.getLocale();

        SearchProduct searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde(altClassCde, countryCode,
            groupMember, prodAltNum, countryTradableCode, productType, locale);
        if (null == searchProduct || StringUtil.isInvalid(searchProduct.getExternalKey())) {
            String msg = "No record found for Mstar|ProdAltNum=" + prodAltNum;
            LogUtil.error(AbstractMstarService.class, msg);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND, msg);
        }
        String id = searchProduct.getExternalKey();
        return super.sendRequest(MstarConstants.MSTARID, id, this.url, this.dataClassJC.createUnmarshaller());
    }
}
