package com.hhhh.group.secwealth.mktdata.elastic.configuration;

import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.Product;
import com.hhhh.group.secwealth.mktdata.elastic.component.ProductEntities;
import com.hhhh.group.secwealth.mktdata.elastic.util.CastorConverter;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalInitializationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class ProductEntityConfig {

    @Value("${predsrch.mappingFile}")
    private String mappingFile;

    @Value("${predsrch.productEntityPath}")
    private String configFilePath;

    private static final String START_TAG = "[t]";

    private static final String END_TAG = "[/t]";

    @Bean(value = "productEntitiesMap")
    public Map<String, Product> initProductEntities() {
        try {
            final URL mappingURL = this.getClass().getResource(this.mappingFile);
            final URL configURL = this.getClass().getResource(this.configFilePath);
            final ProductEntities productEntities = (ProductEntities) CastorConverter.convertXMLToBean(mappingURL,
                    configURL, ProductEntities.class, true);
            Map<String, Product> productEntitiesMap = new ConcurrentHashMap<>();
            if (null != productEntities) {
                Iterator<Map.Entry<String, Product>> it = productEntities.getProductEntities().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Product> m = it.next();
                    Product product = m.getValue();
                    String text = product.getText();
                    if (null != text) {
                        text = text.replace(ProductEntityConfig.START_TAG, CommonConstants.SYMBOL_LEFT_ANGLE_BRACKETS)
                                .replace(ProductEntityConfig.END_TAG, CommonConstants.SYMBOL_RIGHT_ANGLE_BRACKETS);
                        product.setText(text);
                    }
                    String typeStr = m.getKey();
                    String[] types = typeStr.split(CommonConstants.SYMBOL_SEPARATOR);
                    for (String type : types) {
                        productEntitiesMap.put(type, product);
                    }
                }
            }
            return productEntitiesMap;
        } catch (Exception e) {
            log.error("SystemException: ProductEntities, init, Can't init ProductEntities, error message is {}", e.getMessage());
            throw new IllegalInitializationException(ExCodeConstant.EX_CODE_ILLEGAL_INITIALIZATION);
        }
    }
}
