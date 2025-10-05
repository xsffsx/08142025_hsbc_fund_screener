package com.dummy.wpb.product.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FinDocUtilsTest {

    @Test
    void createList_Should_CreateNewFile_When_FileDoesNotExist() throws IOException {
        // Define the initial context
        String filePath = "test.txt";

        // Trigger the scenario
        FinDocUtils.createList(filePath);

        // Verify the expected outcome
        assertTrue(Files.exists(Paths.get(filePath)));

        // Clean up the test file
        Files.deleteIfExists(Paths.get(filePath));
    }

    @Test
    void createList_Should_DeleteExistingFile_When_FileExists() throws IOException {
        // Define the initial context
        String filePath = "test.txt";

        // Trigger the scenario
        FinDocUtils.createList(filePath);

        // Verify the expected outcome
        assertTrue(Files.exists(Paths.get(filePath)));

        // Clean up the test file
        Files.deleteIfExists(Paths.get(filePath));
    }

}