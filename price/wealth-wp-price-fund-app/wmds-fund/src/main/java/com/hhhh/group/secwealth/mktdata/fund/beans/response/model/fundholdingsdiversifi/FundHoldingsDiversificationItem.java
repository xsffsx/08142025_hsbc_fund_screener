
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundholdingsdiversifi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.helper.UtHoldingsHelper;


public class FundHoldingsDiversificationItem {

    String fundName;
    String fundSymbol;
    String fundCurrency;
    Date lastUpdateDate;
    List<UtHoldingsHelper> assetClasses = new ArrayList<UtHoldingsHelper>();
    List<UtHoldingsHelper> marketSectors = new ArrayList<UtHoldingsHelper>();
    List<UtHoldingsHelper> geoClasses = new ArrayList<UtHoldingsHelper>();


    public String getFundName() {
        return this.fundName;
    }


    public void setFundName(final String fundName) {
        this.fundName = fundName;
    }


    public String getFundSymbol() {
        return this.fundSymbol;
    }


    public void setFundSymbol(final String fundSymbol) {
        this.fundSymbol = fundSymbol;
    }


    public String getFundCurrency() {
        return this.fundCurrency;
    }


    public void setFundCurrency(final String fundCurrency) {
        this.fundCurrency = fundCurrency;
    }


    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }


    public void setLastUpdateDate(final Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }


    public List<UtHoldingsHelper> getAssetClasses() {
        return this.assetClasses;
    }


    public void setAssetClasses(final List<UtHoldingsHelper> assetClasses) {
        this.assetClasses = assetClasses;
    }


    public List<UtHoldingsHelper> getMarketSectors() {
        return this.marketSectors;
    }


    public void setMarketSectors(final List<UtHoldingsHelper> marketSectors) {
        this.marketSectors = marketSectors;
    }


    public List<UtHoldingsHelper> getGeoClasses() {
        return this.geoClasses;
    }


    public void setGeoClasses(final List<UtHoldingsHelper> geoClasses) {
        this.geoClasses = geoClasses;
    }

}
