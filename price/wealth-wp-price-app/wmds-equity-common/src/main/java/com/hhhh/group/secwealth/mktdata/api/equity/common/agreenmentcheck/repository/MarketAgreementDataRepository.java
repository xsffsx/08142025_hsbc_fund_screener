/*
 *
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean.MarketAgreement;


/**
 * <p>
 * <b> Market Agreement Repository </b>
 * </p>
 */
@Repository
public interface MarketAgreementDataRepository extends JpaRepository<MarketAgreement, Long> {

    @Query("SELECT m FROM MarketAgreement m WHERE m.subscriberId = :subId AND m.documentId = :docId")
    MarketAgreement findMarketAgreementBySubscriberIdAndDocumentId(@Param("subId") String subId, @Param("docId") long docId);

    @Modifying
    @Transactional
    @Query("UPDATE MarketAgreement m SET m.status = :agreementStatus, m.expiryDate = :expiryDT, m.updateDate = :updateDT WHERE m.subscriberId = :subId AND m.documentId = :docId")
    void updateMarketAgreementRecord(@Param("subId") String subId, @Param("docId") long docId,
        @Param("agreementStatus") boolean agreementStatus, @Param("expiryDT") Date expiryDT, @Param("updateDT") Date updateDT);


    @Query("SELECT e.exchangeCode, d.documentCode FROM ExchangeInfo e, MarketDocument d, ExchangeAgreementMapper ea WHERE  ea.exchangeId = e.id and ea.documentId = d.documentId and e.exchangeCode in:exchangeCodeList")
    List<Object[]> findExchangeDocumentByExchangeList(@Param("exchangeCodeList") List<String> exchangeCodeList);
}
