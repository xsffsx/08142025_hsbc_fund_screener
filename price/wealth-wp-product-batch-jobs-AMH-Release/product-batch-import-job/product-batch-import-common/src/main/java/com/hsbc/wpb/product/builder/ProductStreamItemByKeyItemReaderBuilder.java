package com.dummy.wpb.product.builder;

import com.dummy.wpb.product.model.ProductKey;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.reader.ImportFileItemReader;
import com.dummy.wpb.product.reader.ProductStreamItemByKeyItemReader;
import com.dummy.wpb.product.service.ProductService;
import org.bson.Document;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ProductStreamItemByKeyItemReaderBuilder<T>{
    protected Class<? extends ProductStreamItem> itemType;

    protected Function<T, Document> toProductFunction;

    protected Function<T, ProductKey> toProductKeyFunction;


    protected ProductService productService;

    protected ImportFileItemReader<T> delegate;

    private String name;

    private int pageSize = 50;

    protected String ctryRecCde;

    protected String grpMembrRecCde;


    protected BiConsumer<T, ProductStreamItem> afterBuildItem;

    protected BiConsumer<List<T>, List<ProductStreamItem>> afterBuildItemList;

    public ProductStreamItemByKeyItemReaderBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public ProductStreamItemByKeyItemReaderBuilder<T> itemType(Class<? extends ProductStreamItem> itemType) {
        this.itemType = itemType;
        return this;
    }

    public ProductStreamItemByKeyItemReaderBuilder<T> toProductFunction(Function<T, Document> modelToProductFunction) {
        this.toProductFunction = modelToProductFunction;
        return this;
    }

    public ProductStreamItemByKeyItemReaderBuilder<T> toProductKeyFunction(Function<T, ProductKey> moderlToProductKeyFunction) {
        this.toProductKeyFunction = moderlToProductKeyFunction;
        return this;
    }

    public ProductStreamItemByKeyItemReaderBuilder<T> productService(ProductService productService) {
        this.productService = productService;
        return this;
    }

    public ProductStreamItemByKeyItemReaderBuilder<T> delegate(ImportFileItemReader<T> delegate) {
        this.delegate = delegate;
        return this;
    }

    public ProductStreamItemByKeyItemReaderBuilder<T> pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public ProductStreamItemByKeyItemReaderBuilder<T> ctryRecCde(String ctryRecCde) {
        this.ctryRecCde = ctryRecCde;
        return this;
    }

    public ProductStreamItemByKeyItemReaderBuilder<T> grpMembrRecCde(String grpMembrRecCde) {
        this.grpMembrRecCde = grpMembrRecCde;
        return this;
    }

    public ProductStreamItemByKeyItemReaderBuilder<T> afterBuildItem(BiConsumer<T, ProductStreamItem> afterBuildItem) {
        this.afterBuildItem = afterBuildItem;
        return this;
    }

    public ProductStreamItemByKeyItemReaderBuilder<T> afterBuildItemList(BiConsumer<List<T>, List<ProductStreamItem>> afterBuildItemList) {
        this.afterBuildItemList = afterBuildItemList;
        return this;
    }

    public ProductStreamItemByKeyItemReader<T> build(){
        ProductStreamItemByKeyItemReader<T> reader = new ProductStreamItemByKeyItemReader<>();
        reader.setProductService(productService);
        reader.setToProductFunction(toProductFunction);
        reader.setToProductKeyFunction(toProductKeyFunction);
        reader.setPageSize(pageSize);
        reader.setDelegate(delegate);
        reader.setCtryRecCde(ctryRecCde);
        reader.setGrpMembrRecCde(grpMembrRecCde);
        reader.setAfterBuildItem(afterBuildItem);
        reader.setAfterBuildItemList(afterBuildItemList);
        if (null != itemType){
            reader.setItemType(itemType);
        }
        reader.setName(name);

        reader.afterPropertiesSet();

        return reader;
    }
}
