package com.dummy.wmd.wpc.graphql;

import lombok.Data;

import java.util.Map;

@Data
public class FieldType {
    public static final String SCALAR = "SCALAR";
    public static final String LIST = "LIST";
    public static final String OBJECT = "OBJECT";
    String name;
    String kind;
    String typeName;
    String ofTypeName;
    String ofTypeKind;

    public FieldType(Map<String, Object> map){
        this.name = (String)map.get("name");
        Map<String, Object> typeMap = (Map<String, Object>)map.get("type");
        this.kind = (String) typeMap.get("kind");
        this.typeName = (String)typeMap.get("name");
        Map<String, String> ofTypeMap = (Map<String, String>)typeMap.get("ofType");
        if(null != ofTypeMap) {
            this.ofTypeName = ofTypeMap.get("name");
            this.ofTypeKind = ofTypeMap.get("kind");
        }
    }

    public boolean isScalar(){
        return SCALAR.equals(kind);
    }

    public boolean isList(){
        return LIST.equals(kind);
    }

    public boolean isObject(){
        return OBJECT.equals(kind);
    }
}
