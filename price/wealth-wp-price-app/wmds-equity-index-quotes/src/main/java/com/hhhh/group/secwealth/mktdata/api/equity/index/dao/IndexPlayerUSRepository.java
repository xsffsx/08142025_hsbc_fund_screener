/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.dao;

import com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry.IndexPlayerUSPo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("indexPlayerUSRepository")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled", havingValue = "true")
public interface IndexPlayerUSRepository extends JpaRepository<IndexPlayerUSPo, Long> {

    public List<IndexPlayerUSPo> findByPlayerTypeAndPlayerIdAndCountryCodeAndGroupMember(String playerType, String playerId,
                                                                                            String countryCode, String groupMember);
}
