package com.unitral.microdoc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


public class RecordNotFoundException extends RuntimeException {
    private @Getter HttpStatus status=null;

    public RecordNotFoundException(String message,HttpStatus status) {
        super(message);
        this.status= status;

    }
}
