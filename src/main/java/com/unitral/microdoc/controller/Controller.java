package com.unitral.microdoc.controller;

import com.unitral.microdoc.api.ApiResponse;
import com.unitral.microdoc.dto.DocumentObject;
import com.unitral.microdoc.enums.HttpStatusMessage;
import com.unitral.microdoc.exception.NotParsableDocuments;
import com.unitral.microdoc.service.ParserService;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;


@RestController
@CrossOrigin
@Scope("request")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final ParserService parserService;
    private final ApiResponse apiResponse;

    Controller(ParserService parserService, ApiResponse apiResponse) {
        this.parserService = parserService;
        this.apiResponse = apiResponse;
    }


    @GetMapping("/ping")
    public String Ping() {
        return "Docxpert is up and running!🚀🚀";
    }

    @PostMapping(value ="/upload")
    public ResponseEntity<ApiResponse> uploadDocument(@RequestPart("file") MultipartFile file) throws NotParsableDocuments, TikaException, SAXException,Exception {
        DocumentObject data = parserService.parseFile(file);
        return setApiResponse(data,HttpStatus.OK.value());
    }

    @GetMapping("/type")
    public ResponseEntity<String> getDocumentType() throws NotParsableDocuments {
        return ResponseEntity.ok(this.sd());
//        throw new RecordNotFoundException("No such record found");
//        try {
//          return ResponseEntity.ok(parserService.getFileType2());
//        } catch (Exception e) {
//            return new ResponseEntity<>("Error uploading document: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    private String sd() throws NotParsableDocuments {
        throw new NotParsableDocuments(HttpStatusMessage.UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity<ApiResponse> setApiResponse(Object data, int statusCode) {
        apiResponse.setSuccess(true);
        apiResponse.setStatusCode(statusCode);
        apiResponse.setMessage(null);
        apiResponse.setData(data);
        apiResponse.setTimestamp(java.time.LocalDateTime.now());
        return ResponseEntity.status(statusCode).body(this.apiResponse);
    }

}
