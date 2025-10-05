package com.dummy.wpb.product;

import com.dummy.wpb.product.builder.ProductStreamItemByKeyItemReaderBuilder;
import com.dummy.wpb.product.model.ProductKey;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.xml.ProductKeySegment;
import com.dummy.wpb.product.model.xml.GnrcProductInstrument;
import com.dummy.wpb.product.reader.ImportFileItemReader;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.DocumentUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.function.Function;

import static com.dummy.wpb.product.constant.ConfigConstants.PRODUCT_CODE_ALTERNATED_CLASS_CODE_PRIMARY;

@Configuration
public class SnXmlFileReaderConfiguration {

    @Value("${batch.file.pattern}")
    private String filePattern;

    @Value("${batch.size}")
    private int batchSize;

    @Bean
    @StepScope
    public ItemStreamReader<ProductStreamItem> snXmlFileReader(ProductService productService,
                                                               @Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                                               @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                                               @Value("#{jobParameters['systemCde']}") String systemCde,
                                                               @Value("#{jobParameters['incomingPath']}") String incomingPath) {

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(GnrcProductInstrument.class);
        StaxEventItemReader<GnrcProductInstrument> staxEventItemReader = new StaxEventItemReader<>();
        staxEventItemReader.setFragmentRootElementName("gnrcProd");
        staxEventItemReader.setUnmarshaller(marshaller);

        filePattern = filePattern.replace("{ctryRecCde}", ctryRecCde)
                .replace("{grpMembrRecCde}", grpMembrRecCde)
                .replace("{systemCde}", systemCde);

        return new ProductStreamItemByKeyItemReaderBuilder<GnrcProductInstrument>()
                .name("snXmlFileReader")
                .productService(productService)
                .toProductKeyFunction(toProductKey)
                .toProductFunction(DocumentUtil::parseObject)
                .delegate(new ImportFileItemReader<>(incomingPath, filePattern, staxEventItemReader))
                .ctryRecCde(ctryRecCde)
                .grpMembrRecCde(grpMembrRecCde)
                .pageSize(batchSize)
                .build();
    }

    private final Function<GnrcProductInstrument, ProductKey> toProductKey = gnrcProductInstrument -> {
        ProductKeySegment prodKeySeg = gnrcProductInstrument.getProdKeySeg();
        ProductKey productKey = new ProductKey();
        BeanUtils.copyProperties(prodKeySeg, productKey);
        productKey.setProdAltNum(prodKeySeg.getProdCde());
        String prodCdeAltClassCde = StringUtils.defaultIfBlank(productKey.getProdCdeAltClassCde(), PRODUCT_CODE_ALTERNATED_CLASS_CODE_PRIMARY);
        productKey.setProdCdeAltClassCde(prodCdeAltClassCde);
        return productKey;
    };
}
