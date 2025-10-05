package com.dummy.wmd.wpc.graphql.constant;

/**
 * Amendment record status code, transition in below sequence
 * draft / returned --(requestApproval)--> pending --(approve)--> approved
 *                                                 --(reject)---> rejected
 *                                                 --(return)---> returned
 */
public enum RecStatCde {
    draft,
    returned,
    pending,
    approved,
    rejected,
    deleted
}
