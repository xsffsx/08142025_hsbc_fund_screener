package com.dummy.wmd.wpc.graphql.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    String jsonPath;
    String code;
    String message;

    public void setJsonPath(String jsonPath){
        if(jsonPath.startsWith("[ROOT]")){
            this.jsonPath = jsonPath.substring(7);
        }else{
            this.jsonPath = jsonPath;
        }
    }
}
