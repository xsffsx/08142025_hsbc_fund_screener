package com.hhhh.group.secwealth.mktdata.common.util;


import org.compass.core.util.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringUtilTest {


    @Test
    public void testAll(){
        assertEquals( "US_ORIGINAL",StringUtil.getOriginalName("US"));
        assertEquals( "US_token",StringUtil.getTokenName("US"));
        Assert.isTrue( StringUtil.ifContainField(new String[]{"1","2"},"1"));
        assertEquals("112",StringUtil.appendParamsWithSymbol(new ArrayList<List<String>>(){{
            add( new ArrayList<String>(){{
                add("1");
                add("2");
            }});
        }},"1") );

        assertEquals("US",StringUtil.combineWithVertical("US"));

        assertEquals("US",StringUtil.combineWithUnderline("US"));
        assertEquals("US",StringUtil.combineWithDot("US"));
        Assert.isTrue(!StringUtil.isInvalid("US"));

        Assert.isTrue(!StringUtil.isInvalidforToken("US"));
        Assert.isTrue(StringUtil.isValid("US"));
        Assert.isTrue(StringUtil.isValidforToken("US"));
        Assert.isTrue(StringUtil.stringTokenizeToList("US").size()==1);
        Assert.isTrue(StringUtil.stringTokenizeToList("US","US").size()==0);
        Assert.isTrue(StringUtil.stringTokenizeToSet("US").size()==1);
        Assert.isTrue(StringUtil.stringTokenizeToSet("US","SA").size()==1);

        assertEquals("US",StringUtil.stringMatcher("US","SA"));
        assertEquals("US",StringUtil.replaceSpace("US"));

        Boolean flage=false;
        try {
            StringUtil.streamToStringConvert(new FileInputStream("/da/das/a.log"));
        } catch (IOException e) {
            flage=true;
        }
        Assert.isTrue(flage);


        Assert.isTrue(!StringUtil.isNumeric("US"));
        Assert.isTrue(!StringUtil.isChineseChar("US"));
        Assert.isTrue(StringUtil.isASCIIChar( (new char[1])[0]));

        Assert.isTrue(StringUtil.toStringAndCheckNull( (new char[1])[0]).length()==1);
        Assert.isTrue((StringUtil.toStringWithoutSpace( (new char[1])[0]).length()==0));

        assertEquals("US", StringUtil.toUpperCase( "US"));
        Assert.isTrue(!StringUtil.stringToBoolean( "US"));
        assertNull(StringUtil.transformYNIntoBoolean( "US"));

        assertNull(StringUtil.transformStringtoBoolean( "US"));

        assertEquals("US",StringUtil.formatHKStockSymbol( "US"));
        Assert.isTrue(StringUtil.filterSpecialString( "US",(new char[1])[0],(new char[1])[0]).length==1);

        assertEquals("",StringUtil.getServerName());
        assertEquals("", StringUtil.getServerPath());

    }
}
