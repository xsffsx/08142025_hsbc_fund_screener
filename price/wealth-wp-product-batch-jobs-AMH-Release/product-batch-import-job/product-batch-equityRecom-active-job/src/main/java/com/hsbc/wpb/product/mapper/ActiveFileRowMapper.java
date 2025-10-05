package com.dummy.wpb.product.mapper;

import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

public class ActiveFileRowMapper implements RowMapper<EquityRecommendationsPo> {

    private String[] currentRow;

    @Override
    public EquityRecommendationsPo mapRow(RowSet rowSet) {
        currentRow = rowSet.getCurrentRow();
        if (StringUtils.isBlank(getRowValue(7))) {return null;}
        EquityRecommendationsPo active = new EquityRecommendationsPo();
        active.setReuters(getRowValue(7));
        active.setKeyHighlight(getRowValue(4));
        active.setRationale(getRowValue(12));
        active.setRisk(getRowValue(13));
        return active;
    }

    private String getRowValue(int columnIndex) {
        return currentRow != null && currentRow.length >= columnIndex+1 ?
                currentRow[columnIndex] : null;
    }

}
