/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myScm.scm.configuration;

import com.myScm.scm.errorHandler.EmailNotVerifiedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author LENOVO
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailServices customUserDetailServices;
    private final PasswordEncoder encoder;

    public CustomAuthenticationProvider(CustomUserDetailServices customUserDetailServices, PasswordEncoder encoder) {
        this.customUserDetailServices = customUserDetailServices;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = this.customUserDetailServices.loadUserByUsername(userName);

        if (userDetails == null) {
            throw new BadCredentialsException("invalid_credentials");
        }

        if (!encoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("invalid_credentials");
        }

        // Assuming user has a method isEmailVerified to check email verification status
        if (!((CustomUserDetail) userDetails).isEmailVerified()) {
            throw new EmailNotVerifiedException(userName);
        }

        return new UsernamePasswordAuthenticationToken(userName, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
