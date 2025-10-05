
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMdsbeService;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.OtherFundClassesRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.OtherFundClassesResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.otherfundclass.AssetClass;
import com.hhhh.group.secwealth.mktdata.fund.dao.OtherFundClassesDao;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;


@Service("otherFundClassesService")
public class OtherFundClassesServiceImpl extends AbstractMdsbeService {

    @Autowired
    @Qualifier("otherFundClassesDao")
    private OtherFundClassesDao otherFundClassesDao;

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    public Object execute(final Object object) throws Exception {
        LogUtil.debug(OtherFundClassesServiceImpl.class, "Enter into the OtherFundClassesServiceImpl");
        OtherFundClassesRequest request = (OtherFundClassesRequest) object;
        String locale = request.getLocale();
        int index = this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + locale);

        List<UtProdInstm> utProdInstmList = this.otherFundClassesDao.getUtProdInstmList(request);
        OtherFundClassesResponse response = new OtherFundClassesResponse();

        if (ListUtil.isValid(utProdInstmList)) {
            List<AssetClass> assetClasses = new ArrayList<AssetClass>();
            for (UtProdInstm utProdInstm : utProdInstmList) {
                AssetClass assetClass = new AssetClass();
//                SearchProduct searchProduct = this.internalProductKeyUtil.getProductBySearchWithAltClassCde("M",
//                    request.getCountryCode(), request.getGroupMember(), utProdInstm.getSymbol(), utProdInstm.getMarket(),
//                    utProdInstm.getProductType(), request.getLocale());
               
                SearchProduct searchProduct =  internalProductKeyUtil.getProductBySearchWithAltClassCde("M",
                    request.getCountryCode(), request.getGroupMember(), utProdInstm.getSymbol(), utProdInstm.getMarket(),
                    utProdInstm.getProductType(), request.getLocale());
                
                
                if (null != searchProduct && null != searchProduct.getSearchObject()) {
                    List<ProdAltNumSeg> prodAltNumSegs = searchProduct.getSearchObject().getProdAltNumSeg();
                    assetClass.setProdAltNumSegs(prodAltNumSegs);
                } else {
                    LogUtil.error(OtherFundClassesServiceImpl.class, "No record found for symbol=" + utProdInstm.getSymbol());
                    throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND);
                }

                if (0 == index) {
                    assetClass.setProductName(utProdInstm.getProdName());
                    assetClass.setProductShortName(utProdInstm.getProductShortName());
                    assetClass.setFundShreClsName(utProdInstm.getFundShreClsName());
                } else if (1 == index) {
                    assetClass.setProductName(utProdInstm.getProdPllName());
                    assetClass.setProductShortName(utProdInstm.getProductShortPrimaryLanguageName());
                    assetClass.setFundShreClsName(utProdInstm.getFundShreClsNamePriLang());
                } else if (2 == index) {
                    assetClass.setProductName(utProdInstm.getProdSllName());
                    assetClass.setProductShortName(utProdInstm.getProductShortSecondLanguageName());
                    assetClass.setFundShreClsName(utProdInstm.getFundShreClsNameSecLang());
                } else {
                    assetClass.setProductName(utProdInstm.getProdName());
                    assetClass.setProductShortName(utProdInstm.getProductShortName());
                    assetClass.setFundShreClsName(utProdInstm.getFundShreClsName());
                }
                assetClass.setProdStatCde(utProdInstm.getProdStatCde());
                assetClasses.add(assetClass);
            }
            response.setAssetClasses(assetClasses);
        } else {
            LogUtil.warn(OtherFundClassesServiceImpl.class,
                "Can not get record from DB, No record found for OtherFundClasses|request=", request);
        }
        return response;
    }
}
