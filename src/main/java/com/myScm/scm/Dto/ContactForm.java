package com.myScm.scm.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.myScm.scm.customValidator.imageSize;
import com.myScm.scm.customValidator.imageType;
import com.myScm.scm.entities.Contact;
import com.myScm.scm.entities.SocialLink;
import com.myScm.scm.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactForm {

    private String contactId;
    @NotBlank(message = "contact name can't be empty")
    private String name;
    private String nickName;
    @NotBlank(message = "contact email can't be empty")
    @Email(message = "write well-formed email like (contact@gmail.com)")
    private String contactEmail;
    @Size(min = 10, max = 10, message = "enter only 10 digit")
    @NotBlank(message = "Contact Number can't be empty")
    private String contactNumber;
    private String about;
    private String contactPic;
    @NotBlank(message = "address field can't be empty")
    private String address;
    private boolean favorite;
    @imageSize
    @imageType
    @JsonIgnore
    private MultipartFile image;
    private String imagePublicId;
    private User user;
    private List<SocialLink> links = new ArrayList<>();


    public ContactForm(Contact contact){

        this.contactId = contact.getContactId();
        this.name = contact.getName();
        this.contactEmail = contact.getContactEmail();
        this.contactNumber = contact.getContactNumber();
        this.contactPic = contact.getContactPic();
        this.favorite = contact.isFavorite();
        this.links = contact.getLinks();
    }


}
