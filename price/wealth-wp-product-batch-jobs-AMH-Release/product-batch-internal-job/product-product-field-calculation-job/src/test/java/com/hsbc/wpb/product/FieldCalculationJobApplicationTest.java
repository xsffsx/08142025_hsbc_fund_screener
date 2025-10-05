package com.dummy.wpb.product;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

public class FieldCalculationJobApplicationTest {

    @Test
    public void testMainMethod() throws Exception {

        try (MockedStatic<SpringApplication> springApplication = Mockito.mockStatic(SpringApplication.class)) {
            String[] args = new String[]{"ctryRecCde=HK", "grpMembrRecCde=HBAP"};
            String[] args2 = null;
            Assertions.assertThrows(IllegalArgumentException.class, () ->FieldCalculationJobApplication.main(args2));
            Assertions.assertThrows(IllegalArgumentException.class, () ->FieldCalculationJobApplication.main(args));
        }
    }
}