package com.dummy.wpb.product.service;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class BulkInsertService {
    @Value("${batch.chunk-size}")
    private Integer chunkSize;
    @PersistenceContext
    private EntityManager entityManager;

    public <T> void batchInsert(List<T> list) {
        if (!ObjectUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                entityManager.persist(list.get(i));
                if (i % chunkSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        }
    }
}
