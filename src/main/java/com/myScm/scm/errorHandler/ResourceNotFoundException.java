package com.myScm.scm.errorHandler;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private String message;

    public ResourceNotFoundException() {
        super("Resources Not Found..");
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}
