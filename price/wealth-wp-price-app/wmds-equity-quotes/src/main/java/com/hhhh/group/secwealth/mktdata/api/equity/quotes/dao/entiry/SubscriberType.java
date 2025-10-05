/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;


/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
public enum SubscriberType {

    // http://stackoverflow.com/questions/2493002/how-to-create-a-static-enum-with-a-value-that-has-a-hyphen-symbol-in-java
    PROF("PROF"), NONPROF("NON-PROF");

    /**
     * @return the memberType
     */
    public String getMemberType() {
        return this.memberType;
    }


    /**
     * @param memberType
     *            the memberType to set
     */
    public void setMemberType(final String memberType) {
        this.memberType = memberType;
    }


    private String memberType;


    SubscriberType(final String memberType) {
        this.memberType = memberType;
    }

    @Override
    public String toString() {
        return this.memberType;
    }

}
