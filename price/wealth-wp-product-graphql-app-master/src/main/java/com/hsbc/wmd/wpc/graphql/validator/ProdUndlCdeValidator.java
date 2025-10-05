package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.productConfig;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.UnderlyingConfig;
import com.dummy.wmd.wpc.graphql.service.ProductService;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProdUndlCdeValidator implements ChangeValidator {


    private final ProductService productService;

    private final Map<String, UnderlyingConfig> supportTypes;

    private final List<String> interestJsonPaths;

    public ProdUndlCdeValidator(ProductService productService, productConfig productConfig) {
        this.productService = productService;

        supportTypes = productConfig.getUnderlying()
                .stream()
                .collect(Collectors.toMap(UnderlyingConfig::getProdTypeCde, Function.identity()));

        interestJsonPaths = productConfig.getUnderlying()
                .stream()
                .map(UnderlyingConfig::getPath)
                .collect(Collectors.toList());
    }


    @Override
    public List<Error> validate(Map<String, Object> oldProduct, Map<String, Object> product) {
        List<Error> errors = new ArrayList<>();

        UnderlyingConfig underlyingConfig = supportTypes.get(product.get(Field.prodTypeCde));
        // that means this product doesn't allow to have underlying products
        if (null == underlyingConfig) {
            return errors;
        }

        List<String> instmUndlCdeList = new ArrayList<>();
        List<Map<String, Object>> instmUndlList;
        try {
            instmUndlList = JsonPath.read(product, underlyingConfig.getPath() + "[*]");
            for (Map<String, Object> instmUndl : instmUndlList) {
                Object undlCde = instmUndl.get(Field.instmUndlCde);
                if (ObjectUtils.isNotEmpty(undlCde)) {
                    instmUndlCdeList.add((String) undlCde);
                }
            }
        } catch (PathNotFoundException e) {
            return errors;
        }
        if (instmUndlCdeList.isEmpty()) {
            return errors;
        }

        Bson filter = Filters.and(
                Filters.eq(Field.ctryRecCde, product.get(Field.ctryRecCde)),
                Filters.eq(Field.grpMembrRecCde, product.get(Field.grpMembrRecCde)),
                Filters.in(Field.prodAltPrimNum, instmUndlCdeList),
                Filters.ne(Field.prodStatCde, "D")
        );

        Map<String, Map<String, Object>> existedProds = productService.getProductsByFilter(filter)
                .stream()
                .collect(Collectors.toMap(prodValid -> (String) prodValid.get(Field.prodAltPrimNum), Function.identity(), (prod1, prod2) -> prod1));

        AtomicInteger  index = new AtomicInteger(0);
        instmUndlList.forEach(instmUndl -> {
            String instmUndlCde = (String) instmUndl.get(Field.instmUndlCde);
            Map<String, Object> existedProd = existedProds.get(instmUndlCde);
            String errorPath = String.format("%s[%d]", underlyingConfig.getPath(), index.getAndIncrement());
            if (existedProd == null) {
                errors.add(new Error(errorPath, "instmUndlCde@notFound", "Underlying product doesn't exist: " + instmUndlCde));
                return;
            }
            Object prodId = existedProd.get(Field._id);
            if (!underlyingConfig.getAllowTypes().contains(existedProd.get(Field.prodTypeCde))) {
                String message = String.format("The type underlying product %s must be %s", instmUndlCde, String.join(",", underlyingConfig.getAllowTypes()));
                errors.add(new Error(errorPath, "instmUndlCde@notAllowType", message));
            } else {
                instmUndl.put(Field.prodIdUndlInstm, prodId);
                instmUndl.put(Field.recUpdtDtTm, LocalDateTime.now());
            }
        });

        return errors;
    }

    @Override
    public List<String> interestJsonPaths() {
        return interestJsonPaths;
    }
}
