package com.dummy.wpb.product;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImportProductPriceConfigurationTest {

    @Test
    public void test_productPriceValidator() {
        ImportProductPriceConfiguration configuration = new ImportProductPriceConfiguration();
        Assert.assertNotNull(configuration.prodPriceValidatorProcessor());
    }
}