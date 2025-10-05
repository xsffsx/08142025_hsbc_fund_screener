package com.dummy.wpb.product.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.DebtInstmPo;
import com.dummy.wpb.product.repository.DebtInstmRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DebtInstmWriter implements ProductMigrateWriter {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DebtInstmRepository debtInstmRepository;

    @Override
    public void write(List<? extends Document> productList){
        List<DebtInstmPo> debtInstmPoList = productList.stream().filter(product -> null != product.get(Field.debtInstm))
                .map(product -> {
                    DebtInstmPo debtInstmPo = objectMapper.convertValue(product.get(Field.debtInstm), DebtInstmPo.class);
                    debtInstmPo.setProdIdDebtInstm(product.getLong(Field.prodId));
                    return debtInstmPo;
                }).collect(Collectors.toList());
        debtInstmRepository.saveAll(debtInstmPoList);
    }
}
