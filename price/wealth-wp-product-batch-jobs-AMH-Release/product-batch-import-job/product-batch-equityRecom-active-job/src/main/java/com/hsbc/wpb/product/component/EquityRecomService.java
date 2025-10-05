package com.dummy.wpb.product.component;


import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import com.dummy.wpb.product.model.EquityRecommendations;
import com.dummy.wpb.product.model.graphql.Operation;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.BsonUtils;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.ImportEquityRecActiveJobApplication.JOB_NAME;

@Service
@Slf4j
public class EquityRecomService {

    @Autowired
    private ProductService productService;

    //find products by ISIN code first then R code
    public List<Document> productByFilters(EquityRecommendationsPo item, String ctryRecCde, String grpMembrRecCde) {
        List<Document> prodDocs = getProducts("I", item.getIsin(), ctryRecCde, grpMembrRecCde);
        if (CollectionUtils.isEmpty(prodDocs)) {
            prodDocs = getProducts("R",item.getReuters(), ctryRecCde, grpMembrRecCde);
        }
        return prodDocs;
    }

    private List<Document> getProducts(String prodCdeAltClassCde, String prodAltNum, String ctryRecCde, String grpMembrRecCde) {
        Bson filters = Filters.and(Filters.eq(Field.ctryRecCde, ctryRecCde), Filters.eq(Field.grpMembrRecCde, grpMembrRecCde),
                Filters.ne(Field.prodStatCde, "D"), Filters.elemMatch(Field.altId,
                        Filters.and(Filters.eq(Field.prodCdeAltClassCde, prodCdeAltClassCde),
                                Filters.eq(Field.prodAltNum, prodAltNum))));
        return productService.productByFilters(BsonUtils.toMap(filters));
    }

    public void updateProdByFilters(List<Document> documents, EquityRecommendationsPo item) {
        List<Object> prodIds = documents.stream().map(document -> document.get(Field.prodId)).collect(Collectors.toList());
        Map<String, Object> filterMap = BsonUtils.toMap(Filters.in(Field.prodId, prodIds));
        EquityRecommendations equityRecom = new EquityRecommendations();
        BeanUtils.copyProperties(item, equityRecom);
        List<Operation> opList = buildOps("put", Field.equityRecommendations, equityRecom);
        List<Operation> opList1 = buildOps("put", Field.lastUpdatedBy, JOB_NAME);
        opList.addAll(opList1);
        ProductBatchUpdateInput productBatchUpdateInput = new ProductBatchUpdateInput(filterMap,opList);
        ProductBatchUpdateResult productBatchUpdateResult = productService.batchUpdateProduct(productBatchUpdateInput);
        productBatchUpdateResult.logUpdateResult(log);
        if (!CollectionUtils.isEmpty(productBatchUpdateResult.getInvalidProducts())) {
            throw new productBatchException("validation failed");
        }
    }

    public List<Operation> buildOps(String opAction, String fieldPath, Object o) {
        List<Operation> ops = new LinkedList<>();
        Operation op = new Operation();
        op.setOp(opAction);
        op.setPath(fieldPath);
        op.setValue(o);
        ops.add(op);
        return ops;
    }
}
