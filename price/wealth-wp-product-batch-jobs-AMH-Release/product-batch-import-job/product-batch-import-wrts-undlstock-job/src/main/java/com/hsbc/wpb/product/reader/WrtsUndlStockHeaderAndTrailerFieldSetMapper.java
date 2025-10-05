package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.model.WrtsUndlStockRecord;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import static com.dummy.wpb.product.constant.WrtsUndlStockRecordColumnName.HEADER_OR_TRAILER_CONTENT;
import static com.dummy.wpb.product.constant.WrtsUndlStockRecordColumnName.RECORD_TYPE;

public class WrtsUndlStockHeaderAndTrailerFieldSetMapper implements FieldSetMapper<WrtsUndlStockRecord> {

    @Override
    public WrtsUndlStockRecord mapFieldSet(FieldSet fs) {
        return new WrtsUndlStockRecord(
                fs.readString(RECORD_TYPE),
                fs.readString(HEADER_OR_TRAILER_CONTENT),
                null,
                null,
                null
        );
    }
}
