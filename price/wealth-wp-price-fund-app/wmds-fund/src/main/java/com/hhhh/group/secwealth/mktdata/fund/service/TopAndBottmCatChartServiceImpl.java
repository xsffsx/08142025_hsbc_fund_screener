
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMdsbeService;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.CategoryLine;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.TopAndBottomCategoryChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.TopAndBottmCatChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.TopAndBottmCatChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.topandbottmcatchart.CategoriesChartData;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.topandbottmcatchart.CategoriesList;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;
import com.hhhh.group.secwealth.mktdata.fund.dao.TopAndBottmCatChartServiceDao;
import com.hhhh.group.secwealth.mktdata.fund.util.FeConvertorHelper;

@Service("fundTopAndBottomCatChartService")
public class TopAndBottmCatChartServiceImpl extends AbstractMdsbeService {

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @Autowired
    @Qualifier("topAndBottmCatChartServiceDao")
    private TopAndBottmCatChartServiceDao topAndBottmCatChartServiceDao;

    @Value("#{systemConfig['top.bottom.category.return.count']}")
    private Integer topBottomCategoryReturnCnt;

    @Override
    public Object execute(final Object object) throws Exception {

        TopAndBottmCatChartRequest request = (TopAndBottmCatChartRequest) object;

        List<String> productTypes = StringUtil.isInvalid(request.getProductType()) ? null
            : Arrays.asList(request.getProductType().split(Constants.CRITERIA_ITEM_SEPARATOR));
        List<String> productSubTypes = StringUtil.isInvalid(request.getProductSubType()) ? null
            : Arrays.asList(request.getProductSubType().split(Constants.CRITERIA_ITEM_SEPARATOR));
        List<String> countries = StringUtil.isInvalid(request.getMarket()) ? null
            : Arrays.asList(request.getMarket().split(Constants.CRITERIA_ITEM_SEPARATOR));
        List<String> categoryCodeList = StringUtil.isInvalid(request.getCategoryCode()) ? null
            : Arrays.asList(request.getCategoryCode().split(Constants.CRITERIA_ITEM_SEPARATOR));
        TimeScale timeScale = TimeScale.fromString(request.getMarketPeriod());
        int index =
            this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + request.getLocale());

        TopAndBottomCategoryChartResponse chartResponse = this.topAndBottmCatChartServiceDao.searchUtProdCatList(index,
            productTypes, productSubTypes, countries, categoryCodeList, timeScale, this.topBottomCategoryReturnCnt);

        // Prepare response
        TopAndBottmCatChartResponse response = new TopAndBottmCatChartResponse();
        List<CategoriesList> categories = new ArrayList<CategoriesList>();
        List<CategoriesChartData> topBottomChartData = new ArrayList<CategoriesChartData>();
        List<CategoryLine> chartList = chartResponse.getCategoryLines();
        Set<Date> dates = chartResponse.getDates();
        List<String> dateList = new ArrayList<String>();
        if (null != dates && dates.size() > 0) {
            for (Date date : dates) {
                dateList.add(FeConvertorHelper.dateToString(date));
            }
        }
        // Get the data
        if (ListUtil.isValid(chartList)) {
            List<List<BigDecimal>> allDataPoint = new ArrayList<List<BigDecimal>>();
            for (int i = 0; i < chartList.size(); i++) {
                CategoriesList categoriesList = new CategoriesList();
                categoriesList.setCategoryName(chartList.get(i).getCategoryName());
                categoriesList.setCategoryCode(chartList.get(i).getCategoryCode());
                categoriesList.setChartId(new StringBuilder("key").append(i + 1).toString());
                categories.add(categoriesList);
                // Set all data in one list
                List<BigDecimal> dataList = chartList.get(i).getDataPoints();
                allDataPoint.add(dataList);

            }

            for (int i = 0; i < dateList.size(); i++) {
                CategoriesChartData categoriesChartData = new CategoriesChartData();
                categoriesChartData.setPeriod(dateList.get(i));
                topBottomChartData.add(categoriesChartData);
            }
            for (int i = 0; i < allDataPoint.size(); i++) {
                List<BigDecimal> temp = allDataPoint.get(i);
                if (i + 1 == 1) {
                    for (int j = 0; j < temp.size(); j++) {
                        if (j < topBottomChartData.size() && topBottomChartData.get(j) != null && temp.get(j) != null) {
                            topBottomChartData.get(j).setKey1(temp.get(j).doubleValue());
                        }
                    }
                }
                if (i + 1 == 2) {
                    for (int j = 0; j < temp.size(); j++) {
                        if (j < topBottomChartData.size() && topBottomChartData.get(j) != null && temp.get(j) != null) {
                            topBottomChartData.get(j).setKey2(temp.get(j).doubleValue());
                        }
                    }
                }
                if (i + 1 == 3) {
                    for (int j = 0; j < temp.size(); j++) {
                        if (j < topBottomChartData.size() && topBottomChartData.get(j) != null && temp.get(j) != null) {
                            topBottomChartData.get(j).setKey3(temp.get(j).doubleValue());
                        }
                    }
                }
                if (i + 1 == 4) {
                    for (int j = 0; j < temp.size(); j++) {
                        if (j < topBottomChartData.size() && topBottomChartData.get(j) != null && temp.get(j) != null) {
                            topBottomChartData.get(j).setKey4(temp.get(j).doubleValue());
                        }
                    }
                }
                if (i + 1 == 5) {
                    for (int j = 0; j < temp.size(); j++) {
                        if (j < topBottomChartData.size() && topBottomChartData.get(j) != null && temp.get(j) != null) {
                            topBottomChartData.get(j).setKey5(temp.get(j).doubleValue());
                        }
                    }
                }
                if (i + 1 == 6) {
                    for (int j = 0; j < temp.size(); j++) {
                        if (j < topBottomChartData.size() && topBottomChartData.get(j) != null && temp.get(j) != null) {
                            topBottomChartData.get(j).setKey6(temp.get(j).doubleValue());
                        }
                    }
                }
                if (i + 1 == 7) {
                    for (int j = 0; j < temp.size(); j++) {
                        if (j < topBottomChartData.size() && topBottomChartData.get(j) != null && temp.get(j) != null) {
                            topBottomChartData.get(j).setKey7(temp.get(j).doubleValue());
                        }
                    }
                }
                if (i + 1 == 8) {
                    for (int j = 0; j < temp.size(); j++) {
                        if (j < topBottomChartData.size() && topBottomChartData.get(j) != null && temp.get(j) != null) {
                            topBottomChartData.get(j).setKey8(temp.get(j).doubleValue());
                        }
                    }
                }
                if (i + 1 == 9) {
                    for (int j = 0; j < temp.size(); j++) {
                        if (j < topBottomChartData.size() && topBottomChartData.get(j) != null && temp.get(j) != null) {
                            topBottomChartData.get(j).setKey9(temp.get(j).doubleValue());
                        }
                    }
                }
                if (i + 1 == 10) {
                    for (int j = 0; j < temp.size(); j++) {
                        if (j < topBottomChartData.size() && topBottomChartData.get(j) != null && temp.get(j) != null) {
                            topBottomChartData.get(j).setKey10(temp.get(j).doubleValue());
                        }
                    }
                }
            }
        }
        response.setMarketPeriod(request.getMarketPeriod());
        response.setCategories(categories);
        response.setTopBottomChartData(topBottomChartData);

        return response;
    }
}