package com.dummy.wpb.product.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.EqtyLinkInvstPo;
import com.dummy.wpb.product.repository.EqtyLinkInvstRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EqtyLinkInvstWriter implements ProductMigrateWriter{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EqtyLinkInvstRepository eqtyLinkInvstRepository;

    @Override
    public void write(List<? extends Document> productList) {
        List<EqtyLinkInvstPo> eqtyLinkInvstPoList = productList.stream().filter(product -> null != product.get(Field.eqtyLinkInvst))
                .map(product -> {
                    EqtyLinkInvstPo eqtyLinkInvstPo = objectMapper.convertValue(product.get(Field.eqtyLinkInvst), EqtyLinkInvstPo.class);
                    eqtyLinkInvstPo.setProdIdEqtyLinkInvst(product.getLong(Field.prodId));
                    return eqtyLinkInvstPo;
                }).collect(Collectors.toList());
        eqtyLinkInvstRepository.saveAll(eqtyLinkInvstPoList);
    }
}
