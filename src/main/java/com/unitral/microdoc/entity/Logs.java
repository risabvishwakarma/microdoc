package com.unitral.microdoc.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int pId;

    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String httpRequestId;
    @Column(name = "request_uri")
    private String requestURI;
    private String sessionId;
    private String error;
    private boolean active;


}
