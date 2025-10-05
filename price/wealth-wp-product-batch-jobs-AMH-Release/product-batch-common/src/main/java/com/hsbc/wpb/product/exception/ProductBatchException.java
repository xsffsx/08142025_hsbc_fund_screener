
package com.dummy.wpb.product.exception;

public class productBatchException extends RuntimeException {
	public productBatchException(String message) {
        super(message);
    }

	public productBatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public productBatchException(Throwable cause) {
        super(cause);
    }

}
