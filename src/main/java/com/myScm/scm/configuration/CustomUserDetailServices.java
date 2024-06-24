package com.myScm.scm.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.myScm.scm.entities.User;
import com.myScm.scm.repos.UserRepo;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailServices implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserEmail(username).orElseThrow(() -> new UsernameNotFoundException("invalid_credentials"));

        CustomUserDetail details = new CustomUserDetail(user);

        return details;
    }
    

}
