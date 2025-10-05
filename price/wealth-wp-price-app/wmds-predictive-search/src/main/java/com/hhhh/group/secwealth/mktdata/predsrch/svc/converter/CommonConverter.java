/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.AsetGeoLocAllocSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.AsetSicAllocSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ChanlAttrSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAsetVoltlClassSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdInfoSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdKeySeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdUserDefExtSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ResChannelDtl;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.refData.RefData;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.refData.RefDataLst;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.ut.FundUnswithSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.ut.UtTrstInstmSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.PredictiveSearchSortingUtils;

public class CommonConverter {

    /**
     * <p>
     * <b>Set common fields value to SearchableObject. </b>
     * </p>
     *
     * @param infoSeg
     * @param keySeg
     * @param altNumSegList
     * @return
     * @throws Exception
     */
    public static SearchableObject handleCommonData(final ProdInfoSeg infoSeg, final ProdKeySeg keySeg,
        final List<ProdAltNumSeg> altNumSegList, final boolean[] errors) throws Exception {

        SearchableObject so = new SearchableObject();
        String marketCode = null;
        ProdAltNumSeg trSeg = null;
        ProdAltNumSeg ricSeg = null;
        List<ProdAltNumSeg> tempList = new ArrayList<ProdAltNumSeg>();
        for (ProdAltNumSeg seg : altNumSegList) {
            if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M.equals(seg.getProdCdeAltClassCde())) {
                marketCode = seg.getProdAltNum();
                tempList.add(seg);
            } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_R.equals(seg.getProdCdeAltClassCde())) {
                // ric code
//                String ctryProdTrade1Cde = infoSeg.getCtryProdTrade1Cde();
                String prodAltNum = seg.getProdAltNum();
                ricSeg = new ProdAltNumSeg();
                ricSeg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T);
//                if ("US".equals(ctryProdTrade1Cde)){
//                    String[] symbols = prodAltNum.split("\\.");
//                    String preSymbol = symbols[0];
//                    prodAltNum = preSymbol + ".NB";
//                }
                ricSeg.setProdAltNum(prodAltNum);
            } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T.equals(seg.getProdCdeAltClassCde())) {
                trSeg = new ProdAltNumSeg();
                trSeg.setProdAltNum(seg.getProdAltNum());
                trSeg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T);
            } else {
                tempList.add(seg);
            }
        }

        if (null != marketCode) {
            // Set market code
            so.setSymbol(marketCode);
            so.setSymbol_analyzed(marketCode);
        } else {
            errors[0] = true;
        }

        if (null != ricSeg) {
            tempList.add(ricSeg);
            so.setKey(ricSeg.getProdAltNum());
            so.setKey_analyzed(ricSeg.getProdAltNum());
        } else if (null != trSeg) {
            tempList.add(trSeg);
            so.setKey(trSeg.getProdAltNum());
            so.setKey_analyzed(trSeg.getProdAltNum());
        } else {
            errors[1] = true;
        }
        so.setProdAltNumSeg(tempList);

        // ProductName
        String prodName = CommonConstants.EMPTY_STRING;
        if (null != infoSeg.getProdName()) {
            prodName = infoSeg.getProdName().trim();
        } else {
            LogUtil.error(CommonConverter.class, "The prodName is null");
        }
        String prodPllName = CommonConstants.EMPTY_STRING;
        if (null != infoSeg.getProdPllName()) {
            prodPllName = infoSeg.getProdPllName().trim();
        }
        String prodSllName = CommonConstants.EMPTY_STRING;
        if (null != infoSeg.getProdSllName()) {
            prodSllName = infoSeg.getProdSllName().trim();
        }
        String[] prodNames = {prodName, prodPllName, prodSllName};
        so.setProductName_analyzed(prodNames);
        so.setProductName(prodName);
        /*
         * so.setProdPllName(infoSeg.getProdPllName());
         * so.setProdSllName(infoSeg.getProdSllName());
         */
        // ProductShortName
        String prodShrtName = CommonConstants.EMPTY_STRING;
        if (null != infoSeg.getProdShrtName()) {
            prodShrtName = infoSeg.getProdShrtName().trim();
        } else {
            LogUtil.error(CommonConverter.class, "The prodShrtName is null");
        }
        String prodShrtPllName = CommonConstants.EMPTY_STRING;
        if (null != infoSeg.getProdShrtPllName()) {
            prodShrtPllName = infoSeg.getProdShrtPllName().trim();
        }
        String prodShrtSllName = CommonConstants.EMPTY_STRING;
        if (null != infoSeg.getProdShrtSllName()) {
            prodShrtSllName = infoSeg.getProdShrtSllName().trim();
        }

        so.setAllowSellMipProdInd(infoSeg.getAllowSellMipProdInd());
        so.setAllowSellMipProdInd_analyzed(infoSeg.getAllowSellMipProdInd());
        so.setAllowSwInProdInd(infoSeg.getAllowSwInProdInd());
        so.setAllowSwInProdInd_analyzed(infoSeg.getAllowSwInProdInd());
        so.setAllowSwOutProdInd(infoSeg.getAllowSwOutProdInd());
        so.setAllowSwOutProdInd_analyzed(infoSeg.getAllowSwOutProdInd());
        so.setProdTaxFreeWrapActStaCde(infoSeg.getProdTaxFreeWrapActStaCde());
        so.setProdTaxFreeWrapActStaCde_analyzed(infoSeg.getProdTaxFreeWrapActStaCde());
        so.setProdStatCde(infoSeg.getProdStatCde());
        so.setProdStatCde_analyzed(infoSeg.getProdStatCde());

        // String[] switchGroups = infoSeg.getSwitchableGroups();
        // if (null != switchGroups && switchGroups.length > 0) {
        // so.setSwitchableGroup_analyzed(switchGroups);
        // }

        String[] prodShrtNames = {prodShrtName, prodShrtPllName, prodShrtSllName};
        so.setProductShortName_analyzed(prodShrtNames);
        so.setProductShortName(prodShrtName);
        /*
         * so.setProdShrtPllName(infoSeg.getProdShrtPllName());
         * so.setProdShrtSllName(infoSeg.getProdShrtSllName());
         */
        // ProductType
        String prodType = CommonConstants.EMPTY_STRING;
        // Site key for getting sorting orders map
        String siteKey = keySeg.getCtryRecCde() + keySeg.getGrpMembrRecCde();
        if (null != keySeg.getProdTypeCde()) {
            prodType = keySeg.getProdTypeCde();
            so.setProductType(prodType);
            so.setProductType_analyzed(prodType);

            // Set ProductType weight for sorting
            Map<String, Double> productTypeWeightMap = PredictiveSearchSortingUtils.getProductTypeWeightMap(siteKey);
            Map<String, Map<String, Double>> productSubTypeWeightMap =
                PredictiveSearchSortingUtils.getProductSubTypeWeightMap(siteKey);
            if (productTypeWeightMap != null) {
                if (productTypeWeightMap.containsKey(prodType)) {
                    if (productSubTypeWeightMap != null && productSubTypeWeightMap.containsKey(prodType)) {
                        Map<String, Double> productSubTypeMap = productSubTypeWeightMap.get(prodType);
                        if (productSubTypeMap != null) {
                            String prodSubType = infoSeg.getProdSubtpCde();
                            if (productSubTypeMap.containsKey(prodSubType)) {
                                so.setProductTypeWeight(
                                    productTypeWeightMap.get(prodType) + 0.01 * productSubTypeMap.get(prodSubType));
                            } else {
                                so.setProductTypeWeight(
                                    (double) productTypeWeightMap.get(prodType) + 0.01 * (productSubTypeMap.size() + 1));
                            }
                        }
                    } else {
                        so.setProductTypeWeight((double) productTypeWeightMap.get(prodType));
                    }
                } else {
                    so.setProductTypeWeight((double) (productTypeWeightMap.size() + 1));
                }
            }
        } else {
            LogUtil.error(CommonConverter.class, "The prodType is null");
        }
        // ProductSubType
        String prodSubType = CommonConstants.EMPTY_STRING;
        if (null != infoSeg.getProdSubtpCde()) {
            prodSubType = infoSeg.getProdSubtpCde();
            so.setProductSubType(prodSubType);
            // productType+"_"+productSubType for search
            so.setProductSubType_analyzed(StringUtil.combineWithUnderline(prodType, prodSubType));
        } else {
            LogUtil.debug(CommonConverter.class, "The prodSubType is null");
        }
        // Currency product code
        so.setProductCcy(infoSeg.getCcyProdCde());
        // AllowBuy
        so.setAllowBuy(infoSeg.getAllowBuyProdInd());
        so.setAllowBuy_analyzed(infoSeg.getAllowBuyProdInd());
        // AllowSell
        so.setAllowSell(infoSeg.getAllowSellProdInd());
        // AssetCountry=geoLocAllocSeg.getGeoLocCde()
        List<AsetGeoLocAllocSeg> geoLocAllocSegList = infoSeg.getAsetGeoLocAllocSeg();
        List<AsetSicAllocSeg> sicAllocSegList = infoSeg.getAsetSicAllocSeg();
        if (null != geoLocAllocSegList && geoLocAllocSegList.size() > 0) {
            int geoLocAllocSeg_size = geoLocAllocSegList.size();
            String[] strs1 = new String[geoLocAllocSeg_size];
            for (int i = 0; i < geoLocAllocSeg_size; i++) {
                strs1[i] = geoLocAllocSegList.get(i).getGeoLocCde();
            }
            so.setAssetCountry(strs1);
        }
        // AssetSector=sicAllocSeg.getSicCde()
        if (null != sicAllocSegList && sicAllocSegList.size() > 0) {
            int sicAllocSeg_size = sicAllocSegList.size();
            String[] strs2 = new String[sicAllocSeg_size];
            for (int i = 0; i < sicAllocSeg_size; i++) {
                strs2[i] = sicAllocSegList.get(i).getSicCde();
            }
            so.setAssetSector(strs2);
        }
        // CountryTradableCode
        String countryTradableCode = infoSeg.getCtryProdTrade1Cde();
        so.setCountryTradableCode(countryTradableCode);
        so.setCountryTradableCode_analyzed(countryTradableCode);

        // Set CountryTradableCode weight for sorting
        Map<String, Integer> marketWeightMap = PredictiveSearchSortingUtils.getMarketWeightMap(siteKey);
        if (marketWeightMap != null) {
            if (marketWeightMap.containsKey(countryTradableCode)) {
                so.setCountryTradableCodeWeight(marketWeightMap.get(countryTradableCode));
            } else {
                so.setCountryTradableCodeWeight(marketWeightMap.size() + 1);
            }
        }

        // set risk rating
        so.setRiskLvlCde(infoSeg.getRiskLvlCde());

        // set Market
        so.setMarket(infoSeg.getMktInvstCde());

        String prodCde = CommonConstants.EMPTY_STRING;
        // set product code/W code
        if (null != keySeg.getProdCde()) {
            prodCde = String.valueOf(keySeg.getProdCde());
        } else {
            LogUtil.error(CommonConverter.class, "The Product Code is null");
        }
        so.setProductCode(prodCde);
        so.setProductCode_analyzed(prodCde);

        // id=ProductCode+prodType
        so.setId(new StringBuffer(prodCde).append(prodType).toString());

        return so;
    }

    public static String[] getParentAssetClass(final RefDataLst refDataList, final List<ProdAsetVoltlClassSeg> classSeg)
        throws Exception {
        try {
            if (null != refDataList && null != refDataList.getRefData() && refDataList.getRefData().size() > 0 && null != classSeg
                && classSeg.size() > 0) {
                Set<String> set = new HashSet<String>();
                List<RefData> list = refDataList.getRefData();
                for (RefData data : list) {
                    for (ProdAsetVoltlClassSeg seg : classSeg) {
                        if (data.getCdvCde().equals(seg.getAsetVoltlClassCde())) {
                            set.add(data.getCdvParntCde());
                        }
                    }
                }
                if (null != set && set.size() > 0) {
                    String[] strs = new String[set.size()];
                    set.toArray(strs);
                    return strs;
                }
            }
        } catch (Exception e) {
            LogUtil.error(CommonConverter.class, e.getMessage(), e);
        }
        return null;
    }

    public static String[] getKeyList(final ProdInfoSeg prodInfoSeg, final String ind) throws Exception {
        if (null != prodInfoSeg) {
            Set<String> set = new HashSet<String>();
            if ("switchFundGrpList".equals(ind) && null != prodInfoSeg.getProdUserDefExtSeg()) {
                for (ProdUserDefExtSeg seg : prodInfoSeg.getProdUserDefExtSeg()) {
                    if (null != seg && "switchFundGrpList".equals(seg.getFieldCde())) {
                        set.add(seg.getFieldValue());
                    }
                }
            } else if ("resChannelCde".equals(ind) && null != prodInfoSeg.getResChanSeg()
                && null != prodInfoSeg.getResChanSeg().getResChannelDtl()) {
                for (ResChannelDtl seg : prodInfoSeg.getResChanSeg().getResChannelDtl()) {
                    if (null != seg) {
                        set.add(seg.getResChannelCde());
                    }
                }
            } else if ("chanlCde".equals(ind) && null != prodInfoSeg.getChanlAttrSeg()) {
                for (ChanlAttrSeg seg : prodInfoSeg.getChanlAttrSeg()) {
                    if (null != seg) {
                        set.add(seg.getChanlCde());
                    }
                }
            }
            if (null != set && set.size() > 0) {
                String[] strs = new String[set.size()];
                set.toArray(strs);
                return strs;
            }
        }
        return null;
    }

    public static List<String> getChanlAttrSegInd(final ProdInfoSeg prodInfoSeg, final String ind) throws Exception {
        List<String> chanlList = new ArrayList<String>();
        if (null != prodInfoSeg) {
            if (ListUtil.isValid(prodInfoSeg.getChanlAttrSeg())) {
                for (ChanlAttrSeg seg : prodInfoSeg.getChanlAttrSeg()) {
                    if (null != seg && StringUtil.isValid(seg.getChanlCde()) && ind.equals(seg.getFieldCde())
                        && StringUtil.isValid(seg.getFieldValue())) {
                        chanlList.add(seg.getChanlCde());
                        chanlList.add(seg.getFieldValue());
                    }
                }
            }
        }
        return chanlList;
    }

    public static String getProdUserDefExtSegInd(final ProdInfoSeg prodInfoSeg, final String ind) throws Exception {
        if (null != prodInfoSeg) {
            if (ListUtil.isValid(prodInfoSeg.getProdUserDefExtSeg())) {
                for (ProdUserDefExtSeg seg : prodInfoSeg.getProdUserDefExtSeg()) {
                    if (null != seg && ind.equals(seg.getFieldCde())) {
                        return seg.getFieldValue();
                    }
                }
            }
        }
        return null;
    }

    public static String[] getFundUnswithSeg(final UtTrstInstmSeg seg) throws Exception {
        List<FundUnswithSeg> fundUnswithList = seg.getFundUnswithSeg();
        String[] fundUnswithSeg = null;
        if (ListUtil.isValid(fundUnswithList)) {
            fundUnswithSeg = new String[fundUnswithList.size()];
            for (int j = 0; j < fundUnswithList.size(); j++) {
                fundUnswithSeg[j] = fundUnswithList.get(j).getFundUnswCde();
            }
        }
        return fundUnswithSeg;
    }

    public static void handleCommonLog(final int size, final int symbolErr, final int codeErr, final DataSiteEntity entity,
        final String fileName) {
        if (symbolErr > 0) {
            LogUtil.warn(CommonConverter.class, fileName + "|The number of records without symbol=" + symbolErr);
            entity.setParsingError(true);
        }
        if (codeErr > 0) {
            LogUtil.warn(CommonConverter.class, fileName + "|The number of records without T/R code=" + codeErr);
        }
        int successfulCount = size - symbolErr;
        if (successfulCount > 0) {
            LogUtil.warn(CommonConverter.class,
                fileName + "|The number of records were loaded into index successfully=" + successfulCount);
        }
    }

}
