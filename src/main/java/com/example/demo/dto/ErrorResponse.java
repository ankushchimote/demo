package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String errorCode;
    private String message;
    private String path;
    private Map<String, String> errors;

    public ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String errorCode,
            String message,
            String path) {

        this.timestamp = timestamp;
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
    }

    public ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String errorCode,
            String message,
            String path,
            Map<String, String> errors) {

        this.timestamp = timestamp;
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
        this.errors = errors;
    }
}


//Why Map<String, String> errors?
//
//Because we want:
//
//        "errors": {
//        "productName": "Product name is required",
//        "createdBy": "Created by is required"
//        }
//
//instead of Spring's massive FieldError objects.
//
//And notice that errors is separate from the general message.
//
//For example:
//
//message
//    → "Validation failed"
//
//errors
//    → productName → "Product name is required"
//        → createdBy   → "Created by is required"