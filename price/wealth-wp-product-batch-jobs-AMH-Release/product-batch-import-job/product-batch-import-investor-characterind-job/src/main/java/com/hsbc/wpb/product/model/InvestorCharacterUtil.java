package com.dummy.wpb.product.model;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.InvestorCharacter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.dummy.wpb.product.constant.BatchConstants.DELISTED;

public class InvestorCharacterUtil {

    private InvestorCharacterUtil(){}

    public static final String EFFECTIVE_DATE = "00000000";
    public static final String EFFECTIVE_DATE_FORMAT = "yyyyMMdd";

    public static LocalDate convertDervEffDt(String prdDervRvsEffDt) {
        if (StringUtils.isBlank(prdDervRvsEffDt)) {
            return null;
        }

        if (StringUtils.equals(prdDervRvsEffDt, EFFECTIVE_DATE)) {
            return null;
        }

        return LocalDate.parse(prdDervRvsEffDt, DateTimeFormatter.ofPattern(EFFECTIVE_DATE_FORMAT));
    }

    public static String convertDervEffDtStr(String prdDervRvsEffDt) {
        return ObjectUtils.toString(convertDervEffDt(prdDervRvsEffDt),null);
    }


    public static String toProductKeyInfo(InvestorCharacter character) {
        return String.format("(ctryRecCde: %s, grpMembrRecCde: %s, prodTypeCde: %s, prodAltPrimNum: %s).",
                character.getCtryRecCde(), character.getGrpMembrRecCde(), character.getInternalProdTypeCde(), character.getProdAltPrimNum());
    }

    public static Query toMongoQuery(InvestorCharacter character) {
        return new Query()
                .addCriteria(Criteria.where(Field.ctryRecCde).is(character.getCtryRecCde()))
                .addCriteria(Criteria.where(Field.grpMembrRecCde).is(character.getGrpMembrRecCde()))
                .addCriteria(Criteria.where(Field.prodTypeCde).is(character.getInternalProdTypeCde()))
                .addCriteria(Criteria.where(Field.prodAltPrimNum).is(character.getProdAltPrimNum()))
                .addCriteria(Criteria.where(Field.prodStatCde).ne(DELISTED));
    }

}
