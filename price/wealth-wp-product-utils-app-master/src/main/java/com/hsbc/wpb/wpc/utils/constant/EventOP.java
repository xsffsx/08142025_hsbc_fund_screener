package com.dummy.wpb.wpc.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings({"java:S115"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventOP {
    public static EventOP getInstance(){
        return new EventOP();
    }
    public static final String insert = "INSERT";
    public static final String update = "UPDATE";
    public static final String delete = "DELETE";
}
