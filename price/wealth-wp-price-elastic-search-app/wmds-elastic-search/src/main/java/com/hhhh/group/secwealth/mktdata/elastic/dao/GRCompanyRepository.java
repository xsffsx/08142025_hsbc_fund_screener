package com.hhhh.group.secwealth.mktdata.elastic.dao;

import com.hhhh.group.secwealth.mktdata.elastic.dao.entiry.GRCompanyPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GRCompanyRepository extends JpaRepository<GRCompanyPo, Long>, JpaSpecificationExecutor<GRCompanyPo> {
    List<GRCompanyPo> findAllByMarketEqualsAndExpireIsNull(String market);
}
