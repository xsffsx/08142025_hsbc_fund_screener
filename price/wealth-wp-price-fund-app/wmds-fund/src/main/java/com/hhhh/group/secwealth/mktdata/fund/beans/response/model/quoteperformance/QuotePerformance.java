package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance;

import java.util.List;


public class QuotePerformance {

    private PerformanceOverMultipleTimeHorizons performanceOverMultipleTimeHorizons;
    private RiskAndReturn riskAndReturn;
    private RiskMeasures riskMeasures;


    public PerformanceOverMultipleTimeHorizons getPerformanceOverMultipleTimeHorizons() {
        return this.performanceOverMultipleTimeHorizons;
    }

    public void setPerformanceOverMultipleTimeHorizons(final PerformanceOverMultipleTimeHorizons performanceOverMultipleTimeHorizons) {
        this.performanceOverMultipleTimeHorizons = performanceOverMultipleTimeHorizons;
    }

    public RiskAndReturn getRiskAndReturn() {
        return this.riskAndReturn;
    }

    public void setRiskAndReturn(final RiskAndReturn riskAndReturn) {
        this.riskAndReturn = riskAndReturn;
    }

    public RiskMeasures getRiskMeasures() {
        return this.riskMeasures;
    }

    public void setRiskMeasures(final RiskMeasures riskMeasures) {
        this.riskMeasures = riskMeasures;
    }


    public class PerformanceOverMultipleTimeHorizons {

        List<PerformOverMutiTimeList> items;
        private String lastUpdatedDate;

        
        public List<PerformOverMutiTimeList> getItems() {
            return this.items;
        }

        
        public void setItems(final List<PerformOverMutiTimeList> items) {
            this.items = items;
        }

        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }

        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

    }

    public class TrailingReturnsAnalysis {

        List<TrailingReturnsAnalysisList> item;
        private String lastUpdatedDate;

        public List<TrailingReturnsAnalysisList> getItem() {
            return this.item;
        }

        public void setItem(final List<TrailingReturnsAnalysisList> item) {
            this.item = item;
        }

        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }

        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

    }


    public class RiskAndReturn {
        private String riskRating3Yr;
        private String returnRating3Yr;
        private String lastUpdatedDate;

        
        public String getRiskRating3Yr() {
            return this.riskRating3Yr;
        }

        
        public void setRiskRating3Yr(final String riskRating3Yr) {
            this.riskRating3Yr = riskRating3Yr;
        }

        
        public String getReturnRating3Yr() {
            return this.returnRating3Yr;
        }

        
        public void setReturnRating3Yr(final String returnRating3Yr) {
            this.returnRating3Yr = returnRating3Yr;
        }

        
        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }

        
        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }
    }


    public class RiskMeasures {
        List<RiskMeasuresList> items;
        private String lastUpdatedDate;

        
        public List<RiskMeasuresList> getItems() {
            return this.items;
        }

        
        public void setItems(final List<RiskMeasuresList> items) {
            this.items = items;
        }

        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }

        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

    }
}
