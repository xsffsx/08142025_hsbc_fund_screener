package com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch;


public class Product {

    private String dataConverter;

    private String productDescriptor;

    private String text;

    private String productPackage;

    private long delayConversion;
    
    private String nodeName;
    
    

    public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

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
