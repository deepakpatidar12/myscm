package com.myScm.scm.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myScm.scm.entities.Contact;
import com.myScm.scm.entities.User;

public interface ContactRepo extends JpaRepository<Contact, String> {

    int countByFavoriteAndUser(boolean favorite, User user);

    Page<Contact> findByFavoriteAndUser(boolean favorite, User user, Pageable pageble);

    Page<Contact> findContactsByUser(User user, Pageable pageble);
        
    // For Local Search
    Page<Contact> findByUserAndNameContaining(User user, String name, Pageable pageable);

    Page<Contact> findByUserAndContactEmailContaining(User user, String email, Pageable pageable);

    Page<Contact> findByUserAndContactNumberContaining(User user, String number, Pageable pageable);

    // For Favorite
    Page<Contact> findByUserAndFavoriteAndNameContaining(User user, boolean favorite, String name, Pageable pageable);

    Page<Contact> findByUserAndFavoriteAndContactEmailContaining(User user, boolean favorite, String email, Pageable pageable);

    Page<Contact> findByUserAndFavoriteAndContactNumberContaining(User user, boolean favorite, String number, Pageable pageable);


}







