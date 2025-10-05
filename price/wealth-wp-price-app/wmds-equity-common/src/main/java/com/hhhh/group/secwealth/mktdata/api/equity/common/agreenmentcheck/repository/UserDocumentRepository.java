/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean.MarketDocument;

/**
 * <p>
 * <b> Market Document Repository. </b>
 * </p>
 */
@Repository
public interface UserDocumentRepository extends JpaRepository<MarketDocument, Long> {

    @Query("SELECT distinct(e.exchangeCode) FROM ExchangeInfo e, MarketDocument d, ExchangeAgreementMapper ea WHERE e.id = ea.exchangeId AND d.documentId = ea.documentId AND d.documentCode = (:document_Cd)")
    public List<String> findExchangeCodeByDocumentCode(@Param("document_Cd") String document_Cd);

    @Query("SELECT documentId FROM MarketDocument WHERE documentCode = (:document_Cd)")
    public Long findDocumentId(@Param("document_Cd") String documentCode);

}
