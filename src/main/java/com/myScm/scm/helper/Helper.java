package com.myScm.scm.helper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myScm.scm.Dto.ContactForm;
import com.myScm.scm.Dto.UserForm;
import com.myScm.scm.entities.Contact;
import com.myScm.scm.entities.User;
import  java.util.List;
import  java.util.Arrays;

import jakarta.servlet.http.HttpSession;

@Component
public class Helper {

    final private ObjectMapper mapper;

    public Helper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    // Convert UserForm to User

    public User convertUser(UserForm userform) {

        return mapper.convertValue(userform, User.class);
    }

    // convert User to userForm
    public UserForm convertUserForm(User user){

        return mapper.convertValue(user, UserForm.class);
    }

    // Convert Contact to ContactForm

    public ContactForm convertContactForm(Contact contact){

        return mapper.convertValue(contact, ContactForm.class);
    }

    // Convert ContactForm to Contact

    public Contact convertContact(ContactForm contactForm) {

        return mapper.convertValue(contactForm, Contact.class);
    }

    // Convert All Contact to contactForm
    
    public List<ContactForm> convertAllContacts(List<Contact> contacts){

        return Arrays.asList(this.mapper.convertValue(contacts, ContactForm[].class));
    }

    // Conver ContactPage to ContactForm Page

    public Page<ContactForm> convertPageContactFrom(Page<Contact> pages){

        return pages.map(tocontactForm -> new ContactForm(tocontactForm));
    }

    public void removeMessage() {

        try {

            @SuppressWarnings("null")
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                    .getSession();
            session.removeAttribute("message");
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public String getSmallName(String name){

        String smallName = "";
        if(name.length() < 12 ){
            return name;
        }else{
            for (int s = 0 ; s<name.length() ; s++) {

                if(name.charAt(s) == ' ') {
                    break;
                }
                smallName +=name.charAt(s); 
          }
          return smallName;
        }

    }

    public int getImageSize(){

        return AppConstants.IMAGE_SIZE/1000;
    }


}
