package com.dummy.wpb.product.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.*;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.util.*;
import java.util.regex.Pattern;

public class EnvironmentDecryptApplicationInitializer implements
        ApplicationListener<ApplicationContextInitializedEvent>, Ordered {

    /**
     * Name of the decrypted property source.
     */
    public static final String DECRYPTED_PROPERTY_SOURCE_NAME = "decrypted";

    private static final Pattern COLLECTION_PROPERTY = Pattern
            .compile("(\\S+)?\\[(\\d+)\\](\\.\\S+)?");//NOSONAR

    private static Log logger = LogFactory
            .getLog(EnvironmentDecryptApplicationInitializer.class);

    private TextEncryptor encryptor;

    public static final String ENCRYPT_KEY = "ENCRYPT_KEY";

    private static final String ENCRYPTOR_SALT = "deadbeef";

    private String getEncryptKey() {
        String encryptKey = System.getProperty(ENCRYPT_KEY);
        if (null != encryptKey) {
            logger.info("Got ENCRYPT_KEY from system properties");
            return encryptKey;
        }
        encryptKey = System.getenv(ENCRYPT_KEY);
        if (null != encryptKey) {
            logger.info("Got ENCRYPT_KEY from system env");
            return encryptKey;
        }
        return null;
    }

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        String encryptKey = getEncryptKey();
        if (null == encryptKey) {
            return;
        }

        this.encryptor = Encryptors.text(encryptKey, ENCRYPTOR_SALT);
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();

        Set<String> found = new LinkedHashSet<>();
        Map<String, Object> map = decrypt(propertySources);
        if (!map.isEmpty()) {
            // We have some decrypted properties
            found.addAll(map.keySet());
            insert(applicationContext, new SystemEnvironmentPropertySource(
                    DECRYPTED_PROPERTY_SOURCE_NAME, map));
        }
    }

    private void insert(ApplicationContext applicationContext,
                        PropertySource<?> propertySource) {
        ApplicationContext parent = applicationContext;
        while (parent != null) {
            if (parent.getEnvironment() instanceof ConfigurableEnvironment) {
                ConfigurableEnvironment mutable = (ConfigurableEnvironment) parent
                        .getEnvironment();
                mutable.getPropertySources().addFirst(propertySource);
            }
            parent = parent.getParent();
        }
    }


    public Map<String, Object> decrypt(PropertySources propertySources) {
        Map<String, Object> overrides = new LinkedHashMap<>();
        List<PropertySource<?>> sources = new ArrayList<>();
        for (PropertySource<?> source : propertySources) {
            sources.add(0, source);
        }
        for (PropertySource<?> source : sources) {
            decrypt(source, overrides);
        }
        return overrides;
    }

    private void decrypt(PropertySource<?> source, Map<String, Object> overrides) {
        if (source instanceof CompositePropertySource) {
            for (PropertySource<?> nested : ((CompositePropertySource) source).getPropertySources()) {
                decrypt(nested, overrides);
            }
        } else if (source instanceof EnumerablePropertySource) {
            decryptEnumerablePropertySource((EnumerablePropertySource<?>) source, overrides);
        }
    }

    private void decryptEnumerablePropertySource(EnumerablePropertySource<?> source, Map<String, Object> overrides) {
        Map<String, Object> otherCollectionProperties = new LinkedHashMap<>();
        boolean sourceHasDecryptedCollection = false;

        for (String key : source.getPropertyNames()) {
            Object property = source.getProperty(key);
            if (property != null) {
                String value = property.toString();
                if (value.startsWith("{cipher}")) {
                    value = decryptValue(key, value);
                    overrides.put(key, value);
                    if (COLLECTION_PROPERTY.matcher(key).matches()) {
                        sourceHasDecryptedCollection = true;
                    }
                } else if (COLLECTION_PROPERTY.matcher(key).matches()) {
                    // put non-ecrypted properties so merging of index properties
                    // happens correctly
                    otherCollectionProperties.put(key, value);
                }
            }
        }
        // copy all indexed properties even if not encrypted
        if (sourceHasDecryptedCollection && !otherCollectionProperties.isEmpty()) {
            overrides.putAll(otherCollectionProperties);
        }
    }

    private String decryptValue(String key, String value) {
        value = value.substring("{cipher}".length());
        try {
            value = this.encryptor.decrypt(value);
            if (logger.isDebugEnabled()) {
                logger.debug("Decrypted: key=" + key);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot decrypt: key=" + key, e);
        }
        return value;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}