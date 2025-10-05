
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMdsbeService;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.CategoryOverviewRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.CategoryOverviewResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.categoryoverview.CategoryLevel1Summary;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.categoryoverview.CategorySummary;
import com.hhhh.group.secwealth.mktdata.fund.dao.CategoryOverviewDao;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;


@Service("categoryOverviewService")
public class CategoryOverviewServiceImpl extends AbstractMdsbeService {

    @Value("#{systemConfig['job.start.day.time']}")
    private Integer startDayTime;

    @Autowired
    @Qualifier("categoryOverviewDao")
    private CategoryOverviewDao categoryOverviewDao;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @Override
    public Object execute(final Object object) throws Exception {
        LogUtil.debug(CategoryOverviewServiceImpl.class, "Enter into the CategoryOverviewServiceImpl");
        CategoryOverviewRequest request = (CategoryOverviewRequest) object;
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String locale = request.getLocale();
        CategoryOverviewResponse response = new CategoryOverviewResponse();
        List<Object[]> utProdCatOverviewList = this.categoryOverviewDao.getCategoryOverviewList(request.getProductType(), locale,
            countryCode, groupMember, request.getChannelRestrictCode(), request.getRestrOnlScribInd());
        if (ListUtil.isValid(utProdCatOverviewList)) {
            int index = this.localeMappingUtil.getNameByIndex(countryCode + CommonConstants.SYMBOL_DOT + locale);
            Map<String, List<CategorySummary>> map = new LinkedHashMap<String, List<CategorySummary>>();
            for (Object[] utProdCatOverview : utProdCatOverviewList) {
                String categoryLevel1Code = MiscUtil.safeString(utProdCatOverview[0]);
                String categoryLevel1Name;
                if (0 == index) {
                    categoryLevel1Name = MiscUtil.safeString(utProdCatOverview[1]);
                } else if (1 == index) {
                    categoryLevel1Name = MiscUtil.safeString(utProdCatOverview[2]);
                } else if (2 == index) {
                    categoryLevel1Name = MiscUtil.safeString(utProdCatOverview[3]);
                } else {
                    categoryLevel1Name = MiscUtil.safeString(utProdCatOverview[1]);
                }
                String key = categoryLevel1Code + CommonConstants.SYMBOL_VERTICAL + categoryLevel1Name;
                List<CategorySummary> items = null;
                if (map.get(key) == null) {
                    items = new ArrayList<CategorySummary>();
                } else {
                    items = map.get(key);
                }
                CategorySummary summary = new CategorySummary();
                summary.setCategoryCode(MiscUtil.safeString(utProdCatOverview[4]));
                if (0 == index) {
                    summary.setCategoryName(MiscUtil.safeString(utProdCatOverview[5]));
                } else if (1 == index) {
                    summary.setCategoryName(MiscUtil.safeString(utProdCatOverview[6]));
                } else if (2 == index) {
                    summary.setCategoryName(MiscUtil.safeString(utProdCatOverview[7]));
                } else {
                    summary.setCategoryName(MiscUtil.safeString(utProdCatOverview[5]));
                }
                summary.setReturn1Y(MiscUtil.safeBigDecimalValue(utProdCatOverview[8]));
                summary.setReturn3Y(MiscUtil.safeBigDecimalValue(utProdCatOverview[9]));
                summary.setReturn5Y(MiscUtil.safeBigDecimalValue(utProdCatOverview[10]));
                summary.setReturn10Y(MiscUtil.safeBigDecimalValue(utProdCatOverview[11]));
                summary.setStdDev3Y(MiscUtil.safeBigDecimalValue(utProdCatOverview[12]));
                items.add(summary);
                map.put(key, items);
                String includeNull = request.getIncludeNull();
                if ((StringUtil.isInvalid(includeNull) || CommonConstants.NO.equalsIgnoreCase(includeNull)) && key.contains("null")) {
                    map.remove(key);
                }
            }

            List<CategoryLevel1Summary> categoryOverview = new ArrayList<CategoryLevel1Summary>();
            for (Map.Entry<String, List<CategorySummary>> entry : map.entrySet()) {
                CategoryLevel1Summary info = new CategoryLevel1Summary();
                String[] keys = entry.getKey().split(CommonConstants.SYMBOL_SEPARATOR);
                info.setCategoryLevel1Code(keys[0]);
                info.setCategoryLevel1Name(keys[1]);
                info.setItems(entry.getValue());
                categoryOverview.add(info);
            }
            response.setCategoryOver(categoryOverview);
            response.setLastUpdatedDate(getLastUpdatedDate());
        } else {
            LogUtil.errorBeanToJson(CategoryOverviewServiceImpl.class,
                "Can not get record from DB, No record found for CategoryOverview|request=", request);
        }
        return response;
    }

    private String getLastUpdatedDate() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) >= this.startDayTime) {
            calendar.add(Calendar.MONTH, -1);
        } else {
            calendar.add(Calendar.MONTH, -2);
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat(DateConstants.DateFormat_yyyyMMdd_withHyphen).format(calendar.getTime());
    }
}