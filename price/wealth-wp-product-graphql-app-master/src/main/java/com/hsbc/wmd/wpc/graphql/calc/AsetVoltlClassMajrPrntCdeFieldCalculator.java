package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.constant.CdvTypeCde;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class AsetVoltlClassMajrPrntCdeFieldCalculator implements FieldCalculator{
    private ReferenceDataService referenceDataService;

    @Override
    public Object calculate(Document prod) {
        // retrieve level 1 to level 2 mapping from reference data into a map
        Bson filter = and(
                eq(Field.ctryRecCde, prod.getString(Field.ctryRecCde)),
                eq(Field.grpMembrRecCde, prod.getString(Field.grpMembrRecCde)),
                eq(Field.cdvTypeCde, CdvTypeCde.ASETVOLCLSCDE)
        );
        Map<String, String> mapping = new LinkedHashMap<>();    // Level 1 to Level 2 mapping
        referenceDataService.getReferDataByFilter(filter).forEach(item -> {
            String cdvParntCde = item.getString(Field.cdvParntCde);
            if(null != cdvParntCde) {   // there is null parent code found, ignore it
                mapping.put(item.getString(Field.cdvCde), cdvParntCde);
            }
        });

        // group and sum by grouped class
        Map<String, Double> groupedClass = new LinkedHashMap<>();
        List<Map<String, Object>> classList = (List)prod.get(Field.asetVoltlClass);
        if(null == classList) {
            return null;
        }

        classList.forEach(item -> {
            String classCode = (String)item.get(Field.asetVoltlClassCde);
            if(mapping.containsKey(classCode)) {
                classCode = mapping.get(classCode);
            }
            Double percentage = (Double)item.get(Field.asetVoltlClassWghtPct);
            Double sum = groupedClass.getOrDefault(classCode, (double) 0);
            groupedClass.put(classCode, sum + percentage);
        });

        // select the entry which has max value as result
        return Collections.max(groupedClass.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public void setReferenceDataService(ReferenceDataService referenceDataService) {
        this.referenceDataService = referenceDataService;
    }

    @Override
    public Collection<String> interestJsonPaths() {
        return Collections.singleton(Field.asetVoltlClass);
    }
}
