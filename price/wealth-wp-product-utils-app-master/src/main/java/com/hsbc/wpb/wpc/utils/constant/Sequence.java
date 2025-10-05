package com.dummy.wpb.wpc.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("java:S115")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sequence {
    public static Sequence getInstance(){
        return new Sequence();
    }
    public static final String prodId = "prodId";
    public static final String aset_voltl_class_char_id = "aset_voltl_class_char_id";
    public static final String aset_voltl_class_corl_id = "aset_voltl_class_corl_id";
    public static final String referenceDataId = "referenceDataId";
    public static final String staffLicenseCheckId = "staffLicenseCheckId";
    public static final String sysParamId = "sysParamId";
    public static final String rsrcItemIdFinDoc = "rsrcItemIdFinDoc";
}
