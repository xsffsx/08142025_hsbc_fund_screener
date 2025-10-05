package com.dummy.wpb.product.mapper;

import com.dummy.wpb.product.exception.InvalidRecordException;
import com.dummy.wpb.product.model.FinDocInput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

@Slf4j
public class FinDocExlRowMapper implements RowMapper<FinDocInput> {

    private String[] currentRow;

    @Override
    public FinDocInput mapRow(RowSet rowSet) throws Exception {
        currentRow = rowSet.getCurrentRow();
        int currentRowIndex = rowSet.getCurrentRowIndex();
        FinDocInput finDocInput = new FinDocInput();
        finDocInput.setSeqNum(finDocInput.getSeqNum() + (currentRowIndex-2));//1,2,3,4....

        finDocInput.setCtryCde(getRowValue(0));
        finDocInput.setOrgnCde(getRowValue(1));
        finDocInput.setDocTypeCde(getRowValue(2));
        finDocInput.setDocSubtypeCde(getRowValue(3));
        finDocInput.setDocId(getRowValue(4));
        finDocInput.setLangCatCde(getRowValue(5));
        finDocInput.setFormtTypeCde(getRowValue(6));
        finDocInput.setDocIncmName(getRowValue(7));
        finDocInput.setExpirDt(getRowValue(8));
        finDocInput.setEffDt(getRowValue(9));
        finDocInput.setEffTm(getRowValue(10));
        finDocInput.setDocExplnText(getRowValue(11));
        finDocInput.setProdTypeCde(getRowValue(12));
        finDocInput.setProdSubtypeCde(getRowValue(13));
        finDocInput.setProdCde(getRowValue(14));
        finDocInput.setEmailAdrRpyText(getRowValue(15));
        finDocInput.setUrgInd(getRowValue(16));

        if (isInvalidRow(finDocInput)) {
            log.error("Skip invalid record for financial document excel file.");
            throw new InvalidRecordException("Skip invalid record for financial document excel file.");
        } else {
            return finDocInput;
        }

    }

    private String getRowValue(int columnIndex) {
        if ((currentRow == null) || (currentRow.length == 0)) {
            return null;
        }
        return currentRow.length >= columnIndex+1 && StringUtils.isNotBlank(currentRow[columnIndex]) ? currentRow[columnIndex].trim() : null;
    }

    private boolean isInvalidRow(FinDocInput finDocInput) {
        return finDocInput.getCtryCde() == null && finDocInput.getOrgnCde() == null && finDocInput.getDocTypeCde() == null
                && finDocInput.getDocSubtypeCde() == null && finDocInput.getDocId() == null && finDocInput.getLangCatCde() == null
                && finDocInput.getFormtTypeCde() == null && finDocInput.getDocIncmName() == null && finDocInput.getExpirDt() == null
                && finDocInput.getEffDt() == null && finDocInput.getEffTm() == null && finDocInput.getDocExplnText() == null
                && finDocInput.getProdTypeCde() == null && finDocInput.getProdSubtypeCde() == null && finDocInput.getProdCde() == null
                && finDocInput.getEmailAdrRpyText() == null && finDocInput.getUrgInd() == null;
	}

}
