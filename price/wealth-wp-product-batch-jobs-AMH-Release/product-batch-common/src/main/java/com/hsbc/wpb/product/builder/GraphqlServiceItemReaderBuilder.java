package com.dummy.wpb.product.builder;

import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.GraphqlServiceItemReader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class GraphqlServiceItemReaderBuilder {

    private GraphQLRequest graphQLRequest;

    private GraphQLService graphQLService;

    private String name = ClassUtils.getShortName(GraphqlServiceItemReader.class);

    private int pageSize = 300;


    public GraphqlServiceItemReaderBuilder graphQLRequest(GraphQLRequest graphQLRequest) {
        this.graphQLRequest = graphQLRequest;
        return this;
    }


    public GraphqlServiceItemReaderBuilder graphQLService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
        return this;
    }

    public GraphqlServiceItemReaderBuilder name(String name) {
        this.name = name;
        return this;
    }

    public GraphqlServiceItemReaderBuilder pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public GraphqlServiceItemReader build(){
        GraphqlServiceItemReader reader = new GraphqlServiceItemReader();

        Assert.notNull(this.graphQLService, "graphQLService is required.");
        Assert.notNull(this.graphQLRequest, "graphQLRequest is required.");

        reader.setGraphQLService(graphQLService);
        reader.setGraphQLRequest(graphQLRequest);
        reader.setPageSize(pageSize);
        reader.setName(name);

        return reader;
    }
}
