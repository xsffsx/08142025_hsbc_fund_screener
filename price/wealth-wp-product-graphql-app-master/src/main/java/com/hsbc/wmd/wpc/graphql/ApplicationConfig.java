package com.dummy.wmd.wpc.graphql;

import com.dummy.wmd.wpc.graphql.codec.OffsetDateTimeCodec;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ClusterConnectionMode;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@SuppressWarnings("java:S5122")
@Configuration
@Slf4j
public class ApplicationConfig {
    @Value("${spring.data.mongodb.host}")
    private String mongoHost;
    @Value("#{${spring.data.mongodb.port}}")
    private int mongoPort;
    @Value("${spring.data.mongodb.database}")
    private String mongoDatabase;
    @Value("${spring.data.mongodb.username:}")
    private String mongoUsername;
    @Value("${spring.data.mongodb.password:}")
    private String mongoPassword;
    @Value("/${graphql.url:graphql}")
    private String graphqlUrl;
    @Value("${spring.data.mongodb.enable-replica}")
    private boolean enableReplica;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(graphqlUrl).allowedOrigins("*");
            }
        };
    }

    @Bean
    public MongoClient mongoClient() {
        CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(
                        documentCodec,
                        new OffsetDateTimeCodec()
                )
        );

        if ("localhost".equals(mongoHost)) {
            return MongoClients.create();
        }

        MongoCredential credential = MongoCredential.createCredential(mongoUsername, mongoDatabase, mongoPassword.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(codecRegistry)
                .readPreference(ReadPreference.nearest())
                .credential(credential)
                .applyToSslSettings(builder -> {
                    builder.enabled(true);
                    builder.invalidHostNameAllowed(true);
                })
                .retryWrites(false)
                .applyToClusterSettings(builder -> builder
                        .hosts(Collections.singletonList(new ServerAddress(mongoHost, mongoPort)))
                        .mode(enableReplica ? ClusterConnectionMode.MULTIPLE : ClusterConnectionMode.SINGLE)
                )
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), mongoDatabase);
    }

    @Bean
    public MongoDatabase mongoDatabase() {
        return mongoDatabaseFactory().getMongoDatabase();
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory());
    }

    @Bean
    public MongoTransactionManager mongoTransactionManager() {
        TransactionOptions transactionOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .build();
        return new MongoTransactionManager(mongoDatabaseFactory(), transactionOptions);
    }

    @Bean
    public BlockingQueue<Document> getRequestLogQueue() {
        return new LinkedBlockingDeque<>();
    }

}