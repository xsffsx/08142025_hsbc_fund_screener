/*
 * *************************************************************** Copyright.
 * dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system or translated in any human or computer language
 * in any way or for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name ErrorCode
 *
 * Creation Date Jul 6, 2006
 *
 * Abstract The class stores constants for TMG message functions.
 *
 * Amendment History (In chronological sequence):
 *
 * Amendment Date CMM/PPCR No. Programmer Description
 */
package com.dummy.wpb.product.error;

/**
 * https://wpb-confluence.systems.example.com/display/WWS/SMART+Local+common+error+code+setup
 * <p>
 * Error code consists of
 * OTPSERR_ + <Severity> + <System Short Name> + 3 Digits
 * [Z]	email alert (email)
 * [E]	Common exception (call or email)
 * [X]	urgent (call)
 */
public enum ErrorCode {


    OTPSERR_EBJ000("Unexpected error"),   // Unexpected error

    // DP001 - DP099: Batch output file / system report error
    OTPSERR_ZBJ001("Generate {} {} output interface failed"),   // Generate {} {} output interface failed

    // DP101 - DP199: Batch incoming file upload error
    OTPSERR_EBJ101("Batch upload service error."),   // Batch upload service error
    OTPSERR_EBJ102("Product not found."),   // Product not found
    OTPSERR_EBJ103("Product create / update failed."),   // Product create / update failed
    OTPSERR_EBJ104("Cannot insert an existing record."),   // Cannot insert an existing record
    OTPSERR_EBJ105("Validation failed."),   // Validation failed
    OTPSERR_EBJ106("Cannot create / update an existing prodStatCde = 'D' record."),   // Cannot create / update an existing prodStatCde = 'D' record
    OTPSERR_EBJ107("Can not get reference data."),
    OTPSERR_EBJ108("File cannot be moved."),
    OTPSERR_EBJ109("Unprocessed Ack/Record."),
    OTPSERR_ZBJ110("Record rejected."),
    OTPSERR_EBJ110("Record rejected."),
    OTPSERR_EBJ111("Handled exception."),
    OTPSERR_EBJ112("Uncaught exception."),
    OTPSERR_EBJ113("Script Run Failed."),
    OTPSERR_EBJ114("Smart-GFIX service error.");  //  can not get reference data


    private final String desc;

    ErrorCode() {
        this("");
    }

    ErrorCode(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }


    public static final String DEFAULT_CONTACT_POINT = "Please contact WMD Support.";

    @Override
    public String toString() {
        return name() + ": " + desc();
    }

}
