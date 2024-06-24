package com.myScm.scm.errorHandler;

import lombok.Getter;

@Getter
public class ContactNotFoundException extends RuntimeException{

    private String message;

    public ContactNotFoundException(String message){
        this.message = message;
    }

    public ContactNotFoundException(){
        
    }
}