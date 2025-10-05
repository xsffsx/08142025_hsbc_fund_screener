package com.dummy.wpb.product.configuration;

import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.net.ssl.SSLContext;

import static org.junit.jupiter.api.Assertions.*;

class MongoDbConfigurationTest {

    @ParameterizedTest
    @CsvSource({
        "localhost, 27017, testdb, user, password, false",
        "remotehost, 27017, testdb, user, password, false",
        "remotehost, 27017, testdb, user, password, true"
    })
    @SneakyThrows
    void mongoClient_ReturnsClient(String host, int port, String database, String username, String password, boolean enableReplica) {
        SSLContext sslContext = SSLContext.getDefault();
        ReadPreference readPreference = ReadPreference.primary();
        MongoDbConfiguration config = new MongoDbConfiguration();
        ReflectionTestUtils.setField(config, "mongoHost", host);
        ReflectionTestUtils.setField(config, "mongoPort", port);
        ReflectionTestUtils.setField(config, "mongoDatabase", database);
        ReflectionTestUtils.setField(config, "mongoUsername", username);
        ReflectionTestUtils.setField(config, "mongoPassword", password);
        ReflectionTestUtils.setField(config, "enableReplica", enableReplica);
        MongoClient client = config.mongoClient(sslContext, readPreference);
        assertNotNull(client);
    }

}