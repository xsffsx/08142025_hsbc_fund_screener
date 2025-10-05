package com.dummy.wpb.product.repository;

import com.dummy.wpb.product.jpo.ProdRestrCustCtryPo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProdRestrCustCtryRepository extends CrudRepository<ProdRestrCustCtryPo, String> {

    @Modifying
    @Query("DELETE FROM ProdRestrCustCtryPo p WHERE p.prodId = :prodId")
    void deleteByProdId(@Param("prodId") Long prodId);
}
