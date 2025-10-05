package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.GraphQLProvider;
import com.dummy.wmd.wpc.graphql.RequestContext;
import com.dummy.wmd.wpc.graphql.constant.*;
import com.dummy.wmd.wpc.graphql.fetcher.amendment.AmendmentApproveFetcher;
import com.dummy.wmd.wpc.graphql.fetcher.amendment.AmendmentCreateFetcher;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


public abstract class DefaultFieldConfigCreateService {

    protected AmendmentCreateFetcher amendmentCreateFetcher;
    protected AmendmentApproveFetcher amendmentApproveFetcher;
    protected MongoDatabase mongoDatabase;

    private static class UserInfoHolder {
        private static UserInfo systemApprover;

        public static UserInfo getSystemApprover() {
            if (Objects.isNull(systemApprover)) {
                systemApprover = new UserInfo();
                systemApprover.setId("system-approver");
                systemApprover.setName("system-approver");
                systemApprover.setRoles(Collections.singletonList(RoleName.approver.name()));
            }
            return systemApprover;
        }
    }

    public Document create(Map<String, Object> filter, DocType docType, ActionCde actionCde) {
        Document amendDoc = doAmendmentCreation(filter, docType, actionCde);
        return doAmendmentApproval(amendDoc.getLong(Field._id));
    }

    private Document doAmendmentCreation(Map<String, Object> filter, DocType docType, ActionCde actionCde) {
        DataFetchingEnvironment environment = getDataFetchingEnvironment(filter, docType, actionCde);
        return amendmentCreateFetcher.get(environment);
    }

    protected abstract Map<String, Object> buildAmendmentDocument(Map<String, Object> filter, DocType docType);

    protected Document doAmendmentApproval(Long amendmentId) {
        Map<String, Object> arguments = new LinkedHashMap<>();
        arguments.put("amendmentId", amendmentId);
        arguments.put("approvalAction", ApprovalAction.approved.name());
        arguments.put("comments", "approved by system automatically");
        UserInfo originalUserInfo = RequestContext.getCurrentContext().getUserInfo();
        try {
            RequestContext.getCurrentContext().setUserInfo(UserInfoHolder.getSystemApprover());
            return amendmentApproveFetcher.get(buildDataFetchingEnvironment(arguments));
        } finally {
            if (Objects.isNull(originalUserInfo)) {
                RequestContext.getCurrentContext().remove(RequestContext.USER_INFO);
            } else {
                RequestContext.getCurrentContext().setUserInfo(originalUserInfo);
            }
        }
    }

    private DataFetchingEnvironment getDataFetchingEnvironment(Map<String, Object> filter, DocType docType,
                                                               ActionCde actionCde) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(Field.actionCde, actionCde.name());
        arguments.put(Field.docType, docType.name());
        arguments.put("docChanged", buildAmendmentDocument(filter, docType));
        arguments.put("submit", true);
        return buildDataFetchingEnvironment(arguments);
    }

    protected DataFetchingEnvironment buildDataFetchingEnvironment(Map<String, Object> arguments) {
        return DataFetchingEnvironmentImpl.newDataFetchingEnvironment().arguments(arguments)
                                          .graphQLSchema(GraphQLProvider.getGraphQLSchema()).build();
    }

    @Autowired
    public void setAmendmentCreateFetcher(AmendmentCreateFetcher amendmentCreateFetcher) {
        this.amendmentCreateFetcher = amendmentCreateFetcher;
    }

    @Autowired
    public void setAmendmentApproveFetcher(AmendmentApproveFetcher amendmentApproveFetcher) {
        this.amendmentApproveFetcher = amendmentApproveFetcher;
    }

    @Autowired
    public void setMongoDatabase(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Component
    public static class DefaultConfigCreateServiceHolder {

        private final Map<String, DefaultFieldConfigCreateService> holder = new HashMap<>();

        public DefaultConfigCreateServiceHolder(DefaultCustomerEligibilityCreateService customerEligibilityCreateService) {
            holder.put(DocType.product_customer_eligibility.name(), customerEligibilityCreateService);
        }

        public DefaultFieldConfigCreateService getService(DocType docType) {
            return holder.get(docType.name());
        }
    }
}
