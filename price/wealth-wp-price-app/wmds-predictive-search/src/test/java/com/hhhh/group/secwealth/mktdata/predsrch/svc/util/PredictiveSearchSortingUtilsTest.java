package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;


import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PredictiveSearchSortingUtilsTest {


    @Test
    public void testGetProductTypeWeightMap(){
        assertNotNull(PredictiveSearchSortingUtils.getProductTypeWeightMap("US"));
        assertNotNull(PredictiveSearchSortingUtils.getProductTypeWeightMap("HKHASE"));

    }


    @Test
    public void testGetMarketWeightMap(){
        assertNotNull(PredictiveSearchSortingUtils.getMarketWeightMap("US"));
        assertNotNull(PredictiveSearchSortingUtils.getMarketWeightMap("HKHASE"));

    }

    @Test
    public void testGetProductSubTypeWeightMap(){
        assertNotNull(PredictiveSearchSortingUtils.getProductSubTypeWeightMap("US"));
        assertNotNull(PredictiveSearchSortingUtils.getProductSubTypeWeightMap("HKHASE"));

    }


}
