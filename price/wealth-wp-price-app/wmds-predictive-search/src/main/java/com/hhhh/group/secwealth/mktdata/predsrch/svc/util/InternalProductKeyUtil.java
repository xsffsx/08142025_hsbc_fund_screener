package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.common.ProdCdeAltClassCdeEnum;
import com.hhhh.group.secwealth.mktdata.common.convertor.ServiceKeyToProdCdeAltClassCdeConvertor;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.system.constants.VendorType;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchProduct;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.PredictiveSearchService;

/**
 * The Class InternalProductKeyUtil.
 */
@Component("internalProductKeyUtil")
public class InternalProductKeyUtil {

    @Autowired
    @Qualifier("predictiveSearchService")
    private PredictiveSearchService predictiveSearchService;

    @Autowired
    @Qualifier("serviceKeyToProdCdeAltClassCdeConvertor")
    private ServiceKeyToProdCdeAltClassCdeConvertor prodCdeAltClassCdeConvertor;

    /**
     * Gets the product info.
     * 
     * @param ipk
     *            the ipk
     * @param serviceLog
     *            the service log
     * @param errorLog
     *            the error log
     * @return the product info
     * @throws Exception
     *             the exception
     */
    public SearchableObject getProductInfo(final InternalProductKey ipk) throws Exception {
        return getProductInfoWithLocale(ipk, null);
    }

    /**
     * Gets the product info with local.
     * 
     * @param ipk
     *            the ipk
     * @param locale
     *            the locale
     * @param serviceLog
     *            the service log
     * @param errorLog
     *            the error log
     * @return the product info with locale
     * @throws Exception
     *             the exception
     */
    public SearchableObject getProductInfoWithLocale(final InternalProductKey ipk, final String locale) throws Exception {
        return this.predictiveSearchService.getBySymbolMarket(ipk, locale);
    }


    public SearchProduct getProductBySearchWithAltClassCde(final String altClassCde, final String countryCode,
        final String groupMember, final String prodAltNum, final String countryTradableCode, final String productType,
        final String locale) throws Exception {
        SearchProduct object = new SearchProduct();
        SearchableObject obj = null;
        // Search the product by product code type
        if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M.equals(altClassCde)) {
            obj = this.predictiveSearchService.getBySymbolMarket(countryCode, groupMember, prodAltNum, countryTradableCode,
                productType, locale);
            object.setSearchObject(obj);
        } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T.equals(altClassCde)
            || PredictiveSearchConstant.PROD_ALT_CLASS_CDE_R.equals(altClassCde)) {
            obj = this.predictiveSearchService.getByRicCode(countryCode, groupMember, prodAltNum, locale);
            object.setSearchObject(obj);
        } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_W.equals(altClassCde)) {
            obj = this.predictiveSearchService.getByWCode(countryCode, groupMember, prodAltNum, locale);
            object.setSearchObject(obj);
        } else {
            LogUtil.error(InternalProductKeyUtil.class, "Can't support to predictive search the prodCdeAltClassCde=" + altClassCde);
            return null;
        }
        // Get the external code by prodCdeAltClassCde
        if (null != obj) {
            String prodCdeAltClassCde = this.prodCdeAltClassCdeConvertor.doConvert(VendorType.MSTAR);
            if (null != prodCdeAltClassCde) {
                object.setProdCdeAltClassCde(prodCdeAltClassCde);
            } else {
                LogUtil.error(InternalProductKeyUtil.class, "Can't get the external code prodCdeAltClassCde=" + altClassCde);
                return null;
            }
            if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M.equals(prodCdeAltClassCde)) {
                object.setExternalKey(obj.getSymbol());
            } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T.equals(prodCdeAltClassCde)) {
                object.setExternalKey(obj.getKey());
            } else {
                List<ProdAltNumSeg> prodAltNumSegs = obj.getProdAltNumSeg();
                for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
                    if (prodCdeAltClassCde.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
                        object.setExternalKey(prodAltNumSeg.getProdAltNum());
                        break;
                    }
                }
            }
        } else {
            return null;
        }
        return object;
    }


    /**
     * <p>
     * <b> Get the product code value by passed code type. </b>
     * </p>
     * 
     * @param code
     * @param searchObject
     * @return
     */
    public String getProductCodeValueByCodeType(final ProdCdeAltClassCdeEnum codeType, final SearchableObject searchObject) {
        if (null != searchObject) {
            List<ProdAltNumSeg> prodAltNumSegList = searchObject.getProdAltNumSeg();
            if (ListUtil.isValid(prodAltNumSegList)) {
                for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegList) {
                    if (codeType.toString().equals(prodAltNumSeg.getProdCdeAltClassCde())) {
                        return prodAltNumSeg.getProdAltNum();
                    }
                }
            }
        }
        return "";
    }

}
