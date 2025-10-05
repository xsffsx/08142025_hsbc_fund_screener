package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.labci;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteAccessLogForQuotesRepository extends JpaRepository<QuoteAccessLogForLabciQuotes, Long> {

}
