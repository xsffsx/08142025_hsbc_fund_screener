package com.dummy.wmd.wpc.graphql.model;

import com.dummy.wmd.wpc.graphql.validator.Error;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ReferDataValidationResult {
    // The ReferenceData which is going to be created or updated
    private Map<String, Object> referData;

    // Errors in the ReferenceData validation
    private List<Error> errors;
}
