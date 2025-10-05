package com.dummy.wpb.product.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.EqtyLinkInvstUndlStockPo;
import com.dummy.wpb.product.repository.EqtyLinkInvstUndlStockRepository;
import com.dummy.wpb.product.service.BulkInsertService;
import com.dummy.wpb.product.utils.JsonPathUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EqtyLinkInvstUndlStockWriter implements ProductMigrateWriter{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EqtyLinkInvstUndlStockRepository eqtyLinkInvstUndlStockRepository;

    @Autowired
    private BulkInsertService bulkInsertService;

    @Override
    public void write(List<? extends Document> productList) {
        ArrayList<EqtyLinkInvstUndlStockPo> eqtyLinkInvstUndlStockPoList = new ArrayList<>();
        productList.stream().filter(product -> null != JsonPathUtils.readValue(product, "eqtyLinkInvst.undlStock"))
                .forEach(product -> {
                    eqtyLinkInvstUndlStockRepository.deleteByProdIdEqtyLinkInvst(product.getLong(Field.prodId));
                    List<EqtyLinkInvstUndlStockPo> eqtyLinkInvstUndlStockPos = ((List<Map<String, Object>>) JsonPathUtils.readValue(product, "eqtyLinkInvst.undlStock")).stream()
                            .map(undlStock -> {
                                try {
                                    EqtyLinkInvstUndlStockPo eqtyLinkInvstUndlStockPo = objectMapper.readValue(objectMapper.writeValueAsString(undlStock), EqtyLinkInvstUndlStockPo.class);
                                    eqtyLinkInvstUndlStockPo.setProdIdEqtyLinkInvst(product.getLong(Field.prodId));
                                    eqtyLinkInvstUndlStockPo.setRowid(product.getLong(Field.prodId) + (String)undlStock.get("instmUndlCde"));
                                    return eqtyLinkInvstUndlStockPo;
                                } catch (JsonProcessingException e) {
                                    throw new IllegalArgumentException(e);
                                }
                            }).collect(Collectors.toList());
                    eqtyLinkInvstUndlStockPoList.addAll(eqtyLinkInvstUndlStockPos);
                });
        bulkInsertService.batchInsert(eqtyLinkInvstUndlStockPoList);
    }
}
