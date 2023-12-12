package com.unitral.microdoc.advice;

import com.unitral.microdoc.api.ApiResponse;
import com.unitral.microdoc.entity.Logs;
import com.unitral.microdoc.exception.NotAppropriateDocType;
import com.unitral.microdoc.exception.NotParsableDocuments;
import com.unitral.microdoc.exception.RecordNotFoundException;
import com.unitral.microdoc.repository.LogsRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.sql.Timestamp;
import java.time.Instant;



@RestControllerAdvice
public class ControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);
    private final ApiResponse apiResponse;
    private final LogsRepo logsRepo;

    public ControllerAdvice(ApiResponse apiResponse, LogsRepo logsRepo) {
        this.apiResponse = apiResponse;
        this.logsRepo = logsRepo;

    }

    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<?> handleRecordNotFoundException(RecordNotFoundException ex, HttpServletRequest request) {
        this.printLog(request, ex.getMessage());
        return this.setApiResponse(ex.getMessage(), ex.getStatus().value());
    }

    @ExceptionHandler({TikaException.class, ParserConfigurationException.class, SAXException.class})
    public ResponseEntity<ApiResponse> handleCustomExceptions(Exception ex, HttpServletRequest request) {
        this.printLog(request, ex.getMessage());
        return this.setApiResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(value = {NotParsableDocuments.class})
    public ResponseEntity<ApiResponse> handelDocumentNotParsable(NotParsableDocuments ex, HttpServletRequest request) {
        this.printLog(request, ex.getMessage());
        return this.setApiResponse(ex.getMessage(), ex.getStatus().value());
    }

    @ExceptionHandler({NotAppropriateDocType.class})
    public ResponseEntity<ApiResponse> handelNotAppropriateDocType(NotAppropriateDocType ex, HttpServletRequest request) {
        this.printLog(request, ex.getMessage());
        return this.setApiResponse(ex.getMessage(), ex.getStatus().value());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ApiResponse> handelBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        this.printLog(request, ex.getMessage());
        return this.setApiResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }



    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<ApiResponse> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        this.printLog(request, ex.getMessage());
        return this.setApiResponse("File size should be less than " + ex.getMaxUploadSize() + " bytes", HttpStatus.PAYLOAD_TOO_LARGE.value());
    }

    private ResponseEntity<ApiResponse> setApiResponse(String message, int statusCode) {
        apiResponse.setSuccess(false);
        apiResponse.setStatusCode(statusCode);
        apiResponse.setMessage(message);
        apiResponse.setData(null);
        apiResponse.setTimestamp(java.time.LocalDateTime.now());
        return ResponseEntity.status(statusCode).body(this.apiResponse);
    }

    private void printLog(HttpServletRequest request, String message) {
        logsRepo.save(new Logs(0, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), request.getRequestId(), request.getRequestURI(), request.getSession().getId(), message, true));
        logger.error("Request ID: " + request.getRequestId() + " | Session ID: " + request.getSession().getId() + " | " + message);
    }

}
