package com.myScm.scm.configuration;

import java.io.IOException;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.myScm.scm.entities.Providers;
import com.myScm.scm.entities.User;
import com.myScm.scm.repos.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Auth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder encoder;

    public Auth2SuccessHandler() {
    }


    @SuppressWarnings("null")
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {


        // Check The Login Authentication providers

        // Identify the Provider
        // Google
        // google attribute

        var authuser = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegisterationId = authuser.getAuthorizedClientRegistrationId();

        var user = (DefaultOAuth2User)authentication.getPrincipal();
        User newUser = new User();
        newUser.setUserId(UUID.randomUUID().toString());
        newUser.setUserRole("ROLE_USER");
        newUser.setEmailValified(true);
        newUser.setEnabled(true);
        newUser.setPassword(encoder.encode("dummypass"));


        if (authorizedClientRegisterationId.equals("google")) {

            newUser.setUserEmail(user.getAttribute("email").toString());
            newUser.setProviders(Providers.GOOGLE);
            newUser.setProfilePic(user.getAttribute("picture").toString());
            newUser.setUserName(user.getAttribute("name").toString());
            newUser.setProviderUserId(user.getName());

        } else if (authorizedClientRegisterationId.equals("github")) {
            // Github
            // github attribute

            String email = user.getAttribute("email") != null ? user.getAttribute("email").toString()
                    : user.getAttribute("login").toString() + "@gmail.com";

            String name = user.getAttribute("name") != null ? user.getAttribute("name").toString()
                    : user.getAttribute("login").toString();

            String providerId = user.getName().toString();

            newUser.setUserEmail(email);
            newUser.setProviders(Providers.GITHUB);
            newUser.setProfilePic(user.getAttribute("avatar_url"));
            newUser.setUserName(name);
            newUser.setProviderUserId(providerId);

        } else if (authorizedClientRegisterationId.equals("facebook")) {
            // Facebook
            // facebook attribute

            newUser.setUserEmail("email");
            newUser.setProviders(Providers.FaceBook);
            newUser.setProfilePic("");
            newUser.setUserName("name");

        } else {
        }

        
        User userFromDatabase = this.userRepo.findByUserEmail(newUser.getUserEmail()).orElse(null);

        if (userFromDatabase == null) {

            this.userRepo.save(newUser);
        }
        response.sendRedirect("/scm2/user/dashboard");

    }

}
