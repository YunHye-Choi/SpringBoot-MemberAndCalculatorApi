package com.ai21.Assignment1.exception;

public class InvalidFormulaException extends RuntimeException {
    public InvalidFormulaException() {
        super();
    }
    public InvalidFormulaException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidFormulaException(String message) {
        super(message);
    }
    public InvalidFormulaException(Throwable cause) {
        super(cause);
    }
}
