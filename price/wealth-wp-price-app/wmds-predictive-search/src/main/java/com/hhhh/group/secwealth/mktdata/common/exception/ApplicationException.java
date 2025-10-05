/*
 * *************************************************************** Copyright.
 * hhhh Holdings plc 2012 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system or translated in any human or computer language
 * in any way or for any other purposes whatsoever without the prior written
 * consent of hhhh Holdings plc.
 * ***************************************************************
 *
 * Creation Date Jan 27, 2012 5:47:04 PM
 */

package com.hhhh.group.secwealth.mktdata.common.exception;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * The Class AsyncException.
 */

public class ApplicationException extends BaseException {
    private static final long serialVersionUID = -2546953550644674655L;

    /** Current Errors. */
    private final TreeSet<CommonException> errors = new TreeSet<CommonException>();

    /** Current Warning. */
    private final TreeSet<CommonException> warnings = new TreeSet<CommonException>();

    /**
     * Instantiates a new application exception.
     */
    public ApplicationException(final String message) {
        super(message);
        // initializeFields();
    }

    public ApplicationException(final String message, final CommonException error) {
        super(message);
        // initializeFields();
        this.addError(error);
    }

    public ApplicationException(final String message, final List<CommonException> errors) {
        super(message);
        // initializeFields();
        this.addErrors(errors);
    }

    public ApplicationException(final String message, final CommonException error, final CommonException warning) {
        super(message);
        // initializeFields();
        if (error != null) {
            this.addError(error);
        }
        if (warning != null) {
            this.addWarning(warning);
        }
    }

    public ApplicationException(final String message, final List<CommonException> errors, final List<CommonException> warnings) {
        super(message);
        // initializeFields();
        if (errors != null) {
            this.addErrors(errors);
        }
        if (warnings != null) {
            this.addWarnings(warnings);
        }
    }

    /**
     * Gets the errors.
     *
     * @return the errors
     */
    public Collection<CommonException> getErrors() {
        return this.errors;
    }

    /**
     * Gets the warnings.
     *
     * @return the warnings
     */
    public Collection<CommonException> getWarnings() {
        return this.warnings;
    }

    /**
     * Adds the error.
     *
     * @param error
     *            the error
     */
    public final void addError(final CommonException error) {
        this.errors.add(error);
    }

    /**
     * Adds the errors.
     *
     * @param errors
     *            the errors
     */
    public final void addErrors(final List<CommonException> errors) {
        this.errors.addAll(errors);
    }

    /**
     * Adds the warning.
     *
     * @param warning
     *            the warning
     */
    public final void addWarning(final CommonException warning) {
        this.warnings.add(warning);
    }

    /**
     * Adds the warnings.
     *
     * @param warnings
     *            the warnings
     */
    public final void addWarnings(final List<CommonException> warnings) {
        this.warnings.addAll(warnings);
    }

    /**
     * Initialize fields.
     */
    // private final void initializeFields() {
    // this.errors = new TreeSet<CommonException>();
    // this.warnings = new TreeSet<CommonException>();
    // }

    /**
     * Contains error code.
     *
     * @param errorCode
     *            the error code
     * @return true, if successful
     */
    public boolean containsErrorCode(final String errorCode) {
        if (this.getErrors().isEmpty()) {
            return false;
        }

        for (CommonException fieldCode : this.getErrors()) {
            if (fieldCode.getExceptionCode().equals(errorCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer message = new StringBuffer(super.toString());
        String seperator = System.getProperty("line.separator");
        if (this.errors != null) {
            for (CommonException exception : this.errors) {
                message.append(seperator).append(exception);
            }
        }
        if (this.warnings != null) {
            for (CommonException exception : this.warnings) {
                message.append(seperator).append(exception);
            }
        }
        return message.toString();
    }

    @Override
    public int compareTo(final Object other) {
        ApplicationException castOther = (ApplicationException) other;
        return new CompareToBuilder().append(this.errors, castOther.errors).append(this.warnings, castOther.warnings)
            .toComparison();
    }

}
