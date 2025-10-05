package com.dummy.wmd.wpc.graphql.error;

/**
 * Error code consists of
 *    OTPSERR_ + <Severity> + <System Short Name> + 3 Digits
 */
public enum ErrorCode {
    OTPSERR_ZGQ000,   // Unexpected error
    OTPSERR_ZGQ001,   // LDAP naming error
    OTPSERR_ZGQ002,   // Invalid JWT token error
    /** Download file error */
    OTPSERR_EGQ101,
    /** Upload file error */
    OTPSERR_EGQ102,
}
