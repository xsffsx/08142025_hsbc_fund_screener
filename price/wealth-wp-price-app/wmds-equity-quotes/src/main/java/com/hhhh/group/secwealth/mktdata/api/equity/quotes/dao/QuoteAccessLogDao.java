/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteAccessLogForQuotes;


/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository("quotesQuoteAccessLogDao")
public interface QuoteAccessLogDao extends JpaRepository<QuoteAccessLogForQuotes, Long> {

}
