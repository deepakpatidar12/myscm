package com.myScm.scm.serviceImpl;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.myScm.scm.Dto.ContactForm;
import com.myScm.scm.entities.Contact;
import com.myScm.scm.entities.User;
import com.myScm.scm.errorHandler.ContactNotFoundException;
import com.myScm.scm.errorHandler.ContactValidationException;
import com.myScm.scm.errorHandler.ResourceNotFoundException;
import com.myScm.scm.errorHandler.UnExpectedException;
import com.myScm.scm.helper.AppConstants;
import com.myScm.scm.helper.Helper;
import com.myScm.scm.repos.ContactRepo;
import com.myScm.scm.services.contactServices;

@Service
public class ContactServiceImpl implements contactServices {

    private final ContactRepo contactRepo;
    private Contact contact;
    private final Helper helper;
    private List<Contact> contacts;
    private final CloudnairyServiceImpl cloudinaryService;
    private Pageable pageable;
    private Page<Contact> pageContacts;
    private Page<ContactForm> contactformPages;

    Sort sort = Sort.by("name").ascending();

    public ContactServiceImpl(ContactRepo contactRepo, Helper helper,
            CloudnairyServiceImpl cloudinaryService) {
        this.contactRepo = contactRepo;
        this.helper = helper;
        this.cloudinaryService = cloudinaryService;
    }

    // get Number of favorite contacts

    @Override
    public Page<ContactForm> getContactPages(int page, User user) {
        try {

            this.pageable = PageRequest.of(page, AppConstants.PAGEBLE_PAGE_SIZE, sort);
            this.pageContacts = this.contactRepo.findContactsByUser(user, pageable);

            this.contactformPages = this.helper.convertPageContactFrom(this.pageContacts);

            return this.contactformPages;
        } catch (Exception e) {
            throw new ContactNotFoundException(
                    "we are not fetching to the contact for this user so please try again after some time!!");
        }
    }

    @Override
    public int getFavoriteContacts(User user) {

        try {

            return this.contactRepo.countByFavoriteAndUser(true, user);
        } catch (ContactNotFoundException e) {
            throw new ContactNotFoundException(
                    "we can't find the contact for this user so please try again after some time!!");
        }
    }

    // get All favorite Contacts

    @Override
    public Page<ContactForm> getFavoriteContactsList(int page, User user) {

        try {

            this.pageable = PageRequest.of(page, AppConstants.PAGEBLE_PAGE_SIZE, sort);
            this.pageContacts = this.contactRepo.findByFavoriteAndUser(true, user, pageable);

            this.contactformPages = this.helper.convertPageContactFrom(this.pageContacts);

            return this.contactformPages;
        } catch (Exception e) {
            throw new ContactNotFoundException(
                    "We are not able to fatch the contact for you so please try again after some time!!");
        }

    }

    // Save Contact

    @Override
    public Contact saveContact(ContactForm contactForm, User user) {

        try {

            // convert contactForm to contact
            this.contact = this.helper.convertContact(contactForm);
            this.contact.setUser(user);

            // Save Image on cloudnary

            if (!contactForm.getImage().isEmpty()) {

                String public_id = UUID.randomUUID().toString();

                String imageUrl = this.cloudinaryService.uploadOnCloud(contactForm.getImage(), public_id);
                this.contact.setContactPic(imageUrl);
                this.contact.setImagePublicId(public_id);

            } else {

                this.contact.setContactPic("../../../img/contact.png");

            }

            // set default Social Links

            if (this.contact.getLinks().get(0).getInstagram() == null
                    && this.contact.getLinks().get(0).getInstagram().isEmpty()) {
                this.contact.getLinks().get(0).setInstagram("https://www.instagram.com/");
            }
            if (this.contact.getLinks().get(0).getLinkedIn() == null
                    && this.contact.getLinks().get(0).getLinkedIn().isEmpty()) {
                this.contact.getLinks().get(0).setLinkedIn("https://in.linkedin.com/");
            }

            // set the id of the contact
            contact.getLinks().get(0).setContact(contact);
            this.contact.setLinks(contact.getLinks());
            this.contact.setContactId(UUID.randomUUID().toString());

            return contactRepo.save(this.contact);
        } catch (Exception e) {

            throw new ContactValidationException(
                    "we are facing some erorr for add contact so please try again after some time!!");
        }
    }

    // Get Contact By Id

    @Override
    public Contact getContactByIdAndUser(String id, User user) {

        return contactRepo.findById(id).orElseThrow(() -> new ContactNotFoundException(
                "we can't be fatch to the contact detail so please try again after some time!!"));

    }

    // Delete Contact By Object

    @Override
    public boolean deleteContact(Contact contact, User user) {

        try {

            if (contact != null && this.contactIsExist(contact, user)) {
                user.getContactList().remove(contact);
                if (contact.getImagePublicId() != null) {

                    this.cloudinaryService.deleteImage(contact.getImagePublicId());
                }
                this.contactRepo.delete(contact);
                return true;
            }

            return false;
        } catch (ContactNotFoundException e) {

            throw new ContactNotFoundException(
                    "we can't be able to delete the contact this time so please try again after some time!!");
        }
    }

    // Delete Contact By Id

