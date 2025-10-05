
package com.hhhh.group.secwealth.mktdata.fund.dao.common;

import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.util.*;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.dao.impl.FundSearchResultDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("fundCommonDao")
public abstract class FundCommonDao {

    private static final String OR_MAP_IDLIST = "IdListMap";
    private static final String OR_MAP_SUBHQL = "SubHQL";

    @Autowired
    @Qualifier("localeMappingUtil")
    protected LocaleMappingUtil localeMappingUtil;

    @Autowired
    @Qualifier("baseDao")
    private BaseDao baseDao;

    protected Query createQueryForHql(final String hql) throws Exception {
        EntityManager entityManager = this.baseDao.getEntityManager();
        Query query = entityManager.createQuery(hql.toString());
        return query;
    }

    protected Query createQueryForNativeSql(final String nativeSql) throws Exception {
        EntityManager entityManager = this.baseDao.getEntityManager();
        Query query = entityManager.createNativeQuery(nativeSql.toString());
        return query;
    }

    // get prodIds from online main table by productKeys
    protected List<Integer> getProdIdByProductKeys(final List<ProductKey> productKeys, final Map<String,String> headers) throws Exception {
        List<Integer> prodIds_productKey = new ArrayList<Integer>();
        if (ListUtil.isValid(productKeys)) {
            StringBuilder hql = new StringBuilder();
            hql.append("select u.utProdInstmId.productId from UtProdInstm u");
            for (int i = 0; i < productKeys.size(); i++) {
                if (i == 0) {
                    hql.append(" where (u.market= :market" + i);
                } else {
                    hql.append(" or (u.market= :market" + i);
                }
                hql.append(" and u.productType = :productType" + i);
                hql.append(" and u.symbol = :prodAltNum" + i);
                hql.append(" )");
            }

            QueryUtil.buildCmbSearchHql(hql,headers,"u");

            LogUtil.debug(FundSearchResultDaoImpl.class, "create query for getProdIdByProductKeys is : {}", hql.toString());
            EntityManager entityManager = this.baseDao.getEntityManager();
            Query query = entityManager.createQuery(hql.toString());
            for (int i = 0; i < productKeys.size(); i++) {
                query.setParameter("market" + i, productKeys.get(i).getMarket());
                query.setParameter("productType" + i, productKeys.get(i).getProductType());
                query.setParameter("prodAltNum" + i, productKeys.get(i).getProdAltNum());
            }
            List<Integer> idList = query.getResultList();
            LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "result for getProdIdByProductKeys from DB is ", idList);
            return idList;
        }
        return prodIds_productKey;
    }

    // get peformanceIds from online main table by productKeys
    protected List<String> getPerformanceIdByProductKeys(final List<ProductKey> productKeys, final Map<String,String> headers) throws Exception {
        List<String> performanceIds_productKey = new ArrayList<String>();
        if (ListUtil.isValid(productKeys)) {
            StringBuilder hql = new StringBuilder();
            hql.append("select u.performanceId from UtProdInstm u");
            for (int i = 0; i < productKeys.size(); i++) {
                if (i == 0) {
                    hql.append(" where (u.market= :market" + i);
                } else {
                    hql.append(" or (u.market= :market" + i);
                }
                hql.append(" and u.productType = :productType" + i);
                hql.append(" and u.symbol = :prodAltNum" + i);
                hql.append(" )");
            }

            QueryUtil.buildCmbSearchHql(hql,headers,"u");

            LogUtil.debug(FundSearchResultDaoImpl.class, "create query for getProdIdByProductKeys is : {}", hql.toString());
            EntityManager entityManager = this.baseDao.getEntityManager();
            Query query = entityManager.createQuery(hql.toString());
            for (int i = 0; i < productKeys.size(); i++) {
                query.setParameter("market" + i, productKeys.get(i).getMarket());
                query.setParameter("productType" + i, productKeys.get(i).getProductType());
                query.setParameter("prodAltNum" + i, productKeys.get(i).getProdAltNum());
            }
            List<String> idList = query.getResultList();
            LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "result for getProdIdByProductKeys from DB is ", idList);
            return idList;
        }
        return performanceIds_productKey;
    }

    // generate the hql by list<?>
    protected Map<String, Object> generateHqlOR(final List<?> idList, final String colAlias, final String paramKey) {
        String subHql = "";
        String mapKey = "";
        List<Object> subIdList = new ArrayList<Object>();
        Map<String, List<Object>> idsMap = new HashMap<String, List<Object>>();
        for (int i = 0; i < idList.size(); i++) {
            subIdList.add(idList.get(i));
            if ((subIdList.size() % 1000 == 0) || (i == (idList.size() - 1))) {
                if (StringUtil.isValid(subHql)) {
                    subHql += " or ";
                }
                mapKey = (paramKey + i);
                subHql += (colAlias + " in (:" + mapKey + ") ");
                idsMap.put(mapKey, subIdList);
                subIdList = new ArrayList<Object>();
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(FundCommonDao.OR_MAP_IDLIST, idsMap);
        result.put(FundCommonDao.OR_MAP_SUBHQL, subHql);
        return result;
    }

    // connCondition -- where and
    protected void appendSubHql(final StringBuilder hql, final String connCondition, final Map<String, Object> orMap) {
        Object subHql = orMap.get(FundCommonDao.OR_MAP_SUBHQL);
        if (null != subHql) {
            String str = "";
            if (!"".equals(subHql)) {
                // str += (" "+connCondition+" " + subHql);
                str += (" " + connCondition + " (" + subHql + ")");
            }
            hql.append(str);
        }
    }

    // set quoery param
    @SuppressWarnings("unchecked")
    protected void setQueryParamKey(final Query query, final Map<String, Object> orMap) {
        if (null != orMap && orMap.size() > 0) {
            Map<String, List<Object>> codesMap = (Map<String, List<Object>>) orMap.get(FundCommonDao.OR_MAP_IDLIST);
            if (null != codesMap && codesMap.size() > 0) {
                for (String parameterKey : codesMap.keySet()) {
                    query.setParameter(parameterKey, codesMap.get(parameterKey));
                }
            }
        }
    }

    // AMH UTB - remove restrict products(use restrOnlScribInd)
    public List<Integer> getRestrOnlScribIds(final String restrOnlScribInd) throws Exception {
        List<Integer> restrOnlScribIds = null;
        if (StringUtil.isValid(restrOnlScribInd)) {
            StringBuilder restrictHql = new StringBuilder(
                "select distinct(u.utProdInstmId.productId) from UtProdInstm u where u.restrOnlScribInd = :restrOnlScribInd");
            Query query = createQueryForHql(restrictHql.toString());
            query.setParameter("restrOnlScribInd", restrOnlScribInd);
            restrOnlScribIds = query.getResultList();
        }
        return restrOnlScribIds;
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, List<String>> searchChanlFunds(final String chanlRestCde) throws Exception {
        LogUtil.debug(FundCommonDao.class, "Enter into the search channl fund");

        StringBuilder hql = new StringBuilder(
            "select distinct chanl.utProdChanlId.prodId, chanl.allowBuyProdInd, chanl.allowSellProdInd, chanl.allowSwitchOut, chanl.allowSwitchIn from UtProdChanl chanl where chanl.channelCde = :channelCde");
        Query query = createQueryForHql(hql.toString());
        query.setParameter("channelCde", chanlRestCde);
        List<Object[]> chanlProductList = query.getResultList();

        Map<Integer, List<String>> chanlMap = new HashMap<Integer, List<String>>();
        for (Object[] chanl : chanlProductList) {
            List<String> indicate = new ArrayList<String>();
            if (null != chanl[0] && null != chanl[1] && null != chanl[2]
                    && null != chanl[3] && null != chanl[4]) {
                indicate.add(chanl[1].toString());
                indicate.add(chanl[2].toString());
                indicate.add(chanl[3].toString());
                indicate.add(chanl[4].toString());
                chanlMap.put(Integer.parseInt(chanl[0].toString()), indicate);
            }
        }
        return chanlMap;
    }

    protected String getReturnFieldName(final TimeScale timeScale) {
        switch (timeScale) {
        case ONE_MONTH:
            return "M1";
        case THREE_MONTH:
            return "M3";
        case SIX_MONTH:
            return "M6";
        case ONE_YEAR:
            return "M12";
        case THREE_YEAR:
            return "M36";
        case FIVE_YEAR:
            return "M60";
        case TEN_YEAR:
            return "M120";
        case YTD:
            return "returnytd";
        case QTD:
            return "returnqtd";
        case MTD:
            return "returnmtd";
        default:
            return null;
        }
    }

}
