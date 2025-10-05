
package com.hhhh.group.secwealth.mktdata.fund.dao;

import com.hhhh.group.secwealth.mktdata.fund.beans.request.QuickViewRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;

import java.util.List;
import java.util.Map;


public interface QuickViewDao {

    public List<Object[]> getTopPerformanceFunds(String productType, TimeScale timeScale, String category, String productSubType,
        Integer quickviewResultLimit,final String restrOnlScribInd,final String currency,final String prodStatCde, final Map<String, String> headers) throws Exception;

    public List<Object[]> getBottomPerformanceFunds(String productType, TimeScale timeScale, String category,
        String productSubType, Integer quickviewResultLimit,final String restrOnlScribInd,final String currency,final String prodStatCde, final Map<String, String> headers) throws Exception;

    public List<Object[]> getFundOfQuarter(String productType, TimeScale timeScale, String category, String productSubType,
        Integer quickviewResultLimit, Map<String, String> headers) throws Exception;

    public List<Object[]> getFundsByFundHouse(String category, String productSubType, TimeScale timeScale,
        final String wpcCriteriaValue, final Map<String, String> headers) throws Exception;

    public Integer getFundsByFundHouseCount(String category, String productSubType, TimeScale timeScale, String wpcCriteriaValue, Map<String, String> headers)
        throws Exception;

    public Integer getTopPerformanceFundsTotalCount(String productType, TimeScale timeScale, String category, String productSubType,final String restrOnlScribInd,final String currency,final String prodStatCde, final Map<String, String> headers)
        throws Exception;

    public Integer getFundOfQuarterTotalCount(String productType, TimeScale timeScale, String category, String productSubType, Map<String, String> headers)
        throws Exception;

    public Integer getBottomPerformanceTotalCount(final String productType, final TimeScale timeScale, final String category,
        final String productSubType,final String restrOnlScribInd,final String currency,final String prodStatCde, final Map<String, String> headers) throws Exception;

    public List<Object[]> searchFundByProductKeys(List<ProductKey> productKeyList, TimeScale timeScale, Map<String,String> headers) throws Exception;

    public Integer searchFundByProductKeysCount(List<ProductKey> productKeyList, TimeScale timeScale, Map<String,String> headers) throws Exception;

    public List<Object[]> searchFundsRemoveChanlCde(List<ProductKey> productKeyList, TimeScale timeScale,
                                                    QuickViewRequest request) throws Exception;

    public List<Object[]> getTop5PerformersFunds(String productType, TimeScale timeScale, String category, String productSubType,
        String channelRestrictCode, String restrOnlScribInd, String prodStatCde, String topPerfmList,String currency,Integer quickviewResultLimit, Map<String,String> headers) throws Exception;

    public Integer getTop5PerformersFundsTotalCount(String productType, String category, String productSubType,
        String channelRestrictCode, String restrOnlScribInd, String prodStatCde, String topPerfmList,String currency, Map<String,String> headers) throws Exception;

    public List<Object[]> searchFundFromdb(TimeScale timeScale, String tableNameValue, QuickViewRequest request, final String currency, final String prodStatCde) throws Exception;
    public Integer getEsgFundCount(final TimeScale timeScale ,String restrOnlScribInd, String currency , String prodStatCde, Map<String,String> headers) throws Exception ;

    public List<Object[]> getEsgFund(final TimeScale timeScale ,String restrOnlScribInd, String currency , String prodStatCde, Map<String,String> headers, Integer... quickviewResultLimit) throws Exception ;

    public Integer getGBAFundCount(final TimeScale timeScale ,String restrOnlScribInd, String currency , String prodStatCde, Map<String,String> headers) throws Exception ;

    public List<Object[]> getGBAFund(final TimeScale timeScale ,String restrOnlScribInd, String currency , String prodStatCde, Map<String,String> headers, Integer... quickviewResultLimit) throws Exception ;

    public List<Object[]> getTopDividend(String productType, TimeScale timeScale, String category, String productSubType,
                                                 String channelRestrictCode, String restrOnlScribInd, String prodStatCde, String topPerfmList,String currency,Integer numberOfRecords,Map<String,String> headers) throws Exception;

    public Integer getTopDividendCount(String productType, TimeScale timeScale, String category, String productSubType,
                                                    String channelRestrictCode, String restrOnlScribInd, String prodStatCde, String topPerfmList,String currency,Map<String,String> headers) throws Exception;

}
