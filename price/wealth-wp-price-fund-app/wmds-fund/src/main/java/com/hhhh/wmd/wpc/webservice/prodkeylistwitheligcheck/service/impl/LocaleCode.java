
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "localeCode", propOrder = {
    "localeNationalLanguageSupportCode"
})
public class LocaleCode {

    protected String localeNationalLanguageSupportCode;


    public String getLocaleNationalLanguageSupportCode() {
        return localeNationalLanguageSupportCode;
    }


    public void setLocaleNationalLanguageSupportCode(String value) {
        this.localeNationalLanguageSupportCode = value;
    }

}
