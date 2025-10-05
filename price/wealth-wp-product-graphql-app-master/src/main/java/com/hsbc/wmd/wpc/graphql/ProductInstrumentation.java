package com.dummy.wmd.wpc.graphql;

import com.dummy.ca.aim.security.token.InvalidTokenException;
import com.dummy.ca.aim.security.token.SignedToken;
import com.dummy.ca.aim.security.token.parser.JwtTokenParser;
import com.dummy.wmd.wpc.graphql.constant.productConstants;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.model.UserInfo;
import com.dummy.wmd.wpc.graphql.utils.ObjectMapperUtils;
import com.dummy.wmd.wpc.graphql.utils.QueryEditor;
import com.dummy.wmd.wpc.graphql.utils.RequestLogUtils;
import graphql.ErrorType;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQLContext;
import graphql.execution.*;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldCompleteParameters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

import static com.dummy.wmd.wpc.graphql.constant.Header.X_dummy_SMARTPLUS_TRUST_TOKEN_HEADER;
import static com.dummy.wmd.wpc.graphql.error.productErrorException.product_ERROR_CODE;

@Slf4j
@Component
public class productInstrumentation extends SimpleInstrumentation {
    @Value("#{${product.graphql.enable-console-request-log:True}}")
    private boolean enableConsoleRequestLog;
    @Value("#{${product.graphql.enable-console-data-log:False}}")
    private boolean enableConsoleDataLog;
    @Value("#{${product.graphql.enable-db-request-log:True}}")
    private boolean enableDbRequestLog;
    @Value("#{${product.graphql.enable-db-data-log:True}}")
    private boolean enableDbDataLog;

    @Autowired
    private BlockingQueue<Document> requestLogQueue;

    @Autowired
    private productDataLoaderRegistryFactory registryFactory;

    @Override
    public InstrumentationState createState() {
        return new LogState();
    }

    @Override
    public ExecutionInput instrumentExecutionInput(ExecutionInput executionInput, InstrumentationExecutionParameters parameters) {
        RequestContext ctx = RequestContext.getCurrentContext();
        String query = executionInput.getQuery();
        ExecutionInput transformedInput = executionInput.transform(builder -> {
            // customize the executionId as correlationId in case can get that from the request context
            if (StringUtils.hasText(ctx.getRequestCorrelationId())) {
                builder.executionId(ExecutionId.from(ctx.getRequestCorrelationId()));
            }
            builder.dataLoaderRegistry(registryFactory.create());
        });

        List<String> retrieveAllFieldsFor = (List<String>) parameters.getVariables().getOrDefault("retrieveAllFieldsFor", new ArrayList<>());
        if (!retrieveAllFieldsFor.isEmpty()) {   // checked if ALL_FIELDS exists in the query
            log.debug("Transform query: {}", query);
            String finalQuery = new QueryEditor(retrieveAllFieldsFor, parameters.getSchema()).expandFieldSelection(query);
            log.debug("Transformed query: {}", finalQuery);
            transformedInput = executionInput.transform(builder -> builder.query(finalQuery));
        }
        return super.instrumentExecutionInput(transformedInput, parameters);
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginFieldComplete(InstrumentationFieldCompleteParameters parameters) {
        ExecutionContext executionContext = parameters.getExecutionContext();
        ExecutionStepInfo stepInfo = parameters.getExecutionStepInfo();
        if (stepInfo.getPath().getLevel() == 1) {
            GraphQLContext ctx = executionContext.getContext();
            FetchedValue fetchedValue = (FetchedValue) parameters.getFetchedValue();
            if (null != fetchedValue && null != fetchedValue.getRawFetchedValue()) {
                String name = stepInfo.getPath().getSegmentName();
                ctx.put(name, fetchedValue.getRawFetchedValue());
            }
        }
        return new SimpleInstrumentationContext<>();
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        LogState state = parameters.getInstrumentationState();

        if (enableConsoleRequestLog) {
            Document reqLog = getRequestLog(parameters.getExecutionInput(), executionResult, state, false);
            if (!enableConsoleDataLog) {
                RequestLogUtils.removeData(reqLog);
            }
            log.info("Request log: {}", reqLog.get("millisCost"));
            log.info(ObjectMapperUtils.writeValueAsString(reqLog));
        }
        // add the request info to request log queue
        if (enableDbRequestLog) {
            Document reqLog = getRequestLog(parameters.getExecutionInput(), executionResult, state, true);
            if (!enableDbDataLog) {
                RequestLogUtils.removeData(reqLog);
            }
            requestLogQueue.add(reqLog);
            if (requestLogQueue.size() > 10) {
                log.warn("Request log in queue size={}", requestLogQueue.size());
            }
        }

        return super.instrumentExecutionResult(executionResult, parameters);
    }

    private Document getRequestLog(ExecutionInput input, ExecutionResult executionResult, LogState start, boolean variable2string) {
        RequestContext ctx = RequestContext.getCurrentContext();
        String executionId = input.getExecutionId().toString();
        Document reqInfo = new Document();
        reqInfo.put("executionId", executionId);
        reqInfo.put("userId", ctx.getUserId());
        reqInfo.put("operationName", input.getOperationName());
        reqInfo.put("query", input.getQuery());
        if (variable2string) {
            // store as string, to avoid Invalid BSON field name exception
            reqInfo.put("variables", ObjectMapperUtils.writeValueAsString(input.getVariables()));
        } else {
            reqInfo.put("variables", input.getVariables());
        }
        reqInfo.put("requestTime", new Date(start.getStartTimeMillis()));
        reqInfo.put("responseTime", new Date());
        reqInfo.put("millisCost", start.getMillisCost());
        reqInfo.put("executionResult", ObjectMapperUtils.convertValue(executionResult, Document.class));
        reqInfo.put("hasError", !CollectionUtils.isEmpty(executionResult.getErrors()));
        reqInfo.put("hasData", !Objects.isNull(executionResult.getData()));
        return reqInfo;
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        GraphQLContext graphQLContext = (GraphQLContext) parameters.getExecutionInput().getContext();
        WebRequest webRequest = graphQLContext.get(productConstants.webRequest);

        if (webRequest == null || !StringUtils.hasText(webRequest.getHeader(X_dummy_SMARTPLUS_TRUST_TOKEN_HEADER))) {
            // That means the request is not from ui, so we can proceed with the execution
            return super.beginExecution(parameters);
        }

        String token = webRequest.getHeader(X_dummy_SMARTPLUS_TRUST_TOKEN_HEADER);
        JwtTokenParser jwtTokenParser = new JwtTokenParser();
        try {
            SignedToken signedToken = jwtTokenParser.parse(token);
            if (signedToken.getExpiry().before(new Date())) {
                productErrorException sessionExpiredException = productErrorException.newproductErrorException()
                        .errorClassification(ErrorType.ExecutionAborted)
                        .message("Session expired, please login again.")
                        .extensions(Collections.singletonMap(product_ERROR_CODE, productErrors.SessionExpired))
                        .build();
                throw new AbortExecutionException(Collections.singletonList(sessionExpiredException));
            }
        } catch (InvalidTokenException e) {
            String staffId = Optional.ofNullable((UserInfo) graphQLContext.get(productConstants.userInfo)).map(UserInfo::getId).orElse(null);
            log.warn("Error parsing the JWT token, staffId: {}, cause: {}, token: {}", staffId, e.getMessage(), token);
        }
        return super.beginExecution(parameters);
    }
}
