package com.dummy.wpc.datadaptor.constant;

public enum IngressFileStatus {
    PENDING,        // is_load_to_db and no_of_rec_to_be_proc are null
    PROCESSING,     // is_load_to_db and no_of_rec_to_be_proc are not null and has PENDING rows
    FINISHED,       // no PENDING rows
    ERROR           // ERROR due to the whole file can't be process
}
