package com.dummy.wpb.product.error;

import org.springframework.batch.core.ExitStatus;

public class ErrorCodeExitStatus extends ExitStatus {

    public static final String FAILED = ExitStatus.FAILED.getExitCode();

    public ErrorCodeExitStatus(String exitCode) {
        super(exitCode);
    }

    public ErrorCodeExitStatus(String exitCode, ErrorCode otpsErrorCde) {
        this(exitCode, otpsErrorCde, otpsErrorCde.desc());
    }

    public ErrorCodeExitStatus(String exitCode, ErrorCode otpsErrorCde, String exitDescription) {
        super(exitCode, otpsErrorCde.name() + ": " + exitDescription);
        this.errorDescription = otpsErrorCde.name() + ": " + exitDescription;
    }

    String errorDescription;

    @Override
    public String getExitDescription() {
        return errorDescription;
    }

    @Override
    public ExitStatus and(ExitStatus status) {
        return super.and(status);
    }
}
