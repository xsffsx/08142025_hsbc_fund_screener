package com.dummy.wpb.wpc.utils.model;

import com.dummy.wpb.wpc.utils.CodeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnInfo {
    private static Map<String, String> typeMap = new HashMap<>();
    private static String stringType = "String";
    static {
        typeMap.put("CHAR", stringType);
        typeMap.put("NUMBER", "Long");
        typeMap.put("VARCHAR2", stringType);
        typeMap.put("DATE", stringType);
        typeMap.put("TIMESTAMP", stringType);
    }

    public String getGraphQLTypeName() {
        if("NUMBER".equals(type)){
            if(scale > 0){
                return "Float";
            }
            return "Long";
        }
        String value = typeMap.get(type);
        return null == value ? type : value;
    }

    private String name;
    private String type;
    private int precision;
    private int scale;
    private boolean isPrimaryKey;
    private String comments;

    public String getAttrName(){
        return CodeUtils.toCamelCase(name);
    }
}
