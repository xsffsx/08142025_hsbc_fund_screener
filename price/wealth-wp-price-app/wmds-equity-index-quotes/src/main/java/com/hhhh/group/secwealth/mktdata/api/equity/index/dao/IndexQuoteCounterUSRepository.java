/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.dao;

import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry.IndexQuoteCounterUSPo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Repository("indexQuoteCounterUSRepository")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled", havingValue = "true")
public interface IndexQuoteCounterUSRepository extends JpaRepository<IndexQuoteCounterUSPo, Long> {

    @Transactional
    @Modifying
    @Query(value = "update IndexQuoteCounterUSPo qc  set qc.quoteCounter=qc.quoteCounter + :theQuoteCounter,qc.lastUpdate= :timestamp where qc.playerReferenceNumber= :playerReferenceNumber "
        + "and qc.tradingMarketCode= :tradingMarketCode and qc.quoteYear= :quoteYear and qc.quoteMonth= :quoteMonth and qc.quoteType= :quoteType and qc.subQuoteType= :subQuoteType")
    public int addQuoteAndUpdate(@Param("theQuoteCounter") long theQuoteCounter, @Param("timestamp") Timestamp timestamp,
                                 @Param("playerReferenceNumber") long playerReferenceNumber, @Param("tradingMarketCode") String tradingMarketCode,
                                 @Param("quoteYear") String quoteYear, @Param("quoteMonth") String quoteMonth, @Param("quoteType") String quoteType,
                                 @Param("subQuoteType") String subQuoteType);
}
