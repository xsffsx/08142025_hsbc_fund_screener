
package com.hhhh.group.secwealth.mktdata.fund.constants;

public enum TypeofETF {

    ALL_ETFs("AE"), STANDARD_ETFs("SE"), LEVERAGED_ETFs("LE"), INVERSE_ETFs("INVERS_E"), INDEX_ETFs("INDEX_E"), LEVERAGED_INVERSE_ETFs(
        "LIE");

    private String typeofETF;

    TypeofETF(final String typeofETF) {
        this.typeofETF = typeofETF;
    }


    public String getTypeofETF() {
        return this.typeofETF;
    }

    public void setTypeofETF(final String typeofETF) {
        this.typeofETF = typeofETF;
    }

    public static TypeofETF fromString(final String typeofETFString) {

        if (typeofETFString == null) {
            return null;
        }

        for (TypeofETF typeofETF : TypeofETF.values()) {
            if (typeofETFString.equals(typeofETF.getTypeofETF())) {
                return typeofETF;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.typeofETF;
    }

}
