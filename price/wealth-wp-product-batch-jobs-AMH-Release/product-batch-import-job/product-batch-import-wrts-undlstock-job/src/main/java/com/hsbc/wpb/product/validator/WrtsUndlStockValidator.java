package com.dummy.wpb.product.validator;

import com.dummy.wpb.product.model.WrtsUndlStockRecord;
import com.dummy.wpb.product.utils.ImportWrtsUndlStockUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.dummy.wpb.product.constant.WrtsUndlStockRecordType.HEADER_RECORD_TYPE;
import static com.dummy.wpb.product.constant.WrtsUndlStockRecordType.TRAILER_RECORD_TYPE;
import static com.dummy.wpb.product.constant.WrtsUndlStockSecurityType.*;
import static com.dummy.wpb.product.utils.ImportWrtsUndlStockUtils.incrementReadCount;
import static com.dummy.wpb.product.utils.ImportWrtsUndlStockUtils.incrementSkippedCount;

@Component
@StepScope
@Slf4j
public class WrtsUndlStockValidator implements Validator<WrtsUndlStockRecord> {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public void validate(WrtsUndlStockRecord rec) throws ValidationException {
        checkHeaderOrTrailerRecord(rec.getRecordType());
        incrementReadCount(stepExecution);
        validateSecurityType(rec);
    }

    private void checkHeaderOrTrailerRecord(String recordType) throws ValidationException {
        if (HEADER_RECORD_TYPE.equals(recordType)) {
            throw new ValidationException("This is the header record. Skipping.");
        } else if (TRAILER_RECORD_TYPE.equals(recordType)) {
            throw new ValidationException("This is the trailer record. Skipping.");
        }
    }

    private void validateSecurityType(WrtsUndlStockRecord rec) throws ValidationException {
        if (!Arrays.asList(WRNT, DWAR, CBBC)
                   .contains(rec.getSecurityType())) {
            String message = String.format(
                    "%s: This is not a WRTS record. Skipping.",
                    ImportWrtsUndlStockUtils.getRecordInfoString(rec)
            );
            log.info(message);
            incrementSkippedCount(stepExecution);
            throw new ValidationException(message);
        }
    }
}
