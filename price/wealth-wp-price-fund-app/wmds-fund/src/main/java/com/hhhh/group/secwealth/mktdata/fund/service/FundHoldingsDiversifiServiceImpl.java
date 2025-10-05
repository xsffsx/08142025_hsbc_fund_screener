
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtCatAsetAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMdsbeService;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundHoldingsDiversifiRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundHoldingsDiversifiResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundholdingsdiversifi.AssetClass;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundholdingsdiversifi.GeographicRegion;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundholdingsdiversifi.HoldingsDiversificationItem;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundholdingsdiversifi.HoldingsDiversificationItem.Header;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundholdingsdiversifi.SectorClass;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundHoldingsDiversifiDao;

@Service("fundHoldingsDiversifiService")
public class FundHoldingsDiversifiServiceImpl extends AbstractMdsbeService {

    @Autowired
    @Qualifier("fundHoldingsDiversifiDao")
    private FundHoldingsDiversifiDao fundHoldingsDiversifiDao;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @Override
    public Object execute(final Object object) throws Exception {

        FundHoldingsDiversifiRequest request = (FundHoldingsDiversifiRequest) object;

        int index = this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT
            + request.getLocale());

        List<Integer> prodIds_DB = this.fundHoldingsDiversifiDao.searchProductId(request);
        List<UtProdInstm> holdingFundList = this.fundHoldingsDiversifiDao.searchHoldingDiversification(prodIds_DB);

        List<UtCatAsetAlloc> assetClasses = this.fundHoldingsDiversifiDao.searchAllocation(prodIds_DB, "ASET_VOLTL");
        List<UtCatAsetAlloc> marketSectors = this.fundHoldingsDiversifiDao.searchAllocation(prodIds_DB, "SIC_ALLOC");
        List<UtCatAsetAlloc> geoClasses = this.fundHoldingsDiversifiDao.searchAllocation(prodIds_DB, "GEO_LOC");

        sortHolding(assetClasses);
        sortHolding(marketSectors);
        sortHolding(geoClasses);

        // Prepare Response
        FundHoldingsDiversifiResponse response = new FundHoldingsDiversifiResponse();
        List<HoldingsDiversificationItem> holdingsDiversification = new ArrayList<HoldingsDiversificationItem>();
        HoldingsDiversificationItem holdingsDiversificationItem = null;

