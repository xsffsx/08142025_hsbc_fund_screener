package com.dummy.wmd.wpc.graphql.constant;

public enum CollectionName {
    amendment,
    aset_voltl_class_char,  // Asset Volatility Class Characteristic
    aset_voltl_class_corl,  // Asset Volatility Class Correlation
    chanl_comn_cde,         // Channel Communication Code
    configuration,
    data_sync_log,
    document_revision,
    prod_type_fin_doc,
    metadata,               // Product Metadata
    parallel_run_log,
    prod_prc_hist,          // Product Price History
    product,
    pb_product,
    reference_data,
    sequence,
    staff_license_check,
    prod_atrib_map,
    upload,
    file,
    file_upload,
    file_chunk,
    lock,                   // a lock collection to coordinate multiple instances operations, not yet using
    request_log,
    test_user,              // for test user and roles
    fin_doc,    // for test user and roles
    fin_doc_hist,
    fin_doc_upld,
    prod_type_chanl_attr,
    sys_parm,
    msg_proc_log
}
