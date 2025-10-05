package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.Field;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dummy.wpb.product.SanctionUpdateJobConfiguration.STEP_NAME;

@Component
@StepScope
@Slf4j
public class SanctionUpdateTasklet implements Tasklet, StepExecutionListener {

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    @Value("#{jobParameters['prodTypeCde']}")
    private String prodTypeCde;

    @Value("${batch.supported-product-type-codes}")
    private String supportedProductTypeCodes;

    private final SanctionUpdateService sanctionUpdateService;

    public SanctionUpdateTasklet(SanctionUpdateService sanctionUpdateService) {
        this.sanctionUpdateService = sanctionUpdateService;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("=========================================================");
        log.info("{} started.", STEP_NAME);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExitStatus exitStatus = stepExecution.getExitStatus();
        if (exitStatus.equals(ExitStatus.COMPLETED)) {
            log.info("Number of products to update: {}", stepExecution.getReadCount());
            log.info("        Actual updated count: {}", stepExecution.getWriteCount());
        }
        log.info("{} ended with status {}.", STEP_NAME, exitStatus.getExitCode());
        return exitStatus;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        if (areParamsInvalid(ctryRecCde, grpMembrRecCde, prodTypeCde)) {
            stepContribution.setExitStatus(ExitStatus.FAILED);
            return RepeatStatus.FINISHED;
        }
        List<Document> productList = sanctionUpdateService.aggregateSanctionList(
                ctryRecCde,
                grpMembrRecCde,
                prodTypeCde
        );
        for (Document product : productList) {
            stepContribution.incrementReadCount();
            Long prodId = product.getLong(Field.prodId);
            String prodAltPrimNum = product.getString(Field.prodAltPrimNum);
            List<String> sanctionBuyList = product.getList("undl_sanctionBuyList", String.class);
            List<String> sanctionSellList = product.getList("undl_sanctionSellList", String.class);
            UpdateResult updateResult = sanctionUpdateService.mongoUpdate(prodId, sanctionBuyList, sanctionSellList);
            String productInfoStr = String.format(
                    "prodTypeCde: %s, prodAltPrimNum: %s, prodId: %s, sanctionBuyList: %s, sanctionSellList: %s",
                    prodTypeCde,
                    prodAltPrimNum,
                    prodId,
                    sanctionBuyList,
                    sanctionSellList
            );
            if (updateResult.getModifiedCount() == 1) {
                stepContribution.incrementWriteCount(1);
                log.info("Product ({}) has been successfully updated.", productInfoStr);
            } else {
                log.error("Failed to update product ({}).", productInfoStr);
            }
        }
        return RepeatStatus.FINISHED;
    }

    private boolean areParamsInvalid(String ctryRecCde, String grpMembrRecCde, String prodTypeCde) {
        log.info(
                "Job parameters: ctryRecCde={}, grpMembrRecCde={}, prodTypeCde={}",
                ctryRecCde,
                grpMembrRecCde,
                prodTypeCde
        );
        if (StringUtils.isAnyBlank(ctryRecCde, grpMembrRecCde, prodTypeCde)) {
            log.error(
                    "At least one of the parameters is missing. Please enter valid country record code, group member record code and product type code"
            );
            return true;
        }
        if (!supportedProductTypeCodes.contains(prodTypeCde)) {
            log.error(
                    "Product type code \"{}\" is not supported. Supported product type codes are {}.",
                    prodTypeCde,
                    supportedProductTypeCodes
            );
            return true;
        }
        return false;
    }
}
