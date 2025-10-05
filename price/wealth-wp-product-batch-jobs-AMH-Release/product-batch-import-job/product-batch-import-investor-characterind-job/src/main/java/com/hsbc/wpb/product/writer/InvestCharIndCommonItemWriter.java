package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.InvestorCharacter;
import com.dummy.wpb.product.model.graphql.Operation;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.dummy.wpb.product.ImportInvestorCharacterIndApplication.JOB_NAME;
import static com.dummy.wpb.product.model.InvestorCharacterUtil.*;

@Slf4j
@Component
public class InvestCharIndCommonItemWriter implements ItemWriter<InvestorCharacter> {

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MongoTemplate mongoTemplate;

    public static final String ADD_FUTURE_PLAN = "A";
    public static final String CHANGE_FUTURE_PLAN = "C";
    public static final String CANCLE_FURTURE_PLAN = "D";
    public static final String CANCLE_FURTURE_PLAN_UPDATE_IC_IMMEDIATELY = "U";

    public static final String SIGNNATURE = JOB_NAME + " -- COMMON step";

    @Override
    public void write(List<? extends InvestorCharacter> itemList) throws Exception {
        for (InvestorCharacter character : itemList) {
            List<Operation> operations = doWrite(character);

            if (CollectionUtils.isNotEmpty(operations)) {
                ProductBatchUpdateInput updateInput = new ProductBatchUpdateInput();
                updateInput.setOperations(operations);
                updateInput.setFilter(toMongoQuery(character).getQueryObject());
                ProductBatchUpdateResult updateResult = productService.batchUpdateProduct(updateInput);
                updateResult.logUpdateResult(log);
            }
        }
    }

    protected List<Operation> doWrite(InvestorCharacter character) {
        List<Operation> operations = null;
        String maintCde = character.getMaintCde();
        switch (maintCde) {
            case CANCLE_FURTURE_PLAN:
                // don't need to update fieldGroupCtoffDtTm
                cancleFuturePlan(character);
                break;

            case CANCLE_FURTURE_PLAN_UPDATE_IC_IMMEDIATELY:
                operations = cancleFuturePlanWithUpdateICImmediately(character);
                break;

            case ADD_FUTURE_PLAN:
            case CHANGE_FUTURE_PLAN:
                String prodDervRvsEffDt = character.getProdDervRvsEffDt();

                if (EFFECTIVE_DATE.equals(prodDervRvsEffDt)) {
                    operations = addOrChangeFuturePlanImmediately(character);
                } else {
                    // don't need to update fieldGroupCtoffDtTm
                    addOrChangeFuturePlan(character);
                }
                break;

            default:
        }
        return operations;
    }

    private void addOrChangeFuturePlan(InvestorCharacter character) {
        Update update = new Update()
                .set(Field.prodDervtRvsCde, character.getProdDervtRvsCde())
                .set(Field.prdDervRvsEffDt, convertDervEffDt(character.getProdDervRvsEffDt()))
                .set(Field.lastUpdatedBy, SIGNNATURE)
                .set(Field.recUpdtDtTm, new Date());
        mongoTemplate.updateFirst(toMongoQuery(character), update, CollectionName.product.name());
        log.info("Product has been updated{}", toProductKeyInfo(character));
    }

    private List<Operation> addOrChangeFuturePlanImmediately(InvestorCharacter character) {
        List<Operation> operations = new LinkedList<>();
        operations.add(new Operation("put", Field.prodDervtCde, character.getProdDervtRvsCde()));
        operations.add(new Operation("put", Field.prodDervtRvsCde, character.getProdDervtRvsCde()));
        operations.add(new Operation("put", Field.prdDervRvsEffDt, convertDervEffDtStr(character.getProdDervRvsEffDt())));
        operations.add(new Operation("put", Field.lastUpdatedBy, SIGNNATURE));
        return operations;
    }

    private List<Operation> cancleFuturePlanWithUpdateICImmediately(InvestorCharacter character) {
        List<Operation> operations = new LinkedList<>();
        operations.add(new Operation("put", Field.prodDervtCde, character.getProdDervtRvsCde()));
        operations.add(new Operation("put", Field.prodDervtRvsCde, null));
        operations.add(new Operation("put", Field.prdDervRvsEffDt, null));
        operations.add(new Operation("put", Field.lastUpdatedBy,  SIGNNATURE));
        return operations;
    }

    private void cancleFuturePlan(InvestorCharacter character) {
        Update update = new Update()
                .set(Field.prodDervtRvsCde, null)
                .set(Field.prdDervRvsEffDt, null)
                .set(Field.lastUpdatedBy, SIGNNATURE)
                .set(Field.recUpdtDtTm, new Date());
        mongoTemplate.updateFirst(toMongoQuery(character), update, CollectionName.product.name());
        log.info("Product has been updated{}", toProductKeyInfo(character));
    }
}
