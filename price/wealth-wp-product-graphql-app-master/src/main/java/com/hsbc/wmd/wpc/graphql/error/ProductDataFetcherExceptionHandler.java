package com.dummy.wmd.wpc.graphql.error;

import graphql.ExceptionWhileDataFetching;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import lombok.extern.slf4j.Slf4j;

/**
 * Simply coy from the SimpleDataFetcherExceptionHandler, do any necessary customization here
 *
 */
@Slf4j
public class productDataFetcherExceptionHandler implements DataFetcherExceptionHandler {
    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = handlerParameters.getException();
        SourceLocation sourceLocation = handlerParameters.getSourceLocation();
        ResultPath path = handlerParameters.getPath();
        // errors.exception.stackTrace is an array and too long, change to a shorter String instead?
        ExceptionWhileDataFetching error = new ExceptionWhileDataFetching(path, exception, sourceLocation);
        // to keep the log clear, we don't log the call stack of productErrorException
        if(exception instanceof productErrorException) {
            log.warn(error.getMessage());
        } else {
            log.warn(error.getMessage(), exception);
        }

        return DataFetcherExceptionHandlerResult.newResult().error(error).build();
    }
}
