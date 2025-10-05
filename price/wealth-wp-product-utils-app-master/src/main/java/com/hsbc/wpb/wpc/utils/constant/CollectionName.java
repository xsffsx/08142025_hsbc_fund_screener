package com.dummy.wpb.wpc.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("java:S115")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionName {
    public static CollectionName getInstance(){
        return new CollectionName();
    }
    public static final String metadata = "metadata";
    public static final String aset_voltl_class_char = "aset_voltl_class_char";
    public static final String aset_voltl_class_corl = "aset_voltl_class_corl";
    public static final String configuration = "configuration";
    public static final String prod_type_fin_doc = "prod_type_fin_doc";
    public static final String product = "product";
    public static final String sequence = "sequence";
    public static final String reference_data = "reference_data";
    public static final String staff_license_check = "staff_license_check";
    public static final String data_sync_log = "data_sync_log";
    public static final String amendment = "amendment";
    public static final String prod_prc_hist = "prod_prc_hist";
    public static final String parallel_run_log = "parallel_run_log";
    public static final String request_log = "request_log";
    public static final String lock = "lock";
    public static final String pend_appove_tran = "pend_appove_tran";
    public static final String admin_log = "admin_log";
    public static final String chanl_comn_cde = "chanl_comn_cde";
    public static final String prod_atrib_map = "prod_atrib_map";
    public static final String pb_product = "pb_product";
    public static final String fin_doc = "fin_doc";
    public static final String prod_type_chanl_attr = "prod_type_chanl_attr";
    public static final String prod_type_fin_doc_type_rel = "prod_type_fin_doc_type_rel";
    public static final String fin_doc_hist = "fin_doc_hist";

    public static final String fin_doc_upld = "fin_doc_upld";

    public static final String sys_parm = "sys_parm";
    public static final String sys_actv_log = "sys_actv_log";
    public static final String log_eqty_link_invst = "log_eqty_link_invst";
    public static final String prod_name_seqs = "prod_name_seqs";
}
