/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteMeter;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteMeterId;


/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository
public interface QuoteMeterRepository extends JpaRepository<QuoteMeter, QuoteMeterId> {


    @Query(value = "select q.CUST_SCRIB_ID,q.MKT_PRD_EXCH_ID, q.QUOT_CNT,q.QUOT_USE_CNT, q.UNLMT_QUOT_IND,q.QUOT_DLAY_CNT,q.QUOT_DLAY_USE_CNT,q.UNLMT_DLAY_QUOT_IND,q.USER_UPDT_NUM, q.REC_UPDT_DT_TM FROM QUOT_MTR q inner join MKT_PRD_EXCH_INFO e  on e.MKT_PRD_EXCH_ID = q.MKT_PRD_EXCH_ID WHERE q.CUST_SCRIB_ID = :customer and TRIM(e.MKT_PRD_EXCH_CDE) =:exchangeCode", nativeQuery = true)
    public QuoteMeter fetchQuometerByCustomer(@Param("customer") String customer, @Param("exchangeCode") String exchangeCode);

    @Query(" Select e.exchangeCode,q.realTimeQuota,q.usedRealTimeQuote, q.realQuotaUnlimited,q.delayedQuota,q.usedDelayedQuote,q.delayedQuotaUnlimited from ExchangeInfo e, QuoteMeter q where  e.id = q.exchangeId and q.subscriberId = :customer and e.exchangeCode = :exchangeCode order by e.exchangeCode")
    public List<Object[]> getQuotaByExchangeID(@Param("customer") String customer, @Param("exchangeCode") String exchangeCode);
}