package com.dummy.wpb.product.component;

import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

@Slf4j
public class EquityRecommendationsPoValidator implements Validator<EquityRecommendationsPo> {

    @Override
    public void validate(EquityRecommendationsPo equityRecom) throws ValidationException {
        if (StringUtils.isNotBlank(equityRecom.getErrMsg())) {
            throw new ValidationException(equityRecom.getErrMsg());
        }
    }
}
