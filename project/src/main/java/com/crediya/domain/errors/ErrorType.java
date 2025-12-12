package com.crediya.domain.errors;

public enum ErrorType {

    DUPLICATE_ID_FIELD(100, "The field is required for this action"),
    DUPLICATE_PRIMARY_KEY(101, "The primary field is duplicated"),
    NO_NEGATIVE_WAGES(400, "salary cannot be a negative value."),
    NO_VALIDE_IMPUT(400, "incorrect input value"),
    DATABASE_ERROR(500, "Incorrect request to the database"),
    RESOURCE_NOT_FOUND(404, "resource not found"),
    INVALID_AMOUNT(400, "The loan amount must be greater than zero."),
    INVALID_INSTALLMENTS(400, "The number of installments must be greater than zero."),
    CLIENT_NOT_FOUND(404, "client not found"),
    EMPLOYEE_NOT_FOUND(404, "employee not found"),
    LOAN_NOT_FOUND(404, "The associated loan does not exist."),
    PAYMENT_EXCEEDS_DEBT(400, "The payment amount exceeds the current debt."),
    LOAD_ALREADY_PAID(400, "The loan has been paid off in full."),
    TRANSACTION_ERROR(500, "Payment transaction error");


    private int code;
    private String reason;

    ErrorType(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }


    @Override
    public String toString() {
        return code + ": "+reason;
    }
}
