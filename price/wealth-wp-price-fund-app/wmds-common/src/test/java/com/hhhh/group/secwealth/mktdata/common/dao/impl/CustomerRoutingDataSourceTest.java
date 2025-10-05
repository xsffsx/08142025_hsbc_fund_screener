package com.hhhh.group.secwealth.mktdata.common.dao.impl;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomerRoutingDataSourceTest{

    @InjectMocks
    CustomerRoutingDataSource customerRoutingDataSource;


    @Test
    public void setTargetDataSources(){
        HashMap hashMap = new HashMap();
        List list = new ArrayList();
        list.add(123);
        list.add(234);
        list.add(456);
        list.add(543);
        hashMap.put(0,list);
        customerRoutingDataSource.setTargetDataSources(hashMap);
        Assert.assertNotNull(customerRoutingDataSource);
    }
}
