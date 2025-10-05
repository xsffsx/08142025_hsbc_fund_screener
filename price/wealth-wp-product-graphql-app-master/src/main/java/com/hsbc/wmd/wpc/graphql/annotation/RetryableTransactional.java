package com.dummy.wmd.wpc.graphql.annotation;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This is a combination of {@link org.springframework.retry.annotation.Retryable} and {@link org.springframework.transaction.annotation.Transactional}.
 * <br> <br>
 * It is used for start new transactional for mongodb and will retry 3 times when happen transactional exception.
 * <br> <br>
 * <a href ='https://www.mongodb.com/zh-cn/docs/manual/reference/error-codes/'>Error Code List</a>
 * <br>
 * 112: WriteConflict. 251: NoSuchTransaction.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Retryable(
        exceptionExpression = "#root instanceof T(com.mongodb.MongoServerException) && " +
                "(#root.getCode() == T(com.dummy.wmd.wpc.graphql.constant.MongoDbErrorCodes).WRITE_CONFLICT || " +
                "#root.getCode() == T(com.dummy.wmd.wpc.graphql.constant.MongoDbErrorCodes).NO_SUCH_TRANSACTION)",
        backoff = @Backoff(delay = 2000)
)
@Transactional
public @interface RetryableTransactional {
}
