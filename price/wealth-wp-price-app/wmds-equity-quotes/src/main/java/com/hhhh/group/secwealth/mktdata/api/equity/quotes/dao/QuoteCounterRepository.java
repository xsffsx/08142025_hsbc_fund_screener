package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteCounter;

@Repository
public interface QuoteCounterRepository extends JpaRepository<QuoteCounter, Long> {

	@Query(value = "update MKT_DATA_INQ_CNT set INQ_CNT=INQ_CNT+ ,REC_UPDT_LA_DT_TM= where ", nativeQuery = true)
	void addQuoteAndUpdate(Long theQuoteCounter, Timestamp timestamp, Long userId);

}
