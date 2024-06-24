package com.myScm.scm.errorHandler;

import lombok.Getter;

@Getter
public class UserNotFound extends RuntimeException {

    private String message;

    public UserNotFound(){

    }

    public UserNotFound(String message){
        this.message = message;
    }

}
