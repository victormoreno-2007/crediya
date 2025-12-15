package com.crediya.domain.errors;

public class DuplicateDocumentException extends ErrorDomain {
    private String description;
    public DuplicateDocumentException(String description) {
        super(ErrorType.DUPLICATE_PRIMARY_KEY);
        this.description = description;
    }

    @Override
    public String toString() {
        return this.errorType.getCode() +" : "+ description;
    }
}
