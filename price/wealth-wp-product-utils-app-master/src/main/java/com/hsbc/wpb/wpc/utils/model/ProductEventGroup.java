package com.dummy.wpb.wpc.utils.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProductEventGroup {
    private Date processStartTime;
    private Date eventStartTime;
    private Date eventEndTime;
    private int eventCount;
    private List<Long> prodIdList;
}
