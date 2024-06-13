package com.commerce.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageException extends RuntimeException {

    public FileStorageException(String s) {
        super(s);
    }

    public FileStorageException(String s, Throwable cause) {
        super(s, cause);
    }
}
