package com.dummy.wmd.wpc.graphql.constant;

/**
 * DocType should keep the same as the collection name
 */
public enum DocType {
    product("ProductInput"),
    pb_product,
    amendment,
    aset_voltl_class_char("AssetVolatilityClassCharInput"),
    aset_voltl_class_corl("AssetVolatilityClassCorlInput"),
    chanl_comn_cde,
    fin_doc,
    fin_doc_hist("FinDocAmendmentInput"),
    fin_doc_upld,
    prod_type_fin_doc,
    reference_data("ReferenceDataInput"),
    staff_license_check("StaffLicenseCheckInput"),
    file_upload,
    product_customer_eligibility("ProductCustomerEligibilityInput"),
    product_staff_eligibility("ProductStaffEligibilityInput"),
    product_prod_relation("ProductRelationInput"),
    chanl_related_fileds("ChanlRelatedFieldsInput");

    private String inputTypeName;
    DocType() {
        this(null);
    }

    DocType(String inputTypeName) {
        this.inputTypeName = inputTypeName;
    }

    public String getInputTypeName() {
        return inputTypeName;
    }

}
