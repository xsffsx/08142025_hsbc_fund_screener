package com.dummy.wmd.wpc.graphql.constant;

/**
 * A constant class that defines mongodb error codes.
 * <br>
 * Reference:
 * <a href ='https://www.mongodb.com/zh-cn/docs/manual/reference/error-codes/'>Error Code List</a>
 * */
public class MongoDbErrorCodes {

    private MongoDbErrorCodes() {}

    public static final Integer WRITE_CONFLICT = 112;
    public static final Integer NO_SUCH_TRANSACTION = 251;
    public static final Integer DUPLICATE_KEY = 11000;
}
