package com.dummy.wpb.product.model;

import com.dummy.wpb.product.constant.Field;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
public class InvestorCharacterConverter implements Converter<Document, InvestorCharacter> {
    @Override
    public InvestorCharacter convert(Document document) {
        InvestorCharacter investorCharacter = new InvestorCharacter();
        investorCharacter.setCtryRecCde(document.getString(Field.ctryRecCde));
        investorCharacter.setGrpMembrRecCde(document.getString(Field.grpMembrRecCde));
        investorCharacter.setProdTypeCde(document.getString(Field.prodTypeCde));
        String prodAltPrimNum = document.containsKey(Field.prodCde) ? document.getString(Field.prodCde) : document.getString(Field.prodAltPrimNum);
        investorCharacter.setProdAltPrimNum(prodAltPrimNum);

        investorCharacter.setMaintCde(document.getString("recMaintActnCde"));
        investorCharacter.setProdMktCde(document.getString("mktProdTrdCde"));
        investorCharacter.setProdDervtCde(document.getString(Field.prodDervtCde));
        investorCharacter.setProdDervtRvsCde(document.getString(Field.prodDervtRvsCde));
        investorCharacter.setProdDervRvsEffDt(document.getString("prdDervRvsEffIntfDt"));
        investorCharacter.setInternalProdTypeCde(document.getString(Field.prodTypeCde));
        investorCharacter.setInternalProdSubTypeCde(document.getString(Field.prodSubtpCde));
        investorCharacter.setRecUpdtDtTm(document.getDate(Field.recUpdtDtTm));
        return investorCharacter;
    }
}
