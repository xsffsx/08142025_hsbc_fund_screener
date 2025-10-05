package com.dummy.wpb.product.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;


class NamefilterTest {

    @Test
    void accept_Should_ReturnFalse_When_FilenameIsNull() {
        // Given
        Namefilter namefilter = new Namefilter("null", true);

        // When
        boolean result = namefilter.accept(new File(""), "test.txt");

        // Then
        Assertions.assertFalse(result);
        // When
          result = namefilter.accept(null, "test.txt");

        // Then
        Assertions.assertFalse(result);
    }

    @Test
    void accept_Should_ReturnFalse_When_NoMatchingFilename() {
        // Given
        Namefilter namefilter = new Namefilter(new String[]{"file1", "file2"}, true);

        // When
        boolean result = namefilter.accept(new File(""), "test.txt");

        // Then
        Assertions.assertFalse(result);
    }

    @Test
    void accept_Should_ReturnTrue_When_MatchingFilenameWithPrefix() {
        // Given
        Namefilter namefilter = new Namefilter(new String[]{"test"}, true);

        // When
        boolean result = namefilter.accept(new File(""), "test.txt");

        // Then
        Assertions.assertTrue(result);
    }

    @Test
    void accept_Should_ReturnTrue_When_MatchingFilenameWithSuffix() {
        // Given
        Namefilter namefilter = new Namefilter(new String[]{"txt"}, false);

        // When
        boolean result = namefilter.accept(new File(""), "test.txt");

        // Then
        Assertions.assertTrue(result);
    }

    @Test
    void matching_Should_ReturnTrue_When_MatchingNameWithPrefix() {
        // Given
        Namefilter namefilter = new Namefilter(new String[]{}, true, false);

        // When
        boolean result = namefilter.matching("test.txt", "test", true, false);

        // Then
        Assertions.assertTrue(result);
        // When
          result = namefilter.matching("test.txt", "TEST", true, true);

        // Then
        Assertions.assertTrue(result);
    }

    @Test
    void matching_Should_ReturnTrue_When_MatchingNameWithSuffix() {
        // Given
        Namefilter namefilter = new Namefilter(new String[]{}, false, false);

        // When
        boolean result = namefilter.matching("test.txt", "txt", false, false);

        // Then
        Assertions.assertTrue(result);
        // When
          result = namefilter.matching("test.txt", "TXT", false, true);

        // Then
        Assertions.assertTrue(result);
    }
}