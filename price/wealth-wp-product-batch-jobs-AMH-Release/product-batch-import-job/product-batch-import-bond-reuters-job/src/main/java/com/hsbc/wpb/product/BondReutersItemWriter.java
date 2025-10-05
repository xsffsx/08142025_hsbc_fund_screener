package com.dummy.wpb.product;

import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.writer.ProductImportItemWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BondReutersItemWriter extends ProductImportItemWriter {
    private ReferenceDataService referenceDataService;

    public BondReutersItemWriter(ProductService productService, ReferenceDataService referenceDataService) {
        super(productService);
        this.referenceDataService = referenceDataService;
    }

    @SneakyThrows
    @Override
    public void beforeWrite(List<? extends ProductStreamItem> streamItemList) {
        //Get missing reference data from ExecutionContext
        List<ReferenceData> missingRefList = new ArrayList<>();
        streamItemList.stream()
                .map(item -> ((BondReutersStreamItem) item).getMissingRefDataList())
                .filter(CollectionUtils::isNotEmpty)
                .forEach(missingRefList::addAll);
        if (!missingRefList.isEmpty()) {
            referenceDataService.createRefData(missingRefList);
        }
    }

}