package com.myScm.scm.Dto;

import java.util.List;

import com.myScm.scm.entities.Contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {

    private String userId;

    @NotBlank(message = "user name is not empty!!")
    @Size(min = 3, max = 15)
    private String userName;

    @Email(message = "invalid email address!!")
    @NotBlank(message = "email field not be empty!!")
    private String userEmail;

    @Size(min = 10, max = 10, message = "enter only 10 digit!!")
    @NotBlank(message = "mobile number not be empty!!")
    private String userNumber;

    @Size(min = 8, max = 12)
    @NotBlank(message = "password field not be empty!!")
    private String password;

    private String profilePic;
    private String profilePublicId;

    private List<Contact> contactList;

}
