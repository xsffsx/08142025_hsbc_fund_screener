package com.dummy.wpb.wpc.utils.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Difference {
    private String path;
    private Object left;
    private Object right;

    public Map<String, Object> toMap(String leftKey, String rightKey) {
        Map<String, Object> diffMap = new LinkedHashMap<>();
        diffMap.put("path", path);
        diffMap.put(leftKey, left);
        diffMap.put(rightKey, right);
        return diffMap;
    }
}
