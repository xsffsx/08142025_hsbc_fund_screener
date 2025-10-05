package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.model.BondCharacter;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class BondCharacterFieldSetMapper implements FieldSetMapper<Object> {

    private String header = "HEADER";

    private String trailer = "TRAILER";

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
        StringBuilder sb = new StringBuilder();
        sb.append("File Header: [");
        sb.append(fs.readString("headerIdentifier"));
        sb.append(fs.readString("hederContent"));
        sb.append("]");
        return sb.toString();
    }

    private BondCharacter mapContent(FieldSet fs) {
        BondCharacter item = new BondCharacter();
        item.setProdAltPrimNum(fs.readString("prodAltPrimNum"));
        item.setQtyTypeCde(fs.readString("qtyTypeCde"));
        item.setProdLocCde(fs.readString("prodLocCde"));
        item.setRbpMigrInd(fs.readString("rbpMigrInd"));
        return item;
    }

    private String mapTail(FieldSet fs) {
        StringBuilder sb = new StringBuilder();
        sb.append("File Trailer: [");
        sb.append(fs.readString("trailerIdentifier"));
        sb.append(fs.readString("trailerContent"));
        sb.append("]");
        return sb.toString();
    }
}
