package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.InvestorCharacter;
import com.dummy.wpb.product.model.graphql.Operation;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.LinkedList;
import java.util.List;

import static com.dummy.wpb.product.ImportInvestorCharacterIndApplication.JOB_NAME;
import static com.dummy.wpb.product.model.InvestorCharacterUtil.convertDervEffDtStr;
import static com.dummy.wpb.product.model.InvestorCharacterUtil.toMongoQuery;

@Slf4j
public class InvestCharIndGraphqlItemWriter implements ItemWriter<InvestorCharacter> {
    @Autowired
    ProductService productService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void write(List<? extends InvestorCharacter> itemList) throws Exception {
        for (InvestorCharacter character : itemList) {
            List<Operation> operations = new LinkedList<>();
            operations.add(new Operation("put", Field.prodDervtCde, character.getProdDervtCde()));
            operations.add(new Operation("put", Field.prodDervtRvsCde, character.getProdDervtRvsCde()));
            operations.add(new Operation("put", Field.prdDervRvsEffDt, convertDervEffDtStr(character.getProdDervRvsEffDt())));
            operations.add(new Operation("put", Field.lastUpdatedBy, JOB_NAME + " -- INIT/MONTHLY_RECONCILIATION step"));

            ProductBatchUpdateInput updateInput = new ProductBatchUpdateInput();
            updateInput.setFilter(toMongoQuery(character).getQueryObject());
            updateInput.setOperations(operations);

            ProductBatchUpdateResult updateResult = productService.batchUpdateProduct(updateInput);
            updateResult.logUpdateResult(log);
        }
    }
}
