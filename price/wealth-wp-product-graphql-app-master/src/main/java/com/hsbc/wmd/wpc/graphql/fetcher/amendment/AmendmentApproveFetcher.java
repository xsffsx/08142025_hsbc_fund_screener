package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.RequestContext;
import com.dummy.wmd.wpc.graphql.constant.ApprovalAction;
import com.dummy.wmd.wpc.graphql.constant.RoleName;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AmendmentApproveFetcher implements DataFetcher<Document> {
    private AmendmentService amendmentService;

    public AmendmentApproveFetcher(AmendmentService amendmentService){
        this.amendmentService = amendmentService;
    }

    @Override
    public Document get(DataFetchingEnvironment environment) {
        long amendmentId = environment.getArgument("amendmentId");
        String approvalAction = environment.getArgument("approvalAction");
        String comments = environment.getArgument("comments");

        RequestContext ctx = RequestContext.getCurrentContext();
        String emplyNum = ctx.getUserId();

        Document amendment = amendmentService.amendmentById(amendmentId);
        if(null == amendment) {
            throw new productErrorException(productErrors.DocumentNotFound, "Amendment not found: id=" + amendmentId);
        }

        ctx.assertUserHasRole(RoleName.approver);

        amendment = amendmentService.approveAmendment(emplyNum, amendmentId, ApprovalAction.valueOf(approvalAction), comments);

        // retrieve and return the new document
        return amendment;
    }
}