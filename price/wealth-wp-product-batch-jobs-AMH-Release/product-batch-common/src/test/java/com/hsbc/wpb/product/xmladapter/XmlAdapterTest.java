package com.dummy.wpb.product.xmladapter;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class XmlAdapterTest {

    @Test
    public void localDateAdapterTest() throws Exception {
        LocalDateAdapter localDateAdapter = new LocalDateAdapter();
        // test yyyy-MM-dd HH:mm:ss
        String str1 = "2023-01-01 12:34:56";
        LocalDate localDate1 = localDateAdapter.unmarshal(str1);
        Assert.assertEquals("2023-01-01", localDateAdapter.marshal(localDate1));
        // test yyyy-MM-dd HH:mm
        String str2 = "2023-12-31";
        LocalDate localDate2 = localDateAdapter.unmarshal(str2);
        Assert.assertEquals("2023-12-31", localDateAdapter.marshal(localDate2));
    }

    @Test
    public void localDateTimeAdapterTest() throws Exception {
        LocalDateTimeAdapter localDateAdapter = new LocalDateTimeAdapter();
        // unmarshal yyyy-MM-dd HH:mm:ss
        String str = "2023-01-01 12:34:56";
        LocalDateTime localDateTime = localDateAdapter.unmarshal(str);
        // marshal yyyy-MM-dd'T'HH:mm:ss
        Assert.assertEquals("2023-01-01T12:34:56", localDateAdapter.marshal(localDateTime));
    }

    @Test
    public void localTimeAdapterTest() throws Exception {
        LocalTimeAdapter localTimeAdapter = new LocalTimeAdapter();
        // unmarshal HH:mm:ss
        String str = "11:12:11";
        LocalTime localTime = localTimeAdapter.unmarshal(str);
        Assert.assertEquals("11:12:11", localTimeAdapter.marshal(localTime));
    }
}
