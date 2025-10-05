package com.dummy.wpb.product.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

class WPCBufferReaderTest {

    @Test
    void readLine_Should_ReturnNull_When_EndOfFile() throws IOException {
        // Given
        String input = "";
        Reader reader = new StringReader(input);
        WPCBufferReader bufferReader = new WPCBufferReader(reader, 10);

        // When
        String result = bufferReader.readLine();

        // Then
        Assertions.assertNull(result);
    }

    @Test
    void readLine_Should_ReturnLine_When_LineWithinLimit() throws IOException {
        // Given
        String input = "Hello, World!";
        Reader reader = new StringReader(input);
        WPCBufferReader bufferReader = new WPCBufferReader(reader, 15);

        // When
        String result = bufferReader.readLine();

        // Then
        Assertions.assertEquals(input, result);
    }

    @Test
    void readLine_Should_ThrowIOException_When_LineExceedsLimit() {
        // Given
        String input = "This is a long line that exceeds the limit of 10 KB!";
        Reader reader = new StringReader(input);
        WPCBufferReader bufferReader = new WPCBufferReader(reader, 10);

        // When/Then
        Assertions.assertThrows(IOException.class, bufferReader::readLine);
    }

    @Test
    void readLine_Should_IgnoreCarriageReturn_When_ReadingLine() throws IOException {
        // Given
        String input = "Hello,\rWorld!";
        Reader reader = new StringReader(input);
        WPCBufferReader bufferReader = new WPCBufferReader(reader, 15);

        // When
        String result = bufferReader.readLine();

        // Then
        Assertions.assertEquals("Hello,World!", result);
    }

    @Test
    void readLine_Should_HandleNewLineCharacters_When_ReadingLine() throws IOException {
        // Given
        String input = "Hello,\nWorld!";
        Reader reader = new StringReader(input);
        WPCBufferReader bufferReader = new WPCBufferReader(reader, 15);

        // When
        String result = bufferReader.readLine();

        // Then
        Assertions.assertEquals("Hello,", result);
    }
}