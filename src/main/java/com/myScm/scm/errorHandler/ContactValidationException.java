package com.myScm.scm.errorHandler;

public class ContactValidationException extends RuntimeException{

    public ContactValidationException(String message){
        super(message);
    }
}
