/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.dao;

import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry.IndexAccessLogUSPo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("indexAccessLogUSRepository")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled", havingValue = "true")
public interface IndexAccessLogUSRepository extends JpaRepository<IndexAccessLogUSPo, Long> {

}
