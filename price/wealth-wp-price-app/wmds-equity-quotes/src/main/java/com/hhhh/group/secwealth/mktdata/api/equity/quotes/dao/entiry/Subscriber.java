/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * <p>
 * <b> This entity holds the subscriber information, which are going to be
 * persisted. </b>
 * </p>
 */
@Getter
@Setter
@Entity
@Table(name = "SCRIB")
public class Subscriber implements Serializable {

    private static final long serialVersionUID = -7870254315213569041L;

    @Id
    @Column(name = "CUST_SCRIB_ID")
    private String subscriberId;

    @Column(name = "CUST_SCRIB_TYPE_CDE")
    @Enumerated(EnumType.STRING)
    private SubscriberType subscriberType;

    @Column(name = "CTRY_SCRIB_NAME")
    private String country;

    @Column(name = "USER_UPDT_NUM")
    private String updatedBy;

    @Column(name = "REC_UPDT_DT_TM")
    private Date updatedOn;

    @Override
    public String toString() {
        return "Subscriber [subscriberId=" + subscriberId + ", subscriberType=" + subscriberType + ", country=" + country
            + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((subscriberId == null) ? 0 : subscriberId.hashCode());
        result = prime * result + ((subscriberType == null) ? 0 : subscriberType.hashCode());
        result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
        result = prime * result + ((updatedOn == null) ? 0 : updatedOn.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Subscriber other = (Subscriber) obj;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (subscriberId == null) {
            if (other.subscriberId != null)
                return false;
        } else if (!subscriberId.equals(other.subscriberId))
            return false;
        if (subscriberType != other.subscriberType)
            return false;
        if (updatedBy == null) {
            if (other.updatedBy != null)
                return false;
        } else if (!updatedBy.equals(other.updatedBy))
            return false;
        if (updatedOn == null) {
            if (other.updatedOn != null)
                return false;
        } else if (!updatedOn.equals(other.updatedOn))
            return false;
        return true;
    }


   


}
