package com.hhhh.group.secwealth.mktdata.common.validator.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceEntity implements Serializable {

    private static final long serialVersionUID = 3879473643068526005L;

    /** The req parameters. */
    private String[] reqParameters;

    /** The req list parameters. */
    private String[] reqListParameters;

    /** The validators. */
    private List<Validator> validators = new ArrayList<Validator>();

    /** The service name. */
    private String serviceName;

    /** The request class name. */
    private String requestClassName;

    public String[] getReqParameters() {
        return this.reqParameters;
    }

    public void setReqParameters(final String[] reqParameters) {
        this.reqParameters = reqParameters;
    }

    public String[] getReqListParameters() {
        return this.reqListParameters;
    }

    public void setReqListParameters(final String[] reqListParameters) {
        this.reqListParameters = reqListParameters;
    }

    public List<Validator> getValidators() {
        return this.validators;
    }

    public void setValidators(final List<Validator> validators) {
        this.validators = validators;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRequestClassName() {
        return this.requestClassName;
    }

    public void setRequestClassName(final String requestClassName) {
        this.requestClassName = requestClassName;
    }

}
