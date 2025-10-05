package com.dummy.wpb.product.model;

import com.dummy.wpb.product.constant.Field;

import static com.dummy.wpb.product.constant.BatchConstants.CSV;
import static com.dummy.wpb.product.constant.BatchConstants.XML;

public enum InterfaceType {

    UT(XML, true),
    BOND(XML, true),
    SEC(XML, true),
    SID(XML, true),
    ELI(XML, true),
    DPS(XML, true),
    WRTS(XML, true),
    SN(XML, true),
    ALT(XML, true),
    INS(XML, true),
    PRODPRC(XML, Field.prodPrcUpdtDtTm, false),
    PRODPERFM(XML, false),
    REFDATA(XML, false),
    CUSTELIG(XML, Field.tradeElig + "." + Field.recUpdtDtTm, false),
    UT_CSV(CSV, false),
    DPS_CSV(CSV, false),
    BOND_CSV(CSV, false),
    ELI_CSV(CSV, false),
    SID_CSV(CSV, false),
    SEC_CSV(CSV, false),
    ALT_CSV(CSV, false),
    ALLPROD_CSV(CSV, false);

    public final String fileType;
    public final String deltaField;
    public final boolean hasVersion;

    InterfaceType(final String fileType, final Boolean hasVersion) {
        this(fileType, Field.recUpdtDtTm, hasVersion);
    }

    InterfaceType(final String fileType, final String deltaField, final Boolean hasVersion) {
        this.fileType = fileType;
        this.deltaField = deltaField;
        this.hasVersion = hasVersion;
    }
}
