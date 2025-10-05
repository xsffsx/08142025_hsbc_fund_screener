package com.dummy.wpb.product.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.ProdOvridFieldPo;
import com.dummy.wpb.product.repository.ProdOvridFieldRepository;
import com.dummy.wpb.product.service.BulkInsertService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdOvridFieldWriter implements ProductMigrateWriter{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdOvridFieldRepository prodOvridFieldRepository;

    @Autowired
    private BulkInsertService bulkInsertService;

    @Override
    public void write(List<? extends Document> productList) {
        ArrayList<ProdOvridFieldPo> prodOvridFieldPoList = new ArrayList<>();
        productList.stream().filter(product -> null != product.get("ovridField"))
                .forEach(product -> {
                    prodOvridFieldRepository.deleteByProdId(product.getLong(Field.prodId));
                    List<ProdOvridFieldPo> prodOvridFieldPos = product.getList("ovridField", Document.class).stream().map(ovridField -> {
                        try {
                            ProdOvridFieldPo prodOvridFieldPo = objectMapper.readValue(objectMapper.writeValueAsString(ovridField), ProdOvridFieldPo.class);
                            prodOvridFieldPo.setProdId(product.getLong(Field.prodId));
                            prodOvridFieldPo.setRowid(product.getLong(Field.prodId) + ovridField.getString("fieldCde"));
                            return prodOvridFieldPo;
                        } catch (JsonProcessingException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }).collect(Collectors.toList());
                    prodOvridFieldPoList.addAll(prodOvridFieldPos);
                });
        bulkInsertService.batchInsert(prodOvridFieldPoList);
    }
}
