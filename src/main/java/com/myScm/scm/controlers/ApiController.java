package com.myScm.scm.controlers;

import org.springframework.web.bind.annotation.RestController;

import com.myScm.scm.Dto.ContactForm;
import com.myScm.scm.entities.Contact;
import com.myScm.scm.entities.User;
import com.myScm.scm.helper.Helper;
import com.myScm.scm.serviceImpl.ContactServiceImpl;
import com.myScm.scm.serviceImpl.ExportServiceImpl;
import com.myScm.scm.serviceImpl.UserServiceImple;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ContactServiceImpl contactServiceImpl;
    private final Helper helper;
    private final UserServiceImple userServiceImple;
    private final ExportServiceImpl exportServiceImpl;
    private User user;

    public ApiController(ContactServiceImpl contactServiceImpl, Helper helper, UserServiceImple userServiceImple,
            ExportServiceImpl exportServiceImpl) {
        this.contactServiceImpl = contactServiceImpl;
        this.userServiceImple = userServiceImple;
        this.helper = helper;
        this.exportServiceImpl = exportServiceImpl;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/contact-info/{contact}/{user}")
    public ContactForm getcontactData(@PathVariable("contact") String id, @PathVariable("user") String userid) {

        this.user = this.userServiceImple.getUserById(userid);

        Contact contact = this.contactServiceImpl.getContactByIdAndUser(id, this.user);

        ContactForm form = helper.convertContactForm(contact);

        return form;
    }

    // Add contacts in the fsvorite List
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/makefavorite")
    public ResponseEntity<String> makeFavoriteList(@RequestBody Map<String, Object> contactList) {

        try {

            this.user = this.userServiceImple.getUserById(contactList.get("userId").toString());
            @SuppressWarnings("unchecked")
            ArrayList<String> arr = (ArrayList<String>) contactList.get("contacts");

            System.out.println(arr);
            this.contactServiceImpl.makeFavoriteByIds(this.user, arr);

            return ResponseEntity.ok().body("Items added to favorites successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error");
        }

    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/deleteContacts")
    public ResponseEntity<String> deleteContacts(@RequestBody Map<String, Object> contactList) {

        try {

            this.user = this.userServiceImple.getUserById(contactList.get("userId").toString());
            @SuppressWarnings("unchecked")
            ArrayList<String> arr = (ArrayList<String>) contactList.get("contacts");

            this.contactServiceImpl.deleteContactsbyIds(user, arr);

            return ResponseEntity.ok().body("contact Deleted Successfully ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error");
        }

    }

    // Generating pdf file

    @SuppressWarnings("null")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/generate-pdf")
    public ResponseEntity<InputStreamResource> getContacts(@RequestBody List<String> contactIds) {

        ByteArrayInputStream bis = null;
        try {
            List<ContactForm> contacts = this.contactServiceImpl.getcontactfrombyId(contactIds);

            bis = this.exportServiceImpl.generatePdf(contacts);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=contacts.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InputStreamResource(bis));
        }
    }

    @SuppressWarnings("null")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/generate-excel")
    public ResponseEntity<InputStreamResource> generateExcel(@RequestBody List<String> contactIds) {

        ByteArrayInputStream bis = null;
        try {

            List<ContactForm> contacts = this.contactServiceImpl.getcontactfrombyId(contactIds);

            bis = this.exportServiceImpl.generateExcel(contacts);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=contacts.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(bis));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InputStreamResource(bis));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/deleteSingle")
    public ResponseEntity<String> deleteSingleContacts(@RequestParam(name = "contact") String id,
            Authentication authentication) {

        try {
            this.user = this.userServiceImple.getUserFromLogin(authentication);

            this.contactServiceImpl.deleteContactById(id, user);

            return ResponseEntity.status(HttpStatus.OK).body("Successfull Deleted!!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("INTERNAL_SERVER_ERROR");
        }

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/favoriteSingle")
    public ResponseEntity<String> makeSingleFavorite(@RequestParam(name = "contact") String id,
            Authentication authentication) {

        try {
            this.user = this.userServiceImple.getUserFromLogin(authentication);

            this.contactServiceImpl.makeFavoriteById(id, this.user);

            return ResponseEntity.status(HttpStatus.OK).body("Successfull favorited!!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("INTERNAL_SERVER_ERROR");
        }

    }


}
