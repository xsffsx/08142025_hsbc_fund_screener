package com.dummy.wpb.product.mapper;

import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.model.FinDocMapInput;

import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

public class FinDocMappingExlRowMapper implements RowMapper<FinDocMapInput> {

    private String[] currentRow;

    @Override
    public FinDocMapInput mapRow(RowSet rowSet) throws Exception {
        currentRow = rowSet.getCurrentRow();
        int currentRowIndex = rowSet.getCurrentRowIndex();
        FinDocMapInput fdi = new FinDocMapInput();
        fdi.setRecNum(currentRowIndex-2);//1,2,3,4....
        
        fdi.setActnCde(getRowValue(0));
        fdi.setCtryCde(getRowValue(1));
        fdi.setOrgnCde(getRowValue(2));
        fdi.setProdTypeCde(getRowValue(3));
        fdi.setProdSubtpCde(getRowValue(4));
        fdi.setProdId(getRowValue(5));
        fdi.setDocTypeCdeP(getRowValue(6));
        fdi.setLangCatCdeP(getRowValue(7));
        fdi.setDocSource(getRowValue(8));
        fdi.setDocTypeCde(getRowValue(9));
        fdi.setDocSubtpCde(getRowValue(10));
        fdi.setDocId(getRowValue(11));
        fdi.setLangCatCde(getRowValue(12));
        fdi.setUrl(getRowValue(13));
        fdi.setEmailAdrRpyText(getRowValue(14));
        fdi.setCustClassCde(FinDocConstants.CUST_CLASS_CDE_DEFAULT);
        
        return fdi;
    }

    private String getRowValue(int columnIndex) {
    	if ((columnIndex+1) > currentRow.length) {
    		return null;
    	} else {
    		String rowValue = currentRow[columnIndex].trim();
    		return rowValue.equals("") ? null : rowValue;
    	}  
    }

}
