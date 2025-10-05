package com.dummy.wmd.wpc.graphql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@SuppressWarnings({"java:S1118", "java:S3010"})
@Component
public class Configuration {
    private static int graphqlDefaultLimit;

    public Configuration(@Value("#{${product.graphql.default-limit}}") int defaultLimit){
        graphqlDefaultLimit = defaultLimit;
    }

    public static int getDefaultLimit(){
        return graphqlDefaultLimit;
    }
}
