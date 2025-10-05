
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMdsbeService;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareIndexRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundCompareIndexResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompareindex.CompareIndex;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompareindex.FundCompareIndex;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundCompareIndexDao;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;


@Service("fundCompareIndexService")
public class FundCompareIndexServiceImpl extends AbstractMdsbeService {

    @Autowired
    @Qualifier("fundCompareIndexDao")
    private FundCompareIndexDao fundCompareIndexDao;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @Override
    public Object execute(final Object object) throws Exception {
        LogUtil.debug(FundCompareIndexServiceImpl.class, "Enter into the FundCompareIndexServiceImpl");

        FundCompareIndexRequest request = (FundCompareIndexRequest) object;

        List<Object[]> compareIndexList = this.fundCompareIndexDao.getCompareIndexList(request);

        FundCompareIndexResponse response = new FundCompareIndexResponse();
        if (ListUtil.isValid(compareIndexList)) {
            int index = this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT
                + request.getLocale());
            Map<String, List<CompareIndex>> map = new LinkedHashMap<String, List<CompareIndex>>();
            for (Object[] compareIndexs : compareIndexList) {
                CompareIndex compareIndex = new CompareIndex();
                compareIndex.setIndexId(MiscUtil.safeString(compareIndexs[0]));
                compareIndex.setIndexName(MiscUtil.safeString(compareIndexs[1]));
                String categoryCode = MiscUtil.safeString(compareIndexs[2]);
                String categoryName;
                if (0 == index) {
                    categoryName = MiscUtil.safeString(compareIndexs[3]);
                } else if (1 == index) {
                    categoryName = MiscUtil.safeString(compareIndexs[4]);
                } else if (2 == index) {
                    categoryName = MiscUtil.safeString(compareIndexs[5]);
                } else {
                    categoryName = MiscUtil.safeString(compareIndexs[3]);
                }
                String key = categoryCode + CommonConstants.SYMBOL_VERTICAL + categoryName;
                List<CompareIndex> items = null;
                if (map.get(key) == null) {
                    items = new ArrayList<CompareIndex>();
                } else {
                    items = map.get(key);
                }
                items.add(compareIndex);
                map.put(key, items);
            }

            List<FundCompareIndex> fundCompareIndex = new ArrayList<FundCompareIndex>();
            for (Entry<String, List<CompareIndex>> entry : map.entrySet()) {
                FundCompareIndex info = new FundCompareIndex();
                String[] keys = entry.getKey().split(CommonConstants.SYMBOL_SEPARATOR);
                info.setCategoryCode(keys[0]);
                info.setCategoryName(keys[1]);
                info.setItems(entry.getValue());
                fundCompareIndex.add(info);
            }
            response.setCompareIndex(fundCompareIndex);
        }
        return response;
    }
}