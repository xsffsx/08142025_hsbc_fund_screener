package com.dummy.wmd.wpc.graphql.validator;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChangeValidatorTest {

    @Test
    void interestJsonPaths() {
        List<String> interested = ((ChangeValidator) (oldProduct, newProduct) -> Collections.emptyList()).interestJsonPaths();
         assertEquals(Collections.emptyList(), interested);
    }
}