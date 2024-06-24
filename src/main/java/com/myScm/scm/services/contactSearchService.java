package com.myScm.scm.services;

import org.springframework.data.domain.Page;

import com.myScm.scm.entities.Contact;
import com.myScm.scm.entities.User;

public interface contactSearchService {


   Page<Contact> searchByName(String name , User user , String fromRes);
   Page<Contact> searchByEmail(String email , User user , String fromRes);
   Page<Contact> searchByPhone(String phone , User user , String fromRes);

}
