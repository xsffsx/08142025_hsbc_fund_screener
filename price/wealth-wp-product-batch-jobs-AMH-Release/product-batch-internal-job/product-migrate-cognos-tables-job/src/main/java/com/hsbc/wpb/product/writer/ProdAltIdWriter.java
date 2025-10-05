package com.dummy.wpb.product.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.ProdAltIdPo;
import com.dummy.wpb.product.repository.ProdAltIdRepository;
import com.dummy.wpb.product.service.BulkInsertService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdAltIdWriter implements ProductMigrateWriter {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdAltIdRepository prodAltIdRepository;

    @Autowired
    private BulkInsertService bulkInsertService;

    @Override
    public void write(List<? extends Document> productList) throws Exception {
        ArrayList<ProdAltIdPo> prodAltIdPoList = new ArrayList<>();
        productList.stream().filter(product -> null != product.get(Field.altId))
                .forEach(product -> {
                    prodAltIdRepository.deleteByProdId(product.getLong(Field.prodId));
                    List<ProdAltIdPo> prodAltIdPos = product.getList(Field.altId, Document.class).stream().map(altId -> {
                        try {
                            ProdAltIdPo prodAltIdPo = objectMapper.readValue(objectMapper.writeValueAsString(altId), ProdAltIdPo.class);
                            prodAltIdPo.setProdId(product.getLong(Field.prodId));
                            prodAltIdPo.setCtryRecCde(product.getString(Field.ctryRecCde));
                            prodAltIdPo.setGrpMembrRecCde(product.getString(Field.grpMembrRecCde));
                            prodAltIdPo.setRowid(product.getLong(Field.prodId) + product.getString(Field.ctryRecCde)
                                    + product.getString(Field.grpMembrRecCde) + altId.getString(Field.prodCdeAltClassCde));
                            return prodAltIdPo;
                        } catch (JsonProcessingException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }).collect(Collectors.toList());
                    prodAltIdPoList.addAll(prodAltIdPos);
                });
        bulkInsertService.batchInsert(prodAltIdPoList);
    }
}
