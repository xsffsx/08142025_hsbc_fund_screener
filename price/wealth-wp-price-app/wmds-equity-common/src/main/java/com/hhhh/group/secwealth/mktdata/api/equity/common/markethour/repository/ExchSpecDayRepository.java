/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.bean.ExchSpecDay;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository
public interface ExchSpecDayRepository extends JpaRepository<ExchSpecDay, Long> {
    @Query(value = "select EXCH_SPEC_ID,SPEC_DAY,MARKET_TYPE_CODE,SPEC_DAY_DESC,OPEN_TIME,CLOSE_TIME from EXCH_SPEC_DAY spec join TRD_SESSN_INFO sessn "
        + "on spec.TRD_SESSN_ID = sessn.trd_sessn_id and spec.spec_day = :currentDay and spec.market_type_code = :marketTypeCode ", nativeQuery = true)
    public List<ExchSpecDay> findSpecDay(@Param("currentDay") String currentDay, @Param("marketTypeCode") String marketTypeCode);
}
