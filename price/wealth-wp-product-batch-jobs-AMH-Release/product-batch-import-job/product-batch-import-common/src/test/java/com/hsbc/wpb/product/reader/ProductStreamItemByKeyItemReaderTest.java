package com.dummy.wpb.product.reader;


import com.dummy.wpb.product.builder.ProductStreamItemByKeyItemReaderBuilder;
import com.dummy.wpb.product.model.ProductKey;
import com.dummy.wpb.product.model.xml.DebtInstm;
import com.dummy.wpb.product.model.xml.ProductKeySegment;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.DocumentUtil;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Collections;
import java.util.function.Function;

import static com.dummy.wpb.product.constant.ConfigConstants.PRODUCT_CODE_ALTERNATED_CLASS_CODE_PRIMARY;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;


@RunWith(SpringJUnit4ClassRunner.class)
public class ProductStreamItemByKeyItemReaderTest {

    private ProductService productService = mock(ProductService.class);

    static MockedStatic<LegacyConfig> legacyConfigMockedStatic;

    @Before
    public void setUp() {
        legacyConfigMockedStatic = Mockito.mockStatic(LegacyConfig.class);
        legacyConfigMockedStatic.when(() -> LegacyConfig.getList(anyString())).thenReturn(Collections.emptyList());
    }

    @Test
    public void test() throws Exception {
        String json = CommonUtils.readResource("/test/BOND-50743650.json");
        Document product = Document.parse(json);
        Mockito.when(productService.productByFilters(Mockito.anyMap())).thenReturn(Collections.singletonList(product));
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(DebtInstm.class);
        StaxEventItemReader<DebtInstm> staxEventItemReader = new StaxEventItemReader<>();
        staxEventItemReader.setFragmentRootElementName("debtInstm");
        staxEventItemReader.setUnmarshaller(marshaller);
        String filePattern = "^HK_HBAP_WISDOM_BOND_(\\d{14})\\.[Xx][Mm][Ll]$";
        String incomingPath = new ClassPathResource("/test").getFile().getAbsolutePath();
        ProductStreamItemByKeyItemReader<DebtInstm> reader = new ProductStreamItemByKeyItemReaderBuilder<DebtInstm>()
                .name("bondXmlFileReader")
                .productService(productService)
                .toProductKeyFunction(toProductKey)
                .toProductFunction(DocumentUtil::parseObject)
                .delegate(new ImportFileItemReader<>(incomingPath, filePattern, staxEventItemReader))
                .ctryRecCde("HK")
                .grpMembrRecCde("HBAP")
                .build();
        reader.open(new ExecutionContext());
        //Iterator<ProductStreamItem> productStreamItemIterator = reader.doPageRead();
        //Assert.assertEquals(2, IteratorUtils.size(productStreamItemIterator));
        reader.close();
        Assert.assertNotNull(reader);
    }

    @After
    public void after() {
        legacyConfigMockedStatic.close();
    }

    private final Function<DebtInstm, ProductKey> toProductKey = debtInstm -> {
        ProductKeySegment prodKeySeg = debtInstm.getProdKeySeg();
        ProductKey productKey = new ProductKey();
        BeanUtils.copyProperties(prodKeySeg, productKey);
        productKey.setCcyProdCde(prodKeySeg.getCcyProdCde());
        productKey.setProdAltNum(prodKeySeg.getProdCde());
        productKey.setCtryProdTradeCde(prodKeySeg.getCtryProdTradeCde());
        productKey.setProdTypeCde(prodKeySeg.getProdTypeCde());

        String prodCdeAltClassCde = StringUtils.defaultIfBlank(productKey.getProdCdeAltClassCde(), PRODUCT_CODE_ALTERNATED_CLASS_CODE_PRIMARY);
        productKey.setProdCdeAltClassCde(prodCdeAltClassCde);
        return productKey;
    };


}
