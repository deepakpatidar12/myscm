/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myScm.scm.errorHandler;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author LENOVO
 */
public class EmailNotVerifiedException extends AuthenticationException{
    
    
    public EmailNotVerifiedException(String message){
        super(message);
    }
    
}
