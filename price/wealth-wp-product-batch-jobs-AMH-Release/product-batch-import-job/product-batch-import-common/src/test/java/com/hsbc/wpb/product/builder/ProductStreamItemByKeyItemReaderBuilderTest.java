package com.dummy.wpb.product.builder;

import com.dummy.wpb.product.model.ProductKey;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.reader.ImportFileItemReader;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.impl.DefaultProductService;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;


@RunWith(SpringJUnit4ClassRunner.class)
public class ProductStreamItemByKeyItemReaderBuilderTest {

    @Test
    public void build() {
        ProductStreamItemByKeyItemReaderBuilder<ProductStreamItem> builder = new ProductStreamItemByKeyItemReaderBuilder<>();
        Assert.assertNotNull(builder.name("ProductStreamItem"));
        Assert.assertNotNull(builder.itemType(ProductStreamItem.class));
        Assert.assertNotNull(builder.toProductFunction(new Function<ProductStreamItem, Document>() {
            @Override
            public Document apply(ProductStreamItem productStreamItem) {
                return null;
            }
        }));
        Assert.assertNotNull(builder.toProductKeyFunction(new Function<ProductStreamItem, ProductKey>() {
            @Override
            public ProductKey apply(ProductStreamItem productStreamItem) {
                return null;
            }
        }));
            Assert.assertNotNull(builder.productService(new DefaultProductService(new GraphQLService())));
        Assert.assertNotNull(builder.delegate(new ImportFileItemReader<>("incoming", "xx.xml", new StaxEventItemReader<>())));
        Assert.assertNotNull(builder.pageSize(30000));
        Assert.assertNotNull(builder.ctryRecCde("HK"));
        Assert.assertNotNull(builder.grpMembrRecCde("HBAP"));
        Assert.assertNotNull(builder.afterBuildItem(new BiConsumer<ProductStreamItem, ProductStreamItem>() {
            @Override
            public void accept(ProductStreamItem productStreamItem, ProductStreamItem productStreamItem2) {
                //
            }
        }));
        Assert.assertNotNull(builder.afterBuildItemList(new BiConsumer<List<ProductStreamItem>, List<ProductStreamItem>>() {
            @Override
            public void accept(List<ProductStreamItem> productStreamItems, List<ProductStreamItem> productStreamItems2) {
                //
            }
        }));
        Assert.assertNotNull(builder.build());
    }
}