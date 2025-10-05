package com.dummy.wpb.wpc.utils.service;

import org.apache.http.client.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class InnerEquivalenceTests {

    private InnerEquivalence innerEquivalenceUnderTest;

    @Before
    public void setUp() throws Exception {
        innerEquivalenceUnderTest = new InnerEquivalence();
    }

    @Test
    public void testDoEquivalent_givenObjects_returnsBoolean() {
        Boolean case1 = innerEquivalenceUnderTest.doEquivalent("a", "b");
        assertThat(case1).isFalse();
        Date date = new Date();
        Boolean case2 = innerEquivalenceUnderTest.doEquivalent(date, LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
        assertThat(case2).isTrue();
        date= DateUtils.parseDate("2023-09-01");
        Boolean case3 = innerEquivalenceUnderTest.doEquivalent(date, LocalDateTime.now());
        assertThat(case3).isFalse();
        Map<String,Object> map = new HashMap<>();
        map.put("key","value");
        Map<String,Object> map2 = new HashMap<>(map);
        Boolean case4 = innerEquivalenceUnderTest.doEquivalent(map, map2);
        assertThat(case4).isTrue();
        Map<String,Object> map3 = new HashMap<>();
        Boolean case5 = innerEquivalenceUnderTest.doEquivalent(map, map3);
        assertThat(case5).isFalse();
        Boolean case6 = innerEquivalenceUnderTest.doEquivalent(new ArrayList<>(), new ArrayList<>());
        assertThat(case6).isTrue();
        Boolean case7 = innerEquivalenceUnderTest.doEquivalent(Arrays.asList("value"),Arrays.asList("value","value2"));
        assertThat(case7).isFalse();
        Boolean case8 = innerEquivalenceUnderTest.doEquivalent(Arrays.asList("value"),Arrays.asList("value"));
        assertThat(case8).isTrue();
        Boolean case9 = innerEquivalenceUnderTest.doEquivalent(Arrays.asList("value"),Arrays.asList("value2"));
        assertThat(case9).isFalse();
        Boolean case10 = innerEquivalenceUnderTest.doEquivalent(Arrays.asList(map),Arrays.asList(map3));
        assertThat(case10).isFalse();
        Boolean case11 = innerEquivalenceUnderTest.doEquivalent(Arrays.asList(map),Arrays.asList(map));
        assertThat(case11).isTrue();
    }

    @Test
    public void testDoHash_givenObject_returnsHashCode() {
        assertThat(innerEquivalenceUnderTest.doHash("o")).isEqualTo(111);
    }
}
