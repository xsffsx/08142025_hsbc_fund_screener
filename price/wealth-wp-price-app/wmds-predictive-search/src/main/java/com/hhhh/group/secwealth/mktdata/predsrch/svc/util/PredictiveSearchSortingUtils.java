/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

/**
 * <p>
 * <b> The Predictive Search Sorting Utility Class. </b>
 * </p>
 */
public class PredictiveSearchSortingUtils {

    private static final String DEFAULT_SITE = "defaultSite";

    /**
     * @return the productTypeWeightMap
     */
    public static Map<String, Double> getProductTypeWeightMap(final String site) {
        SortingManager sortingManager = SortingManager.getInstance();
        if (sortingManager.getProductTypeWeightSiteMap().containsKey(site)) {
            return sortingManager.getProductTypeWeightSiteMap().get(site);
        }
        return sortingManager.getProductTypeWeightSiteMap().get(PredictiveSearchSortingUtils.DEFAULT_SITE);
    }

    /**
     * @return the marketWeightMap
     */
    public static Map<String, Integer> getMarketWeightMap(final String site) {
        SortingManager sortingManager = SortingManager.getInstance();
        if (sortingManager.getMarketWeightSiteMap().containsKey(site)) {
            return sortingManager.getMarketWeightSiteMap().get(site);
        }
        return sortingManager.getMarketWeightSiteMap().get(PredictiveSearchSortingUtils.DEFAULT_SITE);
    }

    /**
     * @return the productSubTypeWeightMap
     */
    public static Map<String, Map<String, Double>> getProductSubTypeWeightMap(final String site) {
        SortingManager sortingManager = SortingManager.getInstance();
        if (sortingManager.getProductSubTypeWeightSiteMap().containsKey(site)) {
            return sortingManager.getProductSubTypeWeightSiteMap().get(site);
        }
        return sortingManager.getProductSubTypeWeightSiteMap().get(PredictiveSearchSortingUtils.DEFAULT_SITE);
    }

    static class SortingManager {
        private Map<String, Map<String, Double>> productTypeWeightSiteMap = new ConcurrentHashMap<String, Map<String, Double>>();

        private Map<String, Map<String, Integer>> marketWeightSiteMap = new ConcurrentHashMap<String, Map<String, Integer>>();

        private Map<String, Map<String, Map<String, Double>>> productSubTypeWeightSiteMap =
            new ConcurrentHashMap<String, Map<String, Map<String, Double>>>();

        private static final String PRODUCT_TYPE_SORTING_ORDER = "productTypeSortingOrder";

        private static final String COUNTRY_TRADABLE_CODE_SORTING_ORDER = "countryTradableCodeSortingOrder";

        private static final String PRODUCT_SUB_TYPE_SORTING_ORDER = "productSubTypeSortingOrder";

        private static final String CONFIG_FILE_PATH = "system/predsrch/predsrchSortingOrdersBySite.properties";

        private static final SortingManager instance = new SortingManager();

        // private static Object locker = new Object();

        public static SortingManager getInstance() {
            // if (SortingManager.instance == null) {
            // synchronized (SortingManager.locker) {
            // if (SortingManager.instance == null) {
            // SortingManager.instance = new SortingManager();
            // }
            // }
            // }
            return SortingManager.instance;
        }

        private SortingManager() {
            Properties properties = new Properties();
            try {
                properties.load(this.getClass().getClassLoader().getResourceAsStream(SortingManager.CONFIG_FILE_PATH));
                for (Object obj : properties.keySet()) {
                    String siteSortTypeStr = obj.toString();
                    String[] siteSortTypes = siteSortTypeStr.split(CommonConstants.SYMBOL_DOT_SEPARATOR);
                    String site = siteSortTypes[0];
                    String sortType = siteSortTypes[1];
                    if (SortingManager.PRODUCT_TYPE_SORTING_ORDER.equals(sortType)) {
                        String productTypeSortingOrderStr = properties.getProperty(siteSortTypeStr);
                        String[] productTypeSortingOrder = productTypeSortingOrderStr.split(CommonConstants.SYMBOL_COMMA);
                        Map<String, Double> productTypeWeightMap = new ConcurrentHashMap<String, Double>();
                        if (productTypeSortingOrder != null) {
                            for (int i = 0; i < productTypeSortingOrder.length; i++) {
                                productTypeWeightMap.put(productTypeSortingOrder[i], (double) (i + 1));
                            }
                        }
                        this.productTypeWeightSiteMap.put(site, productTypeWeightMap);
                    }
                    if (SortingManager.PRODUCT_SUB_TYPE_SORTING_ORDER.equals(sortType)) {
                        String productSubTypeSortingOrderStr = properties.getProperty(siteSortTypeStr);
                        String[] productSubTypeSortingOrder = productSubTypeSortingOrderStr.split(CommonConstants.SYMBOL_COMMA);
                        Map<String, Map<String, Double>> productSubTypeInProductTypeWeightMap =
                            new ConcurrentHashMap<String, Map<String, Double>>();
                        Map<String, Double> productSubTypeWeightMap = new ConcurrentHashMap<String, Double>();
                        if (productSubTypeSortingOrder != null && productSubTypeSortingOrder.length >= 2) {
                            String productType = productSubTypeSortingOrder[0];
                            String[] productSubType = productSubTypeSortingOrder[1].split(CommonConstants.SYMBOL_SEPARATOR);
                            if (productSubType != null) {
                                for (int i = 0; i < productSubType.length; i++) {
                                    String[] subTypes = productSubType[i].split(CommonConstants.SYMBOL_COLON);
                                    for (String subType : subTypes) {
                                        productSubTypeWeightMap.put(subType, (double) (i + 1));
                                    }
                                }
                            }
                            productSubTypeInProductTypeWeightMap.put(productType, productSubTypeWeightMap);
                        }
                        this.productSubTypeWeightSiteMap.put(site, productSubTypeInProductTypeWeightMap);
                    }
                    if (SortingManager.COUNTRY_TRADABLE_CODE_SORTING_ORDER.equals(sortType)) {
                        String countryTradableCodeSortingOrderStr = properties.getProperty(siteSortTypeStr);
                        String[] countryTradableCodeSortingOrder =
                            countryTradableCodeSortingOrderStr.split(CommonConstants.SYMBOL_COMMA);
                        Map<String, Integer> marketWeightMap = new ConcurrentHashMap<String, Integer>();
                        if (countryTradableCodeSortingOrder != null) {
                            for (int i = 0; i < countryTradableCodeSortingOrder.length; i++) {
                                marketWeightMap.put(countryTradableCodeSortingOrder[i], i + 1);
                            }
                        }
                        this.marketWeightSiteMap.put(site, marketWeightMap);
                    }
                }
            } catch (IOException e) {
                LogUtil.error(PredictiveSearchSortingUtils.class, "PredictiveSearchSortingUtils error: ", e);
            }
        }

        /**
         * @return the productTypeWeightSiteMap
         */
        public Map<String, Map<String, Double>> getProductTypeWeightSiteMap() {
            return this.productTypeWeightSiteMap;
        }

        /**
         * @return the marketWeightSiteMap
         */
        public Map<String, Map<String, Integer>> getMarketWeightSiteMap() {
            return this.marketWeightSiteMap;
        }

        /**
         * @return the productSubTypeWeightSiteMap
         */
        public Map<String, Map<String, Map<String, Double>>> getProductSubTypeWeightSiteMap() {
            return this.productSubTypeWeightSiteMap;
        }
    }

}
