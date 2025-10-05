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

import java.util.Arrays;
import java.util.List;

import static com.dummy.wpb.product.ImportInvestorCharacterIndApplication.JOB_NAME;
import static com.dummy.wpb.product.model.InvestorCharacterUtil.toMongoQuery;

@Slf4j
public class InvestCharIndDailyUpdateItemWriter implements ItemWriter<InvestorCharacter> {

    @Autowired
    ProductService productService;


    @Override
    public void write(List<? extends InvestorCharacter> characterList) throws Exception {
        for (InvestorCharacter character : characterList) {

            ProductBatchUpdateInput updateInput = new ProductBatchUpdateInput();
            updateInput.setFilter(toMongoQuery(character).getQueryObject());

            Operation operation1 = new Operation("put", Field.prodDervtCde, character.getProdDervtRvsCde());
            Operation operation2 = new Operation("put", Field.lastUpdatedBy, JOB_NAME + "-- DAILY_UPDATE step");
            updateInput.setOperations(Arrays.asList(operation1, operation2));

            ProductBatchUpdateResult updateResult = productService.batchUpdateProduct(updateInput);
            updateResult.logUpdateResult(log);
        }
    }
}
