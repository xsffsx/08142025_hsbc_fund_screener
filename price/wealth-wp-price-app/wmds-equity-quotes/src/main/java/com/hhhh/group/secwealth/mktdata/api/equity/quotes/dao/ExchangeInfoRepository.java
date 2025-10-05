package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.ExchangeInfo;


@Repository
public interface ExchangeInfoRepository extends JpaRepository<ExchangeInfo, Long> {

    public ExchangeInfo findByExchangeCode(String exchangeCode);


}
