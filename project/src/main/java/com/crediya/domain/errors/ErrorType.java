package com.crediya.domain.errors;

public enum ErrorType {

    DUPLICATE_ID_FIELD(100, "El campo es obligatorio o ya está registrado."),
    DUPLICATE_PRIMARY_KEY(101, "El registro principal ya existe (Cédula/ID duplicado)."),
    NO_NEGATIVE_WAGES(400, "El salario no puede ser negativo."),
    NO_VALIDE_IMPUT(400, "Valor ingresado incorrecto."),
    DATABASE_ERROR(500, "Error interno en la base de datos."),
    RESOURCE_NOT_FOUND(404, "Recurso no encontrado."),
    INVALID_AMOUNT(400, "El monto del préstamo debe ser mayor a cero."),
    INVALID_INSTALLMENTS(400, "El número de cuotas debe ser mayor a cero."),
    CLIENT_NOT_FOUND(404, "Cliente no encontrado."),
    EMPLOYEE_NOT_FOUND(404, "Empleado no encontrado."),
    LOAN_NOT_FOUND(404, "El préstamo asociado no existe."),
    PAYMENT_NOT_FOUND(404, "El pago asociado no existe."),
    CANNOT_DELETE_HAS_DATA(409, "No se puede eliminar: Tiene datos asociados (préstamos/pagos)."),
    RECORD_NOT_UPDATED(500, "No se pudo actualizar el registro."),
    RECORD_NOT_DELETED(500, "No se pudo eliminar el registro.");


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
