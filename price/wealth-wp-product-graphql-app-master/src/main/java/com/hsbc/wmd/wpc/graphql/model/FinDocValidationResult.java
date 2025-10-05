package com.dummy.wmd.wpc.graphql.model;

import com.dummy.wmd.wpc.graphql.validator.Error;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class FinDocValidationResult {
    // Fin doc which is going to be created or updated
    private Map<String, Object> finDoc;
    // Errors in validation
    private List<Error> errors;
}
