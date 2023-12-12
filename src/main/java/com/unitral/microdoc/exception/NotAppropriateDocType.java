package com.unitral.microdoc.exception;

import com.unitral.microdoc.enums.HttpStatusMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotAppropriateDocType extends RuntimeException {
    private @Getter HttpStatus status=null;
    public NotAppropriateDocType(HttpStatusMessage httpStatusMessage) {
        super(httpStatusMessage.getMessage());
        this.status= httpStatusMessage.getStatus();
    }
}
