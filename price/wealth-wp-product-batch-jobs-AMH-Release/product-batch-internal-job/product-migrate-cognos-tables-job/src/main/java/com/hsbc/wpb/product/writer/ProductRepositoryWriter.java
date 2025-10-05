package com.dummy.wpb.product.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.*;
import com.dummy.wpb.product.repository.*;
import com.dummy.wpb.product.utils.JsonPathUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class ProductRepositoryWriter implements ProductMigrateWriter {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void write(List<? extends Document> productList) {

        List<ProductPo> productPos = productList.stream().map(product -> {
            try {
                ProductPo productPo = objectMapper.readValue(objectMapper.writeValueAsString(product), ProductPo.class);
                createNotFoundField(product, productPo);
                return productPo;
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }).collect(Collectors.toList());
        productRepository.saveAll(productPos);
    }

    private void createNotFoundField(Document product, ProductPo productPo) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> ctryProdTradeCdes = product.getList(Field.ctryProdTradeCde, String.class);
        if (CollectionUtils.isEmpty(ctryProdTradeCdes)) {
            return;
        }
        for (int i = 0; i < ctryProdTradeCdes.size(); i++) {
            Method setCtryProdTradeCdeMethod = productPo.getClass().getMethod(String.format("setCtryProdTrade%sCde", (i + 1)), String.class);
            String ctryProdTradeCdeValue = (String) JsonPathUtils.readValue(product, String.format("ctryProdTradeCde[%s]", i));
            setCtryProdTradeCdeMethod.invoke(productPo, ctryProdTradeCdeValue);
        }
    }
}
