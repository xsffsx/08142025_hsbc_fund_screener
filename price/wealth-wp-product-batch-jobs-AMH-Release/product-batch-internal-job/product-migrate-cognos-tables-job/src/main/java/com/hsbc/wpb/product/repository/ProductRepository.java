package com.dummy.wpb.product.repository;

import com.dummy.wpb.product.jpo.ProductPo;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductPo, Long> {
}
