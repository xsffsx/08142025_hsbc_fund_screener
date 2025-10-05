package com.dummy.wpb.product.repository;

import com.dummy.wpb.product.jpo.EqtyLinkInvstUndlStockPo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EqtyLinkInvstUndlStockRepository extends CrudRepository<EqtyLinkInvstUndlStockPo, String> {
    @Modifying
    @Query("DELETE FROM EqtyLinkInvstUndlStockPo e WHERE e.prodIdEqtyLinkInvst = :prodIdEqtyLinkInvst")
    void deleteByProdIdEqtyLinkInvst(@Param("prodIdEqtyLinkInvst") Long prodIdEqtyLinkInvst);
}
