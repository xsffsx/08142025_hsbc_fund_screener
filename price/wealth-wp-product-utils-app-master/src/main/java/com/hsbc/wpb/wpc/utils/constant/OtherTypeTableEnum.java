package com.dummy.wpb.wpc.utils.constant;

public enum OtherTypeTableEnum {

    ASET_VOLTL_CLASS_CHAR("ASET_VOLTL_CLASS_CHAR", CollectionName.aset_voltl_class_char),
    ASET_VOLTL_CLASS_CORL("ASET_VOLTL_CLASS_CORL", CollectionName.aset_voltl_class_corl),

    PROD_TYPE_FIN_DOC("PROD_TYPE_FIN_DOC", CollectionName.prod_type_fin_doc),
    PROD_SUBTP_FIN_DOC("PROD_SUBTP_FIN_DOC", CollectionName.prod_type_fin_doc),
    PROD_TYPE_STAF_LIC_CHECK("PROD_TYPE_STAF_LIC_CHECK", CollectionName.staff_license_check),
    PROD_SUBTP_STAF_LIC_CHECK("PROD_SUBTP_STAF_LIC_CHECK", CollectionName.staff_license_check),
    PROD_ATRIB_MAP("PROD_ATRIB_MAP", CollectionName.prod_atrib_map),

    CDE_DESC_VALUE("CDE_DESC_VALUE", CollectionName.reference_data),
    CDE_DESC_VALUE_CHANL_REL("CDE_DESC_VALUE_CHANL_REL", CollectionName.reference_data),

    CHANL_COMN_CDE("CHANL_COMN_CDE", CollectionName.chanl_comn_cde),
    PROD_PRC_HIST("PROD_PRC_HIST", CollectionName.prod_prc_hist);



    private String tableName;
    private String collectionName;
    OtherTypeTableEnum(String tableName, String collectionName){
        this.tableName=tableName;
        this.collectionName=collectionName;
    }
    private String tableName(){
        return this.tableName;
    }
    private String collectionName(){
        return this.collectionName;
    }
    public static String getCollectionName(String tableName){
        OtherTypeTableEnum[] eunms = values();
        for (OtherTypeTableEnum eunm : eunms){
            if(eunm.tableName().equals(tableName)) return eunm.collectionName();
        }
        return null;
    }


}
