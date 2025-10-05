/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean.ExchangeOpenTime;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository
public interface ExchangeOpenTimeRepository extends JpaRepository<ExchangeOpenTime, Long> {
    @Query(value = "select rownum as RN , trd.TRD_SESS_ID as TRD_SESS_ID,trd.TRD_DT,HR_SESS_START_TM_NUM,MINUT_SESS_START_TM_NUM,"
        + "SECND_SESS_START_TM_NUM,HR_SESS_CLOSE_TM_NUM,MINUT_SESS_CLOSE_TM_NUM," + "SECND_SESS_CLOSE_TM_NUM,UTC_OFST_NUM "
        + "from (select trd_sess_id,to_char(trd_dt,'yyyy-MM-dd') as trd_dt from EXCH_SESS_MAP where to_char(trd_dt,'yyyy-MM-dd') "
        + "in :currentDate and mkt_prd_exch_id in (select mkt_prd_exch_id from MKT_PRD_EXCH_INFO where mkt_prd_exch_cde = :exchangeCode group by mkt_prd_exch_id) "
        + "group by trd_sess_id,TRD_DT) trd left join TRD_SESS_INFO info "
        + "on trd.trd_sess_id = info.trd_sess_id", nativeQuery = true)
    List<ExchangeOpenTime> findExchangeOpenTime(@Param("exchangeCode") String exchangeCode,
        @Param("currentDate") List<String> currentDate);
}
