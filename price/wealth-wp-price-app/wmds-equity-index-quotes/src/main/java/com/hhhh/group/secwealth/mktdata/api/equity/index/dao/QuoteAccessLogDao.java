/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry.QuoteAccessLogForIndex;


/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository("indexQuoteAccessLogDao")
public interface QuoteAccessLogDao extends JpaRepository<QuoteAccessLogForIndex, Long> {

}
