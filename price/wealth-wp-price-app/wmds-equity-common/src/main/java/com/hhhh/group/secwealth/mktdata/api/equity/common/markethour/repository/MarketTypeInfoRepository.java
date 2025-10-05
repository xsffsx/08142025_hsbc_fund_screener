/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.bean.MarketTypeInfo;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository
public interface MarketTypeInfoRepository extends JpaRepository<MarketTypeInfo, Long> {
    @Query(value = " select info from MarketTypeInfo info where MARKET_TYPE_CODE = :marketTypeCode ")
    public MarketTypeInfo getExchTimeZone(@Param("marketTypeCode") String marketTypeCode);
}
