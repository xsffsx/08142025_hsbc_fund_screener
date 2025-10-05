/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.bean.DelayProdInfo;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository
public interface DelayProdInfoRepository extends JpaRepository<DelayProdInfo, Long> {

    @Query(value = "select DELAY_PROD_ID,BATCH_ID,PROD_CODE,ASET_CLASS_CDE,"
        + "MARKET_TYPE_CODE ,DELAY_TIME from DELAY_PROD_INFO where ASET_CLASS_CDE= :asetClassCde and MARKET_TYPE_CODE= :prodCode ", nativeQuery = true)
    public DelayProdInfo findDelayProd(String asetClassCde, String prodCode);
}
