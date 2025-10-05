/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hhhh.group.secwealth.mktdata.api.equity.topmover.dao.entiry.QuoteAccessLogForTopMover;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Service("topMoverQuoteAccessLogService")
public class QuoteAccessLogService {

	private final static Logger logger = LoggerFactory.getLogger(QuoteAccessLogService.class);

	@PersistenceContext
	protected EntityManager em;

	private final int maxNum = 10;

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void saveQuoteAccessLog(final List<QuoteAccessLogForTopMover> quoteList) {
		try {
			for (int i = 0; i < quoteList.size(); i++) {
				this.em.persist(quoteList.get(i));
				// commit the transction batch
				if (i % this.maxNum == 0) {
					this.em.flush();
					this.em.clear();
				}
			}
			QuoteAccessLogService.logger.info("save to DB success");
		} catch (Exception e) {
			QuoteAccessLogService.logger.error("batch insert data failuer.", e);
			throw new RuntimeException("DBPersistFailed");
		}
	}
}
