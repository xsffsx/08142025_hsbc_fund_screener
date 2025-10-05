/*
 */
package com.hhhh.group.secwealth.mktdata.common.util;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * <b> The convertor for Country code.such as CAN to CA. </b>
 * </p>
 */
public final class CountryCodeConvertor {
    /**
     * The country code map. The key is ISO3 coutry code and the value is ISO2
     * country
     */
    private static ConcurrentHashMap<String, String> countryCodeMap = new ConcurrentHashMap<String, String>();

    /** The signle instance. */
    private static CountryCodeConvertor instance = null;

    private CountryCodeConvertor() {}

    public static synchronized CountryCodeConvertor getInstance() {
        if (CountryCodeConvertor.instance == null) {
            CountryCodeConvertor.instance = new CountryCodeConvertor();
            for (int i = 0; i < Locale.getISOCountries().length; i++) {
                Locale tempLocale = new Locale("", Locale.getISOCountries()[i]);
                CountryCodeConvertor.countryCodeMap.put(tempLocale.getISO3Country(), Locale.getISOCountries()[i]);
            }
        }
        return CountryCodeConvertor.instance;
    }

    /**
     * Gets the country code map.
     * 
     * @return the countryCodeMap
     */
    public ConcurrentHashMap<String, String> getCountryCodeMap() {
        return CountryCodeConvertor.countryCodeMap;
    }

    /**
     * Sets the country code map.
     * 
     * @param countryCodeMap
     *            the countryCodeMap to set
     */
    public void setCountryCodeMap(final ConcurrentHashMap<String, String> countryCodeMap) {
        CountryCodeConvertor.countryCodeMap = countryCodeMap;
    }

}
