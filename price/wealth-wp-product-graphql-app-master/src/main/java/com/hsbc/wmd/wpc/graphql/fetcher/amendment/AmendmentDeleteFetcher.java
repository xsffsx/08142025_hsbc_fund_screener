package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.RequestContext;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.RecStatCde;
import com.dummy.wmd.wpc.graphql.constant.RoleName;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class AmendmentDeleteFetcher implements DataFetcher<Document> {
    private AmendmentService amendmentService;

    public AmendmentDeleteFetcher(AmendmentService amendmentService) {
        this.amendmentService = amendmentService;
    }

    @Override
    public Document get(DataFetchingEnvironment environment) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.assertUserHasRole(RoleName.editor);

        Long amendmentId = environment.getArgument("amendmentId");

        Document amdm = amendmentService.getAmendmentById(amendmentId);
        if (null == amdm) {
            throw new productErrorException(productErrors.DocumentNotFound, "Amendment not found: id=" + amendmentId);
        }

        // amendment in approved and pending status are not allow to be deleted
        RecStatCde recStatCde = RecStatCde.valueOf(amdm.getString(Field.recStatCde));
        if (!Arrays.asList(RecStatCde.draft, RecStatCde.returned, RecStatCde.pending).contains(recStatCde)) {
            String message = String.format("Amendment in '%s' status is not allow to be deleted", recStatCde);
            throw new productErrorException(productErrors.DocumentStatusError, message);
        }

        amdm.put(Field.recStatCde, RecStatCde.deleted.name());
        amendmentService.updateAmendment(amdm);
        // retrieve and return the new document
        return amdm;
    }
}