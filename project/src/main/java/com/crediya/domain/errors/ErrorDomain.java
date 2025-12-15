package com.crediya.domain.errors;

public class ErrorDomain extends Exception {
    protected ErrorType errorType;
    public ErrorDomain(ErrorType errorType) {
        super(errorType.getReason());
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return  errorType;
    }

    @Override
    public String toString() {
        return errorType.toString();
    }
}
