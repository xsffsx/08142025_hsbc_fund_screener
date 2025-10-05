
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.criteria.ListValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.MinMaxValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchListCriteria;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchListCriteriaItem;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchMinMaxCriteria;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.fund.AbstractMdsbeService;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchCriteriaRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundSearchCriteriaResponse;
import com.hhhh.group.secwealth.mktdata.fund.converter.BaseValidateConverter;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundSearchCriteriaDao;


@Service("fundSearchCriteriaService")
public class FundSearchCriteriaServiceImpl extends AbstractMdsbeService {

    public static final String PARENT_KEY = "_Parent";

    @Resource(name = "fundSearchCriteria")
    private Map<String, String> fundSearchCriteria;

    @Autowired
    @Qualifier("fundSearchCriteriaDao")
    private FundSearchCriteriaDao fundSearchCriteriaDao;

    @Autowired
    @Qualifier("baseValidateConverter")
    private BaseValidateConverter baseValidateConverter;

    @Override
    public Object execute(final Object object) throws Exception {
        long startTime0 = System.currentTimeMillis();
        // Prepare Request
        FundSearchCriteriaRequest request = (FundSearchCriteriaRequest) object;

        String locale = request.getLocale();
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();

        this.baseValidateConverter.validateForSearchCriteria(request.getPredefinedCriterias(), countryCode, groupMember);

        List<MinMaxValue> minMaxCriteriasList = null;
        List<ListValue> listCriteriasList = null;

        String minMaxKeys = Arrays.toString(request.getMinMaxCriteriaKeys()).replace("[", "").replace("]", "");
        String listKeys = Arrays.toString(request.getListCriteriaKeys()).replace("[", "").replace("]", "");

        if (minMaxKeys == null && listKeys == null) {
            LogUtil.error(FundSearchCriteriaServiceImpl.class, "MinMax keys and list keys can not be both null.");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }

        // get minMaxCriteria result from DB
        if (minMaxKeys != null) {
            if (Constants.ALL.equalsIgnoreCase(minMaxKeys)) {
                minMaxKeys = this.fundSearchCriteria.get("allMinMaxCriteriaKeys");
            }
            long startTime1 = System.currentTimeMillis();
            minMaxCriteriasList = this.fundSearchCriteriaDao.searchMinMaxCriteria(request,
                minMaxKeys, countryCode, groupMember);
            long endTime1 = System.currentTimeMillis();
            LogUtil.debug(FundSearchCriteriaServiceImpl.class,
                "[SearchCriteria] minMaxKeys call DB cost Time : " + Long.toString(endTime1 - startTime1));
        }

        // get listCriterias result from DB
        if (listKeys != null) {
            if (Constants.ALL.equalsIgnoreCase(listKeys)) {
                listKeys = this.fundSearchCriteria.get("allListCriteriaKeys");
            }
            long startTime2 = System.currentTimeMillis();
            listCriteriasList = this.fundSearchCriteriaDao.searchListCriteria(request, listKeys, locale, countryCode, groupMember);
            long endTime2 = System.currentTimeMillis();
            LogUtil.debug(FundSearchCriteriaServiceImpl.class,
                "[SearchCriteria] ListCriteria call DB cost Time : " + Long.toString(endTime2 - startTime2));
        }

        FundSearchCriteriaResponse response = new FundSearchCriteriaResponse();

        // prepare min max criteria response
        if (ListUtil.isValid(minMaxCriteriasList)) {
            List<SearchMinMaxCriteria> searchMinMaxCriterias = new ArrayList<SearchMinMaxCriteria>();
            for (MinMaxValue minMaxCriteria : minMaxCriteriasList) {
                SearchMinMaxCriteria searchMinMaxCriteria = new SearchMinMaxCriteria();
                searchMinMaxCriteria.setCriteriaKey(minMaxCriteria.getCriteriaKey());
                searchMinMaxCriteria.setMaximum(minMaxCriteria.getMaxCriteria());
                searchMinMaxCriteria.setMinimum(minMaxCriteria.getMinCriteria());
                searchMinMaxCriterias.add(searchMinMaxCriteria);
            }
            response.setMinMaxCriterias(searchMinMaxCriterias);
        }

        // prepare list criteria response
        if (ListUtil.isValid(listCriteriasList)) {
            List<SearchListCriteria> searchListCriterias = new ArrayList<SearchListCriteria>();
            for (ListValue listCriteria : listCriteriasList) {
                SearchListCriteria searchListCriteria = new SearchListCriteria();
                searchListCriteria.setCriteriaKey(listCriteria.getCriteriaKey());
                Map<Object, Object> items = listCriteria.getMapItems();
                if (null != items) {
                    List<SearchListCriteriaItem> itemList = new ArrayList<SearchListCriteriaItem>();
                    for (Entry<Object, Object> entry : items.entrySet()) {
                        Object key = entry.getKey();
                        String parentKey = key + FundSearchCriteriaServiceImpl.PARENT_KEY;
                        if (null != key && key.toString().indexOf(FundSearchCriteriaServiceImpl.PARENT_KEY) == -1) {
                            SearchListCriteriaItem item = new SearchListCriteriaItem();
                            item.setItemKey(entry.getKey());
                            item.setItemValue(entry.getValue());
                            if (items.containsKey(parentKey)) {
                                item.setParent(items.get(parentKey));
                            }
                            itemList.add(item);
                        }
                    }
                    searchListCriteria.setItems(itemList);
                    searchListCriterias.add(searchListCriteria);
                }
            }
            response.setListCriterias(searchListCriterias);
        }
        long endTime0 = System.currentTimeMillis();
        LogUtil.debug(FundSearchCriteriaServiceImpl.class,
            "[SearchCriteria] call DB total cost Time : " + Long.toString(endTime0 - startTime0));
        return response;
    }

}
