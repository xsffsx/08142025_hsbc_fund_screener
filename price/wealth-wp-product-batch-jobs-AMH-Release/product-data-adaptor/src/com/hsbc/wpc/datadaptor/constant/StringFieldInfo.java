/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		StringFieldInfo
 *
 * Creation Date	Jan 20, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Jan 20, 2006
 *    CMM/PPCR No.		
 *    Programmer		cbdhkg0010
 *    Description
 * 
 */
package com.dummy.wpc.datadaptor.constant;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.common.exception.WPCBaseException;

public class StringFieldInfo extends FieldInfo {

    public final static int THROW_ERROR = 0;
    public final static int TRUNCATE_IF_LENGTH_EXCEED = 1;

    private int fieldLength;
    private int errorHandling;
    private String[] allowValues = new String[0];

    public StringFieldInfo(int in_errorHandling,
        String in_fieldName,
        int in_fieldLength,
        String[] in_allowValues) {
        super(in_fieldName);
        fieldLength = in_fieldLength;
        if (in_allowValues != null && in_allowValues.length > 0) {
            allowValues = in_allowValues;
        }
    }

    /**
     * @return Returns the fieldLength.
     */
    public int getFieldLength() {
        return fieldLength;
    }

    /**
     * @param fieldLength The fieldLength to set.
     */
    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public final static void onValidationError(String msg) throws WPCBaseException {
        final String m = "onValidationError";
        throw new WPCBaseException(StringFieldInfo.class,
            m,
            "Validation Error : " + msg);
    }

    public String validate(String value, boolean checkNullBlank) throws WPCBaseException {
        // check null
        if (checkNullBlank) {
            if (StringUtils.isBlank(value)) {
                onValidationError("Mandatory Field missing : " + getFieldName());
            }
        } else {
            if (StringUtils.isBlank(value)) {
                return null;
            }
        }
        // check length
        if (value.length() > fieldLength) {
            if (errorHandling == TRUNCATE_IF_LENGTH_EXCEED) {
                value = value.substring(fieldLength);
            } else {
                onValidationError("Exceed length(" + getFieldLength()
                    + ") : " + getFieldName() + " : " + value);
            }
        }
        // check possible values
        boolean error = false;
        for (int i = 0; i < allowValues.length; i++) {
            if (value.equals(allowValues[i])) {
            	error = false;
                break;
            }else{
            	error = true;
            }
        }
        if (error) {
            onValidationError("Values not allow : " + getFieldName()
                + " : " + value);
        }
        return value;
    }
}