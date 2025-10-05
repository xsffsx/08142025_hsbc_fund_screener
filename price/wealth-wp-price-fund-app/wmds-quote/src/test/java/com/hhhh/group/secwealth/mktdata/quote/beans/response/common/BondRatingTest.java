package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BondRatingTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new BondRating().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new BondRating().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new BondRating());
    }

    @Test
    void testSetterAndGetter() {
        BondRating obj = new BondRating();
        obj.setRating("test");
        obj.setRatingAgency("test");
        obj.setRatingAgencyCode("test");
        obj.setRatingDate("test");

        Assertions.assertNotNull(obj.getRating());
        Assertions.assertNotNull(obj.getRatingAgency());
        Assertions.assertNotNull(obj.getRatingAgencyCode());
        Assertions.assertNotNull(obj.getRatingDate());
    }
}
