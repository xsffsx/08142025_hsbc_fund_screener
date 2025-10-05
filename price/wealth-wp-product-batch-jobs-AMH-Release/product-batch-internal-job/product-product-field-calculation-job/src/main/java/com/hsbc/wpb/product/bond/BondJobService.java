package com.dummy.wpb.product.bond;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.constant.BatchConstants.*;

@Service
@Slf4j
public class BondJobService {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<String> getBondProdSubtpCdes(String ctryRecCde, String grpMembrRecCde) {
        //query PRODSUBTP reference data
        Criteria refDataCriteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.cdvTypeCde).is(PROD_SUBTP_CDE_REF_TYPE_CDE)
                .and(Field.cdvParntTypeCde).is(PROD_TYPE_CDE_REF_TYPE_CDE)
                .and(Field.cdvParntCde).is(BOND_CD);
        Query refDataQuery = Query.query(refDataCriteria);
        refDataQuery.fields().include(Field.cdvCde);
        return mongoTemplate
                .find(refDataQuery, Document.class, CollectionName.reference_data.name())
                .stream()
                .map(item -> item.getString(Field.cdvCde))
                .collect(Collectors.toList());
    }

    public LocalDate calculateDate(int dateType, int dateCount) {
        LocalDate calculateDate;
        if (Calendar.YEAR == dateType) {
            calculateDate = LocalDate.now().plusYears(dateCount);
        } else if (Calendar.MONTH == dateType) {
            calculateDate = LocalDate.now().plusMonths(dateCount);
        } else if (Calendar.DATE == dateType) {
            calculateDate = LocalDate.now().plusDays(dateCount);
        } else {
            log.error("launchDateType only support 1-Year,2-Month,5-Day,current value is =[" + dateType + "]");
            throw new ItemStreamException("launchDateType only support 1-Year,2-Month,5-Day,current value is =[" + dateType + "]");
        }
        return calculateDate;
    }
}
