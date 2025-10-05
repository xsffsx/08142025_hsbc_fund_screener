package com.hhhh.group.secwealth.mktdata.predsrch.svc.beans;

import com.hhhh.group.secwealth.mktdata.predsrch.svc.converter.DataConverter;

public class Product {

    private String dataConverter;

    private String productDescriptor;

    private String text;

    private String productPackage;

    private DataConverter converter;

    private long delayConversion;

    public String getDataConverter() {
        return this.dataConverter;
    }

    public void setDataConverter(final String dataConverter) {
        this.dataConverter = dataConverter;
    }

    public String getProductDescriptor() {
        return this.productDescriptor;
    }

    public void setProductDescriptor(final String productDescriptor) {
        this.productDescriptor = productDescriptor;
    }

    public String getProductPackage() {
        return this.productPackage;
    }

    public void setProductPackage(final String productPackage) {
        this.productPackage = productPackage;
    }

    public DataConverter getConverter() {
        return this.converter;
    }

    public void setConverter(final DataConverter converter) {
        this.converter = converter;
    }

    public String getText() {
        return this.text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public long getDelayConversion() {
        return this.delayConversion;
    }

    public void setDelayConversion(final long delayConversion) {
        this.delayConversion = delayConversion;
    }

}
