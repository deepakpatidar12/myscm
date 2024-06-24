/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myScm.scm.configuration;

import com.myScm.scm.errorHandler.EmailNotVerifiedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, jakarta.servlet.ServletException {
        if (exception instanceof BadCredentialsException) {
            response.sendRedirect("/scm2/signin?error=" + exception.getMessage());
        } else if (exception instanceof UsernameNotFoundException) {
            response.sendRedirect("/scm2/signin?error=" + exception.getMessage());
        } else if (exception instanceof EmailNotVerifiedException) {
            response.sendRedirect("/scm2/signin?error=email_not_verified&email=" + exception.getMessage());
        } else {
            response.sendRedirect("/scm2/signin?error=internal_error");
        }
    }
}
