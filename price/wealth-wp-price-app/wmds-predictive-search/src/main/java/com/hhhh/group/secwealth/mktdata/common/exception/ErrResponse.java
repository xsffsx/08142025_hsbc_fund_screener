/*
 */
package com.hhhh.group.secwealth.mktdata.common.exception;

/**
 * The Class ErrResponse.
 */
public class ErrResponse {
    /** The response code. */
    private String responseCode;

    /** The reason code. */
    private String reasonCode;

    /** The text. */
    private String text;

    /**
     * Instantiates a new err response.
     */
    public ErrResponse() {
        super();
    }

    /**
     * Instantiates a new err response.
     * 
     * @param responseCde
     *            the response responseCode
     * @param reasonCde
     *            the reason reasonCode
     * @param text
     *            the text
     */
    public ErrResponse(final String responseCode, final String reasonCode, final String text) {
        this.responseCode = responseCode;
        this.reasonCode = reasonCode;
        this.text = text;

    }

    /**
     * Gets the response code.
     * 
     * @return the response code
     */
    public String getResponseCode() {
        return this.responseCode;
    }

    /**
     * Sets the response code.
     * 
     * @param responseCode
     *            the new response code
     */
    public void setResponseCode(final String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Gets the reason code.
     * 
     * @return the reason code
     */
    public String getReasonCode() {
        return this.reasonCode;
    }

    /**
     * Sets the reason code.
     * 
     * @param reasonCode
     *            the new reason code
     */
    public void setReasonCode(final String reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * Gets the text.
     * 
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets the text.
     * 
     * @param text
     *            the new text
     */
    public void setText(final String text) {
        this.text = text;
    }
}
