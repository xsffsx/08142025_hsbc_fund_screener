package com.dummy.wpb.product.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.ProdFormReqmtPo;
import com.dummy.wpb.product.repository.ProdFormReqmtRepository;
import com.dummy.wpb.product.service.BulkInsertService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdFormReqmtWriter implements ProductMigrateWriter{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdFormReqmtRepository prodFormReqmtRepository;

    @Autowired
    private BulkInsertService bulkInsertService;

    @Override
    public void write(List<? extends Document> productList) {
        ArrayList<ProdFormReqmtPo> prodFormReqmtPoList = new ArrayList<>();
        productList.stream().filter(product -> null != product.get("formReqmt"))
                .forEach(product -> {
                    prodFormReqmtRepository.deleteByProdId(product.getLong(Field.prodId));
                    List<ProdFormReqmtPo> prodFormReqmts = product.getList("formReqmt", Document.class).stream().map(formReqmt -> {
                        try {
                            ProdFormReqmtPo prodFormReqmtPo = objectMapper.readValue(objectMapper.writeValueAsString(formReqmt), ProdFormReqmtPo.class);
                            prodFormReqmtPo.setProdId(product.getLong(Field.prodId));
                            prodFormReqmtPo.setRowid(product.getLong(Field.prodId) + formReqmt.getString("formReqCde"));
                            return prodFormReqmtPo;
                        } catch (JsonProcessingException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }).collect(Collectors.toList());
                    prodFormReqmtPoList.addAll(prodFormReqmts);
                });
        bulkInsertService.batchInsert(prodFormReqmtPoList);
    }
}
