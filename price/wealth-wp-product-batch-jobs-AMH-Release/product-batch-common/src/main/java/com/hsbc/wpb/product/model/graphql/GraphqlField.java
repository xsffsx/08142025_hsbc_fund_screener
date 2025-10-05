package com.dummy.wpb.product.model.graphql;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class GraphqlField {
    String name;
    List<GraphqlField> fieldList = new LinkedList<>();

    /**
     * initiate with a name, support dot separated form like a.b.c, which will be transform into
     * a {b {c}}
     * @param name
     */
    public GraphqlField(String name){
        int idx = name.indexOf('.');
        if(-1 == idx){
            this.name = name;
        } else {
            this.name = name.substring(0, idx);
            fieldList.add(new GraphqlField(name.substring(idx + 1)));
        }
    }

    public String toString(){
        String fields = null;
        if(fieldList.size() > 0){
            List<String> strList = new ArrayList<>(fieldList.size());
            fieldList.forEach(field -> strList.add(field.toString()));
            fields = String.format("{%s}", String.join(" ", String.join(" ", strList)));
        }
        return null == fields ? name : String.format("%s %s", name, fields);
    }
}
