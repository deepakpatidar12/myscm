package com.myScm.scm.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myScm.scm.entities.User;

public interface UserRepo extends JpaRepository<User , String> {

    // out Custom db related methods

    Optional<User> findByUserEmail(String userEmail);
    User findByuserEmailAndPassword(String email , String password);
    
}
