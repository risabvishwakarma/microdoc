package com.unitral.microdoc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.tika.metadata.Metadata;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class DocumentObject implements Serializable {
    private  String fileType;
    private @JsonIgnore transient Object parsedData;
    private @JsonIgnore transient Metadata metadata;
    private String fileContent,metadataContent;


    public DocumentObject(String fileType, Object parsedData) {
        this.fileType = fileType;
        this.parsedData = parsedData;
        fileContent=parsedData.toString();

    }
    public DocumentObject(String fileType, Object parsedData, Metadata metadata) {
        this(fileType, parsedData);
        this.metadata = metadata;
        metadataContent=metadata.toString();
    }

    public DocumentObject(){}


}
