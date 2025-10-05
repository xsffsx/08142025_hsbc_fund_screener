/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.beans;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.CastorConverter;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.converter.DataConverter;

/**
 * The Class RequestMap.
 */
@Component("productEntities")
public class ProductEntities {

    // key=supportSite +"_" + product type
    /** The product entities. */
    private Map<String, Product> productEntities = new ConcurrentHashMap<String, Product>();

    /** The mapping file. */
    @Value("#{systemConfig['predsrch.mappingFile']}")
    private String mappingFile;

    /** The config file path. */
    @Value("#{systemConfig['predsrch.productEntityPath']}")
    private String configFilePath;

    /** The Constant START_TAG. */
    private final static String START_TAG = "[t]";

    /** The Constant END_TAG. */
    private final static String END_TAG = "[/t]";

    /**
     * Inits the.
     * 
     * @throws Exception
     *             the exception
     */
    @PostConstruct
    public void init() throws Exception {
        InputStream mappingIn = null;
        InputStream configIn = null;
        try {
            URL mappingFile = this.getClass().getClassLoader().getResource(this.mappingFile);
            URL configFile = this.getClass().getClassLoader().getResource(this.configFilePath);
            mappingIn = mappingFile.openStream();
            configIn = configFile.openStream();
            String mappingContent = StringUtil.streamToStringConvert(mappingIn);
            String configContent = StringUtil.streamToStringConvert(configIn);
            ProductEntities tempEntities = (ProductEntities) CastorConverter.castorXMLToBeanConvert(configContent, mappingContent,
                ProductEntities.class, false);
            if (null != tempEntities) {
                Iterator<Map.Entry<String, Product>> it = tempEntities.getProductEntities().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Product> m = it.next();
                    Product product = m.getValue();
                    String text = product.getText();
                    if (null != text) {
                        text = text.replace(ProductEntities.START_TAG, CommonConstants.SYMBOL_LEFT_ANGLE_BRACKETS).replace(
                            ProductEntities.END_TAG, CommonConstants.SYMBOL_RIGHT_ANGLE_BRACKETS);
                        product.setText(text);
                    }
                    if (null != product.getDataConverter() && null != product.getProductPackage()) {
                        DataConverter converter = (DataConverter) Class.forName(product.getDataConverter()).newInstance();

                        JAXBContext jc = JAXBContext.newInstance(product.getProductPackage());
                        Unmarshaller shaller = jc.createUnmarshaller();
                        converter.setUnmarshaller(shaller);
                        converter.setDelayConversion(product.getDelayConversion());
                        product.setConverter(converter);
                        String typeStr = m.getKey();
                        String[] types = typeStr.split(CommonConstants.SYMBOL_SEPARATOR);
                        for (String type : types) {
                            this.productEntities.put(type, product);
                        }
                    }
                }
            }
        } catch (Exception e) {
            String msg = "SystemException: ProductEntities, init, Can't init ProductEntities";
            LogUtil.error(ProductEntities.class, msg, e);
            throw new SystemException(msg, e);
        } finally {
            if (mappingIn != null) {
                mappingIn.close();
            }
            if (configIn != null) {
                configIn.close();
            }
        }

    }

    /**
     * Gets the product entities.
     * 
     * @return the product entities
     */
    public Map<String, Product> getProductEntities() {
        return this.productEntities;
    }

    /**
     * Sets the product entities.
     * 
     * @param productEntities
     *            the product entities
     */
    public void setProductEntities(final Map<String, Product> productEntities) {
        this.productEntities = productEntities;
    }

}
