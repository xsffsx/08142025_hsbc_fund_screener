package com.dummy.wpb.product.framework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvironmentDecryptApplicationInitializerTest {

    @Test
    void testDecryptsPropertiesCorrectly() {
        TextEncryptor encryptor = Encryptors.text("password", "deadbeef");
        EnvironmentDecryptApplicationInitializer initializer = new EnvironmentDecryptApplicationInitializer();
        ReflectionTestUtils.setField(initializer, "encryptor", encryptor);

        MockPropertySource propertySource = new MockPropertySource()
                .withProperty("encrypted.property", "{cipher}" + encryptor.encrypt("secret"))
                .withProperty("plain.property", "plainValue");

        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(propertySource);

        Map<String, Object> decryptedProperties = initializer.decrypt(environment.getPropertySources());

        assertEquals("secret", decryptedProperties.get("encrypted.property"));
        assertNull(decryptedProperties.get("plain.property"));
    }

    @Test
    void testReturnsEmptyMapWhenNoEncryptedProperties() {
        EnvironmentDecryptApplicationInitializer initializer = new EnvironmentDecryptApplicationInitializer();
        MockPropertySource propertySource = new MockPropertySource()
                .withProperty("plain.property", "plainValue");

        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(propertySource);

        Map<String, Object> decryptedProperties = initializer.decrypt(environment.getPropertySources());

        assertTrue(decryptedProperties.isEmpty());
    }

    @Test
    void testThrowsExceptionWhenDecryptionFails() {
        TextEncryptor encryptor = mock(TextEncryptor.class);
        when(encryptor.decrypt(anyString())).thenThrow(new IllegalStateException("Decryption failed"));

        EnvironmentDecryptApplicationInitializer initializer = new EnvironmentDecryptApplicationInitializer();
        ReflectionTestUtils.setField(initializer, "encryptor", encryptor);

        MockPropertySource propertySource = new MockPropertySource()
                .withProperty("encrypted.property", "{cipher}invalidCipherText");

        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(propertySource);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> initializer.decrypt(environment.getPropertySources()));//NOSONAR

        assertEquals("Cannot decrypt: key=encrypted.property", exception.getMessage());
    }
}