
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMdsbeService;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.TopAndBottomCategoryResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.TopBottomCategory;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.TopAndBottmMufCatRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.TopAndBottmMufCatResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.topandbottmmufcat.FundCategories;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.topandbottmmufcat.FundCategoriesItem;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.topandbottmmufcat.FundCategory;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;
import com.hhhh.group.secwealth.mktdata.fund.dao.TopAndBottmMufCatServiceDao;

@Service("fundTopAndBottomCategoryService")
public class TopAndBottmMufCatServiceImpl extends AbstractMdsbeService {

    public static final String TOPITEM = "TPC";

    public static final String BOTTOMITEM = "BPC";

    @Value("#{systemConfig['top.bottom.category.return.count']}")
    private Integer topBottomCategoryReturnCnt;

    @Autowired
    @Qualifier("topAndBottmMufCatServiceDao")
    private TopAndBottmMufCatServiceDao topAndBottmMufCatServiceDao;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @Override
    public Object execute(final Object object) throws Exception {

        TopAndBottmMufCatRequest request = (TopAndBottmMufCatRequest) object;

        List<String> productTypes = StringUtil.isInvalid(request.getProductType()) ? null
            : Arrays.asList(request.getProductType().split(Constants.CRITERIA_ITEM_SEPARATOR));
        List<String> productSubTypes = StringUtil.isInvalid(request.getProductSubType()) ? null
            : Arrays.asList(request.getProductSubType().split(Constants.CRITERIA_ITEM_SEPARATOR));
        List<String> countries = StringUtil.isInvalid(request.getMarket()) ? null
            : Arrays.asList(request.getMarket().split(Constants.CRITERIA_ITEM_SEPARATOR));
        String assetAllocation = request.getAssetAllocation();
        TimeScale timeScale = TimeScale.fromString(request.getTimeScale());
        String channelRestrictCode = request.getChannelRestrictCode();
        String restrOnlScribInd = request.getRestrOnlScribInd();

        int index =
            this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + request.getLocale());

        TopAndBottomCategoryResponse serviceResponse =
            this.topAndBottmMufCatServiceDao.searchTopAndBottomList(index, productTypes, productSubTypes, countries,
                assetAllocation, timeScale, this.topBottomCategoryReturnCnt, channelRestrictCode, restrOnlScribInd);

        Date lastUpdateDate =
            this.topAndBottmMufCatServiceDao.searchPerformanceTableLastUpdateDate(channelRestrictCode, restrOnlScribInd);

        // Prepare Response
        TopAndBottmMufCatResponse response = new TopAndBottmMufCatResponse();
        // set the ItemName
        FundCategories fundCategories = new FundCategories();
        List<FundCategory> categories = new ArrayList<FundCategory>();
        FundCategory topFundCategory = new FundCategory();
        topFundCategory.setItemName(TopAndBottmMufCatServiceImpl.TOPITEM);
        FundCategory bottomFundCategory = new FundCategory();
        bottomFundCategory.setItemName(TopAndBottmMufCatServiceImpl.BOTTOMITEM);

        // set the value
        List<FundCategoriesItem> topItemList = new ArrayList<FundCategoriesItem>();
        List<TopBottomCategory> topCategories = serviceResponse.getTopCategories();
        if (null != topCategories) {
            for (int i = 0; i < topCategories.size(); i++) {
                FundCategoriesItem fundCategoriesItem = new FundCategoriesItem();
                fundCategoriesItem.setCategoriesName(topCategories.get(i).getCategoryName());
                fundCategoriesItem.setCategoriesCode(topCategories.get(i).getCategoryCode());
                fundCategoriesItem.setNumberOfProducts(topCategories.get(i).getNumberOfFunds());
                fundCategoriesItem.setAverageRisk(topCategories.get(i).getAverageRisk());
                fundCategoriesItem.setTrailingTotalReturn(topCategories.get(i).getTrailingTotalReturn());
                topItemList.add(fundCategoriesItem);
            }
        }
        topFundCategory.setItems(topItemList);

        List<FundCategoriesItem> bottomItemList = new ArrayList<FundCategoriesItem>();
        List<TopBottomCategory> bottomCategories = serviceResponse.getBottomCategories();
        if (null != bottomCategories) {
            for (int i = 0; i < bottomCategories.size(); i++) {
                FundCategoriesItem fundCategoriesItem = new FundCategoriesItem();
                fundCategoriesItem.setCategoriesName(bottomCategories.get(i).getCategoryName());
                fundCategoriesItem.setCategoriesCode(bottomCategories.get(i).getCategoryCode());
                fundCategoriesItem.setNumberOfProducts(bottomCategories.get(i).getNumberOfFunds());
                fundCategoriesItem.setAverageRisk(bottomCategories.get(i).getAverageRisk());
                fundCategoriesItem.setTrailingTotalReturn(bottomCategories.get(i).getTrailingTotalReturn());
                bottomItemList.add(fundCategoriesItem);
            }
        }
        bottomFundCategory.setItems(bottomItemList);
        categories.add(topFundCategory);
        categories.add(bottomFundCategory);
        fundCategories.setCategories(categories);
        fundCategories
            .setLastUpdatedDate(DateUtil.getSimpleDateFormat(lastUpdateDate, DateConstants.DateFormat_yyyyMMdd_withHyphen));
        response.setFundCategories(fundCategories);
        return response;
    }
}