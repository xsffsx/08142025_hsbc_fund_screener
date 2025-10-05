/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.topmover.dao.entiry.QuoteAccessLogForTopMover;


/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository("topMoverQuoteAccessLogDao")
public interface QuoteAccessLogDao extends JpaRepository<QuoteAccessLogForTopMover, Long> {

}
