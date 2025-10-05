package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.RequestContext;
import com.dummy.wmd.wpc.graphql.constant.RoleName;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AmendmentRequestApprovalFetcher implements DataFetcher<Document> {
    private AmendmentService amendmentService;

    public AmendmentRequestApprovalFetcher(AmendmentService amendmentService){
        this.amendmentService = amendmentService;
    }

    @Override
    public Document get(DataFetchingEnvironment environment) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.assertUserHasRole(RoleName.editor);

        long amendmentId = environment.getArgument("amendmentId");
        String comments = environment.getArgument("comments");

        // retrieve and return the new document
        return amendmentService.requestApproval(amendmentId, comments);
    }
}