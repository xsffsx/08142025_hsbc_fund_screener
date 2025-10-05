/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteHistory;


/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
public interface QuoteHistoryRepository extends JpaRepository<QuoteHistory, Long> {

    @Query(value = "select QUOT_HIST_BATCHSEQ.nextval from dual", nativeQuery = true)
    Long querySeqNum();

    @Query(value = "select 1 from dual", nativeQuery = true)
    Integer dbCheck();

}
