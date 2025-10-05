/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.repository;


import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean.ExchAgreeMap;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository
public interface ExchangeAgreementRepository extends JpaRepository<ExchAgreeMap, Long> {
    @Query(value = "select count(1) from mkt_agree mkt join EXCH_AGREE_MAP m on mkt.doc_id = m.doc_id and "
        + "mkt.cust_scrib_id = :subscriberId and m.mkt_prd_exch_id in (select mkt_prd_exch_id from MKT_PRD_EXCH_INFO where mkt_prd_exch_cde = :exchangeCode group by mkt_prd_exch_id) and agree_stat_cde = 'Y' and "
        + " AGREE_EXPIR_DT_TM >= :currentDate", nativeQuery = true)
    Integer hasEffectiveAgreement(@Param("subscriberId") String subscriberId, @Param("exchangeCode") String exchangeCode,
        @Param("currentDate") Date currentDate);
}
