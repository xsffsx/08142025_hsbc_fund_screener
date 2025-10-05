package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundholdingsdiversifi;

import java.util.List;

public class HoldingsDiversificationItem {

    private Header header;

    List<AssetClass> assetClasses;

    List<SectorClass> sectorClasses;

    List<GeographicRegion> geographicRegions;

    private String lastUpdatedDate;

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(final Header header) {
        this.header = header;
    }


    public List<AssetClass> getAssetClasses() {
        return this.assetClasses;
    }


    public void setAssetClasses(final List<AssetClass> assetClasses) {
        this.assetClasses = assetClasses;
    }


    public List<SectorClass> getSectorClasses() {
        return this.sectorClasses;
    }


    public void setSectorClasses(final List<SectorClass> sectorClasses) {
        this.sectorClasses = sectorClasses;
    }


    public List<GeographicRegion> getGeographicRegions() {
        return this.geographicRegions;
    }


    public void setGeographicRegions(final List<GeographicRegion> geographicRegions) {
        this.geographicRegions = geographicRegions;
    }


    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }


    public void setLastUpdatedDate(final String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }


    public class Header {

        private String name;

        private String prodAltNum;

        private String currency;

        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getProdAltNum() {
            return this.prodAltNum;
        }

        public void setProdAltNum(final String prodAltNum) {
            this.prodAltNum = prodAltNum;
        }

        public String getCurrency() {
            return this.currency;
        }

        public void setCurrency(final String currency) {
            this.currency = currency;
        }
    }
}
