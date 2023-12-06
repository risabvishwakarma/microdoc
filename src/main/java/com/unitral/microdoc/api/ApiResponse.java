package com.unitral.microdoc.api;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Data
@Component
@Scope("prototype")
public class ApiResponse {
    private boolean success;
    private int statusCode;
    private String message;
    private Object data;
    private LocalDateTime timestamp;
}
