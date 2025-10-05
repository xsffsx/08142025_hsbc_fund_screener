
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMstarService;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees.Api;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees.DistributionFee;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees.FeeSchedule;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees.FrontLoad;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees.ManagementFee;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees.RedemptionFee;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees.RedemptionFeesData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.RedemptionFeesRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.RedemptionFeesResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees.RedemptionFeesDistributionFee;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees.RedemptionFeesFrontLoad;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees.RedemptionFeesItem;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees.RedemptionFeesManagementFee;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees.RedemptionFeesProspectusCustodianFee;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;

@Service("redemptionFeesService")
public class RedemptionFeesServiceImpl extends AbstractMstarService {

    @Value("#{systemConfig['mstar.conn.url.redemptionfees']}")
    private String url;

    @Value("#{systemConfig['redemptionfees.dataClass']}")
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
        // Prepare request
        RedemptionFeesRequest request = (RedemptionFeesRequest) object;
        // Invoke Service
        RedemptionFeesData redemptionFeesData = (RedemptionFeesData) sendRequest(request);
        RedemptionFeesResponse response = converterResponse(redemptionFeesData);
        return response;
    }

    private Object sendRequest(final RedemptionFeesRequest request) throws Exception {
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
        // Prepare request url & Params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (StringUtil.isValid(this.getCurrencyAlignKey())) {
            params.add(new BasicNameValuePair(AbstractMstarService.CURRENCY, this.getCurrencyAlignKey()));
        }
        String reqUrl = new StringBuilder(this.url).append(CommonConstants.SYMBOL_SLASH).append(MstarConstants.MSTARID)
            .append(CommonConstants.SYMBOL_SLASH).append(id).toString();

        return super.sendRequest(reqUrl, params, this.dataClassJC.createUnmarshaller());
    }

    private RedemptionFeesResponse converterResponse(final RedemptionFeesData data) {
        RedemptionFeesResponse response = new RedemptionFeesResponse();
        List<RedemptionFeesFrontLoad> frontLoads = new ArrayList<RedemptionFeesFrontLoad>();
        List<RedemptionFeesItem> redemptionFees = new ArrayList<RedemptionFeesItem>();
        List<RedemptionFeesManagementFee> managementFees = new ArrayList<RedemptionFeesManagementFee>();
        List<RedemptionFeesDistributionFee> distributionFees = new ArrayList<RedemptionFeesDistributionFee>();
        List<RedemptionFeesProspectusCustodianFee> prospectusCustodianFees = new ArrayList<RedemptionFeesProspectusCustodianFee>();

        if (null != data && null != data.getData() && null != data.getData().getApi()) {
            Api api = data.getData().getApi();
            if (null != api.getLsFrontLoads()) {
                List<FrontLoad> apiFrontLoads = api.getLsFrontLoads().getFrontLoad();
                if (ListUtil.isValid(apiFrontLoads)) {
                    for (FrontLoad fl : apiFrontLoads) {
                        RedemptionFeesFrontLoad rfFrontLoad = new RedemptionFeesFrontLoad();
                        rfFrontLoad.setUnit(fl.getUnit());
                        rfFrontLoad.setBreakpointUnit(fl.getBreakpointUnit());
                        rfFrontLoad.setLowBreakpoint(BigDecimalUtil.fromStringAndCheckNull(fl.getLowBreakpoint()));
                        rfFrontLoad.setHighBreakpoint(BigDecimalUtil.fromStringAndCheckNull(fl.getHighBreakpoint()));
                        rfFrontLoad.setValue(BigDecimalUtil.fromStringAndCheckNull(fl.getValue()));
                        frontLoads.add(rfFrontLoad);
                    }
                }
            }

            if (null != api.getLsRedemptionFees()) {
                List<RedemptionFee> apiRedemptionFees = api.getLsRedemptionFees().getRedemptionFee();
                if (ListUtil.isValid(apiRedemptionFees)) {
                    for (RedemptionFee rf : apiRedemptionFees) {
                        RedemptionFeesItem rfItem = new RedemptionFeesItem();
                        rfItem.setUnit(rf.getUnit());
                        rfItem.setBreakpointUnit(rf.getBreakpointUnit());
                        rfItem.setLowBreakpoint(BigDecimalUtil.fromStringAndCheckNull(rf.getLowBreakpoint()));
                        rfItem.setHighBreakpoint(BigDecimalUtil.fromStringAndCheckNull(rf.getHighBreakpoint()));
                        rfItem.setValue(BigDecimalUtil.fromStringAndCheckNull(rf.getValue()));
                        redemptionFees.add(rfItem);
                    }
                }
            }

            if (null != api.getLsManagementFees()) {
                List<ManagementFee> apiManagementFees = api.getLsManagementFees().getManagementFee();
                if (ListUtil.isValid(apiManagementFees)) {
                    for (ManagementFee mf : apiManagementFees) {
                        RedemptionFeesManagementFee rfmf = new RedemptionFeesManagementFee();
                        rfmf.setUnit(mf.getUnit());
                        rfmf.setBreakpointUnit(mf.getBreakpointUnit());
                        rfmf.setLowBreakpoint(BigDecimalUtil.fromStringAndCheckNull(mf.getLowBreakpoint()));
                        rfmf.setValue(BigDecimalUtil.fromStringAndCheckNull(mf.getValue()));
                        managementFees.add(rfmf);
                    }
                }
            }

            if (null != api.getLsDistributionFees()) {
                List<DistributionFee> apiDistributionFees = api.getLsDistributionFees().getDistributionFee();
                if (ListUtil.isValid(apiDistributionFees)) {
                    for (DistributionFee df : apiDistributionFees) {
                        RedemptionFeesDistributionFee rfdf = new RedemptionFeesDistributionFee();
                        rfdf.setUnit(df.getUnit());
                        rfdf.setBreakpointUnit(df.getBreakpointUnit());
                        rfdf.setLowBreakpoint(BigDecimalUtil.fromStringAndCheckNull(df.getLowBreakpoint()));
                        rfdf.setHighBreakpoint(BigDecimalUtil.fromStringAndCheckNull(df.getHighBreakpoint()));
                        rfdf.setValue(BigDecimalUtil.fromStringAndCheckNull(df.getValue()));
                        distributionFees.add(rfdf);
                    }
                }
            }

            if (null != api.getLsProspectusCustodianFee()) {
                List<FeeSchedule> apiFeeSchedules = api.getLsProspectusCustodianFee().getFeeSchedule();
                if (ListUtil.isValid(apiFeeSchedules)) {
                    for (FeeSchedule fs : apiFeeSchedules) {
                        RedemptionFeesProspectusCustodianFee rfpc = new RedemptionFeesProspectusCustodianFee();
                        rfpc.setUnit(fs.getUnit());
                        rfpc.setBreakpointUnit(fs.getBreakpointUnit());
                        rfpc.setLowBreakpoint(BigDecimalUtil.fromStringAndCheckNull(fs.getLowBreakpoint()));
                        rfpc.setValue(BigDecimalUtil.fromStringAndCheckNull(fs.getValue()));
                        prospectusCustodianFees.add(rfpc);
                    }
                }
            }
            response.setMaximumFrontLoad(BigDecimalUtil.fromStringAndCheckNull(api.getLsMaximumFrontLoad()));
            response.setMaximumRedemptionFee(BigDecimalUtil.fromStringAndCheckNull(api.getLsMaximumRedemptionFee()));
            response.setMaximumManagementFee(BigDecimalUtil.fromStringAndCheckNull(api.getLsMaximumManagementFee()));
            response.setCreationUnitDate(api.getLsCreationUnitDate());
            response.setActualFrontLoad(BigDecimalUtil.fromStringAndCheckNull(api.getAtActualFrontLoad()));
        }
        response.setFrontLoads(frontLoads);
        response.setRedemptionFees(redemptionFees);
        response.setManagementFees(managementFees);
        response.setDistributionFees(distributionFees);
        response.setProspectusCustodianFees(prospectusCustodianFees);
        return response;
    }
}
