package com.dummy.wpb.product.repository;

import com.dummy.wpb.product.jpo.ProdFormReqmtPo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProdFormReqmtRepository extends CrudRepository<ProdFormReqmtPo, String> {
    @Modifying
    @Query("DELETE FROM ProdFormReqmtPo p WHERE p.prodId = :prodId")
    void deleteByProdId(@Param("prodId") Long prodId);
}
