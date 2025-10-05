package com.dummy.wpb.product.hkhbap;

import com.dummy.wpb.product.condition.ConditionalOnJobParameters;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.writer.ProductImportItemWriter;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static com.dummy.wpb.product.constant.BatchImportAction.UPDATE;

@Component
@StepScope
@ConditionalOnJobParameters({"ctryRecCde", "HK", "grpMembrRecCde", "HBAP"})
public class HKBondCustEligWriter extends ProductImportItemWriter {
    @Autowired
    private ItemProcessor<ProductStreamItem, ProductStreamItem> custEligXmlFileProcessor;

    public HKBondCustEligWriter(ProductService productService) {
        super(productService);
    }

    @Override
    @SneakyThrows
    public void afterWrite(List<? extends ProductStreamItem> streamItems) {
        List<ProductStreamItem> hbapBondStreamItems = streamItems.stream().filter(item -> {
            String prodTypeCde = item.getImportProduct().getString(Field.prodTypeCde);
            return BOND_CD.equals(prodTypeCde);
        }).collect(Collectors.toList());


        List<ProductStreamItem> dummyBondStreamItemList = builddummyBondStreamItemList(hbapBondStreamItems);
        for (ProductStreamItem dummyBondStreamItem : dummyBondStreamItemList) {
            custEligXmlFileProcessor.process(dummyBondStreamItem);
        }

        super.write(dummyBondStreamItemList);
    }

    private List<ProductStreamItem> builddummyBondStreamItemList(List<ProductStreamItem> streamItemList) {
        if (CollectionUtils.isEmpty(streamItemList)) {
            return Collections.emptyList();
        }

        Criteria[] criteriaArr = new Criteria[streamItemList.size()];
        for (int i = 0; i < streamItemList.size(); i++) {
            ProductStreamItem streamItem = streamItemList.get(i);
            Document updatedProduct = streamItem.getUpdatedProduct();
            Criteria criteria = new Criteria()
                    .and(Field.ctryRecCde).is(updatedProduct.get(Field.ctryRecCde))
                    .and(Field.grpMembrRecCde).is(dummy)
                    .and(Field.prodTypeCde).is(updatedProduct.get(Field.prodTypeCde))
                    .and(Field.prodAltPrimNum).is(updatedProduct.get(Field.prodAltPrimNum))
                    .and(Field.prodStatCde).ne(DELISTED);
            criteriaArr[i] = criteria;
        }

        List<Document> productList = productService.productByFilters(new Criteria().orOperator(criteriaArr).getCriteriaObject());

        List<ProductStreamItem> dummyStreamItemList = new ArrayList<>();
        for (ProductStreamItem streamItem : streamItemList) {
            Document importProduct = streamItem.getImportProduct();

            Document dummyHistory = productList.stream()
                    .filter(product -> StringUtils.equals(product.getString(Field.prodAltPrimNum), importProduct.getString(Field.prodAltPrimNum)))
                    .findFirst()
                    .orElse(null);

            if (dummyHistory == null) {
                continue;
            }
            ProductStreamItem dummyStreamItem = new ProductStreamItem();
            dummyStreamItem.setActionCode(UPDATE);
            dummyStreamItem.setOriginalProduct(dummyHistory);
            importProduct.put(Field.grpMembrRecCde, dummy);
            dummyStreamItem.setImportProduct(importProduct);
            dummyStreamItemList.add(dummyStreamItem);
        }

        return dummyStreamItemList;
    }
}
