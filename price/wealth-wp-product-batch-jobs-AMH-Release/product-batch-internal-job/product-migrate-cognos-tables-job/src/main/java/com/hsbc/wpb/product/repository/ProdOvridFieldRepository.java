package com.dummy.wpb.product.repository;

import com.dummy.wpb.product.jpo.ProdOvridFieldPo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProdOvridFieldRepository extends CrudRepository<ProdOvridFieldPo, String> {
    @Modifying
    @Query("DELETE FROM ProdOvridFieldPo p WHERE p.prodId = :prodId")
    void deleteByProdId(@Param("prodId") Long prodId);
}
