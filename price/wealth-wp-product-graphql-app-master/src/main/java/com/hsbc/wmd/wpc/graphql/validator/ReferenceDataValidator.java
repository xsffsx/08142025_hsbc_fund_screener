package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Validate the target value is exist in the cdvCde list with given cdvTypeCde / cdvParntTypeCde
 */
@SuppressWarnings("java:S3740")
public class ReferenceDataValidator implements Validator {
    private ReferenceDataService referenceDataService;
    private final String cdvTypeCde;
    private final String cdvParntTypeCde;
    private String resolvedCdvTypeCde;
    private String resolvedCdvParntCde;
    private final String cdvParntCde;

    public ReferenceDataValidator(Map<String, Object> param, ReferenceDataService referenceDataService){
        this.referenceDataService = referenceDataService;
        this.cdvTypeCde = (String)param.get(Field.cdvTypeCde);
        this.cdvParntTypeCde = (String)param.get(Field.cdvParntTypeCde);
        this.cdvParntCde = (String)param.get(Field.cdvParntCde);
    }

    @Override
    public void validate(Object value, ValidationContext context) {
        if(null == value || value.toString().trim().length() == 0) { // we don't check null cdvCde
            return;
        }

        List<Bson> bsonList = new ArrayList<>();
        // country code + group member from context
        bsonList.add(eq(Field.ctryRecCde, context.getCtryRecCde()));
        bsonList.add(eq(Field.grpMembrRecCde, context.getGrpMembrRecCde()));
        resolvedCdvTypeCde = cdvTypeCde;
        if(resolvedCdvTypeCde.startsWith("${")) {
            resolvedCdvTypeCde = (String) context.resolveVariable(cdvTypeCde);
        }
        bsonList.add(eq(Field.cdvTypeCde, resolvedCdvTypeCde));
        bsonList.add(eq(Field.cdvCde, value));
        resolvedCdvParntCde = cdvParntCde;
        if(null != cdvParntTypeCde && null != cdvParntCde) {
            resolvedCdvParntCde = (String)context.resolveVariable(cdvParntCde);   // could be like prodTypeCde
            bsonList.add(eq(Field.cdvParntTypeCde, cdvParntTypeCde));
            bsonList.add(eq(Field.cdvParntCde, resolvedCdvParntCde));
        }

        long count = referenceDataService.countReferDataByFilter(and(bsonList));
        if(count == 0){
            context.addError(getName(), getDefaultMessage(value));
        }
    }

    @Override
    public boolean support(Object value) {
        if(null == value) return false;
        return value instanceof String;
    }

    @Override
    public String getName() {
        return "referenceData";
    }

    @Override
    public String getDefaultMessage(Object value) {
        Map<String, String> params = new LinkedHashMap();
        params.put(Field.cdvTypeCde, resolvedCdvTypeCde);
        params.put(Field.cdvCde, (String) value);
        if(null != cdvParntTypeCde && null != cdvParntCde) {
            params.put(Field.cdvParntTypeCde, resolvedCdvParntCde);
            params.put(Field.cdvParntCde, cdvParntCde);
        }
        return "Reference data doesn't exist: " + params;
    }

    @Override
    public String toString() {
        return "ReferenceDataValidator{" +
                "cdvTypeCde='" + cdvTypeCde + '\'' +
                ", cdvParntTypeCde='" + cdvParntTypeCde + '\'' +
                ", cdvParntCde='" + cdvParntCde + '\'' +
                '}';
    }
}
