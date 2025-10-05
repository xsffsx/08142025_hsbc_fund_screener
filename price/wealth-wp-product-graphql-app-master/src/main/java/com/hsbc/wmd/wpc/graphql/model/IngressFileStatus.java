package com.dummy.wmd.wpc.graphql.model;

public enum IngressFileStatus {
    ERROR,          // is_load_to_db is false and no_of_rec_to_be_proc is null
    PENDING,        // is_load_to_db and no_of_rec_to_be_proc are null
    PROCESSING,     // is_load_to_db and no_of_rec_to_be_proc are not null and has PENDING rows
    FINISHED        // no PENDING rows
}