    @Override
    public void deleteContactById(String contactId, User user) {

        try {

            this.contact = this.getContactByIdAndUser(contactId, user);

            if (this.contact != null) {
                // remove contact from user ContactList
                user.getContactList().remove(this.contact);

                if (contact.getImagePublicId() != null) {

                    this.cloudinaryService.deleteImage(contact.getImagePublicId());
                }

                this.contactRepo.deleteById(contactId);
            }

        } catch (ContactNotFoundException e) {
            throw new ContactNotFoundException(
                    "we can't be able to delete the contact this time so please try again after some time!!");

        }
    }

    // Make Favorite By Id

    @Override
    public Contact makeFavoriteById(String id, User user) {

        try {

            this.contact = this.contactRepo.findById(id).get();

            if (this.contact != null && this.contactIsExist(contact, user)) {
                if (this.contact.isFavorite()) {
                    this.contact.setFavorite(false);
                } else {
                    this.contact.setFavorite(true);
                }
                return this.contactRepo.save(this.contact);
            }

            return null;
        } catch (ContactNotFoundException e) {
            throw new ContactNotFoundException(
                    "we can't be able to favorited this contact at this time , so you can try again after some time!!");
        }
    }

    // Get All Contacts

    @Override
    public List<ContactForm> getAllContacts() {

        try {

            this.contacts = this.contactRepo.findAll();

            List<ContactForm> contactforms = this.helper.convertAllContacts(contacts);

            return contactforms;
        } catch (ContactNotFoundException e) {
            throw new ContactNotFoundException(
                    "we can't be able to the detail for all contacts so try again after some time!!");
        }
    }

    // Make Favorite By Object

    @Override
    public Contact makeFavorite(Contact contact, User user) {

        throw new UnsupportedOperationException("Unimplemented method 'makeFavorite'");
    }

    // Check the contact is exist in User

    @Override
    public boolean contactIsExist(Contact contact, User user) {

        try {

            List<Contact> contacts = user.getContactList();
            if (contacts.contains(contact)) {
                return true;
            }
            return false;

        } catch (ContactNotFoundException e) {
            throw new ContactNotFoundException(
                    "the contact is not specific for you so do not try to access the detail for this contact!!");
        }

    }

    // getContactForm By Contact

    @Override
    public ContactForm getContactFormByContact(Contact contact) {

        try {

            return this.helper.convertContactForm(contact);
        } catch (UnExpectedException e) {
            throw new UnExpectedException("INTERNAL_SERVER_ERROR try again after some time!!");
        }
    }

    // Update Contact detail

    @Override
    public Contact updateContact(ContactForm form, User user) {

        try {

            // Update The Image
            this.contact = this.helper.convertContact(form);

            // Set Image

            if (form.getImage().getSize() > 1) {

                String public_id = UUID.randomUUID().toString();
                String imageUrl = this.cloudinaryService.uploadOnCloud(form.getImage(), public_id);

                if (contact.getImagePublicId() != null && !contact.getImagePublicId().isEmpty()) {
                    this.cloudinaryService.deleteImage(contact.getImagePublicId());
                }

                contact.setContactPic(imageUrl);
                contact.setImagePublicId(public_id);
            } else {
                if (contact.getContactPic().isEmpty()) {
                    contact.setContactPic("../../img/contact.png");
                }
            }

            // set default Social Links

            if (this.contact.getLinks().get(0).getInstagram().isEmpty()) {
                this.contact.getLinks().get(0).setInstagram("https://www.instagram.com/");
            }
            if (this.contact.getLinks().get(0).getLinkedIn().isEmpty()) {
                this.contact.getLinks().get(0).setLinkedIn("https://in.linkedin.com/");
            }

            contact.getLinks().get(0).setContact(this.contact);
            contact.setLinks(this.contact.getLinks());

            contact.setUser(user);
            return this.contactRepo.save(this.contact);

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(
                    "we can't be able to update the detail fot this contact so try again after some time!!");
        }
    }

    @Override
    public Contact getcontactByid(String id) {

        return this.contactRepo.findById(id).orElseThrow(() -> new ContactNotFoundException(
                "we can't be fetch the contact detail for this user so please try again after some time!!"));

    }

    @Override
    public List<ContactForm> getcontactfrombyId(List<String> ids) {

        try {

            List<ContactForm> contactForms = new ArrayList<>();

            for (String id : ids) {

                Contact contact = this.getcontactByid(id);
                ContactForm contactForm = this.getContactFormByContact(contact);

                contactForms.add(contactForm);

            }

            return contactForms;
        } catch (ContactNotFoundException e) {
            throw new ContactNotFoundException(
                    "we are not able to fatching the detail for this user so please try again after some time!!");
        }

    }

    // delete the list of contacts
    @Override
    public void deleteContactsbyIds(User user, List<String> contactlist) {

        try {

            for (String id : contactlist) {

                this.deleteContactById(id, user);
            }

        } catch (ContactNotFoundException e) {
            throw new ContactNotFoundException("we are not able to found the contact so please try again after some time!!");
        }

    }

    // Make list of contact to favorite

    @Override
    public void makeFavoriteByIds(User user, List<String> contactlist) {

        try {

            for (String id : contactlist) {

                this.contact = this.getcontactByid(id);

                if (this.contact != null && this.contactIsExist(contact, user)) {
                    if (this.contact.isFavorite()) {
                        this.contact.setFavorite(false);
                    } else {
                        this.contact.setFavorite(true);
                    }
                    this.contactRepo.save(this.contact);
                }
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException(
                    "we are not able to find the detail for the contacts so please try again after some time!!");
        }

    }
}
