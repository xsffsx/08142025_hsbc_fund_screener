package com.dummy.wpb.wpc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.dummy.wpb.wpc.utils.load.MetadataLoader;
import org.bson.Document;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

public class MiscTests {
    @Test
    public void testTimeZone() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse("2020-05-22 09:01:21");
        ZonedDateTime zonedDateTime = start.toInstant().atZone(ZoneId.of("UTC"));
        assertNotNull(start);
        assertNotNull(zonedDateTime);
        assertNotNull(new Date(zonedDateTime.toLocalDateTime().toEpochSecond(ZoneOffset.UTC)*1000));
    }

    @Test
    public void test2(){
        Instant instant = Instant.now();
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        assertNotNull(instant);
        assertNotNull(now);
        assertNotNull(now.toLocalDateTime());
    }

    @Test
    public void test3() throws ParseException {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String dateStr = zdt.format(formatter);
        Date utcDate = format.parse(dateStr);
        assertNotNull(utcDate);
    }

    @Test
    public void test4() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse("2020-05-22 09:01:21");
        assertNotNull(start);
        assertNotNull(DbUtils.toUTCDate(start));
    }

    @Test
    public void test5(){
        assertNotNull(Boolean.valueOf("Y"));
        assertNotNull(Boolean.valueOf("N"));
        assertNotNull(Boolean.valueOf("D"));
        assertNotNull(Boolean.valueOf("true"));
        assertNotNull(Boolean.valueOf("false"));
    }

    @Test
    public void test6(){
        assertNotNull(new Date(0));
    }

    @Test
    public void test7() {
        List<Integer> ids = Arrays.asList(6,6,2,2,5,7,3,2);
        Object result = ids.stream().collect(
                Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()));
        assertNotNull(result);
    }
}
