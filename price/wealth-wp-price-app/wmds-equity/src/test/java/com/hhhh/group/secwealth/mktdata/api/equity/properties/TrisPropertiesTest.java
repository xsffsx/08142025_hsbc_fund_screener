/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;

public class TrisPropertiesTest {

    @Test
    public void testGetTrisServiceWithInvalidSiteParameter() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, Map<String, String>> service = new HashMap<>();
        trisProps.setService(service);
        String defaultSite = "DEFAULT";
        Map<String, String> defaultOptions = new HashMap<>();
        service.put(defaultSite, defaultOptions);
        defaultOptions.put("realtime", "IDN_RDF");
        defaultOptions.put("delay", "dIDN_RDF");

        String[] invalidSites = {null, "", "INVALID_SITE"};
        for (int i = 0; i < invalidSites.length; i++) {
            String invalidSite = invalidSites[i];
            String result = trisProps.getTrisService(invalidSite, true);
            assertThat(result, equalTo("dIDN_RDF"));
            result = trisProps.getTrisService(invalidSite, false);
            assertThat(result, equalTo("IDN_RDF"));
        }
    }

    @Test
    public void testGetTrisServiceIncorrectInitialization() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, Map<String, String>> service = new HashMap<>();
        String site = "VALID_SITE";
        boolean delay = true;
        try {
            trisProps.getTrisService(site, delay);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }

        trisProps = new TrisProperties();
        service = new HashMap<>();
        trisProps.setService(service);
        String defaultSite = "DEFAULT";
        Map<String, String> defaultOptions = new HashMap<>();
        service.put(defaultSite, defaultOptions);
        String invalidKey = "INVALID_KEY";
        defaultOptions.put(invalidKey, "");
        site = "VALID_SITE";
        delay = true;
        try {
            trisProps.getTrisService(site, delay);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }

        trisProps = new TrisProperties();
        service = new HashMap<>();
        trisProps.setService(service);
        defaultSite = "DEFAULT";
        defaultOptions = new HashMap<>();
        service.put(defaultSite, defaultOptions);
        String invalidValue = "";
        defaultOptions.put("delay", invalidValue);
        site = "VALID_SITE";
        delay = true;
        try {
            trisProps.getTrisService(site, delay);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }
    }

    @Test
    public void testGetTrisClosureWithInvalidSiteParameter() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, String> closure = new HashMap<>();
        trisProps.setClosure(closure);
        String defaultSite = "DEFAULT";
        closure.put(defaultSite, "APP_TRIS_RBWM_SRBP_SG");

        String[] invalidSites = {null, "", "INVALID_SITE"};
        for (int i = 0; i < invalidSites.length; i++) {
            String invalidSite = invalidSites[i];
            String result = trisProps.getTrisClosure(invalidSite);
            assertThat(result, equalTo("APP_TRIS_RBWM_SRBP_SG"));
        }
    }

    @Test
    public void testGetTrisClosureIncorrectInitialization() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, String> closure = new HashMap<>();
        String site = "VALID_SITE";
        try {
            trisProps.getTrisClosure(site);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }

        trisProps = new TrisProperties();
        closure = new HashMap<>();
        trisProps.setClosure(closure);
        site = "VALID_SITE";
        String invalidValue = "";
        closure.put(site, invalidValue);
        try {
            trisProps.getTrisClosure(site);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }
    }

    @Test
    public void testGetTrisTokenAppCodeWithInvalidSiteParameter() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, String> trisTokenAppCode = new HashMap<>();
        trisProps.setTokenAppCode(trisTokenAppCode);
        String defaultSite = "DEFAULT";
        trisTokenAppCode.put(defaultSite, "MDSBE");

        String[] invalidSites = {null, "", "INVALID_SITE"};
        for (int i = 0; i < invalidSites.length; i++) {
            String invalidSite = invalidSites[i];
            String result = trisProps.getTrisTokenAppCode(invalidSite);
            assertThat(result, equalTo("MDSBE"));
        }
    }

    @Test
    public void testGetTrisTokenAppCodeIncorrectInitialization() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, String> trisTokenAppCode = new HashMap<>();
        trisProps.setTokenAppCode(trisTokenAppCode);
        String site = "VALID_SITE";
        try {
            trisProps.getTrisTokenAppCode(site);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }

        trisProps = new TrisProperties();
        trisTokenAppCode = new HashMap<>();
        trisProps.setTokenAppCode(trisTokenAppCode);
        site = "VALID_SITE";
        String invalidValue = "";
        trisTokenAppCode.put(site, invalidValue);
        try {
            trisProps.getTrisTokenAppCode(site);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }
    }

    @Test
    public void testGetTrisTokenTimezoneWithInvalidSiteParameter() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, String> trisTokenTimezone = new HashMap<>();
        trisProps.setTokenTimezone(trisTokenTimezone);
        String defaultSite = "DEFAULT";
        trisTokenTimezone.put(defaultSite, "GMT");

        String[] invalidSites = {null, "", "INVALID_SITE"};
        for (int i = 0; i < invalidSites.length; i++) {
            String invalidSite = invalidSites[i];
            String result = trisProps.getTrisTokenTimezone(invalidSite);
            assertThat(result, equalTo("GMT"));
        }
    }

    @Test
    public void testGetTrisTokenTimezoneIncorrectInitialization() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, String> trisTokenTimezone = new HashMap<>();
        trisProps.setTokenTimezone(trisTokenTimezone);
        String site = "VALID_SITE";
        try {
            trisProps.getTrisTokenTimezone(site);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }

        trisProps = new TrisProperties();
        trisTokenTimezone = new HashMap<>();
        trisProps.setTokenTimezone(trisTokenTimezone);
        site = "VALID_SITE";
        String invalidValue = "";
        trisTokenTimezone.put(site, invalidValue);
        try {
            trisProps.getTrisTokenTimezone(site);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }
    }

    @Test
    public void testGetTrisUrlIncorrectInitialization() {
        TrisProperties trisProps = new TrisProperties();
        trisProps.setUrl("");

        try {
            trisProps.getTrisUrl();
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }
    }

    @Test
    public void testInitTrisFieldMapperIncorrectInitialization() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, Map<String, Map<String, Map<String, String>>>> fieldMapper = new HashMap<>();
        trisProps.setFieldMapper(fieldMapper);
        Map<String, Map<String, Map<String, String>>> siteMapper = new HashMap<>();
        fieldMapper.put("NON_DEFAULT", siteMapper);
        try {
            Whitebox.invokeMethod(trisProps, "initTrisFieldMapper");
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        trisProps = new TrisProperties();
        fieldMapper = new HashMap<>();
        trisProps.setFieldMapper(fieldMapper);
        siteMapper = new HashMap<>();
        fieldMapper.put("DEFAULT", siteMapper);
        Map<String, Map<String, String>> productMapper = new HashMap<>();
        siteMapper.put("NON_DEFAULT", productMapper);
        try {
            Whitebox.invokeMethod(trisProps, "initTrisFieldMapper");
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        trisProps = new TrisProperties();
        fieldMapper = new HashMap<>();
        trisProps.setFieldMapper(fieldMapper);
        siteMapper = new HashMap<>();
        fieldMapper.put("DEFAULT", siteMapper);
        productMapper = new HashMap<>();
        siteMapper.put("DEFAULT", productMapper);
        Map<String, String> exchangeMapper = new HashMap<>();
        productMapper.put("NON_DEFAULT", exchangeMapper);
        try {
            Whitebox.invokeMethod(trisProps, "initTrisFieldMapper");
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        trisProps = new TrisProperties();
        fieldMapper = new HashMap<>();
        trisProps.setFieldMapper(fieldMapper);
        siteMapper = new HashMap<>();
        fieldMapper.put("DEFAULT", siteMapper);
        productMapper = new HashMap<>();
        siteMapper.put("DEFAULT", productMapper);
        exchangeMapper = new HashMap<>();
        productMapper.put("DEFAULT", exchangeMapper);
        try {
            Whitebox.invokeMethod(trisProps, "initTrisFieldMapper");
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testInitTrisFieldMapperKeys() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, Map<String, Map<String, Map<String, String>>>> fieldMapper = new HashMap<>();
        trisProps.setFieldMapper(fieldMapper);
        Map<String, Map<String, Map<String, String>>> siteMapper = new HashMap<>();
        fieldMapper.put("DEFAULT", siteMapper);
        Map<String, Map<String, String>> productMapper = new HashMap<>();
        siteMapper.put("DEFAULT", productMapper);
        Map<String, String> defaultExchangeMapper = new HashMap<>();
        defaultExchangeMapper.put("field", "trisField");
        productMapper.put("DEFAULT", defaultExchangeMapper);
        Map<String, String> specialExchangeMapper = new HashMap<>();
        specialExchangeMapper.put("specialField", "specialTrisField");
        productMapper.put("SPECIAL", specialExchangeMapper);

        try {
            Whitebox.invokeMethod(trisProps, "init");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
        List<String> result = Whitebox.getInternalState(trisProps, "trisFieldMapperKeys");
        assertThat(result, hasSize(2));
        assertThat(result, contains("DEFAULT_DEFAULT_DEFAULT", "DEFAULT_DEFAULT_SPECIAL"));
    }

    @Test
    public void testGetKeys() {
        List<List<String>> columns = new ArrayList<>();
        List<String> firstColumn = new ArrayList<>();
        columns.add(firstColumn);
        List<String> secondColumn = new ArrayList<>();
        columns.add(secondColumn);
        List<String> thirdColumn = new ArrayList<>();
        columns.add(thirdColumn);

        firstColumn.add("A");
        firstColumn.add("B");
        secondColumn.add("a");
        thirdColumn.add("1");
        thirdColumn.add("2");
        thirdColumn.add("3");

        TrisProperties trisProps = new TrisProperties();
        try {
            List<String> result = Whitebox.invokeMethod(trisProps, "getKeys", columns);
            assertThat(result, hasSize(6));
            assertThat(result, contains("A_a_1", "A_a_2", "A_a_3", "B_a_1", "B_a_2", "B_a_3"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testGetTrisFieldMapperKeys() {
        String defaultKey = "DEFAULT";
        String[] keys = {"A", "B", "C"};
        TrisProperties trisProps = new TrisProperties();
        List<String> trisFieldMapperKeys = new ArrayList<>();
        trisProps.setTrisFieldMapperKeys(trisFieldMapperKeys);
        trisFieldMapperKeys.add("DEFAULT_DEFAULT_C");
        trisFieldMapperKeys.add("DEFAULT_DEFAULT_DEFAULT");

        try {
            List<String> result = Whitebox.invokeMethod(trisProps, "getTrisFieldMapperKeys", defaultKey, keys);
            assertThat(result, hasSize(2));
            assertThat(result, contains("DEFAULT_DEFAULT_C", "DEFAULT_DEFAULT_DEFAULT"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testGetTrisFieldMapperKeysWithEmptyKeys() {
        String defaultKey = "DEFAULT";
        TrisProperties trisProps = new TrisProperties();
        List<String> trisFieldMapperKeys = new ArrayList<>();
        trisProps.setTrisFieldMapperKeys(trisFieldMapperKeys);
        trisFieldMapperKeys.add("DEFAULT");
        String[] keys = {};
        try {
            List<String> result = Whitebox.invokeMethod(trisProps, "getTrisFieldMapperKeys", defaultKey, keys);
            assertThat(result, hasSize(1));
            assertThat(result, contains("DEFAULT"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testGetTrisFieldMappers() {
        String defaultKey = "DEFAULT";
        String[] keys = {"A", "B", "C"};
        TrisProperties trisProps = new TrisProperties();
        Map<String, Map<String, String>> trisFieldMapper = new HashMap<>();
        trisProps.setTrisFieldMapper(trisFieldMapper);
        trisFieldMapper.put("DEFAULT_DEFAULT_C", new HashMap<>());
        trisFieldMapper.put("DEFAULT_DEFAULT_DEFAULT", new HashMap<>());
        List<String> trisFieldMapperKeys = new ArrayList<>();
        trisProps.setTrisFieldMapperKeys(trisFieldMapperKeys);
        trisFieldMapperKeys.add("DEFAULT_DEFAULT_C");
        trisFieldMapperKeys.add("DEFAULT_DEFAULT_DEFAULT");

        try {
            List<Map<String, String>> result = Whitebox.invokeMethod(trisProps, "getTrisFieldMappers", defaultKey, keys);
            assertThat(result, hasSize(2));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testGetTrisField() {
        List<Map<String, String>> trisFieldMappers = new ArrayList<>();
        Map<String, String> trisFieldMapper = new HashMap<>();
        String field = "field";
        String trisField = "FIELD_1";
        trisFieldMapper.put(field, trisField);
        trisFieldMappers.add(trisFieldMapper);

        TrisProperties trisProps = new TrisProperties();
        try {
            String[] result = Whitebox.invokeMethod(trisProps, "getTrisField", trisFieldMappers, field);
            assertThat(result, arrayWithSize(1));
            assertThat(result, equalTo(new String[] {trisField}));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        trisFieldMappers = new ArrayList<>();
        trisFieldMapper = new HashMap<>();
        field = "field";
        trisField = "FIELD_1,FIELD_2";
        trisFieldMapper.put(field, trisField);
        trisFieldMappers.add(trisFieldMapper);

        trisProps = new TrisProperties();
        try {
            String[] result = Whitebox.invokeMethod(trisProps, "getTrisField", trisFieldMappers, field);
            assertThat(result, arrayWithSize(2));
            assertThat(result, equalTo(new String[] {"FIELD_1", "FIELD_2"}));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetTrisFields() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, Map<String, String>> trisFieldMapper = new HashMap<>();
        trisProps.setTrisFieldMapper(trisFieldMapper);
        Map<String, String> mapper = new HashMap<>();
        mapper.put("field", "FIELD_1,FIELD_1_1");
        trisFieldMapper.put("DEFAULT_DEFAULT_C", mapper);
        mapper = new HashMap<>();
        mapper.put("field", "FIELD_2");
        trisFieldMapper.put("DEFAULT_DEFAULT_DEFAULT", mapper);
        List<String> trisFieldMapperKeys = new ArrayList<>();
        trisProps.setTrisFieldMapperKeys(trisFieldMapperKeys);
        trisFieldMapperKeys.add("DEFAULT_DEFAULT_C");
        trisFieldMapperKeys.add("DEFAULT_DEFAULT_DEFAULT");
        List<String> fields = new ArrayList<>();
        fields.add("field");

        String defaultKey = "DEFAULT";

        String[] keys = {"A", "B", "C"};
        List<String> result;
        try {
            result = Whitebox.invokeMethod(trisProps, "getTrisFields", fields, defaultKey, keys);
            assertThat(result, hasSize(2));
            assertThat(result, contains("FIELD_1", "FIELD_1_1"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        Map<String, Map<String, String[]>> responseFieldMapper =
            (Map<String, Map<String, String[]>>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER);
        assertThat(responseFieldMapper.get("A|B|C").get("field"), equalTo(new String[] {"FIELD_1", "FIELD_1_1"}));

        String[] otherKeys = {"A", "B", "D"};
        try {
            result = Whitebox.invokeMethod(trisProps, "getTrisFields", fields, defaultKey, otherKeys);
            assertThat(result, hasSize(1));
            assertThat(result, contains("FIELD_2"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        responseFieldMapper =
            (Map<String, Map<String, String[]>>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER);
        assertThat(responseFieldMapper.get("A|B|D").get("field"), equalTo(new String[] {"FIELD_2"}));
    }

}
