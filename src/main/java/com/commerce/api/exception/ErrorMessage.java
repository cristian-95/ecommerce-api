package com.commerce.api.exception;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class ErrorMessage {

    private String message;
    private Integer statusCode;
    private LocalTime timestamp;

    public ErrorMessage() {
        this.timestamp = LocalTime.now();
    }

    public ErrorMessage(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = LocalTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }
}
