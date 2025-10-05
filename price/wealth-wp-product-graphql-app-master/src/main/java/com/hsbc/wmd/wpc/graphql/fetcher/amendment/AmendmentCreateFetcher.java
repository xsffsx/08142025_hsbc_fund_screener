package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.RequestContext;
import com.dummy.wmd.wpc.graphql.constant.ActionCde;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
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

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class AmendmentCreateFetcher implements DataFetcher<Document> {
    private AmendmentService amendmentService;

    public AmendmentCreateFetcher(AmendmentService amendmentService){
        this.amendmentService = amendmentService;
    }

    @Override
    public Document get(DataFetchingEnvironment environment) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.assertUserHasRole(RoleName.editor);

        String emplyNum = ctx.getUserId();
        ActionCde actionCde = ActionCde.valueOf(environment.getArgument(Field.actionCde));
        DocType docType = DocType.valueOf(environment.getArgument(Field.docType));
        Map<String, Object> doc = environment.getArgument("docChanged");
        Boolean submit = environment.getArgument("submit");

        Document amendment = new Document();
        amendment.put(Field.emplyNum, emplyNum);
        amendment.put(Field.actionCde, actionCde.toString());
        amendment.put(Field.docType, docType.toString());
        doc.put(Field.lastUpdatedBy, "wps");
        amendment.put(Field.docBase, environment.getArgument("docBase"));
        amendment.put(Field.doc, doc);
        amendment.put(Field.ctryRecCde, doc.get(Field.ctryRecCde));
        amendment.put(Field.grpMembrRecCde, doc.get(Field.grpMembrRecCde));
        // convert the document to align with GraphQLSchema
        AmendmentUtils.coerceChangeDocument(amendment, environment.getGraphQLSchema());
        Document docChanged = (Document)amendment.get(Field.doc);

        // add - no docBase
        if(ActionCde.add == actionCde) {
            amendmentService.validateCreateDocument(docType, docChanged);
            amendment.put(Field.recStatCde, "draft");
            if (docType.equals(DocType.product_customer_eligibility) || docType.equals(DocType.product_staff_eligibility) ||
                    docType.equals(DocType.product_prod_relation) || docType.equals(DocType.chanl_related_fileds)){
                validateForUpdate(amendment,docType, environment.getGraphQLSchema());
                amendment.remove(Field.docBase);
            }
        }else {

            validateForUpdate(amendment,docType, environment.getGraphQLSchema());

            // update - query the docBase by docType, docId
            if (ActionCde.update == actionCde) {
                amendment.put(Field.recStatCde, "draft");
            }
            // delete - query the docBase by docType, docId, request approval
            else if (ActionCde.delete == actionCde) {
                amendment.put(Field.recStatCde, "pending");
            }
        }

        Document result;
        if(Boolean.TRUE.equals(submit)) {    // if submit=true, submit the document for approval directly
            // make sure the doc changed is valid first
            amendmentService.validateDocument(amendment);
            amendment.put(Field.recStatCde, "pending");
            result = amendmentService.createAmendment(amendment);
        } else {
            result = amendmentService.createAmendment(amendment);
        }

        // retrieve and return the new document
        return result;
    }


    private void validateForUpdate(Document amendment, DocType docType, GraphQLSchema graphQLSchema) {
        Document docChanged = amendment.get(Field.doc, Document.class);
        Long docId = DocumentUtils.getLong(docChanged, Field._id);
        Long docRevision = DocumentUtils.getLong(docChanged, Field.revision);
        if (null == docId) {
            throw new productErrorException(productErrors.MissingField, "_id is missing for update / delete document");
        }
        if (null == docRevision) {
            throw new productErrorException(productErrors.MissingField, "revision is missing for update / delete document");
        }

        boolean hasOngoingAmendment = amendmentService.hasOngoingAmendment(docChanged, docType, docId);
        if (hasOngoingAmendment) {
            String message = String.format("%s with id=%s has on going amendment, which need to be resolved first", docType, docId);
            throw new productErrorException(productErrors.DocumentStatusError, message);
        }

        amendment.put(Field.docId, docId);
        amendment.put(Field.docRevision, docRevision);
        Document docBase = amendment.get(Field.docBase, Document.class);

        Document docLatest = amendmentService.getLatestDocument(docType, docId, docChanged);
        Long latestRevision = DocumentUtils.getLong(docLatest, Field.revision);
        amendment.put(Field.docBase, docLatest);
        AmendmentUtils.coerceChangeDocument(amendment, graphQLSchema);

        if (!Objects.equals(latestRevision, docRevision)) {
            if (null != docBase && Arrays.asList(
                    DocType.product,
                    DocType.product_customer_eligibility,
                    DocType.product_staff_eligibility,
                    DocType.product_prod_relation).contains(docType)) {
                // try resolve conflicts
                AmendmentResolver resolver = new AmendmentResolver(docBase, docChanged, DocumentUtils.clone(amendment.get(Field.docBase, Document.class)));
                amendment.put(Field.doc, resolver.resolve());
            } else {
                String message = String.format("%s with id=%s has been changed, please reload to try again", docType, docId);
                throw new productErrorException(productErrors.DocumentStatusError, message);
            }
        }

    }
}