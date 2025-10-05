/*
 */
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

/**
 * 
 * @author Suey Ke
 * @date Jan 31, 2015
 */
public class BondRating {

    protected String rating;

    protected String ratingAgency;

    protected String ratingAgencyCode;

    protected String ratingDate;

    public String getRating() {
        return this.rating;
    }

    public void setRating(final String rating) {
        this.rating = rating;
    }


    public String getRatingAgency() {
        return this.ratingAgency;
    }


    public void setRatingAgency(final String ratingAgency) {
        this.ratingAgency = ratingAgency;
    }


    public String getRatingAgencyCode() {
        return this.ratingAgencyCode;
    }


    public void setRatingAgencyCode(final String ratingAgencyCode) {
        this.ratingAgencyCode = ratingAgencyCode;
    }


    public String getRatingDate() {
        return this.ratingDate;
    }


    public void setRatingDate(final String ratingDate) {
        this.ratingDate = ratingDate;
    }
}
