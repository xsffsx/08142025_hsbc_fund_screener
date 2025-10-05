package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ProductValidatorTest {

    private ProductValidator productValidator;

    private ValidatorManager validatorManager = Mockito.mock(ValidatorManager.class);

    private ProductService productService = Mockito.mock(ProductService.class);


    private List<ChangeValidator> changeValidators = new ArrayList<>();

    private ProdUndlCdeValidator prodUndlCdeValidator = Mockito.mock(ProdUndlCdeValidator.class);


    @Before
    public void setup() {
        changeValidators.add(prodUndlCdeValidator);
        productValidator = new ProductValidator(validatorManager, productService, changeValidators);
    }


    @Test
    public void testValidate_invaild() {
        Map<String, Object> product = new HashMap<>();
        assertEquals(0, productValidator.validate(null, product, Collections.emptyList()).size());

        product.put(Field.ctryRecCde, "US");
        assertEquals(0, productValidator.validate(null, product, Collections.emptyList()).size());
    }

    @Test
    public void testValidate_givenNoOperations() {
        Map<String, Object> product = new HashMap<>();
        product.put(Field.ctryRecCde, "US");
        product.put(Field.grpMembrRecCde, "G001");
        product.put(Field.prodAltPrimNum, "P001");
        product.put(Field.prodStatCde, "A");
        product.put("testList", Collections.singletonList("SEC"));
        product.put("testMap", Collections.singletonMap("k", "v"));
        List<Error> errors = productValidator.validate(null, product, Collections.emptyList());

        assertEquals(0, errors.size());
    }
}