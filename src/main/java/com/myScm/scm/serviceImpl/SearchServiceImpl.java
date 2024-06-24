package com.myScm.scm.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.myScm.scm.Dto.ContactForm;
import com.myScm.scm.Dto.SearchContactForm;
import com.myScm.scm.entities.Contact;
import com.myScm.scm.entities.User;
import com.myScm.scm.errorHandler.ContactNotFoundException;
import com.myScm.scm.helper.Helper;
import com.myScm.scm.repos.ContactRepo;
import com.myScm.scm.services.contactSearchService;

@Service
public class SearchServiceImpl implements contactSearchService {

    private final ContactRepo contactRepo;
    private final Helper helper;

    public SearchServiceImpl(ContactRepo contactRepo, Helper helper) {
        this.contactRepo = contactRepo;
        this.helper = helper;
    }

    private Page<Contact> pages = null;
    private Page<ContactForm> contactPages = null;
    private Sort sort = Sort.by("name").ascending();
    private final Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);

    @Override
    public Page<Contact> searchByName(String name, User user, String fromRes) {

        if (fromRes.equalsIgnoreCase("fev")) {
            this.pages = this.contactRepo.findByUserAndFavoriteAndNameContaining(user, true, name, pageable);
            return this.pages;
        }

        this.pages = this.contactRepo.findByUserAndNameContaining(user, name, pageable);

        return this.pages;
    }

    @Override
    public Page<Contact> searchByEmail(String email, User user, String fromRes) {

        if (fromRes.equalsIgnoreCase("fev")) {
            this.pages = this.contactRepo.findByUserAndFavoriteAndContactEmailContaining(user, true, email, pageable);
            return this.pages;
        }

        this.pages = this.contactRepo.findByUserAndContactEmailContaining(user, email, pageable);

        return this.pages;
    }

    @Override
    public Page<Contact> searchByPhone(String phone, User user, String fromRes) {

        if (fromRes.equalsIgnoreCase("fev")) {
            this.pages = this.contactRepo.findByUserAndFavoriteAndContactNumberContaining(user, true, phone, pageable);
            return this.pages;
        }

        this.pages = this.contactRepo.findByUserAndContactNumberContaining(user, phone, pageable);

        return this.pages;
    }

    public Page<ContactForm> globalSearchMethod(SearchContactForm contactForm, User user, String formRes) {

        try {

            this.pages = null;

            if (contactForm.getSearchField().equalsIgnoreCase("byName")) {

                // Search field is By Name
                this.pages = this.searchByName(contactForm.getFieldValue(), user, formRes);

            } else if (contactForm.getSearchField().equalsIgnoreCase("byEmail")) {

                // Search field is By Email
                this.pages = this.searchByEmail(contactForm.getFieldValue(), user, formRes);

            } else if (contactForm.getSearchField().equalsIgnoreCase("byNumber")) {

                // Search field is By Number
                this.pages = this.searchByPhone(contactForm.getFieldValue(), user, formRes);
            }

            // Convertion of contactPage to ContactForm Pages
            this.contactPages = this.helper.convertPageContactFrom(this.pages);

            return this.contactPages;
        } catch (Exception e) {
            throw new ContactNotFoundException(
                    "we are not able to find the contact so please try again after some time!!");
        }
    }

}
