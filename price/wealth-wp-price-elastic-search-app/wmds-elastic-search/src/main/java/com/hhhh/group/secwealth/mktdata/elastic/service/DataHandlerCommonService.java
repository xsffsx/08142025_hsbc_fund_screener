package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.*;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.properties.PredsrchSortingOrderProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.elastic.util.PredictiveSearchConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DataHandlerCommonService {

    private static Logger logger = LoggerFactory.getLogger(DataHandlerCommonService.class);

    private static final String PRIORITY99 = "99";

    private static final String PRIORITY_9999 = "9999";

    // popularity this is the default lowest
    private static final String LOWEST_POPULARITY = String.valueOf(Integer.MIN_VALUE);

    @Value("${predsrch.skipErrRecord}")
    private boolean skipErrRecord = false;

    @Autowired
    private PredsrchSortingOrderProperties predsrchSortingOrderProperties;

    public CustomizedEsIndexForProduct handleCommonData(final ProdKeySeg prodKeySeg, final List<ProdAltNumSeg> prodAltNumSegs,
                                                         final ProdInfoSeg prodInfoSeg, final boolean[] errors) {
        CustomizedEsIndexForProduct customizedEsIndexForUtb = new CustomizedEsIndexForProduct();
        String siteKey = "";
        String prodCde = null;
        String prodTypeCde = null;
        if (prodKeySeg != null) {
            siteKey = prodKeySeg.getCtryRecCde() + prodKeySeg.getGrpMembrRecCde();
            customizedEsIndexForUtb.setCtryRecCde(prodKeySeg.getCtryRecCde());
            customizedEsIndexForUtb.setGrpMembrRecCde(prodKeySeg.getGrpMembrRecCde());
            prodCde = prodKeySeg.getProdCde();// 263
            customizedEsIndexForUtb.setProductCode(prodCde);
            prodTypeCde = prodKeySeg.getProdTypeCde();// 167
            customizedEsIndexForUtb.setProductType(prodTypeCde);
        }
        this.updateProductAltNumSegs(prodAltNumSegs, errors, customizedEsIndexForUtb);

        if (prodInfoSeg != null) {
            String prodName = prodInfoSeg.getProdName();
            String prodPllName = prodInfoSeg.getProdPllName();
            String prodSllName = prodInfoSeg.getProdSllName();
            List<String> productNameAnalyzed = new ArrayList<>();
            productNameAnalyzed.add(prodName);
            productNameAnalyzed.add(prodPllName);
            productNameAnalyzed.add(prodSllName);
            customizedEsIndexForUtb.setProductName(prodName);
            customizedEsIndexForUtb.setProductNameAnalyzed(productNameAnalyzed);

            String prodShrtName = prodInfoSeg.getProdShrtName();
            String prodShrtPllName = prodInfoSeg.getProdShrtPllName();
            String prodShrtSllName = prodInfoSeg.getProdShrtSllName();
            List<String> productShrtNameAnalyzed = new ArrayList<>();
            productShrtNameAnalyzed.add(prodShrtName);
            productShrtNameAnalyzed.add(prodShrtPllName);
            productShrtNameAnalyzed.add(prodShrtSllName);
            customizedEsIndexForUtb.setProductShortName(prodShrtName);
            customizedEsIndexForUtb.setProductShrtNameAnalyzed(productShrtNameAnalyzed);

            String allowSellMipProdInd = prodInfoSeg.getAllowSellMipProdInd();
            customizedEsIndexForUtb.setAllowSellMipProdInd(allowSellMipProdInd);

            String allowSwInProdInd = prodInfoSeg.getAllowSwInProdInd();
            customizedEsIndexForUtb.setAllowSwInProdInd(allowSwInProdInd);

            String allowSwOutProdInd = prodInfoSeg.getAllowSwOutProdInd();
            customizedEsIndexForUtb.setAllowSwOutProdInd(allowSwOutProdInd);

            String prodTaxFreeWrapActStaCde = prodInfoSeg.getProdTaxFreeWrapActStaCde();
            customizedEsIndexForUtb.setProdTaxFreeWrapActStaCde(prodTaxFreeWrapActStaCde);

            customizedEsIndexForUtb.setProdStatCde(prodInfoSeg.getProdStatCde());

            String prodSubtpCde = prodInfoSeg.getProdSubtpCde();
            // The prodSubType is null
            customizedEsIndexForUtb.setProductSubType(prodSubtpCde);

            String ccyProdCde = prodInfoSeg.getCcyProdCde();
            customizedEsIndexForUtb.setProductCcy(ccyProdCde);

            String allowBuyProdInd = prodInfoSeg.getAllowBuyProdInd();
            customizedEsIndexForUtb.setAllowBuy(allowBuyProdInd);
            String allowSellProdInd = prodInfoSeg.getAllowSellProdInd();
            customizedEsIndexForUtb.setAllowSell(allowSellProdInd);

            this.updateProductAssetCountries(prodInfoSeg, customizedEsIndexForUtb);
            this.updateProductAssetSectors(prodInfoSeg, customizedEsIndexForUtb);

            String countryTradableCode = prodInfoSeg.getCtryProdTrade1Cde();
            customizedEsIndexForUtb.setCountryTradableCode(countryTradableCode);

            // this part for priority calculation, we may need to review and revise
            // for existing sorting logic, please refer to CommonConverter.java 189-216/259-267 for logic
            // priority calculation = product type + product subtype + market
            String productTypePriority = DataHandlerCommonService.PRIORITY99;
            String productSubTypePriority = DataHandlerCommonService.PRIORITY99;
            Map<String, String> productTypePriorityMap =
                    this.predsrchSortingOrderProperties.getSiteProductTypePriorityMap().get(siteKey);
            if (productTypePriorityMap == null) {
                productTypePriorityMap =
                        this.predsrchSortingOrderProperties.getSiteProductTypePriorityMap().get(CommonConstants.DEFAULT);
            }
            if ((productTypePriorityMap != null) && (productTypePriorityMap.containsKey(prodTypeCde))) {
                productTypePriority = productTypePriorityMap.get(prodTypeCde);
                productSubTypePriority = updateProductSubTypePriority(siteKey, prodTypeCde, prodSubtpCde, productSubTypePriority);
            }

            String countryTradableCodePriority = this.getCountryTradableCodePriority(siteKey, countryTradableCode);
            String marketPriority = this.getMarketPriority(prodInfoSeg, siteKey);
            String typePriority = productTypePriority + productSubTypePriority;
            if (typePriority.length() != 4) {
                typePriority = DataHandlerCommonService.PRIORITY_9999;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("productTypePriority: {}", productTypePriority);
                logger.debug("productSubTypePriority: {}", productSubTypePriority);
                logger.debug("countryTradableCodePriority: {}", countryTradableCodePriority);
                logger.debug("marketPriority: {}", marketPriority);
                logger.debug("priority: {}", typePriority);
            }

            customizedEsIndexForUtb.setProductTypeWeight(typePriority);
            customizedEsIndexForUtb.setCountryTradableCodeWeight(countryTradableCodePriority);
            customizedEsIndexForUtb.setMarketWeight(marketPriority);
            customizedEsIndexForUtb.setPopularity(DataHandlerCommonService.LOWEST_POPULARITY);

            String riskLvlCde = prodInfoSeg.getRiskLvlCde();
            customizedEsIndexForUtb.setRiskLvlCde(riskLvlCde);
            String mktInvstCde = prodInfoSeg.getMktInvstCde();
            customizedEsIndexForUtb.setMarket(mktInvstCde);
            String islmProdInd = prodInfoSeg.getIslmProdInd();
            customizedEsIndexForUtb.setIslmProdInd(islmProdInd);

            customizedEsIndexForUtb.setId(new StringBuffer(prodCde).append(prodTypeCde).toString());

        }
        return customizedEsIndexForUtb;
    }

    private void updateProductAltNumSegs(List<ProdAltNumSeg> prodAltNumSegs, boolean[] errors, CustomizedEsIndexForProduct customizedEsIndexForUtb) {
        List<ProdAltNumSeg> prodAltNumSegList = new ArrayList<>();
        boolean mcode = false;
        boolean rcode = false;
        boolean tcode = false;
        if (ListUtil.isValid(prodAltNumSegs)) {
            for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
                String prodCdeAltClassCde = prodAltNumSeg.getProdCdeAltClassCde();
                String prodAltNum = prodAltNumSeg.getProdAltNum();
                if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_F.equalsIgnoreCase(prodCdeAltClassCde)) {
                    ProdAltNumSeg seg = new ProdAltNumSeg();
                    seg.setProdAltNum(prodAltNum);
                    seg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_F);
                    prodAltNumSegList.add(seg);
                } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_I.equalsIgnoreCase(prodCdeAltClassCde)) {
                    ProdAltNumSeg seg = new ProdAltNumSeg();
                    seg.setProdAltNum(prodAltNum);
                    seg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_I);
                    prodAltNumSegList.add(seg);
                    customizedEsIndexForUtb.setIsin(prodAltNum);
                } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_J.equalsIgnoreCase(prodCdeAltClassCde)) {
                    ProdAltNumSeg seg = new ProdAltNumSeg();
                    seg.setProdAltNum(prodAltNum);
                    seg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_J);
                    prodAltNumSegList.add(seg);
                    customizedEsIndexForUtb.setIsin(prodAltNum);
                } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M.equalsIgnoreCase(prodCdeAltClassCde)) {
                    ProdAltNumSeg seg = new ProdAltNumSeg();
                    seg.setProdAltNum(prodAltNum);
                    seg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M);
                    prodAltNumSegList.add(seg);
                    mcode = true;
                    customizedEsIndexForUtb.setSymbol(prodAltNum);
                    customizedEsIndexForUtb.setSymbolLowercase(prodAltNum.toLowerCase());
                } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_O.equalsIgnoreCase(prodCdeAltClassCde)) {
                    ProdAltNumSeg seg = new ProdAltNumSeg();
                    seg.setProdAltNum(prodAltNum);
                    seg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_O);
                    prodAltNumSegList.add(seg);
                } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_P.equalsIgnoreCase(prodCdeAltClassCde)) {
                    ProdAltNumSeg seg = new ProdAltNumSeg();
                    seg.setProdAltNum(prodAltNum);
                    seg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_P);
                    prodAltNumSegList.add(seg);
                    customizedEsIndexForUtb.setProdCode(prodAltNum);
                } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_R.equalsIgnoreCase(prodCdeAltClassCde)) {
                    addProdAltNumSeg(customizedEsIndexForUtb, prodAltNumSegList, prodAltNum);
                    rcode = true;
                } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_S.equalsIgnoreCase(prodCdeAltClassCde)) {
                    ProdAltNumSeg seg = new ProdAltNumSeg();
                    seg.setProdAltNum(prodAltNum);
                    seg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_S);
                    prodAltNumSegList.add(seg);
                    customizedEsIndexForUtb.setSedol(prodAltNum);
                } else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T.equalsIgnoreCase(prodCdeAltClassCde)) {
                    addProdAltNumSeg(customizedEsIndexForUtb, prodAltNumSegList, prodAltNum);
                    tcode = true;
                }
            }
        }
        customizedEsIndexForUtb.setProdAltNumSegs(prodAltNumSegList);
        this.updateErrorsIfNotValid(errors, mcode, rcode, tcode);
    }

    private void updateProductAssetCountries(ProdInfoSeg prodInfoSeg, CustomizedEsIndexForProduct customizedEsIndexForUtb) {
        List<AsetGeoLocAllocSeg> asetGeoLocAllocSegs = prodInfoSeg.getAsetGeoLocAllocSeg();
        if (ListUtil.isValid(asetGeoLocAllocSegs)) {
            List<String> assetCountries = new ArrayList<>();
            for (AsetGeoLocAllocSeg asetGeoLocAllocSeg : asetGeoLocAllocSegs) {
                assetCountries.add(asetGeoLocAllocSeg.getGeoLocCde());
            }
            customizedEsIndexForUtb.setAssetCountries(assetCountries);
        }
    }

    private void updateProductAssetSectors(ProdInfoSeg prodInfoSeg, CustomizedEsIndexForProduct customizedEsIndexForUtb) {
        List<AsetSicAllocSeg> asetSicAllocSegs = prodInfoSeg.getAsetSicAllocSeg();
        if (ListUtil.isValid(asetSicAllocSegs)) {
            List<String> assetSectors = new ArrayList<>();
            for (AsetSicAllocSeg asetSicAllocSeg : asetSicAllocSegs) {
                assetSectors.add(asetSicAllocSeg.getSicCde());
            }
            customizedEsIndexForUtb.setAssetSectors(assetSectors);
        }
    }

    private void addProdAltNumSeg(CustomizedEsIndexForProduct customizedEsIndexForUtb, List<ProdAltNumSeg> prodAltNumSegList, String prodAltNum) {
        ProdAltNumSeg seg = new ProdAltNumSeg();
        seg.setProdAltNum(prodAltNum);
        seg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T);
        prodAltNumSegList.add(seg);
        if (customizedEsIndexForUtb.getKey() == null) {
            customizedEsIndexForUtb.setKey(prodAltNum);
        }
    }

    private String getMarketPriority(ProdInfoSeg prodInfoSeg, String siteKey) {
        String marketPriority = DataHandlerCommonService.PRIORITY99;
        Map<String, String> marketPriorityMap =
                this.predsrchSortingOrderProperties.getSiteMarketPriorityMap().get(siteKey);
        if (marketPriorityMap == null) {
            marketPriorityMap =
                    this.predsrchSortingOrderProperties.getSiteMarketPriorityMap().get(CommonConstants.DEFAULT);
        }
        //marketCode = prodInfoSeg.getMktInvstCde()
        String marketCode = prodInfoSeg.getMktInvstCde();
        if ((marketPriorityMap != null && marketCode != null) && (marketPriorityMap.containsKey(marketCode))) {
            marketPriority = marketPriorityMap.get(marketCode);
        }
        return marketPriority;
    }

    private String getCountryTradableCodePriority(String siteKey, String countryTradableCode) {
        String countryTradableCodePriority = DataHandlerCommonService.PRIORITY99;
        Map<String, String> countryTradableCodePriorityMap =
                this.predsrchSortingOrderProperties.getSiteCountryTradableCodePriorityMap().get(siteKey);
        if (countryTradableCodePriorityMap == null) {
            countryTradableCodePriorityMap =
                    this.predsrchSortingOrderProperties.getSiteCountryTradableCodePriorityMap().get(CommonConstants.DEFAULT);
        }
        if ((countryTradableCodePriorityMap != null) && (countryTradableCodePriorityMap.containsKey(countryTradableCode))) {
            countryTradableCodePriority = countryTradableCodePriorityMap.get(countryTradableCode);
        }
        return countryTradableCodePriority;
    }

    private String updateProductSubTypePriority(String siteKey, String prodTypeCde, String prodSubtpCde, String productSubTypePriority) {
        Map<String, Map<String, String>> productTypeProductSubTypePriorityMap =
                this.predsrchSortingOrderProperties.getSiteProductTypeProductSubTypePriorityMap().get(siteKey+prodTypeCde);//
        if (productTypeProductSubTypePriorityMap == null) {
            productTypeProductSubTypePriorityMap = this.predsrchSortingOrderProperties
                    .getSiteProductTypeProductSubTypePriorityMap().get(CommonConstants.DEFAULT+prodTypeCde);
        }
        if ((productTypeProductSubTypePriorityMap != null)
                && (productTypeProductSubTypePriorityMap.containsKey(prodTypeCde))) {
            Map<String, String> productSubTypePriorityMap = productTypeProductSubTypePriorityMap.get(prodTypeCde);
            if ((productSubTypePriorityMap != null) && (productSubTypePriorityMap.containsKey(prodSubtpCde))) {
                productSubTypePriority = productSubTypePriorityMap.get(prodSubtpCde);
            }
        }
        return productSubTypePriority;
    }

    private void updateErrorsIfNotValid(boolean[] errors, boolean mcode, boolean rcode, boolean tcode) {
        if (!mcode) {
            errors[0] = true;
        }
        if (!(tcode || rcode)) {
            errors[1] = true;
        }
    }

    /**
     *
     * @param info
     * @param totalNum
     * @param parseUtExceptionFlag
     * @param objList
     * @param utList
     */
    public void updateObjListAndFileInfo(DataSiteEntity.DataFileInfo info, int totalNum, boolean parseUtExceptionFlag, List<CustomizedEsIndexForProduct> objList, List<CustomizedEsIndexForProduct> utList) {
        logger.info("Total num of {} is {}", info.getDataFile().getName(), totalNum);
        if (totalNum > 0 && !parseUtExceptionFlag && ListUtil.isValid(utList)) {
            info.setTotalProductNum(totalNum);
            info.setParseSuccess(true);
            objList.addAll(utList);
        } else {
            info.setParseSuccess(false);
        }
    }

    /**
     *
     * @param size
     * @param symbolErr
     * @param codeErr
     * @param entity
     * @param fileName
     */
    public void handleCommonLog(final int size, final int symbolErr, final int codeErr, final DataSiteEntity entity,
                                final String fileName) {
        if (symbolErr > 0) {
            logger.warn("{}|The number of records without symbol {}", fileName, symbolErr);
            entity.setParsingError(true);
        }
        if (codeErr > 0) {
            logger.warn("{}|The number of records without T/R code {}", fileName, codeErr);
        }
        int successfulCount = size - symbolErr;
        if (successfulCount > 0) {
            logger.warn("File name: {}|The number of records were loaded into index successfully: {}", fileName, successfulCount);
        }
    }

    public void addToListIfValid(List<CustomizedEsIndexForProduct> objList, boolean[] errors, CustomizedEsIndexForProduct customizedEsIndexForUtb) {
        if (errors[0] && this.skipErrRecord) {
            logger.error("|Skip to save the record without symbol|prodName: {}", customizedEsIndexForUtb.getProductName());
        } else {
            objList.add(customizedEsIndexForUtb);
        }
    }
}
