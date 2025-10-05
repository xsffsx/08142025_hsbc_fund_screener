package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.model.ProductValidationResult;
import com.dummy.wmd.wpc.graphql.service.ProductService;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import com.dummy.wmd.wpc.graphql.utils.OperationInputUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

import static com.mongodb.client.model.Filters.*;

/**
 * Validate product fields that did not performed by GraphQL framework, eg.
 *      Cross field validation (in one product)
 *      prodTypeCde against ReferenceData
 *      ASETVOLCLSCDE
 *      ASETGEOLOC
 *      ASETSICALLOC
 */
@SuppressWarnings("java:S3740")
@Slf4j
@Component
public class ProductValidator {
    private ValidatorManager validatorManager;
    private final ProductService productService;

    private final List<ChangeValidator> changeValidators;

    public ProductValidator(ValidatorManager validatorManager, ProductService productService, List<ChangeValidator> changeValidators){
        this.validatorManager = validatorManager;
        this.productService = productService;
        this.changeValidators = changeValidators;
    }

    /**
     * do cross product validation, make sure no active product with the same product code exist
     * countryCode + groupMember + productCode + A, if existed, return an error
     * @param product
     * @return
     */
    private List<Error> crossProductValidate(Map<String, Object> oldProduct, Map<String, Object> product) {
        List<Error> errors = new ArrayList<>();
        if(MapUtils.isEmpty(product)) {
            return errors;
        }

        boolean shouldValidate = (null == oldProduct)
                || Stream.of(Field.ctryRecCde, Field.grpMembrRecCde, Field.prodAltPrimNum, Field.prodStatCde, Field.prodTypeCde)
                .anyMatch(field -> !Objects.equals(oldProduct.get(field), product.get(field)));
        if (!shouldValidate) {
            return errors;
        }

        Long id = DocumentUtils.getLong(product, Field._id);
        String ctryRecCde = (String) product.get(Field.ctryRecCde);
        String grpMembrRecCde = (String) product.get(Field.grpMembrRecCde);
        String prodAltPrimNum = (String) product.get(Field.prodAltPrimNum);
        String prodStatCde = (String) product.get(Field.prodStatCde);
        String prodTypeCde = (String) product.get(Field.prodTypeCde);
        String delistedStatCde = "D";
        // Case1: All status code to Delisted status code: No need to apply any validation, the update action allowed

        // Case2: All status code to non-delisted status:
        if(!delistedStatCde.equals(prodStatCde)) {
            Bson filter = and(
                    ne(Field._id, id),  // for update case, the product itself should be already there, exclude it
                    eq(Field.ctryRecCde, ctryRecCde),
                    eq(Field.grpMembrRecCde, grpMembrRecCde),
                    eq(Field.prodAltPrimNum, prodAltPrimNum),
                    eq(Field.prodTypeCde, prodTypeCde),
                    ne(Field.prodStatCde, delistedStatCde)
            );
            Long count = productService.countProductByFilter(filter);

            // 2.1) If there have a existing product code with non-delisted status(like A, C, P,S etc.) and the same prodTypeCde, if have then should display the validation, like "There have an existing product code with already)
            if (count > 0) {
                String code = "prodAltPrimNum@duplication";
                errors.add(new Error("$", code, "Active product with the same primary code exists: P=" + prodAltPrimNum));
            }
            // 2.2) If there have no existing product code with non-delisted status(like A, C, P,S etc.), the product status code update action will be allowed.
        }
        return errors;
    }

    /**
     * Do validate for the final product saved in the database.
     *
     * @param oldProduct some validation need to pass oldProduct, like: altId. If we can get only changing altId, that will help to save some time.
     * @param product
     * @return
     */
    public List<Error> validate(Map<String, Object> oldProduct, Map<String, Object> product, List<OperationInput> operations) {
        // do cross product validations, in case there is violations, return errors directly
        List<Error> errors = crossProductValidate(oldProduct, product);
        if (CollectionUtils.isNotEmpty(errors)) {
            return errors;
        }


        List<String> changedJsonPaths = OperationInputUtils.extractAllPaths(operations);

        for (ChangeValidator validator : changeValidators) {
            boolean skip = CollectionUtils.isNotEmpty(operations)
                    && CollectionUtils.isNotEmpty(validator.interestJsonPaths())
                    && !CollectionUtils.containsAny(changedJsonPaths, validator.interestJsonPaths());

            if (skip){
                continue;
            }

            List<Error> errs = validator.validate(oldProduct, product);
            if (CollectionUtils.isNotEmpty(errs)) {
                return errs;
            }
        }

        // Check the exists of ctryRecCde, grpMembrRecCde, prodTypeCde first, and then put them into the validation context.
        long start = System.currentTimeMillis();
        ValidationContext context = new ValidationContext(product);
        context.pushNestedPath("");
        validateMap(product, context);
        context.popNestedPath();
        errors = context.getViolations();
        log.info("product validate cost: {}", System.currentTimeMillis() - start);
        return errors;
    }

    public List<Error> validate(Map<String, Object> product) {
        return validate(null, product, Collections.emptyList());
    }

    private void validateMap(Map<String, Object> data, ValidationContext context){
        // validate all direct sub fields for "required" rule
        // To avoid one potential issue here, eg. a mandatory field is not in the Map, it won't be checked in this case,
        // mandatory fields of given object should be checked if the required rule exists
        log.debug("Validate required rules");
        List<String> requiredFields = validatorManager.getDirectSubFields(context.getNestedPath());
        requiredFields.forEach(field -> {
            context.pushNestedPath(field);
            validateAttribute(data.get(field), true, context);
            context.popNestedPath();
        });

        // validate all direct sub fields except "required" rule
        log.debug("Validate other rules except required");
        for(Map.Entry<String,Object> entry: data.entrySet()){
            Object value = entry.getValue();

            context.pushNestedPath(entry.getKey());
            if(value instanceof Map){           // handle object
                validateMap((Map)value, context);
            } else if(value instanceof List) {  // handle array
                validateList((List)value, context);
            } else {
                validateAttribute(value, false, context);
            }
            context.popNestedPath();
        }
    }

    private void validateList(List list, ValidationContext context) {
        // validate the list itself
        validateAttribute(list, false, context);

        // validate list elements in case there is
        for(int i=0; i<list.size(); i++){
            Object item = list.get(i);
            context.pushNestedPath(i);
            if(item instanceof Map){
                validateMap((Map)item, context);
            } else {
                validateAttribute(item, false, context);
            }
            context.popNestedPath();
        }
    }

    /**
     * Validate a scalar attribute.
     *
     * @param value
     * @param validateRequired true = validate "required" rule only; false = validate rules except "required"
     * @param context
     */
    private void validateAttribute(Object value, boolean validateRequired, ValidationContext context) {
        log.debug("validate: {}", context.getNestedPath());
        List<Validator> validators = validatorManager.getValidators(context, validateRequired);
        for(Validator validator: validators){
            if(validator.support(value)) {
                validator.validate(value, context);
            }
        }
    }
}