        for (Integer prodIds : prodIds_DB) {
            if (ListUtil.isValid(holdingFundList)) {
                for (UtProdInstm holdingFund : holdingFundList) {
                    String lastUpdatedDate = null;
                    if (prodIds.equals(holdingFund.getUtProdInstmId().getProductId())) {
                        holdingsDiversificationItem = new HoldingsDiversificationItem();
                        if (null != holdingFund) {
                            // set Header
                            Header header = holdingsDiversificationItem.new Header();
                            if (0 == index) {
                                header.setName(holdingFund.getProdName());
                            } else if (1 == index) {
                                header.setName(holdingFund.getProdPllName());
                            } else if (2 == index) {
                                header.setName(holdingFund.getProdSllName());
                            } else {
                                header.setName(holdingFund.getProdName());
                            }
                            header.setCurrency(holdingFund.getCurrency());
                            header.setProdAltNum(holdingFund.getSymbol());
                            holdingsDiversificationItem.setHeader(header);
                            lastUpdatedDate = DateUtil.getSimpleDateFormat(holdingFund.getAsOfDate(),
                                DateConstants.DateFormat_yyyyMMdd_withHyphen);
                        }
                    } else {
                        continue;
                    }

                    // set AssetClass
                    List<AssetClass> assetClassList = new ArrayList<AssetClass>();
                    if (ListUtil.isValid(assetClasses)) {
                        for (int j = 0; j < assetClasses.size(); j++) {
                            UtCatAsetAlloc alloc = assetClasses.get(j);
                            if (null != alloc) {
                                AssetClass assetClass = new AssetClass();
                                if (prodIds.equals(alloc.getUtCatAsetAllocId().getProductId())) {
                                    if (0 == index) {
                                        assetClass.setAssetClassName(StringUtil.toStringAndCheckNull(alloc.getProdnName1()));
                                    } else if (1 == index) {
                                        assetClass.setAssetClassName(StringUtil.toStringAndCheckNull(alloc.getProdnName2()));
                                    } else if (2 == index) {
                                        assetClass.setAssetClassName(StringUtil.toStringAndCheckNull(alloc.getProdnName3()));
                                    } else {
                                        assetClass.setAssetClassName(StringUtil.toStringAndCheckNull(alloc.getProdnName1()));
                                    }
                                    assetClass.setWeighting(alloc.getFundAllocation());
                                    assetClass.setAllocationCategoryAverage(alloc.getCategoryAllocation());
                                    if ("0".equals(alloc.getIsFundShortPosition())) {
                                        assetClass.setWeightingIndicator(true);
                                    } else {
                                        assetClass.setWeightingIndicator(false);
                                    }
                                    if ("0".equals(alloc.getIsCategoryShortPosition())) {
                                        assetClass.setAllocationCategoryAverageIndicator(true);
                                    } else {
                                        assetClass.setAllocationCategoryAverageIndicator(false);
                                    }
                                    assetClassList.add(assetClass);
                                }
                            }
                        }
                        holdingsDiversificationItem.setAssetClasses(assetClassList);
                    }

                    // set SectorClass
                    List<SectorClass> sectorClassList = new ArrayList<SectorClass>();
                    if (ListUtil.isValid(marketSectors)) {
                        for (int j = 0; j < marketSectors.size(); j++) {
                            UtCatAsetAlloc alloc = marketSectors.get(j);
                            if (null != alloc) {
                                SectorClass sectorClass = new SectorClass();
                                if (prodIds.equals(alloc.getUtCatAsetAllocId().getProductId())) {
                                    if (0 == index) {
                                        sectorClass.setSectorClassName(StringUtil.toStringAndCheckNull(alloc.getProdnName1()));
                                    } else if (1 == index) {
                                        sectorClass.setSectorClassName(StringUtil.toStringAndCheckNull(alloc.getProdnName2()));
                                    } else if (2 == index) {
                                        sectorClass.setSectorClassName(StringUtil.toStringAndCheckNull(alloc.getProdnName3()));
                                    } else {
                                        sectorClass.setSectorClassName(StringUtil.toStringAndCheckNull(alloc.getProdnName1()));
                                    }
                                    sectorClass.setWeighting(alloc.getFundAllocation());
                                    sectorClass.setAllocationCategoryAverage(alloc.getCategoryAllocation());
                                    if ("0".equals(alloc.getIsFundShortPosition())) {
                                        sectorClass.setWeightingIndicator(true);
                                    } else {
                                        sectorClass.setWeightingIndicator(false);
                                    }
                                    if ("0".equals(alloc.getIsCategoryShortPosition())) {
                                        sectorClass.setAllocationCategoryAverageIndicator(true);
                                    } else {
                                        sectorClass.setAllocationCategoryAverageIndicator(false);
                                    }
                                    sectorClassList.add(sectorClass);
                                }
                            }
                        }
                        holdingsDiversificationItem.setSectorClasses(sectorClassList);
                    }

                    // set GeographicRegion
                    List<GeographicRegion> geographicRegionList = new ArrayList<GeographicRegion>();
                    if (ListUtil.isValid(geoClasses)) {
                        for (int j = 0; j < geoClasses.size(); j++) {
                            UtCatAsetAlloc alloc = geoClasses.get(j);
                            if (null != alloc) {
                                GeographicRegion geographicRegion = new GeographicRegion();
                                if (prodIds.equals(alloc.getUtCatAsetAllocId().getProductId())) {
                                    if (0 == index) {
                                        geographicRegion.setGeographicRegionName(StringUtil.toStringAndCheckNull(alloc
                                            .getProdnName1()));
                                    } else if (1 == index) {
                                        geographicRegion.setGeographicRegionName(StringUtil.toStringAndCheckNull(alloc
                                            .getProdnName2()));
                                    } else if (2 == index) {
                                        geographicRegion.setGeographicRegionName(StringUtil.toStringAndCheckNull(alloc
                                            .getProdnName3()));
                                    } else {
                                        geographicRegion.setGeographicRegionName(StringUtil.toStringAndCheckNull(alloc
                                            .getProdnName1()));
                                    }
                                    geographicRegion.setWeighting(alloc.getFundAllocation());
                                    geographicRegion.setAllocationCategoryAverage(alloc.getCategoryAllocation());
                                    if ("0".equals(alloc.getIsFundShortPosition())) {
                                        geographicRegion.setWeightingIndicator(true);
                                    } else {
                                        geographicRegion.setWeightingIndicator(false);
                                    }
                                    if ("0".equals(alloc.getIsCategoryShortPosition())) {
                                        geographicRegion.setAllocationCategoryAverageIndicator(true);
                                    } else {
                                        geographicRegion.setAllocationCategoryAverageIndicator(false);
                                    }
                                    geographicRegionList.add(geographicRegion);
                                }
                            }
                        }
                        holdingsDiversificationItem.setGeographicRegions(geographicRegionList);
                    }
                    holdingsDiversificationItem.setLastUpdatedDate(lastUpdatedDate);
                    holdingsDiversification.add(holdingsDiversificationItem);
                }
            }
            response.setHoldingsDiversifications(holdingsDiversification);
        }
        return response;
    }


    private static void sortHolding(final List<UtCatAsetAlloc> holdingList) {
        Collections.sort(holdingList, new Comparator<UtCatAsetAlloc>() {
            @Override
            public int compare(final UtCatAsetAlloc alloc1, final UtCatAsetAlloc alloc2) {
                if (alloc1 == null || alloc1.getFundAllocation() == null) {
                    return 1;
                } else if (alloc2 == null || alloc2.getFundAllocation() == null) {
                    return -1;
                } else {
                    return alloc2.getFundAllocation().compareTo(alloc1.getFundAllocation());
                }
            }
        });
    }

    private void addAssetAllocation(final List<UtCatAsetAlloc> allocList) {
        if (allocList == null) {
            return;
        }
        BigDecimal allocWeightSum = sumAllocationWeight(allocList);
        for (UtCatAsetAlloc alloc : allocList) {
            BigDecimal weight = alloc.getFundAllocation();
            BigDecimal weightPercent = null;
            if (!allocWeightSum.equals(BigDecimal.ZERO)) {
                weightPercent = weight.abs().multiply(new BigDecimal(100)).divide(allocWeightSum, 5, RoundingMode.HALF_UP);
            }
            alloc.setCategoryAllocation(weightPercent);
        }
    }

    private static BigDecimal sumAllocationWeight(final List<UtCatAsetAlloc> allocList) {
        if (allocList == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (UtCatAsetAlloc alloc : allocList) {
            total = total.add(alloc.getFundAllocation().abs());
        }
        return total;
    }

}
