/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.Subscriber;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    @Query("select s.subscriberId,s.subscriberType from Subscriber s where s.subscriberId = :subscriberId")
    public List<Object[]> findSbuscriber(@Param("subscriberId") String subscriberId);

}
