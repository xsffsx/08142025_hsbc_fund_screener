package com.dummy.wmd.wpc.graphql.calc;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class AsetClassCdeFieldCalculator implements FieldCalculator{
    private ReferenceDataService referenceDataService;

    @Override
    public Object calculate(Document prod) {
        String mktInvstCde = prod.get(Field.mktInvstCde, "");
        String asetUndlCde = "";
        try {
            List<Object> list = JsonPath.read(prod, "undlAset[?(@.seqNum==1)].asetUndlCde");
            if (!list.isEmpty()) {
                asetUndlCde = (String) list.get(0);
            }
        } catch (PathNotFoundException e) {
            // ignore
        }
        String asetClassCde = mktInvstCde + asetUndlCde;

        if (StringUtils.isBlank(asetClassCde)){
            return "OH";
        }

        Bson filter = and(
                eq(Field.ctryRecCde, prod.getString(Field.ctryRecCde)),
                eq(Field.grpMembrRecCde, prod.getString(Field.grpMembrRecCde)),
                eq(Field.cdvTypeCde, "AC"),
                eq(Field.cdvCde, asetClassCde)
        );
        List<Document> documents = referenceDataService.getReferDataByFilter(filter);
        if(!documents.isEmpty()) {
            return asetClassCde;
        } else {
            return "OH";    // Others
        }
    }

    @Override
    public void setReferenceDataService(ReferenceDataService referenceDataService) {
        this.referenceDataService = referenceDataService;
    }

    @Override
    public Collection<String> interestJsonPaths() {
        return Collections.singleton("undlAset");
    }
}
