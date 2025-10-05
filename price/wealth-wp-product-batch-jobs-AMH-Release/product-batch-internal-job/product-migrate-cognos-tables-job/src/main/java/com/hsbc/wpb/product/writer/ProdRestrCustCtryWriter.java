package com.dummy.wpb.product.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.ProdRestrCustCtryPo;
import com.dummy.wpb.product.repository.ProdRestrCustCtryRepository;
import com.dummy.wpb.product.service.BulkInsertService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdRestrCustCtryWriter implements ProductMigrateWriter{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdRestrCustCtryRepository prodRestrCustCtryRepository;

    @Autowired
    private BulkInsertService bulkInsertService;

    @Override
    public void write(List<? extends Document> productList) throws Exception {
        ArrayList<ProdRestrCustCtryPo> prodRestrCustCtryPoList = new ArrayList<>();
        productList.stream().filter(product -> null != product.get("restrCustCtry"))
                .forEach(product -> {
                    prodRestrCustCtryRepository.deleteByProdId(product.getLong(Field.prodId));
                    List<ProdRestrCustCtryPo> restrCustCtrys = product.getList("restrCustCtry", Document.class).stream().map(restrCustCtry -> {
                        try {
                            ProdRestrCustCtryPo prodRestrCustCtryPo = objectMapper.readValue(objectMapper.writeValueAsString(restrCustCtry), ProdRestrCustCtryPo.class);
                            prodRestrCustCtryPo.setProdId(product.getLong(Field.prodId));
                            prodRestrCustCtryPo.setRowid(product.getLong(Field.prodId) + restrCustCtry.getString("ctryIsoCde")
                                    + restrCustCtry.getString("restrCtryTypeCde") + restrCustCtry.getString("restrCde"));
                            return prodRestrCustCtryPo;
                        } catch (JsonProcessingException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }).collect(Collectors.toList());
                    prodRestrCustCtryPoList.addAll(restrCustCtrys);
                });
        bulkInsertService.batchInsert(prodRestrCustCtryPoList);
    }
}
