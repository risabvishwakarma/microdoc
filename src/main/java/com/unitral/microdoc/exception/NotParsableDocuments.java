package com.unitral.microdoc.exception;

import com.unitral.microdoc.enums.HttpStatusMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotParsableDocuments extends RuntimeException {
    private @Getter HttpStatus status=null;
    public NotParsableDocuments(HttpStatusMessage httpStatusMessage) {
        super(httpStatusMessage.getMessage());
        this.status= httpStatusMessage.getStatus();
    }
}
