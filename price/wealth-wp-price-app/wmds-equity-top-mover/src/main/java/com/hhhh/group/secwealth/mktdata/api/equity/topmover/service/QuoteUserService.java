/**
 * @Title QuoteUserResource.java
 * @description TODO
 * @author OJim
 * @time Jul 9, 2019 5:34:58 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.api.equity.topmover.dao.QuoteUserDao;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.dao.entiry.QuoteUserForTopMover;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;

/**
 * 
 */
/**
 * @Title QuoteUserResource.java
 * @description TODO
 * @author OJim
 * @time Jul 9, 2019 5:34:58 PM
 */
@Service("topMoverQuoteUserService")
public class QuoteUserService {

	private final static Logger logger = LoggerFactory.getLogger(QuoteUserService.class);

	@Autowired()
	@Qualifier("topMoverQuoteUserDao")
	private QuoteUserDao quoteUserDao;

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Long getUserByExtnlId(QuoteUserForTopMover quoteUser) {
		Date currentDate = new Date();
		Timestamp currentTime = new Timestamp(currentDate.getTime());
		if (!StringUtils.isEmpty(quoteUser.getUserExtnlId())) {
			QuoteUserForTopMover quoUser = quoteUser;
			Example<QuoteUserForTopMover> example = Example.of(quoUser);
			quoUser = this.quoteUserDao.findOne(example);
			if (quoUser == null) {
				quoteUser.setLastLogin(currentTime);
				quoteUser.setLastUpdate(currentTime);
				try {
					quoUser = this.quoteUserDao.saveAndFlush(quoteUser);
				} catch (Exception e) {
					QuoteUserForTopMover quoUser2 = this.quoteUserDao.findOne(example);
					if (quoUser2 == null) {
						throw new ApplicationException(ExCodeConstant.EX_CODE_DB_PERSIST_FAILED);
					} else {
						return quoUser2.getUserReferenceId();
					}
				}
				QuoteUserService.logger
						.debug("User not found... create new one... and EID is" + quoUser.getUserExtnlId());
				List<QuoteUserForTopMover> quoteUserForTopMovers = this.quoteUserDao.findAll(example);
				if (quoteUserForTopMovers.size() > 1) {
					for (QuoteUserForTopMover quoteUserForTopMover : quoteUserForTopMovers) {
						if (quoteUserForTopMover.getUserReferenceId() <= quoUser.getUserReferenceId()) {
							quoUser = quoteUserForTopMover;
							QuoteUserService.logger
									.debug("User change...and user id is " + quoteUserForTopMover.getUserReferenceId());
						} else {
							quoteUserForTopMover.setMonitorFlag((long) 2);
							this.quoteUserDao.saveAndFlush(quoteUserForTopMover);
							QuoteUserService.logger
									.debug("User save twice should be update monitorflag 2...and user id is "
											+ quoteUserForTopMover.getUserReferenceId());
						}
					}
				}
			}
			QuoteUserService.logger.debug("User return...and user id is " + quoUser.getUserReferenceId());
			return quoUser.getUserReferenceId();
		} else {
			QuoteUserService.logger.error("Error: User Extnl Id is miss");
			throw new ApplicationException(ExCodeConstant.EX_CODE_DB_PERSIST_FAILED);
		}
	}
}
