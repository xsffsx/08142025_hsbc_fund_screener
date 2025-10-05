/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.bean.ExchTradHour;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository
public interface ExchTradHourRepository extends JpaRepository<ExchTradHour, Long> {
    @Query(value = "select SESSN_MAP_ID , MARKET_TYPE_CODE , OPEN_TIME,CLOSE_TIME,DAY_OF_WEEK from TRD_SESSN_MAP sessn "
        + " join TRD_SESSN_INFO trd on sessn.trd_sessn_id = trd.trd_sessn_id "
        + " and sessn.market_type_code = :marketTypeCode and trd.day_of_week= :dayOfWeek", nativeQuery = true)
    public List<ExchTradHour> findTradHour(@Param("marketTypeCode") String marketTypeCode, @Param("dayOfWeek") int dayOfWeek);
}
