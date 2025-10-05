package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.ConfigurationService;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import io.micrometer.core.instrument.util.IOUtils;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

@RunWith(MockitoJUnitRunner.class)
public class AltIdValidatorTest {

    @Mock
    ConfigurationService configurationService;

    @Mock
    MongoTemplate mongoTemplate;
    Document legacyConfig;
    Map<String, Object> configMap;

    @Before
    public void setUp() {
        InputStream inputStream = CommonUtils.class.getResourceAsStream("/legacyConfig/legacyConfig_1.json");
        legacyConfig = Document.parse(IOUtils.toString(inputStream));
        configMap = legacyConfig
                .getList("config", Document.class, new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(
                        config -> config.getString("key"),
                        config -> config.get("value")));

    }


    @Test
    public void testConstructor_withNullConfigMap() {
        Mockito.when(configurationService.getLegacyConfigByFilter()).thenReturn(null);
        AltIdValidator altIdValidator = new AltIdValidator(mongoTemplate, configurationService);
        Assert.assertNotNull(altIdValidator);
    }
    @Test
    public void testDoValidate_false() {
        configMap.put("BATCH.PRODKEY.PRIMARY","test");
        Mockito.when(configurationService.getLegacyConfigByFilter()).thenReturn(configMap);
        AltIdValidator altIdValidator = new AltIdValidator(mongoTemplate, configurationService);
        altIdValidator.validate(null,getProduct("SDSIN24A0020", "SDSIN24A0021"));
        Assert.assertNotNull(altIdValidator);
    }



    @Test
    public void testValidate() {
        Mockito.when(configurationService.getLegacyConfigByFilter()).thenReturn(configMap);
        AltIdValidator altIdValidator1 = new AltIdValidator(mongoTemplate, configurationService);
        Map<String, Object> newProduct = getProduct("SDSIN24A0020", "SDSIN24A0021");
        Map<String, Object> oldProduct = getProduct("SDSIN24A0022", "SDSIN24A0023");
        List<Document> docList = new ArrayList<>();
        docList.add(legacyConfig);
        Mockito.when(mongoTemplate.find(any(Query.class), Mockito.<Class<Document>>any(), anyString())).thenReturn(docList);
        List<Error> errorList = altIdValidator1.validate(oldProduct, newProduct);
        Assertions.assertEquals(2, errorList.size());
    }

    @Test
    public void testValidate_withProdStatCdeD() {
        Mockito.when(configurationService.getLegacyConfigByFilter()).thenReturn(configMap);
        AltIdValidator altIdValidator = new AltIdValidator(mongoTemplate, configurationService);
        Map<String, Object> newProduct = getProduct("SDSIN24A0020", "SDSIN24A0021");
        newProduct.put(Field.prodStatCde, "D");
        Map<String, Object> oldProduct = getProduct("SDSIN24A0022", "SDSIN24A0023");
        List<Error> errorList = altIdValidator.validate(oldProduct, newProduct);
        Assertions.assertTrue(errorList.isEmpty());
    }

    private static Map<String, Object> getProduct(String SDSIN24A0020, String SDSIN24A0021) {
        Map<String, Object> newProduct = new HashMap<>();
        List<Map<String, Object>> newProductList = new ArrayList<>();
        newProductList.add(new Document().append(Field.prodCdeAltClassCde, "M").append(Field.prodAltNum, SDSIN24A0020));
        newProductList.add(new Document().append(Field.prodCdeAltClassCde, "P").append(Field.prodAltNum, SDSIN24A0021));
        newProduct.put(Field.altId, newProductList);
        return newProduct;
    }


}
