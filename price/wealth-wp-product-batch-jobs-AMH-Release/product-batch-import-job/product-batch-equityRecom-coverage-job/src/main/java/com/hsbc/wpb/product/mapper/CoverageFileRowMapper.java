package com.dummy.wpb.product.mapper;

import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

import java.time.LocalDateTime;

@Slf4j
public class CoverageFileRowMapper implements RowMapper<EquityRecommendationsPo> {

    private String[] currentRow;
    private static final String STRING = "String";
    private static final String DOUBLE = "Double";

    @Override
    public EquityRecommendationsPo mapRow(RowSet rowSet) {
        currentRow = rowSet.getCurrentRow();

        EquityRecommendationsPo coverage = new EquityRecommendationsPo();
        try {
            coverage.setReuters((String) getRowValue(2,STRING));
            coverage.setRating((String)getRowValue(4,STRING));
            coverage.setTargetPrice((Double)getRowValue(5,DOUBLE));
            coverage.setUpside((Double)(getRowValue(36,DOUBLE)));
            coverage.setUrl((String)getRowValue(9,STRING));
            coverage.setIsin((String)getRowValue(11,STRING));
            coverage.setSector((String)getRowValue(17,STRING));
            coverage.setIndustryGroup((String)getRowValue(19,STRING));
            coverage.setForwardPe((Double)(getRowValue(29,DOUBLE)));
            coverage.setForwardPb((Double)(getRowValue(30,DOUBLE)));
            coverage.setForwardDividendYield((Double)(getRowValue(31,DOUBLE)));
            coverage.setHistoricDividendYield((Double)(getRowValue(32,DOUBLE)));
            coverage.setRecommended("N");
        } catch (Exception e) {
            String msg = e.getMessage() + " in CoverageFileRowMapper. Error row num: " + (rowSet.getCurrentRowIndex() + 1);
            coverage.setErrMsg(msg);
            log.error(msg);
        }
        if (coverage.getReuters() == null && coverage.getRating() == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        coverage.setRecCreatDtTm(now);
        coverage.setRecUpdtDtTm(now);
        return coverage;
    }

    private Object getRowValue(int columnIndex, String dataType) {
        if(currentRow != null && currentRow.length >= columnIndex+1 && StringUtils.isNoneEmpty(currentRow[columnIndex])) {
            if (StringUtils.equalsIgnoreCase(dataType,STRING)) {
                return currentRow[columnIndex];
            }else if (StringUtils.equalsIgnoreCase(dataType,DOUBLE)) {
                return Double.parseDouble(currentRow[columnIndex]);
            }
        }
        return null;
    }

}
