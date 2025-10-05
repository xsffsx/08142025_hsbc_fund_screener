/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.bean.ExchHlday;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository
public interface ExchHldayRepository extends JpaRepository<ExchHlday, Long> {
    @Query(value = " select EXCH_HLDAY_ID,HOLIDAY,MARKET_TYPE_CODE,HOLIDAY_DESC,OPEN_TIME,CLOSE_TIME from EXCH_HLDAY holiday "
        + "join TRD_SESSN_INFO sessn on holiday.TRD_SESSN_ID = sessn.trd_sessn_id and holiday.holiday= :currentDay and holiday.market_type_code= :marketTypeCode", nativeQuery = true)
    public List<ExchHlday> findHoliday(@Param("currentDay") String currentDay, @Param("marketTypeCode") String marketTypeCode);
}
