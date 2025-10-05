package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductKey;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.ProductKeyUtils;
import com.dummy.wpb.product.utils.BsonUtils;
import com.dummy.wpb.product.utils.JsonPathUtils;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
public class ProductStreamItemByKeyItemReader<T> extends AbstractPaginatedDataItemReader<ProductStreamItem> implements InitializingBean {

    protected Class<? extends ProductStreamItem> itemType = ProductStreamItem.class;

    protected Function<T, Document> toProductFunction;

    protected Function<T, ProductKey> toProductKeyFunction;

    protected ProductService productService;

    protected ImportFileItemReader<T> delegate;

    protected String ctryRecCde;

    protected String grpMembrRecCde;

    protected BiConsumer<T, ProductStreamItem> afterBuildItem;
    protected BiConsumer<List<T>, List<ProductStreamItem>> afterBuildItemList;

    @SneakyThrows
    @Override
    protected Iterator<ProductStreamItem> doPageRead() {
        Map<T, String> modelAndFileName = fetchPageSizeModel();

        List<Document> productList = queryProduct(modelAndFileName.keySet());

        List<ProductStreamItem> streamItemList = buildProductStreamItem(modelAndFileName, productList);

        if (null != afterBuildItemList && !modelAndFileName.isEmpty()) {
            afterBuildItemList.accept(new ArrayList<>(modelAndFileName.keySet()), streamItemList);
        }

        return streamItemList.iterator();
    }

    protected Map<T, String> fetchPageSizeModel() throws Exception {
        Map<T, String> modelAndFileName = new LinkedHashMap<>(pageSize);

        int currSize = 0;
        T model;
        while (currSize < pageSize && (model = delegate.read()) != null) {
            modelAndFileName.put(model, delegate.getCurrentResource().getFilename());
            currSize++;
        }

        return modelAndFileName;
    }

    protected List<Document> queryProduct(Collection<T> modelList) {
        if (modelList.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProductKey> productKeyList = modelList.stream().map(toProductKeyFunction).collect(Collectors.toList());
        Bson prodKeyBson = ProductKeyUtils.productKeyFilters(productKeyList);
        Map<String, Object> filters = BsonUtils.toMap(prodKeyBson);
        filters.put(Field.ctryRecCde, ctryRecCde);
        filters.put(Field.grpMembrRecCde, grpMembrRecCde);
        return productService.productByFilters(filters, Collections.singletonList(Field.fieldGroupCtoff));
    }

    protected List<ProductStreamItem> buildProductStreamItem(Map<T, String> modelAndFileName, List<Document> productList) throws ReflectiveOperationException {
        List<ProductStreamItem> streamItemList = new ArrayList<>();
        for (Map.Entry<T, String> entry : modelAndFileName.entrySet()) {
            T model = entry.getKey();
            String fileName = entry.getValue();

            ProductStreamItem productStreamItem = null;
            productStreamItem = itemType.getDeclaredConstructor().newInstance();
            Document importedProduct = toProductFunction.apply(model);
            ProductKey productKey = toProductKeyFunction.apply(model);
            Document originalProduct = productList.stream().filter(product -> hasProductKey(productKey, product)).findFirst().orElse(null);

            productStreamItem.setImportProduct(importedProduct);
            productStreamItem.setOriginalProduct(originalProduct);
            productStreamItem.setActionCode(MapUtils.isEmpty(originalProduct) ? BatchImportAction.ADD : BatchImportAction.UPDATE);
            productStreamItem.putParam("fileName", fileName);

            if (null != afterBuildItem) {
                afterBuildItem.accept(model, productStreamItem);
            }

            streamItemList.add(productStreamItem);
        }
        return streamItemList;
    }

    private boolean hasProductKey(ProductKey productKey, Document product) {
        List<Map<String, Object>> altIdList = JsonPathUtils.readValue(product, "altId[*]", new ArrayList<>());
        String ctryProdTrade1Cde = getCtryProdTrade1Cde(product);

        for (Map<String, Object> altId : altIdList) {
            ProductKey targetKey = new ProductKey();
            targetKey.setProdCdeAltClassCde((String) altId.get(Field.prodCdeAltClassCde));
            targetKey.setProdTypeCde((String) altId.get(Field.prodTypeCde));
            targetKey.setProdAltNum((String) altId.get(Field.prodAltNum));
            targetKey.setCcyProdCde((product.getString(Field.ccyProdCde)));
            targetKey.setCtryProdTradeCde(ctryProdTrade1Cde);

            if (ProductKeyUtils.areEqual(productKey, targetKey)) {
                return true;
            }
        }

        ProductKey targetKey = new ProductKey();
        targetKey.setProdCdeAltClassCde("W");
        targetKey.setProdTypeCde(product.getString(Field.prodTypeCde));
        targetKey.setProdAltNum(product.getString(Field.prodCde));
        targetKey.setCcyProdCde(product.getString(Field.ccyProdCde));
        targetKey.setCtryProdTradeCde(ctryProdTrade1Cde);

        return ProductKeyUtils.areEqual(productKey, targetKey);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
        super.close();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
        super.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        if (isSaveState()) {
            delegate.update(executionContext);
        }
        super.update(executionContext);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.state(delegate != null, "A ItemReader that reads the model is required.");
        Assert.state(toProductFunction != null, "A functions for converting model to product is required.");
        Assert.state(toProductKeyFunction != null, "A functions for converting model to product key is required.");
        Assert.state(ctryRecCde != null && grpMembrRecCde != null, "The ctryRecCde and grpMembrRecCde is required.");
        Assert.state(productService != null, "ProductService is required.");
    }

    private String getCtryProdTrade1Cde(Document prod) {
        List<String> list = JsonPathUtils.readValue(prod, "ctryProdTradeCde");
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

}
