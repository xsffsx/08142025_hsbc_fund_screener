package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.ProdTypeCde;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@AllArgsConstructor
public class BondFieldsPreSetListener extends BaseChangeListener {

    @Override
    public void beforeValidation(Map<String, Object> oldProd, Map<String, Object> newProd) {
        if (ProdTypeCde.BOND_CD.equals(newProd.get(Field.prodTypeCde))
            && StringUtils.isEmpty((String)newProd.get(Field.ccyProdMktPrcCde))
            && StringUtils.isNotEmpty((String)newProd.get(Field.ccyProdCde))) {
            newProd.put(Field.ccyProdMktPrcCde, newProd.get(Field.ccyProdCde));
        }
    }

    @Override
    public Collection<String> interestJsonPaths() {
        return allInterestJsonPaths;
    }

}
