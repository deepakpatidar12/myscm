package com.myScm.scm.services;

import com.myScm.scm.Dto.ContactForm;
import com.myScm.scm.entities.Contact;
import com.myScm.scm.entities.User;

import java.util.List;

import org.springframework.data.domain.Page;

public interface contactServices {

    public Contact saveContact(ContactForm contactForm , User user);
    public Contact getcontactByid(String id);
    public Contact getContactByIdAndUser(String id , User user);
    public Contact updateContact(ContactForm form, User user);
    public boolean deleteContact(Contact Contact, User user);
    public void deleteContactById(String contactId , User user);
    public Contact makeFavorite(Contact contact, User user);
    public Contact makeFavoriteById(String id, User user);
    public List<ContactForm> getAllContacts();
    public Page<ContactForm> getFavoriteContactsList(int page , User user);
    public boolean contactIsExist(Contact contact, User user);
    public ContactForm getContactFormByContact(Contact contact);
    public int getFavoriteContacts(User user);
    public Page<ContactForm> getContactPages(int page , User user);
    public List<ContactForm> getcontactfrombyId(List<String> ids);
    public void deleteContactsbyIds(User user , List<String> contactlist);
    public void makeFavoriteByIds(User user , List<String> contactlist);

}
