package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.GraphQLProvider;
import com.dummy.wmd.wpc.graphql.RequestContext;
import com.dummy.wmd.wpc.graphql.config.DefaultFieldConfig;
import com.dummy.wmd.wpc.graphql.constant.*;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.fetcher.amendment.AmendmentApproveFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.amendment.AmendmentCreateFetcher;
import com.dummy.wmd.wpc.graphql.model.CustEligConfig;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultCustomerEligibilityCreateServiceTest {

    private DefaultCustomerEligibilityCreateService defaultCustomerEligibilityCreateService;

    private final AmendmentCreateFetcher amendmentCreateFetcher = Mockito.mock(AmendmentCreateFetcher.class);
    private final AmendmentApproveFetcher amendmentApproveFetcher = Mockito.mock(AmendmentApproveFetcher.class);
    private final MongoDatabase mongoDatabase = Mockito.mock(MongoDatabase.class);
    private final MongoCollection mongoCollection = Mockito.mock(MongoCollection.class);
    private final GraphQLSchema graphQLSchema = Mockito.mock(GraphQLSchema.class);
    private final GraphQLObjectType graphQLObjectType = Mockito.mock(GraphQLObjectType.class);
    private final FindIterable findIterable = Mockito.mock(FindIterable.class);
    private final Document prodInfo = Mockito.mock(Document.class);
    private final RequestContext requestContext = Mockito.mock(RequestContext.class);
    private final ConfigurationService configurationService = Mockito.mock(ConfigurationService.class);

    @Before
    public void before() {
        DefaultFieldConfig defaultConfig = new DefaultFieldConfig(configurationService);
        defaultConfig.setCustEligByType(initCustEligConfig());

        defaultCustomerEligibilityCreateService = new DefaultCustomerEligibilityCreateService(defaultConfig);
        defaultCustomerEligibilityCreateService.setAmendmentCreateFetcher(amendmentCreateFetcher);
        defaultCustomerEligibilityCreateService.setAmendmentApproveFetcher(amendmentApproveFetcher);
        defaultCustomerEligibilityCreateService.setMongoDatabase(mongoDatabase);
    }

    private Map<String, CustEligConfig> initCustEligConfig() {
        Map<String, CustEligConfig> custEligConfigMap = new HashMap<>();
        CustEligConfig custEligConfig = new CustEligConfig();
        custEligConfig.setRestrCustCtry(Collections.singletonList(new HashMap<>()));
        custEligConfig.setRestrCustGroup(Collections.singletonList(new HashMap<>()));
        custEligConfig.setFormReqmt(Collections.singletonList(new HashMap<>()));
        custEligConfigMap.put(ProdTypeCde.BOND_CD, custEligConfig);
        return custEligConfigMap;
    }

    @Test
    public void test_create() {
        Document amendment = new Document();
        amendment.put(Field._id, 1000L);

        try (MockedStatic<GraphQLProvider> graphQLProviderMockedStatic = mockStatic(GraphQLProvider.class);
             MockedStatic<RequestContext> requestContextMockedStatic = mockStatic(RequestContext.class)) {
            graphQLProviderMockedStatic.when(GraphQLProvider::getGraphQLSchema).thenReturn(graphQLSchema);
            when(amendmentCreateFetcher.get(any())).thenReturn(amendment);
            when(mongoDatabase.getCollection(CollectionName.product.name())).thenReturn(mongoCollection);
            when(graphQLSchema.getType(anyString())).thenReturn(graphQLObjectType);
            when(mongoCollection.find(any(BsonDocument.class))).thenReturn(findIterable);
            when(findIterable.projection(any(Bson.class))).thenReturn(findIterable);
            when(findIterable.into(any(ArrayList.class))).thenReturn(Collections.emptyList());

            Map<String, Object> filter = Collections.singletonMap("prodId", 12345L);
            Assert.assertThrows(productErrorException.class, () -> defaultCustomerEligibilityCreateService.create(filter,
                    DocType.product_customer_eligibility, ActionCde.add));
            when(findIterable.into(any(ArrayList.class))).thenReturn(Collections.singletonList(prodInfo));
            when(prodInfo.getString(Field.prodTypeCde)).thenReturn(ProdTypeCde.BOND_CD);
            when(amendmentApproveFetcher.get(any(DataFetchingEnvironment.class))).thenReturn(amendment);
            requestContextMockedStatic.when(RequestContext::getCurrentContext).thenReturn(requestContext);
            Document approved = defaultCustomerEligibilityCreateService.create(Collections.singletonMap("prodId", 12345L),
                    DocType.product_customer_eligibility, ActionCde.add);
            Assert.assertEquals(1000L, approved.get(Field._id));

            when(requestContext.getUserInfo()).thenReturn(new UserInfo());
            approved = defaultCustomerEligibilityCreateService.create(Collections.singletonMap("prodId", 12345L),
                    DocType.product_customer_eligibility, ActionCde.add);
            Assert.assertEquals(1000L, approved.get(Field._id));

            when(prodInfo.getString(Field.prodTypeCde)).thenReturn(ProdTypeCde.EQUITY_LINKED_INVESTMENT);
            approved = defaultCustomerEligibilityCreateService.create(Collections.singletonMap("prodId", 12345L),
                    DocType.product_customer_eligibility, ActionCde.add);
            Assert.assertEquals(1000L, approved.get(Field._id));

        }
    }

}