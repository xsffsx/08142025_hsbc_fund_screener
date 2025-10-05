package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.model.ProductPriceStreamItem;
import com.dummy.wpb.product.builder.ProductStreamItemByKeyItemReaderBuilder;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductKey;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.ProductPriceHistory;
import com.dummy.wpb.product.model.xml.ProductKeySegment;
import com.dummy.wpb.product.model.xml.ProductPrice;
import com.dummy.wpb.product.service.ProductPriceHistoryService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.DocumentUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.constant.ConfigConstants.PRODUCT_CODE_ALTERNATED_CLASS_CODE_PRIMARY;

@Configuration
public class ProductPriceXmlReaderConfiguration {

    @Value("${batch.file.pattern}")
    private String filePattern;

    @Value("${batch.size}")
    private int batchSize;


    @Autowired
    private ProductPriceHistoryService productPriceHistoryService;

    @Bean
    @StepScope
    public ItemStreamReader<ProductStreamItem> productPriceXmlFileReader(
            ProductService productService,
            @Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
            @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
            @Value("#{jobParameters['systemCde']}") String systemCde,
            @Value("#{jobParameters['incomingPath']}") String incomingPath) {

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProductPrice.class);
        StaxEventItemReader<ProductPrice> staxEventItemReader = new StaxEventItemReader<>();
        staxEventItemReader.setFragmentRootElementName("prodPrc");
        staxEventItemReader.setUnmarshaller(marshaller);

        filePattern = filePattern.replace("{ctryRecCde}", ctryRecCde)
                .replace("{grpMembrRecCde}", grpMembrRecCde)
                .replace("{systemCde}", systemCde);

        return new ProductStreamItemByKeyItemReaderBuilder<ProductPrice>()
                .name("productPriceXmlFileReader")
                .productService(productService)
                .toProductKeyFunction(toProductKey)
                .itemType(ProductPriceStreamItem.class)
                .toProductFunction(DocumentUtil::parseObject)
                .delegate(new ImportFileItemReader<>(incomingPath, filePattern, staxEventItemReader))
                .ctryRecCde(ctryRecCde)
                .grpMembrRecCde(grpMembrRecCde)
                .afterBuildItem(buildPriceInfo)
                .afterBuildItemList(queryPriceHistory)
                .pageSize(batchSize)
                .build();
    }

    private final Function<ProductPrice, ProductKey> toProductKey = productPrice -> {
        ProductKeySegment prodKeySeg = productPrice.getProdKeySeg();
        ProductKey productKey = new ProductKey();
        BeanUtils.copyProperties(prodKeySeg, productKey);
        productKey.setProdAltNum(prodKeySeg.getProdCde());

        String prodCdeAltClassCde = StringUtils.defaultIfBlank(productKey.getProdCdeAltClassCde(), PRODUCT_CODE_ALTERNATED_CLASS_CODE_PRIMARY);
        productKey.setProdCdeAltClassCde(prodCdeAltClassCde);

        return productKey;
    };


    private final BiConsumer<ProductPrice, ProductStreamItem> buildPriceInfo = ((productPrice, streamItem) -> {
        List<Document> priceHistoryList = (List<Document>) streamItem.getImportProduct().remove("priceHistory");
        ProductPriceStreamItem priceStreamItem = (ProductPriceStreamItem) streamItem;
        priceStreamItem.setImportPriceHistory(priceHistoryList);
    });

    private final BiConsumer<List<ProductPrice>, List<ProductStreamItem>> queryPriceHistory = ((productPriceList, streamItemList) -> {
        List<Criteria> orCriteria = new ArrayList<>();
        for (ProductStreamItem streamItem : streamItemList) {
            if (BatchImportAction.ADD == streamItem.getActionCode()) {
                continue;
            }

            ProductPriceStreamItem priceStreamItem = (ProductPriceStreamItem) streamItem;
            Document originalProduct = priceStreamItem.getOriginalProduct();
            priceStreamItem.setProdId(originalProduct.get(Field.prodId, Number.class).longValue());
            for (Document priceHistory : priceStreamItem.getImportPriceHistory()) {
                if (null == priceHistory.get(Field.prcEffDt)) {
                    continue;
                }

                Criteria criteria = new Criteria()
                        .and(Field.prodId).is(priceStreamItem.getProdId())
                        .and(Field.pdcyPrcCde).is(priceHistory.get(Field.pdcyPrcCde))
                        .and(Field.prcEffDt).is(priceHistory.get(Field.prcEffDt).toString());
                orCriteria.add(criteria);
            }
        }

        if (!orCriteria.isEmpty()) {
            Document filter = new Criteria().orOperator(orCriteria.toArray(new Criteria[0])).getCriteriaObject();
            List<ProductPriceHistory> priceHistoryList = null;
            try {
                priceHistoryList = productPriceHistoryService.productPriceHistoryByFilter(filter);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to query product price history.", e);
            }

            for (ProductStreamItem streamItem : streamItemList) {
                ProductPriceStreamItem priceStreamItem = (ProductPriceStreamItem) streamItem;
                List<ProductPriceHistory> originalPriceHistory = priceHistoryList
                        .stream()
                        .filter(priceHistory -> Objects.equals(priceStreamItem.getProdId(), priceHistory.getProdId()))
                        .collect(Collectors.toList());
                priceStreamItem.setOriginalPriceHistory(originalPriceHistory);
            }
        }
    });
}
