package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.model.WrtsUndlStockRecord;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import static com.dummy.wpb.product.constant.WrtsUndlStockRecordColumnName.*;

public class WrtsUndlStockDetailFieldSetMapper implements FieldSetMapper<WrtsUndlStockRecord> {
    @Override
    public WrtsUndlStockRecord mapFieldSet(FieldSet fs) {
        return new WrtsUndlStockRecord(
                fs.readString(RECORD_TYPE),
                null,
                fs.readString(SECURITY_CODE),
                fs.readString(SECURITY_TYPE),
                fs.readString(UNDERLYING_CODE)
        );
    }
}
