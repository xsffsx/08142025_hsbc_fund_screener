/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.portable.ApplicationException;
import org.powermock.reflect.Whitebox;

import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;


public class ApplicationPropertiesTest {

    @Test
    public void testGetMapperWithInvalidParameters() {
        Map<String, Map<String, Map<String, String>>> mapper = new HashMap<>();
        Map<String, Map<String, String>> firstMapper = new HashMap<>();
        mapper.put("LEVEL_1", firstMapper);
        Map<String, String> secondMapper = new HashMap<>();
        firstMapper.put("LEVEL_2", secondMapper);
        secondMapper.put("key", "value");

        ApplicationProperties appProps = new ApplicationProperties();
        String[] keys = {"LEVEL_1", "OTHER_LEVEL"};
        try {
            Whitebox.invokeMethod(appProps, "getMapper", mapper, 0, "DEFAULT", keys);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        mapper = new HashMap<>();
        appProps = new ApplicationProperties();
        try {
            Whitebox.invokeMethod(appProps, "getMapper", mapper, 0, "DEFAULT", keys);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testGetMapper() {
        Map<String, Map<String, Map<String, String>>> mapper = new HashMap<>();
        Map<String, Map<String, String>> firstMapper = new HashMap<>();
        mapper.put("LEVEL_1", firstMapper);
        Map<String, String> secondMapper = new HashMap<>();
        firstMapper.put("LEVEL_2", secondMapper);
        secondMapper.put("key", "value");
        secondMapper = new HashMap<>();
        firstMapper.put("DEFAULT", secondMapper);
        secondMapper.put("defaultKey", "defaultValue");

        ApplicationProperties appProps = new ApplicationProperties();
        String[] keys = {"LEVEL_1", "LEVEL_2"};
        Map<String, ?> result;
        try {
            result = Whitebox.invokeMethod(appProps, "getMapper", mapper, 0, "DEFAULT", keys);
            assertThat(result, hasKey(equalTo("key")));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        appProps = new ApplicationProperties();
        String[] otherKeys = {"LEVEL_1", "OTHER_LEVEL"};
        try {
            result = Whitebox.invokeMethod(appProps, "getMapper", mapper, 0, "DEFAULT", otherKeys);
            assertThat(result, hasKey(equalTo("defaultKey")));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        appProps = new ApplicationProperties();
        Map<String, String> otherMapper = new HashMap<>();
        otherMapper.put("key", "value");
        try {
            result = Whitebox.invokeMethod(appProps, "getMapper", otherMapper, 0, "DEFAULT", null);
            assertThat(result, hasKey(equalTo("key")));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testConcat() {
        ApplicationProperties appProps = new ApplicationProperties();
        String[] a = {"a", "b", "c"};
        String[] b = {"1", "2", "3"};
        String[] exceptedResult = {"a", "b", "c", "1", "2", "3"};
        try {
            String[] result = Whitebox.invokeMethod(appProps, "concat", a, b);
            assertThat(result, arrayWithSize(exceptedResult.length));
            assertThat(result, equalTo(exceptedResult));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testGetList() {
        Map<String, Map<String, Map<String, List<String>>>> mapper = new HashMap<>();
        Map<String, Map<String, List<String>>> firstMapper = new HashMap<>();
        mapper.put("LEVEL_1", firstMapper);
        Map<String, List<String>> secondMapper = new HashMap<>();
        firstMapper.put("LEVEL_2", secondMapper);
        List<String> tmp = new ArrayList<>();
        tmp.add("A");
        tmp.add("B");
        secondMapper.put("LEVEL_3", tmp);
        List<String> otherTmp = new ArrayList<>();
        otherTmp.add("1");
        otherTmp.add("2");
        secondMapper.put("DEFAULT", otherTmp);

        ApplicationProperties appProps = new ApplicationProperties();
        String[] keys = {"LEVEL_1", "LEVEL_2", "LEVEL_3"};
        try {
            List<String> result = Whitebox.invokeMethod(appProps, "getList", mapper, 0, "DEFAULT", keys);
            assertThat(result, hasSize(tmp.size()));
            assertThat(result, equalTo(tmp));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        appProps = new ApplicationProperties();
        String[] otherKeys = {"LEVEL_1", "LEVEL_2", "OTHER"};
        try {
            List<String> result = Whitebox.invokeMethod(appProps, "getList", mapper, 0, "DEFAULT", otherKeys);
            assertThat(result, hasSize(otherTmp.size()));
            assertThat(result, equalTo(otherTmp));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

//    @Test
//    public void testGetListWithInvalidParameters() {
//        Map<String, Map<String, Map<String, List<String>>>> emptyMapper = new HashMap<>();
//
//        ApplicationProperties appProps = new ApplicationProperties();
//        String[] keys = {"KEYS"};
//        try {
//            Whitebox.invokeMethod(appProps, "getList", emptyMapper, 0, "DEFAULT", keys);
//        } catch (IllegalConfigurationException e) {
//            String exCode = e.getMessage();
//            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION));
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail("Unexcepted exception");
//        }
//
//        Map<String, Map<String, Map<String, List<String>>>> mapper = new HashMap<>();
//        Map<String, Map<String, List<String>>> firstMapper = new HashMap<>();
//        mapper.put("LEVEL_1", firstMapper);
//        Map<String, List<String>> secondMapper = new HashMap<>();
//        firstMapper.put("LEVEL_2", secondMapper);
//        secondMapper.put("LEVEL_3", new ArrayList<>());
//
//        appProps = new ApplicationProperties();
//        String[] emptyKeys = {};
//        try {
//            Whitebox.invokeMethod(appProps, "getList", mapper, 0, "DEFAULT", emptyKeys);
//        } catch (ApplicationException e) {
//            String exCode = e.getMessage();
//            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_UNDEFINED));
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail("Unexcepted exception");
//        }
//
//        appProps = new ApplicationProperties();
//        String[] otherKeys = {"LEVEL_1", "LEVEL_2", "OTHER"};
//        try {
//            Whitebox.invokeMethod(appProps, "getList", mapper, 0, "DEFAULT", otherKeys);
//        } catch (IllegalConfigurationException e) {
//            String exCode = e.getMessage();
//            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION));
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail("Unexcepted exception");
//        }
//
//        appProps = new ApplicationProperties();
//        String[] wrongKeys = {"LEVEL_1"};
//        try {
//            Whitebox.invokeMethod(appProps, "getList", mapper, 0, "DEFAULT", wrongKeys);
//        } catch (ClassCastException e) {
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail("Unexcepted exception");
//        }
//    }

}
