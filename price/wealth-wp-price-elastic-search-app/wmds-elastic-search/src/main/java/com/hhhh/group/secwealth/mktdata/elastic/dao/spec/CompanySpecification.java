package com.hhhh.group.secwealth.mktdata.elastic.dao.spec;

import com.hhhh.group.secwealth.mktdata.elastic.dao.entiry.GRCompanyPo;
import com.hhhh.group.secwealth.mktdata.elastic.dao.entiry.GRDocumentPo;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CompanySpecification {

    public Specification<GRCompanyPo> getRecentUpdateSpec(String market){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtil.isValid(market)) {
                predicates.add(cb.equal(root.get("market").as(String.class), market));
            }
            predicates.add(cb.isNull(root.get("expire").as(String.class)));
            Join<GRCompanyPo, GRDocumentPo> join = root.join("documents", JoinType.LEFT);
            LocalDate date = LocalDate.now().minusDays(7);
            predicates.add(cb.greaterThan(join.get("publishedDate"), date.toString()));
            Predicate[] pre = new Predicate[predicates.size()];
            query.distinct(true);
            query.where(predicates.toArray(pre));
            return cb.and(predicates.toArray(pre));
        };
    }

}
