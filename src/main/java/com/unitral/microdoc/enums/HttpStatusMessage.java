package com.unitral.microdoc.enums;

import org.springframework.http.HttpStatus;


public enum HttpStatusMessage {
    UNPROCESSABLE_ENTITY("Not Parsable Documents.",HttpStatus.UNPROCESSABLE_ENTITY),
    INTERNAL_SERVER_ERROR("Error uploading document: ",HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("No such record found",HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    HttpStatusMessage(String message,HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
