package com.dummy.wpb.product.mapper;

import com.dummy.wpb.product.model.FinDocInput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.LineMapper;

public class FinDocCsvLineMapper implements LineMapper<FinDocInput> {

    private String[] currentRow;
    @Override
    public FinDocInput mapLine(String line, int lineNumber) throws Exception {
        currentRow = line.split(",");
        FinDocInput finDocInput = new FinDocInput();
        finDocInput.setSeqNum(finDocInput.getSeqNum() + (lineNumber-2));//1,2,3,4....

        finDocInput.setCtryCde(getRowValue(0));
        finDocInput.setOrgnCde(getRowValue(1));
        finDocInput.setDocTypeCde(getRowValue(2));
        finDocInput.setDocSubtypeCde(getRowValue(3));
        finDocInput.setDocId(getRowValue(4));
        finDocInput.setDocIncmName(getRowValue(5));
        finDocInput.setLangCatCde(getRowValue(6));
        finDocInput.setFormtTypeCde(getRowValue(7));
        finDocInput.setExpirDt(getRowValue(8));
        finDocInput.setEffDt(getRowValue(9));
        finDocInput.setEffTm(getRowValue(10));
        finDocInput.setEmailAdrRpyText(getRowValue(11));
        finDocInput.setUrgInd(getRowValue(12));

        finDocInput.setDocExplnText(null);
        finDocInput.setProdTypeCde(getRowValue(3));
        finDocInput.setProdSubtypeCde(null);
        finDocInput.setProdCde(getRowValue(4));

        return finDocInput;
    }

    private String getRowValue(int columnIndex) {
        if ((currentRow == null) || (currentRow.length == 0)) {
            return null;
        }
        return currentRow.length >= columnIndex+1 && StringUtils.isNotBlank(currentRow[columnIndex]) ? currentRow[columnIndex].trim() : null;
    }
}
