package com.dummy.wpc.dataSource;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ClusterSettings;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * <p>
 * <b> Insert description of the classes responsibility/role. NOT what uses it.
 * </b>
 * </p>
 */
public class WPCMongoDatabase implements FactoryBean {

    private String mongoUrl;
    private String mongoDatabase;
	private String mongoUsername;
    private String mongoPassword;

    private static final String ENCRYPTOR_SALT = "deadbeef";

	public static final String ENCRYPT_KEY = "ENCRYPT_KEY";

    public void init() {
        // Base64 CryptoUtil
        if (mongoPassword.startsWith("{cipher}")) {
            String encryptKey = getEncryptKey();
            if (StringUtils.isBlank(encryptKey)) {
                System.out.println("The ENCRYPT_KEY is blank, mongo password can not be decrypted");
                return;
            }
            TextEncryptor encryptor = Encryptors.text(encryptKey, ENCRYPTOR_SALT);
            mongoPassword = encryptor.decrypt(mongoPassword.substring("{cipher}".length()));
        }
    }


    @Override
    public MongoDatabase getObject() throws Exception {
        ConnectionString connectionString = new ConnectionString(mongoUrl);

        ClusterSettings.Builder clusterBuilder = ClusterSettings.builder().applyConnectionString(connectionString);
        MongoCredential credential = MongoCredential.createScramSha1Credential(mongoUsername, mongoDatabase,
                mongoPassword.toCharArray());

        TrustManager[] trustAllCerts = new TrustManager[]{new TrustAllManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, null);

        MongoClientSettings settings = MongoClientSettings.builder().credential(credential)
                .readPreference(ReadPreference.secondaryPreferred())
                .retryWrites(false)
                .applyConnectionString(connectionString)
                .applyToSslSettings(builder -> {
                    builder.enabled(true);
                    builder.invalidHostNameAllowed(true);
                    builder.context(sslContext);
                })
                .applyToClusterSettings(builder -> {
                    builder.applySettings(clusterBuilder.build());
                }).build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase(mongoDatabase);
    }

    @Override
    public Class<MongoDatabase> getObjectType() {
        return MongoDatabase.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private static class TrustAllManager
            implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkServerTrusted(X509Certificate[] certs,
                                       String authType) {
        }

        public void checkClientTrusted(X509Certificate[] certs,
                                       String authType) {
        }
    }

    private String getEncryptKey() {
        String encryptKey = System.getProperty(ENCRYPT_KEY);
        if (null != encryptKey) {
            return encryptKey;
        }
        encryptKey = System.getenv(ENCRYPT_KEY);
        if (null != encryptKey) {
            return encryptKey;
        }
        return null;
    }

	public void setMongoUrl(String mongoUrl) {
		this.mongoUrl = mongoUrl;
	}

	public void setMongoDatabase(String mongoDatabase) {
		this.mongoDatabase = mongoDatabase;
	}

	public void setMongoUsername(String mongoUsername) {
		this.mongoUsername = mongoUsername;
	}

	public void setMongoPassword(String mongoPassword) {
		this.mongoPassword = mongoPassword;
	}

}
