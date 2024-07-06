package ru.clevertec.check.exceptions;

public enum ErrorMessage {
    INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR"),
    BAD_REQUEST("BAD REQUEST"),
    NOT_ENOUGH_MONEY("NOT ENOUGH MONEY");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
