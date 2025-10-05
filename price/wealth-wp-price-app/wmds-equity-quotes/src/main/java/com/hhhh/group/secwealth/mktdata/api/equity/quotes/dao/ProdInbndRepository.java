/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.ProdInbnd;

@Repository("prodInbndRepository")
public interface ProdInbndRepository extends JpaRepository<ProdInbnd, Long> {

    @Query("Select p From ProdInbnd p where p.externalNumber in (:externalNumbers) and p.tradingMarketCode = :tradingMarketCode order by p.sourceFlag DESC, p.expiredIndicator")
    public List<ProdInbnd> getProdInbndList(@Param("externalNumbers") List<String> externalNumbers,
        @Param("tradingMarketCode") String tradingMarketCode);
}
