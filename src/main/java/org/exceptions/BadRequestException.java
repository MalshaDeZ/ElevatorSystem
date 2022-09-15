package org.exceptions;

public abstract class BadRequestException extends RuntimeException {

    private static final String ERROR_MESSAGE_HEADER = "Bad Request";

    public BadRequestException(String data) {
        super(ERROR_MESSAGE_HEADER + " : " + String.join(",", data));
    }
}
