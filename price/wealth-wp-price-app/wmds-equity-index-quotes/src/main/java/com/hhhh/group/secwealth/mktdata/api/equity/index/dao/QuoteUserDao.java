/**
 * @Title QuoteUserDao.java
 * @description TODO
 * @author OJim
 * @time Jul 9, 2019 5:40:15 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry.QuoteUserForIndex;

/**
 * 
 */
/**
 * @Title QuoteUserDao.java
 * @description TODO
 * @author OJim
 * @time Jul 9, 2019 5:40:15 PM
 */
@Repository("indexQuoteUserDao")
public interface QuoteUserDao extends JpaRepository<QuoteUserForIndex, Long> {

}
