package com.dummy.wpb.product.utils;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FilenameFilter;
import static org.junit.jupiter.api.Assertions.*;

class NamefilterTest {

    @Test
    void testAcceptMatchingFileWithSingleFilenameAndPrefix() {
        // Given
        String filename = "test";
        boolean prefix = true;
        FilenameFilter filter = new Namefilter(filename, prefix);

        // When
        boolean result = filter.accept(new File(""), "testfile.txt");

        // Then
        assertTrue(result);
    }
    @Test
    void testNotAcceptNonMatchingFileWithMultipleFilenamesAndSuffix() {
        // Given
        String[] filenames = {"file", "txt"};
        boolean prefix = false;
        FilenameFilter filter = new Namefilter(filenames, prefix);

        // When
        boolean result = filter.accept(new File(""), "testfile.pdf");

        // Then
        assertFalse(result);
    }

    @Test
    void testNotAcceptNullFilename() {
        // Given
        String[] filenames = null;
        boolean prefix = false;
        boolean allUpperCase = true;
        FilenameFilter filter = new Namefilter(filenames, prefix, allUpperCase);

        // When
        boolean result = filter.accept(new File(""), "testfile.pdf");

        // Then
        assertFalse(result);
    }

    @Test
    void testAcceptMatchingFileWithPrefix() {
        // Given
        String filenames = "TEST";
        boolean prefix = true;
        boolean allUpperCase = true;
        FilenameFilter filter = new Namefilter(filenames, prefix, allUpperCase);

        // When
        boolean result = filter.accept(new File(""), "testfile.txt");

        // Then
        assertTrue(result);
    }

    @Test
    void testAcceptMatchingFileWithSuffix() {
        // Given
        String[] filenames = {"TXT"};
        boolean prefix = false;
        boolean allUpperCase = true;
        FilenameFilter filter = new Namefilter(filenames, prefix, allUpperCase);

        // When
        boolean result = filter.accept(new File(""), "testfile.txt");

        // Then
        assertTrue(result);
    }
}