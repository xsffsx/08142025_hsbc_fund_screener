package com.dummy.wmd.wpc.graphql.validator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface ChangeValidator {

   List<Error> validate(Map<String, Object> oldProduct, Map<String, Object> newProduct);

   default List<String> interestJsonPaths(){
      return Collections.emptyList();
   }
}