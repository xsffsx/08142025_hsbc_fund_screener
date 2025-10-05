package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.Player;

@Repository
public interface QuotePlayerRepository extends JpaRepository<Player, Long> {
	
	public List<Player> findByPlayerTypeAndPlayerIdAndCountryCodeAndGroupMember(String playerType,String playerId,String countryCode,String groupMember);


}
