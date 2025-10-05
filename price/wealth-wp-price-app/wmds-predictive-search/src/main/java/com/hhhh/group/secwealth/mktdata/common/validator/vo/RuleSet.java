/*
 */
package com.hhhh.group.secwealth.mktdata.common.validator.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.ListUtils;

/**
 * Validator is an interface describing a Java class that can perform Object
 * validation. The object which is validated by this method can have various
 * type, and this class has a method to confirm that whether it can validate a
 * type or not.
 * 
 */
public class RuleSet implements Serializable {

    private static final long serialVersionUID = 102071623532985718L;

    private String type;

    private String fields;

    private String errorKey;

    private String associateFields;

    public void preValidate(final Object obj) throws Exception {}

    @SuppressWarnings("unchecked")
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {
        return ListUtils.EMPTY_LIST;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getFields() {
        return this.fields;
    }

    public void setFields(final String fields) {
        this.fields = fields;
    }

    public String getErrorKey() {
        return this.errorKey;
    }

    public void setErrorKey(final String errorKey) {
        this.errorKey = errorKey;
    }

    public String getAssociateFields() {
        return this.associateFields;
    }

    public void setAssociateFields(final String associateFields) {
        this.associateFields = associateFields;
    }
}
