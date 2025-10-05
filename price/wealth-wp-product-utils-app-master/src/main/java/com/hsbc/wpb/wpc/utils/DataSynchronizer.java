package com.dummy.wpb.wpc.utils;

import java.util.Map;
import java.util.Set;

public interface DataSynchronizer {
    void sync(Set<Map<String, Object>> keySet);
    void delete(Set<String> rowidSet);
}
