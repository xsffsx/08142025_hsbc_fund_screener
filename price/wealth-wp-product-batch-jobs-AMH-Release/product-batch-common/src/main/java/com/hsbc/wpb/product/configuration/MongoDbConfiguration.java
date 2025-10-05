package com.dummy.wpb.product.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import javax.net.ssl.SSLContext;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "spring.data.mongodb.host")
public class MongoDbConfiguration {

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
	@Value("${spring.data.mongodb.enable-replica}")
	private boolean enableReplica;

	@Bean
	public MongoClient mongoClient(SSLContext sslContext,ReadPreference readPreference) {
		if ("localhost".equals(mongoHost)) {
			return MongoClients.create("mongodb://" + mongoHost + ":" + mongoPort + "/");
		}

		MongoCredential credential = MongoCredential.createScramSha1Credential(mongoUsername, mongoDatabase,
				mongoPassword.toCharArray());
		ConnectionString connectionString = new ConnectionString(
				"mongodb://" + mongoHost + ":" + mongoPort + "/?ssl=true");

		ClusterSettings.Builder clusterBuilder = ClusterSettings.builder().applyConnectionString(connectionString);
		clusterBuilder.mode( enableReplica ? ClusterConnectionMode.MULTIPLE : ClusterConnectionMode.SINGLE); // set replica

		MongoClientSettings settings = MongoClientSettings.builder().credential(credential)
				.readPreference(readPreference)
				.retryWrites(false)
				.applyConnectionString(connectionString)
				.applyToSslSettings(builder -> {
					builder.enabled(true);
					builder.invalidHostNameAllowed(true);
					builder.context(sslContext);
				})
				.applyToClusterSettings(builder -> builder.applySettings(clusterBuilder.build())).build();
		return MongoClients.create(settings);
	}

	@Bean
    @ConditionalOnMissingBean(ReadPreference.class)
    public ReadPreference readPreference() {
        return ReadPreference.secondaryPreferred();
    }

	@Bean
	public MongoDatabase mongoDatabase(MongoClient mongoClient) {
		return mongoClient.getDatabase(mongoDatabase);
	}

    @Bean
    @ConditionalOnBean(Converter.class)
    @ConditionalOnMissingBean(MongoCustomConversions.class)
    public MongoCustomConversions mongoConverter(List<Converter<?, ?>> converterList) {
        return new MongoCustomConversions(converterList);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory,
                                       @Autowired(required = false) MongoCustomConversions mongoCustomConversions) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory);
        if (null != mongoCustomConversions) {
            MappingMongoConverter converter = (MappingMongoConverter) mongoTemplate.getConverter();
            converter.setCustomConversions(mongoCustomConversions);
			converter.afterPropertiesSet();
        }
        return mongoTemplate;
    }

	@Bean
	public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient){
		return new SimpleMongoClientDatabaseFactory(mongoClient,mongoDatabase);
	}
}
