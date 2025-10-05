package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.AccessLog;

@Repository
public interface QuoteAccessLogRepository extends JpaRepository<AccessLog, Long> {


}
