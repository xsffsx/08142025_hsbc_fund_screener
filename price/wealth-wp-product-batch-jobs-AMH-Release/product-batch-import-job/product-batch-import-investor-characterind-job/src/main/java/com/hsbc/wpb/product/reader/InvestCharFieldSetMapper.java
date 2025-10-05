package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.model.InvestorCharacter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;


public class InvestCharFieldSetMapper implements FieldSetMapper<Object> {

    private final String ctryRecCde;
    private final String grpMembrRecCde;

    private String header = "HEADER";

    private String trailer = "TRAILER";

    public InvestCharFieldSetMapper(String ctryRecCde, String grpMembrRecCde) {
        this.ctryRecCde = ctryRecCde;
        this.grpMembrRecCde = grpMembrRecCde;
    }

    @Override
    public Object mapFieldSet(FieldSet fs) {

        String prefix = fs.readString(0);
        if (header.equals(prefix)) {
            return mapHeader(fs);
        }

        if (trailer.equals(prefix)) {
            return mapTail(fs);
        }
        return mapContent(fs);
    }

    private String mapHeader(FieldSet fs) {
        return String.format("File Header: [%s%s]", fs.readString("headerIdentifier"), fs.readString("hederContent"));
    }

    private String mapTail(FieldSet fs) {
        return String.format("File Trailer: [%s%s]", fs.readString("trailerIdentifier"), fs.readString("trailerContent"));
    }

    private InvestorCharacter mapContent(FieldSet fs) {
        InvestorCharacter item = new InvestorCharacter();
        item.setCtryRecCde(ctryRecCde);
        item.setGrpMembrRecCde(grpMembrRecCde);
        item.setMaintCde(StringUtils.defaultIfEmpty(fs.readString("maintCode"), null));
        item.setProdAltPrimNum(fs.readString("prodCde"));
        item.setProdTypeCde(fs.readString("prodTypeCde"));
        item.setProdMktCde(StringUtils.defaultIfEmpty(fs.readString("prodMktCde"), null));
        item.setProdDervtCde(StringUtils.defaultIfEmpty(fs.readString("currentICCheckActCde"), null));
        item.setProdDervtRvsCde(StringUtils.defaultIfEmpty(fs.readString("icCheckActCde"), null));
        item.setProdDervRvsEffDt(fs.readString("icCheckEffDate"));
        item.setInternalProdTypeCde(fs.readString("hfiProdType"));
        item.setInternalProdSubTypeCde(fs.readString("hfiProdSubType"));
        item.setRecUpdtDtTm(fs.readDate("recUpdtDtTm", "yyyyMMddHHmmss"));
        return item;

    }
}
