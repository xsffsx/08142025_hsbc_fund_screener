package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.RequestContext;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.RecStatCde;
import com.dummy.wmd.wpc.graphql.constant.RoleName;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import com.dummy.wmd.wpc.graphql.utils.AmendmentResolver;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Arrays;
import java.util.Objects;


@Slf4j
@Component
public class AmendmentUpdateFetcher implements DataFetcher<Document> {
    private AmendmentService amendmentService;

    public AmendmentUpdateFetcher(AmendmentService amendmentService){
        this.amendmentService = amendmentService;
    }

    @Override
    public Document get(DataFetchingEnvironment environment) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.assertUserHasRole(RoleName.editor);

        String emplyNum = ctx.getUserId();
        Long amendmentId = environment.getArgument("amendmentId");
        Document docChanged = new Document(environment.getArgument("docChanged"));
        Boolean submit = environment.getArgument("submit");
        GraphQLSchema graphQLSchema = environment.getGraphQLSchema();

        Document amdm = amendmentService.getAmendmentById(amendmentId);
        if(null == amdm) {
            throw new productErrorException(productErrors.DocumentNotFound, "Amendment not found: id=" + amendmentId);
        }

        //some fields can't be updated, like _id, recStatCde, revision, country code, group member ...
        RecStatCde recStatCde = RecStatCde.valueOf(amdm.getString(Field.recStatCde));
        if((RecStatCde.draft != recStatCde) && (RecStatCde.returned != recStatCde)){
            throw new productErrorException(productErrors.DocumentStatusError, "Amendment must be in 'draft' or 'returned' status for update, recStatCde=" + recStatCde);
        }

        amdm.put(Field.emplyNum, emplyNum);
        amdm.put(Field.recStatCde, RecStatCde.draft.toString());
        docChanged.put(Field.lastUpdatedBy, "wps");
        amdm.put(Field.doc, docChanged);
        amdm.put(Field.recUpdtDtTm, new Date());
        AmendmentUtils.coerceChangeDocument(amdm, graphQLSchema);

        checkConflicts(amdm, graphQLSchema);

        Document result;
        if(Boolean.TRUE.equals(submit)) {    // if submit=true, submit the document for approval directly
            // make sure the doc changed is valid first
            amendmentService.validateDocument(amdm);
            result = amendmentService.updateAmendment(amdm);
            result = amendmentService.requestApproval(DocumentUtils.getLong(result, Field._id), "");
        } else {
            result = amendmentService.updateAmendment(amdm);
        }
        // retrieve and return the new document
        return result;
    }

    private void checkConflicts(Document amendment, GraphQLSchema graphQLSchema) {
        Long docId = amendment.getLong(Field.docId);
        DocType docType = DocType.valueOf(amendment.getString(Field.docType));
        Document docChanged = amendment.get(Field.doc, Document.class);
        Long docRevision = docChanged.getLong(Field.revision);
        Document docBase = amendment.get(Field.docBase, Document.class);

        Document docLatest = amendmentService.getLatestDocument(docType, docId, docChanged);
        Long latestRevision = DocumentUtils.getLong(docLatest, Field.revision);

        if (!Objects.equals(latestRevision, docRevision)) {
            if (null != docBase && Arrays.asList(
                    DocType.product,
                    DocType.product_customer_eligibility,
                    DocType.product_staff_eligibility,
                    DocType.product_prod_relation).contains(docType)) {

                Document amendmentSchemaHelper = new Document(amendment);
                amendmentSchemaHelper.put(Field.docBase, docLatest);
                AmendmentUtils.coerceChangeDocument(amendmentSchemaHelper, graphQLSchema);
                // try resolve conflicts
                AmendmentResolver resolver = new AmendmentResolver(docBase, docChanged, DocumentUtils.clone(amendmentSchemaHelper.get(Field.docBase, Document.class)));
                resolver.resolve();
            } else {
                String message = String.format("%s with id=%s has been changed, please cancel and resubmit it.", docType, docId);
                throw new productErrorException(productErrors.DocumentStatusError, message);
            }
        }
    }
}